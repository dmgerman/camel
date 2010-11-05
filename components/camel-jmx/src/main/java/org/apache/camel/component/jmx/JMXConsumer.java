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
name|ExchangePattern
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
name|impl
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
name|JMXConsumer
operator|.
name|class
argument_list|)
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
DECL|method|JMXConsumer (JMXEndpoint aEndpoint, Processor aProcessor)
specifier|public
name|JMXConsumer
parameter_list|(
name|JMXEndpoint
name|aEndpoint
parameter_list|,
name|Processor
name|aProcessor
parameter_list|)
block|{
name|super
argument_list|(
name|aEndpoint
argument_list|,
name|aProcessor
argument_list|)
expr_stmt|;
name|mFormatter
operator|=
operator|new
name|NotificationXmlFormatter
argument_list|()
expr_stmt|;
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
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
name|JMXEndpoint
name|ep
init|=
operator|(
name|JMXEndpoint
operator|)
name|getEndpoint
argument_list|()
decl_stmt|;
comment|// connect to the mbean server
if|if
condition|(
name|ep
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
name|JMXServiceURL
name|url
init|=
operator|new
name|JMXServiceURL
argument_list|(
name|ep
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
name|ep
operator|.
name|getUser
argument_list|()
block|,
name|ep
operator|.
name|getPassword
argument_list|()
block|}
decl_stmt|;
name|Map
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
name|JMXConnector
name|connector
init|=
name|JMXConnectorFactory
operator|.
name|connect
argument_list|(
name|url
argument_list|,
name|map
argument_list|)
decl_stmt|;
name|setServerConnection
argument_list|(
name|connector
operator|.
name|getMBeanServerConnection
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// subscribe
name|NotificationFilter
name|nf
init|=
name|ep
operator|.
name|getNotificationFilter
argument_list|()
decl_stmt|;
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
comment|/**      * Removes the notification listener      */
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
name|JMXEndpoint
name|ep
init|=
operator|(
name|JMXEndpoint
operator|)
name|getEndpoint
argument_list|()
decl_stmt|;
name|getServerConnection
argument_list|()
operator|.
name|removeNotificationListener
argument_list|(
name|ep
operator|.
name|getJMXObjectName
argument_list|()
argument_list|,
name|this
argument_list|)
expr_stmt|;
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
operator|(
name|JMXEndpoint
operator|)
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
argument_list|(
name|ExchangePattern
operator|.
name|InOnly
argument_list|)
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
name|mFormatter
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
block|}
end_class

end_unit

