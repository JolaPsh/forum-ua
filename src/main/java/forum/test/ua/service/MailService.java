package forum.test.ua.service;

import org.springframework.mail.javamail.MimeMessagePreparator;

import javax.mail.MessagingException;
import java.io.IOException;

/**
 * Created by Joanna Pakosh, 07.2019
 */

public interface MailService {

    void sendRegistrationConfirmation(final MimeMessagePreparator msg) throws MessagingException, IOException;
}
