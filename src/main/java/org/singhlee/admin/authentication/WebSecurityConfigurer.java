package org.singhlee.admin.authentication;


import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.singhlee.admin.authentication.detail.CustomUserDetailsService;
import org.singhlee.admin.authentication.handler.*;
import org.singhlee.admin.common.utils.Constant;
import org.singhlee.admin.common.utils.JwtTokenUtil;
import org.singhlee.admin.common.utils.RedisUtil;
import org.singhlee.admin.config.AuthIgnoreConfig;
import org.singhlee.admin.interceptor.AuthenticationTokenFilter;
import org.singhlee.admin.authentication.handler.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import java.util.Arrays;
import java.util.List;

/**
 * @Description
 * @date 2020-06-16 16:05
 * @Author singhlee
 **/
@Slf4j
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {
    @Value("${jwt.header}")
    private String tokenHeader;
    @Value("${jwt.ignored}")
    private String ignored;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    @Autowired
    private AuthIgnoreConfig authIgnoreConfig;


    @SneakyThrows
    @Override
    protected void configure(HttpSecurity http) {
        List<String> permitAll = authIgnoreConfig.getIgnoreUrls();
        String[] urls = permitAll.stream().distinct().toArray(String[]::new);
        String[] ignoreds = ignored.split(",");
        int str1Length = urls.length;
        int str2length = ignoreds.length;
        urls = Arrays.copyOf(urls, str1Length+str2length);
        System.arraycopy(ignoreds, 0, urls, str1Length, str2length);
        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry = http.authorizeRequests();
        registry.antMatchers(urls).permitAll().anyRequest().authenticated().and().csrf().disable();
        http.cors();

                // 基于token，所以不需要session
        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .headers().frameOptions().disable()
                .and()
                .formLogin()
                .loginProcessingUrl(Constant.TOKEN_ENTRY_POINT_URL)
                .successHandler(authenticationSuccessHandler())
                .failureHandler(authenticationFailureHandler())
                .and()
                .logout()
                .logoutUrl(Constant.TOKEN_LOGOUT_URL)
                .addLogoutHandler(logoutHandler())
                .permitAll()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(customAuthenticationEntryPoint())
                .and()
                .addFilterBefore(new AuthenticationTokenFilter(jwtTokenUtil, tokenHeader, customUserDetailsService, redisUtil), UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService)
                .passwordEncoder(passwordEncoder())
                .and()
                .authenticationProvider(authenticationProvider());
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new CustomAuthenticationFailHandler();
    }

    @Bean
    public LogoutHandler logoutHandler() {
        return new CustomLogoutSuccessHandler();
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new CustomAuthenticationSuccessHandler();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        return new CustomAuthenticationProvider();
    }

    @Bean
    public AuthenticationEntryPoint customAuthenticationEntryPoint() {
        return new CustomAuthenticationEntryPoint();
    }

    @Bean
    @Override
    @SneakyThrows
    public AuthenticationManager authenticationManagerBean() {
        return super.authenticationManagerBean();
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/js/**");
        web.ignoring().antMatchers("/css/**");
        web.ignoring().antMatchers("/health");
        web.ignoring().antMatchers( "/swagger-resources",  "/swagger-ui.html", "/webjars/**","/v2/api-docs/**");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
