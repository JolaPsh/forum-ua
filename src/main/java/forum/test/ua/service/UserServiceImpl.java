package forum.test.ua.service;

import forum.test.ua.AuthorizedUser;
import forum.test.ua.model.User;
import forum.test.ua.repository.UserRepository;
import forum.test.ua.util.exceptions.ApplicationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import static forum.test.ua.util.UserUtil.prepareToSave;

/**
 * Created by Joanna Pakosh, 07.2019
 */

@Service("userService")
public class UserServiceImpl implements UserDetailsService, UserService {

    private final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(final String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email.trim().toLowerCase());
        log.debug("Authenticating email = {}", email);
        if (user == null) {
            throw new UsernameNotFoundException("User with email: " + email + " not found");
        }
        return new AuthorizedUser(user);
    }

    @Override
    public User findUserById(int id) throws ApplicationException {
        return userRepository.findUserById(id);
    }

    @Override
    public User findByEmail(String email) throws ApplicationException {
        return userRepository.findByEmail(email);
    }

    @Override
    public User findByUsername(String username) throws ApplicationException {
        return userRepository.findByUsername(username);
    }

    @Override
    public User create(User user) {
        log.info("new user created: username ={}, email= {}", user.getUsername(), user.getEmail());
        Assert.notNull(user, "user must not be null");
        return userRepository.save(prepareToSave(user, passwordEncoder));
    }

    @Override
    public void update(User user, int id) {
        log.info("update user = {} ", user);
        user.setId(id);
        userRepository.save(prepareToSave(user, passwordEncoder));
    }

    @Override
    public void delete(int id) {
        log.info("delete user = {} ", id);
        userRepository.delete(id);
    }
}
