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
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
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
name|PollingConsumer
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
name|impl
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
comment|/**  * A base class for any kind of {@link Processor} which implements some kind of  * batch processing.  *   * @version $Revision$  */
end_comment

begin_class
DECL|class|BatchProcessor
specifier|public
class|class
name|BatchProcessor
extends|extends
name|ServiceSupport
implements|implements
name|Runnable
implements|,
name|Processor
block|{
DECL|field|DEFAULT_BATCH_TIMEOUT
specifier|public
specifier|static
specifier|final
name|long
name|DEFAULT_BATCH_TIMEOUT
init|=
literal|1000L
decl_stmt|;
DECL|field|DEFAULT_BATCH_SIZE
specifier|public
specifier|static
specifier|final
name|int
name|DEFAULT_BATCH_SIZE
init|=
literal|100
decl_stmt|;
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
name|BatchProcessor
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|endpoint
specifier|private
name|Endpoint
name|endpoint
decl_stmt|;
DECL|field|processor
specifier|private
name|Processor
name|processor
decl_stmt|;
DECL|field|collection
specifier|private
name|Collection
argument_list|<
name|Exchange
argument_list|>
name|collection
decl_stmt|;
DECL|field|batchTimeout
specifier|private
name|long
name|batchTimeout
init|=
name|DEFAULT_BATCH_TIMEOUT
decl_stmt|;
DECL|field|batchSize
specifier|private
name|int
name|batchSize
init|=
name|DEFAULT_BATCH_SIZE
decl_stmt|;
DECL|field|outBatchSize
specifier|private
name|int
name|outBatchSize
decl_stmt|;
DECL|field|consumer
specifier|private
name|PollingConsumer
name|consumer
decl_stmt|;
DECL|field|exceptionHandler
specifier|private
name|ExceptionHandler
name|exceptionHandler
decl_stmt|;
DECL|method|BatchProcessor (Endpoint endpoint, Processor processor, Collection<Exchange> collection)
specifier|public
name|BatchProcessor
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|Processor
name|processor
parameter_list|,
name|Collection
argument_list|<
name|Exchange
argument_list|>
name|collection
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
name|processor
operator|=
name|processor
expr_stmt|;
name|this
operator|.
name|collection
operator|=
name|collection
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
literal|"BatchProcessor[to: "
operator|+
name|processor
operator|+
literal|"]"
return|;
block|}
DECL|method|run ()
specifier|public
name|void
name|run
parameter_list|()
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Starting thread for "
operator|+
name|this
argument_list|)
expr_stmt|;
while|while
condition|(
name|isRunAllowed
argument_list|()
condition|)
block|{
try|try
block|{
name|processBatch
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
name|e
argument_list|)
expr_stmt|;
block|}
block|}
name|collection
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
comment|// Properties
comment|// -------------------------------------------------------------------------
DECL|method|getExceptionHandler ()
specifier|public
name|ExceptionHandler
name|getExceptionHandler
parameter_list|()
block|{
if|if
condition|(
name|exceptionHandler
operator|==
literal|null
condition|)
block|{
name|exceptionHandler
operator|=
operator|new
name|LoggingExceptionHandler
argument_list|(
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|exceptionHandler
return|;
block|}
DECL|method|setExceptionHandler (ExceptionHandler exceptionHandler)
specifier|public
name|void
name|setExceptionHandler
parameter_list|(
name|ExceptionHandler
name|exceptionHandler
parameter_list|)
block|{
name|this
operator|.
name|exceptionHandler
operator|=
name|exceptionHandler
expr_stmt|;
block|}
DECL|method|getBatchSize ()
specifier|public
name|int
name|getBatchSize
parameter_list|()
block|{
return|return
name|batchSize
return|;
block|}
comment|/**      * Sets the<b>in</b> batch size. This is the number of incoming exchanges that this batch processor      * will process before its completed. The default value is {@link #DEFAULT_BATCH_SIZE}.      *      * @param batchSize the size      */
DECL|method|setBatchSize (int batchSize)
specifier|public
name|void
name|setBatchSize
parameter_list|(
name|int
name|batchSize
parameter_list|)
block|{
name|this
operator|.
name|batchSize
operator|=
name|batchSize
expr_stmt|;
block|}
DECL|method|getOutBatchSize ()
specifier|public
name|int
name|getOutBatchSize
parameter_list|()
block|{
return|return
name|outBatchSize
return|;
block|}
comment|/**      * Sets the<b>out</b> batch size. If the batch processor holds more exchanges than this out size then      * the completion is triggered. Can for instance be used to ensure that this batch is completed when      * a certain number of exchanges has been collected. By default this feature is<b>not</b> enabled.      *      * @param outBatchSize the size      */
DECL|method|setOutBatchSize (int outBatchSize)
specifier|public
name|void
name|setOutBatchSize
parameter_list|(
name|int
name|outBatchSize
parameter_list|)
block|{
name|this
operator|.
name|outBatchSize
operator|=
name|outBatchSize
expr_stmt|;
block|}
DECL|method|getBatchTimeout ()
specifier|public
name|long
name|getBatchTimeout
parameter_list|()
block|{
return|return
name|batchTimeout
return|;
block|}
DECL|method|setBatchTimeout (long batchTimeout)
specifier|public
name|void
name|setBatchTimeout
parameter_list|(
name|long
name|batchTimeout
parameter_list|)
block|{
name|this
operator|.
name|batchTimeout
operator|=
name|batchTimeout
expr_stmt|;
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
name|Processor
name|getProcessor
parameter_list|()
block|{
return|return
name|processor
return|;
block|}
comment|/**      * A transactional method to process a batch of messages up to a timeout      * period or number of messages reached.      */
DECL|method|processBatch ()
specifier|protected
specifier|synchronized
name|void
name|processBatch
parameter_list|()
throws|throws
name|Exception
block|{
name|long
name|start
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
decl_stmt|;
name|long
name|end
init|=
name|start
operator|+
name|batchTimeout
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
operator|!
name|isBatchCompleted
argument_list|(
name|i
argument_list|)
condition|;
name|i
operator|++
control|)
block|{
name|long
name|timeout
init|=
name|end
operator|-
name|System
operator|.
name|currentTimeMillis
argument_list|()
decl_stmt|;
if|if
condition|(
name|timeout
operator|<
literal|0L
condition|)
block|{
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"batch timeout expired at batch index: "
operator|+
name|i
argument_list|)
expr_stmt|;
block|}
break|break;
block|}
name|Exchange
name|exchange
init|=
name|consumer
operator|.
name|receive
argument_list|(
name|timeout
argument_list|)
decl_stmt|;
if|if
condition|(
name|exchange
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"receive with timeout: "
operator|+
name|timeout
operator|+
literal|" expired at batch index: "
operator|+
name|i
argument_list|)
expr_stmt|;
block|}
break|break;
block|}
name|collection
operator|.
name|add
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
comment|// we should NOT log the collection directly as it will invoke a toString() on collection
comment|// and it will call collection.iterator() where end-users might do stuff that would break
comment|// calling the iterator a 2nd time as below
comment|// lets send the batch
name|Iterator
argument_list|<
name|Exchange
argument_list|>
name|iter
init|=
name|collection
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|iter
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Exchange
name|exchange
init|=
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|iter
operator|.
name|remove
argument_list|()
expr_stmt|;
name|processExchange
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * A strategy method to decide if the batch is completed the resulting exchanges should be sent      */
DECL|method|isBatchCompleted (int index)
specifier|protected
name|boolean
name|isBatchCompleted
parameter_list|(
name|int
name|index
parameter_list|)
block|{
comment|// out batch size is optional and we should only check if its enabled (> 0)
if|if
condition|(
name|outBatchSize
operator|>
literal|0
operator|&&
name|collection
operator|.
name|size
argument_list|()
operator|>=
name|outBatchSize
condition|)
block|{
return|return
literal|true
return|;
block|}
comment|// fallback yo regular batch size check
return|return
name|index
operator|>=
name|batchSize
return|;
block|}
comment|/**      * Strategy Method to process an exchange in the batch. This method allows      * derived classes to perform custom processing before or after an      * individual exchange is processed      */
DECL|method|processExchange (Exchange exchange)
specifier|protected
name|void
name|processExchange
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
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
name|consumer
operator|=
name|endpoint
operator|.
name|createPollingConsumer
argument_list|()
expr_stmt|;
name|ServiceHelper
operator|.
name|startServices
argument_list|(
name|processor
argument_list|,
name|consumer
argument_list|)
expr_stmt|;
name|Thread
name|thread
init|=
operator|new
name|Thread
argument_list|(
name|this
argument_list|,
name|this
operator|+
literal|" Polling Thread"
argument_list|)
decl_stmt|;
name|thread
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
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
name|consumer
argument_list|,
name|processor
argument_list|)
expr_stmt|;
name|collection
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
DECL|method|getCollection ()
specifier|protected
name|Collection
argument_list|<
name|Exchange
argument_list|>
name|getCollection
parameter_list|()
block|{
return|return
name|collection
return|;
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
comment|// empty since exchanges come from endpoint's polling consumer
block|}
block|}
end_class

end_unit

