begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|jms
operator|.
name|listener
operator|.
name|SimpleMessageListenerContainer
import|;
end_import

begin_comment
comment|/**  * The simple {@link org.springframework.jms.listener.SimpleMessageListenerContainer container} which listen for messages  * on the JMS destination.  *<p/>  * This implementation extends Springs {@link org.springframework.jms.listener.SimpleMessageListenerContainer}.  */
end_comment

begin_class
DECL|class|SimpleJmsMessageListenerContainer
specifier|public
class|class
name|SimpleJmsMessageListenerContainer
extends|extends
name|SimpleMessageListenerContainer
block|{
DECL|field|endpoint
specifier|private
specifier|final
name|JmsEndpoint
name|endpoint
decl_stmt|;
DECL|method|SimpleJmsMessageListenerContainer (JmsEndpoint endpoint)
specifier|public
name|SimpleJmsMessageListenerContainer
parameter_list|(
name|JmsEndpoint
name|endpoint
parameter_list|)
block|{
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|runningAllowed ()
specifier|protected
name|boolean
name|runningAllowed
parameter_list|()
block|{
comment|// do not run if we have been stopped
return|return
name|endpoint
operator|.
name|isRunning
argument_list|()
return|;
block|}
block|}
end_class

end_unit

