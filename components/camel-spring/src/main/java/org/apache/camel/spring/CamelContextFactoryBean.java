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
name|XmlAttribute
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
name|XmlElement
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
name|XmlElementRef
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
name|XmlElements
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
name|builder
operator|.
name|RouteBuilder
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
name|DefaultLifecycleStrategy
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
name|management
operator|.
name|DefaultInstrumentationAgent
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
name|management
operator|.
name|InstrumentationLifecycleStrategy
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
name|model
operator|.
name|IdentifiedType
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
name|model
operator|.
name|RouteBuilderRef
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
name|model
operator|.
name|RouteContainer
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
name|model
operator|.
name|RouteType
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
name|model
operator|.
name|dataformat
operator|.
name|DataFormatType
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
name|interceptor
operator|.
name|Debugger
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
name|interceptor
operator|.
name|Tracer
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
name|LifecycleStrategy
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
name|FactoryBean
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
name|event
operator|.
name|ContextRefreshedEvent
import|;
end_import

begin_comment
comment|/**  * A Spring {@link FactoryBean} to create and initialize a  * {@link SpringCamelContext} and install routes either explicitly configured in  * Spring XML or found by searching the classpath for Java classes which extend  * {@link RouteBuilder} using the nested {@link #setPackages(String[])}.  *  * @version $Revision$  */
end_comment

begin_class
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"camelContext"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|CamelContextFactoryBean
specifier|public
class|class
name|CamelContextFactoryBean
extends|extends
name|IdentifiedType
implements|implements
name|RouteContainer
implements|,
name|FactoryBean
implements|,
name|InitializingBean
implements|,
name|DisposableBean
implements|,
name|ApplicationContextAware
implements|,
name|ApplicationListener
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|CamelContextFactoryBean
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|XmlAttribute
argument_list|(
name|required
operator|=
literal|false
argument_list|)
DECL|field|useJmx
specifier|private
name|Boolean
name|useJmx
init|=
name|Boolean
operator|.
name|TRUE
decl_stmt|;
annotation|@
name|XmlAttribute
argument_list|(
name|required
operator|=
literal|false
argument_list|)
DECL|field|autowireRouteBuilders
specifier|private
name|Boolean
name|autowireRouteBuilders
init|=
name|Boolean
operator|.
name|TRUE
decl_stmt|;
annotation|@
name|XmlAttribute
argument_list|(
name|required
operator|=
literal|false
argument_list|)
DECL|field|trace
specifier|private
name|Boolean
name|trace
decl_stmt|;
annotation|@
name|XmlElement
argument_list|(
name|name
operator|=
literal|"package"
argument_list|,
name|required
operator|=
literal|false
argument_list|)
DECL|field|packages
specifier|private
name|String
index|[]
name|packages
init|=
block|{}
decl_stmt|;
annotation|@
name|XmlElement
argument_list|(
name|name
operator|=
literal|"jmxAgent"
argument_list|,
name|type
operator|=
name|CamelJMXAgentType
operator|.
name|class
argument_list|,
name|required
operator|=
literal|false
argument_list|)
DECL|field|camelJMXAgent
specifier|private
name|CamelJMXAgentType
name|camelJMXAgent
decl_stmt|;
annotation|@
name|XmlElements
argument_list|(
block|{
annotation|@
name|XmlElement
argument_list|(
name|name
operator|=
literal|"beanPostProcessor"
argument_list|,
name|type
operator|=
name|CamelBeanPostProcessor
operator|.
name|class
argument_list|,
name|required
operator|=
literal|false
argument_list|)
block|,
annotation|@
name|XmlElement
argument_list|(
name|name
operator|=
literal|"template"
argument_list|,
name|type
operator|=
name|CamelTemplateFactoryBean
operator|.
name|class
argument_list|,
name|required
operator|=
literal|false
argument_list|)
block|,
annotation|@
name|XmlElement
argument_list|(
name|name
operator|=
literal|"proxy"
argument_list|,
name|type
operator|=
name|CamelProxyFactoryType
operator|.
name|class
argument_list|,
name|required
operator|=
literal|false
argument_list|)
block|,
annotation|@
name|XmlElement
argument_list|(
name|name
operator|=
literal|"export"
argument_list|,
name|type
operator|=
name|CamelServiceExporterType
operator|.
name|class
argument_list|,
name|required
operator|=
literal|false
argument_list|)
block|}
argument_list|)
DECL|field|beans
specifier|private
name|List
name|beans
decl_stmt|;
annotation|@
name|XmlElement
argument_list|(
name|name
operator|=
literal|"routeBuilderRef"
argument_list|,
name|required
operator|=
literal|false
argument_list|)
DECL|field|builderRefs
specifier|private
name|List
argument_list|<
name|RouteBuilderRef
argument_list|>
name|builderRefs
init|=
operator|new
name|ArrayList
argument_list|<
name|RouteBuilderRef
argument_list|>
argument_list|()
decl_stmt|;
annotation|@
name|XmlElement
argument_list|(
name|name
operator|=
literal|"endpoint"
argument_list|,
name|required
operator|=
literal|false
argument_list|)
DECL|field|endpoints
specifier|private
name|List
argument_list|<
name|EndpointFactoryBean
argument_list|>
name|endpoints
decl_stmt|;
annotation|@
name|XmlElementRef
DECL|field|dataFormats
specifier|private
name|List
argument_list|<
name|DataFormatType
argument_list|>
name|dataFormats
decl_stmt|;
annotation|@
name|XmlElement
argument_list|(
name|name
operator|=
literal|"route"
argument_list|,
name|required
operator|=
literal|false
argument_list|)
DECL|field|routes
specifier|private
name|List
argument_list|<
name|RouteType
argument_list|>
name|routes
init|=
operator|new
name|ArrayList
argument_list|<
name|RouteType
argument_list|>
argument_list|()
decl_stmt|;
annotation|@
name|XmlTransient
DECL|field|context
specifier|private
name|SpringCamelContext
name|context
decl_stmt|;
annotation|@
name|XmlTransient
DECL|field|routeBuilder
specifier|private
name|RouteBuilder
name|routeBuilder
decl_stmt|;
annotation|@
name|XmlTransient
DECL|field|additionalBuilders
specifier|private
name|List
argument_list|<
name|RouteBuilder
argument_list|>
name|additionalBuilders
init|=
operator|new
name|ArrayList
argument_list|<
name|RouteBuilder
argument_list|>
argument_list|()
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
DECL|field|contextClassLoaderOnStart
specifier|private
name|ClassLoader
name|contextClassLoaderOnStart
decl_stmt|;
annotation|@
name|XmlTransient
DECL|field|beanPostProcessor
specifier|private
name|BeanPostProcessor
name|beanPostProcessor
decl_stmt|;
DECL|method|CamelContextFactoryBean ()
specifier|public
name|CamelContextFactoryBean
parameter_list|()
block|{
comment|// Lets keep track of the class loader for when we actually do start things up
name|contextClassLoaderOnStart
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
DECL|method|getObject ()
specifier|public
name|Object
name|getObject
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|getContext
argument_list|()
return|;
block|}
DECL|method|getObjectType ()
specifier|public
name|Class
name|getObjectType
parameter_list|()
block|{
return|return
name|SpringCamelContext
operator|.
name|class
return|;
block|}
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
literal|true
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
comment|// lets see if we can find a debugger to add
comment|// TODO there should be a neater way to do this!
name|Debugger
name|debugger
init|=
name|getBeanForType
argument_list|(
name|Debugger
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|debugger
operator|!=
literal|null
condition|)
block|{
name|getContext
argument_list|()
operator|.
name|addInterceptStrategy
argument_list|(
name|debugger
argument_list|)
expr_stmt|;
block|}
name|Tracer
name|tracer
init|=
name|getBeanForType
argument_list|(
name|Tracer
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|tracer
operator|!=
literal|null
condition|)
block|{
name|getContext
argument_list|()
operator|.
name|addInterceptStrategy
argument_list|(
name|tracer
argument_list|)
expr_stmt|;
block|}
comment|// set the lifecycle strategy if defined
name|LifecycleStrategy
name|lifecycleStrategy
init|=
name|getBeanForType
argument_list|(
name|LifecycleStrategy
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|lifecycleStrategy
operator|!=
literal|null
condition|)
block|{
name|getContext
argument_list|()
operator|.
name|setLifecycleStrategy
argument_list|(
name|lifecycleStrategy
argument_list|)
expr_stmt|;
block|}
comment|// set the strategy if defined
name|Registry
name|registry
init|=
name|getBeanForType
argument_list|(
name|Registry
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|registry
operator|!=
literal|null
condition|)
block|{
name|getContext
argument_list|()
operator|.
name|setRegistry
argument_list|(
name|registry
argument_list|)
expr_stmt|;
block|}
comment|// Set the application context and camelContext for the beanPostProcessor
if|if
condition|(
name|beanPostProcessor
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|beanPostProcessor
operator|instanceof
name|ApplicationContextAware
condition|)
block|{
operator|(
operator|(
name|ApplicationContextAware
operator|)
name|beanPostProcessor
operator|)
operator|.
name|setApplicationContext
argument_list|(
name|applicationContext
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|beanPostProcessor
operator|instanceof
name|CamelBeanPostProcessor
condition|)
block|{
operator|(
operator|(
name|CamelBeanPostProcessor
operator|)
name|beanPostProcessor
operator|)
operator|.
name|setCamelContext
argument_list|(
name|getContext
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|// lets force any lazy creation
name|getContext
argument_list|()
operator|.
name|addRouteDefinitions
argument_list|(
name|routes
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|isJmxEnabled
argument_list|()
operator|||
operator|(
name|camelJMXAgent
operator|!=
literal|null
operator|&&
name|camelJMXAgent
operator|.
name|isDisabled
argument_list|()
operator|!=
literal|null
operator|&&
name|camelJMXAgent
operator|.
name|isDisabled
argument_list|()
operator|)
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"JMXAgent disabled"
argument_list|)
expr_stmt|;
name|getContext
argument_list|()
operator|.
name|setLifecycleStrategy
argument_list|(
operator|new
name|DefaultLifecycleStrategy
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|camelJMXAgent
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"JMXAgent enabled"
argument_list|)
expr_stmt|;
if|if
condition|(
name|lifecycleStrategy
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"lifecycleStrategy will be overriden by InstrumentationLifecycleStrategy"
argument_list|)
expr_stmt|;
block|}
name|DefaultInstrumentationAgent
name|agent
init|=
operator|new
name|DefaultInstrumentationAgent
argument_list|()
decl_stmt|;
name|agent
operator|.
name|setConnectorPort
argument_list|(
name|camelJMXAgent
operator|.
name|getConnectorPort
argument_list|()
argument_list|)
expr_stmt|;
name|agent
operator|.
name|setCreateConnector
argument_list|(
name|camelJMXAgent
operator|.
name|isCreateConnector
argument_list|()
argument_list|)
expr_stmt|;
name|agent
operator|.
name|setMBeanObjectDomainName
argument_list|(
name|camelJMXAgent
operator|.
name|getMbeanObjectDomainName
argument_list|()
argument_list|)
expr_stmt|;
name|agent
operator|.
name|setMBeanServerDefaultDomain
argument_list|(
name|camelJMXAgent
operator|.
name|getMbeanServerDefaultDomain
argument_list|()
argument_list|)
expr_stmt|;
name|agent
operator|.
name|setRegistryPort
argument_list|(
name|camelJMXAgent
operator|.
name|getRegistryPort
argument_list|()
argument_list|)
expr_stmt|;
name|agent
operator|.
name|setServiceUrlPath
argument_list|(
name|camelJMXAgent
operator|.
name|getServiceUrlPath
argument_list|()
argument_list|)
expr_stmt|;
name|agent
operator|.
name|setUsePlatformMBeanServer
argument_list|(
name|camelJMXAgent
operator|.
name|isUsePlatformMBeanServer
argument_list|()
argument_list|)
expr_stmt|;
name|getContext
argument_list|()
operator|.
name|setLifecycleStrategy
argument_list|(
operator|new
name|InstrumentationLifecycleStrategy
argument_list|(
name|agent
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Found JAXB created routes: "
operator|+
name|getRoutes
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|findRouteBuiders
argument_list|()
expr_stmt|;
name|installRoutes
argument_list|()
expr_stmt|;
block|}
DECL|method|getBeanForType (Class<T> clazz)
specifier|private
parameter_list|<
name|T
parameter_list|>
name|T
name|getBeanForType
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|clazz
parameter_list|)
block|{
name|T
name|bean
init|=
literal|null
decl_stmt|;
name|String
index|[]
name|names
init|=
name|getApplicationContext
argument_list|()
operator|.
name|getBeanNamesForType
argument_list|(
name|clazz
argument_list|,
literal|true
argument_list|,
literal|true
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
name|bean
operator|=
operator|(
name|T
operator|)
name|getApplicationContext
argument_list|()
operator|.
name|getBean
argument_list|(
name|names
index|[
literal|0
index|]
argument_list|,
name|clazz
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|bean
operator|==
literal|null
condition|)
block|{
name|ApplicationContext
name|parentContext
init|=
name|getApplicationContext
argument_list|()
operator|.
name|getParent
argument_list|()
decl_stmt|;
if|if
condition|(
name|parentContext
operator|!=
literal|null
condition|)
block|{
name|names
operator|=
name|parentContext
operator|.
name|getBeanNamesForType
argument_list|(
name|clazz
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|)
expr_stmt|;
if|if
condition|(
name|names
operator|.
name|length
operator|==
literal|1
condition|)
block|{
name|bean
operator|=
operator|(
name|T
operator|)
name|parentContext
operator|.
name|getBean
argument_list|(
name|names
index|[
literal|0
index|]
argument_list|,
name|clazz
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|bean
return|;
block|}
DECL|method|destroy ()
specifier|public
name|void
name|destroy
parameter_list|()
throws|throws
name|Exception
block|{
name|getContext
argument_list|()
operator|.
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
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Publishing event: "
operator|+
name|event
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|event
operator|instanceof
name|ContextRefreshedEvent
condition|)
block|{
comment|// now lets start the CamelContext so that all its possible
comment|// dependencies are initailized
try|try
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Starting the context now!"
argument_list|)
expr_stmt|;
name|getContext
argument_list|()
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
operator|new
name|RuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
comment|/*          * if (context != null) { context.onApplicationEvent(event); }          */
block|}
comment|// Properties
comment|// -------------------------------------------------------------------------
DECL|method|getContext ()
specifier|public
name|SpringCamelContext
name|getContext
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|context
operator|==
literal|null
condition|)
block|{
name|context
operator|=
name|createContext
argument_list|()
expr_stmt|;
block|}
return|return
name|context
return|;
block|}
DECL|method|setContext (SpringCamelContext context)
specifier|public
name|void
name|setContext
parameter_list|(
name|SpringCamelContext
name|context
parameter_list|)
block|{
name|this
operator|.
name|context
operator|=
name|context
expr_stmt|;
block|}
DECL|method|getRoutes ()
specifier|public
name|List
argument_list|<
name|RouteType
argument_list|>
name|getRoutes
parameter_list|()
block|{
return|return
name|routes
return|;
block|}
DECL|method|setRoutes (List<RouteType> routes)
specifier|public
name|void
name|setRoutes
parameter_list|(
name|List
argument_list|<
name|RouteType
argument_list|>
name|routes
parameter_list|)
block|{
name|this
operator|.
name|routes
operator|=
name|routes
expr_stmt|;
block|}
DECL|method|getRouteBuilder ()
specifier|public
name|RouteBuilder
name|getRouteBuilder
parameter_list|()
block|{
return|return
name|routeBuilder
return|;
block|}
comment|/**      * Set a single {@link RouteBuilder} to be used to create the default routes      * on startup      */
DECL|method|setRouteBuilder (RouteBuilder routeBuilder)
specifier|public
name|void
name|setRouteBuilder
parameter_list|(
name|RouteBuilder
name|routeBuilder
parameter_list|)
block|{
name|this
operator|.
name|routeBuilder
operator|=
name|routeBuilder
expr_stmt|;
block|}
comment|/**      * Set a collection of {@link RouteBuilder} instances to be used to create      * the default routes on startup      */
DECL|method|setRouteBuilders (RouteBuilder[] builders)
specifier|public
name|void
name|setRouteBuilders
parameter_list|(
name|RouteBuilder
index|[]
name|builders
parameter_list|)
block|{
for|for
control|(
name|RouteBuilder
name|builder
range|:
name|builders
control|)
block|{
name|additionalBuilders
operator|.
name|add
argument_list|(
name|builder
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|getApplicationContext ()
specifier|public
name|ApplicationContext
name|getApplicationContext
parameter_list|()
block|{
if|if
condition|(
name|applicationContext
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"No applicationContext has been injected!"
argument_list|)
throw|;
block|}
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
block|{
name|this
operator|.
name|applicationContext
operator|=
name|applicationContext
expr_stmt|;
block|}
DECL|method|getPackages ()
specifier|public
name|String
index|[]
name|getPackages
parameter_list|()
block|{
return|return
name|packages
return|;
block|}
comment|/**      * Sets the package names to be recursively searched for Java classes which      * extend {@link RouteBuilder} to be auto-wired up to the      * {@link SpringCamelContext} as a route. Note that classes are excluded if      * they are specifically configured in the spring.xml      *      * @param packages the package names which are recursively searched      */
DECL|method|setPackages (String[] packages)
specifier|public
name|void
name|setPackages
parameter_list|(
name|String
index|[]
name|packages
parameter_list|)
block|{
name|this
operator|.
name|packages
operator|=
name|packages
expr_stmt|;
block|}
DECL|method|setBeanPostProcessor (BeanPostProcessor postProcessor)
specifier|public
name|void
name|setBeanPostProcessor
parameter_list|(
name|BeanPostProcessor
name|postProcessor
parameter_list|)
block|{
name|this
operator|.
name|beanPostProcessor
operator|=
name|postProcessor
expr_stmt|;
block|}
DECL|method|getBeanPostProcessor ()
specifier|public
name|BeanPostProcessor
name|getBeanPostProcessor
parameter_list|()
block|{
return|return
name|beanPostProcessor
return|;
block|}
comment|/**      * This method merely retrieves the value of the "useJmx" attribute and does       * not consider the "disabled" flag in jmxAgent element.  The useJmx       * attribute will be removed in 2.0.  Please the jmxAgent element instead.      *       * @deprecated Please the jmxAgent element instead. Will be removed in Camel 2.0.      */
DECL|method|isJmxEnabled ()
specifier|public
name|boolean
name|isJmxEnabled
parameter_list|()
block|{
return|return
name|useJmx
operator|.
name|booleanValue
argument_list|()
return|;
block|}
DECL|method|getUseJmx ()
specifier|public
name|Boolean
name|getUseJmx
parameter_list|()
block|{
return|return
name|useJmx
return|;
block|}
comment|/**      * @deprecated Please the jmxAgent element instead. Will be removed in Camel 2.0.      */
DECL|method|setUseJmx (Boolean useJmx)
specifier|public
name|void
name|setUseJmx
parameter_list|(
name|Boolean
name|useJmx
parameter_list|)
block|{
name|this
operator|.
name|useJmx
operator|=
name|useJmx
expr_stmt|;
block|}
DECL|method|setCamelJMXAgent (CamelJMXAgentType agent)
specifier|public
name|void
name|setCamelJMXAgent
parameter_list|(
name|CamelJMXAgentType
name|agent
parameter_list|)
block|{
name|camelJMXAgent
operator|=
name|agent
expr_stmt|;
block|}
DECL|method|getTrace ()
specifier|public
name|Boolean
name|getTrace
parameter_list|()
block|{
return|return
name|trace
return|;
block|}
DECL|method|setTrace (Boolean trace)
specifier|public
name|void
name|setTrace
parameter_list|(
name|Boolean
name|trace
parameter_list|)
block|{
name|this
operator|.
name|trace
operator|=
name|trace
expr_stmt|;
block|}
DECL|method|getCamelJMXAgent ()
specifier|public
name|CamelJMXAgentType
name|getCamelJMXAgent
parameter_list|()
block|{
return|return
name|camelJMXAgent
return|;
block|}
DECL|method|getBuilderRefs ()
specifier|public
name|List
argument_list|<
name|RouteBuilderRef
argument_list|>
name|getBuilderRefs
parameter_list|()
block|{
return|return
name|builderRefs
return|;
block|}
DECL|method|setBuilderRefs (List<RouteBuilderRef> builderRefs)
specifier|public
name|void
name|setBuilderRefs
parameter_list|(
name|List
argument_list|<
name|RouteBuilderRef
argument_list|>
name|builderRefs
parameter_list|)
block|{
name|this
operator|.
name|builderRefs
operator|=
name|builderRefs
expr_stmt|;
block|}
comment|/**      * If enabled this will force all {@link RouteBuilder} classes configured in the Spring      * {@link ApplicationContext} to be registered automatically with this CamelContext.      */
DECL|method|setAutowireRouteBuilders (Boolean autowireRouteBuilders)
specifier|public
name|void
name|setAutowireRouteBuilders
parameter_list|(
name|Boolean
name|autowireRouteBuilders
parameter_list|)
block|{
name|this
operator|.
name|autowireRouteBuilders
operator|=
name|autowireRouteBuilders
expr_stmt|;
block|}
comment|// Implementation methods
comment|// -------------------------------------------------------------------------
comment|/**      * Create the context      */
DECL|method|createContext ()
specifier|protected
name|SpringCamelContext
name|createContext
parameter_list|()
block|{
name|SpringCamelContext
name|ctx
init|=
operator|new
name|SpringCamelContext
argument_list|(
name|getApplicationContext
argument_list|()
argument_list|)
decl_stmt|;
name|ctx
operator|.
name|setName
argument_list|(
name|getId
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|trace
operator|!=
literal|null
condition|)
block|{
name|ctx
operator|.
name|setTrace
argument_list|(
name|trace
argument_list|)
expr_stmt|;
block|}
return|return
name|ctx
return|;
block|}
comment|/**      * Strategy to install all available routes into the context      */
DECL|method|installRoutes ()
specifier|protected
name|void
name|installRoutes
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|autowireRouteBuilders
operator|!=
literal|null
operator|&&
name|autowireRouteBuilders
operator|.
name|booleanValue
argument_list|()
condition|)
block|{
name|Map
name|builders
init|=
name|getApplicationContext
argument_list|()
operator|.
name|getBeansOfType
argument_list|(
name|RouteBuilder
operator|.
name|class
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|)
decl_stmt|;
if|if
condition|(
name|builders
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Object
name|builder
range|:
name|builders
operator|.
name|values
argument_list|()
control|)
block|{
name|getContext
argument_list|()
operator|.
name|addRoutes
argument_list|(
operator|(
name|RouteBuilder
operator|)
name|builder
argument_list|)
expr_stmt|;
block|}
block|}
block|}
for|for
control|(
name|RouteBuilder
name|routeBuilder
range|:
name|additionalBuilders
control|)
block|{
name|getContext
argument_list|()
operator|.
name|addRoutes
argument_list|(
name|routeBuilder
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|routeBuilder
operator|!=
literal|null
condition|)
block|{
name|getContext
argument_list|()
operator|.
name|addRoutes
argument_list|(
name|routeBuilder
argument_list|)
expr_stmt|;
block|}
comment|// lets add route builders added from references
if|if
condition|(
name|builderRefs
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|RouteBuilderRef
name|builderRef
range|:
name|builderRefs
control|)
block|{
name|RouteBuilder
name|builder
init|=
name|builderRef
operator|.
name|createRouteBuilder
argument_list|(
name|getContext
argument_list|()
argument_list|)
decl_stmt|;
name|getContext
argument_list|()
operator|.
name|addRoutes
argument_list|(
name|builder
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Strategy method to try find {@link RouteBuilder} instances on the      * classpath      */
DECL|method|findRouteBuiders ()
specifier|protected
name|void
name|findRouteBuiders
parameter_list|()
throws|throws
name|Exception
throws|,
name|InstantiationException
block|{
if|if
condition|(
name|packages
operator|!=
literal|null
operator|&&
name|packages
operator|.
name|length
operator|>
literal|0
condition|)
block|{
name|RouteBuilderFinder
name|finder
init|=
operator|new
name|RouteBuilderFinder
argument_list|(
name|getContext
argument_list|()
argument_list|,
name|packages
argument_list|,
name|contextClassLoaderOnStart
argument_list|,
name|getBeanPostProcessor
argument_list|()
argument_list|)
decl_stmt|;
name|finder
operator|.
name|appendBuilders
argument_list|(
name|additionalBuilders
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

