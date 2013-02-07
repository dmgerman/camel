begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.servletlistener
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|servletlistener
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
name|spi
operator|.
name|Registry
import|;
end_import

begin_comment
comment|/**  * A callback lifecycle allows end users to implement custom logic before  * the {@link ServletCamelContext} is started and stopped.  */
end_comment

begin_interface
DECL|interface|CamelContextLifecycle
specifier|public
interface|interface
name|CamelContextLifecycle
parameter_list|<
name|R
extends|extends
name|Registry
parameter_list|>
block|{
comment|/**      * Callback before starting {@link ServletCamelContext}.      *      * @param camelContext the Camel context      * @param registry     the registry      * @throws Exception is thrown if any error.      */
DECL|method|beforeStart (ServletCamelContext camelContext, R registry)
name|void
name|beforeStart
parameter_list|(
name|ServletCamelContext
name|camelContext
parameter_list|,
name|R
name|registry
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Callback after {@link ServletCamelContext} has been started.      *      * @param camelContext the Camel context      * @param registry     the registry      * @throws Exception is thrown if any error.      */
DECL|method|afterStart (ServletCamelContext camelContext, R registry)
name|void
name|afterStart
parameter_list|(
name|ServletCamelContext
name|camelContext
parameter_list|,
name|R
name|registry
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Callback before stopping {@link ServletCamelContext}.      *      * @param camelContext the Camel context      * @param registry     the registry      * @throws Exception is thrown if any error.      */
DECL|method|beforeStop (ServletCamelContext camelContext, R registry)
name|void
name|beforeStop
parameter_list|(
name|ServletCamelContext
name|camelContext
parameter_list|,
name|R
name|registry
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Callback after {@link ServletCamelContext} has been stopped.      *      * @param camelContext the Camel context      * @param registry     the registry      * @throws Exception is thrown if any error.      */
DECL|method|afterStop (ServletCamelContext camelContext, R registry)
name|void
name|afterStop
parameter_list|(
name|ServletCamelContext
name|camelContext
parameter_list|,
name|R
name|registry
parameter_list|)
throws|throws
name|Exception
function_decl|;
block|}
end_interface

end_unit

