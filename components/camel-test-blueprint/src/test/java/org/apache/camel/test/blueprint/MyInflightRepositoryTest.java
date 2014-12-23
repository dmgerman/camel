begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.test.blueprint
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|test
operator|.
name|blueprint
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
name|spi
operator|.
name|InflightRepository
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
DECL|class|MyInflightRepositoryTest
specifier|public
class|class
name|MyInflightRepositoryTest
extends|extends
name|CamelBlueprintTestSupport
block|{
annotation|@
name|Override
DECL|method|getBlueprintDescriptor ()
specifier|protected
name|String
name|getBlueprintDescriptor
parameter_list|()
block|{
return|return
literal|"org/apache/camel/test/blueprint/MyInflightRepositoryTest.xml"
return|;
block|}
annotation|@
name|Test
DECL|method|testMyInflightRepository ()
specifier|public
name|void
name|testMyInflightRepository
parameter_list|()
throws|throws
name|Exception
block|{
name|InflightRepository
name|inflightRepository
init|=
name|context
argument_list|()
operator|.
name|getInflightRepository
argument_list|()
decl_stmt|;
comment|// send out the message into the camel route
name|assertTrue
argument_list|(
literal|"Get a wrong instance of inflightRepsitory"
argument_list|,
name|inflightRepository
operator|instanceof
name|MyInflightRepository
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"There is no inflight exchange"
argument_list|,
literal|0
argument_list|,
name|inflightRepository
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:before"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Hello Camel"
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:after"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Bye Camel"
argument_list|)
expr_stmt|;
name|MyInflightRepository
name|myInflightExchangeRepository
init|=
operator|(
name|MyInflightRepository
operator|)
name|inflightRepository
decl_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Hello Camel"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"We should have no inflight exchange there "
argument_list|,
literal|0
argument_list|,
name|myInflightExchangeRepository
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Get a wrong size of timeout exchange"
argument_list|,
literal|1
argument_list|,
name|myInflightExchangeRepository
operator|.
name|getTimeoutExchange
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

