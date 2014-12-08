begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
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
name|Method
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
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlTransient
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
name|CamelContextAware
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
name|Consume
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
name|Consumer
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
name|ConsumerTemplate
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
name|IsSingleton
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
name|NoSuchBeanException
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
name|PollingConsumer
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
name|Processor
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
name|ProxyInstantiationException
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
name|Service
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
name|component
operator|.
name|bean
operator|.
name|BeanInfo
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
name|component
operator|.
name|bean
operator|.
name|BeanProcessor
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
name|component
operator|.
name|bean
operator|.
name|ProxyHelper
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
name|component
operator|.
name|properties
operator|.
name|PropertiesComponent
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
name|processor
operator|.
name|CamelInternalProcessor
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
name|processor
operator|.
name|UnitOfWorkProducer
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
name|CamelContextHelper
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
name|IntrospectionSupport
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
name|util
operator|.
name|ServiceHelper
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
comment|/**  * A helper class for Camel based injector or post processing hooks which can be reused by  * both the<a href="http://camel.apache.org/spring.html">Spring</a>,  *<a href="http://camel.apache.org/guice.html">Guice</a> and  *<a href="http://camel.apache.org/blueprint.html">Blueprint</a> support.  *  * @version   */
end_comment

begin_class
DECL|class|CamelPostProcessorHelper
specifier|public
class|class
name|CamelPostProcessorHelper
implements|implements
name|CamelContextAware
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
name|CamelPostProcessorHelper
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|XmlTransient
DECL|field|camelContext
specifier|private
name|CamelContext
name|camelContext
decl_stmt|;
DECL|method|CamelPostProcessorHelper ()
specifier|public
name|CamelPostProcessorHelper
parameter_list|()
block|{     }
DECL|method|CamelPostProcessorHelper (CamelContext camelContext)
specifier|public
name|CamelPostProcessorHelper
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|this
operator|.
name|setCamelContext
argument_list|(
name|camelContext
argument_list|)
expr_stmt|;
block|}
DECL|method|getCamelContext ()
specifier|public
name|CamelContext
name|getCamelContext
parameter_list|()
block|{
return|return
name|camelContext
return|;
block|}
DECL|method|setCamelContext (CamelContext camelContext)
specifier|public
name|void
name|setCamelContext
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|this
operator|.
name|camelContext
operator|=
name|camelContext
expr_stmt|;
block|}
comment|/**      * Does the given context match this camel context      */
DECL|method|matchContext (String context)
specifier|public
name|boolean
name|matchContext
parameter_list|(
name|String
name|context
parameter_list|)
block|{
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|context
argument_list|)
condition|)
block|{
if|if
condition|(
operator|!
name|getCamelContext
argument_list|()
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|context
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
return|return
literal|true
return|;
block|}
DECL|method|consumerInjection (Method method, Object bean, String beanName)
specifier|public
name|void
name|consumerInjection
parameter_list|(
name|Method
name|method
parameter_list|,
name|Object
name|bean
parameter_list|,
name|String
name|beanName
parameter_list|)
block|{
name|Consume
name|consume
init|=
name|method
operator|.
name|getAnnotation
argument_list|(
name|Consume
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|consume
operator|!=
literal|null
operator|&&
name|matchContext
argument_list|(
name|consume
operator|.
name|context
argument_list|()
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Creating a consumer for: "
operator|+
name|consume
argument_list|)
expr_stmt|;
name|subscribeMethod
argument_list|(
name|method
argument_list|,
name|bean
argument_list|,
name|beanName
argument_list|,
name|consume
operator|.
name|uri
argument_list|()
argument_list|,
name|consume
operator|.
name|ref
argument_list|()
argument_list|,
name|consume
operator|.
name|property
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|subscribeMethod (Method method, Object bean, String beanName, String endpointUri, String endpointName, String endpointProperty)
specifier|public
name|void
name|subscribeMethod
parameter_list|(
name|Method
name|method
parameter_list|,
name|Object
name|bean
parameter_list|,
name|String
name|beanName
parameter_list|,
name|String
name|endpointUri
parameter_list|,
name|String
name|endpointName
parameter_list|,
name|String
name|endpointProperty
parameter_list|)
block|{
comment|// lets bind this method to a listener
name|String
name|injectionPointName
init|=
name|method
operator|.
name|getName
argument_list|()
decl_stmt|;
name|Endpoint
name|endpoint
init|=
name|getEndpointInjection
argument_list|(
name|bean
argument_list|,
name|endpointUri
argument_list|,
name|endpointName
argument_list|,
name|endpointProperty
argument_list|,
name|injectionPointName
argument_list|,
literal|true
argument_list|)
decl_stmt|;
if|if
condition|(
name|endpoint
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|Processor
name|processor
init|=
name|createConsumerProcessor
argument_list|(
name|bean
argument_list|,
name|method
argument_list|,
name|endpoint
argument_list|)
decl_stmt|;
name|Consumer
name|consumer
init|=
name|endpoint
operator|.
name|createConsumer
argument_list|(
name|processor
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Created processor: {} for consumer: {}"
argument_list|,
name|processor
argument_list|,
name|consumer
argument_list|)
expr_stmt|;
name|startService
argument_list|(
name|consumer
argument_list|,
name|bean
argument_list|,
name|beanName
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
name|ObjectHelper
operator|.
name|wrapRuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
block|}
comment|/**      * Stats the given service      */
DECL|method|startService (Service service, Object bean, String beanName)
specifier|protected
name|void
name|startService
parameter_list|(
name|Service
name|service
parameter_list|,
name|Object
name|bean
parameter_list|,
name|String
name|beanName
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|isSingleton
argument_list|(
name|bean
argument_list|,
name|beanName
argument_list|)
condition|)
block|{
name|getCamelContext
argument_list|()
operator|.
name|addService
argument_list|(
name|service
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Service is not singleton so you must remember to stop it manually {}"
argument_list|,
name|service
argument_list|)
expr_stmt|;
name|ServiceHelper
operator|.
name|startService
argument_list|(
name|service
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Create a processor which invokes the given method when an incoming      * message exchange is received      */
DECL|method|createConsumerProcessor (final Object pojo, final Method method, final Endpoint endpoint)
specifier|protected
name|Processor
name|createConsumerProcessor
parameter_list|(
specifier|final
name|Object
name|pojo
parameter_list|,
specifier|final
name|Method
name|method
parameter_list|,
specifier|final
name|Endpoint
name|endpoint
parameter_list|)
block|{
name|BeanInfo
name|info
init|=
operator|new
name|BeanInfo
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
name|method
argument_list|)
decl_stmt|;
name|BeanProcessor
name|answer
init|=
operator|new
name|BeanProcessor
argument_list|(
name|pojo
argument_list|,
name|info
argument_list|)
decl_stmt|;
comment|// must ensure the consumer is being executed in an unit of work so synchronization callbacks etc is invoked
name|CamelInternalProcessor
name|internal
init|=
operator|new
name|CamelInternalProcessor
argument_list|(
name|answer
argument_list|)
decl_stmt|;
name|internal
operator|.
name|addAdvice
argument_list|(
operator|new
name|CamelInternalProcessor
operator|.
name|UnitOfWorkProcessorAdvice
argument_list|(
literal|null
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|internal
return|;
block|}
DECL|method|getEndpointInjection (Object bean, String uri, String name, String propertyName, String injectionPointName, boolean mandatory)
specifier|public
name|Endpoint
name|getEndpointInjection
parameter_list|(
name|Object
name|bean
parameter_list|,
name|String
name|uri
parameter_list|,
name|String
name|name
parameter_list|,
name|String
name|propertyName
parameter_list|,
name|String
name|injectionPointName
parameter_list|,
name|boolean
name|mandatory
parameter_list|)
block|{
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|uri
argument_list|)
operator|&&
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|name
argument_list|)
condition|)
block|{
comment|// if no uri or ref, then fallback and try the endpoint property
return|return
name|doGetEndpointInjection
argument_list|(
name|bean
argument_list|,
name|propertyName
argument_list|,
name|injectionPointName
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|doGetEndpointInjection
argument_list|(
name|uri
argument_list|,
name|name
argument_list|,
name|injectionPointName
argument_list|,
name|mandatory
argument_list|)
return|;
block|}
block|}
DECL|method|doGetEndpointInjection (String uri, String name, String injectionPointName, boolean mandatory)
specifier|private
name|Endpoint
name|doGetEndpointInjection
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|name
parameter_list|,
name|String
name|injectionPointName
parameter_list|,
name|boolean
name|mandatory
parameter_list|)
block|{
return|return
name|CamelContextHelper
operator|.
name|getEndpointInjection
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
name|uri
argument_list|,
name|name
argument_list|,
name|injectionPointName
argument_list|,
name|mandatory
argument_list|)
return|;
block|}
comment|/**      * Gets the injection endpoint from a bean property.      * @param bean the bean      * @param propertyName the property name on the bean      */
DECL|method|doGetEndpointInjection (Object bean, String propertyName, String injectionPointName)
specifier|private
name|Endpoint
name|doGetEndpointInjection
parameter_list|(
name|Object
name|bean
parameter_list|,
name|String
name|propertyName
parameter_list|,
name|String
name|injectionPointName
parameter_list|)
block|{
comment|// fall back and use the method name if no explicit property name was given
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|propertyName
argument_list|)
condition|)
block|{
name|propertyName
operator|=
name|injectionPointName
expr_stmt|;
block|}
comment|// we have a property name, try to lookup a getter method on the bean with that name using this strategy
comment|// 1. first the getter with the name as given
comment|// 2. then the getter with Endpoint as postfix
comment|// 3. then if start with on then try step 1 and 2 again, but omit the on prefix
try|try
block|{
name|Object
name|value
init|=
name|IntrospectionSupport
operator|.
name|getOrElseProperty
argument_list|(
name|bean
argument_list|,
name|propertyName
argument_list|,
literal|null
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|==
literal|null
condition|)
block|{
comment|// try endpoint as postfix
name|value
operator|=
name|IntrospectionSupport
operator|.
name|getOrElseProperty
argument_list|(
name|bean
argument_list|,
name|propertyName
operator|+
literal|"Endpoint"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|value
operator|==
literal|null
operator|&&
name|propertyName
operator|.
name|startsWith
argument_list|(
literal|"on"
argument_list|)
condition|)
block|{
comment|// retry but without the on as prefix
name|propertyName
operator|=
name|propertyName
operator|.
name|substring
argument_list|(
literal|2
argument_list|)
expr_stmt|;
return|return
name|doGetEndpointInjection
argument_list|(
name|bean
argument_list|,
name|propertyName
argument_list|,
name|injectionPointName
argument_list|)
return|;
block|}
if|if
condition|(
name|value
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
elseif|else
if|if
condition|(
name|value
operator|instanceof
name|Endpoint
condition|)
block|{
return|return
operator|(
name|Endpoint
operator|)
name|value
return|;
block|}
else|else
block|{
name|String
name|uriOrRef
init|=
name|getCamelContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|mandatoryConvertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|value
argument_list|)
decl_stmt|;
return|return
name|getCamelContext
argument_list|()
operator|.
name|getEndpoint
argument_list|(
name|uriOrRef
argument_list|)
return|;
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
name|IllegalArgumentException
argument_list|(
literal|"Error getting property "
operator|+
name|propertyName
operator|+
literal|" from bean "
operator|+
name|bean
operator|+
literal|" due "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
comment|/**      * Creates the object to be injected for an {@link org.apache.camel.EndpointInject} or {@link org.apache.camel.Produce} injection point      */
DECL|method|getInjectionValue (Class<?> type, String endpointUri, String endpointRef, String endpointProperty, String injectionPointName, Object bean, String beanName)
specifier|public
name|Object
name|getInjectionValue
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|,
name|String
name|endpointUri
parameter_list|,
name|String
name|endpointRef
parameter_list|,
name|String
name|endpointProperty
parameter_list|,
name|String
name|injectionPointName
parameter_list|,
name|Object
name|bean
parameter_list|,
name|String
name|beanName
parameter_list|)
block|{
if|if
condition|(
name|type
operator|.
name|isAssignableFrom
argument_list|(
name|ProducerTemplate
operator|.
name|class
argument_list|)
condition|)
block|{
return|return
name|createInjectionProducerTemplate
argument_list|(
name|endpointUri
argument_list|,
name|endpointRef
argument_list|,
name|endpointProperty
argument_list|,
name|injectionPointName
argument_list|,
name|bean
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|type
operator|.
name|isAssignableFrom
argument_list|(
name|ConsumerTemplate
operator|.
name|class
argument_list|)
condition|)
block|{
return|return
name|createInjectionConsumerTemplate
argument_list|(
name|endpointUri
argument_list|,
name|endpointRef
argument_list|,
name|endpointProperty
argument_list|,
name|injectionPointName
argument_list|)
return|;
block|}
else|else
block|{
name|Endpoint
name|endpoint
init|=
name|getEndpointInjection
argument_list|(
name|bean
argument_list|,
name|endpointUri
argument_list|,
name|endpointRef
argument_list|,
name|endpointProperty
argument_list|,
name|injectionPointName
argument_list|,
literal|true
argument_list|)
decl_stmt|;
if|if
condition|(
name|endpoint
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|type
operator|.
name|isInstance
argument_list|(
name|endpoint
argument_list|)
condition|)
block|{
return|return
name|endpoint
return|;
block|}
elseif|else
if|if
condition|(
name|type
operator|.
name|isAssignableFrom
argument_list|(
name|Producer
operator|.
name|class
argument_list|)
condition|)
block|{
return|return
name|createInjectionProducer
argument_list|(
name|endpoint
argument_list|,
name|bean
argument_list|,
name|beanName
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|type
operator|.
name|isAssignableFrom
argument_list|(
name|PollingConsumer
operator|.
name|class
argument_list|)
condition|)
block|{
return|return
name|createInjectionPollingConsumer
argument_list|(
name|endpoint
argument_list|,
name|bean
argument_list|,
name|beanName
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|type
operator|.
name|isInterface
argument_list|()
condition|)
block|{
comment|// lets create a proxy
try|try
block|{
return|return
name|ProxyHelper
operator|.
name|createProxy
argument_list|(
name|endpoint
argument_list|,
name|type
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
name|createProxyInstantiationRuntimeException
argument_list|(
name|type
argument_list|,
name|endpoint
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Invalid type: "
operator|+
name|type
operator|.
name|getName
argument_list|()
operator|+
literal|" which cannot be injected via @EndpointInject/@Produce for: "
operator|+
name|endpoint
argument_list|)
throw|;
block|}
block|}
return|return
literal|null
return|;
block|}
block|}
DECL|method|getInjectionPropertyValue (Class<?> type, String propertyName, String propertyDefaultValue, String injectionPointName, Object bean, String beanName)
specifier|public
name|Object
name|getInjectionPropertyValue
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|,
name|String
name|propertyName
parameter_list|,
name|String
name|propertyDefaultValue
parameter_list|,
name|String
name|injectionPointName
parameter_list|,
name|Object
name|bean
parameter_list|,
name|String
name|beanName
parameter_list|)
block|{
try|try
block|{
name|String
name|key
decl_stmt|;
name|String
name|prefix
init|=
name|getCamelContext
argument_list|()
operator|.
name|getPropertyPrefixToken
argument_list|()
decl_stmt|;
name|String
name|suffix
init|=
name|getCamelContext
argument_list|()
operator|.
name|getPropertySuffixToken
argument_list|()
decl_stmt|;
if|if
condition|(
name|prefix
operator|==
literal|null
operator|&&
name|suffix
operator|==
literal|null
condition|)
block|{
comment|// if no custom prefix/suffix then use defaults
name|prefix
operator|=
name|PropertiesComponent
operator|.
name|DEFAULT_PREFIX_TOKEN
expr_stmt|;
name|suffix
operator|=
name|PropertiesComponent
operator|.
name|DEFAULT_SUFFIX_TOKEN
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|propertyName
operator|.
name|startsWith
argument_list|(
name|prefix
argument_list|)
condition|)
block|{
comment|// must enclose the property name with prefix/suffix to have it resolved
name|key
operator|=
name|prefix
operator|+
name|propertyName
operator|+
name|suffix
expr_stmt|;
block|}
else|else
block|{
comment|// key has already prefix/suffix so use it as-is as it may be a compound key
name|key
operator|=
name|propertyName
expr_stmt|;
block|}
name|String
name|value
init|=
name|getCamelContext
argument_list|()
operator|.
name|resolvePropertyPlaceholders
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
return|return
name|getCamelContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|mandatoryConvertTo
argument_list|(
name|type
argument_list|,
name|value
argument_list|)
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|propertyDefaultValue
argument_list|)
condition|)
block|{
try|try
block|{
return|return
name|getCamelContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|mandatoryConvertTo
argument_list|(
name|type
argument_list|,
name|propertyDefaultValue
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e2
parameter_list|)
block|{
throw|throw
name|ObjectHelper
operator|.
name|wrapRuntimeCamelException
argument_list|(
name|e2
argument_list|)
throw|;
block|}
block|}
throw|throw
name|ObjectHelper
operator|.
name|wrapRuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|getInjectionBeanValue (Class<?> type, String name)
specifier|public
name|Object
name|getInjectionBeanValue
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|,
name|String
name|name
parameter_list|)
block|{
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|Set
argument_list|<
name|?
argument_list|>
name|found
init|=
name|getCamelContext
argument_list|()
operator|.
name|getRegistry
argument_list|()
operator|.
name|findByType
argument_list|(
name|type
argument_list|)
decl_stmt|;
if|if
condition|(
name|found
operator|==
literal|null
operator|||
name|found
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|NoSuchBeanException
argument_list|(
name|name
argument_list|,
name|type
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
elseif|else
if|if
condition|(
name|found
operator|.
name|size
argument_list|()
operator|>
literal|1
condition|)
block|{
throw|throw
operator|new
name|NoSuchBeanException
argument_list|(
literal|"Found "
operator|+
name|found
operator|.
name|size
argument_list|()
operator|+
literal|" beans of type: "
operator|+
name|type
operator|+
literal|". Only one bean expected."
argument_list|)
throw|;
block|}
else|else
block|{
comment|// we found only one
return|return
name|found
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
return|;
block|}
block|}
else|else
block|{
return|return
name|CamelContextHelper
operator|.
name|mandatoryLookup
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
name|name
argument_list|,
name|type
argument_list|)
return|;
block|}
block|}
comment|/**      * Factory method to create a {@link org.apache.camel.ProducerTemplate} to be injected into a POJO      */
DECL|method|createInjectionProducerTemplate (String endpointUri, String endpointRef, String endpointProperty, String injectionPointName, Object bean)
specifier|protected
name|ProducerTemplate
name|createInjectionProducerTemplate
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|String
name|endpointRef
parameter_list|,
name|String
name|endpointProperty
parameter_list|,
name|String
name|injectionPointName
parameter_list|,
name|Object
name|bean
parameter_list|)
block|{
comment|// endpoint is optional for this injection point
name|Endpoint
name|endpoint
init|=
name|getEndpointInjection
argument_list|(
name|bean
argument_list|,
name|endpointUri
argument_list|,
name|endpointRef
argument_list|,
name|endpointProperty
argument_list|,
name|injectionPointName
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|ProducerTemplate
name|answer
init|=
operator|new
name|DefaultProducerTemplate
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
name|endpoint
argument_list|)
decl_stmt|;
comment|// start the template so its ready to use
try|try
block|{
name|answer
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
name|ObjectHelper
operator|.
name|wrapRuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
return|return
name|answer
return|;
block|}
comment|/**      * Factory method to create a {@link org.apache.camel.ConsumerTemplate} to be injected into a POJO      */
DECL|method|createInjectionConsumerTemplate (String endpointUri, String endpointRef, String endpointProperty, String injectionPointName)
specifier|protected
name|ConsumerTemplate
name|createInjectionConsumerTemplate
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|String
name|endpointRef
parameter_list|,
name|String
name|endpointProperty
parameter_list|,
name|String
name|injectionPointName
parameter_list|)
block|{
name|ConsumerTemplate
name|answer
init|=
operator|new
name|DefaultConsumerTemplate
argument_list|(
name|getCamelContext
argument_list|()
argument_list|)
decl_stmt|;
comment|// start the template so its ready to use
try|try
block|{
name|answer
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
name|ObjectHelper
operator|.
name|wrapRuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
return|return
name|answer
return|;
block|}
comment|/**      * Factory method to create a started {@link org.apache.camel.PollingConsumer} to be injected into a POJO      */
DECL|method|createInjectionPollingConsumer (Endpoint endpoint, Object bean, String beanName)
specifier|protected
name|PollingConsumer
name|createInjectionPollingConsumer
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|Object
name|bean
parameter_list|,
name|String
name|beanName
parameter_list|)
block|{
try|try
block|{
name|PollingConsumer
name|pollingConsumer
init|=
name|endpoint
operator|.
name|createPollingConsumer
argument_list|()
decl_stmt|;
name|startService
argument_list|(
name|pollingConsumer
argument_list|,
name|bean
argument_list|,
name|beanName
argument_list|)
expr_stmt|;
return|return
name|pollingConsumer
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
name|ObjectHelper
operator|.
name|wrapRuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
comment|/**      * A Factory method to create a started {@link org.apache.camel.Producer} to be injected into a POJO      */
DECL|method|createInjectionProducer (Endpoint endpoint, Object bean, String beanName)
specifier|protected
name|Producer
name|createInjectionProducer
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|Object
name|bean
parameter_list|,
name|String
name|beanName
parameter_list|)
block|{
try|try
block|{
name|Producer
name|producer
init|=
name|endpoint
operator|.
name|createProducer
argument_list|()
decl_stmt|;
name|startService
argument_list|(
name|producer
argument_list|,
name|bean
argument_list|,
name|beanName
argument_list|)
expr_stmt|;
return|return
operator|new
name|UnitOfWorkProducer
argument_list|(
name|producer
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
name|ObjectHelper
operator|.
name|wrapRuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|createProxyInstantiationRuntimeException (Class<?> type, Endpoint endpoint, Exception e)
specifier|protected
name|RuntimeException
name|createProxyInstantiationRuntimeException
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|,
name|Endpoint
name|endpoint
parameter_list|,
name|Exception
name|e
parameter_list|)
block|{
return|return
operator|new
name|ProxyInstantiationException
argument_list|(
name|type
argument_list|,
name|endpoint
argument_list|,
name|e
argument_list|)
return|;
block|}
comment|/**      * Implementations can override this method to determine if the bean is singleton.      *      * @param bean the bean      * @return<tt>true</tt> if its singleton scoped, for prototype scoped<tt>false</tt> is returned.      */
DECL|method|isSingleton (Object bean, String beanName)
specifier|protected
name|boolean
name|isSingleton
parameter_list|(
name|Object
name|bean
parameter_list|,
name|String
name|beanName
parameter_list|)
block|{
if|if
condition|(
name|bean
operator|instanceof
name|IsSingleton
condition|)
block|{
name|IsSingleton
name|singleton
init|=
operator|(
name|IsSingleton
operator|)
name|bean
decl_stmt|;
return|return
name|singleton
operator|.
name|isSingleton
argument_list|()
return|;
block|}
return|return
literal|true
return|;
block|}
block|}
end_class

end_unit

