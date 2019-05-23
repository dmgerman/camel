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
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|com
operator|.
name|jcraft
operator|.
name|jsch
operator|.
name|ProxyHTTP
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
name|impl
operator|.
name|JndiRegistry
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
name|AvailablePortFinder
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

begin_import
import|import
name|org
operator|.
name|littleshoot
operator|.
name|proxy
operator|.
name|DefaultHttpProxyServer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|littleshoot
operator|.
name|proxy
operator|.
name|HttpProxyServer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|littleshoot
operator|.
name|proxy
operator|.
name|ProxyAuthorizationHandler
import|;
end_import

begin_class
DECL|class|SftpSimpleProduceThroughProxyTest
specifier|public
class|class
name|SftpSimpleProduceThroughProxyTest
extends|extends
name|SftpServerTestSupport
block|{
DECL|field|proxyPort
specifier|private
specifier|final
name|int
name|proxyPort
init|=
name|AvailablePortFinder
operator|.
name|getNextAvailable
argument_list|(
literal|25000
argument_list|)
decl_stmt|;
annotation|@
name|Test
DECL|method|testSftpSimpleProduceThroughProxy ()
specifier|public
name|void
name|testSftpSimpleProduceThroughProxy
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
comment|// start http proxy
name|HttpProxyServer
name|proxyServer
init|=
operator|new
name|DefaultHttpProxyServer
argument_list|(
name|proxyPort
argument_list|)
decl_stmt|;
name|proxyServer
operator|.
name|addProxyAuthenticationHandler
argument_list|(
operator|new
name|ProxyAuthorizationHandler
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|authenticate
parameter_list|(
name|String
name|userName
parameter_list|,
name|String
name|password
parameter_list|)
block|{
return|return
literal|"user"
operator|.
name|equals
argument_list|(
name|userName
argument_list|)
operator|&&
literal|"password"
operator|.
name|equals
argument_list|(
name|password
argument_list|)
return|;
block|}
block|}
argument_list|)
expr_stmt|;
name|proxyServer
operator|.
name|start
argument_list|()
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
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
literal|"?username=admin&password=admin&proxy=#proxy"
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
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|FTP_ROOT_DIR
operator|+
literal|"/hello.txt"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"File should exist: "
operator|+
name|file
argument_list|,
name|file
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Hello World"
argument_list|,
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|file
argument_list|)
argument_list|)
expr_stmt|;
name|proxyServer
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSftpSimpleSubPathProduceThroughProxy ()
specifier|public
name|void
name|testSftpSimpleSubPathProduceThroughProxy
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
comment|// start http proxy
name|HttpProxyServer
name|proxyServer
init|=
operator|new
name|DefaultHttpProxyServer
argument_list|(
name|proxyPort
argument_list|)
decl_stmt|;
name|proxyServer
operator|.
name|addProxyAuthenticationHandler
argument_list|(
operator|new
name|ProxyAuthorizationHandler
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|authenticate
parameter_list|(
name|String
name|userName
parameter_list|,
name|String
name|password
parameter_list|)
block|{
return|return
literal|"user"
operator|.
name|equals
argument_list|(
name|userName
argument_list|)
operator|&&
literal|"password"
operator|.
name|equals
argument_list|(
name|password
argument_list|)
return|;
block|}
block|}
argument_list|)
expr_stmt|;
name|proxyServer
operator|.
name|start
argument_list|()
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
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
literal|"/mysub?username=admin&password=admin&proxy=#proxy"
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
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|FTP_ROOT_DIR
operator|+
literal|"/mysub/bye.txt"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"File should exist: "
operator|+
name|file
argument_list|,
name|file
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Bye World"
argument_list|,
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|file
argument_list|)
argument_list|)
expr_stmt|;
name|proxyServer
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSftpSimpleTwoSubPathProduceThroughProxy ()
specifier|public
name|void
name|testSftpSimpleTwoSubPathProduceThroughProxy
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
comment|// start http proxy
name|HttpProxyServer
name|proxyServer
init|=
operator|new
name|DefaultHttpProxyServer
argument_list|(
name|proxyPort
argument_list|)
decl_stmt|;
name|proxyServer
operator|.
name|addProxyAuthenticationHandler
argument_list|(
operator|new
name|ProxyAuthorizationHandler
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|authenticate
parameter_list|(
name|String
name|userName
parameter_list|,
name|String
name|password
parameter_list|)
block|{
return|return
literal|"user"
operator|.
name|equals
argument_list|(
name|userName
argument_list|)
operator|&&
literal|"password"
operator|.
name|equals
argument_list|(
name|password
argument_list|)
return|;
block|}
block|}
argument_list|)
expr_stmt|;
name|proxyServer
operator|.
name|start
argument_list|()
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
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
literal|"/mysub/myother?username=admin&password=admin&proxy=#proxy"
argument_list|,
literal|"Farewell World"
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"farewell.txt"
argument_list|)
expr_stmt|;
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|FTP_ROOT_DIR
operator|+
literal|"/mysub/myother/farewell.txt"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"File should exist: "
operator|+
name|file
argument_list|,
name|file
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Farewell World"
argument_list|,
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|file
argument_list|)
argument_list|)
expr_stmt|;
name|proxyServer
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createRegistry ()
specifier|protected
name|JndiRegistry
name|createRegistry
parameter_list|()
throws|throws
name|Exception
block|{
name|JndiRegistry
name|jndi
init|=
name|super
operator|.
name|createRegistry
argument_list|()
decl_stmt|;
specifier|final
name|ProxyHTTP
name|proxyHTTP
init|=
operator|new
name|ProxyHTTP
argument_list|(
literal|"localhost"
argument_list|,
name|proxyPort
argument_list|)
decl_stmt|;
name|proxyHTTP
operator|.
name|setUserPasswd
argument_list|(
literal|"user"
argument_list|,
literal|"password"
argument_list|)
expr_stmt|;
name|jndi
operator|.
name|bind
argument_list|(
literal|"proxy"
argument_list|,
name|proxyHTTP
argument_list|)
expr_stmt|;
return|return
name|jndi
return|;
block|}
block|}
end_class

end_unit

