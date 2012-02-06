begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cdi.util
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
name|util
package|;
end_package

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
name|naming
operator|.
name|InitialContext
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|NamingException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|AccessController
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|PrivilegedAction
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
name|concurrent
operator|.
name|ConcurrentHashMap
import|;
end_import

begin_comment
comment|/**  *<p>This class provides access to the {@link BeanManager}  * by registering the current {@link BeanManager} in an extension and  * making it available via a singleton factory for the current application.</p>  *<p>This is really handy if you like to access CDI functionality  * from places where no injection is available.</p>  *<p>If a simple but manual bean-lookup is needed, it's easier to use the {@link BeanProvider}.</p>  *<p/>  *<p>As soon as an application shuts down, the reference to the {@link BeanManager} will be removed.<p>  *<p/>  *<p>Usage:<p/>  *<pre>  * BeanManager bm = BeanManagerProvider.getInstance().getBeanManager();  *</pre>  */
end_comment

begin_class
DECL|class|BeanManagerProvider
specifier|public
class|class
name|BeanManagerProvider
implements|implements
name|Extension
block|{
DECL|field|bmp
specifier|private
specifier|static
name|BeanManagerProvider
name|bmp
init|=
literal|null
decl_stmt|;
DECL|field|bms
specifier|private
specifier|volatile
name|Map
argument_list|<
name|ClassLoader
argument_list|,
name|BeanManager
argument_list|>
name|bms
init|=
operator|new
name|ConcurrentHashMap
argument_list|<
name|ClassLoader
argument_list|,
name|BeanManager
argument_list|>
argument_list|()
decl_stmt|;
comment|/**      * Returns if the {@link BeanManagerProvider} has been initialized.      * Usually it isn't needed to call this method in application code.      * It's e.g. useful for other frameworks to check if DeltaSpike and the CDI container in general have been started.      *      * @return true if the bean-manager-provider is ready to be used      */
DECL|method|isActive ()
specifier|public
specifier|static
name|boolean
name|isActive
parameter_list|()
block|{
return|return
name|bmp
operator|!=
literal|null
return|;
block|}
comment|/**      * Allows to get the current provider instance which provides access to the current {@link BeanManager}      *      * @return the singleton BeanManagerProvider      * @throws IllegalStateException if the {@link BeanManagerProvider} isn't ready to be used.      *                               That's the case if the environment isn't configured properly and therefore the {@link AfterBeanDiscovery}      *                               hasn't be called before this method gets called.      */
DECL|method|getInstance ()
specifier|public
specifier|static
name|BeanManagerProvider
name|getInstance
parameter_list|()
block|{
if|if
condition|(
name|bmp
operator|==
literal|null
condition|)
block|{
comment|//X TODO Java-EE5 support needs to be discussed
comment|// workaround for some Java-EE5 environments in combination with a special
comment|// StartupBroadcaster for bootstrapping CDI
comment|// CodiStartupBroadcaster.broadcastStartup();
comment|// here bmp might not be null (depends on the broadcasters)
block|}
if|if
condition|(
name|bmp
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"No "
operator|+
name|BeanManagerProvider
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|" in place! "
operator|+
literal|"Please ensure that you configured the CDI implementation of your choice properly. "
operator|+
literal|"If your setup is correct, please clear all caches and compiled artifacts."
argument_list|)
throw|;
block|}
return|return
name|bmp
return|;
block|}
comment|/**      * The active {@link BeanManager} for the current application (/{@link ClassLoader})      *      * @return the current bean-manager      */
DECL|method|getBeanManager ()
specifier|public
name|BeanManager
name|getBeanManager
parameter_list|()
block|{
name|ClassLoader
name|classLoader
init|=
name|getClassLoader
argument_list|()
decl_stmt|;
name|BeanManager
name|result
init|=
name|bms
operator|.
name|get
argument_list|(
name|classLoader
argument_list|)
decl_stmt|;
if|if
condition|(
name|result
operator|==
literal|null
condition|)
block|{
name|result
operator|=
name|resolveBeanManagerViaJndi
argument_list|()
expr_stmt|;
if|if
condition|(
name|result
operator|!=
literal|null
condition|)
block|{
name|bms
operator|.
name|put
argument_list|(
name|classLoader
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|result
return|;
block|}
DECL|method|getClassLoader ()
specifier|public
specifier|static
name|ClassLoader
name|getClassLoader
parameter_list|()
block|{
name|ClassLoader
name|loader
init|=
name|AccessController
operator|.
name|doPrivileged
argument_list|(
operator|new
name|PrivilegedAction
argument_list|<
name|ClassLoader
argument_list|>
argument_list|()
block|{
comment|/**              * {@inheritDoc}              */
annotation|@
name|Override
specifier|public
name|ClassLoader
name|run
parameter_list|()
block|{
try|try
block|{
return|return
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
block|}
block|}
argument_list|)
decl_stmt|;
if|if
condition|(
name|loader
operator|==
literal|null
condition|)
block|{
name|loader
operator|=
name|BeanManagerProvider
operator|.
name|class
operator|.
name|getClassLoader
argument_list|()
expr_stmt|;
block|}
return|return
name|loader
return|;
block|}
comment|/**      * It basically doesn't matter which of the system events we use,      * but basically we use the {@link AfterBeanDiscovery} event since it allows to use the      * {@link BeanManagerProvider} for all events which occur after the {@link AfterBeanDiscovery} event.      *      * @param afterBeanDiscovery event which we don't actually use ;)      * @param beanManager        the BeanManager we store and make available.      */
DECL|method|setBeanManager (@bserves AfterBeanDiscovery afterBeanDiscovery, BeanManager beanManager)
specifier|protected
name|void
name|setBeanManager
parameter_list|(
annotation|@
name|Observes
name|AfterBeanDiscovery
name|afterBeanDiscovery
parameter_list|,
name|BeanManager
name|beanManager
parameter_list|)
block|{
name|BeanManagerProvider
name|bmpFirst
init|=
name|setBeanManagerProvider
argument_list|(
name|this
argument_list|)
decl_stmt|;
name|ClassLoader
name|cl
init|=
name|getClassLoader
argument_list|()
decl_stmt|;
name|bmpFirst
operator|.
name|bms
operator|.
name|put
argument_list|(
name|cl
argument_list|,
name|beanManager
argument_list|)
expr_stmt|;
comment|//X TODO Java-EE5 support needs to be discussed
comment|//CodiStartupBroadcaster.broadcastStartup();
block|}
comment|/**      * Cleanup on container shutdown      *      * @param beforeShutdown cdi shutdown event      */
DECL|method|cleanupStoredBeanManagerOnShutdown (@bserves BeforeShutdown beforeShutdown)
specifier|protected
name|void
name|cleanupStoredBeanManagerOnShutdown
parameter_list|(
annotation|@
name|Observes
name|BeforeShutdown
name|beforeShutdown
parameter_list|)
block|{
name|bms
operator|.
name|remove
argument_list|(
name|getClassLoader
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Get the BeanManager from the JNDI registry.      *<p/>      * Workaround for JBossAS 6 (see EXTCDI-74)      * {@link #setBeanManager(javax.enterprise.inject.spi.AfterBeanDiscovery, javax.enterprise.inject.spi.BeanManager)}      * is called in context of a different {@link ClassLoader}      *      * @return current {@link javax.enterprise.inject.spi.BeanManager} which is provided via JNDI      */
DECL|method|resolveBeanManagerViaJndi ()
specifier|private
name|BeanManager
name|resolveBeanManagerViaJndi
parameter_list|()
block|{
try|try
block|{
return|return
operator|(
name|BeanManager
operator|)
operator|new
name|InitialContext
argument_list|()
operator|.
name|lookup
argument_list|(
literal|"java:comp/BeanManager"
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|NamingException
name|e
parameter_list|)
block|{
comment|//workaround didn't work -> force NPE
return|return
literal|null
return|;
block|}
block|}
comment|/**      * This function exists to prevent findbugs to complain about      * setting a static member from a non-static function.      *      * @param beanManagerProvider the bean-manager-provider which should be used if there isn't an existing provider      * @return the first BeanManagerProvider      */
DECL|method|setBeanManagerProvider (BeanManagerProvider beanManagerProvider)
specifier|private
specifier|static
name|BeanManagerProvider
name|setBeanManagerProvider
parameter_list|(
name|BeanManagerProvider
name|beanManagerProvider
parameter_list|)
block|{
if|if
condition|(
name|bmp
operator|==
literal|null
condition|)
block|{
name|bmp
operator|=
name|beanManagerProvider
expr_stmt|;
block|}
return|return
name|bmp
return|;
block|}
block|}
end_class

end_unit

