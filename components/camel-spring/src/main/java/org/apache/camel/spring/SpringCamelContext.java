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
name|event
operator|.
name|EventComponent
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
name|event
operator|.
name|EventEndpoint
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
name|ProcessorEndpoint
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
name|Injector
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
name|ManagementMBeanAssembler
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
name|Registry
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
name|spi
operator|.
name|ApplicationContextRegistry
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
name|spi
operator|.
name|SpringInjector
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
name|spi
operator|.
name|SpringManagementMBeanAssembler
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
name|DisposableBean
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
name|InitializingBean
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
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|ApplicationEvent
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
name|ConfigurableApplicationContext
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
name|event
operator|.
name|ContextRefreshedEvent
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
name|event
operator|.
name|ContextStoppedEvent
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
name|support
operator|.
name|ClassPathXmlApplicationContext
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
comment|/**  * A Spring aware implementation of {@link org.apache.camel.CamelContext} which  * will automatically register itself with Springs lifecycle methods plus allows  * spring to be used to customize a any<a  * href="http://camel.apache.org/type-converter.html">Type Converters</a>  * as well as supporting accessing components and beans via the Spring  * {@link ApplicationContext}  *  * @version   */
end_comment

begin_class
DECL|class|SpringCamelContext
specifier|public
class|class
name|SpringCamelContext
extends|extends
name|DefaultCamelContext
implements|implements
name|InitializingBean
implements|,
name|DisposableBean
implements|,
name|ApplicationContextAware
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|SpringCamelContext
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|NO_START
specifier|private
specifier|static
specifier|final
name|ThreadLocal
argument_list|<
name|Boolean
argument_list|>
name|NO_START
init|=
operator|new
name|ThreadLocal
argument_list|<
name|Boolean
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|applicationContext
specifier|private
name|ApplicationContext
name|applicationContext
decl_stmt|;
DECL|field|eventEndpoint
specifier|private
name|EventEndpoint
name|eventEndpoint
decl_stmt|;
DECL|method|SpringCamelContext ()
specifier|public
name|SpringCamelContext
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
block|}
DECL|method|SpringCamelContext (ApplicationContext applicationContext)
specifier|public
name|SpringCamelContext
parameter_list|(
name|ApplicationContext
name|applicationContext
parameter_list|)
block|{
name|this
argument_list|()
expr_stmt|;
name|setApplicationContext
argument_list|(
name|applicationContext
argument_list|)
expr_stmt|;
block|}
DECL|method|setNoStart (boolean b)
specifier|public
specifier|static
name|void
name|setNoStart
parameter_list|(
name|boolean
name|b
parameter_list|)
block|{
if|if
condition|(
name|b
condition|)
block|{
name|NO_START
operator|.
name|set
argument_list|(
name|b
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|NO_START
operator|.
name|remove
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|springCamelContext (ApplicationContext applicationContext)
specifier|public
specifier|static
name|SpringCamelContext
name|springCamelContext
parameter_list|(
name|ApplicationContext
name|applicationContext
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|springCamelContext
argument_list|(
name|applicationContext
argument_list|,
literal|true
argument_list|)
return|;
block|}
DECL|method|springCamelContext (ApplicationContext applicationContext, boolean maybeStart)
specifier|public
specifier|static
name|SpringCamelContext
name|springCamelContext
parameter_list|(
name|ApplicationContext
name|applicationContext
parameter_list|,
name|boolean
name|maybeStart
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|applicationContext
operator|!=
literal|null
condition|)
block|{
comment|// lets try and look up a configured camel context in the context
name|String
index|[]
name|names
init|=
name|applicationContext
operator|.
name|getBeanNamesForType
argument_list|(
name|SpringCamelContext
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|names
operator|.
name|length
operator|==
literal|1
condition|)
block|{
return|return
operator|(
name|SpringCamelContext
operator|)
name|applicationContext
operator|.
name|getBean
argument_list|(
name|names
index|[
literal|0
index|]
argument_list|,
name|SpringCamelContext
operator|.
name|class
argument_list|)
return|;
block|}
block|}
name|SpringCamelContext
name|answer
init|=
operator|new
name|SpringCamelContext
argument_list|()
decl_stmt|;
name|answer
operator|.
name|setApplicationContext
argument_list|(
name|applicationContext
argument_list|)
expr_stmt|;
if|if
condition|(
name|maybeStart
condition|)
block|{
name|answer
operator|.
name|afterPropertiesSet
argument_list|()
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
DECL|method|springCamelContext (String configLocations)
specifier|public
specifier|static
name|SpringCamelContext
name|springCamelContext
parameter_list|(
name|String
name|configLocations
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|springCamelContext
argument_list|(
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
name|configLocations
argument_list|)
argument_list|)
return|;
block|}
DECL|method|afterPropertiesSet ()
specifier|public
name|void
name|afterPropertiesSet
parameter_list|()
throws|throws
name|Exception
block|{
name|maybeStart
argument_list|()
expr_stmt|;
block|}
DECL|method|destroy ()
specifier|public
name|void
name|destroy
parameter_list|()
throws|throws
name|Exception
block|{
name|stop
argument_list|()
expr_stmt|;
block|}
DECL|method|onApplicationEvent (ApplicationEvent event)
specifier|public
name|void
name|onApplicationEvent
parameter_list|(
name|ApplicationEvent
name|event
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"onApplicationEvent: {}"
argument_list|,
name|event
argument_list|)
expr_stmt|;
if|if
condition|(
name|event
operator|instanceof
name|ContextRefreshedEvent
condition|)
block|{
comment|// now lets start the CamelContext so that all its possible
comment|// dependencies are initialized
try|try
block|{
name|maybeStart
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
name|wrapRuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
elseif|else
if|if
condition|(
name|event
operator|instanceof
name|ContextStoppedEvent
condition|)
block|{
try|try
block|{
name|maybeStop
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
name|wrapRuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
if|if
condition|(
name|eventEndpoint
operator|!=
literal|null
condition|)
block|{
name|eventEndpoint
operator|.
name|onApplicationEvent
argument_list|(
name|event
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"No spring-event endpoint enabled to handle event: {}"
argument_list|,
name|event
argument_list|)
expr_stmt|;
block|}
block|}
comment|// Properties
comment|// -----------------------------------------------------------------------
DECL|method|getApplicationContext ()
specifier|public
name|ApplicationContext
name|getApplicationContext
parameter_list|()
block|{
return|return
name|applicationContext
return|;
block|}
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
name|ClassLoader
name|cl
decl_stmt|;
comment|// set the application context classloader
if|if
condition|(
name|applicationContext
operator|!=
literal|null
operator|&&
name|applicationContext
operator|.
name|getClassLoader
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|cl
operator|=
name|applicationContext
operator|.
name|getClassLoader
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Cannot find the class loader from application context, using the thread context class loader instead"
argument_list|)
expr_stmt|;
name|cl
operator|=
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
expr_stmt|;
block|}
name|LOG
operator|.
name|debug
argument_list|(
literal|"Set the application context classloader to: {}"
argument_list|,
name|cl
argument_list|)
expr_stmt|;
name|this
operator|.
name|setApplicationContextClassLoader
argument_list|(
name|cl
argument_list|)
expr_stmt|;
if|if
condition|(
name|applicationContext
operator|instanceof
name|ConfigurableApplicationContext
condition|)
block|{
comment|// only add if not already added
if|if
condition|(
name|hasComponent
argument_list|(
literal|"spring-event"
argument_list|)
operator|==
literal|null
condition|)
block|{
name|addComponent
argument_list|(
literal|"spring-event"
argument_list|,
operator|new
name|EventComponent
argument_list|(
name|applicationContext
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|getEventEndpoint ()
specifier|public
name|EventEndpoint
name|getEventEndpoint
parameter_list|()
block|{
return|return
name|eventEndpoint
return|;
block|}
DECL|method|setEventEndpoint (EventEndpoint eventEndpoint)
specifier|public
name|void
name|setEventEndpoint
parameter_list|(
name|EventEndpoint
name|eventEndpoint
parameter_list|)
block|{
name|this
operator|.
name|eventEndpoint
operator|=
name|eventEndpoint
expr_stmt|;
block|}
comment|// Implementation methods
comment|// -----------------------------------------------------------------------
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
if|if
condition|(
name|eventEndpoint
operator|==
literal|null
condition|)
block|{
name|eventEndpoint
operator|=
name|createEventEndpoint
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|createInjector ()
specifier|protected
name|Injector
name|createInjector
parameter_list|()
block|{
if|if
condition|(
name|applicationContext
operator|instanceof
name|ConfigurableApplicationContext
condition|)
block|{
return|return
operator|new
name|SpringInjector
argument_list|(
operator|(
name|ConfigurableApplicationContext
operator|)
name|applicationContext
argument_list|)
return|;
block|}
else|else
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Cannot use SpringInjector as applicationContext is not a ConfigurableApplicationContext as its: "
operator|+
name|applicationContext
argument_list|)
expr_stmt|;
return|return
name|super
operator|.
name|createInjector
argument_list|()
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|createManagementMBeanAssembler ()
specifier|protected
name|ManagementMBeanAssembler
name|createManagementMBeanAssembler
parameter_list|()
block|{
comment|// use a spring mbean assembler
return|return
operator|new
name|SpringManagementMBeanAssembler
argument_list|()
return|;
block|}
DECL|method|createEventEndpoint ()
specifier|protected
name|EventEndpoint
name|createEventEndpoint
parameter_list|()
block|{
return|return
name|getEndpoint
argument_list|(
literal|"spring-event:default"
argument_list|,
name|EventEndpoint
operator|.
name|class
argument_list|)
return|;
block|}
DECL|method|convertBeanToEndpoint (String uri, Object bean)
specifier|protected
name|Endpoint
name|convertBeanToEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|Object
name|bean
parameter_list|)
block|{
comment|// We will use the type convert to build the endpoint first
name|Endpoint
name|endpoint
init|=
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|Endpoint
operator|.
name|class
argument_list|,
name|bean
argument_list|)
decl_stmt|;
if|if
condition|(
name|endpoint
operator|!=
literal|null
condition|)
block|{
name|endpoint
operator|.
name|setCamelContext
argument_list|(
name|this
argument_list|)
expr_stmt|;
return|return
name|endpoint
return|;
block|}
return|return
operator|new
name|ProcessorEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|,
operator|new
name|BeanProcessor
argument_list|(
name|bean
argument_list|,
name|this
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createRegistry ()
specifier|protected
name|Registry
name|createRegistry
parameter_list|()
block|{
return|return
operator|new
name|ApplicationContextRegistry
argument_list|(
name|getApplicationContext
argument_list|()
argument_list|)
return|;
block|}
DECL|method|maybeStart ()
specifier|private
name|void
name|maybeStart
parameter_list|()
throws|throws
name|Exception
block|{
comment|// for example from unit testing we want to start Camel later and not when Spring framework
comment|// publish a ContextRefreshedEvent
if|if
condition|(
name|NO_START
operator|.
name|get
argument_list|()
operator|==
literal|null
condition|)
block|{
if|if
condition|(
operator|!
name|isStarted
argument_list|()
operator|&&
operator|!
name|isStarting
argument_list|()
condition|)
block|{
name|start
argument_list|()
expr_stmt|;
block|}
else|else
block|{
comment|// ignore as Camel is already started
name|LOG
operator|.
name|trace
argument_list|(
literal|"Ignoring maybeStart() as Apache Camel is already started"
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Ignoring maybeStart() as NO_START is false"
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|maybeStop ()
specifier|private
name|void
name|maybeStop
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|isStopping
argument_list|()
operator|&&
operator|!
name|isStopped
argument_list|()
condition|)
block|{
name|stop
argument_list|()
expr_stmt|;
block|}
else|else
block|{
comment|// ignore as Camel is already stopped
name|LOG
operator|.
name|trace
argument_list|(
literal|"Ignoring maybeStop() as Apache Camel is already stopped"
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"SpringCamelContext("
argument_list|)
operator|.
name|append
argument_list|(
name|getName
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|")"
argument_list|)
expr_stmt|;
if|if
condition|(
name|applicationContext
operator|!=
literal|null
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|" with spring id "
argument_list|)
operator|.
name|append
argument_list|(
name|applicationContext
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

