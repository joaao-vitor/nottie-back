package com.nottie.config;

import com.nottie.model.User;
import com.nottie.repository.UserRepository;
import com.nottie.security.AuditorAwareImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
public class AuditorConfig {
    private final UserRepository userRepository;

    public AuditorConfig(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Bean
    public AuditorAware<User> auditorAware() {
        return new AuditorAwareImpl(userRepository);
    }
}
