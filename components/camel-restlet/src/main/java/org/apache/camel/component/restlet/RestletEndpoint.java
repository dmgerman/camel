begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.restlet
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|restlet
package|;
end_package

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
name|util
operator|.
name|List
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
name|AsyncEndpoint
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
name|Exchange
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
name|Message
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
name|http
operator|.
name|common
operator|.
name|cookie
operator|.
name|CookieHandler
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
name|util
operator|.
name|CollectionStringBuffer
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
name|jsse
operator|.
name|SSLContextParameters
import|;
end_import

begin_import
import|import
name|org
operator|.
name|restlet
operator|.
name|data
operator|.
name|Method
import|;
end_import

begin_comment
comment|/**  * Component for consuming and producing Restful resources using Restlet.  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|firstVersion
operator|=
literal|"2.0.0"
argument_list|,
name|scheme
operator|=
literal|"restlet"
argument_list|,
name|title
operator|=
literal|"Restlet"
argument_list|,
name|syntax
operator|=
literal|"restlet:protocol:host:port/uriPattern"
argument_list|,
name|consumerClass
operator|=
name|RestletConsumer
operator|.
name|class
argument_list|,
name|label
operator|=
literal|"rest"
argument_list|,
name|lenientProperties
operator|=
literal|true
argument_list|)
DECL|class|RestletEndpoint
specifier|public
class|class
name|RestletEndpoint
extends|extends
name|DefaultEndpoint
implements|implements
name|AsyncEndpoint
implements|,
name|HeaderFilterStrategyAware
block|{
DECL|field|DEFAULT_PORT
specifier|private
specifier|static
specifier|final
name|int
name|DEFAULT_PORT
init|=
literal|80
decl_stmt|;
DECL|field|DEFAULT_PROTOCOL
specifier|private
specifier|static
specifier|final
name|String
name|DEFAULT_PROTOCOL
init|=
literal|"http"
decl_stmt|;
DECL|field|DEFAULT_HOST
specifier|private
specifier|static
specifier|final
name|String
name|DEFAULT_HOST
init|=
literal|"localhost"
decl_stmt|;
DECL|field|DEFAULT_SOCKET_TIMEOUT
specifier|private
specifier|static
specifier|final
name|int
name|DEFAULT_SOCKET_TIMEOUT
init|=
literal|30000
decl_stmt|;
DECL|field|DEFAULT_CONNECT_TIMEOUT
specifier|private
specifier|static
specifier|final
name|int
name|DEFAULT_CONNECT_TIMEOUT
init|=
literal|30000
decl_stmt|;
annotation|@
name|UriPath
argument_list|(
name|enums
operator|=
literal|"http,https"
argument_list|)
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|"true"
argument_list|)
DECL|field|protocol
specifier|private
name|String
name|protocol
init|=
name|DEFAULT_PROTOCOL
decl_stmt|;
annotation|@
name|UriPath
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|"true"
argument_list|)
DECL|field|host
specifier|private
name|String
name|host
init|=
name|DEFAULT_HOST
decl_stmt|;
annotation|@
name|UriPath
argument_list|(
name|defaultValue
operator|=
literal|"80"
argument_list|)
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|"true"
argument_list|)
DECL|field|port
specifier|private
name|int
name|port
init|=
name|DEFAULT_PORT
decl_stmt|;
annotation|@
name|UriPath
DECL|field|uriPattern
specifier|private
name|String
name|uriPattern
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
literal|""
operator|+
name|DEFAULT_SOCKET_TIMEOUT
argument_list|)
DECL|field|socketTimeout
specifier|private
name|int
name|socketTimeout
init|=
name|DEFAULT_SOCKET_TIMEOUT
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
literal|""
operator|+
name|DEFAULT_CONNECT_TIMEOUT
argument_list|)
DECL|field|connectTimeout
specifier|private
name|int
name|connectTimeout
init|=
name|DEFAULT_CONNECT_TIMEOUT
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"GET"
argument_list|,
name|enums
operator|=
literal|"ALL,CONNECT,DELETE,GET,HEAD,OPTIONS,PATCH,POST,PUT,TRACE"
argument_list|)
DECL|field|restletMethod
specifier|private
name|Method
name|restletMethod
init|=
name|Method
operator|.
name|GET
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|,
name|javaType
operator|=
literal|"java.lang.String"
argument_list|)
DECL|field|restletMethods
specifier|private
name|Method
index|[]
name|restletMethods
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|)
DECL|field|restletUriPatterns
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|restletUriPatterns
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"security"
argument_list|)
DECL|field|restletRealm
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|restletRealm
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|headerFilterStrategy
specifier|private
name|HeaderFilterStrategy
name|headerFilterStrategy
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|restletBinding
specifier|private
name|RestletBinding
name|restletBinding
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
argument_list|)
DECL|field|throwExceptionOnFailure
specifier|private
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
literal|"consumer,advanced"
argument_list|)
DECL|field|disableStreamCache
specifier|private
name|boolean
name|disableStreamCache
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"security"
argument_list|)
DECL|field|sslContextParameters
specifier|private
name|SSLContextParameters
name|sslContextParameters
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer,advanced"
argument_list|)
DECL|field|streamRepresentation
specifier|private
name|boolean
name|streamRepresentation
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer,advanced"
argument_list|)
DECL|field|autoCloseStream
specifier|private
name|boolean
name|autoCloseStream
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|)
DECL|field|cookieHandler
specifier|private
name|CookieHandler
name|cookieHandler
decl_stmt|;
comment|// should NOT be exposes as @UriParam
DECL|field|queryParameters
specifier|private
specifier|transient
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|queryParameters
decl_stmt|;
DECL|method|RestletEndpoint (RestletComponent component, String remaining)
specifier|public
name|RestletEndpoint
parameter_list|(
name|RestletComponent
name|component
parameter_list|,
name|String
name|remaining
parameter_list|)
throws|throws
name|Exception
block|{
name|super
argument_list|(
name|remaining
argument_list|,
name|component
argument_list|)
expr_stmt|;
block|}
DECL|method|setCompleteEndpointUri (String uri)
specifier|public
name|void
name|setCompleteEndpointUri
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
name|setEndpointUri
argument_list|(
name|uri
argument_list|)
expr_stmt|;
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
annotation|@
name|Override
DECL|method|isLenientProperties ()
specifier|public
name|boolean
name|isLenientProperties
parameter_list|()
block|{
comment|// true to allow dynamic URI options to be configured and passed to external system.
return|return
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|createExchange ()
specifier|public
name|Exchange
name|createExchange
parameter_list|()
block|{
name|Exchange
name|exchange
init|=
name|super
operator|.
name|createExchange
argument_list|()
decl_stmt|;
if|if
condition|(
name|isDisableStreamCache
argument_list|()
condition|)
block|{
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|DISABLE_HTTP_STREAM_CACHE
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
block|}
return|return
name|exchange
return|;
block|}
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
name|RestletConsumer
name|answer
init|=
operator|new
name|RestletConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|)
decl_stmt|;
name|configureConsumer
argument_list|(
name|answer
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
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
name|RestletProducer
argument_list|(
name|this
argument_list|)
return|;
block|}
DECL|method|connect (RestletConsumer restletConsumer)
specifier|public
name|void
name|connect
parameter_list|(
name|RestletConsumer
name|restletConsumer
parameter_list|)
throws|throws
name|Exception
block|{
operator|(
operator|(
name|RestletComponent
operator|)
name|getComponent
argument_list|()
operator|)
operator|.
name|connect
argument_list|(
name|restletConsumer
argument_list|)
expr_stmt|;
block|}
DECL|method|disconnect (RestletConsumer restletConsumer)
specifier|public
name|void
name|disconnect
parameter_list|(
name|RestletConsumer
name|restletConsumer
parameter_list|)
throws|throws
name|Exception
block|{
operator|(
operator|(
name|RestletComponent
operator|)
name|getComponent
argument_list|()
operator|)
operator|.
name|disconnect
argument_list|(
name|restletConsumer
argument_list|)
expr_stmt|;
block|}
DECL|method|getRestletMethod ()
specifier|public
name|Method
name|getRestletMethod
parameter_list|()
block|{
return|return
name|restletMethod
return|;
block|}
comment|/**      * On a producer endpoint, specifies the request method to use.      * On a consumer endpoint, specifies that the endpoint consumes only restletMethod requests.      */
DECL|method|setRestletMethod (Method restletMethod)
specifier|public
name|void
name|setRestletMethod
parameter_list|(
name|Method
name|restletMethod
parameter_list|)
block|{
name|this
operator|.
name|restletMethod
operator|=
name|restletMethod
expr_stmt|;
block|}
DECL|method|getProtocol ()
specifier|public
name|String
name|getProtocol
parameter_list|()
block|{
return|return
name|protocol
return|;
block|}
comment|/**      * The protocol to use which is http or https      */
DECL|method|setProtocol (String protocol)
specifier|public
name|void
name|setProtocol
parameter_list|(
name|String
name|protocol
parameter_list|)
block|{
name|this
operator|.
name|protocol
operator|=
name|protocol
expr_stmt|;
block|}
DECL|method|getHost ()
specifier|public
name|String
name|getHost
parameter_list|()
block|{
return|return
name|host
return|;
block|}
comment|/**      * The hostname of the restlet service      */
DECL|method|setHost (String host)
specifier|public
name|void
name|setHost
parameter_list|(
name|String
name|host
parameter_list|)
block|{
name|this
operator|.
name|host
operator|=
name|host
expr_stmt|;
block|}
DECL|method|getPort ()
specifier|public
name|int
name|getPort
parameter_list|()
block|{
return|return
name|port
return|;
block|}
comment|/**      * The port number of the restlet service      */
DECL|method|setPort (int port)
specifier|public
name|void
name|setPort
parameter_list|(
name|int
name|port
parameter_list|)
block|{
name|this
operator|.
name|port
operator|=
name|port
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
comment|/**      * The Client socket receive timeout, 0 for unlimited wait.      */
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
comment|/**      * The Client will give up connection if the connection is timeout, 0 for unlimited wait.      */
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
DECL|method|getUriPattern ()
specifier|public
name|String
name|getUriPattern
parameter_list|()
block|{
return|return
name|uriPattern
return|;
block|}
comment|/**      * The resource pattern such as /customer/{id}      */
DECL|method|setUriPattern (String uriPattern)
specifier|public
name|void
name|setUriPattern
parameter_list|(
name|String
name|uriPattern
parameter_list|)
block|{
name|this
operator|.
name|uriPattern
operator|=
name|uriPattern
expr_stmt|;
block|}
DECL|method|getRestletBinding ()
specifier|public
name|RestletBinding
name|getRestletBinding
parameter_list|()
block|{
return|return
name|restletBinding
return|;
block|}
comment|/**      * To use a custom RestletBinding to bind between Restlet and Camel message.      */
DECL|method|setRestletBinding (RestletBinding restletBinding)
specifier|public
name|void
name|setRestletBinding
parameter_list|(
name|RestletBinding
name|restletBinding
parameter_list|)
block|{
name|this
operator|.
name|restletBinding
operator|=
name|restletBinding
expr_stmt|;
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
if|if
condition|(
name|restletBinding
operator|instanceof
name|HeaderFilterStrategyAware
condition|)
block|{
operator|(
operator|(
name|HeaderFilterStrategyAware
operator|)
name|restletBinding
operator|)
operator|.
name|setHeaderFilterStrategy
argument_list|(
name|headerFilterStrategy
argument_list|)
expr_stmt|;
block|}
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
comment|/**      * To configure the security realms of restlet as a map.      */
DECL|method|setRestletRealm (Map<String, String> restletRealm)
specifier|public
name|void
name|setRestletRealm
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|restletRealm
parameter_list|)
block|{
name|this
operator|.
name|restletRealm
operator|=
name|restletRealm
expr_stmt|;
block|}
DECL|method|getRestletRealm ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getRestletRealm
parameter_list|()
block|{
return|return
name|restletRealm
return|;
block|}
annotation|@
name|Override
DECL|method|getExchangePattern ()
specifier|public
name|ExchangePattern
name|getExchangePattern
parameter_list|()
block|{
comment|// should always use in out for restlet
return|return
name|ExchangePattern
operator|.
name|InOut
return|;
block|}
comment|/**      * Specify one or more methods separated by commas (e.g. restletMethods=post,put) to be serviced by a restlet consumer endpoint.      * If both restletMethod and restletMethods options are specified, the restletMethod setting is ignored.      * The possible methods are: ALL,CONNECT,DELETE,GET,HEAD,OPTIONS,PATCH,POST,PUT,TRACE      */
DECL|method|setRestletMethods (Method[] restletMethods)
specifier|public
name|void
name|setRestletMethods
parameter_list|(
name|Method
index|[]
name|restletMethods
parameter_list|)
block|{
name|this
operator|.
name|restletMethods
operator|=
name|restletMethods
expr_stmt|;
block|}
DECL|method|getRestletMethods ()
specifier|public
name|Method
index|[]
name|getRestletMethods
parameter_list|()
block|{
return|return
name|restletMethods
return|;
block|}
comment|/**      * Specify one ore more URI templates to be serviced by a restlet consumer endpoint, using the # notation to      * reference a List<String> in the Camel Registry.      * If a URI pattern has been defined in the endpoint URI, both the URI pattern defined in the endpoint and the restletUriPatterns option will be honored.      */
DECL|method|setRestletUriPatterns (List<String> restletUriPatterns)
specifier|public
name|void
name|setRestletUriPatterns
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|restletUriPatterns
parameter_list|)
block|{
name|this
operator|.
name|restletUriPatterns
operator|=
name|restletUriPatterns
expr_stmt|;
block|}
DECL|method|getRestletUriPatterns ()
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getRestletUriPatterns
parameter_list|()
block|{
return|return
name|restletUriPatterns
return|;
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
comment|/**      * Whether to throw exception on a producer failure. If this option is false then the http status code is set as a message header which      * can be checked if it has an error value.      */
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
DECL|method|isDisableStreamCache ()
specifier|public
name|boolean
name|isDisableStreamCache
parameter_list|()
block|{
return|return
name|disableStreamCache
return|;
block|}
comment|/**      * Determines whether or not the raw input stream from Restlet is cached or not      * (Camel will read the stream into a in memory/overflow to file, Stream caching) cache.      * By default Camel will cache the Restlet input stream to support reading it multiple times to ensure Camel      * can retrieve all data from the stream. However you can set this option to true when you for example need      * to access the raw stream, such as streaming it directly to a file or other persistent store.      * DefaultRestletBinding will copy the request input stream into a stream cache and put it into message body      * if this option is false to support reading the stream multiple times.      */
DECL|method|setDisableStreamCache (boolean disableStreamCache)
specifier|public
name|void
name|setDisableStreamCache
parameter_list|(
name|boolean
name|disableStreamCache
parameter_list|)
block|{
name|this
operator|.
name|disableStreamCache
operator|=
name|disableStreamCache
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
comment|/**      * To configure security using SSLContextParameters.      */
DECL|method|setSslContextParameters (SSLContextParameters scp)
specifier|public
name|void
name|setSslContextParameters
parameter_list|(
name|SSLContextParameters
name|scp
parameter_list|)
block|{
name|this
operator|.
name|sslContextParameters
operator|=
name|scp
expr_stmt|;
block|}
DECL|method|isStreamRepresentation ()
specifier|public
name|boolean
name|isStreamRepresentation
parameter_list|()
block|{
return|return
name|streamRepresentation
return|;
block|}
comment|/**      * Whether to support stream representation as response from calling a REST service using the restlet producer.      * If the response is streaming then this option can be enabled to use an {@link java.io.InputStream} as the      * message body on the Camel {@link Message} body. If using this option you may want to enable the      * autoCloseStream option as well to ensure the input stream is closed when the Camel {@link Exchange}      * is done being routed. However if you need to read the stream outside a Camel route, you may need      * to not auto close the stream.      */
DECL|method|setStreamRepresentation (boolean streamRepresentation)
specifier|public
name|void
name|setStreamRepresentation
parameter_list|(
name|boolean
name|streamRepresentation
parameter_list|)
block|{
name|this
operator|.
name|streamRepresentation
operator|=
name|streamRepresentation
expr_stmt|;
block|}
DECL|method|isAutoCloseStream ()
specifier|public
name|boolean
name|isAutoCloseStream
parameter_list|()
block|{
return|return
name|autoCloseStream
return|;
block|}
comment|/**      * Whether to auto close the stream representation as response from calling a REST service using the restlet producer.      * If the response is streaming and the option streamRepresentation is enabled then you may want to auto close      * the {@link InputStream} from the streaming response to ensure the input stream is closed when the Camel {@link Exchange}      * is done being routed. However if you need to read the stream outside a Camel route, you may need      * to not auto close the stream.      */
DECL|method|setAutoCloseStream (boolean autoCloseStream)
specifier|public
name|void
name|setAutoCloseStream
parameter_list|(
name|boolean
name|autoCloseStream
parameter_list|)
block|{
name|this
operator|.
name|autoCloseStream
operator|=
name|autoCloseStream
expr_stmt|;
block|}
DECL|method|getCookieHandler ()
specifier|public
name|CookieHandler
name|getCookieHandler
parameter_list|()
block|{
return|return
name|cookieHandler
return|;
block|}
comment|/**      * Configure a cookie handler to maintain a HTTP session      */
DECL|method|setCookieHandler (CookieHandler cookieHandler)
specifier|public
name|void
name|setCookieHandler
parameter_list|(
name|CookieHandler
name|cookieHandler
parameter_list|)
block|{
name|this
operator|.
name|cookieHandler
operator|=
name|cookieHandler
expr_stmt|;
block|}
DECL|method|getQueryParameters ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getQueryParameters
parameter_list|()
block|{
return|return
name|queryParameters
return|;
block|}
comment|/**      * Additional query parameters for producer      */
DECL|method|setQueryParameters (Map<String, Object> queryParameters)
specifier|public
name|void
name|setQueryParameters
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|queryParameters
parameter_list|)
block|{
name|this
operator|.
name|queryParameters
operator|=
name|queryParameters
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|headerFilterStrategy
operator|==
literal|null
condition|)
block|{
name|headerFilterStrategy
operator|=
operator|new
name|RestletHeaderFilterStrategy
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|restletBinding
operator|==
literal|null
condition|)
block|{
name|restletBinding
operator|=
operator|new
name|DefaultRestletBinding
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|restletBinding
operator|instanceof
name|HeaderFilterStrategyAware
condition|)
block|{
operator|(
operator|(
name|HeaderFilterStrategyAware
operator|)
name|restletBinding
operator|)
operator|.
name|setHeaderFilterStrategy
argument_list|(
name|getHeaderFilterStrategy
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|restletBinding
operator|instanceof
name|DefaultRestletBinding
condition|)
block|{
operator|(
operator|(
name|DefaultRestletBinding
operator|)
name|restletBinding
operator|)
operator|.
name|setStreamRepresentation
argument_list|(
name|isStreamRepresentation
argument_list|()
argument_list|)
expr_stmt|;
operator|(
operator|(
name|DefaultRestletBinding
operator|)
name|restletBinding
operator|)
operator|.
name|setAutoCloseStream
argument_list|(
name|isAutoCloseStream
argument_list|()
argument_list|)
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
comment|// noop
block|}
block|}
end_class

end_unit

