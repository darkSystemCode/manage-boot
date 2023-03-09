package com.hjx.blog_backstage.Services;

import com.hjx.blog_backstage.Entitys.Perms;
import com.hjx.blog_backstage.Entitys.Roles;
import com.hjx.blog_backstage.Entitys.User;
import com.hjx.blog_backstage.Mappers.UserMapper;
import com.hjx.blog_backstage.Utils.MailUtil;
import com.hjx.blog_backstage.Utils.ResultUtil;
import com.hjx.blog_backstage.Utils.UUIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

@Slf4j
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String email;

    private HttpSession session = null;


    public User getUserID(String userID) {
        return userMapper.getUserID(userID);
    }

    /**
     * 获取角色账号 page作为分页标记 ==1时，赋值为0标记从下表为0开始检索记录
     * 其他情况为当前页展示记录数*当前页码-1 公式为：(page - 1) * pageSize
     * @param page 当前页码
     * @param pageSize 当前页展示记录数
     * @return
     *  user
     *  count
     *  userCurrPage
     */
    @Transactional
    public Map<String, Object> getUser(Integer page, Integer pageSize) {
        if(page == 1) {
            page = 0;
        } else {
            page = (page - 1) * pageSize;
        }
        //得到全部用户账号数据
        List<User> allUser = userMapper.getUser(page, pageSize);
        //得到用户账号总数
        Integer dataCount = userMapper.getUserTotal();
        HashMap<String, Object> map = new HashMap<>();
        map.put("user", allUser);
        map.put("count", dataCount);
        map.put("userCurrPage", (page / pageSize) + 1);
        return map;
    }

    @Transactional
    public Map<String, Object> delUser(String user_id, Integer page, Integer pageSize) {
        //删除用户账号
        Integer integer = userMapper.delUser(user_id);
        if(integer > 0) {
            Map<String, Object> user = getUser(page, pageSize);
            return user;
        }
        return null;
    }

    /*
     * 发送验证码到验证邮箱中， 并存入session（2分钟有效）
     * */
    public Integer setCheckCode(String toEmail, HttpServletRequest request) {
        String checkCode = UUIDUtil.getCheckCode();
        session = request.getSession(true);
        session.setAttribute("code", checkCode);
        session.setMaxInactiveInterval(60 * 2); //2分钟失效
        SimpleMailMessage message = MailUtil.sendCheckCode(email, toEmail, checkCode);
        try {
            javaMailSender.send(message);
            return 200;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /*
     * 前台发送请求校验验证码是否正确，正确返回200
     * */
    public Integer getCheckCode(String code, HttpServletRequest request) {
        String checkCode = (String) session.getAttribute("code");
        if (code.equalsIgnoreCase(checkCode)) {
            //校验通过,清空session缓存
            session.setAttribute("code", "");
            return 200;
        }
        return 0;
    }

    public Integer registerUser(User user) {
        Calendar date = Calendar.getInstance();
        user.setUserID(UUIDUtil.get_64UUID());
        user.setUsername(user.getUsername());
        user.setPassword(user.getPassword());
        user.setEmail(user.getEmail());
        user.setCreateTime(date.getTimeInMillis());
        user.setState(1);
        return userMapper.registerUser(user);
    }

    public User toLogin(User user) {
        return userMapper.toLogin(user);
    }

    public Integer checkOldPassW(String oldPassword) {
        String checkRes = userMapper.checkOldPassW(oldPassword);
        if(checkRes == null || checkRes == "") {
            return 201;
        }
        return 0;
    }

    public Integer updatePassword(String newPassword, String username) {
        return userMapper.updatePassword(newPassword, username);
    }

    @Transactional
    public Map<String, Object> editUserInfo(User user, Integer page, Integer pageSize) {
        Integer integer = userMapper.editUser(user);
        if (integer > 0) {
            Map<String, Object> userMap = getUser(page, pageSize);
            if (userMap.size() > 0) {
                return userMap;
            }
        }
        return null;
    }

    /**
     * 添加新的权限 并在添加后以分页形式展示记录 会通过page来判断当前添加的记录归属于那个页码
     * 并使用这个页码查询记录返回
     * @param perms 权限实体类
     * @param page 当前页码
     * @param pageSize 当前页展示记录数
     * @return
     *  map:
     *      perms
     *      permsTotal
     *      permsCurrPage
     */
    @Transactional
    public Map<String, Object> setUserPerms(Perms perms, Integer page, Integer pageSize) {
        HashMap<String, Object> map = new HashMap<>();
        Integer integer = userMapper.setUserPerms(perms);
        if(integer > 0) {
            //得到目前数据库中存在的记录数
            Integer permsTotal = userMapper.getPermsTotal();
            /*
             *如果记录数取余不为0 当前插入的记录不为边界值 则当前页码为记录数除以pageSize取商 + 1
             *按数学除法 商为当前记录前还存在的分页组数 余数为当前记录数在当前页码组中的下标位置
             * 例如： 8 / 3 =2 余数2
             * 如果为0 则取商数为当前页码数
             */
            if(permsTotal % pageSize != 0) {
                //preGroup 当前插入位置前面存在的分组数
                Integer preGroup = permsTotal / pageSize;
                page = preGroup +1;
            } else {
                page = permsTotal / pageSize;
            }
            Map<String, Object> perms1 = getPerms(page, pageSize);
            return perms1;
        }
        return map;
    }

    /**
     * 设置新的角色 使用page判断新添加的角色权限归属于那个页码 并使用页码查询返回记录
     * @param roles
     * @param page
     * @param pageSize
     * @return
     *  roles
     *  rolesTotal
     *  rolesCurrPage
     */
    @Transactional
    public Map<String, Object> setUserRoles(Roles roles, Integer page, Integer pageSize) {
        HashMap<String, Object> map = new HashMap<>();
        Integer integer = userMapper.setUserRoles(roles);
        if(integer > 0) {
            Integer rolesTotal = userMapper.getRolesTotal();
            if(rolesTotal % pageSize != 0) {
                page = (rolesTotal / pageSize) + 1;
            } else {
                page = rolesTotal / pageSize;
            }
            Map<String, Object> roles1 = getRoles(page, pageSize);
            return roles1;
        }
        return map;
    }

    @Transactional
    public List<Object> getPermsAndRoles() {
        LinkedList<Object> list = new LinkedList<>();
        List<Perms> perms = null;
        List<Roles> roles = null;
        try {
            perms = userMapper.getPerms();
            roles = userMapper.getRoles();
            if(!StringUtils.isEmpty(perms) && !StringUtils.isEmpty(roles)) {
                list.add(perms);
                list.add(roles);
                return list;
            }
        } catch (Exception e) {
            System.out.println(e);
            log.info("错误异常{}", e.getMessage());
            throw new RuntimeException("数据库查询异常！");
        }
        return null;
    }

    @Transactional
    public Map<String, Object> addAccount(User user, Integer page, Integer pageSize) {
        Calendar calendar = Calendar.getInstance();
        user.setUserID(UUIDUtil.get_64UUID());
        user.setCreateTime(calendar.getTimeInMillis());
        user.setState(1);
        //添加用户账号
        Integer integer = userMapper.addAccount(user);
        if(integer > 0) {
            Integer accountTotal = userMapper.getUserTotal();
            if(accountTotal % pageSize != 0) {
                page = (accountTotal / pageSize) + 1;
            } else {
                page = accountTotal / pageSize;
            }
            Map<String, Object> userMap = getUser(page, pageSize);
            return userMap;
        }
        return null;
    }

    public User checkEmail(String email) {
        return userMapper.checkEmail(email);
    }

    @Transactional
    public Map<String, Object> getPerms(Integer page, Integer pageSize) {
        /*
         * 页码为1 置换为0 否则 （页码 -1） * pageSize 得到limit的start下标
         */
        if(page == 1) {
            page = 0;
        } else {
            page = (page - 1) * pageSize;
        }
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        List<Perms> allPerms = userMapper.getAllPerms(page, pageSize);
        Integer permsTotal = userMapper.getPermsTotal();
        if(allPerms.size() > 0) {
            map.put("perms", allPerms);
            map.put("permsSize", permsTotal);
            map.put("permsCurrPage", (page / pageSize) + 1);
            return map;
        }
        return null;
    }

    @Transactional
    public Map<String, Object> getRoles(Integer page, Integer pageSize) {
        if(page == 1) {
            page = 0;
        } else {
            page = (page - 1) * pageSize;
        }
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        List<Roles> allRoles = userMapper.getAllRoles(page, pageSize);
        Integer rolesTotal = userMapper.getRolesTotal();
        if(allRoles.size() > 0) {
            map.put("roles", allRoles);
            map.put("rolesSize", rolesTotal);
            map.put("rolesCurrPage", (page / pageSize) + 1);
            return map;
        }
        return null;
    }

    @Transactional
    public Map<String, Object> delPerms(String permsName, Integer page, Integer pageSize) {
        //如果返回1 则删除成功
        Integer integer = userMapper.delPerms(permsName);
        if(integer > 0) {
            /*
             * 同一事务删除 算法和添加一样 先取余 不为0 则取商+1 否则取商
             */
            Integer permsTotal = userMapper.getPermsTotal();
            if(permsTotal % pageSize != 0) {
                page = (permsTotal / pageSize) + 1;
            } else {
                page = permsTotal / pageSize;
            }
            Map<String, Object> perms1 = getPerms(page, pageSize);
            return perms1;
        }
        return null;
    }

    @Transactional
    public Map<String, Object> delRoles(String rolesName, Integer page, Integer pageSize) {
        Integer integer = userMapper.delRoles(rolesName);
        if(integer > 0) {
            Integer rolesTotal = userMapper.getRolesTotal();
            if(rolesTotal % pageSize != 0) {
                page = (rolesTotal / pageSize) + 1;
            } else {
                page = rolesTotal / pageSize;
            }
            Map<String, Object> roles = getRoles(page, pageSize);
            return roles;
        }
        return null;
    }
}
