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
name|cert
operator|.
name|X509Certificate
import|;
end_import

begin_comment
comment|/**  * Default implementation for the XAdES signature properties which determines  * the Signing Certificate from a keystore and an alias.  *   */
end_comment

begin_class
DECL|class|DefaultXAdESSignatureProperties
specifier|public
class|class
name|DefaultXAdESSignatureProperties
extends|extends
name|XAdESSignatureProperties
block|{
DECL|field|keystore
specifier|private
name|KeyStore
name|keystore
decl_stmt|;
DECL|field|alias
specifier|private
name|String
name|alias
decl_stmt|;
DECL|method|DefaultXAdESSignatureProperties ()
specifier|public
name|DefaultXAdESSignatureProperties
parameter_list|()
block|{     }
DECL|method|setKeystore (KeyStore keystore)
specifier|public
name|void
name|setKeystore
parameter_list|(
name|KeyStore
name|keystore
parameter_list|)
block|{
name|this
operator|.
name|keystore
operator|=
name|keystore
expr_stmt|;
block|}
DECL|method|setAlias (String alias)
specifier|public
name|void
name|setAlias
parameter_list|(
name|String
name|alias
parameter_list|)
block|{
name|this
operator|.
name|alias
operator|=
name|alias
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getSigningCertificate ()
specifier|protected
name|X509Certificate
name|getSigningCertificate
parameter_list|()
throws|throws
name|Exception
block|{
comment|//NOPMD
name|X509Certificate
name|cert
init|=
operator|(
name|X509Certificate
operator|)
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
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|XmlSignatureException
argument_list|(
literal|"No certificate found in keystore for alias '%s'"
argument_list|)
throw|;
block|}
return|return
name|cert
return|;
block|}
annotation|@
name|Override
DECL|method|getSigningCertificateChain ()
specifier|protected
name|X509Certificate
index|[]
name|getSigningCertificateChain
parameter_list|()
throws|throws
name|Exception
block|{
comment|//NOPMD
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

