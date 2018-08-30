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

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|ManagedRegisterEndpointTest
specifier|public
class|class
name|ManagedRegisterEndpointTest
extends|extends
name|ManagementTestSupport
block|{
annotation|@
name|Test
DECL|method|testLookupEndpointsByName ()
specifier|public
name|void
name|testLookupEndpointsByName
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
name|name
init|=
name|ObjectName
operator|.
name|getInstance
argument_list|(
literal|"org.apache.camel:context=camel-1,type=endpoints,name=\"direct://start\""
argument_list|)
decl_stmt|;
name|String
name|uri
init|=
operator|(
name|String
operator|)
name|mbeanServer
operator|.
name|getAttribute
argument_list|(
name|name
argument_list|,
literal|"EndpointUri"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"direct://start"
argument_list|,
name|uri
argument_list|)
expr_stmt|;
name|name
operator|=
name|ObjectName
operator|.
name|getInstance
argument_list|(
literal|"org.apache.camel:context=camel-1,type=endpoints,name=\"log://foo\""
argument_list|)
expr_stmt|;
name|uri
operator|=
operator|(
name|String
operator|)
name|mbeanServer
operator|.
name|getAttribute
argument_list|(
name|name
argument_list|,
literal|"EndpointUri"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"log://foo"
argument_list|,
name|uri
argument_list|)
expr_stmt|;
name|name
operator|=
name|ObjectName
operator|.
name|getInstance
argument_list|(
literal|"org.apache.camel:context=camel-1,type=endpoints,name=\"mock://result\""
argument_list|)
expr_stmt|;
name|uri
operator|=
operator|(
name|String
operator|)
name|mbeanServer
operator|.
name|getAttribute
argument_list|(
name|name
argument_list|,
literal|"EndpointUri"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"mock://result"
argument_list|,
name|uri
argument_list|)
expr_stmt|;
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
name|name
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
name|String
name|state
init|=
operator|(
name|String
operator|)
name|mbeanServer
operator|.
name|getAttribute
argument_list|(
name|name
argument_list|,
literal|"State"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Started"
argument_list|,
name|state
argument_list|)
expr_stmt|;
name|Boolean
name|singleton
init|=
operator|(
name|Boolean
operator|)
name|mbeanServer
operator|.
name|getAttribute
argument_list|(
name|name
argument_list|,
literal|"Singleton"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Boolean
operator|.
name|TRUE
argument_list|,
name|singleton
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

