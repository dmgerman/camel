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
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|JAXBContext
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
name|impl
operator|.
name|DefaultModelJAXBContextFactory
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
name|camel
operator|.
name|spi
operator|.
name|ModelJAXBContextFactory
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
name|core
operator|.
name|api
operator|.
name|provider
operator|.
name|BeanProvider
import|;
end_import

begin_comment
comment|/**  * Allows Camel and CDI applications to be booted up on the command line as a Java Application  */
end_comment

begin_class
DECL|class|Main
specifier|public
specifier|abstract
class|class
name|Main
extends|extends
name|MainSupport
block|{
comment|// abstract to prevent cdi management
DECL|field|instance
specifier|private
specifier|static
name|Main
name|instance
decl_stmt|;
DECL|field|jaxbContext
specifier|private
name|JAXBContext
name|jaxbContext
decl_stmt|;
DECL|field|cdiContainer
specifier|private
name|Object
name|cdiContainer
decl_stmt|;
comment|// we don't want to need cdictrl API in OSGi
DECL|method|Main ()
specifier|public
name|Main
parameter_list|()
block|{
comment|// add options...
block|}
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
block|{ }
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
comment|/**      * Returns the currently executing main      *      * @return the current running instance      */
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
name|ProducerTemplate
name|answer
init|=
name|BeanProvider
operator|.
name|getContextualReference
argument_list|(
name|ProducerTemplate
operator|.
name|class
argument_list|,
literal|true
argument_list|)
decl_stmt|;
if|if
condition|(
name|answer
operator|!=
literal|null
condition|)
block|{
return|return
name|answer
return|;
block|}
if|if
condition|(
name|getCamelContexts
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"No CamelContexts are available so cannot create a ProducerTemplate!"
argument_list|)
throw|;
block|}
return|return
name|getCamelContexts
argument_list|()
operator|.
name|get
argument_list|(
literal|0
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
name|List
argument_list|<
name|CamelContext
argument_list|>
name|contexts
init|=
name|BeanProvider
operator|.
name|getContextualReferences
argument_list|(
name|CamelContext
operator|.
name|class
argument_list|,
literal|true
argument_list|)
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
argument_list|<
name|String
argument_list|,
name|CamelContext
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|CamelContext
name|context
range|:
name|contexts
control|)
block|{
name|String
name|name
init|=
name|context
operator|.
name|getName
argument_list|()
decl_stmt|;
name|answer
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|context
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
DECL|method|getModelJAXBContextFactory ()
specifier|public
name|ModelJAXBContextFactory
name|getModelJAXBContextFactory
parameter_list|()
block|{
return|return
operator|new
name|DefaultModelJAXBContextFactory
argument_list|()
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
name|container
init|=
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
operator|(
operator|(
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
operator|)
name|cdiContainer
operator|)
operator|.
name|shutdown
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

