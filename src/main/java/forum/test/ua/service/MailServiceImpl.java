package forum.test.ua.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

/**
 * Created by Joanna Pakosh, 07.2019
 */

@Service("mailService")
public class MailServiceImpl implements MailService {

  private final JavaMailSender javaMailSender;

  @Autowired
  public MailServiceImpl(JavaMailSender javaMailSender) {
    this.javaMailSender = javaMailSender;
  }

  @Override
  public void sendRegistrationConfirmation(final MimeMessagePreparator msg) {
    javaMailSender.send(msg);
  }
}
