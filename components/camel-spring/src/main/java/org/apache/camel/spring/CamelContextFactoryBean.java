begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
package|;
end_package

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
name|springframework
operator|.
name|beans
operator|.
name|factory
operator|.
name|DisposableBean
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|beans
operator|.
name|factory
operator|.
name|FactoryBean
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|beans
operator|.
name|factory
operator|.
name|InitializingBean
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|ApplicationContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|ApplicationContextAware
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|ApplicationEvent
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|ApplicationListener
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

begin_comment
comment|/**  * A Spring {@link FactoryBean} to create and initialize a {@link SpringCamelContext}  * and install routes either explicitly configured in Spring XML or found by searching the classpath for Java classes  * which extend {@link RouteBuilder} using the nested {@link #setPackages(String[])}.  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|CamelContextFactoryBean
specifier|public
class|class
name|CamelContextFactoryBean
implements|implements
name|FactoryBean
implements|,
name|InitializingBean
implements|,
name|DisposableBean
implements|,
name|ApplicationContextAware
implements|,
name|ApplicationListener
block|{
DECL|field|context
specifier|private
name|SpringCamelContext
name|context
decl_stmt|;
DECL|field|routeBuilder
specifier|private
name|RouteBuilder
name|routeBuilder
decl_stmt|;
DECL|field|additionalBuilders
specifier|private
name|List
argument_list|<
name|RouteBuilder
argument_list|>
name|additionalBuilders
init|=
operator|new
name|ArrayList
argument_list|<
name|RouteBuilder
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|packages
specifier|private
name|String
index|[]
name|packages
init|=
block|{}
decl_stmt|;
DECL|field|applicationContext
specifier|private
name|ApplicationContext
name|applicationContext
decl_stmt|;
DECL|method|getObject ()
specifier|public
name|Object
name|getObject
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|getContext
argument_list|()
return|;
block|}
DECL|method|getObjectType ()
specifier|public
name|Class
name|getObjectType
parameter_list|()
block|{
return|return
name|SpringCamelContext
operator|.
name|class
return|;
block|}
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
DECL|method|afterPropertiesSet ()
specifier|public
name|void
name|afterPropertiesSet
parameter_list|()
throws|throws
name|Exception
block|{
comment|// lets force any lazy creation
name|getContext
argument_list|()
expr_stmt|;
name|findRouteBuiders
argument_list|()
expr_stmt|;
name|installRoutes
argument_list|()
expr_stmt|;
comment|// now lets activate the routes
name|getContext
argument_list|()
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
DECL|method|destroy ()
specifier|public
name|void
name|destroy
parameter_list|()
throws|throws
name|Exception
block|{
name|getContext
argument_list|()
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
DECL|method|onApplicationEvent (ApplicationEvent event)
specifier|public
name|void
name|onApplicationEvent
parameter_list|(
name|ApplicationEvent
name|event
parameter_list|)
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
name|onApplicationEvent
argument_list|(
name|event
argument_list|)
expr_stmt|;
block|}
block|}
comment|// Properties
comment|//-------------------------------------------------------------------------
DECL|method|getContext ()
specifier|public
name|SpringCamelContext
name|getContext
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|context
operator|==
literal|null
condition|)
block|{
name|context
operator|=
operator|new
name|SpringCamelContext
argument_list|(
name|getApplicationContext
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|afterPropertiesSet
argument_list|()
expr_stmt|;
block|}
return|return
name|context
return|;
block|}
DECL|method|setContext (SpringCamelContext context)
specifier|public
name|void
name|setContext
parameter_list|(
name|SpringCamelContext
name|context
parameter_list|)
block|{
name|this
operator|.
name|context
operator|=
name|context
expr_stmt|;
block|}
DECL|method|getRouteBuilder ()
specifier|public
name|RouteBuilder
name|getRouteBuilder
parameter_list|()
block|{
return|return
name|routeBuilder
return|;
block|}
comment|/**      * Set a single {@link RouteBuilder} to be used to create the default routes on startup      */
DECL|method|setRouteBuilder (RouteBuilder routeBuilder)
specifier|public
name|void
name|setRouteBuilder
parameter_list|(
name|RouteBuilder
name|routeBuilder
parameter_list|)
block|{
name|this
operator|.
name|routeBuilder
operator|=
name|routeBuilder
expr_stmt|;
block|}
comment|/**      * Set a collection of {@link RouteBuilder} instances to be used to create the default routes on startup      */
DECL|method|setRouteBuilders (RouteBuilder[] builders)
specifier|public
name|void
name|setRouteBuilders
parameter_list|(
name|RouteBuilder
index|[]
name|builders
parameter_list|)
block|{
for|for
control|(
name|RouteBuilder
name|builder
range|:
name|builders
control|)
block|{
name|additionalBuilders
operator|.
name|add
argument_list|(
name|builder
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|getApplicationContext ()
specifier|public
name|ApplicationContext
name|getApplicationContext
parameter_list|()
block|{
return|return
name|applicationContext
return|;
block|}
DECL|method|setApplicationContext (ApplicationContext applicationContext)
specifier|public
name|void
name|setApplicationContext
parameter_list|(
name|ApplicationContext
name|applicationContext
parameter_list|)
block|{
name|this
operator|.
name|applicationContext
operator|=
name|applicationContext
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
comment|/**      * Sets the package names to be recursively searched for Java classes which extend {@link RouteBuilder} to be auto-wired up to the      * {@link SpringCamelContext} as a route. Note that classes are excluded if they are specifically configured in the spring.xml      *      * @param packages the package names which are recursively searched      */
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
comment|// Implementation methods
comment|//-------------------------------------------------------------------------
comment|/**      * Strategy to install all available routes into the context      */
DECL|method|installRoutes ()
specifier|protected
name|void
name|installRoutes
parameter_list|()
throws|throws
name|Exception
block|{
for|for
control|(
name|RouteBuilder
name|routeBuilder
range|:
name|additionalBuilders
control|)
block|{
name|getContext
argument_list|()
operator|.
name|addRoutes
argument_list|(
name|routeBuilder
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|routeBuilder
operator|!=
literal|null
condition|)
block|{
name|getContext
argument_list|()
operator|.
name|addRoutes
argument_list|(
name|routeBuilder
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Strategy method to try find {@link RouteBuilder} instances on the classpath      */
DECL|method|findRouteBuiders ()
specifier|protected
name|void
name|findRouteBuiders
parameter_list|()
throws|throws
name|IllegalAccessException
throws|,
name|InstantiationException
block|{
if|if
condition|(
name|packages
operator|!=
literal|null
operator|&&
name|packages
operator|.
name|length
operator|>
literal|0
condition|)
block|{
name|RouteBuilderFinder
name|finder
init|=
operator|new
name|RouteBuilderFinder
argument_list|(
name|this
argument_list|)
decl_stmt|;
name|finder
operator|.
name|appendBuilders
argument_list|(
name|additionalBuilders
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

