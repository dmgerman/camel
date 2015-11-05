begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
package|;
end_package

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
name|ServicePoolAware
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
name|component
operator|.
name|mock
operator|.
name|MockEndpoint
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
name|ServicePool
import|;
end_import

begin_comment
comment|/**  * Unit test for a custom ServicePool for producer.  *  * @version   */
end_comment

begin_class
DECL|class|CustomProducerServicePoolTest
specifier|public
class|class
name|CustomProducerServicePoolTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|counter
specifier|private
specifier|static
name|int
name|counter
decl_stmt|;
DECL|class|MyEndpoint
specifier|private
specifier|static
specifier|final
class|class
name|MyEndpoint
extends|extends
name|DefaultEndpoint
block|{
DECL|method|MyEndpoint (String endpointUri, CamelContext camelContext)
specifier|private
name|MyEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|setCamelContext
argument_list|(
name|camelContext
argument_list|)
expr_stmt|;
name|setEndpointUri
argument_list|(
name|endpointUri
argument_list|)
expr_stmt|;
block|}
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
name|MyProducer
argument_list|(
name|this
argument_list|)
return|;
block|}
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
literal|null
return|;
block|}
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
annotation|@
name|Override
DECL|method|createEndpointUri ()
specifier|protected
name|String
name|createEndpointUri
parameter_list|()
block|{
return|return
literal|"my"
return|;
block|}
block|}
DECL|class|MyProducer
specifier|private
specifier|static
specifier|final
class|class
name|MyProducer
extends|extends
name|DefaultProducer
implements|implements
name|ServicePoolAware
block|{
DECL|method|MyProducer (Endpoint endpoint)
specifier|public
name|MyProducer
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
name|counter
operator|++
expr_stmt|;
block|}
block|}
DECL|class|MyPool
specifier|private
specifier|static
class|class
name|MyPool
implements|implements
name|ServicePool
argument_list|<
name|Endpoint
argument_list|,
name|Producer
argument_list|>
block|{
DECL|field|producer
specifier|private
name|Producer
name|producer
decl_stmt|;
DECL|method|setCapacity (int capacity)
specifier|public
name|void
name|setCapacity
parameter_list|(
name|int
name|capacity
parameter_list|)
block|{         }
DECL|method|getCapacity ()
specifier|public
name|int
name|getCapacity
parameter_list|()
block|{
return|return
literal|0
return|;
block|}
DECL|method|addAndAcquire (Endpoint endpoint, Producer producer)
specifier|public
name|Producer
name|addAndAcquire
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|Producer
name|producer
parameter_list|)
block|{
if|if
condition|(
name|endpoint
operator|instanceof
name|MyEndpoint
condition|)
block|{
return|return
name|producer
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
DECL|method|acquire (Endpoint endpoint)
specifier|public
name|Producer
name|acquire
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|)
block|{
if|if
condition|(
name|endpoint
operator|instanceof
name|MyEndpoint
condition|)
block|{
name|Producer
name|answer
init|=
name|producer
decl_stmt|;
name|producer
operator|=
literal|null
expr_stmt|;
return|return
name|answer
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
DECL|method|release (Endpoint endpoint, Producer producer)
specifier|public
name|void
name|release
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|Producer
name|producer
parameter_list|)
block|{
name|this
operator|.
name|producer
operator|=
name|producer
expr_stmt|;
block|}
DECL|method|start ()
specifier|public
name|void
name|start
parameter_list|()
throws|throws
name|Exception
block|{         }
DECL|method|size ()
specifier|public
name|int
name|size
parameter_list|()
block|{
return|return
name|producer
operator|!=
literal|null
condition|?
literal|1
else|:
literal|0
return|;
block|}
DECL|method|purge ()
specifier|public
name|void
name|purge
parameter_list|()
block|{
name|producer
operator|=
literal|null
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|isUseRouteBuilder ()
specifier|public
name|boolean
name|isUseRouteBuilder
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
DECL|method|testCustomProducerServicePool ()
specifier|public
name|void
name|testCustomProducerServicePool
parameter_list|()
throws|throws
name|Exception
block|{
name|MyPool
name|pool
init|=
operator|new
name|MyPool
argument_list|()
decl_stmt|;
name|pool
operator|.
name|start
argument_list|()
expr_stmt|;
name|context
operator|.
name|setProducerServicePool
argument_list|(
name|pool
argument_list|)
expr_stmt|;
name|context
operator|.
name|addEndpoint
argument_list|(
literal|"my"
argument_list|,
operator|new
name|MyEndpoint
argument_list|(
literal|"my"
argument_list|,
name|context
argument_list|)
argument_list|)
expr_stmt|;
name|Endpoint
name|endpoint
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"my"
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|pool
operator|.
name|acquire
argument_list|(
name|endpoint
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|pool
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Producer
name|producer
init|=
operator|new
name|MyProducer
argument_list|(
name|endpoint
argument_list|)
decl_stmt|;
name|producer
operator|=
name|pool
operator|.
name|addAndAcquire
argument_list|(
name|endpoint
argument_list|,
name|producer
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|pool
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|pool
operator|.
name|release
argument_list|(
name|endpoint
argument_list|,
name|producer
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|pool
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|producer
operator|=
name|pool
operator|.
name|acquire
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|producer
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|pool
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|pool
operator|.
name|release
argument_list|(
name|endpoint
argument_list|,
name|producer
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|pool
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|pool
operator|.
name|purge
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|pool
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertIsInstanceOf
argument_list|(
name|MyPool
operator|.
name|class
argument_list|,
name|context
operator|.
name|getProducerServicePool
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testCustomProducerServicePoolInRoute ()
specifier|public
name|void
name|testCustomProducerServicePoolInRoute
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|addEndpoint
argument_list|(
literal|"my"
argument_list|,
operator|new
name|MyEndpoint
argument_list|(
literal|"my"
argument_list|,
name|context
argument_list|)
argument_list|)
expr_stmt|;
name|MyPool
name|pool
init|=
operator|new
name|MyPool
argument_list|()
decl_stmt|;
name|pool
operator|.
name|start
argument_list|()
expr_stmt|;
name|context
operator|.
name|setProducerServicePool
argument_list|(
name|pool
argument_list|)
expr_stmt|;
name|context
operator|.
name|addRoutes
argument_list|(
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
literal|"direct:start"
argument_list|)
operator|.
name|to
argument_list|(
literal|"my"
argument_list|,
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Bye World"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|counter
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|pool
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|pool
operator|.
name|purge
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|pool
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

