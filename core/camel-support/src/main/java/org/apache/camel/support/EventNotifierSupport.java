begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.support
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|support
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
name|spi
operator|.
name|CamelEvent
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
name|EventNotifier
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
name|service
operator|.
name|ServiceSupport
import|;
end_import

begin_comment
comment|/**  * Base class to extend for custom {@link EventNotifier} implementations.  */
end_comment

begin_class
DECL|class|EventNotifierSupport
specifier|public
specifier|abstract
class|class
name|EventNotifierSupport
extends|extends
name|ServiceSupport
implements|implements
name|EventNotifier
block|{
DECL|field|ignoreCamelContextEvents
specifier|private
name|boolean
name|ignoreCamelContextEvents
decl_stmt|;
DECL|field|ignoreRouteEvents
specifier|private
name|boolean
name|ignoreRouteEvents
decl_stmt|;
DECL|field|ignoreServiceEvents
specifier|private
name|boolean
name|ignoreServiceEvents
decl_stmt|;
DECL|field|ignoreExchangeEvents
specifier|private
name|boolean
name|ignoreExchangeEvents
decl_stmt|;
DECL|field|ignoreExchangeCreatedEvent
specifier|private
name|boolean
name|ignoreExchangeCreatedEvent
decl_stmt|;
DECL|field|ignoreExchangeCompletedEvent
specifier|private
name|boolean
name|ignoreExchangeCompletedEvent
decl_stmt|;
DECL|field|ignoreExchangeFailedEvents
specifier|private
name|boolean
name|ignoreExchangeFailedEvents
decl_stmt|;
DECL|field|ignoreExchangeRedeliveryEvents
specifier|private
name|boolean
name|ignoreExchangeRedeliveryEvents
decl_stmt|;
DECL|field|ignoreExchangeSendingEvents
specifier|private
name|boolean
name|ignoreExchangeSendingEvents
decl_stmt|;
DECL|field|ignoreExchangeSentEvents
specifier|private
name|boolean
name|ignoreExchangeSentEvents
decl_stmt|;
DECL|field|ignoreStepEvents
specifier|private
name|boolean
name|ignoreStepEvents
decl_stmt|;
DECL|method|isIgnoreCamelContextEvents ()
specifier|public
name|boolean
name|isIgnoreCamelContextEvents
parameter_list|()
block|{
return|return
name|ignoreCamelContextEvents
return|;
block|}
annotation|@
name|Override
DECL|method|isEnabled (CamelEvent event)
specifier|public
name|boolean
name|isEnabled
parameter_list|(
name|CamelEvent
name|event
parameter_list|)
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|isDisabled ()
specifier|public
name|boolean
name|isDisabled
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
DECL|method|setIgnoreCamelContextEvents (boolean ignoreCamelContextEvents)
specifier|public
name|void
name|setIgnoreCamelContextEvents
parameter_list|(
name|boolean
name|ignoreCamelContextEvents
parameter_list|)
block|{
name|this
operator|.
name|ignoreCamelContextEvents
operator|=
name|ignoreCamelContextEvents
expr_stmt|;
block|}
DECL|method|isIgnoreRouteEvents ()
specifier|public
name|boolean
name|isIgnoreRouteEvents
parameter_list|()
block|{
return|return
name|ignoreRouteEvents
return|;
block|}
DECL|method|setIgnoreRouteEvents (boolean ignoreRouteEvents)
specifier|public
name|void
name|setIgnoreRouteEvents
parameter_list|(
name|boolean
name|ignoreRouteEvents
parameter_list|)
block|{
name|this
operator|.
name|ignoreRouteEvents
operator|=
name|ignoreRouteEvents
expr_stmt|;
block|}
DECL|method|isIgnoreServiceEvents ()
specifier|public
name|boolean
name|isIgnoreServiceEvents
parameter_list|()
block|{
return|return
name|ignoreServiceEvents
return|;
block|}
DECL|method|setIgnoreServiceEvents (boolean ignoreServiceEvents)
specifier|public
name|void
name|setIgnoreServiceEvents
parameter_list|(
name|boolean
name|ignoreServiceEvents
parameter_list|)
block|{
name|this
operator|.
name|ignoreServiceEvents
operator|=
name|ignoreServiceEvents
expr_stmt|;
block|}
DECL|method|isIgnoreExchangeEvents ()
specifier|public
name|boolean
name|isIgnoreExchangeEvents
parameter_list|()
block|{
return|return
name|ignoreExchangeEvents
return|;
block|}
DECL|method|setIgnoreExchangeEvents (boolean ignoreExchangeEvents)
specifier|public
name|void
name|setIgnoreExchangeEvents
parameter_list|(
name|boolean
name|ignoreExchangeEvents
parameter_list|)
block|{
name|this
operator|.
name|ignoreExchangeEvents
operator|=
name|ignoreExchangeEvents
expr_stmt|;
block|}
DECL|method|isIgnoreExchangeCreatedEvent ()
specifier|public
name|boolean
name|isIgnoreExchangeCreatedEvent
parameter_list|()
block|{
return|return
name|ignoreExchangeCreatedEvent
return|;
block|}
DECL|method|setIgnoreExchangeCreatedEvent (boolean ignoreExchangeCreatedEvent)
specifier|public
name|void
name|setIgnoreExchangeCreatedEvent
parameter_list|(
name|boolean
name|ignoreExchangeCreatedEvent
parameter_list|)
block|{
name|this
operator|.
name|ignoreExchangeCreatedEvent
operator|=
name|ignoreExchangeCreatedEvent
expr_stmt|;
block|}
DECL|method|isIgnoreExchangeCompletedEvent ()
specifier|public
name|boolean
name|isIgnoreExchangeCompletedEvent
parameter_list|()
block|{
return|return
name|ignoreExchangeCompletedEvent
return|;
block|}
DECL|method|setIgnoreExchangeCompletedEvent (boolean ignoreExchangeCompletedEvent)
specifier|public
name|void
name|setIgnoreExchangeCompletedEvent
parameter_list|(
name|boolean
name|ignoreExchangeCompletedEvent
parameter_list|)
block|{
name|this
operator|.
name|ignoreExchangeCompletedEvent
operator|=
name|ignoreExchangeCompletedEvent
expr_stmt|;
block|}
DECL|method|isIgnoreExchangeFailedEvents ()
specifier|public
name|boolean
name|isIgnoreExchangeFailedEvents
parameter_list|()
block|{
return|return
name|ignoreExchangeFailedEvents
return|;
block|}
DECL|method|setIgnoreExchangeFailedEvents (boolean ignoreExchangeFailedEvents)
specifier|public
name|void
name|setIgnoreExchangeFailedEvents
parameter_list|(
name|boolean
name|ignoreExchangeFailedEvents
parameter_list|)
block|{
name|this
operator|.
name|ignoreExchangeFailedEvents
operator|=
name|ignoreExchangeFailedEvents
expr_stmt|;
block|}
DECL|method|isIgnoreExchangeRedeliveryEvents ()
specifier|public
name|boolean
name|isIgnoreExchangeRedeliveryEvents
parameter_list|()
block|{
return|return
name|ignoreExchangeRedeliveryEvents
return|;
block|}
DECL|method|setIgnoreExchangeRedeliveryEvents (boolean ignoreExchangeRedeliveryEvents)
specifier|public
name|void
name|setIgnoreExchangeRedeliveryEvents
parameter_list|(
name|boolean
name|ignoreExchangeRedeliveryEvents
parameter_list|)
block|{
name|this
operator|.
name|ignoreExchangeRedeliveryEvents
operator|=
name|ignoreExchangeRedeliveryEvents
expr_stmt|;
block|}
DECL|method|isIgnoreExchangeSentEvents ()
specifier|public
name|boolean
name|isIgnoreExchangeSentEvents
parameter_list|()
block|{
return|return
name|ignoreExchangeSentEvents
return|;
block|}
DECL|method|setIgnoreExchangeSentEvents (boolean ignoreExchangeSentEvents)
specifier|public
name|void
name|setIgnoreExchangeSentEvents
parameter_list|(
name|boolean
name|ignoreExchangeSentEvents
parameter_list|)
block|{
name|this
operator|.
name|ignoreExchangeSentEvents
operator|=
name|ignoreExchangeSentEvents
expr_stmt|;
block|}
DECL|method|isIgnoreExchangeSendingEvents ()
specifier|public
name|boolean
name|isIgnoreExchangeSendingEvents
parameter_list|()
block|{
return|return
name|ignoreExchangeSendingEvents
return|;
block|}
DECL|method|setIgnoreExchangeSendingEvents (boolean ignoreExchangeSendingEvents)
specifier|public
name|void
name|setIgnoreExchangeSendingEvents
parameter_list|(
name|boolean
name|ignoreExchangeSendingEvents
parameter_list|)
block|{
name|this
operator|.
name|ignoreExchangeSendingEvents
operator|=
name|ignoreExchangeSendingEvents
expr_stmt|;
block|}
DECL|method|isIgnoreStepEvents ()
specifier|public
name|boolean
name|isIgnoreStepEvents
parameter_list|()
block|{
return|return
name|ignoreStepEvents
return|;
block|}
DECL|method|setIgnoreStepEvents (boolean ignoreStepEvents)
specifier|public
name|void
name|setIgnoreStepEvents
parameter_list|(
name|boolean
name|ignoreStepEvents
parameter_list|)
block|{
name|this
operator|.
name|ignoreStepEvents
operator|=
name|ignoreStepEvents
expr_stmt|;
block|}
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
comment|// noop
block|}
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
comment|// noop
block|}
block|}
end_class

end_unit

