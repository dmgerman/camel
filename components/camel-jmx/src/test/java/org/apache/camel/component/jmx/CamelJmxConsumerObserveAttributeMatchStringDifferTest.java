begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jmx
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jmx
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
name|api
operator|.
name|management
operator|.
name|ManagedCamelContext
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
name|api
operator|.
name|management
operator|.
name|mbean
operator|.
name|ManagedRouteMBean
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
name|test
operator|.
name|junit5
operator|.
name|CamelTestSupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|jupiter
operator|.
name|api
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|CamelJmxConsumerObserveAttributeMatchStringDifferTest
specifier|public
class|class
name|CamelJmxConsumerObserveAttributeMatchStringDifferTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|Override
DECL|method|useJmx ()
specifier|protected
name|boolean
name|useJmx
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Test
DECL|method|testJmxConsumer ()
specifier|public
name|void
name|testJmxConsumer
parameter_list|()
throws|throws
name|Exception
block|{
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|expectedMinimumMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|body
argument_list|()
operator|.
name|contains
argument_list|(
literal|"<newValue>false</newValue>"
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|body
argument_list|()
operator|.
name|contains
argument_list|(
literal|"<attributeName>Tracing</attributeName>"
argument_list|)
expr_stmt|;
comment|// change the attribute so JMX triggers but should be filtered
name|ManagedRouteMBean
name|mr
init|=
name|context
operator|.
name|getExtension
argument_list|(
name|ManagedCamelContext
operator|.
name|class
argument_list|)
operator|.
name|getManagedRoute
argument_list|(
literal|"foo"
argument_list|)
decl_stmt|;
name|mr
operator|.
name|setStatisticsEnabled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
comment|// change the attribute so JMX triggers
name|mr
operator|=
name|context
operator|.
name|getExtension
argument_list|(
name|ManagedCamelContext
operator|.
name|class
argument_list|)
operator|.
name|getManagedRoute
argument_list|(
literal|"foo"
argument_list|)
expr_stmt|;
name|mr
operator|.
name|setTracing
argument_list|(
literal|true
argument_list|)
expr_stmt|;
comment|// change the attribute so JMX triggers
name|mr
operator|=
name|context
operator|.
name|getExtension
argument_list|(
name|ManagedCamelContext
operator|.
name|class
argument_list|)
operator|.
name|getManagedRoute
argument_list|(
literal|"foo"
argument_list|)
expr_stmt|;
name|mr
operator|.
name|setTracing
argument_list|(
literal|false
argument_list|)
expr_stmt|;
comment|// change the attribute so JMX triggers
name|mr
operator|=
name|context
operator|.
name|getExtension
argument_list|(
name|ManagedCamelContext
operator|.
name|class
argument_list|)
operator|.
name|getManagedRoute
argument_list|(
literal|"foo"
argument_list|)
expr_stmt|;
name|mr
operator|.
name|setTracing
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
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
name|String
name|id
init|=
name|getContext
argument_list|()
operator|.
name|getName
argument_list|()
decl_stmt|;
name|fromF
argument_list|(
literal|"jmx:platform?objectDomain=org.apache.camel&key.context=%s&key.type=routes&key.name=\"foo\"&observedAttribute=Tracing&stringToCompare=true&notifyDiffer=true"
argument_list|,
name|id
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"jmxRoute"
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:jmx"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:foo"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:foo"
argument_list|,
literal|"mock:foo"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

