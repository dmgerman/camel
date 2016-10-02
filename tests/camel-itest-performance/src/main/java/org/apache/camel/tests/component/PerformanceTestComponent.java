begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.tests.component
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|tests
operator|.
name|component
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
name|Callable
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
name|CompletionService
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
name|ExecutorCompletionService
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
name|Component
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
name|Consumer
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
name|Producer
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
name|DefaultComponent
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
name|impl
operator|.
name|DefaultEndpoint
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
name|DefaultProducer
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
name|ExchangeHelper
import|;
end_import

begin_class
DECL|class|PerformanceTestComponent
specifier|public
class|class
name|PerformanceTestComponent
extends|extends
name|DefaultComponent
block|{
DECL|field|HEADER_THREADS
specifier|public
specifier|static
specifier|final
name|String
name|HEADER_THREADS
init|=
literal|"CamelPerfThreads"
decl_stmt|;
DECL|field|HEADER_ITERATIONS
specifier|public
specifier|static
specifier|final
name|String
name|HEADER_ITERATIONS
init|=
literal|"CamelPerfIterations"
decl_stmt|;
DECL|field|DEFAULT_THREADS
specifier|private
specifier|static
specifier|final
name|int
name|DEFAULT_THREADS
init|=
literal|8
decl_stmt|;
DECL|field|DEFAULT_ITERATIONS
specifier|private
specifier|static
specifier|final
name|int
name|DEFAULT_ITERATIONS
init|=
literal|100
decl_stmt|;
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
name|Endpoint
name|endpoint
init|=
operator|new
name|PerformanceTestEndpoint
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
return|return
name|endpoint
return|;
block|}
DECL|method|getHeaderValue (Exchange exchange, String header)
specifier|public
specifier|static
name|int
name|getHeaderValue
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|String
name|header
parameter_list|)
block|{
name|Integer
name|value
init|=
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|Integer
operator|.
name|class
argument_list|,
name|exchange
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|header
argument_list|)
argument_list|)
decl_stmt|;
return|return
name|value
operator|!=
literal|null
condition|?
name|value
else|:
name|header
operator|.
name|equals
argument_list|(
name|HEADER_THREADS
argument_list|)
condition|?
name|DEFAULT_THREADS
else|:
name|header
operator|.
name|equals
argument_list|(
name|HEADER_ITERATIONS
argument_list|)
condition|?
name|DEFAULT_ITERATIONS
else|:
literal|0
return|;
block|}
DECL|class|PerformanceTestEndpoint
specifier|private
specifier|static
specifier|final
class|class
name|PerformanceTestEndpoint
extends|extends
name|DefaultEndpoint
block|{
DECL|field|consumer
specifier|private
name|PerformanceTestConsumer
name|consumer
decl_stmt|;
DECL|method|PerformanceTestEndpoint (String uri, Component component)
specifier|protected
name|PerformanceTestEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|Component
name|component
parameter_list|)
block|{
name|super
argument_list|(
name|uri
argument_list|,
name|component
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createConsumer (Processor processor)
specifier|public
name|Consumer
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
synchronized|synchronized
init|(
name|this
init|)
block|{
if|if
condition|(
name|consumer
operator|!=
literal|null
operator|&&
name|processor
operator|!=
name|consumer
operator|.
name|getProcessor
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|Exception
argument_list|(
literal|"PerformanceTestEndpoint doesn not support multiple consumers per Endpoint"
argument_list|)
throw|;
block|}
name|consumer
operator|=
operator|new
name|PerformanceTestConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|)
expr_stmt|;
block|}
return|return
name|consumer
return|;
block|}
annotation|@
name|Override
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|PerformanceTestProducer
argument_list|(
name|this
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
DECL|method|getConsumer ()
specifier|public
name|Consumer
name|getConsumer
parameter_list|()
block|{
return|return
name|consumer
return|;
block|}
block|}
DECL|class|PerformanceTestConsumer
specifier|private
specifier|static
specifier|final
class|class
name|PerformanceTestConsumer
extends|extends
name|DefaultConsumer
block|{
DECL|method|PerformanceTestConsumer (Endpoint endpoint, Processor processor)
specifier|protected
name|PerformanceTestConsumer
parameter_list|(
name|Endpoint
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
block|}
block|}
DECL|class|PerformanceTestProducer
specifier|private
specifier|static
specifier|final
class|class
name|PerformanceTestProducer
extends|extends
name|DefaultProducer
implements|implements
name|AsyncProcessor
block|{
DECL|method|PerformanceTestProducer (Endpoint endpoint)
specifier|protected
name|PerformanceTestProducer
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
DECL|method|process (final Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
specifier|final
name|int
name|count
init|=
name|getHeaderValue
argument_list|(
name|exchange
argument_list|,
name|HEADER_ITERATIONS
argument_list|)
decl_stmt|;
specifier|final
name|int
name|threads
init|=
name|getHeaderValue
argument_list|(
name|exchange
argument_list|,
name|HEADER_THREADS
argument_list|)
decl_stmt|;
name|PerformanceTestEndpoint
name|endpoint
init|=
operator|(
name|PerformanceTestEndpoint
operator|)
name|getEndpoint
argument_list|()
decl_stmt|;
if|if
condition|(
name|endpoint
operator|!=
literal|null
condition|)
block|{
specifier|final
name|DefaultConsumer
name|consumer
init|=
operator|(
name|DefaultConsumer
operator|)
name|endpoint
operator|.
name|getConsumer
argument_list|()
decl_stmt|;
name|ExecutorService
name|executor
init|=
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getExecutorServiceManager
argument_list|()
operator|.
name|newFixedThreadPool
argument_list|(
name|this
argument_list|,
literal|"perf"
argument_list|,
name|threads
argument_list|)
decl_stmt|;
name|CompletionService
argument_list|<
name|Exchange
argument_list|>
name|tasks
init|=
operator|new
name|ExecutorCompletionService
argument_list|<
name|Exchange
argument_list|>
argument_list|(
name|executor
argument_list|)
decl_stmt|;
comment|// StopWatch watch = new StopWatch();  // if we want to clock how long it takes
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|count
condition|;
name|i
operator|++
control|)
block|{
name|tasks
operator|.
name|submit
argument_list|(
operator|new
name|Callable
argument_list|<
name|Exchange
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Exchange
name|call
parameter_list|()
throws|throws
name|Exception
block|{
name|Exchange
name|exch
init|=
name|ExchangeHelper
operator|.
name|createCopy
argument_list|(
name|exchange
argument_list|,
literal|false
argument_list|)
decl_stmt|;
try|try
block|{
name|consumer
operator|.
name|getProcessor
argument_list|()
operator|.
name|process
argument_list|(
name|exch
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
specifier|final
name|Exception
name|e
parameter_list|)
block|{
name|exch
operator|.
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
return|return
name|exch
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|count
condition|;
name|i
operator|++
control|)
block|{
comment|// Future<Exchange> result = tasks.take();
name|tasks
operator|.
name|take
argument_list|()
expr_stmt|;
comment|// wait for all exchanges to complete
block|}
block|}
block|}
annotation|@
name|Override
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
try|try
block|{
name|this
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
name|exchange
operator|.
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
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
block|}
block|}
end_class

end_unit

