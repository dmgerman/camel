begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.crypto.processor
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
name|processor
package|;
end_package

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|PublicKey
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|Signature
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|SignatureException
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
name|DigitalSignatureConfiguration
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
name|DigitalSignatureConstants
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
name|util
operator|.
name|ExchangeHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|codec
operator|.
name|binary
operator|.
name|Base64
import|;
end_import

begin_comment
comment|/**  *<code>VerifyingProcessor</code>  */
end_comment

begin_class
DECL|class|VerifyingProcessor
specifier|public
class|class
name|VerifyingProcessor
extends|extends
name|DigitalSignatureProcessor
block|{
DECL|method|VerifyingProcessor (DigitalSignatureConfiguration configuration)
specifier|public
name|VerifyingProcessor
parameter_list|(
name|DigitalSignatureConfiguration
name|configuration
parameter_list|)
block|{
name|super
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
block|}
DECL|method|process (Exchange exchange)
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
name|Signature
name|signer
init|=
name|createSignatureService
argument_list|()
decl_stmt|;
name|Certificate
name|cert
init|=
name|getCertificate
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
if|if
condition|(
name|cert
operator|==
literal|null
condition|)
block|{
name|PublicKey
name|pk
init|=
name|getPublicKeyOrCertificateFromHeader
argument_list|(
name|exchange
argument_list|,
name|PublicKey
operator|.
name|class
argument_list|,
name|config
operator|.
name|getPublicKey
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|pk
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Cannot verify signature as no Public Key or Certificate has been supplied."
operator|+
literal|" Either supply one in the route definition or via the message header '%s'"
argument_list|,
name|DigitalSignatureConstants
operator|.
name|SIGNATURE_PUBLIC_KEY_OR_CERT
argument_list|)
argument_list|)
throw|;
block|}
name|signer
operator|.
name|initVerify
argument_list|(
name|pk
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|signer
operator|.
name|initVerify
argument_list|(
name|cert
argument_list|)
expr_stmt|;
block|}
name|calculateSignature
argument_list|(
name|exchange
argument_list|,
name|signer
argument_list|)
expr_stmt|;
name|byte
index|[]
name|signature
init|=
name|getSignatureFromExchange
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|signer
operator|.
name|verify
argument_list|(
name|signature
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|SignatureException
argument_list|(
literal|"Cannot verify signature of exchange"
argument_list|)
throw|;
block|}
name|clearMessageHeaders
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|getSignatureFromExchange (Exchange exchange)
specifier|private
name|byte
index|[]
name|getSignatureFromExchange
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|encodedSignature
init|=
name|ExchangeHelper
operator|.
name|getMandatoryHeader
argument_list|(
name|exchange
argument_list|,
name|config
operator|.
name|getSignatureHeaderName
argument_list|()
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|encodedSignature
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Cannot verify exchange as no "
operator|+
name|config
operator|.
name|getSignatureHeaderName
argument_list|()
operator|+
literal|" header is present."
argument_list|)
throw|;
block|}
return|return
operator|new
name|Base64
argument_list|()
operator|.
name|decode
argument_list|(
name|encodedSignature
argument_list|)
return|;
block|}
DECL|method|getCertificate (Exchange exchange)
specifier|private
name|Certificate
name|getCertificate
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|Certificate
name|cert
init|=
name|config
operator|.
name|getCertificate
argument_list|(
name|getAlias
argument_list|(
name|exchange
argument_list|)
argument_list|)
decl_stmt|;
return|return
name|getPublicKeyOrCertificateFromHeader
argument_list|(
name|exchange
argument_list|,
name|Certificate
operator|.
name|class
argument_list|,
name|cert
argument_list|)
return|;
block|}
DECL|method|getPublicKeyOrCertificateFromHeader (Exchange exchange, Class<? extends T> verificationType, T defaultsTo)
specifier|private
parameter_list|<
name|T
parameter_list|>
name|T
name|getPublicKeyOrCertificateFromHeader
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Class
argument_list|<
name|?
extends|extends
name|T
argument_list|>
name|verificationType
parameter_list|,
name|T
name|defaultsTo
parameter_list|)
block|{
name|T
name|pkOrCert
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|DigitalSignatureConstants
operator|.
name|SIGNATURE_PUBLIC_KEY_OR_CERT
argument_list|,
name|verificationType
argument_list|)
decl_stmt|;
if|if
condition|(
name|pkOrCert
operator|==
literal|null
condition|)
block|{
name|pkOrCert
operator|=
name|defaultsTo
expr_stmt|;
block|}
return|return
name|pkOrCert
return|;
block|}
block|}
end_class

end_unit

