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
name|support
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
name|ModelJAXBContextFactory
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
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|StopWatch
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
name|ApplicationListener
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
name|Lifecycle
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
name|Phased
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
name|core
operator|.
name|Ordered
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
name|RuntimeCamelException
operator|.
name|wrapRuntimeCamelException
import|;
end_import

begin_comment
comment|/**  * A Spring aware implementation of {@link org.apache.camel.CamelContext} which  * will automatically register itself with Springs lifecycle methods plus allows  * spring to be used to customize a any<a  * href="http://camel.apache.org/type-converter.html">Type Converters</a>  * as well as supporting accessing components and beans via the Spring  * {@link ApplicationContext}  */
end_comment

begin_class
DECL|class|SpringCamelContext
specifier|public
class|class
name|SpringCamelContext
extends|extends
name|DefaultCamelContext
implements|implements
name|Lifecycle
implements|,
name|ApplicationContextAware
implements|,
name|Phased
implements|,
name|ApplicationListener
argument_list|<
name|ApplicationEvent
argument_list|>
implements|,
name|Ordered
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
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|applicationContext
specifier|private
name|ApplicationContext
name|applicationContext
decl_stmt|;
DECL|field|eventComponent
specifier|private
name|EventComponent
name|eventComponent
decl_stmt|;
DECL|field|shutdownEager
specifier|private
name|boolean
name|shutdownEager
init|=
literal|true
decl_stmt|;
DECL|method|SpringCamelContext ()
specifier|public
name|SpringCamelContext
parameter_list|()
block|{
name|super
argument_list|(
literal|false
argument_list|)
expr_stmt|;
if|if
condition|(
name|Boolean
operator|.
name|getBoolean
argument_list|(
literal|"org.apache.camel.jmx.disabled"
argument_list|)
condition|)
block|{
name|disableJMX
argument_list|()
expr_stmt|;
block|}
name|setManagementMBeanAssembler
argument_list|(
operator|new
name|SpringManagementMBeanAssembler
argument_list|(
name|this
argument_list|)
argument_list|)
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
literal|true
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|NO_START
operator|.
name|set
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * @deprecated its better to create and boot Spring the standard Spring way and to get hold of CamelContext      * using the Spring API.      */
annotation|@
name|Deprecated
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
name|answer
operator|.
name|init
argument_list|()
expr_stmt|;
if|if
condition|(
name|maybeStart
condition|)
block|{
name|answer
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
annotation|@
name|Override
DECL|method|start ()
specifier|public
name|void
name|start
parameter_list|()
block|{
comment|// for example from unit testing we want to start Camel later (manually)
if|if
condition|(
name|Boolean
operator|.
name|TRUE
operator|.
name|equals
argument_list|(
name|NO_START
operator|.
name|get
argument_list|()
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Ignoring start() as NO_START is false"
argument_list|)
expr_stmt|;
return|return;
block|}
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
try|try
block|{
name|StopWatch
name|watch
init|=
operator|new
name|StopWatch
argument_list|()
decl_stmt|;
name|super
operator|.
name|start
argument_list|()
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"start() took {} millis"
argument_list|,
name|watch
operator|.
name|taken
argument_list|()
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
name|wrapRuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
else|else
block|{
comment|// ignore as Camel is already started
name|LOG
operator|.
name|trace
argument_list|(
literal|"Ignoring start() as Camel is already started"
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|stop ()
specifier|public
name|void
name|stop
parameter_list|()
block|{
try|try
block|{
name|super
operator|.
name|stop
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
annotation|@
name|Override
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
comment|// nominally we would prefer to use Lifecycle interface that
comment|// would invoke start() method, but in order to do that
comment|// SpringCamelContext needs to implement SmartLifecycle
comment|// (look at DefaultLifecycleProcessor::startBeans), but it
comment|// cannot implement it as it already implements
comment|// RuntimeConfiguration, and both SmartLifecycle and
comment|// RuntimeConfiguration declare isAutoStartup method but
comment|// with boolean and Boolean return types, and covariant
comment|// methods with primitive types are not allowed by the JLS
comment|// so we need to listen for ContextRefreshedEvent and start
comment|// on its reception
name|start
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|eventComponent
operator|!=
literal|null
condition|)
block|{
name|eventComponent
operator|.
name|onApplicationEvent
argument_list|(
name|event
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|getOrder ()
specifier|public
name|int
name|getOrder
parameter_list|()
block|{
comment|// SpringCamelContext implements Ordered so that it's the last
comment|// in ApplicationListener to receive events, this is important
comment|// for startup as we want all resources to be ready and all
comment|// routes added to the context (see
comment|// org.apache.camel.spring.boot.RoutesCollector)
comment|// and we need to be after CamelContextFactoryBean
return|return
name|LOWEST_PRECEDENCE
return|;
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
annotation|@
name|Override
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
name|eventComponent
operator|=
operator|new
name|EventComponent
argument_list|(
name|applicationContext
argument_list|)
expr_stmt|;
name|addComponent
argument_list|(
literal|"spring-event"
argument_list|,
name|eventComponent
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Whether to shutdown this {@link org.apache.camel.spring.SpringCamelContext} eager (first)      * when Spring {@link org.springframework.context.ApplicationContext} is being stopped.      *<p/>      *<b>Important:</b> This option is default<tt>true</tt> which ensures we shutdown Camel      * before other beans. Setting this to<tt>false</tt> restores old behavior in earlier      * Camel releases, which can be used for special cases to behave as before.      *      * @return<tt>true</tt> to shutdown eager (first),<tt>false</tt> to shutdown last      */
DECL|method|isShutdownEager ()
specifier|public
name|boolean
name|isShutdownEager
parameter_list|()
block|{
return|return
name|shutdownEager
return|;
block|}
comment|/**      * @see #isShutdownEager()      */
DECL|method|setShutdownEager (boolean shutdownEager)
specifier|public
name|void
name|setShutdownEager
parameter_list|(
name|boolean
name|shutdownEager
parameter_list|)
block|{
name|this
operator|.
name|shutdownEager
operator|=
name|shutdownEager
expr_stmt|;
block|}
comment|// Implementation methods
comment|// -----------------------------------------------------------------------
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
annotation|@
name|Override
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
annotation|@
name|Override
DECL|method|createModelJAXBContextFactory ()
specifier|protected
name|ModelJAXBContextFactory
name|createModelJAXBContextFactory
parameter_list|()
block|{
return|return
operator|new
name|SpringModelJAXBContextFactory
argument_list|()
return|;
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
annotation|@
name|Override
DECL|method|getPhase ()
specifier|public
name|int
name|getPhase
parameter_list|()
block|{
comment|// the context is started by invoking start method which
comment|// happens either on ContextRefreshedEvent or explicitly
comment|// invoking the method, for instance CamelContextFactoryBean
comment|// is using that to start the context, _so_ here we want to
comment|// have maximum priority as the getPhase() will be used only
comment|// for stopping, in order to be used for starting we would
comment|// need to implement SmartLifecycle which we cannot
comment|// (explained in comment in the onApplicationEvent method)
comment|// we use LOWEST_PRECEDENCE here as this is taken into account
comment|// only when stopping and then in reversed order
return|return
name|LOWEST_PRECEDENCE
return|;
block|}
annotation|@
name|Override
DECL|method|isRunning ()
specifier|public
name|boolean
name|isRunning
parameter_list|()
block|{
return|return
operator|!
name|isStopping
argument_list|()
operator|&&
operator|!
name|isStopped
argument_list|()
return|;
block|}
block|}
end_class

end_unit

