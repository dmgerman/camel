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
name|NSQConsumer
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
name|NSQMessage
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
name|ServerAddress
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
name|callbacks
operator|.
name|NSQMessageCallback
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
name|lookup
operator|.
name|DefaultNSQLookup
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
name|lookup
operator|.
name|NSQLookup
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
name|support
operator|.
name|DefaultConsumer
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
comment|/**  * The nsq consumer.  */
end_comment

begin_class
DECL|class|NsqConsumer
specifier|public
class|class
name|NsqConsumer
extends|extends
name|DefaultConsumer
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
name|NsqConsumer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|consumer
name|NSQConsumer
name|consumer
decl_stmt|;
DECL|field|processor
specifier|private
specifier|final
name|Processor
name|processor
decl_stmt|;
DECL|field|executor
specifier|private
name|ExecutorService
name|executor
decl_stmt|;
DECL|field|active
specifier|private
name|boolean
name|active
decl_stmt|;
DECL|field|configuration
specifier|private
specifier|final
name|NsqConfiguration
name|configuration
decl_stmt|;
DECL|method|NsqConsumer (NsqEndpoint endpoint, Processor processor)
specifier|public
name|NsqConsumer
parameter_list|(
name|NsqEndpoint
name|endpoint
parameter_list|,
name|Processor
name|processor
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|,
name|processor
argument_list|)
expr_stmt|;
name|this
operator|.
name|processor
operator|=
name|processor
expr_stmt|;
name|this
operator|.
name|configuration
operator|=
name|getEndpoint
argument_list|()
operator|.
name|getConfiguration
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getEndpoint ()
specifier|public
name|NsqEndpoint
name|getEndpoint
parameter_list|()
block|{
return|return
operator|(
name|NsqEndpoint
operator|)
name|super
operator|.
name|getEndpoint
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
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Starting NSQ Consumer"
argument_list|)
expr_stmt|;
name|executor
operator|=
name|getEndpoint
argument_list|()
operator|.
name|createExecutor
argument_list|()
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Getting NSQ Connection"
argument_list|)
expr_stmt|;
name|NSQLookup
name|lookup
init|=
operator|new
name|DefaultNSQLookup
argument_list|()
decl_stmt|;
for|for
control|(
name|ServerAddress
name|server
range|:
name|configuration
operator|.
name|getServerAddresses
argument_list|()
control|)
block|{
name|lookup
operator|.
name|addLookupAddress
argument_list|(
name|server
operator|.
name|getHost
argument_list|()
argument_list|,
name|server
operator|.
name|getPort
argument_list|()
operator|==
literal|0
condition|?
name|configuration
operator|.
name|getLookupServerPort
argument_list|()
else|:
name|server
operator|.
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|consumer
operator|=
operator|new
name|NSQConsumer
argument_list|(
name|lookup
argument_list|,
name|configuration
operator|.
name|getTopic
argument_list|()
argument_list|,
name|configuration
operator|.
name|getChannel
argument_list|()
argument_list|,
operator|new
name|CamelNsqMessageHandler
argument_list|()
argument_list|,
name|getEndpoint
argument_list|()
operator|.
name|getNsqConfig
argument_list|()
argument_list|)
expr_stmt|;
name|consumer
operator|.
name|setLookupPeriod
argument_list|(
name|configuration
operator|.
name|getLookupInterval
argument_list|()
argument_list|)
expr_stmt|;
name|consumer
operator|.
name|setExecutor
argument_list|(
name|getEndpoint
argument_list|()
operator|.
name|createExecutor
argument_list|()
argument_list|)
expr_stmt|;
name|consumer
operator|.
name|start
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
name|LOG
operator|.
name|debug
argument_list|(
literal|"Stopping NSQ Consumer"
argument_list|)
expr_stmt|;
if|if
condition|(
name|consumer
operator|!=
literal|null
condition|)
block|{
name|consumer
operator|.
name|shutdown
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|executor
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|getEndpoint
argument_list|()
operator|!=
literal|null
operator|&&
name|getEndpoint
argument_list|()
operator|.
name|getCamelContext
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|getEndpoint
argument_list|()
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getExecutorServiceManager
argument_list|()
operator|.
name|shutdownNow
argument_list|(
name|executor
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|executor
operator|.
name|shutdownNow
argument_list|()
expr_stmt|;
block|}
block|}
name|executor
operator|=
literal|null
expr_stmt|;
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
block|}
DECL|class|CamelNsqMessageHandler
class|class
name|CamelNsqMessageHandler
implements|implements
name|NSQMessageCallback
block|{
annotation|@
name|Override
DECL|method|message (NSQMessage msg)
specifier|public
name|void
name|message
parameter_list|(
name|NSQMessage
name|msg
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Received Message: {}"
argument_list|,
name|msg
argument_list|)
expr_stmt|;
name|Exchange
name|exchange
init|=
name|getEndpoint
argument_list|()
operator|.
name|createExchange
argument_list|(
name|ExchangePattern
operator|.
name|InOnly
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|msg
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|NsqConstants
operator|.
name|NSQ_MESSAGE_ID
argument_list|,
name|msg
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|NsqConstants
operator|.
name|NSQ_MESSAGE_ATTEMPTS
argument_list|,
name|msg
operator|.
name|getAttempts
argument_list|()
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|NsqConstants
operator|.
name|NSQ_MESSAGE_TIMESTAMP
argument_list|,
name|msg
operator|.
name|getTimestamp
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
if|if
condition|(
name|configuration
operator|.
name|getAutoFinish
argument_list|()
condition|)
block|{
name|msg
operator|.
name|finished
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|exchange
operator|.
name|addOnCompletion
argument_list|(
operator|new
name|NsqSynchronization
argument_list|(
name|msg
argument_list|,
operator|(
name|int
operator|)
name|configuration
operator|.
name|getRequeueInterval
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|processor
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
if|if
condition|(
operator|!
name|configuration
operator|.
name|getAutoFinish
argument_list|()
condition|)
block|{
name|msg
operator|.
name|requeue
argument_list|(
operator|(
name|int
operator|)
name|configuration
operator|.
name|getRequeueInterval
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|getExceptionHandler
argument_list|()
operator|.
name|handleException
argument_list|(
literal|"Error during processing"
argument_list|,
name|exchange
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

