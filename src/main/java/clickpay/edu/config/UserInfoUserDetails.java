package clickpay.edu.config;

import clickpay.edu.entity.Role;
import clickpay.edu.entity.UserInfo;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class UserInfoUserDetails implements UserDetails {

    private String imagePath;
    private String name;
    private String email;
    private String password;
    private List<SimpleGrantedAuthority> authorities;

    public UserInfoUserDetails(UserInfo userInfo) {
        this.imagePath=userInfo.getImagePath();
        this.name=userInfo.getName();
        this.email=userInfo.getEmail();
        this.password=userInfo.getPassword();
        this.authorities=userInfo.getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getRole()))
                .collect(Collectors.toList());
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
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
