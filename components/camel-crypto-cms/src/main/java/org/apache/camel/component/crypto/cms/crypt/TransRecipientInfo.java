begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.crypto.cms.crypt
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
name|crypt
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
name|exception
operator|.
name|CryptoCmsException
import|;
end_import

begin_comment
comment|/**  * Information about the receiver of an encrypted message used in  * CmsEnvelopedDataEncryptor.  *<p>  * Represents the "key transport" recipient info alternative: The  * content-encryption key is encrypted with the public key of the recipient.  * This technique is compatible to PKCS#7 when creating a RecipientInfo for the  * public key of the recipient's certificate, identified by issuer and serial  * number. CMS recommends to use RSA for encrypting the content encryption key.  */
end_comment

begin_interface
DECL|interface|TransRecipientInfo
specifier|public
interface|interface
name|TransRecipientInfo
extends|extends
name|RecipientInfo
block|{
comment|/** Currently, the key encryption algorithm is fixed to "RSA". */
DECL|method|getKeyEncryptionAlgorithm (Exchange exchange)
name|String
name|getKeyEncryptionAlgorithm
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|CryptoCmsException
function_decl|;
comment|/**      * Returns the certificate containign the public key which is used for the      * encryption and the issuer and serial number which is added to the      * recipient information.      */
DECL|method|getCertificate (Exchange exchange)
name|X509Certificate
name|getCertificate
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|CryptoCmsException
function_decl|;
block|}
end_interface

end_unit

