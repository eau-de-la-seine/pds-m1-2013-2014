package com.m1big1.businesslayer.server.internationalisation.language;

import com.m1big1.businesslayer.server.dto.presentation.ErrorDTO;

/**
 * 
 * @author Gokan EKINCI
 */
public interface ILanguageError {
    public ErrorDTO connectionError();
    public ErrorDTO createNodeStep1Error();
    public ErrorDTO createNodeStep2Error();
    public ErrorDTO deleteNodeError();
    public ErrorDTO renameNodeError();
    public ErrorDTO updateWordError();
    public ErrorDTO readWordError();
}
