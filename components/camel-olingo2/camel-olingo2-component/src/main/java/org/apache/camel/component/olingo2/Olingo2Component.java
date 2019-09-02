begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.olingo2
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|olingo2
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
name|security
operator|.
name|GeneralSecurityException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
name|CamelContext
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
name|Endpoint
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
name|SSLContextParametersAware
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
name|olingo2
operator|.
name|api
operator|.
name|impl
operator|.
name|Olingo2AppImpl
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
name|olingo2
operator|.
name|internal
operator|.
name|Olingo2ApiCollection
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
name|olingo2
operator|.
name|internal
operator|.
name|Olingo2ApiName
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
name|annotations
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
name|support
operator|.
name|component
operator|.
name|AbstractApiComponent
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
name|jsse
operator|.
name|SSLContextParameters
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
name|client
operator|.
name|config
operator|.
name|RequestConfig
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
name|nio
operator|.
name|client
operator|.
name|HttpAsyncClientBuilder
import|;
end_import

begin_comment
comment|/**  * Represents the component that manages {@link Olingo2Endpoint}.  */
end_comment

begin_class
annotation|@
name|Component
argument_list|(
literal|"olingo2"
argument_list|)
DECL|class|Olingo2Component
specifier|public
class|class
name|Olingo2Component
extends|extends
name|AbstractApiComponent
argument_list|<
name|Olingo2ApiName
argument_list|,
name|Olingo2Configuration
argument_list|,
name|Olingo2ApiCollection
argument_list|>
implements|implements
name|SSLContextParametersAware
block|{
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"security"
argument_list|,
name|defaultValue
operator|=
literal|"false"
argument_list|)
DECL|field|useGlobalSslContextParameters
specifier|private
name|boolean
name|useGlobalSslContextParameters
decl_stmt|;
comment|// component level shared proxy
DECL|field|apiProxy
specifier|private
name|Olingo2AppWrapper
name|apiProxy
decl_stmt|;
DECL|method|Olingo2Component ()
specifier|public
name|Olingo2Component
parameter_list|()
block|{
name|super
argument_list|(
name|Olingo2Endpoint
operator|.
name|class
argument_list|,
name|Olingo2ApiName
operator|.
name|class
argument_list|,
name|Olingo2ApiCollection
operator|.
name|getCollection
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|Olingo2Component (CamelContext context)
specifier|public
name|Olingo2Component
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
name|super
argument_list|(
name|context
argument_list|,
name|Olingo2Endpoint
operator|.
name|class
argument_list|,
name|Olingo2ApiName
operator|.
name|class
argument_list|,
name|Olingo2ApiCollection
operator|.
name|getCollection
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getApiName (String apiNameStr)
specifier|protected
name|Olingo2ApiName
name|getApiName
parameter_list|(
name|String
name|apiNameStr
parameter_list|)
throws|throws
name|IllegalArgumentException
block|{
return|return
name|Olingo2ApiName
operator|.
name|fromValue
argument_list|(
name|apiNameStr
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createEndpoint (String uri, String remaining, Map<String, Object> parameters)
specifier|protected
name|Endpoint
name|createEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|remaining
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
throws|throws
name|Exception
block|{
comment|// parse remaining to extract resourcePath and queryParams
specifier|final
name|String
index|[]
name|pathSegments
init|=
name|remaining
operator|.
name|split
argument_list|(
literal|"/"
argument_list|,
operator|-
literal|1
argument_list|)
decl_stmt|;
specifier|final
name|String
name|methodName
init|=
name|pathSegments
index|[
literal|0
index|]
decl_stmt|;
if|if
condition|(
name|pathSegments
operator|.
name|length
operator|>
literal|1
condition|)
block|{
specifier|final
name|StringBuilder
name|resourcePath
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|1
init|;
name|i
operator|<
name|pathSegments
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|resourcePath
operator|.
name|append
argument_list|(
name|pathSegments
index|[
name|i
index|]
argument_list|)
expr_stmt|;
if|if
condition|(
name|i
operator|<
operator|(
name|pathSegments
operator|.
name|length
operator|-
literal|1
operator|)
condition|)
block|{
name|resourcePath
operator|.
name|append
argument_list|(
literal|'/'
argument_list|)
expr_stmt|;
block|}
block|}
comment|// This will override any URI supplied ?resourcePath=... param
name|parameters
operator|.
name|put
argument_list|(
name|Olingo2Endpoint
operator|.
name|RESOURCE_PATH_PROPERTY
argument_list|,
name|resourcePath
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|final
name|Olingo2Configuration
name|endpointConfiguration
init|=
name|createEndpointConfiguration
argument_list|(
name|Olingo2ApiName
operator|.
name|DEFAULT
argument_list|)
decl_stmt|;
specifier|final
name|Endpoint
name|endpoint
init|=
name|createEndpoint
argument_list|(
name|uri
argument_list|,
name|methodName
argument_list|,
name|Olingo2ApiName
operator|.
name|DEFAULT
argument_list|,
name|endpointConfiguration
argument_list|)
decl_stmt|;
name|setProperties
argument_list|(
name|endpoint
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
return|return
name|endpoint
return|;
block|}
annotation|@
name|Override
DECL|method|createEndpoint (String uri, String methodName, Olingo2ApiName apiName, Olingo2Configuration endpointConfiguration)
specifier|protected
name|Endpoint
name|createEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|methodName
parameter_list|,
name|Olingo2ApiName
name|apiName
parameter_list|,
name|Olingo2Configuration
name|endpointConfiguration
parameter_list|)
block|{
name|endpointConfiguration
operator|.
name|setApiName
argument_list|(
name|apiName
argument_list|)
expr_stmt|;
name|endpointConfiguration
operator|.
name|setMethodName
argument_list|(
name|methodName
argument_list|)
expr_stmt|;
return|return
operator|new
name|Olingo2Endpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|,
name|apiName
argument_list|,
name|methodName
argument_list|,
name|endpointConfiguration
argument_list|)
return|;
block|}
comment|/**      * To use the shared configuration      */
annotation|@
name|Override
DECL|method|setConfiguration (Olingo2Configuration configuration)
specifier|public
name|void
name|setConfiguration
parameter_list|(
name|Olingo2Configuration
name|configuration
parameter_list|)
block|{
name|super
operator|.
name|setConfiguration
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
block|}
comment|/**      * To use the shared configuration      */
annotation|@
name|Override
DECL|method|getConfiguration ()
specifier|public
name|Olingo2Configuration
name|getConfiguration
parameter_list|()
block|{
return|return
name|super
operator|.
name|getConfiguration
argument_list|()
return|;
block|}
DECL|method|createApiProxy (Olingo2Configuration endpointConfiguration)
specifier|public
name|Olingo2AppWrapper
name|createApiProxy
parameter_list|(
name|Olingo2Configuration
name|endpointConfiguration
parameter_list|)
block|{
specifier|final
name|Olingo2AppWrapper
name|result
decl_stmt|;
if|if
condition|(
name|endpointConfiguration
operator|.
name|equals
argument_list|(
name|this
operator|.
name|configuration
argument_list|)
condition|)
block|{
synchronized|synchronized
init|(
name|this
init|)
block|{
if|if
condition|(
name|apiProxy
operator|==
literal|null
condition|)
block|{
name|apiProxy
operator|=
name|createOlingo2App
argument_list|(
name|this
operator|.
name|configuration
argument_list|)
expr_stmt|;
block|}
block|}
name|result
operator|=
name|apiProxy
expr_stmt|;
block|}
else|else
block|{
name|result
operator|=
name|createOlingo2App
argument_list|(
name|endpointConfiguration
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
annotation|@
name|Override
DECL|method|isUseGlobalSslContextParameters ()
specifier|public
name|boolean
name|isUseGlobalSslContextParameters
parameter_list|()
block|{
return|return
name|this
operator|.
name|useGlobalSslContextParameters
return|;
block|}
comment|/**      * Enable usage of global SSL context parameters.      */
annotation|@
name|Override
DECL|method|setUseGlobalSslContextParameters (boolean useGlobalSslContextParameters)
specifier|public
name|void
name|setUseGlobalSslContextParameters
parameter_list|(
name|boolean
name|useGlobalSslContextParameters
parameter_list|)
block|{
name|this
operator|.
name|useGlobalSslContextParameters
operator|=
name|useGlobalSslContextParameters
expr_stmt|;
block|}
DECL|method|createOlingo2App (Olingo2Configuration configuration)
specifier|private
name|Olingo2AppWrapper
name|createOlingo2App
parameter_list|(
name|Olingo2Configuration
name|configuration
parameter_list|)
block|{
name|Object
name|clientBuilder
init|=
name|configuration
operator|.
name|getHttpAsyncClientBuilder
argument_list|()
decl_stmt|;
if|if
condition|(
name|clientBuilder
operator|==
literal|null
condition|)
block|{
name|HttpAsyncClientBuilder
name|asyncClientBuilder
init|=
name|HttpAsyncClientBuilder
operator|.
name|create
argument_list|()
decl_stmt|;
comment|// apply simple configuration properties
specifier|final
name|RequestConfig
operator|.
name|Builder
name|requestConfigBuilder
init|=
name|RequestConfig
operator|.
name|custom
argument_list|()
decl_stmt|;
name|requestConfigBuilder
operator|.
name|setConnectTimeout
argument_list|(
name|configuration
operator|.
name|getConnectTimeout
argument_list|()
argument_list|)
expr_stmt|;
name|requestConfigBuilder
operator|.
name|setSocketTimeout
argument_list|(
name|configuration
operator|.
name|getSocketTimeout
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|HttpHost
name|proxy
init|=
name|configuration
operator|.
name|getProxy
argument_list|()
decl_stmt|;
if|if
condition|(
name|proxy
operator|!=
literal|null
condition|)
block|{
name|requestConfigBuilder
operator|.
name|setProxy
argument_list|(
name|proxy
argument_list|)
expr_stmt|;
block|}
comment|// set default request config
name|asyncClientBuilder
operator|.
name|setDefaultRequestConfig
argument_list|(
name|requestConfigBuilder
operator|.
name|build
argument_list|()
argument_list|)
expr_stmt|;
name|SSLContextParameters
name|sslContextParameters
init|=
name|configuration
operator|.
name|getSslContextParameters
argument_list|()
decl_stmt|;
if|if
condition|(
name|sslContextParameters
operator|==
literal|null
condition|)
block|{
comment|// use global ssl config
name|sslContextParameters
operator|=
name|retrieveGlobalSslContextParameters
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|sslContextParameters
operator|==
literal|null
condition|)
block|{
comment|// use defaults if not specified
name|sslContextParameters
operator|=
operator|new
name|SSLContextParameters
argument_list|()
expr_stmt|;
block|}
try|try
block|{
name|asyncClientBuilder
operator|.
name|setSSLContext
argument_list|(
name|sslContextParameters
operator|.
name|createSSLContext
argument_list|(
name|getCamelContext
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|GeneralSecurityException
name|e
parameter_list|)
block|{
throw|throw
name|RuntimeCamelException
operator|.
name|wrapRuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
name|RuntimeCamelException
operator|.
name|wrapRuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
name|clientBuilder
operator|=
name|asyncClientBuilder
expr_stmt|;
block|}
name|Olingo2AppImpl
name|olingo2App
decl_stmt|;
if|if
condition|(
name|clientBuilder
operator|==
literal|null
operator|||
name|clientBuilder
operator|instanceof
name|HttpAsyncClientBuilder
condition|)
block|{
name|olingo2App
operator|=
operator|new
name|Olingo2AppImpl
argument_list|(
name|configuration
operator|.
name|getServiceUri
argument_list|()
argument_list|,
operator|(
name|HttpAsyncClientBuilder
operator|)
name|clientBuilder
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|olingo2App
operator|=
operator|new
name|Olingo2AppImpl
argument_list|(
name|configuration
operator|.
name|getServiceUri
argument_list|()
argument_list|,
operator|(
name|HttpClientBuilder
operator|)
name|clientBuilder
argument_list|)
expr_stmt|;
block|}
name|apiProxy
operator|=
operator|new
name|Olingo2AppWrapper
argument_list|(
name|olingo2App
argument_list|)
expr_stmt|;
name|apiProxy
operator|.
name|getOlingo2App
argument_list|()
operator|.
name|setContentType
argument_list|(
name|configuration
operator|.
name|getContentType
argument_list|()
argument_list|)
expr_stmt|;
name|apiProxy
operator|.
name|getOlingo2App
argument_list|()
operator|.
name|setHttpHeaders
argument_list|(
name|configuration
operator|.
name|getHttpHeaders
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|apiProxy
return|;
block|}
DECL|method|closeApiProxy (Olingo2AppWrapper apiProxy)
specifier|public
name|void
name|closeApiProxy
parameter_list|(
name|Olingo2AppWrapper
name|apiProxy
parameter_list|)
block|{
if|if
condition|(
name|this
operator|.
name|apiProxy
operator|!=
name|apiProxy
condition|)
block|{
comment|// not a shared proxy
name|apiProxy
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
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
if|if
condition|(
name|apiProxy
operator|!=
literal|null
condition|)
block|{
name|apiProxy
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

