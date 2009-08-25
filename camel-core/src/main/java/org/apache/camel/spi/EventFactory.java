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
name|Route
import|;
end_import

begin_comment
comment|/**  * Factory to create {@link java.util.EventObject} events} that are emitted when such an event occur.  *<p/>  * For example when an {@link Exchange} is being created and then later when its done.  *  * @version $Revision$  */
end_comment

begin_interface
DECL|interface|EventFactory
specifier|public
interface|interface
name|EventFactory
block|{
DECL|method|createCamelContextStartingEvent (CamelContext context)
name|EventObject
name|createCamelContextStartingEvent
parameter_list|(
name|CamelContext
name|context
parameter_list|)
function_decl|;
DECL|method|createCamelContextStartedEvent (CamelContext context)
name|EventObject
name|createCamelContextStartedEvent
parameter_list|(
name|CamelContext
name|context
parameter_list|)
function_decl|;
DECL|method|createCamelContextStoppingEvent (CamelContext context)
name|EventObject
name|createCamelContextStoppingEvent
parameter_list|(
name|CamelContext
name|context
parameter_list|)
function_decl|;
DECL|method|createCamelContextStoppedEvent (CamelContext context)
name|EventObject
name|createCamelContextStoppedEvent
parameter_list|(
name|CamelContext
name|context
parameter_list|)
function_decl|;
DECL|method|createRouteStartEvent (Route route)
name|EventObject
name|createRouteStartEvent
parameter_list|(
name|Route
name|route
parameter_list|)
function_decl|;
DECL|method|createRouteStopEvent (Route route)
name|EventObject
name|createRouteStopEvent
parameter_list|(
name|Route
name|route
parameter_list|)
function_decl|;
DECL|method|createExchangeCreatedEvent (Exchange exchange)
name|EventObject
name|createExchangeCreatedEvent
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
function_decl|;
DECL|method|createExchangeCompletedEvent (Exchange exchange)
name|EventObject
name|createExchangeCompletedEvent
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
function_decl|;
DECL|method|createExchangeFailedEvent (Exchange exchange)
name|EventObject
name|createExchangeFailedEvent
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

