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
name|List
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
name|component
operator|.
name|seda
operator|.
name|SedaEndpoint
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
name|FromDefinition
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
name|RouteDefinition
import|;
end_import

begin_comment
comment|/**  * This test stops a route, mutates it then restarts it  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|StartAndStopRoutesTest
specifier|public
class|class
name|StartAndStopRoutesTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|endpointA
specifier|protected
name|SedaEndpoint
name|endpointA
decl_stmt|;
DECL|field|endpointB
specifier|protected
name|SedaEndpoint
name|endpointB
decl_stmt|;
DECL|field|endpointC
specifier|protected
name|SedaEndpoint
name|endpointC
decl_stmt|;
DECL|field|expectedBody
specifier|protected
name|Object
name|expectedBody
init|=
literal|"<hello>world!</hello>"
decl_stmt|;
DECL|method|testStartRouteThenStopMutateAndStartRouteAgain ()
specifier|public
name|void
name|testStartRouteThenStopMutateAndStartRouteAgain
parameter_list|()
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|RouteDefinition
argument_list|>
name|routes
init|=
name|context
operator|.
name|getRouteDefinitions
argument_list|()
decl_stmt|;
name|assertCollectionSize
argument_list|(
literal|"Route"
argument_list|,
name|routes
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|RouteDefinition
name|route
init|=
name|routes
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|endpointA
operator|=
name|getMandatoryEndpoint
argument_list|(
literal|"seda:test.a"
argument_list|,
name|SedaEndpoint
operator|.
name|class
argument_list|)
expr_stmt|;
name|endpointB
operator|=
name|getMandatoryEndpoint
argument_list|(
literal|"seda:test.b"
argument_list|,
name|SedaEndpoint
operator|.
name|class
argument_list|)
expr_stmt|;
name|endpointC
operator|=
name|getMandatoryEndpoint
argument_list|(
literal|"seda:test.C"
argument_list|,
name|SedaEndpoint
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertProducerAndConsumerCounts
argument_list|(
literal|"endpointA"
argument_list|,
name|endpointA
argument_list|,
literal|0
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|assertProducerAndConsumerCounts
argument_list|(
literal|"endpointB"
argument_list|,
name|endpointB
argument_list|,
literal|1
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|assertProducerAndConsumerCounts
argument_list|(
literal|"endpointC"
argument_list|,
name|endpointC
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|context
operator|.
name|stopRoute
argument_list|(
name|route
argument_list|)
expr_stmt|;
name|assertProducerAndConsumerCounts
argument_list|(
literal|"endpointA"
argument_list|,
name|endpointA
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|assertProducerAndConsumerCounts
argument_list|(
literal|"endpointB"
argument_list|,
name|endpointB
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|assertProducerAndConsumerCounts
argument_list|(
literal|"endpointC"
argument_list|,
name|endpointC
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|)
expr_stmt|;
comment|// lets mutate the route...
name|FromDefinition
name|fromType
init|=
name|assertOneElement
argument_list|(
name|route
operator|.
name|getInputs
argument_list|()
argument_list|)
decl_stmt|;
name|fromType
operator|.
name|setUri
argument_list|(
literal|"seda:test.C"
argument_list|)
expr_stmt|;
name|context
operator|.
name|startRoute
argument_list|(
name|route
argument_list|)
expr_stmt|;
name|assertProducerAndConsumerCounts
argument_list|(
literal|"endpointA"
argument_list|,
name|endpointA
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|assertProducerAndConsumerCounts
argument_list|(
literal|"endpointB"
argument_list|,
name|endpointB
argument_list|,
literal|1
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|assertProducerAndConsumerCounts
argument_list|(
literal|"endpointC"
argument_list|,
name|endpointC
argument_list|,
literal|0
argument_list|,
literal|1
argument_list|)
expr_stmt|;
comment|// now lets check it works
name|MockEndpoint
name|results
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:results"
argument_list|)
decl_stmt|;
name|results
operator|.
name|expectedBodiesReceived
argument_list|(
name|expectedBody
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
name|endpointC
argument_list|,
name|expectedBody
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|method|assertProducerAndConsumerCounts (String name, SedaEndpoint endpoint, int producerCount, int consumerCount)
specifier|protected
name|void
name|assertProducerAndConsumerCounts
parameter_list|(
name|String
name|name
parameter_list|,
name|SedaEndpoint
name|endpoint
parameter_list|,
name|int
name|producerCount
parameter_list|,
name|int
name|consumerCount
parameter_list|)
block|{
name|assertCollectionSize
argument_list|(
literal|"Producers for "
operator|+
name|name
argument_list|,
name|endpoint
operator|.
name|getProducers
argument_list|()
argument_list|,
name|producerCount
argument_list|)
expr_stmt|;
name|assertCollectionSize
argument_list|(
literal|"Consumers for "
operator|+
name|name
argument_list|,
name|endpoint
operator|.
name|getConsumers
argument_list|()
argument_list|,
name|consumerCount
argument_list|)
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
literal|"seda:test.a"
argument_list|)
operator|.
name|to
argument_list|(
literal|"seda:test.b"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:results"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

