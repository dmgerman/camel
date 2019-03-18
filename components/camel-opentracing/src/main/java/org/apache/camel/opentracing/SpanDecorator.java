begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

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
name|io
operator|.
name|opentracing
operator|.
name|propagation
operator|.
name|TextMap
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
comment|/**      * This method indicates whether the component associated with the SpanDecorator      * should result in a new span being created.      *      * @return Whether a new span should be created      */
DECL|method|newSpan ()
name|boolean
name|newSpan
parameter_list|()
function_decl|;
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
comment|/**      * This method returns the 'span.kind' value for use when the component      * is initiating a communication.      *      * @return The kind      */
DECL|method|getInitiatorSpanKind ()
name|String
name|getInitiatorSpanKind
parameter_list|()
function_decl|;
comment|/**      * This method returns the 'span.kind' value for use when the component      * is receiving a communication.      *      * @return The kind      */
DECL|method|getReceiverSpanKind ()
name|String
name|getReceiverSpanKind
parameter_list|()
function_decl|;
comment|/**      * This method returns the map to be used for headers extraction      * when the component is receiving a communication.      *      * @param map a map containing the objects      * @param encoding whether the headers are encoded      * @return The extraction map      */
DECL|method|getExtractAdapter (Map<String, Object> map, boolean encoding)
name|TextMap
name|getExtractAdapter
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|map
parameter_list|,
name|boolean
name|encoding
parameter_list|)
function_decl|;
comment|/**      * This method returns the map to be used for headers injection      *  when the component is receiving a communication.      *      * @param map a map containing the objects      * @param encoding whether the headers are encoded      * @return The injection map      */
DECL|method|getInjectAdapter (Map<String, Object> map, boolean encoding)
name|TextMap
name|getInjectAdapter
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|map
parameter_list|,
name|boolean
name|encoding
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

