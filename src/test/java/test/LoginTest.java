package test;
import lombok.val;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import data.DataHelper;
import page.DashboardPage;
import page.LoginPage;
import java.sql.SQLException;




public class LoginTest {





    @Test
    @DisplayName("Should login with code from sms")
    void shouldLoginWithSmsCode() throws SQLException{
        val loginPage = new LoginPage();
        loginPage.openLoginPage();
        val authInfo = DataHelper.getAuthInfo();
        val verifPage = loginPage.validLogin(authInfo);
        verifPage.verificationPage();
        val verificationCode = DataHelper.getVerificationCode();
        val dashboardPage = verifPage.validVerify(verificationCode.getCode());
        dashboardPage.verifyDashboardPageVisiblity();
    }

    @Test
    @DisplayName("Should be error notification if login with wrong password")
    void loginWithWrongPassword() {
        val loginPage = new LoginPage();
        loginPage.openLoginPage();
        val authInfo = DataHelper.getAuthInfoWithWrongPassword();
        loginPage.validLogin(authInfo);
        loginPage.errorNotificationCreate();
    }



    @Test
    @DisplayName("Should login with random user and status active")
    void shouldLoginWithRandomUser() throws SQLException {
        val loginPage = new LoginPage();
        loginPage.openLoginPage();
        val authInfo = DataHelper.getRandomAuthInfo("active");
        val verificationPage = loginPage.validLogin(authInfo);
        verificationPage.verificationPage();
        val verificationCode = DataHelper.getVerificationCode();
        val dashboardPage = verificationPage.validVerify(verificationCode.getCode());
        dashboardPage.verifyDashboardPageVisiblity();
    }

    @AfterAll
    static void cleanBase() throws SQLException {
        val dataHelperPage = new DataHelper();
        dataHelperPage.cleanDataBase();
    }

}
