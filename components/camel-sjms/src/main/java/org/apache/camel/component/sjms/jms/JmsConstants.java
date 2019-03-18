begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.sjms.jms
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|sjms
operator|.
name|jms
package|;
end_package

begin_comment
comment|/**  * JMS constants  */
end_comment

begin_interface
DECL|interface|JmsConstants
specifier|public
interface|interface
name|JmsConstants
block|{
DECL|field|QUEUE_PREFIX
name|String
name|QUEUE_PREFIX
init|=
literal|"queue:"
decl_stmt|;
DECL|field|TOPIC_PREFIX
name|String
name|TOPIC_PREFIX
init|=
literal|"topic:"
decl_stmt|;
DECL|field|TEMP_QUEUE_PREFIX
name|String
name|TEMP_QUEUE_PREFIX
init|=
literal|"temp:queue:"
decl_stmt|;
DECL|field|TEMP_TOPIC_PREFIX
name|String
name|TEMP_TOPIC_PREFIX
init|=
literal|"temp:topic:"
decl_stmt|;
comment|/**      * Set by the publishing Client      */
DECL|field|JMS_CORRELATION_ID
name|String
name|JMS_CORRELATION_ID
init|=
literal|"JMSCorrelationID"
decl_stmt|;
comment|/**      * Set on the send or publish event      */
DECL|field|JMS_DELIVERY_MODE
name|String
name|JMS_DELIVERY_MODE
init|=
literal|"JMSDeliveryMode"
decl_stmt|;
comment|/**      * Set on the send or publish event      */
DECL|field|JMS_DESTINATION
name|String
name|JMS_DESTINATION
init|=
literal|"JMSDestination"
decl_stmt|;
comment|/**      * Set on the send or publish event      */
DECL|field|JMS_EXPIRATION
name|String
name|JMS_EXPIRATION
init|=
literal|"JMSExpiration"
decl_stmt|;
comment|/**      * Set on the send or publish event      */
DECL|field|JMS_MESSAGE_ID
name|String
name|JMS_MESSAGE_ID
init|=
literal|"JMSMessageID"
decl_stmt|;
comment|/**      * Set on the send or publish event      */
DECL|field|JMS_PRIORITY
name|String
name|JMS_PRIORITY
init|=
literal|"JMSPriority"
decl_stmt|;
comment|/**      * A redelivery flag set by the JMS provider      */
DECL|field|JMS_REDELIVERED
name|String
name|JMS_REDELIVERED
init|=
literal|"JMSRedelivered"
decl_stmt|;
comment|/**      * The JMS Reply To {@link javax.jms.Destination} set by the publishing Client      */
DECL|field|JMS_REPLY_TO
name|String
name|JMS_REPLY_TO
init|=
literal|"JMSReplyTo"
decl_stmt|;
comment|/**      * Set on the send or publish event      */
DECL|field|JMS_TIMESTAMP
name|String
name|JMS_TIMESTAMP
init|=
literal|"JMSTimestamp"
decl_stmt|;
comment|/**      * Set by the publishing Client      */
DECL|field|JMS_TYPE
name|String
name|JMS_TYPE
init|=
literal|"JMSType"
decl_stmt|;
comment|/**      * Custom headers      */
DECL|field|JMSX_GROUP_ID
name|String
name|JMSX_GROUP_ID
init|=
literal|"JMSXGroupID"
decl_stmt|;
comment|/**      * String representation of JMS delivery modes.      */
DECL|field|JMS_DELIVERY_MODE_PERSISTENT
name|String
name|JMS_DELIVERY_MODE_PERSISTENT
init|=
literal|"PERSISTENT"
decl_stmt|;
DECL|field|JMS_DELIVERY_MODE_NON_PERSISTENT
name|String
name|JMS_DELIVERY_MODE_NON_PERSISTENT
init|=
literal|"NON_PERSISTENT"
decl_stmt|;
block|}
end_interface

end_unit

