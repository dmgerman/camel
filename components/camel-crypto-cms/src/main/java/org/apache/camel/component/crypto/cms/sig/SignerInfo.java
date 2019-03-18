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
name|PrivateKey
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|cert
operator|.
name|Certificate
import|;
end_import

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

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|cms
operator|.
name|CMSAttributeTableGenerator
import|;
end_import

begin_comment
comment|/**  * Signer information.  */
end_comment

begin_interface
DECL|interface|SignerInfo
specifier|public
interface|interface
name|SignerInfo
block|{
DECL|method|getSignatureAlgorithm (Exchange exchange)
name|String
name|getSignatureAlgorithm
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|CryptoCmsException
function_decl|;
DECL|method|getPrivateKey (Exchange exchange)
name|PrivateKey
name|getPrivateKey
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|CryptoCmsException
function_decl|;
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
comment|/**      * Certificates which should be added to the certificate list of the Signed      * Data instance which belong to the private key. Return an empty array if      * you do not want that the certificate chain of the private key to be added      * to the signature certificates.      */
DECL|method|getCertificateChain (Exchange exchange)
name|Certificate
index|[]
name|getCertificateChain
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|CryptoCmsException
function_decl|;
comment|/**      * Returns the generator for the signed attributes.      */
DECL|method|getSignedAttributeGenerator (Exchange exchange)
name|CMSAttributeTableGenerator
name|getSignedAttributeGenerator
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|CryptoCmsException
function_decl|;
comment|/**      * Returns the generator for the unsigned attributes. Can be      *<code>null</code>, then no unsigned attribute is generated.      */
DECL|method|getUnsignedAttributeGenerator (Exchange exchange)
name|CMSAttributeTableGenerator
name|getUnsignedAttributeGenerator
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

