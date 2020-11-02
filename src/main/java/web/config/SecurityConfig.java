package web.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import web.config.handler.LoginSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());  // конфигурация для прохождения аутентификации
    }

    /*@Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        authenticationProvider.setUserDetailsService(userDetailsService);
        return authenticationProvider;
    }*/

    @Override
    protected UserDetailsService userDetailsService() {
        return userDetailsService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()   // делаем страницу регистрации недоступной для авторизированных пользователей
                //страницы аутентификаци доступна всем
                .antMatchers("/", "/login").permitAll()/*anonymous()*/
                // защищенные URL
                .antMatchers("/admin/**").hasAnyRole("ROLE_ADMIN")
                .antMatchers("/id").access("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
                .antMatchers("/hello").hasAnyRole("ROLE_ADMIN").anyRequest().authenticated()
                .and()
                .formLogin()
                // указываем страницу с формой логина
                    .loginPage("/login")
                //указываем логику обработки при логине
                    .successHandler(authenticationSuccessHandler())
                // указываем action с формы логина
                /// .loginProcessingUrl("/login")
                // Указываем параметры логина и пароля с формы логина
                /*.usernameParameter("j_username")
                .passwordParameter("j_password")*/
                // даем доступ к форме логина всем
                    .permitAll()
                .and()
                .logout()
                    .invalidateHttpSession(true)
                    .clearAuthentication(true)
                // указываем URL логаута
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                // указываем URL при удачном логауте
                    .logoutSuccessUrl("/login?logout")
                // разрешаем делать логаут всем
                    .permitAll()
                //выклчаем кроссдоменную секьюрность (на этапе обучения неважна)
                    .and().csrf().disable();
        http.userDetailsService(userDetailsService());

    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                .antMatchers("/resources/**");
    }

    @Bean
    protected PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new LoginSuccessHandler();
    }
}
