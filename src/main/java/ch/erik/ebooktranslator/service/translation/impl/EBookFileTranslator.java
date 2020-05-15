package ch.erik.ebooktranslator.service.translation.impl;

import ch.erik.ebooktranslator.exception.TranslationException;
import ch.erik.ebooktranslator.model.TranslationParameterHolder;
import ch.erik.ebooktranslator.service.translation.EBookTranslator;
import ch.erik.ebooktranslator.service.translation.TranslationLibraryClient;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.siegmann.epublib.domain.*;
import nl.siegmann.epublib.epub.EpubReader;
import nl.siegmann.epublib.epub.EpubWriter;

import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
public class EBookFileTranslator implements EBookTranslator {

    private final TranslationLibraryClient translationLibraryClient;

    @Override
    public byte[] translateEBook(final TranslationParameterHolder translationParameterSet) throws TranslationException {
        final byte[] source = translationParameterSet.getEbook();
        if (source != null) {
            final EpubReader epubReader = new nl.siegmann.epublib.epub.EpubReader();

            try {
                final InputStream inputStream = new ByteArrayInputStream(source);
                final Book book = epubReader.readEpub(inputStream);

                inputStream.close();

                final TableOfContents tableOfContents = book.getTableOfContents();
                final List<TOCReference> tocReferences = tableOfContents.getTocReferences();
                final List<Resource> tocReferenceResources = tocReferences.stream().map(ResourceReference::getResource).collect(Collectors.toList());
                this.translationLibraryClient.translate(tocReferenceResources, translationParameterSet);

                final List<Resource> resources = book.getContents();
                final List<Resource> textResources = resources.stream().filter(resource -> resource.getMediaType().getName().equals("application/xhtml+xml")).collect(Collectors.toList());

                // translate content
                this.translationLibraryClient.translate(textResources, translationParameterSet);

                // Add new cover image
                final String coverImageFilePath = translationParameterSet.getCoverImageFilePath();
                final String[] filePathSegments = coverImageFilePath.split(File.pathSeparator);
                final String fileName = filePathSegments[filePathSegments.length - 1];
                book.setCoverImage(new Resource(new FileInputStream(new File(coverImageFilePath)), fileName));

                return getContentOfTranslatedEbook(book);
            } catch (IOException e) {
                throw new TranslationException(e.getMessage(), e);
            }

        }

        throw new IllegalArgumentException("source must not be null");
    }

    private byte[] getContentOfTranslatedEbook(Book book) throws IOException {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final EpubWriter epubWriter = new EpubWriter();
        epubWriter.write(book, outputStream);

        return outputStream.toByteArray();
    }
}
