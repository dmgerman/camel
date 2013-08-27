begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.xmlsecurity.api
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|xmlsecurity
operator|.
name|api
package|;
end_package

begin_class
DECL|class|XmlSignatureConstants
specifier|public
specifier|final
class|class
name|XmlSignatureConstants
block|{
comment|/**      * Header for indicating that the message body contains non-xml plain text.      * This header is used in the XML signature generator. If the value is set      * to {@link Boolean#TRUE} then the message body is treated as plain text      * Overwrites the configuration parameter      * XmlSignerConfiguration#setPlainText(Boolean)      */
DECL|field|HEADER_MESSAGE_IS_PLAIN_TEXT
specifier|public
specifier|static
specifier|final
name|String
name|HEADER_MESSAGE_IS_PLAIN_TEXT
init|=
literal|"CamelXmlSignatureMessageIsPlainText"
decl_stmt|;
comment|/**      * Header indicating the encoding of the plain text message body. Used in      * the XML signature generator if the header      * {@link #HEADER_MESSAGE_IS_PLAIN_TEXT} is set to {@link Boolean#TRUE}.      * Overwrites the configuration parameter      * XmlSignerConfiguration#setPlainTextEncoding(String).      */
DECL|field|HEADER_PLAIN_TEXT_ENCODING
specifier|public
specifier|static
specifier|final
name|String
name|HEADER_PLAIN_TEXT_ENCODING
init|=
literal|"CamelXmlSignaturePlainTextEncoding"
decl_stmt|;
comment|/**      * Header which indicates that either the resulting signature document in      * the signature generation case or the resulting output of the verifier      * should not contain an XML declaration. If the header is not specified      * then a XML declaration is created.      *<p>      * There is one exception: If the verifier result is a plain text this      * header has no effect.      *<p>      * Possible values of the header are {@link Boolean#TRUE} or      * {@link Boolean#FALSE}.      *<p>      * Overwrites the configuration parameter      * XmlSignatureConfiguration#setOmitXmlDeclaration(Boolean).      *       */
DECL|field|HEADER_OMIT_XML_DECLARATION
specifier|public
specifier|static
specifier|final
name|String
name|HEADER_OMIT_XML_DECLARATION
init|=
literal|"CamelXmlSignatureOmitXmlDeclaration"
decl_stmt|;
DECL|field|HEADER_CONTENT_REFERENCE_URI
specifier|public
specifier|static
specifier|final
name|String
name|HEADER_CONTENT_REFERENCE_URI
init|=
literal|"CamelXmlSignatureContentReferenceUri"
decl_stmt|;
DECL|field|HEADER_CONTENT_REFERENCE_TYPE
specifier|public
specifier|static
specifier|final
name|String
name|HEADER_CONTENT_REFERENCE_TYPE
init|=
literal|"CamelXmlSignatureContentReferenceType"
decl_stmt|;
DECL|method|XmlSignatureConstants ()
specifier|private
name|XmlSignatureConstants
parameter_list|()
block|{
comment|// no instance
block|}
block|}
end_class

end_unit

