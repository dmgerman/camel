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
name|java
operator|.
name|util
operator|.
name|Map
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
name|junit
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|ProducerCacheNonSingletonTest
specifier|public
class|class
name|ProducerCacheNonSingletonTest
extends|extends
name|ContextTestSupport
block|{
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
annotation|@
name|Test
DECL|method|testNonSingleton ()
specifier|public
name|void
name|testNonSingleton
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|addComponent
argument_list|(
literal|"dummy"
argument_list|,
operator|new
name|MyDummyComponent
argument_list|()
argument_list|)
expr_stmt|;
name|ProducerCache
name|cache
init|=
operator|new
name|ProducerCache
argument_list|(
name|this
argument_list|,
name|context
argument_list|,
operator|-
literal|1
argument_list|)
decl_stmt|;
name|cache
operator|.
name|start
argument_list|()
expr_stmt|;
name|Endpoint
name|endpoint
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"dummy:foo"
argument_list|)
decl_stmt|;
name|DefaultAsyncProducer
name|producer
init|=
operator|(
name|DefaultAsyncProducer
operator|)
name|cache
operator|.
name|acquireProducer
argument_list|(
name|endpoint
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|producer
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Should be started"
argument_list|,
name|producer
operator|.
name|getStatus
argument_list|()
operator|.
name|isStarted
argument_list|()
argument_list|)
expr_stmt|;
name|Object
name|found
init|=
name|context
operator|.
name|hasService
argument_list|(
name|MyDummyProducer
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
literal|"Should not store producer on CamelContext"
argument_list|,
name|found
argument_list|)
expr_stmt|;
name|cache
operator|.
name|releaseProducer
argument_list|(
name|endpoint
argument_list|,
name|producer
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Should be stopped"
argument_list|,
name|producer
operator|.
name|getStatus
argument_list|()
operator|.
name|isStopped
argument_list|()
argument_list|)
expr_stmt|;
name|cache
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
DECL|class|MyDummyComponent
specifier|public
class|class
name|MyDummyComponent
extends|extends
name|DefaultComponent
block|{
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
return|return
operator|new
name|MyDummyEndpoint
argument_list|()
return|;
block|}
block|}
DECL|class|MyDummyEndpoint
specifier|public
class|class
name|MyDummyEndpoint
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
return|return
operator|new
name|MyDummyProducer
argument_list|(
name|this
argument_list|)
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
literal|null
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
literal|false
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
literal|"dummy://foo"
return|;
block|}
block|}
DECL|class|MyDummyProducer
specifier|private
class|class
name|MyDummyProducer
extends|extends
name|DefaultAsyncProducer
block|{
DECL|method|MyDummyProducer (Endpoint endpoint)
specifier|public
name|MyDummyProducer
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
return|return
literal|false
return|;
block|}
block|}
block|}
end_class

end_unit

