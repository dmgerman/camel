begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.micrometer.messagehistory
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
name|messagehistory
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
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
name|MeterRegistry
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
name|MessageHistory
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
name|NamedNode
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
name|NonManagedService
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
name|RuntimeCamelException
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
name|StaticService
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
name|micrometer
operator|.
name|MicrometerUtils
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
name|MessageHistoryFactory
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
name|METRICS_REGISTRY_NAME
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

begin_comment
comment|/**  * A factory to setup and use {@link MicrometerMessageHistory} as message history implementation.  */
end_comment

begin_class
DECL|class|MicrometerMessageHistoryFactory
specifier|public
class|class
name|MicrometerMessageHistoryFactory
extends|extends
name|ServiceSupport
implements|implements
name|CamelContextAware
implements|,
name|StaticService
implements|,
name|NonManagedService
implements|,
name|MessageHistoryFactory
block|{
DECL|field|camelContext
specifier|private
name|CamelContext
name|camelContext
decl_stmt|;
DECL|field|meterRegistry
specifier|private
name|MeterRegistry
name|meterRegistry
decl_stmt|;
DECL|field|prettyPrint
specifier|private
name|boolean
name|prettyPrint
init|=
literal|true
decl_stmt|;
DECL|field|durationUnit
specifier|private
name|TimeUnit
name|durationUnit
init|=
name|TimeUnit
operator|.
name|MILLISECONDS
decl_stmt|;
DECL|field|namingStrategy
specifier|private
name|MicrometerMessageHistoryNamingStrategy
name|namingStrategy
init|=
name|MicrometerMessageHistoryNamingStrategy
operator|.
name|DEFAULT
decl_stmt|;
annotation|@
name|Override
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
annotation|@
name|Override
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
DECL|method|getMeterRegistry ()
specifier|public
name|MeterRegistry
name|getMeterRegistry
parameter_list|()
block|{
return|return
name|meterRegistry
return|;
block|}
comment|/**      * To use a specific {@link MeterRegistry} instance.      *<p/>      * If no instance has been configured, then Camel will create a shared instance to be used.      */
DECL|method|setMeterRegistry (MeterRegistry meterRegistry)
specifier|public
name|void
name|setMeterRegistry
parameter_list|(
name|MeterRegistry
name|meterRegistry
parameter_list|)
block|{
name|this
operator|.
name|meterRegistry
operator|=
name|meterRegistry
expr_stmt|;
block|}
DECL|method|isPrettyPrint ()
specifier|public
name|boolean
name|isPrettyPrint
parameter_list|()
block|{
return|return
name|prettyPrint
return|;
block|}
comment|/**      * Whether to use pretty print when outputting JSon      */
DECL|method|setPrettyPrint (boolean prettyPrint)
specifier|public
name|void
name|setPrettyPrint
parameter_list|(
name|boolean
name|prettyPrint
parameter_list|)
block|{
name|this
operator|.
name|prettyPrint
operator|=
name|prettyPrint
expr_stmt|;
block|}
DECL|method|getDurationUnit ()
specifier|public
name|TimeUnit
name|getDurationUnit
parameter_list|()
block|{
return|return
name|durationUnit
return|;
block|}
comment|/**      * Sets the time unit to use for timing the duration of processing a message in the route      */
DECL|method|setDurationUnit (TimeUnit durationUnit)
specifier|public
name|void
name|setDurationUnit
parameter_list|(
name|TimeUnit
name|durationUnit
parameter_list|)
block|{
name|this
operator|.
name|durationUnit
operator|=
name|durationUnit
expr_stmt|;
block|}
DECL|method|getNamingStrategy ()
specifier|public
name|MicrometerMessageHistoryNamingStrategy
name|getNamingStrategy
parameter_list|()
block|{
return|return
name|namingStrategy
return|;
block|}
comment|/**      * Sets the naming strategy for message history meter names      */
DECL|method|setNamingStrategy (MicrometerMessageHistoryNamingStrategy namingStrategy)
specifier|public
name|void
name|setNamingStrategy
parameter_list|(
name|MicrometerMessageHistoryNamingStrategy
name|namingStrategy
parameter_list|)
block|{
name|this
operator|.
name|namingStrategy
operator|=
name|namingStrategy
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|newMessageHistory (String routeId, NamedNode namedNode, long timestamp)
specifier|public
name|MessageHistory
name|newMessageHistory
parameter_list|(
name|String
name|routeId
parameter_list|,
name|NamedNode
name|namedNode
parameter_list|,
name|long
name|timestamp
parameter_list|)
block|{
return|return
operator|new
name|MicrometerMessageHistory
argument_list|(
name|getMeterRegistry
argument_list|()
argument_list|,
name|camelContext
operator|.
name|getRoute
argument_list|(
name|routeId
argument_list|)
argument_list|,
name|namedNode
argument_list|,
name|getNamingStrategy
argument_list|()
argument_list|,
name|timestamp
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|meterRegistry
operator|==
literal|null
condition|)
block|{
name|meterRegistry
operator|=
name|MicrometerUtils
operator|.
name|getOrCreateMeterRegistry
argument_list|(
name|camelContext
operator|.
name|getRegistry
argument_list|()
argument_list|,
name|METRICS_REGISTRY_NAME
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|MicrometerMessageHistoryService
name|messageHistoryService
init|=
name|camelContext
operator|.
name|hasService
argument_list|(
name|MicrometerMessageHistoryService
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|messageHistoryService
operator|==
literal|null
condition|)
block|{
name|messageHistoryService
operator|=
operator|new
name|MicrometerMessageHistoryService
argument_list|()
expr_stmt|;
name|messageHistoryService
operator|.
name|setMeterRegistry
argument_list|(
name|getMeterRegistry
argument_list|()
argument_list|)
expr_stmt|;
name|messageHistoryService
operator|.
name|setPrettyPrint
argument_list|(
name|isPrettyPrint
argument_list|()
argument_list|)
expr_stmt|;
name|messageHistoryService
operator|.
name|setDurationUnit
argument_list|(
name|getDurationUnit
argument_list|()
argument_list|)
expr_stmt|;
name|messageHistoryService
operator|.
name|setMatchingTags
argument_list|(
name|Tags
operator|.
name|of
argument_list|(
name|SERVICE_NAME
argument_list|,
name|MicrometerMessageHistoryService
operator|.
name|class
operator|.
name|getSimpleName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|camelContext
operator|.
name|addService
argument_list|(
name|messageHistoryService
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
name|RuntimeCamelException
operator|.
name|wrapRuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
block|{
comment|// noop
block|}
block|}
end_class

end_unit

