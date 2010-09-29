
package com.ebay.soap.eBLBaseComponents;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import org.w3c.dom.Element;


/**
 * 
 * 				Specifies a query consisting of attributes. Only attributes that are returned
 * 				from GetProductFinder can be used. See the Developer's Guide for more information.
 * 			
 * 
 * <p>Java class for SearchRequestType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SearchRequestType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ProductFinderID" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="SearchAttributes" type="{urn:ebay:apis:eBLBaseComponents}SearchAttributesType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;any/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SearchRequestType", propOrder = {
    "productFinderID",
    "searchAttributes",
    "any"
})
public class SearchRequestType
    implements Serializable
{

    private final static long serialVersionUID = 12343L;
    @XmlElement(name = "ProductFinderID")
    protected Integer productFinderID;
    @XmlElement(name = "SearchAttributes")
    protected List<SearchAttributesType> searchAttributes;
    @XmlAnyElement(lax = true)
    protected List<Object> any;

    /**
     * Gets the value of the productFinderID property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getProductFinderID() {
        return productFinderID;
    }

    /**
     * Sets the value of the productFinderID property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setProductFinderID(Integer value) {
        this.productFinderID = value;
    }

    /**
     * 
     * 
     * @return
     *     array of
     *     {@link SearchAttributesType }
     *     
     */
    public SearchAttributesType[] getSearchAttributes() {
        if (this.searchAttributes == null) {
            return new SearchAttributesType[ 0 ] ;
        }
        return ((SearchAttributesType[]) this.searchAttributes.toArray(new SearchAttributesType[this.searchAttributes.size()] ));
    }

    /**
     * 
     * 
     * @return
     *     one of
     *     {@link SearchAttributesType }
     *     
     */
    public SearchAttributesType getSearchAttributes(int idx) {
        if (this.searchAttributes == null) {
            throw new IndexOutOfBoundsException();
        }
        return this.searchAttributes.get(idx);
    }

    public int getSearchAttributesLength() {
        if (this.searchAttributes == null) {
            return  0;
        }
        return this.searchAttributes.size();
    }

    /**
     * 
     * 
     * @param values
     *     allowed objects are
     *     {@link SearchAttributesType }
     *     
     */
    public void setSearchAttributes(SearchAttributesType[] values) {
        this._getSearchAttributes().clear();
        int len = values.length;
        for (int i = 0; (i<len); i ++) {
            this.searchAttributes.add(values[i]);
        }
    }

    protected List<SearchAttributesType> _getSearchAttributes() {
        if (searchAttributes == null) {
            searchAttributes = new ArrayList<SearchAttributesType>();
        }
        return searchAttributes;
    }

    /**
     * 
     * 
     * @param value
     *     allowed object is
     *     {@link SearchAttributesType }
     *     
     */
    public SearchAttributesType setSearchAttributes(int idx, SearchAttributesType value) {
        return this.searchAttributes.set(idx, value);
    }

    /**
     * 
     * 
     * @return
     *     array of
     *     {@link Element }
     *     {@link Object }
     *     
     */
    public Object[] getAny() {
        if (this.any == null) {
            return new Object[ 0 ] ;
        }
        return ((Object[]) this.any.toArray(new Object[this.any.size()] ));
    }

    /**
     * 
     * 
     * @return
     *     one of
     *     {@link Element }
     *     {@link Object }
     *     
     */
    public Object getAny(int idx) {
        if (this.any == null) {
            throw new IndexOutOfBoundsException();
        }
        return this.any.get(idx);
    }

    public int getAnyLength() {
        if (this.any == null) {
            return  0;
        }
        return this.any.size();
    }

    /**
     * 
     * 
     * @param values
     *     allowed objects are
     *     {@link Element }
     *     {@link Object }
     *     
     */
    public void setAny(Object[] values) {
        this._getAny().clear();
        int len = values.length;
        for (int i = 0; (i<len); i ++) {
            this.any.add(values[i]);
        }
    }

    protected List<Object> _getAny() {
        if (any == null) {
            any = new ArrayList<Object>();
        }
        return any;
    }

    /**
     * 
     * 
     * @param value
     *     allowed object is
     *     {@link Element }
     *     {@link Object }
     *     
     */
    public Object setAny(int idx, Object value) {
        return this.any.set(idx, value);
    }

}
