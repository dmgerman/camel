begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.guava.eventbus
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|guava
operator|.
name|eventbus
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|eventbus
operator|.
name|EventBus
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
name|Component
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
name|Exchange
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
name|MultipleConsumersSupport
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
name|Producer
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
name|DefaultEndpoint
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
name|spi
operator|.
name|UriEndpoint
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
name|spi
operator|.
name|UriParam
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
name|spi
operator|.
name|UriPath
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
name|util
operator|.
name|CamelContextHelper
import|;
end_import

begin_comment
comment|/**  * Guava EventBus (http://docs.guava-libraries.googlecode.com/git/javadoc/com/google/common/eventbus/EventBus.html)  * endpoint. Can create both producer and consumer ends of the route.  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|scheme
operator|=
literal|"guava-eventbus"
argument_list|,
name|title
operator|=
literal|"Guava EventBus"
argument_list|,
name|syntax
operator|=
literal|"guava-eventbus:eventBusRef"
argument_list|,
name|consumerClass
operator|=
name|GuavaEventBusConsumer
operator|.
name|class
argument_list|,
name|label
operator|=
literal|"eventbus"
argument_list|)
DECL|class|GuavaEventBusEndpoint
specifier|public
class|class
name|GuavaEventBusEndpoint
extends|extends
name|DefaultEndpoint
implements|implements
name|MultipleConsumersSupport
block|{
DECL|field|eventBus
specifier|private
name|EventBus
name|eventBus
decl_stmt|;
annotation|@
name|UriPath
DECL|field|eventBusRef
specifier|private
name|String
name|eventBusRef
decl_stmt|;
annotation|@
name|UriParam
DECL|field|eventClass
specifier|private
name|Class
argument_list|<
name|?
argument_list|>
name|eventClass
decl_stmt|;
annotation|@
name|UriParam
DECL|field|listenerInterface
specifier|private
name|Class
argument_list|<
name|?
argument_list|>
name|listenerInterface
decl_stmt|;
DECL|method|GuavaEventBusEndpoint (String endpointUri, Component component, EventBus eventBus, Class<?> listenerInterface)
specifier|public
name|GuavaEventBusEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|Component
name|component
parameter_list|,
name|EventBus
name|eventBus
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|listenerInterface
parameter_list|)
block|{
name|super
argument_list|(
name|endpointUri
argument_list|,
name|component
argument_list|)
expr_stmt|;
name|this
operator|.
name|eventBus
operator|=
name|eventBus
expr_stmt|;
name|this
operator|.
name|listenerInterface
operator|=
name|listenerInterface
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|GuavaEventBusProducer
argument_list|(
name|this
argument_list|,
name|eventBus
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createConsumer (Processor processor)
specifier|public
name|Consumer
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
name|GuavaEventBusConsumer
name|answer
init|=
operator|new
name|GuavaEventBusConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|,
name|eventBus
argument_list|,
name|eventClass
argument_list|,
name|listenerInterface
argument_list|)
decl_stmt|;
name|configureConsumer
argument_list|(
name|answer
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
annotation|@
name|Override
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|isMultipleConsumersSupported ()
specifier|public
name|boolean
name|isMultipleConsumersSupported
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
DECL|method|createExchange (Object event)
specifier|public
name|Exchange
name|createExchange
parameter_list|(
name|Object
name|event
parameter_list|)
block|{
name|Exchange
name|exchange
init|=
name|createExchange
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|event
argument_list|)
expr_stmt|;
return|return
name|exchange
return|;
block|}
DECL|method|getEventBusRef ()
specifier|public
name|String
name|getEventBusRef
parameter_list|()
block|{
return|return
name|eventBusRef
return|;
block|}
DECL|method|setEventBusRef (String eventBusRef)
specifier|public
name|void
name|setEventBusRef
parameter_list|(
name|String
name|eventBusRef
parameter_list|)
block|{
name|this
operator|.
name|eventBusRef
operator|=
name|eventBusRef
expr_stmt|;
block|}
DECL|method|getEventBus ()
specifier|public
name|EventBus
name|getEventBus
parameter_list|()
block|{
return|return
name|eventBus
return|;
block|}
DECL|method|setEventBus (EventBus eventBus)
specifier|public
name|void
name|setEventBus
parameter_list|(
name|EventBus
name|eventBus
parameter_list|)
block|{
name|this
operator|.
name|eventBus
operator|=
name|eventBus
expr_stmt|;
block|}
DECL|method|getEventClass ()
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
name|getEventClass
parameter_list|()
block|{
return|return
name|eventClass
return|;
block|}
DECL|method|setEventClass (Class<?> eventClass)
specifier|public
name|void
name|setEventClass
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|eventClass
parameter_list|)
block|{
name|this
operator|.
name|eventClass
operator|=
name|eventClass
expr_stmt|;
block|}
DECL|method|getListenerInterface ()
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
name|getListenerInterface
parameter_list|()
block|{
return|return
name|listenerInterface
return|;
block|}
DECL|method|setListenerInterface (Class<?> listenerInterface)
specifier|public
name|void
name|setListenerInterface
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|listenerInterface
parameter_list|)
block|{
name|this
operator|.
name|listenerInterface
operator|=
name|listenerInterface
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
if|if
condition|(
name|eventBusRef
operator|!=
literal|null
operator|&&
name|eventBus
operator|==
literal|null
condition|)
block|{
name|eventBus
operator|=
name|CamelContextHelper
operator|.
name|mandatoryLookup
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
name|eventBusRef
argument_list|,
name|EventBus
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

