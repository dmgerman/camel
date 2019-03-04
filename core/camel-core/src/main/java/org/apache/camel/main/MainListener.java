begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.main
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|main
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

begin_comment
comment|/**  * A lifecycle listener to receive callbacks when the Main is started and stopped.  */
end_comment

begin_interface
DECL|interface|MainListener
specifier|public
interface|interface
name|MainListener
block|{
comment|/**      * Callback to configure the created CamelContext.      *      * @param context the created CamelContext      */
DECL|method|configure (CamelContext context)
name|void
name|configure
parameter_list|(
name|CamelContext
name|context
parameter_list|)
function_decl|;
comment|/**      * Callback before the CamelContext is being created and started.      *      * @param main  the main instance      */
DECL|method|beforeStart (MainSupport main)
name|void
name|beforeStart
parameter_list|(
name|MainSupport
name|main
parameter_list|)
function_decl|;
comment|/**      * Callback after the CamelContext has been started.      *      * @param main  the main instance      */
DECL|method|afterStart (MainSupport main)
name|void
name|afterStart
parameter_list|(
name|MainSupport
name|main
parameter_list|)
function_decl|;
comment|/**      * Callback before the CamelContext is being stopped.      *      * @param main  the main instance      */
DECL|method|beforeStop (MainSupport main)
name|void
name|beforeStop
parameter_list|(
name|MainSupport
name|main
parameter_list|)
function_decl|;
comment|/**      * Callback after the CamelContext has been stopped.      *      * @param main  the main instance      */
DECL|method|afterStop (MainSupport main)
name|void
name|afterStop
parameter_list|(
name|MainSupport
name|main
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

