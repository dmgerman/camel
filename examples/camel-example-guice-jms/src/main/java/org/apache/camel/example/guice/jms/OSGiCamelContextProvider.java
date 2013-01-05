begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.example.guice.jms
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|example
operator|.
name|guice
operator|.
name|jms
package|;
end_package

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
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Inject
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
name|Provider
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
name|ProvisionException
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
name|guice
operator|.
name|jndi
operator|.
name|JndiBindings
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
name|guice
operator|.
name|jndi
operator|.
name|internal
operator|.
name|JndiContext
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
name|impl
operator|.
name|JndiRegistry
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
name|osgi
operator|.
name|CamelContextFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|BundleContext
import|;
end_import

begin_class
DECL|class|OSGiCamelContextProvider
specifier|public
class|class
name|OSGiCamelContextProvider
implements|implements
name|Provider
argument_list|<
name|CamelContext
argument_list|>
block|{
DECL|field|factory
specifier|private
specifier|final
name|CamelContextFactory
name|factory
decl_stmt|;
annotation|@
name|Inject
DECL|field|routeBuilders
specifier|private
name|Set
argument_list|<
name|RoutesBuilder
argument_list|>
name|routeBuilders
decl_stmt|;
annotation|@
name|Inject
DECL|field|injector
specifier|private
name|Injector
name|injector
decl_stmt|;
DECL|method|OSGiCamelContextProvider (BundleContext context)
specifier|public
name|OSGiCamelContextProvider
parameter_list|(
name|BundleContext
name|context
parameter_list|)
block|{
comment|// In this we can support to run this provider with or without OSGI
if|if
condition|(
name|context
operator|!=
literal|null
condition|)
block|{
name|factory
operator|=
operator|new
name|CamelContextFactory
argument_list|()
expr_stmt|;
name|factory
operator|.
name|setBundleContext
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|factory
operator|=
literal|null
expr_stmt|;
block|}
block|}
DECL|method|getJndiContext ()
specifier|protected
name|Context
name|getJndiContext
parameter_list|()
block|{
try|try
block|{
name|JndiContext
name|context
init|=
operator|new
name|JndiContext
argument_list|(
literal|null
argument_list|)
decl_stmt|;
if|if
condition|(
name|injector
operator|!=
literal|null
condition|)
block|{
comment|//Feed the empty properties to get the code work
name|JndiBindings
operator|.
name|bindInjectorAndBindings
argument_list|(
name|context
argument_list|,
name|injector
argument_list|,
operator|new
name|Properties
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|context
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ProvisionException
argument_list|(
literal|"Failed to create JNDI bindings. Reason: "
operator|+
name|e
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
comment|// set the JndiRegistry to the camel context
DECL|method|updateRegistry (DefaultCamelContext camelContext)
specifier|protected
name|void
name|updateRegistry
parameter_list|(
name|DefaultCamelContext
name|camelContext
parameter_list|)
block|{
name|camelContext
operator|.
name|setRegistry
argument_list|(
operator|new
name|JndiRegistry
argument_list|(
name|getJndiContext
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|get ()
specifier|public
name|CamelContext
name|get
parameter_list|()
block|{
name|DefaultCamelContext
name|camelContext
decl_stmt|;
if|if
condition|(
name|factory
operator|!=
literal|null
condition|)
block|{
name|camelContext
operator|=
name|factory
operator|.
name|createContext
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|camelContext
operator|=
operator|new
name|DefaultCamelContext
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|routeBuilders
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|RoutesBuilder
name|builder
range|:
name|routeBuilders
control|)
block|{
try|try
block|{
name|camelContext
operator|.
name|addRoutes
argument_list|(
name|builder
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
name|ProvisionException
argument_list|(
literal|"Failed to add the router. Reason: "
operator|+
name|e
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
name|updateRegistry
argument_list|(
name|camelContext
argument_list|)
expr_stmt|;
return|return
name|camelContext
return|;
block|}
block|}
end_class

end_unit

