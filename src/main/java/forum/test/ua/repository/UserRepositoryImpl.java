package forum.test.ua.repository;

import forum.test.ua.model.Role;
import forum.test.ua.model.User;
import forum.test.ua.util.exceptions.NotFoundException;
import forum.test.ua.util.exceptions.SystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by Joanna Pakosh, 07.2019
 */

@Repository
@Transactional(readOnly = true)
public class UserRepositoryImpl implements UserRepository {

    private final Logger log = LoggerFactory.getLogger(UserRepositoryImpl.class);
    private final DataSource dataSource;

    @Autowired
    public UserRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public User findUserById(int id) {
        String method = "_findUserById";
        Connection conn = null;
        CallableStatement cs = null;
        ResultSet rs = null;
        User user = new User();
        log.info("_find user by id started");

        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            cs = conn.prepareCall("{call _getUserById (?, ?)}");
            cs.setInt(1, id);
            cs.registerOutParameter(2, Types.REF_CURSOR);
            cs.execute();
            rs = (ResultSet) cs.getObject(2);
            if (rs.next()) {
                    user.setId(rs.getInt("USR_ID"));
                    user.setUsername(rs.getString("USR_NAME"));
                    user.setEmail(rs.getString("USR_EMAIL"));
                    user.setRegistered(rs.getObject("USR_REGISTERED", LocalDateTime.class));
                    user.setPassword(rs.getString("USR_PASSWORD"));
                    user.setEnabled(rs.getBoolean("USR_ENABLED"));
                    user.setStatus(rs.getString("USR_STATUS"));
                    setRoles(user);
            }
            else {
                throw new NotFoundException("User not found, id =" + id);
            }
        } catch (SQLException exc) {
            throw new SystemException(method, exc.getCause());
        } finally {
            JDBCUtility.freeUpResources(conn, cs, rs);
        }
        log.debug(method, user);
        return user;
    }

    @Override
    public User findByEmail(String email) {
        String method = "_findByEmail";
        Connection conn = null;
        CallableStatement cs = null;
        ResultSet rs = null;
        User user = new User();
        log.info("_find user by email started");

        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            cs = conn.prepareCall("{call _getUserByEmail (?, ?)}");
            cs.setString(1, email);
            cs.registerOutParameter(2, Types.REF_CURSOR);
            cs.execute();
            rs = (ResultSet) cs.getObject(2);
            if (rs.next()) {
                user.setId(rs.getInt("USR_ID"));
                user.setUsername(rs.getString("USR_NAME"));
                user.setEmail(rs.getString("USR_EMAIL"));
                user.setRegistered(rs.getObject("USR_REGISTERED", LocalDateTime.class));
                user.setPassword(rs.getString("USR_PASSWORD"));
                user.setEnabled(rs.getBoolean("USR_ENABLED"));
                user.setStatus(rs.getString("USR_STATUS"));
                setRoles(user);
            }
            else {
                throw new NotFoundException("User not found, email =" + email);
            }
        } catch (SQLException exc) {
            throw new SystemException(method, exc.getCause());
        } finally {
            JDBCUtility.freeUpResources(conn, cs, rs);
        }
        log.debug(method, user.getEmail());
        return user;
    }

    @Override
    public User findByUsername(String username) {
        String method = "_findByUsername";
        Connection conn = null;
        CallableStatement cs = null;
        ResultSet rs = null;
        User user = new User();

        try {
            log.info("_find by username started");
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            cs = conn.prepareCall("{call _getUserByName (?, ?)}");
            cs.setString(1, username);
            cs.registerOutParameter(2, Types.REF_CURSOR);
            cs.execute();
            rs = (ResultSet) cs.getObject(2);
            if (rs.next()) {
                user.setId(rs.getInt("USR_ID"));
                user.setUsername(rs.getString("USR_NAME"));
                user.setEmail(rs.getString("USR_EMAIL"));
                user.setRegistered(rs.getObject("USR_REGISTERED", LocalDateTime.class));
                user.setPassword(rs.getString("USR_PASSWORD"));
                user.setEnabled(rs.getBoolean("USR_ENABLED"));
                user.setStatus(rs.getString("USR_STATUS"));
                setRoles(user);
            }
            else {
                throw new NotFoundException("User not found, username =" + username);
            }
        } catch (SQLException exc) {
            throw new SystemException(method, exc.getCause());
        } finally {
            JDBCUtility.freeUpResources(conn, cs, rs);
        }
        log.debug(method, user.getUsername());
        return user;
    }

    @Override
    public User save(User user) {
        String method = "_save";

        if (user.isNew()) {
            Connection conn = null;
            PreparedStatement cs = null;
            ResultSet rs = null;
            try {
                log.info("_create user started");
                conn = dataSource.getConnection();
                //conn.setAutoCommit(false);
                //cs = conn.prepareCall("{call _createUser (?, ?, ?)}");
                String SQl = "INSERT INTO users (usr_name, usr_email, usr_password)" +
                        " VALUES (?, ?, ?)";
                cs = conn.prepareStatement(SQl, Statement.RETURN_GENERATED_KEYS);
                cs.setString(1, user.getUsername());
                cs.setString(2, user.getEmail());
                cs.setString(3, user.getPassword());
                int affectedRows = cs.executeUpdate();
                if (affectedRows > 0) {
                    try{
                        ResultSet row  = cs.getGeneratedKeys();
                        if (row.next()) {
                            int i = row.getInt(1);
                            user.setId(i);
                        }
                    } catch (Exception exc) {
                        System.out.println(exc.toString());
                    }
                }
                insertRoles(user);
            } catch (SQLException exc) {
                if ("23505".equals(exc.getSQLState())) {
                    throw new DataIntegrityViolationException(method, exc.getCause());
                }
                throw new SystemException(method, exc.getCause());
            } finally {
                JDBCUtility.freeUpResources(conn, cs, rs);
            }
            log.debug(method, user);
        } else {
            Connection conn = null;
            CallableStatement cs = null;
            ResultSet rs = null;
            try {
                log.info("_update user started");
                conn = dataSource.getConnection();
                cs = conn.prepareCall("{call _updateUser (?, ?, ?, ?, ?)}");
                cs.setInt(1, user.getId());
                cs.setString(2, user.getUsername());
                cs.setString(3, user.getEmail());
                cs.setString(4, user.getPassword());
                cs.registerOutParameter(5, Types.INTEGER);
                int count = cs.executeUpdate();
                if (count > 0) {
                    log.info("User with id: {} was updated.", user.getId()); //???
                }
            } catch (SQLException exc) {
                throw new SystemException(method, exc.getCause());
            } finally {
                JDBCUtility.freeUpResources(conn, cs, rs);
            }
            log.debug(method, user);
            // Simplest implementation.
            // More complicated : get user roles from DB and compare them with user.roles (assume that roles are changed rarely).
            // If roles are changed, calculate difference in java and delete/insert them.
            deleteRoles(user);
            insertRoles(user);
        }
        return user;
    }

    @Override
    public void delete(int id) {
        String method = "_deleteUser";
        Connection conn = null;
        CallableStatement cs = null;
        ResultSet rs = null;
        User u = findUserById(id);

        try {
            log.info("_delete user started");
            conn = dataSource.getConnection();
            cs = conn.prepareCall("{call _deleteUser (?)}");
            cs.setInt(1, u.getId());
            cs.executeUpdate();
        }catch (SQLException exc) {
            exc.printStackTrace();
            throw new SystemException(method, exc.getCause());
        } finally {
            JDBCUtility.freeUpResources(conn, cs, rs);
        }
        deleteRoles(u);
    }

    private User setRoles(User u) {
        if (u != null) {
            String method = "_setRoles";
            Connection conn = null;
            CallableStatement cs = null;
            ResultSet rs = null;
            List<Role> roles = new ArrayList<>();
            log.info("_set roles started");

            try {
                conn = dataSource.getConnection();
                conn.setAutoCommit(false);
                cs = conn.prepareCall("{call _setRoles (?, ?)}");
                cs.setInt(1, u.getId());
                cs.registerOutParameter(2, Types.REF_CURSOR);
                cs.execute();
                rs = (ResultSet) cs.getObject(2);
                while (rs.next()) {
                    roles.add(Role.valueOf(rs.getString("USER_ROLE")));
                }
            } catch (SQLException exc) {
                throw new SystemException(method, exc.getCause());
            } finally {
                JDBCUtility.freeUpResources(conn, cs, rs);
            }
            log.debug(method, roles);
            u.setRoles(roles);
        }
        return u;
    }

    private void insertRoles(User u) {
        Set<Role> roles = u.getRoles();
        if (!CollectionUtils.isEmpty(roles)) {
            String method = "_insertRoles";
            Connection conn = null;
            CallableStatement cs = null;
            ResultSet rs = null;
            log.info("_insert roles started");
            System.out.println("user id first_:" + u.getId());

            try {
                conn = dataSource.getConnection();
                cs = conn.prepareCall("{call _insertRoles (?, ?)}");
                for (Iterator it = roles.iterator(); it.hasNext();) {
                    Role role = (Role) it.next();
                    cs.setInt(1, u.getId());
                    cs.setString(2, role.getAuthority());
                    cs.addBatch();
                }
                cs.executeBatch();
            } catch (SQLException exc) {
                throw new SystemException(method, exc.getCause());
            } finally {
                JDBCUtility.freeUpResources(conn, cs, rs);
            }
        }
    }

    private void deleteRoles(User u) {
        if (u != null) {
            String method = "_deleteRoles";
            Connection conn = null;
            CallableStatement cs = null;
            ResultSet rs = null;
            log.info("_delete roles started");

            try {
                conn = dataSource.getConnection();
                cs = conn.prepareCall("{call _deleteRoles (?)}");
                cs.setInt(1, u.getId());
                cs.execute();
            } catch (SQLException exc) {
                throw new SystemException(method, exc.getCause());
            } finally {
                JDBCUtility.freeUpResources(conn, cs, rs);
            }
            log.debug(method, "");
        }
    }
}
