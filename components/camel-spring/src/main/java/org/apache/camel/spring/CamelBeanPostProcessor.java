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
comment|/**  * Spring specific {@link DefaultCamelBeanPostProcessor} which uses Spring {@link BeanPostProcessor} to post process beans.  *  * @see DefaultCamelBeanPostProcessor  */
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
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
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
DECL|field|camelId
specifier|private
name|String
name|camelId
decl_stmt|;
comment|// must use a delegate, as we cannot extend DefaultCamelBeanPostProcessor, as this will cause the
comment|// XSD schema generator to include the DefaultCamelBeanPostProcessor as a type, which we do not want to
annotation|@
name|XmlTransient
DECL|field|delegate
specifier|private
specifier|final
name|DefaultCamelBeanPostProcessor
name|delegate
init|=
operator|new
name|DefaultCamelBeanPostProcessor
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|CamelContext
name|getOrLookupCamelContext
parameter_list|()
block|{
if|if
condition|(
name|camelContext
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|camelId
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Looking up CamelContext by id: {} from Spring ApplicationContext: {}"
argument_list|,
name|camelId
argument_list|,
name|applicationContext
argument_list|)
expr_stmt|;
name|camelContext
operator|=
name|applicationContext
operator|.
name|getBean
argument_list|(
name|camelId
argument_list|,
name|CamelContext
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// lookup by type and grab the single CamelContext if exists
name|LOG
operator|.
name|trace
argument_list|(
literal|"Looking up CamelContext by type from Spring ApplicationContext: {}"
argument_list|,
name|applicationContext
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|CamelContext
argument_list|>
name|contexts
init|=
name|applicationContext
operator|.
name|getBeansOfType
argument_list|(
name|CamelContext
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|contexts
operator|!=
literal|null
operator|&&
name|contexts
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
name|camelContext
operator|=
name|contexts
operator|.
name|values
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
expr_stmt|;
block|}
block|}
block|}
return|return
name|camelContext
return|;
block|}
annotation|@
name|Override
specifier|public
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
comment|// We should also avoid to process the null object bean (in Spring 2.5.x)
if|if
condition|(
name|bean
operator|==
literal|null
operator|||
name|bean
operator|instanceof
name|CamelJMXAgentDefinition
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
name|super
operator|.
name|canPostProcessBean
argument_list|(
name|bean
argument_list|,
name|beanName
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|CamelPostProcessorHelper
name|getPostProcessorHelper
parameter_list|()
block|{
comment|// lets lazily create the post processor
if|if
condition|(
name|camelPostProcessorHelper
operator|==
literal|null
condition|)
block|{
name|camelPostProcessorHelper
operator|=
operator|new
name|CamelPostProcessorHelper
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|CamelContext
name|getCamelContext
parameter_list|()
block|{
comment|// lets lazily lookup the camel context here
comment|// as doing this will cause this context to be started immediately
comment|// breaking the lifecycle ordering of different camel contexts
comment|// so we only want to do this on demand
return|return
name|delegate
operator|.
name|getOrLookupCamelContext
argument_list|()
return|;
block|}
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
name|CamelBeanPostProcessor
operator|.
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
return|return
name|camelPostProcessorHelper
return|;
block|}
block|}
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
try|try
block|{
return|return
name|delegate
operator|.
name|postProcessBeforeInitialization
argument_list|(
name|bean
argument_list|,
name|beanName
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// do not wrap already beans exceptions
if|if
condition|(
name|e
operator|instanceof
name|BeansException
condition|)
block|{
throw|throw
operator|(
name|BeansException
operator|)
name|e
throw|;
block|}
throw|throw
operator|new
name|GenericBeansException
argument_list|(
literal|"Error post processing bean: "
operator|+
name|beanName
argument_list|,
name|e
argument_list|)
throw|;
block|}
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
try|try
block|{
return|return
name|delegate
operator|.
name|postProcessAfterInitialization
argument_list|(
name|bean
argument_list|,
name|beanName
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// do not wrap already beans exceptions
if|if
condition|(
name|e
operator|instanceof
name|BeansException
condition|)
block|{
throw|throw
operator|(
name|BeansException
operator|)
name|e
throw|;
block|}
throw|throw
operator|new
name|GenericBeansException
argument_list|(
literal|"Error post processing bean: "
operator|+
name|beanName
argument_list|,
name|e
argument_list|)
throw|;
block|}
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
block|}
end_class

end_unit

