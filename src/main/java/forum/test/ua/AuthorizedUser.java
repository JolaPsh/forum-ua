package forum.test.ua;

import forum.test.ua.model.User;

/**
 * Created by Joanna Pakosh, 07.2019
 */

public class AuthorizedUser extends org.springframework.security.core.userdetails.User {
    private static final long serialVersionUID = 1L;

    private User user;

    public AuthorizedUser(User user) {
        super(user.getEmail(), user.getPassword(), user.isEnabled(), true, true, true, user.getRoles());
        this.user = user;
    }

    public int getId() {
        return user.getId();
    }

    public User getUserDetails() {
        return user;
    }

    @Override
    public String toString() {
        return user.toString();
    }
}