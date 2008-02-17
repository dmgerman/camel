begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cxf.interceptors
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
name|interceptors
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
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Logger
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
name|CamelInvoker
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
name|invoker
operator|.
name|InvokingContext
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
name|ConduitSelector
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
name|PreexistingConduitSelector
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
name|interceptor
operator|.
name|Fault
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
name|interceptor
operator|.
name|InterceptorChain
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
name|service
operator|.
name|model
operator|.
name|BindingMessageInfo
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
name|model
operator|.
name|BindingOperationInfo
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
name|transport
operator|.
name|Conduit
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
name|ws
operator|.
name|addressing
operator|.
name|EndpointReferenceType
import|;
end_import

begin_comment
comment|/**  * This is the base class for routing interceptors that can intercept and forward  * message to router as different phases.  *  */
end_comment

begin_class
DECL|class|AbstractInvokerInterceptor
specifier|public
specifier|abstract
class|class
name|AbstractInvokerInterceptor
extends|extends
name|AbstractPhaseInterceptor
argument_list|<
name|Message
argument_list|>
block|{
DECL|field|ROUTING_INERCEPTOR_PHASE
specifier|public
specifier|static
specifier|final
name|String
name|ROUTING_INERCEPTOR_PHASE
init|=
literal|"Routing-Phase"
decl_stmt|;
DECL|field|BUNDLE
specifier|public
specifier|static
specifier|final
name|String
name|BUNDLE
init|=
literal|"wsdl-cxf"
decl_stmt|;
DECL|method|AbstractInvokerInterceptor (String phase)
specifier|public
name|AbstractInvokerInterceptor
parameter_list|(
name|String
name|phase
parameter_list|)
block|{
name|super
argument_list|(
name|phase
argument_list|)
expr_stmt|;
block|}
DECL|method|isRequestor (Message message)
specifier|private
name|boolean
name|isRequestor
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
return|return
name|Boolean
operator|.
name|TRUE
operator|.
name|equals
argument_list|(
name|message
operator|.
name|get
argument_list|(
name|Message
operator|.
name|REQUESTOR_ROLE
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Send the intercepted message to router      */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|handleMessage (Message inMessage)
specifier|public
name|void
name|handleMessage
parameter_list|(
name|Message
name|inMessage
parameter_list|)
throws|throws
name|Fault
block|{
if|if
condition|(
name|isRequestor
argument_list|(
name|inMessage
argument_list|)
condition|)
block|{
return|return;
block|}
name|Exchange
name|exchange
init|=
name|inMessage
operator|.
name|getExchange
argument_list|()
decl_stmt|;
name|Message
name|outMessage
init|=
literal|null
decl_stmt|;
try|try
block|{
name|CamelInvoker
name|invoker
init|=
name|exchange
operator|.
name|get
argument_list|(
name|CamelInvoker
operator|.
name|class
argument_list|)
decl_stmt|;
name|invoker
operator|.
name|invoke
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|outMessage
operator|=
name|exchange
operator|.
name|getOutMessage
argument_list|()
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
name|Fault
argument_list|(
name|e
argument_list|)
throw|;
block|}
comment|// set back channel conduit in the exchange if it is not present
name|setBackChannelConduit
argument_list|(
name|exchange
argument_list|,
name|outMessage
argument_list|)
expr_stmt|;
name|Exception
name|ex
init|=
name|outMessage
operator|.
name|getContent
argument_list|(
name|Exception
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|ex
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
operator|!
operator|(
name|ex
operator|instanceof
name|Fault
operator|)
condition|)
block|{
name|ex
operator|=
operator|new
name|Fault
argument_list|(
name|ex
argument_list|)
expr_stmt|;
block|}
throw|throw
operator|(
name|Fault
operator|)
name|ex
throw|;
block|}
name|outMessage
operator|.
name|put
argument_list|(
name|Message
operator|.
name|INBOUND_MESSAGE
argument_list|,
name|Boolean
operator|.
name|FALSE
argument_list|)
expr_stmt|;
name|BindingOperationInfo
name|boi
init|=
name|exchange
operator|.
name|get
argument_list|(
name|BindingOperationInfo
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|boi
operator|!=
literal|null
condition|)
block|{
name|exchange
operator|.
name|put
argument_list|(
name|BindingMessageInfo
operator|.
name|class
argument_list|,
name|boi
operator|.
name|getOutput
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|InvokingContext
name|invokingContext
init|=
name|exchange
operator|.
name|get
argument_list|(
name|InvokingContext
operator|.
name|class
argument_list|)
decl_stmt|;
assert|assert
name|invokingContext
operator|!=
literal|null
assert|;
name|InterceptorChain
name|chain
init|=
name|invokingContext
operator|.
name|getResponseOutInterceptorChain
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
if|if
condition|(
name|chain
operator|!=
literal|null
condition|)
block|{
name|outMessage
operator|.
name|setInterceptorChain
argument_list|(
name|chain
argument_list|)
expr_stmt|;
comment|//Initiate the OutBound Chain.
name|chain
operator|.
name|doIntercept
argument_list|(
name|outMessage
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Creates a conduit if not present on the exchange. outMessage or faultMessage      * It will create a back channel in PAYLOAD and MESSAGE case.      * POJO case will have a coduit due to OutgoingChainInterceptor in PRE_INVOKE Phase      */
DECL|method|setBackChannelConduit (Exchange ex, Message message)
specifier|protected
name|void
name|setBackChannelConduit
parameter_list|(
name|Exchange
name|ex
parameter_list|,
name|Message
name|message
parameter_list|)
throws|throws
name|Fault
block|{
name|Conduit
name|conduit
init|=
name|ex
operator|.
name|getConduit
argument_list|(
name|message
argument_list|)
decl_stmt|;
if|if
condition|(
name|conduit
operator|==
literal|null
condition|)
block|{
try|try
block|{
name|EndpointReferenceType
name|target
init|=
name|ex
operator|.
name|get
argument_list|(
name|EndpointReferenceType
operator|.
name|class
argument_list|)
decl_stmt|;
name|conduit
operator|=
name|ex
operator|.
name|getDestination
argument_list|()
operator|.
name|getBackChannel
argument_list|(
name|ex
operator|.
name|getInMessage
argument_list|()
argument_list|,
literal|null
argument_list|,
name|target
argument_list|)
expr_stmt|;
name|ex
operator|.
name|put
argument_list|(
name|ConduitSelector
operator|.
name|class
argument_list|,
operator|new
name|PreexistingConduitSelector
argument_list|(
name|conduit
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|Fault
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
assert|assert
name|conduit
operator|!=
literal|null
assert|;
block|}
DECL|method|getLogger ()
specifier|protected
specifier|abstract
name|Logger
name|getLogger
parameter_list|()
function_decl|;
block|}
end_class

end_unit

