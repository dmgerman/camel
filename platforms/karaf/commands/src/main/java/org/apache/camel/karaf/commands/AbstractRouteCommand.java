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

begin_class
DECL|class|AbstractRouteCommand
specifier|public
specifier|abstract
class|class
name|AbstractRouteCommand
extends|extends
name|OsgiCommandSupport
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
name|executeOnRoute
argument_list|(
name|camelContext
argument_list|,
name|camelRoute
argument_list|)
expr_stmt|;
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

