begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|io
operator|.
name|PrintStream
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
name|commands
operator|.
name|internal
operator|.
name|RegexUtil
import|;
end_import

begin_comment
comment|/**  * Abstract command for working with a one ore more routes.  */
end_comment

begin_class
DECL|class|AbstractRouteCommand
specifier|public
specifier|abstract
class|class
name|AbstractRouteCommand
extends|extends
name|AbstractCamelCommand
block|{
DECL|field|route
specifier|private
name|String
name|route
decl_stmt|;
DECL|field|context
specifier|private
name|String
name|context
decl_stmt|;
comment|/**      * @param route The Camel route ID or a wildcard expression      * @param context The name of the Camel context.      */
DECL|method|AbstractRouteCommand (String route, String context)
specifier|protected
name|AbstractRouteCommand
parameter_list|(
name|String
name|route
parameter_list|,
name|String
name|context
parameter_list|)
block|{
name|this
operator|.
name|route
operator|=
name|route
expr_stmt|;
name|this
operator|.
name|context
operator|=
name|context
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|execute (CamelController camelController, PrintStream out, PrintStream err)
specifier|public
name|Object
name|execute
parameter_list|(
name|CamelController
name|camelController
parameter_list|,
name|PrintStream
name|out
parameter_list|,
name|PrintStream
name|err
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
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|row
range|:
name|camelRoutes
control|)
block|{
name|String
name|camelContextName
init|=
name|row
operator|.
name|get
argument_list|(
literal|"camelContextName"
argument_list|)
decl_stmt|;
name|String
name|routeId
init|=
name|row
operator|.
name|get
argument_list|(
literal|"routeId"
argument_list|)
decl_stmt|;
if|if
condition|(
name|camelController
operator|instanceof
name|LocalCamelController
condition|)
block|{
name|executeLocal
argument_list|(
operator|(
name|LocalCamelController
operator|)
name|camelController
argument_list|,
name|camelContextName
argument_list|,
name|routeId
argument_list|,
name|out
argument_list|,
name|err
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|executeOnRoute
argument_list|(
name|camelController
argument_list|,
name|camelContextName
argument_list|,
name|routeId
argument_list|,
name|out
argument_list|,
name|err
argument_list|)
expr_stmt|;
block|}
block|}
return|return
literal|null
return|;
block|}
DECL|method|executeLocal (LocalCamelController camelController, String camelContextName, String routeId, PrintStream out, PrintStream err)
specifier|private
name|void
name|executeLocal
parameter_list|(
name|LocalCamelController
name|camelController
parameter_list|,
name|String
name|camelContextName
parameter_list|,
name|String
name|routeId
parameter_list|,
name|PrintStream
name|out
parameter_list|,
name|PrintStream
name|err
parameter_list|)
throws|throws
name|Exception
block|{
name|CamelContext
name|camelContext
init|=
name|camelController
operator|.
name|getLocalCamelContext
argument_list|(
name|context
argument_list|)
decl_stmt|;
if|if
condition|(
name|camelContext
operator|==
literal|null
condition|)
block|{
name|err
operator|.
name|println
argument_list|(
literal|"Camel context "
operator|+
name|context
operator|+
literal|" not found."
argument_list|)
expr_stmt|;
return|return;
block|}
comment|// Setting thread context classloader to the bundle classloader to enable legacy code that relies on it
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
name|ClassLoader
name|applicationContextClassLoader
init|=
name|camelContext
operator|.
name|getApplicationContextClassLoader
argument_list|()
decl_stmt|;
if|if
condition|(
name|applicationContextClassLoader
operator|!=
literal|null
condition|)
block|{
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|setContextClassLoader
argument_list|(
name|applicationContextClassLoader
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|executeOnRoute
argument_list|(
name|camelController
argument_list|,
name|camelContextName
argument_list|,
name|routeId
argument_list|,
name|out
argument_list|,
name|err
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
DECL|method|executeOnRoute (CamelController camelController, String contextName, String routeId, PrintStream out, PrintStream err)
specifier|public
specifier|abstract
name|void
name|executeOnRoute
parameter_list|(
name|CamelController
name|camelController
parameter_list|,
name|String
name|contextName
parameter_list|,
name|String
name|routeId
parameter_list|,
name|PrintStream
name|out
parameter_list|,
name|PrintStream
name|err
parameter_list|)
throws|throws
name|Exception
function_decl|;
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
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
block|{
annotation|@
name|Override
DECL|method|compare (Map<String, String> route1, Map<String, String> route2)
specifier|public
name|int
name|compare
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|route1
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|route2
parameter_list|)
block|{
comment|// sort by camel context first
name|String
name|camel1
init|=
name|route1
operator|.
name|get
argument_list|(
literal|"camelContextName"
argument_list|)
decl_stmt|;
name|String
name|camel2
init|=
name|route2
operator|.
name|get
argument_list|(
literal|"camelContextName"
argument_list|)
decl_stmt|;
if|if
condition|(
name|camel1
operator|.
name|equals
argument_list|(
name|camel2
argument_list|)
condition|)
block|{
return|return
name|route1
operator|.
name|get
argument_list|(
literal|"routeId"
argument_list|)
operator|.
name|compareTo
argument_list|(
name|route2
operator|.
name|get
argument_list|(
literal|"routeId"
argument_list|)
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|camel1
operator|.
name|compareTo
argument_list|(
name|camel2
argument_list|)
return|;
block|}
block|}
block|}
block|}
end_class

end_unit

