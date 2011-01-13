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
name|builder
operator|.
name|RouteBuilder
import|;
end_import

begin_comment
comment|/**  * @version $Revision: 1005721 $  */
end_comment

begin_class
DECL|class|DualManagedThreadPoolWithIdTest
specifier|public
class|class
name|DualManagedThreadPoolWithIdTest
extends|extends
name|ManagementTestSupport
block|{
DECL|method|testManagedThreadPool ()
specifier|public
name|void
name|testManagedThreadPool
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
name|on
init|=
name|ObjectName
operator|.
name|getInstance
argument_list|(
literal|"org.apache.camel:context=localhost/camel-1,type=threadpools,name=myThreads(threads)"
argument_list|)
decl_stmt|;
name|Integer
name|corePoolSize
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
literal|"CorePoolSize"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|15
argument_list|,
name|corePoolSize
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
name|Integer
name|maxPoolSize
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
literal|"MaximumPoolSize"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|30
argument_list|,
name|maxPoolSize
operator|.
name|intValue
argument_list|()
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
name|on
argument_list|,
literal|"Id"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"myThreads"
argument_list|,
name|id
argument_list|)
expr_stmt|;
name|String
name|source
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
literal|"SourceId"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"threads"
argument_list|,
name|source
argument_list|)
expr_stmt|;
name|String
name|route
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
literal|"RouteId"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"route1"
argument_list|,
name|route
argument_list|)
expr_stmt|;
name|on
operator|=
name|ObjectName
operator|.
name|getInstance
argument_list|(
literal|"org.apache.camel:context=localhost/camel-1,type=threadpools,name=myOtherThreads(threads)"
argument_list|)
expr_stmt|;
name|corePoolSize
operator|=
operator|(
name|Integer
operator|)
name|mbeanServer
operator|.
name|getAttribute
argument_list|(
name|on
argument_list|,
literal|"CorePoolSize"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|corePoolSize
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
name|maxPoolSize
operator|=
operator|(
name|Integer
operator|)
name|mbeanServer
operator|.
name|getAttribute
argument_list|(
name|on
argument_list|,
literal|"MaximumPoolSize"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|maxPoolSize
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
name|id
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
literal|"Id"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"myOtherThreads"
argument_list|,
name|id
argument_list|)
expr_stmt|;
name|source
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
literal|"SourceId"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"threads"
argument_list|,
name|source
argument_list|)
expr_stmt|;
name|route
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
literal|"RouteId"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"route2"
argument_list|,
name|route
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
name|threads
argument_list|(
literal|15
argument_list|,
literal|30
argument_list|)
operator|.
name|id
argument_list|(
literal|"myThreads"
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
name|threads
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|)
operator|.
name|id
argument_list|(
literal|"myOtherThreads"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:foo"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

