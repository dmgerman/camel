begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.karaf.commands.internal
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|karaf
operator|.
name|commands
operator|.
name|internal
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
name|karaf
operator|.
name|commands
operator|.
name|CamelController
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
name|framework
operator|.
name|ServiceReference
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
comment|/**  * Implementation of<code>CamelConrtoller</code>.  */
end_comment

begin_class
DECL|class|CamelControllerImpl
specifier|public
class|class
name|CamelControllerImpl
implements|implements
name|CamelController
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|CamelControllerImpl
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|bundleContext
specifier|private
name|BundleContext
name|bundleContext
decl_stmt|;
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
DECL|method|getCamelContexts ()
specifier|public
name|List
argument_list|<
name|CamelContext
argument_list|>
name|getCamelContexts
parameter_list|()
block|{
name|ArrayList
argument_list|<
name|CamelContext
argument_list|>
name|camelContexts
init|=
operator|new
name|ArrayList
argument_list|<
name|CamelContext
argument_list|>
argument_list|()
decl_stmt|;
try|try
block|{
name|ServiceReference
index|[]
name|references
init|=
name|bundleContext
operator|.
name|getServiceReferences
argument_list|(
name|CamelContext
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
literal|null
argument_list|)
decl_stmt|;
if|if
condition|(
name|references
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|ServiceReference
name|reference
range|:
name|references
control|)
block|{
if|if
condition|(
name|reference
operator|!=
literal|null
condition|)
block|{
name|CamelContext
name|camelContext
init|=
operator|(
name|CamelContext
operator|)
name|bundleContext
operator|.
name|getService
argument_list|(
name|reference
argument_list|)
decl_stmt|;
if|if
condition|(
name|camelContext
operator|!=
literal|null
condition|)
block|{
name|camelContexts
operator|.
name|add
argument_list|(
name|camelContext
argument_list|)
expr_stmt|;
block|}
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
name|LOG
operator|.
name|warn
argument_list|(
literal|"Cannot retrieve the list of Camel contexts."
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
return|return
name|camelContexts
return|;
block|}
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
return|return
name|context
operator|.
name|getRoutes
argument_list|()
return|;
block|}
block|}
else|else
block|{
name|ArrayList
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
name|routes
operator|.
name|add
argument_list|(
name|route
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|routes
return|;
block|}
return|return
literal|null
return|;
block|}
DECL|method|getRoute (String routeId, String camelContextName)
specifier|public
name|Route
name|getRoute
parameter_list|(
name|String
name|routeId
parameter_list|,
name|String
name|camelContextName
parameter_list|)
block|{
name|List
argument_list|<
name|Route
argument_list|>
name|routes
init|=
name|this
operator|.
name|getRoutes
argument_list|(
name|camelContextName
argument_list|)
decl_stmt|;
for|for
control|(
name|Route
name|route
range|:
name|routes
control|)
block|{
if|if
condition|(
name|route
operator|.
name|getId
argument_list|()
operator|.
name|equals
argument_list|(
name|routeId
argument_list|)
condition|)
block|{
return|return
name|route
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
DECL|method|getRouteDefinition (String routeId, String camelContextName)
specifier|public
name|RouteDefinition
name|getRouteDefinition
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
return|return
name|context
operator|.
name|getRouteDefinition
argument_list|(
name|routeId
argument_list|)
return|;
block|}
block|}
end_class

end_unit

