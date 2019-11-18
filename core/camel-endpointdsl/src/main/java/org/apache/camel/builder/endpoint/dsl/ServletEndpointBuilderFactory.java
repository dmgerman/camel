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
name|ExchangePattern
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
name|ExceptionHandler
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
comment|/**  * To use a HTTP Servlet as entry for Camel routes when running in a servlet  * container.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_interface
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.EndpointDslMojo"
argument_list|)
DECL|interface|ServletEndpointBuilderFactory
specifier|public
interface|interface
name|ServletEndpointBuilderFactory
block|{
comment|/**      * Builder for endpoint for the Servlet component.      */
DECL|interface|ServletEndpointBuilder
specifier|public
interface|interface
name|ServletEndpointBuilder
extends|extends
name|EndpointConsumerBuilder
block|{
DECL|method|advanced ()
specifier|default
name|AdvancedServletEndpointBuilder
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedServletEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Determines whether or not the raw input stream from Servlet is cached          * or not (Camel will read the stream into a in memory/overflow to file,          * Stream caching) cache. By default Camel will cache the Servlet input          * stream to support reading it multiple times to ensure it Camel can          * retrieve all data from the stream. However you can set this option to          * true when you for example need to access the raw stream, such as          * streaming it directly to a file or other persistent store.          * DefaultHttpBinding will copy the request input stream into a stream          * cache and put it into message body if this option is false to support          * reading the stream multiple times. If you use Servlet to bridge/proxy          * an endpoint then consider enabling this option to improve          * performance, in case you do not need to read the message payload          * multiple times. The http producer will by default cache the response          * body stream. If setting this option to true, then the producers will          * not cache the response body stream but use the response stream as-is          * as the message body.          *           * The option is a:<code>boolean</code> type.          *           * Group: common          */
DECL|method|disableStreamCache ( boolean disableStreamCache)
specifier|default
name|ServletEndpointBuilder
name|disableStreamCache
parameter_list|(
name|boolean
name|disableStreamCache
parameter_list|)
block|{
name|doSetProperty
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
comment|/**          * Determines whether or not the raw input stream from Servlet is cached          * or not (Camel will read the stream into a in memory/overflow to file,          * Stream caching) cache. By default Camel will cache the Servlet input          * stream to support reading it multiple times to ensure it Camel can          * retrieve all data from the stream. However you can set this option to          * true when you for example need to access the raw stream, such as          * streaming it directly to a file or other persistent store.          * DefaultHttpBinding will copy the request input stream into a stream          * cache and put it into message body if this option is false to support          * reading the stream multiple times. If you use Servlet to bridge/proxy          * an endpoint then consider enabling this option to improve          * performance, in case you do not need to read the message payload          * multiple times. The http producer will by default cache the response          * body stream. If setting this option to true, then the producers will          * not cache the response body stream but use the response stream as-is          * as the message body.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: common          */
DECL|method|disableStreamCache ( String disableStreamCache)
specifier|default
name|ServletEndpointBuilder
name|disableStreamCache
parameter_list|(
name|String
name|disableStreamCache
parameter_list|)
block|{
name|doSetProperty
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
name|ServletEndpointBuilder
name|headerFilterStrategy
parameter_list|(
name|HeaderFilterStrategy
name|headerFilterStrategy
parameter_list|)
block|{
name|doSetProperty
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
name|ServletEndpointBuilder
name|headerFilterStrategy
parameter_list|(
name|String
name|headerFilterStrategy
parameter_list|)
block|{
name|doSetProperty
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
comment|/**          * Configure the consumer to work in async mode.          *           * The option is a:<code>boolean</code> type.          *           * Group: consumer          */
DECL|method|async (boolean async)
specifier|default
name|ServletEndpointBuilder
name|async
parameter_list|(
name|boolean
name|async
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"async"
argument_list|,
name|async
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Configure the consumer to work in async mode.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: consumer          */
DECL|method|async (String async)
specifier|default
name|ServletEndpointBuilder
name|async
parameter_list|(
name|String
name|async
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"async"
argument_list|,
name|async
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Allows for bridging the consumer to the Camel routing Error Handler,          * which mean any exceptions occurred while the consumer is trying to          * pickup incoming messages, or the likes, will now be processed as a          * message and handled by the routing Error Handler. By default the          * consumer will use the org.apache.camel.spi.ExceptionHandler to deal          * with exceptions, that will be logged at WARN or ERROR level and          * ignored.          *           * The option is a:<code>boolean</code> type.          *           * Group: consumer          */
DECL|method|bridgeErrorHandler ( boolean bridgeErrorHandler)
specifier|default
name|ServletEndpointBuilder
name|bridgeErrorHandler
parameter_list|(
name|boolean
name|bridgeErrorHandler
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"bridgeErrorHandler"
argument_list|,
name|bridgeErrorHandler
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Allows for bridging the consumer to the Camel routing Error Handler,          * which mean any exceptions occurred while the consumer is trying to          * pickup incoming messages, or the likes, will now be processed as a          * message and handled by the routing Error Handler. By default the          * consumer will use the org.apache.camel.spi.ExceptionHandler to deal          * with exceptions, that will be logged at WARN or ERROR level and          * ignored.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: consumer          */
DECL|method|bridgeErrorHandler ( String bridgeErrorHandler)
specifier|default
name|ServletEndpointBuilder
name|bridgeErrorHandler
parameter_list|(
name|String
name|bridgeErrorHandler
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"bridgeErrorHandler"
argument_list|,
name|bridgeErrorHandler
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * If this option is false the Servlet will disable the HTTP streaming          * and set the content-length header on the response.          *           * The option is a:<code>boolean</code> type.          *           * Group: consumer          */
DECL|method|chunked (boolean chunked)
specifier|default
name|ServletEndpointBuilder
name|chunked
parameter_list|(
name|boolean
name|chunked
parameter_list|)
block|{
name|doSetProperty
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
name|ServletEndpointBuilder
name|chunked
parameter_list|(
name|String
name|chunked
parameter_list|)
block|{
name|doSetProperty
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
comment|/**          * Used to only allow consuming if the HttpMethod matches, such as          * GET/POST/PUT etc. Multiple methods can be specified separated by          * comma.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: consumer          */
DECL|method|httpMethodRestrict ( String httpMethodRestrict)
specifier|default
name|ServletEndpointBuilder
name|httpMethodRestrict
parameter_list|(
name|String
name|httpMethodRestrict
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"httpMethodRestrict"
argument_list|,
name|httpMethodRestrict
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether or not the consumer should try to find a target consumer by          * matching the URI prefix if no exact match is found.          *           * The option is a:<code>boolean</code> type.          *           * Group: consumer          */
DECL|method|matchOnUriPrefix (boolean matchOnUriPrefix)
specifier|default
name|ServletEndpointBuilder
name|matchOnUriPrefix
parameter_list|(
name|boolean
name|matchOnUriPrefix
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"matchOnUriPrefix"
argument_list|,
name|matchOnUriPrefix
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether or not the consumer should try to find a target consumer by          * matching the URI prefix if no exact match is found.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: consumer          */
DECL|method|matchOnUriPrefix (String matchOnUriPrefix)
specifier|default
name|ServletEndpointBuilder
name|matchOnUriPrefix
parameter_list|(
name|String
name|matchOnUriPrefix
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"matchOnUriPrefix"
argument_list|,
name|matchOnUriPrefix
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * If enabled and an Exchange failed processing on the consumer side the          * response's body won't contain the exception's stack trace.          *           * The option is a:<code>boolean</code> type.          *           * Group: consumer          */
DECL|method|muteException (boolean muteException)
specifier|default
name|ServletEndpointBuilder
name|muteException
parameter_list|(
name|boolean
name|muteException
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"muteException"
argument_list|,
name|muteException
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * If enabled and an Exchange failed processing on the consumer side the          * response's body won't contain the exception's stack trace.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: consumer          */
DECL|method|muteException (String muteException)
specifier|default
name|ServletEndpointBuilder
name|muteException
parameter_list|(
name|String
name|muteException
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"muteException"
argument_list|,
name|muteException
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * To use a custom buffer size on the javax.servlet.ServletResponse.          *           * The option is a:<code>java.lang.Integer</code> type.          *           * Group: consumer          */
DECL|method|responseBufferSize ( Integer responseBufferSize)
specifier|default
name|ServletEndpointBuilder
name|responseBufferSize
parameter_list|(
name|Integer
name|responseBufferSize
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"responseBufferSize"
argument_list|,
name|responseBufferSize
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * To use a custom buffer size on the javax.servlet.ServletResponse.          *           * The option will be converted to a<code>java.lang.Integer</code>          * type.          *           * Group: consumer          */
DECL|method|responseBufferSize ( String responseBufferSize)
specifier|default
name|ServletEndpointBuilder
name|responseBufferSize
parameter_list|(
name|String
name|responseBufferSize
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"responseBufferSize"
argument_list|,
name|responseBufferSize
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Name of the servlet to use.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: consumer          */
DECL|method|servletName (String servletName)
specifier|default
name|ServletEndpointBuilder
name|servletName
parameter_list|(
name|String
name|servletName
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"servletName"
argument_list|,
name|servletName
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * If enabled and an Exchange failed processing on the consumer side,          * and if the caused Exception was send back serialized in the response          * as a application/x-java-serialized-object content type. On the          * producer side the exception will be deserialized and thrown as is,          * instead of the HttpOperationFailedException. The caused exception is          * required to be serialized. This is by default turned off. If you          * enable this then be aware that Java will deserialize the incoming          * data from the request to Java and that can be a potential security          * risk.          *           * The option is a:<code>boolean</code> type.          *           * Group: consumer          */
DECL|method|transferException ( boolean transferException)
specifier|default
name|ServletEndpointBuilder
name|transferException
parameter_list|(
name|boolean
name|transferException
parameter_list|)
block|{
name|doSetProperty
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
name|ServletEndpointBuilder
name|transferException
parameter_list|(
name|String
name|transferException
parameter_list|)
block|{
name|doSetProperty
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
block|}
comment|/**      * Advanced builder for endpoint for the Servlet component.      */
DECL|interface|AdvancedServletEndpointBuilder
specifier|public
interface|interface
name|AdvancedServletEndpointBuilder
extends|extends
name|EndpointConsumerBuilder
block|{
DECL|method|basic ()
specifier|default
name|ServletEndpointBuilder
name|basic
parameter_list|()
block|{
return|return
operator|(
name|ServletEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * To use a custom HttpBinding to control the mapping between Camel          * message and HttpClient.          *           * The option is a:          *<code>org.apache.camel.http.common.HttpBinding</code> type.          *           * Group: common (advanced)          */
DECL|method|httpBinding (Object httpBinding)
specifier|default
name|AdvancedServletEndpointBuilder
name|httpBinding
parameter_list|(
name|Object
name|httpBinding
parameter_list|)
block|{
name|doSetProperty
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
name|AdvancedServletEndpointBuilder
name|httpBinding
parameter_list|(
name|String
name|httpBinding
parameter_list|)
block|{
name|doSetProperty
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
comment|/**          * Whether to automatic bind multipart/form-data as attachments on the          * Camel Exchange. The options attachmentMultipartBinding=true and          * disableStreamCache=false cannot work together. Remove          * disableStreamCache to use AttachmentMultipartBinding. This is turn          * off by default as this may require servlet specific configuration to          * enable this when using Servlet's.          *           * The option is a:<code>boolean</code> type.          *           * Group: consumer (advanced)          */
DECL|method|attachmentMultipartBinding ( boolean attachmentMultipartBinding)
specifier|default
name|AdvancedServletEndpointBuilder
name|attachmentMultipartBinding
parameter_list|(
name|boolean
name|attachmentMultipartBinding
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"attachmentMultipartBinding"
argument_list|,
name|attachmentMultipartBinding
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether to automatic bind multipart/form-data as attachments on the          * Camel Exchange. The options attachmentMultipartBinding=true and          * disableStreamCache=false cannot work together. Remove          * disableStreamCache to use AttachmentMultipartBinding. This is turn          * off by default as this may require servlet specific configuration to          * enable this when using Servlet's.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: consumer (advanced)          */
DECL|method|attachmentMultipartBinding ( String attachmentMultipartBinding)
specifier|default
name|AdvancedServletEndpointBuilder
name|attachmentMultipartBinding
parameter_list|(
name|String
name|attachmentMultipartBinding
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"attachmentMultipartBinding"
argument_list|,
name|attachmentMultipartBinding
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether to eager check whether the HTTP requests has content if the          * content-length header is 0 or not present. This can be turned on in          * case HTTP clients do not send streamed data.          *           * The option is a:<code>boolean</code> type.          *           * Group: consumer (advanced)          */
DECL|method|eagerCheckContentAvailable ( boolean eagerCheckContentAvailable)
specifier|default
name|AdvancedServletEndpointBuilder
name|eagerCheckContentAvailable
parameter_list|(
name|boolean
name|eagerCheckContentAvailable
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"eagerCheckContentAvailable"
argument_list|,
name|eagerCheckContentAvailable
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether to eager check whether the HTTP requests has content if the          * content-length header is 0 or not present. This can be turned on in          * case HTTP clients do not send streamed data.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: consumer (advanced)          */
DECL|method|eagerCheckContentAvailable ( String eagerCheckContentAvailable)
specifier|default
name|AdvancedServletEndpointBuilder
name|eagerCheckContentAvailable
parameter_list|(
name|String
name|eagerCheckContentAvailable
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"eagerCheckContentAvailable"
argument_list|,
name|eagerCheckContentAvailable
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * To let the consumer use a custom ExceptionHandler. Notice if the          * option bridgeErrorHandler is enabled then this option is not in use.          * By default the consumer will deal with exceptions, that will be          * logged at WARN or ERROR level and ignored.          *           * The option is a:<code>org.apache.camel.spi.ExceptionHandler</code>          * type.          *           * Group: consumer (advanced)          */
DECL|method|exceptionHandler ( ExceptionHandler exceptionHandler)
specifier|default
name|AdvancedServletEndpointBuilder
name|exceptionHandler
parameter_list|(
name|ExceptionHandler
name|exceptionHandler
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"exceptionHandler"
argument_list|,
name|exceptionHandler
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * To let the consumer use a custom ExceptionHandler. Notice if the          * option bridgeErrorHandler is enabled then this option is not in use.          * By default the consumer will deal with exceptions, that will be          * logged at WARN or ERROR level and ignored.          *           * The option will be converted to a          *<code>org.apache.camel.spi.ExceptionHandler</code> type.          *           * Group: consumer (advanced)          */
DECL|method|exceptionHandler ( String exceptionHandler)
specifier|default
name|AdvancedServletEndpointBuilder
name|exceptionHandler
parameter_list|(
name|String
name|exceptionHandler
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"exceptionHandler"
argument_list|,
name|exceptionHandler
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets the exchange pattern when the consumer creates an exchange.          *           * The option is a:<code>org.apache.camel.ExchangePattern</code> type.          *           * Group: consumer (advanced)          */
DECL|method|exchangePattern ( ExchangePattern exchangePattern)
specifier|default
name|AdvancedServletEndpointBuilder
name|exchangePattern
parameter_list|(
name|ExchangePattern
name|exchangePattern
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"exchangePattern"
argument_list|,
name|exchangePattern
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets the exchange pattern when the consumer creates an exchange.          *           * The option will be converted to a          *<code>org.apache.camel.ExchangePattern</code> type.          *           * Group: consumer (advanced)          */
DECL|method|exchangePattern ( String exchangePattern)
specifier|default
name|AdvancedServletEndpointBuilder
name|exchangePattern
parameter_list|(
name|String
name|exchangePattern
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"exchangePattern"
argument_list|,
name|exchangePattern
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whitelist of accepted filename extensions for accepting uploaded          * files. Multiple extensions can be separated by comma, such as          * txt,xml.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: consumer (advanced)          */
DECL|method|fileNameExtWhitelist ( String fileNameExtWhitelist)
specifier|default
name|AdvancedServletEndpointBuilder
name|fileNameExtWhitelist
parameter_list|(
name|String
name|fileNameExtWhitelist
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"fileNameExtWhitelist"
argument_list|,
name|fileNameExtWhitelist
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Specifies whether to enable HTTP OPTIONS for this Servlet consumer.          * By default OPTIONS is turned off.          *           * The option is a:<code>boolean</code> type.          *           * Group: consumer (advanced)          */
DECL|method|optionsEnabled ( boolean optionsEnabled)
specifier|default
name|AdvancedServletEndpointBuilder
name|optionsEnabled
parameter_list|(
name|boolean
name|optionsEnabled
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"optionsEnabled"
argument_list|,
name|optionsEnabled
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Specifies whether to enable HTTP OPTIONS for this Servlet consumer.          * By default OPTIONS is turned off.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: consumer (advanced)          */
DECL|method|optionsEnabled ( String optionsEnabled)
specifier|default
name|AdvancedServletEndpointBuilder
name|optionsEnabled
parameter_list|(
name|String
name|optionsEnabled
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"optionsEnabled"
argument_list|,
name|optionsEnabled
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Specifies whether to enable HTTP TRACE for this Servlet consumer. By          * default TRACE is turned off.          *           * The option is a:<code>boolean</code> type.          *           * Group: consumer (advanced)          */
DECL|method|traceEnabled (boolean traceEnabled)
specifier|default
name|AdvancedServletEndpointBuilder
name|traceEnabled
parameter_list|(
name|boolean
name|traceEnabled
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"traceEnabled"
argument_list|,
name|traceEnabled
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Specifies whether to enable HTTP TRACE for this Servlet consumer. By          * default TRACE is turned off.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: consumer (advanced)          */
DECL|method|traceEnabled (String traceEnabled)
specifier|default
name|AdvancedServletEndpointBuilder
name|traceEnabled
parameter_list|(
name|String
name|traceEnabled
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"traceEnabled"
argument_list|,
name|traceEnabled
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          *           * The option is a:<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|basicPropertyBinding ( boolean basicPropertyBinding)
specifier|default
name|AdvancedServletEndpointBuilder
name|basicPropertyBinding
parameter_list|(
name|boolean
name|basicPropertyBinding
parameter_list|)
block|{
name|doSetProperty
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
name|AdvancedServletEndpointBuilder
name|basicPropertyBinding
parameter_list|(
name|String
name|basicPropertyBinding
parameter_list|)
block|{
name|doSetProperty
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
name|AdvancedServletEndpointBuilder
name|mapHttpMessageBody
parameter_list|(
name|boolean
name|mapHttpMessageBody
parameter_list|)
block|{
name|doSetProperty
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
name|AdvancedServletEndpointBuilder
name|mapHttpMessageBody
parameter_list|(
name|String
name|mapHttpMessageBody
parameter_list|)
block|{
name|doSetProperty
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
name|AdvancedServletEndpointBuilder
name|mapHttpMessageFormUrlEncodedBody
parameter_list|(
name|boolean
name|mapHttpMessageFormUrlEncodedBody
parameter_list|)
block|{
name|doSetProperty
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
name|AdvancedServletEndpointBuilder
name|mapHttpMessageFormUrlEncodedBody
parameter_list|(
name|String
name|mapHttpMessageFormUrlEncodedBody
parameter_list|)
block|{
name|doSetProperty
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
name|AdvancedServletEndpointBuilder
name|mapHttpMessageHeaders
parameter_list|(
name|boolean
name|mapHttpMessageHeaders
parameter_list|)
block|{
name|doSetProperty
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
name|AdvancedServletEndpointBuilder
name|mapHttpMessageHeaders
parameter_list|(
name|String
name|mapHttpMessageHeaders
parameter_list|)
block|{
name|doSetProperty
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
DECL|method|synchronous (boolean synchronous)
specifier|default
name|AdvancedServletEndpointBuilder
name|synchronous
parameter_list|(
name|boolean
name|synchronous
parameter_list|)
block|{
name|doSetProperty
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
name|AdvancedServletEndpointBuilder
name|synchronous
parameter_list|(
name|String
name|synchronous
parameter_list|)
block|{
name|doSetProperty
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
comment|/**      * Servlet (camel-servlet)      * To use a HTTP Servlet as entry for Camel routes when running in a servlet      * container.      *       * Category: http      * Since: 2.0      * Maven coordinates: org.apache.camel:camel-servlet      *       * Syntax:<code>servlet:contextPath</code>      *       * Path parameter: contextPath (required)      * The context-path to use      */
DECL|method|servlet (String path)
specifier|default
name|ServletEndpointBuilder
name|servlet
parameter_list|(
name|String
name|path
parameter_list|)
block|{
class|class
name|ServletEndpointBuilderImpl
extends|extends
name|AbstractEndpointBuilder
implements|implements
name|ServletEndpointBuilder
implements|,
name|AdvancedServletEndpointBuilder
block|{
specifier|public
name|ServletEndpointBuilderImpl
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|super
argument_list|(
literal|"servlet"
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|new
name|ServletEndpointBuilderImpl
argument_list|(
name|path
argument_list|)
return|;
block|}
block|}
end_interface

end_unit

