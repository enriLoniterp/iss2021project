package  controller

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.provisioning.InMemoryUserDetailsManager


@Configuration
@EnableWebSecurity
class HelloWebSecurityConfiguration : WebSecurityConfigurerAdapter() {

    @kotlin.jvm.Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http
            .authorizeRequests()
            .antMatchers("/", "/home", "/reqenter", "/deposit", "/carenter", "/reqexit", "/pickup").permitAll()
            .anyRequest().authenticated()
            .and()
            .formLogin()
            .loginPage("/login")
            .defaultSuccessUrl("/manager/login", true)
            .permitAll()
            .and()
            .logout()
            .permitAll()
    }

    @Throws(java.lang.Exception::class)
    override fun configure(web: WebSecurity) {
        web.ignoring()
            .antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/images/**", "/vendor/**", "/fonts/**")
            .anyRequest()
    }

    @Bean
    override fun userDetailsServiceBean(): UserDetailsService? {
        val user = User.withDefaultPasswordEncoder()
            .username("user")
            .password("password")
            .roles("ADMIN")
            .build()
        return InMemoryUserDetailsManager(user)
    }
 }