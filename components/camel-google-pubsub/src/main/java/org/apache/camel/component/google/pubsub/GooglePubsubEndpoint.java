begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.google.pubsub
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|google
operator|.
name|pubsub
package|;
end_package

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
name|google
operator|.
name|api
operator|.
name|client
operator|.
name|repackaged
operator|.
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Strings
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|api
operator|.
name|services
operator|.
name|pubsub
operator|.
name|Pubsub
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
name|Component
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
comment|/**  * Messaging client for Google Cloud Platform PubSub Service:  * https://cloud.google.com/pubsub/  *  * Built on top of the Service API libraries (v1).  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|scheme
operator|=
literal|"google-pubsub"
argument_list|,
name|title
operator|=
literal|"Google Pubsub"
argument_list|,
name|syntax
operator|=
literal|"google-pubsub:projectId:destinationName"
argument_list|,
name|label
operator|=
literal|"messaging"
argument_list|)
DECL|class|GooglePubsubEndpoint
specifier|public
class|class
name|GooglePubsubEndpoint
extends|extends
name|DefaultEndpoint
block|{
DECL|field|log
specifier|private
name|Logger
name|log
decl_stmt|;
annotation|@
name|UriPath
argument_list|(
name|description
operator|=
literal|"Project Id"
argument_list|)
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|"true"
argument_list|)
DECL|field|projectId
specifier|private
name|String
name|projectId
decl_stmt|;
annotation|@
name|UriPath
argument_list|(
name|description
operator|=
literal|"Destination Name"
argument_list|)
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|"true"
argument_list|)
DECL|field|destinationName
specifier|private
name|String
name|destinationName
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|name
operator|=
literal|"loggerId"
argument_list|,
name|description
operator|=
literal|"Logger ID to use when a match to the parent route required"
argument_list|)
DECL|field|loggerId
specifier|private
name|String
name|loggerId
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|name
operator|=
literal|"concurrentConsumers"
argument_list|,
name|description
operator|=
literal|"The number of parallel streams consuming from the subscription"
argument_list|,
name|defaultValue
operator|=
literal|1
argument_list|)
DECL|field|concurrentConsumers
specifier|private
name|Integer
name|concurrentConsumers
init|=
literal|1
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|name
operator|=
literal|"maxMessagesPerPoll"
argument_list|,
name|description
operator|=
literal|"The max number of messages to receive from the server in a single API call"
argument_list|,
name|defaultValue
operator|=
literal|1
argument_list|)
DECL|field|maxMessagesPerPoll
specifier|private
name|Integer
name|maxMessagesPerPoll
init|=
literal|1
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|name
operator|=
literal|"connectionFactory"
argument_list|,
name|description
operator|=
literal|"ConnectionFactory to obtain connection to PubSub Service. If non provided the default one will be used"
argument_list|)
DECL|field|connectionFactory
specifier|private
name|GooglePubsubConnectionFactory
name|connectionFactory
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"AUTO"
argument_list|,
name|enums
operator|=
literal|"AUTO,NONE"
argument_list|,
name|description
operator|=
literal|"AUTO = exchange gets ack'ed/nack'ed on completion. NONE = downstream process has to ack/nack explicitly"
argument_list|)
DECL|field|ackMode
specifier|private
name|GooglePubsubConstants
operator|.
name|AckMode
name|ackMode
init|=
name|GooglePubsubConstants
operator|.
name|AckMode
operator|.
name|AUTO
decl_stmt|;
DECL|field|pubsub
specifier|private
name|Pubsub
name|pubsub
decl_stmt|;
DECL|method|GooglePubsubEndpoint (String uri, Component component, String remaining)
specifier|public
name|GooglePubsubEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|Component
name|component
parameter_list|,
name|String
name|remaining
parameter_list|)
block|{
name|super
argument_list|(
name|uri
argument_list|,
name|component
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
operator|(
name|component
operator|instanceof
name|GooglePubsubComponent
operator|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"The component provided is not GooglePubsubComponent : "
operator|+
name|component
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|getComponent ()
specifier|public
name|GooglePubsubComponent
name|getComponent
parameter_list|()
block|{
return|return
operator|(
name|GooglePubsubComponent
operator|)
name|super
operator|.
name|getComponent
argument_list|()
return|;
block|}
DECL|method|afterPropertiesSet ()
specifier|public
name|void
name|afterPropertiesSet
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|loggerId
argument_list|)
condition|)
block|{
name|log
operator|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|log
operator|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|loggerId
argument_list|)
expr_stmt|;
block|}
name|GooglePubsubConnectionFactory
name|cf
init|=
operator|(
literal|null
operator|==
name|connectionFactory
operator|)
condition|?
name|getComponent
argument_list|()
operator|.
name|getConnectionFactory
argument_list|()
else|:
name|connectionFactory
decl_stmt|;
name|pubsub
operator|=
name|cf
operator|.
name|getClient
argument_list|()
expr_stmt|;
name|log
operator|.
name|trace
argument_list|(
literal|"Project ID: {}"
argument_list|,
name|this
operator|.
name|projectId
argument_list|)
expr_stmt|;
name|log
operator|.
name|trace
argument_list|(
literal|"Destination Name: {}"
argument_list|,
name|this
operator|.
name|destinationName
argument_list|)
expr_stmt|;
name|log
operator|.
name|trace
argument_list|(
literal|"From file : {}"
argument_list|,
name|cf
operator|.
name|getCredentialsFileLocation
argument_list|()
argument_list|)
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
name|afterPropertiesSet
argument_list|()
expr_stmt|;
return|return
operator|new
name|GooglePubsubProducer
argument_list|(
name|this
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
name|afterPropertiesSet
argument_list|()
expr_stmt|;
name|setExchangePattern
argument_list|(
name|ExchangePattern
operator|.
name|InOnly
argument_list|)
expr_stmt|;
return|return
operator|new
name|GooglePubsubConsumer
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
literal|"GooglePubsubConsumer["
operator|+
name|getDestinationName
argument_list|()
operator|+
literal|"]"
argument_list|,
name|concurrentConsumers
argument_list|)
return|;
block|}
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
DECL|method|getProjectId ()
specifier|public
name|String
name|getProjectId
parameter_list|()
block|{
return|return
name|projectId
return|;
block|}
DECL|method|setProjectId (String projectId)
specifier|public
name|void
name|setProjectId
parameter_list|(
name|String
name|projectId
parameter_list|)
block|{
name|this
operator|.
name|projectId
operator|=
name|projectId
expr_stmt|;
block|}
DECL|method|getLoggerId ()
specifier|public
name|String
name|getLoggerId
parameter_list|()
block|{
return|return
name|loggerId
return|;
block|}
DECL|method|setLoggerId (String loggerId)
specifier|public
name|void
name|setLoggerId
parameter_list|(
name|String
name|loggerId
parameter_list|)
block|{
name|this
operator|.
name|loggerId
operator|=
name|loggerId
expr_stmt|;
block|}
DECL|method|getDestinationName ()
specifier|public
name|String
name|getDestinationName
parameter_list|()
block|{
return|return
name|destinationName
return|;
block|}
DECL|method|setDestinationName (String destinationName)
specifier|public
name|void
name|setDestinationName
parameter_list|(
name|String
name|destinationName
parameter_list|)
block|{
name|this
operator|.
name|destinationName
operator|=
name|destinationName
expr_stmt|;
block|}
DECL|method|getConcurrentConsumers ()
specifier|public
name|Integer
name|getConcurrentConsumers
parameter_list|()
block|{
return|return
name|concurrentConsumers
return|;
block|}
DECL|method|setConcurrentConsumers (Integer concurrentConsumers)
specifier|public
name|void
name|setConcurrentConsumers
parameter_list|(
name|Integer
name|concurrentConsumers
parameter_list|)
block|{
name|this
operator|.
name|concurrentConsumers
operator|=
name|concurrentConsumers
expr_stmt|;
block|}
DECL|method|getMaxMessagesPerPoll ()
specifier|public
name|Integer
name|getMaxMessagesPerPoll
parameter_list|()
block|{
return|return
name|maxMessagesPerPoll
return|;
block|}
DECL|method|setMaxMessagesPerPoll (Integer maxMessagesPerPoll)
specifier|public
name|void
name|setMaxMessagesPerPoll
parameter_list|(
name|Integer
name|maxMessagesPerPoll
parameter_list|)
block|{
name|this
operator|.
name|maxMessagesPerPoll
operator|=
name|maxMessagesPerPoll
expr_stmt|;
block|}
DECL|method|getAckMode ()
specifier|public
name|GooglePubsubConstants
operator|.
name|AckMode
name|getAckMode
parameter_list|()
block|{
return|return
name|ackMode
return|;
block|}
DECL|method|setAckMode (GooglePubsubConstants.AckMode ackMode)
specifier|public
name|void
name|setAckMode
parameter_list|(
name|GooglePubsubConstants
operator|.
name|AckMode
name|ackMode
parameter_list|)
block|{
name|this
operator|.
name|ackMode
operator|=
name|ackMode
expr_stmt|;
block|}
DECL|method|getPubsub ()
specifier|public
name|Pubsub
name|getPubsub
parameter_list|()
block|{
return|return
name|pubsub
return|;
block|}
DECL|method|getConnectionFactory ()
specifier|public
name|GooglePubsubConnectionFactory
name|getConnectionFactory
parameter_list|()
block|{
return|return
name|connectionFactory
return|;
block|}
DECL|method|setConnectionFactory (GooglePubsubConnectionFactory connectionFactory)
specifier|public
name|void
name|setConnectionFactory
parameter_list|(
name|GooglePubsubConnectionFactory
name|connectionFactory
parameter_list|)
block|{
name|this
operator|.
name|connectionFactory
operator|=
name|connectionFactory
expr_stmt|;
block|}
block|}
end_class

end_unit

