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
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

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
DECL|class|ManagedCamelContextTracerTest
specifier|public
class|class
name|ManagedCamelContextTracerTest
extends|extends
name|ManagementTestSupport
block|{
DECL|method|testCamelContextTracing ()
specifier|public
name|void
name|testCamelContextTracing
parameter_list|()
throws|throws
name|Exception
block|{
name|MBeanServer
name|mbeanServer
init|=
name|getMBeanServer
argument_list|()
decl_stmt|;
name|ObjectName
name|camel
init|=
name|ObjectName
operator|.
name|getInstance
argument_list|(
literal|"org.apache.camel:context=localhost/camel-1,type=context,name=\"camel-1\""
argument_list|)
decl_stmt|;
name|Set
argument_list|<
name|ObjectName
argument_list|>
name|set
init|=
name|mbeanServer
operator|.
name|queryNames
argument_list|(
operator|new
name|ObjectName
argument_list|(
literal|"*:type=tracer,*"
argument_list|)
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|set
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|ObjectName
name|on
init|=
name|set
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
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
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
comment|// should be enabled
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
name|camel
argument_list|,
literal|"Tracing"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Tracing should be enabled"
argument_list|,
literal|true
argument_list|,
name|tracing
operator|.
name|booleanValue
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|destinationUri
init|=
operator|(
name|String
operator|)
name|mbeanServer
operator|.
name|getAttribute
argument_list|(
name|on
argument_list|,
literal|"DestinationUri"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"mock:traced"
argument_list|,
name|destinationUri
argument_list|)
expr_stmt|;
name|String
name|logLevel
init|=
operator|(
name|String
operator|)
name|mbeanServer
operator|.
name|getAttribute
argument_list|(
name|on
argument_list|,
literal|"LogLevel"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|LoggingLevel
operator|.
name|OFF
operator|.
name|name
argument_list|()
argument_list|,
name|logLevel
argument_list|)
expr_stmt|;
name|String
name|logName
init|=
operator|(
name|String
operator|)
name|mbeanServer
operator|.
name|getAttribute
argument_list|(
name|on
argument_list|,
literal|"LogName"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|logName
argument_list|)
expr_stmt|;
name|Boolean
name|logStackTrace
init|=
operator|(
name|Boolean
operator|)
name|mbeanServer
operator|.
name|getAttribute
argument_list|(
name|on
argument_list|,
literal|"LogStackTrace"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Boolean
operator|.
name|FALSE
argument_list|,
name|logStackTrace
argument_list|)
expr_stmt|;
name|Boolean
name|traceInterceptors
init|=
operator|(
name|Boolean
operator|)
name|mbeanServer
operator|.
name|getAttribute
argument_list|(
name|on
argument_list|,
literal|"TraceInterceptors"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Boolean
operator|.
name|FALSE
argument_list|,
name|traceInterceptors
argument_list|)
expr_stmt|;
name|Boolean
name|traceExceptions
init|=
operator|(
name|Boolean
operator|)
name|mbeanServer
operator|.
name|getAttribute
argument_list|(
name|on
argument_list|,
literal|"TraceExceptions"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Boolean
operator|.
name|TRUE
argument_list|,
name|traceExceptions
argument_list|)
expr_stmt|;
name|Boolean
name|traceOutExchanges
init|=
operator|(
name|Boolean
operator|)
name|mbeanServer
operator|.
name|getAttribute
argument_list|(
name|on
argument_list|,
literal|"TraceOutExchanges"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Boolean
operator|.
name|FALSE
argument_list|,
name|traceOutExchanges
argument_list|)
expr_stmt|;
name|Boolean
name|formatterShowBody
init|=
operator|(
name|Boolean
operator|)
name|mbeanServer
operator|.
name|getAttribute
argument_list|(
name|on
argument_list|,
literal|"FormatterShowBody"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Boolean
operator|.
name|TRUE
argument_list|,
name|formatterShowBody
argument_list|)
expr_stmt|;
name|Boolean
name|formatterShowBodyType
init|=
operator|(
name|Boolean
operator|)
name|mbeanServer
operator|.
name|getAttribute
argument_list|(
name|on
argument_list|,
literal|"FormatterShowBodyType"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Boolean
operator|.
name|TRUE
argument_list|,
name|formatterShowBodyType
argument_list|)
expr_stmt|;
name|Boolean
name|formatterShowOutBody
init|=
operator|(
name|Boolean
operator|)
name|mbeanServer
operator|.
name|getAttribute
argument_list|(
name|on
argument_list|,
literal|"FormatterShowOutBody"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Boolean
operator|.
name|FALSE
argument_list|,
name|formatterShowOutBody
argument_list|)
expr_stmt|;
name|Boolean
name|formatterShowOutBodyType
init|=
operator|(
name|Boolean
operator|)
name|mbeanServer
operator|.
name|getAttribute
argument_list|(
name|on
argument_list|,
literal|"FormatterShowOutBodyType"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Boolean
operator|.
name|FALSE
argument_list|,
name|formatterShowOutBodyType
argument_list|)
expr_stmt|;
name|Boolean
name|formatterShowBreadCrumb
init|=
operator|(
name|Boolean
operator|)
name|mbeanServer
operator|.
name|getAttribute
argument_list|(
name|on
argument_list|,
literal|"FormatterShowBreadCrumb"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Boolean
operator|.
name|TRUE
argument_list|,
name|formatterShowBreadCrumb
argument_list|)
expr_stmt|;
name|Boolean
name|formatterShowExchangeId
init|=
operator|(
name|Boolean
operator|)
name|mbeanServer
operator|.
name|getAttribute
argument_list|(
name|on
argument_list|,
literal|"FormatterShowExchangeId"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Boolean
operator|.
name|FALSE
argument_list|,
name|formatterShowExchangeId
argument_list|)
expr_stmt|;
name|Boolean
name|formatterShowHeaders
init|=
operator|(
name|Boolean
operator|)
name|mbeanServer
operator|.
name|getAttribute
argument_list|(
name|on
argument_list|,
literal|"FormatterShowHeaders"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Boolean
operator|.
name|TRUE
argument_list|,
name|formatterShowHeaders
argument_list|)
expr_stmt|;
name|Boolean
name|formatterShowOutHeaders
init|=
operator|(
name|Boolean
operator|)
name|mbeanServer
operator|.
name|getAttribute
argument_list|(
name|on
argument_list|,
literal|"FormatterShowOutHeaders"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Boolean
operator|.
name|FALSE
argument_list|,
name|formatterShowOutHeaders
argument_list|)
expr_stmt|;
name|Boolean
name|formatterShowProperties
init|=
operator|(
name|Boolean
operator|)
name|mbeanServer
operator|.
name|getAttribute
argument_list|(
name|on
argument_list|,
literal|"FormatterShowProperties"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Boolean
operator|.
name|FALSE
argument_list|,
name|formatterShowProperties
argument_list|)
expr_stmt|;
name|Boolean
name|formatterShowNode
init|=
operator|(
name|Boolean
operator|)
name|mbeanServer
operator|.
name|getAttribute
argument_list|(
name|on
argument_list|,
literal|"FormatterShowNode"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Boolean
operator|.
name|TRUE
argument_list|,
name|formatterShowNode
argument_list|)
expr_stmt|;
name|Boolean
name|formatterShowExchangePattern
init|=
operator|(
name|Boolean
operator|)
name|mbeanServer
operator|.
name|getAttribute
argument_list|(
name|on
argument_list|,
literal|"FormatterShowExchangePattern"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Boolean
operator|.
name|TRUE
argument_list|,
name|formatterShowExchangePattern
argument_list|)
expr_stmt|;
name|Boolean
name|formatterShowException
init|=
operator|(
name|Boolean
operator|)
name|mbeanServer
operator|.
name|getAttribute
argument_list|(
name|on
argument_list|,
literal|"FormatterShowException"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Boolean
operator|.
name|TRUE
argument_list|,
name|formatterShowException
argument_list|)
expr_stmt|;
name|Boolean
name|formatterShowShortExchangeId
init|=
operator|(
name|Boolean
operator|)
name|mbeanServer
operator|.
name|getAttribute
argument_list|(
name|on
argument_list|,
literal|"FormatterShowShortExchangeId"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Boolean
operator|.
name|FALSE
argument_list|,
name|formatterShowShortExchangeId
argument_list|)
expr_stmt|;
name|Integer
name|formatterBreadCrumbLength
init|=
operator|(
name|Integer
operator|)
name|mbeanServer
operator|.
name|getAttribute
argument_list|(
name|on
argument_list|,
literal|"FormatterBreadCrumbLength"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|formatterBreadCrumbLength
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
name|Integer
name|formatterNodeLength
init|=
operator|(
name|Integer
operator|)
name|mbeanServer
operator|.
name|getAttribute
argument_list|(
name|on
argument_list|,
literal|"FormatterNodeLength"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|formatterNodeLength
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
name|Integer
name|formatterMaxChars
init|=
operator|(
name|Integer
operator|)
name|mbeanServer
operator|.
name|getAttribute
argument_list|(
name|on
argument_list|,
literal|"FormatterMaxChars"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|formatterMaxChars
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
comment|// now disable tracing
name|mbeanServer
operator|.
name|setAttribute
argument_list|(
name|camel
argument_list|,
operator|new
name|Attribute
argument_list|(
literal|"Tracing"
argument_list|,
name|Boolean
operator|.
name|FALSE
argument_list|)
argument_list|)
expr_stmt|;
comment|// without tracing
name|traced
operator|.
name|reset
argument_list|()
expr_stmt|;
name|traced
operator|.
name|setExpectedMessageCount
argument_list|(
literal|0
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
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

