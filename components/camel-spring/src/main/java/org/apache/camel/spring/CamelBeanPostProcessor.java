begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
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
name|Field
import|;
end_import

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
name|LinkedHashSet
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
name|XmlAccessType
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
name|XmlAccessorType
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
name|XmlRootElement
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
name|EndpointInject
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
name|Produce
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
name|core
operator|.
name|xml
operator|.
name|CamelJMXAgentDefinition
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
name|CamelPostProcessorHelper
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
name|DefaultEndpoint
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
name|spring
operator|.
name|util
operator|.
name|ReflectionUtils
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
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|beans
operator|.
name|BeanInstantiationException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|beans
operator|.
name|BeansException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|beans
operator|.
name|factory
operator|.
name|config
operator|.
name|BeanPostProcessor
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
name|ApplicationContextAware
import|;
end_import

begin_comment
comment|/**  * A bean post processor which implements the<a href="http://camel.apache.org/bean-integration.html">Bean Integration</a>  * features in Camel. Features such as the<a href="http://camel.apache.org/bean-injection.html">Bean Injection</a> of objects like  * {@link Endpoint} and  * {@link org.apache.camel.ProducerTemplate} together with support for  *<a href="http://camel.apache.org/pojo-consuming.html">POJO Consuming</a> via the  * {@link org.apache.camel.Consume} annotation along with  *<a href="http://camel.apache.org/pojo-producing.html">POJO Producing</a> via the  * {@link org.apache.camel.Produce} annotation along with other annotations such as  * {@link org.apache.camel.RecipientList} for creating<a href="http://camel.apache.org/recipientlist-annotation.html">a Recipient List router via annotations</a>.  *<p>  * If you use the&lt;camelContext&gt; element in your<a href="http://camel.apache.org/spring.html">Spring XML</a>  * then one of these bean post processors is implicitly installed and configured for you. So you should never have to  * explicitly create or configure one of these instances.  *  * @version $Revision$  */
end_comment

begin_class
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"beanPostProcessor"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|CamelBeanPostProcessor
specifier|public
class|class
name|CamelBeanPostProcessor
implements|implements
name|BeanPostProcessor
implements|,
name|ApplicationContextAware
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|CamelBeanPostProcessor
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|XmlTransient
DECL|field|prototypeBeans
name|Set
argument_list|<
name|String
argument_list|>
name|prototypeBeans
init|=
operator|new
name|LinkedHashSet
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
annotation|@
name|XmlTransient
DECL|field|camelContext
specifier|private
name|CamelContext
name|camelContext
decl_stmt|;
annotation|@
name|XmlTransient
DECL|field|applicationContext
specifier|private
name|ApplicationContext
name|applicationContext
decl_stmt|;
annotation|@
name|XmlTransient
DECL|field|postProcessor
specifier|private
name|CamelPostProcessorHelper
name|postProcessor
decl_stmt|;
annotation|@
name|XmlTransient
DECL|field|camelId
specifier|private
name|String
name|camelId
decl_stmt|;
DECL|method|CamelBeanPostProcessor ()
specifier|public
name|CamelBeanPostProcessor
parameter_list|()
block|{     }
DECL|method|postProcessBeforeInitialization (Object bean, String beanName)
specifier|public
name|Object
name|postProcessBeforeInitialization
parameter_list|(
name|Object
name|bean
parameter_list|,
name|String
name|beanName
parameter_list|)
throws|throws
name|BeansException
block|{
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Camel bean processing before initialization for bean: "
operator|+
name|beanName
argument_list|)
expr_stmt|;
block|}
comment|// some beans cannot be post processed at this given time, so we gotta check beforehand
if|if
condition|(
operator|!
name|canPostProcessBean
argument_list|(
name|bean
argument_list|,
name|beanName
argument_list|)
condition|)
block|{
return|return
name|bean
return|;
block|}
if|if
condition|(
name|camelContext
operator|==
literal|null
operator|&&
name|applicationContext
operator|.
name|containsBean
argument_list|(
name|camelId
argument_list|)
condition|)
block|{
name|setCamelContext
argument_list|(
operator|(
name|CamelContext
operator|)
name|applicationContext
operator|.
name|getBean
argument_list|(
name|camelId
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|injectFields
argument_list|(
name|bean
argument_list|,
name|beanName
argument_list|)
expr_stmt|;
name|injectMethods
argument_list|(
name|bean
argument_list|,
name|beanName
argument_list|)
expr_stmt|;
if|if
condition|(
name|bean
operator|instanceof
name|CamelContextAware
operator|&&
name|canSetCamelContext
argument_list|(
name|bean
argument_list|,
name|beanName
argument_list|)
condition|)
block|{
name|CamelContextAware
name|contextAware
init|=
operator|(
name|CamelContextAware
operator|)
name|bean
decl_stmt|;
if|if
condition|(
name|camelContext
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"No CamelContext defined yet so cannot inject into: "
operator|+
name|bean
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|contextAware
operator|.
name|setCamelContext
argument_list|(
name|camelContext
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|bean
return|;
block|}
DECL|method|postProcessAfterInitialization (Object bean, String beanName)
specifier|public
name|Object
name|postProcessAfterInitialization
parameter_list|(
name|Object
name|bean
parameter_list|,
name|String
name|beanName
parameter_list|)
throws|throws
name|BeansException
block|{
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Camel bean processing after initialization for bean: "
operator|+
name|beanName
argument_list|)
expr_stmt|;
block|}
comment|// some beans cannot be post processed at this given time, so we gotta check beforehand
if|if
condition|(
operator|!
name|canPostProcessBean
argument_list|(
name|bean
argument_list|,
name|beanName
argument_list|)
condition|)
block|{
return|return
name|bean
return|;
block|}
if|if
condition|(
name|bean
operator|instanceof
name|DefaultEndpoint
condition|)
block|{
name|DefaultEndpoint
name|defaultEndpoint
init|=
operator|(
name|DefaultEndpoint
operator|)
name|bean
decl_stmt|;
name|defaultEndpoint
operator|.
name|setEndpointUriIfNotSpecified
argument_list|(
name|beanName
argument_list|)
expr_stmt|;
block|}
return|return
name|bean
return|;
block|}
comment|// Properties
comment|// -------------------------------------------------------------------------
DECL|method|setApplicationContext (ApplicationContext applicationContext)
specifier|public
name|void
name|setApplicationContext
parameter_list|(
name|ApplicationContext
name|applicationContext
parameter_list|)
throws|throws
name|BeansException
block|{
name|this
operator|.
name|applicationContext
operator|=
name|applicationContext
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
name|postProcessor
operator|=
operator|new
name|CamelPostProcessorHelper
argument_list|(
name|camelContext
argument_list|)
block|{
annotation|@
name|Override
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
name|BeanInstantiationException
argument_list|(
name|type
argument_list|,
literal|"Could not instantiate proxy of type "
operator|+
name|type
operator|.
name|getName
argument_list|()
operator|+
literal|" on endpoint "
operator|+
name|endpoint
argument_list|,
name|e
argument_list|)
return|;
block|}
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
comment|// no application context has been injected which means the bean
comment|// has not been enlisted in Spring application context
if|if
condition|(
name|applicationContext
operator|==
literal|null
operator|||
name|beanName
operator|==
literal|null
condition|)
block|{
return|return
name|super
operator|.
name|isSingleton
argument_list|(
name|bean
argument_list|,
name|beanName
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|applicationContext
operator|.
name|isSingleton
argument_list|(
name|beanName
argument_list|)
return|;
block|}
block|}
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
comment|// only start service and do not add it to CamelContext
name|ServiceHelper
operator|.
name|startService
argument_list|(
name|service
argument_list|)
expr_stmt|;
if|if
condition|(
name|prototypeBeans
operator|.
name|add
argument_list|(
name|beanName
argument_list|)
condition|)
block|{
comment|// do not spam the log with WARN so do this only once per bean name
name|LOG
operator|.
name|warn
argument_list|(
literal|"The bean with id ["
operator|+
name|beanName
operator|+
literal|"] is prototype scoped and cannot stop the injected service when bean is destroyed: "
operator|+
name|service
operator|+
literal|". You may want to stop the service manually from the bean."
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
expr_stmt|;
block|}
DECL|method|getCamelId ()
specifier|public
name|String
name|getCamelId
parameter_list|()
block|{
return|return
name|camelId
return|;
block|}
DECL|method|setCamelId (String camelId)
specifier|public
name|void
name|setCamelId
parameter_list|(
name|String
name|camelId
parameter_list|)
block|{
name|this
operator|.
name|camelId
operator|=
name|camelId
expr_stmt|;
block|}
comment|// Implementation methods
comment|// -------------------------------------------------------------------------
comment|/**      * Can we post process the given bean?      *      * @param bean the bean      * @param beanName the bean name      * @return true to process it      */
DECL|method|canPostProcessBean (Object bean, String beanName)
specifier|protected
name|boolean
name|canPostProcessBean
parameter_list|(
name|Object
name|bean
parameter_list|,
name|String
name|beanName
parameter_list|)
block|{
comment|// the JMXAgent is a bit strange and causes Spring issues if we let it being
comment|// post processed by this one. It does not need it anyway so we are good to go.
if|if
condition|(
name|bean
operator|instanceof
name|CamelJMXAgentDefinition
condition|)
block|{
return|return
literal|false
return|;
block|}
comment|// all other beans can of course be processed
return|return
literal|true
return|;
block|}
DECL|method|canSetCamelContext (Object bean, String beanName)
specifier|protected
name|boolean
name|canSetCamelContext
parameter_list|(
name|Object
name|bean
parameter_list|,
name|String
name|beanName
parameter_list|)
block|{
name|boolean
name|answer
init|=
literal|true
decl_stmt|;
if|if
condition|(
name|bean
operator|instanceof
name|CamelContextAware
condition|)
block|{
name|CamelContextAware
name|camelContextAware
init|=
operator|(
name|CamelContextAware
operator|)
name|bean
decl_stmt|;
name|CamelContext
name|context
init|=
name|camelContextAware
operator|.
name|getCamelContext
argument_list|()
decl_stmt|;
if|if
condition|(
name|context
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"The camel context of "
operator|+
name|beanName
operator|+
literal|" is set, so we skip inject the camel context of it."
argument_list|)
expr_stmt|;
block|}
name|answer
operator|=
literal|false
expr_stmt|;
block|}
block|}
else|else
block|{
name|answer
operator|=
literal|false
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
comment|/**      * A strategy method to allow implementations to perform some custom JBI      * based injection of the POJO      *      * @param bean the bean to be injected      */
DECL|method|injectFields (final Object bean, final String beanName)
specifier|protected
name|void
name|injectFields
parameter_list|(
specifier|final
name|Object
name|bean
parameter_list|,
specifier|final
name|String
name|beanName
parameter_list|)
block|{
name|ReflectionUtils
operator|.
name|doWithFields
argument_list|(
name|bean
operator|.
name|getClass
argument_list|()
argument_list|,
operator|new
name|ReflectionUtils
operator|.
name|FieldCallback
argument_list|()
block|{
specifier|public
name|void
name|doWith
parameter_list|(
name|Field
name|field
parameter_list|)
throws|throws
name|IllegalArgumentException
throws|,
name|IllegalAccessException
block|{
name|EndpointInject
name|endpointInject
init|=
name|field
operator|.
name|getAnnotation
argument_list|(
name|EndpointInject
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|endpointInject
operator|!=
literal|null
operator|&&
name|postProcessor
operator|.
name|matchContext
argument_list|(
name|endpointInject
operator|.
name|context
argument_list|()
argument_list|)
condition|)
block|{
name|injectField
argument_list|(
name|field
argument_list|,
name|endpointInject
operator|.
name|uri
argument_list|()
argument_list|,
name|endpointInject
operator|.
name|ref
argument_list|()
argument_list|,
name|bean
argument_list|,
name|beanName
argument_list|)
expr_stmt|;
block|}
name|Produce
name|produce
init|=
name|field
operator|.
name|getAnnotation
argument_list|(
name|Produce
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|produce
operator|!=
literal|null
operator|&&
name|postProcessor
operator|.
name|matchContext
argument_list|(
name|produce
operator|.
name|context
argument_list|()
argument_list|)
condition|)
block|{
name|injectField
argument_list|(
name|field
argument_list|,
name|produce
operator|.
name|uri
argument_list|()
argument_list|,
name|produce
operator|.
name|ref
argument_list|()
argument_list|,
name|bean
argument_list|,
name|beanName
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|injectField (Field field, String endpointUri, String endpointRef, Object bean, String beanName)
specifier|protected
name|void
name|injectField
parameter_list|(
name|Field
name|field
parameter_list|,
name|String
name|endpointUri
parameter_list|,
name|String
name|endpointRef
parameter_list|,
name|Object
name|bean
parameter_list|,
name|String
name|beanName
parameter_list|)
block|{
name|ReflectionUtils
operator|.
name|setField
argument_list|(
name|field
argument_list|,
name|bean
argument_list|,
name|getPostProcessor
argument_list|()
operator|.
name|getInjectionValue
argument_list|(
name|field
operator|.
name|getType
argument_list|()
argument_list|,
name|endpointUri
argument_list|,
name|endpointRef
argument_list|,
name|field
operator|.
name|getName
argument_list|()
argument_list|,
name|bean
argument_list|,
name|beanName
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|injectMethods (final Object bean, final String beanName)
specifier|protected
name|void
name|injectMethods
parameter_list|(
specifier|final
name|Object
name|bean
parameter_list|,
specifier|final
name|String
name|beanName
parameter_list|)
block|{
name|ReflectionUtils
operator|.
name|doWithMethods
argument_list|(
name|bean
operator|.
name|getClass
argument_list|()
argument_list|,
operator|new
name|ReflectionUtils
operator|.
name|MethodCallback
argument_list|()
block|{
specifier|public
name|void
name|doWith
parameter_list|(
name|Method
name|method
parameter_list|)
throws|throws
name|IllegalArgumentException
throws|,
name|IllegalAccessException
block|{
name|setterInjection
argument_list|(
name|method
argument_list|,
name|bean
argument_list|,
name|beanName
argument_list|)
expr_stmt|;
name|getPostProcessor
argument_list|()
operator|.
name|consumerInjection
argument_list|(
name|method
argument_list|,
name|bean
argument_list|,
name|beanName
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|setterInjection (Method method, Object bean, String beanName)
specifier|protected
name|void
name|setterInjection
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
name|EndpointInject
name|endpointInject
init|=
name|method
operator|.
name|getAnnotation
argument_list|(
name|EndpointInject
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|endpointInject
operator|!=
literal|null
operator|&&
name|postProcessor
operator|.
name|matchContext
argument_list|(
name|endpointInject
operator|.
name|context
argument_list|()
argument_list|)
condition|)
block|{
name|setterInjection
argument_list|(
name|method
argument_list|,
name|bean
argument_list|,
name|beanName
argument_list|,
name|endpointInject
operator|.
name|uri
argument_list|()
argument_list|,
name|endpointInject
operator|.
name|ref
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|Produce
name|produce
init|=
name|method
operator|.
name|getAnnotation
argument_list|(
name|Produce
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|produce
operator|!=
literal|null
operator|&&
name|postProcessor
operator|.
name|matchContext
argument_list|(
name|produce
operator|.
name|context
argument_list|()
argument_list|)
condition|)
block|{
name|setterInjection
argument_list|(
name|method
argument_list|,
name|bean
argument_list|,
name|beanName
argument_list|,
name|produce
operator|.
name|uri
argument_list|()
argument_list|,
name|produce
operator|.
name|ref
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|setterInjection (Method method, Object bean, String beanName, String endpointUri, String endpointRef)
specifier|protected
name|void
name|setterInjection
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
name|endpointRef
parameter_list|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
index|[]
name|parameterTypes
init|=
name|method
operator|.
name|getParameterTypes
argument_list|()
decl_stmt|;
if|if
condition|(
name|parameterTypes
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|parameterTypes
operator|.
name|length
operator|!=
literal|1
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Ignoring badly annotated method for injection due to incorrect number of parameters: "
operator|+
name|method
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|String
name|propertyName
init|=
name|ObjectHelper
operator|.
name|getPropertyName
argument_list|(
name|method
argument_list|)
decl_stmt|;
name|Object
name|value
init|=
name|getPostProcessor
argument_list|()
operator|.
name|getInjectionValue
argument_list|(
name|parameterTypes
index|[
literal|0
index|]
argument_list|,
name|endpointUri
argument_list|,
name|endpointRef
argument_list|,
name|propertyName
argument_list|,
name|bean
argument_list|,
name|beanName
argument_list|)
decl_stmt|;
name|ObjectHelper
operator|.
name|invokeMethod
argument_list|(
name|method
argument_list|,
name|bean
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|getPostProcessor ()
specifier|public
name|CamelPostProcessorHelper
name|getPostProcessor
parameter_list|()
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|postProcessor
argument_list|,
literal|"postProcessor"
argument_list|)
expr_stmt|;
return|return
name|postProcessor
return|;
block|}
block|}
end_class

end_unit

