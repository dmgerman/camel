begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.management
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|management
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|Attribute
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|MBeanServer
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|ObjectName
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
name|LoggingLevel
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
name|processor
operator|.
name|interceptor
operator|.
name|Tracer
import|;
end_import

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|ManagedRouteContextTracerTest
specifier|public
class|class
name|ManagedRouteContextTracerTest
extends|extends
name|ContextTestSupport
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
name|DefaultManagementNamingStrategy
name|naming
init|=
operator|(
name|DefaultManagementNamingStrategy
operator|)
name|context
operator|.
name|getManagementStrategy
argument_list|()
operator|.
name|getManagementNamingStrategy
argument_list|()
decl_stmt|;
name|naming
operator|.
name|setHostName
argument_list|(
literal|"localhost"
argument_list|)
expr_stmt|;
name|naming
operator|.
name|setDomainName
argument_list|(
literal|"org.apache.camel"
argument_list|)
expr_stmt|;
return|return
name|context
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|testRouteTracing ()
specifier|public
name|void
name|testRouteTracing
parameter_list|()
throws|throws
name|Exception
block|{
name|MBeanServer
name|mbeanServer
init|=
name|context
operator|.
name|getManagementStrategy
argument_list|()
operator|.
name|getManagementAgent
argument_list|()
operator|.
name|getMBeanServer
argument_list|()
decl_stmt|;
name|ObjectName
name|on1
init|=
name|ObjectName
operator|.
name|getInstance
argument_list|(
literal|"org.apache.camel:context=localhost/camel-1,type=routes,name=\"route1\""
argument_list|)
decl_stmt|;
name|ObjectName
name|on2
init|=
name|ObjectName
operator|.
name|getInstance
argument_list|(
literal|"org.apache.camel:context=localhost/camel-1,type=routes,name=\"route2\""
argument_list|)
decl_stmt|;
comment|// with tracing
name|MockEndpoint
name|traced
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:traced"
argument_list|)
decl_stmt|;
name|traced
operator|.
name|setExpectedMessageCount
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|MockEndpoint
name|result
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|result
operator|.
name|setExpectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|MockEndpoint
name|foo
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:foo"
argument_list|)
decl_stmt|;
name|foo
operator|.
name|setExpectedMessageCount
argument_list|(
literal|1
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
literal|"direct:foo"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
comment|// should be enabled for route 1
name|Boolean
name|tracing
init|=
operator|(
name|Boolean
operator|)
name|mbeanServer
operator|.
name|getAttribute
argument_list|(
name|on1
argument_list|,
literal|"Tracing"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Tracing should be enabled for route 1"
argument_list|,
literal|true
argument_list|,
name|tracing
operator|.
name|booleanValue
argument_list|()
argument_list|)
expr_stmt|;
comment|// should be disabled for route 2
name|Boolean
name|tracing2
init|=
operator|(
name|Boolean
operator|)
name|mbeanServer
operator|.
name|getAttribute
argument_list|(
name|on2
argument_list|,
literal|"Tracing"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Tracing should be disabled for route 2"
argument_list|,
literal|false
argument_list|,
name|tracing2
operator|.
name|booleanValue
argument_list|()
argument_list|)
expr_stmt|;
comment|// now enable tracing on route 2
name|mbeanServer
operator|.
name|setAttribute
argument_list|(
name|on2
argument_list|,
operator|new
name|Attribute
argument_list|(
literal|"Tracing"
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
argument_list|)
expr_stmt|;
comment|// with tracing
name|traced
operator|.
name|reset
argument_list|()
expr_stmt|;
name|traced
operator|.
name|setExpectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|result
operator|.
name|reset
argument_list|()
expr_stmt|;
name|result
operator|.
name|setExpectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|foo
operator|.
name|reset
argument_list|()
expr_stmt|;
name|foo
operator|.
name|setExpectedMessageCount
argument_list|(
literal|1
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
name|assertMockEndpointsSatisfied
argument_list|()
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
name|Tracer
name|tracer
init|=
operator|new
name|Tracer
argument_list|()
decl_stmt|;
name|tracer
operator|.
name|setDestinationUri
argument_list|(
literal|"mock:traced"
argument_list|)
expr_stmt|;
name|tracer
operator|.
name|setLogLevel
argument_list|(
name|LoggingLevel
operator|.
name|OFF
argument_list|)
expr_stmt|;
name|context
operator|.
name|addInterceptStrategy
argument_list|(
name|tracer
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:foo"
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
name|noTracing
argument_list|()
operator|.
name|to
argument_list|(
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

