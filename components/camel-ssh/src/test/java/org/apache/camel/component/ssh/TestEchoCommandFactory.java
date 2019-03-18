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
name|concurrent
operator|.
name|CountDownLatch
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
name|command
operator|.
name|Command
import|;
end_import

begin_class
DECL|class|TestEchoCommandFactory
specifier|public
class|class
name|TestEchoCommandFactory
extends|extends
name|EchoCommandFactory
block|{
annotation|@
name|Override
DECL|method|createCommand (String command)
specifier|public
name|Command
name|createCommand
parameter_list|(
name|String
name|command
parameter_list|)
block|{
return|return
operator|new
name|TestEchoCommand
argument_list|(
name|command
argument_list|)
return|;
block|}
DECL|class|TestEchoCommand
specifier|public
specifier|static
class|class
name|TestEchoCommand
extends|extends
name|EchoCommand
block|{
DECL|field|latch
specifier|public
specifier|static
name|CountDownLatch
name|latch
init|=
operator|new
name|CountDownLatch
argument_list|(
literal|1
argument_list|)
decl_stmt|;
DECL|method|TestEchoCommand (String command)
specifier|public
name|TestEchoCommand
parameter_list|(
name|String
name|command
parameter_list|)
block|{
name|super
argument_list|(
name|command
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|destroy ()
specifier|public
name|void
name|destroy
parameter_list|()
block|{
if|if
condition|(
name|latch
operator|!=
literal|null
condition|)
block|{
name|latch
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
name|super
operator|.
name|destroy
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

