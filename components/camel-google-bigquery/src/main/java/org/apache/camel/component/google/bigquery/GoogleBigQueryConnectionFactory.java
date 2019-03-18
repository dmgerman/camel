begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.google.bigquery
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|google
operator|.
name|bigquery
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|KeyFactory
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
name|spec
operator|.
name|PKCS8EncodedKeySpec
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
name|Collections
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|api
operator|.
name|client
operator|.
name|googleapis
operator|.
name|auth
operator|.
name|oauth2
operator|.
name|GoogleCredential
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|api
operator|.
name|client
operator|.
name|googleapis
operator|.
name|javanet
operator|.
name|GoogleNetHttpTransport
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|api
operator|.
name|client
operator|.
name|http
operator|.
name|HttpTransport
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|api
operator|.
name|client
operator|.
name|http
operator|.
name|apache
operator|.
name|ApacheHttpTransport
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|api
operator|.
name|client
operator|.
name|json
operator|.
name|JsonFactory
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|api
operator|.
name|client
operator|.
name|json
operator|.
name|jackson2
operator|.
name|JacksonFactory
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|api
operator|.
name|client
operator|.
name|util
operator|.
name|Base64
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|api
operator|.
name|client
operator|.
name|util
operator|.
name|Strings
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|api
operator|.
name|services
operator|.
name|bigquery
operator|.
name|Bigquery
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|api
operator|.
name|services
operator|.
name|bigquery
operator|.
name|BigqueryScopes
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|impl
operator|.
name|client
operator|.
name|CloseableHttpClient
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|impl
operator|.
name|client
operator|.
name|HttpClients
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|impl
operator|.
name|conn
operator|.
name|PoolingHttpClientConnectionManager
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_class
DECL|class|GoogleBigQueryConnectionFactory
specifier|public
class|class
name|GoogleBigQueryConnectionFactory
block|{
DECL|field|JSON_FACTORY
specifier|private
specifier|static
specifier|final
name|JsonFactory
name|JSON_FACTORY
init|=
operator|new
name|JacksonFactory
argument_list|()
decl_stmt|;
DECL|field|logger
specifier|private
specifier|final
name|Logger
name|logger
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|GoogleBigQueryConnectionFactory
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|serviceAccount
specifier|private
name|String
name|serviceAccount
decl_stmt|;
DECL|field|serviceAccountKey
specifier|private
name|String
name|serviceAccountKey
decl_stmt|;
DECL|field|credentialsFileLocation
specifier|private
name|String
name|credentialsFileLocation
decl_stmt|;
DECL|field|serviceURL
specifier|private
name|String
name|serviceURL
decl_stmt|;
DECL|field|client
specifier|private
name|Bigquery
name|client
decl_stmt|;
DECL|method|GoogleBigQueryConnectionFactory ()
specifier|public
name|GoogleBigQueryConnectionFactory
parameter_list|()
block|{     }
DECL|method|getDefaultClient ()
specifier|public
specifier|synchronized
name|Bigquery
name|getDefaultClient
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|this
operator|.
name|client
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|client
operator|=
name|buildClient
argument_list|()
expr_stmt|;
block|}
return|return
name|this
operator|.
name|client
return|;
block|}
DECL|method|getMultiThreadClient (int parallelThreads)
specifier|public
name|Bigquery
name|getMultiThreadClient
parameter_list|(
name|int
name|parallelThreads
parameter_list|)
throws|throws
name|Exception
block|{
name|PoolingHttpClientConnectionManager
name|cm
init|=
operator|new
name|PoolingHttpClientConnectionManager
argument_list|()
decl_stmt|;
name|cm
operator|.
name|setDefaultMaxPerRoute
argument_list|(
name|parallelThreads
argument_list|)
expr_stmt|;
name|cm
operator|.
name|setMaxTotal
argument_list|(
name|parallelThreads
argument_list|)
expr_stmt|;
name|CloseableHttpClient
name|httpClient
init|=
name|HttpClients
operator|.
name|createMinimal
argument_list|(
name|cm
argument_list|)
decl_stmt|;
return|return
name|buildClient
argument_list|(
operator|new
name|ApacheHttpTransport
argument_list|(
name|httpClient
argument_list|)
argument_list|)
return|;
block|}
DECL|method|buildClient ()
specifier|private
name|Bigquery
name|buildClient
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|buildClient
argument_list|(
name|GoogleNetHttpTransport
operator|.
name|newTrustedTransport
argument_list|()
argument_list|)
return|;
block|}
DECL|method|buildClient (HttpTransport httpTransport)
specifier|private
name|Bigquery
name|buildClient
parameter_list|(
name|HttpTransport
name|httpTransport
parameter_list|)
throws|throws
name|Exception
block|{
name|GoogleCredential
name|credential
init|=
literal|null
decl_stmt|;
if|if
condition|(
operator|!
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|serviceAccount
argument_list|)
operator|&&
operator|!
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|serviceAccountKey
argument_list|)
condition|)
block|{
if|if
condition|(
name|logger
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|logger
operator|.
name|debug
argument_list|(
literal|"Service Account and Key have been set explicitly. Initialising BigQuery using Service Account "
operator|+
name|serviceAccount
argument_list|)
expr_stmt|;
block|}
name|credential
operator|=
name|createFromAccountKeyPair
argument_list|(
name|httpTransport
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|credential
operator|==
literal|null
operator|&&
operator|!
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|credentialsFileLocation
argument_list|)
condition|)
block|{
if|if
condition|(
name|logger
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|logger
operator|.
name|debug
argument_list|(
literal|"Key File Name has been set explicitly. Initialising BigQuery using Key File "
operator|+
name|credentialsFileLocation
argument_list|)
expr_stmt|;
block|}
name|credential
operator|=
name|createFromFile
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|credential
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|logger
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|logger
operator|.
name|debug
argument_list|(
literal|"No explicit Service Account or Key File Name have been provided. Initialising BigQuery using defaults "
argument_list|)
expr_stmt|;
block|}
name|credential
operator|=
name|createDefault
argument_list|()
expr_stmt|;
block|}
name|Bigquery
operator|.
name|Builder
name|builder
init|=
operator|new
name|Bigquery
operator|.
name|Builder
argument_list|(
name|httpTransport
argument_list|,
name|JSON_FACTORY
argument_list|,
name|credential
argument_list|)
operator|.
name|setApplicationName
argument_list|(
literal|"camel-google-bigquery"
argument_list|)
decl_stmt|;
comment|// Local emulator, SOCKS proxy, etc.
if|if
condition|(
name|serviceURL
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|setRootUrl
argument_list|(
name|serviceURL
argument_list|)
expr_stmt|;
block|}
return|return
name|builder
operator|.
name|build
argument_list|()
return|;
block|}
DECL|method|createFromFile ()
specifier|private
name|GoogleCredential
name|createFromFile
parameter_list|()
throws|throws
name|Exception
block|{
name|GoogleCredential
name|credential
init|=
name|GoogleCredential
operator|.
name|fromStream
argument_list|(
operator|new
name|FileInputStream
argument_list|(
name|credentialsFileLocation
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|credential
operator|.
name|createScopedRequired
argument_list|()
condition|)
block|{
name|credential
operator|=
name|credential
operator|.
name|createScoped
argument_list|(
name|BigqueryScopes
operator|.
name|all
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|credential
return|;
block|}
DECL|method|createDefault ()
specifier|private
name|GoogleCredential
name|createDefault
parameter_list|()
throws|throws
name|Exception
block|{
name|GoogleCredential
name|credential
init|=
name|GoogleCredential
operator|.
name|getApplicationDefault
argument_list|()
decl_stmt|;
name|Collection
argument_list|<
name|String
argument_list|>
name|scopes
init|=
name|Collections
operator|.
name|singletonList
argument_list|(
name|BigqueryScopes
operator|.
name|BIGQUERY
argument_list|)
decl_stmt|;
if|if
condition|(
name|credential
operator|.
name|createScopedRequired
argument_list|()
condition|)
block|{
name|credential
operator|=
name|credential
operator|.
name|createScoped
argument_list|(
name|scopes
argument_list|)
expr_stmt|;
block|}
return|return
name|credential
return|;
block|}
DECL|method|createFromAccountKeyPair (HttpTransport httpTransport)
specifier|private
name|GoogleCredential
name|createFromAccountKeyPair
parameter_list|(
name|HttpTransport
name|httpTransport
parameter_list|)
block|{
try|try
block|{
return|return
operator|new
name|GoogleCredential
operator|.
name|Builder
argument_list|()
operator|.
name|setTransport
argument_list|(
name|httpTransport
argument_list|)
operator|.
name|setJsonFactory
argument_list|(
name|JSON_FACTORY
argument_list|)
operator|.
name|setServiceAccountId
argument_list|(
name|serviceAccount
argument_list|)
operator|.
name|setServiceAccountScopes
argument_list|(
name|BigqueryScopes
operator|.
name|all
argument_list|()
argument_list|)
operator|.
name|setServiceAccountPrivateKey
argument_list|(
name|getPrivateKeyFromString
argument_list|(
name|serviceAccountKey
argument_list|)
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|getPrivateKeyFromString (String serviceKeyPem)
specifier|private
name|PrivateKey
name|getPrivateKeyFromString
parameter_list|(
name|String
name|serviceKeyPem
parameter_list|)
block|{
name|PrivateKey
name|privateKey
decl_stmt|;
try|try
block|{
name|String
name|privKeyPEM
init|=
name|serviceKeyPem
operator|.
name|replace
argument_list|(
literal|"-----BEGIN PRIVATE KEY-----"
argument_list|,
literal|""
argument_list|)
operator|.
name|replace
argument_list|(
literal|"-----END PRIVATE KEY-----"
argument_list|,
literal|""
argument_list|)
operator|.
name|replace
argument_list|(
literal|"\r"
argument_list|,
literal|""
argument_list|)
operator|.
name|replace
argument_list|(
literal|"\n"
argument_list|,
literal|""
argument_list|)
decl_stmt|;
name|byte
index|[]
name|encoded
init|=
name|Base64
operator|.
name|decodeBase64
argument_list|(
name|privKeyPEM
argument_list|)
decl_stmt|;
name|PKCS8EncodedKeySpec
name|keySpec
init|=
operator|new
name|PKCS8EncodedKeySpec
argument_list|(
name|encoded
argument_list|)
decl_stmt|;
name|privateKey
operator|=
name|KeyFactory
operator|.
name|getInstance
argument_list|(
literal|"RSA"
argument_list|)
operator|.
name|generatePrivate
argument_list|(
name|keySpec
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|String
name|error
init|=
literal|"Constructing Private Key from PEM string failed: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
decl_stmt|;
name|logger
operator|.
name|error
argument_list|(
name|error
argument_list|,
name|e
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
return|return
name|privateKey
return|;
block|}
DECL|method|getServiceAccount ()
specifier|public
name|String
name|getServiceAccount
parameter_list|()
block|{
return|return
name|serviceAccount
return|;
block|}
DECL|method|setServiceAccount (String serviceAccount)
specifier|public
name|GoogleBigQueryConnectionFactory
name|setServiceAccount
parameter_list|(
name|String
name|serviceAccount
parameter_list|)
block|{
name|this
operator|.
name|serviceAccount
operator|=
name|serviceAccount
expr_stmt|;
name|resetClient
argument_list|()
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|getServiceAccountKey ()
specifier|public
name|String
name|getServiceAccountKey
parameter_list|()
block|{
return|return
name|serviceAccountKey
return|;
block|}
DECL|method|setServiceAccountKey (String serviceAccountKey)
specifier|public
name|GoogleBigQueryConnectionFactory
name|setServiceAccountKey
parameter_list|(
name|String
name|serviceAccountKey
parameter_list|)
block|{
name|this
operator|.
name|serviceAccountKey
operator|=
name|serviceAccountKey
expr_stmt|;
name|resetClient
argument_list|()
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|getCredentialsFileLocation ()
specifier|public
name|String
name|getCredentialsFileLocation
parameter_list|()
block|{
return|return
name|credentialsFileLocation
return|;
block|}
DECL|method|setCredentialsFileLocation (String credentialsFileLocation)
specifier|public
name|GoogleBigQueryConnectionFactory
name|setCredentialsFileLocation
parameter_list|(
name|String
name|credentialsFileLocation
parameter_list|)
block|{
name|this
operator|.
name|credentialsFileLocation
operator|=
name|credentialsFileLocation
expr_stmt|;
name|resetClient
argument_list|()
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|getServiceURL ()
specifier|public
name|String
name|getServiceURL
parameter_list|()
block|{
return|return
name|serviceURL
return|;
block|}
DECL|method|setServiceURL (String serviceURL)
specifier|public
name|GoogleBigQueryConnectionFactory
name|setServiceURL
parameter_list|(
name|String
name|serviceURL
parameter_list|)
block|{
name|this
operator|.
name|serviceURL
operator|=
name|serviceURL
expr_stmt|;
name|resetClient
argument_list|()
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|resetClient ()
specifier|private
specifier|synchronized
name|void
name|resetClient
parameter_list|()
block|{
name|this
operator|.
name|client
operator|=
literal|null
expr_stmt|;
block|}
block|}
end_class

end_unit

