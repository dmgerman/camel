begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.netty4
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
package|;
end_package

begin_import
import|import
name|java
operator|.
name|math
operator|.
name|BigInteger
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|Principal
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|net
operator|.
name|ssl
operator|.
name|SSLPeerUnverifiedException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|net
operator|.
name|ssl
operator|.
name|SSLSession
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|security
operator|.
name|cert
operator|.
name|X509Certificate
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
name|ChannelHandlerContext
import|;
end_import

begin_import
import|import
name|io
operator|.
name|netty
operator|.
name|handler
operator|.
name|ssl
operator|.
name|SslHandler
import|;
end_import

begin_import
import|import
name|io
operator|.
name|netty
operator|.
name|util
operator|.
name|Timer
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
name|impl
operator|.
name|SynchronousDelegateProducer
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
name|util
operator|.
name|ObjectHelper
import|;
end_import

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|scheme
operator|=
literal|"netty"
argument_list|,
name|consumerClass
operator|=
name|NettyConsumer
operator|.
name|class
argument_list|)
DECL|class|NettyEndpoint
specifier|public
class|class
name|NettyEndpoint
extends|extends
name|DefaultEndpoint
block|{
annotation|@
name|UriParam
DECL|field|configuration
specifier|private
name|NettyConfiguration
name|configuration
decl_stmt|;
comment|// TODO do we really need this time in netty4
DECL|field|timer
specifier|private
name|Timer
name|timer
decl_stmt|;
DECL|method|NettyEndpoint (String endpointUri, NettyComponent component, NettyConfiguration configuration)
specifier|public
name|NettyEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|NettyComponent
name|component
parameter_list|,
name|NettyConfiguration
name|configuration
parameter_list|)
block|{
name|super
argument_list|(
name|endpointUri
argument_list|,
name|component
argument_list|)
expr_stmt|;
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
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
name|Consumer
name|answer
init|=
operator|new
name|NettyConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|,
name|configuration
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
name|Producer
name|answer
init|=
operator|new
name|NettyProducer
argument_list|(
name|this
argument_list|,
name|configuration
argument_list|)
decl_stmt|;
if|if
condition|(
name|isSynchronous
argument_list|()
condition|)
block|{
return|return
operator|new
name|SynchronousDelegateProducer
argument_list|(
name|answer
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|answer
return|;
block|}
block|}
DECL|method|createExchange (ChannelHandlerContext ctx, Object message)
specifier|public
name|Exchange
name|createExchange
parameter_list|(
name|ChannelHandlerContext
name|ctx
parameter_list|,
name|Object
name|message
parameter_list|)
throws|throws
name|Exception
block|{
name|Exchange
name|exchange
init|=
name|createExchange
argument_list|()
decl_stmt|;
name|updateMessageHeader
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
argument_list|,
name|ctx
argument_list|)
expr_stmt|;
name|NettyPayloadHelper
operator|.
name|setIn
argument_list|(
name|exchange
argument_list|,
name|message
argument_list|)
expr_stmt|;
return|return
name|exchange
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
annotation|@
name|Override
DECL|method|getComponent ()
specifier|public
name|NettyComponent
name|getComponent
parameter_list|()
block|{
return|return
operator|(
name|NettyComponent
operator|)
name|super
operator|.
name|getComponent
argument_list|()
return|;
block|}
DECL|method|getConfiguration ()
specifier|public
name|NettyConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
DECL|method|setConfiguration (NettyConfiguration configuration)
specifier|public
name|void
name|setConfiguration
parameter_list|(
name|NettyConfiguration
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
DECL|method|setTimer (Timer timer)
specifier|public
name|void
name|setTimer
parameter_list|(
name|Timer
name|timer
parameter_list|)
block|{
name|this
operator|.
name|timer
operator|=
name|timer
expr_stmt|;
block|}
DECL|method|getTimer ()
specifier|public
name|Timer
name|getTimer
parameter_list|()
block|{
return|return
name|timer
return|;
block|}
annotation|@
name|Override
DECL|method|createEndpointUri ()
specifier|protected
name|String
name|createEndpointUri
parameter_list|()
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|configuration
argument_list|,
literal|"configuration"
argument_list|)
expr_stmt|;
return|return
literal|"netty:"
operator|+
name|getConfiguration
argument_list|()
operator|.
name|getProtocol
argument_list|()
operator|+
literal|"://"
operator|+
name|getConfiguration
argument_list|()
operator|.
name|getHost
argument_list|()
operator|+
literal|":"
operator|+
name|getConfiguration
argument_list|()
operator|.
name|getPort
argument_list|()
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
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|timer
argument_list|,
literal|"timer"
argument_list|)
expr_stmt|;
block|}
DECL|method|getSSLSession (ChannelHandlerContext ctx)
specifier|protected
name|SSLSession
name|getSSLSession
parameter_list|(
name|ChannelHandlerContext
name|ctx
parameter_list|)
block|{
specifier|final
name|SslHandler
name|sslHandler
init|=
name|ctx
operator|.
name|pipeline
argument_list|()
operator|.
name|get
argument_list|(
name|SslHandler
operator|.
name|class
argument_list|)
decl_stmt|;
name|SSLSession
name|sslSession
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|sslHandler
operator|!=
literal|null
condition|)
block|{
name|sslSession
operator|=
name|sslHandler
operator|.
name|engine
argument_list|()
operator|.
name|getSession
argument_list|()
expr_stmt|;
block|}
return|return
name|sslSession
return|;
block|}
DECL|method|updateMessageHeader (Message in, ChannelHandlerContext ctx)
specifier|protected
name|void
name|updateMessageHeader
parameter_list|(
name|Message
name|in
parameter_list|,
name|ChannelHandlerContext
name|ctx
parameter_list|)
block|{
name|in
operator|.
name|setHeader
argument_list|(
name|NettyConstants
operator|.
name|NETTY_CHANNEL_HANDLER_CONTEXT
argument_list|,
name|ctx
argument_list|)
expr_stmt|;
name|in
operator|.
name|setHeader
argument_list|(
name|NettyConstants
operator|.
name|NETTY_REMOTE_ADDRESS
argument_list|,
name|ctx
operator|.
name|channel
argument_list|()
operator|.
name|remoteAddress
argument_list|()
argument_list|)
expr_stmt|;
name|in
operator|.
name|setHeader
argument_list|(
name|NettyConstants
operator|.
name|NETTY_LOCAL_ADDRESS
argument_list|,
name|ctx
operator|.
name|channel
argument_list|()
operator|.
name|localAddress
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|configuration
operator|.
name|isSsl
argument_list|()
condition|)
block|{
comment|// setup the SslSession header
name|SSLSession
name|sslSession
init|=
name|getSSLSession
argument_list|(
name|ctx
argument_list|)
decl_stmt|;
name|in
operator|.
name|setHeader
argument_list|(
name|NettyConstants
operator|.
name|NETTY_SSL_SESSION
argument_list|,
name|sslSession
argument_list|)
expr_stmt|;
comment|// enrich headers with details from the client certificate if option is enabled
if|if
condition|(
name|configuration
operator|.
name|isSslClientCertHeaders
argument_list|()
condition|)
block|{
name|enrichWithClientCertInformation
argument_list|(
name|sslSession
argument_list|,
name|in
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Enriches the message with client certificate details such as subject name, serial number etc.      *<p/>      * If the certificate is unverified then the headers is not enriched.      *      * @param sslSession  the SSL session      * @param message     the message to enrich      */
DECL|method|enrichWithClientCertInformation (SSLSession sslSession, Message message)
specifier|protected
name|void
name|enrichWithClientCertInformation
parameter_list|(
name|SSLSession
name|sslSession
parameter_list|,
name|Message
name|message
parameter_list|)
block|{
try|try
block|{
name|X509Certificate
index|[]
name|certificates
init|=
name|sslSession
operator|.
name|getPeerCertificateChain
argument_list|()
decl_stmt|;
if|if
condition|(
name|certificates
operator|!=
literal|null
operator|&&
name|certificates
operator|.
name|length
operator|>
literal|0
condition|)
block|{
name|X509Certificate
name|cert
init|=
name|certificates
index|[
literal|0
index|]
decl_stmt|;
name|Principal
name|subject
init|=
name|cert
operator|.
name|getSubjectDN
argument_list|()
decl_stmt|;
if|if
condition|(
name|subject
operator|!=
literal|null
condition|)
block|{
name|message
operator|.
name|setHeader
argument_list|(
name|NettyConstants
operator|.
name|NETTY_SSL_CLIENT_CERT_SUBJECT_NAME
argument_list|,
name|subject
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|Principal
name|issuer
init|=
name|cert
operator|.
name|getIssuerDN
argument_list|()
decl_stmt|;
if|if
condition|(
name|issuer
operator|!=
literal|null
condition|)
block|{
name|message
operator|.
name|setHeader
argument_list|(
name|NettyConstants
operator|.
name|NETTY_SSL_CLIENT_CERT_ISSUER_NAME
argument_list|,
name|issuer
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|BigInteger
name|serial
init|=
name|cert
operator|.
name|getSerialNumber
argument_list|()
decl_stmt|;
if|if
condition|(
name|serial
operator|!=
literal|null
condition|)
block|{
name|message
operator|.
name|setHeader
argument_list|(
name|NettyConstants
operator|.
name|NETTY_SSL_CLIENT_CERT_SERIAL_NO
argument_list|,
name|serial
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|message
operator|.
name|setHeader
argument_list|(
name|NettyConstants
operator|.
name|NETTY_SSL_CLIENT_CERT_NOT_BEFORE
argument_list|,
name|cert
operator|.
name|getNotBefore
argument_list|()
argument_list|)
expr_stmt|;
name|message
operator|.
name|setHeader
argument_list|(
name|NettyConstants
operator|.
name|NETTY_SSL_CLIENT_CERT_NOT_AFTER
argument_list|,
name|cert
operator|.
name|getNotAfter
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|SSLPeerUnverifiedException
name|e
parameter_list|)
block|{
comment|// ignore
block|}
block|}
block|}
end_class

end_unit

