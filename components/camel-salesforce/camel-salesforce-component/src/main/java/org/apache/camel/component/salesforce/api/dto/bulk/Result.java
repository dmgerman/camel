begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|XmlType
import|;
end_import

begin_comment
comment|/**  *<p>Java class for Result complex type.  *<p/>  *<p>The following schema fragment specifies the expected content contained within this class.  *<p/>  *<pre>  *&lt;complexType name="Result">  *&lt;complexContent>  *&lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">  *&lt;sequence>  *&lt;element name="errors" type="{http://www.force.com/2009/06/asyncapi/dataload}ResultError" maxOccurs="unbounded" minOccurs="0"/>  *&lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>  *&lt;element name="success" type="{http://www.w3.org/2001/XMLSchema}boolean"/>  *&lt;element name="created" type="{http://www.w3.org/2001/XMLSchema}boolean"/>  *&lt;/sequence>  *&lt;/restriction>  *&lt;/complexContent>  *&lt;/complexType>  *</pre>  */
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
literal|"Result"
argument_list|,
name|propOrder
operator|=
block|{
literal|"errors"
block|,
literal|"id"
block|,
literal|"success"
block|,
literal|"created"
block|}
argument_list|)
DECL|class|Result
specifier|public
class|class
name|Result
block|{
DECL|field|errors
specifier|protected
name|List
argument_list|<
name|ResultError
argument_list|>
name|errors
decl_stmt|;
DECL|field|id
specifier|protected
name|String
name|id
decl_stmt|;
DECL|field|success
specifier|protected
name|boolean
name|success
decl_stmt|;
DECL|field|created
specifier|protected
name|boolean
name|created
decl_stmt|;
comment|/**      * Gets the value of the errors property.      *<p/>      *<p/>      * This accessor method returns a reference to the live list,      * not a snapshot. Therefore any modification you make to the      * returned list will be present inside the JAXB object.      * This is why there is not a<CODE>set</CODE> method for the errors property.      *<p/>      *<p/>      * For example, to add a new item, do as follows:      *<pre>      *    getErrors().add(newItem);      *</pre>      *<p/>      *<p/>      *<p/>      * Objects of the following type(s) are allowed in the list      * {@link ResultError }      */
DECL|method|getErrors ()
specifier|public
name|List
argument_list|<
name|ResultError
argument_list|>
name|getErrors
parameter_list|()
block|{
if|if
condition|(
name|errors
operator|==
literal|null
condition|)
block|{
name|errors
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
name|errors
return|;
block|}
comment|/**      * Gets the value of the id property.      *      * @return possible object is      *         {@link String }      */
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
comment|/**      * Sets the value of the id property.      *      * @param value allowed object is      *              {@link String }      */
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
comment|/**      * Gets the value of the success property.      */
DECL|method|isSuccess ()
specifier|public
name|boolean
name|isSuccess
parameter_list|()
block|{
return|return
name|success
return|;
block|}
comment|/**      * Sets the value of the success property.      */
DECL|method|setSuccess (boolean value)
specifier|public
name|void
name|setSuccess
parameter_list|(
name|boolean
name|value
parameter_list|)
block|{
name|this
operator|.
name|success
operator|=
name|value
expr_stmt|;
block|}
comment|/**      * Gets the value of the created property.      */
DECL|method|isCreated ()
specifier|public
name|boolean
name|isCreated
parameter_list|()
block|{
return|return
name|created
return|;
block|}
comment|/**      * Sets the value of the created property.      */
DECL|method|setCreated (boolean value)
specifier|public
name|void
name|setCreated
parameter_list|(
name|boolean
name|value
parameter_list|)
block|{
name|this
operator|.
name|created
operator|=
name|value
expr_stmt|;
block|}
block|}
end_class

end_unit

