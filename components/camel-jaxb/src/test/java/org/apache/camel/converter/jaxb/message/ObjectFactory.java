begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.converter.jaxb.message
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|converter
operator|.
name|jaxb
operator|.
name|message
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
name|JAXBElement
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
name|XmlElementDecl
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
name|XmlRegistry
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|namespace
operator|.
name|QName
import|;
end_import

begin_comment
comment|/**  * This object contains factory methods for each  * Java content interface and Java element interface  * generated in the org.apache.camel.converter.jaxb.message package.  *<p>An ObjectFactory allows you to programatically  * construct new instances of the Java representation  * for XML content. The Java representation of XML  * content can consist of schema derived interfaces  * and classes representing the binding of schema  * type definitions, element declarations and model  * groups.  Factory methods for each of these are  * provided in this class.  */
end_comment

begin_class
annotation|@
name|XmlRegistry
DECL|class|ObjectFactory
specifier|public
class|class
name|ObjectFactory
block|{
DECL|field|MESSAGE_QNAME
specifier|private
specifier|static
specifier|final
name|QName
name|MESSAGE_QNAME
init|=
operator|new
name|QName
argument_list|(
literal|"message.jaxb.converter.camel.apache.org"
argument_list|,
literal|"message"
argument_list|)
decl_stmt|;
comment|/**      * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.apache.camel.converter.jaxb.message      */
DECL|method|ObjectFactory ()
specifier|public
name|ObjectFactory
parameter_list|()
block|{     }
comment|/**      * Create an instance of {@link Message }      */
DECL|method|createMessage ()
specifier|public
name|Message
name|createMessage
parameter_list|()
block|{
return|return
operator|new
name|Message
argument_list|()
return|;
block|}
annotation|@
name|XmlElementDecl
argument_list|(
name|namespace
operator|=
literal|"message.jaxb.converter.camel.apache.org"
argument_list|,
name|name
operator|=
literal|"message"
argument_list|)
DECL|method|createMessage (Message value)
specifier|public
name|JAXBElement
argument_list|<
name|Message
argument_list|>
name|createMessage
parameter_list|(
name|Message
name|value
parameter_list|)
block|{
return|return
operator|new
name|JAXBElement
argument_list|<>
argument_list|(
name|MESSAGE_QNAME
argument_list|,
name|Message
operator|.
name|class
argument_list|,
literal|null
argument_list|,
name|value
argument_list|)
return|;
block|}
block|}
end_class

end_unit

