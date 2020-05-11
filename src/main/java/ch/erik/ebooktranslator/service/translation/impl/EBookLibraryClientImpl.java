package ch.erik.ebooktranslator.service.translation.impl;

import ch.erik.ebooktranslator.service.translation.EBookLibraryClient;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
public class EBookLibraryClientImpl implements EBookLibraryClient {

    private static final String B_OK_CC_LOG_IN_URL = "https://singlelogin.org/?from=b-ok.cc";
    private static final String CREDENTIALS_E_MAIL = "erik.sattelmair@gmx.de";
    private static final String CREDENTIALS_PASSWORD = "$&%oRN&YGC0PFsj*GPPqzfK!";

    @Autowired
    private ResourceLoader resourceLoader;

    @Override
    public void downloadEBook() throws IOException {
        System.setProperty("webdriver.chrome.driver", this.resourceLoader.getResource("classpath:/driver/chromedriver").getFile().getPath());

        final WebDriver browser = new ChromeDriver();

        // fist log in
        browser.get(B_OK_CC_LOG_IN_URL);
        final WebElement username = browser.findElement(By.id("username"));
        username.sendKeys(CREDENTIALS_E_MAIL);

        final WebElement password = browser.findElement(By.id("password"));
        password.sendKeys(CREDENTIALS_PASSWORD);

        final WebElement submit = browser.findElement(By.name("submit"));
        submit.click();

        final WebDriverWait wait = new WebDriverWait(browser, 3);
        final WebElement topBook = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("checkBookDownloaded")));
        topBook.click();

        final WebElement topBookDetail = wait.until(ExpectedConditions.elementToBeClickable(By.className("dlButton")));
        topBookDetail.click();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            log.warn("Could not get thread {} to sleep for 2 seconds", Thread.currentThread().getName());
        }

        browser.close();
    }
}
