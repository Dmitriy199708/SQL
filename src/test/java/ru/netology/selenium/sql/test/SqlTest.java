package ru.netology.selenium.sql.test;

import org.junit.jupiter.api.*;
import ru.netology.selenium.sql.data.DataHelper;
import ru.netology.selenium.sql.data.SQLHelper;
import ru.netology.selenium.sql.page.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static ru.netology.selenium.sql.data.SQLHelper.cleanAuthCodes;
import static ru.netology.selenium.sql.data.SQLHelper.cleanDatabase;

public class
SqlTest {
    LoginPage loginPage;

    @AfterEach
    void tearDown() {
        cleanAuthCodes();
    }

    @AfterAll
    static void tearDownAll() {
        cleanDatabase();
    }


    @BeforeEach
    void setUp() {
        loginPage = open("http://localhost:9999", LoginPage.class);
    }

    @Test
    @DisplayName("Should successfully Login to dashboard with exist Login and password from sut test data")
    void shouLdSuccessfulLogin() {
        var authInfo = DataHelper.getAuthInfoWithTestData();
        var verificationPage = loginPage.validLogin(authInfo);
        verificationPage.verifyVerificationPageVisiblity();
        var verificationCode = SQLHelper.getVerificationCode();
        verificationPage.validVerify(verificationCode.getCode());
    }
    @Test
    @DisplayName("Shovld get error notification if user is not exist in base")
    void shouLdbetErrorNotificationifLoginiithRandonUserwithoutAddingToBase() {
        var authInfo = DataHelper.generateRandomUser();
        loginPage.validLogin(authInfo);
        loginPage.verifyErrorNotification("Ошибка! \nНеверно указан логин или пароль");
    }
    @Test
    @DisplayName("Should get error notification if login with exist in base and active user and random verification code")
    void shouLd6etErrorNotificationIFLoginWithExistUserAndRandomVerificationCode() {
        var authInfo = DataHelper.getAuthInfoWithTestData();
        var verificationPage = loginPage.validLogin(authInfo);
        verificationPage.verifyVerificationPageVisiblity();
        var verificationCode = DataHelper.generateRandonVerificationCode();
        verificationPage.verify(verificationCode.getCode());
        verificationPage.verifyErrorNotification("Ошибка! Неверно указан код! Попробуйте ещё раз.");
    }
}
