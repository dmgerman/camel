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
name|Endpoint
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
DECL|class|ManagedEndpointTest
specifier|public
class|class
name|ManagedEndpointTest
extends|extends
name|ManagementTestSupport
block|{
DECL|method|testManageEndpoint ()
specifier|public
name|void
name|testManageEndpoint
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
literal|"org.apache.camel:context=localhost/camel-1,type=endpoints,name=\"seda://test\""
argument_list|)
decl_stmt|;
name|mbeanServer
operator|.
name|isRegistered
argument_list|(
name|on
argument_list|)
expr_stmt|;
name|on
operator|=
name|ObjectName
operator|.
name|getInstance
argument_list|(
literal|"org.apache.camel:context=localhost/camel-1,type=endpoints,name=\"mock://result\""
argument_list|)
expr_stmt|;
name|mbeanServer
operator|.
name|isRegistered
argument_list|(
name|on
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
name|Endpoint
name|result
init|=
name|endpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|from
argument_list|(
literal|"seda:test"
argument_list|)
operator|.
name|to
argument_list|(
name|result
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

