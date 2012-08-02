begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.sjms
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
package|;
end_package

begin_comment
comment|/**  * TODO Add Class documentation for SjmsConstants  *  */
end_comment

begin_class
DECL|class|SjmsConstants
specifier|public
specifier|final
class|class
name|SjmsConstants
block|{
DECL|field|QUEUE_PREFIX
specifier|public
specifier|static
specifier|final
name|String
name|QUEUE_PREFIX
init|=
literal|"queue:"
decl_stmt|;
DECL|field|TOPIC_PREFIX
specifier|public
specifier|static
specifier|final
name|String
name|TOPIC_PREFIX
init|=
literal|"topic:"
decl_stmt|;
DECL|field|TEMP_QUEUE_PREFIX
specifier|public
specifier|static
specifier|final
name|String
name|TEMP_QUEUE_PREFIX
init|=
literal|"temp:queue:"
decl_stmt|;
DECL|field|TEMP_TOPIC_PREFIX
specifier|public
specifier|static
specifier|final
name|String
name|TEMP_TOPIC_PREFIX
init|=
literal|"temp:topic:"
decl_stmt|;
DECL|field|JMS_MESSAGE_TYPE
specifier|public
specifier|static
specifier|final
name|String
name|JMS_MESSAGE_TYPE
init|=
literal|"JmsMessageType"
decl_stmt|;
DECL|field|ORIGINAL_MESSAGE
specifier|public
specifier|static
specifier|final
name|String
name|ORIGINAL_MESSAGE
init|=
literal|"SjmsOriginalMessage"
decl_stmt|;
DECL|method|SjmsConstants ()
specifier|private
name|SjmsConstants
parameter_list|()
block|{
comment|// Helper class
block|}
block|}
end_class

end_unit

