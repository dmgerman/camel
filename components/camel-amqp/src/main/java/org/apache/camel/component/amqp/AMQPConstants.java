begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.amqp
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|amqp
package|;
end_package

begin_comment
comment|/**  * AMQP constants  */
end_comment

begin_class
DECL|class|AMQPConstants
specifier|public
specifier|final
class|class
name|AMQPConstants
block|{
comment|/**      * AMQP message annotation prefix      */
DECL|field|JMS_AMQP_MA_PREFIX
specifier|public
specifier|static
specifier|final
name|String
name|JMS_AMQP_MA_PREFIX
init|=
literal|"JMS_AMQP_MA_"
decl_stmt|;
comment|/**      * AMQP delivery annotation prefix      */
DECL|field|JMS_AMQP_DA_PREFIX
specifier|public
specifier|static
specifier|final
name|String
name|JMS_AMQP_DA_PREFIX
init|=
literal|"JMS_AMQP_DA_"
decl_stmt|;
DECL|method|AMQPConstants ()
specifier|private
name|AMQPConstants
parameter_list|()
block|{
comment|// utility class
block|}
block|}
end_class

end_unit

