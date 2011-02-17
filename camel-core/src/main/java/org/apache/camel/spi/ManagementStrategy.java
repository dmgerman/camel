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
name|org
operator|.
name|fusesource
operator|.
name|commons
operator|.
name|management
operator|.
name|ManagementStrategy
extends|,
name|Service
block|{
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
comment|/**      * Adds the event notifier to use.      *      * @param eventNotifier event notifier      */
DECL|method|addEventNotifier (EventNotifier eventNotifier)
name|void
name|addEventNotifier
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

