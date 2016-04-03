begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|util
operator|.
name|Properties
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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeoutException
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
name|impl
operator|.
name|DefaultConsumer
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
name|ConnectionFactory
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
name|Message
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
name|MessageHandler
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
name|Subscription
import|;
end_import

begin_class
DECL|class|NatsConsumer
specifier|public
class|class
name|NatsConsumer
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
name|NatsConsumer
operator|.
name|class
argument_list|)
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
DECL|field|connection
specifier|private
name|Connection
name|connection
decl_stmt|;
DECL|field|sid
specifier|private
name|Subscription
name|sid
decl_stmt|;
DECL|method|NatsConsumer (NatsEndpoint endpoint, Processor processor)
specifier|public
name|NatsConsumer
parameter_list|(
name|NatsEndpoint
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
block|}
annotation|@
name|Override
DECL|method|getEndpoint ()
specifier|public
name|NatsEndpoint
name|getEndpoint
parameter_list|()
block|{
return|return
operator|(
name|NatsEndpoint
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
literal|"Starting Nats Consumer"
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
literal|"Getting Nats Connection"
argument_list|)
expr_stmt|;
name|connection
operator|=
name|getConnection
argument_list|()
expr_stmt|;
name|executor
operator|.
name|submit
argument_list|(
operator|new
name|NatsConsumingTask
argument_list|(
name|connection
argument_list|,
name|getEndpoint
argument_list|()
operator|.
name|getNatsConfiguration
argument_list|()
argument_list|)
argument_list|)
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
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Flushing Messages before stopping"
argument_list|)
expr_stmt|;
name|connection
operator|.
name|flush
argument_list|()
expr_stmt|;
try|try
block|{
name|sid
operator|.
name|unsubscribe
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|getExceptionHandler
argument_list|()
operator|.
name|handleException
argument_list|(
literal|"Error during unsubscribing"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
name|LOG
operator|.
name|debug
argument_list|(
literal|"Stopping Nats Consumer"
argument_list|)
expr_stmt|;
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
name|LOG
operator|.
name|debug
argument_list|(
literal|"Closing Nats Connection"
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|connection
operator|.
name|isClosed
argument_list|()
condition|)
block|{
name|connection
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|getConnection ()
specifier|private
name|Connection
name|getConnection
parameter_list|()
throws|throws
name|IOException
throws|,
name|InterruptedException
throws|,
name|TimeoutException
block|{
name|Properties
name|prop
init|=
name|getEndpoint
argument_list|()
operator|.
name|getNatsConfiguration
argument_list|()
operator|.
name|createProperties
argument_list|()
decl_stmt|;
name|ConnectionFactory
name|factory
init|=
operator|new
name|ConnectionFactory
argument_list|(
name|prop
argument_list|)
decl_stmt|;
name|connection
operator|=
name|factory
operator|.
name|createConnection
argument_list|()
expr_stmt|;
return|return
name|connection
return|;
block|}
DECL|class|NatsConsumingTask
class|class
name|NatsConsumingTask
implements|implements
name|Runnable
block|{
DECL|field|connection
specifier|private
specifier|final
name|Connection
name|connection
decl_stmt|;
DECL|field|configuration
specifier|private
specifier|final
name|NatsConfiguration
name|configuration
decl_stmt|;
DECL|method|NatsConsumingTask (Connection connection, NatsConfiguration configuration)
specifier|public
name|NatsConsumingTask
parameter_list|(
name|Connection
name|connection
parameter_list|,
name|NatsConfiguration
name|configuration
parameter_list|)
block|{
name|this
operator|.
name|connection
operator|=
name|connection
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
DECL|method|run ()
specifier|public
name|void
name|run
parameter_list|()
block|{
try|try
block|{
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|configuration
operator|.
name|getQueueName
argument_list|()
argument_list|)
condition|)
block|{
name|sid
operator|=
name|connection
operator|.
name|subscribe
argument_list|(
name|getEndpoint
argument_list|()
operator|.
name|getNatsConfiguration
argument_list|()
operator|.
name|getTopic
argument_list|()
argument_list|,
name|getEndpoint
argument_list|()
operator|.
name|getNatsConfiguration
argument_list|()
operator|.
name|getQueueName
argument_list|()
argument_list|,
operator|new
name|MessageHandler
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onMessage
parameter_list|(
name|Message
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
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|msg
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|NatsConstants
operator|.
name|NATS_MESSAGE_TIMESTAMP
argument_list|,
name|System
operator|.
name|currentTimeMillis
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
name|NatsConstants
operator|.
name|NATS_SUBSCRIPTION_ID
argument_list|,
name|sid
argument_list|)
expr_stmt|;
try|try
block|{
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
argument_list|)
expr_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|getEndpoint
argument_list|()
operator|.
name|getNatsConfiguration
argument_list|()
operator|.
name|getMaxMessages
argument_list|()
argument_list|)
condition|)
block|{
name|sid
operator|.
name|autoUnsubscribe
argument_list|(
name|Integer
operator|.
name|parseInt
argument_list|(
name|getEndpoint
argument_list|()
operator|.
name|getNatsConfiguration
argument_list|()
operator|.
name|getMaxMessages
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|sid
operator|=
name|connection
operator|.
name|subscribe
argument_list|(
name|getEndpoint
argument_list|()
operator|.
name|getNatsConfiguration
argument_list|()
operator|.
name|getTopic
argument_list|()
argument_list|,
operator|new
name|MessageHandler
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onMessage
parameter_list|(
name|Message
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
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|msg
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|NatsConstants
operator|.
name|NATS_MESSAGE_TIMESTAMP
argument_list|,
name|System
operator|.
name|currentTimeMillis
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
name|NatsConstants
operator|.
name|NATS_SUBSCRIPTION_ID
argument_list|,
name|sid
argument_list|)
expr_stmt|;
try|try
block|{
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
argument_list|)
expr_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|getEndpoint
argument_list|()
operator|.
name|getNatsConfiguration
argument_list|()
operator|.
name|getMaxMessages
argument_list|()
argument_list|)
condition|)
block|{
name|sid
operator|.
name|autoUnsubscribe
argument_list|(
name|Integer
operator|.
name|parseInt
argument_list|(
name|getEndpoint
argument_list|()
operator|.
name|getNatsConfiguration
argument_list|()
operator|.
name|getMaxMessages
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
name|getExceptionHandler
argument_list|()
operator|.
name|handleException
argument_list|(
literal|"Error during processing"
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

