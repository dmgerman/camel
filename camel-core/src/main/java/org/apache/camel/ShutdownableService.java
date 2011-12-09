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
comment|/**  * A {@link org.apache.camel.Service} which is capable of being shut down.  *<p/>  * This is useful for services which need more fine grained control when the {@link CamelContext} is shutting down.  * This allows customization of behavior when stopping or shutting down.  *<p/>  * For example to shutdown thread pools during shutdown and<b>not</b> at CamelContext termination.  *  * @version   */
end_comment

begin_interface
DECL|interface|ShutdownableService
specifier|public
interface|interface
name|ShutdownableService
extends|extends
name|Service
block|{
comment|/**      * Shutdown the service, which means it cannot be started again.      *      * @throws Exception thrown if shutting down failed      */
DECL|method|shutdown ()
name|void
name|shutdown
parameter_list|()
throws|throws
name|Exception
function_decl|;
block|}
end_interface

end_unit

