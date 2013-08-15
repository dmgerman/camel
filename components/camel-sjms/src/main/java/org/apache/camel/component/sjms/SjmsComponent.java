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
name|KeyFormatStrategy
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
DECL|field|headerFilterStrategy
specifier|private
name|HeaderFilterStrategy
name|headerFilterStrategy
init|=
operator|new
name|SjmsHeaderFilterStrategy
argument_list|()
decl_stmt|;
DECL|field|keyFormatStrategy
specifier|private
name|KeyFormatStrategy
name|keyFormatStrategy
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
name|uri
operator|=
name|normalizeUri
argument_list|(
name|uri
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
return|return
name|endpoint
return|;
block|}
comment|/**      * Helper method used to detect the type of endpoint and add the "queue"      * protocol if it is a default endpoint URI.      *       * @param uri The value passed into our call to create an endpoint      * @return String      * @throws Exception      */
DECL|method|normalizeUri (String uri)
specifier|private
specifier|static
name|String
name|normalizeUri
parameter_list|(
name|String
name|uri
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|tempUri
init|=
name|uri
decl_stmt|;
name|String
name|endpointName
init|=
name|tempUri
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|tempUri
operator|.
name|indexOf
argument_list|(
literal|":"
argument_list|)
argument_list|)
decl_stmt|;
name|tempUri
operator|=
name|tempUri
operator|.
name|substring
argument_list|(
name|endpointName
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|tempUri
operator|.
name|startsWith
argument_list|(
literal|"://"
argument_list|)
condition|)
block|{
name|tempUri
operator|=
name|tempUri
operator|.
name|substring
argument_list|(
literal|3
argument_list|)
expr_stmt|;
block|}
name|String
name|protocol
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|tempUri
operator|.
name|indexOf
argument_list|(
literal|":"
argument_list|)
operator|>
literal|0
condition|)
block|{
name|protocol
operator|=
name|tempUri
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|tempUri
operator|.
name|indexOf
argument_list|(
literal|":"
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|protocol
argument_list|)
condition|)
block|{
name|protocol
operator|=
literal|"queue"
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|protocol
operator|!=
literal|null
operator|&&
operator|(
name|protocol
operator|.
name|equals
argument_list|(
literal|"queue"
argument_list|)
operator|||
name|protocol
operator|.
name|equals
argument_list|(
literal|"topic"
argument_list|)
operator|)
condition|)
block|{
name|tempUri
operator|=
name|tempUri
operator|.
name|substring
argument_list|(
name|protocol
operator|.
name|length
argument_list|()
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|Exception
argument_list|(
literal|"Unsupported Protocol: "
operator|+
name|protocol
argument_list|)
throw|;
block|}
name|String
name|path
init|=
name|tempUri
decl_stmt|;
name|uri
operator|=
name|endpointName
operator|+
literal|"://"
operator|+
name|protocol
operator|+
literal|":"
operator|+
name|path
expr_stmt|;
return|return
name|uri
return|;
block|}
comment|/**      * Helper method used to verify that when there is a namedReplyTo value we      * are using the InOut MEP. If namedReplyTo is defined and the MEP is InOnly      * the endpoint won't be expecting a reply so throw an error to alert the      * user.      *       * @param parameters {@link Endpoint} parameters      * @throws Exception throws a {@link CamelException} when MEP equals InOnly      *             and namedReplyTo is defined.      */
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
name|timedTaskManager
operator|.
name|cancelTasks
argument_list|()
expr_stmt|;
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
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
block|}
comment|/**      * Sets the ConnectionFactory value of connectionFactory for this instance      * of SjmsComponent.      */
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
comment|/**      * Gets the ConnectionFactory value of connectionFactory for this instance      * of SjmsComponent.      *       * @return the connectionFactory      */
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
DECL|method|setKeyFormatStrategy (KeyFormatStrategy keyFormatStrategy)
specifier|public
name|void
name|setKeyFormatStrategy
parameter_list|(
name|KeyFormatStrategy
name|keyFormatStrategy
parameter_list|)
block|{
name|this
operator|.
name|keyFormatStrategy
operator|=
name|keyFormatStrategy
expr_stmt|;
block|}
DECL|method|getKeyFormatStrategy ()
specifier|public
name|KeyFormatStrategy
name|getKeyFormatStrategy
parameter_list|()
block|{
return|return
name|keyFormatStrategy
return|;
block|}
comment|/**      * Gets the TransactionCommitStrategy value of transactionCommitStrategy for this      * instance of SjmsComponent.      *       * @return the transactionCommitStrategy      */
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
comment|/**      * Sets the TransactionCommitStrategy value of transactionCommitStrategy for this      * instance of SjmsComponent.      */
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
block|}
end_class

end_unit

