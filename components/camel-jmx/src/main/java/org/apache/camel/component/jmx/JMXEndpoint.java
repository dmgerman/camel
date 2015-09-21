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
name|util
operator|.
name|Hashtable
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|MalformedObjectNameException
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
name|ObjectName
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
name|Metadata
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
name|ObjectHelper
import|;
end_import

begin_comment
comment|/**  * Endpoint that describes a connection to an mbean.  *<p/>  * The component can connect to the local platform mbean server with the following URI:  *<p/>  *<code>jmx://platform?options</code>  *<p/>  * A remote mbean server url can be provided following the initial JMX scheme like so:  *<p/>  *<code>jmx:service:jmx:rmi:///jndi/rmi://localhost:1099/jmxrmi?options</code>  *<p/>  * You can append query options to the URI in the following format, ?options=value&option2=value&...  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|scheme
operator|=
literal|"jmx"
argument_list|,
name|title
operator|=
literal|"JMX"
argument_list|,
name|syntax
operator|=
literal|"jmx:serverURL"
argument_list|,
name|consumerOnly
operator|=
literal|true
argument_list|,
name|consumerClass
operator|=
name|JMXConsumer
operator|.
name|class
argument_list|,
name|label
operator|=
literal|"monitoring"
argument_list|)
DECL|class|JMXEndpoint
specifier|public
class|class
name|JMXEndpoint
extends|extends
name|DefaultEndpoint
block|{
comment|// error messages as constants so they can be asserted on from unit tests
DECL|field|ERR_PLATFORM_SERVER
specifier|protected
specifier|static
specifier|final
name|String
name|ERR_PLATFORM_SERVER
init|=
literal|"Monitor type consumer only supported on platform server."
decl_stmt|;
DECL|field|ERR_THRESHOLD_LOW
specifier|protected
specifier|static
specifier|final
name|String
name|ERR_THRESHOLD_LOW
init|=
literal|"ThresholdLow must be set when monitoring a gauge attribute."
decl_stmt|;
DECL|field|ERR_THRESHOLD_HIGH
specifier|protected
specifier|static
specifier|final
name|String
name|ERR_THRESHOLD_HIGH
init|=
literal|"ThresholdHigh must be set when monitoring a gauge attribute."
decl_stmt|;
DECL|field|ERR_GAUGE_NOTIFY
specifier|protected
specifier|static
specifier|final
name|String
name|ERR_GAUGE_NOTIFY
init|=
literal|"One or both of NotifyHigh and NotifyLow must be true when monitoring a gauge attribute."
decl_stmt|;
DECL|field|ERR_STRING_NOTIFY
specifier|protected
specifier|static
specifier|final
name|String
name|ERR_STRING_NOTIFY
init|=
literal|"One or both of NotifyDiffer and NotifyMatch must be true when monitoring a string attribute."
decl_stmt|;
DECL|field|ERR_STRING_TO_COMPARE
specifier|protected
specifier|static
specifier|final
name|String
name|ERR_STRING_TO_COMPARE
init|=
literal|"StringToCompare must be specified when monitoring a string attribute."
decl_stmt|;
DECL|field|ERR_OBSERVED_ATTRIBUTE
specifier|protected
specifier|static
specifier|final
name|String
name|ERR_OBSERVED_ATTRIBUTE
init|=
literal|"Observed attribute must be specified"
decl_stmt|;
comment|/**      * server url comes from the remaining endpoint      */
annotation|@
name|UriPath
DECL|field|serverURL
specifier|private
name|String
name|serverURL
decl_stmt|;
comment|/**      * URI Property: [monitor types only] The attribute to observe for the monitor bean.        */
annotation|@
name|UriParam
DECL|field|observedAttribute
specifier|private
name|String
name|observedAttribute
decl_stmt|;
comment|/**      * URI Property: [monitor types only] The frequency to poll the bean to check the monitor.        */
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"10000"
argument_list|)
DECL|field|granularityPeriod
specifier|private
name|long
name|granularityPeriod
init|=
literal|10000
decl_stmt|;
comment|/**      * URI Property: [monitor types only] The type of monitor to create. One of string, gauge, counter.        */
annotation|@
name|UriParam
argument_list|(
name|enums
operator|=
literal|"counter,gauge,string"
argument_list|)
DECL|field|monitorType
specifier|private
name|String
name|monitorType
decl_stmt|;
comment|/**      * URI Property: [counter monitor only] Initial threshold for the monitor. The value must exceed this before notifications are fired.        */
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"counter"
argument_list|)
DECL|field|initThreshold
specifier|private
name|int
name|initThreshold
decl_stmt|;
comment|/**      * URI Property: [counter monitor only] The amount to increment the threshold after it's been exceeded.        */
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"counter"
argument_list|)
DECL|field|offset
specifier|private
name|int
name|offset
decl_stmt|;
comment|/**      * URI Property: [counter monitor only] The value at which the counter is reset to zero        */
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"counter"
argument_list|)
DECL|field|modulus
specifier|private
name|int
name|modulus
decl_stmt|;
comment|/**      * URI Property: [counter + gauge monitor only] If true, then the value reported in the notification is the difference from the threshold as opposed to the value itself.        */
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"counter,gauge"
argument_list|)
DECL|field|differenceMode
specifier|private
name|boolean
name|differenceMode
decl_stmt|;
comment|/**      * URI Property: [gauge monitor only] If true, the gauge will fire a notification when the high threshold is exceeded        */
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"gauge"
argument_list|)
DECL|field|notifyHigh
specifier|private
name|boolean
name|notifyHigh
decl_stmt|;
comment|/**      * URI Property: [gauge monitor only] If true, the gauge will fire a notification when the low threshold is exceeded        */
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"gauge"
argument_list|)
DECL|field|notifyLow
specifier|private
name|boolean
name|notifyLow
decl_stmt|;
comment|/**      * URI Property: [gauge monitor only] Value for the gauge's high threshold        */
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"gauge"
argument_list|)
DECL|field|thresholdHigh
specifier|private
name|Double
name|thresholdHigh
decl_stmt|;
comment|/**      * URI Property: [gauge monitor only] Value for the gauge's low threshold        */
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"gauge"
argument_list|)
DECL|field|thresholdLow
specifier|private
name|Double
name|thresholdLow
decl_stmt|;
comment|/**      * URI Property: [string monitor only] If true, the string monitor will fire a notification when the string attribute differs from the string to compare.        */
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"string"
argument_list|)
DECL|field|notifyDiffer
specifier|private
name|boolean
name|notifyDiffer
decl_stmt|;
comment|/**      * URI Property: [string monitor only] If true, the string monitor will fire a notification when the string attribute matches the string to compare.        */
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"string"
argument_list|)
DECL|field|notifyMatch
specifier|private
name|boolean
name|notifyMatch
decl_stmt|;
comment|/**      * URI Property: [string monitor only] Value for the string monitor's string to compare.        */
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"string"
argument_list|)
DECL|field|stringToCompare
specifier|private
name|String
name|stringToCompare
decl_stmt|;
comment|/**      * URI Property: Format for the message body. Either "xml" or "raw". If xml, the notification is serialized to xml. If raw, then the raw java object is set as the body.      */
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"xml"
argument_list|,
name|enums
operator|=
literal|"xml,raw"
argument_list|)
DECL|field|format
specifier|private
name|String
name|format
init|=
literal|"xml"
decl_stmt|;
comment|/**      * URI Property: credentials for making a remote connection      */
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"security"
argument_list|)
DECL|field|user
specifier|private
name|String
name|user
decl_stmt|;
comment|/**      * URI Property: credentials for making a remote connection      */
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"security"
argument_list|)
DECL|field|password
specifier|private
name|String
name|password
decl_stmt|;
comment|/**      * URI Property: The domain for the mbean you're connecting to      */
annotation|@
name|UriParam
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|"true"
argument_list|)
DECL|field|objectDomain
specifier|private
name|String
name|objectDomain
decl_stmt|;
comment|/**      * URI Property: The name key for the mbean you're connecting to. This value is mutually exclusive with the object properties that get passed.      */
annotation|@
name|UriParam
DECL|field|objectName
specifier|private
name|String
name|objectName
decl_stmt|;
comment|/**      * URI Property: Reference to a bean that implements the NotificationFilter.      */
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|notificationFilter
specifier|private
name|NotificationFilter
name|notificationFilter
decl_stmt|;
comment|/**      * URI Property: Value to handback to the listener when a notification is received. This value will be put in the message header with the key "jmx.handback"      */
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|handback
specifier|private
name|Object
name|handback
decl_stmt|;
comment|/**      * URI Property:  If true the consumer will throw an exception if unable to establish the JMX connection upon startup.  If false, the consumer will attempt      *                to establish the JMX connection every 'x' seconds until the connection is made -- where 'x' is the configured  reconnectionDelay       */
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"true"
argument_list|,
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|testConnectionOnStartup
specifier|private
name|boolean
name|testConnectionOnStartup
init|=
literal|true
decl_stmt|;
comment|/**      * URI Property:  If true the consumer will attempt to reconnect to the JMX server when any connection failure occurs.  The consumer will attempt      *                to re-establish the JMX connection every 'x' seconds until the connection is made-- where 'x' is the configured  reconnectionDelay      */
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|reconnectOnConnectionFailure
specifier|private
name|boolean
name|reconnectOnConnectionFailure
decl_stmt|;
comment|/**       * URI Property:  The number of seconds to wait before attempting to retry establishment of the initial connection or attempt to reconnect a lost connection       */
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"10"
argument_list|,
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|reconnectDelay
specifier|private
name|int
name|reconnectDelay
init|=
literal|10
decl_stmt|;
comment|/**      * URI Property: properties for the object name. These values will be used if the objectName param is not set      */
DECL|field|objectProperties
specifier|private
name|Hashtable
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|objectProperties
decl_stmt|;
comment|/**      * cached object name that was built from the objectName param or the hashtable      */
DECL|field|jmxObjectName
specifier|private
specifier|transient
name|ObjectName
name|jmxObjectName
decl_stmt|;
DECL|method|JMXEndpoint (String aEndpointUri, JMXComponent aComponent)
specifier|public
name|JMXEndpoint
parameter_list|(
name|String
name|aEndpointUri
parameter_list|,
name|JMXComponent
name|aComponent
parameter_list|)
block|{
name|super
argument_list|(
name|aEndpointUri
argument_list|,
name|aComponent
argument_list|)
expr_stmt|;
block|}
DECL|method|createConsumer (Processor aProcessor)
specifier|public
name|Consumer
name|createConsumer
parameter_list|(
name|Processor
name|aProcessor
parameter_list|)
throws|throws
name|Exception
block|{
comment|// validate that all of the endpoint is configured properly
if|if
condition|(
name|getMonitorType
argument_list|()
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
operator|!
name|isPlatformServer
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|ERR_PLATFORM_SERVER
argument_list|)
throw|;
block|}
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|getObservedAttribute
argument_list|()
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|ERR_OBSERVED_ATTRIBUTE
argument_list|)
throw|;
block|}
if|if
condition|(
name|getMonitorType
argument_list|()
operator|.
name|equals
argument_list|(
literal|"string"
argument_list|)
condition|)
block|{
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|getStringToCompare
argument_list|()
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|ERR_STRING_TO_COMPARE
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
name|isNotifyDiffer
argument_list|()
operator|&&
operator|!
name|isNotifyMatch
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|ERR_STRING_NOTIFY
argument_list|)
throw|;
block|}
block|}
elseif|else
if|if
condition|(
name|getMonitorType
argument_list|()
operator|.
name|equals
argument_list|(
literal|"gauge"
argument_list|)
condition|)
block|{
if|if
condition|(
operator|!
name|isNotifyHigh
argument_list|()
operator|&&
operator|!
name|isNotifyLow
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|ERR_GAUGE_NOTIFY
argument_list|)
throw|;
block|}
if|if
condition|(
name|getThresholdHigh
argument_list|()
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|ERR_THRESHOLD_HIGH
argument_list|)
throw|;
block|}
if|if
condition|(
name|getThresholdLow
argument_list|()
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|ERR_THRESHOLD_LOW
argument_list|)
throw|;
block|}
block|}
name|JMXMonitorConsumer
name|answer
init|=
operator|new
name|JMXMonitorConsumer
argument_list|(
name|this
argument_list|,
name|aProcessor
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
else|else
block|{
comment|// shouldn't need any other validation.
name|JMXConsumer
name|answer
init|=
operator|new
name|JMXConsumer
argument_list|(
name|this
argument_list|,
name|aProcessor
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
block|}
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"producing JMX notifications is not supported"
argument_list|)
throw|;
block|}
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
DECL|method|getFormat ()
specifier|public
name|String
name|getFormat
parameter_list|()
block|{
return|return
name|format
return|;
block|}
DECL|method|setFormat (String aFormat)
specifier|public
name|void
name|setFormat
parameter_list|(
name|String
name|aFormat
parameter_list|)
block|{
name|format
operator|=
name|aFormat
expr_stmt|;
block|}
DECL|method|isXML ()
specifier|public
name|boolean
name|isXML
parameter_list|()
block|{
return|return
literal|"xml"
operator|.
name|equals
argument_list|(
name|getFormat
argument_list|()
argument_list|)
return|;
block|}
DECL|method|isPlatformServer ()
specifier|public
name|boolean
name|isPlatformServer
parameter_list|()
block|{
return|return
literal|"platform"
operator|.
name|equals
argument_list|(
name|getServerURL
argument_list|()
argument_list|)
return|;
block|}
DECL|method|getUser ()
specifier|public
name|String
name|getUser
parameter_list|()
block|{
return|return
name|user
return|;
block|}
DECL|method|setUser (String aUser)
specifier|public
name|void
name|setUser
parameter_list|(
name|String
name|aUser
parameter_list|)
block|{
name|user
operator|=
name|aUser
expr_stmt|;
block|}
DECL|method|getPassword ()
specifier|public
name|String
name|getPassword
parameter_list|()
block|{
return|return
name|password
return|;
block|}
DECL|method|setPassword (String aPassword)
specifier|public
name|void
name|setPassword
parameter_list|(
name|String
name|aPassword
parameter_list|)
block|{
name|password
operator|=
name|aPassword
expr_stmt|;
block|}
DECL|method|getObjectDomain ()
specifier|public
name|String
name|getObjectDomain
parameter_list|()
block|{
return|return
name|objectDomain
return|;
block|}
DECL|method|setObjectDomain (String aObjectDomain)
specifier|public
name|void
name|setObjectDomain
parameter_list|(
name|String
name|aObjectDomain
parameter_list|)
block|{
name|objectDomain
operator|=
name|aObjectDomain
expr_stmt|;
block|}
DECL|method|getObjectName ()
specifier|public
name|String
name|getObjectName
parameter_list|()
block|{
return|return
name|objectName
return|;
block|}
DECL|method|setObjectName (String aObjectName)
specifier|public
name|void
name|setObjectName
parameter_list|(
name|String
name|aObjectName
parameter_list|)
block|{
if|if
condition|(
name|getObjectProperties
argument_list|()
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Cannot set both objectName and objectProperties"
argument_list|)
throw|;
block|}
name|objectName
operator|=
name|aObjectName
expr_stmt|;
block|}
DECL|method|getServerURL ()
specifier|protected
name|String
name|getServerURL
parameter_list|()
block|{
return|return
name|serverURL
return|;
block|}
DECL|method|setServerURL (String aServerURL)
specifier|protected
name|void
name|setServerURL
parameter_list|(
name|String
name|aServerURL
parameter_list|)
block|{
name|serverURL
operator|=
name|aServerURL
expr_stmt|;
block|}
DECL|method|getNotificationFilter ()
specifier|public
name|NotificationFilter
name|getNotificationFilter
parameter_list|()
block|{
return|return
name|notificationFilter
return|;
block|}
DECL|method|setNotificationFilter (NotificationFilter aFilterRef)
specifier|public
name|void
name|setNotificationFilter
parameter_list|(
name|NotificationFilter
name|aFilterRef
parameter_list|)
block|{
name|notificationFilter
operator|=
name|aFilterRef
expr_stmt|;
block|}
DECL|method|getHandback ()
specifier|public
name|Object
name|getHandback
parameter_list|()
block|{
return|return
name|handback
return|;
block|}
DECL|method|setHandback (Object aHandback)
specifier|public
name|void
name|setHandback
parameter_list|(
name|Object
name|aHandback
parameter_list|)
block|{
name|handback
operator|=
name|aHandback
expr_stmt|;
block|}
DECL|method|getObjectProperties ()
specifier|public
name|Hashtable
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getObjectProperties
parameter_list|()
block|{
return|return
name|objectProperties
return|;
block|}
comment|/**      * Setter for the ObjectProperties is either called by reflection when      * processing the URI or manually by the component.      *<p/>      * If the URI contained a value with a reference like "objectProperties=#myHashtable"      * then the Hashtable will be set in place.      *<p/>      * If there are extra properties that begin with "key." then the component will      * create a Hashtable with these values after removing the "key." prefix.      */
DECL|method|setObjectProperties (Hashtable<String, String> aObjectProperties)
specifier|public
name|void
name|setObjectProperties
parameter_list|(
name|Hashtable
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|aObjectProperties
parameter_list|)
block|{
if|if
condition|(
name|getObjectName
argument_list|()
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Cannot set both objectName and objectProperties"
argument_list|)
throw|;
block|}
name|objectProperties
operator|=
name|aObjectProperties
expr_stmt|;
block|}
DECL|method|getJMXObjectName ()
specifier|protected
name|ObjectName
name|getJMXObjectName
parameter_list|()
throws|throws
name|MalformedObjectNameException
block|{
if|if
condition|(
name|jmxObjectName
operator|==
literal|null
condition|)
block|{
name|ObjectName
name|on
init|=
name|buildObjectName
argument_list|()
decl_stmt|;
name|setJMXObjectName
argument_list|(
name|on
argument_list|)
expr_stmt|;
block|}
return|return
name|jmxObjectName
return|;
block|}
DECL|method|setJMXObjectName (ObjectName aCachedObjectName)
specifier|protected
name|void
name|setJMXObjectName
parameter_list|(
name|ObjectName
name|aCachedObjectName
parameter_list|)
block|{
name|jmxObjectName
operator|=
name|aCachedObjectName
expr_stmt|;
block|}
DECL|method|getObservedAttribute ()
specifier|public
name|String
name|getObservedAttribute
parameter_list|()
block|{
return|return
name|observedAttribute
return|;
block|}
DECL|method|setObservedAttribute (String aObservedAttribute)
specifier|public
name|void
name|setObservedAttribute
parameter_list|(
name|String
name|aObservedAttribute
parameter_list|)
block|{
name|observedAttribute
operator|=
name|aObservedAttribute
expr_stmt|;
block|}
DECL|method|getGranularityPeriod ()
specifier|public
name|long
name|getGranularityPeriod
parameter_list|()
block|{
return|return
name|granularityPeriod
return|;
block|}
DECL|method|setGranularityPeriod (long aGranularityPeriod)
specifier|public
name|void
name|setGranularityPeriod
parameter_list|(
name|long
name|aGranularityPeriod
parameter_list|)
block|{
name|granularityPeriod
operator|=
name|aGranularityPeriod
expr_stmt|;
block|}
DECL|method|getMonitorType ()
specifier|public
name|String
name|getMonitorType
parameter_list|()
block|{
return|return
name|monitorType
return|;
block|}
DECL|method|setMonitorType (String aMonitorType)
specifier|public
name|void
name|setMonitorType
parameter_list|(
name|String
name|aMonitorType
parameter_list|)
block|{
name|monitorType
operator|=
name|aMonitorType
expr_stmt|;
block|}
DECL|method|getInitThreshold ()
specifier|public
name|int
name|getInitThreshold
parameter_list|()
block|{
return|return
name|initThreshold
return|;
block|}
DECL|method|setInitThreshold (int aInitThreshold)
specifier|public
name|void
name|setInitThreshold
parameter_list|(
name|int
name|aInitThreshold
parameter_list|)
block|{
name|initThreshold
operator|=
name|aInitThreshold
expr_stmt|;
block|}
DECL|method|getOffset ()
specifier|public
name|int
name|getOffset
parameter_list|()
block|{
return|return
name|offset
return|;
block|}
DECL|method|setOffset (int aOffset)
specifier|public
name|void
name|setOffset
parameter_list|(
name|int
name|aOffset
parameter_list|)
block|{
name|offset
operator|=
name|aOffset
expr_stmt|;
block|}
DECL|method|getModulus ()
specifier|public
name|int
name|getModulus
parameter_list|()
block|{
return|return
name|modulus
return|;
block|}
DECL|method|setModulus (int aModulus)
specifier|public
name|void
name|setModulus
parameter_list|(
name|int
name|aModulus
parameter_list|)
block|{
name|modulus
operator|=
name|aModulus
expr_stmt|;
block|}
DECL|method|isDifferenceMode ()
specifier|public
name|boolean
name|isDifferenceMode
parameter_list|()
block|{
return|return
name|differenceMode
return|;
block|}
DECL|method|setDifferenceMode (boolean aDifferenceMode)
specifier|public
name|void
name|setDifferenceMode
parameter_list|(
name|boolean
name|aDifferenceMode
parameter_list|)
block|{
name|differenceMode
operator|=
name|aDifferenceMode
expr_stmt|;
block|}
DECL|method|isNotifyHigh ()
specifier|public
name|boolean
name|isNotifyHigh
parameter_list|()
block|{
return|return
name|notifyHigh
return|;
block|}
DECL|method|setNotifyHigh (boolean aNotifyHigh)
specifier|public
name|void
name|setNotifyHigh
parameter_list|(
name|boolean
name|aNotifyHigh
parameter_list|)
block|{
name|notifyHigh
operator|=
name|aNotifyHigh
expr_stmt|;
block|}
DECL|method|isNotifyLow ()
specifier|public
name|boolean
name|isNotifyLow
parameter_list|()
block|{
return|return
name|notifyLow
return|;
block|}
DECL|method|setNotifyLow (boolean aNotifyLow)
specifier|public
name|void
name|setNotifyLow
parameter_list|(
name|boolean
name|aNotifyLow
parameter_list|)
block|{
name|notifyLow
operator|=
name|aNotifyLow
expr_stmt|;
block|}
DECL|method|getThresholdHigh ()
specifier|public
name|Double
name|getThresholdHigh
parameter_list|()
block|{
return|return
name|thresholdHigh
return|;
block|}
DECL|method|setThresholdHigh (Double aThresholdHigh)
specifier|public
name|void
name|setThresholdHigh
parameter_list|(
name|Double
name|aThresholdHigh
parameter_list|)
block|{
name|thresholdHigh
operator|=
name|aThresholdHigh
expr_stmt|;
block|}
DECL|method|getThresholdLow ()
specifier|public
name|Double
name|getThresholdLow
parameter_list|()
block|{
return|return
name|thresholdLow
return|;
block|}
DECL|method|setThresholdLow (Double aThresholdLow)
specifier|public
name|void
name|setThresholdLow
parameter_list|(
name|Double
name|aThresholdLow
parameter_list|)
block|{
name|thresholdLow
operator|=
name|aThresholdLow
expr_stmt|;
block|}
DECL|method|isNotifyDiffer ()
specifier|public
name|boolean
name|isNotifyDiffer
parameter_list|()
block|{
return|return
name|notifyDiffer
return|;
block|}
DECL|method|setNotifyDiffer (boolean aNotifyDiffer)
specifier|public
name|void
name|setNotifyDiffer
parameter_list|(
name|boolean
name|aNotifyDiffer
parameter_list|)
block|{
name|notifyDiffer
operator|=
name|aNotifyDiffer
expr_stmt|;
block|}
DECL|method|isNotifyMatch ()
specifier|public
name|boolean
name|isNotifyMatch
parameter_list|()
block|{
return|return
name|notifyMatch
return|;
block|}
DECL|method|setNotifyMatch (boolean aNotifyMatch)
specifier|public
name|void
name|setNotifyMatch
parameter_list|(
name|boolean
name|aNotifyMatch
parameter_list|)
block|{
name|notifyMatch
operator|=
name|aNotifyMatch
expr_stmt|;
block|}
DECL|method|getStringToCompare ()
specifier|public
name|String
name|getStringToCompare
parameter_list|()
block|{
return|return
name|stringToCompare
return|;
block|}
DECL|method|setStringToCompare (String aStringToCompare)
specifier|public
name|void
name|setStringToCompare
parameter_list|(
name|String
name|aStringToCompare
parameter_list|)
block|{
name|stringToCompare
operator|=
name|aStringToCompare
expr_stmt|;
block|}
DECL|method|getTestConnectionOnStartup ()
specifier|public
name|boolean
name|getTestConnectionOnStartup
parameter_list|()
block|{
return|return
name|this
operator|.
name|testConnectionOnStartup
return|;
block|}
DECL|method|setTestConnectionOnStartup (boolean testConnectionOnStartup)
specifier|public
name|void
name|setTestConnectionOnStartup
parameter_list|(
name|boolean
name|testConnectionOnStartup
parameter_list|)
block|{
name|this
operator|.
name|testConnectionOnStartup
operator|=
name|testConnectionOnStartup
expr_stmt|;
block|}
DECL|method|getReconnectOnConnectionFailure ()
specifier|public
name|boolean
name|getReconnectOnConnectionFailure
parameter_list|()
block|{
return|return
name|this
operator|.
name|reconnectOnConnectionFailure
return|;
block|}
DECL|method|setReconnectOnConnectionFailure (boolean reconnectOnConnectionFailure)
specifier|public
name|void
name|setReconnectOnConnectionFailure
parameter_list|(
name|boolean
name|reconnectOnConnectionFailure
parameter_list|)
block|{
name|this
operator|.
name|reconnectOnConnectionFailure
operator|=
name|reconnectOnConnectionFailure
expr_stmt|;
block|}
DECL|method|getReconnectDelay ()
specifier|public
name|int
name|getReconnectDelay
parameter_list|()
block|{
return|return
name|this
operator|.
name|reconnectDelay
return|;
block|}
DECL|method|setReconnectDelay (int reconnectDelay)
specifier|public
name|void
name|setReconnectDelay
parameter_list|(
name|int
name|reconnectDelay
parameter_list|)
block|{
name|this
operator|.
name|reconnectDelay
operator|=
name|reconnectDelay
expr_stmt|;
block|}
DECL|method|buildObjectName ()
specifier|private
name|ObjectName
name|buildObjectName
parameter_list|()
throws|throws
name|MalformedObjectNameException
block|{
name|ObjectName
name|objectName
decl_stmt|;
if|if
condition|(
name|getObjectProperties
argument_list|()
operator|==
literal|null
condition|)
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|(
name|getObjectDomain
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|':'
argument_list|)
operator|.
name|append
argument_list|(
literal|"name="
argument_list|)
operator|.
name|append
argument_list|(
name|getObjectName
argument_list|()
argument_list|)
decl_stmt|;
name|objectName
operator|=
operator|new
name|ObjectName
argument_list|(
name|sb
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|objectName
operator|=
operator|new
name|ObjectName
argument_list|(
name|getObjectDomain
argument_list|()
argument_list|,
name|getObjectProperties
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|objectName
return|;
block|}
block|}
end_class

end_unit

