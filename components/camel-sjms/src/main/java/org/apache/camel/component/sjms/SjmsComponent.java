begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.sjms
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|sjms
package|;
end_package

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
name|jms
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
name|CamelException
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
name|component
operator|.
name|sjms
operator|.
name|jms
operator|.
name|ConnectionFactoryResource
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
name|sjms
operator|.
name|jms
operator|.
name|ConnectionResource
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
name|sjms
operator|.
name|jms
operator|.
name|DefaultJmsKeyFormatStrategy
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
name|sjms
operator|.
name|jms
operator|.
name|DestinationCreationStrategy
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
name|sjms
operator|.
name|jms
operator|.
name|JmsKeyFormatStrategy
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
name|sjms
operator|.
name|jms
operator|.
name|MessageCreatedStrategy
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
name|sjms
operator|.
name|taskmanager
operator|.
name|TimedTaskManager
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
comment|/**  * The<a href="http://camel.apache.org/sjms">Simple JMS</a> component.  */
end_comment

begin_class
DECL|class|SjmsComponent
specifier|public
class|class
name|SjmsComponent
extends|extends
name|UriEndpointComponent
implements|implements
name|HeaderFilterStrategyAware
block|{
DECL|field|LOGGER
specifier|private
specifier|static
specifier|final
name|Logger
name|LOGGER
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|SjmsComponent
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|connectionFactory
specifier|private
name|ConnectionFactory
name|connectionFactory
decl_stmt|;
DECL|field|connectionResource
specifier|private
name|ConnectionResource
name|connectionResource
decl_stmt|;
DECL|field|closeConnectionResource
specifier|private
specifier|volatile
name|boolean
name|closeConnectionResource
decl_stmt|;
DECL|field|headerFilterStrategy
specifier|private
name|HeaderFilterStrategy
name|headerFilterStrategy
init|=
operator|new
name|SjmsHeaderFilterStrategy
argument_list|()
decl_stmt|;
DECL|field|jmsKeyFormatStrategy
specifier|private
name|JmsKeyFormatStrategy
name|jmsKeyFormatStrategy
init|=
operator|new
name|DefaultJmsKeyFormatStrategy
argument_list|()
decl_stmt|;
DECL|field|connectionCount
specifier|private
name|Integer
name|connectionCount
init|=
literal|1
decl_stmt|;
DECL|field|transactionCommitStrategy
specifier|private
name|TransactionCommitStrategy
name|transactionCommitStrategy
decl_stmt|;
DECL|field|timedTaskManager
specifier|private
name|TimedTaskManager
name|timedTaskManager
decl_stmt|;
DECL|field|destinationCreationStrategy
specifier|private
name|DestinationCreationStrategy
name|destinationCreationStrategy
decl_stmt|;
DECL|field|asyncStartStopExecutorService
specifier|private
name|ExecutorService
name|asyncStartStopExecutorService
decl_stmt|;
DECL|field|messageCreatedStrategy
specifier|private
name|MessageCreatedStrategy
name|messageCreatedStrategy
decl_stmt|;
DECL|method|SjmsComponent ()
specifier|public
name|SjmsComponent
parameter_list|()
block|{
name|super
argument_list|(
name|SjmsEndpoint
operator|.
name|class
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
name|validateMepAndReplyTo
argument_list|(
name|parameters
argument_list|)
expr_stmt|;
name|SjmsEndpoint
name|endpoint
init|=
operator|new
name|SjmsEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|,
name|remaining
argument_list|)
decl_stmt|;
name|setProperties
argument_list|(
name|endpoint
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
if|if
condition|(
name|endpoint
operator|.
name|isTransacted
argument_list|()
condition|)
block|{
name|endpoint
operator|.
name|setSynchronous
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|transactionCommitStrategy
operator|!=
literal|null
condition|)
block|{
name|endpoint
operator|.
name|setTransactionCommitStrategy
argument_list|(
name|transactionCommitStrategy
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|destinationCreationStrategy
operator|!=
literal|null
condition|)
block|{
name|endpoint
operator|.
name|setDestinationCreationStrategy
argument_list|(
name|destinationCreationStrategy
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|headerFilterStrategy
operator|!=
literal|null
condition|)
block|{
name|endpoint
operator|.
name|setHeaderFilterStrategy
argument_list|(
name|headerFilterStrategy
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|messageCreatedStrategy
operator|!=
literal|null
condition|)
block|{
name|endpoint
operator|.
name|setMessageCreatedStrategy
argument_list|(
name|messageCreatedStrategy
argument_list|)
expr_stmt|;
block|}
return|return
name|endpoint
return|;
block|}
comment|/**      * Helper method used to verify that when there is a namedReplyTo value we      * are using the InOut MEP. If namedReplyTo is defined and the MEP is InOnly      * the endpoint won't be expecting a reply so throw an error to alert the      * user.      *      * @param parameters {@link Endpoint} parameters      * @throws Exception throws a {@link CamelException} when MEP equals InOnly      *                   and namedReplyTo is defined.      */
DECL|method|validateMepAndReplyTo (Map<String, Object> parameters)
specifier|private
specifier|static
name|void
name|validateMepAndReplyTo
parameter_list|(
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
name|boolean
name|namedReplyToSet
init|=
name|parameters
operator|.
name|containsKey
argument_list|(
literal|"namedReplyTo"
argument_list|)
decl_stmt|;
name|boolean
name|mepSet
init|=
name|parameters
operator|.
name|containsKey
argument_list|(
literal|"exchangePattern"
argument_list|)
decl_stmt|;
if|if
condition|(
name|namedReplyToSet
operator|&&
name|mepSet
condition|)
block|{
if|if
condition|(
operator|!
name|parameters
operator|.
name|get
argument_list|(
literal|"exchangePattern"
argument_list|)
operator|.
name|equals
argument_list|(
name|ExchangePattern
operator|.
name|InOut
operator|.
name|toString
argument_list|()
argument_list|)
condition|)
block|{
name|String
name|namedReplyTo
init|=
operator|(
name|String
operator|)
name|parameters
operator|.
name|get
argument_list|(
literal|"namedReplyTo"
argument_list|)
decl_stmt|;
name|ExchangePattern
name|mep
init|=
name|ExchangePattern
operator|.
name|valueOf
argument_list|(
operator|(
name|String
operator|)
name|parameters
operator|.
name|get
argument_list|(
literal|"exchangePattern"
argument_list|)
argument_list|)
decl_stmt|;
throw|throw
operator|new
name|CamelException
argument_list|(
literal|"Setting parameter namedReplyTo="
operator|+
name|namedReplyTo
operator|+
literal|" requires a MEP of type InOut. Parameter exchangePattern is set to "
operator|+
name|mep
argument_list|)
throw|;
block|}
block|}
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
name|timedTaskManager
operator|=
operator|new
name|TimedTaskManager
argument_list|()
expr_stmt|;
name|LOGGER
operator|.
name|trace
argument_list|(
literal|"Verify ConnectionResource"
argument_list|)
expr_stmt|;
if|if
condition|(
name|getConnectionResource
argument_list|()
operator|==
literal|null
condition|)
block|{
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"No ConnectionResource provided. Initialize the ConnectionFactoryResource."
argument_list|)
expr_stmt|;
comment|// We always use a connection pool, even for a pool of 1
name|ConnectionFactoryResource
name|connections
init|=
operator|new
name|ConnectionFactoryResource
argument_list|(
name|getConnectionCount
argument_list|()
argument_list|,
name|getConnectionFactory
argument_list|()
argument_list|)
decl_stmt|;
name|connections
operator|.
name|fillPool
argument_list|()
expr_stmt|;
name|setConnectionResource
argument_list|(
name|connections
argument_list|)
expr_stmt|;
comment|// we created the resource so we should close it when stopping
name|closeConnectionResource
operator|=
literal|true
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|getConnectionResource
argument_list|()
operator|instanceof
name|ConnectionFactoryResource
condition|)
block|{
operator|(
operator|(
name|ConnectionFactoryResource
operator|)
name|getConnectionResource
argument_list|()
operator|)
operator|.
name|fillPool
argument_list|()
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
if|if
condition|(
name|timedTaskManager
operator|!=
literal|null
condition|)
block|{
name|timedTaskManager
operator|.
name|cancelTasks
argument_list|()
expr_stmt|;
name|timedTaskManager
operator|=
literal|null
expr_stmt|;
block|}
if|if
condition|(
name|closeConnectionResource
condition|)
block|{
if|if
condition|(
name|getConnectionResource
argument_list|()
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|getConnectionResource
argument_list|()
operator|instanceof
name|ConnectionFactoryResource
condition|)
block|{
operator|(
operator|(
name|ConnectionFactoryResource
operator|)
name|getConnectionResource
argument_list|()
operator|)
operator|.
name|drainPool
argument_list|()
expr_stmt|;
block|}
block|}
name|connectionResource
operator|=
literal|null
expr_stmt|;
block|}
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doShutdown ()
specifier|protected
name|void
name|doShutdown
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|asyncStartStopExecutorService
operator|!=
literal|null
condition|)
block|{
name|getCamelContext
argument_list|()
operator|.
name|getExecutorServiceManager
argument_list|()
operator|.
name|shutdownNow
argument_list|(
name|asyncStartStopExecutorService
argument_list|)
expr_stmt|;
name|asyncStartStopExecutorService
operator|=
literal|null
expr_stmt|;
block|}
name|super
operator|.
name|doShutdown
argument_list|()
expr_stmt|;
block|}
DECL|method|getAsyncStartStopExecutorService ()
specifier|protected
specifier|synchronized
name|ExecutorService
name|getAsyncStartStopExecutorService
parameter_list|()
block|{
if|if
condition|(
name|asyncStartStopExecutorService
operator|==
literal|null
condition|)
block|{
comment|// use a cached thread pool for async start tasks as they can run for a while, and we need a dedicated thread
comment|// for each task, and the thread pool will shrink when no more tasks running
name|asyncStartStopExecutorService
operator|=
name|getCamelContext
argument_list|()
operator|.
name|getExecutorServiceManager
argument_list|()
operator|.
name|newCachedThreadPool
argument_list|(
name|this
argument_list|,
literal|"AsyncStartStopListener"
argument_list|)
expr_stmt|;
block|}
return|return
name|asyncStartStopExecutorService
return|;
block|}
comment|/**      * A ConnectionFactory is required to enable the SjmsComponent.      * It can be set directly or set set as part of a ConnectionResource.      */
DECL|method|setConnectionFactory (ConnectionFactory connectionFactory)
specifier|public
name|void
name|setConnectionFactory
parameter_list|(
name|ConnectionFactory
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
DECL|method|getConnectionFactory ()
specifier|public
name|ConnectionFactory
name|getConnectionFactory
parameter_list|()
block|{
return|return
name|connectionFactory
return|;
block|}
annotation|@
name|Override
DECL|method|getHeaderFilterStrategy ()
specifier|public
name|HeaderFilterStrategy
name|getHeaderFilterStrategy
parameter_list|()
block|{
return|return
name|this
operator|.
name|headerFilterStrategy
return|;
block|}
comment|/**      * To use a custom HeaderFilterStrategy to filter header to and from Camel message.      */
annotation|@
name|Override
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
comment|/**      * A ConnectionResource is an interface that allows for customization and container control of the ConnectionFactory.      * See Plugable Connection Resource Management for further details.      */
DECL|method|setConnectionResource (ConnectionResource connectionResource)
specifier|public
name|void
name|setConnectionResource
parameter_list|(
name|ConnectionResource
name|connectionResource
parameter_list|)
block|{
name|this
operator|.
name|connectionResource
operator|=
name|connectionResource
expr_stmt|;
block|}
DECL|method|getConnectionResource ()
specifier|public
name|ConnectionResource
name|getConnectionResource
parameter_list|()
block|{
return|return
name|connectionResource
return|;
block|}
comment|/**      * The maximum number of connections available to endpoints started under this component      */
DECL|method|setConnectionCount (Integer maxConnections)
specifier|public
name|void
name|setConnectionCount
parameter_list|(
name|Integer
name|maxConnections
parameter_list|)
block|{
name|this
operator|.
name|connectionCount
operator|=
name|maxConnections
expr_stmt|;
block|}
DECL|method|getConnectionCount ()
specifier|public
name|Integer
name|getConnectionCount
parameter_list|()
block|{
return|return
name|connectionCount
return|;
block|}
comment|/**      * Pluggable strategy for encoding and decoding JMS keys so they can be compliant with the JMS specification.      * Camel provides one implementation out of the box: default.      * The default strategy will safely marshal dots and hyphens (. and -).      * Can be used for JMS brokers which do not care whether JMS header keys contain illegal characters.      * You can provide your own implementation of the org.apache.camel.component.jms.JmsKeyFormatStrategy      * and refer to it using the # notation.      */
DECL|method|setJmsKeyFormatStrategy (JmsKeyFormatStrategy jmsKeyFormatStrategy)
specifier|public
name|void
name|setJmsKeyFormatStrategy
parameter_list|(
name|JmsKeyFormatStrategy
name|jmsKeyFormatStrategy
parameter_list|)
block|{
name|this
operator|.
name|jmsKeyFormatStrategy
operator|=
name|jmsKeyFormatStrategy
expr_stmt|;
block|}
DECL|method|getJmsKeyFormatStrategy ()
specifier|public
name|JmsKeyFormatStrategy
name|getJmsKeyFormatStrategy
parameter_list|()
block|{
return|return
name|jmsKeyFormatStrategy
return|;
block|}
DECL|method|getTransactionCommitStrategy ()
specifier|public
name|TransactionCommitStrategy
name|getTransactionCommitStrategy
parameter_list|()
block|{
return|return
name|transactionCommitStrategy
return|;
block|}
comment|/**      * To configure which kind of commit strategy to use. Camel provides two implementations out      * of the box, default and batch.      */
DECL|method|setTransactionCommitStrategy (TransactionCommitStrategy commitStrategy)
specifier|public
name|void
name|setTransactionCommitStrategy
parameter_list|(
name|TransactionCommitStrategy
name|commitStrategy
parameter_list|)
block|{
name|this
operator|.
name|transactionCommitStrategy
operator|=
name|commitStrategy
expr_stmt|;
block|}
DECL|method|getDestinationCreationStrategy ()
specifier|public
name|DestinationCreationStrategy
name|getDestinationCreationStrategy
parameter_list|()
block|{
return|return
name|destinationCreationStrategy
return|;
block|}
comment|/**      * To use a custom DestinationCreationStrategy.      */
DECL|method|setDestinationCreationStrategy (DestinationCreationStrategy destinationCreationStrategy)
specifier|public
name|void
name|setDestinationCreationStrategy
parameter_list|(
name|DestinationCreationStrategy
name|destinationCreationStrategy
parameter_list|)
block|{
name|this
operator|.
name|destinationCreationStrategy
operator|=
name|destinationCreationStrategy
expr_stmt|;
block|}
DECL|method|getTimedTaskManager ()
specifier|public
name|TimedTaskManager
name|getTimedTaskManager
parameter_list|()
block|{
return|return
name|timedTaskManager
return|;
block|}
comment|/**      * To use a custom TimedTaskManager      */
DECL|method|setTimedTaskManager (TimedTaskManager timedTaskManager)
specifier|public
name|void
name|setTimedTaskManager
parameter_list|(
name|TimedTaskManager
name|timedTaskManager
parameter_list|)
block|{
name|this
operator|.
name|timedTaskManager
operator|=
name|timedTaskManager
expr_stmt|;
block|}
DECL|method|getMessageCreatedStrategy ()
specifier|public
name|MessageCreatedStrategy
name|getMessageCreatedStrategy
parameter_list|()
block|{
return|return
name|messageCreatedStrategy
return|;
block|}
comment|/**      * To use the given MessageCreatedStrategy which are invoked when Camel creates new instances of<tt>javax.jms.Message</tt>      * objects when Camel is sending a JMS message.      */
DECL|method|setMessageCreatedStrategy (MessageCreatedStrategy messageCreatedStrategy)
specifier|public
name|void
name|setMessageCreatedStrategy
parameter_list|(
name|MessageCreatedStrategy
name|messageCreatedStrategy
parameter_list|)
block|{
name|this
operator|.
name|messageCreatedStrategy
operator|=
name|messageCreatedStrategy
expr_stmt|;
block|}
block|}
end_class

end_unit

