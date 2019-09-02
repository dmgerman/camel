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
name|commons
operator|.
name|lang3
operator|.
name|builder
operator|.
name|HashCodeBuilder
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
name|entity
operator|.
name|ContentType
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
comment|/**  * Component configuration for Olingo2 component.  */
end_comment

begin_class
annotation|@
name|UriParams
DECL|class|Olingo2Configuration
specifier|public
class|class
name|Olingo2Configuration
block|{
DECL|field|DEFAULT_CONTENT_TYPE
specifier|private
specifier|static
specifier|final
name|String
name|DEFAULT_CONTENT_TYPE
init|=
name|ContentType
operator|.
name|APPLICATION_JSON
operator|.
name|toString
argument_list|()
decl_stmt|;
DECL|field|DEFAULT_TIMEOUT
specifier|private
specifier|static
specifier|final
name|int
name|DEFAULT_TIMEOUT
init|=
literal|30
operator|*
literal|1000
decl_stmt|;
annotation|@
name|UriPath
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|true
argument_list|)
DECL|field|apiName
specifier|private
name|Olingo2ApiName
name|apiName
decl_stmt|;
annotation|@
name|UriPath
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|true
argument_list|)
DECL|field|methodName
specifier|private
name|String
name|methodName
decl_stmt|;
annotation|@
name|UriParam
DECL|field|serviceUri
specifier|private
name|String
name|serviceUri
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"application/json;charset=utf-8"
argument_list|)
DECL|field|contentType
specifier|private
name|String
name|contentType
init|=
name|DEFAULT_CONTENT_TYPE
decl_stmt|;
annotation|@
name|UriParam
DECL|field|httpHeaders
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|httpHeaders
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|""
operator|+
name|DEFAULT_TIMEOUT
argument_list|)
DECL|field|connectTimeout
specifier|private
name|int
name|connectTimeout
init|=
name|DEFAULT_TIMEOUT
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|""
operator|+
name|DEFAULT_TIMEOUT
argument_list|)
DECL|field|socketTimeout
specifier|private
name|int
name|socketTimeout
init|=
name|DEFAULT_TIMEOUT
decl_stmt|;
annotation|@
name|UriParam
DECL|field|proxy
specifier|private
name|HttpHost
name|proxy
decl_stmt|;
annotation|@
name|UriParam
DECL|field|sslContextParameters
specifier|private
name|SSLContextParameters
name|sslContextParameters
decl_stmt|;
annotation|@
name|UriParam
DECL|field|httpAsyncClientBuilder
specifier|private
name|HttpAsyncClientBuilder
name|httpAsyncClientBuilder
decl_stmt|;
annotation|@
name|UriParam
DECL|field|httpClientBuilder
specifier|private
name|HttpClientBuilder
name|httpClientBuilder
decl_stmt|;
annotation|@
name|UriParam
DECL|field|filterAlreadySeen
specifier|private
name|boolean
name|filterAlreadySeen
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|,
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|splitResult
specifier|private
name|boolean
name|splitResult
init|=
literal|true
decl_stmt|;
DECL|method|getApiName ()
specifier|public
name|Olingo2ApiName
name|getApiName
parameter_list|()
block|{
return|return
name|apiName
return|;
block|}
comment|/**      * What kind of operation to perform      */
DECL|method|setApiName (Olingo2ApiName apiName)
specifier|public
name|void
name|setApiName
parameter_list|(
name|Olingo2ApiName
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
comment|/**      * What sub operation to use for the selected operation      */
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
comment|/**      * Target OData service base URI, e.g.      * http://services.odata.org/OData/OData.svc      */
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
comment|/**      * Content-Type header value can be used to specify JSON or XML message      * format, defaults to application/json;charset=utf-8      */
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
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getHttpHeaders
parameter_list|()
block|{
return|return
name|httpHeaders
return|;
block|}
comment|/**      * Custom HTTP headers to inject into every request, this could include      * OAuth tokens, etc.      */
DECL|method|setHttpHeaders (Map<String, String> httpHeaders)
specifier|public
name|void
name|setHttpHeaders
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
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
name|int
name|getConnectTimeout
parameter_list|()
block|{
return|return
name|connectTimeout
return|;
block|}
comment|/**      * HTTP connection creation timeout in milliseconds, defaults to 30,000 (30      * seconds)      */
DECL|method|setConnectTimeout (int connectTimeout)
specifier|public
name|void
name|setConnectTimeout
parameter_list|(
name|int
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
name|int
name|getSocketTimeout
parameter_list|()
block|{
return|return
name|socketTimeout
return|;
block|}
comment|/**      * HTTP request timeout in milliseconds, defaults to 30,000 (30 seconds)      */
DECL|method|setSocketTimeout (int socketTimeout)
specifier|public
name|void
name|setSocketTimeout
parameter_list|(
name|int
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
comment|/**      * HTTP proxy server configuration      */
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
comment|/**      * To configure security using SSLContextParameters      */
DECL|method|setSslContextParameters (SSLContextParameters sslContextParameters)
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
comment|/**      * Custom HTTP async client builder for more complex HTTP client      * configuration, overrides connectionTimeout, socketTimeout, proxy and      * sslContext. Note that a socketTimeout MUST be specified in the builder,      * otherwise OData requests could block indefinitely      */
DECL|method|setHttpAsyncClientBuilder (HttpAsyncClientBuilder httpAsyncClientBuilder)
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
comment|/**      * Custom HTTP client builder for more complex HTTP client configuration,      * overrides connectionTimeout, socketTimeout, proxy and sslContext. Note      * that a socketTimeout MUST be specified in the builder, otherwise OData      * requests could block indefinitely      */
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
comment|/**      * Filter flag for filtering out already seen results      */
DECL|method|isFilterAlreadySeen ()
specifier|public
name|boolean
name|isFilterAlreadySeen
parameter_list|()
block|{
return|return
name|filterAlreadySeen
return|;
block|}
comment|/**      * Set this to true to filter out results that have already been      * communicated by this component.      *       * @param filterAlreadySeen      */
DECL|method|setFilterAlreadySeen (boolean filterAlreadySeen)
specifier|public
name|void
name|setFilterAlreadySeen
parameter_list|(
name|boolean
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
DECL|method|isSplitResult ()
specifier|public
name|boolean
name|isSplitResult
parameter_list|()
block|{
return|return
name|splitResult
return|;
block|}
comment|/**      * For endpoints that return an array or collection, a consumer endpoint will map every element to distinct messages, unless      * splitResult is set to false.      */
DECL|method|setSplitResult (boolean splitResult)
specifier|public
name|void
name|setSplitResult
parameter_list|(
name|boolean
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
annotation|@
name|Override
DECL|method|hashCode ()
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
operator|new
name|HashCodeBuilder
argument_list|()
operator|.
name|append
argument_list|(
name|serviceUri
argument_list|)
operator|.
name|append
argument_list|(
name|contentType
argument_list|)
operator|.
name|append
argument_list|(
name|httpHeaders
argument_list|)
operator|.
name|append
argument_list|(
name|connectTimeout
argument_list|)
operator|.
name|append
argument_list|(
name|socketTimeout
argument_list|)
operator|.
name|append
argument_list|(
name|proxy
argument_list|)
operator|.
name|append
argument_list|(
name|sslContextParameters
argument_list|)
operator|.
name|append
argument_list|(
name|httpAsyncClientBuilder
argument_list|)
operator|.
name|append
argument_list|(
name|httpClientBuilder
argument_list|)
operator|.
name|hashCode
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|equals (Object obj)
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|obj
parameter_list|)
block|{
if|if
condition|(
name|obj
operator|instanceof
name|Olingo2Configuration
condition|)
block|{
name|Olingo2Configuration
name|other
init|=
operator|(
name|Olingo2Configuration
operator|)
name|obj
decl_stmt|;
return|return
name|serviceUri
operator|==
literal|null
condition|?
name|other
operator|.
name|serviceUri
operator|==
literal|null
else|:
name|serviceUri
operator|.
name|equals
argument_list|(
name|other
operator|.
name|serviceUri
argument_list|)
operator|&&
name|contentType
operator|==
literal|null
condition|?
name|other
operator|.
name|contentType
operator|==
literal|null
else|:
name|contentType
operator|.
name|equals
argument_list|(
name|other
operator|.
name|contentType
argument_list|)
operator|&&
name|httpHeaders
operator|==
literal|null
condition|?
name|other
operator|.
name|httpHeaders
operator|==
literal|null
else|:
name|httpHeaders
operator|.
name|equals
argument_list|(
name|other
operator|.
name|httpHeaders
argument_list|)
operator|&&
name|connectTimeout
operator|==
name|other
operator|.
name|connectTimeout
operator|&&
name|socketTimeout
operator|==
name|other
operator|.
name|socketTimeout
operator|&&
name|proxy
operator|==
literal|null
condition|?
name|other
operator|.
name|proxy
operator|==
literal|null
else|:
name|proxy
operator|.
name|equals
argument_list|(
name|other
operator|.
name|proxy
argument_list|)
operator|&&
name|sslContextParameters
operator|==
literal|null
condition|?
name|other
operator|.
name|sslContextParameters
operator|==
literal|null
else|:
name|sslContextParameters
operator|.
name|equals
argument_list|(
name|other
operator|.
name|sslContextParameters
argument_list|)
operator|&&
name|httpAsyncClientBuilder
operator|==
literal|null
condition|?
name|other
operator|.
name|httpAsyncClientBuilder
operator|==
literal|null
else|:
name|httpAsyncClientBuilder
operator|.
name|equals
argument_list|(
name|other
operator|.
name|httpAsyncClientBuilder
argument_list|)
operator|&&
name|httpClientBuilder
operator|==
literal|null
condition|?
name|other
operator|.
name|httpClientBuilder
operator|==
literal|null
else|:
name|httpClientBuilder
operator|.
name|equals
argument_list|(
name|other
operator|.
name|httpClientBuilder
argument_list|)
return|;
block|}
return|return
literal|false
return|;
block|}
block|}
end_class

end_unit

