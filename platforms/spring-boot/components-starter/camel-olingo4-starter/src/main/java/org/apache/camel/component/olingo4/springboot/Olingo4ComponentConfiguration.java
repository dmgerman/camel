begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.olingo4.springboot
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|olingo4
operator|.
name|springboot
package|;
end_package

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
name|javax
operator|.
name|annotation
operator|.
name|Generated
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
name|olingo4
operator|.
name|internal
operator|.
name|Olingo4ApiName
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
name|spring
operator|.
name|boot
operator|.
name|ComponentConfigurationPropertiesCommon
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

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|context
operator|.
name|properties
operator|.
name|ConfigurationProperties
import|;
end_import

begin_comment
comment|/**  * Communicates with OData 4.0 services using Apache Olingo OData API.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_class
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.SpringBootAutoConfigurationMojo"
argument_list|)
annotation|@
name|ConfigurationProperties
argument_list|(
name|prefix
operator|=
literal|"camel.component.olingo4"
argument_list|)
DECL|class|Olingo4ComponentConfiguration
specifier|public
class|class
name|Olingo4ComponentConfiguration
extends|extends
name|ComponentConfigurationPropertiesCommon
block|{
comment|/**      * Whether to enable auto configuration of the olingo4 component. This is      * enabled by default.      */
DECL|field|enabled
specifier|private
name|Boolean
name|enabled
decl_stmt|;
comment|/**      * To use the shared configuration      */
DECL|field|configuration
specifier|private
name|Olingo4ConfigurationNestedConfiguration
name|configuration
decl_stmt|;
comment|/**      * Enable usage of global SSL context parameters.      */
DECL|field|useGlobalSslContextParameters
specifier|private
name|Boolean
name|useGlobalSslContextParameters
init|=
literal|false
decl_stmt|;
comment|/**      * Whether the component should use basic property binding (Camel 2.x) or      * the newer property binding with additional capabilities      */
DECL|field|basicPropertyBinding
specifier|private
name|Boolean
name|basicPropertyBinding
init|=
literal|false
decl_stmt|;
comment|/**      * Whether the producer should be started lazy (on the first message). By      * starting lazy you can use this to allow CamelContext and routes to      * startup in situations where a producer may otherwise fail during starting      * and cause the route to fail being started. By deferring this startup to      * be lazy then the startup failure can be handled during routing messages      * via Camel's routing error handlers. Beware that when the first message is      * processed then creating and starting the producer may take a little time      * and prolong the total processing time of the processing.      */
DECL|field|lazyStartProducer
specifier|private
name|Boolean
name|lazyStartProducer
init|=
literal|false
decl_stmt|;
comment|/**      * Allows for bridging the consumer to the Camel routing Error Handler,      * which mean any exceptions occurred while the consumer is trying to pickup      * incoming messages, or the likes, will now be processed as a message and      * handled by the routing Error Handler. By default the consumer will use      * the org.apache.camel.spi.ExceptionHandler to deal with exceptions, that      * will be logged at WARN or ERROR level and ignored.      */
DECL|field|bridgeErrorHandler
specifier|private
name|Boolean
name|bridgeErrorHandler
init|=
literal|false
decl_stmt|;
DECL|method|getConfiguration ()
specifier|public
name|Olingo4ConfigurationNestedConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
DECL|method|setConfiguration ( Olingo4ConfigurationNestedConfiguration configuration)
specifier|public
name|void
name|setConfiguration
parameter_list|(
name|Olingo4ConfigurationNestedConfiguration
name|configuration
parameter_list|)
block|{
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
block|}
DECL|method|getUseGlobalSslContextParameters ()
specifier|public
name|Boolean
name|getUseGlobalSslContextParameters
parameter_list|()
block|{
return|return
name|useGlobalSslContextParameters
return|;
block|}
DECL|method|setUseGlobalSslContextParameters ( Boolean useGlobalSslContextParameters)
specifier|public
name|void
name|setUseGlobalSslContextParameters
parameter_list|(
name|Boolean
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
DECL|method|getBasicPropertyBinding ()
specifier|public
name|Boolean
name|getBasicPropertyBinding
parameter_list|()
block|{
return|return
name|basicPropertyBinding
return|;
block|}
DECL|method|setBasicPropertyBinding (Boolean basicPropertyBinding)
specifier|public
name|void
name|setBasicPropertyBinding
parameter_list|(
name|Boolean
name|basicPropertyBinding
parameter_list|)
block|{
name|this
operator|.
name|basicPropertyBinding
operator|=
name|basicPropertyBinding
expr_stmt|;
block|}
DECL|method|getLazyStartProducer ()
specifier|public
name|Boolean
name|getLazyStartProducer
parameter_list|()
block|{
return|return
name|lazyStartProducer
return|;
block|}
DECL|method|setLazyStartProducer (Boolean lazyStartProducer)
specifier|public
name|void
name|setLazyStartProducer
parameter_list|(
name|Boolean
name|lazyStartProducer
parameter_list|)
block|{
name|this
operator|.
name|lazyStartProducer
operator|=
name|lazyStartProducer
expr_stmt|;
block|}
DECL|method|getBridgeErrorHandler ()
specifier|public
name|Boolean
name|getBridgeErrorHandler
parameter_list|()
block|{
return|return
name|bridgeErrorHandler
return|;
block|}
DECL|method|setBridgeErrorHandler (Boolean bridgeErrorHandler)
specifier|public
name|void
name|setBridgeErrorHandler
parameter_list|(
name|Boolean
name|bridgeErrorHandler
parameter_list|)
block|{
name|this
operator|.
name|bridgeErrorHandler
operator|=
name|bridgeErrorHandler
expr_stmt|;
block|}
DECL|class|Olingo4ConfigurationNestedConfiguration
specifier|public
specifier|static
class|class
name|Olingo4ConfigurationNestedConfiguration
block|{
DECL|field|CAMEL_NESTED_CLASS
specifier|public
specifier|static
specifier|final
name|Class
name|CAMEL_NESTED_CLASS
init|=
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|olingo4
operator|.
name|Olingo4Configuration
operator|.
name|class
decl_stmt|;
comment|/**          * What kind of operation to perform          */
DECL|field|apiName
specifier|private
name|Olingo4ApiName
name|apiName
decl_stmt|;
comment|/**          * What sub operation to use for the selected operation          */
DECL|field|methodName
specifier|private
name|String
name|methodName
decl_stmt|;
comment|/**          * Target OData service base URI, e.g.          * http://services.odata.org/OData/OData.svc          */
DECL|field|serviceUri
specifier|private
name|String
name|serviceUri
decl_stmt|;
comment|/**          * Content-Type header value can be used to specify JSON or XML message          * format, defaults to application/json;charset=utf-8          */
DECL|field|contentType
specifier|private
name|String
name|contentType
init|=
literal|"application/json;charset=utf-8"
decl_stmt|;
comment|/**          * Custom HTTP headers to inject into every request, this could include          * OAuth tokens, etc.          */
DECL|field|httpHeaders
specifier|private
name|Map
name|httpHeaders
decl_stmt|;
comment|/**          * HTTP connection creation timeout in milliseconds, defaults to 30,000          * (30 seconds)          */
DECL|field|connectTimeout
specifier|private
name|Integer
name|connectTimeout
init|=
literal|30000
decl_stmt|;
comment|/**          * HTTP request timeout in milliseconds, defaults to 30,000 (30 seconds)          */
DECL|field|socketTimeout
specifier|private
name|Integer
name|socketTimeout
init|=
literal|30000
decl_stmt|;
comment|/**          * HTTP proxy server configuration          */
DECL|field|proxy
specifier|private
name|HttpHost
name|proxy
decl_stmt|;
comment|/**          * To configure security using SSLContextParameters          */
DECL|field|sslContextParameters
specifier|private
name|SSLContextParameters
name|sslContextParameters
decl_stmt|;
comment|/**          * Custom HTTP async client builder for more complex HTTP client          * configuration, overrides connectionTimeout, socketTimeout, proxy and          * sslContext. Note that a socketTimeout MUST be specified in the          * builder, otherwise OData requests could block indefinitely          */
DECL|field|httpAsyncClientBuilder
specifier|private
name|HttpAsyncClientBuilder
name|httpAsyncClientBuilder
decl_stmt|;
comment|/**          * Custom HTTP client builder for more complex HTTP client          * configuration, overrides connectionTimeout, socketTimeout, proxy and          * sslContext. Note that a socketTimeout MUST be specified in the          * builder, otherwise OData requests could block indefinitely          */
DECL|field|httpClientBuilder
specifier|private
name|HttpClientBuilder
name|httpClientBuilder
decl_stmt|;
comment|/**          * Set this to true to filter out results that have already been          * communicated by this component.          */
DECL|field|filterAlreadySeen
specifier|private
name|Boolean
name|filterAlreadySeen
init|=
literal|false
decl_stmt|;
comment|/**          * For endpoints that return an array or collection, a consumer endpoint          * will map every element to distinct messages, unless splitResult is          * set to false.          */
DECL|field|splitResult
specifier|private
name|Boolean
name|splitResult
init|=
literal|true
decl_stmt|;
DECL|method|getApiName ()
specifier|public
name|Olingo4ApiName
name|getApiName
parameter_list|()
block|{
return|return
name|apiName
return|;
block|}
DECL|method|setApiName (Olingo4ApiName apiName)
specifier|public
name|void
name|setApiName
parameter_list|(
name|Olingo4ApiName
name|apiName
parameter_list|)
block|{
name|this
operator|.
name|apiName
operator|=
name|apiName
expr_stmt|;
block|}
DECL|method|getMethodName ()
specifier|public
name|String
name|getMethodName
parameter_list|()
block|{
return|return
name|methodName
return|;
block|}
DECL|method|setMethodName (String methodName)
specifier|public
name|void
name|setMethodName
parameter_list|(
name|String
name|methodName
parameter_list|)
block|{
name|this
operator|.
name|methodName
operator|=
name|methodName
expr_stmt|;
block|}
DECL|method|getServiceUri ()
specifier|public
name|String
name|getServiceUri
parameter_list|()
block|{
return|return
name|serviceUri
return|;
block|}
DECL|method|setServiceUri (String serviceUri)
specifier|public
name|void
name|setServiceUri
parameter_list|(
name|String
name|serviceUri
parameter_list|)
block|{
name|this
operator|.
name|serviceUri
operator|=
name|serviceUri
expr_stmt|;
block|}
DECL|method|getContentType ()
specifier|public
name|String
name|getContentType
parameter_list|()
block|{
return|return
name|contentType
return|;
block|}
DECL|method|setContentType (String contentType)
specifier|public
name|void
name|setContentType
parameter_list|(
name|String
name|contentType
parameter_list|)
block|{
name|this
operator|.
name|contentType
operator|=
name|contentType
expr_stmt|;
block|}
DECL|method|getHttpHeaders ()
specifier|public
name|Map
name|getHttpHeaders
parameter_list|()
block|{
return|return
name|httpHeaders
return|;
block|}
DECL|method|setHttpHeaders (Map httpHeaders)
specifier|public
name|void
name|setHttpHeaders
parameter_list|(
name|Map
name|httpHeaders
parameter_list|)
block|{
name|this
operator|.
name|httpHeaders
operator|=
name|httpHeaders
expr_stmt|;
block|}
DECL|method|getConnectTimeout ()
specifier|public
name|Integer
name|getConnectTimeout
parameter_list|()
block|{
return|return
name|connectTimeout
return|;
block|}
DECL|method|setConnectTimeout (Integer connectTimeout)
specifier|public
name|void
name|setConnectTimeout
parameter_list|(
name|Integer
name|connectTimeout
parameter_list|)
block|{
name|this
operator|.
name|connectTimeout
operator|=
name|connectTimeout
expr_stmt|;
block|}
DECL|method|getSocketTimeout ()
specifier|public
name|Integer
name|getSocketTimeout
parameter_list|()
block|{
return|return
name|socketTimeout
return|;
block|}
DECL|method|setSocketTimeout (Integer socketTimeout)
specifier|public
name|void
name|setSocketTimeout
parameter_list|(
name|Integer
name|socketTimeout
parameter_list|)
block|{
name|this
operator|.
name|socketTimeout
operator|=
name|socketTimeout
expr_stmt|;
block|}
DECL|method|getProxy ()
specifier|public
name|HttpHost
name|getProxy
parameter_list|()
block|{
return|return
name|proxy
return|;
block|}
DECL|method|setProxy (HttpHost proxy)
specifier|public
name|void
name|setProxy
parameter_list|(
name|HttpHost
name|proxy
parameter_list|)
block|{
name|this
operator|.
name|proxy
operator|=
name|proxy
expr_stmt|;
block|}
DECL|method|getSslContextParameters ()
specifier|public
name|SSLContextParameters
name|getSslContextParameters
parameter_list|()
block|{
return|return
name|sslContextParameters
return|;
block|}
DECL|method|setSslContextParameters ( SSLContextParameters sslContextParameters)
specifier|public
name|void
name|setSslContextParameters
parameter_list|(
name|SSLContextParameters
name|sslContextParameters
parameter_list|)
block|{
name|this
operator|.
name|sslContextParameters
operator|=
name|sslContextParameters
expr_stmt|;
block|}
DECL|method|getHttpAsyncClientBuilder ()
specifier|public
name|HttpAsyncClientBuilder
name|getHttpAsyncClientBuilder
parameter_list|()
block|{
return|return
name|httpAsyncClientBuilder
return|;
block|}
DECL|method|setHttpAsyncClientBuilder ( HttpAsyncClientBuilder httpAsyncClientBuilder)
specifier|public
name|void
name|setHttpAsyncClientBuilder
parameter_list|(
name|HttpAsyncClientBuilder
name|httpAsyncClientBuilder
parameter_list|)
block|{
name|this
operator|.
name|httpAsyncClientBuilder
operator|=
name|httpAsyncClientBuilder
expr_stmt|;
block|}
DECL|method|getHttpClientBuilder ()
specifier|public
name|HttpClientBuilder
name|getHttpClientBuilder
parameter_list|()
block|{
return|return
name|httpClientBuilder
return|;
block|}
DECL|method|setHttpClientBuilder (HttpClientBuilder httpClientBuilder)
specifier|public
name|void
name|setHttpClientBuilder
parameter_list|(
name|HttpClientBuilder
name|httpClientBuilder
parameter_list|)
block|{
name|this
operator|.
name|httpClientBuilder
operator|=
name|httpClientBuilder
expr_stmt|;
block|}
DECL|method|getFilterAlreadySeen ()
specifier|public
name|Boolean
name|getFilterAlreadySeen
parameter_list|()
block|{
return|return
name|filterAlreadySeen
return|;
block|}
DECL|method|setFilterAlreadySeen (Boolean filterAlreadySeen)
specifier|public
name|void
name|setFilterAlreadySeen
parameter_list|(
name|Boolean
name|filterAlreadySeen
parameter_list|)
block|{
name|this
operator|.
name|filterAlreadySeen
operator|=
name|filterAlreadySeen
expr_stmt|;
block|}
DECL|method|getSplitResult ()
specifier|public
name|Boolean
name|getSplitResult
parameter_list|()
block|{
return|return
name|splitResult
return|;
block|}
DECL|method|setSplitResult (Boolean splitResult)
specifier|public
name|void
name|setSplitResult
parameter_list|(
name|Boolean
name|splitResult
parameter_list|)
block|{
name|this
operator|.
name|splitResult
operator|=
name|splitResult
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

