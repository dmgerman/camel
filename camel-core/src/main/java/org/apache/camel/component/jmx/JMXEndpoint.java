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
name|javax
operator|.
name|management
operator|.
name|MBeanServer
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
name|ObjectName
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
comment|/**  * JMXEndpoint for monitoring JMX attributs using {@link CounterMonitor}.  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|JMXEndpoint
specifier|public
class|class
name|JMXEndpoint
extends|extends
name|DefaultEndpoint
argument_list|<
name|JMXExchange
argument_list|>
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|JMXEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|name
specifier|private
name|String
name|name
decl_stmt|;
DECL|field|ourName
specifier|private
name|ObjectName
name|ourName
decl_stmt|;
DECL|field|observedObjectName
specifier|private
name|String
name|observedObjectName
decl_stmt|;
DECL|field|attributeName
specifier|private
name|String
name|attributeName
decl_stmt|;
DECL|field|granularityPeriod
specifier|private
name|long
name|granularityPeriod
init|=
literal|5000
decl_stmt|;
DECL|field|threshold
specifier|private
name|Number
name|threshold
decl_stmt|;
DECL|field|offset
specifier|private
name|Number
name|offset
decl_stmt|;
DECL|field|mbeanServer
specifier|private
name|MBeanServer
name|mbeanServer
decl_stmt|;
DECL|field|counterMonitor
specifier|private
name|CounterMonitor
name|counterMonitor
init|=
operator|new
name|CounterMonitor
argument_list|()
decl_stmt|;
DECL|method|JMXEndpoint (String endpointUri, JMXComponent component)
specifier|protected
name|JMXEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|JMXComponent
name|component
parameter_list|)
block|{
name|super
argument_list|(
name|endpointUri
argument_list|,
name|component
argument_list|)
expr_stmt|;
name|observedObjectName
operator|=
name|endpointUri
expr_stmt|;
block|}
DECL|method|JMXEndpoint (String endpointUri)
specifier|public
name|JMXEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|)
block|{
name|super
argument_list|(
name|endpointUri
argument_list|)
expr_stmt|;
block|}
DECL|method|createProducer ()
specifier|public
name|Producer
argument_list|<
name|JMXExchange
argument_list|>
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Producer not supported"
argument_list|)
throw|;
block|}
DECL|method|createConsumer (Processor proc)
specifier|public
name|Consumer
argument_list|<
name|JMXExchange
argument_list|>
name|createConsumer
parameter_list|(
name|Processor
name|proc
parameter_list|)
throws|throws
name|Exception
block|{
name|ObjectName
name|observedName
init|=
operator|new
name|ObjectName
argument_list|(
name|observedObjectName
argument_list|)
decl_stmt|;
if|if
condition|(
name|name
operator|==
literal|null
condition|)
block|{
name|String
name|type
init|=
name|observedName
operator|.
name|getKeyProperty
argument_list|(
literal|"type"
argument_list|)
decl_stmt|;
name|type
operator|=
name|type
operator|!=
literal|null
condition|?
name|type
else|:
literal|"UNKNOWN"
expr_stmt|;
name|name
operator|=
name|mbeanServer
operator|.
name|getDefaultDomain
argument_list|()
operator|+
literal|":type=CounterMonitor_"
operator|+
name|type
expr_stmt|;
block|}
name|JMXConsumer
name|result
init|=
operator|new
name|JMXConsumer
argument_list|(
name|this
argument_list|,
name|proc
argument_list|)
decl_stmt|;
name|ourName
operator|=
operator|new
name|ObjectName
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|counterMonitor
operator|.
name|setNotify
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|counterMonitor
operator|.
name|addObservedObject
argument_list|(
name|observedName
argument_list|)
expr_stmt|;
name|counterMonitor
operator|.
name|setObservedAttribute
argument_list|(
name|attributeName
argument_list|)
expr_stmt|;
name|counterMonitor
operator|.
name|setGranularityPeriod
argument_list|(
name|granularityPeriod
argument_list|)
expr_stmt|;
name|counterMonitor
operator|.
name|setDifferenceMode
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|counterMonitor
operator|.
name|setInitThreshold
argument_list|(
name|threshold
argument_list|)
expr_stmt|;
name|counterMonitor
operator|.
name|setOffset
argument_list|(
name|offset
argument_list|)
expr_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Registering and adding notification listener for ["
operator|+
name|counterMonitor
operator|+
literal|"] with name ["
operator|+
name|ourName
operator|+
literal|"]"
argument_list|)
expr_stmt|;
block|}
name|mbeanServer
operator|.
name|registerMBean
argument_list|(
name|counterMonitor
argument_list|,
name|ourName
argument_list|)
expr_stmt|;
comment|// TODO: How do we remove the listener?
name|mbeanServer
operator|.
name|addNotificationListener
argument_list|(
name|ourName
argument_list|,
name|result
argument_list|,
literal|null
argument_list|,
operator|new
name|Object
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
DECL|method|createExchange (Notification notification)
specifier|public
name|JMXExchange
name|createExchange
parameter_list|(
name|Notification
name|notification
parameter_list|)
block|{
return|return
operator|new
name|JMXExchange
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
name|getExchangePattern
argument_list|()
argument_list|,
name|notification
argument_list|)
return|;
block|}
DECL|method|createExchange ()
specifier|public
name|Exchange
name|createExchange
parameter_list|()
block|{
return|return
operator|new
name|JMXExchange
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
name|getExchangePattern
argument_list|()
argument_list|,
literal|null
argument_list|)
return|;
block|}
DECL|method|createExchange (ExchangePattern pattern)
specifier|public
name|Exchange
name|createExchange
parameter_list|(
name|ExchangePattern
name|pattern
parameter_list|)
block|{
return|return
operator|new
name|JMXExchange
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
name|pattern
argument_list|,
literal|null
argument_list|)
return|;
block|}
DECL|method|getAttributeName ()
specifier|public
name|String
name|getAttributeName
parameter_list|()
block|{
return|return
name|attributeName
return|;
block|}
DECL|method|setAttributeName (String attributeName)
specifier|public
name|void
name|setAttributeName
parameter_list|(
name|String
name|attributeName
parameter_list|)
block|{
name|this
operator|.
name|attributeName
operator|=
name|attributeName
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
DECL|method|setGranularityPeriod (long granularityPeriod)
specifier|public
name|void
name|setGranularityPeriod
parameter_list|(
name|long
name|granularityPeriod
parameter_list|)
block|{
name|this
operator|.
name|granularityPeriod
operator|=
name|granularityPeriod
expr_stmt|;
block|}
DECL|method|getName ()
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
DECL|method|setName (String name)
specifier|public
name|void
name|setName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
DECL|method|getOffset ()
specifier|public
name|Number
name|getOffset
parameter_list|()
block|{
return|return
name|offset
return|;
block|}
DECL|method|setOffset (Number offset)
specifier|public
name|void
name|setOffset
parameter_list|(
name|Number
name|offset
parameter_list|)
block|{
name|this
operator|.
name|offset
operator|=
name|offset
expr_stmt|;
block|}
DECL|method|getThreshold ()
specifier|public
name|Number
name|getThreshold
parameter_list|()
block|{
return|return
name|threshold
return|;
block|}
DECL|method|setThreshold (Number threshold)
specifier|public
name|void
name|setThreshold
parameter_list|(
name|Number
name|threshold
parameter_list|)
block|{
name|this
operator|.
name|threshold
operator|=
name|threshold
expr_stmt|;
block|}
DECL|method|getMbeanServer ()
specifier|public
name|MBeanServer
name|getMbeanServer
parameter_list|()
block|{
return|return
name|mbeanServer
return|;
block|}
DECL|method|setMbeanServer (MBeanServer mbeanServer)
specifier|public
name|void
name|setMbeanServer
parameter_list|(
name|MBeanServer
name|mbeanServer
parameter_list|)
block|{
name|this
operator|.
name|mbeanServer
operator|=
name|mbeanServer
expr_stmt|;
block|}
block|}
end_class

end_unit

