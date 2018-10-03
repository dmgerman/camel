begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jmx
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jmx
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|management
operator|.
name|ManagementFactory
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ExecutorService
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
name|ScheduledExecutorService
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
name|TimeUnit
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|MBeanServerConnection
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|Notification
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|NotificationFilter
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|NotificationListener
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|ObjectName
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|remote
operator|.
name|JMXConnectionNotification
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|remote
operator|.
name|JMXConnector
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|remote
operator|.
name|JMXConnectorFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|remote
operator|.
name|JMXServiceURL
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
name|Message
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
name|support
operator|.
name|DefaultConsumer
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
name|ServiceHelper
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
name|URISupport
import|;
end_import

begin_comment
comment|/**  * Consumer will add itself as a NotificationListener on the object  * specified by the objectName param.  */
end_comment

begin_class
DECL|class|JMXConsumer
specifier|public
class|class
name|JMXConsumer
extends|extends
name|DefaultConsumer
implements|implements
name|NotificationListener
block|{
DECL|field|mJmxEndpoint
specifier|private
name|JMXEndpoint
name|mJmxEndpoint
decl_stmt|;
DECL|field|mConnector
specifier|private
name|JMXConnector
name|mConnector
decl_stmt|;
DECL|field|mConnectionId
specifier|private
name|String
name|mConnectionId
decl_stmt|;
comment|/**      * Used for processing notifications (should not block notification thread)      */
DECL|field|executorService
specifier|private
name|ExecutorService
name|executorService
decl_stmt|;
DECL|field|shutdownExecutorService
specifier|private
name|boolean
name|shutdownExecutorService
decl_stmt|;
comment|/**      * Used to schedule delayed connection attempts      */
DECL|field|mScheduledExecutor
specifier|private
name|ScheduledExecutorService
name|mScheduledExecutor
decl_stmt|;
comment|/**      * Used to receive notifications about lost connections      */
DECL|field|mConnectionNotificationListener
specifier|private
name|ConnectionNotificationListener
name|mConnectionNotificationListener
decl_stmt|;
comment|/**      * connection to the mbean server (local or remote)      */
DECL|field|mServerConnection
specifier|private
name|MBeanServerConnection
name|mServerConnection
decl_stmt|;
comment|/**      * used to format Notification objects as xml      */
DECL|field|mFormatter
specifier|private
name|NotificationXmlFormatter
name|mFormatter
decl_stmt|;
DECL|method|JMXConsumer (JMXEndpoint endpoint, Processor processor)
specifier|public
name|JMXConsumer
parameter_list|(
name|JMXEndpoint
name|endpoint
parameter_list|,
name|Processor
name|processor
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
name|mJmxEndpoint
operator|=
name|endpoint
expr_stmt|;
name|this
operator|.
name|mFormatter
operator|=
operator|new
name|NotificationXmlFormatter
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getEndpoint ()
specifier|public
name|JMXEndpoint
name|getEndpoint
parameter_list|()
block|{
return|return
operator|(
name|JMXEndpoint
operator|)
name|super
operator|.
name|getEndpoint
argument_list|()
return|;
block|}
comment|/**      * Initializes the mbean server connection and starts listening for      * Notification events from the object.      */
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
name|ServiceHelper
operator|.
name|startService
argument_list|(
name|mFormatter
argument_list|)
expr_stmt|;
if|if
condition|(
name|executorService
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|getEndpoint
argument_list|()
operator|.
name|getExecutorService
argument_list|()
operator|!=
literal|null
condition|)
block|{
comment|// use shared thread-pool
name|executorService
operator|=
name|getEndpoint
argument_list|()
operator|.
name|getExecutorService
argument_list|()
expr_stmt|;
block|}
else|else
block|{
comment|// lets just use a single threaded thread-pool to process these notifications
name|String
name|name
init|=
literal|"JMXConsumer["
operator|+
name|getEndpoint
argument_list|()
operator|.
name|getJMXObjectName
argument_list|()
operator|.
name|getCanonicalName
argument_list|()
operator|+
literal|"]"
decl_stmt|;
name|executorService
operator|=
name|getEndpoint
argument_list|()
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getExecutorServiceManager
argument_list|()
operator|.
name|newSingleThreadExecutor
argument_list|(
name|this
argument_list|,
name|name
argument_list|)
expr_stmt|;
name|shutdownExecutorService
operator|=
literal|true
expr_stmt|;
block|}
block|}
comment|// connect to the mbean server
if|if
condition|(
name|mJmxEndpoint
operator|.
name|isPlatformServer
argument_list|()
condition|)
block|{
name|setServerConnection
argument_list|(
name|ManagementFactory
operator|.
name|getPlatformMBeanServer
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
try|try
block|{
name|initNetworkConnection
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
if|if
condition|(
operator|!
name|mJmxEndpoint
operator|.
name|getTestConnectionOnStartup
argument_list|()
condition|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Failed to connect to JMX server.>> {}"
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
name|scheduleDelayedStart
argument_list|()
expr_stmt|;
return|return;
block|}
else|else
block|{
throw|throw
name|e
throw|;
block|}
block|}
block|}
comment|// subscribe
name|addNotificationListener
argument_list|()
expr_stmt|;
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
block|}
comment|/**      * Initializes a network connection to the configured JMX server and registers a connection       * notification listener to to receive notifications of connection loss      */
DECL|method|initNetworkConnection ()
specifier|private
name|void
name|initNetworkConnection
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
name|mConnector
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|mConnector
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// ignore, as this is best effort
block|}
block|}
name|JMXServiceURL
name|url
init|=
operator|new
name|JMXServiceURL
argument_list|(
name|mJmxEndpoint
operator|.
name|getServerURL
argument_list|()
argument_list|)
decl_stmt|;
name|String
index|[]
name|creds
init|=
block|{
name|mJmxEndpoint
operator|.
name|getUser
argument_list|()
block|,
name|mJmxEndpoint
operator|.
name|getPassword
argument_list|()
block|}
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
index|[]
argument_list|>
name|map
init|=
name|Collections
operator|.
name|singletonMap
argument_list|(
name|JMXConnector
operator|.
name|CREDENTIALS
argument_list|,
name|creds
argument_list|)
decl_stmt|;
name|mConnector
operator|=
name|JMXConnectorFactory
operator|.
name|connect
argument_list|(
name|url
argument_list|,
name|map
argument_list|)
expr_stmt|;
name|mConnector
operator|.
name|addConnectionNotificationListener
argument_list|(
name|getConnectionNotificationListener
argument_list|()
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|mConnectionId
operator|=
name|mConnector
operator|.
name|getConnectionId
argument_list|()
expr_stmt|;
name|setServerConnection
argument_list|(
name|mConnector
operator|.
name|getMBeanServerConnection
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns the connection notification listener; creates the default listener if one does not       * already exist      */
DECL|method|getConnectionNotificationListener ()
specifier|protected
name|ConnectionNotificationListener
name|getConnectionNotificationListener
parameter_list|()
block|{
if|if
condition|(
name|mConnectionNotificationListener
operator|==
literal|null
condition|)
block|{
name|mConnectionNotificationListener
operator|=
operator|new
name|ConnectionNotificationListener
argument_list|()
expr_stmt|;
block|}
return|return
name|mConnectionNotificationListener
return|;
block|}
comment|/**      * Schedules execution of the doStart() operation to occur again after the reconnect delay      */
DECL|method|scheduleDelayedStart ()
specifier|protected
name|void
name|scheduleDelayedStart
parameter_list|()
throws|throws
name|Exception
block|{
name|Runnable
name|startRunnable
init|=
operator|new
name|Runnable
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|run
parameter_list|()
block|{
try|try
block|{
name|doStart
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"An unrecoverable exception has occurred while starting the JMX consumer"
operator|+
literal|"for endpoint {}"
argument_list|,
name|URISupport
operator|.
name|sanitizeUri
argument_list|(
name|mJmxEndpoint
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
decl_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Delaying JMX consumer startup for endpoint {}. Trying again in {} seconds."
argument_list|,
name|URISupport
operator|.
name|sanitizeUri
argument_list|(
name|mJmxEndpoint
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
argument_list|,
name|mJmxEndpoint
operator|.
name|getReconnectDelay
argument_list|()
argument_list|)
expr_stmt|;
name|getExecutor
argument_list|()
operator|.
name|schedule
argument_list|(
name|startRunnable
argument_list|,
name|mJmxEndpoint
operator|.
name|getReconnectDelay
argument_list|()
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
block|}
comment|/**      * Helper class used for receiving connection loss notifications      */
DECL|class|ConnectionNotificationListener
specifier|private
class|class
name|ConnectionNotificationListener
implements|implements
name|NotificationListener
block|{
annotation|@
name|Override
DECL|method|handleNotification (Notification notification, Object handback)
specifier|public
name|void
name|handleNotification
parameter_list|(
name|Notification
name|notification
parameter_list|,
name|Object
name|handback
parameter_list|)
block|{
name|JMXConnectionNotification
name|connectionNotification
init|=
operator|(
name|JMXConnectionNotification
operator|)
name|notification
decl_stmt|;
comment|// only reset the connection if the notification is for the connection from this endpoint
if|if
condition|(
operator|!
name|connectionNotification
operator|.
name|getConnectionId
argument_list|()
operator|.
name|equals
argument_list|(
name|mConnectionId
argument_list|)
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|connectionNotification
operator|.
name|getType
argument_list|()
operator|.
name|equals
argument_list|(
name|JMXConnectionNotification
operator|.
name|NOTIFS_LOST
argument_list|)
operator|||
name|connectionNotification
operator|.
name|getType
argument_list|()
operator|.
name|equals
argument_list|(
name|JMXConnectionNotification
operator|.
name|CLOSED
argument_list|)
operator|||
name|connectionNotification
operator|.
name|getType
argument_list|()
operator|.
name|equals
argument_list|(
name|JMXConnectionNotification
operator|.
name|FAILED
argument_list|)
condition|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Lost JMX connection for : {}"
argument_list|,
name|URISupport
operator|.
name|sanitizeUri
argument_list|(
name|mJmxEndpoint
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|mJmxEndpoint
operator|.
name|getReconnectOnConnectionFailure
argument_list|()
condition|)
block|{
name|scheduleReconnect
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"The JMX consumer will not be reconnected. Use 'reconnectOnConnectionFailure' to "
operator|+
literal|"enable reconnections."
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
comment|/**      * Schedules an attempt to re-initialize a lost connection after the reconnect delay      */
DECL|method|scheduleReconnect ()
specifier|protected
name|void
name|scheduleReconnect
parameter_list|()
block|{
name|Runnable
name|startRunnable
init|=
operator|new
name|Runnable
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|run
parameter_list|()
block|{
try|try
block|{
name|initNetworkConnection
argument_list|()
expr_stmt|;
name|addNotificationListener
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Failed to reconnect to JMX server.>> {}"
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
name|scheduleReconnect
argument_list|()
expr_stmt|;
block|}
block|}
block|}
decl_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Delaying JMX consumer reconnection for endpoint {}. Trying again in {} seconds."
argument_list|,
name|URISupport
operator|.
name|sanitizeUri
argument_list|(
name|mJmxEndpoint
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
argument_list|,
name|mJmxEndpoint
operator|.
name|getReconnectDelay
argument_list|()
argument_list|)
expr_stmt|;
name|getExecutor
argument_list|()
operator|.
name|schedule
argument_list|(
name|startRunnable
argument_list|,
name|mJmxEndpoint
operator|.
name|getReconnectDelay
argument_list|()
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns the thread executor used for scheduling delayed connection events.  Creates the executor      * if it does not already exist      */
DECL|method|getExecutor ()
specifier|private
name|ScheduledExecutorService
name|getExecutor
parameter_list|()
block|{
if|if
condition|(
name|this
operator|.
name|mScheduledExecutor
operator|==
literal|null
condition|)
block|{
name|mScheduledExecutor
operator|=
name|mJmxEndpoint
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getExecutorServiceManager
argument_list|()
operator|.
name|newSingleThreadScheduledExecutor
argument_list|(
name|this
argument_list|,
literal|"JMXConnectionExecutor"
argument_list|)
expr_stmt|;
block|}
return|return
name|mScheduledExecutor
return|;
block|}
comment|/**      * Adds a notification listener to the target bean.      */
DECL|method|addNotificationListener ()
specifier|protected
name|void
name|addNotificationListener
parameter_list|()
throws|throws
name|Exception
block|{
name|JMXEndpoint
name|ep
init|=
name|getEndpoint
argument_list|()
decl_stmt|;
name|NotificationFilter
name|nf
init|=
name|ep
operator|.
name|getNotificationFilter
argument_list|()
decl_stmt|;
comment|// if we should observe a single attribute then use filter
if|if
condition|(
name|nf
operator|==
literal|null
operator|&&
name|ep
operator|.
name|getObservedAttribute
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Observing attribute: {}"
argument_list|,
name|ep
operator|.
name|getObservedAttribute
argument_list|()
argument_list|)
expr_stmt|;
name|boolean
name|match
init|=
operator|!
name|ep
operator|.
name|isNotifyDiffer
argument_list|()
decl_stmt|;
name|nf
operator|=
operator|new
name|JMXConsumerNotificationFilter
argument_list|(
name|ep
operator|.
name|getObservedAttribute
argument_list|()
argument_list|,
name|ep
operator|.
name|getStringToCompare
argument_list|()
argument_list|,
name|match
argument_list|)
expr_stmt|;
block|}
name|ObjectName
name|objectName
init|=
name|ep
operator|.
name|getJMXObjectName
argument_list|()
decl_stmt|;
name|getServerConnection
argument_list|()
operator|.
name|addNotificationListener
argument_list|(
name|objectName
argument_list|,
name|this
argument_list|,
name|nf
argument_list|,
name|ep
operator|.
name|getHandback
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Removes the notification listeners and terminates the background connection polling process if it exists      */
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
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
if|if
condition|(
name|mScheduledExecutor
operator|!=
literal|null
condition|)
block|{
name|getEndpoint
argument_list|()
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getExecutorServiceManager
argument_list|()
operator|.
name|shutdownNow
argument_list|(
name|mScheduledExecutor
argument_list|)
expr_stmt|;
name|mScheduledExecutor
operator|=
literal|null
expr_stmt|;
block|}
name|removeNotificationListeners
argument_list|()
expr_stmt|;
if|if
condition|(
name|mConnector
operator|!=
literal|null
condition|)
block|{
name|mConnector
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
name|ServiceHelper
operator|.
name|stopService
argument_list|(
name|mFormatter
argument_list|)
expr_stmt|;
if|if
condition|(
name|shutdownExecutorService
operator|&&
name|executorService
operator|!=
literal|null
condition|)
block|{
name|getEndpoint
argument_list|()
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getExecutorServiceManager
argument_list|()
operator|.
name|shutdownNow
argument_list|(
name|executorService
argument_list|)
expr_stmt|;
name|executorService
operator|=
literal|null
expr_stmt|;
block|}
block|}
comment|/**      * Removes the configured notification listener and the connection notification listener from the       * connection      */
DECL|method|removeNotificationListeners ()
specifier|protected
name|void
name|removeNotificationListeners
parameter_list|()
throws|throws
name|Exception
block|{
name|getServerConnection
argument_list|()
operator|.
name|removeNotificationListener
argument_list|(
name|mJmxEndpoint
operator|.
name|getJMXObjectName
argument_list|()
argument_list|,
name|this
argument_list|)
expr_stmt|;
if|if
condition|(
name|mConnectionNotificationListener
operator|!=
literal|null
condition|)
block|{
name|mConnector
operator|.
name|removeConnectionNotificationListener
argument_list|(
name|mConnectionNotificationListener
argument_list|)
expr_stmt|;
name|mConnectionNotificationListener
operator|=
literal|null
expr_stmt|;
block|}
block|}
DECL|method|getServerConnection ()
specifier|protected
name|MBeanServerConnection
name|getServerConnection
parameter_list|()
block|{
return|return
name|mServerConnection
return|;
block|}
DECL|method|setServerConnection (MBeanServerConnection aServerConnection)
specifier|protected
name|void
name|setServerConnection
parameter_list|(
name|MBeanServerConnection
name|aServerConnection
parameter_list|)
block|{
name|mServerConnection
operator|=
name|aServerConnection
expr_stmt|;
block|}
comment|/**      * Processes the Notification received. The handback will be set as      * the header "jmx.handback" while the Notification will be set as      * the body.      *<p/>      * If the format is set to "xml" then the Notification will be converted      * to XML first using {@link NotificationXmlFormatter}      *      * @see javax.management.NotificationListener#handleNotification(javax.management.Notification, java.lang.Object)      */
DECL|method|handleNotification (Notification aNotification, Object aHandback)
specifier|public
name|void
name|handleNotification
parameter_list|(
name|Notification
name|aNotification
parameter_list|,
name|Object
name|aHandback
parameter_list|)
block|{
name|JMXEndpoint
name|ep
init|=
name|getEndpoint
argument_list|()
decl_stmt|;
name|Exchange
name|exchange
init|=
name|getEndpoint
argument_list|()
operator|.
name|createExchange
argument_list|()
decl_stmt|;
name|Message
name|message
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|message
operator|.
name|setHeader
argument_list|(
literal|"jmx.handback"
argument_list|,
name|aHandback
argument_list|)
expr_stmt|;
try|try
block|{
if|if
condition|(
name|ep
operator|.
name|isXML
argument_list|()
condition|)
block|{
name|message
operator|.
name|setBody
argument_list|(
name|getFormatter
argument_list|()
operator|.
name|format
argument_list|(
name|aNotification
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|message
operator|.
name|setBody
argument_list|(
name|aNotification
argument_list|)
expr_stmt|;
block|}
comment|// process the notification from thred pool to not block this notification callback thread from the JVM
name|executorService
operator|.
name|submit
argument_list|(
parameter_list|()
lambda|->
block|{
try|try
block|{
name|getProcessor
argument_list|()
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|getExceptionHandler
argument_list|()
operator|.
name|handleException
argument_list|(
literal|"Failed to process notification"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NotificationFormatException
name|e
parameter_list|)
block|{
name|getExceptionHandler
argument_list|()
operator|.
name|handleException
argument_list|(
literal|"Failed to marshal notification"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|getFormatter ()
specifier|protected
name|NotificationXmlFormatter
name|getFormatter
parameter_list|()
block|{
return|return
name|mFormatter
return|;
block|}
block|}
end_class

end_unit

