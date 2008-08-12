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
name|ContextTestSupport
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
name|interfaces
operator|.
name|FtpServerContext
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
DECL|field|ftpServer
specifier|protected
name|FtpServer
name|ftpServer
decl_stmt|;
DECL|method|getPort ()
specifier|public
specifier|abstract
name|int
name|getPort
parameter_list|()
function_decl|;
DECL|method|setUp ()
specifier|protected
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
name|initFtpServer
argument_list|()
expr_stmt|;
if|if
condition|(
name|ftpServer
operator|.
name|isStopped
argument_list|()
condition|)
block|{
name|ftpServer
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
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
if|if
condition|(
operator|!
name|ftpServer
operator|.
name|isStopped
argument_list|()
condition|)
block|{
name|ftpServer
operator|.
name|getServerContext
argument_list|()
operator|.
name|dispose
argument_list|()
expr_stmt|;
name|ftpServer
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|initFtpServer ()
specifier|protected
name|void
name|initFtpServer
parameter_list|()
throws|throws
name|Exception
block|{
name|ftpServer
operator|=
operator|new
name|FtpServer
argument_list|()
expr_stmt|;
name|ftpServer
operator|.
name|getListener
argument_list|(
literal|"default"
argument_list|)
operator|.
name|setPort
argument_list|(
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

