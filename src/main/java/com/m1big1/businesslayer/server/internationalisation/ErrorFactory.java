package com.m1big1.businesslayer.server.internationalisation;

import com.m1big1.businesslayer.server.dto.presentation.ErrorDTO;
import com.m1big1.businesslayer.server.internationalisation.language.EnglishLanguageError;
import com.m1big1.businesslayer.server.internationalisation.language.FrenchLanguageError;
import com.m1big1.businesslayer.server.internationalisation.language.ILanguageError;
import com.m1big1.businesslayer.server.internationalisation.language.TurkishLanguageError;

/**
 * 
 * @author Gokan EKINCI
 */
public class ErrorFactory {
    public ErrorDTO getErrorDTO(Language clientLanguage, ErrorCode errorCode){           
        switch(clientLanguage){
            case ENGLISH: return getErrorDTO(new EnglishLanguageError(), errorCode);           
            case FRENCH:  return getErrorDTO(new FrenchLanguageError(), errorCode);          
            case TURKISH: return getErrorDTO(new TurkishLanguageError(), errorCode);
            default:      return null;
        }        
    }
    
    private ErrorDTO getErrorDTO(ILanguageError languageError, ErrorCode errorCode){        
        switch(errorCode){
            case CONNECTION:        return languageError.connectionError();
            case CREATE_NODE_STEP1: return languageError.createNodeStep1Error();
            case CREATE_NODE_STEP2: return languageError.createNodeStep2Error();
            case DELETE_NODE:       return languageError.deleteNodeError();
            case RENAME_NODE:       return languageError.renameNodeError();
            case UPDATE_WORD:       return languageError.updateWordError();
            case READ_WORD:         return languageError.readWordError();
            default:                return null;
        }        
    }
}
