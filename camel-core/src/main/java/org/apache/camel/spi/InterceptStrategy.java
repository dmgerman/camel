begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
comment|/**  * The purpose of this interface is to allow an implementation to wrap  * processors in a route with interceptors.  For example, a possible  * usecase is to gather performance statistics at the processor's level.  *  * @version $Revision$  */
end_comment

begin_interface
DECL|interface|InterceptStrategy
specifier|public
interface|interface
name|InterceptStrategy
block|{
comment|/**      * This method is invoked by      * {@link ProcessorDefinition#wrapProcessor(RouteContext, Processor)}      * to give the implementor an opportunity to wrap the target processor      * in a route.      *      * @param context       Camel context      * @param definition    the model this interceptor represents      * @param target        the processor to be wrapped      * @param nextTarget    the next processor to be routed to      * @return processor wrapped with an interceptor or not wrapped      * @throws Exception can be thrown      */
DECL|method|wrapProcessorInInterceptors (CamelContext context, ProcessorDefinition<?> definition, Processor target, Processor nextTarget)
name|Processor
name|wrapProcessorInInterceptors
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
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

