begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spi
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
name|Service
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

begin_comment
comment|/**  * Strategy for management.  *<p/>  * This is totally pluggable allowing to use a custom or 3rd party management implementation with Camel.  *  * @see org.apache.camel.spi.EventNotifier  * @see org.apache.camel.spi.EventFactory  * @see org.apache.camel.spi.ManagementNamingStrategy  * @see org.apache.camel.spi.ManagementAgent  * @version   */
end_comment

begin_interface
DECL|interface|ManagementStrategy
specifier|public
interface|interface
name|ManagementStrategy
extends|extends
name|Service
block|{
comment|/**      * Adds a managed object allowing the ManagementStrategy implementation to record or expose      * the object as it sees fit.      *      * @param managedObject the managed object      * @throws Exception can be thrown if the object could not be added      */
DECL|method|manageObject (Object managedObject)
name|void
name|manageObject
parameter_list|(
name|Object
name|managedObject
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Adds a managed object allowing the ManagementStrategy implementation      * to record or expose the object as it sees fit.      *      * @param managedObject the managed object      * @param preferredName representing the preferred name, maybe a String, or a JMX ObjectName      * @throws Exception can be thrown if the object could not be added      */
DECL|method|manageNamedObject (Object managedObject, Object preferredName)
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
function_decl|;
comment|/**      * Construct an object name, where either the object to be managed and/or      * a custom name component are provided      *      * @param managedObject the object to be managed      * @param customName a custom name component      * @param nameType the name type required      * @return an object name of the required type if supported, otherwise<tt>null</tt>      * @throws Exception can be thrown if the object name could not be created      */
DECL|method|getManagedObjectName (Object managedObject, String customName, Class<T> nameType)
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
function_decl|;
comment|/**      * Removes the managed object.      *      * @param managedObject the managed object      * @throws Exception can be thrown if the object could not be removed      */
DECL|method|unmanageObject (Object managedObject)
name|void
name|unmanageObject
parameter_list|(
name|Object
name|managedObject
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Removes a managed object by name.      *      * @param name an object name previously created by this strategy.      * @throws Exception can be thrown if the object could not be removed      */
DECL|method|unmanageNamedObject (Object name)
name|void
name|unmanageNamedObject
parameter_list|(
name|Object
name|name
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Determines if an object or name is managed.      *      * @param managedObject the object to consider      * @param name the name to consider      * @return<tt>true</tt> if the given object or name is managed      */
DECL|method|isManaged (Object managedObject, Object name)
name|boolean
name|isManaged
parameter_list|(
name|Object
name|managedObject
parameter_list|,
name|Object
name|name
parameter_list|)
function_decl|;
comment|/**      * Management events provide a single model for capturing information about execution points in the      * application code. Management strategy implementations decide if and where to record these events.      * Applications communicate events to management strategy implementations via the notify(EventObject)      * method.      *      * @param event the event      * @throws Exception can be thrown if the notification failed      */
DECL|method|notify (EventObject event)
name|void
name|notify
parameter_list|(
name|EventObject
name|event
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Gets the event notifiers.      *      * @return event notifiers      */
DECL|method|getEventNotifiers ()
name|List
argument_list|<
name|EventNotifier
argument_list|>
name|getEventNotifiers
parameter_list|()
function_decl|;
comment|/**      * Sets the list of event notifier to use.      *      * @param eventNotifier list of event notifiers      */
DECL|method|setEventNotifiers (List<EventNotifier> eventNotifier)
name|void
name|setEventNotifiers
parameter_list|(
name|List
argument_list|<
name|EventNotifier
argument_list|>
name|eventNotifier
parameter_list|)
function_decl|;
comment|/**      * Adds the event notifier to use.      *<p/>      * Ensure the event notifier has been started if its a {@link Service}, as otherwise      * it would not be used.      *      * @param eventNotifier event notifier      */
DECL|method|addEventNotifier (EventNotifier eventNotifier)
name|void
name|addEventNotifier
parameter_list|(
name|EventNotifier
name|eventNotifier
parameter_list|)
function_decl|;
comment|/**      * Removes the event notifier      *      * @param eventNotifier event notifier to remove      * @return<tt>true</tt> if removed,<tt>false</tt> if already removed      */
DECL|method|removeEventNotifier (EventNotifier eventNotifier)
name|boolean
name|removeEventNotifier
parameter_list|(
name|EventNotifier
name|eventNotifier
parameter_list|)
function_decl|;
comment|/**      * Gets the event factory      *      * @return event factory      */
DECL|method|getEventFactory ()
name|EventFactory
name|getEventFactory
parameter_list|()
function_decl|;
comment|/**      * Sets the event factory to use      *      * @param eventFactory event factory      */
DECL|method|setEventFactory (EventFactory eventFactory)
name|void
name|setEventFactory
parameter_list|(
name|EventFactory
name|eventFactory
parameter_list|)
function_decl|;
comment|/**      * Gets the naming strategy to use      *      * @return naming strategy      */
DECL|method|getManagementNamingStrategy ()
name|ManagementNamingStrategy
name|getManagementNamingStrategy
parameter_list|()
function_decl|;
comment|/**      * Sets the naming strategy to use      *      * @param strategy naming strategy      */
DECL|method|setManagementNamingStrategy (ManagementNamingStrategy strategy)
name|void
name|setManagementNamingStrategy
parameter_list|(
name|ManagementNamingStrategy
name|strategy
parameter_list|)
function_decl|;
comment|/**      * Gets the object strategy to use      *      * @return object strategy      */
DECL|method|getManagementObjectStrategy ()
name|ManagementObjectStrategy
name|getManagementObjectStrategy
parameter_list|()
function_decl|;
comment|/**      * Sets the object strategy to use      *      * @param strategy object strategy      */
DECL|method|setManagementObjectStrategy (ManagementObjectStrategy strategy)
name|void
name|setManagementObjectStrategy
parameter_list|(
name|ManagementObjectStrategy
name|strategy
parameter_list|)
function_decl|;
comment|/**      * Gets the management agent      *      * @return management agent      */
DECL|method|getManagementAgent ()
name|ManagementAgent
name|getManagementAgent
parameter_list|()
function_decl|;
comment|/**      * Sets the management agent to use      *      * @param managementAgent management agent      */
DECL|method|setManagementAgent (ManagementAgent managementAgent)
name|void
name|setManagementAgent
parameter_list|(
name|ManagementAgent
name|managementAgent
parameter_list|)
function_decl|;
comment|/**      * Filter whether the processor should be managed or not.      *<p/>      * Is used to filter out unwanted processors to avoid managing at too fine grained level.      *      * @param definition definition of the processor      * @return<tt>true</tt> to manage it      */
DECL|method|manageProcessor (ProcessorDefinition<?> definition)
name|boolean
name|manageProcessor
parameter_list|(
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
name|definition
parameter_list|)
function_decl|;
comment|/**      * Sets the whether only manage processors if they have been configured with a custom id      *<p/>      * Default is false.      *      * @param flag<tt>true</tt> will only manage if custom id was set.      */
DECL|method|onlyManageProcessorWithCustomId (boolean flag)
name|void
name|onlyManageProcessorWithCustomId
parameter_list|(
name|boolean
name|flag
parameter_list|)
function_decl|;
comment|/**      * Checks whether only to manage processors if they have been configured with a custom id      *      * @return true or false      */
DECL|method|isOnlyManageProcessorWithCustomId ()
name|boolean
name|isOnlyManageProcessorWithCustomId
parameter_list|()
function_decl|;
comment|/**      * Sets whether load statistics is enabled.      *      * @param flag<tt>true</tt> to enable load statistics      */
DECL|method|setLoadStatisticsEnabled (boolean flag)
name|void
name|setLoadStatisticsEnabled
parameter_list|(
name|boolean
name|flag
parameter_list|)
function_decl|;
comment|/**      * Gets whether load statistics is enabled      *      * @return<tt>true</tt> if enabled      */
DECL|method|isLoadStatisticsEnabled ()
name|boolean
name|isLoadStatisticsEnabled
parameter_list|()
function_decl|;
comment|/**      * Sets the statistics level      *<p/>      * Default is {@link org.apache.camel.ManagementStatisticsLevel#All}      *      * @param level the new level      */
DECL|method|setStatisticsLevel (ManagementStatisticsLevel level)
name|void
name|setStatisticsLevel
parameter_list|(
name|ManagementStatisticsLevel
name|level
parameter_list|)
function_decl|;
comment|/**      * Gets the statistics level      *      * @return the level      */
DECL|method|getStatisticsLevel ()
name|ManagementStatisticsLevel
name|getStatisticsLevel
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

