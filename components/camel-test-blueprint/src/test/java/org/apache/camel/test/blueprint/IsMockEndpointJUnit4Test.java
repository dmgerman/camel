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

begin_class
DECL|class|IsMockEndpointJUnit4Test
specifier|public
class|class
name|IsMockEndpointJUnit4Test
extends|extends
name|CamelBlueprintTestSupport
block|{
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
literal|"mock:seda:result"
argument_list|,
name|context
operator|=
literal|"IsMockEndpoints"
argument_list|)
DECL|field|mockSeda
specifier|private
name|MockEndpoint
name|mockSeda
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
literal|"mock:bar"
argument_list|,
name|context
operator|=
literal|"IsMockEndpoints"
argument_list|)
DECL|field|mockBar
specifier|private
name|MockEndpoint
name|mockBar
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
literal|"mock:baz"
argument_list|,
name|context
operator|=
literal|"IsMockEndpoints"
argument_list|)
DECL|field|mockBaz
specifier|private
name|MockEndpoint
name|mockBaz
decl_stmt|;
annotation|@
name|Override
DECL|method|getBlueprintDescriptor ()
specifier|protected
name|String
name|getBlueprintDescriptor
parameter_list|()
block|{
return|return
literal|"org/apache/camel/test/blueprint/IsMockEndpoints.xml"
return|;
block|}
annotation|@
name|Override
DECL|method|isMockEndpoints ()
specifier|public
name|String
name|isMockEndpoints
parameter_list|()
block|{
return|return
literal|"*"
return|;
block|}
annotation|@
name|Test
DECL|method|testMockAllEndpoints ()
specifier|public
name|void
name|testMockAllEndpoints
parameter_list|()
throws|throws
name|Exception
block|{
name|mockSeda
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"bar"
argument_list|)
expr_stmt|;
name|mockBar
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"bar"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:foo"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|context
operator|.
name|hasEndpoint
argument_list|(
literal|"mock:seda:result"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|context
operator|.
name|hasEndpoint
argument_list|(
literal|"mock:baz"
argument_list|)
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testMockBar ()
specifier|public
name|void
name|testMockBar
parameter_list|()
throws|throws
name|Exception
block|{
name|mockBar
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"bar"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:foo"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|context
operator|.
name|hasEndpoint
argument_list|(
literal|"mock:bar"
argument_list|)
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testMockBaz ()
specifier|public
name|void
name|testMockBaz
parameter_list|()
throws|throws
name|Exception
block|{
name|mockBaz
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"baz"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:foo"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|context
operator|.
name|hasEndpoint
argument_list|(
literal|"mock:baz"
argument_list|)
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

