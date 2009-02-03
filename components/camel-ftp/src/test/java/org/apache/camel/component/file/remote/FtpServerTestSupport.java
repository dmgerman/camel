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
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileOutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Random
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
name|converter
operator|.
name|IOConverter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ftpserver
operator|.
name|FtpServer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ftpserver
operator|.
name|FtpServerFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ftpserver
operator|.
name|filesystem
operator|.
name|nativefs
operator|.
name|NativeFileSystemFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ftpserver
operator|.
name|ftplet
operator|.
name|UserManager
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ftpserver
operator|.
name|listener
operator|.
name|ListenerFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ftpserver
operator|.
name|usermanager
operator|.
name|ClearTextPasswordEncryptor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ftpserver
operator|.
name|usermanager
operator|.
name|impl
operator|.
name|PropertiesUserManager
import|;
end_import

begin_comment
comment|/**  * Base class for unit testing using a FTPServer  */
end_comment

begin_class
DECL|class|FtpServerTestSupport
specifier|public
specifier|abstract
class|class
name|FtpServerTestSupport
extends|extends
name|ContextTestSupport
block|{
DECL|field|FTP_ROOT_DIR
specifier|public
specifier|static
specifier|final
name|String
name|FTP_ROOT_DIR
init|=
literal|"./res/home/"
decl_stmt|;
DECL|field|ftpServer
specifier|protected
name|FtpServer
name|ftpServer
decl_stmt|;
DECL|field|port
specifier|private
name|int
name|port
decl_stmt|;
DECL|method|getPort ()
specifier|public
name|int
name|getPort
parameter_list|()
block|{
return|return
name|port
return|;
block|}
DECL|method|setUp ()
specifier|protected
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|initPort
argument_list|()
expr_stmt|;
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|initFtpServer
argument_list|()
expr_stmt|;
name|ftpServer
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
DECL|method|tearDown ()
specifier|protected
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|tearDown
argument_list|()
expr_stmt|;
name|ftpServer
operator|.
name|stop
argument_list|()
expr_stmt|;
name|ftpServer
operator|=
literal|null
expr_stmt|;
name|port
operator|=
literal|0
expr_stmt|;
block|}
DECL|method|initFtpServer ()
specifier|protected
name|void
name|initFtpServer
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|port
operator|<
literal|21000
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Port number is not initialized in an expected range: "
operator|+
name|getPort
argument_list|()
argument_list|)
throw|;
block|}
name|FtpServerFactory
name|serverFactory
init|=
operator|new
name|FtpServerFactory
argument_list|()
decl_stmt|;
comment|// setup user management to read our users.properties and use clear text passwords
name|File
name|file
init|=
operator|new
name|File
argument_list|(
literal|"./src/test/resources/users.properties"
argument_list|)
operator|.
name|getAbsoluteFile
argument_list|()
decl_stmt|;
name|UserManager
name|uman
init|=
operator|new
name|PropertiesUserManager
argument_list|(
operator|new
name|ClearTextPasswordEncryptor
argument_list|()
argument_list|,
name|file
argument_list|,
literal|"admin"
argument_list|)
decl_stmt|;
name|serverFactory
operator|.
name|setUserManager
argument_list|(
name|uman
argument_list|)
expr_stmt|;
name|NativeFileSystemFactory
name|fsf
init|=
operator|new
name|NativeFileSystemFactory
argument_list|()
decl_stmt|;
name|fsf
operator|.
name|setCreateHome
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|serverFactory
operator|.
name|setFileSystem
argument_list|(
name|fsf
argument_list|)
expr_stmt|;
name|ListenerFactory
name|factory
init|=
operator|new
name|ListenerFactory
argument_list|()
decl_stmt|;
name|factory
operator|.
name|setPort
argument_list|(
name|port
argument_list|)
expr_stmt|;
name|serverFactory
operator|.
name|addListener
argument_list|(
literal|"default"
argument_list|,
name|factory
operator|.
name|createListener
argument_list|()
argument_list|)
expr_stmt|;
name|ftpServer
operator|=
name|serverFactory
operator|.
name|createServer
argument_list|()
expr_stmt|;
block|}
DECL|method|initPort ()
specifier|protected
name|void
name|initPort
parameter_list|()
throws|throws
name|Exception
block|{
name|File
name|file
init|=
operator|new
name|File
argument_list|(
literal|"./target/ftpport.txt"
argument_list|)
decl_stmt|;
name|file
operator|=
name|file
operator|.
name|getAbsoluteFile
argument_list|()
expr_stmt|;
if|if
condition|(
operator|!
name|file
operator|.
name|exists
argument_list|()
condition|)
block|{
comment|// start from somewhere in the 21xxx range
name|port
operator|=
literal|21000
operator|+
operator|new
name|Random
argument_list|()
operator|.
name|nextInt
argument_list|(
literal|900
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// read port number from file
name|String
name|s
init|=
name|IOConverter
operator|.
name|toString
argument_list|(
name|file
argument_list|)
decl_stmt|;
name|port
operator|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|s
argument_list|)
expr_stmt|;
comment|// use next number
name|port
operator|++
expr_stmt|;
block|}
comment|// save to file, do not append
name|FileOutputStream
name|fos
init|=
operator|new
name|FileOutputStream
argument_list|(
name|file
argument_list|,
literal|false
argument_list|)
decl_stmt|;
try|try
block|{
name|fos
operator|.
name|write
argument_list|(
operator|(
literal|""
operator|+
name|getPort
argument_list|()
operator|)
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|fos
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

