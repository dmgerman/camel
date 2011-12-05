begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.seda
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|seda
package|;
end_package

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
name|ShutdownRunningTask
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

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|FileSedaShutdownCompleteAllTasksTest
specifier|public
class|class
name|FileSedaShutdownCompleteAllTasksTest
extends|extends
name|ContextTestSupport
block|{
annotation|@
name|Override
DECL|method|setUp ()
specifier|protected
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|deleteDirectory
argument_list|(
literal|"target/seda"
argument_list|)
expr_stmt|;
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
block|}
DECL|method|testShutdownCompleteAllTasks ()
specifier|public
name|void
name|testShutdownCompleteAllTasks
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|String
name|url
init|=
literal|"file:target/seda"
decl_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
name|url
argument_list|,
literal|"A"
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"a.txt"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
name|url
argument_list|,
literal|"B"
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"b.txt"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
name|url
argument_list|,
literal|"C"
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"c.txt"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
name|url
argument_list|,
literal|"D"
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"d.txt"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
name|url
argument_list|,
literal|"E"
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"e.txt"
argument_list|)
expr_stmt|;
comment|// give it 20 seconds to shutdown
name|context
operator|.
name|getShutdownStrategy
argument_list|()
operator|.
name|setTimeout
argument_list|(
literal|20
argument_list|)
expr_stmt|;
name|context
operator|.
name|addRoutes
argument_list|(
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
name|url
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"route1"
argument_list|)
comment|// let it complete all tasks during shutdown
operator|.
name|shutdownRunningTask
argument_list|(
name|ShutdownRunningTask
operator|.
name|CompleteAllTasks
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:delay"
argument_list|)
operator|.
name|delay
argument_list|(
literal|1000
argument_list|)
operator|.
name|to
argument_list|(
literal|"seda:foo"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"seda:foo"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"route2"
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:bar"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:bar"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|MockEndpoint
name|bar
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:bar"
argument_list|)
decl_stmt|;
name|bar
operator|.
name|expectedMinimumMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
comment|// shutdown during processing
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
comment|// should route all 5
name|assertEquals
argument_list|(
literal|"Should complete all messages"
argument_list|,
literal|5
argument_list|,
name|bar
operator|.
name|getReceivedCounter
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|isUseRouteBuilder ()
specifier|public
name|boolean
name|isUseRouteBuilder
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
block|}
end_class

end_unit

