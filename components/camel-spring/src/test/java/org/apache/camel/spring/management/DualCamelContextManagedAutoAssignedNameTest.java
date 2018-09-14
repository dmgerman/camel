begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring.management
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
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
name|Iterator
import|;
end_import

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
name|junit
operator|.
name|Test
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|support
operator|.
name|AbstractXmlApplicationContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|support
operator|.
name|ClassPathXmlApplicationContext
import|;
end_import

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|DualCamelContextManagedAutoAssignedNameTest
specifier|public
class|class
name|DualCamelContextManagedAutoAssignedNameTest
extends|extends
name|DualCamelContextManagedTest
block|{
DECL|method|createApplicationContext ()
specifier|protected
name|AbstractXmlApplicationContext
name|createApplicationContext
parameter_list|()
block|{
return|return
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
literal|"org/apache/camel/spring/management/dualCamelContextManagedAutoAssignedNameTest.xml"
argument_list|)
return|;
block|}
annotation|@
name|Test
DECL|method|testDualCamelContextManaged ()
specifier|public
name|void
name|testDualCamelContextManaged
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
literal|"*:type=routes,*"
argument_list|)
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|set
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Iterator
argument_list|<
name|ObjectName
argument_list|>
name|it
init|=
name|set
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|ObjectName
name|on1
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|ObjectName
name|on2
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Route 1 is missing"
argument_list|,
name|on1
operator|.
name|getCanonicalName
argument_list|()
operator|.
name|contains
argument_list|(
literal|"route1"
argument_list|)
operator|||
name|on2
operator|.
name|getCanonicalName
argument_list|()
operator|.
name|contains
argument_list|(
literal|"route1"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Route 2 is missing"
argument_list|,
name|on1
operator|.
name|getCanonicalName
argument_list|()
operator|.
name|contains
argument_list|(
literal|"route2"
argument_list|)
operator|||
name|on2
operator|.
name|getCanonicalName
argument_list|()
operator|.
name|contains
argument_list|(
literal|"route2"
argument_list|)
argument_list|)
expr_stmt|;
name|set
operator|=
name|mbeanServer
operator|.
name|queryNames
argument_list|(
operator|new
name|ObjectName
argument_list|(
literal|"*:type=endpoints,*"
argument_list|)
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Size should be 4 or higher, was: "
operator|+
name|set
operator|.
name|size
argument_list|()
argument_list|,
name|set
operator|.
name|size
argument_list|()
operator|>=
literal|4
argument_list|)
expr_stmt|;
for|for
control|(
name|ObjectName
name|on
range|:
name|set
control|)
block|{
name|String
name|name
init|=
name|on
operator|.
name|getCanonicalName
argument_list|()
decl_stmt|;
if|if
condition|(
name|name
operator|.
name|contains
argument_list|(
literal|"mock://mock1"
argument_list|)
condition|)
block|{
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
literal|"CamelId"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"camel-1"
argument_list|,
name|id
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|name
operator|.
name|contains
argument_list|(
literal|"mock://mock2"
argument_list|)
condition|)
block|{
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
literal|"CamelId"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"camel-2"
argument_list|,
name|id
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|name
operator|.
name|contains
argument_list|(
literal|"file://target/route1"
argument_list|)
condition|)
block|{
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
literal|"CamelId"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"camel-1"
argument_list|,
name|id
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|name
operator|.
name|contains
argument_list|(
literal|"file://target/route2"
argument_list|)
condition|)
block|{
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
literal|"CamelId"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"camel-2"
argument_list|,
name|id
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

