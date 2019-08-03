begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.nsq
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|nsq
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
name|com
operator|.
name|github
operator|.
name|brainlag
operator|.
name|nsq
operator|.
name|NSQConfig
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
name|JdkSslContext
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
name|SslContext
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

begin_comment
comment|/**  * Represents a nsq endpoint.  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|firstVersion
operator|=
literal|"2.23.0"
argument_list|,
name|scheme
operator|=
literal|"nsq"
argument_list|,
name|title
operator|=
literal|"NSQ"
argument_list|,
name|syntax
operator|=
literal|"nsq:servers"
argument_list|,
name|label
operator|=
literal|"messaging"
argument_list|)
DECL|class|NsqEndpoint
specifier|public
class|class
name|NsqEndpoint
extends|extends
name|DefaultEndpoint
block|{
annotation|@
name|UriParam
DECL|field|configuration
specifier|private
name|NsqConfiguration
name|configuration
decl_stmt|;
DECL|method|NsqEndpoint (String uri, NsqComponent component, NsqConfiguration configuration)
specifier|public
name|NsqEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|NsqComponent
name|component
parameter_list|,
name|NsqConfiguration
name|configuration
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
name|configuration
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
name|NsqProducer
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
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|configuration
operator|.
name|getTopic
argument_list|()
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
literal|"Missing required endpoint configuration: topic must be defined for NSQ consumer"
argument_list|)
throw|;
block|}
return|return
operator|new
name|NsqConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|)
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
literal|"NsqTopic["
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
DECL|method|getNsqConfiguration ()
specifier|public
name|NsqConfiguration
name|getNsqConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
DECL|method|getNsqConfig ()
specifier|public
name|NSQConfig
name|getNsqConfig
parameter_list|()
throws|throws
name|GeneralSecurityException
throws|,
name|IOException
block|{
name|NSQConfig
name|nsqConfig
init|=
operator|new
name|NSQConfig
argument_list|()
decl_stmt|;
if|if
condition|(
name|getNsqConfiguration
argument_list|()
operator|.
name|getSslContextParameters
argument_list|()
operator|!=
literal|null
operator|&&
name|getNsqConfiguration
argument_list|()
operator|.
name|isSecure
argument_list|()
condition|)
block|{
name|SslContext
name|sslContext
init|=
operator|new
name|JdkSslContext
argument_list|(
name|getNsqConfiguration
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
argument_list|,
literal|true
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|nsqConfig
operator|.
name|setSslContext
argument_list|(
name|sslContext
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|configuration
operator|.
name|getUserAgent
argument_list|()
operator|!=
literal|null
operator|&&
operator|!
name|configuration
operator|.
name|getUserAgent
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|nsqConfig
operator|.
name|setUserAgent
argument_list|(
name|configuration
operator|.
name|getUserAgent
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|configuration
operator|.
name|getMessageTimeout
argument_list|()
operator|>
operator|-
literal|1
condition|)
block|{
name|nsqConfig
operator|.
name|setMsgTimeout
argument_list|(
operator|(
name|int
operator|)
name|configuration
operator|.
name|getMessageTimeout
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|nsqConfig
return|;
block|}
block|}
end_class

end_unit

