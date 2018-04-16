begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.management
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|management
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
name|CamelContextAware
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
name|ManagementStatisticsLevel
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
name|event
operator|.
name|DefaultEventFactory
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
name|model
operator|.
name|ProcessorDefinition
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
name|camel
operator|.
name|spi
operator|.
name|ManagementAgent
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
name|ManagementNamingStrategy
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
name|ManagementObjectStrategy
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
name|ServiceSupport
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
name|ServiceHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_comment
comment|/**  * A default management strategy that does<b>not</b> manage.  *<p/>  * This is default only used if Camel detects that it cannot use the JMX capable  * {@link org.apache.camel.management.ManagedManagementStrategy} strategy. Then Camel will  * fallback to use this instead that is basically a simple and<tt>noop</tt> strategy.  *<p/>  * This class can also be used to extend your custom management implement. In fact the JMX capable  * provided by Camel extends this class as well.  *  * @see ManagedManagementStrategy  * @version   */
end_comment

begin_class
DECL|class|DefaultManagementStrategy
specifier|public
class|class
name|DefaultManagementStrategy
extends|extends
name|ServiceSupport
implements|implements
name|ManagementStrategy
implements|,
name|CamelContextAware
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|DefaultManagementStrategy
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|eventNotifiers
specifier|private
name|List
argument_list|<
name|EventNotifier
argument_list|>
name|eventNotifiers
init|=
operator|new
name|CopyOnWriteArrayList
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|eventFactory
specifier|private
name|EventFactory
name|eventFactory
init|=
operator|new
name|DefaultEventFactory
argument_list|()
decl_stmt|;
DECL|field|managementNamingStrategy
specifier|private
name|ManagementNamingStrategy
name|managementNamingStrategy
decl_stmt|;
DECL|field|managementObjectStrategy
specifier|private
name|ManagementObjectStrategy
name|managementObjectStrategy
decl_stmt|;
DECL|field|managementAgent
specifier|private
name|ManagementAgent
name|managementAgent
decl_stmt|;
DECL|field|camelContext
specifier|private
name|CamelContext
name|camelContext
decl_stmt|;
DECL|method|DefaultManagementStrategy ()
specifier|public
name|DefaultManagementStrategy
parameter_list|()
block|{     }
DECL|method|DefaultManagementStrategy (CamelContext camelContext)
specifier|public
name|DefaultManagementStrategy
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|this
operator|.
name|camelContext
operator|=
name|camelContext
expr_stmt|;
block|}
DECL|method|getEventNotifiers ()
specifier|public
name|List
argument_list|<
name|EventNotifier
argument_list|>
name|getEventNotifiers
parameter_list|()
block|{
return|return
name|eventNotifiers
return|;
block|}
DECL|method|addEventNotifier (EventNotifier eventNotifier)
specifier|public
name|void
name|addEventNotifier
parameter_list|(
name|EventNotifier
name|eventNotifier
parameter_list|)
block|{
name|this
operator|.
name|eventNotifiers
operator|.
name|add
argument_list|(
name|eventNotifier
argument_list|)
expr_stmt|;
block|}
DECL|method|removeEventNotifier (EventNotifier eventNotifier)
specifier|public
name|boolean
name|removeEventNotifier
parameter_list|(
name|EventNotifier
name|eventNotifier
parameter_list|)
block|{
return|return
name|eventNotifiers
operator|.
name|remove
argument_list|(
name|eventNotifier
argument_list|)
return|;
block|}
DECL|method|setEventNotifiers (List<EventNotifier> eventNotifiers)
specifier|public
name|void
name|setEventNotifiers
parameter_list|(
name|List
argument_list|<
name|EventNotifier
argument_list|>
name|eventNotifiers
parameter_list|)
block|{
name|this
operator|.
name|eventNotifiers
operator|=
name|eventNotifiers
expr_stmt|;
block|}
DECL|method|getEventFactory ()
specifier|public
name|EventFactory
name|getEventFactory
parameter_list|()
block|{
return|return
name|eventFactory
return|;
block|}
DECL|method|setEventFactory (EventFactory eventFactory)
specifier|public
name|void
name|setEventFactory
parameter_list|(
name|EventFactory
name|eventFactory
parameter_list|)
block|{
name|this
operator|.
name|eventFactory
operator|=
name|eventFactory
expr_stmt|;
block|}
DECL|method|getManagementNamingStrategy ()
specifier|public
name|ManagementNamingStrategy
name|getManagementNamingStrategy
parameter_list|()
block|{
if|if
condition|(
name|managementNamingStrategy
operator|==
literal|null
condition|)
block|{
name|managementNamingStrategy
operator|=
operator|new
name|DefaultManagementNamingStrategy
argument_list|()
expr_stmt|;
block|}
return|return
name|managementNamingStrategy
return|;
block|}
DECL|method|setManagementNamingStrategy (ManagementNamingStrategy managementNamingStrategy)
specifier|public
name|void
name|setManagementNamingStrategy
parameter_list|(
name|ManagementNamingStrategy
name|managementNamingStrategy
parameter_list|)
block|{
name|this
operator|.
name|managementNamingStrategy
operator|=
name|managementNamingStrategy
expr_stmt|;
block|}
DECL|method|getManagementObjectStrategy ()
specifier|public
name|ManagementObjectStrategy
name|getManagementObjectStrategy
parameter_list|()
block|{
if|if
condition|(
name|managementObjectStrategy
operator|==
literal|null
condition|)
block|{
name|managementObjectStrategy
operator|=
operator|new
name|DefaultManagementObjectStrategy
argument_list|()
expr_stmt|;
block|}
return|return
name|managementObjectStrategy
return|;
block|}
DECL|method|setManagementObjectStrategy (ManagementObjectStrategy managementObjectStrategy)
specifier|public
name|void
name|setManagementObjectStrategy
parameter_list|(
name|ManagementObjectStrategy
name|managementObjectStrategy
parameter_list|)
block|{
name|this
operator|.
name|managementObjectStrategy
operator|=
name|managementObjectStrategy
expr_stmt|;
block|}
DECL|method|getManagementAgent ()
specifier|public
name|ManagementAgent
name|getManagementAgent
parameter_list|()
block|{
return|return
name|managementAgent
return|;
block|}
DECL|method|setManagementAgent (ManagementAgent managementAgent)
specifier|public
name|void
name|setManagementAgent
parameter_list|(
name|ManagementAgent
name|managementAgent
parameter_list|)
block|{
name|this
operator|.
name|managementAgent
operator|=
name|managementAgent
expr_stmt|;
block|}
annotation|@
name|Deprecated
DECL|method|onlyManageProcessorWithCustomId (boolean flag)
specifier|public
name|void
name|onlyManageProcessorWithCustomId
parameter_list|(
name|boolean
name|flag
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Using @deprecated option onlyManageProcessorWithCustomId on ManagementStrategy. Configure this on ManagementAgent instead."
argument_list|)
expr_stmt|;
if|if
condition|(
name|managementAgent
operator|!=
literal|null
condition|)
block|{
name|getManagementAgent
argument_list|()
operator|.
name|setOnlyRegisterProcessorWithCustomId
argument_list|(
name|flag
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Not started"
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Deprecated
DECL|method|isOnlyManageProcessorWithCustomId ()
specifier|public
name|boolean
name|isOnlyManageProcessorWithCustomId
parameter_list|()
block|{
if|if
condition|(
name|managementAgent
operator|!=
literal|null
condition|)
block|{
name|boolean
name|only
init|=
name|getManagementAgent
argument_list|()
operator|.
name|getOnlyRegisterProcessorWithCustomId
argument_list|()
operator|!=
literal|null
operator|&&
name|getManagementAgent
argument_list|()
operator|.
name|getOnlyRegisterProcessorWithCustomId
argument_list|()
decl_stmt|;
return|return
name|only
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Not started"
argument_list|)
throw|;
block|}
block|}
DECL|method|manageProcessor (ProcessorDefinition<?> definition)
specifier|public
name|boolean
name|manageProcessor
parameter_list|(
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
name|definition
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
DECL|method|manageObject (Object managedObject)
specifier|public
name|void
name|manageObject
parameter_list|(
name|Object
name|managedObject
parameter_list|)
throws|throws
name|Exception
block|{
comment|// noop
block|}
DECL|method|manageNamedObject (Object managedObject, Object preferredName)
specifier|public
name|void
name|manageNamedObject
parameter_list|(
name|Object
name|managedObject
parameter_list|,
name|Object
name|preferredName
parameter_list|)
throws|throws
name|Exception
block|{
comment|// noop
block|}
DECL|method|getManagedObjectName (Object managedObject, String customName, Class<T> nameType)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|getManagedObjectName
parameter_list|(
name|Object
name|managedObject
parameter_list|,
name|String
name|customName
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|nameType
parameter_list|)
throws|throws
name|Exception
block|{
comment|// noop
return|return
literal|null
return|;
block|}
DECL|method|unmanageObject (Object managedObject)
specifier|public
name|void
name|unmanageObject
parameter_list|(
name|Object
name|managedObject
parameter_list|)
throws|throws
name|Exception
block|{
comment|// noop
block|}
DECL|method|unmanageNamedObject (Object name)
specifier|public
name|void
name|unmanageNamedObject
parameter_list|(
name|Object
name|name
parameter_list|)
throws|throws
name|Exception
block|{
comment|// noop
block|}
DECL|method|isManaged (Object managedObject, Object name)
specifier|public
name|boolean
name|isManaged
parameter_list|(
name|Object
name|managedObject
parameter_list|,
name|Object
name|name
parameter_list|)
block|{
comment|// noop
return|return
literal|false
return|;
block|}
DECL|method|getCamelContext ()
specifier|public
name|CamelContext
name|getCamelContext
parameter_list|()
block|{
return|return
name|camelContext
return|;
block|}
DECL|method|setCamelContext (CamelContext camelContext)
specifier|public
name|void
name|setCamelContext
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|this
operator|.
name|camelContext
operator|=
name|camelContext
expr_stmt|;
block|}
DECL|method|notify (EventObject event)
specifier|public
name|void
name|notify
parameter_list|(
name|EventObject
name|event
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|eventNotifiers
operator|!=
literal|null
operator|&&
operator|!
name|eventNotifiers
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
for|for
control|(
name|EventNotifier
name|notifier
range|:
name|eventNotifiers
control|)
block|{
if|if
condition|(
name|notifier
operator|.
name|isEnabled
argument_list|(
name|event
argument_list|)
condition|)
block|{
name|notifier
operator|.
name|notify
argument_list|(
name|event
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
annotation|@
name|Deprecated
DECL|method|setStatisticsLevel (ManagementStatisticsLevel level)
specifier|public
name|void
name|setStatisticsLevel
parameter_list|(
name|ManagementStatisticsLevel
name|level
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Using @deprecated option statisticsLevel on ManagementStrategy. Configure this on ManagementAgent instead."
argument_list|)
expr_stmt|;
if|if
condition|(
name|managementAgent
operator|!=
literal|null
condition|)
block|{
name|getManagementAgent
argument_list|()
operator|.
name|setStatisticsLevel
argument_list|(
name|level
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Not started"
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Deprecated
DECL|method|getStatisticsLevel ()
specifier|public
name|ManagementStatisticsLevel
name|getStatisticsLevel
parameter_list|()
block|{
if|if
condition|(
name|managementAgent
operator|!=
literal|null
condition|)
block|{
return|return
name|getManagementAgent
argument_list|()
operator|.
name|getStatisticsLevel
argument_list|()
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Not started"
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Deprecated
DECL|method|isLoadStatisticsEnabled ()
specifier|public
name|boolean
name|isLoadStatisticsEnabled
parameter_list|()
block|{
if|if
condition|(
name|managementAgent
operator|!=
literal|null
condition|)
block|{
name|boolean
name|load
init|=
name|getManagementAgent
argument_list|()
operator|.
name|getLoadStatisticsEnabled
argument_list|()
operator|!=
literal|null
operator|&&
name|getManagementAgent
argument_list|()
operator|.
name|getLoadStatisticsEnabled
argument_list|()
decl_stmt|;
return|return
name|load
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Not started"
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Deprecated
DECL|method|setLoadStatisticsEnabled (boolean loadStatisticsEnabled)
specifier|public
name|void
name|setLoadStatisticsEnabled
parameter_list|(
name|boolean
name|loadStatisticsEnabled
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Using @deprecated option loadStatisticsEnabled on ManagementStrategy. Configure this on ManagementAgent instead."
argument_list|)
expr_stmt|;
if|if
condition|(
name|managementAgent
operator|!=
literal|null
condition|)
block|{
name|getManagementAgent
argument_list|()
operator|.
name|setLoadStatisticsEnabled
argument_list|(
name|loadStatisticsEnabled
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Not started"
argument_list|)
throw|;
block|}
block|}
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"JMX is disabled"
argument_list|)
expr_stmt|;
name|doStartManagementStrategy
argument_list|()
expr_stmt|;
block|}
DECL|method|doStartManagementStrategy ()
specifier|protected
name|void
name|doStartManagementStrategy
parameter_list|()
throws|throws
name|Exception
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|camelContext
argument_list|,
literal|"CamelContext"
argument_list|)
expr_stmt|;
if|if
condition|(
name|eventNotifiers
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|EventNotifier
name|notifier
range|:
name|eventNotifiers
control|)
block|{
comment|// inject CamelContext if the service is aware
if|if
condition|(
name|notifier
operator|instanceof
name|CamelContextAware
condition|)
block|{
name|CamelContextAware
name|aware
init|=
operator|(
name|CamelContextAware
operator|)
name|notifier
decl_stmt|;
name|aware
operator|.
name|setCamelContext
argument_list|(
name|camelContext
argument_list|)
expr_stmt|;
block|}
name|ServiceHelper
operator|.
name|startService
argument_list|(
name|notifier
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|managementAgent
operator|!=
literal|null
condition|)
block|{
name|ServiceHelper
operator|.
name|startService
argument_list|(
name|managementAgent
argument_list|)
expr_stmt|;
comment|// set the naming strategy using the domain name from the agent
if|if
condition|(
name|managementNamingStrategy
operator|==
literal|null
condition|)
block|{
name|setManagementNamingStrategy
argument_list|(
operator|new
name|DefaultManagementNamingStrategy
argument_list|(
name|managementAgent
operator|.
name|getMBeanObjectDomainName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|managementNamingStrategy
operator|instanceof
name|CamelContextAware
condition|)
block|{
operator|(
operator|(
name|CamelContextAware
operator|)
name|managementNamingStrategy
operator|)
operator|.
name|setCamelContext
argument_list|(
name|getCamelContext
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
name|ServiceHelper
operator|.
name|stopServices
argument_list|(
name|managementAgent
argument_list|,
name|eventNotifiers
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

