begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.commands
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|commands
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
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Comparator
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
name|Iterator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedHashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedHashSet
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
name|java
operator|.
name|util
operator|.
name|Properties
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

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
name|javax
operator|.
name|management
operator|.
name|ObjectName
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
name|JAXBException
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
name|Route
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
name|catalog
operator|.
name|CamelComponentCatalog
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
name|catalog
operator|.
name|DefaultCamelComponentCatalog
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
name|commands
operator|.
name|internal
operator|.
name|RegexUtil
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
name|ModelHelper
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
name|rest
operator|.
name|RestDefinition
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
name|rest
operator|.
name|RestsDefinition
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
name|spi
operator|.
name|RestRegistry
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
name|JsonSchemaHelper
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

begin_comment
comment|/**  * Abstract {@link org.apache.camel.commands.CamelController} that implementators should extend.  */
end_comment

begin_class
DECL|class|AbstractCamelController
specifier|public
specifier|abstract
class|class
name|AbstractCamelController
implements|implements
name|CamelController
block|{
DECL|field|catalog
specifier|private
name|CamelComponentCatalog
name|catalog
init|=
operator|new
name|DefaultCamelComponentCatalog
argument_list|()
decl_stmt|;
DECL|method|getCamelContext (String name)
specifier|public
name|CamelContext
name|getCamelContext
parameter_list|(
name|String
name|name
parameter_list|)
block|{
for|for
control|(
name|CamelContext
name|camelContext
range|:
name|this
operator|.
name|getCamelContexts
argument_list|()
control|)
block|{
if|if
condition|(
name|camelContext
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
return|return
name|camelContext
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
DECL|method|getRoutes (String camelContextName)
specifier|public
name|List
argument_list|<
name|Route
argument_list|>
name|getRoutes
parameter_list|(
name|String
name|camelContextName
parameter_list|)
block|{
return|return
name|getRoutes
argument_list|(
name|camelContextName
argument_list|,
literal|null
argument_list|)
return|;
block|}
DECL|method|getRoutes (String camelContextName, String filter)
specifier|public
name|List
argument_list|<
name|Route
argument_list|>
name|getRoutes
parameter_list|(
name|String
name|camelContextName
parameter_list|,
name|String
name|filter
parameter_list|)
block|{
name|List
argument_list|<
name|Route
argument_list|>
name|routes
init|=
operator|new
name|ArrayList
argument_list|<
name|Route
argument_list|>
argument_list|()
decl_stmt|;
if|if
condition|(
name|camelContextName
operator|!=
literal|null
condition|)
block|{
name|CamelContext
name|context
init|=
name|this
operator|.
name|getCamelContext
argument_list|(
name|camelContextName
argument_list|)
decl_stmt|;
if|if
condition|(
name|context
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Route
name|route
range|:
name|context
operator|.
name|getRoutes
argument_list|()
control|)
block|{
if|if
condition|(
name|filter
operator|==
literal|null
operator|||
name|route
operator|.
name|getId
argument_list|()
operator|.
name|matches
argument_list|(
name|filter
argument_list|)
condition|)
block|{
name|routes
operator|.
name|add
argument_list|(
name|route
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
else|else
block|{
name|List
argument_list|<
name|CamelContext
argument_list|>
name|camelContexts
init|=
name|this
operator|.
name|getCamelContexts
argument_list|()
decl_stmt|;
for|for
control|(
name|CamelContext
name|camelContext
range|:
name|camelContexts
control|)
block|{
for|for
control|(
name|Route
name|route
range|:
name|camelContext
operator|.
name|getRoutes
argument_list|()
control|)
block|{
if|if
condition|(
name|filter
operator|==
literal|null
operator|||
name|route
operator|.
name|getId
argument_list|()
operator|.
name|matches
argument_list|(
name|filter
argument_list|)
condition|)
block|{
name|routes
operator|.
name|add
argument_list|(
name|route
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
comment|// sort the list
name|Collections
operator|.
name|sort
argument_list|(
name|routes
argument_list|,
operator|new
name|Comparator
argument_list|<
name|Route
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|int
name|compare
parameter_list|(
name|Route
name|o1
parameter_list|,
name|Route
name|o2
parameter_list|)
block|{
comment|// group by camel context first, then by route name
name|String
name|c1
init|=
name|o1
operator|.
name|getRouteContext
argument_list|()
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getName
argument_list|()
decl_stmt|;
name|String
name|c2
init|=
name|o2
operator|.
name|getRouteContext
argument_list|()
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getName
argument_list|()
decl_stmt|;
name|int
name|answer
init|=
name|c1
operator|.
name|compareTo
argument_list|(
name|c2
argument_list|)
decl_stmt|;
if|if
condition|(
name|answer
operator|==
literal|0
condition|)
block|{
comment|// okay from same camel context, then sort by route id
name|answer
operator|=
name|o1
operator|.
name|getId
argument_list|()
operator|.
name|compareTo
argument_list|(
name|o2
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
block|}
argument_list|)
expr_stmt|;
return|return
name|routes
return|;
block|}
DECL|method|resetRouteStats (String camelContextName)
specifier|public
name|void
name|resetRouteStats
parameter_list|(
name|String
name|camelContextName
parameter_list|)
block|{
name|CamelContext
name|context
init|=
name|this
operator|.
name|getCamelContext
argument_list|(
name|camelContextName
argument_list|)
decl_stmt|;
if|if
condition|(
name|context
operator|==
literal|null
condition|)
block|{
return|return;
block|}
try|try
block|{
name|ManagementAgent
name|agent
init|=
name|context
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
name|mBeanServer
init|=
name|agent
operator|.
name|getMBeanServer
argument_list|()
decl_stmt|;
comment|// reset route mbeans
name|ObjectName
name|query
init|=
name|ObjectName
operator|.
name|getInstance
argument_list|(
name|agent
operator|.
name|getMBeanObjectDomainName
argument_list|()
operator|+
literal|":type=routes,*"
argument_list|)
decl_stmt|;
name|Set
argument_list|<
name|ObjectName
argument_list|>
name|set
init|=
name|mBeanServer
operator|.
name|queryNames
argument_list|(
name|query
argument_list|,
literal|null
argument_list|)
decl_stmt|;
for|for
control|(
name|ObjectName
name|routeMBean
range|:
name|set
control|)
block|{
name|String
name|camelId
init|=
operator|(
name|String
operator|)
name|mBeanServer
operator|.
name|getAttribute
argument_list|(
name|routeMBean
argument_list|,
literal|"CamelId"
argument_list|)
decl_stmt|;
if|if
condition|(
name|camelId
operator|!=
literal|null
operator|&&
name|camelId
operator|.
name|equals
argument_list|(
name|context
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|mBeanServer
operator|.
name|invoke
argument_list|(
name|routeMBean
argument_list|,
literal|"reset"
argument_list|,
operator|new
name|Object
index|[]
block|{
literal|true
block|}
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"boolean"
block|}
argument_list|)
expr_stmt|;
block|}
block|}
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
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"deprecation"
argument_list|)
DECL|method|getRouteModelAsXml (String routeId, String camelContextName)
specifier|public
name|String
name|getRouteModelAsXml
parameter_list|(
name|String
name|routeId
parameter_list|,
name|String
name|camelContextName
parameter_list|)
block|{
name|CamelContext
name|context
init|=
name|this
operator|.
name|getCamelContext
argument_list|(
name|camelContextName
argument_list|)
decl_stmt|;
if|if
condition|(
name|context
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|RouteDefinition
name|route
init|=
name|context
operator|.
name|getRouteDefinition
argument_list|(
name|routeId
argument_list|)
decl_stmt|;
if|if
condition|(
name|route
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|String
name|xml
decl_stmt|;
try|try
block|{
name|xml
operator|=
name|ModelHelper
operator|.
name|dumpModelAsXml
argument_list|(
name|route
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|JAXBException
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
return|return
name|xml
return|;
block|}
annotation|@
name|Override
DECL|method|getRouteStatsAsXml (String routeId, String camelContextName, boolean fullStats, boolean includeProcessors)
specifier|public
name|String
name|getRouteStatsAsXml
parameter_list|(
name|String
name|routeId
parameter_list|,
name|String
name|camelContextName
parameter_list|,
name|boolean
name|fullStats
parameter_list|,
name|boolean
name|includeProcessors
parameter_list|)
block|{
name|CamelContext
name|context
init|=
name|this
operator|.
name|getCamelContext
argument_list|(
name|camelContextName
argument_list|)
decl_stmt|;
if|if
condition|(
name|context
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
try|try
block|{
name|ManagementAgent
name|agent
init|=
name|context
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
name|mBeanServer
init|=
name|agent
operator|.
name|getMBeanServer
argument_list|()
decl_stmt|;
name|Set
argument_list|<
name|ObjectName
argument_list|>
name|set
init|=
name|mBeanServer
operator|.
name|queryNames
argument_list|(
operator|new
name|ObjectName
argument_list|(
name|agent
operator|.
name|getMBeanObjectDomainName
argument_list|()
operator|+
literal|":type=routes,name=\""
operator|+
name|routeId
operator|+
literal|"\",*"
argument_list|)
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|Iterator
argument_list|<
name|ObjectName
argument_list|>
name|iterator
init|=
name|set
operator|.
name|iterator
argument_list|()
decl_stmt|;
if|if
condition|(
name|iterator
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|ObjectName
name|routeMBean
init|=
name|iterator
operator|.
name|next
argument_list|()
decl_stmt|;
comment|// the route must be part of the camel context
name|String
name|camelId
init|=
operator|(
name|String
operator|)
name|mBeanServer
operator|.
name|getAttribute
argument_list|(
name|routeMBean
argument_list|,
literal|"CamelId"
argument_list|)
decl_stmt|;
if|if
condition|(
name|camelId
operator|!=
literal|null
operator|&&
name|camelId
operator|.
name|equals
argument_list|(
name|camelContextName
argument_list|)
condition|)
block|{
name|String
name|xml
init|=
operator|(
name|String
operator|)
name|mBeanServer
operator|.
name|invoke
argument_list|(
name|routeMBean
argument_list|,
literal|"dumpRouteStatsAsXml"
argument_list|,
operator|new
name|Object
index|[]
block|{
name|fullStats
block|,
name|includeProcessors
block|}
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"boolean"
block|,
literal|"boolean"
block|}
argument_list|)
decl_stmt|;
return|return
name|xml
return|;
block|}
block|}
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
return|return
literal|null
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"deprecation"
argument_list|)
DECL|method|getRestModelAsXml (String camelContextName)
specifier|public
name|String
name|getRestModelAsXml
parameter_list|(
name|String
name|camelContextName
parameter_list|)
block|{
name|CamelContext
name|context
init|=
name|this
operator|.
name|getCamelContext
argument_list|(
name|camelContextName
argument_list|)
decl_stmt|;
if|if
condition|(
name|context
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|List
argument_list|<
name|RestDefinition
argument_list|>
name|rests
init|=
name|context
operator|.
name|getRestDefinitions
argument_list|()
decl_stmt|;
if|if
condition|(
name|rests
operator|==
literal|null
operator|||
name|rests
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
comment|// use a rests definition to dump the rests
name|RestsDefinition
name|def
init|=
operator|new
name|RestsDefinition
argument_list|()
decl_stmt|;
name|def
operator|.
name|setRests
argument_list|(
name|rests
argument_list|)
expr_stmt|;
name|String
name|xml
decl_stmt|;
try|try
block|{
name|xml
operator|=
name|ModelHelper
operator|.
name|dumpModelAsXml
argument_list|(
name|def
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|JAXBException
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
return|return
name|xml
return|;
block|}
DECL|method|getEndpoints (String camelContextName)
specifier|public
name|List
argument_list|<
name|Endpoint
argument_list|>
name|getEndpoints
parameter_list|(
name|String
name|camelContextName
parameter_list|)
block|{
name|List
argument_list|<
name|Endpoint
argument_list|>
name|answer
init|=
operator|new
name|ArrayList
argument_list|<
name|Endpoint
argument_list|>
argument_list|()
decl_stmt|;
if|if
condition|(
name|camelContextName
operator|!=
literal|null
condition|)
block|{
name|CamelContext
name|context
init|=
name|this
operator|.
name|getCamelContext
argument_list|(
name|camelContextName
argument_list|)
decl_stmt|;
if|if
condition|(
name|context
operator|!=
literal|null
condition|)
block|{
name|List
argument_list|<
name|Endpoint
argument_list|>
name|endpoints
init|=
operator|new
name|ArrayList
argument_list|<
name|Endpoint
argument_list|>
argument_list|(
name|context
operator|.
name|getEndpoints
argument_list|()
argument_list|)
decl_stmt|;
comment|// sort routes
name|Collections
operator|.
name|sort
argument_list|(
name|endpoints
argument_list|,
operator|new
name|Comparator
argument_list|<
name|Endpoint
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|int
name|compare
parameter_list|(
name|Endpoint
name|o1
parameter_list|,
name|Endpoint
name|o2
parameter_list|)
block|{
return|return
name|o1
operator|.
name|getEndpointKey
argument_list|()
operator|.
name|compareTo
argument_list|(
name|o2
operator|.
name|getEndpointKey
argument_list|()
argument_list|)
return|;
block|}
block|}
argument_list|)
expr_stmt|;
name|answer
operator|.
name|addAll
argument_list|(
name|endpoints
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
comment|// already sorted by camel context
name|List
argument_list|<
name|CamelContext
argument_list|>
name|camelContexts
init|=
name|this
operator|.
name|getCamelContexts
argument_list|()
decl_stmt|;
for|for
control|(
name|CamelContext
name|camelContext
range|:
name|camelContexts
control|)
block|{
name|List
argument_list|<
name|Endpoint
argument_list|>
name|endpoints
init|=
operator|new
name|ArrayList
argument_list|<
name|Endpoint
argument_list|>
argument_list|(
name|camelContext
operator|.
name|getEndpoints
argument_list|()
argument_list|)
decl_stmt|;
comment|// sort routes
name|Collections
operator|.
name|sort
argument_list|(
name|endpoints
argument_list|,
operator|new
name|Comparator
argument_list|<
name|Endpoint
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|int
name|compare
parameter_list|(
name|Endpoint
name|o1
parameter_list|,
name|Endpoint
name|o2
parameter_list|)
block|{
return|return
name|o1
operator|.
name|getEndpointKey
argument_list|()
operator|.
name|compareTo
argument_list|(
name|o2
operator|.
name|getEndpointKey
argument_list|()
argument_list|)
return|;
block|}
block|}
argument_list|)
expr_stmt|;
name|answer
operator|.
name|addAll
argument_list|(
name|endpoints
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|answer
return|;
block|}
DECL|method|getRestServices (String camelContextName)
specifier|public
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
name|getRestServices
parameter_list|(
name|String
name|camelContextName
parameter_list|)
block|{
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
name|answer
init|=
operator|new
name|ArrayList
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
if|if
condition|(
name|camelContextName
operator|!=
literal|null
condition|)
block|{
name|CamelContext
name|context
init|=
name|this
operator|.
name|getCamelContext
argument_list|(
name|camelContextName
argument_list|)
decl_stmt|;
if|if
condition|(
name|context
operator|!=
literal|null
condition|)
block|{
name|List
argument_list|<
name|RestRegistry
operator|.
name|RestService
argument_list|>
name|services
init|=
operator|new
name|ArrayList
argument_list|<
name|RestRegistry
operator|.
name|RestService
argument_list|>
argument_list|(
name|context
operator|.
name|getRestRegistry
argument_list|()
operator|.
name|listAllRestServices
argument_list|()
argument_list|)
decl_stmt|;
name|Collections
operator|.
name|sort
argument_list|(
name|services
argument_list|,
operator|new
name|Comparator
argument_list|<
name|RestRegistry
operator|.
name|RestService
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|int
name|compare
parameter_list|(
name|RestRegistry
operator|.
name|RestService
name|o1
parameter_list|,
name|RestRegistry
operator|.
name|RestService
name|o2
parameter_list|)
block|{
return|return
name|o1
operator|.
name|getUrl
argument_list|()
operator|.
name|compareTo
argument_list|(
name|o2
operator|.
name|getUrl
argument_list|()
argument_list|)
return|;
block|}
block|}
argument_list|)
expr_stmt|;
for|for
control|(
name|RestRegistry
operator|.
name|RestService
name|service
range|:
name|services
control|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|row
init|=
operator|new
name|LinkedHashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|row
operator|.
name|put
argument_list|(
literal|"basePath"
argument_list|,
name|service
operator|.
name|getBasePath
argument_list|()
argument_list|)
expr_stmt|;
name|row
operator|.
name|put
argument_list|(
literal|"baseUrl"
argument_list|,
name|service
operator|.
name|getBaseUrl
argument_list|()
argument_list|)
expr_stmt|;
name|row
operator|.
name|put
argument_list|(
literal|"consumes"
argument_list|,
name|service
operator|.
name|getConsumes
argument_list|()
argument_list|)
expr_stmt|;
name|row
operator|.
name|put
argument_list|(
literal|"description"
argument_list|,
name|service
operator|.
name|getDescription
argument_list|()
argument_list|)
expr_stmt|;
name|row
operator|.
name|put
argument_list|(
literal|"inType"
argument_list|,
name|service
operator|.
name|getInType
argument_list|()
argument_list|)
expr_stmt|;
name|row
operator|.
name|put
argument_list|(
literal|"method"
argument_list|,
name|service
operator|.
name|getMethod
argument_list|()
argument_list|)
expr_stmt|;
name|row
operator|.
name|put
argument_list|(
literal|"outType"
argument_list|,
name|service
operator|.
name|getOutType
argument_list|()
argument_list|)
expr_stmt|;
name|row
operator|.
name|put
argument_list|(
literal|"produces"
argument_list|,
name|service
operator|.
name|getProduces
argument_list|()
argument_list|)
expr_stmt|;
name|row
operator|.
name|put
argument_list|(
literal|"routeId"
argument_list|,
name|service
operator|.
name|getRouteId
argument_list|()
argument_list|)
expr_stmt|;
name|row
operator|.
name|put
argument_list|(
literal|"state"
argument_list|,
name|service
operator|.
name|getState
argument_list|()
argument_list|)
expr_stmt|;
name|row
operator|.
name|put
argument_list|(
literal|"uriTemplate"
argument_list|,
name|service
operator|.
name|getUriTemplate
argument_list|()
argument_list|)
expr_stmt|;
name|row
operator|.
name|put
argument_list|(
literal|"url"
argument_list|,
name|service
operator|.
name|getUrl
argument_list|()
argument_list|)
expr_stmt|;
name|answer
operator|.
name|add
argument_list|(
name|row
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|answer
return|;
block|}
DECL|method|explainEndpointAsJSon (String camelContextName, String uri, boolean allOptions)
specifier|public
name|String
name|explainEndpointAsJSon
parameter_list|(
name|String
name|camelContextName
parameter_list|,
name|String
name|uri
parameter_list|,
name|boolean
name|allOptions
parameter_list|)
throws|throws
name|Exception
block|{
name|CamelContext
name|context
init|=
name|this
operator|.
name|getCamelContext
argument_list|(
name|camelContextName
argument_list|)
decl_stmt|;
if|if
condition|(
name|context
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|context
operator|.
name|explainEndpointJson
argument_list|(
name|uri
argument_list|,
name|allOptions
argument_list|)
return|;
block|}
DECL|method|listComponents (String camelContextName)
specifier|public
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
name|listComponents
parameter_list|(
name|String
name|camelContextName
parameter_list|)
throws|throws
name|Exception
block|{
name|CamelContext
name|context
init|=
name|this
operator|.
name|getCamelContext
argument_list|(
name|camelContextName
argument_list|)
decl_stmt|;
if|if
condition|(
name|context
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
name|answer
init|=
operator|new
name|ArrayList
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
comment|// find all components
name|Map
argument_list|<
name|String
argument_list|,
name|Properties
argument_list|>
name|components
init|=
name|context
operator|.
name|findComponents
argument_list|()
decl_stmt|;
comment|// gather component detail for each component
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Properties
argument_list|>
name|entry
range|:
name|components
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|String
name|name
init|=
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|String
name|description
init|=
literal|null
decl_stmt|;
name|String
name|label
init|=
literal|null
decl_stmt|;
comment|// the status can be:
comment|// - loaded = in use
comment|// - classpath = on the classpath
comment|// - release = available from the Apache Camel release
name|String
name|status
init|=
name|context
operator|.
name|hasComponent
argument_list|(
name|name
argument_list|)
operator|!=
literal|null
condition|?
literal|"in use"
else|:
literal|"on classpath"
decl_stmt|;
name|String
name|type
init|=
literal|null
decl_stmt|;
name|String
name|groupId
init|=
literal|null
decl_stmt|;
name|String
name|artifactId
init|=
literal|null
decl_stmt|;
name|String
name|version
init|=
literal|null
decl_stmt|;
comment|// load component json data, and parse it to gather the component meta-data
name|String
name|json
init|=
name|context
operator|.
name|getComponentParameterJsonSchema
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
name|rows
init|=
name|JsonSchemaHelper
operator|.
name|parseJsonSchema
argument_list|(
literal|"component"
argument_list|,
name|json
argument_list|,
literal|false
argument_list|)
decl_stmt|;
for|for
control|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|row
range|:
name|rows
control|)
block|{
if|if
condition|(
name|row
operator|.
name|containsKey
argument_list|(
literal|"description"
argument_list|)
condition|)
block|{
name|description
operator|=
name|row
operator|.
name|get
argument_list|(
literal|"description"
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|row
operator|.
name|containsKey
argument_list|(
literal|"label"
argument_list|)
condition|)
block|{
name|label
operator|=
name|row
operator|.
name|get
argument_list|(
literal|"label"
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|row
operator|.
name|containsKey
argument_list|(
literal|"javaType"
argument_list|)
condition|)
block|{
name|type
operator|=
name|row
operator|.
name|get
argument_list|(
literal|"javaType"
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|row
operator|.
name|containsKey
argument_list|(
literal|"groupId"
argument_list|)
condition|)
block|{
name|groupId
operator|=
name|row
operator|.
name|get
argument_list|(
literal|"groupId"
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|row
operator|.
name|containsKey
argument_list|(
literal|"artifactId"
argument_list|)
condition|)
block|{
name|artifactId
operator|=
name|row
operator|.
name|get
argument_list|(
literal|"artifactId"
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|row
operator|.
name|containsKey
argument_list|(
literal|"version"
argument_list|)
condition|)
block|{
name|version
operator|=
name|row
operator|.
name|get
argument_list|(
literal|"version"
argument_list|)
expr_stmt|;
block|}
block|}
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|row
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|row
operator|.
name|put
argument_list|(
literal|"name"
argument_list|,
name|name
argument_list|)
expr_stmt|;
name|row
operator|.
name|put
argument_list|(
literal|"status"
argument_list|,
name|status
argument_list|)
expr_stmt|;
if|if
condition|(
name|description
operator|!=
literal|null
condition|)
block|{
name|row
operator|.
name|put
argument_list|(
literal|"description"
argument_list|,
name|description
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|label
operator|!=
literal|null
condition|)
block|{
name|row
operator|.
name|put
argument_list|(
literal|"label"
argument_list|,
name|label
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|type
operator|!=
literal|null
condition|)
block|{
name|row
operator|.
name|put
argument_list|(
literal|"type"
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|groupId
operator|!=
literal|null
condition|)
block|{
name|row
operator|.
name|put
argument_list|(
literal|"groupId"
argument_list|,
name|groupId
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|artifactId
operator|!=
literal|null
condition|)
block|{
name|row
operator|.
name|put
argument_list|(
literal|"artifactId"
argument_list|,
name|artifactId
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|version
operator|!=
literal|null
condition|)
block|{
name|row
operator|.
name|put
argument_list|(
literal|"version"
argument_list|,
name|version
argument_list|)
expr_stmt|;
block|}
name|answer
operator|.
name|add
argument_list|(
name|row
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
annotation|@
name|Override
DECL|method|listComponentsCatalog (String filter)
specifier|public
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
name|listComponentsCatalog
parameter_list|(
name|String
name|filter
parameter_list|)
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
name|answer
init|=
operator|new
name|ArrayList
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
if|if
condition|(
name|filter
operator|!=
literal|null
condition|)
block|{
name|filter
operator|=
name|RegexUtil
operator|.
name|wildcardAsRegex
argument_list|(
name|filter
argument_list|)
expr_stmt|;
block|}
name|List
argument_list|<
name|String
argument_list|>
name|names
init|=
name|filter
operator|!=
literal|null
condition|?
name|catalog
operator|.
name|findComponentNames
argument_list|(
name|filter
argument_list|)
else|:
name|catalog
operator|.
name|findComponentNames
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|name
range|:
name|names
control|)
block|{
comment|// load component json data, and parse it to gather the component meta-data
name|String
name|json
init|=
name|catalog
operator|.
name|componentJSonSchema
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
name|rows
init|=
name|JsonSchemaHelper
operator|.
name|parseJsonSchema
argument_list|(
literal|"component"
argument_list|,
name|json
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|String
name|description
init|=
literal|null
decl_stmt|;
name|String
name|label
init|=
literal|null
decl_stmt|;
comment|// the status can be:
comment|// - loaded = in use
comment|// - classpath = on the classpath
comment|// - release = available from the Apache Camel release
name|String
name|status
init|=
literal|"release"
decl_stmt|;
name|String
name|type
init|=
literal|null
decl_stmt|;
name|String
name|groupId
init|=
literal|null
decl_stmt|;
name|String
name|artifactId
init|=
literal|null
decl_stmt|;
name|String
name|version
init|=
literal|null
decl_stmt|;
for|for
control|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|row
range|:
name|rows
control|)
block|{
if|if
condition|(
name|row
operator|.
name|containsKey
argument_list|(
literal|"description"
argument_list|)
condition|)
block|{
name|description
operator|=
name|row
operator|.
name|get
argument_list|(
literal|"description"
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|row
operator|.
name|containsKey
argument_list|(
literal|"label"
argument_list|)
condition|)
block|{
name|label
operator|=
name|row
operator|.
name|get
argument_list|(
literal|"label"
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|row
operator|.
name|containsKey
argument_list|(
literal|"javaType"
argument_list|)
condition|)
block|{
name|type
operator|=
name|row
operator|.
name|get
argument_list|(
literal|"javaType"
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|row
operator|.
name|containsKey
argument_list|(
literal|"groupId"
argument_list|)
condition|)
block|{
name|groupId
operator|=
name|row
operator|.
name|get
argument_list|(
literal|"groupId"
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|row
operator|.
name|containsKey
argument_list|(
literal|"artifactId"
argument_list|)
condition|)
block|{
name|artifactId
operator|=
name|row
operator|.
name|get
argument_list|(
literal|"artifactId"
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|row
operator|.
name|containsKey
argument_list|(
literal|"version"
argument_list|)
condition|)
block|{
name|version
operator|=
name|row
operator|.
name|get
argument_list|(
literal|"version"
argument_list|)
expr_stmt|;
block|}
block|}
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|row
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|row
operator|.
name|put
argument_list|(
literal|"name"
argument_list|,
name|name
argument_list|)
expr_stmt|;
name|row
operator|.
name|put
argument_list|(
literal|"status"
argument_list|,
name|status
argument_list|)
expr_stmt|;
if|if
condition|(
name|description
operator|!=
literal|null
condition|)
block|{
name|row
operator|.
name|put
argument_list|(
literal|"description"
argument_list|,
name|description
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|label
operator|!=
literal|null
condition|)
block|{
name|row
operator|.
name|put
argument_list|(
literal|"label"
argument_list|,
name|label
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|type
operator|!=
literal|null
condition|)
block|{
name|row
operator|.
name|put
argument_list|(
literal|"type"
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|groupId
operator|!=
literal|null
condition|)
block|{
name|row
operator|.
name|put
argument_list|(
literal|"groupId"
argument_list|,
name|groupId
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|artifactId
operator|!=
literal|null
condition|)
block|{
name|row
operator|.
name|put
argument_list|(
literal|"artifactId"
argument_list|,
name|artifactId
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|version
operator|!=
literal|null
condition|)
block|{
name|row
operator|.
name|put
argument_list|(
literal|"version"
argument_list|,
name|version
argument_list|)
expr_stmt|;
block|}
name|answer
operator|.
name|add
argument_list|(
name|row
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
annotation|@
name|Override
DECL|method|listLabelCatalog ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Set
argument_list|<
name|String
argument_list|>
argument_list|>
name|listLabelCatalog
parameter_list|()
throws|throws
name|Exception
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Set
argument_list|<
name|String
argument_list|>
argument_list|>
name|answer
init|=
operator|new
name|LinkedHashMap
argument_list|<
name|String
argument_list|,
name|Set
argument_list|<
name|String
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|labels
init|=
name|catalog
operator|.
name|findLabels
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|label
range|:
name|labels
control|)
block|{
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
name|components
init|=
name|listComponentsCatalog
argument_list|(
name|label
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|components
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|names
init|=
operator|new
name|LinkedHashSet
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|info
range|:
name|components
control|)
block|{
name|String
name|name
init|=
name|info
operator|.
name|get
argument_list|(
literal|"name"
argument_list|)
decl_stmt|;
if|if
condition|(
name|name
operator|!=
literal|null
condition|)
block|{
name|names
operator|.
name|add
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
block|}
name|answer
operator|.
name|put
argument_list|(
name|label
argument_list|,
name|names
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|answer
return|;
block|}
block|}
end_class

end_unit

