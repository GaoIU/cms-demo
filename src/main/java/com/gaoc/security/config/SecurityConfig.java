package com.gaoc.security.config;

import com.gaoc.core.properties.BaseProperties;
import com.gaoc.security.service.MyExpiredSessionStrategy;
import com.gaoc.security.service.MyUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

//	private final JwtAuthenticationFilter jwtAuthenticationFilter;

//	private final MyAccessDeniedHandler myAccessDeniedHandler;

//	private final MyAuthenticationEntryPoint myAuthenticationEntryPoint;

    private final BaseProperties baseProperties;

    private final MyUserDetailsService userDetailsService;

    private final MyExpiredSessionStrategy expiredSessionStrategy;

//    private final MyLogoutSuccessHandler logoutSuccessHandler;

    // 使用session，注释掉jwt
//	@Override
//	protected void configure(HttpSecurity http) throws Exception {
//		// 定义授权
//		http.csrf().disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
//				.authorizeRequests().antMatchers(HttpMethod.POST, "/signIn").permitAll().antMatchers(HttpMethod.OPTIONS)
//				.permitAll().anyRequest().authenticated();
//
//		// 禁用缓存
//		http.headers().cacheControl();
//
//		// 添加拦截器
//		http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
//
//		// 无权限或者未登录时返回
//		http.exceptionHandling().accessDeniedHandler(myAccessDeniedHandler)
//				.authenticationEntryPoint(myAuthenticationEntryPoint);
//	}

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();

        http.formLogin().loginPage("/").usernameParameter(baseProperties.getUsernameParameter())
                .passwordParameter(baseProperties.getPasswordParameter());

        http.headers().frameOptions().sameOrigin();

        http.authorizeRequests().antMatchers(HttpMethod.OPTIONS).permitAll()
                .antMatchers(HttpMethod.GET, "/", "/invalid", "/signOut").permitAll()
                .antMatchers(HttpMethod.POST, "/signIn").permitAll()
                .antMatchers("/home", "/index", "/info", "/password", "/403", "/404", "/500", "/file/upload").authenticated().anyRequest()
                .access("@myUserDetailsService.hasPermission(request, authentication)");

        http.exceptionHandling().accessDeniedPage("/403");

        http.sessionManagement().invalidSessionUrl("/invalid").maximumSessions(1).expiredSessionStrategy(expiredSessionStrategy);

        http.logout().logoutUrl("/signOut").logoutSuccessUrl("/").invalidateHttpSession(true).deleteCookies("JSESSIONID");
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(HttpMethod.GET, "/css/**", "/img/**", "/js/**", "/layui/**", "/viewUI-4.3.2/**", "/favicon.ico");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}
