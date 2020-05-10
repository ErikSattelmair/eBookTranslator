package ch.erik.ebooktranslator.service.impl;

import ch.erik.ebooktranslator.exception.TranslationException;
import ch.erik.ebooktranslator.model.TranslationRequestModel;
import ch.erik.ebooktranslator.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;

@Service
@Slf4j
public class WorkflowEngineImpl implements WorkflowEngine {

    private static final String DOWNLOAD_FOLDER = "/Users/erik/Downloads";

    @Autowired
    private ParameterValidator parameterValidator;

    @Autowired
    private EBookLibraryClient eBookLibraryClient;

    @Autowired
    private EBookSourceFetchService eBookSourceFetchService;

    @Autowired
    private EBookSaveService eBookSaveService;

    @Override
    public boolean startWorkflow(final TranslationRequestModel translationRequestModel) {
        log.info("Start processing...");

        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        final String ebookCoverImage = translationRequestModel.getCoverImageFilePath();

        log.info("Validate cover image file path...");
        final boolean isCoverImageFilePathValid = this.parameterValidator.isCoverImageFilePathValid(ebookCoverImage);
        log.info("Validation done. Cover image file path is {}", isCoverImageFilePathValid);

        final boolean isEbookFilePathValid = this.parameterValidator.isEbookFilePathValid(translationRequestModel.isUseEbookFileFromDisk(), translationRequestModel.getEbookFilePath());
        if (!isCoverImageFilePathValid || !isEbookFilePathValid) {
            log.error("Input parameter not valid!");
            return false;
        }

        final boolean useEbookOnDisk = translationRequestModel.isUseEbookFileFromDisk();
        byte[] eBook;

        try {
            if (useEbookOnDisk) {
                final String ebookFilePath = translationRequestModel.getEbookFilePath();
                log.debug("Use ebook file from disk at {}", ebookFilePath);

                eBook = Files.readAllBytes(new File(ebookFilePath).toPath());
                log.info("E-Book fetched");
            } else {
                log.info("Downloading E-Book...");
                this.eBookLibraryClient.downloadEBook();
                log.info("Downloading E-Book done");

                log.info("Fetching E-Book service...");
                eBook = this.eBookSourceFetchService.getEbookSource(DOWNLOAD_FOLDER);
                log.info("E-Book fetched");
            }

            log.info("Translating E-Book...");
            final EBookTranslator eBookTranslator = new EBookFileTranslator(getTranslationEngine(translationRequestModel));
            final boolean useProxy = translationRequestModel.isUseProxy();
            final byte[] translatedEbook = eBookTranslator.translateEBook(eBook, ebookCoverImage, useProxy);
            log.info("Translation done");

            log.info("Saving E-Book...");
            this.eBookSaveService.saveBook(translatedEbook);
            log.info("E-Book saved");

            stopWatch.stop();
            log.info("Processing took " + stopWatch.getTime() + " seconds.");

            log.info("Processing done");

            return true;
        } catch (Exception | TranslationException e) {
            log.error("Error while processing workflow! Reason {}", e.getMessage());
        }

        return false;
    }

    private TranslationLibraryClient getTranslationEngine(final TranslationRequestModel translationRequestModel) {
        if (translationRequestModel.isUseDeepl()) {
            return new DeeplTranslationLibraryClient();
        } else if (translationRequestModel.isUseGoogle()) {
            return new GoogleTranslationLibraryClient();
        } else {
            return new MyMemoryTranslationLibraryClient();
        }
    }
}
