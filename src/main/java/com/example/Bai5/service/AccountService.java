package com.example.Bai5.service;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import com.example.Bai5.model.Account;
import com.example.Bai5.repository.AccountRepository;

@Service
public class AccountService implements UserDetailsService {

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        Account account = accountRepository.findByLoginName(username)
                .orElseThrow(() -> new UsernameNotFoundException("Could not find user"));

        Set<SimpleGrantedAuthority> authorities = account.getRoles().stream()
                                .map(role -> {
                                        String roleName = role.getName();
                                        if (!roleName.startsWith("ROLE_")) {
                                                roleName = "ROLE_" + roleName;
                                        }
                                        return new SimpleGrantedAuthority(roleName);
                                })
                .collect(Collectors.toSet());

        return new org.springframework.security.core.userdetails.User(
                account.getLogin_name(),
                account.getPassword(),
                authorities
        );
    }
}