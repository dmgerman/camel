begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.restbinding
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|restbinding
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
name|model
operator|.
name|RouteDefinition
import|;
end_import

begin_class
DECL|class|FromRestGetTest
specifier|public
class|class
name|FromRestGetTest
extends|extends
name|ContextTestSupport
block|{
annotation|@
name|Override
DECL|method|createCamelContext ()
specifier|protected
name|CamelContext
name|createCamelContext
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContext
name|context
init|=
name|super
operator|.
name|createCamelContext
argument_list|()
decl_stmt|;
comment|// register our dummy rest capable component
name|context
operator|.
name|addComponent
argument_list|(
literal|"dummy-rest"
argument_list|,
operator|new
name|DummyRestBindingCapableComponent
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|context
return|;
block|}
comment|/*    public void testFromRestModel() {         assertEquals(1, context.getRoutes().size());          RouteDefinition route = context.getRouteDefinition("foo");         assertNotNull(route);          FromRestDefinition from = (FromRestDefinition) route.getInputs().get(0);         assertNotNull(from);         assertEquals("get", from.getVerb());         assertEquals("/hello", from.getPath());         assertNull(from.getAccept());     }      public void testFromRest() throws Exception {         getMockEndpoint("mock:foo").expectedMessageCount(1);          template.sendBody("seda:get-hello", "Hello World");          assertMockEndpointsSatisfied();     }*/
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
comment|//                rest()
comment|//                    .get("/hello").to("mock:foo")
comment|//                    .get("/bye").to("mock:bar");
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

