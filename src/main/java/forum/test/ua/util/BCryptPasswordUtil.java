package forum.test.ua.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Created by Joanna Pakosh, 07.2019
 */

public class BCryptPasswordUtil  {

    @Autowired static Environment env;

    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        System.out.println("--------------------------------------------------------------------------------------");
        System.out.println("bcrypt \"user1\": " + encoder.encode("user1"));
        System.out.println("--------------------------------------------------------------------------------------");
        System.out.println("bcrypt \"user2\":" + encoder.encode("user2"));
        System.out.println("--------------------------------------------------------------------------------------");
        System.out.println("bcrypt \"user3\":" + encoder.encode("user3"));

        String key = env.getProperty("jdbc.username");
    }
}
