begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.blueprint
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|blueprint
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
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
name|Properties
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
name|TypeConverter
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
name|osgi
operator|.
name|OsgiCamelContextHelper
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
name|osgi
operator|.
name|OsgiFactoryFinderResolver
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
name|osgi
operator|.
name|OsgiTypeConverter
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
name|osgi
operator|.
name|utils
operator|.
name|BundleContextUtils
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
name|osgi
operator|.
name|utils
operator|.
name|BundleDelegatingClassLoader
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
name|spi
operator|.
name|FactoryFinder
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
name|util
operator|.
name|LoadPropertiesException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|BundleContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|ServiceEvent
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|ServiceListener
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|ServiceRegistration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|service
operator|.
name|blueprint
operator|.
name|container
operator|.
name|BlueprintContainer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|service
operator|.
name|blueprint
operator|.
name|container
operator|.
name|BlueprintEvent
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|service
operator|.
name|blueprint
operator|.
name|container
operator|.
name|BlueprintListener
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

begin_class
DECL|class|BlueprintCamelContext
specifier|public
class|class
name|BlueprintCamelContext
extends|extends
name|DefaultCamelContext
implements|implements
name|ServiceListener
implements|,
name|BlueprintListener
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
name|BlueprintCamelContext
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|bundleContext
specifier|private
name|BundleContext
name|bundleContext
decl_stmt|;
DECL|field|blueprintContainer
specifier|private
name|BlueprintContainer
name|blueprintContainer
decl_stmt|;
DECL|field|registration
specifier|private
name|ServiceRegistration
argument_list|<
name|?
argument_list|>
name|registration
decl_stmt|;
DECL|method|BlueprintCamelContext ()
specifier|public
name|BlueprintCamelContext
parameter_list|()
block|{     }
DECL|method|BlueprintCamelContext (BundleContext bundleContext, BlueprintContainer blueprintContainer)
specifier|public
name|BlueprintCamelContext
parameter_list|(
name|BundleContext
name|bundleContext
parameter_list|,
name|BlueprintContainer
name|blueprintContainer
parameter_list|)
block|{
name|this
operator|.
name|bundleContext
operator|=
name|bundleContext
expr_stmt|;
name|this
operator|.
name|blueprintContainer
operator|=
name|blueprintContainer
expr_stmt|;
comment|// inject common osgi
name|OsgiCamelContextHelper
operator|.
name|osgiUpdate
argument_list|(
name|this
argument_list|,
name|bundleContext
argument_list|)
expr_stmt|;
comment|// and these are blueprint specific
name|setComponentResolver
argument_list|(
operator|new
name|BlueprintComponentResolver
argument_list|(
name|bundleContext
argument_list|)
argument_list|)
expr_stmt|;
name|setLanguageResolver
argument_list|(
operator|new
name|BlueprintLanguageResolver
argument_list|(
name|bundleContext
argument_list|)
argument_list|)
expr_stmt|;
name|setDataFormatResolver
argument_list|(
operator|new
name|BlueprintDataFormatResolver
argument_list|(
name|bundleContext
argument_list|)
argument_list|)
expr_stmt|;
name|setApplicationContextClassLoader
argument_list|(
operator|new
name|BundleDelegatingClassLoader
argument_list|(
name|bundleContext
operator|.
name|getBundle
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|getBundleContext ()
specifier|public
name|BundleContext
name|getBundleContext
parameter_list|()
block|{
return|return
name|bundleContext
return|;
block|}
DECL|method|setBundleContext (BundleContext bundleContext)
specifier|public
name|void
name|setBundleContext
parameter_list|(
name|BundleContext
name|bundleContext
parameter_list|)
block|{
name|this
operator|.
name|bundleContext
operator|=
name|bundleContext
expr_stmt|;
block|}
DECL|method|getBlueprintContainer ()
specifier|public
name|BlueprintContainer
name|getBlueprintContainer
parameter_list|()
block|{
return|return
name|blueprintContainer
return|;
block|}
DECL|method|setBlueprintContainer (BlueprintContainer blueprintContainer)
specifier|public
name|void
name|setBlueprintContainer
parameter_list|(
name|BlueprintContainer
name|blueprintContainer
parameter_list|)
block|{
name|this
operator|.
name|blueprintContainer
operator|=
name|blueprintContainer
expr_stmt|;
block|}
DECL|method|init ()
specifier|public
name|void
name|init
parameter_list|()
throws|throws
name|Exception
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"init {}"
argument_list|,
name|this
argument_list|)
expr_stmt|;
comment|// add service listener so we can be notified when blueprint container is done
comment|// and we would be ready to start CamelContext
name|bundleContext
operator|.
name|addServiceListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
comment|// add blueprint listener as service, as we need this for the blueprint container
comment|// to support change events when it changes states
name|registration
operator|=
name|bundleContext
operator|.
name|registerService
argument_list|(
name|BlueprintListener
operator|.
name|class
argument_list|,
name|this
argument_list|,
literal|null
argument_list|)
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
name|LOG
operator|.
name|trace
argument_list|(
literal|"destroy {}"
argument_list|,
name|this
argument_list|)
expr_stmt|;
comment|// remove listener and stop this CamelContext
try|try
block|{
name|bundleContext
operator|.
name|removeServiceListener
argument_list|(
name|this
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
literal|"Error removing ServiceListener "
operator|+
name|this
operator|+
literal|". This exception is ignored."
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|registration
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|registration
operator|.
name|unregister
argument_list|()
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
literal|"Error unregistering service registration "
operator|+
name|registration
operator|+
literal|". This exception is ignored."
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
name|registration
operator|=
literal|null
expr_stmt|;
block|}
comment|// must stop Camel
name|stop
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|findComponents ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Properties
argument_list|>
name|findComponents
parameter_list|()
throws|throws
name|LoadPropertiesException
throws|,
name|IOException
block|{
return|return
name|BundleContextUtils
operator|.
name|findComponents
argument_list|(
name|bundleContext
argument_list|,
name|this
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getComponentDocumentation (String componentName)
specifier|public
name|String
name|getComponentDocumentation
parameter_list|(
name|String
name|componentName
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|BundleContextUtils
operator|.
name|getComponentDocumentation
argument_list|(
name|bundleContext
argument_list|,
name|this
argument_list|,
name|componentName
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|blueprintEvent (BlueprintEvent event)
specifier|public
name|void
name|blueprintEvent
parameter_list|(
name|BlueprintEvent
name|event
parameter_list|)
block|{
comment|// noop as we just needed to enlist the BlueprintListener to have events triggered to serviceChanged method
block|}
annotation|@
name|Override
DECL|method|serviceChanged (ServiceEvent event)
specifier|public
name|void
name|serviceChanged
parameter_list|(
name|ServiceEvent
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
literal|"Service {} changed to {}"
argument_list|,
name|event
argument_list|,
name|event
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// look for blueprint container to be registered, and then we can start the CamelContext
if|if
condition|(
name|event
operator|.
name|getType
argument_list|()
operator|==
name|ServiceEvent
operator|.
name|REGISTERED
operator|&&
name|event
operator|.
name|getServiceReference
argument_list|()
operator|.
name|isAssignableTo
argument_list|(
name|bundleContext
operator|.
name|getBundle
argument_list|()
argument_list|,
literal|"org.osgi.service.blueprint.container.BlueprintContainer"
argument_list|)
operator|&&
name|bundleContext
operator|.
name|getBundle
argument_list|()
operator|.
name|equals
argument_list|(
name|event
operator|.
name|getServiceReference
argument_list|()
operator|.
name|getBundle
argument_list|()
argument_list|)
condition|)
block|{
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
name|LOG
operator|.
name|error
argument_list|(
literal|"Error occurred during starting Camel: "
operator|+
name|this
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
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Override
DECL|method|createTypeConverter ()
specifier|protected
name|TypeConverter
name|createTypeConverter
parameter_list|()
block|{
comment|// CAMEL-3614: make sure we use a bundle context which imports org.apache.camel.impl.converter package
name|BundleContext
name|ctx
init|=
name|BundleContextUtils
operator|.
name|getBundleContext
argument_list|(
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|ctx
operator|==
literal|null
condition|)
block|{
name|ctx
operator|=
name|bundleContext
expr_stmt|;
block|}
name|FactoryFinder
name|finder
init|=
operator|new
name|OsgiFactoryFinderResolver
argument_list|(
name|bundleContext
argument_list|)
operator|.
name|resolveDefaultFactoryFinder
argument_list|(
name|getClassResolver
argument_list|()
argument_list|)
decl_stmt|;
return|return
operator|new
name|OsgiTypeConverter
argument_list|(
name|ctx
argument_list|,
name|getInjector
argument_list|()
argument_list|,
name|finder
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
name|Registry
name|reg
init|=
operator|new
name|BlueprintContainerRegistry
argument_list|(
name|getBlueprintContainer
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|OsgiCamelContextHelper
operator|.
name|wrapRegistry
argument_list|(
name|this
argument_list|,
name|reg
argument_list|,
name|bundleContext
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|start ()
specifier|public
name|void
name|start
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|ClassLoader
name|original
init|=
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
decl_stmt|;
try|try
block|{
comment|// let's set a more suitable TCCL while starting the context
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|setContextClassLoader
argument_list|(
name|getApplicationContextClassLoader
argument_list|()
argument_list|)
expr_stmt|;
name|super
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|setContextClassLoader
argument_list|(
name|original
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|maybeStart ()
specifier|private
name|void
name|maybeStart
parameter_list|()
throws|throws
name|Exception
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"maybeStart: {}"
argument_list|,
name|this
argument_list|)
expr_stmt|;
comment|// for example from unit testing we want to start Camel later and not
comment|// when blueprint loading the bundle
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
name|LOG
operator|.
name|debug
argument_list|(
literal|"Starting {}"
argument_list|,
name|this
argument_list|)
expr_stmt|;
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
literal|"Ignoring maybeStart() as {} is already started"
argument_list|,
name|this
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

