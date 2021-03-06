begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|support
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
name|support
operator|.
name|DefaultEndpoint
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

begin_comment
comment|/**  * Test that endpoints are only shutdown once when CamelContext is stopping.  */
end_comment

begin_class
DECL|class|EndpointShutdownOnceTest
specifier|public
class|class
name|EndpointShutdownOnceTest
extends|extends
name|Assert
block|{
annotation|@
name|Test
DECL|method|testEndpointShutdown ()
specifier|public
name|void
name|testEndpointShutdown
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContext
name|context
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
name|context
operator|.
name|addComponent
argument_list|(
literal|"my"
argument_list|,
operator|new
name|MyComponent
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|MyEndpoint
name|my
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"my:foo"
argument_list|,
name|MyEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Should be started"
argument_list|,
name|my
operator|.
name|getStatus
argument_list|()
operator|.
name|isStarted
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
name|assertFalse
argument_list|(
literal|"Should not be started"
argument_list|,
name|my
operator|.
name|getStatus
argument_list|()
operator|.
name|isStarted
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Should be stopped"
argument_list|,
name|my
operator|.
name|getStatus
argument_list|()
operator|.
name|isStopped
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Should only shutdown once"
argument_list|,
literal|1
argument_list|,
name|my
operator|.
name|getInvoked
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|class|MyComponent
specifier|private
specifier|static
specifier|final
class|class
name|MyComponent
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
name|MyEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|)
return|;
block|}
block|}
DECL|class|MyEndpoint
specifier|private
specifier|static
specifier|final
class|class
name|MyEndpoint
extends|extends
name|DefaultEndpoint
block|{
DECL|field|invoked
specifier|private
specifier|volatile
name|int
name|invoked
decl_stmt|;
DECL|method|MyEndpoint (String endpointUri, Component component)
specifier|private
name|MyEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|Component
name|component
parameter_list|)
block|{
name|super
argument_list|(
name|endpointUri
argument_list|,
name|component
argument_list|)
expr_stmt|;
block|}
DECL|method|getInvoked ()
specifier|public
name|int
name|getInvoked
parameter_list|()
block|{
return|return
name|invoked
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
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|doShutdown ()
specifier|protected
name|void
name|doShutdown
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|doShutdown
argument_list|()
expr_stmt|;
name|invoked
operator|++
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

