begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.jmxconnect
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|jmxconnect
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|MBeanServerConnection
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|ObjectName
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
name|Endpoint
import|;
end_import

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_interface
DECL|interface|MBeanCamelServerConnection
specifier|public
interface|interface
name|MBeanCamelServerConnection
extends|extends
name|MBeanServerConnection
block|{
comment|/**      * Add a Notification listener      *      * @param listenerId      * @param name      * @param replyToEndpoint      */
DECL|method|addNotificationListener (String listenerId, ObjectName name, Endpoint replyToEndpoint)
name|void
name|addNotificationListener
parameter_list|(
name|String
name|listenerId
parameter_list|,
name|ObjectName
name|name
parameter_list|,
name|Endpoint
name|replyToEndpoint
parameter_list|)
function_decl|;
comment|/**      * Remove a Notification listener      *      * @param listenerId      */
DECL|method|removeNotificationListener (String listenerId)
name|void
name|removeNotificationListener
parameter_list|(
name|String
name|listenerId
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

