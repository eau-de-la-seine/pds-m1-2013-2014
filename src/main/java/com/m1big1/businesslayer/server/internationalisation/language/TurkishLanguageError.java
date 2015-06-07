package com.m1big1.businesslayer.server.internationalisation.language;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.m1big1.businesslayer.server.dto.presentation.ErrorDTO;

/**
 * 
 * @author Gokan
 */
public class TurkishLanguageError implements ILanguageError {
    private static Logger logger = Logger.getLogger(TurkishLanguageError.class);
  
    private static String 
        errorTitleConnection,
        errorMessageConnection,
        errorTitleCreateNodeStep1,
        errorMessageCreateNodeStep1,
        errorTitleCreateNodeStep2,
        errorMessageCreateNodeStep2,
        errorTitleDeleteNode,
        errorMessageDeleteNode,
        errorTitleRenameNode,
        errorMessageRenameNode,
        errorTitleUpdateWord,
        errorMessageUpdateWord,
        errorTitleReadWord,
        errorMessageReadWord;
            
    static {  
        try {
            Properties properties = new Properties();
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            InputStream propertiesFile = classLoader.getResourceAsStream("languages/language_tr.properties");
            properties.load(propertiesFile);
            propertiesFile.close();  
            
            errorTitleConnection = properties.getProperty("errorTitleConnection");
            errorMessageConnection = properties.getProperty("errorMessageConnection");
            errorTitleCreateNodeStep1 = properties.getProperty("errorTitleCreateNodeStep1");
            errorMessageCreateNodeStep1 = properties.getProperty("errorMessageCreateNodeStep1");
            errorTitleCreateNodeStep2 = properties.getProperty("errorTitleCreateNodeStep2");
            errorMessageCreateNodeStep2 = properties.getProperty("errorMessageCreateNodeStep2");
            errorTitleDeleteNode = properties.getProperty("errorTitleDeleteNode");
            errorMessageDeleteNode = properties.getProperty("errorMessageDeleteNode");
            errorTitleRenameNode = properties.getProperty("errorTitleRenameNode");
            errorMessageRenameNode = properties.getProperty("errorMessageRenameNode");
            errorTitleUpdateWord = properties.getProperty("errorTitleUpdateWord");
            errorMessageUpdateWord = properties.getProperty("errorMessageUpdateWord");
            errorTitleReadWord = properties.getProperty("errorTitleReadWord");
            errorMessageReadWord = properties.getProperty("errorMessageReadWord");
        } catch (IOException e) {
            logger.fatal("Error when trying to load language_tr.properties", e);
        }
    }

    @Override
    public ErrorDTO connectionError() {
        ErrorDTO error = new ErrorDTO();
        error.setErrorTitle(errorTitleConnection);
        error.setErrorMessage(errorMessageConnection);
        return error;
    }

    @Override
    public ErrorDTO createNodeStep1Error() {
        ErrorDTO error = new ErrorDTO();
        error.setErrorTitle(errorTitleCreateNodeStep1);
        error.setErrorMessage(errorMessageCreateNodeStep1);
        return error;
    }

    @Override
    public ErrorDTO createNodeStep2Error() {
        ErrorDTO error = new ErrorDTO();
        error.setErrorTitle(errorTitleCreateNodeStep2);
        error.setErrorMessage(errorMessageCreateNodeStep2);
        return error;
    }

    @Override
    public ErrorDTO deleteNodeError() {
        ErrorDTO error = new ErrorDTO();
        error.setErrorTitle(errorTitleDeleteNode);
        error.setErrorMessage(errorMessageDeleteNode);
        return error;
    }

    @Override
    public ErrorDTO renameNodeError() {
        ErrorDTO error = new ErrorDTO();
        error.setErrorTitle(errorTitleRenameNode);
        error.setErrorMessage(errorMessageRenameNode);
        return error;
    }

    @Override
    public ErrorDTO updateWordError() {
        ErrorDTO error = new ErrorDTO();
        error.setErrorTitle(errorTitleUpdateWord);
        error.setErrorMessage(errorMessageUpdateWord);
        return error;
    }

    @Override
    public ErrorDTO readWordError() {
        ErrorDTO error = new ErrorDTO();
        error.setErrorTitle(errorTitleReadWord);
        error.setErrorMessage(errorMessageReadWord);
        return error;
    }
        
}
