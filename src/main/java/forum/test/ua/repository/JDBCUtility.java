package forum.test.ua.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by Joanna Pakosh, 05.2020
 */

public class JDBCUtility {

    private static final Logger log = LoggerFactory.getLogger(JDBCUtility.class);

    public static void freeUpResources(Connection conn, Statement stmt, ResultSet rs) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException exc) {
                log.warn("");
            }
        }

        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException exc) {
                log.warn("");
            }
        }

        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException exc) {
                log.warn("");
            }
        }
    }
}
