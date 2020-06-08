package forum.test.ua.service;

import forum.test.ua.model.User;
import forum.test.ua.util.exceptions.NotFoundException;

/**
 * Created by Joanna Pakosh, 05.2020
 */

public interface UserService {

    User findUserById(int id) throws NotFoundException;

    User findByEmail(String email) throws NotFoundException;

    User findByUsername(String username) throws NotFoundException;

    User create(User user);

    void update(User user, int id);

    void delete(int id) throws NotFoundException;
}
