package com.m1big1.businesslayer.server.word;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.log4j.Logger;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;



/**
 * <p>This class is useful for creating, writing, or reading a Word document.</p>
 * <p>This class uses XWPF, it's an Apache POI framework, download it here : http://poi.apache.org/download.html</p>
 * @author Gokan EKINCI
 */
public class WordManager {
    private static Logger logger = Logger.getLogger(WordManager.class);
    private String path;
    
    
    /**
     * <p>Constructor of the WordManager</p>
     * <p>Example of use : 
     * WordManager wordManager = new WordManager("C:/files/1.docx");
     * @param path The path of your document
     */
    public WordManager(String path){
        this.path = path;
    }
    
    /**
     * <p>Write into a Word document</p>
     * <p>This method treats exceptions internally because it does not return any value.</p>
     * <p>Example of use : 
     * WordManager wordManager = new WordManager("C:/files/1.docx");
     * wordManager.write("Etre ou ne pas être, tel est la question !");</p>
     * <p>Inspired by @see https://svn.apache.org/repos/asf/poi/trunk/src/examples/src/org/apache/poi/xwpf/usermodel/SimpleDocument.java</p>
     * @param text The text you want to insert into your word document.
     */
    public void write(String text){
        XWPFDocument doc = new XWPFDocument();
        XWPFParagraph paragraph = doc.createParagraph();
        XWPFRun run = paragraph.createRun();
        run.setText(text);
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(path);
            doc.write(out);
        } catch (FileNotFoundException e) {
            logger.fatal("Word document is not found", e);
        } catch (IOException e) {
            logger.fatal("Error when writing", e);
        } finally {
            try {
                if(out != null) out.close();
            } catch (IOException e) {
                logger.fatal("Error when closing FileOutputStream", e);
            }

        }
    }

    
    /**
     * <p>Read a Word document</p>
     * <p>This method launch an Exception if there is a problem because it returns a value</p>
     * <p>Example of use : 
     * WordManager wordManager = new WordManager("C:/files/1.docx");
     * wordManager.read();</p>
     * @return
     * @throws IOException This method launch exception if cannot read Word the document 
     */
    public String read() throws IOException {
        FileInputStream fis = null;
        String value = null;
        try {
            fis = new FileInputStream(path);
            XWPFWordExtractor extractor = new XWPFWordExtractor(new XWPFDocument(fis));       
            value = extractor.getText();
            extractor.close();
        } catch (FileNotFoundException e) {
            logger.fatal("Error when Reading Word, FileNotFound", e);
        } finally {
            try {
                if(fis != null) fis.close();
            } catch (IOException e) {
                logger.fatal("Error when closing FileInputStream", e);
            }
        }
        
        return value;
    }
    
    /**
     * <p>Create a Word document</p>
     */
    public void createWordDocument(){
        write("");
    }
}
