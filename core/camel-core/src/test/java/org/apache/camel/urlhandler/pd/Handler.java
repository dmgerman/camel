begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.urlhandler.pd
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|urlhandler
operator|.
name|pd
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URLConnection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URLStreamHandler
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|charset
operator|.
name|StandardCharsets
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_comment
comment|/**  * Class to simulate a change of the XSD document. During the first call of the  * resource a XSD is returned which does not fit to the XML document. In the  * second call a XSD fitting to the XML document is returned. Used in  * org.apache.camel.component.validator.ValidatorEndpointClearCachedSchemaTest  */
end_comment

begin_class
DECL|class|Handler
specifier|public
class|class
name|Handler
extends|extends
name|URLStreamHandler
block|{
DECL|field|counter
specifier|private
specifier|static
name|int
name|counter
decl_stmt|;
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|Handler
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|xsdtemplate1
specifier|private
specifier|final
name|String
name|xsdtemplate1
init|=
literal|"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
operator|+
comment|//
literal|"<xsd:schema targetNamespace=\"http://apache.camel.org/test\" xmlns=\"http://apache.camel.org/test\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\">"
operator|+
comment|//
literal|"<xsd:complexType name=\"TestMessage\">"
operator|+
comment|//
literal|"<xsd:sequence>"
operator|+
comment|//
literal|"<xsd:element name=\"Content\" type=\"xsd:string\" />"
operator|+
comment|// //
comment|// wrong
comment|// element
comment|// name
comment|// will
comment|// cause
comment|// the
comment|// validation
comment|// to
comment|// fail
literal|"</xsd:sequence>"
operator|+
comment|//
literal|"<xsd:attribute name=\"attr\" type=\"xsd:string\" default=\"xsd1\"/>"
operator|+
comment|//
literal|"</xsd:complexType>"
operator|+
comment|//
literal|"<xsd:element name=\"TestMessage\" type=\"TestMessage\" />"
operator|+
comment|//
literal|"</xsd:schema>"
decl_stmt|;
comment|//
DECL|field|xsdtemplate2
specifier|private
specifier|final
name|String
name|xsdtemplate2
init|=
name|xsdtemplate1
operator|.
name|replace
argument_list|(
literal|"\"Content\""
argument_list|,
literal|"\"MessageContent\""
argument_list|)
decl_stmt|;
comment|// correct
comment|// element
comment|// name
comment|// -->
comment|// validation
comment|// will
comment|// be
comment|// correct
DECL|field|xsd1
specifier|private
name|byte
index|[]
name|xsd1
init|=
name|xsdtemplate1
operator|.
name|getBytes
argument_list|(
name|StandardCharsets
operator|.
name|UTF_8
argument_list|)
decl_stmt|;
DECL|field|xsd2
specifier|private
name|byte
index|[]
name|xsd2
init|=
name|xsdtemplate2
operator|.
name|getBytes
argument_list|(
name|StandardCharsets
operator|.
name|UTF_8
argument_list|)
decl_stmt|;
annotation|@
name|Override
DECL|method|openConnection (URL u)
specifier|protected
name|URLConnection
name|openConnection
parameter_list|(
name|URL
name|u
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|getCounter
argument_list|()
operator|==
literal|0
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"resolved XSD1"
argument_list|)
expr_stmt|;
name|incrementCounter
argument_list|()
expr_stmt|;
return|return
operator|new
name|URLConnection
argument_list|(
name|u
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|void
name|connect
parameter_list|()
throws|throws
name|IOException
block|{
name|connected
operator|=
literal|true
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|InputStream
name|getInputStream
parameter_list|()
throws|throws
name|IOException
block|{
return|return
operator|new
name|ByteArrayInputStream
argument_list|(
name|xsd1
argument_list|)
return|;
block|}
block|}
return|;
block|}
else|else
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"resolved XSD2"
argument_list|)
expr_stmt|;
name|incrementCounter
argument_list|()
expr_stmt|;
return|return
operator|new
name|URLConnection
argument_list|(
name|u
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|void
name|connect
parameter_list|()
throws|throws
name|IOException
block|{
name|connected
operator|=
literal|true
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|InputStream
name|getInputStream
parameter_list|()
throws|throws
name|IOException
block|{
return|return
operator|new
name|ByteArrayInputStream
argument_list|(
name|xsd2
argument_list|)
return|;
block|}
block|}
return|;
block|}
block|}
DECL|method|incrementCounter ()
specifier|public
specifier|static
specifier|synchronized
name|void
name|incrementCounter
parameter_list|()
block|{
name|counter
operator|++
expr_stmt|;
block|}
DECL|method|getCounter ()
specifier|public
specifier|static
specifier|synchronized
name|int
name|getCounter
parameter_list|()
block|{
return|return
name|counter
return|;
block|}
block|}
end_class

end_unit

