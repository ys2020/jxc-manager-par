package com.lzj.admin.config.security;

import com.lzj.admin.filters.CaptchaCodeFilter;
import com.lzj.admin.pojo.User;
import com.lzj.admin.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.annotation.Resource;
import javax.sql.DataSource;

@SpringBootConfiguration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JxcAuthenticationSuccessHandler jxcAuthenticationSuccessHandler;
    @Autowired
    private JxcAuthenticationFailedHandler jxcAuthenticationFailedHandler;
    @Autowired
    private JxcLogoutSuccessHandler jxcLogoutSuccessHandler;
    @Resource
    private IUserService iUserService;
    @Resource
    private CaptchaCodeFilter captchaCodeFilter;
    @Resource
    private DataSource dataSource;
    @Override
    public void configure(WebSecurity web) throws Exception {
    web.ignoring().antMatchers("/css/**","/error/**","/images/**","/js/**","/lib/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //禁用crsf
        http.csrf().disable()
                .addFilterBefore(captchaCodeFilter, UsernamePasswordAuthenticationFilter.class)
                .headers().frameOptions().disable()
                .and()
                    .formLogin()
                    .usernameParameter("userName")
                    .passwordParameter("password")
                    .loginPage("/index")
                    .loginProcessingUrl("/login")
                    .successHandler(jxcAuthenticationSuccessHandler)
                    .failureHandler(jxcAuthenticationFailedHandler)
                .and()
                    .logout()
                    .logoutUrl("/signout")
                    .logoutSuccessHandler(jxcLogoutSuccessHandler)
                    .deleteCookies("JSESSIONID")
                .and()
                .rememberMe()
                .rememberMeParameter("rememberMe")
                //保存在浏览器端的cookie的名称，如果不设置默认也是remember-me
                .rememberMeCookieName("remember-me-cookie")
                //设置token的有效期，即多长时间内可以免除重复登录，单位是秒。
                .tokenValiditySeconds(7  * 24 * 60 * 60)
                //自定义
                .tokenRepository(persistentTokenRepository())
                .and()
                    .authorizeRequests().antMatchers("/login","/index","/image").permitAll()
                    .anyRequest().authenticated();

    }
    @Bean
    public UserDetailsService userDetailsService(){
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
                User userDetails= iUserService.findUserByUserName(s);
                return userDetails;
            }
        };
    }
    @Bean
    public PasswordEncoder encoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    public PersistentTokenRepository persistentTokenRepository(){
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
        return tokenRepository;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService()).passwordEncoder(encoder());
    }
}
