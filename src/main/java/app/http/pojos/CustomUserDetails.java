package app.http.pojos;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class CustomUserDetails implements UserDetails {

    public enum Type {
        ADMIN("A", Collections.singleton(() -> "ADMIN")),
        USER("U", Collections.singleton(() -> "USER"));

        private final String token;
        private final Collection<? extends GrantedAuthority> roles;

        Type(String token, Collection<? extends GrantedAuthority> roles) {
            this.token = token;
            this.roles = roles;
        }

        public String getToken() {
            return token;
        }

        public Collection<? extends GrantedAuthority> getRoles() {
            return roles;
        }
    }

    private int id;
    private String username;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(int id, String username, String password, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.authorities = authorities;
    }

    public int getId() {
        return id;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
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
