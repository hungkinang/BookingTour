package edu.bookingtour.config;

import edu.bookingtour.service.CustomOAuth2UserService;
import edu.bookingtour.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.session.InvalidSessionStrategy;
import org.springframework.security.web.util.matcher.AndRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

        private final CustomUserDetailsService userDetailsService;

        public SecurityConfig(CustomUserDetailsService userDetailsService) {
                this.userDetailsService = userDetailsService;
        }

        @Bean
        public SessionRegistry sessionRegistry() {
                return new SessionRegistryImpl();
        }

        @Bean
        public HttpSessionRequestCache httpSessionRequestCache() {
                RequestMatcher staticResources = new OrRequestMatcher(
                                new AntPathRequestMatcher("/uploads/**"),
                                new AntPathRequestMatcher("/css/**"),
                                new AntPathRequestMatcher("/js/**"),
                                new AntPathRequestMatcher("/anh/**"),
                                new AntPathRequestMatcher("/img/**"),
                                new AntPathRequestMatcher("/images/**"),
                                new AntPathRequestMatcher("/favicon.ico"),
                                new AntPathRequestMatcher("/favicon.icon"),
                                new AntPathRequestMatcher("/error"));

                HttpSessionRequestCache cache = new HttpSessionRequestCache();
                cache.setRequestMatcher(new AndRequestMatcher(
                                new NegatedRequestMatcher(staticResources),
                                new AntPathRequestMatcher("/**")));
                return cache;
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        @Bean
        public DaoAuthenticationProvider authenticationProvider() {
                DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
                authProvider.setUserDetailsService(userDetailsService);
                authProvider.setPasswordEncoder(passwordEncoder());
                return authProvider;
        }

        @Bean
        public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
                return config.getAuthenticationManager();
        }

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http, CustomOAuth2UserService customOAuth2UserService,
                        SessionRegistry sessionRegistry, InvalidSessionStrategy invalidSessionStrategy,
                        HttpSessionRequestCache httpSessionRequestCache)
                        throws Exception {
                http
                                .authenticationProvider(authenticationProvider())
                                .requestCache(cache -> cache.requestCache(httpSessionRequestCache))

                                .authorizeHttpRequests(authorize -> authorize
                                                .requestMatchers("/tour/*/dat-tour").authenticated()

                                                .requestMatchers(
                                                                "/login",
                                                                "/register",
                                                                "/error",
                                                                "/css/**",
                                                                "/js/**",
                                                                "/anh/**",
                                                                "/img/**",
                                                                "/images/**",
                                                                "/uploads/**",
                                                                "/",
                                                                "/tour/**",
                                                                "/api/public/**",
                                                                "/contact",
                                                                "/payment/**",
                                                                "/slogan.png",
                                                                "/favicon.ico",
                                                                "/favicon.icon")
                                                .permitAll()

                                                .requestMatchers("/admin/**").hasRole("ADMIN")

                                                .requestMatchers("/user/**", "/booking/**").authenticated()

                                                .anyRequest().authenticated())

                                .formLogin(form -> form
                                                .loginPage("/login")
                                                .loginProcessingUrl("/perform-login")
                                                .usernameParameter("username")
                                                .passwordParameter("password")
                                                .defaultSuccessUrl("/redirect-after-login", false)
                                                .failureUrl("/login?error=true")
                                                .permitAll())

                                .logout(logout -> logout
                                                .logoutUrl("/logout")
                                                .logoutSuccessUrl("/login?logout=true")
                                                .invalidateHttpSession(true)
                                                .clearAuthentication(true)
                                                .deleteCookies("JSESSIONID", "remember-me")
                                                .permitAll())

                                .exceptionHandling(exception -> exception
                                                .accessDeniedPage("/access-denied"))

                                .sessionManagement(session -> session
                                                .sessionFixation().changeSessionId()
                                                .invalidSessionStrategy(invalidSessionStrategy)
                                                .maximumSessions(1)
                                                .maxSessionsPreventsLogin(false)
                                                .sessionRegistry(sessionRegistry))

                                .oauth2Login(oauth -> oauth
                                                .loginPage("/login")
                                                .userInfoEndpoint(userInfo -> userInfo
                                                                .userService(customOAuth2UserService))
                                                .failureHandler((request, response, exception) -> {
                                                        request.getSession().setAttribute("error.message",
                                                                        exception.getMessage());
                                                        response.sendRedirect("/login?error=true");
                                                })
                                                .defaultSuccessUrl("/redirect-after-login", false));

                return http.build();
        }
}
