begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
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
name|RejectedExecutionException
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
name|impl
operator|.
name|converter
operator|.
name|AsyncProcessorTypeConverter
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
name|model
operator|.
name|ExceptionType
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
name|processor
operator|.
name|exceptionpolicy
operator|.
name|ExceptionPolicyStrategy
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
name|AsyncProcessorHelper
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
name|ServiceHelper
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
name|logging
operator|.
name|Log
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
name|logging
operator|.
name|LogFactory
import|;
end_import

begin_comment
comment|/**  * Implements a<a  * href="http://activemq.apache.org/camel/dead-letter-channel.html">Dead Letter  * Channel</a> after attempting to redeliver the message using the  * {@link RedeliveryPolicy}  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|DeadLetterChannel
specifier|public
class|class
name|DeadLetterChannel
extends|extends
name|ErrorHandlerSupport
implements|implements
name|AsyncProcessor
block|{
DECL|field|REDELIVERY_COUNTER
specifier|public
specifier|static
specifier|final
name|String
name|REDELIVERY_COUNTER
init|=
literal|"org.apache.camel.RedeliveryCounter"
decl_stmt|;
DECL|field|REDELIVERED
specifier|public
specifier|static
specifier|final
name|String
name|REDELIVERED
init|=
literal|"org.apache.camel.Redelivered"
decl_stmt|;
DECL|field|EXCEPTION_CAUSE_PROPERTY
specifier|public
specifier|static
specifier|final
name|String
name|EXCEPTION_CAUSE_PROPERTY
init|=
literal|"CamelCauseException"
decl_stmt|;
DECL|class|RedeliveryData
specifier|private
class|class
name|RedeliveryData
block|{
DECL|field|redeliveryCounter
name|int
name|redeliveryCounter
decl_stmt|;
DECL|field|redeliveryDelay
name|long
name|redeliveryDelay
decl_stmt|;
DECL|field|sync
name|boolean
name|sync
init|=
literal|true
decl_stmt|;
comment|// default behaviour which can be overloaded on a per exception basis
DECL|field|currentRedeliveryPolicy
name|RedeliveryPolicy
name|currentRedeliveryPolicy
init|=
name|redeliveryPolicy
decl_stmt|;
DECL|field|failureProcessor
name|Processor
name|failureProcessor
init|=
name|deadLetter
decl_stmt|;
block|}
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|DeadLetterChannel
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|FAILURE_HANDLED_PROPERTY
specifier|private
specifier|static
specifier|final
name|String
name|FAILURE_HANDLED_PROPERTY
init|=
name|DeadLetterChannel
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|".FAILURE_HANDLED"
decl_stmt|;
DECL|field|output
specifier|private
name|Processor
name|output
decl_stmt|;
DECL|field|deadLetter
specifier|private
name|Processor
name|deadLetter
decl_stmt|;
DECL|field|outputAsync
specifier|private
name|AsyncProcessor
name|outputAsync
decl_stmt|;
DECL|field|redeliveryPolicy
specifier|private
name|RedeliveryPolicy
name|redeliveryPolicy
decl_stmt|;
DECL|field|logger
specifier|private
name|Logger
name|logger
decl_stmt|;
DECL|method|DeadLetterChannel (Processor output, Processor deadLetter)
specifier|public
name|DeadLetterChannel
parameter_list|(
name|Processor
name|output
parameter_list|,
name|Processor
name|deadLetter
parameter_list|)
block|{
name|this
argument_list|(
name|output
argument_list|,
name|deadLetter
argument_list|,
operator|new
name|RedeliveryPolicy
argument_list|()
argument_list|,
name|DeadLetterChannel
operator|.
name|createDefaultLogger
argument_list|()
argument_list|,
name|ErrorHandlerSupport
operator|.
name|createDefaultExceptionPolicyStrategy
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|DeadLetterChannel (Processor output, Processor deadLetter, RedeliveryPolicy redeliveryPolicy, Logger logger, ExceptionPolicyStrategy exceptionPolicyStrategy)
specifier|public
name|DeadLetterChannel
parameter_list|(
name|Processor
name|output
parameter_list|,
name|Processor
name|deadLetter
parameter_list|,
name|RedeliveryPolicy
name|redeliveryPolicy
parameter_list|,
name|Logger
name|logger
parameter_list|,
name|ExceptionPolicyStrategy
name|exceptionPolicyStrategy
parameter_list|)
block|{
name|this
operator|.
name|deadLetter
operator|=
name|deadLetter
expr_stmt|;
name|this
operator|.
name|output
operator|=
name|output
expr_stmt|;
name|this
operator|.
name|outputAsync
operator|=
name|AsyncProcessorTypeConverter
operator|.
name|convert
argument_list|(
name|output
argument_list|)
expr_stmt|;
name|this
operator|.
name|redeliveryPolicy
operator|=
name|redeliveryPolicy
expr_stmt|;
name|this
operator|.
name|logger
operator|=
name|logger
expr_stmt|;
name|setExceptionPolicy
argument_list|(
name|exceptionPolicyStrategy
argument_list|)
expr_stmt|;
block|}
DECL|method|createDefaultLogger ()
specifier|public
specifier|static
parameter_list|<
name|E
extends|extends
name|Exchange
parameter_list|>
name|Logger
name|createDefaultLogger
parameter_list|()
block|{
return|return
operator|new
name|Logger
argument_list|(
name|LOG
argument_list|,
name|LoggingLevel
operator|.
name|ERROR
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"DeadLetterChannel["
operator|+
name|output
operator|+
literal|", "
operator|+
name|deadLetter
operator|+
literal|", "
operator|+
name|redeliveryPolicy
operator|+
literal|"]"
return|;
block|}
DECL|method|process (Exchange exchange, final AsyncCallback callback)
specifier|public
name|boolean
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
specifier|final
name|AsyncCallback
name|callback
parameter_list|)
block|{
return|return
name|process
argument_list|(
name|exchange
argument_list|,
name|callback
argument_list|,
operator|new
name|RedeliveryData
argument_list|()
argument_list|)
return|;
block|}
DECL|method|process (final Exchange exchange, final AsyncCallback callback, final RedeliveryData data)
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
parameter_list|,
specifier|final
name|RedeliveryData
name|data
parameter_list|)
block|{
while|while
condition|(
literal|true
condition|)
block|{
comment|// We can't keep retrying if the route is being shutdown.
if|if
condition|(
operator|!
name|isRunAllowed
argument_list|()
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
operator|new
name|RejectedExecutionException
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|callback
operator|.
name|done
argument_list|(
name|data
operator|.
name|sync
argument_list|)
expr_stmt|;
return|return
name|data
operator|.
name|sync
return|;
block|}
if|if
condition|(
name|exchange
operator|.
name|getException
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|Throwable
name|e
init|=
name|exchange
operator|.
name|getException
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|setException
argument_list|(
literal|null
argument_list|)
expr_stmt|;
comment|// Reset it since we are handling it.
name|logger
operator|.
name|log
argument_list|(
literal|"Failed delivery for exchangeId: "
operator|+
name|exchange
operator|.
name|getExchangeId
argument_list|()
operator|+
literal|". On delivery attempt: "
operator|+
name|data
operator|.
name|redeliveryCounter
operator|+
literal|" caught: "
operator|+
name|e
argument_list|,
name|e
argument_list|)
expr_stmt|;
name|data
operator|.
name|redeliveryCounter
operator|=
name|incrementRedeliveryCounter
argument_list|(
name|exchange
argument_list|,
name|e
argument_list|)
expr_stmt|;
name|ExceptionType
name|exceptionPolicy
init|=
name|getExceptionPolicy
argument_list|(
name|exchange
argument_list|,
name|e
argument_list|)
decl_stmt|;
if|if
condition|(
name|exceptionPolicy
operator|!=
literal|null
condition|)
block|{
name|data
operator|.
name|currentRedeliveryPolicy
operator|=
name|exceptionPolicy
operator|.
name|createRedeliveryPolicy
argument_list|(
name|data
operator|.
name|currentRedeliveryPolicy
argument_list|)
expr_stmt|;
name|Processor
name|processor
init|=
name|exceptionPolicy
operator|.
name|getErrorHandler
argument_list|()
decl_stmt|;
if|if
condition|(
name|processor
operator|!=
literal|null
condition|)
block|{
name|data
operator|.
name|failureProcessor
operator|=
name|processor
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
operator|!
name|data
operator|.
name|currentRedeliveryPolicy
operator|.
name|shouldRedeliver
argument_list|(
name|data
operator|.
name|redeliveryCounter
argument_list|)
condition|)
block|{
name|setFailureHandled
argument_list|(
name|exchange
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|AsyncProcessor
name|afp
init|=
name|AsyncProcessorTypeConverter
operator|.
name|convert
argument_list|(
name|data
operator|.
name|failureProcessor
argument_list|)
decl_stmt|;
name|boolean
name|sync
init|=
name|afp
operator|.
name|process
argument_list|(
name|exchange
argument_list|,
operator|new
name|AsyncCallback
argument_list|()
block|{
specifier|public
name|void
name|done
parameter_list|(
name|boolean
name|sync
parameter_list|)
block|{
name|restoreExceptionOnExchange
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|callback
operator|.
name|done
argument_list|(
name|data
operator|.
name|sync
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
name|restoreExceptionOnExchange
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|logger
operator|.
name|log
argument_list|(
literal|"Failed delivery for exchangeId: "
operator|+
name|exchange
operator|.
name|getExchangeId
argument_list|()
operator|+
literal|". Handled by the failure processor: "
operator|+
name|data
operator|.
name|failureProcessor
argument_list|)
expr_stmt|;
return|return
name|sync
return|;
block|}
if|if
condition|(
name|data
operator|.
name|redeliveryCounter
operator|>
literal|0
condition|)
block|{
comment|// Figure out how long we should wait to resend this message.
name|data
operator|.
name|redeliveryDelay
operator|=
name|data
operator|.
name|currentRedeliveryPolicy
operator|.
name|getRedeliveryDelay
argument_list|(
name|data
operator|.
name|redeliveryDelay
argument_list|)
expr_stmt|;
name|sleep
argument_list|(
name|data
operator|.
name|redeliveryDelay
argument_list|)
expr_stmt|;
block|}
name|exchange
operator|.
name|setProperty
argument_list|(
name|EXCEPTION_CAUSE_PROPERTY
argument_list|,
name|exchange
operator|.
name|getException
argument_list|()
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setException
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|boolean
name|sync
init|=
name|outputAsync
operator|.
name|process
argument_list|(
name|exchange
argument_list|,
operator|new
name|AsyncCallback
argument_list|()
block|{
specifier|public
name|void
name|done
parameter_list|(
name|boolean
name|sync
parameter_list|)
block|{
comment|// Only handle the async case...
if|if
condition|(
name|sync
condition|)
block|{
return|return;
block|}
name|data
operator|.
name|sync
operator|=
literal|false
expr_stmt|;
if|if
condition|(
name|exchange
operator|.
name|getException
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|process
argument_list|(
name|exchange
argument_list|,
name|callback
argument_list|,
name|data
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|callback
operator|.
name|done
argument_list|(
name|sync
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|sync
condition|)
block|{
comment|// It is going to be processed async..
return|return
literal|false
return|;
block|}
if|if
condition|(
name|exchange
operator|.
name|getException
argument_list|()
operator|==
literal|null
operator|||
name|isFailureHandled
argument_list|(
name|exchange
argument_list|)
condition|)
block|{
comment|// If everything went well.. then we exit here..
name|callback
operator|.
name|done
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
comment|// error occurred so loop back around.....
block|}
block|}
DECL|method|isFailureHandled (Exchange exchange)
specifier|public
specifier|static
name|boolean
name|isFailureHandled
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|exchange
operator|.
name|getProperty
argument_list|(
name|FAILURE_HANDLED_PROPERTY
argument_list|)
operator|!=
literal|null
return|;
block|}
DECL|method|setFailureHandled (Exchange exchange, boolean isHandled)
specifier|public
specifier|static
name|void
name|setFailureHandled
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|boolean
name|isHandled
parameter_list|)
block|{
if|if
condition|(
name|isHandled
condition|)
block|{
name|exchange
operator|.
name|setProperty
argument_list|(
name|FAILURE_HANDLED_PROPERTY
argument_list|,
name|exchange
operator|.
name|getException
argument_list|()
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setException
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|exchange
operator|.
name|setException
argument_list|(
name|exchange
operator|.
name|getProperty
argument_list|(
name|FAILURE_HANDLED_PROPERTY
argument_list|,
name|Throwable
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|removeProperty
argument_list|(
name|FAILURE_HANDLED_PROPERTY
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|restoreExceptionOnExchange (Exchange exchange)
specifier|public
specifier|static
name|void
name|restoreExceptionOnExchange
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
name|exchange
operator|.
name|getProperty
argument_list|(
name|FAILURE_HANDLED_PROPERTY
argument_list|,
name|Throwable
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|process (Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|AsyncProcessorHelper
operator|.
name|process
argument_list|(
name|this
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
comment|// Properties
comment|// -------------------------------------------------------------------------
comment|/**      * Returns the output processor      */
DECL|method|getOutput ()
specifier|public
name|Processor
name|getOutput
parameter_list|()
block|{
return|return
name|output
return|;
block|}
comment|/**      * Returns the dead letter that message exchanges will be sent to if the      * redelivery attempts fail      */
DECL|method|getDeadLetter ()
specifier|public
name|Processor
name|getDeadLetter
parameter_list|()
block|{
return|return
name|deadLetter
return|;
block|}
DECL|method|getRedeliveryPolicy ()
specifier|public
name|RedeliveryPolicy
name|getRedeliveryPolicy
parameter_list|()
block|{
return|return
name|redeliveryPolicy
return|;
block|}
comment|/**      * Sets the redelivery policy      */
DECL|method|setRedeliveryPolicy (RedeliveryPolicy redeliveryPolicy)
specifier|public
name|void
name|setRedeliveryPolicy
parameter_list|(
name|RedeliveryPolicy
name|redeliveryPolicy
parameter_list|)
block|{
name|this
operator|.
name|redeliveryPolicy
operator|=
name|redeliveryPolicy
expr_stmt|;
block|}
DECL|method|getLogger ()
specifier|public
name|Logger
name|getLogger
parameter_list|()
block|{
return|return
name|logger
return|;
block|}
comment|/**      * Sets the logger strategy; which {@link Log} to use and which      * {@link LoggingLevel} to use      */
DECL|method|setLogger (Logger logger)
specifier|public
name|void
name|setLogger
parameter_list|(
name|Logger
name|logger
parameter_list|)
block|{
name|this
operator|.
name|logger
operator|=
name|logger
expr_stmt|;
block|}
comment|// Implementation methods
comment|// -------------------------------------------------------------------------
comment|/**      * Increments the redelivery counter and adds the redelivered flag if the      * message has been redelivered      */
DECL|method|incrementRedeliveryCounter (Exchange exchange, Throwable e)
specifier|protected
name|int
name|incrementRedeliveryCounter
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Throwable
name|e
parameter_list|)
block|{
name|Message
name|in
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|Integer
name|counter
init|=
name|in
operator|.
name|getHeader
argument_list|(
name|REDELIVERY_COUNTER
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
decl_stmt|;
name|int
name|next
init|=
literal|1
decl_stmt|;
if|if
condition|(
name|counter
operator|!=
literal|null
condition|)
block|{
name|next
operator|=
name|counter
operator|+
literal|1
expr_stmt|;
block|}
name|in
operator|.
name|setHeader
argument_list|(
name|REDELIVERY_COUNTER
argument_list|,
name|next
argument_list|)
expr_stmt|;
name|in
operator|.
name|setHeader
argument_list|(
name|REDELIVERED
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
return|return
name|next
return|;
block|}
DECL|method|sleep (long redeliveryDelay)
specifier|protected
name|void
name|sleep
parameter_list|(
name|long
name|redeliveryDelay
parameter_list|)
block|{
if|if
condition|(
name|redeliveryDelay
operator|>
literal|0
condition|)
block|{
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
literal|"Sleeping for: "
operator|+
name|redeliveryDelay
operator|+
literal|" millis until attempting redelivery"
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|Thread
operator|.
name|sleep
argument_list|(
name|redeliveryDelay
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
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
literal|"Thread interrupted: "
operator|+
name|e
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
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
name|ServiceHelper
operator|.
name|startServices
argument_list|(
name|output
argument_list|,
name|deadLetter
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
name|ServiceHelper
operator|.
name|stopServices
argument_list|(
name|deadLetter
argument_list|,
name|output
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

