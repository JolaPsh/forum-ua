package forum.test.ua.util;

import forum.test.ua.model.Role;
import forum.test.ua.model.User;
import forum.test.ua.to.UserTo;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.StringUtils;

import java.util.Collections;

/**
 * Created by Joanna Pakosh, 07.2019
 */

public class UserUtil {

    public static User createNewUser(UserTo newUser) {
        return new User(null, newUser.getUsername(), newUser.getEmail(), newUser.getPassword(), Role.ROLE_USER);
    }

    public static User prepareToSave(User user, BCryptPasswordEncoder passwordEncoder) {
        String password = user.getPassword();
        user.setPassword(StringUtils.isEmpty(password) ? password : passwordEncoder.encode(password));
        user.setEmail(user.getEmail().trim().toLowerCase());
        user.setRole(Collections.singletonList(Role.ROLE_USER));
        return user;
    }
}