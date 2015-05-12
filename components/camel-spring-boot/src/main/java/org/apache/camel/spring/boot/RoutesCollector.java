begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring.boot
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|boot
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileNotFoundException
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
name|RoutesBuilder
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
name|ApplicationListener
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
name|event
operator|.
name|ContextRefreshedEvent
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|core
operator|.
name|io
operator|.
name|Resource
import|;
end_import

begin_comment
comment|/**  * Collects routes from the various sources (like Spring application context beans registry or opinionated classpath  * locations) and injects these into the Camel context.  */
end_comment

begin_class
DECL|class|RoutesCollector
specifier|public
class|class
name|RoutesCollector
implements|implements
name|ApplicationListener
argument_list|<
name|ContextRefreshedEvent
argument_list|>
block|{
comment|// Static collaborators
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|RoutesCollector
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// Collaborators
DECL|field|camelContextConfigurations
specifier|private
specifier|final
name|List
argument_list|<
name|CamelContextConfiguration
argument_list|>
name|camelContextConfigurations
decl_stmt|;
comment|// Constructors
DECL|method|RoutesCollector (List<CamelContextConfiguration> camelContextConfigurations)
specifier|public
name|RoutesCollector
parameter_list|(
name|List
argument_list|<
name|CamelContextConfiguration
argument_list|>
name|camelContextConfigurations
parameter_list|)
block|{
name|this
operator|.
name|camelContextConfigurations
operator|=
operator|new
name|ArrayList
argument_list|<
name|CamelContextConfiguration
argument_list|>
argument_list|(
name|camelContextConfigurations
argument_list|)
expr_stmt|;
block|}
comment|// Overridden
annotation|@
name|Override
DECL|method|onApplicationEvent (ContextRefreshedEvent contextRefreshedEvent)
specifier|public
name|void
name|onApplicationEvent
parameter_list|(
name|ContextRefreshedEvent
name|contextRefreshedEvent
parameter_list|)
block|{
name|ApplicationContext
name|applicationContext
init|=
name|contextRefreshedEvent
operator|.
name|getApplicationContext
argument_list|()
decl_stmt|;
if|if
condition|(
name|applicationContext
operator|.
name|getParent
argument_list|()
operator|==
literal|null
condition|)
block|{
name|CamelContext
name|camelContext
init|=
name|contextRefreshedEvent
operator|.
name|getApplicationContext
argument_list|()
operator|.
name|getBean
argument_list|(
name|CamelContext
operator|.
name|class
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Post-processing CamelContext bean: {}"
argument_list|,
name|camelContext
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|RoutesBuilder
name|routesBuilder
range|:
name|applicationContext
operator|.
name|getBeansOfType
argument_list|(
name|RoutesBuilder
operator|.
name|class
argument_list|)
operator|.
name|values
argument_list|()
control|)
block|{
try|try
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Injecting following route into the CamelContext: {}"
argument_list|,
name|routesBuilder
argument_list|)
expr_stmt|;
name|camelContext
operator|.
name|addRoutes
argument_list|(
name|routesBuilder
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
name|loadXmlRoutes
argument_list|(
name|applicationContext
argument_list|,
name|camelContext
argument_list|)
expr_stmt|;
if|if
condition|(
name|camelContextConfigurations
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|CamelContextConfiguration
name|camelContextConfiguration
range|:
name|camelContextConfigurations
control|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"CamelContextConfiguration found. Invoking: {}"
argument_list|,
name|camelContextConfiguration
argument_list|)
expr_stmt|;
name|camelContextConfiguration
operator|.
name|beforeApplicationStart
argument_list|(
name|camelContext
argument_list|)
expr_stmt|;
block|}
block|}
try|try
block|{
name|camelContext
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
throw|throw
operator|new
name|CamelSpringBootInitializationException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
else|else
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Not at root context - defer adding routes"
argument_list|)
expr_stmt|;
block|}
block|}
comment|// Helpers
DECL|method|loadXmlRoutes (ApplicationContext applicationContext, CamelContext camelContext)
specifier|private
name|void
name|loadXmlRoutes
parameter_list|(
name|ApplicationContext
name|applicationContext
parameter_list|,
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Started XML routes detection. Scanning classpath (/camel/*.xml)..."
argument_list|)
expr_stmt|;
try|try
block|{
name|Resource
index|[]
name|xmlRoutes
init|=
name|applicationContext
operator|.
name|getResources
argument_list|(
literal|"classpath:camel/*.xml"
argument_list|)
decl_stmt|;
for|for
control|(
name|Resource
name|xmlRoute
range|:
name|xmlRoutes
control|)
block|{
name|RoutesDefinition
name|xmlDefinition
init|=
name|camelContext
operator|.
name|loadRoutesDefinition
argument_list|(
name|xmlRoute
operator|.
name|getInputStream
argument_list|()
argument_list|)
decl_stmt|;
name|camelContext
operator|.
name|addRouteDefinitions
argument_list|(
name|xmlDefinition
operator|.
name|getRoutes
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|FileNotFoundException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"No XMl routes found in the classpath (/camel/*.xml). Skipping XML routes detection."
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

