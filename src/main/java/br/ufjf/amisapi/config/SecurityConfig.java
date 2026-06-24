package br.ufjf.amisapi.config;

import br.ufjf.amisapi.security.JwtAuthFilter;
import br.ufjf.amisapi.security.JwtService;
import br.ufjf.amisapi.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private JwtService jwtService;

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public OncePerRequestFilter jwtFilter(){
        return new JwtAuthFilter(jwtService, usuarioService);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(usuarioService)
                .passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors().disable()
                .csrf().disable()
                .authorizeRequests()

                .antMatchers("/api/v1/usuarios/**").permitAll()
                .antMatchers("/api/v1/escritorios/**").permitAll()


                .antMatchers("/api/v1/clientes/**").permitAll()
                .antMatchers("/api/v1/areas-processo/**").permitAll()
                .antMatchers("/api/v1/tipos-tarefa/**").permitAll()
                .antMatchers("/api/v1/disponibilidades/**").permitAll()

                // Exemplo de rotas que você pode querer restringir no futuro
                // .antMatchers("/api/v1/processos/**").hasAnyRole("USER", "ADMIN")
                // .antMatchers("/api/v1/faturas/**").hasRole("ADMIN")
                // .antMatchers("/api/v1/contratos/**").hasRole("ADMIN")
                .antMatchers("/api/v1/processos/**").permitAll()
                .antMatchers("/api/v1/tarefas/**").permitAll()
                .antMatchers("/api/v1/faturas/**").permitAll()
                .antMatchers("/api/v1/contratos/**").permitAll()

                .anyRequest().authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        // Ignora a segurança nas rotas do Swagger para a documentação continuar acessível
        web.ignoring().antMatchers(
                "/v2/api-docs",
                "/v3/api-docs",
                "/configuration/ui",
                "/swagger-resources/**",
                "/configuration/security",
                "/swagger-ui/**",
                "/swagger-ui.html",
                "/webjars/**");
    }
}