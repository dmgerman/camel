begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jms
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jms
package|;
end_package

begin_comment
comment|/**  * JMS constants  */
end_comment

begin_class
DECL|class|JmsConstants
specifier|public
specifier|final
class|class
name|JmsConstants
block|{
DECL|field|JMS_DESTINATION
specifier|public
specifier|static
specifier|final
name|String
name|JMS_DESTINATION
init|=
literal|"CamelJmsDestination"
decl_stmt|;
DECL|field|JMS_DESTINATION_NAME
specifier|public
specifier|static
specifier|final
name|String
name|JMS_DESTINATION_NAME
init|=
literal|"CamelJmsDestinationName"
decl_stmt|;
DECL|field|JMS_MESSAGE_TYPE
specifier|public
specifier|static
specifier|final
name|String
name|JMS_MESSAGE_TYPE
init|=
literal|"CamelJmsMessageType"
decl_stmt|;
DECL|field|JMS_DELIVERY_MODE
specifier|public
specifier|static
specifier|final
name|String
name|JMS_DELIVERY_MODE
init|=
literal|"CamelJmsDeliveryMode"
decl_stmt|;
DECL|field|JMS_REPLY_TO_NAME
specifier|public
specifier|static
specifier|final
name|String
name|JMS_REPLY_TO_NAME
init|=
literal|"CamelJmsReplyToName"
decl_stmt|;
DECL|method|JmsConstants ()
specifier|private
name|JmsConstants
parameter_list|()
block|{
comment|// utility class
block|}
block|}
end_class

end_unit

