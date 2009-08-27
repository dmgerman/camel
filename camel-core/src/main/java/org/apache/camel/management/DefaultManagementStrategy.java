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
name|ManagementStrategy
import|;
end_import

begin_import
import|import
name|org
operator|.
name|fusesource
operator|.
name|commons
operator|.
name|management
operator|.
name|Statistic
import|;
end_import

begin_comment
comment|/**  * A default management strategy that does<b>not</b> manage.  *<p/>  * This is default only used if Camel detects that it cannot use the JMX capable  * {@link org.apache.camel.management.ManagedManagementStrategy} strategy. Then Camel will  * fallback to use this instead that is basically a simple and<tt>noop</tt> strategy.  *<p/>  * This class can also be used to extend your custom management implement. In fact the JMX capable  * provided by Camel extends this class as well.  *  * @see ManagedManagementStrategy  * @version $Revision$  */
end_comment

begin_class
DECL|class|DefaultManagementStrategy
specifier|public
class|class
name|DefaultManagementStrategy
implements|implements
name|ManagementStrategy
block|{
DECL|field|eventNotifier
specifier|private
name|EventNotifier
name|eventNotifier
init|=
operator|new
name|DefaultEventNotifier
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
DECL|field|onlyManageProcessorWithCustomId
specifier|private
name|boolean
name|onlyManageProcessorWithCustomId
decl_stmt|;
DECL|field|managementAgent
specifier|private
name|ManagementAgent
name|managementAgent
decl_stmt|;
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
DECL|method|setEventNotifier (EventNotifier eventNotifier)
specifier|public
name|void
name|setEventNotifier
parameter_list|(
name|EventNotifier
name|eventNotifier
parameter_list|)
block|{
name|this
operator|.
name|eventNotifier
operator|=
name|eventNotifier
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
DECL|method|onlyManageProcessorWithCustomId (boolean flag)
specifier|public
name|void
name|onlyManageProcessorWithCustomId
parameter_list|(
name|boolean
name|flag
parameter_list|)
block|{
name|onlyManageProcessorWithCustomId
operator|=
name|flag
expr_stmt|;
block|}
DECL|method|isOnlyManageProcessorWithCustomId ()
specifier|public
name|boolean
name|isOnlyManageProcessorWithCustomId
parameter_list|()
block|{
return|return
name|onlyManageProcessorWithCustomId
return|;
block|}
DECL|method|manageProcessor (ProcessorDefinition definition)
specifier|public
name|boolean
name|manageProcessor
parameter_list|(
name|ProcessorDefinition
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
DECL|method|manageNamedObject (Object managedObject, Object preferedName)
specifier|public
name|void
name|manageNamedObject
parameter_list|(
name|Object
name|managedObject
parameter_list|,
name|Object
name|preferedName
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
name|eventNotifier
operator|!=
literal|null
operator|&&
name|eventNotifier
operator|.
name|isEnabled
argument_list|(
name|event
argument_list|)
condition|)
block|{
name|eventNotifier
operator|.
name|notify
argument_list|(
name|event
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|createStatistic (String name, Object owner, Statistic.UpdateMode updateMode)
specifier|public
name|Statistic
name|createStatistic
parameter_list|(
name|String
name|name
parameter_list|,
name|Object
name|owner
parameter_list|,
name|Statistic
operator|.
name|UpdateMode
name|updateMode
parameter_list|)
block|{
comment|// noop
return|return
literal|null
return|;
block|}
DECL|method|start ()
specifier|public
name|void
name|start
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|managementAgent
operator|!=
literal|null
condition|)
block|{
name|managementAgent
operator|.
name|start
argument_list|()
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
block|}
DECL|method|stop ()
specifier|public
name|void
name|stop
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|managementAgent
operator|!=
literal|null
condition|)
block|{
name|managementAgent
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

