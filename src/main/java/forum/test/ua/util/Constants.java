package forum.test.ua.util;

/**
 * Created by Joanna Pakosh, 07.2019
 */

public class Constants {

    public static final String MESSAGE_SUBJECT = "Registration confirmation";
    public static final String MESSAGE_BODY = getMessageBody().toString();
    public static final String MESSAGE_SUCCESSED = "User has been registered successfully.";
    public static final String MESSAGE_CONFIRM_EMAIL = "To verify the data provided we have sent you a confirmation email. Follow the link below.";

    public static StringBuilder getMessageBody(){
        return new StringBuilder().append("Thanks for registering! Your new account has been created. From now on," +
                "please log in to your account using your email address and your password.\n")
                .append("Here are a few pointers to help you get settled in.\n")
                .append("Follow the link below.\n")
                .append("http://localhost:8080/\n")
                .append("Sincerely,\n")
                .append("Team\n")
                .append("######@gmail.com");
    }
}
