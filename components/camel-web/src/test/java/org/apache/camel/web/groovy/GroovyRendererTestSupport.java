begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.web.groovy
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|web
operator|.
name|groovy
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
name|Map
operator|.
name|Entry
import|;
end_import

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|TestCase
import|;
end_import

begin_import
import|import
name|groovy
operator|.
name|lang
operator|.
name|GroovyClassLoader
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
name|web
operator|.
name|util
operator|.
name|GroovyRenderer
import|;
end_import

begin_comment
comment|/**  * An abstract class that provides basic support for GroovyRenderer test  */
end_comment

begin_class
DECL|class|GroovyRendererTestSupport
specifier|public
specifier|abstract
class|class
name|GroovyRendererTestSupport
extends|extends
name|TestCase
block|{
DECL|field|header
specifier|private
name|String
name|header
init|=
name|GroovyRenderer
operator|.
name|HEADER
decl_stmt|;
DECL|field|footer
specifier|private
specifier|final
name|String
name|footer
init|=
name|GroovyRenderer
operator|.
name|FOOTER
decl_stmt|;
DECL|field|context
specifier|private
name|CamelContext
name|context
decl_stmt|;
comment|/**      * get the first route in camelContext      */
DECL|method|getRoute (String dsl)
specifier|public
name|RouteDefinition
name|getRoute
parameter_list|(
name|String
name|dsl
parameter_list|)
throws|throws
name|Exception
block|{
name|createAndAddRoute
argument_list|(
name|dsl
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|RouteDefinition
argument_list|>
name|list
init|=
name|context
operator|.
name|getRouteDefinitions
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|list
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|list
operator|.
name|get
argument_list|(
literal|0
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
comment|/**      * get all routes in camelContext      */
DECL|method|getRoutes (String dsl)
specifier|public
name|List
argument_list|<
name|RouteDefinition
argument_list|>
name|getRoutes
parameter_list|(
name|String
name|dsl
parameter_list|)
throws|throws
name|Exception
block|{
name|createAndAddRoute
argument_list|(
name|dsl
argument_list|)
expr_stmt|;
return|return
name|context
operator|.
name|getRouteDefinitions
argument_list|()
return|;
block|}
DECL|method|render (String dsl)
specifier|public
name|String
name|render
parameter_list|(
name|String
name|dsl
parameter_list|)
throws|throws
name|Exception
block|{
name|RouteDefinition
name|route
init|=
name|getRoute
argument_list|(
name|dsl
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|route
argument_list|)
expr_stmt|;
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|GroovyRenderer
operator|.
name|renderRoute
argument_list|(
name|sb
argument_list|,
name|route
argument_list|)
expr_stmt|;
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
comment|/**      * render a route with some import packages      */
DECL|method|render (String dsl, String[] imports)
specifier|public
name|String
name|render
parameter_list|(
name|String
name|dsl
parameter_list|,
name|String
index|[]
name|imports
parameter_list|)
throws|throws
name|Exception
block|{
comment|// add import
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|importPackage
range|:
name|imports
control|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|importPackage
argument_list|)
operator|.
name|append
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
block|}
name|header
operator|=
name|sb
operator|.
name|toString
argument_list|()
operator|+
literal|"\n"
operator|+
name|header
expr_stmt|;
return|return
name|render
argument_list|(
name|dsl
argument_list|)
return|;
block|}
comment|/**      * render a route with some import packages and new object      */
DECL|method|render (String dsl, String[] imports, Map<String, String> newObjects)
specifier|public
name|String
name|render
parameter_list|(
name|String
name|dsl
parameter_list|,
name|String
index|[]
name|imports
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|newObjects
parameter_list|)
throws|throws
name|Exception
block|{
comment|// add new objects
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|entry
range|:
name|newObjects
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|String
name|objectName
init|=
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|String
name|clazzName
init|=
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|clazzName
argument_list|)
operator|.
name|append
argument_list|(
literal|" "
argument_list|)
operator|.
name|append
argument_list|(
name|objectName
argument_list|)
operator|.
name|append
argument_list|(
literal|" = new "
argument_list|)
operator|.
name|append
argument_list|(
name|clazzName
argument_list|)
operator|.
name|append
argument_list|(
literal|"();\n"
argument_list|)
expr_stmt|;
block|}
name|header
operator|+=
name|sb
operator|.
name|toString
argument_list|()
expr_stmt|;
return|return
name|render
argument_list|(
name|dsl
argument_list|,
name|imports
argument_list|)
return|;
block|}
DECL|method|renderRoutes (String dsl)
specifier|public
name|String
name|renderRoutes
parameter_list|(
name|String
name|dsl
parameter_list|)
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|RouteDefinition
argument_list|>
name|routes
init|=
name|getRoutes
argument_list|(
name|dsl
argument_list|)
decl_stmt|;
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|GroovyRenderer
operator|.
name|renderRoutes
argument_list|(
name|sb
argument_list|,
name|routes
argument_list|)
expr_stmt|;
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
comment|/**      * create routes using the dsl and add them into camelContext      */
DECL|method|createAndAddRoute (String dsl)
specifier|private
name|void
name|createAndAddRoute
parameter_list|(
name|String
name|dsl
parameter_list|)
throws|throws
name|Exception
throws|,
name|InstantiationException
throws|,
name|IllegalAccessException
block|{
if|if
condition|(
name|context
operator|!=
literal|null
condition|)
block|{
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
name|context
operator|=
operator|new
name|DefaultCamelContext
argument_list|()
expr_stmt|;
name|String
name|routeStr
init|=
name|header
operator|+
name|dsl
operator|+
name|footer
decl_stmt|;
name|GroovyClassLoader
name|classLoader
init|=
operator|new
name|GroovyClassLoader
argument_list|()
decl_stmt|;
name|Class
name|clazz
init|=
name|classLoader
operator|.
name|parseClass
argument_list|(
name|routeStr
argument_list|)
decl_stmt|;
name|RouteBuilder
name|builder
init|=
operator|(
name|RouteBuilder
operator|)
name|clazz
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|context
operator|.
name|addRoutes
argument_list|(
name|builder
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

