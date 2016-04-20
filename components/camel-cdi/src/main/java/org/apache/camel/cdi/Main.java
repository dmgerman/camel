begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.cdi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|cdi
package|;
end_package

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
name|Set
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|Function
operator|.
name|identity
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Collectors
operator|.
name|toMap
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|enterprise
operator|.
name|inject
operator|.
name|UnsatisfiedResolutionException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|enterprise
operator|.
name|inject
operator|.
name|spi
operator|.
name|Bean
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|enterprise
operator|.
name|inject
operator|.
name|spi
operator|.
name|BeanManager
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
name|ProducerTemplate
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
name|main
operator|.
name|MainSupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|deltaspike
operator|.
name|cdise
operator|.
name|api
operator|.
name|CdiContainer
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
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|cdi
operator|.
name|AnyLiteral
operator|.
name|ANY
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
name|cdi
operator|.
name|BeanManagerHelper
operator|.
name|getReference
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
name|cdi
operator|.
name|BeanManagerHelper
operator|.
name|getReferenceByType
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|deltaspike
operator|.
name|cdise
operator|.
name|api
operator|.
name|CdiContainerLoader
operator|.
name|getCdiContainer
import|;
end_import

begin_comment
comment|/**  * Camel CDI boot integration. Allows Camel and CDI to be booted up on the command line as a JVM process.  * See http://camel.apache.org/camel-boot.html.  */
end_comment

begin_class
annotation|@
name|Vetoed
DECL|class|Main
specifier|public
class|class
name|Main
extends|extends
name|MainSupport
block|{
static|static
block|{
comment|// Since version 2.3.0.Final and WELD-1915, Weld SE registers a shutdown hook that conflicts
comment|// with Camel main support. See WELD-2051. The system property above is available starting
comment|// Weld 2.3.1.Final to deactivate the registration of the shutdown hook.
name|System
operator|.
name|setProperty
argument_list|(
literal|"org.jboss.weld.se.shutdownHook"
argument_list|,
name|String
operator|.
name|valueOf
argument_list|(
name|Boolean
operator|.
name|FALSE
argument_list|)
argument_list|)
expr_stmt|;
block|}
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
name|Main
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|instance
specifier|private
specifier|static
name|Main
name|instance
decl_stmt|;
DECL|field|cdiContainer
specifier|private
name|CdiContainer
name|cdiContainer
decl_stmt|;
DECL|method|main (String... args)
specifier|public
specifier|static
name|void
name|main
parameter_list|(
name|String
modifier|...
name|args
parameter_list|)
throws|throws
name|Exception
block|{
name|Main
name|main
init|=
operator|new
name|Main
argument_list|()
decl_stmt|;
name|instance
operator|=
name|main
expr_stmt|;
name|main
operator|.
name|run
argument_list|(
name|args
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns the currently executing instance.      *      * @return the current running instance      */
DECL|method|getInstance ()
specifier|public
specifier|static
name|Main
name|getInstance
parameter_list|()
block|{
return|return
name|instance
return|;
block|}
annotation|@
name|Override
DECL|method|findOrCreateCamelTemplate ()
specifier|protected
name|ProducerTemplate
name|findOrCreateCamelTemplate
parameter_list|()
block|{
return|return
name|getReferenceByType
argument_list|(
name|cdiContainer
operator|.
name|getBeanManager
argument_list|()
argument_list|,
name|CamelContext
operator|.
name|class
argument_list|)
operator|.
name|orElseThrow
argument_list|(
parameter_list|()
lambda|->
operator|new
name|UnsatisfiedResolutionException
argument_list|(
literal|"No default Camel context is deployed, "
operator|+
literal|"cannot create default ProducerTemplate!"
argument_list|)
argument_list|)
operator|.
name|createProducerTemplate
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getCamelContextMap ()
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|CamelContext
argument_list|>
name|getCamelContextMap
parameter_list|()
block|{
name|BeanManager
name|manager
init|=
name|cdiContainer
operator|.
name|getBeanManager
argument_list|()
decl_stmt|;
return|return
name|manager
operator|.
name|getBeans
argument_list|(
name|CamelContext
operator|.
name|class
argument_list|,
name|ANY
argument_list|)
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|bean
lambda|->
name|getReference
argument_list|(
name|manager
argument_list|,
name|CamelContext
operator|.
name|class
argument_list|,
name|bean
argument_list|)
argument_list|)
operator|.
name|collect
argument_list|(
name|toMap
argument_list|(
name|CamelContext
operator|::
name|getName
argument_list|,
name|identity
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
comment|// TODO: Use standard CDI Java SE support when CDI 2.0 becomes a prerequisite
name|CdiContainer
name|container
init|=
name|getCdiContainer
argument_list|()
decl_stmt|;
name|container
operator|.
name|boot
argument_list|()
expr_stmt|;
name|container
operator|.
name|getContextControl
argument_list|()
operator|.
name|startContexts
argument_list|()
expr_stmt|;
name|cdiContainer
operator|=
name|container
expr_stmt|;
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
name|postProcessContext
argument_list|()
expr_stmt|;
name|warnIfNoCamelFound
argument_list|()
expr_stmt|;
block|}
DECL|method|warnIfNoCamelFound ()
specifier|private
name|void
name|warnIfNoCamelFound
parameter_list|()
block|{
name|BeanManager
name|manager
init|=
name|cdiContainer
operator|.
name|getBeanManager
argument_list|()
decl_stmt|;
name|Set
argument_list|<
name|Bean
argument_list|<
name|?
argument_list|>
argument_list|>
name|contexts
init|=
name|manager
operator|.
name|getBeans
argument_list|(
name|CamelContext
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// Warn if the default CDI Camel context has no routes
if|if
condition|(
name|contexts
operator|.
name|size
argument_list|()
operator|==
literal|1
operator|&&
name|getReference
argument_list|(
name|manager
argument_list|,
name|CamelContext
operator|.
name|class
argument_list|,
name|contexts
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
argument_list|)
operator|.
name|getRoutes
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Camel CDI main has started with no Camel routes! "
operator|+
literal|"You may add some RouteBuilder beans to your project."
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
if|if
condition|(
name|cdiContainer
operator|!=
literal|null
condition|)
block|{
name|cdiContainer
operator|.
name|shutdown
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

