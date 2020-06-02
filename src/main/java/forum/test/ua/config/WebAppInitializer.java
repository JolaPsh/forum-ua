package forum.test.ua.config;

import forum.test.ua.repository.UserRepositoryImpl;
import forum.test.ua.service.UserServiceImpl;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.Filter;

/**
 * Created by Joanna Pakosh, 07.2019
 */

public class WebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    private static final String CHARACTER_ENCODING = "UTF-8";

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class<?>[]{UserServiceImpl.class,
                UserRepositoryImpl.class,
                DbConfig.class,
                OAuth2SecurityConfig.class
        };
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[]{MvcWebConfig.class};
    }

    @Override
    protected String[] getServletMappings() {
        return new String[]{"/*"};
    }

    @Override
    protected Filter[] getServletFilters() {
        final CharacterEncodingFilter encodingFilter = new CharacterEncodingFilter();
        encodingFilter.setEncoding(CHARACTER_ENCODING);
        encodingFilter.setForceEncoding(true);
        return new Filter[]{encodingFilter};
    }
}