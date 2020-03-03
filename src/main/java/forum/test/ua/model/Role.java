package forum.test.ua.model;

import org.springframework.security.core.GrantedAuthority;

/**
 * Created by Joanna Pakosh, 07.2019
 */

public enum Role implements GrantedAuthority {
    ROLE_USER,
    ROLE_ADMIN;

    @Override
    public String getAuthority() {
        return name();
    }
}