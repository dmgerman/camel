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
DECL|class|ManagedSetAndRemoveHeaderAndPropertiesTest
specifier|public
class|class
name|ManagedSetAndRemoveHeaderAndPropertiesTest
extends|extends
name|ManagementTestSupport
block|{
annotation|@
name|Test
DECL|method|testSetAndRemove ()
specifier|public
name|void
name|testSetAndRemove
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
comment|// fire a message to get it running
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
name|MBeanServer
name|mbeanServer
init|=
name|getMBeanServer
argument_list|()
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
literal|"*:type=processors,*"
argument_list|)
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|8
argument_list|,
name|set
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|boolean
name|found
init|=
literal|false
decl_stmt|;
name|boolean
name|found2
init|=
literal|false
decl_stmt|;
name|boolean
name|found3
init|=
literal|false
decl_stmt|;
name|boolean
name|found4
init|=
literal|false
decl_stmt|;
name|boolean
name|found5
init|=
literal|false
decl_stmt|;
name|boolean
name|found6
init|=
literal|false
decl_stmt|;
for|for
control|(
name|ObjectName
name|on
range|:
name|set
control|)
block|{
name|boolean
name|registered
init|=
name|mbeanServer
operator|.
name|isRegistered
argument_list|(
name|on
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Should be registered"
argument_list|,
literal|true
argument_list|,
name|registered
argument_list|)
expr_stmt|;
comment|// should be one with name setFoo
name|String
name|id
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
literal|"ProcessorId"
argument_list|)
decl_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"id = {}"
argument_list|,
name|id
argument_list|)
expr_stmt|;
name|found
operator||=
literal|"setFoo"
operator|.
name|equals
argument_list|(
name|id
argument_list|)
expr_stmt|;
name|found2
operator||=
literal|"setBeer"
operator|.
name|equals
argument_list|(
name|id
argument_list|)
expr_stmt|;
name|found3
operator||=
literal|"unsetFoo"
operator|.
name|equals
argument_list|(
name|id
argument_list|)
expr_stmt|;
name|found4
operator||=
literal|"unsetFoos"
operator|.
name|equals
argument_list|(
name|id
argument_list|)
expr_stmt|;
name|found5
operator||=
literal|"unsetBeer"
operator|.
name|equals
argument_list|(
name|id
argument_list|)
expr_stmt|;
name|found6
operator||=
literal|"unsetBeers"
operator|.
name|equals
argument_list|(
name|id
argument_list|)
expr_stmt|;
block|}
name|assertTrue
argument_list|(
literal|"Should find setHeader mbean"
argument_list|,
name|found
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Should find setProperty mbean"
argument_list|,
name|found2
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Should find removeHeader mbean"
argument_list|,
name|found3
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Should find removeHeaders mbean"
argument_list|,
name|found4
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Should find removeProperty mbean"
argument_list|,
name|found5
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Should find removeProperty mbean"
argument_list|,
name|found6
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
name|routeId
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|setHeader
argument_list|(
literal|"foo"
argument_list|,
name|constant
argument_list|(
literal|"bar"
argument_list|)
argument_list|)
operator|.
name|id
argument_list|(
literal|"setFoo"
argument_list|)
operator|.
name|setProperty
argument_list|(
literal|"beer"
argument_list|,
name|constant
argument_list|(
literal|"yes"
argument_list|)
argument_list|)
operator|.
name|id
argument_list|(
literal|"setBeer"
argument_list|)
operator|.
name|removeHeader
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|id
argument_list|(
literal|"unsetFoo"
argument_list|)
operator|.
name|removeHeaders
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|id
argument_list|(
literal|"unsetFoos"
argument_list|)
operator|.
name|removeProperty
argument_list|(
literal|"beer"
argument_list|)
operator|.
name|id
argument_list|(
literal|"unsetBeer"
argument_list|)
operator|.
name|removeProperties
argument_list|(
literal|"beer"
argument_list|)
operator|.
name|id
argument_list|(
literal|"unsetBeers"
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:foo"
argument_list|)
operator|.
name|id
argument_list|(
literal|"logFoo"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|id
argument_list|(
literal|"mockResult"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

