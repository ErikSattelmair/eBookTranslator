package ch.erik.ebooktranslator.service;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

@Slf4j
public class BrowserUtil {

    private BrowserUtil() {
    }

    public static WebDriver createBrowser() throws IOException {
        System.setProperty("webdriver.chrome.driver", new ClassPathResource("driver/chromedriver").getFile().getPath());

        // Set proxy
        final Proxy proxy = new Proxy();
        final String httpProxyAddress = ProxyUtil.getRandomHttpProxy();
        final String httpsProxyAddress = ProxyUtil.getRandomHttpsProxy();
        proxy.setHttpProxy(httpProxyAddress);
        proxy.setSslProxy(httpsProxyAddress);

        log.info("Using http proxy {}", httpProxyAddress);
        log.info("Using https proxy {}", httpsProxyAddress);

        final ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.setCapability("proxy", proxy);

        return new ChromeDriver(chromeOptions);
    }

}
