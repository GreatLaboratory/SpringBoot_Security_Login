package com.example.admin.config;

import com.example.admin.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    UserService userService;

    // Spring Security의 설정을 구현하는 메소드
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(
                        "/registration/**",
                        "/js/**",
                        "/css/**",
                        "/img/**",
                        "/webjars/**")
                    .permitAll()
                    .anyRequest().authenticated()
                    .and()
                // 위에 antMatchers를 필두로 된 부분은 /regis부터 /webjars까지 경로의 리소스들이나 url요청은 전부 인증이나 권한없이 다 접근을 허용하겠다는 뜻
                // .anyRequest().authenticated() 이건 위의 모두접근허용외에 나머지요청은 전부 인증된 사용자만 접근가능하게한다는 뜻

                .formLogin()
                    .loginPage("/login")
                    //.defaultSuccessUrl("/")  -> 이렇게 하면 The request was rejected because the URL contained a potentially malicious String ";" 에러뿜는데 이유가 뭘까...
                    .permitAll()
                    .and()
                // 위에 formLogin파트는 모두 로그인폼에 관련한 설정
                // 보통은 .loginProcessingUrl("/authenticate")이런식으로 login폼작성 후  인증과정을 보낼 processingurl이 필요한데
                // 여기선 그냥 /login으로 redirect한다. maincontroller 주석참고
                // .failureUrl("/login?error")은 디폴트로 로그인실패 시 가는 설정이기때문에 위 코드에선 생략했다.
                // .defaultSuccessUrl("/home") 보통 이런 식으로 login성공했을 때의 특정경로를 지정하고 만약 없으면 원래 요청하려던 url-getmapping으로 간다. (로그인아이디정보 보유하게끔 해야할 듯)
                // .usernameParameter("name")이나 .passwordParameter("password")으로 login가능한 특정 회원정보를 미리 설정할 수도 있음.

                .logout()
                    .invalidateHttpSession(true)
                    .clearAuthentication(true)
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                    .logoutSuccessUrl("/login?logout")
                    .permitAll();
                // .invalidateHttpSession(true) 이건 로그아웃했을 때 세션을 끊는 역할 - true가 디폴트값임.
                // .clearAuthentication(true) 위에랑 비슷하게 권한을 없애주는 역할
                // .logoutRequestMatcher(new AntPathRequestMatcher("/logout")) 이건 logoutUrl("/logout")과 같은 뜻으로 /logout로 url요청이 들어오면
                // 위에 있는 두 줄의 처리를 해주고 spring security가 로그아웃을 진행해서 성공했을 경우 아래의 /login?logout으로 보내는 역할
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    //커스텀 인증을 구현하는 클래스이다. AuthenticationProvider를 구현하는 클래스
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(userService);
        auth.setPasswordEncoder(passwordEncoder());
        return auth;
    }


    // 위에 있는 커스텀인증을 구현한 authenticationProvider()을 실제 권한매니저로 제공&빌드한다.
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }
}