begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jbpm.server
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jbpm
operator|.
name|server
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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
name|impl
operator|.
name|DefaultCamelContext
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
name|FromDefinition
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
name|RoutesDefinition
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jbpm
operator|.
name|services
operator|.
name|api
operator|.
name|service
operator|.
name|ServiceRegistry
import|;
end_import

begin_import
import|import
name|org
operator|.
name|kie
operator|.
name|server
operator|.
name|services
operator|.
name|api
operator|.
name|KieContainerInstance
import|;
end_import

begin_import
import|import
name|org
operator|.
name|kie
operator|.
name|server
operator|.
name|services
operator|.
name|api
operator|.
name|KieServerExtension
import|;
end_import

begin_import
import|import
name|org
operator|.
name|kie
operator|.
name|server
operator|.
name|services
operator|.
name|api
operator|.
name|KieServerRegistry
import|;
end_import

begin_import
import|import
name|org
operator|.
name|kie
operator|.
name|server
operator|.
name|services
operator|.
name|api
operator|.
name|SupportedTransports
import|;
end_import

begin_import
import|import
name|org
operator|.
name|kie
operator|.
name|server
operator|.
name|services
operator|.
name|impl
operator|.
name|KieServerImpl
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_class
DECL|class|CamelKieServerExtension
specifier|public
class|class
name|CamelKieServerExtension
implements|implements
name|KieServerExtension
block|{
DECL|field|EXTENSION_NAME
specifier|public
specifier|static
specifier|final
name|String
name|EXTENSION_NAME
init|=
literal|"Camel"
decl_stmt|;
DECL|field|LOGGER
specifier|private
specifier|static
specifier|final
name|Logger
name|LOGGER
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|CamelKieServerExtension
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|DISABLED
specifier|private
specifier|static
specifier|final
name|Boolean
name|DISABLED
init|=
name|Boolean
operator|.
name|parseBoolean
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"org.camel.server.ext.disabled"
argument_list|,
literal|"false"
argument_list|)
argument_list|)
decl_stmt|;
DECL|field|camel
specifier|protected
name|DefaultCamelContext
name|camel
decl_stmt|;
DECL|field|managedCamel
specifier|protected
name|boolean
name|managedCamel
decl_stmt|;
DECL|field|camelContexts
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|DefaultCamelContext
argument_list|>
name|camelContexts
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
DECL|method|CamelKieServerExtension ()
specifier|public
name|CamelKieServerExtension
parameter_list|()
block|{
name|this
operator|.
name|managedCamel
operator|=
literal|true
expr_stmt|;
block|}
DECL|method|CamelKieServerExtension (DefaultCamelContext camel)
specifier|public
name|CamelKieServerExtension
parameter_list|(
name|DefaultCamelContext
name|camel
parameter_list|)
block|{
name|this
operator|.
name|camel
operator|=
name|camel
expr_stmt|;
name|this
operator|.
name|managedCamel
operator|=
literal|false
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|isInitialized ()
specifier|public
name|boolean
name|isInitialized
parameter_list|()
block|{
return|return
name|camel
operator|!=
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|isActive ()
specifier|public
name|boolean
name|isActive
parameter_list|()
block|{
return|return
operator|!
name|DISABLED
return|;
block|}
annotation|@
name|Override
DECL|method|init (KieServerImpl kieServer, KieServerRegistry registry)
specifier|public
name|void
name|init
parameter_list|(
name|KieServerImpl
name|kieServer
parameter_list|,
name|KieServerRegistry
name|registry
parameter_list|)
block|{
if|if
condition|(
name|this
operator|.
name|managedCamel
operator|&&
name|this
operator|.
name|camel
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|camel
operator|=
operator|new
name|DefaultCamelContext
argument_list|()
expr_stmt|;
name|this
operator|.
name|camel
operator|.
name|setName
argument_list|(
literal|"KIE Server Camel context"
argument_list|)
expr_stmt|;
try|try
init|(
name|InputStream
name|is
init|=
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"/global-camel-routes.xml"
argument_list|)
init|)
block|{
if|if
condition|(
name|is
operator|!=
literal|null
condition|)
block|{
name|RoutesDefinition
name|routes
init|=
name|camel
operator|.
name|loadRoutesDefinition
argument_list|(
name|is
argument_list|)
decl_stmt|;
name|camel
operator|.
name|addRouteDefinitions
argument_list|(
name|routes
operator|.
name|getRoutes
argument_list|()
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
name|LOGGER
operator|.
name|error
argument_list|(
literal|"Error while adding Camel context for KIE Server"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
name|ServiceRegistry
operator|.
name|get
argument_list|()
operator|.
name|register
argument_list|(
literal|"GlobalCamelService"
argument_list|,
name|this
operator|.
name|camel
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|destroy (KieServerImpl kieServer, KieServerRegistry registry)
specifier|public
name|void
name|destroy
parameter_list|(
name|KieServerImpl
name|kieServer
parameter_list|,
name|KieServerRegistry
name|registry
parameter_list|)
block|{
name|ServiceRegistry
operator|.
name|get
argument_list|()
operator|.
name|remove
argument_list|(
literal|"GlobalCamelService"
argument_list|)
expr_stmt|;
if|if
condition|(
name|this
operator|.
name|managedCamel
operator|&&
name|this
operator|.
name|camel
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|this
operator|.
name|camel
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOGGER
operator|.
name|error
argument_list|(
literal|"Failed at stopping KIE Server extension {}"
argument_list|,
name|EXTENSION_NAME
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Override
DECL|method|createContainer (String id, KieContainerInstance kieContainerInstance, Map<String, Object> parameters)
specifier|public
name|void
name|createContainer
parameter_list|(
name|String
name|id
parameter_list|,
name|KieContainerInstance
name|kieContainerInstance
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
block|{
name|ClassLoader
name|classloader
init|=
name|kieContainerInstance
operator|.
name|getKieContainer
argument_list|()
operator|.
name|getClassLoader
argument_list|()
decl_stmt|;
try|try
init|(
name|InputStream
name|is
init|=
name|classloader
operator|.
name|getResourceAsStream
argument_list|(
literal|"camel-routes.xml"
argument_list|)
init|)
block|{
if|if
condition|(
name|is
operator|!=
literal|null
condition|)
block|{
name|DefaultCamelContext
name|context
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
name|context
operator|.
name|setName
argument_list|(
literal|"KIE Server Camel context for container "
operator|+
name|kieContainerInstance
operator|.
name|getContainerId
argument_list|()
argument_list|)
expr_stmt|;
name|RoutesDefinition
name|routes
init|=
name|context
operator|.
name|loadRoutesDefinition
argument_list|(
name|is
argument_list|)
decl_stmt|;
name|annotateKJarRoutes
argument_list|(
name|routes
argument_list|,
name|id
argument_list|)
expr_stmt|;
name|context
operator|.
name|addRouteDefinitions
argument_list|(
name|routes
operator|.
name|getRoutes
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|camelContexts
operator|.
name|put
argument_list|(
name|id
argument_list|,
name|context
argument_list|)
expr_stmt|;
name|ServiceRegistry
operator|.
name|get
argument_list|()
operator|.
name|register
argument_list|(
name|id
operator|+
literal|"_CamelService"
argument_list|,
name|context
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
name|LOGGER
operator|.
name|error
argument_list|(
literal|"Error while adding Camel context for {}"
argument_list|,
name|kieContainerInstance
operator|.
name|getContainerId
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|updateContainer (String id, KieContainerInstance kieContainerInstance, Map<String, Object> parameters)
specifier|public
name|void
name|updateContainer
parameter_list|(
name|String
name|id
parameter_list|,
name|KieContainerInstance
name|kieContainerInstance
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
block|{
name|disposeContainer
argument_list|(
name|id
argument_list|,
name|kieContainerInstance
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
name|createContainer
argument_list|(
name|id
argument_list|,
name|kieContainerInstance
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|isUpdateContainerAllowed (String id, KieContainerInstance kieContainerInstance, Map<String, Object> parameters)
specifier|public
name|boolean
name|isUpdateContainerAllowed
parameter_list|(
name|String
name|id
parameter_list|,
name|KieContainerInstance
name|kieContainerInstance
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|disposeContainer (String id, KieContainerInstance kieContainerInstance, Map<String, Object> parameters)
specifier|public
name|void
name|disposeContainer
parameter_list|(
name|String
name|id
parameter_list|,
name|KieContainerInstance
name|kieContainerInstance
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
block|{
name|DefaultCamelContext
name|context
init|=
name|camelContexts
operator|.
name|get
argument_list|(
name|id
argument_list|)
decl_stmt|;
if|if
condition|(
name|context
operator|!=
literal|null
condition|)
block|{
name|ServiceRegistry
operator|.
name|get
argument_list|()
operator|.
name|remove
argument_list|(
name|id
operator|+
literal|"_CamelService"
argument_list|)
expr_stmt|;
try|try
block|{
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOGGER
operator|.
name|error
argument_list|(
literal|"Error while removing Camel context for container {}"
argument_list|,
name|id
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Override
DECL|method|getAppComponents (SupportedTransports type)
specifier|public
name|List
argument_list|<
name|Object
argument_list|>
name|getAppComponents
parameter_list|(
name|SupportedTransports
name|type
parameter_list|)
block|{
return|return
name|Collections
operator|.
name|emptyList
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getAppComponents (Class<T> serviceType)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|getAppComponents
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|serviceType
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|getImplementedCapability ()
specifier|public
name|String
name|getImplementedCapability
parameter_list|()
block|{
return|return
literal|"Integration"
return|;
block|}
annotation|@
name|Override
DECL|method|getServices ()
specifier|public
name|List
argument_list|<
name|Object
argument_list|>
name|getServices
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|emptyList
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getExtensionName ()
specifier|public
name|String
name|getExtensionName
parameter_list|()
block|{
return|return
name|EXTENSION_NAME
return|;
block|}
annotation|@
name|Override
DECL|method|getStartOrder ()
specifier|public
name|Integer
name|getStartOrder
parameter_list|()
block|{
return|return
literal|50
return|;
block|}
annotation|@
name|Override
DECL|method|serverStarted ()
specifier|public
name|void
name|serverStarted
parameter_list|()
block|{
if|if
condition|(
name|this
operator|.
name|managedCamel
operator|&&
name|this
operator|.
name|camel
operator|!=
literal|null
operator|&&
operator|!
name|this
operator|.
name|camel
operator|.
name|isStarted
argument_list|()
condition|)
block|{
try|try
block|{
name|this
operator|.
name|camel
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOGGER
operator|.
name|error
argument_list|(
literal|"Failed at start Camel context"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|EXTENSION_NAME
operator|+
literal|" KIE Server extension"
return|;
block|}
DECL|method|annotateKJarRoutes (RoutesDefinition routes, String deploymentId)
specifier|protected
name|void
name|annotateKJarRoutes
parameter_list|(
name|RoutesDefinition
name|routes
parameter_list|,
name|String
name|deploymentId
parameter_list|)
block|{
for|for
control|(
name|RouteDefinition
name|route
range|:
name|routes
operator|.
name|getRoutes
argument_list|()
control|)
block|{
for|for
control|(
name|FromDefinition
name|from
range|:
name|route
operator|.
name|getInputs
argument_list|()
control|)
block|{
if|if
condition|(
name|from
operator|.
name|getUri
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"jbpm:events"
argument_list|)
operator|&&
operator|!
name|from
operator|.
name|getUri
argument_list|()
operator|.
name|contains
argument_list|(
literal|"deploymentId"
argument_list|)
condition|)
block|{
name|StringBuilder
name|uri
init|=
operator|new
name|StringBuilder
argument_list|(
name|from
operator|.
name|getUri
argument_list|()
argument_list|)
decl_stmt|;
name|String
index|[]
name|split
init|=
name|from
operator|.
name|getUri
argument_list|()
operator|.
name|split
argument_list|(
literal|"\\?"
argument_list|)
decl_stmt|;
if|if
condition|(
name|split
operator|.
name|length
operator|==
literal|1
condition|)
block|{
comment|// no query given
name|uri
operator|.
name|append
argument_list|(
literal|"?"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// already query params exist
name|uri
operator|.
name|append
argument_list|(
literal|"&"
argument_list|)
expr_stmt|;
block|}
name|uri
operator|.
name|append
argument_list|(
literal|"deploymentId="
argument_list|)
operator|.
name|append
argument_list|(
name|deploymentId
argument_list|)
expr_stmt|;
name|from
operator|.
name|setUri
argument_list|(
name|uri
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|from
operator|.
name|getUri
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

