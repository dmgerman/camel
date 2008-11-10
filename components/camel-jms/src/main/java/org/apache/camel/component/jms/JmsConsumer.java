begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|apache
operator|.
name|camel
operator|.
name|Consumer
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
name|Processor
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
name|impl
operator|.
name|DefaultConsumer
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
name|AbstractMessageListenerContainer
import|;
end_import

begin_comment
comment|/**  * A {@link Consumer} which uses Spring's {@link AbstractMessageListenerContainer} implementations to consume JMS messages  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|JmsConsumer
specifier|public
class|class
name|JmsConsumer
extends|extends
name|DefaultConsumer
block|{
DECL|field|listenerContainer
specifier|private
specifier|final
name|AbstractMessageListenerContainer
name|listenerContainer
decl_stmt|;
DECL|field|messageListener
specifier|private
name|EndpointMessageListener
name|messageListener
decl_stmt|;
DECL|method|JmsConsumer (JmsEndpoint endpoint, Processor processor, AbstractMessageListenerContainer listenerContainer)
specifier|public
name|JmsConsumer
parameter_list|(
name|JmsEndpoint
name|endpoint
parameter_list|,
name|Processor
name|processor
parameter_list|,
name|AbstractMessageListenerContainer
name|listenerContainer
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|,
name|processor
argument_list|)
expr_stmt|;
name|this
operator|.
name|listenerContainer
operator|=
name|listenerContainer
expr_stmt|;
name|createMessageListener
argument_list|(
name|endpoint
argument_list|,
name|processor
argument_list|)
expr_stmt|;
name|this
operator|.
name|listenerContainer
operator|.
name|setMessageListener
argument_list|(
name|messageListener
argument_list|)
expr_stmt|;
block|}
DECL|method|getListenerContainer ()
specifier|public
name|AbstractMessageListenerContainer
name|getListenerContainer
parameter_list|()
block|{
return|return
name|listenerContainer
return|;
block|}
DECL|method|getEndpointMessageListener ()
specifier|public
name|EndpointMessageListener
name|getEndpointMessageListener
parameter_list|()
block|{
return|return
name|messageListener
return|;
block|}
DECL|method|createMessageListener (JmsEndpoint endpoint, Processor processor)
specifier|protected
name|void
name|createMessageListener
parameter_list|(
name|JmsEndpoint
name|endpoint
parameter_list|,
name|Processor
name|processor
parameter_list|)
block|{
name|messageListener
operator|=
operator|new
name|EndpointMessageListener
argument_list|(
name|endpoint
argument_list|,
name|processor
argument_list|)
expr_stmt|;
name|messageListener
operator|.
name|setBinding
argument_list|(
name|endpoint
operator|.
name|getBinding
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
name|listenerContainer
operator|.
name|afterPropertiesSet
argument_list|()
expr_stmt|;
name|listenerContainer
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
name|listenerContainer
operator|.
name|stop
argument_list|()
expr_stmt|;
name|listenerContainer
operator|.
name|destroy
argument_list|()
expr_stmt|;
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

