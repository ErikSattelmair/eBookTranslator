package ch.erik.ebooktranslator.service;

import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

public class BrowserUtil {

    private BrowserUtil() {
    }

    public static WebDriver createBrowser() throws IOException {
        System.setProperty("webdriver.chrome.driver", new ClassPathResource("driver/chromedriver").getFile().getPath());

        // Set proxy
        final Proxy proxy = new Proxy();
        final String proxyAddress = ProxyHelper.getRandomProxy();
        proxy.setHttpProxy(proxyAddress);
        proxy.setSslProxy(proxyAddress);
        proxy.setFtpProxy(proxyAddress);

        final ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.setCapability("proxy", proxy);

        return new ChromeDriver(chromeOptions);
    }

}
