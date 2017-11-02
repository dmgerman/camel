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

begin_comment
comment|/**  * Extended {@link StartupListener} that is invoked when the {@link CamelContext} is fully started.  *<p/>  *<b>Important:</b> You can use this listener to add and start new routes to the {@link CamelContext} which is now supported.  *  * @see StartupListener  */
end_comment

begin_interface
DECL|interface|ExtendedStartupListener
specifier|public
interface|interface
name|ExtendedStartupListener
extends|extends
name|StartupListener
block|{
comment|/**      * Callback invoked when the {@link CamelContext} has been fully started.      *      * @param context        the Camel context      * @param alreadyStarted whether or not the {@link CamelContext} already has been started. For example the context      *                       could already have been started, and then a service is added/started later which still      *                       triggers this callback to be invoked.      * @throws Exception     can be thrown in case of errors to fail the startup process and have the application      *                       fail on startup.      */
DECL|method|onCamelContextFullyStarted (CamelContext context, boolean alreadyStarted)
name|void
name|onCamelContextFullyStarted
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|boolean
name|alreadyStarted
parameter_list|)
throws|throws
name|Exception
function_decl|;
block|}
end_interface

end_unit

