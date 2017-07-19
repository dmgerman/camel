begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.milo
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|milo
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
name|EndpointInject
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
name|Produce
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
name|ProducerTemplate
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
name|RoutesBuilder
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
name|milo
operator|.
name|server
operator|.
name|MiloServerComponent
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
name|junit
operator|.
name|Test
import|;
end_import

begin_comment
comment|/**  * Testing the monitor functionality for item  */
end_comment

begin_class
DECL|class|MonitorItemTest
specifier|public
class|class
name|MonitorItemTest
extends|extends
name|AbstractMiloServerTest
block|{
DECL|field|DIRECT_START_1
specifier|private
specifier|static
specifier|final
name|String
name|DIRECT_START_1
init|=
literal|"direct:start1"
decl_stmt|;
DECL|field|MILO_SERVER_ITEM_1
specifier|private
specifier|static
specifier|final
name|String
name|MILO_SERVER_ITEM_1
init|=
literal|"milo-server:myitem1"
decl_stmt|;
DECL|field|MILO_CLIENT_ITEM_C1_1
specifier|private
specifier|static
specifier|final
name|String
name|MILO_CLIENT_ITEM_C1_1
init|=
literal|"milo-client:tcp://foo:bar@localhost:@@port@@?node="
operator|+
name|NodeIds
operator|.
name|nodeValue
argument_list|(
name|MiloServerComponent
operator|.
name|DEFAULT_NAMESPACE_URI
argument_list|,
literal|"items-myitem1"
argument_list|)
decl_stmt|;
DECL|field|MOCK_TEST_1
specifier|private
specifier|static
specifier|final
name|String
name|MOCK_TEST_1
init|=
literal|"mock:test1"
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
name|MOCK_TEST_1
argument_list|)
DECL|field|test1Endpoint
specifier|protected
name|MockEndpoint
name|test1Endpoint
decl_stmt|;
annotation|@
name|Produce
argument_list|(
name|uri
operator|=
name|DIRECT_START_1
argument_list|)
DECL|field|producer1
specifier|protected
name|ProducerTemplate
name|producer1
decl_stmt|;
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RoutesBuilder
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
name|DIRECT_START_1
argument_list|)
operator|.
name|to
argument_list|(
name|MILO_SERVER_ITEM_1
argument_list|)
expr_stmt|;
name|from
argument_list|(
name|resolve
argument_list|(
name|MILO_CLIENT_ITEM_C1_1
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
name|MOCK_TEST_1
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
comment|/**      * Monitor multiple events      */
annotation|@
name|Test
DECL|method|testMonitorItem1 ()
specifier|public
name|void
name|testMonitorItem1
parameter_list|()
throws|throws
name|Exception
block|{
comment|/*          * we will wait 2 * 1_000 milliseconds between server updates since the          * default server update rate is 1_000 milliseconds          */
comment|// set server values
name|this
operator|.
name|producer1
operator|.
name|sendBody
argument_list|(
literal|"Foo"
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|2_000
argument_list|)
expr_stmt|;
name|this
operator|.
name|producer1
operator|.
name|sendBody
argument_list|(
literal|"Bar"
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|2_000
argument_list|)
expr_stmt|;
name|this
operator|.
name|producer1
operator|.
name|sendBody
argument_list|(
literal|"Baz"
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|2_000
argument_list|)
expr_stmt|;
comment|// item 1 ... only this one receives
name|this
operator|.
name|test1Endpoint
operator|.
name|setExpectedCount
argument_list|(
literal|3
argument_list|)
expr_stmt|;
comment|// tests
name|testBody
argument_list|(
name|this
operator|.
name|test1Endpoint
operator|.
name|message
argument_list|(
literal|0
argument_list|)
argument_list|,
name|assertGoodValue
argument_list|(
literal|"Foo"
argument_list|)
argument_list|)
expr_stmt|;
name|testBody
argument_list|(
name|this
operator|.
name|test1Endpoint
operator|.
name|message
argument_list|(
literal|1
argument_list|)
argument_list|,
name|assertGoodValue
argument_list|(
literal|"Bar"
argument_list|)
argument_list|)
expr_stmt|;
name|testBody
argument_list|(
name|this
operator|.
name|test1Endpoint
operator|.
name|message
argument_list|(
literal|2
argument_list|)
argument_list|,
name|assertGoodValue
argument_list|(
literal|"Baz"
argument_list|)
argument_list|)
expr_stmt|;
comment|// assert
name|this
operator|.
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

