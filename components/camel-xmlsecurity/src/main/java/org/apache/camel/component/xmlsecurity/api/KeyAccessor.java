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

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|crypto
operator|.
name|KeySelector
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|crypto
operator|.
name|dsig
operator|.
name|keyinfo
operator|.
name|KeyInfo
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|crypto
operator|.
name|dsig
operator|.
name|keyinfo
operator|.
name|KeyInfoFactory
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
name|w3c
operator|.
name|dom
operator|.
name|Node
import|;
end_import

begin_comment
comment|/**  * Returns the key selector and the optional KeyInfo instance for signing an XML  * document. There is a default implementation {@link DefaultKeySelector}.  *<p>  * The XML signature generator will first call {@link #getKeySelector(Message)}  * and then {@link KeyAccessor#getKeyInfo(Message, Node, KeyInfoFactory)}.  */
end_comment

begin_interface
DECL|interface|KeyAccessor
specifier|public
interface|interface
name|KeyAccessor
block|{
comment|/**      * Returns the key selector which determines the key for signing the XML      * document. The method is called every time a XML document is signed.      *       * If<code>null</code> is returned the XML signature generator will throw a      * {@link XmlSignatureNoKeyException}.      *       * @param message      *            the incoming message, from which you can read headers to      *            configure the key selector, for example, a header could      *            contain a private key for the key selector      * @return key selector, must not be<code>null</code>      * @throws Exception      *             if an error occurs      */
DECL|method|getKeySelector (Message message)
name|KeySelector
name|getKeySelector
parameter_list|(
name|Message
name|message
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Returns the optional key info to be incorporated into the XML signature.      * If<code>null</code> is returned, no key info element is created. You can      * create a key info instance via the key info factory.      *       * @param message      *            incoming message, from which you can read headers, for      *            example, there could be a header which contains the public key      *            or certificate for the key info      * @param messageBody      *            the message body as DOM node. If the message body is plain      *            text then the node will be a text node. If the message body is      *            a XML document, then the node is the root element.      * @param keyInfoFactory      *            key info factory for creating the KeyInfo instance      * @return key info, can be<code>null</code>      * @throws Exception      *             if an error occurs      */
DECL|method|getKeyInfo (Message message, Node messageBody, KeyInfoFactory keyInfoFactory)
name|KeyInfo
name|getKeyInfo
parameter_list|(
name|Message
name|message
parameter_list|,
name|Node
name|messageBody
parameter_list|,
name|KeyInfoFactory
name|keyInfoFactory
parameter_list|)
throws|throws
name|Exception
function_decl|;
block|}
end_interface

end_unit

