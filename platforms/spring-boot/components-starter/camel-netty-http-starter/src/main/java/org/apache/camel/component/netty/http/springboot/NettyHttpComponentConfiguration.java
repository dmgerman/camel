begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.netty.http.springboot
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|netty
operator|.
name|http
operator|.
name|springboot
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
name|LoggingLevel
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
name|netty
operator|.
name|http
operator|.
name|SecurityAuthenticator
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
name|netty
operator|.
name|http
operator|.
name|SecurityConstraint
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
comment|/**  * Netty HTTP server and client using the Netty 3.x library.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
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
literal|"camel.component.netty-http"
argument_list|)
DECL|class|NettyHttpComponentConfiguration
specifier|public
class|class
name|NettyHttpComponentConfiguration
extends|extends
name|ComponentConfigurationPropertiesCommon
block|{
comment|/**      * Whether to enable auto configuration of the netty-http component. This is      * enabled by default.      */
DECL|field|enabled
specifier|private
name|Boolean
name|enabled
decl_stmt|;
comment|/**      * To use a custom org.apache.camel.component.netty.http.NettyHttpBinding      * for binding to/from Netty and Camel Message API. The option is a      * org.apache.camel.component.netty.http.NettyHttpBinding type.      */
DECL|field|nettyHttpBinding
specifier|private
name|String
name|nettyHttpBinding
decl_stmt|;
comment|/**      * To use the NettyConfiguration as configuration when creating endpoints.      */
DECL|field|configuration
specifier|private
name|NettyHttpConfigurationNestedConfiguration
name|configuration
decl_stmt|;
comment|/**      * To use a custom org.apache.camel.spi.HeaderFilterStrategy to filter      * headers. The option is a org.apache.camel.spi.HeaderFilterStrategy type.      */
DECL|field|headerFilterStrategy
specifier|private
name|String
name|headerFilterStrategy
decl_stmt|;
comment|/**      * Refers to a      * org.apache.camel.component.netty.http.NettyHttpSecurityConfiguration for      * configuring secure web resources.      */
DECL|field|securityConfiguration
specifier|private
name|NettyHttpSecurityConfigurationNestedConfiguration
name|securityConfiguration
decl_stmt|;
comment|/**      * Enable usage of global SSL context parameters.      */
DECL|field|useGlobalSslContextParameters
specifier|private
name|Boolean
name|useGlobalSslContextParameters
init|=
literal|false
decl_stmt|;
comment|/**      * The core pool size for the ordered thread pool, if its in use. The      * default value is 16.      */
DECL|field|maximumPoolSize
specifier|private
name|Integer
name|maximumPoolSize
init|=
literal|16
decl_stmt|;
comment|/**      * Whether the component should resolve property placeholders on itself when      * starting. Only properties which are of String type can use property      * placeholders.      */
DECL|field|resolvePropertyPlaceholders
specifier|private
name|Boolean
name|resolvePropertyPlaceholders
init|=
literal|true
decl_stmt|;
DECL|method|getNettyHttpBinding ()
specifier|public
name|String
name|getNettyHttpBinding
parameter_list|()
block|{
return|return
name|nettyHttpBinding
return|;
block|}
DECL|method|setNettyHttpBinding (String nettyHttpBinding)
specifier|public
name|void
name|setNettyHttpBinding
parameter_list|(
name|String
name|nettyHttpBinding
parameter_list|)
block|{
name|this
operator|.
name|nettyHttpBinding
operator|=
name|nettyHttpBinding
expr_stmt|;
block|}
DECL|method|getConfiguration ()
specifier|public
name|NettyHttpConfigurationNestedConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
DECL|method|setConfiguration ( NettyHttpConfigurationNestedConfiguration configuration)
specifier|public
name|void
name|setConfiguration
parameter_list|(
name|NettyHttpConfigurationNestedConfiguration
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
DECL|method|getHeaderFilterStrategy ()
specifier|public
name|String
name|getHeaderFilterStrategy
parameter_list|()
block|{
return|return
name|headerFilterStrategy
return|;
block|}
DECL|method|setHeaderFilterStrategy (String headerFilterStrategy)
specifier|public
name|void
name|setHeaderFilterStrategy
parameter_list|(
name|String
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
DECL|method|getSecurityConfiguration ()
specifier|public
name|NettyHttpSecurityConfigurationNestedConfiguration
name|getSecurityConfiguration
parameter_list|()
block|{
return|return
name|securityConfiguration
return|;
block|}
DECL|method|setSecurityConfiguration ( NettyHttpSecurityConfigurationNestedConfiguration securityConfiguration)
specifier|public
name|void
name|setSecurityConfiguration
parameter_list|(
name|NettyHttpSecurityConfigurationNestedConfiguration
name|securityConfiguration
parameter_list|)
block|{
name|this
operator|.
name|securityConfiguration
operator|=
name|securityConfiguration
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
DECL|method|getMaximumPoolSize ()
specifier|public
name|Integer
name|getMaximumPoolSize
parameter_list|()
block|{
return|return
name|maximumPoolSize
return|;
block|}
DECL|method|setMaximumPoolSize (Integer maximumPoolSize)
specifier|public
name|void
name|setMaximumPoolSize
parameter_list|(
name|Integer
name|maximumPoolSize
parameter_list|)
block|{
name|this
operator|.
name|maximumPoolSize
operator|=
name|maximumPoolSize
expr_stmt|;
block|}
DECL|method|getResolvePropertyPlaceholders ()
specifier|public
name|Boolean
name|getResolvePropertyPlaceholders
parameter_list|()
block|{
return|return
name|resolvePropertyPlaceholders
return|;
block|}
DECL|method|setResolvePropertyPlaceholders ( Boolean resolvePropertyPlaceholders)
specifier|public
name|void
name|setResolvePropertyPlaceholders
parameter_list|(
name|Boolean
name|resolvePropertyPlaceholders
parameter_list|)
block|{
name|this
operator|.
name|resolvePropertyPlaceholders
operator|=
name|resolvePropertyPlaceholders
expr_stmt|;
block|}
DECL|class|NettyHttpSecurityConfigurationNestedConfiguration
specifier|public
specifier|static
class|class
name|NettyHttpSecurityConfigurationNestedConfiguration
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
name|netty
operator|.
name|http
operator|.
name|NettyHttpSecurityConfiguration
operator|.
name|class
decl_stmt|;
comment|/**          * Whether to enable authentication          *<p/>          * This is by default enabled.          */
DECL|field|authenticate
specifier|private
name|Boolean
name|authenticate
decl_stmt|;
comment|/**          * The supported restricted.          *<p/>          * Currently only Basic is supported.          */
DECL|field|constraint
specifier|private
name|String
name|constraint
decl_stmt|;
comment|/**          * Sets the name of the realm to use.          */
DECL|field|realm
specifier|private
name|String
name|realm
decl_stmt|;
comment|/**          * Sets a {@link SecurityConstraint} to use for checking if a web          * resource is restricted or not          *<p/>          * By default this is<tt>null</tt>, which means all resources is          * restricted.          */
DECL|field|securityConstraint
specifier|private
name|SecurityConstraint
name|securityConstraint
decl_stmt|;
comment|/**          * Sets the {@link SecurityAuthenticator} to use for authenticating the          * {@link HttpPrincipal} .          */
DECL|field|securityAuthenticator
specifier|private
name|SecurityAuthenticator
name|securityAuthenticator
decl_stmt|;
comment|/**          * Sets a logging level to use for logging denied login attempts (incl          * stacktraces)          *<p/>          * This level is by default DEBUG.          */
DECL|field|loginDeniedLoggingLevel
specifier|private
name|LoggingLevel
name|loginDeniedLoggingLevel
decl_stmt|;
DECL|field|roleClassName
specifier|private
name|String
name|roleClassName
decl_stmt|;
DECL|method|getAuthenticate ()
specifier|public
name|Boolean
name|getAuthenticate
parameter_list|()
block|{
return|return
name|authenticate
return|;
block|}
DECL|method|setAuthenticate (Boolean authenticate)
specifier|public
name|void
name|setAuthenticate
parameter_list|(
name|Boolean
name|authenticate
parameter_list|)
block|{
name|this
operator|.
name|authenticate
operator|=
name|authenticate
expr_stmt|;
block|}
DECL|method|getConstraint ()
specifier|public
name|String
name|getConstraint
parameter_list|()
block|{
return|return
name|constraint
return|;
block|}
DECL|method|setConstraint (String constraint)
specifier|public
name|void
name|setConstraint
parameter_list|(
name|String
name|constraint
parameter_list|)
block|{
name|this
operator|.
name|constraint
operator|=
name|constraint
expr_stmt|;
block|}
DECL|method|getRealm ()
specifier|public
name|String
name|getRealm
parameter_list|()
block|{
return|return
name|realm
return|;
block|}
DECL|method|setRealm (String realm)
specifier|public
name|void
name|setRealm
parameter_list|(
name|String
name|realm
parameter_list|)
block|{
name|this
operator|.
name|realm
operator|=
name|realm
expr_stmt|;
block|}
DECL|method|getSecurityConstraint ()
specifier|public
name|SecurityConstraint
name|getSecurityConstraint
parameter_list|()
block|{
return|return
name|securityConstraint
return|;
block|}
DECL|method|setSecurityConstraint (SecurityConstraint securityConstraint)
specifier|public
name|void
name|setSecurityConstraint
parameter_list|(
name|SecurityConstraint
name|securityConstraint
parameter_list|)
block|{
name|this
operator|.
name|securityConstraint
operator|=
name|securityConstraint
expr_stmt|;
block|}
DECL|method|getSecurityAuthenticator ()
specifier|public
name|SecurityAuthenticator
name|getSecurityAuthenticator
parameter_list|()
block|{
return|return
name|securityAuthenticator
return|;
block|}
DECL|method|setSecurityAuthenticator ( SecurityAuthenticator securityAuthenticator)
specifier|public
name|void
name|setSecurityAuthenticator
parameter_list|(
name|SecurityAuthenticator
name|securityAuthenticator
parameter_list|)
block|{
name|this
operator|.
name|securityAuthenticator
operator|=
name|securityAuthenticator
expr_stmt|;
block|}
DECL|method|getLoginDeniedLoggingLevel ()
specifier|public
name|LoggingLevel
name|getLoginDeniedLoggingLevel
parameter_list|()
block|{
return|return
name|loginDeniedLoggingLevel
return|;
block|}
DECL|method|setLoginDeniedLoggingLevel ( LoggingLevel loginDeniedLoggingLevel)
specifier|public
name|void
name|setLoginDeniedLoggingLevel
parameter_list|(
name|LoggingLevel
name|loginDeniedLoggingLevel
parameter_list|)
block|{
name|this
operator|.
name|loginDeniedLoggingLevel
operator|=
name|loginDeniedLoggingLevel
expr_stmt|;
block|}
DECL|method|getRoleClassName ()
specifier|public
name|String
name|getRoleClassName
parameter_list|()
block|{
return|return
name|roleClassName
return|;
block|}
DECL|method|setRoleClassName (String roleClassName)
specifier|public
name|void
name|setRoleClassName
parameter_list|(
name|String
name|roleClassName
parameter_list|)
block|{
name|this
operator|.
name|roleClassName
operator|=
name|roleClassName
expr_stmt|;
block|}
block|}
DECL|class|NettyHttpConfigurationNestedConfiguration
specifier|public
specifier|static
class|class
name|NettyHttpConfigurationNestedConfiguration
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
name|netty
operator|.
name|http
operator|.
name|NettyHttpConfiguration
operator|.
name|class
decl_stmt|;
comment|/**          * The protocol to use which is either http or https          */
DECL|field|protocol
specifier|private
name|String
name|protocol
decl_stmt|;
comment|/**          * The local hostname such as localhost, or 0.0.0.0 when being a          * consumer. The remote HTTP server hostname when using producer.          */
DECL|field|host
specifier|private
name|String
name|host
decl_stmt|;
comment|/**          * The port number. Is default 80 for http and 443 for https.          */
DECL|field|port
specifier|private
name|Integer
name|port
decl_stmt|;
comment|/**          * Allow using gzip/deflate for compression on the Netty HTTP server if          * the client supports it from the HTTP headers.          */
DECL|field|compression
specifier|private
name|Boolean
name|compression
init|=
literal|false
decl_stmt|;
comment|/**          * Option to disable throwing the HttpOperationFailedException in case          * of failed responses from the remote server. This allows you to get          * all responses regardless of the HTTP status code.          */
DECL|field|throwExceptionOnFailure
specifier|private
name|Boolean
name|throwExceptionOnFailure
init|=
literal|true
decl_stmt|;
comment|/**          * If enabled and an Exchange failed processing on the consumer side,          * and if the caused Exception was send back serialized in the response          * as a application/x-java-serialized-object content type. On the          * producer side the exception will be deserialized and thrown as is,          * instead of the HttpOperationFailedException. The caused exception is          * required to be serialized. This is by default turned off. If you          * enable this then be aware that Java will deserialize the incoming          * data from the request to Java and that can be a potential security          * risk.          */
DECL|field|transferException
specifier|private
name|Boolean
name|transferException
init|=
literal|false
decl_stmt|;
comment|/**          * If this option is enabled, then during binding from Netty to Camel          * Message then the header values will be URL decoded (eg %20 will be a          * space character. Notice this option is used by the default          * org.apache.camel.component.netty.http.NettyHttpBinding and therefore          * if you implement a custom          * org.apache.camel.component.netty.http.NettyHttpBinding then you would          * need to decode the headers accordingly to this option.          */
DECL|field|urlDecodeHeaders
specifier|private
name|Boolean
name|urlDecodeHeaders
init|=
literal|false
decl_stmt|;
comment|/**          * If this option is enabled, then during binding from Netty to Camel          * Message then the headers will be mapped as well (eg added as header          * to the Camel Message as well). You can turn off this option to          * disable this. The headers can still be accessed from the          * org.apache.camel.component.netty.http.NettyHttpMessage message with          * the method getHttpRequest() that returns the Netty HTTP request          * org.jboss.netty.handler.codec.http.HttpRequest instance.          */
DECL|field|mapHeaders
specifier|private
name|Boolean
name|mapHeaders
init|=
literal|true
decl_stmt|;
comment|/**          * Whether or not Camel should try to find a target consumer by matching          * the URI prefix if no exact match is found.          */
DECL|field|matchOnUriPrefix
specifier|private
name|Boolean
name|matchOnUriPrefix
init|=
literal|false
decl_stmt|;
comment|/**          * If the option is true, the producer will ignore the Exchange.HTTP_URI          * header, and use the endpoint's URI for request. You may also set the          * throwExceptionOnFailure to be false to let the producer send all the          * fault response back. The consumer working in the bridge mode will          * skip the gzip compression and WWW URL form encoding (by adding the          * Exchange.SKIP_GZIP_ENCODING and Exchange.SKIP_WWW_FORM_URLENCODED          * headers to the consumed exchange).          */
DECL|field|bridgeEndpoint
specifier|private
name|Boolean
name|bridgeEndpoint
init|=
literal|false
decl_stmt|;
comment|/**          * Resource path          */
DECL|field|path
specifier|private
name|String
name|path
decl_stmt|;
comment|/**          * Determines whether or not the raw input stream from Netty          * HttpRequest#getContent() is cached or not (Camel will read the stream          * into a in light-weight memory based Stream caching) cache. By default          * Camel will cache the Netty input stream to support reading it          * multiple times to ensure it Camel can retrieve all data from the          * stream. However you can set this option to true when you for example          * need to access the raw stream, such as streaming it directly to a          * file or other persistent store. Mind that if you enable this option,          * then you cannot read the Netty stream multiple times out of the box,          * and you would need manually to reset the reader index on the Netty          * raw stream.          */
DECL|field|disableStreamCache
specifier|private
name|Boolean
name|disableStreamCache
init|=
literal|false
decl_stmt|;
comment|/**          * Whether to send back HTTP status code 503 when the consumer has been          * suspended. If the option is false then the Netty Acceptor is unbound          * when the consumer is suspended, so clients cannot connect anymore.          */
DECL|field|send503whenSuspended
specifier|private
name|Boolean
name|send503whenSuspended
init|=
literal|true
decl_stmt|;
comment|/**          * Value in bytes the max content length per chunked frame received on          * the Netty HTTP server.          */
DECL|field|chunkedMaxContentLength
specifier|private
name|Integer
name|chunkedMaxContentLength
init|=
literal|1048576
decl_stmt|;
comment|/**          * The maximum length of all headers. If the sum of the length of each          * header exceeds this value, a TooLongFrameException will be raised.          */
DECL|field|maxHeaderSize
specifier|private
name|Integer
name|maxHeaderSize
init|=
literal|8192
decl_stmt|;
DECL|field|allowDefaultCodec
specifier|private
name|Boolean
name|allowDefaultCodec
decl_stmt|;
comment|/**          * The status codes which are considered a success response. The values          * are inclusive. Multiple ranges can be defined, separated by comma,          * e.g. 200-204,209,301-304. Each range must be a single number or          * from-to with the dash included. The default range is 200-299          */
DECL|field|okStatusCodeRange
specifier|private
name|String
name|okStatusCodeRange
init|=
literal|"200-299"
decl_stmt|;
comment|/**          * Sets whether to use a relative path in HTTP requests. Some third          * party backend systems such as IBM Datapower do not support absolute          * URIs in HTTP POSTs, and setting this option to true can work around          * this problem.          */
DECL|field|useRelativePath
specifier|private
name|Boolean
name|useRelativePath
init|=
literal|false
decl_stmt|;
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
name|Integer
name|getPort
parameter_list|()
block|{
return|return
name|port
return|;
block|}
DECL|method|setPort (Integer port)
specifier|public
name|void
name|setPort
parameter_list|(
name|Integer
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
DECL|method|getCompression ()
specifier|public
name|Boolean
name|getCompression
parameter_list|()
block|{
return|return
name|compression
return|;
block|}
DECL|method|setCompression (Boolean compression)
specifier|public
name|void
name|setCompression
parameter_list|(
name|Boolean
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
DECL|method|getThrowExceptionOnFailure ()
specifier|public
name|Boolean
name|getThrowExceptionOnFailure
parameter_list|()
block|{
return|return
name|throwExceptionOnFailure
return|;
block|}
DECL|method|setThrowExceptionOnFailure (Boolean throwExceptionOnFailure)
specifier|public
name|void
name|setThrowExceptionOnFailure
parameter_list|(
name|Boolean
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
DECL|method|getTransferException ()
specifier|public
name|Boolean
name|getTransferException
parameter_list|()
block|{
return|return
name|transferException
return|;
block|}
DECL|method|setTransferException (Boolean transferException)
specifier|public
name|void
name|setTransferException
parameter_list|(
name|Boolean
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
DECL|method|getUrlDecodeHeaders ()
specifier|public
name|Boolean
name|getUrlDecodeHeaders
parameter_list|()
block|{
return|return
name|urlDecodeHeaders
return|;
block|}
DECL|method|setUrlDecodeHeaders (Boolean urlDecodeHeaders)
specifier|public
name|void
name|setUrlDecodeHeaders
parameter_list|(
name|Boolean
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
DECL|method|getMapHeaders ()
specifier|public
name|Boolean
name|getMapHeaders
parameter_list|()
block|{
return|return
name|mapHeaders
return|;
block|}
DECL|method|setMapHeaders (Boolean mapHeaders)
specifier|public
name|void
name|setMapHeaders
parameter_list|(
name|Boolean
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
DECL|method|getMatchOnUriPrefix ()
specifier|public
name|Boolean
name|getMatchOnUriPrefix
parameter_list|()
block|{
return|return
name|matchOnUriPrefix
return|;
block|}
DECL|method|setMatchOnUriPrefix (Boolean matchOnUriPrefix)
specifier|public
name|void
name|setMatchOnUriPrefix
parameter_list|(
name|Boolean
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
DECL|method|getBridgeEndpoint ()
specifier|public
name|Boolean
name|getBridgeEndpoint
parameter_list|()
block|{
return|return
name|bridgeEndpoint
return|;
block|}
DECL|method|setBridgeEndpoint (Boolean bridgeEndpoint)
specifier|public
name|void
name|setBridgeEndpoint
parameter_list|(
name|Boolean
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
DECL|method|getDisableStreamCache ()
specifier|public
name|Boolean
name|getDisableStreamCache
parameter_list|()
block|{
return|return
name|disableStreamCache
return|;
block|}
DECL|method|setDisableStreamCache (Boolean disableStreamCache)
specifier|public
name|void
name|setDisableStreamCache
parameter_list|(
name|Boolean
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
DECL|method|getSend503whenSuspended ()
specifier|public
name|Boolean
name|getSend503whenSuspended
parameter_list|()
block|{
return|return
name|send503whenSuspended
return|;
block|}
DECL|method|setSend503whenSuspended (Boolean send503whenSuspended)
specifier|public
name|void
name|setSend503whenSuspended
parameter_list|(
name|Boolean
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
name|Integer
name|getChunkedMaxContentLength
parameter_list|()
block|{
return|return
name|chunkedMaxContentLength
return|;
block|}
DECL|method|setChunkedMaxContentLength (Integer chunkedMaxContentLength)
specifier|public
name|void
name|setChunkedMaxContentLength
parameter_list|(
name|Integer
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
name|Integer
name|getMaxHeaderSize
parameter_list|()
block|{
return|return
name|maxHeaderSize
return|;
block|}
DECL|method|setMaxHeaderSize (Integer maxHeaderSize)
specifier|public
name|void
name|setMaxHeaderSize
parameter_list|(
name|Integer
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
DECL|method|getAllowDefaultCodec ()
specifier|public
name|Boolean
name|getAllowDefaultCodec
parameter_list|()
block|{
return|return
name|allowDefaultCodec
return|;
block|}
DECL|method|setAllowDefaultCodec (Boolean allowDefaultCodec)
specifier|public
name|void
name|setAllowDefaultCodec
parameter_list|(
name|Boolean
name|allowDefaultCodec
parameter_list|)
block|{
name|this
operator|.
name|allowDefaultCodec
operator|=
name|allowDefaultCodec
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
DECL|method|getUseRelativePath ()
specifier|public
name|Boolean
name|getUseRelativePath
parameter_list|()
block|{
return|return
name|useRelativePath
return|;
block|}
DECL|method|setUseRelativePath (Boolean useRelativePath)
specifier|public
name|void
name|setUseRelativePath
parameter_list|(
name|Boolean
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
block|}
block|}
end_class

end_unit

