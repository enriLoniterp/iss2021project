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
            .antMatchers( "/home", "/reqenter", "/deposit", "/carenter", "/reqexit", "/pickup", "/resources/**", "/static/**", "/css/**", "/js/**", "/img/**", "/vendor/**", "/fonts/**").permitAll()
            .and()
            .authorizeRequests().antMatchers("/manager/performlogin", "/manager/performlogout").authenticated().and()
            .formLogin()
            .loginPage("/login")
            .defaultSuccessUrl("/manager/performlogin", true)
            .permitAll()
            .and()
            .logout().invalidateHttpSession(true)
            .permitAll()
    }

    @Bean
    override fun userDetailsServiceBean(): UserDetailsService? {
        val user = User.withDefaultPasswordEncoder()
            .username("manager")
            .password("password")
            .roles("MANAGER")
            .build()
        return InMemoryUserDetailsManager(user)
    }
 }
