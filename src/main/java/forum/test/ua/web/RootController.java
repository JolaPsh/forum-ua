package forum.test.ua.web;

import forum.test.ua.service.MailService;
import forum.test.ua.service.UserService;
import forum.test.ua.to.UserTo;
import forum.test.ua.util.Constants;
import forum.test.ua.util.UserUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.IOException;

/**
 * Created by Joanna Pakosh, 07.2019
 */

@Controller
public class RootController {

    private final Logger log = LoggerFactory.getLogger(RootController.class);

    private final UserService userService;
    private final MailService mailService;
    private static final String authorizationRequestBaseUri = "oauth2/authorization";

    @Autowired
    private ClientRegistrationRepository clientRegistrationRepository;

    @Autowired
    public RootController(UserService userService, MailService mailService) {
        this.userService = userService;
        this.mailService = mailService;
    }

    @GetMapping(value = {"/", "/login"})
    public String login(final Model model) {
        log.info("Start page, log in.");
        String facebookAuthenticationUrl = authorizationRequestBaseUri + "/" +
                clientRegistrationRepository.findByRegistrationId("facebook").getRegistrationId();

        String googleAuthenticationUrl = authorizationRequestBaseUri + "/" +
                clientRegistrationRepository.findByRegistrationId("google").getRegistrationId();

        model.addAttribute("googleUrl", googleAuthenticationUrl);
        model.addAttribute("facebookUrl", facebookAuthenticationUrl);
        return "login";
    }

    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    @GetMapping("/home")
    public String homepage() {
        log.info("You are logged in/registered as " + SecurityContextHolder.getContext()
                .getAuthentication().getName());
        return "home";
    }

    @GetMapping("/register")
    public String register(final Model model) {
        model.addAttribute("userTo", new UserTo());
        model.addAttribute("register", true);
        return "register";
    }

    @Secured({"ROLE_USER"})
    @PostMapping("/register")
    @ResponseStatus(value = HttpStatus.CREATED)
    public String registerNewUser(final ModelMap model, @ModelAttribute("userTo") @Valid UserTo userTo,
                                  final BindingResult bindResult) throws MailException, IOException, MessagingException {

        if (bindResult.hasErrors()) {
            log.error("[User is not valid]");
            return "register";
        }

        try {
            userService.create(UserUtil.createNewUser(userTo));
            mailService.sendRegistrationConfirmation(msg -> {
                        MimeMessageHelper msgHelper = new MimeMessageHelper(msg, true, "UTF-8");
                        msgHelper.setFrom("test.yjava77@gmail.com");
                        msgHelper.setTo(userTo.getEmail());
                        msgHelper.setSubject(Constants.MESSAGE_SUBJECT);
                        msgHelper.setText("Dear " + userTo.getUsername() + "!\n" + Constants.MESSAGE_BODY);
                    }
            );
            log.debug("Sending registration confirmation...");

            model.addAttribute("successMessage", Constants.MESSAGE_SUCCESSED);
            model.addAttribute("confirmMail", Constants.MESSAGE_CONFIRM_EMAIL);
            log.info("User {}, name: {} has been registered successfully!", userTo.getEmail(),
                    userTo.getUsername());
            return "registeredSuccess";
        } catch (DataIntegrityViolationException exc) {
            bindResult.rejectValue("email", "userTo.duplicateEmail");
            log.info("DataIntegrityViolationException : {}", exc.getMessage());
            // model.addAttribute("register", true);
            return "register";
        }
    }
}