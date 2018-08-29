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
name|org
operator|.
name|junit
operator|.
name|Test
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

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|ManagedTracerOptionsTest
specifier|public
class|class
name|ManagedTracerOptionsTest
extends|extends
name|ManagementTestSupport
block|{
annotation|@
name|Test
DECL|method|testManagedTracerOptions ()
specifier|public
name|void
name|testManagedTracerOptions
parameter_list|()
throws|throws
name|Exception
block|{
comment|// JMX tests dont work well on AIX CI servers (hangs them)
if|if
condition|(
name|isPlatform
argument_list|(
literal|"aix"
argument_list|)
condition|)
block|{
return|return;
block|}
name|MBeanServer
name|mbeanServer
init|=
name|getMBeanServer
argument_list|()
decl_stmt|;
name|ObjectName
name|on
init|=
operator|new
name|ObjectName
argument_list|(
literal|"org.apache.camel:context=camel-1,type=tracer,name=Tracer"
argument_list|)
decl_stmt|;
name|mbeanServer
operator|.
name|isRegistered
argument_list|(
name|on
argument_list|)
expr_stmt|;
name|mbeanServer
operator|.
name|setAttribute
argument_list|(
name|on
argument_list|,
operator|new
name|Attribute
argument_list|(
literal|"Enabled"
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
argument_list|)
expr_stmt|;
name|Boolean
name|enabled
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
literal|"Enabled"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|enabled
operator|.
name|booleanValue
argument_list|()
argument_list|)
expr_stmt|;
name|mbeanServer
operator|.
name|setAttribute
argument_list|(
name|on
argument_list|,
operator|new
name|Attribute
argument_list|(
literal|"DestinationUri"
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|duri
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
literal|null
argument_list|,
name|duri
argument_list|)
expr_stmt|;
name|mbeanServer
operator|.
name|setAttribute
argument_list|(
name|on
argument_list|,
operator|new
name|Attribute
argument_list|(
literal|"DestinationUri"
argument_list|,
literal|"mock://traced"
argument_list|)
argument_list|)
expr_stmt|;
name|duri
operator|=
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
expr_stmt|;
name|assertEquals
argument_list|(
literal|"mock://traced"
argument_list|,
name|duri
argument_list|)
expr_stmt|;
name|Boolean
name|useJpa
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
literal|"UseJpa"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|useJpa
operator|.
name|booleanValue
argument_list|()
argument_list|)
expr_stmt|;
name|mbeanServer
operator|.
name|setAttribute
argument_list|(
name|on
argument_list|,
operator|new
name|Attribute
argument_list|(
literal|"LogName"
argument_list|,
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|ln
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
name|assertEquals
argument_list|(
literal|"foo"
argument_list|,
name|ln
argument_list|)
expr_stmt|;
name|mbeanServer
operator|.
name|setAttribute
argument_list|(
name|on
argument_list|,
operator|new
name|Attribute
argument_list|(
literal|"LogLevel"
argument_list|,
literal|"WARN"
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|ll
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
name|WARN
operator|.
name|name
argument_list|()
argument_list|,
name|ll
argument_list|)
expr_stmt|;
name|mbeanServer
operator|.
name|setAttribute
argument_list|(
name|on
argument_list|,
operator|new
name|Attribute
argument_list|(
literal|"LogStackTrace"
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
argument_list|)
expr_stmt|;
name|Boolean
name|lst
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
literal|true
argument_list|,
name|lst
operator|.
name|booleanValue
argument_list|()
argument_list|)
expr_stmt|;
name|mbeanServer
operator|.
name|setAttribute
argument_list|(
name|on
argument_list|,
operator|new
name|Attribute
argument_list|(
literal|"TraceInterceptors"
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
argument_list|)
expr_stmt|;
name|Boolean
name|ti
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
literal|true
argument_list|,
name|ti
operator|.
name|booleanValue
argument_list|()
argument_list|)
expr_stmt|;
name|mbeanServer
operator|.
name|setAttribute
argument_list|(
name|on
argument_list|,
operator|new
name|Attribute
argument_list|(
literal|"TraceExceptions"
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
argument_list|)
expr_stmt|;
name|Boolean
name|te
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
literal|true
argument_list|,
name|te
operator|.
name|booleanValue
argument_list|()
argument_list|)
expr_stmt|;
name|mbeanServer
operator|.
name|setAttribute
argument_list|(
name|on
argument_list|,
operator|new
name|Attribute
argument_list|(
literal|"TraceOutExchanges"
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
argument_list|)
expr_stmt|;
name|Boolean
name|toe
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
literal|true
argument_list|,
name|toe
operator|.
name|booleanValue
argument_list|()
argument_list|)
expr_stmt|;
name|doAssertFormatter
argument_list|(
name|mbeanServer
argument_list|,
name|on
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|expectedMessageCount
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
DECL|method|doAssertFormatter (MBeanServer mbeanServer, ObjectName on)
specifier|private
name|void
name|doAssertFormatter
parameter_list|(
name|MBeanServer
name|mbeanServer
parameter_list|,
name|ObjectName
name|on
parameter_list|)
throws|throws
name|Exception
block|{
comment|// JMX tests dont work well on AIX CI servers (hangs them)
if|if
condition|(
name|isPlatform
argument_list|(
literal|"aix"
argument_list|)
condition|)
block|{
return|return;
block|}
name|mbeanServer
operator|.
name|setAttribute
argument_list|(
name|on
argument_list|,
operator|new
name|Attribute
argument_list|(
literal|"FormatterShowBody"
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
argument_list|)
expr_stmt|;
name|Boolean
name|fsb
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
literal|true
argument_list|,
name|fsb
operator|.
name|booleanValue
argument_list|()
argument_list|)
expr_stmt|;
name|mbeanServer
operator|.
name|setAttribute
argument_list|(
name|on
argument_list|,
operator|new
name|Attribute
argument_list|(
literal|"FormatterShowBodyType"
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
argument_list|)
expr_stmt|;
name|Boolean
name|fsbt
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
literal|true
argument_list|,
name|fsbt
operator|.
name|booleanValue
argument_list|()
argument_list|)
expr_stmt|;
name|mbeanServer
operator|.
name|setAttribute
argument_list|(
name|on
argument_list|,
operator|new
name|Attribute
argument_list|(
literal|"FormatterShowOutBody"
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
argument_list|)
expr_stmt|;
name|Boolean
name|fsob
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
literal|true
argument_list|,
name|fsob
operator|.
name|booleanValue
argument_list|()
argument_list|)
expr_stmt|;
name|mbeanServer
operator|.
name|setAttribute
argument_list|(
name|on
argument_list|,
operator|new
name|Attribute
argument_list|(
literal|"FormatterShowOutBodyType"
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
argument_list|)
expr_stmt|;
name|Boolean
name|fsobt
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
literal|true
argument_list|,
name|fsobt
operator|.
name|booleanValue
argument_list|()
argument_list|)
expr_stmt|;
name|mbeanServer
operator|.
name|setAttribute
argument_list|(
name|on
argument_list|,
operator|new
name|Attribute
argument_list|(
literal|"FormatterShowBreadCrumb"
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
argument_list|)
expr_stmt|;
name|Boolean
name|fsbc
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
literal|true
argument_list|,
name|fsbc
operator|.
name|booleanValue
argument_list|()
argument_list|)
expr_stmt|;
name|mbeanServer
operator|.
name|setAttribute
argument_list|(
name|on
argument_list|,
operator|new
name|Attribute
argument_list|(
literal|"FormatterShowExchangeId"
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
argument_list|)
expr_stmt|;
name|Boolean
name|fsei
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
literal|true
argument_list|,
name|fsei
operator|.
name|booleanValue
argument_list|()
argument_list|)
expr_stmt|;
name|mbeanServer
operator|.
name|setAttribute
argument_list|(
name|on
argument_list|,
operator|new
name|Attribute
argument_list|(
literal|"FormatterShowShortExchangeId"
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
argument_list|)
expr_stmt|;
name|Boolean
name|fssei
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
literal|true
argument_list|,
name|fssei
operator|.
name|booleanValue
argument_list|()
argument_list|)
expr_stmt|;
name|mbeanServer
operator|.
name|setAttribute
argument_list|(
name|on
argument_list|,
operator|new
name|Attribute
argument_list|(
literal|"FormatterShowHeaders"
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
argument_list|)
expr_stmt|;
name|Boolean
name|fsh
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
literal|true
argument_list|,
name|fsh
operator|.
name|booleanValue
argument_list|()
argument_list|)
expr_stmt|;
name|mbeanServer
operator|.
name|setAttribute
argument_list|(
name|on
argument_list|,
operator|new
name|Attribute
argument_list|(
literal|"FormatterShowOutHeaders"
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
argument_list|)
expr_stmt|;
name|Boolean
name|fsoh
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
literal|true
argument_list|,
name|fsoh
operator|.
name|booleanValue
argument_list|()
argument_list|)
expr_stmt|;
name|mbeanServer
operator|.
name|setAttribute
argument_list|(
name|on
argument_list|,
operator|new
name|Attribute
argument_list|(
literal|"FormatterShowProperties"
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
argument_list|)
expr_stmt|;
name|Boolean
name|fsp
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
literal|true
argument_list|,
name|fsp
operator|.
name|booleanValue
argument_list|()
argument_list|)
expr_stmt|;
name|mbeanServer
operator|.
name|setAttribute
argument_list|(
name|on
argument_list|,
operator|new
name|Attribute
argument_list|(
literal|"FormatterShowNode"
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
argument_list|)
expr_stmt|;
name|Boolean
name|fsn
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
literal|true
argument_list|,
name|fsn
operator|.
name|booleanValue
argument_list|()
argument_list|)
expr_stmt|;
name|mbeanServer
operator|.
name|setAttribute
argument_list|(
name|on
argument_list|,
operator|new
name|Attribute
argument_list|(
literal|"FormatterShowRouteId"
argument_list|,
name|Boolean
operator|.
name|FALSE
argument_list|)
argument_list|)
expr_stmt|;
name|Boolean
name|fsr
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
literal|"FormatterShowRouteId"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|fsr
operator|.
name|booleanValue
argument_list|()
argument_list|)
expr_stmt|;
name|mbeanServer
operator|.
name|setAttribute
argument_list|(
name|on
argument_list|,
operator|new
name|Attribute
argument_list|(
literal|"FormatterShowExchangePattern"
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
argument_list|)
expr_stmt|;
name|Boolean
name|fsep
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
literal|true
argument_list|,
name|fsep
operator|.
name|booleanValue
argument_list|()
argument_list|)
expr_stmt|;
name|mbeanServer
operator|.
name|setAttribute
argument_list|(
name|on
argument_list|,
operator|new
name|Attribute
argument_list|(
literal|"FormatterShowException"
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
argument_list|)
expr_stmt|;
name|Boolean
name|fsex
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
literal|true
argument_list|,
name|fsex
operator|.
name|booleanValue
argument_list|()
argument_list|)
expr_stmt|;
name|mbeanServer
operator|.
name|setAttribute
argument_list|(
name|on
argument_list|,
operator|new
name|Attribute
argument_list|(
literal|"FormatterBreadCrumbLength"
argument_list|,
literal|100
argument_list|)
argument_list|)
expr_stmt|;
name|Integer
name|fbcl
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
literal|100
argument_list|,
name|fbcl
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
name|mbeanServer
operator|.
name|setAttribute
argument_list|(
name|on
argument_list|,
operator|new
name|Attribute
argument_list|(
literal|"FormatterNodeLength"
argument_list|,
literal|50
argument_list|)
argument_list|)
expr_stmt|;
name|Integer
name|fnl
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
literal|50
argument_list|,
name|fnl
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
name|mbeanServer
operator|.
name|setAttribute
argument_list|(
name|on
argument_list|,
operator|new
name|Attribute
argument_list|(
literal|"FormatterMaxChars"
argument_list|,
literal|250
argument_list|)
argument_list|)
expr_stmt|;
name|Integer
name|fmc
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
literal|250
argument_list|,
name|fmc
operator|.
name|intValue
argument_list|()
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
name|context
operator|.
name|setTracing
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:start"
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

