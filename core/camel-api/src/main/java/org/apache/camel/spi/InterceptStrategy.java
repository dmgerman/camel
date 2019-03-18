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
name|CamelContext
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
name|Processor
import|;
end_import

begin_comment
comment|/**  * The purpose of this interface is to allow an implementation to wrap  * processors in a route with interceptors.  For example, a possible  * usecase is to gather performance statistics at the processor's level.  *<p/>  *<b>Important:</b> This SPI is not intended to be used by Camel end users - do NOT use this.  *<p/>  * Its<b>strongly</b> adviced to use an {@link org.apache.camel.AsyncProcessor} as the returned wrapped  * {@link Processor} which ensures the interceptor works well with the asynchronous routing engine.  * You can use the {@link org.apache.camel.processor.DelegateAsyncProcessor} to easily return an  * {@link org.apache.camel.AsyncProcessor} and override the  * {@link org.apache.camel.AsyncProcessor#process(org.apache.camel.Exchange, org.apache.camel.AsyncCallback)} to  * implement your interceptor logic. And just invoke the super method to<b>continue</b> routing.  */
end_comment

begin_interface
DECL|interface|InterceptStrategy
specifier|public
interface|interface
name|InterceptStrategy
block|{
comment|// TODO: Camel 3.0 make this an internal API
comment|/**      * This method is invoked by      * {@link org.apache.camel.model.ProcessorDefinition#wrapProcessor(RouteContext, Processor)}      * to give the implementor an opportunity to wrap the target processor      * in a route.      *<p/>      *<b>Important:</b> See the class javadoc for advice on letting interceptor be compatible with the      * asynchronous routing engine.      *      * @param context       Camel context      * @param definition    the model this interceptor represents      * @param target        the processor to be wrapped      * @param nextTarget    the next processor to be routed to      * @return processor    wrapped with an interceptor or not wrapped.      * @throws Exception can be thrown      */
DECL|method|wrapProcessorInInterceptors (CamelContext context, NamedNode definition, Processor target, Processor nextTarget)
name|Processor
name|wrapProcessorInInterceptors
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|NamedNode
name|definition
parameter_list|,
name|Processor
name|target
parameter_list|,
name|Processor
name|nextTarget
parameter_list|)
throws|throws
name|Exception
function_decl|;
block|}
end_interface

end_unit

