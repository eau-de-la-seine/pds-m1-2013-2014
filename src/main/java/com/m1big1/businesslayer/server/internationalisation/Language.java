package com.m1big1.businesslayer.server.internationalisation;

/**
 * 
 * @author Gokan EKINCI
 */
public enum Language {
    ENGLISH("ENGLISH"), FRENCH("FRENCH"), TURKISH("TURKISH");
    
    
    public final String lang;
    
    private Language(String lang){
        this.lang = lang;
    }
    
    @Override
    public String toString(){
        return lang;
    }
}
