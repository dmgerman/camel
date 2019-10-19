begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.graphql
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|graphql
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
name|net
operator|.
name|URI
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Component
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
name|Consumer
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
name|Processor
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
name|Producer
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
name|spi
operator|.
name|Metadata
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
name|UriEndpoint
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
name|UriPath
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
name|support
operator|.
name|DefaultEndpoint
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
name|IOHelper
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
name|ObjectHelper
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
name|json
operator|.
name|JsonObject
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
name|HttpHeaders
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
name|HttpHost
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
name|auth
operator|.
name|AuthScope
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
name|auth
operator|.
name|UsernamePasswordCredentials
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
name|client
operator|.
name|CredentialsProvider
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
name|client
operator|.
name|utils
operator|.
name|HttpClientUtils
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
name|BasicCredentialsProvider
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
name|HttpClientBuilder
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
name|message
operator|.
name|BasicHeader
import|;
end_import

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|firstVersion
operator|=
literal|"3.0.0"
argument_list|,
name|scheme
operator|=
literal|"graphql"
argument_list|,
name|title
operator|=
literal|"GraphQL"
argument_list|,
name|syntax
operator|=
literal|"graphql:httpUri"
argument_list|,
name|label
operator|=
literal|"api"
argument_list|,
name|producerOnly
operator|=
literal|true
argument_list|)
DECL|class|GraphqlEndpoint
specifier|public
class|class
name|GraphqlEndpoint
extends|extends
name|DefaultEndpoint
block|{
annotation|@
name|UriPath
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|true
argument_list|)
DECL|field|httpUri
specifier|private
name|URI
name|httpUri
decl_stmt|;
annotation|@
name|UriParam
DECL|field|proxyHost
specifier|private
name|String
name|proxyHost
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"security"
argument_list|,
name|secret
operator|=
literal|true
argument_list|)
DECL|field|accessToken
specifier|private
name|String
name|accessToken
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"security"
argument_list|,
name|secret
operator|=
literal|true
argument_list|)
DECL|field|username
specifier|private
name|String
name|username
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"security"
argument_list|,
name|secret
operator|=
literal|true
argument_list|)
DECL|field|password
specifier|private
name|String
name|password
decl_stmt|;
annotation|@
name|UriParam
DECL|field|query
specifier|private
name|String
name|query
decl_stmt|;
annotation|@
name|UriParam
DECL|field|queryFile
specifier|private
name|String
name|queryFile
decl_stmt|;
annotation|@
name|UriParam
DECL|field|operationName
specifier|private
name|String
name|operationName
decl_stmt|;
annotation|@
name|UriParam
DECL|field|variables
specifier|private
name|JsonObject
name|variables
decl_stmt|;
DECL|field|httpClient
specifier|private
name|CloseableHttpClient
name|httpClient
decl_stmt|;
DECL|method|GraphqlEndpoint (String uri, Component component)
specifier|public
name|GraphqlEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|Component
name|component
parameter_list|)
block|{
name|super
argument_list|(
name|uri
argument_list|,
name|component
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
name|HttpClientUtils
operator|.
name|closeQuietly
argument_list|(
name|this
operator|.
name|httpClient
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|GraphqlProducer
argument_list|(
name|this
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createConsumer (Processor processor)
specifier|public
name|Consumer
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"You cannot receive messages at this endpoint: "
operator|+
name|getEndpointUri
argument_list|()
argument_list|)
throw|;
block|}
DECL|method|getHttpclient ()
specifier|public
name|CloseableHttpClient
name|getHttpclient
parameter_list|()
block|{
if|if
condition|(
name|httpClient
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|httpClient
operator|=
name|createHttpClient
argument_list|()
expr_stmt|;
block|}
return|return
name|httpClient
return|;
block|}
DECL|method|createHttpClient ()
specifier|private
name|CloseableHttpClient
name|createHttpClient
parameter_list|()
block|{
name|HttpClientBuilder
name|httpClientBuilder
init|=
name|HttpClients
operator|.
name|custom
argument_list|()
decl_stmt|;
if|if
condition|(
name|proxyHost
operator|!=
literal|null
condition|)
block|{
name|String
index|[]
name|parts
init|=
name|proxyHost
operator|.
name|split
argument_list|(
literal|":"
argument_list|)
decl_stmt|;
name|String
name|hostname
init|=
name|parts
index|[
literal|0
index|]
decl_stmt|;
name|int
name|port
init|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|parts
index|[
literal|1
index|]
argument_list|)
decl_stmt|;
name|httpClientBuilder
operator|.
name|setProxy
argument_list|(
operator|new
name|HttpHost
argument_list|(
name|hostname
argument_list|,
name|port
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|accessToken
operator|!=
literal|null
condition|)
block|{
name|httpClientBuilder
operator|.
name|setDefaultHeaders
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|BasicHeader
argument_list|(
name|HttpHeaders
operator|.
name|AUTHORIZATION
argument_list|,
literal|"Bearer "
operator|+
name|accessToken
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|username
operator|!=
literal|null
operator|&&
name|password
operator|!=
literal|null
condition|)
block|{
name|CredentialsProvider
name|credentialsProvider
init|=
operator|new
name|BasicCredentialsProvider
argument_list|()
decl_stmt|;
name|credentialsProvider
operator|.
name|setCredentials
argument_list|(
name|AuthScope
operator|.
name|ANY
argument_list|,
operator|new
name|UsernamePasswordCredentials
argument_list|(
name|username
argument_list|,
name|password
argument_list|)
argument_list|)
expr_stmt|;
name|httpClientBuilder
operator|.
name|setDefaultCredentialsProvider
argument_list|(
name|credentialsProvider
argument_list|)
expr_stmt|;
block|}
return|return
name|httpClientBuilder
operator|.
name|build
argument_list|()
return|;
block|}
DECL|method|getHttpUri ()
specifier|public
name|URI
name|getHttpUri
parameter_list|()
block|{
return|return
name|httpUri
return|;
block|}
comment|/**      * The GraphQL server URI.      */
DECL|method|setHttpUri (URI httpUri)
specifier|public
name|void
name|setHttpUri
parameter_list|(
name|URI
name|httpUri
parameter_list|)
block|{
name|this
operator|.
name|httpUri
operator|=
name|httpUri
expr_stmt|;
block|}
DECL|method|getProxyHostname ()
specifier|public
name|String
name|getProxyHostname
parameter_list|()
block|{
return|return
name|proxyHost
return|;
block|}
comment|/**      * The proxy host in the format "hostname:port".      */
DECL|method|setProxyHost (String proxyHost)
specifier|public
name|void
name|setProxyHost
parameter_list|(
name|String
name|proxyHost
parameter_list|)
block|{
name|this
operator|.
name|proxyHost
operator|=
name|proxyHost
expr_stmt|;
block|}
DECL|method|getAccessToken ()
specifier|public
name|String
name|getAccessToken
parameter_list|()
block|{
return|return
name|accessToken
return|;
block|}
comment|/**      * The access token sent in the Authorization header.      */
DECL|method|setAccessToken (String accessToken)
specifier|public
name|void
name|setAccessToken
parameter_list|(
name|String
name|accessToken
parameter_list|)
block|{
name|this
operator|.
name|accessToken
operator|=
name|accessToken
expr_stmt|;
block|}
DECL|method|getUsername ()
specifier|public
name|String
name|getUsername
parameter_list|()
block|{
return|return
name|username
return|;
block|}
comment|/**      * The username for Basic authentication.      */
DECL|method|setUsername (String username)
specifier|public
name|void
name|setUsername
parameter_list|(
name|String
name|username
parameter_list|)
block|{
name|this
operator|.
name|username
operator|=
name|username
expr_stmt|;
block|}
DECL|method|getPassword ()
specifier|public
name|String
name|getPassword
parameter_list|()
block|{
return|return
name|password
return|;
block|}
comment|/**      * The password for Basic authentication.      */
DECL|method|setPassword (String password)
specifier|public
name|void
name|setPassword
parameter_list|(
name|String
name|password
parameter_list|)
block|{
name|this
operator|.
name|password
operator|=
name|password
expr_stmt|;
block|}
DECL|method|getQuery ()
specifier|public
name|String
name|getQuery
parameter_list|()
block|{
if|if
condition|(
name|query
operator|==
literal|null
operator|&&
name|queryFile
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|query
operator|=
name|IOHelper
operator|.
name|loadText
argument_list|(
name|ObjectHelper
operator|.
name|loadResourceAsStream
argument_list|(
name|queryFile
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
literal|"Failed to read query file: "
operator|+
name|queryFile
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
return|return
name|query
return|;
block|}
comment|/**      * The query text.      */
DECL|method|setQuery (String query)
specifier|public
name|void
name|setQuery
parameter_list|(
name|String
name|query
parameter_list|)
block|{
name|this
operator|.
name|query
operator|=
name|query
expr_stmt|;
block|}
DECL|method|getQueryFile ()
specifier|public
name|String
name|getQueryFile
parameter_list|()
block|{
return|return
name|queryFile
return|;
block|}
comment|/**      * The query file name located in the classpath.      */
DECL|method|setQueryFile (String queryFile)
specifier|public
name|void
name|setQueryFile
parameter_list|(
name|String
name|queryFile
parameter_list|)
block|{
name|this
operator|.
name|queryFile
operator|=
name|queryFile
expr_stmt|;
block|}
DECL|method|getOperationName ()
specifier|public
name|String
name|getOperationName
parameter_list|()
block|{
return|return
name|operationName
return|;
block|}
comment|/**      * The query or mutation name.      */
DECL|method|setOperationName (String operationName)
specifier|public
name|void
name|setOperationName
parameter_list|(
name|String
name|operationName
parameter_list|)
block|{
name|this
operator|.
name|operationName
operator|=
name|operationName
expr_stmt|;
block|}
DECL|method|getVariables ()
specifier|public
name|JsonObject
name|getVariables
parameter_list|()
block|{
return|return
name|variables
return|;
block|}
comment|/**      * The JsonObject instance containing the operation variables.      */
DECL|method|setVariables (JsonObject variables)
specifier|public
name|void
name|setVariables
parameter_list|(
name|JsonObject
name|variables
parameter_list|)
block|{
name|this
operator|.
name|variables
operator|=
name|variables
expr_stmt|;
block|}
block|}
end_class

end_unit

