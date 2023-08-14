package com.spring.eyesmap.global.security;

import com.spring.eyesmap.domain.account.repository.Account;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@Data
@Getter
@AllArgsConstructor
public class AccountDetails implements UserDetails {

    private Account account;

    // check authority(role)
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList();
        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {

                return String.valueOf(account.getRole());
            }
        });
        return collection;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return account.getUserId().toString();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}