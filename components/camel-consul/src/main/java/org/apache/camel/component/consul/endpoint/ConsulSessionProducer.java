begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.consul.endpoint
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|consul
operator|.
name|endpoint
package|;
end_package

begin_import
import|import
name|com
operator|.
name|orbitz
operator|.
name|consul
operator|.
name|Consul
import|;
end_import

begin_import
import|import
name|com
operator|.
name|orbitz
operator|.
name|consul
operator|.
name|SessionClient
import|;
end_import

begin_import
import|import
name|com
operator|.
name|orbitz
operator|.
name|consul
operator|.
name|model
operator|.
name|session
operator|.
name|Session
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
name|InvokeOnHeader
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
name|component
operator|.
name|consul
operator|.
name|ConsulConfiguration
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
name|consul
operator|.
name|ConsulConstants
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
name|consul
operator|.
name|ConsulEndpoint
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
name|util
operator|.
name|ObjectHelper
import|;
end_import

begin_class
DECL|class|ConsulSessionProducer
specifier|public
specifier|final
class|class
name|ConsulSessionProducer
extends|extends
name|AbstractConsulProducer
argument_list|<
name|SessionClient
argument_list|>
block|{
DECL|method|ConsulSessionProducer (ConsulEndpoint endpoint, ConsulConfiguration configuration)
specifier|public
name|ConsulSessionProducer
parameter_list|(
name|ConsulEndpoint
name|endpoint
parameter_list|,
name|ConsulConfiguration
name|configuration
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|,
name|configuration
argument_list|,
name|Consul
operator|::
name|sessionClient
argument_list|)
expr_stmt|;
block|}
annotation|@
name|InvokeOnHeader
argument_list|(
name|ConsulSessionActions
operator|.
name|CREATE
argument_list|)
DECL|method|create (Message message)
specifier|protected
name|void
name|create
parameter_list|(
name|Message
name|message
parameter_list|)
throws|throws
name|Exception
block|{
name|setBodyAndResult
argument_list|(
name|message
argument_list|,
name|getClient
argument_list|()
operator|.
name|createSession
argument_list|(
name|message
operator|.
name|getMandatoryBody
argument_list|(
name|Session
operator|.
name|class
argument_list|)
argument_list|,
name|message
operator|.
name|getHeader
argument_list|(
name|ConsulConstants
operator|.
name|CONSUL_DATACENTER
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|InvokeOnHeader
argument_list|(
name|ConsulSessionActions
operator|.
name|DESTROY
argument_list|)
DECL|method|destroy (Message message)
specifier|protected
name|void
name|destroy
parameter_list|(
name|Message
name|message
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|sessionId
init|=
name|message
operator|.
name|getHeader
argument_list|(
name|ConsulConstants
operator|.
name|CONSUL_SESSION
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|sessionId
argument_list|)
condition|)
block|{
name|getClient
argument_list|()
operator|.
name|destroySession
argument_list|(
name|message
operator|.
name|getMandatoryBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
argument_list|,
name|message
operator|.
name|getHeader
argument_list|(
name|ConsulConstants
operator|.
name|CONSUL_DATACENTER
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|getClient
argument_list|()
operator|.
name|destroySession
argument_list|(
name|sessionId
argument_list|,
name|message
operator|.
name|getHeader
argument_list|(
name|ConsulConstants
operator|.
name|CONSUL_DATACENTER
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|setBodyAndResult
argument_list|(
name|message
argument_list|,
literal|null
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
annotation|@
name|InvokeOnHeader
argument_list|(
name|ConsulSessionActions
operator|.
name|INFO
argument_list|)
DECL|method|info (Message message)
specifier|protected
name|void
name|info
parameter_list|(
name|Message
name|message
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|sessionId
init|=
name|message
operator|.
name|getHeader
argument_list|(
name|ConsulConstants
operator|.
name|CONSUL_SESSION
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|sessionId
argument_list|)
condition|)
block|{
name|setBodyAndResult
argument_list|(
name|message
argument_list|,
name|getClient
argument_list|()
operator|.
name|getSessionInfo
argument_list|(
name|message
operator|.
name|getMandatoryBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
argument_list|,
name|message
operator|.
name|getHeader
argument_list|(
name|ConsulConstants
operator|.
name|CONSUL_DATACENTER
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|orElse
argument_list|(
literal|null
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|setBodyAndResult
argument_list|(
name|message
argument_list|,
name|getClient
argument_list|()
operator|.
name|getSessionInfo
argument_list|(
name|sessionId
argument_list|,
name|message
operator|.
name|getHeader
argument_list|(
name|ConsulConstants
operator|.
name|CONSUL_DATACENTER
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|orElse
argument_list|(
literal|null
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|InvokeOnHeader
argument_list|(
name|ConsulSessionActions
operator|.
name|LIST
argument_list|)
DECL|method|list (Message message)
specifier|protected
name|void
name|list
parameter_list|(
name|Message
name|message
parameter_list|)
throws|throws
name|Exception
block|{
name|setBodyAndResult
argument_list|(
name|message
argument_list|,
name|getClient
argument_list|()
operator|.
name|listSessions
argument_list|(
name|message
operator|.
name|getHeader
argument_list|(
name|ConsulConstants
operator|.
name|CONSUL_DATACENTER
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|InvokeOnHeader
argument_list|(
name|ConsulSessionActions
operator|.
name|RENEW
argument_list|)
DECL|method|renew (Message message)
specifier|protected
name|void
name|renew
parameter_list|(
name|Message
name|message
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|sessionId
init|=
name|message
operator|.
name|getHeader
argument_list|(
name|ConsulConstants
operator|.
name|CONSUL_SESSION
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|sessionId
argument_list|)
condition|)
block|{
name|setBodyAndResult
argument_list|(
name|message
argument_list|,
name|getClient
argument_list|()
operator|.
name|renewSession
argument_list|(
name|message
operator|.
name|getHeader
argument_list|(
name|ConsulConstants
operator|.
name|CONSUL_DATACENTER
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|,
name|message
operator|.
name|getMandatoryBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|setBodyAndResult
argument_list|(
name|message
argument_list|,
name|getClient
argument_list|()
operator|.
name|renewSession
argument_list|(
name|message
operator|.
name|getHeader
argument_list|(
name|ConsulConstants
operator|.
name|CONSUL_DATACENTER
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|,
name|sessionId
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

