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
name|EventObject
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
name|support
operator|.
name|EventNotifierSupport
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

begin_class
DECL|class|AbstractMicrometerEventNotifier
specifier|public
specifier|abstract
class|class
name|AbstractMicrometerEventNotifier
parameter_list|<
name|T
extends|extends
name|EventObject
parameter_list|>
extends|extends
name|EventNotifierSupport
implements|implements
name|CamelContextAware
block|{
DECL|field|eventType
specifier|private
specifier|final
name|Class
argument_list|<
name|T
argument_list|>
name|eventType
decl_stmt|;
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
DECL|method|AbstractMicrometerEventNotifier (Class<T> eventType)
specifier|public
name|AbstractMicrometerEventNotifier
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|eventType
parameter_list|)
block|{
name|super
argument_list|()
expr_stmt|;
name|this
operator|.
name|eventType
operator|=
name|eventType
expr_stmt|;
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
annotation|@
name|Override
DECL|method|isEnabled (EventObject eventObject)
specifier|public
name|boolean
name|isEnabled
parameter_list|(
name|EventObject
name|eventObject
parameter_list|)
block|{
return|return
name|eventType
operator|.
name|isAssignableFrom
argument_list|(
name|eventObject
operator|.
name|getClass
argument_list|()
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
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
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
name|MicrometerEventNotifierService
name|registryService
init|=
name|camelContext
operator|.
name|hasService
argument_list|(
name|MicrometerEventNotifierService
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|registryService
operator|==
literal|null
condition|)
block|{
name|registryService
operator|=
operator|new
name|MicrometerEventNotifierService
argument_list|()
expr_stmt|;
name|registryService
operator|.
name|setMeterRegistry
argument_list|(
name|getMeterRegistry
argument_list|()
argument_list|)
expr_stmt|;
name|registryService
operator|.
name|setPrettyPrint
argument_list|(
name|isPrettyPrint
argument_list|()
argument_list|)
expr_stmt|;
name|registryService
operator|.
name|setDurationUnit
argument_list|(
name|getDurationUnit
argument_list|()
argument_list|)
expr_stmt|;
name|registryService
operator|.
name|setMatchingTags
argument_list|(
name|Tags
operator|.
name|of
argument_list|(
name|SERVICE_NAME
argument_list|,
name|registryService
operator|.
name|getClass
argument_list|()
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
name|registryService
argument_list|)
expr_stmt|;
comment|// ensure registry service is started
name|ServiceHelper
operator|.
name|startService
argument_list|(
name|registryService
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
name|ObjectHelper
operator|.
name|wrapRuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
try|try
block|{          }
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
name|ObjectHelper
operator|.
name|wrapRuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

