package  controller

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.provisioning.InMemoryUserDetailsManager


@Configuration
@EnableWebSecurity
class HelloWebSecurityConfiguration : WebSecurityConfigurerAdapter() {

    @Bean
    override fun userDetailsService(): UserDetailsService? {
        val user: UserDetails = User.withDefaultPasswordEncoder()
            .username("antonionatali")
            .password("antonionatali")
            .roles("USER")
            .build()
        return InMemoryUserDetailsManager(user)
    }

     @Throws(Exception::class)
     override fun configure(http: HttpSecurity) {
         http
             .csrf().disable()
             .authorizeRequests()
             .antMatchers("/admin/**").hasRole("ADMIN")
             .antMatchers("/login*").permitAll()
             .anyRequest().authenticated()
             .and()
             .formLogin()
             .loginPage("/login.html")
             .loginProcessingUrl("/manager/perform_login")
             .defaultSuccessUrl("/homeMgmt.html", true)
             .failureUrl("/login.html?error=true")
             .and()
             .logout()
             .logoutUrl("/manager/logout")

     }
 }