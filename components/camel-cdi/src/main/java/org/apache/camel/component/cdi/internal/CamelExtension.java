begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cdi.internal
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|cdi
operator|.
name|internal
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
name|ejb
operator|.
name|Startup
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
name|ApplicationScoped
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
name|event
operator|.
name|Observes
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
name|AfterBeanDiscovery
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
name|AfterDeploymentValidation
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
name|AnnotatedType
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
name|javax
operator|.
name|enterprise
operator|.
name|inject
operator|.
name|spi
operator|.
name|BeforeShutdown
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
name|Extension
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
name|InjectionTarget
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
name|ProcessAnnotatedType
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
name|ProcessBean
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
name|ProcessInjectionTarget
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|inject
operator|.
name|Inject
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|inject
operator|.
name|Singleton
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
name|component
operator|.
name|cdi
operator|.
name|CdiCamelContext
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
name|DefaultCamelBeanPostProcessor
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
name|ReflectionHelper
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
name|util
operator|.
name|metadata
operator|.
name|builder
operator|.
name|AnnotatedTypeBuilder
import|;
end_import

begin_comment
comment|/**  * Set of camel specific hooks for CDI.  */
end_comment

begin_class
DECL|class|CamelExtension
specifier|public
class|class
name|CamelExtension
implements|implements
name|Extension
block|{
comment|/**      * Context instance.      */
DECL|field|camelContext
specifier|private
name|CamelContext
name|camelContext
decl_stmt|;
DECL|field|postProcessor
specifier|private
name|DefaultCamelBeanPostProcessor
name|postProcessor
decl_stmt|;
DECL|field|eagerBeans
specifier|private
name|Map
argument_list|<
name|Bean
argument_list|<
name|?
argument_list|>
argument_list|,
name|BeanAdapter
argument_list|>
name|eagerBeans
init|=
operator|new
name|HashMap
argument_list|<
name|Bean
argument_list|<
name|?
argument_list|>
argument_list|,
name|BeanAdapter
argument_list|>
argument_list|()
decl_stmt|;
DECL|method|CamelExtension ()
specifier|public
name|CamelExtension
parameter_list|()
block|{     }
comment|/**      * Process camel context aware bean definitions.      *      * @param process Annotated type.      * @throws Exception In case of exceptions.      */
DECL|method|contextAwareness (@bserves ProcessAnnotatedType<CamelContextAware> process)
specifier|protected
name|void
name|contextAwareness
parameter_list|(
annotation|@
name|Observes
name|ProcessAnnotatedType
argument_list|<
name|CamelContextAware
argument_list|>
name|process
parameter_list|)
throws|throws
name|Exception
block|{
name|AnnotatedType
argument_list|<
name|CamelContextAware
argument_list|>
name|annotatedType
init|=
name|process
operator|.
name|getAnnotatedType
argument_list|()
decl_stmt|;
name|Class
argument_list|<
name|CamelContextAware
argument_list|>
name|javaClass
init|=
name|annotatedType
operator|.
name|getJavaClass
argument_list|()
decl_stmt|;
if|if
condition|(
name|CamelContextAware
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|javaClass
argument_list|)
condition|)
block|{
name|Method
name|method
init|=
name|javaClass
operator|.
name|getMethod
argument_list|(
literal|"setCamelContext"
argument_list|,
name|CamelContext
operator|.
name|class
argument_list|)
decl_stmt|;
name|AnnotatedTypeBuilder
argument_list|<
name|CamelContextAware
argument_list|>
name|builder
init|=
operator|new
name|AnnotatedTypeBuilder
argument_list|<
name|CamelContextAware
argument_list|>
argument_list|()
operator|.
name|readFromType
argument_list|(
name|javaClass
argument_list|)
operator|.
name|addToMethod
argument_list|(
name|method
argument_list|,
operator|new
name|InjectLiteral
argument_list|()
argument_list|)
decl_stmt|;
name|process
operator|.
name|setAnnotatedType
argument_list|(
name|builder
operator|.
name|create
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Disable creation of default CamelContext bean and rely on context created      * and managed by extension.      *      * @param process Annotated type.      */
DECL|method|disableDefaultContext (@bserves ProcessAnnotatedType<CamelContext> process)
specifier|protected
name|void
name|disableDefaultContext
parameter_list|(
annotation|@
name|Observes
name|ProcessAnnotatedType
argument_list|<
name|CamelContext
argument_list|>
name|process
parameter_list|)
block|{
name|process
operator|.
name|veto
argument_list|()
expr_stmt|;
block|}
comment|/**      * Registers managed camel bean.      *      * @param abd     After bean discovery event.      * @param manager Bean manager.      */
DECL|method|registerManagedCamelContext (@bserves AfterBeanDiscovery abd, BeanManager manager)
specifier|protected
name|void
name|registerManagedCamelContext
parameter_list|(
annotation|@
name|Observes
name|AfterBeanDiscovery
name|abd
parameter_list|,
name|BeanManager
name|manager
parameter_list|)
block|{
name|abd
operator|.
name|addBean
argument_list|(
operator|new
name|CamelContextBean
argument_list|(
name|manager
operator|.
name|createInjectionTarget
argument_list|(
name|manager
operator|.
name|createAnnotatedType
argument_list|(
name|CdiCamelContext
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Start up camel context.      *      * @param adv After deployment validation event.      * @throws Exception In case of failures.      */
DECL|method|validate (@bserves AfterDeploymentValidation adv)
specifier|protected
name|void
name|validate
parameter_list|(
annotation|@
name|Observes
name|AfterDeploymentValidation
name|adv
parameter_list|)
throws|throws
name|Exception
block|{
name|getCamelContext
argument_list|()
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
comment|/**      * Shutdown camel context.      *      * @param bsd Shutdown event.      * @throws Exception In case of failures.      */
DECL|method|shutdown (@bserves BeforeShutdown bsd)
specifier|protected
name|void
name|shutdown
parameter_list|(
annotation|@
name|Observes
name|BeforeShutdown
name|bsd
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|camelContext
operator|!=
literal|null
condition|)
block|{
name|camelContext
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * Lets detect all beans annotated with @Consume      */
DECL|method|detectConsumeBeans (@bserves ProcessBean<?> event)
specifier|public
name|void
name|detectConsumeBeans
parameter_list|(
annotation|@
name|Observes
name|ProcessBean
argument_list|<
name|?
argument_list|>
name|event
parameter_list|)
block|{
specifier|final
name|Bean
argument_list|<
name|?
argument_list|>
name|bean
init|=
name|event
operator|.
name|getBean
argument_list|()
decl_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|beanClass
init|=
name|bean
operator|.
name|getBeanClass
argument_list|()
decl_stmt|;
name|ReflectionHelper
operator|.
name|doWithMethods
argument_list|(
name|beanClass
argument_list|,
operator|new
name|ReflectionHelper
operator|.
name|MethodCallback
argument_list|()
block|{
annotation|@
name|Override
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
name|eagerlyCreate
argument_list|(
name|bean
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
comment|// lets force singletons and application scoped objects
comment|// to be created eagerly to ensure they startup
if|if
condition|(
name|eagerlyCreateSingletonsOnStartup
argument_list|()
operator|&&
name|isApplicationScopeOrSingleton
argument_list|(
name|beanClass
argument_list|)
operator|&&
name|beanClass
operator|.
name|getAnnotation
argument_list|(
name|Startup
operator|.
name|class
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|eagerlyCreate
argument_list|(
name|bean
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Should we eagerly startup @Singleton and @ApplicationScoped beans annotated with @Startup?      * Defaults to true which enables us to start camel contexts on startup      */
DECL|method|eagerlyCreateSingletonsOnStartup ()
specifier|protected
name|boolean
name|eagerlyCreateSingletonsOnStartup
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
comment|/**      * Lets force the CDI container to create all beans annotated with @Consume so that the consumer becomes active      */
DECL|method|startConsumeBeans (@bserves AfterDeploymentValidation event, BeanManager beanManager)
specifier|public
name|void
name|startConsumeBeans
parameter_list|(
annotation|@
name|Observes
name|AfterDeploymentValidation
name|event
parameter_list|,
name|BeanManager
name|beanManager
parameter_list|)
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
literal|"camelContext"
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|Map
operator|.
name|Entry
argument_list|<
name|Bean
argument_list|<
name|?
argument_list|>
argument_list|,
name|BeanAdapter
argument_list|>
argument_list|>
name|entries
init|=
name|eagerBeans
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
name|Bean
argument_list|<
name|?
argument_list|>
argument_list|,
name|BeanAdapter
argument_list|>
name|entry
range|:
name|entries
control|)
block|{
name|Bean
argument_list|<
name|?
argument_list|>
name|bean
init|=
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|CreationalContext
argument_list|<
name|?
argument_list|>
name|creationalContext
init|=
name|beanManager
operator|.
name|createCreationalContext
argument_list|(
name|bean
argument_list|)
decl_stmt|;
comment|// force lazy creation
name|beanManager
operator|.
name|getReference
argument_list|(
name|bean
argument_list|,
name|Object
operator|.
name|class
argument_list|,
name|creationalContext
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Lets perform injection of all beans which use Camel annotations      */
DECL|method|onInjectionTarget (@bserves ProcessInjectionTarget<Object> event)
specifier|public
name|void
name|onInjectionTarget
parameter_list|(
annotation|@
name|Observes
name|ProcessInjectionTarget
argument_list|<
name|Object
argument_list|>
name|event
parameter_list|)
block|{
specifier|final
name|InjectionTarget
argument_list|<
name|Object
argument_list|>
name|injectionTarget
init|=
name|event
operator|.
name|getInjectionTarget
argument_list|()
decl_stmt|;
specifier|final
name|Class
argument_list|<
name|?
argument_list|>
name|beanClass
init|=
name|event
operator|.
name|getAnnotatedType
argument_list|()
operator|.
name|getJavaClass
argument_list|()
decl_stmt|;
comment|// TODO this is a bit of a hack - what should the bean name be?
specifier|final
name|String
name|beanName
init|=
name|event
operator|.
name|getInjectionTarget
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
specifier|final
name|BeanAdapter
name|adapter
init|=
name|createBeanAdapter
argument_list|(
name|beanClass
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|adapter
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|DelegateInjectionTarget
name|newTarget
init|=
operator|new
name|DelegateInjectionTarget
argument_list|(
name|injectionTarget
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|void
name|postConstruct
parameter_list|(
name|Object
name|instance
parameter_list|)
block|{
name|super
operator|.
name|postConstruct
argument_list|(
name|instance
argument_list|)
expr_stmt|;
comment|// now lets do the post instruct to inject our Camel injections
name|adapter
operator|.
name|inject
argument_list|(
name|getPostProcessor
argument_list|()
argument_list|,
name|instance
argument_list|,
name|beanName
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
name|event
operator|.
name|setInjectionTarget
argument_list|(
name|newTarget
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Perform injection on an existing bean such as a test case which is created directly by a testing framework.      *      * This is because BeanProvider.injectFields() does not invoke the onInjectionTarget() method so the injection      * of @Produce / @EndpointInject and processing of the @Consume annotations are not performed.      */
DECL|method|inject (Object bean)
specifier|public
name|void
name|inject
parameter_list|(
name|Object
name|bean
parameter_list|)
block|{
specifier|final
name|BeanAdapter
name|adapter
init|=
name|createBeanAdapter
argument_list|(
name|bean
operator|.
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|adapter
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
comment|// TODO this is a bit of a hack - what should the bean name be?
specifier|final
name|String
name|beanName
init|=
name|bean
operator|.
name|toString
argument_list|()
decl_stmt|;
name|adapter
operator|.
name|inject
argument_list|(
name|getPostProcessor
argument_list|()
argument_list|,
name|bean
argument_list|,
name|beanName
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|createBeanAdapter (Class<?> beanClass)
specifier|private
name|BeanAdapter
name|createBeanAdapter
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|beanClass
parameter_list|)
block|{
specifier|final
name|BeanAdapter
name|adapter
init|=
operator|new
name|BeanAdapter
argument_list|()
decl_stmt|;
name|ReflectionHelper
operator|.
name|doWithFields
argument_list|(
name|beanClass
argument_list|,
operator|new
name|ReflectionHelper
operator|.
name|FieldCallback
argument_list|()
block|{
annotation|@
name|Override
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
operator|!
name|injectAnnotatedField
argument_list|(
name|field
argument_list|)
condition|)
block|{
name|adapter
operator|.
name|addProduceField
argument_list|(
name|field
argument_list|)
expr_stmt|;
block|}
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
condition|)
block|{
name|adapter
operator|.
name|addEndpointField
argument_list|(
name|field
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
name|ReflectionHelper
operator|.
name|doWithMethods
argument_list|(
name|beanClass
argument_list|,
operator|new
name|ReflectionHelper
operator|.
name|MethodCallback
argument_list|()
block|{
annotation|@
name|Override
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
name|adapter
operator|.
name|addConsumeMethod
argument_list|(
name|method
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
name|adapter
operator|.
name|addProduceMethod
argument_list|(
name|method
argument_list|)
expr_stmt|;
block|}
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
condition|)
block|{
name|adapter
operator|.
name|addEndpointMethod
argument_list|(
name|method
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
return|return
name|adapter
return|;
block|}
DECL|method|getPostProcessor ()
specifier|protected
name|DefaultCamelBeanPostProcessor
name|getPostProcessor
parameter_list|()
block|{
if|if
condition|(
name|postProcessor
operator|==
literal|null
condition|)
block|{
name|postProcessor
operator|=
operator|new
name|DefaultCamelBeanPostProcessor
argument_list|(
name|getCamelContext
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|postProcessor
return|;
block|}
DECL|method|eagerlyCreate (Bean<?> bean)
specifier|protected
name|BeanAdapter
name|eagerlyCreate
parameter_list|(
name|Bean
argument_list|<
name|?
argument_list|>
name|bean
parameter_list|)
block|{
name|BeanAdapter
name|beanAdapter
init|=
name|eagerBeans
operator|.
name|get
argument_list|(
name|bean
argument_list|)
decl_stmt|;
if|if
condition|(
name|beanAdapter
operator|==
literal|null
condition|)
block|{
name|beanAdapter
operator|=
operator|new
name|BeanAdapter
argument_list|()
expr_stmt|;
name|eagerBeans
operator|.
name|put
argument_list|(
name|bean
argument_list|,
name|beanAdapter
argument_list|)
expr_stmt|;
block|}
return|return
name|beanAdapter
return|;
block|}
DECL|method|getCamelContext ()
specifier|public
name|CamelContext
name|getCamelContext
parameter_list|()
block|{
if|if
condition|(
name|camelContext
operator|==
literal|null
condition|)
block|{
name|camelContext
operator|=
name|BeanProvider
operator|.
name|getContextualReference
argument_list|(
name|CamelContext
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
return|return
name|camelContext
return|;
block|}
comment|/**      * Returns true if this field is annotated with @Inject      */
DECL|method|injectAnnotatedField (Field field)
specifier|protected
specifier|static
name|boolean
name|injectAnnotatedField
parameter_list|(
name|Field
name|field
parameter_list|)
block|{
return|return
name|field
operator|.
name|getAnnotation
argument_list|(
name|Inject
operator|.
name|class
argument_list|)
operator|!=
literal|null
return|;
block|}
comment|/**      * Returns true for singletons or application scoped beans      */
DECL|method|isApplicationScopeOrSingleton (Class<?> aClass)
specifier|private
name|boolean
name|isApplicationScopeOrSingleton
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|aClass
parameter_list|)
block|{
return|return
name|aClass
operator|.
name|getAnnotation
argument_list|(
name|Singleton
operator|.
name|class
argument_list|)
operator|!=
literal|null
operator|||
name|aClass
operator|.
name|getAnnotation
argument_list|(
name|ApplicationScoped
operator|.
name|class
argument_list|)
operator|!=
literal|null
return|;
block|}
block|}
end_class

end_unit

