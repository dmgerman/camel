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
name|MessageDriven
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
name|impl
operator|.
name|DefaultProducerTemplate
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
name|NoSuchBeanDefinitionException
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
name|isNotNullAndNonEmpty
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
name|isNullOrBlank
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
name|wrapRuntimeCamelException
import|;
end_import

begin_comment
comment|/**  * A bean post processor which implements the<a href="http://activemq.apache.org/camel/bean-integration.html">Bean Integration</a>  * features in Camel such as the<a href="http://activemq.apache.org/camel/bean-injection.html">Bean Injection</a> of objects like  * {@link Endpoint} and  * {@link org.apache.camel.ProducerTemplate} together with support for  *<a href="http://activemq.apache.org/camel/pojo-consuming.html">POJO Consuming</a> via the   * {@link org.apache.camel.Consume} and {@link org.apache.camel.MessageDriven} annotations along with  *<a href="http://activemq.apache.org/camel/pojo-producing.html">POJO Producing</a> via the  * {@link org.apache.camel.Produce} annotation along with other annotations such as  * {@link org.apache.camel.RecipientList} for creating<a href="http://activemq.apache.org/camel/recipientlist-annotation.html">a Recipient List router via annotations</a>.  *<p>  * If you use the&lt;camelContext&gt; element in your<a href="http://activemq.apache.org/camel/spring.html">Spring XML</a>   * then one of these bean post processors is implicity installed and configured for you. So you should never have to  * explicitly create or configure one of these instances.  *  * @version $Revision$  */
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
DECL|field|camelContext
specifier|private
name|SpringCamelContext
name|camelContext
decl_stmt|;
annotation|@
name|XmlTransient
DECL|field|applicationContext
specifier|private
name|ApplicationContext
name|applicationContext
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
name|injectFields
argument_list|(
name|bean
argument_list|)
expr_stmt|;
name|injectMethods
argument_list|(
name|bean
argument_list|)
expr_stmt|;
if|if
condition|(
name|bean
operator|instanceof
name|CamelContextAware
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
name|SpringCamelContext
name|getCamelContext
parameter_list|()
block|{
return|return
name|camelContext
return|;
block|}
DECL|method|setCamelContext (SpringCamelContext camelContext)
specifier|public
name|void
name|setCamelContext
parameter_list|(
name|SpringCamelContext
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
comment|// Implementation methods
comment|// -------------------------------------------------------------------------
comment|/**      * A strategy method to allow implementations to perform some custom JBI      * based injection of the POJO      *      * @param bean the bean to be injected      */
DECL|method|injectFields (final Object bean)
specifier|protected
name|void
name|injectFields
parameter_list|(
specifier|final
name|Object
name|bean
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
name|annotation
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
name|annotation
operator|!=
literal|null
condition|)
block|{
name|injectField
argument_list|(
name|field
argument_list|,
name|annotation
operator|.
name|uri
argument_list|()
argument_list|,
name|annotation
operator|.
name|name
argument_list|()
argument_list|,
name|bean
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
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|injectField (Field field, String endpointUri, String endpointRef, Object bean)
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
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|injectMethods (final Object bean)
specifier|protected
name|void
name|injectMethods
parameter_list|(
specifier|final
name|Object
name|bean
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
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
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
argument_list|)
expr_stmt|;
name|consumerInjection
argument_list|(
name|method
argument_list|,
name|bean
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|setterInjection (Method method, Object bean)
specifier|protected
name|void
name|setterInjection
parameter_list|(
name|Method
name|method
parameter_list|,
name|Object
name|bean
parameter_list|)
block|{
name|EndpointInject
name|annoation
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
name|annoation
operator|!=
literal|null
condition|)
block|{
name|setterInjection
argument_list|(
name|method
argument_list|,
name|bean
argument_list|,
name|annoation
operator|.
name|uri
argument_list|()
argument_list|,
name|annoation
operator|.
name|name
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
condition|)
block|{
name|setterInjection
argument_list|(
name|method
argument_list|,
name|bean
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
DECL|method|setterInjection (Method method, Object bean, String endpointUri, String endpointRef)
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
DECL|method|consumerInjection (final Object bean)
specifier|protected
name|void
name|consumerInjection
parameter_list|(
specifier|final
name|Object
name|bean
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
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
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
comment|/*                  * TODO support callbacks? if                  * (method.getAnnotation(Callback.class) != null) { try {                  * Expression e = ExpressionFactory.createExpression(                  * method.getAnnotation(Callback.class).condition());                  * JexlContext jc = JexlHelper.createContext();                  * jc.getVars().put("this", obj); Object r = e.evaluate(jc); if                  * (!(r instanceof Boolean)) { throw new                  * RuntimeException("Expression did not returned a boolean value                  * but: " + r); } Boolean oldVal =                  * req.getCallbacks().get(method); Boolean newVal = (Boolean) r;                  * if ((oldVal == null || !oldVal)&& newVal) {                  * req.getCallbacks().put(method, newVal); method.invoke(obj,                  * new Object[0]); // TODO: handle return value and sent it as                  * the answer } } catch (Exception e) { throw new                  * RuntimeException("Unable to invoke callback", e); } }                  */
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|consumerInjection (Method method, Object bean)
specifier|protected
name|void
name|consumerInjection
parameter_list|(
name|Method
name|method
parameter_list|,
name|Object
name|bean
parameter_list|)
block|{
name|MessageDriven
name|annotation
init|=
name|method
operator|.
name|getAnnotation
argument_list|(
name|MessageDriven
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|annotation
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Creating a consumer for: "
operator|+
name|annotation
argument_list|)
expr_stmt|;
name|subscribeMethod
argument_list|(
name|method
argument_list|,
name|bean
argument_list|,
name|annotation
operator|.
name|uri
argument_list|()
argument_list|,
name|annotation
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
block|}
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
condition|)
block|{
name|LOG
operator|.
name|info
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
name|consume
operator|.
name|uri
argument_list|()
argument_list|,
name|consume
operator|.
name|ref
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|subscribeMethod (Method method, Object bean, String endpointUri, String endpointName)
specifier|protected
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
name|endpointUri
parameter_list|,
name|String
name|endpointName
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
name|endpointUri
argument_list|,
name|endpointName
argument_list|,
name|injectionPointName
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
name|LOG
operator|.
name|info
argument_list|(
literal|"Created processor: "
operator|+
name|processor
argument_list|)
expr_stmt|;
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
name|startService
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
name|e
argument_list|)
expr_stmt|;
throw|throw
name|wrapRuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
block|}
DECL|method|startService (Service service)
specifier|protected
name|void
name|startService
parameter_list|(
name|Service
name|service
parameter_list|)
throws|throws
name|Exception
block|{
name|camelContext
operator|.
name|addService
argument_list|(
name|service
argument_list|)
expr_stmt|;
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
name|BeanProcessor
name|answer
init|=
operator|new
name|BeanProcessor
argument_list|(
name|pojo
argument_list|,
name|getCamelContext
argument_list|()
argument_list|)
decl_stmt|;
name|answer
operator|.
name|setMethodObject
argument_list|(
name|method
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
comment|/**      * Creates the object to be injected for an {@link org.apache.camel.EndpointInject} or {@link Produce} injection point      */
DECL|method|getInjectionValue (Class<?> type, String endpointUri, String endpointRef, String injectionPointName)
specifier|protected
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
name|injectionPointName
parameter_list|)
block|{
name|Endpoint
name|endpoint
init|=
name|getEndpointInjection
argument_list|(
name|endpointUri
argument_list|,
name|endpointRef
argument_list|,
name|injectionPointName
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
name|DefaultProducerTemplate
operator|.
name|class
argument_list|)
condition|)
block|{
return|return
operator|new
name|DefaultProducerTemplate
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
name|endpoint
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
literal|" which cannot be injected via @EndpointInject for "
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
comment|/**      * Factory method to create a started {@link PollingConsumer} to be injected      * into a POJO      */
DECL|method|createInjectionPollingConsumer (Endpoint endpoint)
specifier|protected
name|PollingConsumer
name|createInjectionPollingConsumer
parameter_list|(
name|Endpoint
name|endpoint
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
name|wrapRuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
comment|/**      * A Factory method to create a started {@link Producer} to be injected into      * a POJO      */
DECL|method|createInjectionProducer (Endpoint endpoint)
specifier|protected
name|Producer
name|createInjectionProducer
parameter_list|(
name|Endpoint
name|endpoint
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
argument_list|)
expr_stmt|;
return|return
name|producer
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
name|wrapRuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|getEndpointInjection (String uri, String name, String injectionPointName)
specifier|protected
name|Endpoint
name|getEndpointInjection
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|name
parameter_list|,
name|String
name|injectionPointName
parameter_list|)
block|{
name|Endpoint
name|endpoint
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|isNotNullAndNonEmpty
argument_list|(
name|uri
argument_list|)
condition|)
block|{
name|endpoint
operator|=
name|camelContext
operator|.
name|getEndpoint
argument_list|(
name|uri
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|isNullOrBlank
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|name
operator|=
name|injectionPointName
expr_stmt|;
block|}
name|endpoint
operator|=
operator|(
name|Endpoint
operator|)
name|applicationContext
operator|.
name|getBean
argument_list|(
name|name
argument_list|)
expr_stmt|;
if|if
condition|(
name|endpoint
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NoSuchBeanDefinitionException
argument_list|(
name|name
argument_list|)
throw|;
block|}
block|}
return|return
name|endpoint
return|;
block|}
block|}
end_class

end_unit

