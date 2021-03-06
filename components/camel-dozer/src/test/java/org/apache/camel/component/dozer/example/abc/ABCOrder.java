begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.dozer.example.abc
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|dozer
operator|.
name|example
operator|.
name|abc
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessorType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAttribute
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlElement
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlRootElement
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlType
import|;
end_import

begin_comment
comment|/**  *<p>Java class for anonymous complex type.  *   *<p>The following schema fragment specifies the expected content contained within this class.  *   *<pre>  *&lt;complexType>  *&lt;complexContent>  *&lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">  *&lt;sequence>  *&lt;element name="header">  *&lt;complexType>  *&lt;complexContent>  *&lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">  *&lt;sequence>  *&lt;element name="status" type="{http://www.w3.org/2001/XMLSchema}string"/>  *&lt;element name="customer-num" type="{http://www.w3.org/2001/XMLSchema}string"/>  *&lt;element name="order-num" type="{http://www.w3.org/2001/XMLSchema}string"/>  *&lt;/sequence>  *&lt;/restriction>  *&lt;/complexContent>  *&lt;/complexType>  *&lt;/element>  *&lt;element name="order-items">  *&lt;complexType>  *&lt;complexContent>  *&lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">  *&lt;sequence>  *&lt;element name="item" maxOccurs="unbounded" minOccurs="0">  *&lt;complexType>  *&lt;complexContent>  *&lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">  *&lt;sequence>  *&lt;element name="price" type="{http://www.w3.org/2001/XMLSchema}float"/>  *&lt;element name="quantity" type="{http://www.w3.org/2001/XMLSchema}short"/>  *&lt;/sequence>  *&lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}string" />  *&lt;/restriction>  *&lt;/complexContent>  *&lt;/complexType>  *&lt;/element>  *&lt;/sequence>  *&lt;/restriction>  *&lt;/complexContent>  *&lt;/complexType>  *&lt;/element>  *&lt;/sequence>  *&lt;/restriction>  *&lt;/complexContent>  *&lt;/complexType>  *</pre>  *   */
end_comment

begin_class
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
annotation|@
name|XmlType
argument_list|(
name|name
operator|=
literal|""
argument_list|,
name|propOrder
operator|=
block|{
literal|"header"
block|,
literal|"orderItems"
block|}
argument_list|)
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"ABCOrder"
argument_list|)
DECL|class|ABCOrder
specifier|public
class|class
name|ABCOrder
block|{
annotation|@
name|XmlElement
argument_list|(
name|required
operator|=
literal|true
argument_list|)
DECL|field|header
specifier|protected
name|ABCOrder
operator|.
name|Header
name|header
decl_stmt|;
annotation|@
name|XmlElement
argument_list|(
name|name
operator|=
literal|"order-items"
argument_list|,
name|required
operator|=
literal|true
argument_list|)
DECL|field|orderItems
specifier|protected
name|ABCOrder
operator|.
name|OrderItems
name|orderItems
decl_stmt|;
comment|/**      * Gets the value of the header property.      *       * @return      *     possible object is      *     {@link ABCOrder.Header }      *           */
DECL|method|getHeader ()
specifier|public
name|ABCOrder
operator|.
name|Header
name|getHeader
parameter_list|()
block|{
return|return
name|header
return|;
block|}
comment|/**      * Sets the value of the header property.      *       * @param value      *     allowed object is      *     {@link ABCOrder.Header }      *           */
DECL|method|setHeader (ABCOrder.Header value)
specifier|public
name|void
name|setHeader
parameter_list|(
name|ABCOrder
operator|.
name|Header
name|value
parameter_list|)
block|{
name|this
operator|.
name|header
operator|=
name|value
expr_stmt|;
block|}
comment|/**      * Gets the value of the orderItems property.      *       * @return      *     possible object is      *     {@link ABCOrder.OrderItems }      *           */
DECL|method|getOrderItems ()
specifier|public
name|ABCOrder
operator|.
name|OrderItems
name|getOrderItems
parameter_list|()
block|{
return|return
name|orderItems
return|;
block|}
comment|/**      * Sets the value of the orderItems property.      *       * @param value      *     allowed object is      *     {@link ABCOrder.OrderItems }      *           */
DECL|method|setOrderItems (ABCOrder.OrderItems value)
specifier|public
name|void
name|setOrderItems
parameter_list|(
name|ABCOrder
operator|.
name|OrderItems
name|value
parameter_list|)
block|{
name|this
operator|.
name|orderItems
operator|=
name|value
expr_stmt|;
block|}
comment|/**      *<p>Java class for anonymous complex type.      *       *<p>The following schema fragment specifies the expected content contained within this class.      *       *<pre>      *&lt;complexType>      *&lt;complexContent>      *&lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">      *&lt;sequence>      *&lt;element name="status" type="{http://www.w3.org/2001/XMLSchema}string"/>      *&lt;element name="customer-num" type="{http://www.w3.org/2001/XMLSchema}string"/>      *&lt;element name="order-num" type="{http://www.w3.org/2001/XMLSchema}string"/>      *&lt;/sequence>      *&lt;/restriction>      *&lt;/complexContent>      *&lt;/complexType>      *</pre>      *       *       */
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
annotation|@
name|XmlType
argument_list|(
name|name
operator|=
literal|""
argument_list|,
name|propOrder
operator|=
block|{
literal|"status"
block|,
literal|"customerNum"
block|,
literal|"orderNum"
block|}
argument_list|)
DECL|class|Header
specifier|public
specifier|static
class|class
name|Header
block|{
annotation|@
name|XmlElement
argument_list|(
name|required
operator|=
literal|true
argument_list|)
DECL|field|status
specifier|protected
name|String
name|status
decl_stmt|;
annotation|@
name|XmlElement
argument_list|(
name|name
operator|=
literal|"customer-num"
argument_list|,
name|required
operator|=
literal|true
argument_list|)
DECL|field|customerNum
specifier|protected
name|String
name|customerNum
decl_stmt|;
annotation|@
name|XmlElement
argument_list|(
name|name
operator|=
literal|"order-num"
argument_list|,
name|required
operator|=
literal|true
argument_list|)
DECL|field|orderNum
specifier|protected
name|String
name|orderNum
decl_stmt|;
comment|/**          * Gets the value of the status property.          *           * @return          *     possible object is          *     {@link String }          *               */
DECL|method|getStatus ()
specifier|public
name|String
name|getStatus
parameter_list|()
block|{
return|return
name|status
return|;
block|}
comment|/**          * Sets the value of the status property.          *           * @param value          *     allowed object is          *     {@link String }          *               */
DECL|method|setStatus (String value)
specifier|public
name|void
name|setStatus
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|this
operator|.
name|status
operator|=
name|value
expr_stmt|;
block|}
comment|/**          * Gets the value of the customerNum property.          *           * @return          *     possible object is          *     {@link String }          *               */
DECL|method|getCustomerNum ()
specifier|public
name|String
name|getCustomerNum
parameter_list|()
block|{
return|return
name|customerNum
return|;
block|}
comment|/**          * Sets the value of the customerNum property.          *           * @param value          *     allowed object is          *     {@link String }          *               */
DECL|method|setCustomerNum (String value)
specifier|public
name|void
name|setCustomerNum
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|this
operator|.
name|customerNum
operator|=
name|value
expr_stmt|;
block|}
comment|/**          * Gets the value of the orderNum property.          *           * @return          *     possible object is          *     {@link String }          *               */
DECL|method|getOrderNum ()
specifier|public
name|String
name|getOrderNum
parameter_list|()
block|{
return|return
name|orderNum
return|;
block|}
comment|/**          * Sets the value of the orderNum property.          *           * @param value          *     allowed object is          *     {@link String }          *               */
DECL|method|setOrderNum (String value)
specifier|public
name|void
name|setOrderNum
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|this
operator|.
name|orderNum
operator|=
name|value
expr_stmt|;
block|}
block|}
comment|/**      *<p>Java class for anonymous complex type.      *       *<p>The following schema fragment specifies the expected content contained within this class.      *       *<pre>      *&lt;complexType>      *&lt;complexContent>      *&lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">      *&lt;sequence>      *&lt;element name="item" maxOccurs="unbounded" minOccurs="0">      *&lt;complexType>      *&lt;complexContent>      *&lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">      *&lt;sequence>      *&lt;element name="price" type="{http://www.w3.org/2001/XMLSchema}float"/>      *&lt;element name="quantity" type="{http://www.w3.org/2001/XMLSchema}short"/>      *&lt;/sequence>      *&lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}string" />      *&lt;/restriction>      *&lt;/complexContent>      *&lt;/complexType>      *&lt;/element>      *&lt;/sequence>      *&lt;/restriction>      *&lt;/complexContent>      *&lt;/complexType>      *</pre>      *       *       */
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
annotation|@
name|XmlType
argument_list|(
name|name
operator|=
literal|""
argument_list|,
name|propOrder
operator|=
block|{
literal|"item"
block|}
argument_list|)
DECL|class|OrderItems
specifier|public
specifier|static
class|class
name|OrderItems
block|{
DECL|field|item
specifier|protected
name|List
argument_list|<
name|ABCOrder
operator|.
name|OrderItems
operator|.
name|Item
argument_list|>
name|item
decl_stmt|;
comment|/**          * Gets the value of the item property.          *           *<p>          * This accessor method returns a reference to the live list,          * not a snapshot. Therefore any modification you make to the          * returned list will be present inside the JAXB object.          * This is why there is not a<CODE>set</CODE> method for the item property.          *           *<p>          * For example, to add a new item, do as follows:          *<pre>          *    getItem().add(newItem);          *</pre>          *           *           *<p>          * Objects of the following type(s) are allowed in the list          * {@link ABCOrder.OrderItems.Item }          *           *           */
DECL|method|getItem ()
specifier|public
name|List
argument_list|<
name|ABCOrder
operator|.
name|OrderItems
operator|.
name|Item
argument_list|>
name|getItem
parameter_list|()
block|{
if|if
condition|(
name|item
operator|==
literal|null
condition|)
block|{
name|item
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
block|}
return|return
name|this
operator|.
name|item
return|;
block|}
comment|/**          *<p>Java class for anonymous complex type.          *           *<p>The following schema fragment specifies the expected content contained within this class.          *           *<pre>          *&lt;complexType>          *&lt;complexContent>          *&lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">          *&lt;sequence>          *&lt;element name="price" type="{http://www.w3.org/2001/XMLSchema}float"/>          *&lt;element name="quantity" type="{http://www.w3.org/2001/XMLSchema}short"/>          *&lt;/sequence>          *&lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}string" />          *&lt;/restriction>          *&lt;/complexContent>          *&lt;/complexType>          *</pre>          *           *           */
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
annotation|@
name|XmlType
argument_list|(
name|name
operator|=
literal|""
argument_list|,
name|propOrder
operator|=
block|{
literal|"price"
block|,
literal|"quantity"
block|}
argument_list|)
DECL|class|Item
specifier|public
specifier|static
class|class
name|Item
block|{
DECL|field|price
specifier|protected
name|float
name|price
decl_stmt|;
DECL|field|quantity
specifier|protected
name|short
name|quantity
decl_stmt|;
annotation|@
name|XmlAttribute
argument_list|(
name|name
operator|=
literal|"id"
argument_list|)
DECL|field|id
specifier|protected
name|String
name|id
decl_stmt|;
comment|/**              * Gets the value of the price property.              *               */
DECL|method|getPrice ()
specifier|public
name|float
name|getPrice
parameter_list|()
block|{
return|return
name|price
return|;
block|}
comment|/**              * Sets the value of the price property.              *               */
DECL|method|setPrice (float value)
specifier|public
name|void
name|setPrice
parameter_list|(
name|float
name|value
parameter_list|)
block|{
name|this
operator|.
name|price
operator|=
name|value
expr_stmt|;
block|}
comment|/**              * Gets the value of the quantity property.              *               */
DECL|method|getQuantity ()
specifier|public
name|short
name|getQuantity
parameter_list|()
block|{
return|return
name|quantity
return|;
block|}
comment|/**              * Sets the value of the quantity property.              *               */
DECL|method|setQuantity (short value)
specifier|public
name|void
name|setQuantity
parameter_list|(
name|short
name|value
parameter_list|)
block|{
name|this
operator|.
name|quantity
operator|=
name|value
expr_stmt|;
block|}
comment|/**              * Gets the value of the id property.              *               * @return              *     possible object is              *     {@link String }              *                   */
DECL|method|getId ()
specifier|public
name|String
name|getId
parameter_list|()
block|{
return|return
name|id
return|;
block|}
comment|/**              * Sets the value of the id property.              *               * @param value              *     allowed object is              *     {@link String }              *                   */
DECL|method|setId (String value)
specifier|public
name|void
name|setId
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|this
operator|.
name|id
operator|=
name|value
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

