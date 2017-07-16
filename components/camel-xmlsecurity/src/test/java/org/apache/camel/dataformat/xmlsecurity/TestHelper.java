begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.dataformat.xmlsecurity
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|dataformat
operator|.
name|xmlsecurity
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
name|InputStream
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|crypto
operator|.
name|Cipher
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|crypto
operator|.
name|SecretKey
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|crypto
operator|.
name|spec
operator|.
name|SecretKeySpec
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
name|CamelContext
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
name|Exchange
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
name|Message
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
name|Processor
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
name|ProducerTemplate
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
name|component
operator|.
name|mock
operator|.
name|MockEndpoint
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
name|converter
operator|.
name|jaxp
operator|.
name|XmlConverter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|xml
operator|.
name|security
operator|.
name|encryption
operator|.
name|XMLCipher
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|xml
operator|.
name|security
operator|.
name|encryption
operator|.
name|XMLEncryptionException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
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

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Document
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|NodeList
import|;
end_import

begin_import
import|import
name|org
operator|.
name|xmlunit
operator|.
name|builder
operator|.
name|DiffBuilder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|xmlunit
operator|.
name|diff
operator|.
name|Diff
import|;
end_import

begin_class
DECL|class|TestHelper
specifier|public
class|class
name|TestHelper
block|{
DECL|field|NS_XML_FRAGMENT
specifier|protected
specifier|static
specifier|final
name|String
name|NS_XML_FRAGMENT
init|=
literal|"<ns1:cheesesites xmlns:ns1=\"http://cheese.xmlsecurity.camel.apache.org/\">"
operator|+
literal|"<netherlands>"
operator|+
literal|"<source>cow</source>"
operator|+
literal|"<cheese>gouda</cheese>"
operator|+
literal|"</netherlands>"
operator|+
literal|"<italy>"
operator|+
literal|"<source>cow</source>"
operator|+
literal|"<cheese>gorgonzola</cheese>"
operator|+
literal|"</italy>"
operator|+
literal|"<france>"
operator|+
literal|"<source>goat</source>"
operator|+
literal|"<cheese>brie</cheese>"
operator|+
literal|"</france>"
operator|+
literal|"</ns1:cheesesites>"
decl_stmt|;
DECL|field|XML_FRAGMENT
specifier|protected
specifier|static
specifier|final
name|String
name|XML_FRAGMENT
init|=
literal|"<cheesesites>"
operator|+
literal|"<netherlands>"
operator|+
literal|"<source>cow</source>"
operator|+
literal|"<cheese>gouda</cheese>"
operator|+
literal|"</netherlands>"
operator|+
literal|"<italy>"
operator|+
literal|"<source>cow</source>"
operator|+
literal|"<cheese>gorgonzola</cheese>"
operator|+
literal|"</italy>"
operator|+
literal|"<france>"
operator|+
literal|"<source>goat</source>"
operator|+
literal|"<cheese>brie</cheese>"
operator|+
literal|"</france>"
operator|+
literal|"</cheesesites>"
decl_stmt|;
DECL|field|HAS_3DES
specifier|static
specifier|final
name|boolean
name|HAS_3DES
decl_stmt|;
static|static
block|{
name|boolean
name|ok
init|=
literal|false
decl_stmt|;
try|try
block|{
name|org
operator|.
name|apache
operator|.
name|xml
operator|.
name|security
operator|.
name|Init
operator|.
name|init
argument_list|()
expr_stmt|;
name|XMLCipher
operator|.
name|getInstance
argument_list|(
name|XMLCipher
operator|.
name|TRIPLEDES_KeyWrap
argument_list|)
expr_stmt|;
name|ok
operator|=
literal|true
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|XMLEncryptionException
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
name|HAS_3DES
operator|=
name|ok
expr_stmt|;
block|}
DECL|field|UNRESTRICTED_POLICIES_INSTALLED
specifier|static
specifier|final
name|boolean
name|UNRESTRICTED_POLICIES_INSTALLED
decl_stmt|;
static|static
block|{
name|boolean
name|ok
init|=
literal|false
decl_stmt|;
try|try
block|{
name|byte
index|[]
name|data
init|=
block|{
literal|0x00
block|,
literal|0x01
block|,
literal|0x02
block|,
literal|0x03
block|,
literal|0x04
block|,
literal|0x05
block|,
literal|0x06
block|,
literal|0x07
block|}
decl_stmt|;
name|SecretKey
name|key192
init|=
operator|new
name|SecretKeySpec
argument_list|(
operator|new
name|byte
index|[]
block|{
literal|0x00
block|,
literal|0x01
block|,
literal|0x02
block|,
literal|0x03
block|,
literal|0x04
block|,
literal|0x05
block|,
literal|0x06
block|,
literal|0x07
block|,
literal|0x08
block|,
literal|0x09
block|,
literal|0x0a
block|,
literal|0x0b
block|,
literal|0x0c
block|,
literal|0x0d
block|,
literal|0x0e
block|,
literal|0x0f
block|,
literal|0x10
block|,
literal|0x11
block|,
literal|0x12
block|,
literal|0x13
block|,
literal|0x14
block|,
literal|0x15
block|,
literal|0x16
block|,
literal|0x17
block|}
argument_list|,
literal|"AES"
argument_list|)
decl_stmt|;
name|Cipher
name|c
init|=
name|Cipher
operator|.
name|getInstance
argument_list|(
literal|"AES"
argument_list|)
decl_stmt|;
name|c
operator|.
name|init
argument_list|(
name|Cipher
operator|.
name|ENCRYPT_MODE
argument_list|,
name|key192
argument_list|)
expr_stmt|;
name|c
operator|.
name|doFinal
argument_list|(
name|data
argument_list|)
expr_stmt|;
name|ok
operator|=
literal|true
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|//
block|}
name|UNRESTRICTED_POLICIES_INSTALLED
operator|=
name|ok
expr_stmt|;
block|}
DECL|field|log
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|TestHelper
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|sendText (final String fragment, CamelContext context)
specifier|protected
name|void
name|sendText
parameter_list|(
specifier|final
name|String
name|fragment
parameter_list|,
name|CamelContext
name|context
parameter_list|)
throws|throws
name|Exception
block|{
name|ProducerTemplate
name|template
init|=
name|context
operator|.
name|createProducerTemplate
argument_list|()
decl_stmt|;
name|template
operator|.
name|start
argument_list|()
expr_stmt|;
name|template
operator|.
name|send
argument_list|(
literal|"direct:start"
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
comment|// Set the property of the charset encoding
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|CHARSET_NAME
argument_list|,
literal|"UTF-8"
argument_list|)
expr_stmt|;
name|Message
name|in
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|in
operator|.
name|setBody
argument_list|(
name|fragment
argument_list|)
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"xmlFragment: {}"
argument_list|,
name|fragment
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|testEncryption (String fragment, CamelContext context)
specifier|protected
name|Document
name|testEncryption
parameter_list|(
name|String
name|fragment
parameter_list|,
name|CamelContext
name|context
parameter_list|)
throws|throws
name|Exception
block|{
name|MockEndpoint
name|resultEndpoint
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"mock:encrypted"
argument_list|,
name|MockEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|resultEndpoint
operator|.
name|setExpectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|sendText
argument_list|(
name|fragment
argument_list|,
name|context
argument_list|)
expr_stmt|;
name|resultEndpoint
operator|.
name|assertIsSatisfied
argument_list|(
literal|100
argument_list|)
expr_stmt|;
name|Exchange
name|exchange
init|=
name|resultEndpoint
operator|.
name|getExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|Document
name|inDoc
init|=
name|getDocumentForInMessage
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
if|if
condition|(
name|log
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|logMessage
argument_list|(
name|exchange
argument_list|,
name|inDoc
argument_list|)
expr_stmt|;
block|}
name|Assert
operator|.
name|assertTrue
argument_list|(
literal|"The XML message has no encrypted data."
argument_list|,
name|hasEncryptedData
argument_list|(
name|inDoc
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|inDoc
return|;
block|}
DECL|method|testEncryption (CamelContext context)
specifier|protected
name|void
name|testEncryption
parameter_list|(
name|CamelContext
name|context
parameter_list|)
throws|throws
name|Exception
block|{
name|testEncryption
argument_list|(
name|XML_FRAGMENT
argument_list|,
name|context
argument_list|)
expr_stmt|;
block|}
DECL|method|testDecryption (String fragment, CamelContext context)
specifier|protected
name|void
name|testDecryption
parameter_list|(
name|String
name|fragment
parameter_list|,
name|CamelContext
name|context
parameter_list|)
throws|throws
name|Exception
block|{
name|MockEndpoint
name|resultEndpoint
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"mock:decrypted"
argument_list|,
name|MockEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|resultEndpoint
operator|.
name|setExpectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
comment|// verify that the message was encrypted before checking that it is decrypted
name|testEncryption
argument_list|(
name|fragment
argument_list|,
name|context
argument_list|)
expr_stmt|;
name|resultEndpoint
operator|.
name|assertIsSatisfied
argument_list|(
literal|100
argument_list|)
expr_stmt|;
name|Exchange
name|exchange
init|=
name|resultEndpoint
operator|.
name|getExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|Document
name|inDoc
init|=
name|getDocumentForInMessage
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
if|if
condition|(
name|log
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|logMessage
argument_list|(
name|exchange
argument_list|,
name|inDoc
argument_list|)
expr_stmt|;
block|}
name|Assert
operator|.
name|assertFalse
argument_list|(
literal|"The XML message has encrypted data."
argument_list|,
name|hasEncryptedData
argument_list|(
name|inDoc
argument_list|)
argument_list|)
expr_stmt|;
comment|// verify that the decrypted message matches what was sent
name|Diff
name|xmlDiff
init|=
name|DiffBuilder
operator|.
name|compare
argument_list|(
name|fragment
argument_list|)
operator|.
name|withTest
argument_list|(
name|inDoc
argument_list|)
operator|.
name|checkForIdentical
argument_list|()
operator|.
name|build
argument_list|()
decl_stmt|;
name|Assert
operator|.
name|assertFalse
argument_list|(
literal|"The decrypted document does not match the control document:\n"
operator|+
name|xmlDiff
operator|.
name|toString
argument_list|()
argument_list|,
name|xmlDiff
operator|.
name|hasDifferences
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testDecryption (CamelContext context)
specifier|protected
name|void
name|testDecryption
parameter_list|(
name|CamelContext
name|context
parameter_list|)
throws|throws
name|Exception
block|{
name|testDecryption
argument_list|(
name|XML_FRAGMENT
argument_list|,
name|context
argument_list|)
expr_stmt|;
block|}
DECL|method|hasEncryptedData (Document doc)
specifier|private
name|boolean
name|hasEncryptedData
parameter_list|(
name|Document
name|doc
parameter_list|)
throws|throws
name|Exception
block|{
name|NodeList
name|nodeList
init|=
name|doc
operator|.
name|getElementsByTagNameNS
argument_list|(
literal|"http://www.w3.org/2001/04/xmlenc#"
argument_list|,
literal|"EncryptedData"
argument_list|)
decl_stmt|;
return|return
name|nodeList
operator|.
name|getLength
argument_list|()
operator|>
literal|0
return|;
block|}
DECL|method|logMessage (Exchange exchange, Document inDoc)
specifier|private
name|void
name|logMessage
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Document
name|inDoc
parameter_list|)
throws|throws
name|Exception
block|{
name|XmlConverter
name|converter
init|=
operator|new
name|XmlConverter
argument_list|()
decl_stmt|;
name|String
name|xmlStr
init|=
name|converter
operator|.
name|toString
argument_list|(
name|inDoc
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
name|log
operator|.
name|debug
argument_list|(
name|xmlStr
argument_list|)
expr_stmt|;
block|}
DECL|method|getDocumentForInMessage (Exchange exchange)
specifier|private
name|Document
name|getDocumentForInMessage
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|byte
index|[]
name|body
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|byte
index|[]
operator|.
expr|class
argument_list|)
decl_stmt|;
name|Document
name|d
init|=
name|createDocumentfromInputStream
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|body
argument_list|)
argument_list|,
name|exchange
operator|.
name|getContext
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|d
return|;
block|}
DECL|method|createDocumentfromInputStream (InputStream is, CamelContext context)
specifier|private
name|Document
name|createDocumentfromInputStream
parameter_list|(
name|InputStream
name|is
parameter_list|,
name|CamelContext
name|context
parameter_list|)
block|{
return|return
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|Document
operator|.
name|class
argument_list|,
name|is
argument_list|)
return|;
block|}
block|}
end_class

end_unit

