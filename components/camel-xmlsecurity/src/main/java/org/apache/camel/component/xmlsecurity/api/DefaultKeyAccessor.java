begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|UUID
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
name|X509Data
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
comment|/**  * Accesses the public key from a key-store and returns a KeyInfo which  * contains the X.509 certificate chain corresponding to the public key.  */
end_comment

begin_class
DECL|class|DefaultKeyAccessor
specifier|public
class|class
name|DefaultKeyAccessor
extends|extends
name|DefaultKeySelector
implements|implements
name|KeyAccessor
block|{
DECL|field|provider
specifier|private
name|String
name|provider
decl_stmt|;
DECL|method|DefaultKeyAccessor ()
specifier|public
name|DefaultKeyAccessor
parameter_list|()
block|{      }
DECL|method|getProvider ()
specifier|public
name|String
name|getProvider
parameter_list|()
block|{
return|return
name|provider
return|;
block|}
DECL|method|setProvider (String provider)
specifier|public
name|void
name|setProvider
parameter_list|(
name|String
name|provider
parameter_list|)
block|{
name|this
operator|.
name|provider
operator|=
name|provider
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getKeySelector (Message message)
specifier|public
name|KeySelector
name|getKeySelector
parameter_list|(
name|Message
name|message
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|this
return|;
block|}
annotation|@
name|Override
DECL|method|getKeyInfo (Message message, Node messageBody, KeyInfoFactory factory)
specifier|public
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
name|factory
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|createKeyInfo
argument_list|(
name|factory
argument_list|)
return|;
block|}
DECL|method|createKeyInfo (KeyInfoFactory kif)
specifier|private
name|KeyInfo
name|createKeyInfo
parameter_list|(
name|KeyInfoFactory
name|kif
parameter_list|)
throws|throws
name|Exception
block|{
name|X509Certificate
index|[]
name|chain
init|=
name|getCertificateChain
argument_list|()
decl_stmt|;
if|if
condition|(
name|chain
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|X509Data
name|x509D
init|=
name|kif
operator|.
name|newX509Data
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|chain
argument_list|)
argument_list|)
decl_stmt|;
return|return
name|kif
operator|.
name|newKeyInfo
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|x509D
argument_list|)
argument_list|,
literal|"_"
operator|+
name|UUID
operator|.
name|randomUUID
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
return|;
block|}
DECL|method|getCertificateChain ()
specifier|private
name|X509Certificate
index|[]
name|getCertificateChain
parameter_list|()
throws|throws
name|Exception
block|{
name|KeyStore
name|keystore
init|=
name|getKeyStore
argument_list|()
decl_stmt|;
if|if
condition|(
name|keystore
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|String
name|alias
init|=
name|getAlias
argument_list|()
decl_stmt|;
if|if
condition|(
name|alias
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|Certificate
index|[]
name|certs
init|=
name|keystore
operator|.
name|getCertificateChain
argument_list|(
name|alias
argument_list|)
decl_stmt|;
if|if
condition|(
name|certs
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|ArrayList
argument_list|<
name|X509Certificate
argument_list|>
name|certList
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|certs
operator|.
name|length
argument_list|)
decl_stmt|;
for|for
control|(
name|Certificate
name|cert
range|:
name|certs
control|)
block|{
if|if
condition|(
name|cert
operator|instanceof
name|X509Certificate
condition|)
block|{
name|certList
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
name|certList
operator|.
name|toArray
argument_list|(
operator|new
name|X509Certificate
index|[
name|certList
operator|.
name|size
argument_list|()
index|]
argument_list|)
return|;
block|}
block|}
end_class

end_unit

