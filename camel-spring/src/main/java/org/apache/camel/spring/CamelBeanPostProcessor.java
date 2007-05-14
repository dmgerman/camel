begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|org
operator|.
name|aopalliance
operator|.
name|intercept
operator|.
name|MethodInvocation
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
name|Exchange
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
name|RuntimeCamelException
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
name|CamelTemplate
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
name|spring
operator|.
name|util
operator|.
name|DefaultMethodInvocationStrategy
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
name|MethodInvocationStrategy
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
name|isNotNullOrBlank
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
name|ArrayList
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

begin_comment
comment|/**  * A post processor to perform injection of {@link Endpoint} and {@link Producer} instances together with binding  * methods annotated with {@link @MessageDriven} to a Camel consumer.  *  * @version $Revision: 1.1 $  */
end_comment

begin_class
DECL|class|CamelBeanPostProcessor
specifier|public
class|class
name|CamelBeanPostProcessor
implements|implements
name|BeanPostProcessor
implements|,
name|ApplicationContextAware
block|{
DECL|field|log
specifier|private
specifier|static
specifier|final
specifier|transient
name|Log
name|log
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
DECL|field|camelContext
specifier|private
name|CamelContext
name|camelContext
decl_stmt|;
DECL|field|applicationContext
specifier|private
name|ApplicationContext
name|applicationContext
decl_stmt|;
DECL|field|invocationStrategy
specifier|private
name|MethodInvocationStrategy
name|invocationStrategy
init|=
operator|new
name|DefaultMethodInvocationStrategy
argument_list|()
decl_stmt|;
comment|//private List<Consumer> consumers = new ArrayList<Consumer>();
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
comment|//-------------------------------------------------------------------------
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
DECL|method|getInvocationStrategy ()
specifier|public
name|MethodInvocationStrategy
name|getInvocationStrategy
parameter_list|()
block|{
return|return
name|invocationStrategy
return|;
block|}
DECL|method|setInvocationStrategy (MethodInvocationStrategy invocationStrategy)
specifier|public
name|void
name|setInvocationStrategy
parameter_list|(
name|MethodInvocationStrategy
name|invocationStrategy
parameter_list|)
block|{
name|this
operator|.
name|invocationStrategy
operator|=
name|invocationStrategy
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
comment|// Implementation methods
comment|//-------------------------------------------------------------------------
comment|/**      * A strategy method to allow implementations to perform some custom JBI based injection of the POJO      *      * @param bean the bean to be injected      */
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
name|ReflectionUtils
operator|.
name|setField
argument_list|(
name|field
argument_list|,
name|bean
argument_list|,
name|getEndpointInjectionValue
argument_list|(
name|annotation
argument_list|,
name|field
operator|.
name|getType
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
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
name|log
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
name|Object
name|value
init|=
name|getEndpointInjectionValue
argument_list|(
name|annoation
argument_list|,
name|parameterTypes
index|[
literal|0
index|]
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
comment|/*                  TODO support callbacks?                  if (method.getAnnotation(Callback.class) != null) {                     try {                         Expression e = ExpressionFactory.createExpression(                                 method.getAnnotation(Callback.class).condition());                         JexlContext jc = JexlHelper.createContext();                         jc.getVars().put("this", obj);                         Object r = e.evaluate(jc);                         if (!(r instanceof Boolean)) {                             throw new RuntimeException("Expression did not returned a boolean value but: " + r);                         }                         Boolean oldVal = req.getCallbacks().get(method);                         Boolean newVal = (Boolean) r;                         if ((oldVal == null || !oldVal)&& newVal) {                             req.getCallbacks().put(method, newVal);                             method.invoke(obj, new Object[0]);                             // TODO: handle return value and sent it as the answer                         }                     } catch (Exception e) {                         throw new RuntimeException("Unable to invoke callback", e);                     }                 }                 */
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
name|log
operator|.
name|info
argument_list|(
literal|"Creating a consumer for: "
operator|+
name|annotation
argument_list|)
expr_stmt|;
comment|// lets bind this method to a listener
name|Endpoint
name|endpoint
init|=
name|getEndpointInjection
argument_list|(
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
name|log
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
name|consumer
operator|.
name|start
argument_list|()
expr_stmt|;
name|addConsumer
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
name|log
operator|.
name|warn
argument_list|(
name|e
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
block|}
block|}
comment|/**      * Create a processor which invokes the given method when an incoming message exchange is received      */
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
specifier|final
name|BeanInfo
name|beanInfo
init|=
operator|new
name|BeanInfo
argument_list|(
name|pojo
operator|.
name|getClass
argument_list|()
argument_list|,
name|invocationStrategy
argument_list|)
decl_stmt|;
return|return
operator|new
name|Processor
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"Processor on "
operator|+
name|endpoint
return|;
block|}
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|log
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|">>>> invoking method for: "
operator|+
name|exchange
argument_list|)
expr_stmt|;
block|}
name|MethodInvocation
name|invocation
init|=
name|beanInfo
operator|.
name|createInvocation
argument_list|(
name|method
argument_list|,
name|pojo
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
if|if
condition|(
name|invocation
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"No method invocation could be created"
argument_list|)
throw|;
block|}
try|try
block|{
name|invocation
operator|.
name|proceed
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
name|e
throw|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|throwable
parameter_list|)
block|{
throw|throw
operator|new
name|Exception
argument_list|(
name|throwable
argument_list|)
throw|;
block|}
block|}
block|}
return|;
block|}
DECL|method|addConsumer (Consumer consumer)
specifier|protected
name|void
name|addConsumer
parameter_list|(
name|Consumer
name|consumer
parameter_list|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Adding consumer: "
operator|+
name|consumer
argument_list|)
expr_stmt|;
comment|//consumers.add(consumer);
block|}
comment|/**      * Creates the value for the injection point for the given annotation      */
DECL|method|getEndpointInjectionValue (EndpointInject annotation, Class<?> type)
specifier|protected
name|Object
name|getEndpointInjectionValue
parameter_list|(
name|EndpointInject
name|annotation
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|)
block|{
name|Endpoint
name|endpoint
init|=
name|getEndpointInjection
argument_list|(
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
try|try
block|{
return|return
name|endpoint
operator|.
name|createProducer
argument_list|()
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
name|RuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
elseif|else
if|if
condition|(
name|type
operator|.
name|isAssignableFrom
argument_list|(
name|CamelTemplate
operator|.
name|class
argument_list|)
condition|)
block|{
return|return
operator|new
name|CamelTemplate
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
name|endpoint
argument_list|)
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
DECL|method|getEndpointInjection (String uri, String name)
specifier|protected
name|Endpoint
name|getEndpointInjection
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|name
parameter_list|)
block|{
name|Endpoint
name|endpoint
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|isNotNullOrBlank
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
name|isNotNullOrBlank
argument_list|(
name|name
argument_list|)
condition|)
block|{
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
else|else
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"No uri or name specified on @EndpointInject annotation!"
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|endpoint
return|;
block|}
block|}
end_class

end_unit

