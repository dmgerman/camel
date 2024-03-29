begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|CamelExchangeException
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
name|Message
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
name|support
operator|.
name|DefaultProducer
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
name|client
operator|.
name|SshClient
import|;
end_import

begin_class
DECL|class|SshProducer
specifier|public
class|class
name|SshProducer
extends|extends
name|DefaultProducer
block|{
DECL|field|endpoint
specifier|private
name|SshEndpoint
name|endpoint
decl_stmt|;
DECL|field|client
specifier|private
name|SshClient
name|client
decl_stmt|;
DECL|method|SshProducer (SshEndpoint endpoint)
specifier|public
name|SshProducer
parameter_list|(
name|SshEndpoint
name|endpoint
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
name|client
operator|=
name|SshClient
operator|.
name|setUpDefaultClient
argument_list|()
expr_stmt|;
name|client
operator|.
name|start
argument_list|()
expr_stmt|;
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
if|if
condition|(
name|client
operator|!=
literal|null
condition|)
block|{
name|client
operator|.
name|stop
argument_list|()
expr_stmt|;
name|client
operator|=
literal|null
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|process (Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
specifier|final
name|Message
name|in
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|String
name|command
init|=
name|in
operator|.
name|getMandatoryBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeaders
argument_list|()
decl_stmt|;
try|try
block|{
name|String
name|knownHostResource
init|=
name|endpoint
operator|.
name|getKnownHostsResource
argument_list|()
decl_stmt|;
if|if
condition|(
name|knownHostResource
operator|!=
literal|null
condition|)
block|{
name|client
operator|.
name|setServerKeyVerifier
argument_list|(
operator|new
name|ResourceBasedSSHKeyVerifier
argument_list|(
name|exchange
operator|.
name|getContext
argument_list|()
argument_list|,
name|knownHostResource
argument_list|,
name|endpoint
operator|.
name|isFailOnUnknownHost
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|SshResult
name|result
init|=
name|SshHelper
operator|.
name|sendExecCommand
argument_list|(
name|headers
argument_list|,
name|command
argument_list|,
name|endpoint
argument_list|,
name|client
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|result
operator|.
name|getStdout
argument_list|()
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setHeader
argument_list|(
name|SshResult
operator|.
name|EXIT_VALUE
argument_list|,
name|result
operator|.
name|getExitValue
argument_list|()
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setHeader
argument_list|(
name|SshResult
operator|.
name|STDERR
argument_list|,
name|result
operator|.
name|getStderr
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|CamelExchangeException
argument_list|(
literal|"Cannot execute command: "
operator|+
name|command
argument_list|,
name|exchange
argument_list|,
name|e
argument_list|)
throw|;
block|}
comment|// propagate headers
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getHeaders
argument_list|()
operator|.
name|putAll
argument_list|(
name|in
operator|.
name|getHeaders
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

