begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|commons
operator|.
name|net
operator|.
name|ftp
operator|.
name|FTPClient
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
DECL|class|FtpConsumerDisconnectTest
specifier|public
class|class
name|FtpConsumerDisconnectTest
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
literal|"/done?password=admin&disconnect=true&delay=5000"
return|;
block|}
annotation|@
name|Override
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
comment|// force the singleton FtpEndpoint to make use of a custom FTPClient
name|FtpEndpoint
argument_list|<
name|?
argument_list|>
name|endpoint
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
name|getFtpUrl
argument_list|()
argument_list|,
name|FtpEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|endpoint
operator|.
name|setFtpClient
argument_list|(
operator|new
name|FTPClient
argument_list|()
argument_list|)
expr_stmt|;
name|sendFile
argument_list|(
name|getFtpUrl
argument_list|()
argument_list|,
literal|"Hello World"
argument_list|,
literal|"claus.txt"
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
name|getFtpUrl
argument_list|()
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
annotation|@
name|Test
DECL|method|testDisconnectOnDone ()
specifier|public
name|void
name|testDisconnectOnDone
parameter_list|()
throws|throws
name|Exception
block|{
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
comment|// give time for ftp consumer to disconnect (delay is 5000 which is long enough to avoid a second poll cycle)
name|Thread
operator|.
name|sleep
argument_list|(
literal|2000
argument_list|)
expr_stmt|;
name|FtpEndpoint
argument_list|<
name|?
argument_list|>
name|endpoint
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
name|getFtpUrl
argument_list|()
argument_list|,
name|FtpEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"The FtpEndpoint is configured to disconnect after each poll"
argument_list|,
name|endpoint
operator|.
name|isDisconnect
argument_list|()
argument_list|)
expr_stmt|;
name|FTPClient
name|ftpClient
init|=
name|endpoint
operator|.
name|getFtpClient
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
literal|"The FTPClient should not be connected"
argument_list|,
name|ftpClient
operator|.
name|isConnected
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

