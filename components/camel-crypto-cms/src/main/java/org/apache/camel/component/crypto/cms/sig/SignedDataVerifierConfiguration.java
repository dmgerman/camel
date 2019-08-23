begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.crypto.cms.sig
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|crypto
operator|.
name|cms
operator|.
name|sig
package|;
end_package

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|cert
operator|.
name|X509Certificate
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
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
name|component
operator|.
name|crypto
operator|.
name|cms
operator|.
name|common
operator|.
name|CryptoCmsConstants
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
name|crypto
operator|.
name|cms
operator|.
name|common
operator|.
name|CryptoCmsUnMarshallerConfiguration
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
name|crypto
operator|.
name|cms
operator|.
name|exception
operator|.
name|CryptoCmsException
import|;
end_import

begin_interface
DECL|interface|SignedDataVerifierConfiguration
specifier|public
interface|interface
name|SignedDataVerifierConfiguration
extends|extends
name|CryptoCmsUnMarshallerConfiguration
block|{
comment|/**      * Indicates whether the value in the Signed Data header (given by      * {@link CryptoCmsConstants#CAMEL_CRYPTO_CMS_SIGNED_DATA} is base64      * encoded.      */
DECL|method|isSignedDataHeaderBase64 ()
name|boolean
name|isSignedDataHeaderBase64
parameter_list|()
function_decl|;
comment|/**      * If<code>true</code> then the signatures of all signers are checked. If      *<code>false</code> then the verifier searches for a signer which matches      * with one of the specified certificates and verifies only the signature of      * the first found signer.      */
DECL|method|isVerifySignaturesOfAllSigners ()
name|boolean
name|isVerifySignaturesOfAllSigners
parameter_list|()
function_decl|;
comment|/**      * Returns the collection of certificates whose public keys are used to      * verify the signatures contained in the Signed Data object if the      * certificates match the signer information given in the Signed Data      * object.      */
DECL|method|getCertificates (Exchange exchange)
name|Collection
argument_list|<
name|X509Certificate
argument_list|>
name|getCertificates
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|CryptoCmsException
function_decl|;
comment|/** Creates a copy of this instance. For example by cloning. */
DECL|method|copy ()
name|SignedDataVerifierConfiguration
name|copy
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

