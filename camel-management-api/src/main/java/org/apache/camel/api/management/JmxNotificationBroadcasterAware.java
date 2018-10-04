begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.api.management
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|api
operator|.
name|management
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|NotificationBroadcasterSupport
import|;
end_import

begin_comment
comment|/**  * Strategy to use a custom {@link NotificationBroadcasterSupport} when broadcasting  * JMX notifications using for example the {@link org.apache.camel.management.JmxNotificationEventNotifier}.  */
end_comment

begin_interface
DECL|interface|JmxNotificationBroadcasterAware
specifier|public
interface|interface
name|JmxNotificationBroadcasterAware
block|{
comment|/**      * Sets to use a custom broadcaster      *      * @param broadcaster the custom broadcaster      */
DECL|method|setNotificationBroadcaster (NotificationBroadcasterSupport broadcaster)
name|void
name|setNotificationBroadcaster
parameter_list|(
name|NotificationBroadcasterSupport
name|broadcaster
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

