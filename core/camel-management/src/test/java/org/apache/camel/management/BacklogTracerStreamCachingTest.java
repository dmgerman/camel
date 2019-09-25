begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|io
operator|.
name|ByteArrayInputStream
import|;
end_import

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
name|Exchange
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
name|BacklogTracerEventMessage
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
name|junit
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|BacklogTracerStreamCachingTest
specifier|public
class|class
name|BacklogTracerStreamCachingTest
extends|extends
name|ManagementTestSupport
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|Test
DECL|method|testBacklogTracerEventMessageStreamCaching ()
specifier|public
name|void
name|testBacklogTracerEventMessageStreamCaching
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
literal|"org.apache.camel:context=camel-1,type=tracer,name=BacklogTracer"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|on
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|mbeanServer
operator|.
name|isRegistered
argument_list|(
name|on
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
literal|"Should not be enabled"
argument_list|,
name|Boolean
operator|.
name|FALSE
argument_list|,
name|enabled
argument_list|)
expr_stmt|;
name|Integer
name|size
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
literal|"BacklogSize"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Should be 1000"
argument_list|,
literal|1000
argument_list|,
name|size
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
name|Boolean
name|removeOnDump
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
literal|"RemoveOnDump"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Boolean
operator|.
name|TRUE
argument_list|,
name|removeOnDump
argument_list|)
expr_stmt|;
comment|// enable streams
name|mbeanServer
operator|.
name|setAttribute
argument_list|(
name|on
argument_list|,
operator|new
name|Attribute
argument_list|(
literal|"BodyIncludeStreams"
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
argument_list|)
expr_stmt|;
comment|// enable it
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
name|getMockEndpoint
argument_list|(
literal|"mock:bar"
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
name|List
argument_list|<
name|Exchange
argument_list|>
name|exchanges
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:bar"
argument_list|)
operator|.
name|getReceivedExchanges
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|BacklogTracerEventMessage
argument_list|>
name|events
init|=
operator|(
name|List
argument_list|<
name|BacklogTracerEventMessage
argument_list|>
operator|)
name|mbeanServer
operator|.
name|invoke
argument_list|(
name|on
argument_list|,
literal|"dumpTracedMessages"
argument_list|,
operator|new
name|Object
index|[]
block|{
literal|"bar"
block|}
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"java.lang.String"
block|}
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|events
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|events
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|BacklogTracerEventMessage
name|event1
init|=
name|events
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"bar"
argument_list|,
name|event1
operator|.
name|getToNode
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"<message exchangeId=\""
operator|+
name|exchanges
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getExchangeId
argument_list|()
operator|+
literal|"\">\n"
operator|+
literal|"<body type=\"org.apache.camel.converter.stream.ByteArrayInputStreamCache\">Bye World</body>\n"
operator|+
literal|"</message>"
argument_list|,
name|event1
operator|.
name|getMessageAsXml
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
name|setUseBreadcrumb
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|context
operator|.
name|setBacklogTracing
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|streamCaching
argument_list|()
operator|.
name|process
argument_list|(
name|exchange
lambda|->
block|{
name|ByteArrayInputStream
name|is
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
literal|"Bye World"
operator|.
name|getBytes
argument_list|()
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|is
argument_list|)
expr_stmt|;
block|}
argument_list|)
operator|.
name|log
argument_list|(
literal|"Got ${body}"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:bar"
argument_list|)
operator|.
name|id
argument_list|(
literal|"bar"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

