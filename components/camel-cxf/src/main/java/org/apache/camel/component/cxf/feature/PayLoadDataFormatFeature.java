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
name|java
operator|.
name|util
operator|.
name|Collections
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
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|dom
operator|.
name|DOMSource
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
name|interceptors
operator|.
name|ConfigureDocLitWrapperInterceptor
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
name|interceptor
operator|.
name|SoapHeaderInterceptor
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
comment|/**  * This feature just setting up the CXF endpoint interceptor for handling the  * Message in PAYLOAD data format  */
end_comment

begin_class
DECL|class|PayLoadDataFormatFeature
specifier|public
class|class
name|PayLoadDataFormatFeature
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
name|PayLoadDataFormatFeature
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|REMOVING_FAULT_IN_INTERCEPTORS
specifier|private
specifier|static
specifier|final
name|Collection
argument_list|<
name|Class
argument_list|>
name|REMOVING_FAULT_IN_INTERCEPTORS
decl_stmt|;
DECL|field|DEFAULT_ALLOW_STREAMING
specifier|private
specifier|static
specifier|final
name|boolean
name|DEFAULT_ALLOW_STREAMING
decl_stmt|;
static|static
block|{
name|REMOVING_FAULT_IN_INTERCEPTORS
operator|=
operator|new
name|ArrayList
argument_list|<
name|Class
argument_list|>
argument_list|()
expr_stmt|;
name|REMOVING_FAULT_IN_INTERCEPTORS
operator|.
name|add
argument_list|(
name|ClientFaultConverter
operator|.
name|class
argument_list|)
expr_stmt|;
name|String
name|s
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"org.apache.camel.component.cxf.streaming"
argument_list|)
decl_stmt|;
name|DEFAULT_ALLOW_STREAMING
operator|=
name|s
operator|==
literal|null
operator|||
name|Boolean
operator|.
name|parseBoolean
argument_list|(
name|s
argument_list|)
expr_stmt|;
block|}
DECL|field|allowStreaming
name|boolean
name|allowStreaming
init|=
name|DEFAULT_ALLOW_STREAMING
decl_stmt|;
DECL|method|PayLoadDataFormatFeature ()
specifier|public
name|PayLoadDataFormatFeature
parameter_list|()
block|{     }
DECL|method|PayLoadDataFormatFeature (Boolean streaming)
specifier|public
name|PayLoadDataFormatFeature
parameter_list|(
name|Boolean
name|streaming
parameter_list|)
block|{
if|if
condition|(
name|streaming
operator|!=
literal|null
condition|)
block|{
name|allowStreaming
operator|=
name|streaming
expr_stmt|;
block|}
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
name|removeFaultInInterceptorFromClient
argument_list|(
name|client
argument_list|)
expr_stmt|;
name|removeInterceptor
argument_list|(
name|client
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getInInterceptors
argument_list|()
argument_list|,
name|HolderInInterceptor
operator|.
name|class
argument_list|)
expr_stmt|;
name|removeInterceptor
argument_list|(
name|client
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getBinding
argument_list|()
operator|.
name|getInInterceptors
argument_list|()
argument_list|,
name|SoapHeaderInterceptor
operator|.
name|class
argument_list|)
expr_stmt|;
name|client
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getBinding
argument_list|()
operator|.
name|getInInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|ConfigureDocLitWrapperInterceptor
argument_list|(
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|resetPartTypes
argument_list|(
name|client
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getBinding
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
name|server
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getBinding
argument_list|()
operator|.
name|getInInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|ConfigureDocLitWrapperInterceptor
argument_list|(
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|removeInterceptor
argument_list|(
name|server
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getInInterceptors
argument_list|()
argument_list|,
name|HolderInInterceptor
operator|.
name|class
argument_list|)
expr_stmt|;
name|removeInterceptor
argument_list|(
name|server
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getOutInterceptors
argument_list|()
argument_list|,
name|HolderOutInterceptor
operator|.
name|class
argument_list|)
expr_stmt|;
name|removeInterceptor
argument_list|(
name|server
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getBinding
argument_list|()
operator|.
name|getInInterceptors
argument_list|()
argument_list|,
name|SoapHeaderInterceptor
operator|.
name|class
argument_list|)
expr_stmt|;
name|resetPartTypes
argument_list|(
name|server
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getBinding
argument_list|()
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
else|else
block|{
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
comment|//last part can be streamed, others need DOM parsing
if|if
condition|(
name|x
operator|<
operator|(
name|size
operator|-
literal|1
operator|)
condition|)
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
name|allowStreaming
condition|?
name|DOMSource
operator|.
name|class
else|:
literal|null
argument_list|)
expr_stmt|;
block|}
else|else
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
name|allowStreaming
condition|?
name|Source
operator|.
name|class
else|:
literal|null
argument_list|)
expr_stmt|;
block|}
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
comment|//last part can be streamed, others need DOM parsing
if|if
condition|(
name|x
operator|<
operator|(
name|size
operator|-
literal|1
operator|)
condition|)
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
name|allowStreaming
condition|?
name|DOMSource
operator|.
name|class
else|:
literal|null
argument_list|)
expr_stmt|;
block|}
else|else
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
name|allowStreaming
condition|?
name|Source
operator|.
name|class
else|:
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
DECL|method|removeFaultInInterceptorFromClient (Client client)
specifier|private
name|void
name|removeFaultInInterceptorFromClient
parameter_list|(
name|Client
name|client
parameter_list|)
block|{
name|removeInterceptors
argument_list|(
name|client
operator|.
name|getInFaultInterceptors
argument_list|()
argument_list|,
name|REMOVING_FAULT_IN_INTERCEPTORS
argument_list|)
expr_stmt|;
name|removeInterceptors
argument_list|(
name|client
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getService
argument_list|()
operator|.
name|getInFaultInterceptors
argument_list|()
argument_list|,
name|REMOVING_FAULT_IN_INTERCEPTORS
argument_list|)
expr_stmt|;
name|removeInterceptors
argument_list|(
name|client
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getInFaultInterceptors
argument_list|()
argument_list|,
name|REMOVING_FAULT_IN_INTERCEPTORS
argument_list|)
expr_stmt|;
name|removeInterceptors
argument_list|(
name|client
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getBinding
argument_list|()
operator|.
name|getInFaultInterceptors
argument_list|()
argument_list|,
name|REMOVING_FAULT_IN_INTERCEPTORS
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

