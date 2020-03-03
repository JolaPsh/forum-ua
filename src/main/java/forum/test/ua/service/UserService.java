package forum.test.ua.service;

import forum.test.ua.AuthorizedUser;
import forum.test.ua.model.User;
import forum.test.ua.repository.UserRepositoryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import static forum.test.ua.util.UserUtil.prepareToSave;

/**
 * Created by Joanna Pakosh, 07.2019
 */

@Service("userService")
public class UserService implements UserDetailsService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepositoryImpl userRepositoryImpl;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(final String email) throws UsernameNotFoundException {
        User user = userRepositoryImpl.findByEmail(email.trim().toLowerCase());
        log.debug("Authenticating email = {}", email);
        if (user == null) {
            throw new UsernameNotFoundException("User with email: " + email + " not found");
        }
        return new AuthorizedUser(user);
    }

    public User create(User user) {
        log.info("new user created: username ={}, email= {}", user.getUsername(), user.getEmail());
        return userRepositoryImpl.save(prepareToSave(user, passwordEncoder));
    }
}
