begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cxf.feature
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|cxf
operator|.
name|feature
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
name|Executor
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
name|cxf
operator|.
name|MessageInvoker
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|endpoint
operator|.
name|Endpoint
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
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
name|cxf
operator|.
name|message
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
name|cxf
operator|.
name|phase
operator|.
name|AbstractPhaseInterceptor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|phase
operator|.
name|Phase
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|service
operator|.
name|Service
import|;
end_import

begin_comment
comment|/**  * This interceptor just works for invoking the MessageInvoker implementor  *  */
end_comment

begin_class
DECL|class|MessageInvokerInterceptor
specifier|public
class|class
name|MessageInvokerInterceptor
extends|extends
name|AbstractPhaseInterceptor
argument_list|<
name|Message
argument_list|>
block|{
DECL|method|MessageInvokerInterceptor ()
specifier|public
name|MessageInvokerInterceptor
parameter_list|()
block|{
name|super
argument_list|(
name|Phase
operator|.
name|INVOKE
argument_list|)
expr_stmt|;
block|}
DECL|method|handleMessage (final Message message)
specifier|public
name|void
name|handleMessage
parameter_list|(
specifier|final
name|Message
name|message
parameter_list|)
block|{
specifier|final
name|Exchange
name|exchange
init|=
name|message
operator|.
name|getExchange
argument_list|()
decl_stmt|;
specifier|final
name|Endpoint
name|endpoint
init|=
name|exchange
operator|.
name|get
argument_list|(
name|Endpoint
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|final
name|Service
name|service
init|=
name|endpoint
operator|.
name|getService
argument_list|()
decl_stmt|;
specifier|final
name|MessageInvoker
name|invoker
init|=
operator|(
name|MessageInvoker
operator|)
name|service
operator|.
name|getInvoker
argument_list|()
decl_stmt|;
comment|// How to deal with the oneway messge
name|Runnable
name|invocation
init|=
operator|new
name|Runnable
argument_list|()
block|{
specifier|public
name|void
name|run
parameter_list|()
block|{
name|Exchange
name|runableEx
init|=
name|message
operator|.
name|getExchange
argument_list|()
decl_stmt|;
name|invoker
operator|.
name|invoke
argument_list|(
name|runableEx
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|exchange
operator|.
name|isOneWay
argument_list|()
condition|)
block|{
name|Endpoint
name|ep
init|=
name|exchange
operator|.
name|get
argument_list|(
name|Endpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|Message
name|outMessage
init|=
name|runableEx
operator|.
name|getOutMessage
argument_list|()
decl_stmt|;
if|if
condition|(
name|outMessage
operator|==
literal|null
condition|)
block|{
name|outMessage
operator|=
name|ep
operator|.
name|getBinding
argument_list|()
operator|.
name|createMessage
argument_list|()
expr_stmt|;
name|exchange
operator|.
name|setOutMessage
argument_list|(
name|outMessage
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
decl_stmt|;
name|Executor
name|executor
init|=
name|getExecutor
argument_list|(
name|endpoint
argument_list|)
decl_stmt|;
if|if
condition|(
name|exchange
operator|.
name|get
argument_list|(
name|Executor
operator|.
name|class
argument_list|)
operator|==
name|executor
condition|)
block|{
comment|// already executing on the appropriate executor
name|invocation
operator|.
name|run
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|exchange
operator|.
name|put
argument_list|(
name|Executor
operator|.
name|class
argument_list|,
name|executor
argument_list|)
expr_stmt|;
name|executor
operator|.
name|execute
argument_list|(
name|invocation
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Get the Executor for this invocation.      * @param endpoint      * @return      */
DECL|method|getExecutor (final Endpoint endpoint)
specifier|private
name|Executor
name|getExecutor
parameter_list|(
specifier|final
name|Endpoint
name|endpoint
parameter_list|)
block|{
return|return
name|endpoint
operator|.
name|getService
argument_list|()
operator|.
name|getExecutor
argument_list|()
return|;
block|}
block|}
end_class

end_unit

