package forum.test.ua.service;

import forum.test.ua.model.User;
import forum.test.ua.util.exceptions.ApplicationException;

/**
 * Created by Joanna Pakosh, 05.2020
 */

public interface UserService {

    User findUserById(int id) throws ApplicationException;

    User findByEmail(String email) throws ApplicationException;

    User findByUsername(String username) throws ApplicationException;

    User create(User user);

    void update(User user, int id);

    void delete(int id);
}
