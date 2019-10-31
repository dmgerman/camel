begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.microprofile.metrics.event.notifier
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
name|spi
operator|.
name|CamelEvent
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
name|support
operator|.
name|service
operator|.
name|ServiceHelper
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
name|MetricRegistry
import|;
end_import

begin_class
DECL|class|AbstractMicroProfileMetricsEventNotifier
specifier|public
specifier|abstract
class|class
name|AbstractMicroProfileMetricsEventNotifier
parameter_list|<
name|T
extends|extends
name|CamelEvent
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
DECL|field|metricRegistry
specifier|private
name|MetricRegistry
name|metricRegistry
decl_stmt|;
DECL|method|AbstractMicroProfileMetricsEventNotifier (Class<T> eventType)
specifier|public
name|AbstractMicroProfileMetricsEventNotifier
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|eventType
parameter_list|)
block|{
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
DECL|method|getMetricRegistry ()
specifier|public
name|MetricRegistry
name|getMetricRegistry
parameter_list|()
block|{
return|return
name|metricRegistry
return|;
block|}
DECL|method|setMetricRegistry (MetricRegistry metricRegistry)
specifier|public
name|void
name|setMetricRegistry
parameter_list|(
name|MetricRegistry
name|metricRegistry
parameter_list|)
block|{
name|this
operator|.
name|metricRegistry
operator|=
name|metricRegistry
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|isEnabled (CamelEvent eventObject)
specifier|public
name|boolean
name|isEnabled
parameter_list|(
name|CamelEvent
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
name|metricRegistry
operator|==
literal|null
condition|)
block|{
name|metricRegistry
operator|=
name|MicroProfileMetricsHelper
operator|.
name|getMetricRegistry
argument_list|(
name|camelContext
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|MicroProfileMetricsEventNotifierService
name|service
init|=
name|camelContext
operator|.
name|hasService
argument_list|(
name|MicroProfileMetricsEventNotifierService
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|service
operator|==
literal|null
condition|)
block|{
name|service
operator|=
operator|new
name|MicroProfileMetricsEventNotifierService
argument_list|()
expr_stmt|;
name|service
operator|.
name|setMetricRegistry
argument_list|(
name|getMetricRegistry
argument_list|()
argument_list|)
expr_stmt|;
name|camelContext
operator|.
name|addService
argument_list|(
name|service
argument_list|)
expr_stmt|;
name|ServiceHelper
operator|.
name|startService
argument_list|(
name|service
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
block|}
end_class

end_unit

