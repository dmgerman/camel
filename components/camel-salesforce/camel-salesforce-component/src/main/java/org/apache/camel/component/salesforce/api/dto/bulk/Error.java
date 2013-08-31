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
comment|/**  *<p>Java class for Error complex type.  *<p/>  *<p>The following schema fragment specifies the expected content contained within this class.  *<p/>  *<pre>  *&lt;complexType name="Error">  *&lt;complexContent>  *&lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">  *&lt;sequence>  *&lt;element name="exceptionCode" type="{http://www.w3.org/2001/XMLSchema}string"/>  *&lt;element name="exceptionMessage" type="{http://www.w3.org/2001/XMLSchema}string"/>  *&lt;/sequence>  *&lt;/restriction>  *&lt;/complexContent>  *&lt;/complexType>  *</pre>  */
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
literal|"Error"
argument_list|,
name|propOrder
operator|=
block|{
literal|"exceptionCode"
block|,
literal|"exceptionMessage"
block|}
argument_list|)
DECL|class|Error
specifier|public
class|class
name|Error
block|{
annotation|@
name|XmlElement
argument_list|(
name|required
operator|=
literal|true
argument_list|)
DECL|field|exceptionCode
specifier|protected
name|String
name|exceptionCode
decl_stmt|;
annotation|@
name|XmlElement
argument_list|(
name|required
operator|=
literal|true
argument_list|)
DECL|field|exceptionMessage
specifier|protected
name|String
name|exceptionMessage
decl_stmt|;
comment|/**      * Gets the value of the exceptionCode property.      *      * @return possible object is      *         {@link String }      */
DECL|method|getExceptionCode ()
specifier|public
name|String
name|getExceptionCode
parameter_list|()
block|{
return|return
name|exceptionCode
return|;
block|}
comment|/**      * Sets the value of the exceptionCode property.      *      * @param value allowed object is      *              {@link String }      */
DECL|method|setExceptionCode (String value)
specifier|public
name|void
name|setExceptionCode
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|this
operator|.
name|exceptionCode
operator|=
name|value
expr_stmt|;
block|}
comment|/**      * Gets the value of the exceptionMessage property.      *      * @return possible object is      *         {@link String }      */
DECL|method|getExceptionMessage ()
specifier|public
name|String
name|getExceptionMessage
parameter_list|()
block|{
return|return
name|exceptionMessage
return|;
block|}
comment|/**      * Sets the value of the exceptionMessage property.      *      * @param value allowed object is      *              {@link String }      */
DECL|method|setExceptionMessage (String value)
specifier|public
name|void
name|setExceptionMessage
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|this
operator|.
name|exceptionMessage
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

