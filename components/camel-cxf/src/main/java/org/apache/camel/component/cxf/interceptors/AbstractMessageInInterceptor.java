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
name|ResourceBundle
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
name|javax
operator|.
name|xml
operator|.
name|namespace
operator|.
name|QName
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|XMLStreamException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|XMLStreamReader
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Document
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Element
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
name|common
operator|.
name|i18n
operator|.
name|BundleUtils
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
name|common
operator|.
name|logging
operator|.
name|LogUtils
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
name|BindingInfo
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
name|service
operator|.
name|model
operator|.
name|OperationInfo
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
name|staxutils
operator|.
name|StaxUtils
import|;
end_import

begin_comment
comment|/**  * This is the base class for message interceptors that intercepts message as DOM content  * infers the BindingOperationInfo and then set the  *  */
end_comment

begin_class
DECL|class|AbstractMessageInInterceptor
specifier|public
specifier|abstract
class|class
name|AbstractMessageInInterceptor
parameter_list|<
name|T
extends|extends
name|Message
parameter_list|>
extends|extends
name|AbstractPhaseInterceptor
argument_list|<
name|T
argument_list|>
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|AbstractMessageInInterceptor
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|AbstractMessageInInterceptor (String phase)
specifier|public
name|AbstractMessageInInterceptor
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
specifier|protected
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
comment|/**      * Infer the OperationInfo from the XML Document and get the list of       * parts as DOM Element      */
DECL|method|handleMessage (T message)
specifier|public
name|void
name|handleMessage
parameter_list|(
name|T
name|message
parameter_list|)
throws|throws
name|Fault
block|{
name|Logger
name|logger
init|=
name|getLogger
argument_list|()
decl_stmt|;
if|if
condition|(
name|isFaultMessage
argument_list|(
name|message
argument_list|)
condition|)
block|{
name|message
operator|.
name|getInterceptorChain
argument_list|()
operator|.
name|abort
argument_list|()
expr_stmt|;
name|Endpoint
name|ep
init|=
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|get
argument_list|(
name|Endpoint
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|ep
operator|.
name|getInFaultObserver
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|ep
operator|.
name|getInFaultObserver
argument_list|()
operator|.
name|onMessage
argument_list|(
name|message
argument_list|)
expr_stmt|;
return|return;
block|}
comment|//Fault f = createFault(message, payloadEl);
comment|//message.setContent(Exception.class, f);
comment|//return;
block|}
name|Document
name|document
init|=
name|createDOMMessage
argument_list|(
name|message
argument_list|)
decl_stmt|;
comment|//Document document = message.getContent(Document.class);
name|Element
name|payloadEl
init|=
operator|(
name|Element
operator|)
name|document
operator|.
name|getChildNodes
argument_list|()
operator|.
name|item
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|Exchange
name|ex
init|=
name|message
operator|.
name|getExchange
argument_list|()
decl_stmt|;
name|BindingOperationInfo
name|boi
init|=
name|ex
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
operator|==
literal|null
condition|)
block|{
name|BindingInfo
name|bi
init|=
name|ex
operator|.
name|get
argument_list|(
name|BindingInfo
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|bi
operator|==
literal|null
condition|)
block|{
name|Endpoint
name|ep
init|=
name|ex
operator|.
name|get
argument_list|(
name|Endpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|bi
operator|=
name|ep
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|getBinding
argument_list|()
expr_stmt|;
name|ex
operator|.
name|put
argument_list|(
name|BindingInfo
operator|.
name|class
argument_list|,
name|bi
argument_list|)
expr_stmt|;
block|}
comment|// handling inbound message
if|if
condition|(
name|logger
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|INFO
argument_list|)
condition|)
block|{
name|logger
operator|.
name|info
argument_list|(
literal|"AbstractRoutingMessageInInterceptor Infer BindingOperationInfo."
argument_list|)
expr_stmt|;
block|}
name|boi
operator|=
name|getBindingOperation
argument_list|(
name|message
argument_list|,
name|document
argument_list|)
expr_stmt|;
if|if
condition|(
name|boi
operator|==
literal|null
condition|)
block|{
name|QName
name|startQName
init|=
operator|new
name|QName
argument_list|(
name|payloadEl
operator|.
name|getNamespaceURI
argument_list|()
argument_list|,
name|payloadEl
operator|.
name|getLocalName
argument_list|()
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"StartQName "
operator|+
name|startQName
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|Fault
argument_list|(
operator|new
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|common
operator|.
name|i18n
operator|.
name|Message
argument_list|(
literal|"REQ_NOT_UNDERSTOOD"
argument_list|,
name|LOG
argument_list|,
name|startQName
argument_list|)
argument_list|)
throw|;
block|}
if|if
condition|(
name|boi
operator|!=
literal|null
condition|)
block|{
name|ex
operator|.
name|put
argument_list|(
name|BindingOperationInfo
operator|.
name|class
argument_list|,
name|boi
argument_list|)
expr_stmt|;
name|ex
operator|.
name|put
argument_list|(
name|OperationInfo
operator|.
name|class
argument_list|,
name|boi
operator|.
name|getOperationInfo
argument_list|()
argument_list|)
expr_stmt|;
name|ex
operator|.
name|setOneWay
argument_list|(
name|boi
operator|.
name|getOperationInfo
argument_list|()
operator|.
name|isOneWay
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|logger
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|INFO
argument_list|)
condition|)
block|{
name|logger
operator|.
name|info
argument_list|(
literal|"DOMInInterceptor- BindingOperation is:"
operator|+
name|boi
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|BindingMessageInfo
name|bmi
init|=
name|isRequestor
argument_list|(
name|message
argument_list|)
condition|?
name|boi
operator|.
name|getOutput
argument_list|()
else|:
name|boi
operator|.
name|getInput
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|Element
argument_list|>
name|partList
init|=
name|getPartList
argument_list|(
name|message
argument_list|,
name|payloadEl
argument_list|,
name|bmi
argument_list|)
decl_stmt|;
name|message
operator|.
name|put
argument_list|(
name|List
operator|.
name|class
argument_list|,
name|partList
argument_list|)
expr_stmt|;
name|Element
name|header
init|=
name|getHeader
argument_list|(
name|message
argument_list|)
decl_stmt|;
name|message
operator|.
name|put
argument_list|(
name|Element
operator|.
name|class
argument_list|,
name|header
argument_list|)
expr_stmt|;
block|}
comment|/**      * This method is called to convert a incoming message format e.g Stax Stream      * to a DOM Tree. Default Implementation converts Stax Stream to a DOM      * @param inMessage      * @param Document      */
DECL|method|createDOMMessage (T message)
specifier|protected
name|Document
name|createDOMMessage
parameter_list|(
name|T
name|message
parameter_list|)
block|{
name|Document
name|doc
init|=
literal|null
decl_stmt|;
try|try
block|{
if|if
condition|(
name|getLogger
argument_list|()
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|INFO
argument_list|)
condition|)
block|{
name|getLogger
argument_list|()
operator|.
name|info
argument_list|(
literal|"AbstractMessageInInterceptor Converting Stax Stream to DOM"
argument_list|)
expr_stmt|;
block|}
name|XMLStreamReader
name|xsr
init|=
name|message
operator|.
name|getContent
argument_list|(
name|XMLStreamReader
operator|.
name|class
argument_list|)
decl_stmt|;
name|doc
operator|=
name|StaxUtils
operator|.
name|read
argument_list|(
name|xsr
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|XMLStreamException
name|xe
parameter_list|)
block|{
throw|throw
operator|new
name|Fault
argument_list|(
operator|new
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|common
operator|.
name|i18n
operator|.
name|Message
argument_list|(
literal|"STAX_READ_EXC"
argument_list|,
name|LOG
argument_list|)
argument_list|,
name|xe
argument_list|)
throw|;
block|}
return|return
name|doc
return|;
block|}
DECL|method|getLogger ()
specifier|protected
specifier|abstract
name|Logger
name|getLogger
parameter_list|()
function_decl|;
comment|/**      * This method is called on incoming to check if it is a fault.      * @param inMessage      * @param boolean      */
DECL|method|isFaultMessage (T message)
specifier|protected
specifier|abstract
name|boolean
name|isFaultMessage
parameter_list|(
name|T
name|message
parameter_list|)
function_decl|;
comment|/**      * This method is called when the routing message interceptor has received a inbound message      * It infers the binding operation by matching the root Element with a binding operation      * from the service model.      * @param inMessage      * @param resultPayload      */
DECL|method|getBindingOperation (T inMessage, Document document)
specifier|protected
specifier|abstract
name|BindingOperationInfo
name|getBindingOperation
parameter_list|(
name|T
name|inMessage
parameter_list|,
name|Document
name|document
parameter_list|)
function_decl|;
comment|/**      * This method is called when the routing message interceptor has intercepted a inbound      * message as a DOM Content.  It retreives the message parts as DOM Element      * and returns a List<Element>      * @param inMessage      * @param rootElement      * @param bindingMessageInfo      * @return List<Element>      */
DECL|method|getPartList (T inMessage, Element rootElement, BindingMessageInfo boi)
specifier|protected
specifier|abstract
name|List
argument_list|<
name|Element
argument_list|>
name|getPartList
parameter_list|(
name|T
name|inMessage
parameter_list|,
name|Element
name|rootElement
parameter_list|,
name|BindingMessageInfo
name|boi
parameter_list|)
function_decl|;
comment|/**      * This method is called when the routing message interceptor has intercepted a inbound      * message as a DOM Content.  It retreives the header parts as DOM Element      * and returns a Element.      * @param inMessage      * @return Element      */
DECL|method|getHeader (T inMessage)
specifier|protected
specifier|abstract
name|Element
name|getHeader
parameter_list|(
name|T
name|inMessage
parameter_list|)
function_decl|;
block|}
end_class

end_unit

