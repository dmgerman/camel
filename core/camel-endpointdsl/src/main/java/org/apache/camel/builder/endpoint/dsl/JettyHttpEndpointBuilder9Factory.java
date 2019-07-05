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
comment|/**  * The jetty component provides HTTP-based endpoints for consuming and producing  * HTTP requests.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_interface
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.EndpointDslMojo"
argument_list|)
DECL|interface|JettyHttpEndpointBuilder9Factory
specifier|public
interface|interface
name|JettyHttpEndpointBuilder9Factory
block|{
comment|/**      * Builder for endpoint for the Jetty component.      */
DECL|interface|JettyHttpEndpointBuilder9
specifier|public
interface|interface
name|JettyHttpEndpointBuilder9
extends|extends
name|EndpointConsumerBuilder
block|{
DECL|method|advanced ()
specifier|default
name|AdvancedJettyHttpEndpointBuilder9
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedJettyHttpEndpointBuilder9
operator|)
name|this
return|;
block|}
comment|/**          * Determines whether or not the raw input stream from Servlet is cached          * or not (Camel will read the stream into a in memory/overflow to file,          * Stream caching) cache. By default Camel will cache the Servlet input          * stream to support reading it multiple times to ensure it Camel can          * retrieve all data from the stream. However you can set this option to          * true when you for example need to access the raw stream, such as          * streaming it directly to a file or other persistent store.          * DefaultHttpBinding will copy the request input stream into a stream          * cache and put it into message body if this option is false to support          * reading the stream multiple times. If you use Servlet to bridge/proxy          * an endpoint then consider enabling this option to improve          * performance, in case you do not need to read the message payload          * multiple times. The http/http4 producer will by default cache the          * response body stream. If setting this option to true, then the          * producers will not cache the response body stream but use the          * response stream as-is as the message body.          *           * The option is a:<code>boolean</code> type.          *           * Group: common          */
DECL|method|disableStreamCache ( boolean disableStreamCache)
specifier|default
name|JettyHttpEndpointBuilder9
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
comment|/**          * Determines whether or not the raw input stream from Servlet is cached          * or not (Camel will read the stream into a in memory/overflow to file,          * Stream caching) cache. By default Camel will cache the Servlet input          * stream to support reading it multiple times to ensure it Camel can          * retrieve all data from the stream. However you can set this option to          * true when you for example need to access the raw stream, such as          * streaming it directly to a file or other persistent store.          * DefaultHttpBinding will copy the request input stream into a stream          * cache and put it into message body if this option is false to support          * reading the stream multiple times. If you use Servlet to bridge/proxy          * an endpoint then consider enabling this option to improve          * performance, in case you do not need to read the message payload          * multiple times. The http/http4 producer will by default cache the          * response body stream. If setting this option to true, then the          * producers will not cache the response body stream but use the          * response stream as-is as the message body.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: common          */
DECL|method|disableStreamCache ( String disableStreamCache)
specifier|default
name|JettyHttpEndpointBuilder9
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
comment|/**          * To use a custom HeaderFilterStrategy to filter header to and from          * Camel message.          *           * The option is a:          *<code>org.apache.camel.spi.HeaderFilterStrategy</code> type.          *           * Group: common          */
DECL|method|headerFilterStrategy ( HeaderFilterStrategy headerFilterStrategy)
specifier|default
name|JettyHttpEndpointBuilder9
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
comment|/**          * To use a custom HeaderFilterStrategy to filter header to and from          * Camel message.          *           * The option will be converted to a          *<code>org.apache.camel.spi.HeaderFilterStrategy</code> type.          *           * Group: common          */
DECL|method|headerFilterStrategy ( String headerFilterStrategy)
specifier|default
name|JettyHttpEndpointBuilder9
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
comment|/**          * If this option is false the Servlet will disable the HTTP streaming          * and set the content-length header on the response.          *           * The option is a:<code>boolean</code> type.          *           * Group: consumer          */
DECL|method|chunked (boolean chunked)
specifier|default
name|JettyHttpEndpointBuilder9
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
comment|/**          * If this option is false the Servlet will disable the HTTP streaming          * and set the content-length header on the response.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: consumer          */
DECL|method|chunked (String chunked)
specifier|default
name|JettyHttpEndpointBuilder9
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
comment|/**          * Whether Jetty org.eclipse.jetty.servlets.MultiPartFilter is enabled          * or not. You should set this value to false when bridging endpoints,          * to ensure multipart requests is proxied/bridged as well.          *           * The option is a:<code>boolean</code> type.          *           * Group: consumer          */
DECL|method|enableMultipartFilter ( boolean enableMultipartFilter)
specifier|default
name|JettyHttpEndpointBuilder9
name|enableMultipartFilter
parameter_list|(
name|boolean
name|enableMultipartFilter
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"enableMultipartFilter"
argument_list|,
name|enableMultipartFilter
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether Jetty org.eclipse.jetty.servlets.MultiPartFilter is enabled          * or not. You should set this value to false when bridging endpoints,          * to ensure multipart requests is proxied/bridged as well.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: consumer          */
DECL|method|enableMultipartFilter ( String enableMultipartFilter)
specifier|default
name|JettyHttpEndpointBuilder9
name|enableMultipartFilter
parameter_list|(
name|String
name|enableMultipartFilter
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"enableMultipartFilter"
argument_list|,
name|enableMultipartFilter
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * If enabled and an Exchange failed processing on the consumer side,          * and if the caused Exception was send back serialized in the response          * as a application/x-java-serialized-object content type. On the          * producer side the exception will be deserialized and thrown as is,          * instead of the HttpOperationFailedException. The caused exception is          * required to be serialized. This is by default turned off. If you          * enable this then be aware that Java will deserialize the incoming          * data from the request to Java and that can be a potential security          * risk.          *           * The option is a:<code>boolean</code> type.          *           * Group: consumer          */
DECL|method|transferException ( boolean transferException)
specifier|default
name|JettyHttpEndpointBuilder9
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
comment|/**          * If enabled and an Exchange failed processing on the consumer side,          * and if the caused Exception was send back serialized in the response          * as a application/x-java-serialized-object content type. On the          * producer side the exception will be deserialized and thrown as is,          * instead of the HttpOperationFailedException. The caused exception is          * required to be serialized. This is by default turned off. If you          * enable this then be aware that Java will deserialize the incoming          * data from the request to Java and that can be a potential security          * risk.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: consumer          */
DECL|method|transferException ( String transferException)
specifier|default
name|JettyHttpEndpointBuilder9
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
comment|/**          * To configure security using SSLContextParameters.          *           * The option is a:          *<code>org.apache.camel.support.jsse.SSLContextParameters</code> type.          *           * Group: security          */
DECL|method|sslContextParameters ( Object sslContextParameters)
specifier|default
name|JettyHttpEndpointBuilder9
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
comment|/**          * To configure security using SSLContextParameters.          *           * The option will be converted to a          *<code>org.apache.camel.support.jsse.SSLContextParameters</code> type.          *           * Group: security          */
DECL|method|sslContextParameters ( String sslContextParameters)
specifier|default
name|JettyHttpEndpointBuilder9
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
block|}
comment|/**      * Advanced builder for endpoint for the Jetty component.      */
DECL|interface|AdvancedJettyHttpEndpointBuilder9
specifier|public
interface|interface
name|AdvancedJettyHttpEndpointBuilder9
extends|extends
name|EndpointConsumerBuilder
block|{
DECL|method|basic ()
specifier|default
name|JettyHttpEndpointBuilder9
name|basic
parameter_list|()
block|{
return|return
operator|(
name|JettyHttpEndpointBuilder9
operator|)
name|this
return|;
block|}
comment|/**          * To use a custom HttpBinding to control the mapping between Camel          * message and HttpClient.          *           * The option is a:          *<code>org.apache.camel.http.common.HttpBinding</code> type.          *           * Group: common (advanced)          */
DECL|method|httpBinding (Object httpBinding)
specifier|default
name|AdvancedJettyHttpEndpointBuilder9
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
comment|/**          * To use a custom HttpBinding to control the mapping between Camel          * message and HttpClient.          *           * The option will be converted to a          *<code>org.apache.camel.http.common.HttpBinding</code> type.          *           * Group: common (advanced)          */
DECL|method|httpBinding (String httpBinding)
specifier|default
name|AdvancedJettyHttpEndpointBuilder9
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
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          *           * The option is a:<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|basicPropertyBinding ( boolean basicPropertyBinding)
specifier|default
name|AdvancedJettyHttpEndpointBuilder9
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
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|basicPropertyBinding ( String basicPropertyBinding)
specifier|default
name|AdvancedJettyHttpEndpointBuilder9
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
comment|/**          * If this option is true then IN exchange Body of the exchange will be          * mapped to HTTP body. Setting this to false will avoid the HTTP          * mapping.          *           * The option is a:<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|mapHttpMessageBody ( boolean mapHttpMessageBody)
specifier|default
name|AdvancedJettyHttpEndpointBuilder9
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
comment|/**          * If this option is true then IN exchange Body of the exchange will be          * mapped to HTTP body. Setting this to false will avoid the HTTP          * mapping.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|mapHttpMessageBody ( String mapHttpMessageBody)
specifier|default
name|AdvancedJettyHttpEndpointBuilder9
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
comment|/**          * If this option is true then IN exchange Form Encoded body of the          * exchange will be mapped to HTTP. Setting this to false will avoid the          * HTTP Form Encoded body mapping.          *           * The option is a:<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|mapHttpMessageFormUrlEncodedBody ( boolean mapHttpMessageFormUrlEncodedBody)
specifier|default
name|AdvancedJettyHttpEndpointBuilder9
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
comment|/**          * If this option is true then IN exchange Form Encoded body of the          * exchange will be mapped to HTTP. Setting this to false will avoid the          * HTTP Form Encoded body mapping.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|mapHttpMessageFormUrlEncodedBody ( String mapHttpMessageFormUrlEncodedBody)
specifier|default
name|AdvancedJettyHttpEndpointBuilder9
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
comment|/**          * If this option is true then IN exchange Headers of the exchange will          * be mapped to HTTP headers. Setting this to false will avoid the HTTP          * Headers mapping.          *           * The option is a:<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|mapHttpMessageHeaders ( boolean mapHttpMessageHeaders)
specifier|default
name|AdvancedJettyHttpEndpointBuilder9
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
comment|/**          * If this option is true then IN exchange Headers of the exchange will          * be mapped to HTTP headers. Setting this to false will avoid the HTTP          * Headers mapping.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|mapHttpMessageHeaders ( String mapHttpMessageHeaders)
specifier|default
name|AdvancedJettyHttpEndpointBuilder9
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
comment|/**          * Sets whether synchronous processing should be strictly used, or Camel          * is allowed to use asynchronous processing (if supported).          *           * The option is a:<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|synchronous ( boolean synchronous)
specifier|default
name|AdvancedJettyHttpEndpointBuilder9
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
comment|/**          * Sets whether synchronous processing should be strictly used, or Camel          * is allowed to use asynchronous processing (if supported).          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|synchronous (String synchronous)
specifier|default
name|AdvancedJettyHttpEndpointBuilder9
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
block|}
comment|/**      * Jetty (camel-jetty)      * The jetty component provides HTTP-based endpoints for consuming and      * producing HTTP requests.      *       * Syntax:<code>jetty:httpUri</code>      * Category: http      * Available as of version: 1.2      * Maven coordinates: org.apache.camel:camel-jetty      */
DECL|method|jettyHttp9 (String path)
specifier|default
name|JettyHttpEndpointBuilder9
name|jettyHttp9
parameter_list|(
name|String
name|path
parameter_list|)
block|{
class|class
name|JettyHttpEndpointBuilder9Impl
extends|extends
name|AbstractEndpointBuilder
implements|implements
name|JettyHttpEndpointBuilder9
implements|,
name|AdvancedJettyHttpEndpointBuilder9
block|{
specifier|public
name|JettyHttpEndpointBuilder9Impl
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|super
argument_list|(
literal|"jetty"
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|new
name|JettyHttpEndpointBuilder9Impl
argument_list|(
name|path
argument_list|)
return|;
block|}
block|}
end_interface

end_unit

