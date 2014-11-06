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
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Properties
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
name|util
operator|.
name|StringHelper
import|;
end_import

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|ManagedCamelContextTest
specifier|public
class|class
name|ManagedCamelContextTest
extends|extends
name|ManagementTestSupport
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
comment|// to force a different management name than the camel id
name|context
operator|.
name|getManagementNameStrategy
argument_list|()
operator|.
name|setNamePattern
argument_list|(
literal|"19-#name#"
argument_list|)
expr_stmt|;
return|return
name|context
return|;
block|}
DECL|method|testManagedCamelContext ()
specifier|public
name|void
name|testManagedCamelContext
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
name|ObjectName
operator|.
name|getInstance
argument_list|(
literal|"org.apache.camel:context=19-camel-1,type=context,name=\"camel-1\""
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Should be registered"
argument_list|,
name|mbeanServer
operator|.
name|isRegistered
argument_list|(
name|on
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|name
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
literal|"CamelId"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"camel-1"
argument_list|,
name|name
argument_list|)
expr_stmt|;
name|String
name|managementName
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
literal|"ManagementName"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"19-camel-1"
argument_list|,
name|managementName
argument_list|)
expr_stmt|;
name|String
name|uptime
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
literal|"Uptime"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|uptime
argument_list|)
expr_stmt|;
name|String
name|status
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
literal|"State"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Started"
argument_list|,
name|status
argument_list|)
expr_stmt|;
name|Boolean
name|messageHistory
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
literal|"MessageHistory"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Boolean
operator|.
name|TRUE
argument_list|,
name|messageHistory
argument_list|)
expr_stmt|;
name|Integer
name|total
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
literal|"TotalRoutes"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|total
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
name|Integer
name|started
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
literal|"StartedRoutes"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|started
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
comment|// invoke operations
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Hello World"
argument_list|)
expr_stmt|;
name|mbeanServer
operator|.
name|invoke
argument_list|(
name|on
argument_list|,
literal|"sendBody"
argument_list|,
operator|new
name|Object
index|[]
block|{
literal|"direct:start"
block|,
literal|"Hello World"
block|}
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"java.lang.String"
block|,
literal|"java.lang.Object"
block|}
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|resetMocks
argument_list|()
expr_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Hello World"
argument_list|)
expr_stmt|;
name|mbeanServer
operator|.
name|invoke
argument_list|(
name|on
argument_list|,
literal|"sendStringBody"
argument_list|,
operator|new
name|Object
index|[]
block|{
literal|"direct:start"
block|,
literal|"Hello World"
block|}
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"java.lang.String"
block|,
literal|"java.lang.String"
block|}
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|Object
name|reply
init|=
name|mbeanServer
operator|.
name|invoke
argument_list|(
name|on
argument_list|,
literal|"requestBody"
argument_list|,
operator|new
name|Object
index|[]
block|{
literal|"direct:foo"
block|,
literal|"Hello World"
block|}
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"java.lang.String"
block|,
literal|"java.lang.Object"
block|}
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Bye World"
argument_list|,
name|reply
argument_list|)
expr_stmt|;
name|reply
operator|=
name|mbeanServer
operator|.
name|invoke
argument_list|(
name|on
argument_list|,
literal|"requestStringBody"
argument_list|,
operator|new
name|Object
index|[]
block|{
literal|"direct:foo"
block|,
literal|"Hello World"
block|}
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"java.lang.String"
block|,
literal|"java.lang.String"
block|}
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Bye World"
argument_list|,
name|reply
argument_list|)
expr_stmt|;
name|resetMocks
argument_list|()
expr_stmt|;
name|mock
operator|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Hello World"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedHeaderReceived
argument_list|(
literal|"foo"
argument_list|,
literal|123
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"foo"
argument_list|,
literal|123
argument_list|)
expr_stmt|;
name|mbeanServer
operator|.
name|invoke
argument_list|(
name|on
argument_list|,
literal|"sendBodyAndHeaders"
argument_list|,
operator|new
name|Object
index|[]
block|{
literal|"direct:start"
block|,
literal|"Hello World"
block|,
name|headers
block|}
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"java.lang.String"
block|,
literal|"java.lang.Object"
block|,
literal|"java.util.Map"
block|}
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|resetMocks
argument_list|()
expr_stmt|;
name|mock
operator|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Hello World"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedHeaderReceived
argument_list|(
literal|"foo"
argument_list|,
literal|123
argument_list|)
expr_stmt|;
name|reply
operator|=
name|mbeanServer
operator|.
name|invoke
argument_list|(
name|on
argument_list|,
literal|"requestBodyAndHeaders"
argument_list|,
operator|new
name|Object
index|[]
block|{
literal|"direct:start"
block|,
literal|"Hello World"
block|,
name|headers
block|}
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"java.lang.String"
block|,
literal|"java.lang.Object"
block|,
literal|"java.util.Map"
block|}
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Hello World"
argument_list|,
name|reply
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
comment|// test can send
name|Boolean
name|can
init|=
operator|(
name|Boolean
operator|)
name|mbeanServer
operator|.
name|invoke
argument_list|(
name|on
argument_list|,
literal|"canSendToEndpoint"
argument_list|,
operator|new
name|Object
index|[]
block|{
literal|"direct:start"
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
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|can
operator|.
name|booleanValue
argument_list|()
argument_list|)
expr_stmt|;
name|can
operator|=
operator|(
name|Boolean
operator|)
name|mbeanServer
operator|.
name|invoke
argument_list|(
name|on
argument_list|,
literal|"canSendToEndpoint"
argument_list|,
operator|new
name|Object
index|[]
block|{
literal|"timer:foo"
block|}
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"java.lang.String"
block|}
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|can
operator|.
name|booleanValue
argument_list|()
argument_list|)
expr_stmt|;
comment|// stop Camel
name|mbeanServer
operator|.
name|invoke
argument_list|(
name|on
argument_list|,
literal|"stop"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|testManagedCamelContextCreateEndpoint ()
specifier|public
name|void
name|testManagedCamelContextCreateEndpoint
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
name|ObjectName
operator|.
name|getInstance
argument_list|(
literal|"org.apache.camel:context=19-camel-1,type=context,name=\"camel-1\""
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|context
operator|.
name|hasEndpoint
argument_list|(
literal|"seda:bar"
argument_list|)
argument_list|)
expr_stmt|;
comment|// create a new endpoint
name|Object
name|reply
init|=
name|mbeanServer
operator|.
name|invoke
argument_list|(
name|on
argument_list|,
literal|"createEndpoint"
argument_list|,
operator|new
name|Object
index|[]
block|{
literal|"seda:bar"
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
name|assertEquals
argument_list|(
name|Boolean
operator|.
name|TRUE
argument_list|,
name|reply
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|context
operator|.
name|hasEndpoint
argument_list|(
literal|"seda:bar"
argument_list|)
argument_list|)
expr_stmt|;
name|ObjectName
name|seda
init|=
name|ObjectName
operator|.
name|getInstance
argument_list|(
literal|"org.apache.camel:context=19-camel-1,type=endpoints,name=\"seda://bar\""
argument_list|)
decl_stmt|;
name|boolean
name|registered
init|=
name|mbeanServer
operator|.
name|isRegistered
argument_list|(
name|seda
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Should be registered "
operator|+
name|seda
argument_list|,
name|registered
argument_list|)
expr_stmt|;
comment|// create it again
name|reply
operator|=
name|mbeanServer
operator|.
name|invoke
argument_list|(
name|on
argument_list|,
literal|"createEndpoint"
argument_list|,
operator|new
name|Object
index|[]
block|{
literal|"seda:bar"
block|}
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"java.lang.String"
block|}
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Boolean
operator|.
name|FALSE
argument_list|,
name|reply
argument_list|)
expr_stmt|;
name|registered
operator|=
name|mbeanServer
operator|.
name|isRegistered
argument_list|(
name|seda
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Should be registered "
operator|+
name|seda
argument_list|,
name|registered
argument_list|)
expr_stmt|;
block|}
DECL|method|testManagedCamelContextRemoveEndpoint ()
specifier|public
name|void
name|testManagedCamelContextRemoveEndpoint
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
name|ObjectName
operator|.
name|getInstance
argument_list|(
literal|"org.apache.camel:context=19-camel-1,type=context,name=\"camel-1\""
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|context
operator|.
name|hasEndpoint
argument_list|(
literal|"seda:bar"
argument_list|)
argument_list|)
expr_stmt|;
comment|// create a new endpoint
name|Object
name|reply
init|=
name|mbeanServer
operator|.
name|invoke
argument_list|(
name|on
argument_list|,
literal|"createEndpoint"
argument_list|,
operator|new
name|Object
index|[]
block|{
literal|"seda:bar"
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
name|assertEquals
argument_list|(
name|Boolean
operator|.
name|TRUE
argument_list|,
name|reply
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|context
operator|.
name|hasEndpoint
argument_list|(
literal|"seda:bar"
argument_list|)
argument_list|)
expr_stmt|;
name|ObjectName
name|seda
init|=
name|ObjectName
operator|.
name|getInstance
argument_list|(
literal|"org.apache.camel:context=19-camel-1,type=endpoints,name=\"seda://bar\""
argument_list|)
decl_stmt|;
name|boolean
name|registered
init|=
name|mbeanServer
operator|.
name|isRegistered
argument_list|(
name|seda
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Should be registered "
operator|+
name|seda
argument_list|,
name|registered
argument_list|)
expr_stmt|;
comment|// remove it
name|Object
name|num
init|=
name|mbeanServer
operator|.
name|invoke
argument_list|(
name|on
argument_list|,
literal|"removeEndpoints"
argument_list|,
operator|new
name|Object
index|[]
block|{
literal|"seda:*"
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
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|num
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|context
operator|.
name|hasEndpoint
argument_list|(
literal|"seda:bar"
argument_list|)
argument_list|)
expr_stmt|;
name|registered
operator|=
name|mbeanServer
operator|.
name|isRegistered
argument_list|(
name|seda
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"Should not be registered "
operator|+
name|seda
argument_list|,
name|registered
argument_list|)
expr_stmt|;
comment|// remove it again
name|num
operator|=
name|mbeanServer
operator|.
name|invoke
argument_list|(
name|on
argument_list|,
literal|"removeEndpoints"
argument_list|,
operator|new
name|Object
index|[]
block|{
literal|"seda:*"
block|}
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"java.lang.String"
block|}
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|num
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|context
operator|.
name|hasEndpoint
argument_list|(
literal|"seda:bar"
argument_list|)
argument_list|)
expr_stmt|;
name|registered
operator|=
name|mbeanServer
operator|.
name|isRegistered
argument_list|(
name|seda
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"Should not be registered "
operator|+
name|seda
argument_list|,
name|registered
argument_list|)
expr_stmt|;
block|}
DECL|method|testFindComponentsInClasspath ()
specifier|public
name|void
name|testFindComponentsInClasspath
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
name|ObjectName
operator|.
name|getInstance
argument_list|(
literal|"org.apache.camel:context=19-camel-1,type=context,name=\"camel-1\""
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Should be registered"
argument_list|,
name|mbeanServer
operator|.
name|isRegistered
argument_list|(
name|on
argument_list|)
argument_list|)
expr_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|Map
argument_list|<
name|String
argument_list|,
name|Properties
argument_list|>
name|info
init|=
operator|(
name|Map
argument_list|<
name|String
argument_list|,
name|Properties
argument_list|>
operator|)
name|mbeanServer
operator|.
name|invoke
argument_list|(
name|on
argument_list|,
literal|"findComponents"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|info
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|23
argument_list|,
name|info
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Properties
name|prop
init|=
name|info
operator|.
name|get
argument_list|(
literal|"seda"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|prop
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"seda"
argument_list|,
name|prop
operator|.
name|get
argument_list|(
literal|"name"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"org.apache.camel"
argument_list|,
name|prop
operator|.
name|get
argument_list|(
literal|"groupId"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"camel-core"
argument_list|,
name|prop
operator|.
name|get
argument_list|(
literal|"artifactId"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testManagedCamelContextCreateRouteStaticEndpointJson ()
specifier|public
name|void
name|testManagedCamelContextCreateRouteStaticEndpointJson
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
name|ObjectName
operator|.
name|getInstance
argument_list|(
literal|"org.apache.camel:context=19-camel-1,type=context,name=\"camel-1\""
argument_list|)
decl_stmt|;
comment|// get the json
name|String
name|json
init|=
operator|(
name|String
operator|)
name|mbeanServer
operator|.
name|invoke
argument_list|(
name|on
argument_list|,
literal|"createRouteStaticEndpointJson"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|json
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|7
argument_list|,
name|StringHelper
operator|.
name|countChar
argument_list|(
name|json
argument_list|,
literal|'{'
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|7
argument_list|,
name|StringHelper
operator|.
name|countChar
argument_list|(
name|json
argument_list|,
literal|'}'
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|json
operator|.
name|contains
argument_list|(
literal|"{ \"uri\": \"direct://start\" }"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|json
operator|.
name|contains
argument_list|(
literal|"{ \"uri\": \"direct://foo\" }"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testManagedCamelContextExplainEndpointUri ()
specifier|public
name|void
name|testManagedCamelContextExplainEndpointUri
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
name|ObjectName
operator|.
name|getInstance
argument_list|(
literal|"org.apache.camel:context=19-camel-1,type=context,name=\"camel-1\""
argument_list|)
decl_stmt|;
comment|// get the json
name|String
name|json
init|=
operator|(
name|String
operator|)
name|mbeanServer
operator|.
name|invoke
argument_list|(
name|on
argument_list|,
literal|"explainEndpointJson"
argument_list|,
operator|new
name|Object
index|[]
block|{
literal|"log:foo?groupDelay=2000&groupSize=5"
block|,
literal|false
block|}
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"java.lang.String"
block|,
literal|"boolean"
block|}
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|json
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|json
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|StringHelper
operator|.
name|countChar
argument_list|(
name|json
argument_list|,
literal|'{'
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|StringHelper
operator|.
name|countChar
argument_list|(
name|json
argument_list|,
literal|'}'
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|json
operator|.
name|contains
argument_list|(
literal|"\"groupDelay\": { \"value\": \"2000\", \"description\": \"Set the initial delay for stats in millis\" },"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|json
operator|.
name|contains
argument_list|(
literal|"\"groupSize\": { \"value\": \"5\", \"description\": \"An integer that specifies a group size for throughput logging.\" }"
argument_list|)
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
name|from
argument_list|(
literal|"direct:foo"
argument_list|)
operator|.
name|transform
argument_list|(
name|constant
argument_list|(
literal|"Bye World"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

