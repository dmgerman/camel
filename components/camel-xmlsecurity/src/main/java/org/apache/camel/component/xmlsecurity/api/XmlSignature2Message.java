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
name|crypto
operator|.
name|dsig
operator|.
name|Reference
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
name|XMLObject
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
name|apache
operator|.
name|camel
operator|.
name|Message
import|;
end_import

begin_comment
comment|/**  * Used in the signature verifier to map the references and objects of the XML  * signature to the output message.  */
end_comment

begin_interface
DECL|interface|XmlSignature2Message
specifier|public
interface|interface
name|XmlSignature2Message
block|{
comment|/**      * Maps the references and objects of an XML signature to the camel message.      *       * @param input      *            input      * @param output      *            output message      * @throws Exception      */
DECL|method|mapToMessage (Input input, Message output)
name|void
name|mapToMessage
parameter_list|(
name|Input
name|input
parameter_list|,
name|Message
name|output
parameter_list|)
throws|throws
name|Exception
function_decl|;
DECL|interface|Input
specifier|public
interface|interface
name|Input
block|{
comment|/**          * Returns the references.          *           * @return list of references, cannot be<code>null</code>          */
DECL|method|getReferences ()
name|List
argument_list|<
name|Reference
argument_list|>
name|getReferences
parameter_list|()
function_decl|;
comment|/**          * Returns the objects.          *           * @return objects, cannot be<code>null</code>          */
DECL|method|getObjects ()
name|List
argument_list|<
name|XMLObject
argument_list|>
name|getObjects
parameter_list|()
function_decl|;
comment|/** Message body containing the XML signature as DOM. */
DECL|method|getMessageBodyDocument ()
name|Document
name|getMessageBodyDocument
parameter_list|()
function_decl|;
comment|/**          * Indicator whether XML declaration should be omitted. Configured in          * the endpoint URI.          *           * @return {@link Boolean#TRUE} if the XML declaration shall be omitted          *         in the output document.          */
DECL|method|omitXmlDeclaration ()
name|Boolean
name|omitXmlDeclaration
parameter_list|()
function_decl|;
comment|/**          * Output node search value for determining the node from the XML          * signature document which shall be set to the output message body.          */
DECL|method|getOutputNodeSearch ()
name|Object
name|getOutputNodeSearch
parameter_list|()
function_decl|;
comment|/**          * Search type. Which determines the class and meaning of          * {@link #getOutputNodeSearch()}.          */
DECL|method|getOutputNodeSearchType ()
name|String
name|getOutputNodeSearchType
parameter_list|()
function_decl|;
comment|/**          * Indicator whether the XML signature elements should be removed from          * the document set to the output message.          */
DECL|method|getRemoveSignatureElements ()
name|Boolean
name|getRemoveSignatureElements
parameter_list|()
function_decl|;
block|}
block|}
end_interface

end_unit

