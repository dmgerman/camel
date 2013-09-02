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
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|TimeUnit
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
name|locks
operator|.
name|Condition
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
name|locks
operator|.
name|Lock
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
name|locks
operator|.
name|ReentrantLock
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
name|CamelContext
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
name|CamelExchangeException
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
name|Navigate
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
name|Traceable
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
name|LoggingExceptionHandler
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
name|resequencer
operator|.
name|ResequencerEngine
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
name|resequencer
operator|.
name|SequenceElementComparator
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
name|resequencer
operator|.
name|SequenceSender
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
name|ExceptionHandler
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
name|ServiceSupport
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
name|ObjectHelper
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
comment|/**  * A resequencer that re-orders a (continuous) stream of {@link Exchange}s. The  * algorithm implemented by {@link ResequencerEngine} is based on the detection  * of gaps in a message stream rather than on a fixed batch size. Gap detection  * in combination with timeouts removes the constraint of having to know the  * number of messages of a sequence (i.e. the batch size) in advance.  *<p>  * Messages must contain a unique sequence number for which a predecessor and a  * successor is known. For example a message with the sequence number 3 has a  * predecessor message with the sequence number 2 and a successor message with  * the sequence number 4. The message sequence 2,3,5 has a gap because the  * successor of 3 is missing. The resequencer therefore has to retain message 5  * until message 4 arrives (or a timeout occurs).  *<p>  * Instances of this class poll for {@link Exchange}s from a given  *<code>endpoint</code>. Resequencing work and the delivery of messages to  * the next<code>processor</code> is done within the single polling thread.  *   * @version   *   * @see ResequencerEngine  */
end_comment

begin_class
DECL|class|StreamResequencer
specifier|public
class|class
name|StreamResequencer
extends|extends
name|ServiceSupport
implements|implements
name|SequenceSender
argument_list|<
name|Exchange
argument_list|>
implements|,
name|AsyncProcessor
implements|,
name|Navigate
argument_list|<
name|Processor
argument_list|>
implements|,
name|Traceable
block|{
DECL|field|DELIVERY_ATTEMPT_INTERVAL
specifier|private
specifier|static
specifier|final
name|long
name|DELIVERY_ATTEMPT_INTERVAL
init|=
literal|1000L
decl_stmt|;
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
name|StreamResequencer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|camelContext
specifier|private
specifier|final
name|CamelContext
name|camelContext
decl_stmt|;
DECL|field|exceptionHandler
specifier|private
specifier|final
name|ExceptionHandler
name|exceptionHandler
decl_stmt|;
DECL|field|engine
specifier|private
specifier|final
name|ResequencerEngine
argument_list|<
name|Exchange
argument_list|>
name|engine
decl_stmt|;
DECL|field|processor
specifier|private
specifier|final
name|Processor
name|processor
decl_stmt|;
DECL|field|delivery
specifier|private
name|Delivery
name|delivery
decl_stmt|;
DECL|field|capacity
specifier|private
name|int
name|capacity
decl_stmt|;
DECL|field|ignoreInvalidExchanges
specifier|private
name|boolean
name|ignoreInvalidExchanges
decl_stmt|;
comment|/**      * Creates a new {@link StreamResequencer} instance.      *       * @param processor next processor that processes re-ordered exchanges.      * @param comparator a sequence element comparator for exchanges.      */
DECL|method|StreamResequencer (CamelContext camelContext, Processor processor, SequenceElementComparator<Exchange> comparator)
specifier|public
name|StreamResequencer
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|Processor
name|processor
parameter_list|,
name|SequenceElementComparator
argument_list|<
name|Exchange
argument_list|>
name|comparator
parameter_list|)
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|camelContext
argument_list|,
literal|"CamelContext"
argument_list|)
expr_stmt|;
name|this
operator|.
name|camelContext
operator|=
name|camelContext
expr_stmt|;
name|this
operator|.
name|engine
operator|=
operator|new
name|ResequencerEngine
argument_list|<
name|Exchange
argument_list|>
argument_list|(
name|comparator
argument_list|)
expr_stmt|;
name|this
operator|.
name|engine
operator|.
name|setSequenceSender
argument_list|(
name|this
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
name|exceptionHandler
operator|=
operator|new
name|LoggingExceptionHandler
argument_list|(
name|camelContext
argument_list|,
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns this resequencer's exception handler.      */
DECL|method|getExceptionHandler ()
specifier|public
name|ExceptionHandler
name|getExceptionHandler
parameter_list|()
block|{
return|return
name|exceptionHandler
return|;
block|}
comment|/**      * Returns the next processor.      */
DECL|method|getProcessor ()
specifier|public
name|Processor
name|getProcessor
parameter_list|()
block|{
return|return
name|processor
return|;
block|}
comment|/**      * Returns this resequencer's capacity. The capacity is the maximum number      * of exchanges that can be managed by this resequencer at a given point in      * time. If the capacity if reached, polling from the endpoint will be      * skipped for<code>timeout</code> milliseconds giving exchanges the      * possibility to time out and to be delivered after the waiting period.      *       * @return this resequencer's capacity.      */
DECL|method|getCapacity ()
specifier|public
name|int
name|getCapacity
parameter_list|()
block|{
return|return
name|capacity
return|;
block|}
comment|/**      * Returns this resequencer's timeout. This sets the resequencer engine's      * timeout via {@link ResequencerEngine#setTimeout(long)}. This value is      * also used to define the polling timeout from the endpoint.      *       * @return this resequencer's timeout. (Processor)      * @see ResequencerEngine#setTimeout(long)      */
DECL|method|getTimeout ()
specifier|public
name|long
name|getTimeout
parameter_list|()
block|{
return|return
name|engine
operator|.
name|getTimeout
argument_list|()
return|;
block|}
DECL|method|setCapacity (int capacity)
specifier|public
name|void
name|setCapacity
parameter_list|(
name|int
name|capacity
parameter_list|)
block|{
name|this
operator|.
name|capacity
operator|=
name|capacity
expr_stmt|;
block|}
DECL|method|setTimeout (long timeout)
specifier|public
name|void
name|setTimeout
parameter_list|(
name|long
name|timeout
parameter_list|)
block|{
name|engine
operator|.
name|setTimeout
argument_list|(
name|timeout
argument_list|)
expr_stmt|;
block|}
DECL|method|isIgnoreInvalidExchanges ()
specifier|public
name|boolean
name|isIgnoreInvalidExchanges
parameter_list|()
block|{
return|return
name|ignoreInvalidExchanges
return|;
block|}
DECL|method|setRejectOld (Boolean rejectOld)
specifier|public
name|void
name|setRejectOld
parameter_list|(
name|Boolean
name|rejectOld
parameter_list|)
block|{
name|engine
operator|.
name|setRejectOld
argument_list|(
name|rejectOld
argument_list|)
expr_stmt|;
block|}
comment|/**      * Sets whether to ignore invalid exchanges which cannot be used by this stream resequencer.      *<p/>      * Default is<tt>false</tt>, by which an {@link CamelExchangeException} is thrown if the {@link Exchange}      * is invalid.      */
DECL|method|setIgnoreInvalidExchanges (boolean ignoreInvalidExchanges)
specifier|public
name|void
name|setIgnoreInvalidExchanges
parameter_list|(
name|boolean
name|ignoreInvalidExchanges
parameter_list|)
block|{
name|this
operator|.
name|ignoreInvalidExchanges
operator|=
name|ignoreInvalidExchanges
expr_stmt|;
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
literal|"StreamResequencer[to: "
operator|+
name|processor
operator|+
literal|"]"
return|;
block|}
DECL|method|getTraceLabel ()
specifier|public
name|String
name|getTraceLabel
parameter_list|()
block|{
return|return
literal|"streamResequence"
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
name|ServiceHelper
operator|.
name|startServices
argument_list|(
name|processor
argument_list|)
expr_stmt|;
name|delivery
operator|=
operator|new
name|Delivery
argument_list|()
expr_stmt|;
name|engine
operator|.
name|start
argument_list|()
expr_stmt|;
name|delivery
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
comment|// let's stop everything in the reverse order
comment|// no need to stop the worker thread -- it will stop automatically when this service is stopped
name|engine
operator|.
name|stop
argument_list|()
expr_stmt|;
name|ServiceHelper
operator|.
name|stopServices
argument_list|(
name|processor
argument_list|)
expr_stmt|;
block|}
comment|/**      * Sends the<code>exchange</code> to the next<code>processor</code>.      *       * @param exchange exchange to send.      */
DECL|method|sendElement (Exchange exchange)
specifier|public
name|void
name|sendElement
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|processor
operator|.
name|process
argument_list|(
name|exchange
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
DECL|method|process (Exchange exchange, AsyncCallback callback)
specifier|public
name|boolean
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|AsyncCallback
name|callback
parameter_list|)
block|{
while|while
condition|(
name|engine
operator|.
name|size
argument_list|()
operator|>=
name|capacity
condition|)
block|{
try|try
block|{
name|Thread
operator|.
name|sleep
argument_list|(
name|getTimeout
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
comment|// we was interrupted so break out
name|exchange
operator|.
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
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
block|}
try|try
block|{
name|engine
operator|.
name|insert
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|delivery
operator|.
name|request
argument_list|()
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
name|isIgnoreInvalidExchanges
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Invalid Exchange. This Exchange will be ignored: {}"
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|exchange
operator|.
name|setException
argument_list|(
operator|new
name|CamelExchangeException
argument_list|(
literal|"Error processing Exchange in StreamResequencer"
argument_list|,
name|exchange
argument_list|,
name|e
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
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
DECL|method|hasNext ()
specifier|public
name|boolean
name|hasNext
parameter_list|()
block|{
return|return
name|processor
operator|!=
literal|null
return|;
block|}
DECL|method|next ()
specifier|public
name|List
argument_list|<
name|Processor
argument_list|>
name|next
parameter_list|()
block|{
if|if
condition|(
operator|!
name|hasNext
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
name|List
argument_list|<
name|Processor
argument_list|>
name|answer
init|=
operator|new
name|ArrayList
argument_list|<
name|Processor
argument_list|>
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|answer
operator|.
name|add
argument_list|(
name|processor
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
DECL|class|Delivery
class|class
name|Delivery
extends|extends
name|Thread
block|{
DECL|field|deliveryRequestLock
specifier|private
name|Lock
name|deliveryRequestLock
init|=
operator|new
name|ReentrantLock
argument_list|()
decl_stmt|;
DECL|field|deliveryRequestCondition
specifier|private
name|Condition
name|deliveryRequestCondition
init|=
name|deliveryRequestLock
operator|.
name|newCondition
argument_list|()
decl_stmt|;
DECL|method|Delivery ()
specifier|public
name|Delivery
parameter_list|()
block|{
name|super
argument_list|(
name|camelContext
operator|.
name|getExecutorServiceManager
argument_list|()
operator|.
name|resolveThreadName
argument_list|(
literal|"Resequencer Delivery"
argument_list|)
argument_list|)
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
while|while
condition|(
name|isRunAllowed
argument_list|()
condition|)
block|{
try|try
block|{
name|deliveryRequestLock
operator|.
name|lock
argument_list|()
expr_stmt|;
try|try
block|{
name|deliveryRequestCondition
operator|.
name|await
argument_list|(
name|DELIVERY_ATTEMPT_INTERVAL
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|deliveryRequestLock
operator|.
name|unlock
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
break|break;
block|}
try|try
block|{
name|engine
operator|.
name|deliver
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
comment|// a fail safe to handle all exceptions being thrown
name|getExceptionHandler
argument_list|()
operator|.
name|handleException
argument_list|(
name|t
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|cancel ()
specifier|public
name|void
name|cancel
parameter_list|()
block|{
name|interrupt
argument_list|()
expr_stmt|;
block|}
DECL|method|request ()
specifier|public
name|void
name|request
parameter_list|()
block|{
name|deliveryRequestLock
operator|.
name|lock
argument_list|()
expr_stmt|;
try|try
block|{
name|deliveryRequestCondition
operator|.
name|signal
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
name|deliveryRequestLock
operator|.
name|unlock
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

