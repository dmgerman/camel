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
DECL|class|ManagedErrorHandlerTest
specifier|public
class|class
name|ManagedErrorHandlerTest
extends|extends
name|ManagementTestSupport
block|{
annotation|@
name|Test
DECL|method|testManagedErrorHandler ()
specifier|public
name|void
name|testManagedErrorHandler
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
literal|"*:type=errorhandlers,*"
argument_list|)
argument_list|,
literal|null
argument_list|)
decl_stmt|;
comment|// there should only be 2 error handler types as route 1 and route 3 uses the same default error handler
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
name|String
name|name1
init|=
name|on1
operator|.
name|getCanonicalName
argument_list|()
decl_stmt|;
name|String
name|name2
init|=
name|on2
operator|.
name|getCanonicalName
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Should be a default error handler"
argument_list|,
name|name1
operator|.
name|contains
argument_list|(
literal|"CamelDefaultErrorHandlerBuilder"
argument_list|)
operator|||
name|name2
operator|.
name|contains
argument_list|(
literal|"CamelDefaultErrorHandlerBuilder"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Should be a dead letter error handler"
argument_list|,
name|name1
operator|.
name|contains
argument_list|(
literal|"DeadLetterChannelBuilder"
argument_list|)
operator|||
name|name2
operator|.
name|contains
argument_list|(
literal|"DeadLetterChannelBuilder"
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
literal|"direct:foo"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:foo"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:bar"
argument_list|)
operator|.
name|errorHandler
argument_list|(
name|deadLetterChannel
argument_list|(
literal|"mock:dead"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:bar"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:baz"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:baz"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

