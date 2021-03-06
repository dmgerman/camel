begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.milo
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|milo
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|MalformedURLException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|GeneralSecurityException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|Key
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|KeyPair
import|;
end_import

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
name|PrivateKey
import|;
end_import

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
name|NoSuchElementException
import|;
end_import

begin_class
DECL|class|KeyStoreLoader
specifier|public
class|class
name|KeyStoreLoader
block|{
DECL|field|DEFAULT_KEY_STORE_TYPE
specifier|public
specifier|static
specifier|final
name|String
name|DEFAULT_KEY_STORE_TYPE
init|=
literal|"PKCS12"
decl_stmt|;
DECL|field|type
specifier|private
name|String
name|type
init|=
name|DEFAULT_KEY_STORE_TYPE
decl_stmt|;
DECL|field|url
specifier|private
name|URL
name|url
decl_stmt|;
DECL|field|keyStorePassword
specifier|private
name|String
name|keyStorePassword
decl_stmt|;
DECL|field|keyPassword
specifier|private
name|String
name|keyPassword
decl_stmt|;
DECL|field|keyAlias
specifier|private
name|String
name|keyAlias
decl_stmt|;
DECL|class|Result
specifier|public
specifier|static
class|class
name|Result
block|{
DECL|field|certificate
specifier|private
specifier|final
name|X509Certificate
name|certificate
decl_stmt|;
DECL|field|keyPair
specifier|private
specifier|final
name|KeyPair
name|keyPair
decl_stmt|;
DECL|method|Result (final X509Certificate certificate, final KeyPair keyPair)
specifier|public
name|Result
parameter_list|(
specifier|final
name|X509Certificate
name|certificate
parameter_list|,
specifier|final
name|KeyPair
name|keyPair
parameter_list|)
block|{
name|this
operator|.
name|certificate
operator|=
name|certificate
expr_stmt|;
name|this
operator|.
name|keyPair
operator|=
name|keyPair
expr_stmt|;
block|}
DECL|method|getCertificate ()
specifier|public
name|X509Certificate
name|getCertificate
parameter_list|()
block|{
return|return
name|this
operator|.
name|certificate
return|;
block|}
DECL|method|getKeyPair ()
specifier|public
name|KeyPair
name|getKeyPair
parameter_list|()
block|{
return|return
name|this
operator|.
name|keyPair
return|;
block|}
block|}
DECL|method|KeyStoreLoader ()
specifier|public
name|KeyStoreLoader
parameter_list|()
block|{     }
DECL|method|setType (final String type)
specifier|public
name|void
name|setType
parameter_list|(
specifier|final
name|String
name|type
parameter_list|)
block|{
name|this
operator|.
name|type
operator|=
name|type
operator|!=
literal|null
condition|?
name|type
else|:
name|DEFAULT_KEY_STORE_TYPE
expr_stmt|;
block|}
DECL|method|getType ()
specifier|public
name|String
name|getType
parameter_list|()
block|{
return|return
name|this
operator|.
name|type
return|;
block|}
DECL|method|setUrl (final URL url)
specifier|public
name|void
name|setUrl
parameter_list|(
specifier|final
name|URL
name|url
parameter_list|)
block|{
name|this
operator|.
name|url
operator|=
name|url
expr_stmt|;
block|}
DECL|method|getUrl ()
specifier|public
name|URL
name|getUrl
parameter_list|()
block|{
return|return
name|this
operator|.
name|url
return|;
block|}
DECL|method|setUrl (final String url)
specifier|public
name|void
name|setUrl
parameter_list|(
specifier|final
name|String
name|url
parameter_list|)
throws|throws
name|MalformedURLException
block|{
name|this
operator|.
name|url
operator|=
operator|new
name|URL
argument_list|(
name|url
argument_list|)
expr_stmt|;
block|}
DECL|method|setKeyStorePassword (final String keyStorePassword)
specifier|public
name|void
name|setKeyStorePassword
parameter_list|(
specifier|final
name|String
name|keyStorePassword
parameter_list|)
block|{
name|this
operator|.
name|keyStorePassword
operator|=
name|keyStorePassword
expr_stmt|;
block|}
DECL|method|getKeyStorePassword ()
specifier|public
name|String
name|getKeyStorePassword
parameter_list|()
block|{
return|return
name|this
operator|.
name|keyStorePassword
return|;
block|}
DECL|method|setKeyPassword (final String keyPassword)
specifier|public
name|void
name|setKeyPassword
parameter_list|(
specifier|final
name|String
name|keyPassword
parameter_list|)
block|{
name|this
operator|.
name|keyPassword
operator|=
name|keyPassword
expr_stmt|;
block|}
DECL|method|getKeyPassword ()
specifier|public
name|String
name|getKeyPassword
parameter_list|()
block|{
return|return
name|this
operator|.
name|keyPassword
return|;
block|}
DECL|method|setKeyAlias (final String keyAlias)
specifier|public
name|void
name|setKeyAlias
parameter_list|(
specifier|final
name|String
name|keyAlias
parameter_list|)
block|{
name|this
operator|.
name|keyAlias
operator|=
name|keyAlias
expr_stmt|;
block|}
DECL|method|getKeyAlias ()
specifier|public
name|String
name|getKeyAlias
parameter_list|()
block|{
return|return
name|this
operator|.
name|keyAlias
return|;
block|}
DECL|method|load ()
specifier|public
name|Result
name|load
parameter_list|()
throws|throws
name|GeneralSecurityException
throws|,
name|IOException
block|{
specifier|final
name|KeyStore
name|keyStore
init|=
name|KeyStore
operator|.
name|getInstance
argument_list|(
name|this
operator|.
name|type
argument_list|)
decl_stmt|;
try|try
init|(
name|InputStream
name|stream
init|=
name|this
operator|.
name|url
operator|.
name|openStream
argument_list|()
init|)
block|{
name|keyStore
operator|.
name|load
argument_list|(
name|stream
argument_list|,
name|this
operator|.
name|keyStorePassword
operator|!=
literal|null
condition|?
name|this
operator|.
name|keyStorePassword
operator|.
name|toCharArray
argument_list|()
else|:
literal|null
argument_list|)
expr_stmt|;
block|}
name|String
name|effectiveKeyAlias
init|=
name|this
operator|.
name|keyAlias
decl_stmt|;
if|if
condition|(
name|effectiveKeyAlias
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|keyStore
operator|.
name|size
argument_list|()
operator|!=
literal|1
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Key store contains more than one key. The use of the 'keyAlias' parameter is required."
argument_list|)
throw|;
block|}
try|try
block|{
name|effectiveKeyAlias
operator|=
name|keyStore
operator|.
name|aliases
argument_list|()
operator|.
name|nextElement
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
specifier|final
name|NoSuchElementException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Failed to enumerate key alias"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
specifier|final
name|Key
name|privateKey
init|=
name|keyStore
operator|.
name|getKey
argument_list|(
name|effectiveKeyAlias
argument_list|,
name|this
operator|.
name|keyPassword
operator|!=
literal|null
condition|?
name|this
operator|.
name|keyPassword
operator|.
name|toCharArray
argument_list|()
else|:
literal|null
argument_list|)
decl_stmt|;
if|if
condition|(
name|privateKey
operator|instanceof
name|PrivateKey
condition|)
block|{
specifier|final
name|X509Certificate
name|certificate
init|=
operator|(
name|X509Certificate
operator|)
name|keyStore
operator|.
name|getCertificate
argument_list|(
name|effectiveKeyAlias
argument_list|)
decl_stmt|;
if|if
condition|(
name|certificate
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
specifier|final
name|PublicKey
name|publicKey
init|=
name|certificate
operator|.
name|getPublicKey
argument_list|()
decl_stmt|;
specifier|final
name|KeyPair
name|keyPair
init|=
operator|new
name|KeyPair
argument_list|(
name|publicKey
argument_list|,
operator|(
name|PrivateKey
operator|)
name|privateKey
argument_list|)
decl_stmt|;
return|return
operator|new
name|Result
argument_list|(
name|certificate
argument_list|,
name|keyPair
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

