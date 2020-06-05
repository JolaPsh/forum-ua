package forum.test.ua.web.rest;

import forum.test.ua.model.User;
import forum.test.ua.service.UserService;
import forum.test.ua.to.UserTo;
import forum.test.ua.util.UserUtil;
import forum.test.ua.util.exceptions.SystemException;
import forum.test.ua.util.validators.FieldValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Joanna, 03.2020.
 */

@RestController
@RequestMapping(value = AdminRestController.REST_URL)
public class AdminRestController {

    private final Logger log = LoggerFactory.getLogger(AdminRestController.class);
    static final String REST_URL = "/admin/rest/users";

    private final UserService userService;

    @Autowired
    public AdminRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public User getUserById(@PathVariable("id") int id) {
        log.info(" getUserById {{}} ", id);
        return userService.findUserById(id);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public User getUserByEmail(@RequestParam("email") String email) {
        log.info(" getUserByEmail {{}} ", email);
        return userService.findByEmail(email);
    }

    @GetMapping(value = "/by", produces = MediaType.APPLICATION_JSON_VALUE)
    public User getUserByName(@RequestParam("name") String name) {
        log.info(" getUserByName {{}} ", name);
        return userService.findByUsername(name);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createUser(@RequestBody UserTo userTo) throws SystemException {
        log.info("createUser {{}} ", userTo);

        User newUser = null;
        try {
            FieldValidator.notEmpty(userTo.getUsername(), "username");
            FieldValidator.notEmpty(userTo.getEmail(), "email");
            FieldValidator.notEmpty(userTo.getPassword(), "password");
            FieldValidator.notEmpty(userTo.getRoles().toString(), "roles");
            newUser = userService.create(UserUtil.createNewUser(userTo));
        } catch (DataIntegrityViolationException exc) {
            return ResponseEntity.badRequest().body("User with this email already exists");
        }
        int generatedId = newUser.getId();
        return new ResponseEntity<>("User with id " + generatedId + " created.", HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateUser(@RequestBody User user, @PathVariable("id") int id) {
        log.info(" updateUser {{}} ", id);
        String[] params = {user.getUsername(), user.getEmail(), user.getPassword()};
        FieldValidator.atLeastOneParameter(params);
        userService.update(user, id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable("id") int id) {
        log.info(" deleteUser {{}} ", id);
        userService.delete(id);
    }
}
