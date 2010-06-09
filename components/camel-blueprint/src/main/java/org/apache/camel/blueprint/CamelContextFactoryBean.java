begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.blueprint
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|blueprint
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessorType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAttribute
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlElement
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlElements
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlRootElement
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlTransient
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
name|RoutesBuilder
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
name|ShutdownRoute
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
name|ShutdownRunningTask
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
name|builder
operator|.
name|RouteBuilder
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
name|core
operator|.
name|xml
operator|.
name|AbstractCamelContextFactoryBean
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
name|core
operator|.
name|xml
operator|.
name|CamelJMXAgentDefinition
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
name|core
operator|.
name|xml
operator|.
name|CamelPropertyPlaceholderDefinition
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
name|core
operator|.
name|xml
operator|.
name|CamelProxyFactoryDefinition
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
name|core
operator|.
name|xml
operator|.
name|CamelServiceExporterDefinition
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
name|model
operator|.
name|InterceptDefinition
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
name|model
operator|.
name|InterceptFromDefinition
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
name|model
operator|.
name|InterceptSendToEndpointDefinition
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
name|model
operator|.
name|OnCompletionDefinition
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
name|model
operator|.
name|OnExceptionDefinition
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
name|model
operator|.
name|PackageScanDefinition
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
name|model
operator|.
name|RouteBuilderDefinition
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
name|model
operator|.
name|RouteContextRefDefinition
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
name|model
operator|.
name|RouteDefinition
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
name|model
operator|.
name|ThreadPoolProfileDefinition
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
name|model
operator|.
name|config
operator|.
name|PropertiesDefinition
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
name|model
operator|.
name|dataformat
operator|.
name|DataFormatsDefinition
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|BundleContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|service
operator|.
name|blueprint
operator|.
name|container
operator|.
name|BlueprintContainer
import|;
end_import

begin_comment
comment|/**  * A bean to create and initialize a {@link BlueprintCamelContext}  * and install routes either explicitly configured in  * Blueprint XML or found by searching the classpath for Java classes which extend  * {@link RouteBuilder} using the nested {@link #setPackages(String[])}.  *  * @version $Revision$  */
end_comment

begin_class
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"camelContext"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|CamelContextFactoryBean
specifier|public
class|class
name|CamelContextFactoryBean
extends|extends
name|AbstractCamelContextFactoryBean
argument_list|<
name|BlueprintCamelContext
argument_list|>
block|{
annotation|@
name|XmlAttribute
argument_list|(
name|name
operator|=
literal|"depends-on"
argument_list|,
name|required
operator|=
literal|false
argument_list|)
DECL|field|dependsOn
specifier|private
name|String
name|dependsOn
decl_stmt|;
annotation|@
name|XmlAttribute
argument_list|(
name|required
operator|=
literal|false
argument_list|)
DECL|field|trace
specifier|private
name|String
name|trace
decl_stmt|;
annotation|@
name|XmlAttribute
argument_list|(
name|required
operator|=
literal|false
argument_list|)
DECL|field|streamCache
specifier|private
name|String
name|streamCache
init|=
literal|"false"
decl_stmt|;
annotation|@
name|XmlAttribute
argument_list|(
name|required
operator|=
literal|false
argument_list|)
DECL|field|delayer
specifier|private
name|String
name|delayer
decl_stmt|;
annotation|@
name|XmlAttribute
argument_list|(
name|required
operator|=
literal|false
argument_list|)
DECL|field|handleFault
specifier|private
name|String
name|handleFault
decl_stmt|;
annotation|@
name|XmlAttribute
argument_list|(
name|required
operator|=
literal|false
argument_list|)
DECL|field|errorHandlerRef
specifier|private
name|String
name|errorHandlerRef
decl_stmt|;
annotation|@
name|XmlAttribute
argument_list|(
name|required
operator|=
literal|false
argument_list|)
DECL|field|autoStartup
specifier|private
name|String
name|autoStartup
init|=
literal|"true"
decl_stmt|;
annotation|@
name|XmlAttribute
argument_list|(
name|required
operator|=
literal|false
argument_list|)
DECL|field|shutdownRoute
specifier|private
name|ShutdownRoute
name|shutdownRoute
decl_stmt|;
annotation|@
name|XmlAttribute
argument_list|(
name|required
operator|=
literal|false
argument_list|)
DECL|field|shutdownRunningTask
specifier|private
name|ShutdownRunningTask
name|shutdownRunningTask
decl_stmt|;
annotation|@
name|XmlElement
argument_list|(
name|name
operator|=
literal|"properties"
argument_list|,
name|required
operator|=
literal|false
argument_list|)
DECL|field|properties
specifier|private
name|PropertiesDefinition
name|properties
decl_stmt|;
annotation|@
name|XmlElement
argument_list|(
name|name
operator|=
literal|"propertyPlaceholder"
argument_list|,
name|type
operator|=
name|CamelPropertyPlaceholderDefinition
operator|.
name|class
argument_list|,
name|required
operator|=
literal|false
argument_list|)
DECL|field|camelPropertyPlaceholder
specifier|private
name|CamelPropertyPlaceholderDefinition
name|camelPropertyPlaceholder
decl_stmt|;
annotation|@
name|XmlElement
argument_list|(
name|name
operator|=
literal|"package"
argument_list|,
name|required
operator|=
literal|false
argument_list|)
DECL|field|packages
specifier|private
name|String
index|[]
name|packages
init|=
block|{}
decl_stmt|;
annotation|@
name|XmlElement
argument_list|(
name|name
operator|=
literal|"packageScan"
argument_list|,
name|type
operator|=
name|PackageScanDefinition
operator|.
name|class
argument_list|,
name|required
operator|=
literal|false
argument_list|)
DECL|field|packageScan
specifier|private
name|PackageScanDefinition
name|packageScan
decl_stmt|;
annotation|@
name|XmlElement
argument_list|(
name|name
operator|=
literal|"jmxAgent"
argument_list|,
name|type
operator|=
name|CamelJMXAgentDefinition
operator|.
name|class
argument_list|,
name|required
operator|=
literal|false
argument_list|)
DECL|field|camelJMXAgent
specifier|private
name|CamelJMXAgentDefinition
name|camelJMXAgent
decl_stmt|;
annotation|@
name|XmlElements
argument_list|(
block|{
comment|//        @XmlElement(name = "beanPostProcessor", type = CamelBeanPostProcessor.class, required = false),
annotation|@
name|XmlElement
argument_list|(
name|name
operator|=
literal|"template"
argument_list|,
name|type
operator|=
name|CamelProducerTemplateFactoryBean
operator|.
name|class
argument_list|,
name|required
operator|=
literal|false
argument_list|)
block|,
annotation|@
name|XmlElement
argument_list|(
name|name
operator|=
literal|"consumerTemplate"
argument_list|,
name|type
operator|=
name|CamelConsumerTemplateFactoryBean
operator|.
name|class
argument_list|,
name|required
operator|=
literal|false
argument_list|)
block|,
annotation|@
name|XmlElement
argument_list|(
name|name
operator|=
literal|"proxy"
argument_list|,
name|type
operator|=
name|CamelProxyFactoryDefinition
operator|.
name|class
argument_list|,
name|required
operator|=
literal|false
argument_list|)
block|,
annotation|@
name|XmlElement
argument_list|(
name|name
operator|=
literal|"export"
argument_list|,
name|type
operator|=
name|CamelServiceExporterDefinition
operator|.
name|class
argument_list|,
name|required
operator|=
literal|false
argument_list|)
comment|//,
comment|//        @XmlElement(name = "errorHandler", type = ErrorHandlerDefinition.class, required = false)
block|}
argument_list|)
DECL|field|beans
specifier|private
name|List
name|beans
decl_stmt|;
annotation|@
name|XmlElement
argument_list|(
name|name
operator|=
literal|"routeBuilder"
argument_list|,
name|required
operator|=
literal|false
argument_list|)
DECL|field|builderRefs
specifier|private
name|List
argument_list|<
name|RouteBuilderDefinition
argument_list|>
name|builderRefs
init|=
operator|new
name|ArrayList
argument_list|<
name|RouteBuilderDefinition
argument_list|>
argument_list|()
decl_stmt|;
annotation|@
name|XmlElement
argument_list|(
name|name
operator|=
literal|"routeContextRef"
argument_list|,
name|required
operator|=
literal|false
argument_list|)
DECL|field|routeRefs
specifier|private
name|List
argument_list|<
name|RouteContextRefDefinition
argument_list|>
name|routeRefs
init|=
operator|new
name|ArrayList
argument_list|<
name|RouteContextRefDefinition
argument_list|>
argument_list|()
decl_stmt|;
annotation|@
name|XmlElement
argument_list|(
name|name
operator|=
literal|"threadPoolProfile"
argument_list|,
name|required
operator|=
literal|false
argument_list|)
DECL|field|threadPoolProfiles
specifier|private
name|List
argument_list|<
name|ThreadPoolProfileDefinition
argument_list|>
name|threadPoolProfiles
decl_stmt|;
annotation|@
name|XmlElement
argument_list|(
name|name
operator|=
literal|"threadPool"
argument_list|,
name|required
operator|=
literal|false
argument_list|)
DECL|field|threadPools
specifier|private
name|List
argument_list|<
name|CamelThreadPoolFactoryBean
argument_list|>
name|threadPools
decl_stmt|;
annotation|@
name|XmlElement
argument_list|(
name|name
operator|=
literal|"endpoint"
argument_list|,
name|required
operator|=
literal|false
argument_list|)
DECL|field|endpoints
specifier|private
name|List
argument_list|<
name|CamelEndpointFactoryBean
argument_list|>
name|endpoints
decl_stmt|;
annotation|@
name|XmlElement
argument_list|(
name|name
operator|=
literal|"dataFormats"
argument_list|,
name|required
operator|=
literal|false
argument_list|)
DECL|field|dataFormats
specifier|private
name|DataFormatsDefinition
name|dataFormats
decl_stmt|;
annotation|@
name|XmlElement
argument_list|(
name|name
operator|=
literal|"onException"
argument_list|,
name|required
operator|=
literal|false
argument_list|)
DECL|field|onExceptions
specifier|private
name|List
argument_list|<
name|OnExceptionDefinition
argument_list|>
name|onExceptions
init|=
operator|new
name|ArrayList
argument_list|<
name|OnExceptionDefinition
argument_list|>
argument_list|()
decl_stmt|;
annotation|@
name|XmlElement
argument_list|(
name|name
operator|=
literal|"onCompletion"
argument_list|,
name|required
operator|=
literal|false
argument_list|)
DECL|field|onCompletions
specifier|private
name|List
argument_list|<
name|OnCompletionDefinition
argument_list|>
name|onCompletions
init|=
operator|new
name|ArrayList
argument_list|<
name|OnCompletionDefinition
argument_list|>
argument_list|()
decl_stmt|;
annotation|@
name|XmlElement
argument_list|(
name|name
operator|=
literal|"intercept"
argument_list|,
name|required
operator|=
literal|false
argument_list|)
DECL|field|intercepts
specifier|private
name|List
argument_list|<
name|InterceptDefinition
argument_list|>
name|intercepts
init|=
operator|new
name|ArrayList
argument_list|<
name|InterceptDefinition
argument_list|>
argument_list|()
decl_stmt|;
annotation|@
name|XmlElement
argument_list|(
name|name
operator|=
literal|"interceptFrom"
argument_list|,
name|required
operator|=
literal|false
argument_list|)
DECL|field|interceptFroms
specifier|private
name|List
argument_list|<
name|InterceptFromDefinition
argument_list|>
name|interceptFroms
init|=
operator|new
name|ArrayList
argument_list|<
name|InterceptFromDefinition
argument_list|>
argument_list|()
decl_stmt|;
annotation|@
name|XmlElement
argument_list|(
name|name
operator|=
literal|"interceptSendToEndpoint"
argument_list|,
name|required
operator|=
literal|false
argument_list|)
DECL|field|interceptSendToEndpoints
specifier|private
name|List
argument_list|<
name|InterceptSendToEndpointDefinition
argument_list|>
name|interceptSendToEndpoints
init|=
operator|new
name|ArrayList
argument_list|<
name|InterceptSendToEndpointDefinition
argument_list|>
argument_list|()
decl_stmt|;
annotation|@
name|XmlElement
argument_list|(
name|name
operator|=
literal|"route"
argument_list|,
name|required
operator|=
literal|false
argument_list|)
DECL|field|routes
specifier|private
name|List
argument_list|<
name|RouteDefinition
argument_list|>
name|routes
init|=
operator|new
name|ArrayList
argument_list|<
name|RouteDefinition
argument_list|>
argument_list|()
decl_stmt|;
annotation|@
name|XmlTransient
DECL|field|context
specifier|private
name|BlueprintCamelContext
name|context
decl_stmt|;
annotation|@
name|XmlTransient
DECL|field|contextClassLoaderOnStart
specifier|private
name|ClassLoader
name|contextClassLoaderOnStart
decl_stmt|;
comment|//    @XmlTransient
comment|//    private ApplicationContext applicationContext;
comment|//    @XmlTransient
comment|//    private BeanPostProcessor beanPostProcessor;
annotation|@
name|XmlTransient
DECL|field|blueprintContainer
specifier|private
name|BlueprintContainer
name|blueprintContainer
decl_stmt|;
annotation|@
name|XmlTransient
DECL|field|bundleContext
specifier|private
name|BundleContext
name|bundleContext
decl_stmt|;
DECL|method|getObjectType ()
specifier|public
name|Class
name|getObjectType
parameter_list|()
block|{
return|return
name|BlueprintCamelContext
operator|.
name|class
return|;
block|}
annotation|@
name|Override
DECL|method|getContext (boolean create)
specifier|public
name|BlueprintCamelContext
name|getContext
parameter_list|(
name|boolean
name|create
parameter_list|)
block|{
if|if
condition|(
name|context
operator|==
literal|null
operator|&&
name|create
condition|)
block|{
name|context
operator|=
name|createContext
argument_list|()
expr_stmt|;
block|}
return|return
name|context
return|;
block|}
DECL|method|setBlueprintContainer (BlueprintContainer blueprintContainer)
specifier|public
name|void
name|setBlueprintContainer
parameter_list|(
name|BlueprintContainer
name|blueprintContainer
parameter_list|)
block|{
name|this
operator|.
name|blueprintContainer
operator|=
name|blueprintContainer
expr_stmt|;
block|}
DECL|method|setBundleContext (BundleContext bundleContext)
specifier|public
name|void
name|setBundleContext
parameter_list|(
name|BundleContext
name|bundleContext
parameter_list|)
block|{
name|this
operator|.
name|bundleContext
operator|=
name|bundleContext
expr_stmt|;
block|}
DECL|method|createContext ()
specifier|protected
name|BlueprintCamelContext
name|createContext
parameter_list|()
block|{
return|return
operator|new
name|BlueprintCamelContext
argument_list|(
name|bundleContext
argument_list|,
name|blueprintContainer
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|initCustomRegistry (BlueprintCamelContext context)
specifier|protected
name|void
name|initCustomRegistry
parameter_list|(
name|BlueprintCamelContext
name|context
parameter_list|)
block|{     }
annotation|@
name|Override
DECL|method|getBeanForType (Class<S> clazz)
specifier|protected
parameter_list|<
name|S
parameter_list|>
name|S
name|getBeanForType
parameter_list|(
name|Class
argument_list|<
name|S
argument_list|>
name|clazz
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|initBeanPostProcessor (BlueprintCamelContext context)
specifier|protected
name|void
name|initBeanPostProcessor
parameter_list|(
name|BlueprintCamelContext
name|context
parameter_list|)
block|{     }
annotation|@
name|Override
DECL|method|postProcessBeforeInit (RouteBuilder builder)
specifier|protected
name|void
name|postProcessBeforeInit
parameter_list|(
name|RouteBuilder
name|builder
parameter_list|)
block|{     }
annotation|@
name|Override
DECL|method|findRouteBuilders (String[] normalized, List<RoutesBuilder> builders)
specifier|protected
name|void
name|findRouteBuilders
parameter_list|(
name|String
index|[]
name|normalized
parameter_list|,
name|List
argument_list|<
name|RoutesBuilder
argument_list|>
name|builders
parameter_list|)
throws|throws
name|Exception
block|{     }
DECL|method|getDependsOn ()
specifier|public
name|String
name|getDependsOn
parameter_list|()
block|{
return|return
name|dependsOn
return|;
block|}
DECL|method|setDependsOn (String dependsOn)
specifier|public
name|void
name|setDependsOn
parameter_list|(
name|String
name|dependsOn
parameter_list|)
block|{
name|this
operator|.
name|dependsOn
operator|=
name|dependsOn
expr_stmt|;
block|}
DECL|method|getAutoStartup ()
specifier|public
name|String
name|getAutoStartup
parameter_list|()
block|{
return|return
name|autoStartup
return|;
block|}
DECL|method|setAutoStartup (String autoStartup)
specifier|public
name|void
name|setAutoStartup
parameter_list|(
name|String
name|autoStartup
parameter_list|)
block|{
name|this
operator|.
name|autoStartup
operator|=
name|autoStartup
expr_stmt|;
block|}
DECL|method|getShutdownRoute ()
specifier|public
name|ShutdownRoute
name|getShutdownRoute
parameter_list|()
block|{
return|return
name|shutdownRoute
return|;
block|}
DECL|method|setShutdownRoute (ShutdownRoute shutdownRoute)
specifier|public
name|void
name|setShutdownRoute
parameter_list|(
name|ShutdownRoute
name|shutdownRoute
parameter_list|)
block|{
name|this
operator|.
name|shutdownRoute
operator|=
name|shutdownRoute
expr_stmt|;
block|}
DECL|method|getShutdownRunningTask ()
specifier|public
name|ShutdownRunningTask
name|getShutdownRunningTask
parameter_list|()
block|{
return|return
name|shutdownRunningTask
return|;
block|}
DECL|method|setShutdownRunningTask (ShutdownRunningTask shutdownRunningTask)
specifier|public
name|void
name|setShutdownRunningTask
parameter_list|(
name|ShutdownRunningTask
name|shutdownRunningTask
parameter_list|)
block|{
name|this
operator|.
name|shutdownRunningTask
operator|=
name|shutdownRunningTask
expr_stmt|;
block|}
DECL|method|getCamelPropertyPlaceholder ()
specifier|public
name|CamelPropertyPlaceholderDefinition
name|getCamelPropertyPlaceholder
parameter_list|()
block|{
return|return
name|camelPropertyPlaceholder
return|;
block|}
DECL|method|setCamelPropertyPlaceholder (CamelPropertyPlaceholderDefinition camelPropertyPlaceholder)
specifier|public
name|void
name|setCamelPropertyPlaceholder
parameter_list|(
name|CamelPropertyPlaceholderDefinition
name|camelPropertyPlaceholder
parameter_list|)
block|{
name|this
operator|.
name|camelPropertyPlaceholder
operator|=
name|camelPropertyPlaceholder
expr_stmt|;
block|}
DECL|method|getRouteRefs ()
specifier|public
name|List
argument_list|<
name|RouteContextRefDefinition
argument_list|>
name|getRouteRefs
parameter_list|()
block|{
return|return
name|routeRefs
return|;
block|}
DECL|method|setRouteRefs (List<RouteContextRefDefinition> routeRefs)
specifier|public
name|void
name|setRouteRefs
parameter_list|(
name|List
argument_list|<
name|RouteContextRefDefinition
argument_list|>
name|routeRefs
parameter_list|)
block|{
name|this
operator|.
name|routeRefs
operator|=
name|routeRefs
expr_stmt|;
block|}
DECL|method|getThreadPoolProfiles ()
specifier|public
name|List
argument_list|<
name|ThreadPoolProfileDefinition
argument_list|>
name|getThreadPoolProfiles
parameter_list|()
block|{
return|return
name|threadPoolProfiles
return|;
block|}
DECL|method|setThreadPoolProfiles (List<ThreadPoolProfileDefinition> threadPoolProfiles)
specifier|public
name|void
name|setThreadPoolProfiles
parameter_list|(
name|List
argument_list|<
name|ThreadPoolProfileDefinition
argument_list|>
name|threadPoolProfiles
parameter_list|)
block|{
name|this
operator|.
name|threadPoolProfiles
operator|=
name|threadPoolProfiles
expr_stmt|;
block|}
DECL|method|getThreadPools ()
specifier|public
name|List
argument_list|<
name|CamelThreadPoolFactoryBean
argument_list|>
name|getThreadPools
parameter_list|()
block|{
return|return
name|threadPools
return|;
block|}
DECL|method|setThreadPools (List<CamelThreadPoolFactoryBean> threadPools)
specifier|public
name|void
name|setThreadPools
parameter_list|(
name|List
argument_list|<
name|CamelThreadPoolFactoryBean
argument_list|>
name|threadPools
parameter_list|)
block|{
name|this
operator|.
name|threadPools
operator|=
name|threadPools
expr_stmt|;
block|}
DECL|method|getTrace ()
specifier|public
name|String
name|getTrace
parameter_list|()
block|{
return|return
name|trace
return|;
block|}
DECL|method|setTrace (String trace)
specifier|public
name|void
name|setTrace
parameter_list|(
name|String
name|trace
parameter_list|)
block|{
name|this
operator|.
name|trace
operator|=
name|trace
expr_stmt|;
block|}
DECL|method|getStreamCache ()
specifier|public
name|String
name|getStreamCache
parameter_list|()
block|{
return|return
name|streamCache
return|;
block|}
DECL|method|setStreamCache (String streamCache)
specifier|public
name|void
name|setStreamCache
parameter_list|(
name|String
name|streamCache
parameter_list|)
block|{
name|this
operator|.
name|streamCache
operator|=
name|streamCache
expr_stmt|;
block|}
DECL|method|getDelayer ()
specifier|public
name|String
name|getDelayer
parameter_list|()
block|{
return|return
name|delayer
return|;
block|}
DECL|method|setDelayer (String delayer)
specifier|public
name|void
name|setDelayer
parameter_list|(
name|String
name|delayer
parameter_list|)
block|{
name|this
operator|.
name|delayer
operator|=
name|delayer
expr_stmt|;
block|}
DECL|method|getHandleFault ()
specifier|public
name|String
name|getHandleFault
parameter_list|()
block|{
return|return
name|handleFault
return|;
block|}
DECL|method|setHandleFault (String handleFault)
specifier|public
name|void
name|setHandleFault
parameter_list|(
name|String
name|handleFault
parameter_list|)
block|{
name|this
operator|.
name|handleFault
operator|=
name|handleFault
expr_stmt|;
block|}
DECL|method|getErrorHandlerRef ()
specifier|public
name|String
name|getErrorHandlerRef
parameter_list|()
block|{
return|return
name|errorHandlerRef
return|;
block|}
DECL|method|setErrorHandlerRef (String errorHandlerRef)
specifier|public
name|void
name|setErrorHandlerRef
parameter_list|(
name|String
name|errorHandlerRef
parameter_list|)
block|{
name|this
operator|.
name|errorHandlerRef
operator|=
name|errorHandlerRef
expr_stmt|;
block|}
DECL|method|getProperties ()
specifier|public
name|PropertiesDefinition
name|getProperties
parameter_list|()
block|{
return|return
name|properties
return|;
block|}
DECL|method|setProperties (PropertiesDefinition properties)
specifier|public
name|void
name|setProperties
parameter_list|(
name|PropertiesDefinition
name|properties
parameter_list|)
block|{
name|this
operator|.
name|properties
operator|=
name|properties
expr_stmt|;
block|}
DECL|method|getPackages ()
specifier|public
name|String
index|[]
name|getPackages
parameter_list|()
block|{
return|return
name|packages
return|;
block|}
DECL|method|setPackages (String[] packages)
specifier|public
name|void
name|setPackages
parameter_list|(
name|String
index|[]
name|packages
parameter_list|)
block|{
name|this
operator|.
name|packages
operator|=
name|packages
expr_stmt|;
block|}
DECL|method|getPackageScan ()
specifier|public
name|PackageScanDefinition
name|getPackageScan
parameter_list|()
block|{
return|return
name|packageScan
return|;
block|}
DECL|method|setPackageScan (PackageScanDefinition packageScan)
specifier|public
name|void
name|setPackageScan
parameter_list|(
name|PackageScanDefinition
name|packageScan
parameter_list|)
block|{
name|this
operator|.
name|packageScan
operator|=
name|packageScan
expr_stmt|;
block|}
DECL|method|getCamelJMXAgent ()
specifier|public
name|CamelJMXAgentDefinition
name|getCamelJMXAgent
parameter_list|()
block|{
return|return
name|camelJMXAgent
return|;
block|}
DECL|method|setCamelJMXAgent (CamelJMXAgentDefinition camelJMXAgent)
specifier|public
name|void
name|setCamelJMXAgent
parameter_list|(
name|CamelJMXAgentDefinition
name|camelJMXAgent
parameter_list|)
block|{
name|this
operator|.
name|camelJMXAgent
operator|=
name|camelJMXAgent
expr_stmt|;
block|}
DECL|method|getBeans ()
specifier|public
name|List
name|getBeans
parameter_list|()
block|{
return|return
name|beans
return|;
block|}
DECL|method|setBeans (List beans)
specifier|public
name|void
name|setBeans
parameter_list|(
name|List
name|beans
parameter_list|)
block|{
name|this
operator|.
name|beans
operator|=
name|beans
expr_stmt|;
block|}
DECL|method|getBuilderRefs ()
specifier|public
name|List
argument_list|<
name|RouteBuilderDefinition
argument_list|>
name|getBuilderRefs
parameter_list|()
block|{
return|return
name|builderRefs
return|;
block|}
DECL|method|setBuilderRefs (List<RouteBuilderDefinition> builderRefs)
specifier|public
name|void
name|setBuilderRefs
parameter_list|(
name|List
argument_list|<
name|RouteBuilderDefinition
argument_list|>
name|builderRefs
parameter_list|)
block|{
name|this
operator|.
name|builderRefs
operator|=
name|builderRefs
expr_stmt|;
block|}
DECL|method|getEndpoints ()
specifier|public
name|List
argument_list|<
name|CamelEndpointFactoryBean
argument_list|>
name|getEndpoints
parameter_list|()
block|{
return|return
name|endpoints
return|;
block|}
DECL|method|setEndpoints (List<CamelEndpointFactoryBean> endpoints)
specifier|public
name|void
name|setEndpoints
parameter_list|(
name|List
argument_list|<
name|CamelEndpointFactoryBean
argument_list|>
name|endpoints
parameter_list|)
block|{
name|this
operator|.
name|endpoints
operator|=
name|endpoints
expr_stmt|;
block|}
DECL|method|getDataFormats ()
specifier|public
name|DataFormatsDefinition
name|getDataFormats
parameter_list|()
block|{
return|return
name|dataFormats
return|;
block|}
DECL|method|setDataFormats (DataFormatsDefinition dataFormats)
specifier|public
name|void
name|setDataFormats
parameter_list|(
name|DataFormatsDefinition
name|dataFormats
parameter_list|)
block|{
name|this
operator|.
name|dataFormats
operator|=
name|dataFormats
expr_stmt|;
block|}
DECL|method|getOnExceptions ()
specifier|public
name|List
argument_list|<
name|OnExceptionDefinition
argument_list|>
name|getOnExceptions
parameter_list|()
block|{
return|return
name|onExceptions
return|;
block|}
DECL|method|setOnExceptions (List<OnExceptionDefinition> onExceptions)
specifier|public
name|void
name|setOnExceptions
parameter_list|(
name|List
argument_list|<
name|OnExceptionDefinition
argument_list|>
name|onExceptions
parameter_list|)
block|{
name|this
operator|.
name|onExceptions
operator|=
name|onExceptions
expr_stmt|;
block|}
DECL|method|getOnCompletions ()
specifier|public
name|List
argument_list|<
name|OnCompletionDefinition
argument_list|>
name|getOnCompletions
parameter_list|()
block|{
return|return
name|onCompletions
return|;
block|}
DECL|method|setOnCompletions (List<OnCompletionDefinition> onCompletions)
specifier|public
name|void
name|setOnCompletions
parameter_list|(
name|List
argument_list|<
name|OnCompletionDefinition
argument_list|>
name|onCompletions
parameter_list|)
block|{
name|this
operator|.
name|onCompletions
operator|=
name|onCompletions
expr_stmt|;
block|}
DECL|method|getIntercepts ()
specifier|public
name|List
argument_list|<
name|InterceptDefinition
argument_list|>
name|getIntercepts
parameter_list|()
block|{
return|return
name|intercepts
return|;
block|}
DECL|method|setIntercepts (List<InterceptDefinition> intercepts)
specifier|public
name|void
name|setIntercepts
parameter_list|(
name|List
argument_list|<
name|InterceptDefinition
argument_list|>
name|intercepts
parameter_list|)
block|{
name|this
operator|.
name|intercepts
operator|=
name|intercepts
expr_stmt|;
block|}
DECL|method|getInterceptFroms ()
specifier|public
name|List
argument_list|<
name|InterceptFromDefinition
argument_list|>
name|getInterceptFroms
parameter_list|()
block|{
return|return
name|interceptFroms
return|;
block|}
DECL|method|setInterceptFroms (List<InterceptFromDefinition> interceptFroms)
specifier|public
name|void
name|setInterceptFroms
parameter_list|(
name|List
argument_list|<
name|InterceptFromDefinition
argument_list|>
name|interceptFroms
parameter_list|)
block|{
name|this
operator|.
name|interceptFroms
operator|=
name|interceptFroms
expr_stmt|;
block|}
DECL|method|getInterceptSendToEndpoints ()
specifier|public
name|List
argument_list|<
name|InterceptSendToEndpointDefinition
argument_list|>
name|getInterceptSendToEndpoints
parameter_list|()
block|{
return|return
name|interceptSendToEndpoints
return|;
block|}
DECL|method|setInterceptSendToEndpoints (List<InterceptSendToEndpointDefinition> interceptSendToEndpoints)
specifier|public
name|void
name|setInterceptSendToEndpoints
parameter_list|(
name|List
argument_list|<
name|InterceptSendToEndpointDefinition
argument_list|>
name|interceptSendToEndpoints
parameter_list|)
block|{
name|this
operator|.
name|interceptSendToEndpoints
operator|=
name|interceptSendToEndpoints
expr_stmt|;
block|}
DECL|method|getRoutes ()
specifier|public
name|List
argument_list|<
name|RouteDefinition
argument_list|>
name|getRoutes
parameter_list|()
block|{
return|return
name|routes
return|;
block|}
DECL|method|setRoutes (List<RouteDefinition> routes)
specifier|public
name|void
name|setRoutes
parameter_list|(
name|List
argument_list|<
name|RouteDefinition
argument_list|>
name|routes
parameter_list|)
block|{
name|this
operator|.
name|routes
operator|=
name|routes
expr_stmt|;
block|}
DECL|method|getContextClassLoaderOnStart ()
specifier|public
name|ClassLoader
name|getContextClassLoaderOnStart
parameter_list|()
block|{
return|return
name|contextClassLoaderOnStart
return|;
block|}
block|}
end_class

end_unit

