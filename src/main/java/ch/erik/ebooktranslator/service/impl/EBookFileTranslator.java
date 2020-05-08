package ch.erik.ebooktranslator.service.impl;

import ch.erik.ebooktranslator.exception.TranslationException;
import ch.erik.ebooktranslator.service.EBookTranslator;
import ch.erik.ebooktranslator.service.XmlFragmentProcessor;
import lombok.extern.slf4j.Slf4j;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.epub.EpubWriter;
import org.jdom2.JDOMException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class EBookFileTranslator implements EBookTranslator {

    @Autowired
    private XmlFragmentProcessor xmlFragmentProcessor;

    @Override
    public byte[] translateEBook(final byte[] source) throws TranslationException {
        if(source != null) {
            final nl.siegmann.epublib.epub.EpubReader epubReader = new nl.siegmann.epublib.epub.EpubReader();

            try {
                final InputStream inputStream = new ByteArrayInputStream(source);
                final Book book = epubReader.readEpub(inputStream);
                final List<Resource> resources = book.getContents();
                final List<Resource> textResources = resources.stream().filter(resource -> resource.getMediaType().getName().equals("application/xhtml+xml")).collect(Collectors.toList());

                for (final Resource textResource : textResources) {
                    textResource.setData(xmlFragmentProcessor.processHtmlFragment(textResource.getData()));
                }

                inputStream.close();

                final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                final EpubWriter epubWriter = new EpubWriter();
                epubWriter.write(book, outputStream);
                outputStream.close();

                return outputStream.toByteArray();
            } catch (IOException | JDOMException e) {
                throw new TranslationException(e.getMessage(), e);
            }

        }

        throw new IllegalArgumentException("source must not be null");
    }
}
