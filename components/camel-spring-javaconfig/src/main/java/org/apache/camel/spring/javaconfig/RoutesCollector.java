begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring.javaconfig
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|javaconfig
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Modifier
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

begin_comment
comment|/**  * Collects routes and rests from the various sources (like Spring application context beans registry or opinionated  * classpath locations) and injects these into the Camel context.  */
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
DECL|field|applicationContext
specifier|private
specifier|final
name|ApplicationContext
name|applicationContext
decl_stmt|;
DECL|field|configuration
specifier|private
specifier|final
name|CamelConfiguration
name|configuration
decl_stmt|;
comment|// Constructors
DECL|method|RoutesCollector (ApplicationContext applicationContext, CamelConfiguration configuration)
specifier|public
name|RoutesCollector
parameter_list|(
name|ApplicationContext
name|applicationContext
parameter_list|,
name|CamelConfiguration
name|configuration
parameter_list|)
block|{
name|this
operator|.
name|applicationContext
operator|=
name|applicationContext
expr_stmt|;
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
block|}
comment|// Overridden
annotation|@
name|Override
DECL|method|onApplicationEvent (ContextRefreshedEvent event)
specifier|public
name|void
name|onApplicationEvent
parameter_list|(
name|ContextRefreshedEvent
name|event
parameter_list|)
block|{
name|ApplicationContext
name|applicationContext
init|=
name|event
operator|.
name|getApplicationContext
argument_list|()
decl_stmt|;
comment|// only listen to context refresh of "my" applicationContext
if|if
condition|(
name|this
operator|.
name|applicationContext
operator|.
name|equals
argument_list|(
name|applicationContext
argument_list|)
condition|)
block|{
name|CamelContext
name|camelContext
init|=
name|event
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
comment|// only add and start Camel if its stopped (initial state)
if|if
condition|(
name|camelContext
operator|.
name|getStatus
argument_list|()
operator|.
name|isStopped
argument_list|()
condition|)
block|{
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
name|configuration
operator|.
name|routes
argument_list|()
control|)
block|{
comment|// filter out abstract classes
name|boolean
name|abs
init|=
name|Modifier
operator|.
name|isAbstract
argument_list|(
name|routesBuilder
operator|.
name|getClass
argument_list|()
operator|.
name|getModifiers
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|abs
condition|)
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
name|CamelSpringJavaconfigInitializationException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
block|}
try|try
block|{
name|boolean
name|skip
init|=
literal|"true"
operator|.
name|equalsIgnoreCase
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"skipStartingCamelContext"
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|skip
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Skipping starting CamelContext(s) as system property skipStartingCamelContext is set to be true."
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// start camel
name|camelContext
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|CamelSpringJavaconfigInitializationException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
block|}
else|else
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Ignore ContextRefreshedEvent: {}"
argument_list|,
name|event
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

