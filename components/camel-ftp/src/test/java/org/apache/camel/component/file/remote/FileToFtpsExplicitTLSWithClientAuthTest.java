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
name|Test
import|;
end_import

begin_comment
comment|/**  * Test the ftps component over TLS (explicit) with client authentication  */
end_comment

begin_class
DECL|class|FileToFtpsExplicitTLSWithClientAuthTest
specifier|public
class|class
name|FileToFtpsExplicitTLSWithClientAuthTest
extends|extends
name|FtpsServerExplicitTLSWithClientAuthTestSupport
block|{
DECL|method|getFtpUrl ()
specifier|protected
name|String
name|getFtpUrl
parameter_list|()
block|{
return|return
literal|"ftps://admin@localhost:"
operator|+
name|getPort
argument_list|()
operator|+
literal|"/tmp2/camel?password=admin&initialDelay=2000&disableSecureDataChannelDefaults=true"
operator|+
literal|"&securityProtocol=TLSv1.2&implicit=false&ftpClient.keyStore.file=./src/test/resources/server.jks&ftpClient.keyStore.type=JKS"
operator|+
literal|"&ftpClient.keyStore.algorithm=SunX509&ftpClient.keyStore.password=password&ftpClient.keyStore.keyPassword=password&delete=true"
return|;
block|}
annotation|@
name|Test
DECL|method|testFromFileToFtp ()
specifier|public
name|void
name|testFromFileToFtp
parameter_list|()
throws|throws
name|Exception
block|{
comment|// some platforms cannot test SSL
if|if
condition|(
operator|!
name|canTest
condition|)
block|{
return|return;
block|}
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
literal|2
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
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
literal|"file:src/main/data?noop=true"
argument_list|)
operator|.
name|log
argument_list|(
literal|"Got ${file:name}"
argument_list|)
operator|.
name|to
argument_list|(
name|getFtpUrl
argument_list|()
argument_list|)
expr_stmt|;
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
block|}
end_class

end_unit

