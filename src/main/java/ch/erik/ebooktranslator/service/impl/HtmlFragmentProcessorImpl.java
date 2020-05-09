package ch.erik.ebooktranslator.service.impl;

import ch.erik.ebooktranslator.service.TranslationLibraryClient;
import ch.erik.ebooktranslator.service.XmlFragmentProcessor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jdom2.JDOMException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
public class HtmlFragmentProcessorImpl implements XmlFragmentProcessor {

    private static final String HTML_TAGS_CONTAINING_TEXT = "p, span, a, b, i, strong, em, br, h1, h2, h3, h4, h5, h6, " +
            "blockquote, q, code, pre, li, dt, dd, mark, ins, del, sup, sub, small";

    @Autowired
    private TranslationLibraryClient translationLibraryClient;

    @Override
    public byte[] processHtmlFragment(final byte[] xmlFragment) throws JDOMException, IOException {
        if(xmlFragment != null && xmlFragment.length > 0) {
            final Document document = Jsoup.parse(new String(xmlFragment));

            document.title(this.translationLibraryClient.translate(document.title()));

            final Elements elements = document.select(HTML_TAGS_CONTAINING_TEXT);
            elements.textNodes().stream().parallel()
                    .filter(textNode -> StringUtils.isNotBlank(textNode.text()))
                    .forEach(textNode -> {
                        final String text = textNode.text();
                        try {
                            textNode.text(this.translationLibraryClient.translate(text));
                        } catch (IOException e) {
                            log.error("Error while translating text {}", text);
                        }
                    });

            return document.outerHtml().getBytes();
        }

        throw new IllegalArgumentException("xml fragment must not be null!");
    }

}
