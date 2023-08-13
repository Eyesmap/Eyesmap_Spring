//package com.spring.eyesmap.global.security;
//
//import com.spring.eyesmap.domain.account.repository.Account;
//import com.spring.eyesmap.domain.account.repository.AccountRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//public class AccountDetailsService implements UserDetailsService {
//    private final AccountRepository accountRepository;
//    // value for Authentication
//    @Override
//    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
//        Account findAccount = accountRepository.findById(id).orElse(null);
//        if(findAccount!=null) {
//            return new AccountDetails(findAccount);
//        }
//        return null;
//    }
//}