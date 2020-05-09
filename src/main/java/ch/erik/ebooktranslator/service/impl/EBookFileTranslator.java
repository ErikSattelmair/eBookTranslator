package ch.erik.ebooktranslator.service.impl;

import ch.erik.ebooktranslator.exception.TranslationException;
import ch.erik.ebooktranslator.service.EBookTranslator;
import ch.erik.ebooktranslator.service.TranslationLibraryClient;
import lombok.extern.slf4j.Slf4j;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.epub.EpubReader;
import nl.siegmann.epublib.epub.EpubWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class EBookFileTranslator implements EBookTranslator {

    @Autowired
    @Qualifier("deepl")
    private TranslationLibraryClient translationLibraryClient;

    @Override
    public byte[] translateEBook(final byte[] source) throws TranslationException {
        if(source != null) {
            final EpubReader epubReader = new nl.siegmann.epublib.epub.EpubReader();

            try {
                final InputStream inputStream = new ByteArrayInputStream(source);
                final Book book = epubReader.readEpub(inputStream);
                final List<Resource> resources = book.getContents();
                final List<Resource> textResources = resources.stream().filter(resource -> resource.getMediaType().getName().equals("application/xhtml+xml")).collect(Collectors.toList());

                this.translationLibraryClient.translate(textResources);

                inputStream.close();

                final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                final EpubWriter epubWriter = new EpubWriter();
                epubWriter.write(book, outputStream);
                outputStream.close();

                return outputStream.toByteArray();
            } catch (IOException e) {
                throw new TranslationException(e.getMessage(), e);
            }

        }

        throw new IllegalArgumentException("source must not be null");
    }
}
