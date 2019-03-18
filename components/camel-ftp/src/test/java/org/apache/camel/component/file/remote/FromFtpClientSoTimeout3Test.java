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
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
name|test
operator|.
name|junit4
operator|.
name|CamelTestSupport
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
name|FTPFile
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
comment|/**  * Test re-creating operations  * @see {org.apache.camel.component.file.remote.RemoteFileConsumer#recoverableConnectIfNecessary}  */
end_comment

begin_class
DECL|class|FromFtpClientSoTimeout3Test
specifier|public
class|class
name|FromFtpClientSoTimeout3Test
extends|extends
name|CamelTestSupport
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
literal|"/timeout/?soTimeout=5000"
return|;
block|}
DECL|method|getPort ()
specifier|private
name|String
name|getPort
parameter_list|()
block|{
return|return
literal|"21"
return|;
block|}
annotation|@
name|Test
DECL|method|test ()
specifier|public
name|void
name|test
parameter_list|()
throws|throws
name|Exception
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|FtpEndpoint
argument_list|<
name|FTPFile
argument_list|>
name|ftpEndpoint
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
comment|// set "ftp://admin@localhost:21/timeout/?ftpClient.soTimeout=10"
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|ftpClientParameters
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|ftpClientParameters
operator|.
name|put
argument_list|(
literal|"soTimeout"
argument_list|,
literal|"10"
argument_list|)
expr_stmt|;
name|ftpEndpoint
operator|.
name|setFtpClientParameters
argument_list|(
name|ftpClientParameters
argument_list|)
expr_stmt|;
comment|// test RemoteFileConsumer#buildConsumer
name|assertEquals
argument_list|(
name|ftpClientParameters
operator|.
name|get
argument_list|(
literal|"soTimeout"
argument_list|)
argument_list|,
literal|"10"
argument_list|)
expr_stmt|;
name|ftpEndpoint
operator|.
name|createRemoteFileOperations
argument_list|()
expr_stmt|;
comment|// test RemoteFileConsumer#recoverableConnectIfNecessary
comment|// recover by re-creating operations which should most likely be able to recover
name|assertEquals
argument_list|(
name|ftpClientParameters
operator|.
name|get
argument_list|(
literal|"soTimeout"
argument_list|)
argument_list|,
literal|"10"
argument_list|)
expr_stmt|;
name|ftpEndpoint
operator|.
name|createRemoteFileOperations
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
block|}
end_class

end_unit

