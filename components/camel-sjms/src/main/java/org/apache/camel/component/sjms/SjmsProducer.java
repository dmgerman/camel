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
name|component
operator|.
name|sjms
operator|.
name|pool
operator|.
name|ObjectPool
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
name|DefaultAsyncProducer
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
comment|/**  * Base SjmsProducer class.  *  */
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
comment|/**      * The {@link MessageProducerResources} pool for all {@link SjmsProducer} classes.      *      */
DECL|class|MessageProducerPool
specifier|protected
class|class
name|MessageProducerPool
extends|extends
name|ObjectPool
argument_list|<
name|MessageProducerResources
argument_list|>
block|{
DECL|method|MessageProducerPool ()
specifier|public
name|MessageProducerPool
parameter_list|()
block|{
name|super
argument_list|(
name|getProducerCount
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createObject ()
specifier|protected
name|MessageProducerResources
name|createObject
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|doCreateProducerModel
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|destroyObject (MessageProducerResources model)
specifier|protected
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
comment|/**      * The {@link MessageProducer} resources for all {@link SjmsProducer} classes.       */
DECL|class|MessageProducerResources
specifier|protected
class|class
name|MessageProducerResources
block|{
DECL|field|session
specifier|private
specifier|final
name|Session
name|session
decl_stmt|;
DECL|field|messageProducer
specifier|private
specifier|final
name|MessageProducer
name|messageProducer
decl_stmt|;
comment|/**          * TODO Add Constructor Javadoc          *           * @param session          * @param messageProducer          */
DECL|method|MessageProducerResources (Session session, MessageProducer messageProducer)
specifier|public
name|MessageProducerResources
parameter_list|(
name|Session
name|session
parameter_list|,
name|MessageProducer
name|messageProducer
parameter_list|)
block|{
name|super
argument_list|()
expr_stmt|;
name|this
operator|.
name|session
operator|=
name|session
expr_stmt|;
name|this
operator|.
name|messageProducer
operator|=
name|messageProducer
expr_stmt|;
block|}
comment|/**          * Gets the Session value of session for this instance of          * MessageProducerResources.          *           * @return the session          */
DECL|method|getSession ()
specifier|public
name|Session
name|getSession
parameter_list|()
block|{
return|return
name|session
return|;
block|}
comment|/**          * Gets the QueueSender value of queueSender for this instance of          * MessageProducerResources.          *           * @return the queueSender          */
DECL|method|getMessageProducer ()
specifier|public
name|MessageProducer
name|getMessageProducer
parameter_list|()
block|{
return|return
name|messageProducer
return|;
block|}
block|}
DECL|field|producers
specifier|private
name|MessageProducerPool
name|producers
decl_stmt|;
DECL|field|executor
specifier|private
specifier|final
name|ExecutorService
name|executor
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
name|this
operator|.
name|executor
operator|=
name|endpoint
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
name|MessageProducerPool
argument_list|()
argument_list|)
expr_stmt|;
name|getProducers
argument_list|()
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
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
if|if
condition|(
name|getProducers
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|getProducers
argument_list|()
operator|.
name|drainPool
argument_list|()
expr_stmt|;
name|setProducers
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|doCreateProducerModel ()
specifier|public
specifier|abstract
name|MessageProducerResources
name|doCreateProducerModel
parameter_list|()
throws|throws
name|Exception
function_decl|;
DECL|method|sendMessage (Exchange exchange, final AsyncCallback callback)
specifier|public
specifier|abstract
name|void
name|sendMessage
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
specifier|final
name|AsyncCallback
name|callback
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
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|ObjectHelper
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
argument_list|)
expr_stmt|;
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
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Exception: "
operator|+
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
literal|" - SUCCESS"
argument_list|)
expr_stmt|;
block|}
return|return
name|isSynchronous
argument_list|()
return|;
block|}
DECL|method|getSjmsEndpoint ()
specifier|protected
name|SjmsEndpoint
name|getSjmsEndpoint
parameter_list|()
block|{
return|return
operator|(
name|SjmsEndpoint
operator|)
name|this
operator|.
name|getEndpoint
argument_list|()
return|;
block|}
DECL|method|getConnectionResource ()
specifier|protected
name|ConnectionResource
name|getConnectionResource
parameter_list|()
block|{
return|return
name|getSjmsEndpoint
argument_list|()
operator|.
name|getConnectionResource
argument_list|()
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
name|getSjmsEndpoint
argument_list|()
operator|.
name|getAcknowledgementMode
argument_list|()
operator|.
name|intValue
argument_list|()
return|;
block|}
comment|/**      * Gets the synchronous value for this instance of DestinationProducer.      *      * @return true if synchronous, otherwise false       */
DECL|method|isSynchronous ()
specifier|public
name|boolean
name|isSynchronous
parameter_list|()
block|{
return|return
name|getSjmsEndpoint
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
name|getSjmsEndpoint
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
name|getSjmsEndpoint
argument_list|()
operator|.
name|getDestinationName
argument_list|()
return|;
block|}
comment|/**      * Sets the producer pool for this instance of SjmsProducer.      *      * @param producers A MessageProducerPool      */
DECL|method|setProducers (MessageProducerPool producers)
specifier|public
name|void
name|setProducers
parameter_list|(
name|MessageProducerPool
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
comment|/**      * Gets the MessageProducerPool value of producers for this instance of SjmsProducer.      *      * @return the producers      */
DECL|method|getProducers ()
specifier|public
name|MessageProducerPool
name|getProducers
parameter_list|()
block|{
return|return
name|producers
return|;
block|}
comment|/**      * Test to verify if this endpoint is a JMS Topic or Queue.      *       * @return true if it is a Topic, otherwise it is a Queue      */
DECL|method|isTopic ()
specifier|public
name|boolean
name|isTopic
parameter_list|()
block|{
return|return
name|getSjmsEndpoint
argument_list|()
operator|.
name|isTopic
argument_list|()
return|;
block|}
comment|/**      * Test to determine if this endpoint should use a JMS Transaction.      *       * @return true if transacted, otherwise false      */
DECL|method|isEndpointTransacted ()
specifier|public
name|boolean
name|isEndpointTransacted
parameter_list|()
block|{
return|return
name|getSjmsEndpoint
argument_list|()
operator|.
name|isTransacted
argument_list|()
return|;
block|}
comment|/**      * Returns the named reply to value for this producer      *       * @return true if it is a Topic, otherwise it is a Queue      */
DECL|method|getNamedReplyTo ()
specifier|public
name|String
name|getNamedReplyTo
parameter_list|()
block|{
return|return
name|getSjmsEndpoint
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
name|getSjmsEndpoint
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
name|getSjmsEndpoint
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
name|getSjmsEndpoint
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
name|getSjmsEndpoint
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
name|getSjmsEndpoint
argument_list|()
operator|.
name|getResponseTimeOut
argument_list|()
return|;
block|}
block|}
end_class

end_unit

