package ch.erik.ebooktranslator.service.impl;

import ch.erik.ebooktranslator.exception.TranslationException;
import ch.erik.ebooktranslator.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    private EBookTranslator eBookTranslator;

    @Autowired
    private EBookSaveService eBookSaveService;

    @Override
    public void startWorkflow(final String coverImageFilePath, final boolean useProxy) throws Exception, TranslationException {
        log.info("Start processing...");

        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        log.info("Validate cover image file path...");
        final boolean isCoverImageFilePathValid = this.parameterValidator.isCoverImageFilePathValid(coverImageFilePath);
        log.info("Validation done. Cover image file path is {}", isCoverImageFilePathValid);

        if (isCoverImageFilePathValid) {
            log.info("Downloading E-Book...");
            this.eBookLibraryClient.downloadEBook();
            log.info("Downloading E-Book done");

            log.info("Fetching E-Book service...");
            final byte[] eBook = this.eBookSourceFetchService.getEbookSource(DOWNLOAD_FOLDER);
            log.info("E-Book fetched");

            log.info("Translating E-Book...");
            final byte[] translatedEbook = this.eBookTranslator.translateEBook(eBook, coverImageFilePath, useProxy);
            log.info("Translation done");

            log.info("Saving E-Book...");
            this.eBookSaveService.saveBook(translatedEbook);
            log.info("E-Book saved");
        }

        stopWatch.stop();
        log.info("Processing took " + stopWatch.getTime() + " seconds.");

        log.info("Processing done");
    }
}
