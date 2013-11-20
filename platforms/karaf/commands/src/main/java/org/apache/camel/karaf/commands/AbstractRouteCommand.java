begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.karaf.commands
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
package|;
end_package

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
name|felix
operator|.
name|gogo
operator|.
name|commands
operator|.
name|Argument
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
name|util
operator|.
name|CamelContextHelper
operator|.
name|getRouteStartupOrder
import|;
end_import

begin_class
DECL|class|AbstractRouteCommand
specifier|public
specifier|abstract
class|class
name|AbstractRouteCommand
extends|extends
name|CamelCommandSupport
block|{
annotation|@
name|Argument
argument_list|(
name|index
operator|=
literal|0
argument_list|,
name|name
operator|=
literal|"route"
argument_list|,
name|description
operator|=
literal|"The Camel route ID or a wildcard expression"
argument_list|,
name|required
operator|=
literal|true
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|)
DECL|field|route
name|String
name|route
decl_stmt|;
annotation|@
name|Argument
argument_list|(
name|index
operator|=
literal|1
argument_list|,
name|name
operator|=
literal|"context"
argument_list|,
name|description
operator|=
literal|"The Camel context name."
argument_list|,
name|required
operator|=
literal|false
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|)
DECL|field|context
name|String
name|context
decl_stmt|;
DECL|method|executeOnRoute (CamelContext camelContext, Route camelRoute)
specifier|public
specifier|abstract
name|void
name|executeOnRoute
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|Route
name|camelRoute
parameter_list|)
throws|throws
name|Exception
function_decl|;
DECL|method|doExecute ()
specifier|public
name|Object
name|doExecute
parameter_list|()
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|Route
argument_list|>
name|camelRoutes
init|=
name|camelController
operator|.
name|getRoutes
argument_list|(
name|context
argument_list|,
name|RegexUtil
operator|.
name|wildcardAsRegex
argument_list|(
name|route
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|camelRoutes
operator|==
literal|null
operator|||
name|camelRoutes
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Camel routes using "
operator|+
name|route
operator|+
literal|" not found."
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
comment|// we want the routes sorted
name|Collections
operator|.
name|sort
argument_list|(
name|camelRoutes
argument_list|,
operator|new
name|RouteComparator
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|Route
name|camelRoute
range|:
name|camelRoutes
control|)
block|{
name|CamelContext
name|camelContext
init|=
name|camelRoute
operator|.
name|getRouteContext
argument_list|()
operator|.
name|getCamelContext
argument_list|()
decl_stmt|;
comment|// Setting thread context classloader to the bundle classloader to enable
comment|// legacy code that relies on it
name|ClassLoader
name|oldClassloader
init|=
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
decl_stmt|;
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|setContextClassLoader
argument_list|(
name|camelContext
operator|.
name|getApplicationContextClassLoader
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|executeOnRoute
argument_list|(
name|camelContext
argument_list|,
name|camelRoute
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|setContextClassLoader
argument_list|(
name|oldClassloader
argument_list|)
expr_stmt|;
block|}
block|}
return|return
literal|null
return|;
block|}
comment|/**      * To sort the routes.      */
DECL|class|RouteComparator
specifier|private
specifier|static
specifier|final
class|class
name|RouteComparator
implements|implements
name|Comparator
argument_list|<
name|Route
argument_list|>
block|{
annotation|@
name|Override
DECL|method|compare (Route route1, Route route2)
specifier|public
name|int
name|compare
parameter_list|(
name|Route
name|route1
parameter_list|,
name|Route
name|route2
parameter_list|)
block|{
comment|// sort by camel context first
name|CamelContext
name|camel1
init|=
name|route1
operator|.
name|getRouteContext
argument_list|()
operator|.
name|getCamelContext
argument_list|()
decl_stmt|;
name|CamelContext
name|camel2
init|=
name|route2
operator|.
name|getRouteContext
argument_list|()
operator|.
name|getCamelContext
argument_list|()
decl_stmt|;
if|if
condition|(
name|camel1
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|camel2
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
comment|// and then accordingly to startup order
name|Integer
name|order1
init|=
name|getRouteStartupOrder
argument_list|(
name|camel1
argument_list|,
name|route1
operator|.
name|getId
argument_list|()
argument_list|)
decl_stmt|;
name|Integer
name|order2
init|=
name|getRouteStartupOrder
argument_list|(
name|camel2
argument_list|,
name|route2
operator|.
name|getId
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|order1
operator|==
literal|0
operator|&&
name|order2
operator|==
literal|0
condition|)
block|{
comment|// fallback and use name if not startup order was found
return|return
name|route1
operator|.
name|getId
argument_list|()
operator|.
name|compareTo
argument_list|(
name|route2
operator|.
name|getId
argument_list|()
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|order1
operator|.
name|compareTo
argument_list|(
name|order2
argument_list|)
return|;
block|}
block|}
else|else
block|{
return|return
name|camel1
operator|.
name|getName
argument_list|()
operator|.
name|compareTo
argument_list|(
name|camel2
operator|.
name|getName
argument_list|()
argument_list|)
return|;
block|}
block|}
block|}
block|}
end_class

end_unit

