begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Future
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|Connection
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|MessageProducer
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|Session
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
name|AsyncCallback
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
name|tx
operator|.
name|SessionTransactionSynchronization
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
name|DefaultAsyncProducer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|pool
operator|.
name|BasePoolableObjectFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|pool
operator|.
name|impl
operator|.
name|GenericObjectPool
import|;
end_import

begin_comment
comment|/**  * Base SjmsProducer class.  */
end_comment

begin_class
DECL|class|SjmsProducer
specifier|public
specifier|abstract
class|class
name|SjmsProducer
extends|extends
name|DefaultAsyncProducer
block|{
comment|/**      * The {@link MessageProducerResources} pool for all {@link SjmsProducer}      * classes.      */
DECL|class|MessageProducerResourcesFactory
specifier|protected
class|class
name|MessageProducerResourcesFactory
extends|extends
name|BasePoolableObjectFactory
argument_list|<
name|MessageProducerResources
argument_list|>
block|{
annotation|@
name|Override
DECL|method|makeObject ()
specifier|public
name|MessageProducerResources
name|makeObject
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|doCreateProducerModel
argument_list|(
name|createSession
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|destroyObject (MessageProducerResources model)
specifier|public
name|void
name|destroyObject
parameter_list|(
name|MessageProducerResources
name|model
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|model
operator|.
name|getMessageProducer
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|model
operator|.
name|getMessageProducer
argument_list|()
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|model
operator|.
name|getSession
argument_list|()
operator|!=
literal|null
condition|)
block|{
try|try
block|{
if|if
condition|(
name|model
operator|.
name|getSession
argument_list|()
operator|.
name|getTransacted
argument_list|()
condition|)
block|{
try|try
block|{
name|model
operator|.
name|getSession
argument_list|()
operator|.
name|rollback
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// Do nothing. Just make sure we are cleaned up
block|}
block|}
name|model
operator|.
name|getSession
argument_list|()
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// TODO why is the session closed already?
block|}
block|}
block|}
block|}
DECL|field|producers
specifier|private
name|GenericObjectPool
argument_list|<
name|MessageProducerResources
argument_list|>
name|producers
decl_stmt|;
DECL|field|executor
specifier|private
name|ExecutorService
name|executor
decl_stmt|;
DECL|field|asyncStart
specifier|private
name|Future
argument_list|<
name|?
argument_list|>
name|asyncStart
decl_stmt|;
DECL|method|SjmsProducer (Endpoint endpoint)
specifier|public
name|SjmsProducer
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
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
name|this
operator|.
name|executor
operator|=
name|getEndpoint
argument_list|()
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getExecutorServiceManager
argument_list|()
operator|.
name|newDefaultThreadPool
argument_list|(
name|this
argument_list|,
literal|"SjmsProducer"
argument_list|)
expr_stmt|;
if|if
condition|(
name|getProducers
argument_list|()
operator|==
literal|null
condition|)
block|{
name|setProducers
argument_list|(
operator|new
name|GenericObjectPool
argument_list|<>
argument_list|(
operator|new
name|MessageProducerResourcesFactory
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|getProducers
argument_list|()
operator|.
name|setMaxActive
argument_list|(
name|getProducerCount
argument_list|()
argument_list|)
expr_stmt|;
name|getProducers
argument_list|()
operator|.
name|setMaxIdle
argument_list|(
name|getProducerCount
argument_list|()
argument_list|)
expr_stmt|;
name|getProducers
argument_list|()
operator|.
name|setLifo
argument_list|(
literal|false
argument_list|)
expr_stmt|;
if|if
condition|(
name|getEndpoint
argument_list|()
operator|.
name|isPrefillPool
argument_list|()
condition|)
block|{
if|if
condition|(
name|getEndpoint
argument_list|()
operator|.
name|isAsyncStartListener
argument_list|()
condition|)
block|{
name|asyncStart
operator|=
name|getEndpoint
argument_list|()
operator|.
name|getComponent
argument_list|()
operator|.
name|getAsyncStartStopExecutorService
argument_list|()
operator|.
name|submit
argument_list|(
operator|new
name|Runnable
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|run
parameter_list|()
block|{
try|try
block|{
name|fillProducersPool
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Error filling producer pool for destination: "
operator|+
name|getDestinationName
argument_list|()
operator|+
literal|". This exception will be ignored."
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"AsyncStartListenerTask["
operator|+
name|getDestinationName
argument_list|()
operator|+
literal|"]"
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|fillProducersPool
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
DECL|method|fillProducersPool ()
specifier|private
name|void
name|fillProducersPool
parameter_list|()
throws|throws
name|Exception
block|{
while|while
condition|(
name|producers
operator|.
name|getNumIdle
argument_list|()
operator|<
name|producers
operator|.
name|getMaxIdle
argument_list|()
condition|)
block|{
name|producers
operator|.
name|addObject
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
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
if|if
condition|(
name|asyncStart
operator|!=
literal|null
operator|&&
operator|!
name|asyncStart
operator|.
name|isDone
argument_list|()
condition|)
block|{
name|asyncStart
operator|.
name|cancel
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|getProducers
argument_list|()
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|getEndpoint
argument_list|()
operator|.
name|isAsyncStopListener
argument_list|()
condition|)
block|{
name|getEndpoint
argument_list|()
operator|.
name|getComponent
argument_list|()
operator|.
name|getAsyncStartStopExecutorService
argument_list|()
operator|.
name|submit
argument_list|(
operator|new
name|Runnable
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|run
parameter_list|()
block|{
try|try
block|{
name|getProducers
argument_list|()
operator|.
name|close
argument_list|()
expr_stmt|;
name|setProducers
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Error closing producers on destination: "
operator|+
name|getDestinationName
argument_list|()
operator|+
literal|". This exception will be ignored."
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"AsyncStopListenerTask["
operator|+
name|getDestinationName
argument_list|()
operator|+
literal|"]"
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|getProducers
argument_list|()
operator|.
name|close
argument_list|()
expr_stmt|;
name|setProducers
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|this
operator|.
name|executor
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
name|shutdownGraceful
argument_list|(
name|this
operator|.
name|executor
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|getEndpoint ()
specifier|public
name|SjmsEndpoint
name|getEndpoint
parameter_list|()
block|{
return|return
operator|(
name|SjmsEndpoint
operator|)
name|super
operator|.
name|getEndpoint
argument_list|()
return|;
block|}
DECL|method|doCreateProducerModel (Session session)
specifier|protected
name|MessageProducerResources
name|doCreateProducerModel
parameter_list|(
name|Session
name|session
parameter_list|)
throws|throws
name|Exception
block|{
name|MessageProducerResources
name|answer
decl_stmt|;
try|try
block|{
name|MessageProducer
name|messageProducer
init|=
name|getEndpoint
argument_list|()
operator|.
name|getJmsObjectFactory
argument_list|()
operator|.
name|createMessageProducer
argument_list|(
name|session
argument_list|,
name|getEndpoint
argument_list|()
argument_list|)
decl_stmt|;
name|answer
operator|=
operator|new
name|MessageProducerResources
argument_list|(
name|session
argument_list|,
name|messageProducer
argument_list|,
name|getCommitStrategy
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Unable to create the MessageProducer"
argument_list|,
name|e
argument_list|)
expr_stmt|;
throw|throw
name|e
throw|;
block|}
return|return
name|answer
return|;
block|}
DECL|method|createSession ()
specifier|protected
name|Session
name|createSession
parameter_list|()
throws|throws
name|Exception
block|{
name|ConnectionResource
name|connectionResource
init|=
name|getOrCreateConnectionResource
argument_list|()
decl_stmt|;
name|Connection
name|conn
init|=
name|connectionResource
operator|.
name|borrowConnection
argument_list|()
decl_stmt|;
try|try
block|{
return|return
name|conn
operator|.
name|createSession
argument_list|(
name|isEndpointTransacted
argument_list|()
argument_list|,
name|getAcknowledgeMode
argument_list|()
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Unable to create the Session"
argument_list|,
name|e
argument_list|)
expr_stmt|;
throw|throw
name|e
throw|;
block|}
finally|finally
block|{
name|connectionResource
operator|.
name|returnConnection
argument_list|(
name|conn
argument_list|)
expr_stmt|;
block|}
block|}
DECL|interface|ReleaseProducerCallback
specifier|protected
interface|interface
name|ReleaseProducerCallback
block|{
DECL|method|release (MessageProducerResources producer)
name|void
name|release
parameter_list|(
name|MessageProducerResources
name|producer
parameter_list|)
throws|throws
name|Exception
function_decl|;
block|}
DECL|class|CloseProducerCallback
specifier|protected
class|class
name|CloseProducerCallback
implements|implements
name|ReleaseProducerCallback
block|{
annotation|@
name|Override
DECL|method|release (MessageProducerResources producer)
specifier|public
name|void
name|release
parameter_list|(
name|MessageProducerResources
name|producer
parameter_list|)
throws|throws
name|Exception
block|{
name|producer
operator|.
name|getMessageProducer
argument_list|()
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
DECL|class|ReturnProducerCallback
specifier|protected
class|class
name|ReturnProducerCallback
implements|implements
name|ReleaseProducerCallback
block|{
annotation|@
name|Override
DECL|method|release (MessageProducerResources producer)
specifier|public
name|void
name|release
parameter_list|(
name|MessageProducerResources
name|producer
parameter_list|)
throws|throws
name|Exception
block|{
name|getProducers
argument_list|()
operator|.
name|returnObject
argument_list|(
name|producer
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|sendMessage (Exchange exchange, AsyncCallback callback, MessageProducerResources producer, ReleaseProducerCallback releaseProducerCallback)
specifier|public
specifier|abstract
name|void
name|sendMessage
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|AsyncCallback
name|callback
parameter_list|,
name|MessageProducerResources
name|producer
parameter_list|,
name|ReleaseProducerCallback
name|releaseProducerCallback
parameter_list|)
throws|throws
name|Exception
function_decl|;
annotation|@
name|Override
DECL|method|process (final Exchange exchange, final AsyncCallback callback)
specifier|public
name|boolean
name|process
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|,
specifier|final
name|AsyncCallback
name|callback
parameter_list|)
block|{
if|if
condition|(
name|log
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Processing Exchange.id:{}"
argument_list|,
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|)
expr_stmt|;
block|}
try|try
block|{
specifier|final
name|MessageProducerResources
name|producer
decl_stmt|;
specifier|final
name|ReleaseProducerCallback
name|releaseProducerCallback
decl_stmt|;
if|if
condition|(
name|isEndpointTransacted
argument_list|()
operator|&&
name|isSharedJMSSession
argument_list|()
condition|)
block|{
name|Session
name|session
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|SjmsConstants
operator|.
name|JMS_SESSION
argument_list|,
name|Session
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|session
operator|!=
literal|null
operator|&&
name|session
operator|.
name|getTransacted
argument_list|()
condition|)
block|{
comment|// Join existing transacted session - Synchronization must have been added
comment|// by the session initiator
name|producer
operator|=
name|doCreateProducerModel
argument_list|(
name|session
argument_list|)
expr_stmt|;
name|releaseProducerCallback
operator|=
operator|new
name|CloseProducerCallback
argument_list|()
expr_stmt|;
block|}
else|else
block|{
comment|// Propagate JMS session and register Synchronization as an initiator
name|producer
operator|=
name|getProducers
argument_list|()
operator|.
name|borrowObject
argument_list|()
expr_stmt|;
name|releaseProducerCallback
operator|=
operator|new
name|ReturnProducerCallback
argument_list|()
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|SjmsConstants
operator|.
name|JMS_SESSION
argument_list|,
name|producer
operator|.
name|getSession
argument_list|()
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getUnitOfWork
argument_list|()
operator|.
name|addSynchronization
argument_list|(
operator|new
name|SessionTransactionSynchronization
argument_list|(
name|producer
operator|.
name|getSession
argument_list|()
argument_list|,
name|producer
operator|.
name|getCommitStrategy
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|producer
operator|=
name|getProducers
argument_list|()
operator|.
name|borrowObject
argument_list|()
expr_stmt|;
name|releaseProducerCallback
operator|=
operator|new
name|ReturnProducerCallback
argument_list|()
expr_stmt|;
if|if
condition|(
name|isEndpointTransacted
argument_list|()
condition|)
block|{
name|exchange
operator|.
name|getUnitOfWork
argument_list|()
operator|.
name|addSynchronization
argument_list|(
operator|new
name|SessionTransactionSynchronization
argument_list|(
name|producer
operator|.
name|getSession
argument_list|()
argument_list|,
name|producer
operator|.
name|getCommitStrategy
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|producer
operator|==
literal|null
condition|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
operator|new
name|Exception
argument_list|(
literal|"Unable to send message: connection not available"
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
operator|!
name|isSynchronous
argument_list|()
condition|)
block|{
if|if
condition|(
name|log
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"  Sending message asynchronously: {}"
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|getExecutor
argument_list|()
operator|.
name|execute
argument_list|(
operator|new
name|Runnable
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|run
parameter_list|()
block|{
try|try
block|{
name|sendMessage
argument_list|(
name|exchange
argument_list|,
name|callback
argument_list|,
name|producer
argument_list|,
name|releaseProducerCallback
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|RuntimeCamelException
operator|.
name|wrapRuntimeCamelException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|log
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"  Sending message synchronously: {}"
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|sendMessage
argument_list|(
name|exchange
argument_list|,
name|callback
argument_list|,
name|producer
argument_list|,
name|releaseProducerCallback
argument_list|)
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
if|if
condition|(
name|log
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Processing Exchange.id:{}"
argument_list|,
name|exchange
operator|.
name|getExchangeId
argument_list|()
operator|+
literal|" - FAILED"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|log
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Exception: {}"
argument_list|,
name|e
operator|.
name|getLocalizedMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
name|exchange
operator|.
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
name|log
operator|.
name|debug
argument_list|(
literal|"Processing Exchange.id:{}"
argument_list|,
name|exchange
operator|.
name|getExchangeId
argument_list|()
operator|+
literal|" - SUCCESS"
argument_list|)
expr_stmt|;
return|return
name|isSynchronous
argument_list|()
return|;
block|}
comment|/**      * @deprecated use {@link #getOrCreateConnectionResource()}      */
annotation|@
name|Deprecated
DECL|method|getConnectionResource ()
specifier|protected
name|ConnectionResource
name|getConnectionResource
parameter_list|()
block|{
return|return
name|getEndpoint
argument_list|()
operator|.
name|getConnectionResource
argument_list|()
return|;
block|}
DECL|method|getOrCreateConnectionResource ()
specifier|protected
name|ConnectionResource
name|getOrCreateConnectionResource
parameter_list|()
block|{
name|ConnectionResource
name|answer
init|=
name|getEndpoint
argument_list|()
operator|.
name|getConnectionResource
argument_list|()
decl_stmt|;
if|if
condition|(
name|answer
operator|==
literal|null
condition|)
block|{
name|answer
operator|=
name|getEndpoint
argument_list|()
operator|.
name|createConnectionResource
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
comment|/**      * Gets the acknowledgment mode for this instance of DestinationProducer.      *      * @return int      */
DECL|method|getAcknowledgeMode ()
specifier|public
name|int
name|getAcknowledgeMode
parameter_list|()
block|{
return|return
name|getEndpoint
argument_list|()
operator|.
name|getAcknowledgementMode
argument_list|()
operator|.
name|intValue
argument_list|()
return|;
block|}
comment|/**      * Gets the synchronous value for this instance of DestinationProducer.      *      * @return true if synchronous, otherwise false      */
DECL|method|isSynchronous ()
specifier|public
name|boolean
name|isSynchronous
parameter_list|()
block|{
return|return
name|getEndpoint
argument_list|()
operator|.
name|isSynchronous
argument_list|()
return|;
block|}
comment|/**      * Gets the replyTo for this instance of DestinationProducer.      *      * @return String      */
DECL|method|getReplyTo ()
specifier|public
name|String
name|getReplyTo
parameter_list|()
block|{
return|return
name|getEndpoint
argument_list|()
operator|.
name|getNamedReplyTo
argument_list|()
return|;
block|}
comment|/**      * Gets the destinationName for this instance of DestinationProducer.      *      * @return String      */
DECL|method|getDestinationName ()
specifier|public
name|String
name|getDestinationName
parameter_list|()
block|{
return|return
name|getEndpoint
argument_list|()
operator|.
name|getDestinationName
argument_list|()
return|;
block|}
comment|/**      * Sets the producer pool for this instance of SjmsProducer.      *      * @param producers A MessageProducerPool      */
DECL|method|setProducers (GenericObjectPool<MessageProducerResources> producers)
specifier|public
name|void
name|setProducers
parameter_list|(
name|GenericObjectPool
argument_list|<
name|MessageProducerResources
argument_list|>
name|producers
parameter_list|)
block|{
name|this
operator|.
name|producers
operator|=
name|producers
expr_stmt|;
block|}
comment|/**      * Gets the MessageProducerPool value of producers for this instance of      * SjmsProducer.      *      * @return the producers      */
DECL|method|getProducers ()
specifier|public
name|GenericObjectPool
argument_list|<
name|MessageProducerResources
argument_list|>
name|getProducers
parameter_list|()
block|{
return|return
name|producers
return|;
block|}
comment|/**      * Test to verify if this endpoint is a JMS Topic or Queue.      *      * @return true if it is a Topic, otherwise it is a Queue      */
DECL|method|isTopic ()
specifier|public
name|boolean
name|isTopic
parameter_list|()
block|{
return|return
name|getEndpoint
argument_list|()
operator|.
name|isTopic
argument_list|()
return|;
block|}
comment|/**      * Test to determine if this endpoint should use a JMS Transaction.      *      * @return true if transacted, otherwise false      */
DECL|method|isEndpointTransacted ()
specifier|public
name|boolean
name|isEndpointTransacted
parameter_list|()
block|{
return|return
name|getEndpoint
argument_list|()
operator|.
name|isTransacted
argument_list|()
return|;
block|}
comment|/**      * Test to determine if this endpoint should share a JMS Session with other SJMS endpoints.      *      * @return true if shared, otherwise false      */
DECL|method|isSharedJMSSession ()
specifier|public
name|boolean
name|isSharedJMSSession
parameter_list|()
block|{
return|return
name|getEndpoint
argument_list|()
operator|.
name|isSharedJMSSession
argument_list|()
return|;
block|}
comment|/**      * Returns the named reply to value for this producer      *      * @return true if it is a Topic, otherwise it is a Queue      */
DECL|method|getNamedReplyTo ()
specifier|public
name|String
name|getNamedReplyTo
parameter_list|()
block|{
return|return
name|getEndpoint
argument_list|()
operator|.
name|getNamedReplyTo
argument_list|()
return|;
block|}
comment|/**      * Gets the producerCount for this instance of SjmsProducer.      *      * @return int      */
DECL|method|getProducerCount ()
specifier|public
name|int
name|getProducerCount
parameter_list|()
block|{
return|return
name|getEndpoint
argument_list|()
operator|.
name|getProducerCount
argument_list|()
return|;
block|}
comment|/**      * Gets consumerCount for this instance of SjmsProducer.      *      * @return int      */
DECL|method|getConsumerCount ()
specifier|public
name|int
name|getConsumerCount
parameter_list|()
block|{
return|return
name|getEndpoint
argument_list|()
operator|.
name|getConsumerCount
argument_list|()
return|;
block|}
comment|/**      * Gets the executor for this instance of SjmsProducer.      *      * @return ExecutorService      */
DECL|method|getExecutor ()
specifier|public
name|ExecutorService
name|getExecutor
parameter_list|()
block|{
return|return
name|executor
return|;
block|}
comment|/**      * Gets the ttl for this instance of SjmsProducer.      *      * @return long      */
DECL|method|getTtl ()
specifier|public
name|long
name|getTtl
parameter_list|()
block|{
return|return
name|getEndpoint
argument_list|()
operator|.
name|getTtl
argument_list|()
return|;
block|}
comment|/**      * Gets the boolean value of persistent for this instance of SjmsProducer.      *      * @return true if persistent, otherwise false      */
DECL|method|isPersistent ()
specifier|public
name|boolean
name|isPersistent
parameter_list|()
block|{
return|return
name|getEndpoint
argument_list|()
operator|.
name|isPersistent
argument_list|()
return|;
block|}
comment|/**      * Gets responseTimeOut for this instance of SjmsProducer.      *      * @return long      */
DECL|method|getResponseTimeOut ()
specifier|public
name|long
name|getResponseTimeOut
parameter_list|()
block|{
return|return
name|getEndpoint
argument_list|()
operator|.
name|getResponseTimeOut
argument_list|()
return|;
block|}
comment|/**      * Gets commitStrategy for this instance of SjmsProducer.      *      * @return TransactionCommitStrategy      */
DECL|method|getCommitStrategy ()
specifier|protected
name|TransactionCommitStrategy
name|getCommitStrategy
parameter_list|()
block|{
if|if
condition|(
name|isEndpointTransacted
argument_list|()
condition|)
block|{
return|return
name|getEndpoint
argument_list|()
operator|.
name|getTransactionCommitStrategy
argument_list|()
return|;
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

