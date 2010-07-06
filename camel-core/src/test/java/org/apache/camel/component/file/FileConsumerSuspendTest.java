begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.file
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|file
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
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
name|ContextTestSupport
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
name|Route
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
name|impl
operator|.
name|RoutePolicySupport
import|;
end_import

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|FileConsumerSuspendTest
specifier|public
class|class
name|FileConsumerSuspendTest
extends|extends
name|ContextTestSupport
block|{
DECL|method|testConsumeSuspendFile ()
specifier|public
name|void
name|testConsumeSuspendFile
parameter_list|()
throws|throws
name|Exception
block|{
name|deleteDirectory
argument_list|(
literal|"target/suspended"
argument_list|)
expr_stmt|;
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
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"file://target/suspended"
argument_list|,
literal|"Bye World"
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"bye.txt"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"file://target/suspended"
argument_list|,
literal|"Hello World"
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"hello.txt"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
comment|// the route is suspended by the policy so we should only receive one
name|String
index|[]
name|files
init|=
operator|new
name|File
argument_list|(
literal|"target/suspended/"
argument_list|)
operator|.
name|getAbsoluteFile
argument_list|()
operator|.
name|list
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|files
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"The file should exists"
argument_list|,
literal|1
argument_list|,
name|files
operator|.
name|length
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
name|MyPolicy
name|myPolicy
init|=
operator|new
name|MyPolicy
argument_list|()
decl_stmt|;
name|from
argument_list|(
literal|"file://target/suspended?maxMessagesPerPoll=1&delete=true"
argument_list|)
operator|.
name|routePolicy
argument_list|(
name|myPolicy
argument_list|)
operator|.
name|id
argument_list|(
literal|"myRoute"
argument_list|)
operator|.
name|convertBodyTo
argument_list|(
name|String
operator|.
name|class
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
DECL|class|MyPolicy
specifier|private
class|class
name|MyPolicy
extends|extends
name|RoutePolicySupport
block|{
DECL|field|counter
specifier|private
name|int
name|counter
decl_stmt|;
DECL|method|onExchangeDone (Route route, Exchange exchange)
specifier|public
name|void
name|onExchangeDone
parameter_list|(
name|Route
name|route
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
comment|// only stop it at first run
if|if
condition|(
name|counter
operator|++
operator|==
literal|0
condition|)
block|{
try|try
block|{
name|super
operator|.
name|stopConsumer
argument_list|(
name|route
operator|.
name|getConsumer
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|handleException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
end_class

end_unit

