begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.guice
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|guice
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
import|;
end_import

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
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Properties
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
import|import
name|javax
operator|.
name|naming
operator|.
name|Context
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|InitialContext
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
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|JAXBException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Binding
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Injector
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Key
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|internal
operator|.
name|Iterables
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|internal
operator|.
name|Maps
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
name|util
operator|.
name|ObjectHelper
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
name|view
operator|.
name|ModelFileGenerator
import|;
end_import

begin_import
import|import
name|org
operator|.
name|guiceyfruit
operator|.
name|Injectors
import|;
end_import

begin_comment
comment|/**  * A command line tool for booting up a CamelContext using a Guice Injector via JNDI  * assuming that a valid jndi.properties is on the classpath  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|Main
specifier|public
class|class
name|Main
extends|extends
name|MainSupport
block|{
DECL|field|instance
specifier|private
specifier|static
name|Main
name|instance
decl_stmt|;
DECL|field|injector
specifier|private
name|Injector
name|injector
decl_stmt|;
DECL|field|jndiProperties
specifier|private
name|String
name|jndiProperties
decl_stmt|;
DECL|method|Main ()
specifier|public
name|Main
parameter_list|()
block|{
name|addOption
argument_list|(
operator|new
name|ParameterOption
argument_list|(
literal|"j"
argument_list|,
literal|"jndiProperties"
argument_list|,
literal|"Sets the classpath based jndi properties file location"
argument_list|,
literal|"jndiProperties"
argument_list|)
block|{
annotation|@
name|Override
specifier|protected
name|void
name|doProcess
parameter_list|(
name|String
name|arg
parameter_list|,
name|String
name|parameter
parameter_list|,
name|LinkedList
argument_list|<
name|String
argument_list|>
name|remainingArgs
parameter_list|)
block|{
name|setJndiProperties
argument_list|(
name|parameter
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|setJndiProperties (String properties)
specifier|public
name|void
name|setJndiProperties
parameter_list|(
name|String
name|properties
parameter_list|)
block|{
name|this
operator|.
name|jndiProperties
operator|=
name|properties
expr_stmt|;
block|}
DECL|method|getJndiProperties ()
specifier|public
name|String
name|getJndiProperties
parameter_list|()
block|{
return|return
name|this
operator|.
name|jndiProperties
return|;
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
comment|// Properties
comment|// -------------------------------------------------------------------------
DECL|method|setInjector (Injector injector)
specifier|protected
name|void
name|setInjector
parameter_list|(
name|Injector
name|injector
parameter_list|)
block|{
name|this
operator|.
name|injector
operator|=
name|injector
expr_stmt|;
block|}
DECL|method|getInjector ()
specifier|protected
name|Injector
name|getInjector
parameter_list|()
block|{
return|return
name|injector
return|;
block|}
comment|// Implementation methods
comment|// -------------------------------------------------------------------------
DECL|method|getInjectorFromContext ()
specifier|protected
name|Injector
name|getInjectorFromContext
parameter_list|()
throws|throws
name|Exception
block|{
name|Context
name|context
init|=
literal|null
decl_stmt|;
name|URL
name|jndiPropertiesUrl
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|jndiProperties
argument_list|)
condition|)
block|{
name|jndiPropertiesUrl
operator|=
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
name|jndiProperties
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|jndiPropertiesUrl
operator|!=
literal|null
condition|)
block|{
name|Properties
name|properties
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|properties
operator|.
name|load
argument_list|(
name|jndiPropertiesUrl
operator|.
name|openStream
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|=
operator|new
name|InitialContext
argument_list|(
name|properties
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|context
operator|=
operator|new
name|InitialContext
argument_list|()
expr_stmt|;
block|}
return|return
operator|(
name|Injector
operator|)
name|context
operator|.
name|lookup
argument_list|(
name|Injector
operator|.
name|class
operator|.
name|getName
argument_list|()
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
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
name|setInjector
argument_list|(
name|getInjectorFromContext
argument_list|()
argument_list|)
expr_stmt|;
name|postProcessContext
argument_list|()
expr_stmt|;
block|}
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Apache Camel stopping"
argument_list|)
expr_stmt|;
if|if
condition|(
name|injector
operator|!=
literal|null
condition|)
block|{
name|Injectors
operator|.
name|close
argument_list|(
name|injector
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|findOrCreateCamelTemplate ()
specifier|protected
name|ProducerTemplate
name|findOrCreateCamelTemplate
parameter_list|()
block|{
if|if
condition|(
name|injector
operator|!=
literal|null
condition|)
block|{
name|Set
argument_list|<
name|ProducerTemplate
argument_list|>
name|set
init|=
name|Injectors
operator|.
name|getInstancesOf
argument_list|(
name|injector
argument_list|,
name|ProducerTemplate
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|set
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
comment|// TODO should be Iterables.get(set, 0);
return|return
name|Iterables
operator|.
name|getOnlyElement
argument_list|(
name|set
argument_list|)
return|;
block|}
block|}
for|for
control|(
name|CamelContext
name|camelContext
range|:
name|getCamelContexts
argument_list|()
control|)
block|{
return|return
name|camelContext
operator|.
name|createProducerTemplate
argument_list|()
return|;
block|}
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"No CamelContexts are available so cannot create a ProducerTemplate!"
argument_list|)
throw|;
block|}
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
name|Map
argument_list|<
name|String
argument_list|,
name|CamelContext
argument_list|>
name|answer
init|=
name|Maps
operator|.
name|newHashMap
argument_list|()
decl_stmt|;
if|if
condition|(
name|injector
operator|!=
literal|null
condition|)
block|{
name|Set
argument_list|<
name|Map
operator|.
name|Entry
argument_list|<
name|Key
argument_list|<
name|?
argument_list|>
argument_list|,
name|Binding
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|>
name|entries
init|=
name|injector
operator|.
name|getBindings
argument_list|()
operator|.
name|entrySet
argument_list|()
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|Key
argument_list|<
name|?
argument_list|>
argument_list|,
name|Binding
argument_list|<
name|?
argument_list|>
argument_list|>
name|entry
range|:
name|entries
control|)
block|{
name|Key
argument_list|<
name|?
argument_list|>
name|key
init|=
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|keyType
init|=
name|Injectors
operator|.
name|getKeyType
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|keyType
operator|!=
literal|null
operator|&&
name|CamelContext
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|keyType
argument_list|)
condition|)
block|{
name|Binding
argument_list|<
name|?
argument_list|>
name|binding
init|=
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|Object
name|value
init|=
name|binding
operator|.
name|getProvider
argument_list|()
operator|.
name|get
argument_list|()
decl_stmt|;
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
name|CamelContext
name|castValue
init|=
name|CamelContext
operator|.
name|class
operator|.
name|cast
argument_list|(
name|value
argument_list|)
decl_stmt|;
name|answer
operator|.
name|put
argument_list|(
name|key
operator|.
name|toString
argument_list|()
argument_list|,
name|castValue
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
return|return
name|answer
return|;
block|}
DECL|method|createModelFileGenerator ()
specifier|protected
name|ModelFileGenerator
name|createModelFileGenerator
parameter_list|()
throws|throws
name|JAXBException
block|{
return|return
operator|new
name|ModelFileGenerator
argument_list|(
name|JAXBContext
operator|.
name|newInstance
argument_list|(
literal|"org.apache.camel.model:org.apache.camel.model.config:org.apache.camel.model.dataformat:org.apache.camel.model.language:org.apache.camel.model.loadbalancer"
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit

