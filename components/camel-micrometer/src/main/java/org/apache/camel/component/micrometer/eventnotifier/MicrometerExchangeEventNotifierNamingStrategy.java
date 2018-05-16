begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.micrometer.eventnotifier
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|micrometer
operator|.
name|eventnotifier
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|Predicate
import|;
end_import

begin_import
import|import
name|io
operator|.
name|micrometer
operator|.
name|core
operator|.
name|instrument
operator|.
name|Meter
import|;
end_import

begin_import
import|import
name|io
operator|.
name|micrometer
operator|.
name|core
operator|.
name|instrument
operator|.
name|Tags
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
name|Endpoint
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
name|management
operator|.
name|event
operator|.
name|AbstractExchangeEvent
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|micrometer
operator|.
name|MicrometerConstants
operator|.
name|CAMEL_CONTEXT_TAG
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|micrometer
operator|.
name|MicrometerConstants
operator|.
name|DEFAULT_CAMEL_EXCHANGE_EVENT_METER_NAME
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|micrometer
operator|.
name|MicrometerConstants
operator|.
name|ENDPOINT_NAME
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|micrometer
operator|.
name|MicrometerConstants
operator|.
name|EVENT_TYPE_TAG
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|micrometer
operator|.
name|MicrometerConstants
operator|.
name|FAILED_TAG
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|micrometer
operator|.
name|MicrometerConstants
operator|.
name|SERVICE_NAME
import|;
end_import

begin_interface
DECL|interface|MicrometerExchangeEventNotifierNamingStrategy
specifier|public
interface|interface
name|MicrometerExchangeEventNotifierNamingStrategy
block|{
DECL|field|EVENT_NOTIFIERS
name|Predicate
argument_list|<
name|Meter
operator|.
name|Id
argument_list|>
name|EVENT_NOTIFIERS
init|=
name|id
lambda|->
name|MicrometerEventNotifierService
operator|.
name|class
operator|.
name|getSimpleName
argument_list|()
operator|.
name|equals
argument_list|(
name|id
operator|.
name|getTag
argument_list|(
name|SERVICE_NAME
argument_list|)
argument_list|)
decl_stmt|;
DECL|field|DEFAULT
name|MicrometerExchangeEventNotifierNamingStrategy
name|DEFAULT
init|=
parameter_list|(
name|event
parameter_list|,
name|endpoint
parameter_list|)
lambda|->
name|DEFAULT_CAMEL_EXCHANGE_EVENT_METER_NAME
decl_stmt|;
DECL|method|getName (Exchange exchange, Endpoint endpoint)
name|String
name|getName
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Endpoint
name|endpoint
parameter_list|)
function_decl|;
DECL|method|getTags (AbstractExchangeEvent event, Endpoint endpoint)
specifier|default
name|Tags
name|getTags
parameter_list|(
name|AbstractExchangeEvent
name|event
parameter_list|,
name|Endpoint
name|endpoint
parameter_list|)
block|{
return|return
name|Tags
operator|.
name|of
argument_list|(
name|CAMEL_CONTEXT_TAG
argument_list|,
name|event
operator|.
name|getExchange
argument_list|()
operator|.
name|getContext
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|SERVICE_NAME
argument_list|,
name|MicrometerEventNotifierService
operator|.
name|class
operator|.
name|getSimpleName
argument_list|()
argument_list|,
name|EVENT_TYPE_TAG
argument_list|,
name|event
operator|.
name|getClass
argument_list|()
operator|.
name|getSimpleName
argument_list|()
argument_list|,
name|ENDPOINT_NAME
argument_list|,
name|endpoint
operator|.
name|getEndpointUri
argument_list|()
argument_list|,
name|FAILED_TAG
argument_list|,
name|Boolean
operator|.
name|toString
argument_list|(
name|event
operator|.
name|getExchange
argument_list|()
operator|.
name|isFailed
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
block|}
end_interface

end_unit

