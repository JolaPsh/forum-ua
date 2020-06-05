create or replace function _getuserbyemail(in pi_user_email varchar,
                                           out user_cursor refcursor)
    language plpgsql
as
$$
BEGIN
    OPEN user_cursor FOR SELECT * FROM users WHERE usr_email = pi_user_email;
END;
$$;

alter function _getuserbyemail(in varchar, out refcursor) owner to postgres;

---------------------------------------------------------------------------------------------------------------
create or replace function _getUserById(in pi_user_id int,
                                        out user_cursor refcursor)
    language plpgsql
as
$$
BEGIN
    OPEN user_cursor FOR SELECT * FROM users where usr_id = pi_user_id;
END;
$$;

alter function _getUserById(in int, out refcursor) owner to postgres;
---------------------------------------------------------------------------------------------------------------
create or replace function _getUserByName(in pi_user_name varchar,
                                          out user_cursor refcursor)
    language plpgsql
as
$$
BEGIN
    OPEN user_cursor FOR SELECT * FROM users where usr_name = pi_user_name;
END;
$$;

alter function _getUserByName(in varchar, out refcursor) owner to postgres;
---------------------------------------------------------------------------------------------------------------
create or replace function _createUser(in pi_user_name varchar,
                                       in pi_user_email varchar,
                                       in pi_user_password varchar) --returns refcursor
    returns void
    language plpgsql
as
$$
BEGIN
    INSERT INTO users (usr_id, usr_name, usr_email, usr_registered, usr_password, usr_enabled)
    VALUES (NEXTVAL('global_seq'), pi_user_name, pi_user_email, current_timestamp, pi_user_password, true);
    --RETURNING usr_id INTO user_cursor;
END;
$$;

alter function _createUser(in varchar, in varchar, in varchar) owner to postgres;
---------------------------------------------------------------------------------------------------------------
create or replace function _updateUser (in pi_user_id integer,
                                        in pi_user_name varchar,
                                        in pi_user_email varchar,
                                        in pi_user_password varchar,
                                        out result_id integer) returns integer
    language plpgsql
as
$$
BEGIN
    UPDATE users
        SET usr_name = COALESCE(pi_user_name, usr_name),
            usr_email = COALESCE(pi_user_email, usr_email),
            usr_password = COALESCE(pi_user_password, usr_password)
    WHERE usr_id = pi_user_id
    AND  (pi_user_name IS NOT NULL AND pi_user_name IS DISTINCT FROM usr_name OR
          pi_user_email IS NOT NULL AND pi_user_email IS DISTINCT FROM usr_email OR
          pi_user_password IS NOT NULL AND pi_user_password IS DISTINCT FROM usr_password
        )
    RETURNING usr_id INTO result_id;
END;
$$;

alter function _updateUser(in integer, in varchar, in varchar, in varchar, out integer) owner to postgres;
---------------------------------------------------------------------------------------------------------------
create or replace function _deleteUser(in pi_user_id integer) returns void
    language plpgsql
as
$$
BEGIN
    UPDATE users
    SET usr_status = 'C'
    where usr_id = pi_user_id;
END;
$$;

alter function _deleteUser(in integer) owner to postgres;
---------------------------------------------------------------------------------------------------------------
create or replace function _setRoles(in pi_user_id integer,
                                     out role_cursor refcursor)
    language plpgsql
as
$$
BEGIN
    OPEN role_cursor FOR SELECT user_role FROM user_roles where user_id = pi_user_id;
END;
$$;

alter function _setRoles(in integer, out refcursor) owner to postgres;
---------------------------------------------------------------------------------------------------------------
create or replace function _deleteRoles(in pi_user_id integer) returns void
    language plpgsql
as
$$
BEGIN
    UPDATE user_roles
    SET user_role = null
    WHERE user_id = pi_user_id;
END;
$$;

alter function _deleteRoles(in integer, out refcursor) owner to postgres;
---------------------------------------------------------------------------------------------------------------
create or replace function _insertRoles(in pi_user_id integer, in pi_user_role varchar) returns void
    language plpgsql
as
$$
BEGIN
    INSERT INTO user_roles VALUES (pi_user_id, pi_user_role);
END;
$$;

alter function _insertRoles(in integer, in varchar) owner to postgres;
---------------------------------------------------------------------------------------------------------------