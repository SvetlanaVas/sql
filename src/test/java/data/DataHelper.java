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

        public DataHelper() {
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

                runner.update(conn, dataSQL, Long.toString(count + 1),
                        login, "$2a$10$EcC8LSizV72Yx3I.tN2R7uEm0pHtREtKsB3870dzeidCa6sXc0vt6", status);
                /*runner.update(conn, dataSQL, count,
                        login, "$2a$10$EcC8LSizV72Yx3I.tN2R7uEm0pHtREtKsB3870dzeidCa6sXc0vt6", status);*/
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

        public static void cleanDataBase() throws SQLException {
            val cleanCards = "DELETE FROM cards";
            val cleanAuthCodes = "DELETE FROM auth_codes";
            val cleanUser = "DELETE FROM users";
            val runner = new QueryRunner();
            try (val conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/app", "app", "pass")) {
                val cleanCardsUser = runner.execute(conn, cleanCards, new BeanHandler<>(User.class));
                val cleanAuthCodesUser = runner.execute(conn, cleanAuthCodes, new BeanHandler<>(User.class));
                val cleanUserUser = runner.execute(conn, cleanUser, new BeanHandler<>(User.class));
            }
        }
    }