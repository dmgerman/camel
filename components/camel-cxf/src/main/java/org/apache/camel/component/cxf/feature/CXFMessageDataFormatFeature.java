begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|soap
operator|.
name|SOAPMessage
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|Source
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
name|Bus
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
name|Binding
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
name|SoapBinding
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
name|saaj
operator|.
name|SAAJInInterceptor
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
name|saaj
operator|.
name|SAAJOutInterceptor
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
name|Client
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
name|endpoint
operator|.
name|Server
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
name|AbstractInDatabindingInterceptor
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
name|ClientFaultConverter
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
name|jaxws
operator|.
name|interceptors
operator|.
name|HolderInInterceptor
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
name|jaxws
operator|.
name|interceptors
operator|.
name|HolderOutInterceptor
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
name|jaxws
operator|.
name|interceptors
operator|.
name|MessageModeInInterceptor
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
name|jaxws
operator|.
name|interceptors
operator|.
name|MessageModeOutInterceptor
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
name|jaxws
operator|.
name|interceptors
operator|.
name|WrapperClassInInterceptor
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
name|jaxws
operator|.
name|interceptors
operator|.
name|WrapperClassOutInterceptor
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
name|MessageInfo
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_comment
comment|/**  *<p>  * MessageDataFormatFeature sets up the CXF endpoint interceptor for handling the  * Message in Message data format.    *</p>  */
end_comment

begin_class
DECL|class|CXFMessageDataFormatFeature
specifier|public
class|class
name|CXFMessageDataFormatFeature
extends|extends
name|AbstractDataFormatFeature
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|CXFMessageDataFormatFeature
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|REMOVING_IN_INTERCEPTORS
specifier|private
specifier|static
specifier|final
name|Collection
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|REMOVING_IN_INTERCEPTORS
decl_stmt|;
DECL|field|REMOVING_OUT_INTERCEPTORS
specifier|private
specifier|static
specifier|final
name|Collection
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|REMOVING_OUT_INTERCEPTORS
decl_stmt|;
static|static
block|{
name|REMOVING_IN_INTERCEPTORS
operator|=
operator|new
name|ArrayList
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|()
expr_stmt|;
name|REMOVING_IN_INTERCEPTORS
operator|.
name|add
argument_list|(
name|HolderInInterceptor
operator|.
name|class
argument_list|)
expr_stmt|;
name|REMOVING_IN_INTERCEPTORS
operator|.
name|add
argument_list|(
name|WrapperClassInInterceptor
operator|.
name|class
argument_list|)
expr_stmt|;
name|REMOVING_OUT_INTERCEPTORS
operator|=
operator|new
name|ArrayList
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|()
expr_stmt|;
name|REMOVING_OUT_INTERCEPTORS
operator|.
name|add
argument_list|(
name|HolderOutInterceptor
operator|.
name|class
argument_list|)
expr_stmt|;
name|REMOVING_OUT_INTERCEPTORS
operator|.
name|add
argument_list|(
name|WrapperClassOutInterceptor
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|initialize (Client client, Bus bus)
specifier|public
name|void
name|initialize
parameter_list|(
name|Client
name|client
parameter_list|,
name|Bus
name|bus
parameter_list|)
block|{
name|setupEndpoint
argument_list|(
name|client
operator|.
name|getEndpoint
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|initialize (Server server, Bus bus)
specifier|public
name|void
name|initialize
parameter_list|(
name|Server
name|server
parameter_list|,
name|Bus
name|bus
parameter_list|)
block|{
name|setupEndpoint
argument_list|(
name|server
operator|.
name|getEndpoint
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|setupEndpoint (Endpoint ep)
specifier|protected
name|void
name|setupEndpoint
parameter_list|(
name|Endpoint
name|ep
parameter_list|)
block|{
name|resetPartTypes
argument_list|(
name|ep
operator|.
name|getBinding
argument_list|()
argument_list|)
expr_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|fmt
init|=
name|Source
operator|.
name|class
decl_stmt|;
if|if
condition|(
name|ep
operator|.
name|getBinding
argument_list|()
operator|instanceof
name|SoapBinding
condition|)
block|{
name|ep
operator|.
name|getInInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|SAAJInInterceptor
argument_list|()
argument_list|)
expr_stmt|;
name|SAAJOutInterceptor
name|out
init|=
operator|new
name|SAAJOutInterceptor
argument_list|()
decl_stmt|;
name|ep
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|ep
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|MessageModeOutInterceptor
argument_list|(
name|out
argument_list|,
name|ep
operator|.
name|getBinding
argument_list|()
operator|.
name|getBindingInfo
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|fmt
operator|=
name|SOAPMessage
operator|.
name|class
expr_stmt|;
block|}
else|else
block|{
name|ep
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|MessageModeOutInterceptor
argument_list|(
name|Source
operator|.
name|class
argument_list|,
name|ep
operator|.
name|getBinding
argument_list|()
operator|.
name|getBindingInfo
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|ep
operator|.
name|getInInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|MessageModeInInterceptor
argument_list|(
name|fmt
argument_list|,
name|ep
operator|.
name|getBinding
argument_list|()
operator|.
name|getBindingInfo
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|ep
operator|.
name|put
argument_list|(
name|AbstractInDatabindingInterceptor
operator|.
name|NO_VALIDATE_PARTS
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
comment|// need to remove the wrapper class and holder interceptor
name|removeInterceptors
argument_list|(
name|ep
operator|.
name|getInInterceptors
argument_list|()
argument_list|,
name|REMOVING_IN_INTERCEPTORS
argument_list|)
expr_stmt|;
name|removeInterceptors
argument_list|(
name|ep
operator|.
name|getOutInterceptors
argument_list|()
argument_list|,
name|REMOVING_OUT_INTERCEPTORS
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
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
DECL|method|resetPartTypes (Binding bop2)
specifier|private
name|void
name|resetPartTypes
parameter_list|(
name|Binding
name|bop2
parameter_list|)
block|{
comment|// The HypbridSourceDatabinding, based on JAXB, will possibly set
comment|// JAXB types into the parts.  Since we need the Source objects,
comment|// we'll reset the types to either Source (for streaming), or null
comment|// (for non-streaming, defaults to DOMSource.
for|for
control|(
name|BindingOperationInfo
name|bop
range|:
name|bop2
operator|.
name|getBindingInfo
argument_list|()
operator|.
name|getOperations
argument_list|()
control|)
block|{
name|resetPartTypes
argument_list|(
name|bop
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|resetPartTypes (BindingOperationInfo bop)
specifier|private
name|void
name|resetPartTypes
parameter_list|(
name|BindingOperationInfo
name|bop
parameter_list|)
block|{
if|if
condition|(
name|bop
operator|.
name|isUnwrapped
argument_list|()
condition|)
block|{
name|bop
operator|=
name|bop
operator|.
name|getWrappedOperation
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|bop
operator|.
name|isUnwrappedCapable
argument_list|()
condition|)
block|{
name|resetPartTypeClass
argument_list|(
name|bop
operator|.
name|getWrappedOperation
argument_list|()
operator|.
name|getOperationInfo
argument_list|()
operator|.
name|getInput
argument_list|()
argument_list|)
expr_stmt|;
name|resetPartTypeClass
argument_list|(
name|bop
operator|.
name|getWrappedOperation
argument_list|()
operator|.
name|getOperationInfo
argument_list|()
operator|.
name|getOutput
argument_list|()
argument_list|)
expr_stmt|;
name|resetPartTypeClass
argument_list|(
name|bop
operator|.
name|getWrappedOperation
argument_list|()
operator|.
name|getInput
argument_list|()
argument_list|)
expr_stmt|;
name|resetPartTypeClass
argument_list|(
name|bop
operator|.
name|getWrappedOperation
argument_list|()
operator|.
name|getOutput
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|resetPartTypeClass
argument_list|(
name|bop
operator|.
name|getOperationInfo
argument_list|()
operator|.
name|getInput
argument_list|()
argument_list|)
expr_stmt|;
name|resetPartTypeClass
argument_list|(
name|bop
operator|.
name|getOperationInfo
argument_list|()
operator|.
name|getOutput
argument_list|()
argument_list|)
expr_stmt|;
name|resetPartTypeClass
argument_list|(
name|bop
operator|.
name|getInput
argument_list|()
argument_list|)
expr_stmt|;
name|resetPartTypeClass
argument_list|(
name|bop
operator|.
name|getOutput
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|resetPartTypeClass (BindingMessageInfo bmi)
specifier|protected
name|void
name|resetPartTypeClass
parameter_list|(
name|BindingMessageInfo
name|bmi
parameter_list|)
block|{
if|if
condition|(
name|bmi
operator|!=
literal|null
condition|)
block|{
name|int
name|size
init|=
name|bmi
operator|.
name|getMessageParts
argument_list|()
operator|.
name|size
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|x
init|=
literal|0
init|;
name|x
operator|<
name|size
condition|;
name|x
operator|++
control|)
block|{
name|bmi
operator|.
name|getMessageParts
argument_list|()
operator|.
name|get
argument_list|(
name|x
argument_list|)
operator|.
name|setTypeClass
argument_list|(
name|Source
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|resetPartTypeClass (MessageInfo msgInfo)
specifier|protected
name|void
name|resetPartTypeClass
parameter_list|(
name|MessageInfo
name|msgInfo
parameter_list|)
block|{
if|if
condition|(
name|msgInfo
operator|!=
literal|null
condition|)
block|{
name|int
name|size
init|=
name|msgInfo
operator|.
name|getMessageParts
argument_list|()
operator|.
name|size
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|x
init|=
literal|0
init|;
name|x
operator|<
name|size
condition|;
name|x
operator|++
control|)
block|{
name|msgInfo
operator|.
name|getMessageParts
argument_list|()
operator|.
name|get
argument_list|(
name|x
argument_list|)
operator|.
name|setTypeClass
argument_list|(
name|Source
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

