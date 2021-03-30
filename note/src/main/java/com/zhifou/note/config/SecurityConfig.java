package com.zhifou.note.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhifou.note.bean.ResponseBean;
import com.zhifou.note.bean.Status;
import com.zhifou.note.user.filter.TokenFilter;
import com.zhifou.note.user.filter.ValidateCodeFilter;
import com.zhifou.note.user.handle.AdminAuthenticationFailureHandle;
import com.zhifou.note.user.handle.AdminAuthenticationSuccessHandle;
import com.zhifou.note.user.handle.NoteAuthenticationFailureHandle;
import com.zhifou.note.user.handle.NoteAuthenticationSuccessHandle;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.Resource;
import java.io.PrintWriter;

/**
 * @author : li
 * @Date: 2021-01-05 15:45
 */
@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Configuration
    @Order(1)
    static class AdminSecurityConfig extends WebSecurityConfigurerAdapter{
        @Resource
        private ValidateCodeFilter validateCodeFilter;
        @Resource
        private AdminAuthenticationFailureHandle adminAuthenticationFailureHandle;
        @Resource
        private AdminAuthenticationSuccessHandle adminAuthenticationSuccessHandle;
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .antMatcher("/admin/**")//多HttpSecurity配置时必须设置这个，除最后一个外，因为不设置的话默认匹配所有，就不会执行到下面的HttpSecurity了
                    .addFilterBefore(validateCodeFilter, UsernamePasswordAuthenticationFilter.class) // 添加验证码校验过滤器
                    .formLogin()
                    .loginPage("/admin/login")//登陆界面页面跳转URL
                    .loginProcessingUrl("/admin/login")//登陆界面发起登陆请求的URL
                    .successHandler(adminAuthenticationSuccessHandle)
                    .failureHandler(adminAuthenticationFailureHandle)
                    .permitAll()//表单登录，permitAll()表示这个不需要验证
                    .and()//Return the SecurityBuilder
                    .logout()
                    .logoutUrl("/logout")//登出请求地址
                    .and()
                    .authorizeRequests()//启用基于 HttpServletRequest 的访问限制，开始配置哪些URL需要被保护、哪些不需要被保护
                    .antMatchers("/static/**","/logout").permitAll()//未登陆用户允许的请求
                    .anyRequest().hasRole("ADMIN")//其他/admin路径下的请求全部需要登陆，获得ADMIN角色
                    .and()
                    .headers().frameOptions().sameOrigin()//设置X-Frame-Options同源可访问
                    .and()
                    .csrf().disable();
        }
    }

    @Configuration
    @Order(2)
    static class NoteSecurityConfig extends WebSecurityConfigurerAdapter{
        @Resource
        private ValidateCodeFilter validateCodeFilter;
        @Resource
        private NoteAuthenticationFailureHandle noteAuthenticationFailureHandle;
        @Resource
        private NoteAuthenticationSuccessHandle noteAuthenticationSuccessHandle;
        @Resource
        private TokenFilter tokenFilter;

        @Override
        public void configure(HttpSecurity http) throws Exception {
            http
//                    .antMatcher("/**")//多HttpSecurity配置时必须设置这个，除最后一个外，因为不设置的话默认匹配所有，就不会执行到下面的HttpSecurity了
                    .addFilterBefore(validateCodeFilter, UsernamePasswordAuthenticationFilter.class) // 添加验证码校验过滤器
                    .addFilterBefore(tokenFilter, UsernamePasswordAuthenticationFilter.class)//添加JWT身份认证的filter
                    .formLogin()
                    .loginProcessingUrl("/login")//登陆界面发起登陆请求的URL
                    .successHandler(noteAuthenticationSuccessHandle)
                    .failureHandler(noteAuthenticationFailureHandle)
                    .permitAll()//表单登录，permitAll()表示这个不需要验证
                    .and()
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                    .exceptionHandling().authenticationEntryPoint((request,response,authException) -> {
                        response.setContentType("application/json;charset=utf-8");
                        ResponseBean result = new ResponseBean();
                        result.setStatus(Status.USER_NOT_LOGIN);
                        result.setMessage(Status.USER_NOT_LOGIN.getMessage());
                        PrintWriter out = response.getWriter();
                        out.write(new ObjectMapper().writeValueAsString(result));
                    })
                    .and()//Return the SecurityBuilder
                    .logout()
                    .logoutUrl("/logout")//登出请求地址
                    .and()
                    .authorizeRequests()//启用基于 HttpServletRequest 的访问限制，开始配置哪些URL需要被保护、哪些不需要被保护
                    .antMatchers(HttpMethod.GET,"/note/**","/notes/**","/types","/tags").permitAll()
                    .antMatchers("/getImageCode","/getMailCode/**",
                            "/register/**","/static/**","/webjars/**","/swagger-resources/**",
                            "/webjars/**", "/v2/**", "/swagger-ui.html/**",
                            "/druid/**","/logout").permitAll()//未登陆用户允许的请求
                    .anyRequest().authenticated()//其他/路径下的请求全部需要登陆，获得USER角色
                    .and()
                    .headers().frameOptions().sameOrigin()//设置X-Frame-Options同源可访问
                    .and().csrf().disable().headers().cacheControl();
        }

    }




    @Bean
    public PasswordEncoder getPE(){
        return new BCryptPasswordEncoder();
    }


}
