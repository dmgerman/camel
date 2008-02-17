begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cxf
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
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Method
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

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
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Level
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
name|ExchangePattern
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
name|frontend
operator|.
name|MethodDispatcher
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
name|helpers
operator|.
name|CastUtils
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
name|message
operator|.
name|MessageContentsList
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
name|invoker
operator|.
name|Invoker
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

begin_class
DECL|class|CamelInvoker
specifier|public
class|class
name|CamelInvoker
implements|implements
name|Invoker
implements|,
name|MessageInvoker
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|Logger
operator|.
name|getLogger
argument_list|(
name|CamelInvoker
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
DECL|field|cxfConsumer
specifier|private
name|CxfConsumer
name|cxfConsumer
decl_stmt|;
DECL|method|CamelInvoker (CxfConsumer consumer)
specifier|public
name|CamelInvoker
parameter_list|(
name|CxfConsumer
name|consumer
parameter_list|)
block|{
name|cxfConsumer
operator|=
name|consumer
expr_stmt|;
block|}
comment|/**     * This method is called when the incoming message is to     * be passed into the camel processor. The return value is the response     * from the processor     * @param inMessage     */
DECL|method|invoke (Exchange exchange)
specifier|public
name|void
name|invoke
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|Message
name|inMessage
init|=
name|exchange
operator|.
name|getInMessage
argument_list|()
decl_stmt|;
comment|//TODO set the request context here
name|CxfEndpoint
name|endpoint
init|=
operator|(
name|CxfEndpoint
operator|)
name|cxfConsumer
operator|.
name|getEndpoint
argument_list|()
decl_stmt|;
name|CxfExchange
name|cxfExchange
init|=
name|endpoint
operator|.
name|createExchange
argument_list|(
name|inMessage
argument_list|)
decl_stmt|;
try|try
block|{
name|cxfConsumer
operator|.
name|getProcessor
argument_list|()
operator|.
name|process
argument_list|(
name|cxfExchange
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
comment|// catch the exception and send back to cxf client
throw|throw
operator|new
name|Fault
argument_list|(
name|ex
argument_list|)
throw|;
block|}
comment|// make sure the client has return back the message
name|copybackExchange
argument_list|(
name|cxfExchange
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
name|Message
name|outMessage
init|=
name|exchange
operator|.
name|getOutMessage
argument_list|()
decl_stmt|;
comment|// update the outMessageContext
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
block|}
DECL|method|copybackExchange (CxfExchange result, Exchange exchange)
specifier|public
name|void
name|copybackExchange
parameter_list|(
name|CxfExchange
name|result
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
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
name|Message
name|outMessage
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|result
operator|.
name|isFailed
argument_list|()
condition|)
block|{
name|CxfMessage
name|fault
init|=
name|result
operator|.
name|getFault
argument_list|()
decl_stmt|;
name|outMessage
operator|=
name|exchange
operator|.
name|getInFaultMessage
argument_list|()
expr_stmt|;
if|if
condition|(
name|outMessage
operator|==
literal|null
condition|)
block|{
name|outMessage
operator|=
name|endpoint
operator|.
name|getBinding
argument_list|()
operator|.
name|createMessage
argument_list|()
expr_stmt|;
name|outMessage
operator|.
name|setExchange
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setInFaultMessage
argument_list|(
name|outMessage
argument_list|)
expr_stmt|;
block|}
name|Exception
name|ex
init|=
operator|(
name|Exception
operator|)
name|fault
operator|.
name|getBody
argument_list|()
decl_stmt|;
name|outMessage
operator|.
name|setContent
argument_list|(
name|Exception
operator|.
name|class
argument_list|,
name|ex
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|outMessage
operator|=
name|result
operator|.
name|getOutMessage
argument_list|()
expr_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|FINEST
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|finest
argument_list|(
literal|"Get the response outMessage "
operator|+
name|outMessage
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|outMessage
operator|==
literal|null
condition|)
block|{
name|outMessage
operator|=
name|endpoint
operator|.
name|getBinding
argument_list|()
operator|.
name|createMessage
argument_list|()
expr_stmt|;
block|}
name|exchange
operator|.
name|setOutMessage
argument_list|(
name|outMessage
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|updateContext (Map<String, Object> from, Map<String, Object> to)
specifier|public
name|void
name|updateContext
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|from
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|to
parameter_list|)
block|{
if|if
condition|(
name|to
operator|!=
literal|null
operator|&&
name|from
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Iterator
name|iter
init|=
name|from
operator|.
name|entrySet
argument_list|()
operator|.
name|iterator
argument_list|()
init|;
name|iter
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|Map
operator|.
name|Entry
name|entry
init|=
operator|(
name|Map
operator|.
name|Entry
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|String
name|key
init|=
operator|(
name|String
operator|)
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
comment|//Requires deep copy.
if|if
condition|(
operator|!
operator|(
name|Message
operator|.
name|INBOUND_MESSAGE
operator|.
name|equals
argument_list|(
name|key
argument_list|)
operator|||
name|Message
operator|.
name|REQUESTOR_ROLE
operator|.
name|equals
argument_list|(
name|key
argument_list|)
operator|||
name|Message
operator|.
name|PROTOCOL_HEADERS
operator|.
name|equals
argument_list|(
name|key
argument_list|)
operator|)
condition|)
block|{
name|to
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
comment|/**      * This method is called when the incoming pojo or WebServiceProvider invocation is called      * from the service invocation interceptor. The return value is the response      * from the processor      * @param inMessage      * @return outMessage      */
DECL|method|invoke (Exchange exchange, Object o)
specifier|public
name|Object
name|invoke
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Object
name|o
parameter_list|)
block|{
name|CxfEndpoint
name|endpoint
init|=
operator|(
name|CxfEndpoint
operator|)
name|cxfConsumer
operator|.
name|getEndpoint
argument_list|()
decl_stmt|;
name|Object
name|params
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|o
operator|instanceof
name|List
condition|)
block|{
name|params
operator|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|List
argument_list|<
name|?
argument_list|>
operator|)
name|o
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|o
operator|!=
literal|null
condition|)
block|{
name|params
operator|=
operator|new
name|MessageContentsList
argument_list|(
name|o
argument_list|)
expr_stmt|;
block|}
name|CxfExchange
name|cxfExchange
init|=
name|endpoint
operator|.
name|createExchange
argument_list|(
name|exchange
operator|.
name|getInMessage
argument_list|()
argument_list|)
decl_stmt|;
name|BindingOperationInfo
name|bop
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
name|MethodDispatcher
name|md
init|=
operator|(
name|MethodDispatcher
operator|)
name|exchange
operator|.
name|get
argument_list|(
name|Service
operator|.
name|class
argument_list|)
operator|.
name|get
argument_list|(
name|MethodDispatcher
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|Method
name|m
init|=
name|md
operator|.
name|getMethod
argument_list|(
name|bop
argument_list|)
decl_stmt|;
if|if
condition|(
name|bop
operator|.
name|getOperationInfo
argument_list|()
operator|.
name|isOneWay
argument_list|()
condition|)
block|{
name|cxfExchange
operator|.
name|setPattern
argument_list|(
name|ExchangePattern
operator|.
name|InOnly
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|cxfExchange
operator|.
name|setPattern
argument_list|(
name|ExchangePattern
operator|.
name|InOut
argument_list|)
expr_stmt|;
block|}
name|cxfExchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|CxfConstants
operator|.
name|OPERATION_NAME
argument_list|,
name|m
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|cxfExchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|params
argument_list|)
expr_stmt|;
try|try
block|{
name|cxfConsumer
operator|.
name|getProcessor
argument_list|()
operator|.
name|process
argument_list|(
name|cxfExchange
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
comment|// catch the exception and send back to cxf client
throw|throw
operator|new
name|Fault
argument_list|(
name|ex
argument_list|)
throw|;
block|}
name|Object
name|result
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|cxfExchange
operator|.
name|isFailed
argument_list|()
condition|)
block|{
name|Exception
name|ex
init|=
operator|(
name|Exception
operator|)
name|cxfExchange
operator|.
name|getFault
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
throw|throw
operator|new
name|Fault
argument_list|(
name|ex
argument_list|)
throw|;
block|}
else|else
block|{
name|result
operator|=
name|cxfExchange
operator|.
name|getOut
argument_list|()
operator|.
name|getBody
argument_list|()
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
block|}
end_class

end_unit

