begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.util
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|EventObject
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
name|Route
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
name|EventFactory
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
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
import|;
end_import

begin_comment
comment|/**  * Helper for easily sending event notifcations in a single line of code  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|EventHelper
specifier|public
specifier|final
class|class
name|EventHelper
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|EventHelper
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|EventHelper ()
specifier|private
name|EventHelper
parameter_list|()
block|{     }
DECL|method|notifyCamelContextStarting (CamelContext context)
specifier|public
specifier|static
name|void
name|notifyCamelContextStarting
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
name|EventNotifier
name|notifier
init|=
name|context
operator|.
name|getManagementStrategy
argument_list|()
operator|.
name|getEventNotifier
argument_list|()
decl_stmt|;
if|if
condition|(
name|notifier
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|EventFactory
name|factory
init|=
name|context
operator|.
name|getManagementStrategy
argument_list|()
operator|.
name|getEventFactory
argument_list|()
decl_stmt|;
if|if
condition|(
name|factory
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|EventObject
name|event
init|=
name|factory
operator|.
name|createCamelContextStartingEvent
argument_list|(
name|context
argument_list|)
decl_stmt|;
if|if
condition|(
name|event
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|doNotifyEvent
argument_list|(
name|notifier
argument_list|,
name|event
argument_list|)
expr_stmt|;
block|}
DECL|method|notifyCamelContextStarted (CamelContext context)
specifier|public
specifier|static
name|void
name|notifyCamelContextStarted
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
name|EventNotifier
name|notifier
init|=
name|context
operator|.
name|getManagementStrategy
argument_list|()
operator|.
name|getEventNotifier
argument_list|()
decl_stmt|;
if|if
condition|(
name|notifier
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|EventFactory
name|factory
init|=
name|context
operator|.
name|getManagementStrategy
argument_list|()
operator|.
name|getEventFactory
argument_list|()
decl_stmt|;
if|if
condition|(
name|factory
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|EventObject
name|event
init|=
name|factory
operator|.
name|createCamelContextStartedEvent
argument_list|(
name|context
argument_list|)
decl_stmt|;
if|if
condition|(
name|event
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|doNotifyEvent
argument_list|(
name|notifier
argument_list|,
name|event
argument_list|)
expr_stmt|;
block|}
DECL|method|notifyCamelContextStopping (CamelContext context)
specifier|public
specifier|static
name|void
name|notifyCamelContextStopping
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
name|EventNotifier
name|notifier
init|=
name|context
operator|.
name|getManagementStrategy
argument_list|()
operator|.
name|getEventNotifier
argument_list|()
decl_stmt|;
if|if
condition|(
name|notifier
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|EventFactory
name|factory
init|=
name|context
operator|.
name|getManagementStrategy
argument_list|()
operator|.
name|getEventFactory
argument_list|()
decl_stmt|;
if|if
condition|(
name|factory
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|EventObject
name|event
init|=
name|factory
operator|.
name|createCamelContextStoppingEvent
argument_list|(
name|context
argument_list|)
decl_stmt|;
if|if
condition|(
name|event
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|doNotifyEvent
argument_list|(
name|notifier
argument_list|,
name|event
argument_list|)
expr_stmt|;
block|}
DECL|method|notifyCamelContextStopped (CamelContext context)
specifier|public
specifier|static
name|void
name|notifyCamelContextStopped
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
name|EventNotifier
name|notifier
init|=
name|context
operator|.
name|getManagementStrategy
argument_list|()
operator|.
name|getEventNotifier
argument_list|()
decl_stmt|;
if|if
condition|(
name|notifier
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|EventFactory
name|factory
init|=
name|context
operator|.
name|getManagementStrategy
argument_list|()
operator|.
name|getEventFactory
argument_list|()
decl_stmt|;
if|if
condition|(
name|factory
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|EventObject
name|event
init|=
name|factory
operator|.
name|createCamelContextStoppedEvent
argument_list|(
name|context
argument_list|)
decl_stmt|;
if|if
condition|(
name|event
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|doNotifyEvent
argument_list|(
name|notifier
argument_list|,
name|event
argument_list|)
expr_stmt|;
block|}
DECL|method|notifyRouteStarted (CamelContext context, Route route)
specifier|public
specifier|static
name|void
name|notifyRouteStarted
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|Route
name|route
parameter_list|)
block|{
name|EventNotifier
name|notifier
init|=
name|context
operator|.
name|getManagementStrategy
argument_list|()
operator|.
name|getEventNotifier
argument_list|()
decl_stmt|;
if|if
condition|(
name|notifier
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|EventFactory
name|factory
init|=
name|context
operator|.
name|getManagementStrategy
argument_list|()
operator|.
name|getEventFactory
argument_list|()
decl_stmt|;
if|if
condition|(
name|factory
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|EventObject
name|event
init|=
name|factory
operator|.
name|createRouteStartedEvent
argument_list|(
name|route
argument_list|)
decl_stmt|;
if|if
condition|(
name|event
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|doNotifyEvent
argument_list|(
name|notifier
argument_list|,
name|event
argument_list|)
expr_stmt|;
block|}
DECL|method|notifyRouteStopped (CamelContext context, Route route)
specifier|public
specifier|static
name|void
name|notifyRouteStopped
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|Route
name|route
parameter_list|)
block|{
name|EventNotifier
name|notifier
init|=
name|context
operator|.
name|getManagementStrategy
argument_list|()
operator|.
name|getEventNotifier
argument_list|()
decl_stmt|;
if|if
condition|(
name|notifier
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|EventFactory
name|factory
init|=
name|context
operator|.
name|getManagementStrategy
argument_list|()
operator|.
name|getEventFactory
argument_list|()
decl_stmt|;
if|if
condition|(
name|factory
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|EventObject
name|event
init|=
name|factory
operator|.
name|createRouteStoppedEvent
argument_list|(
name|route
argument_list|)
decl_stmt|;
if|if
condition|(
name|event
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|doNotifyEvent
argument_list|(
name|notifier
argument_list|,
name|event
argument_list|)
expr_stmt|;
block|}
DECL|method|notifyExchangeCreated (CamelContext context, Exchange exchange)
specifier|public
specifier|static
name|void
name|notifyExchangeCreated
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
name|EventNotifier
name|notifier
init|=
name|context
operator|.
name|getManagementStrategy
argument_list|()
operator|.
name|getEventNotifier
argument_list|()
decl_stmt|;
if|if
condition|(
name|notifier
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|EventFactory
name|factory
init|=
name|context
operator|.
name|getManagementStrategy
argument_list|()
operator|.
name|getEventFactory
argument_list|()
decl_stmt|;
if|if
condition|(
name|factory
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|EventObject
name|event
init|=
name|factory
operator|.
name|createExchangeCreatedEvent
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
if|if
condition|(
name|event
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|doNotifyEvent
argument_list|(
name|notifier
argument_list|,
name|event
argument_list|)
expr_stmt|;
block|}
DECL|method|notifyExchangeDone (CamelContext context, Exchange exchange)
specifier|public
specifier|static
name|void
name|notifyExchangeDone
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
name|EventNotifier
name|notifier
init|=
name|context
operator|.
name|getManagementStrategy
argument_list|()
operator|.
name|getEventNotifier
argument_list|()
decl_stmt|;
if|if
condition|(
name|notifier
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|EventFactory
name|factory
init|=
name|context
operator|.
name|getManagementStrategy
argument_list|()
operator|.
name|getEventFactory
argument_list|()
decl_stmt|;
if|if
condition|(
name|factory
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|EventObject
name|event
init|=
name|factory
operator|.
name|createExchangeCompletedEvent
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
if|if
condition|(
name|event
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|doNotifyEvent
argument_list|(
name|notifier
argument_list|,
name|event
argument_list|)
expr_stmt|;
block|}
DECL|method|notifyExchangeFailed (CamelContext context, Exchange exchange)
specifier|public
specifier|static
name|void
name|notifyExchangeFailed
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
name|EventNotifier
name|notifier
init|=
name|context
operator|.
name|getManagementStrategy
argument_list|()
operator|.
name|getEventNotifier
argument_list|()
decl_stmt|;
if|if
condition|(
name|notifier
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|EventFactory
name|factory
init|=
name|context
operator|.
name|getManagementStrategy
argument_list|()
operator|.
name|getEventFactory
argument_list|()
decl_stmt|;
if|if
condition|(
name|factory
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|EventObject
name|event
init|=
name|factory
operator|.
name|createExchangeFailedEvent
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
if|if
condition|(
name|event
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|doNotifyEvent
argument_list|(
name|notifier
argument_list|,
name|event
argument_list|)
expr_stmt|;
block|}
DECL|method|doNotifyEvent (EventNotifier notifier, EventObject event)
specifier|private
specifier|static
name|void
name|doNotifyEvent
parameter_list|(
name|EventNotifier
name|notifier
parameter_list|,
name|EventObject
name|event
parameter_list|)
block|{
if|if
condition|(
operator|!
name|notifier
operator|.
name|isEnabled
argument_list|(
name|event
argument_list|)
condition|)
block|{
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Notification of event is disabled: "
operator|+
name|event
argument_list|)
expr_stmt|;
block|}
return|return;
block|}
try|try
block|{
name|notifier
operator|.
name|notify
argument_list|(
name|event
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Error notifying event "
operator|+
name|event
operator|+
literal|". This exception will be ignored. "
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

