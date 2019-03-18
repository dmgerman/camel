begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.reactive.streams
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|reactive
operator|.
name|streams
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
name|reactive
operator|.
name|streams
operator|.
name|api
operator|.
name|CamelReactiveStreamsService
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
name|reactive
operator|.
name|streams
operator|.
name|engine
operator|.
name|ReactiveStreamsEngineConfiguration
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
name|Metadata
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
name|annotations
operator|.
name|Component
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
name|DefaultComponent
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

begin_comment
comment|/**  * The Camel reactive-streams component.  */
end_comment

begin_class
annotation|@
name|Component
argument_list|(
literal|"reactive-streams"
argument_list|)
DECL|class|ReactiveStreamsComponent
specifier|public
class|class
name|ReactiveStreamsComponent
extends|extends
name|DefaultComponent
block|{
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|internalEngineConfiguration
specifier|private
name|ReactiveStreamsEngineConfiguration
name|internalEngineConfiguration
init|=
operator|new
name|ReactiveStreamsEngineConfiguration
argument_list|()
decl_stmt|;
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|,
name|defaultValue
operator|=
literal|"BUFFER"
argument_list|)
DECL|field|backpressureStrategy
specifier|private
name|ReactiveStreamsBackpressureStrategy
name|backpressureStrategy
init|=
name|ReactiveStreamsBackpressureStrategy
operator|.
name|BUFFER
decl_stmt|;
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|serviceType
specifier|private
name|String
name|serviceType
decl_stmt|;
DECL|field|service
specifier|private
name|CamelReactiveStreamsService
name|service
decl_stmt|;
DECL|method|ReactiveStreamsComponent ()
specifier|public
name|ReactiveStreamsComponent
parameter_list|()
block|{     }
comment|// ****************************************
comment|// Lifecycle/Implementation
comment|// ****************************************
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
comment|// force creation of ReactiveStreamsService
name|getReactiveStreamsService
argument_list|()
expr_stmt|;
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
name|ServiceHelper
operator|.
name|stopService
argument_list|(
name|service
argument_list|)
expr_stmt|;
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createEndpoint (String uri, String remaining, Map<String, Object> parameters)
specifier|protected
name|Endpoint
name|createEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|remaining
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
throws|throws
name|Exception
block|{
name|ReactiveStreamsEndpoint
name|endpoint
init|=
operator|new
name|ReactiveStreamsEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|)
decl_stmt|;
name|endpoint
operator|.
name|setStream
argument_list|(
name|remaining
argument_list|)
expr_stmt|;
name|setProperties
argument_list|(
name|endpoint
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
if|if
condition|(
name|endpoint
operator|.
name|getBackpressureStrategy
argument_list|()
operator|==
literal|null
condition|)
block|{
name|endpoint
operator|.
name|setBackpressureStrategy
argument_list|(
name|this
operator|.
name|backpressureStrategy
argument_list|)
expr_stmt|;
block|}
return|return
name|endpoint
return|;
block|}
comment|// ****************************************
comment|// Properties
comment|// ****************************************
DECL|method|getInternalEngineConfiguration ()
specifier|public
name|ReactiveStreamsEngineConfiguration
name|getInternalEngineConfiguration
parameter_list|()
block|{
return|return
name|internalEngineConfiguration
return|;
block|}
comment|/**      * Configures the internal engine for Reactive Streams.      */
DECL|method|setInternalEngineConfiguration (ReactiveStreamsEngineConfiguration internalEngineConfiguration)
specifier|public
name|void
name|setInternalEngineConfiguration
parameter_list|(
name|ReactiveStreamsEngineConfiguration
name|internalEngineConfiguration
parameter_list|)
block|{
name|this
operator|.
name|internalEngineConfiguration
operator|=
name|internalEngineConfiguration
expr_stmt|;
block|}
DECL|method|getBackpressureStrategy ()
specifier|public
name|ReactiveStreamsBackpressureStrategy
name|getBackpressureStrategy
parameter_list|()
block|{
return|return
name|backpressureStrategy
return|;
block|}
comment|/**      * The backpressure strategy to use when pushing events to a slow subscriber.      */
DECL|method|setBackpressureStrategy (ReactiveStreamsBackpressureStrategy backpressureStrategy)
specifier|public
name|void
name|setBackpressureStrategy
parameter_list|(
name|ReactiveStreamsBackpressureStrategy
name|backpressureStrategy
parameter_list|)
block|{
name|this
operator|.
name|backpressureStrategy
operator|=
name|backpressureStrategy
expr_stmt|;
block|}
DECL|method|getServiceType ()
specifier|public
name|String
name|getServiceType
parameter_list|()
block|{
return|return
name|serviceType
return|;
block|}
comment|/**      * Set the type of the underlying reactive streams implementation to use. The      * implementation is looked up from the registry or using a ServiceLoader, the      * default implementation is DefaultCamelReactiveStreamsService      *      * @param serviceType the reactive service implementation name type      */
DECL|method|setServiceType (String serviceType)
specifier|public
name|void
name|setServiceType
parameter_list|(
name|String
name|serviceType
parameter_list|)
block|{
name|this
operator|.
name|serviceType
operator|=
name|serviceType
expr_stmt|;
block|}
comment|/**      * Lazy creation of the CamelReactiveStreamsService      *      * @return the reactive streams service      */
DECL|method|getReactiveStreamsService ()
specifier|public
specifier|synchronized
name|CamelReactiveStreamsService
name|getReactiveStreamsService
parameter_list|()
block|{
if|if
condition|(
name|service
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|service
operator|=
name|ReactiveStreamsHelper
operator|.
name|resolveReactiveStreamsService
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
name|this
operator|.
name|serviceType
argument_list|,
name|this
operator|.
name|internalEngineConfiguration
argument_list|)
expr_stmt|;
try|try
block|{
comment|// Start the service and add it to the Camel context to expose managed attributes
name|getCamelContext
argument_list|()
operator|.
name|addService
argument_list|(
name|service
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
return|return
name|service
return|;
block|}
comment|// ****************************************
comment|// Helpers
comment|// ****************************************
DECL|method|withServiceType (String serviceType)
specifier|public
specifier|static
specifier|final
name|ReactiveStreamsComponent
name|withServiceType
parameter_list|(
name|String
name|serviceType
parameter_list|)
block|{
name|ReactiveStreamsComponent
name|component
init|=
operator|new
name|ReactiveStreamsComponent
argument_list|()
decl_stmt|;
name|component
operator|.
name|setServiceType
argument_list|(
name|serviceType
argument_list|)
expr_stmt|;
return|return
name|component
return|;
block|}
block|}
end_class

end_unit

