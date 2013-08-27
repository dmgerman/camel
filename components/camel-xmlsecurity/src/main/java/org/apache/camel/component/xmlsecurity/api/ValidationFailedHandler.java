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
name|XMLSignature
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
name|XMLSignatureException
import|;
end_import

begin_comment
comment|/**  * Handler for handling the case when the core signature validation fails or a  * {@link XMLSignatureException} occurs during calling  * {@link XMLSignature#validate(javax.xml.crypto.dsig.XMLValidateContext)}.  *   * This handler can be used to react in a specific way on validation failures.  * For example, the handler could write logs or may even ignore certain  * validation failures.  *<p>  * Typically the handler throws an exception when a validation failure occurs.  *   * There is a certain order how the methods are called.  *<ul>  *<li>First, the method {@link #start()} is called when a core validation  * fails.  *<li>Then {@link #signatureValueValidationFailed(SignatureValue)} is called if  * the signature validation fails.  *<li>Then, for each reference in the signed info whose validation fails  * {@link #referenceValidationFailed(Reference)} is called.  *<li>Then, for each reference in the manifests whose validation fails, the  * method {@link #manifestReferenceValidationFailed(Reference)} is called.  *<li>Then, the method {@link #ignoreCoreValidationFailure()} is called where  * you can finally decide whether the processing should go on or be interrupted.  *<li>It is ensured that the method {@link #end()} is called at the end of the  * validation, even if the methods called before have thrown an exception. This  * allows you to hold state between the start and end of the validation handling  * process.  *</ul>  * If you throw an exception then the validation checking is interrupted and  * after that only the {@link #end()} method is called in a finally block. Best  * practice is to interrupt the validation at the first occurrence of a  * validation error.  */
end_comment

begin_interface
DECL|interface|ValidationFailedHandler
specifier|public
interface|interface
name|ValidationFailedHandler
block|{
comment|/**      * Method called when an XMLSignatureException is thrown by the method      * {@link XMLSignature#validate(javax.xml.crypto.dsig.XMLValidateContext)}.      *<p>      * You can return more specific exceptions which are useful for your      * use-case.      *       * @param e exception      * @return exception exception which is then thrown by XmlSignerProcessor.      */
DECL|method|onXMLSignatureException (XMLSignatureException e)
name|Exception
name|onXMLSignatureException
parameter_list|(
name|XMLSignatureException
name|e
parameter_list|)
function_decl|;
DECL|method|start ()
name|void
name|start
parameter_list|()
function_decl|;
DECL|method|signatureValueValidationFailed (SignatureValue value)
name|void
name|signatureValueValidationFailed
parameter_list|(
name|SignatureValue
name|value
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|// NOPMD
DECL|method|referenceValidationFailed (Reference ref)
name|void
name|referenceValidationFailed
parameter_list|(
name|Reference
name|ref
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|// NOPMD
DECL|method|manifestReferenceValidationFailed (Reference ref)
name|void
name|manifestReferenceValidationFailed
parameter_list|(
name|Reference
name|ref
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|// NOPMD
comment|/**      * If<tt>true</tt> is returned then the verifier will go-on as if there was      * no validation failure. If<tt>false</tt> is returned than the verifier      * will throw an {@link XmlSignatureInvalidException}.      *<p>      * Best practice is to return<code>false</code> to ensure that after a core      * validation failure, the verification fails.      *       * @return true or false      * @throws Exception      */
DECL|method|ignoreCoreValidationFailure ()
name|boolean
name|ignoreCoreValidationFailure
parameter_list|()
throws|throws
name|Exception
function_decl|;
comment|// NOPMD
DECL|method|end ()
name|void
name|end
parameter_list|()
throws|throws
name|Exception
function_decl|;
comment|// NOPMD
block|}
end_interface

end_unit

