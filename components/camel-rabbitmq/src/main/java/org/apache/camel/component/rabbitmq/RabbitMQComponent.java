begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.rabbitmq
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|rabbitmq
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
name|Map
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
name|TrustManager
import|;
end_import

begin_import
import|import
name|com
operator|.
name|rabbitmq
operator|.
name|client
operator|.
name|ConnectionFactory
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
name|impl
operator|.
name|UriEndpointComponent
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

begin_class
DECL|class|RabbitMQComponent
specifier|public
class|class
name|RabbitMQComponent
extends|extends
name|UriEndpointComponent
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
name|RabbitMQComponent
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|RabbitMQComponent ()
specifier|public
name|RabbitMQComponent
parameter_list|()
block|{
name|super
argument_list|(
name|RabbitMQEndpoint
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
DECL|method|RabbitMQComponent (CamelContext context)
specifier|public
name|RabbitMQComponent
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
name|super
argument_list|(
name|context
argument_list|,
name|RabbitMQEndpoint
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createEndpoint (String uri, String remaining, Map<String, Object> params)
specifier|protected
name|RabbitMQEndpoint
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
name|params
parameter_list|)
throws|throws
name|Exception
block|{
name|URI
name|host
init|=
operator|new
name|URI
argument_list|(
literal|"http://"
operator|+
name|remaining
argument_list|)
decl_stmt|;
name|String
name|hostname
init|=
name|host
operator|.
name|getHost
argument_list|()
decl_stmt|;
name|int
name|portNumber
init|=
name|host
operator|.
name|getPort
argument_list|()
decl_stmt|;
name|String
name|exchangeName
init|=
literal|""
decl_stmt|;
comment|// We need to support the exchange to be "" the path is empty
if|if
condition|(
name|host
operator|.
name|getPath
argument_list|()
operator|.
name|trim
argument_list|()
operator|.
name|length
argument_list|()
operator|>
literal|1
condition|)
block|{
name|exchangeName
operator|=
name|host
operator|.
name|getPath
argument_list|()
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
comment|// ConnectionFactory reference
name|ConnectionFactory
name|connectionFactory
init|=
name|resolveAndRemoveReferenceParameter
argument_list|(
name|params
argument_list|,
literal|"connectionFactory"
argument_list|,
name|ConnectionFactory
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|clientProperties
init|=
name|resolveAndRemoveReferenceParameter
argument_list|(
name|params
argument_list|,
literal|"clientProperties"
argument_list|,
name|Map
operator|.
name|class
argument_list|)
decl_stmt|;
name|TrustManager
name|trustManager
init|=
name|resolveAndRemoveReferenceParameter
argument_list|(
name|params
argument_list|,
literal|"trustManager"
argument_list|,
name|TrustManager
operator|.
name|class
argument_list|)
decl_stmt|;
name|RabbitMQEndpoint
name|endpoint
decl_stmt|;
if|if
condition|(
name|connectionFactory
operator|==
literal|null
condition|)
block|{
name|endpoint
operator|=
operator|new
name|RabbitMQEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|endpoint
operator|=
operator|new
name|RabbitMQEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|,
name|connectionFactory
argument_list|)
expr_stmt|;
block|}
name|endpoint
operator|.
name|setHostname
argument_list|(
name|hostname
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|setPortNumber
argument_list|(
name|portNumber
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|setExchangeName
argument_list|(
name|exchangeName
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|setClientProperties
argument_list|(
name|clientProperties
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|setTrustManager
argument_list|(
name|trustManager
argument_list|)
expr_stmt|;
name|setProperties
argument_list|(
name|endpoint
argument_list|,
name|params
argument_list|)
expr_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Creating RabbitMQEndpoint with host {}:{} and exchangeName: {}"
argument_list|,
operator|new
name|Object
index|[]
block|{
name|endpoint
operator|.
name|getHostname
argument_list|()
block|,
name|endpoint
operator|.
name|getPortNumber
argument_list|()
block|,
name|endpoint
operator|.
name|getExchangeName
argument_list|()
block|}
argument_list|)
expr_stmt|;
block|}
return|return
name|endpoint
return|;
block|}
block|}
end_class

end_unit

