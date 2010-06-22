begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.management.mbean
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|management
operator|.
name|mbean
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|MBeanNotificationInfo
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|NotificationBroadcasterSupport
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
name|CamelContext
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
name|management
operator|.
name|JmxNotificationBroadcasterAware
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
name|spi
operator|.
name|ManagementStrategy
import|;
end_import

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|ManagedEventNotifier
specifier|public
class|class
name|ManagedEventNotifier
extends|extends
name|NotificationBroadcasterSupport
implements|implements
name|ManagedEventNotifierMBean
block|{
DECL|field|context
specifier|private
name|CamelContext
name|context
decl_stmt|;
DECL|field|eventNotifier
specifier|private
name|EventNotifier
name|eventNotifier
decl_stmt|;
DECL|method|ManagedEventNotifier (CamelContext context, EventNotifier eventNotifier)
specifier|public
name|ManagedEventNotifier
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|EventNotifier
name|eventNotifier
parameter_list|)
block|{
name|this
operator|.
name|context
operator|=
name|context
expr_stmt|;
name|this
operator|.
name|eventNotifier
operator|=
name|eventNotifier
expr_stmt|;
if|if
condition|(
name|eventNotifier
operator|instanceof
name|JmxNotificationBroadcasterAware
condition|)
block|{
operator|(
operator|(
name|JmxNotificationBroadcasterAware
operator|)
name|eventNotifier
operator|)
operator|.
name|setNotificationBroadcaster
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|init (ManagementStrategy strategy)
specifier|public
name|void
name|init
parameter_list|(
name|ManagementStrategy
name|strategy
parameter_list|)
block|{
comment|// do nothing
block|}
DECL|method|getContext ()
specifier|public
name|CamelContext
name|getContext
parameter_list|()
block|{
return|return
name|context
return|;
block|}
DECL|method|getEventNotifier ()
specifier|public
name|EventNotifier
name|getEventNotifier
parameter_list|()
block|{
return|return
name|eventNotifier
return|;
block|}
DECL|method|isIgnoreCamelContextEvents ()
specifier|public
name|boolean
name|isIgnoreCamelContextEvents
parameter_list|()
block|{
return|return
name|getEventNotifier
argument_list|()
operator|.
name|isIgnoreCamelContextEvents
argument_list|()
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
name|getEventNotifier
argument_list|()
operator|.
name|setIgnoreCamelContextEvents
argument_list|(
name|ignoreCamelContextEvents
argument_list|)
expr_stmt|;
block|}
DECL|method|isIgnoreRouteEvents ()
specifier|public
name|boolean
name|isIgnoreRouteEvents
parameter_list|()
block|{
return|return
name|getEventNotifier
argument_list|()
operator|.
name|isIgnoreRouteEvents
argument_list|()
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
name|getEventNotifier
argument_list|()
operator|.
name|setIgnoreRouteEvents
argument_list|(
name|ignoreRouteEvents
argument_list|)
expr_stmt|;
block|}
DECL|method|isIgnoreServiceEvents ()
specifier|public
name|boolean
name|isIgnoreServiceEvents
parameter_list|()
block|{
return|return
name|getEventNotifier
argument_list|()
operator|.
name|isIgnoreServiceEvents
argument_list|()
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
name|getEventNotifier
argument_list|()
operator|.
name|setIgnoreServiceEvents
argument_list|(
name|ignoreServiceEvents
argument_list|)
expr_stmt|;
block|}
DECL|method|isIgnoreExchangeEvents ()
specifier|public
name|boolean
name|isIgnoreExchangeEvents
parameter_list|()
block|{
return|return
name|getEventNotifier
argument_list|()
operator|.
name|isIgnoreExchangeEvents
argument_list|()
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
name|getEventNotifier
argument_list|()
operator|.
name|setIgnoreExchangeEvents
argument_list|(
name|ignoreExchangeEvents
argument_list|)
expr_stmt|;
block|}
DECL|method|isIgnoreExchangeCreatedEvent ()
specifier|public
name|boolean
name|isIgnoreExchangeCreatedEvent
parameter_list|()
block|{
return|return
name|getEventNotifier
argument_list|()
operator|.
name|isIgnoreExchangeCreatedEvent
argument_list|()
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
name|getEventNotifier
argument_list|()
operator|.
name|setIgnoreExchangeCreatedEvent
argument_list|(
name|ignoreExchangeCreatedEvent
argument_list|)
expr_stmt|;
block|}
DECL|method|isIgnoreExchangeCompletedEvent ()
specifier|public
name|boolean
name|isIgnoreExchangeCompletedEvent
parameter_list|()
block|{
return|return
name|getEventNotifier
argument_list|()
operator|.
name|isIgnoreExchangeCompletedEvent
argument_list|()
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
name|getEventNotifier
argument_list|()
operator|.
name|setIgnoreExchangeCompletedEvent
argument_list|(
name|ignoreExchangeCompletedEvent
argument_list|)
expr_stmt|;
block|}
DECL|method|isIgnoreExchangeFailureEvents ()
specifier|public
name|boolean
name|isIgnoreExchangeFailureEvents
parameter_list|()
block|{
return|return
name|getEventNotifier
argument_list|()
operator|.
name|isIgnoreExchangeFailureEvents
argument_list|()
return|;
block|}
DECL|method|setIgnoreExchangeFailureEvents (boolean ignoreExchangeFailureEvents)
specifier|public
name|void
name|setIgnoreExchangeFailureEvents
parameter_list|(
name|boolean
name|ignoreExchangeFailureEvents
parameter_list|)
block|{
name|getEventNotifier
argument_list|()
operator|.
name|setIgnoreExchangeFailureEvents
argument_list|(
name|ignoreExchangeFailureEvents
argument_list|)
expr_stmt|;
block|}
DECL|method|isIgnoreExchangeSentEvents ()
specifier|public
name|boolean
name|isIgnoreExchangeSentEvents
parameter_list|()
block|{
return|return
name|getEventNotifier
argument_list|()
operator|.
name|isIgnoreExchangeSentEvents
argument_list|()
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
name|getEventNotifier
argument_list|()
operator|.
name|setIgnoreExchangeSentEvents
argument_list|(
name|ignoreExchangeSentEvents
argument_list|)
expr_stmt|;
block|}
DECL|method|getNotificationInfo ()
specifier|public
name|MBeanNotificationInfo
index|[]
name|getNotificationInfo
parameter_list|()
block|{
comment|// all the class names in the event package
name|String
index|[]
name|names
init|=
block|{
literal|"CamelContextStartedEvent"
block|,
literal|"CamelContextStartingEvent"
block|,
literal|"CamelContextStartupFailureEvent"
block|,
literal|"CamelContextStopFailureEvent"
block|,
literal|"CamelContextStoppedEvent"
block|,
literal|"CamelContextStoppingEvent"
block|,
literal|"ExchangeCompletedEvent"
block|,
literal|"ExchangeCreatedEvent"
block|,
literal|"ExchangeFailureEvent"
block|,
literal|"ExchangeFailureHandledEvent"
block|,
literal|"ExchangeSentEvent"
block|,
literal|"RouteStartedEvent"
block|,
literal|"RouteStoppedEvent"
block|,
literal|"ServiceStartupFailureEvent"
block|,
literal|"ServiceStopFailureEvent"
block|}
decl_stmt|;
name|List
argument_list|<
name|MBeanNotificationInfo
argument_list|>
name|infos
init|=
operator|new
name|ArrayList
argument_list|<
name|MBeanNotificationInfo
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|name
range|:
name|names
control|)
block|{
name|MBeanNotificationInfo
name|info
init|=
operator|new
name|MBeanNotificationInfo
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"org.apache.camel.management.event"
block|}
argument_list|,
literal|"org.apache.camel.management.event."
operator|+
name|name
argument_list|,
literal|"The event "
operator|+
name|name
operator|+
literal|" occurred"
argument_list|)
decl_stmt|;
name|infos
operator|.
name|add
argument_list|(
name|info
argument_list|)
expr_stmt|;
block|}
return|return
name|infos
operator|.
name|toArray
argument_list|(
operator|new
name|MBeanNotificationInfo
index|[
name|infos
operator|.
name|size
argument_list|()
index|]
argument_list|)
return|;
block|}
block|}
end_class

end_unit

