package com.example.alvaBackend.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;


@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;


    @Autowired
    public SecurityConfig(CustomUserDetailsService customUserDetailsService,JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.customUserDetailsService = customUserDetailsService;
        this.jwtAuthenticationFilter=jwtAuthenticationFilter;

    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> {




                    auth.requestMatchers("/api/authentication/createUser","/api/authentication/login","/api/authentication/activateAccount",
                            "/api/userManagement/resetPassword/requestCode","/api/userManagement/resetPassword/verifyCode",
                          "/api/userManagement/{id}/image","/api/authentication/getByMatricule/{matricule}","/api/userManagement/resetPassword/updateOldPassword",
                            "/api/authentication/admin/approve/{token}","/api/authentication/user/activate/{token}","/api/userManagement/resetPassword/updatePassword",
                            "/api/userManagement/{id}/uploadImage","/api/userManagement/{id}/image","/posts/addPost","/posts/updatePost/{id}","/posts/archivePost/{id}",
                            "/posts/{id}/addAttachment","/posts/{id}/updateAttachment","/posts/getBy/{id}","/posts/delete/{id}","/posts/non-archived",
                            "/posts/byDepartment/{departmentId}","/api/userManagement/getUserbyId/{id}","/posts/user/{id}",
                            "/departmentManagement/addDepartment","/departmentManagement/updateDepartment/{id}","/departmentManagement/deleteDepartment/{id}",
                            "/departmentManagement/getDepartment/{id}","/departmentManagement/getAllDepartments","/api/authentication/deleteAccount","/api/comments/createComment","/api/comments/byUser/{userId}","/api/comments/post/{postId}/tree","/api/comments/update/{commentId}","/api/comments/delete/{commentId}"
                    ,"/api/comments/{commentId}/replies","/api/comments/reply").permitAll();


                    auth.requestMatchers("/api/authentication/sendActivationEmail","/api/authentication/deleteAccount","/api/authentication/inactiveAccount"
                            ,"/api/userManagement/rejectActivationRequest","/api/userManagement/createUserAccount","/api/authentication/inactiveAccount",
                            "/api/userManagement/unblockUser","/api/userManagement/getAllAccounts", "/api/userManagement/activateAccountAdmin",
                            "/api/userManagement/sendActivationEmailAdmin").hasRole("ADMIN");

                    //auth.requestMatchers("/api/userManagement/resetPassword/updateOldPassword").hasRole("WORKER");
                    //auth.requestMatchers("/api/userManagement/resetPassword/updateOldPassword").hasRole("EMPLOYEE");
                   //auth.requestMatchers("/api/userManagement/resetPassword/updateOldPassword").hasRole("MANAGER");
                    auth.anyRequest().authenticated();
                })
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();

    }


    // a bean to configure cors to allow http://localhost:4200 to make requests
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of("http://localhost:5173","http://localhost:51155")); // Specify allowed origins


        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*")); // Specify allowed headers
        configuration.setAllowCredentials(true); // Allow cookies or authorization headers
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        authenticationProvider.setUserDetailsService(customUserDetailsService);

        return authenticationProvider;
    }
}

