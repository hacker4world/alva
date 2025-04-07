package com.example.alvaBackend.Security;

import com.example.alvaBackend.Entities.Admin;
import com.example.alvaBackend.Entities.Employee;
import com.example.alvaBackend.Entities.Manager;
import com.example.alvaBackend.Entities.User;
import com.example.alvaBackend.Repositories.userRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private userRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User with matricule number " + email + " not found"));

        System.out.println("existed");


        String userRole = "";

        if (user instanceof Manager) {
            userRole = "ROLE_MANAGER";
        } else if (user instanceof Employee) {
            userRole = "ROLE_EMPLOYEE";
        } else if (user instanceof Admin) {
            userRole = "ROLE_ADMIN";
        } else {
            userRole = "ROLE_WORKER";
        }

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                List.of(new SimpleGrantedAuthority(userRole))
        );
    }
}
