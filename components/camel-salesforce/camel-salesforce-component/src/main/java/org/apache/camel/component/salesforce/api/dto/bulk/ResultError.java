begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.salesforce.api.dto.bulk
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|salesforce
operator|.
name|api
operator|.
name|dto
operator|.
name|bulk
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
name|XmlType
import|;
end_import

begin_comment
comment|//CHECKSTYLE:OFF
end_comment

begin_comment
comment|/**  *<p>  * Java class for ResultError complex type.  *<p/>  *<p>  * The following schema fragment specifies the expected content contained within  * this class.  *<p/>  *   *<pre>  *&lt;complexType name="ResultError">  *&lt;complexContent>  *&lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">  *&lt;sequence>  *&lt;element name="fields" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>  *&lt;element name="message" type="{http://www.w3.org/2001/XMLSchema}string"/>  *&lt;element name="statusCode" type="{http://www.force.com/2009/06/asyncapi/dataload}StatusCode"/>  *&lt;/sequence>  *&lt;/restriction>  *&lt;/complexContent>  *&lt;/complexType>  *</pre>  */
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
literal|"ResultError"
argument_list|,
name|propOrder
operator|=
block|{
literal|"fields"
block|,
literal|"message"
block|,
literal|"statusCode"
block|}
argument_list|)
DECL|class|ResultError
specifier|public
class|class
name|ResultError
block|{
annotation|@
name|XmlElement
argument_list|(
name|nillable
operator|=
literal|true
argument_list|)
DECL|field|fields
specifier|protected
name|List
argument_list|<
name|String
argument_list|>
name|fields
decl_stmt|;
annotation|@
name|XmlElement
argument_list|(
name|required
operator|=
literal|true
argument_list|)
DECL|field|message
specifier|protected
name|String
name|message
decl_stmt|;
annotation|@
name|XmlElement
argument_list|(
name|required
operator|=
literal|true
argument_list|)
DECL|field|statusCode
specifier|protected
name|StatusCode
name|statusCode
decl_stmt|;
comment|/**      * Gets the value of the fields property.      *<p/>      *<p/>      * This accessor method returns a reference to the live list, not a      * snapshot. Therefore any modification you make to the returned list will      * be present inside the JAXB object. This is why there is not a      *<CODE>set</CODE> method for the fields property.      *<p/>      *<p/>      * For example, to add a new item, do as follows:      *       *<pre>      * getFields().add(newItem);      *</pre>      *<p/>      *<p/>      *<p/>      * Objects of the following type(s) are allowed in the list {@link String }      */
DECL|method|getFields ()
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getFields
parameter_list|()
block|{
if|if
condition|(
name|fields
operator|==
literal|null
condition|)
block|{
name|fields
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
name|fields
return|;
block|}
comment|/**      * Gets the value of the message property.      *      * @return possible object is {@link String }      */
DECL|method|getMessage ()
specifier|public
name|String
name|getMessage
parameter_list|()
block|{
return|return
name|message
return|;
block|}
comment|/**      * Sets the value of the message property.      *      * @param value allowed object is {@link String }      */
DECL|method|setMessage (String value)
specifier|public
name|void
name|setMessage
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|this
operator|.
name|message
operator|=
name|value
expr_stmt|;
block|}
comment|/**      * Gets the value of the statusCode property.      *      * @return possible object is {@link StatusCode }      */
DECL|method|getStatusCode ()
specifier|public
name|StatusCode
name|getStatusCode
parameter_list|()
block|{
return|return
name|statusCode
return|;
block|}
comment|/**      * Sets the value of the statusCode property.      *      * @param value allowed object is {@link StatusCode }      */
DECL|method|setStatusCode (StatusCode value)
specifier|public
name|void
name|setStatusCode
parameter_list|(
name|StatusCode
name|value
parameter_list|)
block|{
name|this
operator|.
name|statusCode
operator|=
name|value
expr_stmt|;
block|}
block|}
end_class

begin_comment
comment|//CHECKSTYLE:ON
end_comment

end_unit

