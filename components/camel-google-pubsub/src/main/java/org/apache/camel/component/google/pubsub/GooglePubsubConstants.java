begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.google.pubsub
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|google
operator|.
name|pubsub
package|;
end_package

begin_class
DECL|class|GooglePubsubConstants
specifier|public
specifier|final
class|class
name|GooglePubsubConstants
block|{
DECL|field|MESSAGE_ID
specifier|public
specifier|static
specifier|final
name|String
name|MESSAGE_ID
init|=
literal|"CamelGooglePubsub.MessageId"
decl_stmt|;
DECL|field|ACK_ID
specifier|public
specifier|static
specifier|final
name|String
name|ACK_ID
init|=
literal|"CamelGooglePubsub.MsgAckId"
decl_stmt|;
DECL|field|PUBLISH_TIME
specifier|public
specifier|static
specifier|final
name|String
name|PUBLISH_TIME
init|=
literal|"CamelGooglePubsub.PublishTime"
decl_stmt|;
DECL|field|ATTRIBUTES
specifier|public
specifier|static
specifier|final
name|String
name|ATTRIBUTES
init|=
literal|"CamelGooglePubsub.Attributes"
decl_stmt|;
DECL|field|ACK_DEADLINE
specifier|public
specifier|static
specifier|final
name|String
name|ACK_DEADLINE
init|=
literal|"CamelGooglePubsub.AckDeadline"
decl_stmt|;
DECL|enum|AckMode
specifier|public
enum|enum
name|AckMode
block|{
DECL|enumConstant|AUTO
DECL|enumConstant|NONE
name|AUTO
block|,
name|NONE
block|}
DECL|method|GooglePubsubConstants ()
specifier|private
name|GooglePubsubConstants
parameter_list|()
block|{
comment|//not called
block|}
block|}
end_class

end_unit

