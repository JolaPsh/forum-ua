package forum.test.ua.model;

import org.springframework.data.domain.Persistable;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

/**
 * Created by Joanna Pakosh, 07.2019
 */

public class User implements Persistable<Integer> {

    private Integer id;
    private String username;
    private String email;
    private LocalDateTime registered;
    private String password;
    private boolean enabled;
    private Set<Role> roles;

    public User() {
    }

    public User(User u) {
        this(u.getId(), u.getUsername(), u.getEmail(), u.getPassword(), u.getRegistered(), u.isEnabled(), u.getRoles());
    }

    public User(Integer id, String username, String email, String password, LocalDateTime registered, boolean enabled, Collection<Role> roles) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.registered = registered;
        this.password = password;
        this.enabled = enabled;
        setRole(roles);
    }

    public User(Integer id, String username, String email, String password, Role role, Role... roles) {
        this(id, username, email, password, LocalDateTime.now(), true, EnumSet.of(role, roles));
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getRegistered() {
        return registered;
    }

    public void setRegistered(LocalDateTime registered) {
        this.registered = registered;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRole(Collection<Role> roles) {
        this.roles = CollectionUtils.isEmpty(roles) ? Collections.emptySet() : EnumSet.copyOf(roles);
    }

    public boolean isNew() {
        return this.id == null;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", registered=" + registered +
                ", password='" + password + '\'' +
                ", enabled=" + enabled +
                ", roles=" + roles +
                '}';
    }
}