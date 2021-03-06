begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.asterisk
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|asterisk
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
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
name|camel
operator|.
name|util
operator|.
name|ObjectHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|asteriskjava
operator|.
name|manager
operator|.
name|AuthenticationFailedException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|asteriskjava
operator|.
name|manager
operator|.
name|TimeoutException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|asteriskjava
operator|.
name|manager
operator|.
name|action
operator|.
name|ManagerAction
import|;
end_import

begin_import
import|import
name|org
operator|.
name|asteriskjava
operator|.
name|manager
operator|.
name|response
operator|.
name|ManagerResponse
import|;
end_import

begin_comment
comment|/**  * The Asterisk producer.  */
end_comment

begin_class
DECL|class|AsteriskProducer
specifier|public
class|class
name|AsteriskProducer
extends|extends
name|DefaultProducer
block|{
DECL|field|endpoint
specifier|private
specifier|final
name|AsteriskEndpoint
name|endpoint
decl_stmt|;
DECL|field|connection
specifier|private
specifier|final
name|AsteriskConnection
name|connection
decl_stmt|;
DECL|method|AsteriskProducer (AsteriskEndpoint endpoint)
specifier|public
name|AsteriskProducer
parameter_list|(
name|AsteriskEndpoint
name|endpoint
parameter_list|)
throws|throws
name|IllegalStateException
throws|,
name|IOException
throws|,
name|AuthenticationFailedException
throws|,
name|TimeoutException
throws|,
name|CamelAsteriskException
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
name|this
operator|.
name|connection
operator|=
operator|new
name|AsteriskConnection
argument_list|(
name|endpoint
operator|.
name|getHostname
argument_list|()
argument_list|,
name|endpoint
operator|.
name|getUsername
argument_list|()
argument_list|,
name|endpoint
operator|.
name|getPassword
argument_list|()
argument_list|)
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
name|connection
operator|.
name|login
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
name|connection
operator|.
name|logoff
argument_list|()
expr_stmt|;
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
comment|// The action set in the URI can be overridden using the message
comment|// header CamelAsteriskAction
name|AsteriskAction
name|action
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|AsteriskConstants
operator|.
name|ACTION
argument_list|,
name|AsteriskAction
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|action
operator|==
literal|null
condition|)
block|{
name|action
operator|=
name|endpoint
operator|.
name|getAction
argument_list|()
expr_stmt|;
block|}
comment|// Action must be set
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|action
argument_list|,
literal|"action"
argument_list|)
expr_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Send action {}"
argument_list|,
name|action
argument_list|)
expr_stmt|;
name|ManagerAction
name|managerAction
init|=
name|action
operator|.
name|apply
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|ManagerResponse
name|managerResponse
init|=
name|connection
operator|.
name|sendAction
argument_list|(
name|managerAction
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|managerResponse
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

