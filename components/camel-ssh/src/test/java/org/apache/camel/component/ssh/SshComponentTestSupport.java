begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.ssh
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|ssh
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
name|test
operator|.
name|AvailablePortFinder
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
name|sshd
operator|.
name|SshServer
import|;
end_import

begin_class
DECL|class|SshComponentTestSupport
specifier|public
class|class
name|SshComponentTestSupport
extends|extends
name|CamelTestSupport
block|{
DECL|field|sshd
specifier|protected
name|SshServer
name|sshd
decl_stmt|;
DECL|field|port
specifier|protected
name|int
name|port
decl_stmt|;
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
name|port
operator|=
name|AvailablePortFinder
operator|.
name|getNextAvailable
argument_list|(
literal|22000
argument_list|)
expr_stmt|;
name|sshd
operator|=
name|SshServer
operator|.
name|setUpDefaultServer
argument_list|()
expr_stmt|;
name|sshd
operator|.
name|setPort
argument_list|(
name|port
argument_list|)
expr_stmt|;
name|sshd
operator|.
name|setKeyPairProvider
argument_list|(
operator|new
name|FileKeyPairProvider
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"src/test/resources/hostkey.pem"
block|}
argument_list|)
argument_list|)
expr_stmt|;
name|sshd
operator|.
name|setCommandFactory
argument_list|(
operator|new
name|TestEchoCommandFactory
argument_list|()
argument_list|)
expr_stmt|;
name|sshd
operator|.
name|setPasswordAuthenticator
argument_list|(
operator|new
name|BogusPasswordAuthenticator
argument_list|()
argument_list|)
expr_stmt|;
name|sshd
operator|.
name|setPublickeyAuthenticator
argument_list|(
operator|new
name|BogusPublickeyAuthenticator
argument_list|()
argument_list|)
expr_stmt|;
name|sshd
operator|.
name|start
argument_list|()
expr_stmt|;
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|tearDown ()
specifier|public
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
name|sshd
operator|!=
literal|null
condition|)
block|{
name|sshd
operator|.
name|stop
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|50
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

