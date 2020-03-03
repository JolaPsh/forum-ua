package forum.test.ua.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by Joanna Pakosh, 07.2019
 */

public class PropsUtil {

   public Properties getProps() {
        Properties props= new Properties();
        try (InputStream in = PropsUtil.class.getClassLoader().getResourceAsStream("props/mail.properties")) {
            if (in != null) {
                props.load(in);
            } else {
                throw new FileNotFoundException("File mail.properties not found in the classpath");
            }
        }
        catch(IOException exc){
            exc.printStackTrace();
        }
        return props;
    }
}
