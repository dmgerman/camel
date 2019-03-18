begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.model
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|model
package|;
end_package

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
name|XmlID
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
comment|/**  * The unique identifier for a bean. The scope of the identifier is the enclosing bean factory.  *<p>  * The following schema fragment specifies the expected content contained within this class.  *<pre>  *&lt;complexType name="identifiedType">  *&lt;complexContent>  *&lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">  *&lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}ID" />  *&lt;/restriction>  *&lt;/complexContent>  *&lt;/complexType>  *</pre>  */
end_comment

begin_class
annotation|@
name|XmlType
argument_list|(
name|name
operator|=
literal|"identifiedType"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|IdentifiedType
specifier|public
specifier|abstract
class|class
name|IdentifiedType
block|{
annotation|@
name|XmlAttribute
annotation|@
name|XmlID
DECL|field|id
specifier|private
name|String
name|id
decl_stmt|;
comment|/**      * Gets the value of the id property.      */
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
comment|/**      * Sets the value of the id property.      */
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
end_class

end_unit

