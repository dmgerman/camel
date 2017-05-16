begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.support
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|support
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayInputStream
import|;
end_import

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
name|w3c
operator|.
name|dom
operator|.
name|Document
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Node
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|NodeList
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
name|api
operator|.
name|management
operator|.
name|ManagedAttribute
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
name|api
operator|.
name|management
operator|.
name|ManagedOperation
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
name|RoutesDefinition
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
name|ReloadStrategy
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
name|CollectionStringBuffer
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
name|LRUCache
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
name|StringHelper
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
name|XmlLineNumberParser
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

begin_comment
comment|/**  * Base class for implementing custom {@link ReloadStrategy} SPI plugins.  */
end_comment

begin_class
DECL|class|ReloadStrategySupport
specifier|public
specifier|abstract
class|class
name|ReloadStrategySupport
extends|extends
name|ServiceSupport
implements|implements
name|ReloadStrategy
block|{
DECL|field|log
specifier|protected
specifier|final
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
DECL|field|camelContext
specifier|private
name|CamelContext
name|camelContext
decl_stmt|;
comment|// store state
DECL|field|cache
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|ResourceState
argument_list|>
name|cache
init|=
operator|new
name|LRUCache
argument_list|<
name|String
argument_list|,
name|ResourceState
argument_list|>
argument_list|(
literal|100
argument_list|)
decl_stmt|;
DECL|field|succeeded
specifier|private
name|int
name|succeeded
decl_stmt|;
DECL|field|failed
specifier|private
name|int
name|failed
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
annotation|@
name|Override
DECL|method|onReloadXml (CamelContext camelContext, String name, InputStream resource)
specifier|public
name|void
name|onReloadXml
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|String
name|name
parameter_list|,
name|InputStream
name|resource
parameter_list|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Reloading routes from XML resource: {}"
argument_list|,
name|name
argument_list|)
expr_stmt|;
name|Document
name|dom
decl_stmt|;
name|String
name|xml
decl_stmt|;
try|try
block|{
name|xml
operator|=
name|camelContext
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|mandatoryConvertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|resource
argument_list|)
expr_stmt|;
comment|// the JAXB model expects the spring namespace (even for blueprint)
name|dom
operator|=
name|XmlLineNumberParser
operator|.
name|parseXml
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|xml
operator|.
name|getBytes
argument_list|()
argument_list|)
argument_list|,
literal|null
argument_list|,
literal|"camelContext,routeContext,routes"
argument_list|,
literal|"http://camel.apache.org/schema/spring"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|failed
operator|++
expr_stmt|;
name|log
operator|.
name|warn
argument_list|(
literal|"Cannot load the resource "
operator|+
name|name
operator|+
literal|" as XML"
argument_list|)
expr_stmt|;
return|return;
block|}
name|ResourceState
name|state
init|=
name|cache
operator|.
name|get
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|state
operator|==
literal|null
condition|)
block|{
name|state
operator|=
operator|new
name|ResourceState
argument_list|(
name|name
argument_list|,
name|dom
argument_list|,
name|xml
argument_list|)
expr_stmt|;
name|cache
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|state
argument_list|)
expr_stmt|;
block|}
name|String
name|oldXml
init|=
name|state
operator|.
name|getXml
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|Integer
argument_list|>
name|changed
init|=
name|StringHelper
operator|.
name|changedLines
argument_list|(
name|oldXml
argument_list|,
name|xml
argument_list|)
decl_stmt|;
comment|// find all<route> which are the routes
name|NodeList
name|list
init|=
name|dom
operator|.
name|getElementsByTagName
argument_list|(
literal|"route"
argument_list|)
decl_stmt|;
comment|// collect which routes are updated/skipped
name|List
argument_list|<
name|RouteDefinition
argument_list|>
name|routes
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
if|if
condition|(
name|list
operator|!=
literal|null
operator|&&
name|list
operator|.
name|getLength
argument_list|()
operator|>
literal|0
condition|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|list
operator|.
name|getLength
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|Node
name|node
init|=
name|list
operator|.
name|item
argument_list|(
name|i
argument_list|)
decl_stmt|;
comment|// what line number are this within
name|String
name|lineNumber
init|=
operator|(
name|String
operator|)
name|node
operator|.
name|getUserData
argument_list|(
name|XmlLineNumberParser
operator|.
name|LINE_NUMBER
argument_list|)
decl_stmt|;
name|String
name|lineNumberEnd
init|=
operator|(
name|String
operator|)
name|node
operator|.
name|getUserData
argument_list|(
name|XmlLineNumberParser
operator|.
name|LINE_NUMBER_END
argument_list|)
decl_stmt|;
if|if
condition|(
name|lineNumber
operator|!=
literal|null
operator|&&
name|lineNumberEnd
operator|!=
literal|null
operator|&&
operator|!
name|changed
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|int
name|start
init|=
name|Integer
operator|.
name|valueOf
argument_list|(
name|lineNumber
argument_list|)
decl_stmt|;
name|int
name|end
init|=
name|Integer
operator|.
name|valueOf
argument_list|(
name|lineNumberEnd
argument_list|)
decl_stmt|;
name|boolean
name|within
init|=
name|withinChanged
argument_list|(
name|start
argument_list|,
name|end
argument_list|,
name|changed
argument_list|)
decl_stmt|;
if|if
condition|(
name|within
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Updating route in lines: {}-{}"
argument_list|,
name|start
argument_list|,
name|end
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"No changes to route in lines: {}-{}"
argument_list|,
name|start
argument_list|,
name|end
argument_list|)
expr_stmt|;
continue|continue;
block|}
block|}
try|try
block|{
comment|// load from XML -> JAXB model and store as routes to be updated
name|RoutesDefinition
name|loaded
init|=
name|ModelHelper
operator|.
name|loadRoutesDefinition
argument_list|(
name|camelContext
argument_list|,
name|node
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|loaded
operator|.
name|getRoutes
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|routes
operator|.
name|addAll
argument_list|(
name|loaded
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
name|failed
operator|++
expr_stmt|;
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
if|if
condition|(
operator|!
name|routes
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
try|try
block|{
name|boolean
name|unassignedRouteIds
init|=
literal|false
decl_stmt|;
name|CollectionStringBuffer
name|csb
init|=
operator|new
name|CollectionStringBuffer
argument_list|(
literal|","
argument_list|)
decl_stmt|;
comment|// collect route ids and force assign ids if not in use
for|for
control|(
name|RouteDefinition
name|route
range|:
name|routes
control|)
block|{
name|unassignedRouteIds
operator||=
operator|!
name|route
operator|.
name|hasCustomIdAssigned
argument_list|()
expr_stmt|;
name|String
name|id
init|=
name|route
operator|.
name|idOrCreate
argument_list|(
name|camelContext
operator|.
name|getNodeIdFactory
argument_list|()
argument_list|)
decl_stmt|;
name|csb
operator|.
name|append
argument_list|(
name|id
argument_list|)
expr_stmt|;
block|}
name|log
operator|.
name|debug
argument_list|(
literal|"Reloading routes: [{}] from XML resource: {}"
argument_list|,
name|csb
argument_list|,
name|name
argument_list|)
expr_stmt|;
if|if
condition|(
name|unassignedRouteIds
condition|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Routes with no id's detected. Its recommended to assign id's to your routes so Camel can reload the routes correctly."
argument_list|)
expr_stmt|;
block|}
comment|// update the routes (add will remove and shutdown first)
name|camelContext
operator|.
name|addRouteDefinitions
argument_list|(
name|routes
argument_list|)
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Reloaded routes: [{}] from XML resource: {}"
argument_list|,
name|csb
argument_list|,
name|name
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|failed
operator|++
expr_stmt|;
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
comment|// update cache
name|state
operator|=
operator|new
name|ResourceState
argument_list|(
name|name
argument_list|,
name|dom
argument_list|,
name|xml
argument_list|)
expr_stmt|;
name|cache
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|state
argument_list|)
expr_stmt|;
name|succeeded
operator|++
expr_stmt|;
block|}
DECL|method|withinChanged (int start, int end, List<Integer> changed)
specifier|private
name|boolean
name|withinChanged
parameter_list|(
name|int
name|start
parameter_list|,
name|int
name|end
parameter_list|,
name|List
argument_list|<
name|Integer
argument_list|>
name|changed
parameter_list|)
block|{
for|for
control|(
name|int
name|change
range|:
name|changed
control|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Changed line: {} within {}-{}"
argument_list|,
name|change
argument_list|,
name|start
argument_list|,
name|end
argument_list|)
expr_stmt|;
if|if
condition|(
name|change
operator|>=
name|start
operator|&&
name|change
operator|<=
name|end
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Number of reloads succeeded"
argument_list|)
DECL|method|getReloadCounter ()
specifier|public
name|int
name|getReloadCounter
parameter_list|()
block|{
return|return
name|succeeded
return|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Number of reloads failed"
argument_list|)
DECL|method|getFailedCounter ()
specifier|public
name|int
name|getFailedCounter
parameter_list|()
block|{
return|return
name|failed
return|;
block|}
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Reset counters"
argument_list|)
DECL|method|resetCounters ()
specifier|public
name|void
name|resetCounters
parameter_list|()
block|{
name|succeeded
operator|=
literal|0
expr_stmt|;
name|failed
operator|=
literal|0
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
comment|// noop
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
name|cache
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
comment|/**      * To keep state of last reloaded resource      */
DECL|class|ResourceState
specifier|private
specifier|static
specifier|final
class|class
name|ResourceState
block|{
DECL|field|name
specifier|private
specifier|final
name|String
name|name
decl_stmt|;
DECL|field|dom
specifier|private
specifier|final
name|Document
name|dom
decl_stmt|;
DECL|field|xml
specifier|private
specifier|final
name|String
name|xml
decl_stmt|;
DECL|method|ResourceState (String name, Document dom, String xml)
name|ResourceState
parameter_list|(
name|String
name|name
parameter_list|,
name|Document
name|dom
parameter_list|,
name|String
name|xml
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
name|this
operator|.
name|dom
operator|=
name|dom
expr_stmt|;
name|this
operator|.
name|xml
operator|=
name|xml
expr_stmt|;
block|}
DECL|method|getName ()
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
DECL|method|getDom ()
specifier|public
name|Document
name|getDom
parameter_list|()
block|{
return|return
name|dom
return|;
block|}
DECL|method|getXml ()
specifier|public
name|String
name|getXml
parameter_list|()
block|{
return|return
name|xml
return|;
block|}
block|}
block|}
end_class

end_unit

