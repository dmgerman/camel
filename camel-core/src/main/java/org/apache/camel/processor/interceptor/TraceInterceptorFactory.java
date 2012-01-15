begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor.interceptor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
operator|.
name|interceptor
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
name|Processor
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
name|model
operator|.
name|ProcessorDefinition
import|;
end_import

begin_comment
comment|/**  * Factory to create the trace interceptor.  */
end_comment

begin_interface
DECL|interface|TraceInterceptorFactory
specifier|public
interface|interface
name|TraceInterceptorFactory
block|{
comment|/**      * Create a trace interceptor.      *<p/>      * It is expected that the factory will create a subclass of {@link TraceInterceptor},      * however any Processor will suffice.      *<p/>      * Use this factory to take more control of how trace events are persisted.      *      * @param node      the current node      * @param target    the current target      * @param formatter the trace formatter      * @param tracer    the tracer      * @return the created trace interceptor      */
DECL|method|createTraceInterceptor (ProcessorDefinition<?> node, Processor target, TraceFormatter formatter, Tracer tracer)
name|Processor
name|createTraceInterceptor
parameter_list|(
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
name|node
parameter_list|,
name|Processor
name|target
parameter_list|,
name|TraceFormatter
name|formatter
parameter_list|,
name|Tracer
name|tracer
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

