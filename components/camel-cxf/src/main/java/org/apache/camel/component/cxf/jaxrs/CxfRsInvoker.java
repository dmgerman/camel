begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cxf.jaxrs
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
name|jaxrs
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
name|camel
operator|.
name|Processor
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
name|jaxrs
operator|.
name|JAXRSInvoker
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

begin_class
DECL|class|CxfRsInvoker
specifier|public
class|class
name|CxfRsInvoker
extends|extends
name|JAXRSInvoker
block|{
DECL|field|processor
specifier|private
name|Processor
name|processor
decl_stmt|;
DECL|field|endpoint
specifier|private
name|CxfRsEndpoint
name|endpoint
decl_stmt|;
DECL|method|CxfRsInvoker (CxfRsEndpoint endpoint, Processor processor)
specifier|public
name|CxfRsInvoker
parameter_list|(
name|CxfRsEndpoint
name|endpoint
parameter_list|,
name|Processor
name|processor
parameter_list|)
block|{
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
name|this
operator|.
name|processor
operator|=
name|processor
expr_stmt|;
block|}
DECL|method|performInvocation (Exchange cxfExchange, final Object serviceObject, Method method, Object[] paramArray)
specifier|protected
name|Object
name|performInvocation
parameter_list|(
name|Exchange
name|cxfExchange
parameter_list|,
specifier|final
name|Object
name|serviceObject
parameter_list|,
name|Method
name|method
parameter_list|,
name|Object
index|[]
name|paramArray
parameter_list|)
throws|throws
name|Exception
block|{
name|paramArray
operator|=
name|insertExchange
argument_list|(
name|method
argument_list|,
name|paramArray
argument_list|,
name|cxfExchange
argument_list|)
expr_stmt|;
name|ExchangePattern
name|ep
init|=
name|ExchangePattern
operator|.
name|InOut
decl_stmt|;
if|if
condition|(
name|method
operator|.
name|getReturnType
argument_list|()
operator|==
name|Void
operator|.
name|class
condition|)
block|{
name|ep
operator|=
name|ExchangePattern
operator|.
name|InOnly
expr_stmt|;
block|}
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Exchange
name|camelExchange
init|=
name|endpoint
operator|.
name|createExchange
argument_list|(
name|ep
argument_list|)
decl_stmt|;
name|CxfRsBinding
name|binding
init|=
name|endpoint
operator|.
name|getBinding
argument_list|()
decl_stmt|;
name|binding
operator|.
name|populateExchangeFromCxfRsRequest
argument_list|(
name|cxfExchange
argument_list|,
name|camelExchange
argument_list|,
name|method
argument_list|,
name|paramArray
argument_list|)
expr_stmt|;
name|processor
operator|.
name|process
argument_list|(
name|camelExchange
argument_list|)
expr_stmt|;
if|if
condition|(
name|camelExchange
operator|.
name|getException
argument_list|()
operator|!=
literal|null
condition|)
block|{
throw|throw
name|camelExchange
operator|.
name|getException
argument_list|()
throw|;
block|}
return|return
name|binding
operator|.
name|populateCxfRsResponseFromExchange
argument_list|(
name|camelExchange
argument_list|,
name|cxfExchange
argument_list|)
return|;
block|}
block|}
end_class

end_unit

