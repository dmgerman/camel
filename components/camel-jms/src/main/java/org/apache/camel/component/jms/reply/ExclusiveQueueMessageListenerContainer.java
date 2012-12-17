begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jms.reply
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
operator|.
name|reply
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jms
operator|.
name|DefaultJmsMessageListenerContainer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jms
operator|.
name|JmsEndpoint
import|;
end_import

begin_comment
comment|/**  * This {@link org.springframework.jms.listener.DefaultMessageListenerContainer} is used for reply queues  * which are exclusive.  *<p/>  * Mind that exclusive reply queues is per producer, so if you run in a clustered environment then  * each producer should use an unique reply queue destination name. If not then other nodes may steal reply  * messages which was intended for another. For clustered environments it may be safer to use shared queues  * as each node will only consume reply messages which are intended for itself.  *<p/>  * See more details at<a href="http://camel.apache.org/jms">camel-jms</a>.  *  * @see SharedQueueMessageListenerContainer  */
end_comment

begin_class
DECL|class|ExclusiveQueueMessageListenerContainer
specifier|public
class|class
name|ExclusiveQueueMessageListenerContainer
extends|extends
name|DefaultJmsMessageListenerContainer
block|{
comment|// no need to override any methods currently
DECL|method|ExclusiveQueueMessageListenerContainer (JmsEndpoint endpoint)
specifier|public
name|ExclusiveQueueMessageListenerContainer
parameter_list|(
name|JmsEndpoint
name|endpoint
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

