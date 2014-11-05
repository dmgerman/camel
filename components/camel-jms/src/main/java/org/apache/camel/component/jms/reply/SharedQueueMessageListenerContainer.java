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

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

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
name|DefaultMessageListenerContainer
import|;
end_import

begin_comment
comment|/**  * This {@link DefaultMessageListenerContainer} is used for reply queues which are shared.  *<p/>  * This implementation supports using a fixed or dynamic JMS Message Selector to pickup the  * designated reply messages from the shared queue. Since the queue is shared, then we can only  * pickup the reply messages which is intended for us, so to support that we must use JMS  * Message Selectors.  *<p/>  * See more details at<a href="http://camel.apache.org/jms">camel-jms</a>.  *  * @see ExclusiveQueueMessageListenerContainer  */
end_comment

begin_class
DECL|class|SharedQueueMessageListenerContainer
specifier|public
class|class
name|SharedQueueMessageListenerContainer
extends|extends
name|DefaultJmsMessageListenerContainer
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|SharedQueueMessageListenerContainer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|fixedMessageSelector
specifier|private
name|String
name|fixedMessageSelector
decl_stmt|;
DECL|field|creator
specifier|private
name|MessageSelectorCreator
name|creator
decl_stmt|;
comment|/**      * Use a fixed JMS message selector      *      * @param endpoint the endpoint      * @param fixedMessageSelector the fixed selector      */
DECL|method|SharedQueueMessageListenerContainer (JmsEndpoint endpoint, String fixedMessageSelector)
specifier|public
name|SharedQueueMessageListenerContainer
parameter_list|(
name|JmsEndpoint
name|endpoint
parameter_list|,
name|String
name|fixedMessageSelector
parameter_list|)
block|{
comment|// request-reply listener container should not allow quick-stop so we can keep listening for reply messages
name|super
argument_list|(
name|endpoint
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|this
operator|.
name|fixedMessageSelector
operator|=
name|fixedMessageSelector
expr_stmt|;
block|}
comment|/**      * Use a dynamic JMS message selector      *      * @param endpoint the endpoint      * @param creator the create to create the dynamic selector      */
DECL|method|SharedQueueMessageListenerContainer (JmsEndpoint endpoint, MessageSelectorCreator creator)
specifier|public
name|SharedQueueMessageListenerContainer
parameter_list|(
name|JmsEndpoint
name|endpoint
parameter_list|,
name|MessageSelectorCreator
name|creator
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|this
operator|.
name|creator
operator|=
name|creator
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getMessageSelector ()
specifier|public
name|String
name|getMessageSelector
parameter_list|()
block|{
comment|// override this method and return the appropriate selector
name|String
name|id
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|fixedMessageSelector
operator|!=
literal|null
condition|)
block|{
name|id
operator|=
name|fixedMessageSelector
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|creator
operator|!=
literal|null
condition|)
block|{
name|id
operator|=
name|creator
operator|.
name|get
argument_list|()
expr_stmt|;
block|}
name|LOG
operator|.
name|trace
argument_list|(
literal|"Using MessageSelector[{}]"
argument_list|,
name|id
argument_list|)
expr_stmt|;
return|return
name|id
return|;
block|}
block|}
end_class

end_unit

