package ch.erik.ebooktranslator.service.impl;

import ch.erik.ebooktranslator.service.BrowserUtil;
import lombok.extern.slf4j.Slf4j;
import nl.siegmann.epublib.domain.Resource;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
public abstract class AbstractWebDriverTranslationLibrary extends AbstractTranslationLibraryClient {

    @Override
    public boolean translate(final List<Resource> textResources) throws IOException {
        final WebDriver browser = BrowserUtil.createBrowser();
        browser.get(this.getUrl());

        final WebElement source = browser.findElement(By.id("source"));
        source.click();

        for (final Resource textResource : textResources) {
            final byte[] resourceContent = textResource.getData();
            final Document document = Jsoup.parse(new String(resourceContent));

            document.title(translateText(browser, document.title()));

            final Elements elements = document.select(HTML_TAGS_CONTAINING_TEXT);
            final List<TextNode> textNodes = elements.textNodes();

            textNodes.stream()
                    .filter(textNode -> StringUtils.isNotBlank(textNode.getWholeText()))
                    .forEach(textNode -> translateText(browser, textNode.text()));
        }

        browser.close();

        return true;
    }

    private String translateText(final WebDriver browser, final String text) {
        final int maxRetries = 10;
        int retries = 0;

        while (retries <= maxRetries) {
            try {
                final WebElement sourceTextArea = browser.findElement(By.xpath(getSourceWebElementClass()));
                sourceTextArea.clear();

                final WebDriverWait wait = new WebDriverWait(browser, 5);
                wait.until(valueLoadedCondition());
                sourceTextArea.sendKeys(text);

                wait.until(valueLoadedCondition());

                return browser.findElement(By.xpath(getTargetWebElementClass())).getText();
            } catch (Exception e) {
                log.error("Could not perform request. Reason {}", e.getMessage());
                log.debug("Retries left: {}", maxRetries - retries);
                retries++;
            }
        }

        throw new RuntimeException("Could no read translation");
    }

    private ExpectedCondition<Boolean> valueLoadedCondition() {
        return driver -> {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return true;
        };
    }

    protected abstract String getUrl();

    protected abstract String getSourceWebElementClass();

    protected abstract String getTargetWebElementClass();
}
