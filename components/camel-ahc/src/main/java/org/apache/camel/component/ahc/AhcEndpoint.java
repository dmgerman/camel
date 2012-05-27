begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.ahc
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|ahc
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
name|javax
operator|.
name|net
operator|.
name|ssl
operator|.
name|SSLContext
import|;
end_import

begin_import
import|import
name|com
operator|.
name|ning
operator|.
name|http
operator|.
name|client
operator|.
name|AsyncHttpClient
import|;
end_import

begin_import
import|import
name|com
operator|.
name|ning
operator|.
name|http
operator|.
name|client
operator|.
name|AsyncHttpClientConfig
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
name|jsse
operator|.
name|SSLContextParameters
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
DECL|class|AhcEndpoint
specifier|public
class|class
name|AhcEndpoint
extends|extends
name|DefaultEndpoint
implements|implements
name|HeaderFilterStrategyAware
block|{
DECL|field|client
specifier|private
name|AsyncHttpClient
name|client
decl_stmt|;
DECL|field|clientConfig
specifier|private
name|AsyncHttpClientConfig
name|clientConfig
decl_stmt|;
DECL|field|headerFilterStrategy
specifier|private
name|HeaderFilterStrategy
name|headerFilterStrategy
init|=
operator|new
name|HttpHeaderFilterStrategy
argument_list|()
decl_stmt|;
DECL|field|binding
specifier|private
name|AhcBinding
name|binding
decl_stmt|;
DECL|field|httpUri
specifier|private
name|URI
name|httpUri
decl_stmt|;
DECL|field|bridgeEndpoint
specifier|private
name|boolean
name|bridgeEndpoint
decl_stmt|;
DECL|field|throwExceptionOnFailure
specifier|private
name|boolean
name|throwExceptionOnFailure
init|=
literal|true
decl_stmt|;
DECL|field|transferException
specifier|private
name|boolean
name|transferException
decl_stmt|;
DECL|field|sslContextParameters
specifier|private
name|SSLContextParameters
name|sslContextParameters
decl_stmt|;
DECL|method|AhcEndpoint (String endpointUri, AhcComponent component, URI httpUri)
specifier|public
name|AhcEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|AhcComponent
name|component
parameter_list|,
name|URI
name|httpUri
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
name|httpUri
operator|=
name|httpUri
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getComponent ()
specifier|public
name|AhcComponent
name|getComponent
parameter_list|()
block|{
return|return
operator|(
name|AhcComponent
operator|)
name|super
operator|.
name|getComponent
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|client
argument_list|,
literal|"AsyncHttpClient"
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|httpUri
argument_list|,
literal|"HttpUri"
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|binding
argument_list|,
literal|"AhcBinding"
argument_list|,
name|this
argument_list|)
expr_stmt|;
return|return
operator|new
name|AhcProducer
argument_list|(
name|this
argument_list|)
return|;
block|}
annotation|@
name|Override
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
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"This component does not support consuming from this endpoint"
argument_list|)
throw|;
block|}
annotation|@
name|Override
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
annotation|@
name|Override
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
DECL|method|getClient ()
specifier|public
name|AsyncHttpClient
name|getClient
parameter_list|()
block|{
return|return
name|client
return|;
block|}
DECL|method|setClient (AsyncHttpClient client)
specifier|public
name|void
name|setClient
parameter_list|(
name|AsyncHttpClient
name|client
parameter_list|)
block|{
name|this
operator|.
name|client
operator|=
name|client
expr_stmt|;
block|}
DECL|method|getClientConfig ()
specifier|public
name|AsyncHttpClientConfig
name|getClientConfig
parameter_list|()
block|{
return|return
name|clientConfig
return|;
block|}
DECL|method|setClientConfig (AsyncHttpClientConfig clientConfig)
specifier|public
name|void
name|setClientConfig
parameter_list|(
name|AsyncHttpClientConfig
name|clientConfig
parameter_list|)
block|{
name|this
operator|.
name|clientConfig
operator|=
name|clientConfig
expr_stmt|;
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
DECL|method|getBinding ()
specifier|public
name|AhcBinding
name|getBinding
parameter_list|()
block|{
return|return
name|binding
return|;
block|}
DECL|method|setBinding (AhcBinding binding)
specifier|public
name|void
name|setBinding
parameter_list|(
name|AhcBinding
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
if|if
condition|(
name|client
operator|==
literal|null
condition|)
block|{
name|AsyncHttpClientConfig
name|config
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|clientConfig
operator|!=
literal|null
condition|)
block|{
name|AsyncHttpClientConfig
operator|.
name|Builder
name|builder
init|=
name|AhcComponent
operator|.
name|cloneConfig
argument_list|(
name|clientConfig
argument_list|)
decl_stmt|;
if|if
condition|(
name|sslContextParameters
operator|!=
literal|null
condition|)
block|{
comment|// TODO: Remove us, but part of investigation issue on Java7 on ubuntu
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|sslContextParameters
argument_list|)
expr_stmt|;
name|SSLContext
name|ssl
init|=
name|sslContextParameters
operator|.
name|createSSLContext
argument_list|()
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|ssl
argument_list|)
expr_stmt|;
name|builder
operator|.
name|setSSLContext
argument_list|(
name|ssl
argument_list|)
expr_stmt|;
block|}
name|config
operator|=
name|builder
operator|.
name|build
argument_list|()
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|sslContextParameters
operator|!=
literal|null
condition|)
block|{
name|AsyncHttpClientConfig
operator|.
name|Builder
name|builder
init|=
operator|new
name|AsyncHttpClientConfig
operator|.
name|Builder
argument_list|()
decl_stmt|;
comment|// TODO: Remove us, but part of investigation issue on Java7 on ubuntu
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|sslContextParameters
argument_list|)
expr_stmt|;
name|SSLContext
name|ssl
init|=
name|sslContextParameters
operator|.
name|createSSLContext
argument_list|()
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|ssl
argument_list|)
expr_stmt|;
name|builder
operator|.
name|setSSLContext
argument_list|(
name|ssl
argument_list|)
expr_stmt|;
name|config
operator|=
name|builder
operator|.
name|build
argument_list|()
expr_stmt|;
block|}
block|}
if|if
condition|(
name|config
operator|==
literal|null
condition|)
block|{
name|client
operator|=
operator|new
name|AsyncHttpClient
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|client
operator|=
operator|new
name|AsyncHttpClient
argument_list|(
name|config
argument_list|)
expr_stmt|;
block|}
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
comment|// ensure client is closed when stopping
if|if
condition|(
name|client
operator|!=
literal|null
operator|&&
operator|!
name|client
operator|.
name|isClosed
argument_list|()
condition|)
block|{
name|client
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
name|client
operator|=
literal|null
expr_stmt|;
block|}
block|}
end_class

end_unit

