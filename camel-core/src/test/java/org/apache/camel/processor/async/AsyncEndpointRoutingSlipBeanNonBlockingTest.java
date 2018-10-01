begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor.async
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
operator|.
name|async
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
name|ContextTestSupport
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
name|RoutingSlip
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
name|JndiRegistry
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
name|SendProcessor
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
name|junit
operator|.
name|Assert
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|AsyncEndpointRoutingSlipBeanNonBlockingTest
specifier|public
class|class
name|AsyncEndpointRoutingSlipBeanNonBlockingTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|innerCallback
specifier|private
name|AsyncCallback
name|innerCallback
decl_stmt|;
DECL|field|innerExchange
specifier|private
name|Exchange
name|innerExchange
decl_stmt|;
annotation|@
name|Override
DECL|method|createRegistry ()
specifier|protected
name|JndiRegistry
name|createRegistry
parameter_list|()
throws|throws
name|Exception
block|{
name|JndiRegistry
name|jndi
init|=
name|super
operator|.
name|createRegistry
argument_list|()
decl_stmt|;
name|jndi
operator|.
name|bind
argument_list|(
literal|"myBean"
argument_list|,
operator|new
name|MyRoutingSlipBean
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|jndi
return|;
block|}
annotation|@
name|Test
DECL|method|testAsyncEndpointDontBlock ()
specifier|public
name|void
name|testAsyncEndpointDontBlock
parameter_list|()
throws|throws
name|Exception
block|{
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Bye Camel"
argument_list|)
expr_stmt|;
name|Endpoint
name|startEndpoint
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"direct:start"
argument_list|)
decl_stmt|;
name|SendProcessor
name|asyncSender
init|=
operator|new
name|SendProcessor
argument_list|(
name|startEndpoint
argument_list|)
decl_stmt|;
name|ServiceHelper
operator|.
name|startService
argument_list|(
name|asyncSender
argument_list|)
expr_stmt|;
name|ExecutorService
name|executorService
init|=
name|context
operator|.
name|getExecutorServiceManager
argument_list|()
operator|.
name|newSingleThreadExecutor
argument_list|(
name|this
argument_list|,
literal|"test"
argument_list|)
decl_stmt|;
try|try
block|{
name|Future
argument_list|<
name|Boolean
argument_list|>
name|asyncFuture
init|=
name|executorService
operator|.
name|submit
argument_list|(
operator|new
name|ExchangeSubmitter
argument_list|(
name|startEndpoint
argument_list|,
name|asyncSender
argument_list|)
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertFalse
argument_list|(
name|asyncFuture
operator|.
name|get
argument_list|(
literal|5
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
argument_list|)
expr_stmt|;
name|innerExchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"Bye Camel"
argument_list|)
expr_stmt|;
name|innerCallback
operator|.
name|done
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
name|executorService
operator|.
name|shutdown
argument_list|()
expr_stmt|;
name|ServiceHelper
operator|.
name|stopAndShutdownService
argument_list|(
name|asyncSender
argument_list|)
expr_stmt|;
block|}
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
name|context
operator|.
name|addComponent
argument_list|(
literal|"async"
argument_list|,
operator|new
name|MyAsyncComponent
argument_list|()
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|to
argument_list|(
literal|"bean:myBean"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:asyncRoute"
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|MyAsyncProcessor
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|class|MyRoutingSlipBean
specifier|public
specifier|static
class|class
name|MyRoutingSlipBean
block|{
annotation|@
name|RoutingSlip
DECL|method|doSomething ()
specifier|public
name|String
name|doSomething
parameter_list|()
block|{
return|return
literal|"direct:asyncRoute,mock:result"
return|;
block|}
block|}
DECL|class|ExchangeSubmitter
specifier|private
specifier|static
class|class
name|ExchangeSubmitter
implements|implements
name|Callable
argument_list|<
name|Boolean
argument_list|>
block|{
DECL|field|startEndpoint
specifier|private
specifier|final
name|Endpoint
name|startEndpoint
decl_stmt|;
DECL|field|asyncSender
specifier|private
specifier|final
name|SendProcessor
name|asyncSender
decl_stmt|;
DECL|method|ExchangeSubmitter (Endpoint startEndpoint, SendProcessor asyncSender)
name|ExchangeSubmitter
parameter_list|(
name|Endpoint
name|startEndpoint
parameter_list|,
name|SendProcessor
name|asyncSender
parameter_list|)
block|{
name|this
operator|.
name|startEndpoint
operator|=
name|startEndpoint
expr_stmt|;
name|this
operator|.
name|asyncSender
operator|=
name|asyncSender
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|call ()
specifier|public
name|Boolean
name|call
parameter_list|()
throws|throws
name|Exception
block|{
name|Exchange
name|exchange
init|=
name|startEndpoint
operator|.
name|createExchange
argument_list|(
name|ExchangePattern
operator|.
name|InOut
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"Hello Camel"
argument_list|)
expr_stmt|;
return|return
name|asyncSender
operator|.
name|process
argument_list|(
name|exchange
argument_list|,
operator|new
name|AsyncCallback
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|done
parameter_list|(
name|boolean
name|doneSync
parameter_list|)
block|{
name|Assert
operator|.
name|assertFalse
argument_list|(
name|doneSync
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
return|;
block|}
block|}
DECL|class|MyAsyncProcessor
specifier|private
class|class
name|MyAsyncProcessor
implements|implements
name|AsyncProcessor
block|{
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
name|innerCallback
operator|=
name|callback
expr_stmt|;
name|innerExchange
operator|=
name|exchange
expr_stmt|;
return|return
literal|false
return|;
block|}
annotation|@
name|Override
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
block|}
block|}
end_class

end_unit

