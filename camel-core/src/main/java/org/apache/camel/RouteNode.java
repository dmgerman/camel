begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel
package|package
name|org
operator|.
name|apache
operator|.
name|camel
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
comment|/**  * Represents a model of a node in the runtime route path.  *  * @version $Revision$  */
end_comment

begin_interface
DECL|interface|RouteNode
specifier|public
interface|interface
name|RouteNode
block|{
comment|/**      * Gets the actual processor this node represents.      *      * @return the processor, can be<tt>null</tt> in special cases such as an intercepted node      */
DECL|method|getProcessor ()
name|Processor
name|getProcessor
parameter_list|()
function_decl|;
comment|/**      * Gets the model definition that represents this node      *      * @return the definition, is newer<tt>null</tt>      */
DECL|method|getProcessorDefinition ()
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
name|getProcessorDefinition
parameter_list|()
function_decl|;
comment|/**      * Gets a label about this node to be used for tracing or tooling etc.      *      * @param exchange the current exchange      * @return  a label for this node      */
DECL|method|getLabel (Exchange exchange)
name|String
name|getLabel
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
function_decl|;
comment|/**      * Whether this node is abstract (no real processor under the cover).      *<p/>      * Some nodes is abstract that represents intermediate steps for instance with      * onException, onCompletion or intercept      *      * @return whether this node is abstract or not      */
DECL|method|isAbstract ()
name|boolean
name|isAbstract
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

