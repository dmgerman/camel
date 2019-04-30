begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|eclipse
operator|.
name|milo
operator|.
name|opcua
operator|.
name|stack
operator|.
name|core
operator|.
name|types
operator|.
name|builtin
operator|.
name|Variant
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

begin_import
import|import static
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
name|NodeIds
operator|.
name|nodeValue
import|;
end_import

begin_comment
comment|/**  * Unit tests for writing from the client side  */
end_comment

begin_class
DECL|class|WriteClientTest
specifier|public
class|class
name|WriteClientTest
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
DECL|field|DIRECT_START_2
specifier|private
specifier|static
specifier|final
name|String
name|DIRECT_START_2
init|=
literal|"direct:start2"
decl_stmt|;
DECL|field|DIRECT_START_3
specifier|private
specifier|static
specifier|final
name|String
name|DIRECT_START_3
init|=
literal|"direct:start3"
decl_stmt|;
DECL|field|DIRECT_START_4
specifier|private
specifier|static
specifier|final
name|String
name|DIRECT_START_4
init|=
literal|"direct:start4"
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
DECL|field|MILO_SERVER_ITEM_2
specifier|private
specifier|static
specifier|final
name|String
name|MILO_SERVER_ITEM_2
init|=
literal|"milo-server:myitem2"
decl_stmt|;
DECL|field|MILO_CLIENT_BASE_C1
specifier|private
specifier|static
specifier|final
name|String
name|MILO_CLIENT_BASE_C1
init|=
literal|"milo-client:tcp://foo:bar@localhost:@@port@@"
decl_stmt|;
DECL|field|MILO_CLIENT_BASE_C2
specifier|private
specifier|static
specifier|final
name|String
name|MILO_CLIENT_BASE_C2
init|=
literal|"milo-client:tcp://foo2:bar2@localhost:@@port@@"
decl_stmt|;
DECL|field|MILO_CLIENT_ITEM_C1_1
specifier|private
specifier|static
specifier|final
name|String
name|MILO_CLIENT_ITEM_C1_1
init|=
name|MILO_CLIENT_BASE_C1
operator|+
literal|"?node="
operator|+
name|nodeValue
argument_list|(
name|MiloServerComponent
operator|.
name|DEFAULT_NAMESPACE_URI
argument_list|,
literal|"items-myitem1"
argument_list|)
operator|+
literal|"&overrideHost=true"
decl_stmt|;
DECL|field|MILO_CLIENT_ITEM_C1_2
specifier|private
specifier|static
specifier|final
name|String
name|MILO_CLIENT_ITEM_C1_2
init|=
name|MILO_CLIENT_BASE_C1
operator|+
literal|"?node="
operator|+
name|nodeValue
argument_list|(
name|MiloServerComponent
operator|.
name|DEFAULT_NAMESPACE_URI
argument_list|,
literal|"items-myitem2"
argument_list|)
operator|+
literal|"&overrideHost=true"
decl_stmt|;
DECL|field|MILO_CLIENT_ITEM_C2_1
specifier|private
specifier|static
specifier|final
name|String
name|MILO_CLIENT_ITEM_C2_1
init|=
name|MILO_CLIENT_BASE_C2
operator|+
literal|"?node="
operator|+
name|nodeValue
argument_list|(
name|MiloServerComponent
operator|.
name|DEFAULT_NAMESPACE_URI
argument_list|,
literal|"items-myitem1"
argument_list|)
operator|+
literal|"&overrideHost=true"
decl_stmt|;
DECL|field|MILO_CLIENT_ITEM_C2_2
specifier|private
specifier|static
specifier|final
name|String
name|MILO_CLIENT_ITEM_C2_2
init|=
name|MILO_CLIENT_BASE_C2
operator|+
literal|"?node="
operator|+
name|nodeValue
argument_list|(
name|MiloServerComponent
operator|.
name|DEFAULT_NAMESPACE_URI
argument_list|,
literal|"items-myitem2"
argument_list|)
operator|+
literal|"&overrideHost=true"
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
DECL|field|MOCK_TEST_2
specifier|private
specifier|static
specifier|final
name|String
name|MOCK_TEST_2
init|=
literal|"mock:test2"
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
name|MOCK_TEST_1
argument_list|)
DECL|field|test1Endpoint
specifier|protected
name|MockEndpoint
name|test1Endpoint
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
name|MOCK_TEST_2
argument_list|)
DECL|field|test2Endpoint
specifier|protected
name|MockEndpoint
name|test2Endpoint
decl_stmt|;
annotation|@
name|Produce
argument_list|(
name|DIRECT_START_1
argument_list|)
DECL|field|producer1
specifier|protected
name|ProducerTemplate
name|producer1
decl_stmt|;
annotation|@
name|Produce
argument_list|(
name|DIRECT_START_2
argument_list|)
DECL|field|producer2
specifier|protected
name|ProducerTemplate
name|producer2
decl_stmt|;
annotation|@
name|Produce
argument_list|(
name|DIRECT_START_3
argument_list|)
DECL|field|producer3
specifier|protected
name|ProducerTemplate
name|producer3
decl_stmt|;
annotation|@
name|Produce
argument_list|(
name|DIRECT_START_4
argument_list|)
DECL|field|producer4
specifier|protected
name|ProducerTemplate
name|producer4
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
name|MILO_SERVER_ITEM_1
argument_list|)
operator|.
name|to
argument_list|(
name|MOCK_TEST_1
argument_list|)
expr_stmt|;
name|from
argument_list|(
name|MILO_SERVER_ITEM_2
argument_list|)
operator|.
name|to
argument_list|(
name|MOCK_TEST_2
argument_list|)
expr_stmt|;
name|from
argument_list|(
name|DIRECT_START_1
argument_list|)
operator|.
name|to
argument_list|(
name|resolve
argument_list|(
name|MILO_CLIENT_ITEM_C1_1
argument_list|)
argument_list|)
expr_stmt|;
name|from
argument_list|(
name|DIRECT_START_2
argument_list|)
operator|.
name|to
argument_list|(
name|resolve
argument_list|(
name|MILO_CLIENT_ITEM_C1_2
argument_list|)
argument_list|)
expr_stmt|;
name|from
argument_list|(
name|DIRECT_START_3
argument_list|)
operator|.
name|to
argument_list|(
name|resolve
argument_list|(
name|MILO_CLIENT_ITEM_C2_1
argument_list|)
argument_list|)
expr_stmt|;
name|from
argument_list|(
name|DIRECT_START_4
argument_list|)
operator|.
name|to
argument_list|(
name|resolve
argument_list|(
name|MILO_CLIENT_ITEM_C2_2
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
annotation|@
name|Test
DECL|method|testWrite1 ()
specifier|public
name|void
name|testWrite1
parameter_list|()
throws|throws
name|Exception
block|{
comment|// item 1
name|this
operator|.
name|test1Endpoint
operator|.
name|setExpectedCount
argument_list|(
literal|2
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
literal|0
argument_list|)
argument_list|,
name|assertGoodValue
argument_list|(
literal|"Foo1"
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
literal|"Foo2"
argument_list|)
argument_list|)
expr_stmt|;
comment|// item 2
name|this
operator|.
name|test2Endpoint
operator|.
name|setExpectedCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
comment|// send
name|sendValue
argument_list|(
name|this
operator|.
name|producer1
argument_list|,
operator|new
name|Variant
argument_list|(
literal|"Foo1"
argument_list|)
argument_list|)
expr_stmt|;
name|sendValue
argument_list|(
name|this
operator|.
name|producer1
argument_list|,
operator|new
name|Variant
argument_list|(
literal|"Foo2"
argument_list|)
argument_list|)
expr_stmt|;
comment|// assert
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testWrite2 ()
specifier|public
name|void
name|testWrite2
parameter_list|()
throws|throws
name|Exception
block|{
comment|// item 1
name|this
operator|.
name|test1Endpoint
operator|.
name|setExpectedCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
comment|// item 2
name|this
operator|.
name|test2Endpoint
operator|.
name|setExpectedCount
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|testBody
argument_list|(
name|this
operator|.
name|test2Endpoint
operator|.
name|message
argument_list|(
literal|0
argument_list|)
argument_list|,
name|assertGoodValue
argument_list|(
literal|"Foo1"
argument_list|)
argument_list|)
expr_stmt|;
name|testBody
argument_list|(
name|this
operator|.
name|test2Endpoint
operator|.
name|message
argument_list|(
literal|1
argument_list|)
argument_list|,
name|assertGoodValue
argument_list|(
literal|"Foo2"
argument_list|)
argument_list|)
expr_stmt|;
comment|// send
name|sendValue
argument_list|(
name|this
operator|.
name|producer2
argument_list|,
operator|new
name|Variant
argument_list|(
literal|"Foo1"
argument_list|)
argument_list|)
expr_stmt|;
name|sendValue
argument_list|(
name|this
operator|.
name|producer2
argument_list|,
operator|new
name|Variant
argument_list|(
literal|"Foo2"
argument_list|)
argument_list|)
expr_stmt|;
comment|// assert
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testWrite3 ()
specifier|public
name|void
name|testWrite3
parameter_list|()
throws|throws
name|Exception
block|{
comment|// item 1
name|this
operator|.
name|test1Endpoint
operator|.
name|setExpectedCount
argument_list|(
literal|2
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
literal|0
argument_list|)
argument_list|,
name|assertGoodValue
argument_list|(
literal|"Foo1"
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
literal|"Foo3"
argument_list|)
argument_list|)
expr_stmt|;
comment|// item 1
name|this
operator|.
name|test2Endpoint
operator|.
name|setExpectedCount
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|testBody
argument_list|(
name|this
operator|.
name|test2Endpoint
operator|.
name|message
argument_list|(
literal|0
argument_list|)
argument_list|,
name|assertGoodValue
argument_list|(
literal|"Foo2"
argument_list|)
argument_list|)
expr_stmt|;
name|testBody
argument_list|(
name|this
operator|.
name|test2Endpoint
operator|.
name|message
argument_list|(
literal|1
argument_list|)
argument_list|,
name|assertGoodValue
argument_list|(
literal|"Foo4"
argument_list|)
argument_list|)
expr_stmt|;
comment|// send
name|sendValue
argument_list|(
name|this
operator|.
name|producer1
argument_list|,
operator|new
name|Variant
argument_list|(
literal|"Foo1"
argument_list|)
argument_list|)
expr_stmt|;
name|sendValue
argument_list|(
name|this
operator|.
name|producer2
argument_list|,
operator|new
name|Variant
argument_list|(
literal|"Foo2"
argument_list|)
argument_list|)
expr_stmt|;
name|sendValue
argument_list|(
name|this
operator|.
name|producer3
argument_list|,
operator|new
name|Variant
argument_list|(
literal|"Foo3"
argument_list|)
argument_list|)
expr_stmt|;
name|sendValue
argument_list|(
name|this
operator|.
name|producer4
argument_list|,
operator|new
name|Variant
argument_list|(
literal|"Foo4"
argument_list|)
argument_list|)
expr_stmt|;
comment|// assert
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|method|sendValue (final ProducerTemplate producerTemplate, final Variant variant)
specifier|private
specifier|static
name|void
name|sendValue
parameter_list|(
specifier|final
name|ProducerTemplate
name|producerTemplate
parameter_list|,
specifier|final
name|Variant
name|variant
parameter_list|)
block|{
comment|// we always write synchronously since we do need the message order
name|producerTemplate
operator|.
name|sendBodyAndHeader
argument_list|(
name|variant
argument_list|,
literal|"await"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

