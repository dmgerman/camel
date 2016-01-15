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
name|HashMap
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
name|apache
operator|.
name|deltaspike
operator|.
name|cdise
operator|.
name|api
operator|.
name|CdiContainerLoader
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
name|BeanManager
name|manager
init|=
name|cdiContainer
operator|.
name|getBeanManager
argument_list|()
decl_stmt|;
name|Bean
argument_list|<
name|?
argument_list|>
name|bean
init|=
name|manager
operator|.
name|resolve
argument_list|(
name|manager
operator|.
name|getBeans
argument_list|(
name|CamelContext
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|bean
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|UnsatisfiedResolutionException
argument_list|(
literal|"No default Camel context is deployed, cannot create default ProducerTemplate!"
argument_list|)
throw|;
block|}
name|CamelContext
name|context
init|=
operator|(
name|CamelContext
operator|)
name|manager
operator|.
name|getReference
argument_list|(
name|bean
argument_list|,
name|CamelContext
operator|.
name|class
argument_list|,
name|manager
operator|.
name|createCreationalContext
argument_list|(
name|bean
argument_list|)
argument_list|)
decl_stmt|;
return|return
name|context
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
name|Map
argument_list|<
name|String
argument_list|,
name|CamelContext
argument_list|>
name|answer
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Bean
argument_list|<
name|?
argument_list|>
name|bean
range|:
name|manager
operator|.
name|getBeans
argument_list|(
name|CamelContext
operator|.
name|class
argument_list|,
name|AnyLiteral
operator|.
name|INSTANCE
argument_list|)
control|)
block|{
name|CamelContext
name|context
init|=
operator|(
name|CamelContext
operator|)
name|manager
operator|.
name|getReference
argument_list|(
name|bean
argument_list|,
name|CamelContext
operator|.
name|class
argument_list|,
name|manager
operator|.
name|createCreationalContext
argument_list|(
name|bean
argument_list|)
argument_list|)
decl_stmt|;
name|answer
operator|.
name|put
argument_list|(
name|context
operator|.
name|getName
argument_list|()
argument_list|,
name|context
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
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
name|CdiContainerLoader
operator|.
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

