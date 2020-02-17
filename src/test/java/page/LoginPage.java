package page;


import com.codeborne.selenide.SelenideElement;
import data.DataHelper;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class LoginPage {
    private SelenideElement form = $("form");
    private SelenideElement loginField = form.$("[data-test-id=login] input");
    private SelenideElement passwordField = form.$("[data-test-id=password] input");
    private SelenideElement loginButton = form.$("[data-test-id=action-login]");
    private SelenideElement errorNotification = $("[data-test-id='error-notification']");


    public void openLoginPage() {
        open("http://localhost:9999");
    }

    public void errorNotificationCreate() {
        errorNotification.shouldBe(visible);
    }

    public VerificationPage validLogin(DataHelper.AuthInfo info) {
        loginField.setValue(info.getLogin());
        passwordField.setValue(info.getPassword());
        loginButton.click();
        return new VerificationPage();
    }
}