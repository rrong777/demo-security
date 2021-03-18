package com.rrong777.web.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

// 这个类肯定要作为容器管理的bean存在。
@Component
public class MyUserDetailsService implements UserDetailsService {

    private Logger logger = LoggerFactory.getLogger(getClass());
    // 已经在Security配置文件中注入过了。
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("用户名：" + username);
        // SpringSecurity提供的User对象，这个对象实现了UserDetails接口
        // 三个参数 用户名 - 密码（数据库中存的） - 权限集合。最后这个参数告诉Security我当前登录的用户拥有哪些权限，然后Security用户会根据我们在WebSecurityConfig里面配置的东西
        // 去校验我们用户的权限，
        // AuthorityUtils.commaSeparatedStringToAuthorityList 这个方法是把逗号隔开的字符串，转换成一个Security需要的GrantedAuthorities集合的。
        // 假装下面这个对象是从数据库取得。
        // 用户名你随意传一个进来，密码123456即可。

        // 注入PasswordEncoder之后，这里的密码仍然是明文的，显然不行，这里返回的是加密过后的。 不然登录的时候输入正确的密码也无法登录，会显示坏的凭证
//        return new User(username,"123456", AuthorityUtils.commaSeparatedStringToAuthorityList("admin"));
        // 将密码加密之后返回，就可以登录了
        String password = passwordEncoder.encode("123456");
        logger.info("假装从数据库查密码：" + password);
        return new User(username,password, AuthorityUtils.commaSeparatedStringToAuthorityList("admin,ROLE_USER"));
        // 四个布尔值分别表示账户可用。是否过期，密码没过期，账号是否被冻结
//        return new User(username,"123456",
//                true,true,true,false,
//                AuthorityUtils.commaSeparatedStringToAuthorityList("admin"));
    }
}
