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
name|function
operator|.
name|Function
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|enterprise
operator|.
name|context
operator|.
name|spi
operator|.
name|CreationalContext
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
name|CreationException
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
name|Endpoint
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
name|FailedToCreateProducerException
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
name|Producer
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
name|core
operator|.
name|xml
operator|.
name|CamelProxyFactoryDefinition
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
name|getReferenceByName
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
name|component
operator|.
name|bean
operator|.
name|ProxyHelper
operator|.
name|createProxy
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
name|util
operator|.
name|ObjectHelper
operator|.
name|isNotEmpty
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
name|support
operator|.
name|ServiceHelper
operator|.
name|startService
import|;
end_import

begin_class
DECL|class|XmlProxyFactoryBean
specifier|final
class|class
name|XmlProxyFactoryBean
parameter_list|<
name|T
parameter_list|>
extends|extends
name|SyntheticBean
argument_list|<
name|T
argument_list|>
block|{
DECL|field|manager
specifier|private
specifier|final
name|BeanManager
name|manager
decl_stmt|;
DECL|field|context
specifier|private
specifier|final
name|Bean
argument_list|<
name|?
argument_list|>
name|context
decl_stmt|;
DECL|field|proxy
specifier|private
specifier|final
name|CamelProxyFactoryDefinition
name|proxy
decl_stmt|;
DECL|method|XmlProxyFactoryBean (BeanManager manager, SyntheticAnnotated annotated, Class<?> type, Function<Bean<T>, String> toString, Bean<?> context, CamelProxyFactoryDefinition proxy)
name|XmlProxyFactoryBean
parameter_list|(
name|BeanManager
name|manager
parameter_list|,
name|SyntheticAnnotated
name|annotated
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|,
name|Function
argument_list|<
name|Bean
argument_list|<
name|T
argument_list|>
argument_list|,
name|String
argument_list|>
name|toString
parameter_list|,
name|Bean
argument_list|<
name|?
argument_list|>
name|context
parameter_list|,
name|CamelProxyFactoryDefinition
name|proxy
parameter_list|)
block|{
name|super
argument_list|(
name|manager
argument_list|,
name|annotated
argument_list|,
name|type
argument_list|,
literal|null
argument_list|,
name|toString
argument_list|)
expr_stmt|;
name|this
operator|.
name|manager
operator|=
name|manager
expr_stmt|;
name|this
operator|.
name|context
operator|=
name|context
expr_stmt|;
name|this
operator|.
name|proxy
operator|=
name|proxy
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|create (CreationalContext<T> creationalContext)
specifier|public
name|T
name|create
parameter_list|(
name|CreationalContext
argument_list|<
name|T
argument_list|>
name|creationalContext
parameter_list|)
block|{
try|try
block|{
name|CamelContext
name|context
init|=
name|isNotEmpty
argument_list|(
name|proxy
operator|.
name|getCamelContextId
argument_list|()
argument_list|)
condition|?
name|getReferenceByName
argument_list|(
name|manager
argument_list|,
name|proxy
operator|.
name|getCamelContextId
argument_list|()
argument_list|,
name|CamelContext
operator|.
name|class
argument_list|)
operator|.
name|get
argument_list|()
else|:
name|getReference
argument_list|(
name|manager
argument_list|,
name|CamelContext
operator|.
name|class
argument_list|,
name|this
operator|.
name|context
argument_list|)
decl_stmt|;
name|Endpoint
name|endpoint
decl_stmt|;
if|if
condition|(
name|isNotEmpty
argument_list|(
name|proxy
operator|.
name|getServiceRef
argument_list|()
argument_list|)
condition|)
block|{
name|endpoint
operator|=
name|context
operator|.
name|getRegistry
argument_list|()
operator|.
name|lookupByNameAndType
argument_list|(
name|proxy
operator|.
name|getServiceRef
argument_list|()
argument_list|,
name|Endpoint
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|isNotEmpty
argument_list|(
name|proxy
operator|.
name|getServiceUrl
argument_list|()
argument_list|)
condition|)
block|{
name|endpoint
operator|=
name|context
operator|.
name|getEndpoint
argument_list|(
name|proxy
operator|.
name|getServiceUrl
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"serviceUrl or serviceRef must not be empty!"
argument_list|)
throw|;
block|}
block|}
if|if
condition|(
name|endpoint
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|UnsatisfiedResolutionException
argument_list|(
literal|"Could not resolve endpoint: "
operator|+
operator|(
name|isNotEmpty
argument_list|(
name|proxy
operator|.
name|getServiceRef
argument_list|()
argument_list|)
condition|?
name|proxy
operator|.
name|getServiceRef
argument_list|()
else|:
name|proxy
operator|.
name|getServiceUrl
argument_list|()
operator|)
argument_list|)
throw|;
block|}
comment|// binding is enabled by default
name|boolean
name|bind
init|=
name|proxy
operator|.
name|getBinding
argument_list|()
operator|!=
literal|null
condition|?
name|proxy
operator|.
name|getBinding
argument_list|()
else|:
literal|true
decl_stmt|;
try|try
block|{
comment|// Start the endpoint before we create the producer
name|startService
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|Producer
name|producer
init|=
name|endpoint
operator|.
name|createProducer
argument_list|()
decl_stmt|;
comment|// Add and start the producer
name|context
operator|.
name|addService
argument_list|(
name|producer
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|)
expr_stmt|;
return|return
name|createProxy
argument_list|(
name|endpoint
argument_list|,
name|bind
argument_list|,
name|producer
argument_list|,
operator|(
name|Class
argument_list|<
name|T
argument_list|>
operator|)
name|proxy
operator|.
name|getServiceInterface
argument_list|()
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|cause
parameter_list|)
block|{
throw|throw
operator|new
name|FailedToCreateProducerException
argument_list|(
name|endpoint
argument_list|,
name|cause
argument_list|)
throw|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|cause
parameter_list|)
block|{
throw|throw
operator|new
name|CreationException
argument_list|(
literal|"Error while creating instance for "
operator|+
name|this
argument_list|,
name|cause
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|destroy (T instance, CreationalContext<T> creationalContext)
specifier|public
name|void
name|destroy
parameter_list|(
name|T
name|instance
parameter_list|,
name|CreationalContext
argument_list|<
name|T
argument_list|>
name|creationalContext
parameter_list|)
block|{
comment|// We let the Camel context manage the lifecycle of the consumer and
comment|// shut it down when Camel stops.
block|}
block|}
end_class

end_unit

