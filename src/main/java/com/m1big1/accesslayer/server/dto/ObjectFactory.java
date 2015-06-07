//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.05.09 at 03:10:05 PM CEST 
//


package com.m1big1.accesslayer.server.dto;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.m1big1.accesslayer.dto package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _Node_QNAME = new QName("", "node");
    private final static QName _HistoryEvent_QNAME = new QName("", "history-event");
    private final static QName _DTO_QNAME = new QName("", "DTO");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.m1big1.accesslayer.dto
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link HistoryEventDTO }
     * 
     */
    public HistoryEventDTO createHistoryEventDTO() {
        return new HistoryEventDTO();
    }

    /**
     * Create an instance of {@link SearchWordDTO }
     * 
     */
    public SearchWordDTO createSearchWordDTO() {
        return new SearchWordDTO();
    }

    /**
     * Create an instance of {@link UpdateWordDTO }
     * 
     */
    public UpdateWordDTO createUpdateWordDTO() {
        return new UpdateWordDTO();
    }

    /**
     * Create an instance of {@link HistoryDTO }
     * 
     */
    public HistoryDTO createHistoryDTO() {
        return new HistoryDTO();
    }

    /**
     * Create an instance of {@link FindDatabaseIdByLoginPasswordDTO }
     * 
     */
    public FindDatabaseIdByLoginPasswordDTO createFindDatabaseIdByLoginPasswordDTO() {
        return new FindDatabaseIdByLoginPasswordDTO();
    }

    /**
     * Create an instance of {@link RenameNodeDTO }
     * 
     */
    public RenameNodeDTO createRenameNodeDTO() {
        return new RenameNodeDTO();
    }

    /**
     * Create an instance of {@link NodeStructureDTO }
     * 
     */
    public NodeStructureDTO createNodeStructureDTO() {
        return new NodeStructureDTO();
    }

    /**
     * Create an instance of {@link FindUserNodeListByDatabaseIdDTO }
     * 
     */
    public FindUserNodeListByDatabaseIdDTO createFindUserNodeListByDatabaseIdDTO() {
        return new FindUserNodeListByDatabaseIdDTO();
    }

    /**
     * Create an instance of {@link DeleteNodeDTO }
     * 
     */
    public DeleteNodeDTO createDeleteNodeDTO() {
        return new DeleteNodeDTO();
    }

    /**
     * Create an instance of {@link ReadWordDTO }
     * 
     */
    public ReadWordDTO createReadWordDTO() {
        return new ReadWordDTO();
    }

    /**
     * Create an instance of {@link CreateNodeDTO }
     * 
     */
    public CreateNodeDTO createCreateNodeDTO() {
        return new CreateNodeDTO();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link NodeStructureDTO }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "node")
    public JAXBElement<NodeStructureDTO> createNode(NodeStructureDTO value) {
        return new JAXBElement<NodeStructureDTO>(_Node_QNAME, NodeStructureDTO.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link HistoryEventDTO }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "history-event")
    public JAXBElement<HistoryEventDTO> createHistoryEvent(HistoryEventDTO value) {
        return new JAXBElement<HistoryEventDTO>(_HistoryEvent_QNAME, HistoryEventDTO.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DTO }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "DTO")
    public JAXBElement<DTO> createDTO(DTO value) {
        return new JAXBElement<DTO>(_DTO_QNAME, DTO.class, null, value);
    }

}
