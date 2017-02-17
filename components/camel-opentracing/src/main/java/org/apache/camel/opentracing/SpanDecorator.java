begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.opentracing
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|opentracing
package|;
end_package

begin_import
import|import
name|io
operator|.
name|opentracing
operator|.
name|Span
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
name|Endpoint
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
name|camel
operator|.
name|opentracing
operator|.
name|decorators
operator|.
name|AbstractSpanDecorator
import|;
end_import

begin_comment
comment|/**  * This interface represents a decorator specific to the component/endpoint  * being instrumented.  */
end_comment

begin_interface
DECL|interface|SpanDecorator
specifier|public
interface|interface
name|SpanDecorator
block|{
comment|/* Prefix for camel component tag */
DECL|field|CAMEL_COMPONENT
name|String
name|CAMEL_COMPONENT
init|=
literal|"camel-"
decl_stmt|;
DECL|field|DEFAULT
name|SpanDecorator
name|DEFAULT
init|=
operator|new
name|AbstractSpanDecorator
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|String
name|getComponent
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
block|}
decl_stmt|;
comment|/**      * The camel component associated with the decorator.      *      * @return The camel component name      */
DECL|method|getComponent ()
name|String
name|getComponent
parameter_list|()
function_decl|;
comment|/**      * This method returns the operation name to use with the Span representing      * this exchange and endpoint.      *      * @param exchange The exchange      * @param endpoint The endpoint      * @return The operation name      */
DECL|method|getOperationName (Exchange exchange, Endpoint endpoint)
name|String
name|getOperationName
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Endpoint
name|endpoint
parameter_list|)
function_decl|;
comment|/**      * This method adds appropriate details (tags/logs) to the supplied span      * based on the pre processing of the exchange.      *      * @param span The span      * @param exchange The exchange      * @param endpoint The endpoint      */
DECL|method|pre (Span span, Exchange exchange, Endpoint endpoint)
name|void
name|pre
parameter_list|(
name|Span
name|span
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|Endpoint
name|endpoint
parameter_list|)
function_decl|;
comment|/**      * This method adds appropriate details (tags/logs) to the supplied span      * based on the post processing of the exchange.      *      * @param span The span      * @param exchange The exchange      * @param endpoint The endpoint      */
DECL|method|post (Span span, Exchange exchange, Endpoint endpoint)
name|void
name|post
parameter_list|(
name|Span
name|span
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|Endpoint
name|endpoint
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

