begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_comment
comment|// This file was generated by the JavaTM Architecture for XML Binding(JAXB)
end_comment

begin_comment
comment|// Reference Implementation, v2.2.8-b130911.1802
end_comment

begin_comment
comment|// See<a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
end_comment

begin_comment
comment|// Any modifications to this file will be lost upon recompilation of the source schema.
end_comment

begin_comment
comment|// Generated on: 2017.02.12 at 12:58:35 AM EET
end_comment

begin_comment
comment|//
end_comment

begin_package
DECL|package|net.javacrumbs.calc.model
package|package
name|net
operator|.
name|javacrumbs
operator|.
name|calc
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
comment|/**  * Java class for anonymous complex type.  *   *<p>The following schema fragment specifies the expected content contained within this class.  *   *<pre>  *&lt;complexType>  *&lt;complexContent>  *&lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">  *&lt;sequence>  *&lt;element name="result" type="{http://www.w3.org/2001/XMLSchema}int"/>  *&lt;/sequence>  *&lt;/restriction>  *&lt;/complexContent>  *&lt;/complexType>  *</pre>  *   *   */
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
literal|"result"
block|}
argument_list|)
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"plusResponse"
argument_list|)
DECL|class|PlusResponse
specifier|public
class|class
name|PlusResponse
block|{
DECL|field|result
specifier|protected
name|int
name|result
decl_stmt|;
comment|/**      * Gets the value of the result property.      *       */
DECL|method|getResult ()
specifier|public
name|int
name|getResult
parameter_list|()
block|{
return|return
name|result
return|;
block|}
comment|/**      * Sets the value of the result property.      *       */
DECL|method|setResult (int value)
specifier|public
name|void
name|setResult
parameter_list|(
name|int
name|value
parameter_list|)
block|{
name|this
operator|.
name|result
operator|=
name|value
expr_stmt|;
block|}
block|}
end_class

end_unit

