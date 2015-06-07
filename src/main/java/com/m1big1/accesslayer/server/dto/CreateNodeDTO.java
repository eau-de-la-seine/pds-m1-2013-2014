//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.05.09 at 03:10:05 PM CEST 
//


package com.m1big1.accesslayer.server.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;extension base="{}DTO">
 *       &lt;sequence>
 *       &lt;/sequence>
 *       &lt;attribute name="user-database-id" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="parent-folder-id" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="node-type" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="node-name" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="node-id" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="step" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="success" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "CreateNodeDTO")
public class CreateNodeDTO
    extends DTO
{

    @XmlAttribute(name = "user-database-id", required = true)
    protected int userDatabaseId;
    @XmlAttribute(name = "parent-folder-id", required = true)
    protected int parentFolderId;
    @XmlAttribute(name = "node-type", required = true)
    protected int nodeType;
    @XmlAttribute(name = "node-name")
    protected String nodeName;
    @XmlAttribute(name = "node-id", required = true)
    protected int nodeId;
    @XmlAttribute(required = true)
    protected int step;
    @XmlAttribute(required = true)
    protected boolean success;

    /**
     * Gets the value of the userDatabaseId property.
     * 
     */
    public int getUserDatabaseId() {
        return userDatabaseId;
    }

    /**
     * Sets the value of the userDatabaseId property.
     * 
     */
    public void setUserDatabaseId(int value) {
        this.userDatabaseId = value;
    }

    /**
     * Gets the value of the parentFolderId property.
     * 
     */
    public int getParentFolderId() {
        return parentFolderId;
    }

    /**
     * Sets the value of the parentFolderId property.
     * 
     */
    public void setParentFolderId(int value) {
        this.parentFolderId = value;
    }

    /**
     * Gets the value of the nodeType property.
     * 
     */
    public int getNodeType() {
        return nodeType;
    }

    /**
     * Sets the value of the nodeType property.
     * 
     */
    public void setNodeType(int value) {
        this.nodeType = value;
    }

    /**
     * Gets the value of the nodeName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNodeName() {
        return nodeName;
    }

    /**
     * Sets the value of the nodeName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNodeName(String value) {
        this.nodeName = value;
    }

    /**
     * Gets the value of the nodeId property.
     * 
     */
    public int getNodeId() {
        return nodeId;
    }

    /**
     * Sets the value of the nodeId property.
     * 
     */
    public void setNodeId(int value) {
        this.nodeId = value;
    }

    /**
     * Gets the value of the step property.
     * 
     */
    public int getStep() {
        return step;
    }

    /**
     * Sets the value of the step property.
     * 
     */
    public void setStep(int value) {
        this.step = value;
    }

    /**
     * Gets the value of the success property.
     * 
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * Sets the value of the success property.
     * 
     */
    public void setSuccess(boolean value) {
        this.success = value;
    }

}
