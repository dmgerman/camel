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
name|ArrayList
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
name|wsdl
operator|.
name|Definition
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
name|binding
operator|.
name|soap
operator|.
name|SoapMessage
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
name|binding
operator|.
name|soap
operator|.
name|SoapVersion
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
name|binding
operator|.
name|soap
operator|.
name|model
operator|.
name|SoapBindingInfo
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
name|binding
operator|.
name|soap
operator|.
name|model
operator|.
name|SoapHeaderInfo
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
name|MessagePartInfo
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
name|wsdl11
operator|.
name|WSDLServiceBuilder
import|;
end_import

begin_class
DECL|class|SoapMessageOutInterceptor
specifier|public
class|class
name|SoapMessageOutInterceptor
extends|extends
name|AbstractMessageOutInterceptor
argument_list|<
name|SoapMessage
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
name|SoapMessageInInterceptor
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|SoapMessageOutInterceptor ()
specifier|public
name|SoapMessageOutInterceptor
parameter_list|()
block|{
name|super
argument_list|(
name|Phase
operator|.
name|PREPARE_SEND
argument_list|)
expr_stmt|;
name|addAfter
argument_list|(
name|DOMOutInterceptor
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|getLogger ()
specifier|protected
name|Logger
name|getLogger
parameter_list|()
block|{
return|return
name|LOG
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|handleMessage (SoapMessage message)
specifier|public
name|void
name|handleMessage
parameter_list|(
name|SoapMessage
name|message
parameter_list|)
throws|throws
name|Fault
block|{
comment|// header is not store as the element
name|Element
name|header
init|=
name|message
operator|.
name|get
argument_list|(
name|Element
operator|.
name|class
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Element
argument_list|>
name|payload
init|=
name|message
operator|.
name|get
argument_list|(
name|List
operator|.
name|class
argument_list|)
decl_stmt|;
name|Exchange
name|exchange
init|=
name|message
operator|.
name|getExchange
argument_list|()
decl_stmt|;
name|BindingMessageInfo
name|bmi
init|=
name|exchange
operator|.
name|get
argument_list|(
name|BindingMessageInfo
operator|.
name|class
argument_list|)
decl_stmt|;
comment|//Headers -represent as -Element,Body -represent as StaxStream.
comment|//Check if BindingOperationInfo contains header
name|List
argument_list|<
name|SoapHeaderInfo
argument_list|>
name|bindingHdr
init|=
name|bmi
operator|.
name|getExtensors
argument_list|(
name|SoapHeaderInfo
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|bindingHdr
operator|!=
literal|null
operator|&&
operator|!
name|bindingHdr
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
if|if
condition|(
name|LOG
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|INFO
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"SoapMessageOutInterceptor BindingOperation header processing."
argument_list|)
expr_stmt|;
block|}
name|List
argument_list|<
name|Element
argument_list|>
name|headerList
init|=
operator|new
name|ArrayList
argument_list|<
name|Element
argument_list|>
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|Element
argument_list|>
name|newPayload
init|=
operator|new
name|ArrayList
argument_list|<
name|Element
argument_list|>
argument_list|(
name|payload
argument_list|)
decl_stmt|;
comment|//Look for headers in Payload.
for|for
control|(
name|SoapHeaderInfo
name|shi
range|:
name|bindingHdr
control|)
block|{
name|List
argument_list|<
name|Element
argument_list|>
name|tmpList
init|=
operator|new
name|ArrayList
argument_list|<
name|Element
argument_list|>
argument_list|()
decl_stmt|;
name|MessagePartInfo
name|mpi
init|=
name|shi
operator|.
name|getPart
argument_list|()
decl_stmt|;
name|QName
name|hdrName
init|=
name|mpi
operator|.
name|getConcreteName
argument_list|()
decl_stmt|;
for|for
control|(
name|Element
name|el
range|:
name|payload
control|)
block|{
name|QName
name|elName
init|=
operator|new
name|QName
argument_list|(
name|el
operator|.
name|getNamespaceURI
argument_list|()
argument_list|,
name|el
operator|.
name|getLocalName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|elName
operator|.
name|equals
argument_list|(
name|hdrName
argument_list|)
condition|)
block|{
name|newPayload
operator|.
name|remove
argument_list|(
name|el
argument_list|)
expr_stmt|;
name|tmpList
operator|.
name|add
argument_list|(
name|el
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|tmpList
operator|.
name|size
argument_list|()
operator|>
literal|1
condition|)
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
literal|"MULTIPLE_HDR_PARTS"
argument_list|,
name|LOG
argument_list|,
name|hdrName
argument_list|)
argument_list|)
throw|;
block|}
name|headerList
operator|.
name|addAll
argument_list|(
name|tmpList
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|LOG
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|INFO
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"DOMOutInterceptor Copy Payload parts to SOAPHeaders"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|headerList
operator|.
name|size
argument_list|()
operator|!=
literal|0
condition|)
block|{
name|SoapVersion
name|version
init|=
operator|(
operator|(
name|SoapMessage
operator|)
name|message
operator|)
operator|.
name|getVersion
argument_list|()
decl_stmt|;
name|header
operator|=
name|createElement
argument_list|(
name|version
operator|.
name|getHeader
argument_list|()
argument_list|,
name|headerList
argument_list|)
expr_stmt|;
block|}
name|payload
operator|=
name|newPayload
expr_stmt|;
block|}
comment|//Set SOAP Header Element.
comment|//Child Elements Could be binding specified parts or user specified headers.
comment|//REVISTED the soap headers
comment|//message.setHeaders(Element.class, header);
comment|//TODO Moving Parts from Header to Payload.
comment|//For e.g Payload ROuting from SOAP11<-> SOAP12
comment|//So write payload and header to outbound message
if|if
condition|(
name|LOG
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|INFO
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"SoapMessageOutInterceptor binding operation style processing."
argument_list|)
expr_stmt|;
block|}
name|SoapBindingInfo
name|soapBinding
init|=
operator|(
name|SoapBindingInfo
operator|)
name|exchange
operator|.
name|get
argument_list|(
name|BindingInfo
operator|.
name|class
argument_list|)
decl_stmt|;
name|String
name|style
init|=
name|soapBinding
operator|.
name|getStyle
argument_list|(
name|bmi
operator|.
name|getBindingOperation
argument_list|()
operator|.
name|getOperationInfo
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
literal|"rpc"
operator|.
name|equals
argument_list|(
name|style
argument_list|)
condition|)
block|{
comment|//Add the Operation Node or Operation+"Response" node
comment|//Remove the operation element.
name|OperationInfo
name|oi
init|=
name|bmi
operator|.
name|getBindingOperation
argument_list|()
operator|.
name|getOperationInfo
argument_list|()
decl_stmt|;
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
name|Definition
name|def
init|=
name|ep
operator|.
name|getService
argument_list|()
operator|.
name|getServiceInfos
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getProperty
argument_list|(
name|WSDLServiceBuilder
operator|.
name|WSDL_DEFINITION
argument_list|,
name|Definition
operator|.
name|class
argument_list|)
decl_stmt|;
name|String
name|prefix
init|=
name|def
operator|.
name|getPrefix
argument_list|(
name|oi
operator|.
name|getName
argument_list|()
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
literal|""
operator|.
name|equals
argument_list|(
name|prefix
argument_list|)
condition|)
block|{
name|prefix
operator|=
literal|"tns"
expr_stmt|;
block|}
name|QName
name|opName
init|=
literal|null
decl_stmt|;
name|boolean
name|isClient
init|=
name|isRequestor
argument_list|(
name|message
argument_list|)
decl_stmt|;
if|if
condition|(
name|isClient
condition|)
block|{
name|opName
operator|=
operator|new
name|QName
argument_list|(
name|oi
operator|.
name|getName
argument_list|()
operator|.
name|getNamespaceURI
argument_list|()
argument_list|,
name|oi
operator|.
name|getName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
argument_list|,
name|prefix
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|opName
operator|=
operator|new
name|QName
argument_list|(
name|oi
operator|.
name|getName
argument_list|()
operator|.
name|getNamespaceURI
argument_list|()
argument_list|,
name|oi
operator|.
name|getName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
operator|+
literal|"Response"
argument_list|,
name|prefix
argument_list|)
expr_stmt|;
block|}
name|Element
name|opEl
init|=
name|createElement
argument_list|(
name|opName
argument_list|,
name|payload
argument_list|)
decl_stmt|;
name|payload
operator|=
operator|new
name|ArrayList
argument_list|<
name|Element
argument_list|>
argument_list|()
expr_stmt|;
name|payload
operator|.
name|add
argument_list|(
name|opEl
argument_list|)
expr_stmt|;
block|}
name|message
operator|.
name|put
argument_list|(
name|List
operator|.
name|class
argument_list|,
name|payload
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

