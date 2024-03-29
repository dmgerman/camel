begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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

begin_comment
comment|/**  * To be implemented by the user class to get a {@link NotificationSender} injected  */
end_comment

begin_interface
DECL|interface|NotificationSenderAware
specifier|public
interface|interface
name|NotificationSenderAware
block|{
comment|/**      * {@link NotificationSender} to use for sending notifications.      *      * @param sender sender to use for sending notifications      */
DECL|method|setNotificationSender (NotificationSender sender)
name|void
name|setNotificationSender
parameter_list|(
name|NotificationSender
name|sender
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

