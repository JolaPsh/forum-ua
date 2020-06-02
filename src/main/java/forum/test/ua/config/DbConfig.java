package forum.test.ua.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * Created by Joanna Pakosh, 07.2019
 */

@Configuration
@PropertySource("classpath:db/db.properties")
@EnableTransactionManagement
public class DbConfig {

    @Autowired
    private Environment env;

    @Bean
    public DataSource getDataSource() {
        DriverManagerDataSource managerDataSource = new DriverManagerDataSource();
        managerDataSource.setDriverClassName(env.getProperty("jdbc.driver"));
        managerDataSource.setUrl(env.getProperty("jdbc.url"));
        managerDataSource.setUsername(env.getProperty("jdbc.username"));
        managerDataSource.setPassword(env.getProperty("jdbc.password"));
        return managerDataSource;
    }

    @Bean
    public DataSourceTransactionManager getTransactionManager() {
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
        transactionManager.setDataSource(getDataSource()); // you must be inside transaction for cursors to work
        return transactionManager;
    }
}