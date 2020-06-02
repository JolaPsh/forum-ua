package forum.test.ua.util;

import forum.test.ua.model.User;
import forum.test.ua.to.UserTo;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.StringUtils;

/**
 * Created by Joanna Pakosh, 07.2019
 */

public class UserUtil {

    public UserUtil () {}

    public static User createNewUser(UserTo newUser) {
        return new User(null, newUser.getUsername(),
                newUser.getEmail(),
                newUser.getPassword(),
                newUser.getStatus(),
                newUser.getRoles());
    }

    public static User prepareToSave(User user, BCryptPasswordEncoder passwordEncoder) {
        String username = user.getUsername();
        String email = user.getEmail();
        String password = user.getPassword();

        user.setUsername(StringUtils.isEmpty(username) ? username : user.getUsername().trim().toLowerCase());
        user.setPassword(StringUtils.isEmpty(password) ? password : passwordEncoder.encode(password));
        user.setEmail(StringUtils.isEmpty(email) ? email : user.getEmail().trim().toLowerCase());
        return user;
    }
}