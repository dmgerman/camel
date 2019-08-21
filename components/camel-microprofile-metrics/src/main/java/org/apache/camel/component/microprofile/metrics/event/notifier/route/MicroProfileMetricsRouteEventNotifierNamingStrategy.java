begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.microprofile.metrics.event.notifier.route
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|microprofile
operator|.
name|metrics
operator|.
name|event
operator|.
name|notifier
operator|.
name|route
package|;
end_package

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
name|component
operator|.
name|microprofile
operator|.
name|metrics
operator|.
name|MicroProfileMetricsHelper
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
name|component
operator|.
name|microprofile
operator|.
name|metrics
operator|.
name|event
operator|.
name|notifier
operator|.
name|MicroProfileMetricsEventNotifierService
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
name|CamelEvent
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|microprofile
operator|.
name|metrics
operator|.
name|Tag
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
name|microprofile
operator|.
name|metrics
operator|.
name|MicroProfileMetricsConstants
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
name|microprofile
operator|.
name|metrics
operator|.
name|MicroProfileMetricsConstants
operator|.
name|DEFAULT_CAMEL_ROUTES_ADDED_METRIC_NAME
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
name|microprofile
operator|.
name|metrics
operator|.
name|MicroProfileMetricsConstants
operator|.
name|DEFAULT_CAMEL_ROUTES_RUNNING_METRIC_NAME
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
name|microprofile
operator|.
name|metrics
operator|.
name|MicroProfileMetricsConstants
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
name|microprofile
operator|.
name|metrics
operator|.
name|MicroProfileMetricsConstants
operator|.
name|SERVICE_NAME
import|;
end_import

begin_interface
DECL|interface|MicroProfileMetricsRouteEventNotifierNamingStrategy
specifier|public
interface|interface
name|MicroProfileMetricsRouteEventNotifierNamingStrategy
block|{
DECL|field|DEFAULT
name|MicroProfileMetricsRouteEventNotifierNamingStrategy
name|DEFAULT
init|=
operator|new
name|MicroProfileMetricsRouteEventNotifierNamingStrategy
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|String
name|getRouteAddedName
parameter_list|()
block|{
return|return
name|DEFAULT_CAMEL_ROUTES_ADDED_METRIC_NAME
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getRouteRunningName
parameter_list|()
block|{
return|return
name|DEFAULT_CAMEL_ROUTES_RUNNING_METRIC_NAME
return|;
block|}
block|}
decl_stmt|;
DECL|method|getRouteAddedName ()
name|String
name|getRouteAddedName
parameter_list|()
function_decl|;
DECL|method|getRouteRunningName ()
name|String
name|getRouteRunningName
parameter_list|()
function_decl|;
DECL|method|getTags (CamelContext camelContext)
specifier|default
name|Tag
index|[]
name|getTags
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|String
index|[]
name|tags
init|=
block|{
name|SERVICE_NAME
operator|+
literal|"="
operator|+
name|MicroProfileMetricsEventNotifierService
operator|.
name|class
operator|.
name|getSimpleName
argument_list|()
block|,
name|CAMEL_CONTEXT_TAG
operator|+
literal|"="
operator|+
name|camelContext
operator|.
name|getName
argument_list|()
block|,
name|EVENT_TYPE_TAG
operator|+
literal|"="
operator|+
name|CamelEvent
operator|.
name|RouteEvent
operator|.
name|class
operator|.
name|getSimpleName
argument_list|()
block|,         }
decl_stmt|;
return|return
name|MicroProfileMetricsHelper
operator|.
name|parseTagArray
argument_list|(
name|tags
argument_list|)
return|;
block|}
block|}
end_interface

end_unit

