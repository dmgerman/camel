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
name|TestSupport
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
name|impl
operator|.
name|DefaultCamelContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|After
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
DECL|class|TwoManagedCamelContextTest
specifier|public
class|class
name|TwoManagedCamelContextTest
extends|extends
name|TestSupport
block|{
DECL|field|camel1
specifier|private
name|CamelContext
name|camel1
decl_stmt|;
DECL|field|camel2
specifier|private
name|CamelContext
name|camel2
decl_stmt|;
DECL|method|createCamelContext (String name)
specifier|protected
name|CamelContext
name|createCamelContext
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|Exception
block|{
name|DefaultCamelContext
name|context
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
name|context
operator|.
name|setName
argument_list|(
name|name
argument_list|)
expr_stmt|;
return|return
name|context
return|;
block|}
annotation|@
name|Test
DECL|method|testTwoManagedCamelContext ()
specifier|public
name|void
name|testTwoManagedCamelContext
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
name|camel1
operator|=
name|createCamelContext
argument_list|(
literal|"foo"
argument_list|)
expr_stmt|;
name|camel2
operator|=
name|createCamelContext
argument_list|(
literal|"bar"
argument_list|)
expr_stmt|;
name|camel1
operator|.
name|start
argument_list|()
expr_stmt|;
name|camel2
operator|.
name|start
argument_list|()
expr_stmt|;
name|MBeanServer
name|mbeanServer
init|=
name|camel1
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
name|on
init|=
name|ObjectName
operator|.
name|getInstance
argument_list|(
literal|"org.apache.camel:context=foo,type=context,name=\"foo\""
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
name|ObjectName
name|on2
init|=
name|ObjectName
operator|.
name|getInstance
argument_list|(
literal|"org.apache.camel:context=bar,type=context,name=\"bar\""
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
name|on2
argument_list|)
argument_list|)
expr_stmt|;
name|camel1
operator|.
name|stop
argument_list|()
expr_stmt|;
name|camel2
operator|.
name|stop
argument_list|()
expr_stmt|;
name|assertFalse
argument_list|(
literal|"Should be unregistered"
argument_list|,
name|mbeanServer
operator|.
name|isRegistered
argument_list|(
name|on
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"Should be unregistered"
argument_list|,
name|mbeanServer
operator|.
name|isRegistered
argument_list|(
name|on2
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
annotation|@
name|After
DECL|method|tearDown ()
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|camel1
operator|!=
literal|null
condition|)
block|{
name|camel1
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|camel2
operator|!=
literal|null
condition|)
block|{
name|camel2
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
name|super
operator|.
name|tearDown
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

