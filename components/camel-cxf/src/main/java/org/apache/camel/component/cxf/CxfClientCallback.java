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
name|util
operator|.
name|Map
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
name|AsyncCallback
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
name|cxf
operator|.
name|endpoint
operator|.
name|ClientCallback
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

begin_class
DECL|class|CxfClientCallback
specifier|public
class|class
name|CxfClientCallback
extends|extends
name|ClientCallback
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
name|CxfClientCallback
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|camelAsyncCallback
specifier|private
specifier|final
name|AsyncCallback
name|camelAsyncCallback
decl_stmt|;
DECL|field|camelExchange
specifier|private
specifier|final
name|Exchange
name|camelExchange
decl_stmt|;
DECL|field|cxfExchange
specifier|private
specifier|final
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|Exchange
name|cxfExchange
decl_stmt|;
DECL|field|boi
specifier|private
specifier|final
name|BindingOperationInfo
name|boi
decl_stmt|;
DECL|field|binding
specifier|private
specifier|final
name|CxfBinding
name|binding
decl_stmt|;
DECL|method|CxfClientCallback (AsyncCallback callback, Exchange camelExchange, org.apache.cxf.message.Exchange cxfExchange, BindingOperationInfo boi, CxfBinding binding)
specifier|public
name|CxfClientCallback
parameter_list|(
name|AsyncCallback
name|callback
parameter_list|,
name|Exchange
name|camelExchange
parameter_list|,
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|Exchange
name|cxfExchange
parameter_list|,
name|BindingOperationInfo
name|boi
parameter_list|,
name|CxfBinding
name|binding
parameter_list|)
block|{
name|this
operator|.
name|camelAsyncCallback
operator|=
name|callback
expr_stmt|;
name|this
operator|.
name|camelExchange
operator|=
name|camelExchange
expr_stmt|;
name|this
operator|.
name|cxfExchange
operator|=
name|cxfExchange
expr_stmt|;
name|this
operator|.
name|boi
operator|=
name|boi
expr_stmt|;
name|this
operator|.
name|binding
operator|=
name|binding
expr_stmt|;
block|}
DECL|method|handleResponse (Map<String, Object> ctx, Object[] res)
specifier|public
name|void
name|handleResponse
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|ctx
parameter_list|,
name|Object
index|[]
name|res
parameter_list|)
block|{
try|try
block|{
name|super
operator|.
name|handleResponse
argument_list|(
name|ctx
argument_list|,
name|res
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
comment|// bind the CXF response to Camel exchange and
comment|// call camel callback
comment|// for one way messages callback is already called in
comment|// process method of org.apache.camel.component.cxf.CxfProducer
if|if
condition|(
operator|!
name|boi
operator|.
name|getOperationInfo
argument_list|()
operator|.
name|isOneWay
argument_list|()
condition|)
block|{
comment|// copy the InMessage header to OutMessage header
name|camelExchange
operator|.
name|getOut
argument_list|()
operator|.
name|getHeaders
argument_list|()
operator|.
name|putAll
argument_list|(
name|camelExchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeaders
argument_list|()
argument_list|)
expr_stmt|;
name|binding
operator|.
name|populateExchangeFromCxfResponse
argument_list|(
name|camelExchange
argument_list|,
name|cxfExchange
argument_list|,
name|ctx
argument_list|)
expr_stmt|;
name|camelAsyncCallback
operator|.
name|done
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"{} calling handleResponse"
argument_list|,
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|handleException (Map<String, Object> ctx, Throwable ex)
specifier|public
name|void
name|handleException
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|ctx
parameter_list|,
name|Throwable
name|ex
parameter_list|)
block|{
try|try
block|{
name|super
operator|.
name|handleException
argument_list|(
name|ctx
argument_list|,
name|ex
argument_list|)
expr_stmt|;
comment|// need to call the conduitSelector complete method to enable the fail over feature
name|ConduitSelector
name|conduitSelector
init|=
name|cxfExchange
operator|.
name|get
argument_list|(
name|ConduitSelector
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|conduitSelector
operator|!=
literal|null
condition|)
block|{
name|conduitSelector
operator|.
name|complete
argument_list|(
name|cxfExchange
argument_list|)
expr_stmt|;
name|ex
operator|=
name|cxfExchange
operator|.
name|getOutMessage
argument_list|()
operator|.
name|getContent
argument_list|(
name|Exception
operator|.
name|class
argument_list|)
expr_stmt|;
if|if
condition|(
name|ex
operator|==
literal|null
operator|&&
name|cxfExchange
operator|.
name|getInMessage
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|ex
operator|=
name|cxfExchange
operator|.
name|getInMessage
argument_list|()
operator|.
name|getContent
argument_list|(
name|Exception
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|ex
operator|!=
literal|null
condition|)
block|{
name|camelExchange
operator|.
name|setException
argument_list|(
name|ex
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|camelExchange
operator|.
name|setException
argument_list|(
name|ex
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
comment|// copy the context information and
comment|// call camel callback
comment|// for one way messages callback is already called in
comment|// process method of org.apache.camel.component.cxf.CxfProducer
if|if
condition|(
operator|!
name|boi
operator|.
name|getOperationInfo
argument_list|()
operator|.
name|isOneWay
argument_list|()
condition|)
block|{
comment|// copy the InMessage header to OutMessage header
name|camelExchange
operator|.
name|getOut
argument_list|()
operator|.
name|getHeaders
argument_list|()
operator|.
name|putAll
argument_list|(
name|camelExchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeaders
argument_list|()
argument_list|)
expr_stmt|;
name|binding
operator|.
name|populateExchangeFromCxfResponse
argument_list|(
name|camelExchange
argument_list|,
name|cxfExchange
argument_list|,
name|ctx
argument_list|)
expr_stmt|;
name|camelAsyncCallback
operator|.
name|done
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"{} calling handleException"
argument_list|,
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

