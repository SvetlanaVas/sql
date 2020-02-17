package data;
import com.github.javafaker.Faker;
import lombok.Value;
import lombok.val;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.DriverManager;
import java.sql.SQLException;


import  java.sql.*;


    public class DataHelper {

        private DataHelper() {
        }

        @Value
        public static class AuthInfo {
            private String login;
            private String password;
        }

        public static AuthInfo getAuthInfo() {

            return new AuthInfo("vasya", "qwerty123");
        }

        public static AuthInfo getAuthInfoWithWrongPassword() {
            return new AuthInfo("vasya", "qwerty");
        }

        @Value
        public static class VerificationCode {
            private String code;
        }
        public static AuthInfo getRandomAuthInfo(String status) throws SQLException {
            val faker = new Faker();
            String login = faker.name().firstName();
            val runner = new QueryRunner();
            val countSQL = "SELECT COUNT(*) FROM users;";
            val dataSQL = "INSERT INTO users (id, login, password, status) VALUES (?, ?, ?, ?);";
            try (val conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/app",
                    "app", "pass")) {
                long count = runner.query(conn, countSQL, new ScalarHandler<>());
                // при создании нового пользователя, логин меняется, но пароль в зашифрованном виде используется тот,
                // который принадлежит пользователю с логином vasya (см. DBeaver)
                runner.update(conn, dataSQL, Long.toString(count + 1),
                        login, "$2a$10$Pml3uwcimo7D/XZVwf2OaOWML5yYu5c.ziYKTwG36kAhzdmJRCYN2", status);
            }
            return new AuthInfo(login, "qwerty123");
        }


        public static VerificationCode getVerificationCode() throws SQLException {
            String verificationCode = "";
            val codesSQL = "SELECT * FROM auth_codes ORDER BY created DESC LIMIT 1;";
            val runner = new QueryRunner();
            try (val conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/app", "app", "pass")) {
                val usersCode = runner.query(conn, codesSQL, new BeanHandler<>(User.class));
                verificationCode = usersCode.getCode();
            }
            return new VerificationCode(verificationCode);
        }
    }