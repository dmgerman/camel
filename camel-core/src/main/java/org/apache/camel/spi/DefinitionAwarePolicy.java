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
name|model
operator|.
name|ProcessorDefinition
import|;
end_import

begin_comment
comment|/**  * A strategy capable of applying interceptors to a processor.  *<p/>  * This<i>aware</i> policy allows you to do any custom work before the processor is wrapped.  * For example to manipulate the {@link org.apache.camel.model.ProcessorDefinition definiton}.  *  *  * @see org.apache.camel.spi.Policy  * @version $Revision: 761894 $  */
end_comment

begin_interface
DECL|interface|DefinitionAwarePolicy
specifier|public
interface|interface
name|DefinitionAwarePolicy
extends|extends
name|Policy
block|{
comment|/**      * Callback invoked before the wrap.      *<p/>      * This allows you to do any custom logic before the processor is wrapped. For example to      * manipulate the {@link org.apache.camel.model.ProcessorDefinition definiton}      *      * @param routeContext   the route context      * @param definition     the processor definition      */
DECL|method|beforeWrap (RouteContext routeContext, ProcessorDefinition<?> definition)
name|void
name|beforeWrap
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|,
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
name|definition
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

