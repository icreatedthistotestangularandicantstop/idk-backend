package app.core.security;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Collections;

public enum AuthorityType {
    USER(Collections.singleton(() -> AuthorityType.USER_ROLE));

    public static final  String USER_ROLE = "USER";
    private final Collection<? extends GrantedAuthority> roles;

    AuthorityType(Collection<? extends GrantedAuthority> roles) {
        this.roles = roles;
    }

    public Collection<? extends GrantedAuthority> getRoles() {
            return roles;
        }
}
