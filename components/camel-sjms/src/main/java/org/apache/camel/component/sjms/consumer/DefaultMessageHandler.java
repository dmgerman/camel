begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.sjms.consumer
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
operator|.
name|consumer
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
name|Message
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|MessageListener
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
name|AsyncProcessor
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
name|TransactionCommitStrategy
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
name|JmsMessageExchangeHelper
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
name|spi
operator|.
name|Synchronization
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
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|ObjectHelper
operator|.
name|wrapRuntimeCamelException
import|;
end_import

begin_comment
comment|/**  * TODO Add Class documentation for DefaultMessageHandler  */
end_comment

begin_class
DECL|class|DefaultMessageHandler
specifier|public
specifier|abstract
class|class
name|DefaultMessageHandler
implements|implements
name|MessageListener
block|{
DECL|field|log
specifier|protected
specifier|final
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
DECL|field|executor
specifier|private
specifier|final
name|ExecutorService
name|executor
decl_stmt|;
DECL|field|endpoint
specifier|private
name|Endpoint
name|endpoint
decl_stmt|;
DECL|field|processor
specifier|private
name|AsyncProcessor
name|processor
decl_stmt|;
DECL|field|session
specifier|private
name|Session
name|session
decl_stmt|;
DECL|field|transacted
specifier|private
name|boolean
name|transacted
decl_stmt|;
DECL|field|synchronous
specifier|private
name|boolean
name|synchronous
init|=
literal|true
decl_stmt|;
DECL|field|synchronization
specifier|private
name|Synchronization
name|synchronization
decl_stmt|;
DECL|field|topic
specifier|private
name|boolean
name|topic
decl_stmt|;
DECL|field|commitStrategy
specifier|private
name|TransactionCommitStrategy
name|commitStrategy
decl_stmt|;
DECL|method|DefaultMessageHandler (Endpoint endpoint, ExecutorService executor)
specifier|public
name|DefaultMessageHandler
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|ExecutorService
name|executor
parameter_list|)
block|{
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
name|this
operator|.
name|executor
operator|=
name|executor
expr_stmt|;
block|}
DECL|method|DefaultMessageHandler (Endpoint endpoint, ExecutorService executor, Synchronization synchronization)
specifier|public
name|DefaultMessageHandler
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|ExecutorService
name|executor
parameter_list|,
name|Synchronization
name|synchronization
parameter_list|)
block|{
name|super
argument_list|()
expr_stmt|;
name|this
operator|.
name|synchronization
operator|=
name|synchronization
expr_stmt|;
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
name|this
operator|.
name|executor
operator|=
name|executor
expr_stmt|;
block|}
DECL|method|DefaultMessageHandler (Endpoint endpoint, ExecutorService executor, TransactionCommitStrategy commitStrategy)
specifier|public
name|DefaultMessageHandler
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|ExecutorService
name|executor
parameter_list|,
name|TransactionCommitStrategy
name|commitStrategy
parameter_list|)
block|{
name|super
argument_list|()
expr_stmt|;
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
name|this
operator|.
name|executor
operator|=
name|executor
expr_stmt|;
name|this
operator|.
name|commitStrategy
operator|=
name|commitStrategy
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onMessage (Message message)
specifier|public
name|void
name|onMessage
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|handleMessage
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
comment|/**      * @param message      */
DECL|method|handleMessage (Message message)
specifier|private
name|void
name|handleMessage
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|RuntimeCamelException
name|rce
init|=
literal|null
decl_stmt|;
try|try
block|{
specifier|final
name|DefaultExchange
name|exchange
init|=
operator|(
name|DefaultExchange
operator|)
name|JmsMessageExchangeHelper
operator|.
name|createExchange
argument_list|(
name|message
argument_list|,
name|getEndpoint
argument_list|()
argument_list|)
decl_stmt|;
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
if|if
condition|(
name|isTransacted
argument_list|()
operator|&&
name|synchronization
operator|!=
literal|null
condition|)
block|{
name|exchange
operator|.
name|addOnCompletion
argument_list|(
name|synchronization
argument_list|)
expr_stmt|;
block|}
try|try
block|{
if|if
condition|(
name|isTransacted
argument_list|()
operator|||
name|isSynchronous
argument_list|()
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"  Handling synchronous message: {}"
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
name|doHandleMessage
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"  Handling asynchronous message: {}"
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
name|executor
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
name|doHandleMessage
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
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
if|if
condition|(
name|exchange
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|exchange
operator|.
name|getException
argument_list|()
operator|==
literal|null
condition|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
name|e
throw|;
block|}
block|}
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|rce
operator|=
name|wrapRuntimeCamelException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|rce
operator|!=
literal|null
condition|)
block|{
throw|throw
name|rce
throw|;
block|}
block|}
block|}
DECL|method|doHandleMessage (final Exchange exchange)
specifier|public
specifier|abstract
name|void
name|doHandleMessage
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|)
function_decl|;
DECL|method|close ()
specifier|public
specifier|abstract
name|void
name|close
parameter_list|()
function_decl|;
DECL|method|setTransacted (boolean transacted)
specifier|public
name|void
name|setTransacted
parameter_list|(
name|boolean
name|transacted
parameter_list|)
block|{
name|this
operator|.
name|transacted
operator|=
name|transacted
expr_stmt|;
block|}
DECL|method|isTransacted ()
specifier|public
name|boolean
name|isTransacted
parameter_list|()
block|{
return|return
name|transacted
return|;
block|}
DECL|method|getEndpoint ()
specifier|public
name|Endpoint
name|getEndpoint
parameter_list|()
block|{
return|return
name|endpoint
return|;
block|}
DECL|method|getProcessor ()
specifier|public
name|AsyncProcessor
name|getProcessor
parameter_list|()
block|{
return|return
name|processor
return|;
block|}
DECL|method|setProcessor (AsyncProcessor processor)
specifier|public
name|void
name|setProcessor
parameter_list|(
name|AsyncProcessor
name|processor
parameter_list|)
block|{
name|this
operator|.
name|processor
operator|=
name|processor
expr_stmt|;
block|}
DECL|method|setSession (Session session)
specifier|public
name|void
name|setSession
parameter_list|(
name|Session
name|session
parameter_list|)
block|{
name|this
operator|.
name|session
operator|=
name|session
expr_stmt|;
block|}
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
DECL|method|setSynchronous (boolean async)
specifier|public
name|void
name|setSynchronous
parameter_list|(
name|boolean
name|async
parameter_list|)
block|{
name|this
operator|.
name|synchronous
operator|=
name|async
expr_stmt|;
block|}
DECL|method|isSynchronous ()
specifier|public
name|boolean
name|isSynchronous
parameter_list|()
block|{
return|return
name|synchronous
return|;
block|}
DECL|method|setTopic (boolean topic)
specifier|public
name|void
name|setTopic
parameter_list|(
name|boolean
name|topic
parameter_list|)
block|{
name|this
operator|.
name|topic
operator|=
name|topic
expr_stmt|;
block|}
DECL|method|isTopic ()
specifier|public
name|boolean
name|isTopic
parameter_list|()
block|{
return|return
name|topic
return|;
block|}
DECL|method|getCommitStrategy ()
specifier|public
name|TransactionCommitStrategy
name|getCommitStrategy
parameter_list|()
block|{
return|return
name|commitStrategy
return|;
block|}
block|}
end_class

end_unit

