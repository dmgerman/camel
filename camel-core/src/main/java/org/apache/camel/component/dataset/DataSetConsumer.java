begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.dataset
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|dataset
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
name|processor
operator|.
name|ThroughputLogger
import|;
end_import

begin_comment
comment|/**  * DataSet consumer.  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|DataSetConsumer
specifier|public
class|class
name|DataSetConsumer
extends|extends
name|DefaultConsumer
block|{
DECL|field|endpoint
specifier|private
name|DataSetEndpoint
name|endpoint
decl_stmt|;
DECL|field|reporter
specifier|private
name|Processor
name|reporter
decl_stmt|;
DECL|field|executorService
specifier|private
name|ExecutorService
name|executorService
decl_stmt|;
DECL|method|DataSetConsumer (DataSetEndpoint endpoint, Processor processor)
specifier|public
name|DataSetConsumer
parameter_list|(
name|DataSetEndpoint
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
name|endpoint
operator|=
name|endpoint
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
name|reporter
operator|==
literal|null
condition|)
block|{
name|reporter
operator|=
name|createReporter
argument_list|()
expr_stmt|;
block|}
specifier|final
name|DataSet
name|dataSet
init|=
name|endpoint
operator|.
name|getDataSet
argument_list|()
decl_stmt|;
specifier|final
name|long
name|preloadSize
init|=
name|endpoint
operator|.
name|getPreloadSize
argument_list|()
decl_stmt|;
name|sendMessages
argument_list|(
literal|0
argument_list|,
name|preloadSize
argument_list|)
expr_stmt|;
name|executorService
operator|=
name|endpoint
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getExecutorServiceStrategy
argument_list|()
operator|.
name|newSingleThreadExecutor
argument_list|(
name|this
argument_list|,
name|endpoint
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
name|executorService
operator|.
name|execute
argument_list|(
operator|new
name|Runnable
argument_list|()
block|{
specifier|public
name|void
name|run
parameter_list|()
block|{
if|if
condition|(
name|endpoint
operator|.
name|getInitialDelay
argument_list|()
operator|>
literal|0
condition|)
block|{
try|try
block|{
name|Thread
operator|.
name|sleep
argument_list|(
name|endpoint
operator|.
name|getInitialDelay
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
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|interrupt
argument_list|()
expr_stmt|;
return|return;
block|}
block|}
name|sendMessages
argument_list|(
name|preloadSize
argument_list|,
name|dataSet
operator|.
name|getSize
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
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
if|if
condition|(
name|executorService
operator|!=
literal|null
condition|)
block|{
name|executorService
operator|.
name|shutdown
argument_list|()
expr_stmt|;
name|executorService
operator|=
literal|null
expr_stmt|;
block|}
block|}
DECL|method|sendMessages (long startIndex, long endIndex)
specifier|protected
name|void
name|sendMessages
parameter_list|(
name|long
name|startIndex
parameter_list|,
name|long
name|endIndex
parameter_list|)
block|{
try|try
block|{
for|for
control|(
name|long
name|i
init|=
name|startIndex
init|;
name|i
operator|<
name|endIndex
condition|;
name|i
operator|++
control|)
block|{
name|Exchange
name|exchange
init|=
name|endpoint
operator|.
name|createExchange
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|getProcessor
argument_list|()
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
try|try
block|{
name|long
name|delay
init|=
name|endpoint
operator|.
name|getProduceDelay
argument_list|()
decl_stmt|;
if|if
condition|(
name|delay
operator|>
literal|0
condition|)
block|{
name|Thread
operator|.
name|sleep
argument_list|(
name|delay
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|interrupt
argument_list|()
expr_stmt|;
break|break;
block|}
if|if
condition|(
name|reporter
operator|!=
literal|null
condition|)
block|{
name|reporter
operator|.
name|process
argument_list|(
name|exchange
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
name|handleException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|createReporter ()
specifier|protected
name|ThroughputLogger
name|createReporter
parameter_list|()
block|{
name|ThroughputLogger
name|answer
init|=
operator|new
name|ThroughputLogger
argument_list|(
name|endpoint
operator|.
name|getEndpointUri
argument_list|()
argument_list|,
operator|(
name|int
operator|)
name|endpoint
operator|.
name|getDataSet
argument_list|()
operator|.
name|getReportCount
argument_list|()
argument_list|)
decl_stmt|;
name|answer
operator|.
name|setAction
argument_list|(
literal|"Sent"
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
block|}
end_class

end_unit

