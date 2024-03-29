begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.foo.bar
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|foo
operator|.
name|bar
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|ObjectHelper
import|;
end_import

begin_comment
comment|/**  *<p>Java class for PersonType complex type.  *   *<p>The following schema fragment specifies the expected content contained within this class.  *   *<pre>  *&lt;complexType name="PersonType">  *&lt;complexContent>  *&lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">  *&lt;sequence>  *&lt;element name="firstName" type="{http://www.w3.org/2001/XMLSchema}string"/>  *&lt;element name="lastName" type="{http://www.w3.org/2001/XMLSchema}string"/>  *&lt;/sequence>  *&lt;/restriction>  *&lt;/complexContent>  *&lt;/complexType>  *</pre>  *   */
end_comment

begin_class
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"Person"
argument_list|)
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
literal|"PersonType"
argument_list|,
name|propOrder
operator|=
block|{
literal|"firstName"
block|,
literal|"lastName"
block|}
argument_list|)
DECL|class|PersonType
specifier|public
class|class
name|PersonType
block|{
annotation|@
name|XmlElement
argument_list|(
name|required
operator|=
literal|true
argument_list|)
DECL|field|firstName
specifier|protected
name|String
name|firstName
decl_stmt|;
annotation|@
name|XmlElement
argument_list|(
name|required
operator|=
literal|true
argument_list|)
DECL|field|lastName
specifier|protected
name|String
name|lastName
decl_stmt|;
comment|/**      * Gets the value of the firstName property.      *       * @return      *     possible object is      *     {@link String }      *           */
DECL|method|getFirstName ()
specifier|public
name|String
name|getFirstName
parameter_list|()
block|{
return|return
name|firstName
return|;
block|}
comment|/**      * Sets the value of the firstName property.      *       * @param value      *     allowed object is      *     {@link String }      *           */
DECL|method|setFirstName (String value)
specifier|public
name|void
name|setFirstName
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|this
operator|.
name|firstName
operator|=
name|value
expr_stmt|;
block|}
comment|/**      * Gets the value of the lastName property.      *       * @return      *     possible object is      *     {@link String }      *           */
DECL|method|getLastName ()
specifier|public
name|String
name|getLastName
parameter_list|()
block|{
return|return
name|lastName
return|;
block|}
comment|/**      * Sets the value of the lastName property.      *       * @param value      *     allowed object is      *     {@link String }      *           */
DECL|method|setLastName (String value)
specifier|public
name|void
name|setLastName
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|this
operator|.
name|lastName
operator|=
name|value
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|equals (Object o)
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
if|if
condition|(
name|o
operator|instanceof
name|PersonType
condition|)
block|{
name|PersonType
name|that
init|=
operator|(
name|PersonType
operator|)
name|o
decl_stmt|;
return|return
name|ObjectHelper
operator|.
name|equal
argument_list|(
name|this
operator|.
name|firstName
argument_list|,
name|that
operator|.
name|firstName
argument_list|)
operator|&&
name|ObjectHelper
operator|.
name|equal
argument_list|(
name|this
operator|.
name|lastName
argument_list|,
name|that
operator|.
name|lastName
argument_list|)
return|;
block|}
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|hashCode ()
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|firstName
operator|.
name|hashCode
argument_list|()
operator|+
name|lastName
operator|.
name|hashCode
argument_list|()
operator|*
literal|100
return|;
block|}
block|}
end_class

end_unit

