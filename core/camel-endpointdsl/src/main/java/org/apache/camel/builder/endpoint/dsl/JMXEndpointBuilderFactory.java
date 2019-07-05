begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.builder.endpoint.dsl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
operator|.
name|endpoint
operator|.
name|dsl
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
name|javax
operator|.
name|annotation
operator|.
name|Generated
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
name|builder
operator|.
name|EndpointConsumerBuilder
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
name|builder
operator|.
name|EndpointProducerBuilder
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
name|builder
operator|.
name|endpoint
operator|.
name|AbstractEndpointBuilder
import|;
end_import

begin_comment
comment|/**  * The jmx component allows to receive JMX notifications.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_interface
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.EndpointDslMojo"
argument_list|)
DECL|interface|JMXEndpointBuilderFactory
specifier|public
interface|interface
name|JMXEndpointBuilderFactory
block|{
comment|/**      * Builder for endpoint for the JMX component.      */
DECL|interface|JMXEndpointBuilder
specifier|public
interface|interface
name|JMXEndpointBuilder
extends|extends
name|EndpointConsumerBuilder
block|{
DECL|method|advanced ()
specifier|default
name|AdvancedJMXEndpointBuilder
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedJMXEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Format for the message body. Either xml or raw. If xml, the          * notification is serialized to xml. If raw, then the raw java object          * is set as the body.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: consumer          */
DECL|method|format (String format)
specifier|default
name|JMXEndpointBuilder
name|format
parameter_list|(
name|String
name|format
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"format"
argument_list|,
name|format
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The frequency to poll the bean to check the monitor (monitor types          * only).          *           * The option is a:<code>long</code> type.          *           * Group: consumer          */
DECL|method|granularityPeriod (long granularityPeriod)
specifier|default
name|JMXEndpointBuilder
name|granularityPeriod
parameter_list|(
name|long
name|granularityPeriod
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"granularityPeriod"
argument_list|,
name|granularityPeriod
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The frequency to poll the bean to check the monitor (monitor types          * only).          *           * The option will be converted to a<code>long</code> type.          *           * Group: consumer          */
DECL|method|granularityPeriod (String granularityPeriod)
specifier|default
name|JMXEndpointBuilder
name|granularityPeriod
parameter_list|(
name|String
name|granularityPeriod
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"granularityPeriod"
argument_list|,
name|granularityPeriod
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The type of monitor to create. One of string, gauge, counter (monitor          * types only).          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: consumer          */
DECL|method|monitorType (String monitorType)
specifier|default
name|JMXEndpointBuilder
name|monitorType
parameter_list|(
name|String
name|monitorType
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"monitorType"
argument_list|,
name|monitorType
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The domain for the mbean you're connecting to.          *           * The option is a:<code>java.lang.String</code> type.          *           * Required: true          * Group: consumer          */
DECL|method|objectDomain (String objectDomain)
specifier|default
name|JMXEndpointBuilder
name|objectDomain
parameter_list|(
name|String
name|objectDomain
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"objectDomain"
argument_list|,
name|objectDomain
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The name key for the mbean you're connecting to. This value is          * mutually exclusive with the object properties that get passed.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: consumer          */
DECL|method|objectName (String objectName)
specifier|default
name|JMXEndpointBuilder
name|objectName
parameter_list|(
name|String
name|objectName
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"objectName"
argument_list|,
name|objectName
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The attribute to observe for the monitor bean or consumer.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: consumer          */
DECL|method|observedAttribute (String observedAttribute)
specifier|default
name|JMXEndpointBuilder
name|observedAttribute
parameter_list|(
name|String
name|observedAttribute
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"observedAttribute"
argument_list|,
name|observedAttribute
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Initial threshold for the monitor. The value must exceed this before          * notifications are fired (counter monitor only).          *           * The option is a:<code>int</code> type.          *           * Group: counter          */
DECL|method|initThreshold (int initThreshold)
specifier|default
name|JMXEndpointBuilder
name|initThreshold
parameter_list|(
name|int
name|initThreshold
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"initThreshold"
argument_list|,
name|initThreshold
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Initial threshold for the monitor. The value must exceed this before          * notifications are fired (counter monitor only).          *           * The option will be converted to a<code>int</code> type.          *           * Group: counter          */
DECL|method|initThreshold (String initThreshold)
specifier|default
name|JMXEndpointBuilder
name|initThreshold
parameter_list|(
name|String
name|initThreshold
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"initThreshold"
argument_list|,
name|initThreshold
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The value at which the counter is reset to zero (counter monitor          * only).          *           * The option is a:<code>int</code> type.          *           * Group: counter          */
DECL|method|modulus (int modulus)
specifier|default
name|JMXEndpointBuilder
name|modulus
parameter_list|(
name|int
name|modulus
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"modulus"
argument_list|,
name|modulus
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The value at which the counter is reset to zero (counter monitor          * only).          *           * The option will be converted to a<code>int</code> type.          *           * Group: counter          */
DECL|method|modulus (String modulus)
specifier|default
name|JMXEndpointBuilder
name|modulus
parameter_list|(
name|String
name|modulus
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"modulus"
argument_list|,
name|modulus
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The amount to increment the threshold after it's been exceeded          * (counter monitor only).          *           * The option is a:<code>int</code> type.          *           * Group: counter          */
DECL|method|offset (int offset)
specifier|default
name|JMXEndpointBuilder
name|offset
parameter_list|(
name|int
name|offset
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"offset"
argument_list|,
name|offset
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The amount to increment the threshold after it's been exceeded          * (counter monitor only).          *           * The option will be converted to a<code>int</code> type.          *           * Group: counter          */
DECL|method|offset (String offset)
specifier|default
name|JMXEndpointBuilder
name|offset
parameter_list|(
name|String
name|offset
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"offset"
argument_list|,
name|offset
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * If true, then the value reported in the notification is the          * difference from the threshold as opposed to the value itself (counter          * and gauge monitor only).          *           * The option is a:<code>boolean</code> type.          *           * Group: gauge          */
DECL|method|differenceMode (boolean differenceMode)
specifier|default
name|JMXEndpointBuilder
name|differenceMode
parameter_list|(
name|boolean
name|differenceMode
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"differenceMode"
argument_list|,
name|differenceMode
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * If true, then the value reported in the notification is the          * difference from the threshold as opposed to the value itself (counter          * and gauge monitor only).          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: gauge          */
DECL|method|differenceMode (String differenceMode)
specifier|default
name|JMXEndpointBuilder
name|differenceMode
parameter_list|(
name|String
name|differenceMode
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"differenceMode"
argument_list|,
name|differenceMode
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * If true, the gauge will fire a notification when the high threshold          * is exceeded (gauge monitor only).          *           * The option is a:<code>boolean</code> type.          *           * Group: gauge          */
DECL|method|notifyHigh (boolean notifyHigh)
specifier|default
name|JMXEndpointBuilder
name|notifyHigh
parameter_list|(
name|boolean
name|notifyHigh
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"notifyHigh"
argument_list|,
name|notifyHigh
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * If true, the gauge will fire a notification when the high threshold          * is exceeded (gauge monitor only).          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: gauge          */
DECL|method|notifyHigh (String notifyHigh)
specifier|default
name|JMXEndpointBuilder
name|notifyHigh
parameter_list|(
name|String
name|notifyHigh
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"notifyHigh"
argument_list|,
name|notifyHigh
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * If true, the gauge will fire a notification when the low threshold is          * exceeded (gauge monitor only).          *           * The option is a:<code>boolean</code> type.          *           * Group: gauge          */
DECL|method|notifyLow (boolean notifyLow)
specifier|default
name|JMXEndpointBuilder
name|notifyLow
parameter_list|(
name|boolean
name|notifyLow
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"notifyLow"
argument_list|,
name|notifyLow
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * If true, the gauge will fire a notification when the low threshold is          * exceeded (gauge monitor only).          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: gauge          */
DECL|method|notifyLow (String notifyLow)
specifier|default
name|JMXEndpointBuilder
name|notifyLow
parameter_list|(
name|String
name|notifyLow
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"notifyLow"
argument_list|,
name|notifyLow
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Value for the gauge's high threshold (gauge monitor only).          *           * The option is a:<code>java.lang.Double</code> type.          *           * Group: gauge          */
DECL|method|thresholdHigh (Double thresholdHigh)
specifier|default
name|JMXEndpointBuilder
name|thresholdHigh
parameter_list|(
name|Double
name|thresholdHigh
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"thresholdHigh"
argument_list|,
name|thresholdHigh
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Value for the gauge's high threshold (gauge monitor only).          *           * The option will be converted to a<code>java.lang.Double</code> type.          *           * Group: gauge          */
DECL|method|thresholdHigh (String thresholdHigh)
specifier|default
name|JMXEndpointBuilder
name|thresholdHigh
parameter_list|(
name|String
name|thresholdHigh
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"thresholdHigh"
argument_list|,
name|thresholdHigh
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Value for the gauge's low threshold (gauge monitor only).          *           * The option is a:<code>java.lang.Double</code> type.          *           * Group: gauge          */
DECL|method|thresholdLow (Double thresholdLow)
specifier|default
name|JMXEndpointBuilder
name|thresholdLow
parameter_list|(
name|Double
name|thresholdLow
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"thresholdLow"
argument_list|,
name|thresholdLow
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Value for the gauge's low threshold (gauge monitor only).          *           * The option will be converted to a<code>java.lang.Double</code> type.          *           * Group: gauge          */
DECL|method|thresholdLow (String thresholdLow)
specifier|default
name|JMXEndpointBuilder
name|thresholdLow
parameter_list|(
name|String
name|thresholdLow
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"thresholdLow"
argument_list|,
name|thresholdLow
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Credentials for making a remote connection.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: security          */
DECL|method|password (String password)
specifier|default
name|JMXEndpointBuilder
name|password
parameter_list|(
name|String
name|password
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"password"
argument_list|,
name|password
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Credentials for making a remote connection.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: security          */
DECL|method|user (String user)
specifier|default
name|JMXEndpointBuilder
name|user
parameter_list|(
name|String
name|user
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"user"
argument_list|,
name|user
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
comment|/**      * Advanced builder for endpoint for the JMX component.      */
DECL|interface|AdvancedJMXEndpointBuilder
specifier|public
interface|interface
name|AdvancedJMXEndpointBuilder
extends|extends
name|EndpointConsumerBuilder
block|{
DECL|method|basic ()
specifier|default
name|JMXEndpointBuilder
name|basic
parameter_list|()
block|{
return|return
operator|(
name|JMXEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          *           * The option is a:<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|basicPropertyBinding ( boolean basicPropertyBinding)
specifier|default
name|AdvancedJMXEndpointBuilder
name|basicPropertyBinding
parameter_list|(
name|boolean
name|basicPropertyBinding
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"basicPropertyBinding"
argument_list|,
name|basicPropertyBinding
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|basicPropertyBinding ( String basicPropertyBinding)
specifier|default
name|AdvancedJMXEndpointBuilder
name|basicPropertyBinding
parameter_list|(
name|String
name|basicPropertyBinding
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"basicPropertyBinding"
argument_list|,
name|basicPropertyBinding
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * To use a custom shared thread pool for the consumers. By default each          * consume has their own thread-pool to process and route notifications.          *           * The option is a:<code>java.util.concurrent.ExecutorService</code>          * type.          *           * Group: advanced          */
DECL|method|executorService ( ExecutorService executorService)
specifier|default
name|AdvancedJMXEndpointBuilder
name|executorService
parameter_list|(
name|ExecutorService
name|executorService
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"executorService"
argument_list|,
name|executorService
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * To use a custom shared thread pool for the consumers. By default each          * consume has their own thread-pool to process and route notifications.          *           * The option will be converted to a          *<code>java.util.concurrent.ExecutorService</code> type.          *           * Group: advanced          */
DECL|method|executorService ( String executorService)
specifier|default
name|AdvancedJMXEndpointBuilder
name|executorService
parameter_list|(
name|String
name|executorService
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"executorService"
argument_list|,
name|executorService
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Value to handback to the listener when a notification is received.          * This value will be put in the message header with the key          * jmx.handback.          *           * The option is a:<code>java.lang.Object</code> type.          *           * Group: advanced          */
DECL|method|handback (Object handback)
specifier|default
name|AdvancedJMXEndpointBuilder
name|handback
parameter_list|(
name|Object
name|handback
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"handback"
argument_list|,
name|handback
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Value to handback to the listener when a notification is received.          * This value will be put in the message header with the key          * jmx.handback.          *           * The option will be converted to a<code>java.lang.Object</code> type.          *           * Group: advanced          */
DECL|method|handback (String handback)
specifier|default
name|AdvancedJMXEndpointBuilder
name|handback
parameter_list|(
name|String
name|handback
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"handback"
argument_list|,
name|handback
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Reference to a bean that implements the NotificationFilter.          *           * The option is a:<code>javax.management.NotificationFilter</code>          * type.          *           * Group: advanced          */
DECL|method|notificationFilter ( Object notificationFilter)
specifier|default
name|AdvancedJMXEndpointBuilder
name|notificationFilter
parameter_list|(
name|Object
name|notificationFilter
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"notificationFilter"
argument_list|,
name|notificationFilter
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Reference to a bean that implements the NotificationFilter.          *           * The option will be converted to a          *<code>javax.management.NotificationFilter</code> type.          *           * Group: advanced          */
DECL|method|notificationFilter ( String notificationFilter)
specifier|default
name|AdvancedJMXEndpointBuilder
name|notificationFilter
parameter_list|(
name|String
name|notificationFilter
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"notificationFilter"
argument_list|,
name|notificationFilter
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Properties for the object name. These values will be used if the          * objectName param is not set.          *           * The option is a:<code>java.util.Map&lt;java.lang.String,          * java.lang.String&gt;</code> type.          *           * Group: advanced          */
DECL|method|objectProperties ( Map<String, String> objectProperties)
specifier|default
name|AdvancedJMXEndpointBuilder
name|objectProperties
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|objectProperties
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"objectProperties"
argument_list|,
name|objectProperties
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Properties for the object name. These values will be used if the          * objectName param is not set.          *           * The option will be converted to a          *<code>java.util.Map&lt;java.lang.String, java.lang.String&gt;</code>          * type.          *           * Group: advanced          */
DECL|method|objectProperties ( String objectProperties)
specifier|default
name|AdvancedJMXEndpointBuilder
name|objectProperties
parameter_list|(
name|String
name|objectProperties
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"objectProperties"
argument_list|,
name|objectProperties
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The number of seconds to wait before attempting to retry          * establishment of the initial connection or attempt to reconnect a          * lost connection.          *           * The option is a:<code>int</code> type.          *           * Group: advanced          */
DECL|method|reconnectDelay (int reconnectDelay)
specifier|default
name|AdvancedJMXEndpointBuilder
name|reconnectDelay
parameter_list|(
name|int
name|reconnectDelay
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"reconnectDelay"
argument_list|,
name|reconnectDelay
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The number of seconds to wait before attempting to retry          * establishment of the initial connection or attempt to reconnect a          * lost connection.          *           * The option will be converted to a<code>int</code> type.          *           * Group: advanced          */
DECL|method|reconnectDelay (String reconnectDelay)
specifier|default
name|AdvancedJMXEndpointBuilder
name|reconnectDelay
parameter_list|(
name|String
name|reconnectDelay
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"reconnectDelay"
argument_list|,
name|reconnectDelay
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * If true the consumer will attempt to reconnect to the JMX server when          * any connection failure occurs. The consumer will attempt to          * re-establish the JMX connection every 'x' seconds until the          * connection is made-- where 'x' is the configured reconnectionDelay.          *           * The option is a:<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|reconnectOnConnectionFailure ( boolean reconnectOnConnectionFailure)
specifier|default
name|AdvancedJMXEndpointBuilder
name|reconnectOnConnectionFailure
parameter_list|(
name|boolean
name|reconnectOnConnectionFailure
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"reconnectOnConnectionFailure"
argument_list|,
name|reconnectOnConnectionFailure
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * If true the consumer will attempt to reconnect to the JMX server when          * any connection failure occurs. The consumer will attempt to          * re-establish the JMX connection every 'x' seconds until the          * connection is made-- where 'x' is the configured reconnectionDelay.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|reconnectOnConnectionFailure ( String reconnectOnConnectionFailure)
specifier|default
name|AdvancedJMXEndpointBuilder
name|reconnectOnConnectionFailure
parameter_list|(
name|String
name|reconnectOnConnectionFailure
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"reconnectOnConnectionFailure"
argument_list|,
name|reconnectOnConnectionFailure
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets whether synchronous processing should be strictly used, or Camel          * is allowed to use asynchronous processing (if supported).          *           * The option is a:<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|synchronous (boolean synchronous)
specifier|default
name|AdvancedJMXEndpointBuilder
name|synchronous
parameter_list|(
name|boolean
name|synchronous
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"synchronous"
argument_list|,
name|synchronous
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets whether synchronous processing should be strictly used, or Camel          * is allowed to use asynchronous processing (if supported).          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|synchronous (String synchronous)
specifier|default
name|AdvancedJMXEndpointBuilder
name|synchronous
parameter_list|(
name|String
name|synchronous
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"synchronous"
argument_list|,
name|synchronous
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * If true the consumer will throw an exception if unable to establish          * the JMX connection upon startup. If false, the consumer will attempt          * to establish the JMX connection every 'x' seconds until the          * connection is made -- where 'x' is the configured reconnectionDelay.          *           * The option is a:<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|testConnectionOnStartup ( boolean testConnectionOnStartup)
specifier|default
name|AdvancedJMXEndpointBuilder
name|testConnectionOnStartup
parameter_list|(
name|boolean
name|testConnectionOnStartup
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"testConnectionOnStartup"
argument_list|,
name|testConnectionOnStartup
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * If true the consumer will throw an exception if unable to establish          * the JMX connection upon startup. If false, the consumer will attempt          * to establish the JMX connection every 'x' seconds until the          * connection is made -- where 'x' is the configured reconnectionDelay.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|testConnectionOnStartup ( String testConnectionOnStartup)
specifier|default
name|AdvancedJMXEndpointBuilder
name|testConnectionOnStartup
parameter_list|(
name|String
name|testConnectionOnStartup
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"testConnectionOnStartup"
argument_list|,
name|testConnectionOnStartup
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
comment|/**      * JMX (camel-jmx)      * The jmx component allows to receive JMX notifications.      *       * Syntax:<code>jmx:serverURL</code>      * Category: monitoring      * Available as of version: 2.6      * Maven coordinates: org.apache.camel:camel-jmx      */
DECL|method|jMX (String path)
specifier|default
name|JMXEndpointBuilder
name|jMX
parameter_list|(
name|String
name|path
parameter_list|)
block|{
class|class
name|JMXEndpointBuilderImpl
extends|extends
name|AbstractEndpointBuilder
implements|implements
name|JMXEndpointBuilder
implements|,
name|AdvancedJMXEndpointBuilder
block|{
specifier|public
name|JMXEndpointBuilderImpl
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|super
argument_list|(
literal|"jmx"
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|new
name|JMXEndpointBuilderImpl
argument_list|(
name|path
argument_list|)
return|;
block|}
block|}
end_interface

end_unit

