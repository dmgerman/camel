begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|UUID
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|AttributeNotFoundException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|InstanceNotFoundException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|MBeanException
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
name|javax
operator|.
name|management
operator|.
name|ReflectionException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|monitor
operator|.
name|CounterMonitor
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|monitor
operator|.
name|GaugeMonitor
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|monitor
operator|.
name|Monitor
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|monitor
operator|.
name|StringMonitor
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

begin_comment
comment|/**  * Variant of the consumer that creates and registers a monitor bean to   * monitor object and attribute referenced by the endpoint. The only   * difference here is the act of adding and removing the notification  * listener.  */
end_comment

begin_class
DECL|class|JMXMonitorConsumer
specifier|public
class|class
name|JMXMonitorConsumer
extends|extends
name|JMXConsumer
block|{
comment|/** name of our monitor. We keep a reference since it needs to be removed when we stop listening */
DECL|field|mMonitorObjectName
name|ObjectName
name|mMonitorObjectName
decl_stmt|;
DECL|method|JMXMonitorConsumer (JMXEndpoint aEndpoint, Processor aProcessor)
specifier|public
name|JMXMonitorConsumer
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
block|}
annotation|@
name|Override
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
comment|// create the monitor bean
name|Monitor
name|bean
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|ep
operator|.
name|getMonitorType
argument_list|()
operator|.
name|equals
argument_list|(
literal|"counter"
argument_list|)
condition|)
block|{
name|CounterMonitor
name|counter
init|=
operator|new
name|CounterMonitor
argument_list|()
decl_stmt|;
name|Number
name|initThreshold
init|=
name|convertNumberToAttributeType
argument_list|(
name|ep
operator|.
name|getInitThreshold
argument_list|()
argument_list|,
name|ep
operator|.
name|getJMXObjectName
argument_list|()
argument_list|,
name|ep
operator|.
name|getObservedAttribute
argument_list|()
argument_list|)
decl_stmt|;
name|Number
name|offset
init|=
name|convertNumberToAttributeType
argument_list|(
name|ep
operator|.
name|getOffset
argument_list|()
argument_list|,
name|ep
operator|.
name|getJMXObjectName
argument_list|()
argument_list|,
name|ep
operator|.
name|getObservedAttribute
argument_list|()
argument_list|)
decl_stmt|;
name|Number
name|modulus
init|=
name|convertNumberToAttributeType
argument_list|(
name|ep
operator|.
name|getModulus
argument_list|()
argument_list|,
name|ep
operator|.
name|getJMXObjectName
argument_list|()
argument_list|,
name|ep
operator|.
name|getObservedAttribute
argument_list|()
argument_list|)
decl_stmt|;
name|counter
operator|.
name|setInitThreshold
argument_list|(
name|initThreshold
argument_list|)
expr_stmt|;
name|counter
operator|.
name|setOffset
argument_list|(
name|offset
argument_list|)
expr_stmt|;
name|counter
operator|.
name|setModulus
argument_list|(
name|modulus
argument_list|)
expr_stmt|;
name|counter
operator|.
name|setDifferenceMode
argument_list|(
name|ep
operator|.
name|isDifferenceMode
argument_list|()
argument_list|)
expr_stmt|;
name|counter
operator|.
name|setNotify
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|bean
operator|=
name|counter
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|ep
operator|.
name|getMonitorType
argument_list|()
operator|.
name|equals
argument_list|(
literal|"gauge"
argument_list|)
condition|)
block|{
name|GaugeMonitor
name|gm
init|=
operator|new
name|GaugeMonitor
argument_list|()
decl_stmt|;
name|gm
operator|.
name|setNotifyHigh
argument_list|(
name|ep
operator|.
name|isNotifyHigh
argument_list|()
argument_list|)
expr_stmt|;
name|gm
operator|.
name|setNotifyLow
argument_list|(
name|ep
operator|.
name|isNotifyLow
argument_list|()
argument_list|)
expr_stmt|;
name|gm
operator|.
name|setDifferenceMode
argument_list|(
name|ep
operator|.
name|isDifferenceMode
argument_list|()
argument_list|)
expr_stmt|;
name|Number
name|highValue
init|=
name|convertNumberToAttributeType
argument_list|(
name|ep
operator|.
name|getThresholdHigh
argument_list|()
argument_list|,
name|ep
operator|.
name|getJMXObjectName
argument_list|()
argument_list|,
name|ep
operator|.
name|getObservedAttribute
argument_list|()
argument_list|)
decl_stmt|;
name|Number
name|lowValue
init|=
name|convertNumberToAttributeType
argument_list|(
name|ep
operator|.
name|getThresholdLow
argument_list|()
argument_list|,
name|ep
operator|.
name|getJMXObjectName
argument_list|()
argument_list|,
name|ep
operator|.
name|getObservedAttribute
argument_list|()
argument_list|)
decl_stmt|;
name|gm
operator|.
name|setThresholds
argument_list|(
name|highValue
argument_list|,
name|lowValue
argument_list|)
expr_stmt|;
name|bean
operator|=
name|gm
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|ep
operator|.
name|getMonitorType
argument_list|()
operator|.
name|equals
argument_list|(
literal|"string"
argument_list|)
condition|)
block|{
name|StringMonitor
name|sm
init|=
operator|new
name|StringMonitor
argument_list|()
decl_stmt|;
name|sm
operator|.
name|setNotifyDiffer
argument_list|(
name|ep
operator|.
name|isNotifyDiffer
argument_list|()
argument_list|)
expr_stmt|;
name|sm
operator|.
name|setNotifyMatch
argument_list|(
name|ep
operator|.
name|isNotifyMatch
argument_list|()
argument_list|)
expr_stmt|;
name|sm
operator|.
name|setStringToCompare
argument_list|(
name|ep
operator|.
name|getStringToCompare
argument_list|()
argument_list|)
expr_stmt|;
name|bean
operator|=
name|sm
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unsupported monitortype: "
operator|+
name|ep
operator|.
name|getMonitorType
argument_list|()
argument_list|)
throw|;
block|}
name|bean
operator|.
name|addObservedObject
argument_list|(
name|ep
operator|.
name|getJMXObjectName
argument_list|()
argument_list|)
expr_stmt|;
name|bean
operator|.
name|setObservedAttribute
argument_list|(
name|ep
operator|.
name|getObservedAttribute
argument_list|()
argument_list|)
expr_stmt|;
name|bean
operator|.
name|setGranularityPeriod
argument_list|(
name|ep
operator|.
name|getGranularityPeriod
argument_list|()
argument_list|)
expr_stmt|;
comment|// register the bean
name|mMonitorObjectName
operator|=
operator|new
name|ObjectName
argument_list|(
name|ep
operator|.
name|getObjectDomain
argument_list|()
argument_list|,
literal|"name"
argument_list|,
literal|"camel-jmx-monitor-"
operator|+
name|UUID
operator|.
name|randomUUID
argument_list|()
argument_list|)
expr_stmt|;
name|ManagementFactory
operator|.
name|getPlatformMBeanServer
argument_list|()
operator|.
name|registerMBean
argument_list|(
name|bean
argument_list|,
name|mMonitorObjectName
argument_list|)
expr_stmt|;
comment|// add ourselves as a listener to it
name|NotificationFilter
name|nf
init|=
name|ep
operator|.
name|getNotificationFilter
argument_list|()
decl_stmt|;
name|getServerConnection
argument_list|()
operator|.
name|addNotificationListener
argument_list|(
name|mMonitorObjectName
argument_list|,
name|this
argument_list|,
name|nf
argument_list|,
name|bean
argument_list|)
expr_stmt|;
name|bean
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|removeNotificationListeners ()
specifier|protected
name|void
name|removeNotificationListeners
parameter_list|()
throws|throws
name|Exception
block|{
comment|// remove ourselves as a listener
name|ManagementFactory
operator|.
name|getPlatformMBeanServer
argument_list|()
operator|.
name|removeNotificationListener
argument_list|(
name|mMonitorObjectName
argument_list|,
name|this
argument_list|)
expr_stmt|;
comment|// unregister the monitor bean
name|ManagementFactory
operator|.
name|getPlatformMBeanServer
argument_list|()
operator|.
name|unregisterMBean
argument_list|(
name|mMonitorObjectName
argument_list|)
expr_stmt|;
block|}
DECL|method|convertNumberToAttributeType (Number toConvert, ObjectName jmxObjectName, String observedAttribute)
specifier|private
name|Number
name|convertNumberToAttributeType
parameter_list|(
name|Number
name|toConvert
parameter_list|,
name|ObjectName
name|jmxObjectName
parameter_list|,
name|String
name|observedAttribute
parameter_list|)
throws|throws
name|InstanceNotFoundException
throws|,
name|ReflectionException
throws|,
name|AttributeNotFoundException
throws|,
name|MBeanException
block|{
name|Object
name|attr
init|=
name|ManagementFactory
operator|.
name|getPlatformMBeanServer
argument_list|()
operator|.
name|getAttribute
argument_list|(
name|jmxObjectName
argument_list|,
name|observedAttribute
argument_list|)
decl_stmt|;
if|if
condition|(
name|attr
operator|instanceof
name|Byte
condition|)
block|{
return|return
name|toConvert
operator|!=
literal|null
condition|?
name|toConvert
operator|.
name|byteValue
argument_list|()
else|:
literal|null
return|;
block|}
elseif|else
if|if
condition|(
name|attr
operator|instanceof
name|Integer
condition|)
block|{
return|return
name|toConvert
operator|!=
literal|null
condition|?
name|toConvert
operator|.
name|intValue
argument_list|()
else|:
literal|null
return|;
block|}
elseif|else
if|if
condition|(
name|attr
operator|instanceof
name|Short
condition|)
block|{
return|return
name|toConvert
operator|!=
literal|null
condition|?
name|toConvert
operator|.
name|shortValue
argument_list|()
else|:
literal|null
return|;
block|}
elseif|else
if|if
condition|(
name|attr
operator|instanceof
name|Long
condition|)
block|{
return|return
name|toConvert
operator|!=
literal|null
condition|?
name|toConvert
operator|.
name|longValue
argument_list|()
else|:
literal|null
return|;
block|}
elseif|else
if|if
condition|(
name|attr
operator|instanceof
name|Float
condition|)
block|{
return|return
name|toConvert
operator|!=
literal|null
condition|?
name|toConvert
operator|.
name|floatValue
argument_list|()
else|:
literal|null
return|;
block|}
else|else
block|{
return|return
name|toConvert
return|;
block|}
block|}
block|}
end_class

end_unit

