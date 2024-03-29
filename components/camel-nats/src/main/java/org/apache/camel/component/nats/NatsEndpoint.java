begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.nats
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|nats
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|GeneralSecurityException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ExecutorService
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
name|io
operator|.
name|nats
operator|.
name|client
operator|.
name|Connection
import|;
end_import

begin_import
import|import
name|io
operator|.
name|nats
operator|.
name|client
operator|.
name|Nats
import|;
end_import

begin_import
import|import
name|io
operator|.
name|nats
operator|.
name|client
operator|.
name|Options
import|;
end_import

begin_import
import|import
name|io
operator|.
name|nats
operator|.
name|client
operator|.
name|Options
operator|.
name|Builder
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
name|support
operator|.
name|DefaultEndpoint
import|;
end_import

begin_comment
comment|/**  * The nats component allows you produce and consume messages from<a href="http://nats.io/">NATS</a>.  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|firstVersion
operator|=
literal|"2.17.0"
argument_list|,
name|scheme
operator|=
literal|"nats"
argument_list|,
name|title
operator|=
literal|"Nats"
argument_list|,
name|syntax
operator|=
literal|"nats:topic"
argument_list|,
name|label
operator|=
literal|"messaging"
argument_list|)
DECL|class|NatsEndpoint
specifier|public
class|class
name|NatsEndpoint
extends|extends
name|DefaultEndpoint
block|{
annotation|@
name|UriParam
DECL|field|configuration
specifier|private
name|NatsConfiguration
name|configuration
decl_stmt|;
DECL|method|NatsEndpoint (String uri, NatsComponent component, NatsConfiguration config)
specifier|public
name|NatsEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|NatsComponent
name|component
parameter_list|,
name|NatsConfiguration
name|config
parameter_list|)
block|{
name|super
argument_list|(
name|uri
argument_list|,
name|component
argument_list|)
expr_stmt|;
name|this
operator|.
name|configuration
operator|=
name|config
expr_stmt|;
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
return|return
operator|new
name|NatsProducer
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
name|NatsConsumer
name|consumer
init|=
operator|new
name|NatsConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|)
decl_stmt|;
name|configureConsumer
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
return|return
name|consumer
return|;
block|}
DECL|method|createExecutor ()
specifier|public
name|ExecutorService
name|createExecutor
parameter_list|()
block|{
return|return
name|getCamelContext
argument_list|()
operator|.
name|getExecutorServiceManager
argument_list|()
operator|.
name|newFixedThreadPool
argument_list|(
name|this
argument_list|,
literal|"NatsTopic["
operator|+
name|configuration
operator|.
name|getTopic
argument_list|()
operator|+
literal|"]"
argument_list|,
name|configuration
operator|.
name|getPoolSize
argument_list|()
argument_list|)
return|;
block|}
DECL|method|getConfiguration ()
specifier|public
name|NatsConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
DECL|method|getConnection ()
specifier|public
name|Connection
name|getConnection
parameter_list|()
throws|throws
name|InterruptedException
throws|,
name|IllegalArgumentException
throws|,
name|GeneralSecurityException
throws|,
name|IOException
block|{
name|Builder
name|builder
init|=
name|getConfiguration
argument_list|()
operator|.
name|createOptions
argument_list|()
decl_stmt|;
if|if
condition|(
name|getConfiguration
argument_list|()
operator|.
name|getSslContextParameters
argument_list|()
operator|!=
literal|null
operator|&&
name|getConfiguration
argument_list|()
operator|.
name|isSecure
argument_list|()
condition|)
block|{
name|SSLContext
name|sslCtx
init|=
name|getConfiguration
argument_list|()
operator|.
name|getSslContextParameters
argument_list|()
operator|.
name|createSSLContext
argument_list|(
name|getCamelContext
argument_list|()
argument_list|)
decl_stmt|;
name|builder
operator|.
name|sslContext
argument_list|(
name|sslCtx
argument_list|)
expr_stmt|;
block|}
name|Options
name|options
init|=
name|builder
operator|.
name|build
argument_list|()
decl_stmt|;
return|return
name|Nats
operator|.
name|connect
argument_list|(
name|options
argument_list|)
return|;
block|}
block|}
end_class

end_unit

