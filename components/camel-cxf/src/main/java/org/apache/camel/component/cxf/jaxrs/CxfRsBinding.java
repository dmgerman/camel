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
name|cxf
operator|.
name|message
operator|.
name|Exchange
import|;
end_import

begin_interface
DECL|interface|CxfRsBinding
specifier|public
interface|interface
name|CxfRsBinding
block|{
comment|/**      * Populate the camel exchange from the CxfRsRequest, the exchange will be consumed       * by the processor which the CxfRsConsumer attached.      *       * @param camelExchange  camel exchange object      * @param cxfExchange  cxf exchange object       * @param method  the method which is need for the camel component      * @param paramArray  the parameter list for the method invocation       */
DECL|method|populateExchangeFromCxfRsRequest (Exchange cxfExchange, org.apache.camel.Exchange camelExchange, Method method, Object[] paramArray)
name|void
name|populateExchangeFromCxfRsRequest
parameter_list|(
name|Exchange
name|cxfExchange
parameter_list|,
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Exchange
name|camelExchange
parameter_list|,
name|Method
name|method
parameter_list|,
name|Object
index|[]
name|paramArray
parameter_list|)
function_decl|;
comment|/**      * Populate the CxfRsResponse object from the camel exchange      * @param camelExchange  camel exchange object      * @param cxfExchange  cxf exchange object       * @return the response object      * @throws Exception       */
DECL|method|populateCxfRsResponseFromExchange (org.apache.camel.Exchange camelExchange, Exchange cxfExchange)
name|Object
name|populateCxfRsResponseFromExchange
parameter_list|(
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Exchange
name|camelExchange
parameter_list|,
name|Exchange
name|cxfExchange
parameter_list|)
throws|throws
name|Exception
function_decl|;
block|}
end_interface

end_unit

