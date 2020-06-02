package forum.test.ua.repository;

import forum.test.ua.model.User;

/**
 * Created by Joanna Pakosh, 07.2019
 */

public interface UserRepository {

    User findUserById(int id);

    User findByEmail(String email);

    User findByUsername(String username);

    User save(User u);
}
