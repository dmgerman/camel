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
name|java
operator|.
name|util
operator|.
name|Map
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
name|CamelContext
import|;
end_import

begin_comment
comment|/**  * Service Factory for ManagementStrategy  */
end_comment

begin_interface
DECL|interface|ManagementStrategyFactory
specifier|public
interface|interface
name|ManagementStrategyFactory
block|{
comment|/**      * Creates the {@link ManagementStrategy}.      *      * @param context     the camel context      * @param properties  optional options to set on {@link ManagementAgent}      * @return the created strategy      * @throws Exception is thrown if error creating the strategy      */
DECL|method|create (CamelContext context, Map<String, Object> properties)
name|ManagementStrategy
name|create
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Creates the associated {@link LifecycleStrategy} that the management strategy uses.      *      * @param context     the camel context      * @return the created lifecycle strategy      * @throws Exception is thrown if error creating the lifecycle strategy      */
DECL|method|createLifecycle (CamelContext context)
name|LifecycleStrategy
name|createLifecycle
parameter_list|(
name|CamelContext
name|context
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Setup the management on the {@link CamelContext}.      *<p/>      * This allows implementations to provide the logic for setting up management on Camel.      *      * @param camelContext  the camel context      * @param strategy      the management strategy      * @param lifecycle      the associated lifecycle strategy (optional)      */
DECL|method|setupManagement (CamelContext camelContext, ManagementStrategy strategy, LifecycleStrategy lifecycle)
name|void
name|setupManagement
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|ManagementStrategy
name|strategy
parameter_list|,
name|LifecycleStrategy
name|lifecycle
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

