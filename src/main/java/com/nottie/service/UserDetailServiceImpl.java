package com.nottie.service;

import com.nottie.exception.NotFoundException;
import com.nottie.repository.UserDetailRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailServiceImpl implements UserDetailsService {
    private final UserDetailRepository userDetailRepository;

    public UserDetailServiceImpl(UserDetailRepository userDetailRepository) {
        this.userDetailRepository = userDetailRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userDetailRepository.findByEmail(email).orElseThrow(() ->  new NotFoundException("No user found by email " + email));
    }
}
