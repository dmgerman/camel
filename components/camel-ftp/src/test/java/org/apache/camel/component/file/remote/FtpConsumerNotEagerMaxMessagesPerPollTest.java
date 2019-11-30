begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.file.remote
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
operator|.
name|remote
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
name|junit
operator|.
name|jupiter
operator|.
name|api
operator|.
name|BeforeEach
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|jupiter
operator|.
name|api
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|FtpConsumerNotEagerMaxMessagesPerPollTest
specifier|public
class|class
name|FtpConsumerNotEagerMaxMessagesPerPollTest
extends|extends
name|FtpServerTestSupport
block|{
DECL|method|getFtpUrl ()
specifier|private
name|String
name|getFtpUrl
parameter_list|()
block|{
return|return
literal|"ftp://admin@localhost:"
operator|+
name|getPort
argument_list|()
operator|+
literal|"/poll/?password=admin&delay=6000&delete=true"
operator|+
literal|"&sortBy=file:name&maxMessagesPerPoll=2&eagerMaxMessagesPerPoll=false"
return|;
block|}
annotation|@
name|Override
annotation|@
name|BeforeEach
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|prepareFtpServer
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testMaxMessagesPerPoll ()
specifier|public
name|void
name|testMaxMessagesPerPoll
parameter_list|()
throws|throws
name|Exception
block|{
comment|// start route
name|context
operator|.
name|getRouteController
argument_list|()
operator|.
name|startRoute
argument_list|(
literal|"foo"
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
name|expectedBodiesReceived
argument_list|(
literal|"AAA"
argument_list|,
literal|"BBB"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|setResultWaitTime
argument_list|(
literal|4000
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedPropertyReceived
argument_list|(
name|Exchange
operator|.
name|BATCH_SIZE
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|mock
operator|.
name|reset
argument_list|()
expr_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"CCC"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedPropertyReceived
argument_list|(
name|Exchange
operator|.
name|BATCH_SIZE
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|method|prepareFtpServer ()
specifier|private
name|void
name|prepareFtpServer
parameter_list|()
throws|throws
name|Exception
block|{
name|sendFile
argument_list|(
name|getFtpUrl
argument_list|()
argument_list|,
literal|"CCC"
argument_list|,
literal|"ccc.txt"
argument_list|)
expr_stmt|;
name|sendFile
argument_list|(
name|getFtpUrl
argument_list|()
argument_list|,
literal|"AAA"
argument_list|,
literal|"aaa.txt"
argument_list|)
expr_stmt|;
name|sendFile
argument_list|(
name|getFtpUrl
argument_list|()
argument_list|,
literal|"BBB"
argument_list|,
literal|"bbb.txt"
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
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
name|getFtpUrl
argument_list|()
argument_list|)
operator|.
name|noAutoStartup
argument_list|()
operator|.
name|routeId
argument_list|(
literal|"foo"
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

