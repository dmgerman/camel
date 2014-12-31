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
name|List
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
name|javax
operator|.
name|management
operator|.
name|openmbean
operator|.
name|TabularData
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
name|management
operator|.
name|ManagedCamelContextTest
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
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|processor
operator|.
name|SpringTestHelper
operator|.
name|createSpringCamelContext
import|;
end_import

begin_class
DECL|class|SpringManagedCamelContextTest
specifier|public
class|class
name|SpringManagedCamelContextTest
extends|extends
name|ManagedCamelContextTest
block|{
DECL|method|createCamelContext ()
specifier|protected
name|CamelContext
name|createCamelContext
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|createSpringCamelContext
argument_list|(
name|this
argument_list|,
literal|"org/apache/camel/spring/management/SpringManagedCamelContextTest.xml"
argument_list|)
return|;
block|}
annotation|@
name|Test
DECL|method|testFindEipNames ()
specifier|public
name|void
name|testFindEipNames
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
name|List
argument_list|<
name|String
argument_list|>
name|info
init|=
operator|(
name|List
argument_list|<
name|String
argument_list|>
operator|)
name|mbeanServer
operator|.
name|invoke
argument_list|(
name|on
argument_list|,
literal|"findEipNames"
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
literal|152
argument_list|,
name|info
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|info
operator|.
name|contains
argument_list|(
literal|"transform"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|info
operator|.
name|contains
argument_list|(
literal|"split"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|info
operator|.
name|contains
argument_list|(
literal|"from"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testFindEips ()
specifier|public
name|void
name|testFindEips
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
literal|"findEips"
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
literal|152
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
literal|"transform"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|prop
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"transform"
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
literal|"org.apache.camel.model.TransformDefinition"
argument_list|,
name|prop
operator|.
name|get
argument_list|(
literal|"class"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testListEips ()
specifier|public
name|void
name|testListEips
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
name|TabularData
name|data
init|=
operator|(
name|TabularData
operator|)
name|mbeanServer
operator|.
name|invoke
argument_list|(
name|on
argument_list|,
literal|"listEips"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|data
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|152
argument_list|,
name|data
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

