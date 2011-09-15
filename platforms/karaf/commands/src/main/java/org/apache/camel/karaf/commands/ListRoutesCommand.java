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
name|LinkedList
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
name|Command
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|shell
operator|.
name|console
operator|.
name|OsgiCommandSupport
import|;
end_import

begin_comment
comment|/**  * Command to list all Camel routes.  */
end_comment

begin_class
annotation|@
name|Command
argument_list|(
name|scope
operator|=
literal|"camel"
argument_list|,
name|name
operator|=
literal|"list-routes"
argument_list|,
name|description
operator|=
literal|"List all Camel routes."
argument_list|)
DECL|class|ListRoutesCommand
specifier|public
class|class
name|ListRoutesCommand
extends|extends
name|OsgiCommandSupport
block|{
DECL|field|HEADER_FORMAT
specifier|protected
specifier|static
specifier|final
name|String
name|HEADER_FORMAT
init|=
literal|"%-20s %-20s %-20s"
decl_stmt|;
DECL|field|OUTPUT_FORMAT
specifier|protected
specifier|static
specifier|final
name|String
name|OUTPUT_FORMAT
init|=
literal|"[%-18s] [%-18s] [%-18s]"
decl_stmt|;
DECL|field|UNKNOWN
specifier|protected
specifier|static
specifier|final
name|String
name|UNKNOWN
init|=
literal|"Unknown"
decl_stmt|;
DECL|field|ROUTE_ID
specifier|protected
specifier|static
specifier|final
name|String
name|ROUTE_ID
init|=
literal|"Route Id"
decl_stmt|;
DECL|field|CONTEXT_ID
specifier|protected
specifier|static
specifier|final
name|String
name|CONTEXT_ID
init|=
literal|"Context Name"
decl_stmt|;
DECL|field|STATUS
specifier|protected
specifier|static
specifier|final
name|String
name|STATUS
init|=
literal|"Status"
decl_stmt|;
annotation|@
name|Argument
argument_list|(
name|index
operator|=
literal|0
argument_list|,
name|name
operator|=
literal|"name"
argument_list|,
name|description
operator|=
literal|"The Camel context name where to look for the route"
argument_list|,
name|required
operator|=
literal|false
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|)
DECL|field|name
name|String
name|name
decl_stmt|;
DECL|field|camelController
specifier|private
name|CamelController
name|camelController
decl_stmt|;
DECL|method|setCamelController (CamelController camelController)
specifier|public
name|void
name|setCamelController
parameter_list|(
name|CamelController
name|camelController
parameter_list|)
block|{
name|this
operator|.
name|camelController
operator|=
name|camelController
expr_stmt|;
block|}
DECL|method|doExecute ()
specifier|protected
name|Object
name|doExecute
parameter_list|()
throws|throws
name|Exception
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|String
operator|.
name|format
argument_list|(
name|HEADER_FORMAT
argument_list|,
name|ROUTE_ID
argument_list|,
name|CONTEXT_ID
argument_list|,
name|STATUS
argument_list|)
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|CamelContext
argument_list|>
name|camelContexts
init|=
operator|new
name|LinkedList
argument_list|<
name|CamelContext
argument_list|>
argument_list|()
decl_stmt|;
if|if
condition|(
name|name
operator|!=
literal|null
operator|&&
name|camelController
operator|.
name|getCamelContext
argument_list|(
name|name
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|camelContexts
operator|.
name|add
argument_list|(
name|camelController
operator|.
name|getCamelContext
argument_list|(
name|name
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|camelContexts
operator|=
name|camelController
operator|.
name|getCamelContexts
argument_list|()
expr_stmt|;
block|}
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
name|RouteDefinition
argument_list|>
name|routeDefinitions
init|=
name|camelController
operator|.
name|getRouteDefinitions
argument_list|(
name|camelContext
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|routeDefinitions
operator|!=
literal|null
operator|&&
operator|!
name|routeDefinitions
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
for|for
control|(
name|RouteDefinition
name|routeDefinition
range|:
name|routeDefinitions
control|)
block|{
name|String
name|contextName
init|=
name|camelContext
operator|.
name|getName
argument_list|()
decl_stmt|;
name|String
name|status
init|=
name|camelContext
operator|.
name|getRouteStatus
argument_list|(
name|routeDefinition
operator|.
name|getId
argument_list|()
argument_list|)
operator|.
name|name
argument_list|()
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|String
operator|.
name|format
argument_list|(
name|OUTPUT_FORMAT
argument_list|,
name|routeDefinition
operator|.
name|getId
argument_list|()
argument_list|,
name|contextName
argument_list|,
name|status
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

