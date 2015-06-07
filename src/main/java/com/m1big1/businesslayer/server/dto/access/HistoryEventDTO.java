//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.05.09 at 01:30:17 AM CEST 
//


package com.m1big1.businesslayer.server.dto.access;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for HistoryEventDTO complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="HistoryEventDTO">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="next-history-event" type="{}HistoryEventDTO" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="event-date" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="event-kind" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="complete-path" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "HistoryEventDTO", propOrder = {
    "nextHistoryEvent"
})
public class HistoryEventDTO {

    @XmlElement(name = "next-history-event")
    protected HistoryEventDTO nextHistoryEvent;
    @XmlAttribute(name = "event-date")
    protected String eventDate;
    @XmlAttribute(name = "event-kind")
    protected String eventKind;
    @XmlAttribute(name = "complete-path")
    protected String completePath;

    /**
     * Gets the value of the nextHistoryEvent property.
     * 
     * @return
     *     possible object is
     *     {@link HistoryEventDTO }
     *     
     */
    public HistoryEventDTO getNextHistoryEvent() {
        return nextHistoryEvent;
    }

    /**
     * Sets the value of the nextHistoryEvent property.
     * 
     * @param value
     *     allowed object is
     *     {@link HistoryEventDTO }
     *     
     */
    public void setNextHistoryEvent(HistoryEventDTO value) {
        this.nextHistoryEvent = value;
    }

    /**
     * Gets the value of the eventDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEventDate() {
        return eventDate;
    }

    /**
     * Sets the value of the eventDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEventDate(String value) {
        this.eventDate = value;
    }

    /**
     * Gets the value of the eventKind property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEventKind() {
        return eventKind;
    }

    /**
     * Sets the value of the eventKind property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEventKind(String value) {
        this.eventKind = value;
    }

    /**
     * Gets the value of the completePath property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCompletePath() {
        return completePath;
    }

    /**
     * Sets the value of the completePath property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCompletePath(String value) {
        this.completePath = value;
    }

}