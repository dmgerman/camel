begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.main
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|main
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
name|RuntimeCamelException
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
name|Model
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
name|support
operator|.
name|OrderedComparator
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
comment|/**  * To configure routes using {@link RoutesCollector} which collects the routes from various sources.  */
end_comment

begin_class
DECL|class|RoutesConfigurer
specifier|public
class|class
name|RoutesConfigurer
block|{
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
name|RoutesConfigurer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|routesCollector
specifier|private
specifier|final
name|RoutesCollector
name|routesCollector
decl_stmt|;
DECL|field|routesBuilders
specifier|private
specifier|final
name|List
argument_list|<
name|RoutesBuilder
argument_list|>
name|routesBuilders
decl_stmt|;
comment|/**      * Creates a new routes configurer      *      * @param routesCollector  routes collector      */
DECL|method|RoutesConfigurer (RoutesCollector routesCollector)
specifier|public
name|RoutesConfigurer
parameter_list|(
name|RoutesCollector
name|routesCollector
parameter_list|)
block|{
name|this
argument_list|(
name|routesCollector
argument_list|,
operator|new
name|ArrayList
argument_list|<>
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates a new routes configurer      *      * @param routesCollector  routes collector      * @param routesBuilders   existing route builders      */
DECL|method|RoutesConfigurer (RoutesCollector routesCollector, List<RoutesBuilder> routesBuilders)
specifier|public
name|RoutesConfigurer
parameter_list|(
name|RoutesCollector
name|routesCollector
parameter_list|,
name|List
argument_list|<
name|RoutesBuilder
argument_list|>
name|routesBuilders
parameter_list|)
block|{
name|this
operator|.
name|routesCollector
operator|=
name|routesCollector
expr_stmt|;
name|this
operator|.
name|routesBuilders
operator|=
name|routesBuilders
expr_stmt|;
block|}
comment|/**      * Collects routes and rests from the various sources (like registry or opinionated      * classpath locations) and injects (adds) these into the Camel context.      *      * @param camelContext  the Camel context      * @param config        the configuration      */
DECL|method|configureRoutes (CamelContext camelContext, DefaultConfigurationProperties config)
specifier|public
name|void
name|configureRoutes
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|DefaultConfigurationProperties
name|config
parameter_list|)
block|{
if|if
condition|(
name|config
operator|.
name|isRoutesCollectorEnabled
argument_list|()
condition|)
block|{
try|try
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"RoutesCollectorEnabled: {}"
argument_list|,
name|routesCollector
argument_list|)
expr_stmt|;
specifier|final
name|List
argument_list|<
name|RoutesBuilder
argument_list|>
name|routes
init|=
name|routesCollector
operator|.
name|collectRoutesFromRegistry
argument_list|(
name|camelContext
argument_list|,
name|config
operator|.
name|getJavaRoutesExcludePattern
argument_list|()
argument_list|,
name|config
operator|.
name|getJavaRoutesIncludePattern
argument_list|()
argument_list|)
decl_stmt|;
comment|// add newly discovered routes
name|routesBuilders
operator|.
name|addAll
argument_list|(
name|routes
argument_list|)
expr_stmt|;
comment|// sort routes according to ordered
name|routesBuilders
operator|.
name|sort
argument_list|(
name|OrderedComparator
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
comment|// then add the routes
for|for
control|(
name|RoutesBuilder
name|builder
range|:
name|routesBuilders
control|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Adding routes into CamelContext from RoutesBuilder: {}"
argument_list|,
name|builder
argument_list|)
expr_stmt|;
name|camelContext
operator|.
name|addRoutes
argument_list|(
name|builder
argument_list|)
expr_stmt|;
block|}
name|boolean
name|scan
init|=
operator|!
name|config
operator|.
name|getXmlRoutes
argument_list|()
operator|.
name|equals
argument_list|(
literal|"false"
argument_list|)
decl_stmt|;
if|if
condition|(
name|scan
condition|)
block|{
name|List
argument_list|<
name|RoutesDefinition
argument_list|>
name|defs
init|=
name|routesCollector
operator|.
name|collectXmlRoutesFromDirectory
argument_list|(
name|camelContext
argument_list|,
name|config
operator|.
name|getXmlRoutes
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|RoutesDefinition
name|def
range|:
name|defs
control|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Adding routes into CamelContext from XML files: {}"
argument_list|,
name|config
operator|.
name|getXmlRoutes
argument_list|()
argument_list|)
expr_stmt|;
name|camelContext
operator|.
name|getExtension
argument_list|(
name|Model
operator|.
name|class
argument_list|)
operator|.
name|addRouteDefinitions
argument_list|(
name|def
operator|.
name|getRoutes
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|boolean
name|scanRests
init|=
operator|!
name|config
operator|.
name|getXmlRests
argument_list|()
operator|.
name|equals
argument_list|(
literal|"false"
argument_list|)
decl_stmt|;
if|if
condition|(
name|scanRests
condition|)
block|{
name|List
argument_list|<
name|RestsDefinition
argument_list|>
name|defs
init|=
name|routesCollector
operator|.
name|collectXmlRestsFromDirectory
argument_list|(
name|camelContext
argument_list|,
name|config
operator|.
name|getXmlRests
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|RestsDefinition
name|def
range|:
name|defs
control|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Adding rests into CamelContext from XML files: {}"
argument_list|,
name|config
operator|.
name|getXmlRests
argument_list|()
argument_list|)
expr_stmt|;
name|camelContext
operator|.
name|getExtension
argument_list|(
name|Model
operator|.
name|class
argument_list|)
operator|.
name|addRestDefinitions
argument_list|(
name|def
operator|.
name|getRests
argument_list|()
argument_list|,
literal|true
argument_list|)
expr_stmt|;
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
name|RuntimeCamelException
operator|.
name|wrapRuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
block|}
block|}
end_class

end_unit

