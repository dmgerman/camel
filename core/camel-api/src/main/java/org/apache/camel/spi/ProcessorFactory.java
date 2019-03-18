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
comment|/**  * A factory to create {@link Processor} based on the {@link org.apache.camel.model.ProcessorDefinition definition}.  *<p/>  * This allows you to implement a custom factory in which you can control the creation of the processors.  * It also allows you to manipulate the {@link org.apache.camel.model.ProcessorDefinition definition}s for example to  * configure or change options. Its also possible to add new steps in the route by adding outputs to  * {@link org.apache.camel.model.ProcessorDefinition definition}s.  *<p/>  *<b>Important:</b> By returning<tt>null</tt> from the create methods you fallback to let the default implementation in Camel create  * the {@link Processor}. You want to do this if you<i>only</i> want to manipulate the  * {@link org.apache.camel.model.ProcessorDefinition definition}s.  */
end_comment

begin_interface
DECL|interface|ProcessorFactory
specifier|public
interface|interface
name|ProcessorFactory
block|{
comment|/**      * Creates the child processor.      *<p/>      * The child processor is an output from the given definition, for example the sub route in a splitter EIP.      *      * @param routeContext  the route context      * @param definition    the definition which represents the processor      * @param mandatory     whether or not the child is mandatory      * @return the created processor, or<tt>null</tt> to let the default implementation in Camel create the processor.      * @throws Exception can be thrown if error creating the processor      */
DECL|method|createChildProcessor (RouteContext routeContext, NamedNode definition, boolean mandatory)
name|Processor
name|createChildProcessor
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|,
name|NamedNode
name|definition
parameter_list|,
name|boolean
name|mandatory
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Creates the processor.      *      * @param routeContext  the route context      * @param definition    the definition which represents the processor      * @return the created processor, or<tt>null</tt> to let the default implementation in Camel create the processor.      * @throws Exception can be thrown if error creating the processor      */
DECL|method|createProcessor (RouteContext routeContext, NamedNode definition)
name|Processor
name|createProcessor
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|,
name|NamedNode
name|definition
parameter_list|)
throws|throws
name|Exception
function_decl|;
block|}
end_interface

end_unit

