begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.jmxconnect
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|jmxconnect
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
name|util
operator|.
name|UuidGenerator
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

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|*
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|CopyOnWriteArrayList
import|;
end_import

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|MBeanCamelServerConnectionClient
specifier|public
class|class
name|MBeanCamelServerConnectionClient
extends|extends
name|MBeanServerConnectionDelegate
implements|implements
name|Processor
block|{
DECL|field|log
specifier|private
specifier|static
specifier|final
name|Log
name|log
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|MBeanCamelServerConnectionClient
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|serverConnection
specifier|private
name|MBeanCamelServerConnection
name|serverConnection
decl_stmt|;
DECL|field|replyToEndpoint
specifier|private
name|Endpoint
name|replyToEndpoint
decl_stmt|;
DECL|field|listeners
specifier|private
name|List
name|listeners
init|=
operator|new
name|CopyOnWriteArrayList
argument_list|()
decl_stmt|;
DECL|field|idGenerator
specifier|private
name|UuidGenerator
name|idGenerator
init|=
operator|new
name|UuidGenerator
argument_list|()
decl_stmt|;
DECL|field|localNotifier
specifier|private
name|NotificationBroadcasterSupport
name|localNotifier
init|=
operator|new
name|NotificationBroadcasterSupport
argument_list|()
decl_stmt|;
DECL|method|MBeanCamelServerConnectionClient (MBeanCamelServerConnection serverConnection)
specifier|public
name|MBeanCamelServerConnectionClient
parameter_list|(
name|MBeanCamelServerConnection
name|serverConnection
parameter_list|)
block|{
name|super
argument_list|(
name|serverConnection
argument_list|)
expr_stmt|;
name|this
operator|.
name|serverConnection
operator|=
name|serverConnection
expr_stmt|;
block|}
comment|/**      * Add a notification listener      */
DECL|method|addNotificationListener (ObjectName name, NotificationListener listener, NotificationFilter filter, Object handback)
specifier|public
name|void
name|addNotificationListener
parameter_list|(
name|ObjectName
name|name
parameter_list|,
name|NotificationListener
name|listener
parameter_list|,
name|NotificationFilter
name|filter
parameter_list|,
name|Object
name|handback
parameter_list|)
block|{
name|String
name|id
init|=
name|generateId
argument_list|()
decl_stmt|;
name|ListenerInfo
name|info
init|=
operator|new
name|ListenerInfo
argument_list|(
name|id
argument_list|,
name|listener
argument_list|,
name|filter
argument_list|,
name|handback
argument_list|)
decl_stmt|;
name|listeners
operator|.
name|add
argument_list|(
name|info
argument_list|)
expr_stmt|;
name|localNotifier
operator|.
name|addNotificationListener
argument_list|(
name|listener
argument_list|,
name|filter
argument_list|,
name|handback
argument_list|)
expr_stmt|;
comment|// TODO need to create an endpoint for replies!!!
if|if
condition|(
name|replyToEndpoint
operator|==
literal|null
condition|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"no replyToDestination for replies to be received!"
argument_list|)
expr_stmt|;
block|}
name|serverConnection
operator|.
name|addNotificationListener
argument_list|(
name|id
argument_list|,
name|name
argument_list|,
name|replyToEndpoint
argument_list|)
expr_stmt|;
block|}
DECL|method|generateId ()
specifier|public
name|String
name|generateId
parameter_list|()
block|{
return|return
name|idGenerator
operator|.
name|generateId
argument_list|()
return|;
block|}
comment|/**      * Remove a Notification Listener      */
DECL|method|removeNotificationListener (ObjectName name, NotificationListener listener)
specifier|public
name|void
name|removeNotificationListener
parameter_list|(
name|ObjectName
name|name
parameter_list|,
name|NotificationListener
name|listener
parameter_list|)
throws|throws
name|ListenerNotFoundException
block|{
for|for
control|(
name|Iterator
name|i
init|=
name|listeners
operator|.
name|iterator
argument_list|()
init|;
name|i
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|ListenerInfo
name|li
init|=
operator|(
name|ListenerInfo
operator|)
name|i
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|li
operator|.
name|getListener
argument_list|()
operator|==
name|listener
condition|)
block|{
name|listeners
operator|.
name|remove
argument_list|(
name|i
argument_list|)
expr_stmt|;
name|serverConnection
operator|.
name|removeNotificationListener
argument_list|(
name|li
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
name|localNotifier
operator|.
name|removeNotificationListener
argument_list|(
name|listener
argument_list|)
expr_stmt|;
block|}
comment|/**      * Remove a Notification Listener      */
DECL|method|removeNotificationListener (ObjectName name, NotificationListener listener, NotificationFilter filter, Object handback)
specifier|public
name|void
name|removeNotificationListener
parameter_list|(
name|ObjectName
name|name
parameter_list|,
name|NotificationListener
name|listener
parameter_list|,
name|NotificationFilter
name|filter
parameter_list|,
name|Object
name|handback
parameter_list|)
throws|throws
name|ListenerNotFoundException
block|{
for|for
control|(
name|Iterator
name|i
init|=
name|listeners
operator|.
name|iterator
argument_list|()
init|;
name|i
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|ListenerInfo
name|li
init|=
operator|(
name|ListenerInfo
operator|)
name|i
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|li
operator|.
name|getListener
argument_list|()
operator|==
name|listener
operator|&&
name|li
operator|.
name|getFilter
argument_list|()
operator|==
name|filter
operator|&&
name|li
operator|.
name|getHandback
argument_list|()
operator|==
name|handback
condition|)
block|{
name|listeners
operator|.
name|remove
argument_list|(
name|i
argument_list|)
expr_stmt|;
name|serverConnection
operator|.
name|removeNotificationListener
argument_list|(
name|li
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|localNotifier
operator|.
name|removeNotificationListener
argument_list|(
name|listener
argument_list|,
name|filter
argument_list|,
name|handback
argument_list|)
expr_stmt|;
block|}
DECL|method|process (Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|Notification
name|notification
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|Notification
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|notification
operator|!=
literal|null
condition|)
block|{
name|localNotifier
operator|.
name|sendNotification
argument_list|(
name|notification
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Received message which is not a Notification: "
operator|+
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

