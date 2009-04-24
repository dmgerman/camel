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
comment|/**  * The purpose of this interface is to allow an implementation to  * provide custom logic to wrap a processor with error handler  *  * @version $Revision$  * @deprecated is replaced by {@link org.apache.camel.Channel}  */
end_comment

begin_interface
DECL|interface|ErrorHandlerWrappingStrategy
specifier|public
interface|interface
name|ErrorHandlerWrappingStrategy
block|{
comment|/**      * This method is invoked by      * {@link ProcessorDefinition#wrapProcessor(RouteContext, Processor)}      * to give the implementor an opportunity to wrap the target processor      * in a route.      *      * @param processorType the object that invokes this method      * @param target the processor to be wrapped      * @return processor wrapped with an interceptor or not wrapped      * @throws Exception can be thrown      * @deprecated is replaced by {@link org.apache.camel.Channel}      */
DECL|method|wrapProcessorInErrorHandler (ProcessorDefinition processorType, Processor target)
name|Processor
name|wrapProcessorInErrorHandler
parameter_list|(
name|ProcessorDefinition
name|processorType
parameter_list|,
name|Processor
name|target
parameter_list|)
throws|throws
name|Exception
function_decl|;
block|}
end_interface

end_unit

