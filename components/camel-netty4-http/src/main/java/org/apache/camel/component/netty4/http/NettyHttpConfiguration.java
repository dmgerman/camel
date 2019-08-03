begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.netty4.http
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|netty4
operator|.
name|http
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
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
name|io
operator|.
name|netty
operator|.
name|channel
operator|.
name|ChannelHandler
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
name|component
operator|.
name|netty4
operator|.
name|NettyConfiguration
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

begin_comment
comment|/**  * Extended configuration for using HTTP with Netty.  */
end_comment

begin_class
annotation|@
name|UriParams
DECL|class|NettyHttpConfiguration
specifier|public
class|class
name|NettyHttpConfiguration
extends|extends
name|NettyConfiguration
block|{
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
literal|true
argument_list|)
DECL|field|protocol
specifier|private
name|String
name|protocol
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
DECL|field|host
specifier|private
name|String
name|host
decl_stmt|;
annotation|@
name|UriPath
argument_list|(
name|name
operator|=
literal|"port"
argument_list|)
DECL|field|dummy
specifier|private
name|int
name|dummy
decl_stmt|;
annotation|@
name|UriPath
DECL|field|path
specifier|private
name|String
name|path
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer,advanced"
argument_list|)
DECL|field|urlDecodeHeaders
specifier|private
name|boolean
name|urlDecodeHeaders
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer,advanced"
argument_list|,
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|mapHeaders
specifier|private
name|boolean
name|mapHeaders
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
DECL|field|compression
specifier|private
name|boolean
name|compression
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
literal|"advanced"
argument_list|)
DECL|field|transferException
specifier|private
name|boolean
name|transferException
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|)
DECL|field|matchOnUriPrefix
specifier|private
name|boolean
name|matchOnUriPrefix
decl_stmt|;
annotation|@
name|UriParam
DECL|field|bridgeEndpoint
specifier|private
name|boolean
name|bridgeEndpoint
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced"
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
literal|"consumer"
argument_list|,
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|send503whenSuspended
specifier|private
name|boolean
name|send503whenSuspended
init|=
literal|true
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer,advanced"
argument_list|,
name|defaultValue
operator|=
literal|""
operator|+
literal|1024
operator|*
literal|1024
argument_list|)
DECL|field|chunkedMaxContentLength
specifier|private
name|int
name|chunkedMaxContentLength
init|=
literal|1024
operator|*
literal|1024
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer,advanced"
argument_list|,
name|defaultValue
operator|=
literal|"8192"
argument_list|)
DECL|field|maxHeaderSize
specifier|private
name|int
name|maxHeaderSize
init|=
literal|8192
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer,advanced"
argument_list|,
name|defaultValue
operator|=
literal|"200-299"
argument_list|)
DECL|field|okStatusCodeRange
specifier|private
name|String
name|okStatusCodeRange
init|=
literal|"200-299"
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer,advanced"
argument_list|,
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|useRelativePath
specifier|private
name|boolean
name|useRelativePath
init|=
literal|true
decl_stmt|;
DECL|method|NettyHttpConfiguration ()
specifier|public
name|NettyHttpConfiguration
parameter_list|()
block|{
comment|// we need sync=true as http is request/reply by nature
name|setSync
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|setReuseAddress
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|setServerInitializerFactory
argument_list|(
operator|new
name|HttpServerInitializerFactory
argument_list|()
argument_list|)
expr_stmt|;
name|setClientInitializerFactory
argument_list|(
operator|new
name|HttpClientInitializerFactory
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|copy ()
specifier|public
name|NettyHttpConfiguration
name|copy
parameter_list|()
block|{
try|try
block|{
comment|// clone as NettyHttpConfiguration
name|NettyHttpConfiguration
name|answer
init|=
operator|(
name|NettyHttpConfiguration
operator|)
name|clone
argument_list|()
decl_stmt|;
comment|// make sure the lists is copied in its own instance
name|List
argument_list|<
name|ChannelHandler
argument_list|>
name|encodersCopy
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|getEncoders
argument_list|()
argument_list|)
decl_stmt|;
name|answer
operator|.
name|setEncoders
argument_list|(
name|encodersCopy
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|ChannelHandler
argument_list|>
name|decodersCopy
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|getDecoders
argument_list|()
argument_list|)
decl_stmt|;
name|answer
operator|.
name|setDecoders
argument_list|(
name|decodersCopy
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
catch|catch
parameter_list|(
name|CloneNotSupportedException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
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
comment|/**      * The protocol to use which is either http, https or proxy - a consumer only option.      */
annotation|@
name|Override
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
annotation|@
name|Override
DECL|method|getHost ()
specifier|public
name|String
name|getHost
parameter_list|()
block|{
comment|// override to setup better documentation for netty-http
return|return
name|super
operator|.
name|getHost
argument_list|()
return|;
block|}
comment|/**      * The local hostname such as localhost, or 0.0.0.0 when being a consumer.      * The remote HTTP server hostname when using producer.      */
annotation|@
name|Override
DECL|method|setHost (String host)
specifier|public
name|void
name|setHost
parameter_list|(
name|String
name|host
parameter_list|)
block|{
comment|// override to setup better documentation for netty-http
name|super
operator|.
name|setHost
argument_list|(
name|host
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getPort ()
specifier|public
name|int
name|getPort
parameter_list|()
block|{
comment|// override to setup better documentation for netty-http
return|return
name|super
operator|.
name|getPort
argument_list|()
return|;
block|}
comment|/**      * The port number. Is default 80 for http and 443 for https.      */
annotation|@
name|Override
DECL|method|setPort (int port)
specifier|public
name|void
name|setPort
parameter_list|(
name|int
name|port
parameter_list|)
block|{
comment|// override to setup better documentation for netty-http
name|super
operator|.
name|setPort
argument_list|(
name|port
argument_list|)
expr_stmt|;
block|}
DECL|method|isCompression ()
specifier|public
name|boolean
name|isCompression
parameter_list|()
block|{
return|return
name|compression
return|;
block|}
comment|/**      * Allow using gzip/deflate for compression on the Netty HTTP server if the client supports it from the HTTP headers.      */
DECL|method|setCompression (boolean compression)
specifier|public
name|void
name|setCompression
parameter_list|(
name|boolean
name|compression
parameter_list|)
block|{
name|this
operator|.
name|compression
operator|=
name|compression
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
comment|/**      * If enabled and an Exchange failed processing on the consumer side, and if the caused Exception was send back serialized      * in the response as a application/x-java-serialized-object content type.      * On the producer side the exception will be deserialized and thrown as is, instead of the HttpOperationFailedException.      * The caused exception is required to be serialized.      *<p/>      * This is by default turned off. If you enable this then be aware that Java will deserialize the incoming      * data from the request to Java and that can be a potential security risk.      */
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
DECL|method|isUrlDecodeHeaders ()
specifier|public
name|boolean
name|isUrlDecodeHeaders
parameter_list|()
block|{
return|return
name|urlDecodeHeaders
return|;
block|}
comment|/**      * If this option is enabled, then during binding from Netty to Camel Message then the header values will be URL decoded      * (eg %20 will be a space character. Notice this option is used by the default org.apache.camel.component.netty.http.NettyHttpBinding      * and therefore if you implement a custom org.apache.camel.component.netty4.http.NettyHttpBinding then you would      * need to decode the headers accordingly to this option.      */
DECL|method|setUrlDecodeHeaders (boolean urlDecodeHeaders)
specifier|public
name|void
name|setUrlDecodeHeaders
parameter_list|(
name|boolean
name|urlDecodeHeaders
parameter_list|)
block|{
name|this
operator|.
name|urlDecodeHeaders
operator|=
name|urlDecodeHeaders
expr_stmt|;
block|}
DECL|method|isMapHeaders ()
specifier|public
name|boolean
name|isMapHeaders
parameter_list|()
block|{
return|return
name|mapHeaders
return|;
block|}
comment|/**      * If this option is enabled, then during binding from Netty to Camel Message then the headers will be mapped as well      * (eg added as header to the Camel Message as well). You can turn off this option to disable this.      * The headers can still be accessed from the org.apache.camel.component.netty.http.NettyHttpMessage message with      * the method getHttpRequest() that returns the Netty HTTP request io.netty.handler.codec.http.HttpRequest instance.      */
DECL|method|setMapHeaders (boolean mapHeaders)
specifier|public
name|void
name|setMapHeaders
parameter_list|(
name|boolean
name|mapHeaders
parameter_list|)
block|{
name|this
operator|.
name|mapHeaders
operator|=
name|mapHeaders
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
comment|/**      * Whether or not Camel should try to find a target consumer by matching the URI prefix if no exact match is found.      */
DECL|method|setMatchOnUriPrefix (boolean matchOnUriPrefix)
specifier|public
name|void
name|setMatchOnUriPrefix
parameter_list|(
name|boolean
name|matchOnUriPrefix
parameter_list|)
block|{
name|this
operator|.
name|matchOnUriPrefix
operator|=
name|matchOnUriPrefix
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
comment|/**      * If the option is true, the producer will ignore the Exchange.HTTP_URI header, and use the endpoint's URI for request.      * You may also set the throwExceptionOnFailure to be false to let the producer send all the fault response back.      * The consumer working in the bridge mode will skip the gzip compression and WWW URL form encoding (by adding the Exchange.SKIP_GZIP_ENCODING      * and Exchange.SKIP_WWW_FORM_URLENCODED headers to the consumed exchange).      */
DECL|method|setBridgeEndpoint (boolean bridgeEndpoint)
specifier|public
name|void
name|setBridgeEndpoint
parameter_list|(
name|boolean
name|bridgeEndpoint
parameter_list|)
block|{
name|this
operator|.
name|bridgeEndpoint
operator|=
name|bridgeEndpoint
expr_stmt|;
block|}
DECL|method|getPath ()
specifier|public
name|String
name|getPath
parameter_list|()
block|{
return|return
name|path
return|;
block|}
comment|/**      * Resource path      */
DECL|method|setPath (String path)
specifier|public
name|void
name|setPath
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|this
operator|.
name|path
operator|=
name|path
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
comment|/**      * Determines whether or not the raw input stream from Netty HttpRequest#getContent() or HttpResponset#getContent()      * is cached or not (Camel will read the stream into a in light-weight memory based Stream caching) cache.      * By default Camel will cache the Netty input stream to support reading it multiple times to ensure it Camel      * can retrieve all data from the stream. However you can set this option to true when you for example need to      * access the raw stream, such as streaming it directly to a file or other persistent store. Mind that      * if you enable this option, then you cannot read the Netty stream multiple times out of the box, and you would      * need manually to reset the reader index on the Netty raw stream. Also Netty will auto-close the Netty stream      * when the Netty HTTP server/HTTP client is done processing, which means that if the asynchronous routing engine is in      * use then any asynchronous thread that may continue routing the {@link org.apache.camel.Exchange} may not      * be able to read the Netty stream, because Netty has closed it.      */
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
DECL|method|isSend503whenSuspended ()
specifier|public
name|boolean
name|isSend503whenSuspended
parameter_list|()
block|{
return|return
name|send503whenSuspended
return|;
block|}
comment|/**      * Whether to send back HTTP status code 503 when the consumer has been suspended.      * If the option is false then the Netty Acceptor is unbound when the consumer is suspended, so clients cannot connect anymore.      */
DECL|method|setSend503whenSuspended (boolean send503whenSuspended)
specifier|public
name|void
name|setSend503whenSuspended
parameter_list|(
name|boolean
name|send503whenSuspended
parameter_list|)
block|{
name|this
operator|.
name|send503whenSuspended
operator|=
name|send503whenSuspended
expr_stmt|;
block|}
DECL|method|getChunkedMaxContentLength ()
specifier|public
name|int
name|getChunkedMaxContentLength
parameter_list|()
block|{
return|return
name|chunkedMaxContentLength
return|;
block|}
comment|/**      * Value in bytes the max content length per chunked frame received on the Netty HTTP server.      */
DECL|method|setChunkedMaxContentLength (int chunkedMaxContentLength)
specifier|public
name|void
name|setChunkedMaxContentLength
parameter_list|(
name|int
name|chunkedMaxContentLength
parameter_list|)
block|{
name|this
operator|.
name|chunkedMaxContentLength
operator|=
name|chunkedMaxContentLength
expr_stmt|;
block|}
DECL|method|getMaxHeaderSize ()
specifier|public
name|int
name|getMaxHeaderSize
parameter_list|()
block|{
return|return
name|maxHeaderSize
return|;
block|}
comment|/**      * The maximum length of all headers.      * If the sum of the length of each header exceeds this value, a {@link io.netty.handler.codec.TooLongFrameException} will be raised.      */
DECL|method|setMaxHeaderSize (int maxHeaderSize)
specifier|public
name|void
name|setMaxHeaderSize
parameter_list|(
name|int
name|maxHeaderSize
parameter_list|)
block|{
name|this
operator|.
name|maxHeaderSize
operator|=
name|maxHeaderSize
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
comment|/**      * The status codes which are considered a success response. The values are inclusive. Multiple ranges can be      * defined, separated by comma, e.g.<tt>200-204,209,301-304</tt>. Each range must be a single number or from-to with the      * dash included.      *<p/>      * The default range is<tt>200-299</tt>      */
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
comment|/**      * Sets whether to use a relative path in HTTP requests.      */
DECL|method|setUseRelativePath (boolean useRelativePath)
specifier|public
name|void
name|setUseRelativePath
parameter_list|(
name|boolean
name|useRelativePath
parameter_list|)
block|{
name|this
operator|.
name|useRelativePath
operator|=
name|useRelativePath
expr_stmt|;
block|}
DECL|method|isUseRelativePath ()
specifier|public
name|boolean
name|isUseRelativePath
parameter_list|()
block|{
return|return
name|this
operator|.
name|useRelativePath
return|;
block|}
DECL|method|isHttpProxy ()
specifier|public
name|boolean
name|isHttpProxy
parameter_list|()
block|{
return|return
literal|"proxy"
operator|.
name|equals
argument_list|(
name|super
operator|.
name|protocol
argument_list|)
return|;
block|}
block|}
end_class

end_unit

