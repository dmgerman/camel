begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel
package|package
name|org
operator|.
name|apache
operator|.
name|camel
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
name|Executors
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
name|builder
operator|.
name|RouteBuilder
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
name|support
operator|.
name|SynchronizationAdapter
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
DECL|class|UnitOfWorkSyncProcessTest
specifier|public
class|class
name|UnitOfWorkSyncProcessTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|consumerThread
specifier|private
specifier|static
name|String
name|consumerThread
decl_stmt|;
DECL|field|afterThread
specifier|private
specifier|static
name|String
name|afterThread
decl_stmt|;
DECL|field|taskThread
specifier|private
specifier|static
name|String
name|taskThread
decl_stmt|;
DECL|field|doneThread
specifier|private
specifier|static
name|String
name|doneThread
decl_stmt|;
DECL|field|executorService
specifier|private
name|ExecutorService
name|executorService
init|=
name|Executors
operator|.
name|newSingleThreadExecutor
argument_list|()
decl_stmt|;
DECL|method|testUnitOfWorkSync ()
specifier|public
name|void
name|testUnitOfWorkSync
parameter_list|()
throws|throws
name|Exception
block|{
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
comment|// should be same thread
name|assertEquals
argument_list|(
name|taskThread
argument_list|,
name|afterThread
argument_list|)
expr_stmt|;
comment|// should not be same
name|assertNotSame
argument_list|(
name|doneThread
argument_list|,
name|afterThread
argument_list|)
expr_stmt|;
name|assertNotSame
argument_list|(
name|doneThread
argument_list|,
name|consumerThread
argument_list|)
expr_stmt|;
comment|// should be same thread
name|assertEquals
argument_list|(
name|consumerThread
argument_list|,
name|doneThread
argument_list|)
expr_stmt|;
name|executorService
operator|.
name|shutdownNow
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
operator|new
name|MyEndpoint
argument_list|()
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|AsyncProcessor
argument_list|()
block|{
annotation|@
name|Override
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
name|executorService
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
name|taskThread
operator|=
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getName
argument_list|()
expr_stmt|;
try|try
block|{
name|Thread
operator|.
name|sleep
argument_list|(
literal|100
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
comment|// ignore
block|}
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"foo"
argument_list|,
literal|123
argument_list|)
expr_stmt|;
name|callback
operator|.
name|done
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
annotation|@
name|Override
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
comment|// noop
block|}
block|}
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|Processor
argument_list|()
block|{
annotation|@
name|Override
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
name|afterThread
operator|=
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getName
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|class|MyEndpoint
specifier|private
specifier|final
class|class
name|MyEndpoint
extends|extends
name|DefaultEndpoint
block|{
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
comment|// not supported
return|return
literal|null
return|;
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
return|return
operator|new
name|MyConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createEndpointUri ()
specifier|protected
name|String
name|createEndpointUri
parameter_list|()
block|{
return|return
literal|"myEndpoint://foo"
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
block|}
DECL|class|MyConsumer
specifier|private
specifier|final
class|class
name|MyConsumer
implements|implements
name|Consumer
block|{
DECL|field|processor
specifier|private
name|Processor
name|processor
decl_stmt|;
DECL|field|endpoint
specifier|private
name|Endpoint
name|endpoint
decl_stmt|;
DECL|method|MyConsumer (Endpoint endpoint, Processor processor)
specifier|private
name|MyConsumer
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|Processor
name|processor
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
block|}
annotation|@
name|Override
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
annotation|@
name|Override
DECL|method|start ()
specifier|public
name|void
name|start
parameter_list|()
throws|throws
name|Exception
block|{
name|consumerThread
operator|=
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getName
argument_list|()
expr_stmt|;
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|UNIT_OF_WORK_PROCESS_SYNC
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|addOnCompletion
argument_list|(
operator|new
name|SynchronizationAdapter
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onDone
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|doneThread
operator|=
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getName
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
comment|// just fire the exchange when started
name|processor
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|stop ()
specifier|public
name|void
name|stop
parameter_list|()
throws|throws
name|Exception
block|{
comment|// noop
block|}
block|}
block|}
end_class

end_unit

