package page;


import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.page;

public class VerificationPage {
    private SelenideElement smsCode = $("[data-test-id=code] input");
    private SelenideElement verifyButton = $("[data-test-id='action-verify']");
    private SelenideElement errorNotification = $("[data-test-id='error-notification']");

    public void verificationPage() {
        smsCode.shouldBe(Condition.visible);
    }

    public void errorNotificationCreate() {
        errorNotification.shouldBe(visible);
    }

    public DashboardPage validVerify(String verificationCode) {
        smsCode.setValue(verificationCode);
        verifyButton.click();
        return new DashboardPage();
    }
}