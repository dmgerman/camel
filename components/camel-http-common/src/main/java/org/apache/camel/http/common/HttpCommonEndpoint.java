begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.http.common
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|http
operator|.
name|common
package|;
end_package

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
name|net
operator|.
name|URISyntaxException
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
name|impl
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
name|spi
operator|.
name|HeaderFilterStrategy
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
name|HeaderFilterStrategyAware
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
name|UriPath
import|;
end_import

begin_class
DECL|class|HttpCommonEndpoint
specifier|public
specifier|abstract
class|class
name|HttpCommonEndpoint
extends|extends
name|DefaultEndpoint
implements|implements
name|HeaderFilterStrategyAware
block|{
comment|// Note: all options must be documented with description in annotations so extended components can access the documentation
DECL|field|component
name|HttpCommonComponent
name|component
decl_stmt|;
DECL|field|urlRewrite
name|UrlRewrite
name|urlRewrite
decl_stmt|;
annotation|@
name|UriPath
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|,
name|description
operator|=
literal|"The url of the HTTP endpoint to call."
argument_list|)
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|"true"
argument_list|)
DECL|field|httpUri
name|URI
name|httpUri
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|description
operator|=
literal|"To use a custom HeaderFilterStrategy to filter header to and from Camel message."
argument_list|)
DECL|field|headerFilterStrategy
name|HeaderFilterStrategy
name|headerFilterStrategy
init|=
operator|new
name|HttpHeaderFilterStrategy
argument_list|()
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|description
operator|=
literal|"To use a custom HttpBinding to control the mapping between Camel message and HttpClient."
argument_list|)
DECL|field|binding
name|HttpBinding
name|binding
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|,
name|defaultValue
operator|=
literal|"true"
argument_list|,
name|description
operator|=
literal|"Option to disable throwing the HttpOperationFailedException in case of failed responses from the remote server."
operator|+
literal|" This allows you to get all responses regardless of the HTTP status code."
argument_list|)
DECL|field|throwExceptionOnFailure
name|boolean
name|throwExceptionOnFailure
init|=
literal|true
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|,
name|description
operator|=
literal|"If the option is true, HttpProducer will ignore the Exchange.HTTP_URI header, and use the endpoint's URI for request."
operator|+
literal|" You may also set the option throwExceptionOnFailure to be false to let the HttpProducer send all the fault response back."
argument_list|)
DECL|field|bridgeEndpoint
name|boolean
name|bridgeEndpoint
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|,
name|description
operator|=
literal|"Whether or not the consumer should try to find a target consumer by matching the URI prefix if no exact match is found."
argument_list|)
DECL|field|matchOnUriPrefix
name|boolean
name|matchOnUriPrefix
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"true"
argument_list|,
name|description
operator|=
literal|"If this option is false Jetty servlet will disable the HTTP streaming and set the content-length header on the response"
argument_list|)
DECL|field|chunked
name|boolean
name|chunked
init|=
literal|true
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|,
name|description
operator|=
literal|"Determines whether or not the raw input stream from Jetty is cached or not"
operator|+
literal|" (Camel will read the stream into a in memory/overflow to file, Stream caching) cache."
operator|+
literal|" By default Camel will cache the Jetty input stream to support reading it multiple times to ensure it Camel"
operator|+
literal|" can retrieve all data from the stream. However you can set this option to true when you for example need"
operator|+
literal|" to access the raw stream, such as streaming it directly to a file or other persistent store."
operator|+
literal|" DefaultHttpBinding will copy the request input stream into a stream cache and put it into message body"
operator|+
literal|" if this option is false to support reading the stream multiple times."
operator|+
literal|" If you use Jetty to bridge/proxy an endpoint then consider enabling this option to improve performance,"
operator|+
literal|" in case you do not need to read the message payload multiple times."
argument_list|)
DECL|field|disableStreamCache
name|boolean
name|disableStreamCache
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|,
name|description
operator|=
literal|"The proxy host name"
argument_list|)
DECL|field|proxyHost
name|String
name|proxyHost
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|,
name|description
operator|=
literal|"The proxy port number"
argument_list|)
DECL|field|proxyPort
name|int
name|proxyPort
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|,
name|enums
operator|=
literal|"Basic,Digest,NTLM"
argument_list|,
name|description
operator|=
literal|"Authentication method for proxy, either as Basic, Digest or NTLM."
argument_list|)
DECL|field|authMethodPriority
name|String
name|authMethodPriority
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|description
operator|=
literal|"Option to disable throwing the HttpOperationFailedException in case of failed responses from the remote server."
operator|+
literal|" This allows you to get all responses regardless of the HTTP status code."
argument_list|)
DECL|field|transferException
name|boolean
name|transferException
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|,
name|description
operator|=
literal|"Specifies whether to enable HTTP TRACE for this Jetty consumer. By default TRACE is turned off."
argument_list|)
DECL|field|traceEnabled
name|boolean
name|traceEnabled
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|,
name|description
operator|=
literal|"Used to only allow consuming if the HttpMethod matches, such as GET/POST/PUT etc. Multiple methods can be specified separated by comma."
argument_list|)
DECL|field|httpMethodRestrict
name|String
name|httpMethodRestrict
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|,
name|description
operator|=
literal|"To use a custom buffer size on the javax.servlet.ServletResponse."
argument_list|)
DECL|field|responseBufferSize
name|Integer
name|responseBufferSize
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|,
name|description
operator|=
literal|"If this option is true, The http producer won't read response body and cache the input stream"
argument_list|)
DECL|field|ignoreResponseBody
name|boolean
name|ignoreResponseBody
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|,
name|defaultValue
operator|=
literal|"true"
argument_list|,
name|description
operator|=
literal|"If this option is true then IN exchange headers will be copied to OUT exchange headers according to copy strategy."
operator|+
literal|" Setting this to false, allows to only include the headers from the HTTP response (not propagating IN headers)."
argument_list|)
DECL|field|copyHeaders
name|boolean
name|copyHeaders
init|=
literal|true
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|,
name|description
operator|=
literal|"Whether to eager check whether the HTTP requests has content if the content-length header is 0 or not present."
operator|+
literal|" This can be turned on in case HTTP clients do not send streamed data."
argument_list|)
DECL|field|eagerCheckContentAvailable
name|boolean
name|eagerCheckContentAvailable
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|,
name|defaultValue
operator|=
literal|"200-299"
argument_list|,
name|description
operator|=
literal|"The status codes which is considered a success response. The values are inclusive. The range must be defined as from-to with the dash included."
argument_list|)
DECL|field|okStatusCodeRange
specifier|private
name|String
name|okStatusCodeRange
init|=
literal|"200-299"
decl_stmt|;
DECL|method|HttpCommonEndpoint ()
specifier|public
name|HttpCommonEndpoint
parameter_list|()
block|{     }
DECL|method|HttpCommonEndpoint (String endPointURI, HttpCommonComponent component, URI httpURI)
specifier|public
name|HttpCommonEndpoint
parameter_list|(
name|String
name|endPointURI
parameter_list|,
name|HttpCommonComponent
name|component
parameter_list|,
name|URI
name|httpURI
parameter_list|)
throws|throws
name|URISyntaxException
block|{
name|super
argument_list|(
name|endPointURI
argument_list|,
name|component
argument_list|)
expr_stmt|;
name|this
operator|.
name|component
operator|=
name|component
expr_stmt|;
name|this
operator|.
name|httpUri
operator|=
name|httpURI
expr_stmt|;
block|}
DECL|method|connect (HttpConsumer consumer)
specifier|public
name|void
name|connect
parameter_list|(
name|HttpConsumer
name|consumer
parameter_list|)
throws|throws
name|Exception
block|{
name|component
operator|.
name|connect
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
block|}
DECL|method|disconnect (HttpConsumer consumer)
specifier|public
name|void
name|disconnect
parameter_list|(
name|HttpConsumer
name|consumer
parameter_list|)
throws|throws
name|Exception
block|{
name|component
operator|.
name|disconnect
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
block|}
DECL|method|isLenientProperties ()
specifier|public
name|boolean
name|isLenientProperties
parameter_list|()
block|{
comment|// true to allow dynamic URI options to be configured and passed to external system for eg. the HttpProducer
return|return
literal|true
return|;
block|}
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
comment|// Properties
comment|//-------------------------------------------------------------------------
DECL|method|getBinding ()
specifier|public
name|HttpBinding
name|getBinding
parameter_list|()
block|{
if|if
condition|(
name|binding
operator|==
literal|null
condition|)
block|{
comment|// create a new binding and use the options from this endpoint
name|binding
operator|=
operator|new
name|DefaultHttpBinding
argument_list|()
expr_stmt|;
name|binding
operator|.
name|setHeaderFilterStrategy
argument_list|(
name|getHeaderFilterStrategy
argument_list|()
argument_list|)
expr_stmt|;
name|binding
operator|.
name|setTransferException
argument_list|(
name|isTransferException
argument_list|()
argument_list|)
expr_stmt|;
name|binding
operator|.
name|setEagerCheckContentAvailable
argument_list|(
name|isEagerCheckContentAvailable
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|binding
return|;
block|}
comment|/**      * To use a custom HttpBinding to control the mapping between Camel message and HttpClient.      */
DECL|method|setBinding (HttpBinding binding)
specifier|public
name|void
name|setBinding
parameter_list|(
name|HttpBinding
name|binding
parameter_list|)
block|{
name|this
operator|.
name|binding
operator|=
name|binding
expr_stmt|;
block|}
DECL|method|getPath ()
specifier|public
name|String
name|getPath
parameter_list|()
block|{
comment|//if the path is empty, we just return the default path here
return|return
name|httpUri
operator|.
name|getPath
argument_list|()
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|?
literal|"/"
else|:
name|httpUri
operator|.
name|getPath
argument_list|()
return|;
block|}
DECL|method|getPort ()
specifier|public
name|int
name|getPort
parameter_list|()
block|{
if|if
condition|(
name|httpUri
operator|.
name|getPort
argument_list|()
operator|==
operator|-
literal|1
condition|)
block|{
if|if
condition|(
literal|"https"
operator|.
name|equals
argument_list|(
name|getProtocol
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|443
return|;
block|}
else|else
block|{
return|return
literal|80
return|;
block|}
block|}
return|return
name|httpUri
operator|.
name|getPort
argument_list|()
return|;
block|}
DECL|method|getProtocol ()
specifier|public
name|String
name|getProtocol
parameter_list|()
block|{
return|return
name|httpUri
operator|.
name|getScheme
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
comment|/**      * The url of the HTTP endpoint to call.      */
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
DECL|method|getHeaderFilterStrategy ()
specifier|public
name|HeaderFilterStrategy
name|getHeaderFilterStrategy
parameter_list|()
block|{
return|return
name|headerFilterStrategy
return|;
block|}
comment|/**      * To use a custom HeaderFilterStrategy to filter header to and from Camel message.      */
DECL|method|setHeaderFilterStrategy (HeaderFilterStrategy headerFilterStrategy)
specifier|public
name|void
name|setHeaderFilterStrategy
parameter_list|(
name|HeaderFilterStrategy
name|headerFilterStrategy
parameter_list|)
block|{
name|this
operator|.
name|headerFilterStrategy
operator|=
name|headerFilterStrategy
expr_stmt|;
block|}
DECL|method|isThrowExceptionOnFailure ()
specifier|public
name|boolean
name|isThrowExceptionOnFailure
parameter_list|()
block|{
return|return
name|throwExceptionOnFailure
return|;
block|}
comment|/**      * Option to disable throwing the HttpOperationFailedException in case of failed responses from the remote server.      * This allows you to get all responses regardless of the HTTP status code.      */
DECL|method|setThrowExceptionOnFailure (boolean throwExceptionOnFailure)
specifier|public
name|void
name|setThrowExceptionOnFailure
parameter_list|(
name|boolean
name|throwExceptionOnFailure
parameter_list|)
block|{
name|this
operator|.
name|throwExceptionOnFailure
operator|=
name|throwExceptionOnFailure
expr_stmt|;
block|}
DECL|method|isBridgeEndpoint ()
specifier|public
name|boolean
name|isBridgeEndpoint
parameter_list|()
block|{
return|return
name|bridgeEndpoint
return|;
block|}
comment|/**      * If the option is true, HttpProducer will ignore the Exchange.HTTP_URI header, and use the endpoint's URI for request.      * You may also set the option throwExceptionOnFailure to be false to let the HttpProducer send all the fault response back.      */
DECL|method|setBridgeEndpoint (boolean bridge)
specifier|public
name|void
name|setBridgeEndpoint
parameter_list|(
name|boolean
name|bridge
parameter_list|)
block|{
name|this
operator|.
name|bridgeEndpoint
operator|=
name|bridge
expr_stmt|;
block|}
DECL|method|isMatchOnUriPrefix ()
specifier|public
name|boolean
name|isMatchOnUriPrefix
parameter_list|()
block|{
return|return
name|matchOnUriPrefix
return|;
block|}
comment|/**      * Whether or not the consumer should try to find a target consumer by matching the URI prefix if no exact match is found.      *<p/>      * See more details at: http://camel.apache.org/how-do-i-let-jetty-match-wildcards.html      */
DECL|method|setMatchOnUriPrefix (boolean match)
specifier|public
name|void
name|setMatchOnUriPrefix
parameter_list|(
name|boolean
name|match
parameter_list|)
block|{
name|this
operator|.
name|matchOnUriPrefix
operator|=
name|match
expr_stmt|;
block|}
DECL|method|isDisableStreamCache ()
specifier|public
name|boolean
name|isDisableStreamCache
parameter_list|()
block|{
return|return
name|this
operator|.
name|disableStreamCache
return|;
block|}
comment|/**      * Determines whether or not the raw input stream from Jetty is cached or not      * (Camel will read the stream into a in memory/overflow to file, Stream caching) cache.      * By default Camel will cache the Jetty input stream to support reading it multiple times to ensure it Camel      * can retrieve all data from the stream. However you can set this option to true when you for example need      * to access the raw stream, such as streaming it directly to a file or other persistent store.      * DefaultHttpBinding will copy the request input stream into a stream cache and put it into message body      * if this option is false to support reading the stream multiple times.      * If you use Jetty to bridge/proxy an endpoint then consider enabling this option to improve performance,      * in case you do not need to read the message payload multiple times.      */
DECL|method|setDisableStreamCache (boolean disable)
specifier|public
name|void
name|setDisableStreamCache
parameter_list|(
name|boolean
name|disable
parameter_list|)
block|{
name|this
operator|.
name|disableStreamCache
operator|=
name|disable
expr_stmt|;
block|}
DECL|method|isChunked ()
specifier|public
name|boolean
name|isChunked
parameter_list|()
block|{
return|return
name|this
operator|.
name|chunked
return|;
block|}
comment|/**      * If this option is false Jetty servlet will disable the HTTP streaming and set the content-length header on the response      */
DECL|method|setChunked (boolean chunked)
specifier|public
name|void
name|setChunked
parameter_list|(
name|boolean
name|chunked
parameter_list|)
block|{
name|this
operator|.
name|chunked
operator|=
name|chunked
expr_stmt|;
block|}
DECL|method|getProxyHost ()
specifier|public
name|String
name|getProxyHost
parameter_list|()
block|{
return|return
name|proxyHost
return|;
block|}
comment|/**      * The proxy host name      */
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
DECL|method|getProxyPort ()
specifier|public
name|int
name|getProxyPort
parameter_list|()
block|{
return|return
name|proxyPort
return|;
block|}
comment|/**      * The proxy port number      */
DECL|method|setProxyPort (int proxyPort)
specifier|public
name|void
name|setProxyPort
parameter_list|(
name|int
name|proxyPort
parameter_list|)
block|{
name|this
operator|.
name|proxyPort
operator|=
name|proxyPort
expr_stmt|;
block|}
DECL|method|getAuthMethodPriority ()
specifier|public
name|String
name|getAuthMethodPriority
parameter_list|()
block|{
return|return
name|authMethodPriority
return|;
block|}
comment|/**      * Authentication method for proxy, either as Basic, Digest or NTLM.      */
DECL|method|setAuthMethodPriority (String authMethodPriority)
specifier|public
name|void
name|setAuthMethodPriority
parameter_list|(
name|String
name|authMethodPriority
parameter_list|)
block|{
name|this
operator|.
name|authMethodPriority
operator|=
name|authMethodPriority
expr_stmt|;
block|}
DECL|method|isTransferException ()
specifier|public
name|boolean
name|isTransferException
parameter_list|()
block|{
return|return
name|transferException
return|;
block|}
comment|/**      * Option to disable throwing the HttpOperationFailedException in case of failed responses from the remote server.      * This allows you to get all responses regardless of the HTTP status code.      */
DECL|method|setTransferException (boolean transferException)
specifier|public
name|void
name|setTransferException
parameter_list|(
name|boolean
name|transferException
parameter_list|)
block|{
name|this
operator|.
name|transferException
operator|=
name|transferException
expr_stmt|;
block|}
DECL|method|isTraceEnabled ()
specifier|public
name|boolean
name|isTraceEnabled
parameter_list|()
block|{
return|return
name|this
operator|.
name|traceEnabled
return|;
block|}
comment|/**      * Specifies whether to enable HTTP TRACE for this Jetty consumer. By default TRACE is turned off.      */
DECL|method|setTraceEnabled (boolean traceEnabled)
specifier|public
name|void
name|setTraceEnabled
parameter_list|(
name|boolean
name|traceEnabled
parameter_list|)
block|{
name|this
operator|.
name|traceEnabled
operator|=
name|traceEnabled
expr_stmt|;
block|}
DECL|method|getHttpMethodRestrict ()
specifier|public
name|String
name|getHttpMethodRestrict
parameter_list|()
block|{
return|return
name|httpMethodRestrict
return|;
block|}
comment|/**      * Used to only allow consuming if the HttpMethod matches, such as GET/POST/PUT etc.      * Multiple methods can be specified separated by comma.      */
DECL|method|setHttpMethodRestrict (String httpMethodRestrict)
specifier|public
name|void
name|setHttpMethodRestrict
parameter_list|(
name|String
name|httpMethodRestrict
parameter_list|)
block|{
name|this
operator|.
name|httpMethodRestrict
operator|=
name|httpMethodRestrict
expr_stmt|;
block|}
DECL|method|getUrlRewrite ()
specifier|public
name|UrlRewrite
name|getUrlRewrite
parameter_list|()
block|{
return|return
name|urlRewrite
return|;
block|}
comment|/**      * Refers to a custom org.apache.camel.component.http.UrlRewrite which allows you to rewrite urls when you bridge/proxy endpoints.      * See more details at http://camel.apache.org/urlrewrite.html      */
DECL|method|setUrlRewrite (UrlRewrite urlRewrite)
specifier|public
name|void
name|setUrlRewrite
parameter_list|(
name|UrlRewrite
name|urlRewrite
parameter_list|)
block|{
name|this
operator|.
name|urlRewrite
operator|=
name|urlRewrite
expr_stmt|;
block|}
DECL|method|getResponseBufferSize ()
specifier|public
name|Integer
name|getResponseBufferSize
parameter_list|()
block|{
return|return
name|responseBufferSize
return|;
block|}
comment|/**      * To use a custom buffer size on the javax.servlet.ServletResponse.      */
DECL|method|setResponseBufferSize (Integer responseBufferSize)
specifier|public
name|void
name|setResponseBufferSize
parameter_list|(
name|Integer
name|responseBufferSize
parameter_list|)
block|{
name|this
operator|.
name|responseBufferSize
operator|=
name|responseBufferSize
expr_stmt|;
block|}
DECL|method|isIgnoreResponseBody ()
specifier|public
name|boolean
name|isIgnoreResponseBody
parameter_list|()
block|{
return|return
name|ignoreResponseBody
return|;
block|}
comment|/**      * If this option is true, The http producer won't read response body and cache the input stream.      */
DECL|method|setIgnoreResponseBody (boolean ignoreResponseBody)
specifier|public
name|void
name|setIgnoreResponseBody
parameter_list|(
name|boolean
name|ignoreResponseBody
parameter_list|)
block|{
name|this
operator|.
name|ignoreResponseBody
operator|=
name|ignoreResponseBody
expr_stmt|;
block|}
comment|/**      * If this option is true then IN exchange headers will be copied to OUT exchange headers according to copy strategy.      * Setting this to false, allows to only include the headers from the HTTP response (not propagating IN headers).      */
DECL|method|isCopyHeaders ()
specifier|public
name|boolean
name|isCopyHeaders
parameter_list|()
block|{
return|return
name|copyHeaders
return|;
block|}
DECL|method|setCopyHeaders (boolean copyHeaders)
specifier|public
name|void
name|setCopyHeaders
parameter_list|(
name|boolean
name|copyHeaders
parameter_list|)
block|{
name|this
operator|.
name|copyHeaders
operator|=
name|copyHeaders
expr_stmt|;
block|}
DECL|method|isEagerCheckContentAvailable ()
specifier|public
name|boolean
name|isEagerCheckContentAvailable
parameter_list|()
block|{
return|return
name|eagerCheckContentAvailable
return|;
block|}
comment|/**      * Whether to eager check whether the HTTP requests has content if the content-length header is 0 or not present.      * This can be turned on in case HTTP clients do not send streamed data.      */
DECL|method|setEagerCheckContentAvailable (boolean eagerCheckContentAvailable)
specifier|public
name|void
name|setEagerCheckContentAvailable
parameter_list|(
name|boolean
name|eagerCheckContentAvailable
parameter_list|)
block|{
name|this
operator|.
name|eagerCheckContentAvailable
operator|=
name|eagerCheckContentAvailable
expr_stmt|;
block|}
DECL|method|getOkStatusCodeRange ()
specifier|public
name|String
name|getOkStatusCodeRange
parameter_list|()
block|{
return|return
name|okStatusCodeRange
return|;
block|}
comment|/**      * The status codes which is considered a success response. The values are inclusive. The range must be defined as from-to with the dash included.      *<p/>      * The default range is<tt>200-299</tt>      */
DECL|method|setOkStatusCodeRange (String okStatusCodeRange)
specifier|public
name|void
name|setOkStatusCodeRange
parameter_list|(
name|String
name|okStatusCodeRange
parameter_list|)
block|{
name|this
operator|.
name|okStatusCodeRange
operator|=
name|okStatusCodeRange
expr_stmt|;
block|}
block|}
end_class

end_unit

