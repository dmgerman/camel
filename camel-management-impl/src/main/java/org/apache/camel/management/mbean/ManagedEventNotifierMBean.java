begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.management.mbean
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|management
operator|.
name|mbean
package|;
end_package

begin_interface
DECL|interface|ManagedEventNotifierMBean
specifier|public
interface|interface
name|ManagedEventNotifierMBean
block|{
DECL|method|isIgnoreCamelContextEvents ()
name|boolean
name|isIgnoreCamelContextEvents
parameter_list|()
function_decl|;
DECL|method|setIgnoreCamelContextEvents (boolean ignoreCamelContextEvents)
name|void
name|setIgnoreCamelContextEvents
parameter_list|(
name|boolean
name|ignoreCamelContextEvents
parameter_list|)
function_decl|;
DECL|method|isIgnoreRouteEvents ()
name|boolean
name|isIgnoreRouteEvents
parameter_list|()
function_decl|;
DECL|method|setIgnoreRouteEvents (boolean ignoreRouteEvents)
name|void
name|setIgnoreRouteEvents
parameter_list|(
name|boolean
name|ignoreRouteEvents
parameter_list|)
function_decl|;
DECL|method|isIgnoreServiceEvents ()
name|boolean
name|isIgnoreServiceEvents
parameter_list|()
function_decl|;
DECL|method|setIgnoreServiceEvents (boolean ignoreServiceEvents)
name|void
name|setIgnoreServiceEvents
parameter_list|(
name|boolean
name|ignoreServiceEvents
parameter_list|)
function_decl|;
DECL|method|isIgnoreExchangeEvents ()
name|boolean
name|isIgnoreExchangeEvents
parameter_list|()
function_decl|;
DECL|method|setIgnoreExchangeEvents (boolean ignoreExchangeEvents)
name|void
name|setIgnoreExchangeEvents
parameter_list|(
name|boolean
name|ignoreExchangeEvents
parameter_list|)
function_decl|;
DECL|method|isIgnoreExchangeCreatedEvent ()
name|boolean
name|isIgnoreExchangeCreatedEvent
parameter_list|()
function_decl|;
DECL|method|setIgnoreExchangeCreatedEvent (boolean ignoreExchangeCreatedEvent)
name|void
name|setIgnoreExchangeCreatedEvent
parameter_list|(
name|boolean
name|ignoreExchangeCreatedEvent
parameter_list|)
function_decl|;
DECL|method|isIgnoreExchangeCompletedEvent ()
name|boolean
name|isIgnoreExchangeCompletedEvent
parameter_list|()
function_decl|;
DECL|method|setIgnoreExchangeCompletedEvent (boolean ignoreExchangeCompletedEvent)
name|void
name|setIgnoreExchangeCompletedEvent
parameter_list|(
name|boolean
name|ignoreExchangeCompletedEvent
parameter_list|)
function_decl|;
DECL|method|isIgnoreExchangeFailedEvents ()
name|boolean
name|isIgnoreExchangeFailedEvents
parameter_list|()
function_decl|;
DECL|method|setIgnoreExchangeFailedEvents (boolean ignoreExchangeFailedEvents)
name|void
name|setIgnoreExchangeFailedEvents
parameter_list|(
name|boolean
name|ignoreExchangeFailedEvents
parameter_list|)
function_decl|;
DECL|method|isIgnoreExchangeRedeliveryEvents ()
name|boolean
name|isIgnoreExchangeRedeliveryEvents
parameter_list|()
function_decl|;
DECL|method|setIgnoreExchangeRedeliveryEvents (boolean ignoreExchangeRedeliveryEvents)
name|void
name|setIgnoreExchangeRedeliveryEvents
parameter_list|(
name|boolean
name|ignoreExchangeRedeliveryEvents
parameter_list|)
function_decl|;
DECL|method|isIgnoreExchangeSentEvents ()
name|boolean
name|isIgnoreExchangeSentEvents
parameter_list|()
function_decl|;
DECL|method|setIgnoreExchangeSentEvents (boolean ignoreExchangeSentEvents)
name|void
name|setIgnoreExchangeSentEvents
parameter_list|(
name|boolean
name|ignoreExchangeSentEvents
parameter_list|)
function_decl|;
DECL|method|isIgnoreExchangeSendingEvents ()
name|boolean
name|isIgnoreExchangeSendingEvents
parameter_list|()
function_decl|;
DECL|method|setIgnoreExchangeSendingEvents (boolean ignoreExchangeSendingEvents)
name|void
name|setIgnoreExchangeSendingEvents
parameter_list|(
name|boolean
name|ignoreExchangeSendingEvents
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

