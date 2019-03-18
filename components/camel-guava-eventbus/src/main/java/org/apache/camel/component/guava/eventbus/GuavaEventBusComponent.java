begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

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
name|Endpoint
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
name|annotations
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
name|support
operator|.
name|DefaultComponent
import|;
end_import

begin_comment
comment|/**  * Camel component for Guava EventBus  * (http://docs.guava-libraries.googlecode.com/git/javadoc/com/google/common/eventbus/EventBus.html). Supports both  * producer and consumer endpoints.  */
end_comment

begin_class
annotation|@
name|Component
argument_list|(
literal|"guava-eventbus"
argument_list|)
DECL|class|GuavaEventBusComponent
specifier|public
class|class
name|GuavaEventBusComponent
extends|extends
name|DefaultComponent
block|{
DECL|field|eventBus
specifier|private
name|EventBus
name|eventBus
decl_stmt|;
DECL|field|listenerInterface
specifier|private
name|Class
argument_list|<
name|?
argument_list|>
name|listenerInterface
decl_stmt|;
DECL|method|GuavaEventBusComponent ()
specifier|public
name|GuavaEventBusComponent
parameter_list|()
block|{     }
annotation|@
name|Override
DECL|method|createEndpoint (String uri, String remaining, Map<String, Object> parameters)
specifier|protected
name|Endpoint
name|createEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|remaining
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
throws|throws
name|Exception
block|{
name|GuavaEventBusEndpoint
name|answer
init|=
operator|new
name|GuavaEventBusEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|,
name|eventBus
argument_list|,
name|listenerInterface
argument_list|)
decl_stmt|;
name|answer
operator|.
name|setEventBusRef
argument_list|(
name|remaining
argument_list|)
expr_stmt|;
name|setProperties
argument_list|(
name|answer
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
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
comment|/**      * To use the given Guava EventBus instance      */
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
comment|/**      * The interface with method(s) marked with the @Subscribe annotation.      * Dynamic proxy will be created over the interface so it could be registered as the EventBus listener.      * Particularly useful when creating multi-event listeners and for handling DeadEvent properly. This option cannot be used together with eventClass option.      */
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
block|}
end_class

end_unit

