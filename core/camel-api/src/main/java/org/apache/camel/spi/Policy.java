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
comment|/**  * A strategy capable of applying interceptors to a processor.  *<p/>  * Its<b>strongly</b> advised to use an {@link org.apache.camel.AsyncProcessor} as the returned wrapped  * {@link Processor} which ensures the policy works well with the asynchronous routing engine.  * You can use the {@link org.apache.camel.processor.DelegateAsyncProcessor} to easily return an  * {@link org.apache.camel.AsyncProcessor} and override the  * {@link org.apache.camel.AsyncProcessor#process(org.apache.camel.Exchange, org.apache.camel.AsyncCallback)} to  * implement your interceptor logic. And just invoke the super method to<b>continue</b> routing.  *<p/>  * Mind that not all frameworks supports asynchronous routing, for example some transaction managers, such as  * Spring Transaction uses the current thread to store state of the transaction, and thus can't transfer this  * state to other threads when routing continues asynchronously.  */
end_comment

begin_interface
DECL|interface|Policy
specifier|public
interface|interface
name|Policy
block|{
comment|/**      * Hook invoked before the wrap.      *<p/>      * This allows you to do any custom logic before the processor is wrapped. For example to      * manipulate the {@link org.apache.camel.model.ProcessorDefinition definiton}.      *      * @param routeContext   the route context      * @param definition     the processor definition      */
DECL|method|beforeWrap (RouteContext routeContext, NamedNode definition)
name|void
name|beforeWrap
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|,
name|NamedNode
name|definition
parameter_list|)
function_decl|;
comment|/**      * Wraps any applicable interceptors around the given processor.      *      * @param routeContext the route context      * @param processor the processor to be intercepted      * @return either the original processor or a processor wrapped in one or more processors      */
DECL|method|wrap (RouteContext routeContext, Processor processor)
name|Processor
name|wrap
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|,
name|Processor
name|processor
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

