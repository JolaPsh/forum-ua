package forum.test.ua.config;

import forum.test.ua.service.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.InMemoryOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.AuthenticatedPrincipalOAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.accept.ContentNegotiationStrategy;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.security.config.oauth2.client.CommonOAuth2Provider.FACEBOOK;
import static org.springframework.security.config.oauth2.client.CommonOAuth2Provider.GOOGLE;

/**
 * Created by Joanna Pakosh, 08.2019.
 */

@Configuration
@EnableWebSecurity
@PropertySource("classpath:props/social-cfg.properties")
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class OAuth2SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final Logger log = LoggerFactory.getLogger(OAuth2SecurityConfig.class);
    private static String CLIENT_PROPERTY_KEY = "spring.security.oauth2.client.registration.";

    @Autowired
    private Environment env;

    @Autowired
    private UserServiceImpl userServiceSecurity;

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        return new InMemoryClientRegistrationRepository(getRegistrations());
    }

    @Bean
    public OAuth2AuthorizedClientService authorizedClientService() {
        return new InMemoryOAuth2AuthorizedClientService(clientRegistrationRepository());
    }

    @Bean
    public OAuth2AuthorizedClientRepository authorizedClientRepository(
            OAuth2AuthorizedClientService authorizedClientService) {
        return new AuthenticatedPrincipalOAuth2AuthorizedClientRepository(authorizedClientService);
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void setContentNegotationStrategy(ContentNegotiationStrategy contentNegotiationStrategy) {
        super.setContentNegotationStrategy(contentNegotiationStrategy);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/admin/**")
                .access("hasRole('ROLE_ADMIN')")
                .and().httpBasic();

        http.authorizeRequests()
                .antMatchers("/login**",
                        "/register**",
                        "/signin/**").permitAll()
                .antMatchers("/home**").access("hasRole('ROLE_USER')")
                //  .anyRequest().authenticated()
                //  .and().httpBasic()
                .and().csrf().disable()
                .formLogin()
                .loginPage("/login")
                .usernameParameter("email")
                .defaultSuccessUrl("/home")
                .failureUrl("/login?error=true")
                .permitAll()
                .and()
                .oauth2Login()
                .clientRegistrationRepository(this.clientRegistrationRepository())
                .authorizedClientRepository(this.authorizedClientRepository(authorizedClientService()))
                .authorizedClientService(this.authorizedClientService())
                .defaultSuccessUrl("/home")
                .failureUrl("/login")
                // .authorizationEndpoint()
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login?logout")
                .and()
                .exceptionHandling().accessDeniedPage("/error");
    }

    private List<ClientRegistration> getRegistrations() {
        String googleClientId = env.getProperty(CLIENT_PROPERTY_KEY + "google.client-id");
        String googleClientSecret = env.getProperty(CLIENT_PROPERTY_KEY + "google.client-secret");
        String facebookClientId = env.getProperty(CLIENT_PROPERTY_KEY + "facebook.app-id");
        String facebookClientSecret = env.getProperty(CLIENT_PROPERTY_KEY + "facebook.app-secret");

        List<ClientRegistration> registrations = new ArrayList<>();
        registrations.add(GOOGLE.getBuilder("google").clientId(googleClientId)
                .clientSecret(googleClientSecret)
                .build());
        registrations.add(FACEBOOK.getBuilder("facebook").clientId(facebookClientId)
                .clientSecret(facebookClientSecret)
                .build());
        return registrations;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        log.info("AuthenticationManagerBuilder is configured ={}", auth.isConfigured());
        auth.userDetailsService(userServiceSecurity).passwordEncoder(bCryptPasswordEncoder());
    }
}
