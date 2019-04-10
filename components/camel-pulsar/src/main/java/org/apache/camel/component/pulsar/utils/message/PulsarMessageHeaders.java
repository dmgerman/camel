begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.pulsar.utils.message
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|pulsar
operator|.
name|utils
operator|.
name|message
package|;
end_package

begin_interface
DECL|interface|PulsarMessageHeaders
specifier|public
interface|interface
name|PulsarMessageHeaders
block|{
DECL|field|PROPERTIES
name|String
name|PROPERTIES
init|=
literal|"properties"
decl_stmt|;
DECL|field|PRODUCER_NAME
name|String
name|PRODUCER_NAME
init|=
literal|"producer_name"
decl_stmt|;
DECL|field|SEQUENCE_ID
name|String
name|SEQUENCE_ID
init|=
literal|"sequence_id"
decl_stmt|;
DECL|field|PUBLISH_TIME
name|String
name|PUBLISH_TIME
init|=
literal|"publish_time"
decl_stmt|;
DECL|field|MESSAGE_ID
name|String
name|MESSAGE_ID
init|=
literal|"message_id"
decl_stmt|;
DECL|field|EVENT_TIME
name|String
name|EVENT_TIME
init|=
literal|"event_time"
decl_stmt|;
DECL|field|KEY
name|String
name|KEY
init|=
literal|"key"
decl_stmt|;
DECL|field|KEY_BYTES
name|String
name|KEY_BYTES
init|=
literal|"key_bytes"
decl_stmt|;
DECL|field|TOPIC_NAME
name|String
name|TOPIC_NAME
init|=
literal|"topic_name"
decl_stmt|;
block|}
end_interface

end_unit

