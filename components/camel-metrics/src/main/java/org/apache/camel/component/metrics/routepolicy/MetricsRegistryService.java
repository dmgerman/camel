begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.metrics.routepolicy
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|metrics
operator|.
name|routepolicy
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|MBeanServer
import|;
end_import

begin_import
import|import
name|com
operator|.
name|codahale
operator|.
name|metrics
operator|.
name|JmxReporter
import|;
end_import

begin_import
import|import
name|com
operator|.
name|codahale
operator|.
name|metrics
operator|.
name|MetricRegistry
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
name|spi
operator|.
name|ManagementAgent
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

begin_comment
comment|/**  * Service holding the {@link MetricRegistry} which registers all metrics.  */
end_comment

begin_class
DECL|class|MetricsRegistryService
specifier|public
specifier|final
class|class
name|MetricsRegistryService
extends|extends
name|ServiceSupport
implements|implements
name|CamelContextAware
block|{
DECL|field|camelContext
specifier|private
name|CamelContext
name|camelContext
decl_stmt|;
DECL|field|registry
specifier|private
name|MetricRegistry
name|registry
decl_stmt|;
DECL|field|reporter
specifier|private
name|JmxReporter
name|reporter
decl_stmt|;
DECL|field|useJmx
specifier|private
name|boolean
name|useJmx
decl_stmt|;
DECL|field|jmxDomain
specifier|private
name|String
name|jmxDomain
init|=
literal|"org.apache.camel.metrics"
decl_stmt|;
DECL|method|getRegistry ()
specifier|public
name|MetricRegistry
name|getRegistry
parameter_list|()
block|{
return|return
name|registry
return|;
block|}
DECL|method|setRegistry (MetricRegistry registry)
specifier|public
name|void
name|setRegistry
parameter_list|(
name|MetricRegistry
name|registry
parameter_list|)
block|{
name|this
operator|.
name|registry
operator|=
name|registry
expr_stmt|;
block|}
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
DECL|method|isUseJmx ()
specifier|public
name|boolean
name|isUseJmx
parameter_list|()
block|{
return|return
name|useJmx
return|;
block|}
DECL|method|setUseJmx (boolean useJmx)
specifier|public
name|void
name|setUseJmx
parameter_list|(
name|boolean
name|useJmx
parameter_list|)
block|{
name|this
operator|.
name|useJmx
operator|=
name|useJmx
expr_stmt|;
block|}
DECL|method|getJmxDomain ()
specifier|public
name|String
name|getJmxDomain
parameter_list|()
block|{
return|return
name|jmxDomain
return|;
block|}
DECL|method|setJmxDomain (String jmxDomain)
specifier|public
name|void
name|setJmxDomain
parameter_list|(
name|String
name|jmxDomain
parameter_list|)
block|{
name|this
operator|.
name|jmxDomain
operator|=
name|jmxDomain
expr_stmt|;
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
name|registry
operator|==
literal|null
condition|)
block|{
name|registry
operator|=
operator|new
name|MetricRegistry
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|useJmx
condition|)
block|{
name|ManagementAgent
name|agent
init|=
name|getCamelContext
argument_list|()
operator|.
name|getManagementStrategy
argument_list|()
operator|.
name|getManagementAgent
argument_list|()
decl_stmt|;
if|if
condition|(
name|agent
operator|!=
literal|null
condition|)
block|{
name|MBeanServer
name|server
init|=
name|agent
operator|.
name|getMBeanServer
argument_list|()
decl_stmt|;
if|if
condition|(
name|server
operator|!=
literal|null
condition|)
block|{
name|String
name|domain
init|=
name|jmxDomain
operator|+
literal|"."
operator|+
name|getCamelContext
argument_list|()
operator|.
name|getManagementName
argument_list|()
decl_stmt|;
name|reporter
operator|=
name|JmxReporter
operator|.
name|forRegistry
argument_list|(
name|registry
argument_list|)
operator|.
name|registerWith
argument_list|(
name|server
argument_list|)
operator|.
name|inDomain
argument_list|(
name|domain
argument_list|)
operator|.
name|build
argument_list|()
expr_stmt|;
name|reporter
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"CamelContext has not enabled JMX"
argument_list|)
throw|;
block|}
block|}
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
if|if
condition|(
name|reporter
operator|!=
literal|null
condition|)
block|{
name|reporter
operator|.
name|stop
argument_list|()
expr_stmt|;
name|reporter
operator|=
literal|null
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

