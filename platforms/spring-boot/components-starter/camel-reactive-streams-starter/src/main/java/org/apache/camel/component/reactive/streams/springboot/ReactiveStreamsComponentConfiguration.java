begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.reactive.streams.springboot
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
operator|.
name|springboot
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|Generated
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
name|ReactiveStreamsBackpressureStrategy
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
name|ReactiveStreamsComponent
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
name|spring
operator|.
name|boot
operator|.
name|ComponentConfigurationPropertiesCommon
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|context
operator|.
name|properties
operator|.
name|ConfigurationProperties
import|;
end_import

begin_comment
comment|/**  * Reactive Camel using reactive streams  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_class
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.SpringBootAutoConfigurationMojo"
argument_list|)
annotation|@
name|ConfigurationProperties
argument_list|(
name|prefix
operator|=
literal|"camel.component.reactive-streams"
argument_list|)
DECL|class|ReactiveStreamsComponentConfiguration
specifier|public
class|class
name|ReactiveStreamsComponentConfiguration
extends|extends
name|ComponentConfigurationPropertiesCommon
block|{
comment|/**      * Whether to enable auto configuration of the reactive-streams component.      * This is enabled by default.      */
DECL|field|enabled
specifier|private
name|Boolean
name|enabled
decl_stmt|;
comment|/**      * Configures the internal engine for Reactive Streams.      */
DECL|field|internalEngineConfiguration
specifier|private
name|ReactiveStreamsEngineConfigurationNestedConfiguration
name|internalEngineConfiguration
decl_stmt|;
comment|/**      * The backpressure strategy to use when pushing events to a slow      * subscriber.      */
DECL|field|backpressureStrategy
specifier|private
name|ReactiveStreamsBackpressureStrategy
name|backpressureStrategy
init|=
name|ReactiveStreamsBackpressureStrategy
operator|.
name|BUFFER
decl_stmt|;
comment|/**      * Set the type of the underlying reactive streams implementation to use.      * The implementation is looked up from the registry or using a      * ServiceLoader, the default implementation is      * DefaultCamelReactiveStreamsService      */
DECL|field|serviceType
specifier|private
name|String
name|serviceType
decl_stmt|;
comment|/**      * Whether the component should use basic property binding (Camel 2.x) or      * the newer property binding with additional capabilities      */
DECL|field|basicPropertyBinding
specifier|private
name|Boolean
name|basicPropertyBinding
init|=
literal|false
decl_stmt|;
DECL|method|getInternalEngineConfiguration ()
specifier|public
name|ReactiveStreamsEngineConfigurationNestedConfiguration
name|getInternalEngineConfiguration
parameter_list|()
block|{
return|return
name|internalEngineConfiguration
return|;
block|}
DECL|method|setInternalEngineConfiguration ( ReactiveStreamsEngineConfigurationNestedConfiguration internalEngineConfiguration)
specifier|public
name|void
name|setInternalEngineConfiguration
parameter_list|(
name|ReactiveStreamsEngineConfigurationNestedConfiguration
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
DECL|method|setBackpressureStrategy ( ReactiveStreamsBackpressureStrategy backpressureStrategy)
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
DECL|method|getBasicPropertyBinding ()
specifier|public
name|Boolean
name|getBasicPropertyBinding
parameter_list|()
block|{
return|return
name|basicPropertyBinding
return|;
block|}
DECL|method|setBasicPropertyBinding (Boolean basicPropertyBinding)
specifier|public
name|void
name|setBasicPropertyBinding
parameter_list|(
name|Boolean
name|basicPropertyBinding
parameter_list|)
block|{
name|this
operator|.
name|basicPropertyBinding
operator|=
name|basicPropertyBinding
expr_stmt|;
block|}
DECL|class|ReactiveStreamsEngineConfigurationNestedConfiguration
specifier|public
specifier|static
class|class
name|ReactiveStreamsEngineConfigurationNestedConfiguration
block|{
DECL|field|CAMEL_NESTED_CLASS
specifier|public
specifier|static
specifier|final
name|Class
name|CAMEL_NESTED_CLASS
init|=
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
operator|.
name|class
decl_stmt|;
comment|/**          * The name of the thread pool used by the reactive streams internal          * engine.          */
DECL|field|threadPoolName
specifier|private
name|String
name|threadPoolName
decl_stmt|;
comment|/**          * The minimum number of threads used by the reactive streams internal          * engine.          */
DECL|field|threadPoolMinSize
specifier|private
name|Integer
name|threadPoolMinSize
decl_stmt|;
comment|/**          * The maximum number of threads used by the reactive streams internal          * engine.          */
DECL|field|threadPoolMaxSize
specifier|private
name|Integer
name|threadPoolMaxSize
decl_stmt|;
DECL|method|getThreadPoolName ()
specifier|public
name|String
name|getThreadPoolName
parameter_list|()
block|{
return|return
name|threadPoolName
return|;
block|}
DECL|method|setThreadPoolName (String threadPoolName)
specifier|public
name|void
name|setThreadPoolName
parameter_list|(
name|String
name|threadPoolName
parameter_list|)
block|{
name|this
operator|.
name|threadPoolName
operator|=
name|threadPoolName
expr_stmt|;
block|}
DECL|method|getThreadPoolMinSize ()
specifier|public
name|Integer
name|getThreadPoolMinSize
parameter_list|()
block|{
return|return
name|threadPoolMinSize
return|;
block|}
DECL|method|setThreadPoolMinSize (Integer threadPoolMinSize)
specifier|public
name|void
name|setThreadPoolMinSize
parameter_list|(
name|Integer
name|threadPoolMinSize
parameter_list|)
block|{
name|this
operator|.
name|threadPoolMinSize
operator|=
name|threadPoolMinSize
expr_stmt|;
block|}
DECL|method|getThreadPoolMaxSize ()
specifier|public
name|Integer
name|getThreadPoolMaxSize
parameter_list|()
block|{
return|return
name|threadPoolMaxSize
return|;
block|}
DECL|method|setThreadPoolMaxSize (Integer threadPoolMaxSize)
specifier|public
name|void
name|setThreadPoolMaxSize
parameter_list|(
name|Integer
name|threadPoolMaxSize
parameter_list|)
block|{
name|this
operator|.
name|threadPoolMaxSize
operator|=
name|threadPoolMaxSize
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

