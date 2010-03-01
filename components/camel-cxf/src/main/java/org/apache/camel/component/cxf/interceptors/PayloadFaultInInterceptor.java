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
name|SoapFault
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
name|staxutils
operator|.
name|StaxUtils
import|;
end_import

begin_comment
comment|/**  * Interceptor to create a Fault object from a CXF message  *   * @version @Revision: 789534 $  */
end_comment

begin_class
DECL|class|PayloadFaultInInterceptor
specifier|public
class|class
name|PayloadFaultInInterceptor
extends|extends
name|AbstractPhaseInterceptor
argument_list|<
name|Message
argument_list|>
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
name|PayloadFaultInInterceptor
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
DECL|method|PayloadFaultInInterceptor ()
specifier|public
name|PayloadFaultInInterceptor
parameter_list|()
block|{
name|this
argument_list|(
name|Phase
operator|.
name|POST_PROTOCOL
argument_list|)
expr_stmt|;
block|}
DECL|method|PayloadFaultInInterceptor (String phase)
specifier|public
name|PayloadFaultInInterceptor
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
DECL|method|handleMessage (Message message)
specifier|public
name|void
name|handleMessage
parameter_list|(
name|Message
name|message
parameter_list|)
throws|throws
name|Fault
block|{
name|XMLStreamReader
name|reader
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
if|if
condition|(
name|message
operator|instanceof
name|SoapMessage
condition|)
block|{
name|message
operator|.
name|setContent
argument_list|(
name|Exception
operator|.
name|class
argument_list|,
name|createFault
argument_list|(
operator|(
name|SoapMessage
operator|)
name|message
argument_list|,
name|reader
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|LOG
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|FINE
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"Message type '"
operator|+
name|message
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|"' is not supported."
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|createFault (SoapMessage message, XMLStreamReader reader)
specifier|public
specifier|static
name|SoapFault
name|createFault
parameter_list|(
name|SoapMessage
name|message
parameter_list|,
name|XMLStreamReader
name|reader
parameter_list|)
block|{
name|String
name|exMessage
init|=
literal|null
decl_stmt|;
name|QName
name|faultCode
init|=
literal|null
decl_stmt|;
name|String
name|role
init|=
literal|null
decl_stmt|;
name|Element
name|detail
init|=
literal|null
decl_stmt|;
try|try
block|{
while|while
condition|(
name|reader
operator|.
name|nextTag
argument_list|()
operator|==
name|XMLStreamReader
operator|.
name|START_ELEMENT
condition|)
block|{
if|if
condition|(
name|reader
operator|.
name|getLocalName
argument_list|()
operator|.
name|equals
argument_list|(
literal|"faultcode"
argument_list|)
condition|)
block|{
name|faultCode
operator|=
name|StaxUtils
operator|.
name|readQName
argument_list|(
name|reader
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|reader
operator|.
name|getLocalName
argument_list|()
operator|.
name|equals
argument_list|(
literal|"faultstring"
argument_list|)
condition|)
block|{
name|exMessage
operator|=
name|reader
operator|.
name|getElementText
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|reader
operator|.
name|getLocalName
argument_list|()
operator|.
name|equals
argument_list|(
literal|"faultactor"
argument_list|)
condition|)
block|{
name|role
operator|=
name|reader
operator|.
name|getElementText
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|reader
operator|.
name|getLocalName
argument_list|()
operator|.
name|equals
argument_list|(
literal|"detail"
argument_list|)
condition|)
block|{
name|detail
operator|=
name|StaxUtils
operator|.
name|read
argument_list|(
name|reader
argument_list|)
operator|.
name|getDocumentElement
argument_list|()
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|XMLStreamException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|SoapFault
argument_list|(
literal|"Could not parse message."
argument_list|,
name|e
argument_list|,
name|message
operator|.
name|getVersion
argument_list|()
operator|.
name|getSender
argument_list|()
argument_list|)
throw|;
block|}
name|SoapFault
name|fault
init|=
operator|new
name|SoapFault
argument_list|(
name|exMessage
argument_list|,
name|faultCode
argument_list|)
decl_stmt|;
name|fault
operator|.
name|setDetail
argument_list|(
name|detail
argument_list|)
expr_stmt|;
name|fault
operator|.
name|setRole
argument_list|(
name|role
argument_list|)
expr_stmt|;
return|return
name|fault
return|;
block|}
block|}
end_class

end_unit

