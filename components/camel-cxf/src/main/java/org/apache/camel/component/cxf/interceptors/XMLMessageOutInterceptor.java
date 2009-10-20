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
name|Collection
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
name|w3c
operator|.
name|dom
operator|.
name|Node
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
name|NodeList
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
name|bindings
operator|.
name|xformat
operator|.
name|XMLBindingMessageFormat
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
name|XMLMessage
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

begin_class
DECL|class|XMLMessageOutInterceptor
specifier|public
class|class
name|XMLMessageOutInterceptor
extends|extends
name|AbstractMessageOutInterceptor
argument_list|<
name|XMLMessage
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
name|XMLMessageOutInterceptor
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|XMLMessageOutInterceptor ()
specifier|public
name|XMLMessageOutInterceptor
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
DECL|method|handleMessage (XMLMessage message)
specifier|public
name|void
name|handleMessage
parameter_list|(
name|XMLMessage
name|message
parameter_list|)
throws|throws
name|Fault
block|{
name|Exchange
name|exchange
init|=
name|message
operator|.
name|getExchange
argument_list|()
decl_stmt|;
comment|//BindingOperationInfo boi = exchange.get(BindingOperationInfo.class);
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
if|if
condition|(
name|bmi
operator|==
literal|null
operator|&&
name|payload
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
literal|"NO_XML_ROOT_NODE"
argument_list|,
name|LOG
argument_list|)
argument_list|)
throw|;
block|}
if|if
condition|(
name|bmi
operator|!=
literal|null
condition|)
block|{
name|XMLBindingMessageFormat
name|msgFormat
init|=
name|bmi
operator|.
name|getExtensor
argument_list|(
name|XMLBindingMessageFormat
operator|.
name|class
argument_list|)
decl_stmt|;
name|QName
name|rootName
init|=
name|msgFormat
operator|!=
literal|null
condition|?
name|msgFormat
operator|.
name|getRootNode
argument_list|()
else|:
literal|null
decl_stmt|;
if|if
condition|(
name|rootName
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|payload
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
literal|"NO_XML_ROOT_NODE"
argument_list|,
name|LOG
argument_list|)
argument_list|)
throw|;
block|}
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
name|INFO
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"DOMOutInterceptor Create xmlformat RootNode element"
argument_list|)
expr_stmt|;
block|}
name|Element
name|el
init|=
name|createElement
argument_list|(
name|rootName
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
name|el
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
name|message
operator|.
name|remove
argument_list|(
name|Element
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

