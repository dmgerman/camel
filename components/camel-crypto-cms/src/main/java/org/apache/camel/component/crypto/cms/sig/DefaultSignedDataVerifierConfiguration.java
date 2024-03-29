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
name|KeyStore
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|KeyStoreException
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
name|java
operator|.
name|util
operator|.
name|ArrayList
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
name|java
operator|.
name|util
operator|.
name|Enumeration
import|;
end_import

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
name|RuntimeCamelException
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
name|DefaultCryptoCmsUnMarshallerConfiguration
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
name|apache
operator|.
name|camel
operator|.
name|spi
operator|.
name|UriParam
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
name|spi
operator|.
name|UriParams
import|;
end_import

begin_comment
comment|/**  * Fetches the X.509 certificates which can be used for the verification from a  * Java keystore.  */
end_comment

begin_class
annotation|@
name|UriParams
DECL|class|DefaultSignedDataVerifierConfiguration
specifier|public
class|class
name|DefaultSignedDataVerifierConfiguration
extends|extends
name|DefaultCryptoCmsUnMarshallerConfiguration
implements|implements
name|SignedDataVerifierConfiguration
implements|,
name|Cloneable
block|{
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"verify"
argument_list|)
DECL|field|signedDataHeaderBase64
specifier|private
name|boolean
name|signedDataHeaderBase64
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"verify"
argument_list|,
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|verifySignaturesOfAllSigners
specifier|private
name|boolean
name|verifySignaturesOfAllSigners
init|=
literal|true
decl_stmt|;
comment|/**      * Indicates whether the value in the header CamelCryptoCmsSignedData is      * base64 encoded. Default value is<code>false</code>.      *<p>      * Only relevant for detached signatures. In the detached signature case,      * the header contains the Signed Data object.      */
DECL|method|setSignedDataHeaderBase64 (boolean signedDataHeaderBase64)
specifier|public
name|void
name|setSignedDataHeaderBase64
parameter_list|(
name|boolean
name|signedDataHeaderBase64
parameter_list|)
block|{
name|this
operator|.
name|signedDataHeaderBase64
operator|=
name|signedDataHeaderBase64
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|isSignedDataHeaderBase64 ()
specifier|public
name|boolean
name|isSignedDataHeaderBase64
parameter_list|()
block|{
return|return
name|signedDataHeaderBase64
return|;
block|}
comment|/**      * If<code>true</code> then the signatures of all signers contained in the      * Signed Data object are verified. If<code>false</code> then only one      * signature whose signer info matches with one of the specified      * certificates is verified. Default value is<code>true</code>.      */
DECL|method|setVerifySignaturesOfAllSigners (boolean verifySignaturesOfAllSigners)
specifier|public
name|void
name|setVerifySignaturesOfAllSigners
parameter_list|(
name|boolean
name|verifySignaturesOfAllSigners
parameter_list|)
block|{
name|this
operator|.
name|verifySignaturesOfAllSigners
operator|=
name|verifySignaturesOfAllSigners
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|isVerifySignaturesOfAllSigners ()
specifier|public
name|boolean
name|isVerifySignaturesOfAllSigners
parameter_list|()
block|{
return|return
name|verifySignaturesOfAllSigners
return|;
block|}
annotation|@
name|Override
DECL|method|getCertificates (Exchange exchange)
specifier|public
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
block|{
name|KeyStore
name|keystore
init|=
name|getKeyStore
argument_list|()
decl_stmt|;
try|try
block|{
name|List
argument_list|<
name|X509Certificate
argument_list|>
name|certs
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|keystore
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Enumeration
argument_list|<
name|String
argument_list|>
name|aliases
init|=
name|keystore
operator|.
name|aliases
argument_list|()
init|;
name|aliases
operator|.
name|hasMoreElements
argument_list|()
condition|;
control|)
block|{
name|String
name|alias
init|=
name|aliases
operator|.
name|nextElement
argument_list|()
decl_stmt|;
name|Certificate
name|cert
init|=
name|keystore
operator|.
name|getCertificate
argument_list|(
name|alias
argument_list|)
decl_stmt|;
if|if
condition|(
name|cert
operator|instanceof
name|X509Certificate
condition|)
block|{
name|certs
operator|.
name|add
argument_list|(
operator|(
name|X509Certificate
operator|)
name|cert
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|certs
return|;
block|}
catch|catch
parameter_list|(
name|KeyStoreException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|CryptoCmsException
argument_list|(
literal|"Problem during reading the certificates of the verifier keystore"
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|copy ()
specifier|public
name|DefaultSignedDataVerifierConfiguration
name|copy
parameter_list|()
block|{
try|try
block|{
return|return
operator|(
name|DefaultSignedDataVerifierConfiguration
operator|)
name|clone
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|CloneNotSupportedException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
comment|// should never happen
block|}
block|}
block|}
end_class

end_unit

