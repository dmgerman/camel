begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.netty.http
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
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Locale
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
name|netty
operator|.
name|NettyComponent
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
name|component
operator|.
name|netty
operator|.
name|NettyServerBootstrapConfiguration
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
name|handlers
operator|.
name|HttpServerMultiplexChannelHandler
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
name|RestApiConsumerFactory
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
name|RestConfiguration
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
name|RestConsumerFactory
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
name|FileUtil
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
name|HostUtils
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
name|IntrospectionSupport
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
name|ObjectHelper
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
name|ServiceHelper
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
name|URISupport
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
name|UnsafeUriCharactersEncoder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_comment
comment|/**  * Netty HTTP based component.  */
end_comment

begin_class
DECL|class|NettyHttpComponent
specifier|public
class|class
name|NettyHttpComponent
extends|extends
name|NettyComponent
implements|implements
name|HeaderFilterStrategyAware
implements|,
name|RestConsumerFactory
implements|,
name|RestApiConsumerFactory
implements|,
name|SSLContextParametersAware
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|NettyHttpComponent
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// factories which is created by this component and therefore manage their lifecycles
DECL|field|multiplexChannelHandlers
specifier|private
specifier|final
name|Map
argument_list|<
name|Integer
argument_list|,
name|HttpServerConsumerChannelFactory
argument_list|>
name|multiplexChannelHandlers
init|=
operator|new
name|HashMap
argument_list|<
name|Integer
argument_list|,
name|HttpServerConsumerChannelFactory
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|bootstrapFactories
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|HttpServerBootstrapFactory
argument_list|>
name|bootstrapFactories
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|HttpServerBootstrapFactory
argument_list|>
argument_list|()
decl_stmt|;
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|nettyHttpBinding
specifier|private
name|NettyHttpBinding
name|nettyHttpBinding
decl_stmt|;
annotation|@
name|Metadata
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
name|Metadata
argument_list|(
name|label
operator|=
literal|"security"
argument_list|)
DECL|field|securityConfiguration
specifier|private
name|NettyHttpSecurityConfiguration
name|securityConfiguration
decl_stmt|;
DECL|method|NettyHttpComponent ()
specifier|public
name|NettyHttpComponent
parameter_list|()
block|{
comment|// use the http configuration and filter strategy
name|super
argument_list|(
name|NettyHttpEndpoint
operator|.
name|class
argument_list|)
expr_stmt|;
name|setConfiguration
argument_list|(
operator|new
name|NettyHttpConfiguration
argument_list|()
argument_list|)
expr_stmt|;
name|setHeaderFilterStrategy
argument_list|(
operator|new
name|NettyHttpHeaderFilterStrategy
argument_list|()
argument_list|)
expr_stmt|;
comment|// use the binding that supports Rest DSL
name|setNettyHttpBinding
argument_list|(
operator|new
name|RestNettyHttpBinding
argument_list|(
name|getHeaderFilterStrategy
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
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
name|NettyConfiguration
name|config
decl_stmt|;
if|if
condition|(
name|getConfiguration
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|config
operator|=
name|getConfiguration
argument_list|()
operator|.
name|copy
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|config
operator|=
operator|new
name|NettyHttpConfiguration
argument_list|()
expr_stmt|;
block|}
name|HeaderFilterStrategy
name|headerFilterStrategy
init|=
name|resolveAndRemoveReferenceParameter
argument_list|(
name|parameters
argument_list|,
literal|"headerFilterStrategy"
argument_list|,
name|HeaderFilterStrategy
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// merge any custom bootstrap configuration on the config
name|NettyServerBootstrapConfiguration
name|bootstrapConfiguration
init|=
name|resolveAndRemoveReferenceParameter
argument_list|(
name|parameters
argument_list|,
literal|"bootstrapConfiguration"
argument_list|,
name|NettyServerBootstrapConfiguration
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|bootstrapConfiguration
operator|!=
literal|null
condition|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|options
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
if|if
condition|(
name|IntrospectionSupport
operator|.
name|getProperties
argument_list|(
name|bootstrapConfiguration
argument_list|,
name|options
argument_list|,
literal|null
argument_list|,
literal|false
argument_list|)
condition|)
block|{
name|IntrospectionSupport
operator|.
name|setProperties
argument_list|(
name|getCamelContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
argument_list|,
name|config
argument_list|,
name|options
argument_list|)
expr_stmt|;
block|}
block|}
comment|// any custom security configuration
name|NettyHttpSecurityConfiguration
name|securityConfiguration
init|=
name|resolveAndRemoveReferenceParameter
argument_list|(
name|parameters
argument_list|,
literal|"securityConfiguration"
argument_list|,
name|NettyHttpSecurityConfiguration
operator|.
name|class
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|securityOptions
init|=
name|IntrospectionSupport
operator|.
name|extractProperties
argument_list|(
name|parameters
argument_list|,
literal|"securityConfiguration."
argument_list|)
decl_stmt|;
comment|// are we using a shared http server?
name|int
name|sharedPort
init|=
operator|-
literal|1
decl_stmt|;
name|NettySharedHttpServer
name|shared
init|=
name|resolveAndRemoveReferenceParameter
argument_list|(
name|parameters
argument_list|,
literal|"nettySharedHttpServer"
argument_list|,
name|NettySharedHttpServer
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|shared
operator|!=
literal|null
condition|)
block|{
comment|// use port number from the shared http server
name|LOG
operator|.
name|debug
argument_list|(
literal|"Using NettySharedHttpServer: {} with port: {}"
argument_list|,
name|shared
argument_list|,
name|shared
operator|.
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
name|sharedPort
operator|=
name|shared
operator|.
name|getPort
argument_list|()
expr_stmt|;
block|}
comment|// we must include the protocol in the remaining
name|boolean
name|hasProtocol
init|=
name|remaining
operator|.
name|startsWith
argument_list|(
literal|"http://"
argument_list|)
operator|||
name|remaining
operator|.
name|startsWith
argument_list|(
literal|"http:"
argument_list|)
operator|||
name|remaining
operator|.
name|startsWith
argument_list|(
literal|"https://"
argument_list|)
operator|||
name|remaining
operator|.
name|startsWith
argument_list|(
literal|"https:"
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|hasProtocol
condition|)
block|{
comment|// http is the default protocol
name|remaining
operator|=
literal|"http://"
operator|+
name|remaining
expr_stmt|;
block|}
name|boolean
name|hasSlash
init|=
name|remaining
operator|.
name|startsWith
argument_list|(
literal|"http://"
argument_list|)
operator|||
name|remaining
operator|.
name|startsWith
argument_list|(
literal|"https://"
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|hasSlash
condition|)
block|{
comment|// must have double slash after protocol
if|if
condition|(
name|remaining
operator|.
name|startsWith
argument_list|(
literal|"http:"
argument_list|)
condition|)
block|{
name|remaining
operator|=
literal|"http://"
operator|+
name|remaining
operator|.
name|substring
argument_list|(
literal|5
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|remaining
operator|=
literal|"https://"
operator|+
name|remaining
operator|.
name|substring
argument_list|(
literal|6
argument_list|)
expr_stmt|;
block|}
block|}
name|LOG
operator|.
name|debug
argument_list|(
literal|"Netty http url: {}"
argument_list|,
name|remaining
argument_list|)
expr_stmt|;
comment|// set port on configuration which is either shared or using default values
if|if
condition|(
name|sharedPort
operator|!=
operator|-
literal|1
condition|)
block|{
name|config
operator|.
name|setPort
argument_list|(
name|sharedPort
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|config
operator|.
name|getPort
argument_list|()
operator|==
operator|-
literal|1
operator|||
name|config
operator|.
name|getPort
argument_list|()
operator|==
literal|0
condition|)
block|{
if|if
condition|(
name|remaining
operator|.
name|startsWith
argument_list|(
literal|"http:"
argument_list|)
condition|)
block|{
name|config
operator|.
name|setPort
argument_list|(
literal|80
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|remaining
operator|.
name|startsWith
argument_list|(
literal|"https:"
argument_list|)
condition|)
block|{
name|config
operator|.
name|setPort
argument_list|(
literal|443
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|config
operator|.
name|getPort
argument_list|()
operator|==
operator|-
literal|1
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Port number must be configured"
argument_list|)
throw|;
block|}
comment|// configure configuration
name|config
operator|=
name|parseConfiguration
argument_list|(
name|config
argument_list|,
name|remaining
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
name|setProperties
argument_list|(
name|config
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
if|if
condition|(
name|config
operator|.
name|getSslContextParameters
argument_list|()
operator|==
literal|null
condition|)
block|{
name|config
operator|.
name|setSslContextParameters
argument_list|(
name|getGlobalSSLContextParameters
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// validate config
name|config
operator|.
name|validateConfiguration
argument_list|()
expr_stmt|;
comment|// create the address uri which includes the remainder parameters (which is not configuration parameters for this component)
name|URI
name|u
init|=
operator|new
name|URI
argument_list|(
name|UnsafeUriCharactersEncoder
operator|.
name|encodeHttpURI
argument_list|(
name|remaining
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|addressUri
init|=
name|URISupport
operator|.
name|createRemainingURI
argument_list|(
name|u
argument_list|,
name|parameters
argument_list|)
operator|.
name|toString
argument_list|()
decl_stmt|;
name|NettyHttpEndpoint
name|answer
init|=
operator|new
name|NettyHttpEndpoint
argument_list|(
name|addressUri
argument_list|,
name|this
argument_list|,
name|config
argument_list|)
decl_stmt|;
name|answer
operator|.
name|setTimer
argument_list|(
name|getTimer
argument_list|()
argument_list|)
expr_stmt|;
comment|// must use a copy of the binding on the endpoint to avoid sharing same instance that can cause side-effects
if|if
condition|(
name|answer
operator|.
name|getNettyHttpBinding
argument_list|()
operator|==
literal|null
condition|)
block|{
name|Object
name|binding
init|=
name|getNettyHttpBinding
argument_list|()
decl_stmt|;
if|if
condition|(
name|binding
operator|instanceof
name|RestNettyHttpBinding
condition|)
block|{
name|NettyHttpBinding
name|copy
init|=
operator|(
operator|(
name|RestNettyHttpBinding
operator|)
name|binding
operator|)
operator|.
name|copy
argument_list|()
decl_stmt|;
name|answer
operator|.
name|setNettyHttpBinding
argument_list|(
name|copy
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|binding
operator|instanceof
name|DefaultNettyHttpBinding
condition|)
block|{
name|NettyHttpBinding
name|copy
init|=
operator|(
operator|(
name|DefaultNettyHttpBinding
operator|)
name|binding
operator|)
operator|.
name|copy
argument_list|()
decl_stmt|;
name|answer
operator|.
name|setNettyHttpBinding
argument_list|(
name|copy
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|headerFilterStrategy
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setHeaderFilterStrategy
argument_list|(
name|headerFilterStrategy
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|answer
operator|.
name|getHeaderFilterStrategy
argument_list|()
operator|==
literal|null
condition|)
block|{
name|answer
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
name|securityConfiguration
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setSecurityConfiguration
argument_list|(
name|securityConfiguration
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|answer
operator|.
name|getSecurityConfiguration
argument_list|()
operator|==
literal|null
condition|)
block|{
name|answer
operator|.
name|setSecurityConfiguration
argument_list|(
name|getSecurityConfiguration
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// configure any security options
if|if
condition|(
name|securityOptions
operator|!=
literal|null
operator|&&
operator|!
name|securityOptions
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|securityConfiguration
operator|=
name|answer
operator|.
name|getSecurityConfiguration
argument_list|()
expr_stmt|;
if|if
condition|(
name|securityConfiguration
operator|==
literal|null
condition|)
block|{
name|securityConfiguration
operator|=
operator|new
name|NettyHttpSecurityConfiguration
argument_list|()
expr_stmt|;
name|answer
operator|.
name|setSecurityConfiguration
argument_list|(
name|securityConfiguration
argument_list|)
expr_stmt|;
block|}
name|setProperties
argument_list|(
name|securityConfiguration
argument_list|,
name|securityOptions
argument_list|)
expr_stmt|;
name|validateParameters
argument_list|(
name|uri
argument_list|,
name|securityOptions
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
name|answer
operator|.
name|setNettySharedHttpServer
argument_list|(
name|shared
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
annotation|@
name|Override
DECL|method|parseConfiguration (NettyConfiguration configuration, String remaining, Map<String, Object> parameters)
specifier|protected
name|NettyConfiguration
name|parseConfiguration
parameter_list|(
name|NettyConfiguration
name|configuration
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
comment|// ensure uri is encoded to be valid
name|String
name|safe
init|=
name|UnsafeUriCharactersEncoder
operator|.
name|encodeHttpURI
argument_list|(
name|remaining
argument_list|)
decl_stmt|;
name|URI
name|uri
init|=
operator|new
name|URI
argument_list|(
name|safe
argument_list|)
decl_stmt|;
name|configuration
operator|.
name|parseURI
argument_list|(
name|uri
argument_list|,
name|parameters
argument_list|,
name|this
argument_list|,
literal|"http"
argument_list|,
literal|"https"
argument_list|)
expr_stmt|;
comment|// force using tcp as the underlying transport
name|configuration
operator|.
name|setProtocol
argument_list|(
literal|"tcp"
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|setTextline
argument_list|(
literal|false
argument_list|)
expr_stmt|;
if|if
condition|(
name|configuration
operator|instanceof
name|NettyHttpConfiguration
condition|)
block|{
operator|(
operator|(
name|NettyHttpConfiguration
operator|)
name|configuration
operator|)
operator|.
name|setPath
argument_list|(
name|uri
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|configuration
return|;
block|}
DECL|method|getNettyHttpBinding ()
specifier|public
name|NettyHttpBinding
name|getNettyHttpBinding
parameter_list|()
block|{
return|return
name|nettyHttpBinding
return|;
block|}
comment|/**      * To use a custom org.apache.camel.component.netty.http.NettyHttpBinding for binding to/from Netty and Camel Message API.      */
DECL|method|setNettyHttpBinding (NettyHttpBinding nettyHttpBinding)
specifier|public
name|void
name|setNettyHttpBinding
parameter_list|(
name|NettyHttpBinding
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
annotation|@
name|Override
DECL|method|getConfiguration ()
specifier|public
name|NettyHttpConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
operator|(
name|NettyHttpConfiguration
operator|)
name|super
operator|.
name|getConfiguration
argument_list|()
return|;
block|}
DECL|method|setConfiguration (NettyHttpConfiguration configuration)
specifier|public
name|void
name|setConfiguration
parameter_list|(
name|NettyHttpConfiguration
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
comment|/**      * To use a custom org.apache.camel.spi.HeaderFilterStrategy to filter headers.      */
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
DECL|method|getSecurityConfiguration ()
specifier|public
name|NettyHttpSecurityConfiguration
name|getSecurityConfiguration
parameter_list|()
block|{
return|return
name|securityConfiguration
return|;
block|}
comment|/**      * Refers to a org.apache.camel.component.netty.http.NettyHttpSecurityConfiguration for configuring secure web resources.      */
DECL|method|setSecurityConfiguration (NettyHttpSecurityConfiguration securityConfiguration)
specifier|public
name|void
name|setSecurityConfiguration
parameter_list|(
name|NettyHttpSecurityConfiguration
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
DECL|method|getMultiplexChannelHandler (int port)
specifier|public
specifier|synchronized
name|HttpServerConsumerChannelFactory
name|getMultiplexChannelHandler
parameter_list|(
name|int
name|port
parameter_list|)
block|{
name|HttpServerConsumerChannelFactory
name|answer
init|=
name|multiplexChannelHandlers
operator|.
name|get
argument_list|(
name|port
argument_list|)
decl_stmt|;
if|if
condition|(
name|answer
operator|==
literal|null
condition|)
block|{
name|answer
operator|=
operator|new
name|HttpServerMultiplexChannelHandler
argument_list|()
expr_stmt|;
name|answer
operator|.
name|init
argument_list|(
name|port
argument_list|)
expr_stmt|;
name|multiplexChannelHandlers
operator|.
name|put
argument_list|(
name|port
argument_list|,
name|answer
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
DECL|method|getOrCreateHttpNettyServerBootstrapFactory (NettyHttpConsumer consumer)
specifier|protected
specifier|synchronized
name|HttpServerBootstrapFactory
name|getOrCreateHttpNettyServerBootstrapFactory
parameter_list|(
name|NettyHttpConsumer
name|consumer
parameter_list|)
block|{
name|String
name|key
init|=
name|consumer
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getAddress
argument_list|()
decl_stmt|;
name|HttpServerBootstrapFactory
name|answer
init|=
name|bootstrapFactories
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|answer
operator|==
literal|null
condition|)
block|{
name|HttpServerConsumerChannelFactory
name|channelFactory
init|=
name|getMultiplexChannelHandler
argument_list|(
name|consumer
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getPort
argument_list|()
argument_list|)
decl_stmt|;
name|answer
operator|=
operator|new
name|HttpServerBootstrapFactory
argument_list|(
name|channelFactory
argument_list|)
expr_stmt|;
name|answer
operator|.
name|init
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
name|consumer
operator|.
name|getConfiguration
argument_list|()
argument_list|,
operator|new
name|HttpServerPipelineFactory
argument_list|(
name|consumer
argument_list|)
argument_list|)
expr_stmt|;
name|bootstrapFactories
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|answer
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
annotation|@
name|Override
DECL|method|createConsumer (CamelContext camelContext, Processor processor, String verb, String basePath, String uriTemplate, String consumes, String produces, RestConfiguration configuration, Map<String, Object> parameters)
specifier|public
name|Consumer
name|createConsumer
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|Processor
name|processor
parameter_list|,
name|String
name|verb
parameter_list|,
name|String
name|basePath
parameter_list|,
name|String
name|uriTemplate
parameter_list|,
name|String
name|consumes
parameter_list|,
name|String
name|produces
parameter_list|,
name|RestConfiguration
name|configuration
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
return|return
name|doCreateConsumer
argument_list|(
name|camelContext
argument_list|,
name|processor
argument_list|,
name|verb
argument_list|,
name|basePath
argument_list|,
name|uriTemplate
argument_list|,
name|consumes
argument_list|,
name|produces
argument_list|,
name|configuration
argument_list|,
name|parameters
argument_list|,
literal|false
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createApiConsumer (CamelContext camelContext, Processor processor, String contextPath, RestConfiguration configuration, Map<String, Object> parameters)
specifier|public
name|Consumer
name|createApiConsumer
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|Processor
name|processor
parameter_list|,
name|String
name|contextPath
parameter_list|,
name|RestConfiguration
name|configuration
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
comment|// reuse the createConsumer method we already have. The api need to use GET and match on uri prefix
return|return
name|doCreateConsumer
argument_list|(
name|camelContext
argument_list|,
name|processor
argument_list|,
literal|"GET"
argument_list|,
name|contextPath
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|configuration
argument_list|,
name|parameters
argument_list|,
literal|true
argument_list|)
return|;
block|}
DECL|method|doCreateConsumer (CamelContext camelContext, Processor processor, String verb, String basePath, String uriTemplate, String consumes, String produces, RestConfiguration configuration, Map<String, Object> parameters, boolean api)
name|Consumer
name|doCreateConsumer
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|Processor
name|processor
parameter_list|,
name|String
name|verb
parameter_list|,
name|String
name|basePath
parameter_list|,
name|String
name|uriTemplate
parameter_list|,
name|String
name|consumes
parameter_list|,
name|String
name|produces
parameter_list|,
name|RestConfiguration
name|configuration
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|,
name|boolean
name|api
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|path
init|=
name|basePath
decl_stmt|;
if|if
condition|(
name|uriTemplate
operator|!=
literal|null
condition|)
block|{
comment|// make sure to avoid double slashes
if|if
condition|(
name|uriTemplate
operator|.
name|startsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|path
operator|=
name|path
operator|+
name|uriTemplate
expr_stmt|;
block|}
else|else
block|{
name|path
operator|=
name|path
operator|+
literal|"/"
operator|+
name|uriTemplate
expr_stmt|;
block|}
block|}
name|path
operator|=
name|FileUtil
operator|.
name|stripLeadingSeparator
argument_list|(
name|path
argument_list|)
expr_stmt|;
name|String
name|scheme
init|=
literal|"http"
decl_stmt|;
name|String
name|host
init|=
literal|""
decl_stmt|;
name|int
name|port
init|=
literal|0
decl_stmt|;
comment|// if no explicit port/host configured, then use port from rest configuration
name|RestConfiguration
name|config
init|=
name|configuration
decl_stmt|;
if|if
condition|(
name|config
operator|==
literal|null
condition|)
block|{
name|config
operator|=
name|camelContext
operator|.
name|getRestConfiguration
argument_list|(
literal|"netty-http"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|config
operator|.
name|getScheme
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|scheme
operator|=
name|config
operator|.
name|getScheme
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|config
operator|.
name|getHost
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|host
operator|=
name|config
operator|.
name|getHost
argument_list|()
expr_stmt|;
block|}
name|int
name|num
init|=
name|config
operator|.
name|getPort
argument_list|()
decl_stmt|;
if|if
condition|(
name|num
operator|>
literal|0
condition|)
block|{
name|port
operator|=
name|num
expr_stmt|;
block|}
comment|// prefix path with context-path if configured in rest-dsl configuration
name|String
name|contextPath
init|=
name|config
operator|.
name|getContextPath
argument_list|()
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|contextPath
argument_list|)
condition|)
block|{
name|contextPath
operator|=
name|FileUtil
operator|.
name|stripTrailingSeparator
argument_list|(
name|contextPath
argument_list|)
expr_stmt|;
name|contextPath
operator|=
name|FileUtil
operator|.
name|stripLeadingSeparator
argument_list|(
name|contextPath
argument_list|)
expr_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|contextPath
argument_list|)
condition|)
block|{
name|path
operator|=
name|contextPath
operator|+
literal|"/"
operator|+
name|path
expr_stmt|;
block|}
block|}
comment|// if no explicit hostname set then resolve the hostname
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|host
argument_list|)
condition|)
block|{
if|if
condition|(
name|config
operator|.
name|getRestHostNameResolver
argument_list|()
operator|==
name|RestConfiguration
operator|.
name|RestHostNameResolver
operator|.
name|allLocalIp
condition|)
block|{
name|host
operator|=
literal|"0.0.0.0"
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|config
operator|.
name|getRestHostNameResolver
argument_list|()
operator|==
name|RestConfiguration
operator|.
name|RestHostNameResolver
operator|.
name|localHostName
condition|)
block|{
name|host
operator|=
name|HostUtils
operator|.
name|getLocalHostName
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|config
operator|.
name|getRestHostNameResolver
argument_list|()
operator|==
name|RestConfiguration
operator|.
name|RestHostNameResolver
operator|.
name|localIp
condition|)
block|{
name|host
operator|=
name|HostUtils
operator|.
name|getLocalIp
argument_list|()
expr_stmt|;
block|}
block|}
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|map
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
comment|// build query string, and append any endpoint configuration properties
if|if
condition|(
name|config
operator|.
name|getComponent
argument_list|()
operator|==
literal|null
operator|||
name|config
operator|.
name|getComponent
argument_list|()
operator|.
name|equals
argument_list|(
literal|"netty-http"
argument_list|)
condition|)
block|{
comment|// setup endpoint options
if|if
condition|(
name|config
operator|.
name|getEndpointProperties
argument_list|()
operator|!=
literal|null
operator|&&
operator|!
name|config
operator|.
name|getEndpointProperties
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|map
operator|.
name|putAll
argument_list|(
name|config
operator|.
name|getEndpointProperties
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|boolean
name|cors
init|=
name|config
operator|.
name|isEnableCORS
argument_list|()
decl_stmt|;
if|if
condition|(
name|cors
condition|)
block|{
comment|// allow HTTP Options as we want to handle CORS in rest-dsl
name|map
operator|.
name|put
argument_list|(
literal|"optionsEnabled"
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
block|}
name|String
name|query
init|=
name|URISupport
operator|.
name|createQueryString
argument_list|(
name|map
argument_list|)
decl_stmt|;
name|String
name|url
decl_stmt|;
if|if
condition|(
name|api
condition|)
block|{
name|url
operator|=
literal|"netty-http:%s://%s:%s/%s?matchOnUriPrefix=true&httpMethodRestrict=%s"
expr_stmt|;
block|}
else|else
block|{
name|url
operator|=
literal|"netty-http:%s://%s:%s/%s?httpMethodRestrict=%s"
expr_stmt|;
block|}
comment|// must use upper case for restrict
name|String
name|restrict
init|=
name|verb
operator|.
name|toUpperCase
argument_list|(
name|Locale
operator|.
name|US
argument_list|)
decl_stmt|;
if|if
condition|(
name|cors
condition|)
block|{
name|restrict
operator|+=
literal|",OPTIONS"
expr_stmt|;
block|}
comment|// get the endpoint
name|url
operator|=
name|String
operator|.
name|format
argument_list|(
name|url
argument_list|,
name|scheme
argument_list|,
name|host
argument_list|,
name|port
argument_list|,
name|path
argument_list|,
name|restrict
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|query
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|url
operator|=
name|url
operator|+
literal|"&"
operator|+
name|query
expr_stmt|;
block|}
name|NettyHttpEndpoint
name|endpoint
init|=
name|camelContext
operator|.
name|getEndpoint
argument_list|(
name|url
argument_list|,
name|NettyHttpEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|setProperties
argument_list|(
name|camelContext
argument_list|,
name|endpoint
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
comment|// configure consumer properties
name|Consumer
name|consumer
init|=
name|endpoint
operator|.
name|createConsumer
argument_list|(
name|processor
argument_list|)
decl_stmt|;
if|if
condition|(
name|config
operator|.
name|getConsumerProperties
argument_list|()
operator|!=
literal|null
operator|&&
operator|!
name|config
operator|.
name|getConsumerProperties
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|setProperties
argument_list|(
name|camelContext
argument_list|,
name|consumer
argument_list|,
name|config
operator|.
name|getConsumerProperties
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|consumer
return|;
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
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
name|RestConfiguration
name|config
init|=
name|getCamelContext
argument_list|()
operator|.
name|getRestConfiguration
argument_list|(
literal|"netty-http"
argument_list|,
literal|true
argument_list|)
decl_stmt|;
comment|// configure additional options on netty-http configuration
if|if
condition|(
name|config
operator|.
name|getComponentProperties
argument_list|()
operator|!=
literal|null
operator|&&
operator|!
name|config
operator|.
name|getComponentProperties
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|setProperties
argument_list|(
name|this
argument_list|,
name|config
operator|.
name|getComponentProperties
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
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
name|ServiceHelper
operator|.
name|stopServices
argument_list|(
name|bootstrapFactories
operator|.
name|values
argument_list|()
argument_list|)
expr_stmt|;
name|bootstrapFactories
operator|.
name|clear
argument_list|()
expr_stmt|;
name|ServiceHelper
operator|.
name|stopService
argument_list|(
name|multiplexChannelHandlers
operator|.
name|values
argument_list|()
argument_list|)
expr_stmt|;
name|multiplexChannelHandlers
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

