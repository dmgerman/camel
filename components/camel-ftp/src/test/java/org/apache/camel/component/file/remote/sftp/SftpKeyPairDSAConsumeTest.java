begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.file.remote.sftp
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
operator|.
name|sftp
package|;
end_package

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|KeyPair
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|KeyPairGenerator
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
name|sshd
operator|.
name|server
operator|.
name|auth
operator|.
name|pubkey
operator|.
name|PublickeyAuthenticator
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|BeforeClass
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
DECL|class|SftpKeyPairDSAConsumeTest
specifier|public
class|class
name|SftpKeyPairDSAConsumeTest
extends|extends
name|SftpServerTestSupport
block|{
DECL|field|keyPair
specifier|private
specifier|static
name|KeyPair
name|keyPair
decl_stmt|;
annotation|@
name|BeforeClass
DECL|method|createKeys ()
specifier|public
specifier|static
name|void
name|createKeys
parameter_list|()
throws|throws
name|Exception
block|{
name|KeyPairGenerator
name|keyGen
init|=
name|KeyPairGenerator
operator|.
name|getInstance
argument_list|(
literal|"DSA"
argument_list|)
decl_stmt|;
name|keyGen
operator|.
name|initialize
argument_list|(
literal|1024
argument_list|)
expr_stmt|;
name|keyPair
operator|=
name|keyGen
operator|.
name|generateKeyPair
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSftpSimpleConsume ()
specifier|public
name|void
name|testSftpSimpleConsume
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|canTest
argument_list|()
condition|)
block|{
return|return;
block|}
name|String
name|expected
init|=
literal|"Hello World"
decl_stmt|;
comment|// create file using regular file
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"file://"
operator|+
name|FTP_ROOT_DIR
argument_list|,
name|expected
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"hello.txt"
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
name|mock
operator|.
name|expectedHeaderReceived
argument_list|(
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"hello.txt"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
name|expected
argument_list|)
expr_stmt|;
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
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getPublickeyAuthenticator ()
specifier|protected
name|PublickeyAuthenticator
name|getPublickeyAuthenticator
parameter_list|()
block|{
return|return
parameter_list|(
name|username
parameter_list|,
name|key
parameter_list|,
name|session
parameter_list|)
lambda|->
name|key
operator|.
name|equals
argument_list|(
name|keyPair
operator|.
name|getPublic
argument_list|()
argument_list|)
return|;
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
name|context
operator|.
name|getRegistry
argument_list|()
operator|.
name|bind
argument_list|(
literal|"keyPair"
argument_list|,
name|keyPair
argument_list|)
expr_stmt|;
name|context
operator|.
name|getRegistry
argument_list|()
operator|.
name|bind
argument_list|(
literal|"knownHosts"
argument_list|,
name|buildKnownHosts
argument_list|()
argument_list|)
expr_stmt|;
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
literal|"sftp://localhost:"
operator|+
name|getPort
argument_list|()
operator|+
literal|"/"
operator|+
name|FTP_ROOT_DIR
operator|+
literal|"?username=admin&knownHosts=#knownHosts&keyPair=#keyPair&delay=10s&strictHostKeyChecking=yes&disconnect=true"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|noAutoStartup
argument_list|()
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

