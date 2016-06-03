begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.ironmq
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|ironmq
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|MalformedURLException
import|;
end_import

begin_import
import|import
name|io
operator|.
name|iron
operator|.
name|ironmq
operator|.
name|Client
import|;
end_import

begin_import
import|import
name|io
operator|.
name|iron
operator|.
name|ironmq
operator|.
name|Cloud
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
name|impl
operator|.
name|DefaultExchange
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
name|DefaultScheduledPollConsumerScheduler
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
name|ScheduledPollEndpoint
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
comment|/**  * Represents a IronMQ endpoint.  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|scheme
operator|=
literal|"ironmq"
argument_list|,
name|syntax
operator|=
literal|"ironmq:queueName"
argument_list|,
name|title
operator|=
literal|"ironmq"
argument_list|,
name|consumerClass
operator|=
name|IronMQConsumer
operator|.
name|class
argument_list|,
name|label
operator|=
literal|"cloud,messaging"
argument_list|)
DECL|class|IronMQEndpoint
specifier|public
class|class
name|IronMQEndpoint
extends|extends
name|ScheduledPollEndpoint
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
name|IronMQEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|UriParam
DECL|field|configuration
specifier|private
name|IronMQConfiguration
name|configuration
decl_stmt|;
DECL|field|client
specifier|private
name|Client
name|client
decl_stmt|;
DECL|method|IronMQEndpoint (String uri, IronMQComponent component, IronMQConfiguration ironMQConfiguration)
specifier|public
name|IronMQEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|IronMQComponent
name|component
parameter_list|,
name|IronMQConfiguration
name|ironMQConfiguration
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
name|ironMQConfiguration
expr_stmt|;
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
name|IronMQProducer
argument_list|(
name|this
argument_list|,
name|getClient
argument_list|()
operator|.
name|queue
argument_list|(
name|configuration
operator|.
name|getQueueName
argument_list|()
argument_list|)
argument_list|)
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
name|IronMQConsumer
name|ironMQConsumer
init|=
operator|new
name|IronMQConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|,
name|getClient
argument_list|()
operator|.
name|queue
argument_list|(
name|configuration
operator|.
name|getQueueName
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|configureConsumer
argument_list|(
name|ironMQConsumer
argument_list|)
expr_stmt|;
name|ironMQConsumer
operator|.
name|setMaxMessagesPerPoll
argument_list|(
name|configuration
operator|.
name|getMaxMessagesPerPoll
argument_list|()
argument_list|)
expr_stmt|;
name|DefaultScheduledPollConsumerScheduler
name|scheduler
init|=
operator|new
name|DefaultScheduledPollConsumerScheduler
argument_list|()
decl_stmt|;
name|scheduler
operator|.
name|setConcurrentTasks
argument_list|(
name|configuration
operator|.
name|getConcurrentConsumers
argument_list|()
argument_list|)
expr_stmt|;
name|ironMQConsumer
operator|.
name|setScheduler
argument_list|(
name|scheduler
argument_list|)
expr_stmt|;
return|return
name|ironMQConsumer
return|;
block|}
DECL|method|createExchange (io.iron.ironmq.Message msg)
specifier|public
name|Exchange
name|createExchange
parameter_list|(
name|io
operator|.
name|iron
operator|.
name|ironmq
operator|.
name|Message
name|msg
parameter_list|)
block|{
return|return
name|createExchange
argument_list|(
name|getExchangePattern
argument_list|()
argument_list|,
name|msg
argument_list|)
return|;
block|}
DECL|method|createExchange (ExchangePattern pattern, io.iron.ironmq.Message msg)
specifier|private
name|Exchange
name|createExchange
parameter_list|(
name|ExchangePattern
name|pattern
parameter_list|,
name|io
operator|.
name|iron
operator|.
name|ironmq
operator|.
name|Message
name|msg
parameter_list|)
block|{
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|this
argument_list|,
name|pattern
argument_list|)
decl_stmt|;
name|Message
name|message
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
if|if
condition|(
name|configuration
operator|.
name|isPreserveHeaders
argument_list|()
condition|)
block|{
name|GsonUtil
operator|.
name|copyFrom
argument_list|(
name|msg
argument_list|,
name|message
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|message
operator|.
name|setBody
argument_list|(
name|msg
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|message
operator|.
name|setHeader
argument_list|(
name|IronMQConstants
operator|.
name|MESSAGE_ID
argument_list|,
name|msg
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|message
operator|.
name|setHeader
argument_list|(
name|IronMQConstants
operator|.
name|MESSAGE_RESERVATION_ID
argument_list|,
name|msg
operator|.
name|getReservationId
argument_list|()
argument_list|)
expr_stmt|;
name|message
operator|.
name|setHeader
argument_list|(
name|IronMQConstants
operator|.
name|MESSAGE_RESERVED_COUNT
argument_list|,
name|msg
operator|.
name|getReservedCount
argument_list|()
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
name|client
operator|=
name|getConfiguration
argument_list|()
operator|.
name|getClient
argument_list|()
operator|!=
literal|null
condition|?
name|getConfiguration
argument_list|()
operator|.
name|getClient
argument_list|()
else|:
name|getClient
argument_list|()
expr_stmt|;
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
name|client
operator|=
literal|null
expr_stmt|;
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
block|}
DECL|method|getClient ()
specifier|public
name|Client
name|getClient
parameter_list|()
block|{
if|if
condition|(
name|client
operator|==
literal|null
condition|)
block|{
name|client
operator|=
name|createClient
argument_list|()
expr_stmt|;
block|}
return|return
name|client
return|;
block|}
DECL|method|setClient (Client client)
specifier|public
name|void
name|setClient
parameter_list|(
name|Client
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
comment|/**      * Provide the possibility to override this method for an mock      * implementation      *       * @return Client      */
DECL|method|createClient ()
name|Client
name|createClient
parameter_list|()
block|{
name|Cloud
name|cloud
decl_stmt|;
try|try
block|{
name|cloud
operator|=
operator|new
name|Cloud
argument_list|(
name|configuration
operator|.
name|getIronMQCloud
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|MalformedURLException
name|e
parameter_list|)
block|{
name|cloud
operator|=
name|Cloud
operator|.
name|ironAWSUSEast
expr_stmt|;
name|LOG
operator|.
name|warn
argument_list|(
literal|"Unable to parse ironMQCloud {} will use {}"
argument_list|,
name|configuration
operator|.
name|getIronMQCloud
argument_list|()
argument_list|,
name|cloud
operator|.
name|getHost
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|client
operator|=
operator|new
name|Client
argument_list|(
name|configuration
operator|.
name|getProjectId
argument_list|()
argument_list|,
name|configuration
operator|.
name|getToken
argument_list|()
argument_list|,
name|cloud
argument_list|)
expr_stmt|;
return|return
name|client
return|;
block|}
DECL|method|getConfiguration ()
specifier|public
name|IronMQConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
block|}
end_class

end_unit

