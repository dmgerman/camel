begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.builder.endpoint.dsl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
operator|.
name|endpoint
operator|.
name|dsl
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
name|builder
operator|.
name|EndpointConsumerBuilder
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
name|builder
operator|.
name|EndpointProducerBuilder
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
name|builder
operator|.
name|endpoint
operator|.
name|AbstractEndpointBuilder
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

begin_comment
comment|/**  * For calling out to external HTTP servers using Apache HTTP Client 4.x.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_interface
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.EndpointDslMojo"
argument_list|)
DECL|interface|HttpEndpointBuilderFactory
specifier|public
interface|interface
name|HttpEndpointBuilderFactory
block|{
comment|/**      * Builder for endpoint for the HTTP4 component.      */
DECL|interface|HttpEndpointBuilder
specifier|public
interface|interface
name|HttpEndpointBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|advanced ()
specifier|default
name|AdvancedHttpEndpointBuilder
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedHttpEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * The url of the HTTP endpoint to call.          * The option is a<code>java.net.URI</code> type.          * @group common          */
DECL|method|httpUri (URI httpUri)
specifier|default
name|HttpEndpointBuilder
name|httpUri
parameter_list|(
name|URI
name|httpUri
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"httpUri"
argument_list|,
name|httpUri
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The url of the HTTP endpoint to call.          * The option will be converted to a<code>java.net.URI</code> type.          * @group common          */
DECL|method|httpUri (String httpUri)
specifier|default
name|HttpEndpointBuilder
name|httpUri
parameter_list|(
name|String
name|httpUri
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"httpUri"
argument_list|,
name|httpUri
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Determines whether or not the raw input stream from Servlet is cached          * or not (Camel will read the stream into a in memory/overflow to file,          * Stream caching) cache. By default Camel will cache the Servlet input          * stream to support reading it multiple times to ensure it Camel can          * retrieve all data from the stream. However you can set this option to          * true when you for example need to access the raw stream, such as          * streaming it directly to a file or other persistent store.          * DefaultHttpBinding will copy the request input stream into a stream          * cache and put it into message body if this option is false to support          * reading the stream multiple times. If you use Servlet to bridge/proxy          * an endpoint then consider enabling this option to improve          * performance, in case you do not need to read the message payload          * multiple times. The http/http4 producer will by default cache the          * response body stream. If setting this option to true, then the          * producers will not cache the response body stream but use the          * response stream as-is as the message body.          * The option is a<code>boolean</code> type.          * @group common          */
DECL|method|disableStreamCache ( boolean disableStreamCache)
specifier|default
name|HttpEndpointBuilder
name|disableStreamCache
parameter_list|(
name|boolean
name|disableStreamCache
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"disableStreamCache"
argument_list|,
name|disableStreamCache
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Determines whether or not the raw input stream from Servlet is cached          * or not (Camel will read the stream into a in memory/overflow to file,          * Stream caching) cache. By default Camel will cache the Servlet input          * stream to support reading it multiple times to ensure it Camel can          * retrieve all data from the stream. However you can set this option to          * true when you for example need to access the raw stream, such as          * streaming it directly to a file or other persistent store.          * DefaultHttpBinding will copy the request input stream into a stream          * cache and put it into message body if this option is false to support          * reading the stream multiple times. If you use Servlet to bridge/proxy          * an endpoint then consider enabling this option to improve          * performance, in case you do not need to read the message payload          * multiple times. The http/http4 producer will by default cache the          * response body stream. If setting this option to true, then the          * producers will not cache the response body stream but use the          * response stream as-is as the message body.          * The option will be converted to a<code>boolean</code> type.          * @group common          */
DECL|method|disableStreamCache (String disableStreamCache)
specifier|default
name|HttpEndpointBuilder
name|disableStreamCache
parameter_list|(
name|String
name|disableStreamCache
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"disableStreamCache"
argument_list|,
name|disableStreamCache
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * To use a custom HeaderFilterStrategy to filter header to and from          * Camel message.          * The option is a          *<code>org.apache.camel.spi.HeaderFilterStrategy</code> type.          * @group common          */
DECL|method|headerFilterStrategy ( HeaderFilterStrategy headerFilterStrategy)
specifier|default
name|HttpEndpointBuilder
name|headerFilterStrategy
parameter_list|(
name|HeaderFilterStrategy
name|headerFilterStrategy
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"headerFilterStrategy"
argument_list|,
name|headerFilterStrategy
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * To use a custom HeaderFilterStrategy to filter header to and from          * Camel message.          * The option will be converted to a          *<code>org.apache.camel.spi.HeaderFilterStrategy</code> type.          * @group common          */
DECL|method|headerFilterStrategy ( String headerFilterStrategy)
specifier|default
name|HttpEndpointBuilder
name|headerFilterStrategy
parameter_list|(
name|String
name|headerFilterStrategy
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"headerFilterStrategy"
argument_list|,
name|headerFilterStrategy
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * If this option is false the Servlet will disable the HTTP streaming          * and set the content-length header on the response.          * The option is a<code>boolean</code> type.          * @group producer          */
DECL|method|chunked (boolean chunked)
specifier|default
name|HttpEndpointBuilder
name|chunked
parameter_list|(
name|boolean
name|chunked
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"chunked"
argument_list|,
name|chunked
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * If this option is false the Servlet will disable the HTTP streaming          * and set the content-length header on the response.          * The option will be converted to a<code>boolean</code> type.          * @group producer          */
DECL|method|chunked (String chunked)
specifier|default
name|HttpEndpointBuilder
name|chunked
parameter_list|(
name|String
name|chunked
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"chunked"
argument_list|,
name|chunked
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * If enabled and an Exchange failed processing on the consumer side,          * and if the caused Exception was send back serialized in the response          * as a application/x-java-serialized-object content type. On the          * producer side the exception will be deserialized and thrown as is,          * instead of the HttpOperationFailedException. The caused exception is          * required to be serialized. This is by default turned off. If you          * enable this then be aware that Java will deserialize the incoming          * data from the request to Java and that can be a potential security          * risk.          * The option is a<code>boolean</code> type.          * @group producer          */
DECL|method|transferException (boolean transferException)
specifier|default
name|HttpEndpointBuilder
name|transferException
parameter_list|(
name|boolean
name|transferException
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"transferException"
argument_list|,
name|transferException
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * If enabled and an Exchange failed processing on the consumer side,          * and if the caused Exception was send back serialized in the response          * as a application/x-java-serialized-object content type. On the          * producer side the exception will be deserialized and thrown as is,          * instead of the HttpOperationFailedException. The caused exception is          * required to be serialized. This is by default turned off. If you          * enable this then be aware that Java will deserialize the incoming          * data from the request to Java and that can be a potential security          * risk.          * The option will be converted to a<code>boolean</code> type.          * @group producer          */
DECL|method|transferException (String transferException)
specifier|default
name|HttpEndpointBuilder
name|transferException
parameter_list|(
name|String
name|transferException
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"transferException"
argument_list|,
name|transferException
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * To configure security using SSLContextParameters. Important: Only one          * instance of org.apache.camel.util.jsse.SSLContextParameters is          * supported per HttpComponent. If you need to use 2 or more different          * instances, you need to define a new HttpComponent per instance you          * need.          * The option is a          *<code>org.apache.camel.support.jsse.SSLContextParameters</code> type.          * @group security          */
DECL|method|sslContextParameters ( Object sslContextParameters)
specifier|default
name|HttpEndpointBuilder
name|sslContextParameters
parameter_list|(
name|Object
name|sslContextParameters
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"sslContextParameters"
argument_list|,
name|sslContextParameters
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * To configure security using SSLContextParameters. Important: Only one          * instance of org.apache.camel.util.jsse.SSLContextParameters is          * supported per HttpComponent. If you need to use 2 or more different          * instances, you need to define a new HttpComponent per instance you          * need.          * The option will be converted to a          *<code>org.apache.camel.support.jsse.SSLContextParameters</code> type.          * @group security          */
DECL|method|sslContextParameters ( String sslContextParameters)
specifier|default
name|HttpEndpointBuilder
name|sslContextParameters
parameter_list|(
name|String
name|sslContextParameters
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"sslContextParameters"
argument_list|,
name|sslContextParameters
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * To use a custom X509HostnameVerifier such as DefaultHostnameVerifier          * or org.apache.http.conn.ssl.NoopHostnameVerifier.          * The option is a<code>javax.net.ssl.HostnameVerifier</code> type.          * @group security          */
DECL|method|x509HostnameVerifier ( Object x509HostnameVerifier)
specifier|default
name|HttpEndpointBuilder
name|x509HostnameVerifier
parameter_list|(
name|Object
name|x509HostnameVerifier
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"x509HostnameVerifier"
argument_list|,
name|x509HostnameVerifier
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * To use a custom X509HostnameVerifier such as DefaultHostnameVerifier          * or org.apache.http.conn.ssl.NoopHostnameVerifier.          * The option will be converted to a          *<code>javax.net.ssl.HostnameVerifier</code> type.          * @group security          */
DECL|method|x509HostnameVerifier ( String x509HostnameVerifier)
specifier|default
name|HttpEndpointBuilder
name|x509HostnameVerifier
parameter_list|(
name|String
name|x509HostnameVerifier
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"x509HostnameVerifier"
argument_list|,
name|x509HostnameVerifier
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
comment|/**      * Advanced builder for endpoint for the HTTP4 component.      */
DECL|interface|AdvancedHttpEndpointBuilder
specifier|public
interface|interface
name|AdvancedHttpEndpointBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|basic ()
specifier|default
name|HttpEndpointBuilder
name|basic
parameter_list|()
block|{
return|return
operator|(
name|HttpEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * To use a custom HttpBinding to control the mapping between Camel          * message and HttpClient.          * The option is a<code>org.apache.camel.http.common.HttpBinding</code>          * type.          * @group common (advanced)          */
DECL|method|httpBinding (Object httpBinding)
specifier|default
name|AdvancedHttpEndpointBuilder
name|httpBinding
parameter_list|(
name|Object
name|httpBinding
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"httpBinding"
argument_list|,
name|httpBinding
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * To use a custom HttpBinding to control the mapping between Camel          * message and HttpClient.          * The option will be converted to a          *<code>org.apache.camel.http.common.HttpBinding</code> type.          * @group common (advanced)          */
DECL|method|httpBinding (String httpBinding)
specifier|default
name|AdvancedHttpEndpointBuilder
name|httpBinding
parameter_list|(
name|String
name|httpBinding
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"httpBinding"
argument_list|,
name|httpBinding
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          * The option is a<code>boolean</code> type.          * @group advanced          */
DECL|method|basicPropertyBinding ( boolean basicPropertyBinding)
specifier|default
name|AdvancedHttpEndpointBuilder
name|basicPropertyBinding
parameter_list|(
name|boolean
name|basicPropertyBinding
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"basicPropertyBinding"
argument_list|,
name|basicPropertyBinding
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          * The option will be converted to a<code>boolean</code> type.          * @group advanced          */
DECL|method|basicPropertyBinding ( String basicPropertyBinding)
specifier|default
name|AdvancedHttpEndpointBuilder
name|basicPropertyBinding
parameter_list|(
name|String
name|basicPropertyBinding
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"basicPropertyBinding"
argument_list|,
name|basicPropertyBinding
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Provide access to the http client request parameters used on new          * RequestConfig instances used by producers or consumers of this          * endpoint.          * The option is a          *<code>org.apache.http.impl.client.HttpClientBuilder</code> type.          * @group advanced          */
DECL|method|clientBuilder (Object clientBuilder)
specifier|default
name|AdvancedHttpEndpointBuilder
name|clientBuilder
parameter_list|(
name|Object
name|clientBuilder
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"clientBuilder"
argument_list|,
name|clientBuilder
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Provide access to the http client request parameters used on new          * RequestConfig instances used by producers or consumers of this          * endpoint.          * The option will be converted to a          *<code>org.apache.http.impl.client.HttpClientBuilder</code> type.          * @group advanced          */
DECL|method|clientBuilder (String clientBuilder)
specifier|default
name|AdvancedHttpEndpointBuilder
name|clientBuilder
parameter_list|(
name|String
name|clientBuilder
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"clientBuilder"
argument_list|,
name|clientBuilder
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * To use a custom HttpClientConnectionManager to manage connections.          * The option is a          *<code>org.apache.http.conn.HttpClientConnectionManager</code> type.          * @group advanced          */
DECL|method|clientConnectionManager ( Object clientConnectionManager)
specifier|default
name|AdvancedHttpEndpointBuilder
name|clientConnectionManager
parameter_list|(
name|Object
name|clientConnectionManager
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"clientConnectionManager"
argument_list|,
name|clientConnectionManager
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * To use a custom HttpClientConnectionManager to manage connections.          * The option will be converted to a          *<code>org.apache.http.conn.HttpClientConnectionManager</code> type.          * @group advanced          */
DECL|method|clientConnectionManager ( String clientConnectionManager)
specifier|default
name|AdvancedHttpEndpointBuilder
name|clientConnectionManager
parameter_list|(
name|String
name|clientConnectionManager
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"clientConnectionManager"
argument_list|,
name|clientConnectionManager
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The maximum number of connections per route.          * The option is a<code>int</code> type.          * @group advanced          */
DECL|method|connectionsPerRoute ( int connectionsPerRoute)
specifier|default
name|AdvancedHttpEndpointBuilder
name|connectionsPerRoute
parameter_list|(
name|int
name|connectionsPerRoute
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"connectionsPerRoute"
argument_list|,
name|connectionsPerRoute
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The maximum number of connections per route.          * The option will be converted to a<code>int</code> type.          * @group advanced          */
DECL|method|connectionsPerRoute ( String connectionsPerRoute)
specifier|default
name|AdvancedHttpEndpointBuilder
name|connectionsPerRoute
parameter_list|(
name|String
name|connectionsPerRoute
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"connectionsPerRoute"
argument_list|,
name|connectionsPerRoute
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets a custom HttpClient to be used by the producer.          * The option is a<code>org.apache.http.client.HttpClient</code> type.          * @group advanced          */
DECL|method|httpClient (Object httpClient)
specifier|default
name|AdvancedHttpEndpointBuilder
name|httpClient
parameter_list|(
name|Object
name|httpClient
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"httpClient"
argument_list|,
name|httpClient
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets a custom HttpClient to be used by the producer.          * The option will be converted to a          *<code>org.apache.http.client.HttpClient</code> type.          * @group advanced          */
DECL|method|httpClient (String httpClient)
specifier|default
name|AdvancedHttpEndpointBuilder
name|httpClient
parameter_list|(
name|String
name|httpClient
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"httpClient"
argument_list|,
name|httpClient
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Register a custom configuration strategy for new HttpClient instances          * created by producers or consumers such as to configure authentication          * mechanisms etc.          * The option is a          *<code>org.apache.camel.component.http4.HttpClientConfigurer</code>          * type.          * @group advanced          */
DECL|method|httpClientConfigurer ( Object httpClientConfigurer)
specifier|default
name|AdvancedHttpEndpointBuilder
name|httpClientConfigurer
parameter_list|(
name|Object
name|httpClientConfigurer
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"httpClientConfigurer"
argument_list|,
name|httpClientConfigurer
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Register a custom configuration strategy for new HttpClient instances          * created by producers or consumers such as to configure authentication          * mechanisms etc.          * The option will be converted to a          *<code>org.apache.camel.component.http4.HttpClientConfigurer</code>          * type.          * @group advanced          */
DECL|method|httpClientConfigurer ( String httpClientConfigurer)
specifier|default
name|AdvancedHttpEndpointBuilder
name|httpClientConfigurer
parameter_list|(
name|String
name|httpClientConfigurer
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"httpClientConfigurer"
argument_list|,
name|httpClientConfigurer
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * To configure the HttpClient using the key/values from the Map.          * The option is a<code>java.util.Map&lt;java.lang.String,          * java.lang.Object&gt;</code> type.          * @group advanced          */
DECL|method|httpClientOptions ( Map<String, Object> httpClientOptions)
specifier|default
name|AdvancedHttpEndpointBuilder
name|httpClientOptions
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|httpClientOptions
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"httpClientOptions"
argument_list|,
name|httpClientOptions
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * To configure the HttpClient using the key/values from the Map.          * The option will be converted to a          *<code>java.util.Map&lt;java.lang.String, java.lang.Object&gt;</code>          * type.          * @group advanced          */
DECL|method|httpClientOptions ( String httpClientOptions)
specifier|default
name|AdvancedHttpEndpointBuilder
name|httpClientOptions
parameter_list|(
name|String
name|httpClientOptions
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"httpClientOptions"
argument_list|,
name|httpClientOptions
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * To use a custom HttpContext instance.          * The option is a<code>org.apache.http.protocol.HttpContext</code>          * type.          * @group advanced          */
DECL|method|httpContext (Object httpContext)
specifier|default
name|AdvancedHttpEndpointBuilder
name|httpContext
parameter_list|(
name|Object
name|httpContext
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"httpContext"
argument_list|,
name|httpContext
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * To use a custom HttpContext instance.          * The option will be converted to a          *<code>org.apache.http.protocol.HttpContext</code> type.          * @group advanced          */
DECL|method|httpContext (String httpContext)
specifier|default
name|AdvancedHttpEndpointBuilder
name|httpContext
parameter_list|(
name|String
name|httpContext
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"httpContext"
argument_list|,
name|httpContext
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * If this option is true then IN exchange Body of the exchange will be          * mapped to HTTP body. Setting this to false will avoid the HTTP          * mapping.          * The option is a<code>boolean</code> type.          * @group advanced          */
DECL|method|mapHttpMessageBody ( boolean mapHttpMessageBody)
specifier|default
name|AdvancedHttpEndpointBuilder
name|mapHttpMessageBody
parameter_list|(
name|boolean
name|mapHttpMessageBody
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"mapHttpMessageBody"
argument_list|,
name|mapHttpMessageBody
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * If this option is true then IN exchange Body of the exchange will be          * mapped to HTTP body. Setting this to false will avoid the HTTP          * mapping.          * The option will be converted to a<code>boolean</code> type.          * @group advanced          */
DECL|method|mapHttpMessageBody ( String mapHttpMessageBody)
specifier|default
name|AdvancedHttpEndpointBuilder
name|mapHttpMessageBody
parameter_list|(
name|String
name|mapHttpMessageBody
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"mapHttpMessageBody"
argument_list|,
name|mapHttpMessageBody
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * If this option is true then IN exchange Form Encoded body of the          * exchange will be mapped to HTTP. Setting this to false will avoid the          * HTTP Form Encoded body mapping.          * The option is a<code>boolean</code> type.          * @group advanced          */
DECL|method|mapHttpMessageFormUrlEncodedBody ( boolean mapHttpMessageFormUrlEncodedBody)
specifier|default
name|AdvancedHttpEndpointBuilder
name|mapHttpMessageFormUrlEncodedBody
parameter_list|(
name|boolean
name|mapHttpMessageFormUrlEncodedBody
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"mapHttpMessageFormUrlEncodedBody"
argument_list|,
name|mapHttpMessageFormUrlEncodedBody
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * If this option is true then IN exchange Form Encoded body of the          * exchange will be mapped to HTTP. Setting this to false will avoid the          * HTTP Form Encoded body mapping.          * The option will be converted to a<code>boolean</code> type.          * @group advanced          */
DECL|method|mapHttpMessageFormUrlEncodedBody ( String mapHttpMessageFormUrlEncodedBody)
specifier|default
name|AdvancedHttpEndpointBuilder
name|mapHttpMessageFormUrlEncodedBody
parameter_list|(
name|String
name|mapHttpMessageFormUrlEncodedBody
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"mapHttpMessageFormUrlEncodedBody"
argument_list|,
name|mapHttpMessageFormUrlEncodedBody
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * If this option is true then IN exchange Headers of the exchange will          * be mapped to HTTP headers. Setting this to false will avoid the HTTP          * Headers mapping.          * The option is a<code>boolean</code> type.          * @group advanced          */
DECL|method|mapHttpMessageHeaders ( boolean mapHttpMessageHeaders)
specifier|default
name|AdvancedHttpEndpointBuilder
name|mapHttpMessageHeaders
parameter_list|(
name|boolean
name|mapHttpMessageHeaders
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"mapHttpMessageHeaders"
argument_list|,
name|mapHttpMessageHeaders
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * If this option is true then IN exchange Headers of the exchange will          * be mapped to HTTP headers. Setting this to false will avoid the HTTP          * Headers mapping.          * The option will be converted to a<code>boolean</code> type.          * @group advanced          */
DECL|method|mapHttpMessageHeaders ( String mapHttpMessageHeaders)
specifier|default
name|AdvancedHttpEndpointBuilder
name|mapHttpMessageHeaders
parameter_list|(
name|String
name|mapHttpMessageHeaders
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"mapHttpMessageHeaders"
argument_list|,
name|mapHttpMessageHeaders
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The maximum number of connections.          * The option is a<code>int</code> type.          * @group advanced          */
DECL|method|maxTotalConnections ( int maxTotalConnections)
specifier|default
name|AdvancedHttpEndpointBuilder
name|maxTotalConnections
parameter_list|(
name|int
name|maxTotalConnections
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"maxTotalConnections"
argument_list|,
name|maxTotalConnections
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The maximum number of connections.          * The option will be converted to a<code>int</code> type.          * @group advanced          */
DECL|method|maxTotalConnections ( String maxTotalConnections)
specifier|default
name|AdvancedHttpEndpointBuilder
name|maxTotalConnections
parameter_list|(
name|String
name|maxTotalConnections
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"maxTotalConnections"
argument_list|,
name|maxTotalConnections
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets whether synchronous processing should be strictly used, or Camel          * is allowed to use asynchronous processing (if supported).          * The option is a<code>boolean</code> type.          * @group advanced          */
DECL|method|synchronous (boolean synchronous)
specifier|default
name|AdvancedHttpEndpointBuilder
name|synchronous
parameter_list|(
name|boolean
name|synchronous
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"synchronous"
argument_list|,
name|synchronous
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets whether synchronous processing should be strictly used, or Camel          * is allowed to use asynchronous processing (if supported).          * The option will be converted to a<code>boolean</code> type.          * @group advanced          */
DECL|method|synchronous (String synchronous)
specifier|default
name|AdvancedHttpEndpointBuilder
name|synchronous
parameter_list|(
name|String
name|synchronous
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"synchronous"
argument_list|,
name|synchronous
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * To use System Properties as fallback for configuration.          * The option is a<code>boolean</code> type.          * @group advanced          */
DECL|method|useSystemProperties ( boolean useSystemProperties)
specifier|default
name|AdvancedHttpEndpointBuilder
name|useSystemProperties
parameter_list|(
name|boolean
name|useSystemProperties
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"useSystemProperties"
argument_list|,
name|useSystemProperties
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * To use System Properties as fallback for configuration.          * The option will be converted to a<code>boolean</code> type.          * @group advanced          */
DECL|method|useSystemProperties ( String useSystemProperties)
specifier|default
name|AdvancedHttpEndpointBuilder
name|useSystemProperties
parameter_list|(
name|String
name|useSystemProperties
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"useSystemProperties"
argument_list|,
name|useSystemProperties
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
comment|/**      * Proxy enum for<code>org.apache.camel.http.common.HttpMethods</code>      * enum.      */
DECL|enum|HttpMethods
enum|enum
name|HttpMethods
block|{
DECL|enumConstant|GET
name|GET
block|,
DECL|enumConstant|POST
name|POST
block|,
DECL|enumConstant|PUT
name|PUT
block|,
DECL|enumConstant|DELETE
name|DELETE
block|,
DECL|enumConstant|HEAD
name|HEAD
block|,
DECL|enumConstant|OPTIONS
name|OPTIONS
block|,
DECL|enumConstant|TRACE
name|TRACE
block|,
DECL|enumConstant|PATCH
name|PATCH
block|;     }
comment|/**      * For calling out to external HTTP servers using Apache HTTP Client 4.x.      * Creates a builder to build endpoints for the HTTP4 component.      */
DECL|method|http (String path)
specifier|default
name|HttpEndpointBuilder
name|http
parameter_list|(
name|String
name|path
parameter_list|)
block|{
class|class
name|HttpEndpointBuilderImpl
extends|extends
name|AbstractEndpointBuilder
implements|implements
name|HttpEndpointBuilder
implements|,
name|AdvancedHttpEndpointBuilder
block|{
specifier|public
name|HttpEndpointBuilderImpl
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|super
argument_list|(
literal|"http"
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|new
name|HttpEndpointBuilderImpl
argument_list|(
name|path
argument_list|)
return|;
block|}
block|}
end_interface

end_unit

