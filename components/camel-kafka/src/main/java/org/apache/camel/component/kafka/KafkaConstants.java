begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.kafka
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|kafka
package|;
end_package

begin_comment
comment|/**  *  */
end_comment

begin_class
DECL|class|KafkaConstants
specifier|public
specifier|final
class|class
name|KafkaConstants
block|{
DECL|field|DEFAULT_GROUP
specifier|public
specifier|static
specifier|final
name|String
name|DEFAULT_GROUP
init|=
literal|"group1"
decl_stmt|;
DECL|field|PARTITION_KEY
specifier|public
specifier|static
specifier|final
name|String
name|PARTITION_KEY
init|=
literal|"kafka.PARTITION_KEY"
decl_stmt|;
DECL|field|PARTITION
specifier|public
specifier|static
specifier|final
name|String
name|PARTITION
init|=
literal|"kafka.EXCHANGE_NAME"
decl_stmt|;
DECL|field|KEY
specifier|public
specifier|static
specifier|final
name|String
name|KEY
init|=
literal|"kafka.CONTENT_TYPE"
decl_stmt|;
DECL|field|TOPIC
specifier|public
specifier|static
specifier|final
name|String
name|TOPIC
init|=
literal|"kafka.TOPIC"
decl_stmt|;
DECL|method|KafkaConstants ()
specifier|private
name|KafkaConstants
parameter_list|()
block|{
comment|// Utility class
block|}
block|}
end_class

end_unit

