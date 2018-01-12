begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.mllp
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|mllp
package|;
end_package

begin_comment
comment|/**  * Constants for the MLLP Protocol and the Camel MLLP component.  */
end_comment

begin_class
DECL|class|MllpConstants
specifier|public
specifier|final
class|class
name|MllpConstants
block|{
DECL|field|MLLP_LOCAL_ADDRESS
specifier|public
specifier|static
specifier|final
name|String
name|MLLP_LOCAL_ADDRESS
init|=
literal|"CamelMllpLocalAddress"
decl_stmt|;
DECL|field|MLLP_REMOTE_ADDRESS
specifier|public
specifier|static
specifier|final
name|String
name|MLLP_REMOTE_ADDRESS
init|=
literal|"CamelMllpRemoteAddress"
decl_stmt|;
DECL|field|MLLP_ACKNOWLEDGEMENT
specifier|public
specifier|static
specifier|final
name|String
name|MLLP_ACKNOWLEDGEMENT
init|=
literal|"CamelMllpAcknowledgement"
decl_stmt|;
DECL|field|MLLP_ACKNOWLEDGEMENT_STRING
specifier|public
specifier|static
specifier|final
name|String
name|MLLP_ACKNOWLEDGEMENT_STRING
init|=
literal|"CamelMllpAcknowledgementString"
decl_stmt|;
DECL|field|MLLP_ACKNOWLEDGEMENT_TYPE
specifier|public
specifier|static
specifier|final
name|String
name|MLLP_ACKNOWLEDGEMENT_TYPE
init|=
literal|"CamelMllpAcknowledgementType"
decl_stmt|;
DECL|field|MLLP_ACKNOWLEDGEMENT_EXCEPTION
specifier|public
specifier|static
specifier|final
name|String
name|MLLP_ACKNOWLEDGEMENT_EXCEPTION
init|=
literal|"CamelMllpAcknowledgementException"
decl_stmt|;
DECL|field|MLLP_AUTO_ACKNOWLEDGE
specifier|public
specifier|static
specifier|final
name|String
name|MLLP_AUTO_ACKNOWLEDGE
init|=
literal|"CamelMllpAutoAcknowledge"
decl_stmt|;
comment|/*      Connection Control Exchange Properties       - For Consumers, "SEND" => ACKNOWLEDGEMENT       - For Producers, "SEND" => MESSAGE       */
DECL|field|MLLP_CLOSE_CONNECTION_BEFORE_SEND
specifier|public
specifier|static
specifier|final
name|String
name|MLLP_CLOSE_CONNECTION_BEFORE_SEND
init|=
literal|"CamelMllpCloseConnectionBeforeSend"
decl_stmt|;
DECL|field|MLLP_RESET_CONNECTION_BEFORE_SEND
specifier|public
specifier|static
specifier|final
name|String
name|MLLP_RESET_CONNECTION_BEFORE_SEND
init|=
literal|"CamelMllpResetConnectionBeforeSend"
decl_stmt|;
DECL|field|MLLP_CLOSE_CONNECTION_AFTER_SEND
specifier|public
specifier|static
specifier|final
name|String
name|MLLP_CLOSE_CONNECTION_AFTER_SEND
init|=
literal|"CamelMllpCloseConnectionAfterSend"
decl_stmt|;
DECL|field|MLLP_RESET_CONNECTION_AFTER_SEND
specifier|public
specifier|static
specifier|final
name|String
name|MLLP_RESET_CONNECTION_AFTER_SEND
init|=
literal|"CamelMllpResetConnectionAfterSend"
decl_stmt|;
comment|// MSH-3
DECL|field|MLLP_SENDING_APPLICATION
specifier|public
specifier|static
specifier|final
name|String
name|MLLP_SENDING_APPLICATION
init|=
literal|"CamelMllpSendingApplication"
decl_stmt|;
comment|// MSH-4
DECL|field|MLLP_SENDING_FACILITY
specifier|public
specifier|static
specifier|final
name|String
name|MLLP_SENDING_FACILITY
init|=
literal|"CamelMllpSendingFacility"
decl_stmt|;
comment|// MSH-5
DECL|field|MLLP_RECEIVING_APPLICATION
specifier|public
specifier|static
specifier|final
name|String
name|MLLP_RECEIVING_APPLICATION
init|=
literal|"CamelMllpReceivingApplication"
decl_stmt|;
comment|// MSH-6
DECL|field|MLLP_RECEIVING_FACILITY
specifier|public
specifier|static
specifier|final
name|String
name|MLLP_RECEIVING_FACILITY
init|=
literal|"CamelMllpReceivingFacility"
decl_stmt|;
comment|// MSH-7
DECL|field|MLLP_TIMESTAMP
specifier|public
specifier|static
specifier|final
name|String
name|MLLP_TIMESTAMP
init|=
literal|"CamelMllpTimestamp"
decl_stmt|;
comment|// MSH-8
DECL|field|MLLP_SECURITY
specifier|public
specifier|static
specifier|final
name|String
name|MLLP_SECURITY
init|=
literal|"CamelMllpSecurity"
decl_stmt|;
comment|// MSH-9
DECL|field|MLLP_MESSAGE_TYPE
specifier|public
specifier|static
specifier|final
name|String
name|MLLP_MESSAGE_TYPE
init|=
literal|"CamelMllpMessageType"
decl_stmt|;
comment|// MSH-9.1
DECL|field|MLLP_EVENT_TYPE
specifier|public
specifier|static
specifier|final
name|String
name|MLLP_EVENT_TYPE
init|=
literal|"CamelMllpEventType"
decl_stmt|;
comment|// MSH-9.2
DECL|field|MLLP_TRIGGER_EVENT
specifier|public
specifier|static
specifier|final
name|String
name|MLLP_TRIGGER_EVENT
init|=
literal|"CamelMllpTriggerEvent"
decl_stmt|;
comment|// MSH-10
DECL|field|MLLP_MESSAGE_CONTROL
specifier|public
specifier|static
specifier|final
name|String
name|MLLP_MESSAGE_CONTROL
init|=
literal|"CamelMllpMessageControlId"
decl_stmt|;
comment|// MSH-11
DECL|field|MLLP_PROCESSING_ID
specifier|public
specifier|static
specifier|final
name|String
name|MLLP_PROCESSING_ID
init|=
literal|"CamelMllpProcessingId"
decl_stmt|;
comment|// MSH-12
DECL|field|MLLP_VERSION_ID
specifier|public
specifier|static
specifier|final
name|String
name|MLLP_VERSION_ID
init|=
literal|"CamelMllpVersionId"
decl_stmt|;
comment|// MSH-18
DECL|field|MLLP_CHARSET
specifier|public
specifier|static
specifier|final
name|String
name|MLLP_CHARSET
init|=
literal|"CamelMllpCharset"
decl_stmt|;
DECL|method|MllpConstants ()
specifier|private
name|MllpConstants
parameter_list|()
block|{
comment|//utility class, never constructed
block|}
block|}
end_class

end_unit

