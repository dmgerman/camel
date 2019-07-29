begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spi
package|;
end_package

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
name|NamedNode
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
name|NamedRoute
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
name|Route
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
name|StaticService
import|;
end_import

begin_comment
comment|/**  * SPI for tracing messages.  */
end_comment

begin_interface
DECL|interface|Tracer
specifier|public
interface|interface
name|Tracer
extends|extends
name|StaticService
block|{
comment|/**      * Whether or not to trace the given processor definition.      *      * @param definition the processor definition      * @return<tt>true</tt> to trace,<tt>false</tt> to skip tracing      */
DECL|method|shouldTrace (NamedNode definition)
name|boolean
name|shouldTrace
parameter_list|(
name|NamedNode
name|definition
parameter_list|)
function_decl|;
comment|/**      * Trace before the route (eg input to route)      *      * @param route     the route      * @param exchange  the exchange      */
DECL|method|traceBeforeRoute (NamedRoute route, Exchange exchange)
name|void
name|traceBeforeRoute
parameter_list|(
name|NamedRoute
name|route
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
function_decl|;
comment|/**      * Trace before the given node      *      * @param node      the node EIP      * @param exchange  the exchange      */
DECL|method|traceBeforeNode (NamedNode node, Exchange exchange)
name|void
name|traceBeforeNode
parameter_list|(
name|NamedNode
name|node
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
function_decl|;
comment|/**      * Trace after the given node      *      * @param node      the node EIP      * @param exchange  the exchange      */
DECL|method|traceAfterNode (NamedNode node, Exchange exchange)
name|void
name|traceAfterNode
parameter_list|(
name|NamedNode
name|node
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
function_decl|;
comment|/**      * Trace after the route (eg output from route)      *      * @param route     the route      * @param exchange  the exchange      */
DECL|method|traceAfterRoute (Route route, Exchange exchange)
name|void
name|traceAfterRoute
parameter_list|(
name|Route
name|route
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
function_decl|;
comment|/**      * Number of traced messages      */
DECL|method|getTraceCounter ()
name|long
name|getTraceCounter
parameter_list|()
function_decl|;
comment|/**      * Reset trace counter      */
DECL|method|resetTraceCounter ()
name|void
name|resetTraceCounter
parameter_list|()
function_decl|;
comment|/**      * Whether the tracer is enabled      */
DECL|method|isEnabled ()
name|boolean
name|isEnabled
parameter_list|()
function_decl|;
comment|/**      * Whether the tracer is enabled      */
DECL|method|setEnabled (boolean enabled)
name|void
name|setEnabled
parameter_list|(
name|boolean
name|enabled
parameter_list|)
function_decl|;
comment|/**      * Tracing pattern to match which node EIPs to trace.      * For example to match all To EIP nodes, use to*.      * The pattern matches by node and route id's      * Multiple patterns can be separated by comma.      */
DECL|method|getTracePattern ()
name|String
name|getTracePattern
parameter_list|()
function_decl|;
comment|/**      * Tracing pattern to match which node EIPs to trace.      * For example to match all To EIP nodes, use to*.      * The pattern matches by node and route id's      * Multiple patterns can be separated by comma.      */
DECL|method|setTracePattern (String tracePattern)
name|void
name|setTracePattern
parameter_list|(
name|String
name|tracePattern
parameter_list|)
function_decl|;
comment|/**      * Whether to include tracing of before/after routes to trace the input and responses of routes.      */
DECL|method|isTraceBeforeAndAfterRoute ()
name|boolean
name|isTraceBeforeAndAfterRoute
parameter_list|()
function_decl|;
comment|/**      * Whether to include tracing of before/after routes to trace the input and responses of routes.      */
DECL|method|setTraceBeforeAndAfterRoute (boolean traceBeforeAndAfterRoute)
name|void
name|setTraceBeforeAndAfterRoute
parameter_list|(
name|boolean
name|traceBeforeAndAfterRoute
parameter_list|)
function_decl|;
comment|/**      * To use a custom exchange formatter for formatting the output of the {@link Exchange} in the trace logs.      */
DECL|method|getExchangeFormatter ()
name|ExchangeFormatter
name|getExchangeFormatter
parameter_list|()
function_decl|;
comment|/**      * To use a custom exchange formatter for formatting the output of the {@link Exchange} in the trace logs.      */
DECL|method|setExchangeFormatter (ExchangeFormatter exchangeFormatter)
name|void
name|setExchangeFormatter
parameter_list|(
name|ExchangeFormatter
name|exchangeFormatter
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

