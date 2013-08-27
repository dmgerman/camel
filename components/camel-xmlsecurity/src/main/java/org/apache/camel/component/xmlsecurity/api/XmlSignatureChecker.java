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
name|SignedInfo
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
name|javax
operator|.
name|xml
operator|.
name|crypto
operator|.
name|dsig
operator|.
name|XMLSignature
operator|.
name|SignatureValue
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
comment|/**  * This interface gives the application the possibility to check whether the  * expected parts are signed.  *<p>  * Only relevant for the XML signature verifier.  *<p>  * See http://www.w3.org/TR/xmldsig-bestpractices/#check-what-is-signed  */
end_comment

begin_interface
DECL|interface|XmlSignatureChecker
specifier|public
interface|interface
name|XmlSignatureChecker
block|{
comment|/**      * Checks whether the signature document has the expected structure and      * contains the expected transformations and references. See      * http://www.w3.org/TR/xmldsig-bestpractices/#check-what-is-signed      *       * @param input      *            input parameters      * @throws Exception      *             when XML signature does not pass the check      */
DECL|method|checkBeforeCoreValidation (Input input)
name|void
name|checkBeforeCoreValidation
parameter_list|(
name|Input
name|input
parameter_list|)
throws|throws
name|Exception
function_decl|;
DECL|interface|Input
specifier|public
interface|interface
name|Input
block|{
comment|/** Signed info instance. */
DECL|method|getSignedInfo ()
name|SignedInfo
name|getSignedInfo
parameter_list|()
function_decl|;
comment|/** Signature value instance. */
DECL|method|getSignatureValue ()
name|SignatureValue
name|getSignatureValue
parameter_list|()
function_decl|;
comment|/** XML objects list. */
DECL|method|getObjects ()
name|List
argument_list|<
name|?
extends|extends
name|XMLObject
argument_list|>
name|getObjects
parameter_list|()
function_decl|;
comment|/** Key info. */
DECL|method|getKeyInfo ()
name|KeyInfo
name|getKeyInfo
parameter_list|()
function_decl|;
comment|/** Message body containing the XML signature as DOM. */
DECL|method|getMessageBodyDocument ()
name|Document
name|getMessageBodyDocument
parameter_list|()
function_decl|;
comment|/** Message. */
DECL|method|getMessage ()
name|Message
name|getMessage
parameter_list|()
function_decl|;
block|}
block|}
end_interface

end_unit

