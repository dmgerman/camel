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
comment|/**  * Allows objects to be notified when {@link CamelContext} have just been started.  *<p/>  * This can be used to perform any custom work when the entire {@link CamelContext} has been initialized and started.  * For example this ensures that all the Camel routes has been started and are up and running, before this callback  * is being invoked.  *<p/>  * For example the QuartzComponent leverages this to ensure the Quartz scheduler is started late, when all the  * Camel routes and services already have been started.  *  * @version $Revision$  */
end_comment

begin_interface
DECL|interface|StartupListener
specifier|public
interface|interface
name|StartupListener
block|{
comment|/**      * Callback invoked when the {@link org.apache.camel.CamelContext} has just been started.      *      * @param context the camel context      * @throws Exception can be thrown in case of errors to fail the startup process and have the application      * fail on startup.      */
DECL|method|onCamelContextStarted (CamelContext context)
name|void
name|onCamelContextStarted
parameter_list|(
name|CamelContext
name|context
parameter_list|)
throws|throws
name|Exception
function_decl|;
block|}
end_interface

end_unit

