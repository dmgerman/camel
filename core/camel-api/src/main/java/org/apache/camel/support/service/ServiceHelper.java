begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.support.service
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|support
operator|.
name|service
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
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
name|List
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Channel
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
name|Navigate
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
name|ShutdownableService
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
name|StatefulService
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
name|Suspendable
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
name|SuspendableService
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

begin_comment
comment|/**  * A collection of helper methods for working with {@link Service} objects.  */
end_comment

begin_class
DECL|class|ServiceHelper
specifier|public
specifier|final
class|class
name|ServiceHelper
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
name|ServiceHelper
operator|.
name|class
argument_list|)
decl_stmt|;
comment|/**      * Utility classes should not have a public constructor.      */
DECL|method|ServiceHelper ()
specifier|private
name|ServiceHelper
parameter_list|()
block|{     }
comment|/**      * Starts the given {@code value} if it's a {@link Service} or a collection of it.      *<p/>      * Calling this method has no effect if {@code value} is {@code null}.      */
DECL|method|startService (Object value)
specifier|public
specifier|static
name|void
name|startService
parameter_list|(
name|Object
name|value
parameter_list|)
block|{
if|if
condition|(
name|value
operator|instanceof
name|Service
condition|)
block|{
operator|(
operator|(
name|Service
operator|)
name|value
operator|)
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|value
operator|instanceof
name|Iterable
condition|)
block|{
for|for
control|(
name|Object
name|o
range|:
operator|(
name|Iterable
operator|)
name|value
control|)
block|{
name|startService
argument_list|(
name|o
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Starts each element of the given {@code services} if {@code services} itself is      * not {@code null}, otherwise this method would return immediately.      *       * @see #startService(Object)      */
DECL|method|startService (Object... services)
specifier|public
specifier|static
name|void
name|startService
parameter_list|(
name|Object
modifier|...
name|services
parameter_list|)
block|{
if|if
condition|(
name|services
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Object
name|o
range|:
name|services
control|)
block|{
name|startService
argument_list|(
name|o
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Stops each element of the given {@code services} if {@code services} itself is      * not {@code null}, otherwise this method would return immediately.      *<p/>      * If there's any exception being thrown while stopping the elements one after the      * other this method would rethrow the<b>first</b> such exception being thrown.      *       * @see #stopService(Collection)      */
DECL|method|stopService (Object... services)
specifier|public
specifier|static
name|void
name|stopService
parameter_list|(
name|Object
modifier|...
name|services
parameter_list|)
block|{
if|if
condition|(
name|services
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Object
name|o
range|:
name|services
control|)
block|{
name|stopService
argument_list|(
name|o
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Stops the given {@code value}, rethrowing the first exception caught.      *<p/>      * Calling this method has no effect if {@code value} is {@code null}.      *       * @see Service#stop()      * @see #stopService(Collection)      */
DECL|method|stopService (Object value)
specifier|public
specifier|static
name|void
name|stopService
parameter_list|(
name|Object
name|value
parameter_list|)
block|{
if|if
condition|(
name|value
operator|instanceof
name|Service
condition|)
block|{
operator|(
operator|(
name|Service
operator|)
name|value
operator|)
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|value
operator|instanceof
name|Iterable
condition|)
block|{
for|for
control|(
name|Object
name|o
range|:
operator|(
name|Iterable
operator|)
name|value
control|)
block|{
name|stopService
argument_list|(
name|o
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Stops each element of the given {@code services} if {@code services} itself is      * not {@code null}, otherwise this method would return immediately.      *<p/>      * If there's any exception being thrown while stopping the elements one after the      * other this method would rethrow the<b>first</b> such exception being thrown.      *       * @see #stopService(Object)      */
DECL|method|stopService (Collection<?> services)
specifier|public
specifier|static
name|void
name|stopService
parameter_list|(
name|Collection
argument_list|<
name|?
argument_list|>
name|services
parameter_list|)
block|{
if|if
condition|(
name|services
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|RuntimeException
name|firstException
init|=
literal|null
decl_stmt|;
for|for
control|(
name|Object
name|value
range|:
name|services
control|)
block|{
try|try
block|{
name|stopService
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RuntimeException
name|e
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
literal|"Caught exception stopping service: {}"
argument_list|,
name|value
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|firstException
operator|==
literal|null
condition|)
block|{
name|firstException
operator|=
name|e
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|firstException
operator|!=
literal|null
condition|)
block|{
throw|throw
name|firstException
throw|;
block|}
block|}
comment|/**      * Stops and shutdowns each element of the given {@code services} if {@code services} itself is      * not {@code null}, otherwise this method would return immediately.      *<p/>      * If there's any exception being thrown while stopping/shutting down the elements one after      * the other this method would rethrow the<b>first</b> such exception being thrown.      *       * @see #stopAndShutdownServices(Collection)      */
DECL|method|stopAndShutdownServices (Object... services)
specifier|public
specifier|static
name|void
name|stopAndShutdownServices
parameter_list|(
name|Object
modifier|...
name|services
parameter_list|)
block|{
if|if
condition|(
name|services
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|List
argument_list|<
name|Object
argument_list|>
name|list
init|=
name|Arrays
operator|.
name|asList
argument_list|(
name|services
argument_list|)
decl_stmt|;
name|stopAndShutdownServices
argument_list|(
name|list
argument_list|)
expr_stmt|;
block|}
comment|/**      * Stops and shutdowns the given {@code service}, rethrowing the first exception caught.      *<p/>      * Calling this method has no effect if {@code value} is {@code null}.      *       * @see #stopService(Object)      * @see ShutdownableService#shutdown()      */
DECL|method|stopAndShutdownService (Object value)
specifier|public
specifier|static
name|void
name|stopAndShutdownService
parameter_list|(
name|Object
name|value
parameter_list|)
block|{
name|stopService
argument_list|(
name|value
argument_list|)
expr_stmt|;
comment|// then try to shutdown
if|if
condition|(
name|value
operator|instanceof
name|ShutdownableService
condition|)
block|{
name|ShutdownableService
name|service
init|=
operator|(
name|ShutdownableService
operator|)
name|value
decl_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"Shutting down service {}"
argument_list|,
name|value
argument_list|)
expr_stmt|;
name|service
operator|.
name|shutdown
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * Stops and shutdowns each element of the given {@code services} if {@code services}      * itself is not {@code null}, otherwise this method would return immediately.      *<p/>      * If there's any exception being thrown while stopping/shutting down the elements one after      * the other this method would rethrow the<b>first</b> such exception being thrown.      *       * @see #stopService(Object)      * @see ShutdownableService#shutdown()      */
DECL|method|stopAndShutdownServices (Collection<?> services)
specifier|public
specifier|static
name|void
name|stopAndShutdownServices
parameter_list|(
name|Collection
argument_list|<
name|?
argument_list|>
name|services
parameter_list|)
block|{
if|if
condition|(
name|services
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|RuntimeException
name|firstException
init|=
literal|null
decl_stmt|;
for|for
control|(
name|Object
name|value
range|:
name|services
control|)
block|{
try|try
block|{
comment|// must stop it first
name|stopService
argument_list|(
name|value
argument_list|)
expr_stmt|;
comment|// then try to shutdown
if|if
condition|(
name|value
operator|instanceof
name|ShutdownableService
condition|)
block|{
name|ShutdownableService
name|service
init|=
operator|(
name|ShutdownableService
operator|)
name|value
decl_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"Shutting down service: {}"
argument_list|,
name|service
argument_list|)
expr_stmt|;
name|service
operator|.
name|shutdown
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|RuntimeException
name|e
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
literal|"Caught exception shutting down service: {}"
argument_list|,
name|value
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|firstException
operator|==
literal|null
condition|)
block|{
name|firstException
operator|=
name|e
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|firstException
operator|!=
literal|null
condition|)
block|{
throw|throw
name|firstException
throw|;
block|}
block|}
comment|/**      * Resumes each element of the given {@code services} if {@code services} itself is      * not {@code null}, otherwise this method would return immediately.      *<p/>      * If there's any exception being thrown while resuming the elements one after the      * other this method would rethrow the<b>first</b> such exception being thrown.      *       * @see #resumeService(Object)      */
DECL|method|resumeServices (Collection<?> services)
specifier|public
specifier|static
name|void
name|resumeServices
parameter_list|(
name|Collection
argument_list|<
name|?
argument_list|>
name|services
parameter_list|)
block|{
if|if
condition|(
name|services
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|RuntimeException
name|firstException
init|=
literal|null
decl_stmt|;
for|for
control|(
name|Object
name|value
range|:
name|services
control|)
block|{
if|if
condition|(
name|value
operator|instanceof
name|Service
condition|)
block|{
name|Service
name|service
init|=
operator|(
name|Service
operator|)
name|value
decl_stmt|;
try|try
block|{
name|resumeService
argument_list|(
name|service
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RuntimeException
name|e
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
literal|"Caught exception resuming service: {}"
argument_list|,
name|service
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|firstException
operator|==
literal|null
condition|)
block|{
name|firstException
operator|=
name|e
expr_stmt|;
block|}
block|}
block|}
block|}
if|if
condition|(
name|firstException
operator|!=
literal|null
condition|)
block|{
throw|throw
name|firstException
throw|;
block|}
block|}
comment|/**      * Resumes the given {@code service}.      *<p/>      * If {@code service} is both {@link org.apache.camel.Suspendable} and {@link org.apache.camel.SuspendableService} then      * its {@link org.apache.camel.SuspendableService#resume()} is called but      *<b>only</b> if {@code service} is already {@link #isSuspended(Object)      * suspended}.      *<p/>      * If {@code service} is<b>not</b> a      * {@link org.apache.camel.Suspendable} and {@link org.apache.camel.SuspendableService} then its      * {@link org.apache.camel.Service#start()} is called.      *<p/>      * Calling this method has no effect if {@code service} is {@code null}.      *       * @param service the service      * @return<tt>true</tt> if either<tt>resume</tt> method or      *         {@link #startService(Object)} was called,<tt>false</tt>      *         otherwise.      * @throws Exception is thrown if error occurred      * @see #startService(Object)      */
DECL|method|resumeService (Object service)
specifier|public
specifier|static
name|boolean
name|resumeService
parameter_list|(
name|Object
name|service
parameter_list|)
block|{
if|if
condition|(
name|service
operator|instanceof
name|Suspendable
operator|&&
name|service
operator|instanceof
name|SuspendableService
condition|)
block|{
name|SuspendableService
name|ss
init|=
operator|(
name|SuspendableService
operator|)
name|service
decl_stmt|;
if|if
condition|(
name|ss
operator|.
name|isSuspended
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Resuming service {}"
argument_list|,
name|service
argument_list|)
expr_stmt|;
name|ss
operator|.
name|resume
argument_list|()
expr_stmt|;
return|return
literal|true
return|;
block|}
else|else
block|{
return|return
literal|false
return|;
block|}
block|}
else|else
block|{
name|startService
argument_list|(
name|service
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
block|}
comment|/**      * Suspends each element of the given {@code services} if {@code services} itself is      * not {@code null}, otherwise this method would return immediately.      *<p/>      * If there's any exception being thrown while suspending the elements one after the      * other this method would rethrow the<b>first</b> such exception being thrown.      *       * @see #suspendService(Object)      */
DECL|method|suspendServices (Collection<?> services)
specifier|public
specifier|static
name|void
name|suspendServices
parameter_list|(
name|Collection
argument_list|<
name|?
argument_list|>
name|services
parameter_list|)
block|{
if|if
condition|(
name|services
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|RuntimeException
name|firstException
init|=
literal|null
decl_stmt|;
for|for
control|(
name|Object
name|value
range|:
name|services
control|)
block|{
if|if
condition|(
name|value
operator|instanceof
name|Service
condition|)
block|{
name|Service
name|service
init|=
operator|(
name|Service
operator|)
name|value
decl_stmt|;
try|try
block|{
name|suspendService
argument_list|(
name|service
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RuntimeException
name|e
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
literal|"Caught exception suspending service: {}"
argument_list|,
name|service
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|firstException
operator|==
literal|null
condition|)
block|{
name|firstException
operator|=
name|e
expr_stmt|;
block|}
block|}
block|}
block|}
if|if
condition|(
name|firstException
operator|!=
literal|null
condition|)
block|{
throw|throw
name|firstException
throw|;
block|}
block|}
comment|/**      * Suspends the given {@code service}.      *<p/>      * If {@code service} is both {@link org.apache.camel.Suspendable} and {@link org.apache.camel.SuspendableService} then      * its {@link org.apache.camel.SuspendableService#suspend()} is called but      *<b>only</b> if {@code service} is<b>not</b> already      * {@link #isSuspended(Object) suspended}.      *<p/>      * If {@code service} is<b>not</b> a      * {@link org.apache.camel.Suspendable} and {@link org.apache.camel.SuspendableService} then its      * {@link org.apache.camel.Service#stop()} is called.      *<p/>      * Calling this method has no effect if {@code service} is {@code null}.      *       * @param service the service      * @return<tt>true</tt> if either the<tt>suspend</tt> method or      *         {@link #stopService(Object)} was called,<tt>false</tt>      *         otherwise.      * @throws Exception is thrown if error occurred      * @see #stopService(Object)      */
DECL|method|suspendService (Object service)
specifier|public
specifier|static
name|boolean
name|suspendService
parameter_list|(
name|Object
name|service
parameter_list|)
block|{
if|if
condition|(
name|service
operator|instanceof
name|Suspendable
operator|&&
name|service
operator|instanceof
name|SuspendableService
condition|)
block|{
name|SuspendableService
name|ss
init|=
operator|(
name|SuspendableService
operator|)
name|service
decl_stmt|;
if|if
condition|(
operator|!
name|ss
operator|.
name|isSuspended
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Suspending service {}"
argument_list|,
name|service
argument_list|)
expr_stmt|;
name|ss
operator|.
name|suspend
argument_list|()
expr_stmt|;
return|return
literal|true
return|;
block|}
else|else
block|{
return|return
literal|false
return|;
block|}
block|}
else|else
block|{
name|stopService
argument_list|(
name|service
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
block|}
comment|/**      * Is the given service stopping or already stopped?      *      * @return<tt>true</tt> if stopping or already stopped,<tt>false</tt> otherwise      * @see StatefulService#isStopping()      * @see StatefulService#isStopped()      */
DECL|method|isStopped (Object value)
specifier|public
specifier|static
name|boolean
name|isStopped
parameter_list|(
name|Object
name|value
parameter_list|)
block|{
if|if
condition|(
name|value
operator|instanceof
name|StatefulService
condition|)
block|{
name|StatefulService
name|service
init|=
operator|(
name|StatefulService
operator|)
name|value
decl_stmt|;
if|if
condition|(
name|service
operator|.
name|isStopping
argument_list|()
operator|||
name|service
operator|.
name|isStopped
argument_list|()
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
comment|/**      * Is the given service starting or already started?      *      * @return<tt>true</tt> if starting or already started,<tt>false</tt> otherwise      * @see StatefulService#isStarting()      * @see StatefulService#isStarted()      */
DECL|method|isStarted (Object value)
specifier|public
specifier|static
name|boolean
name|isStarted
parameter_list|(
name|Object
name|value
parameter_list|)
block|{
if|if
condition|(
name|value
operator|instanceof
name|StatefulService
condition|)
block|{
name|StatefulService
name|service
init|=
operator|(
name|StatefulService
operator|)
name|value
decl_stmt|;
if|if
condition|(
name|service
operator|.
name|isStarting
argument_list|()
operator|||
name|service
operator|.
name|isStarted
argument_list|()
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
comment|/**      * Is the given service suspending or already suspended?      *      * @return<tt>true</tt> if suspending or already suspended,<tt>false</tt> otherwise      * @see StatefulService#isSuspending()      * @see StatefulService#isSuspended()      */
DECL|method|isSuspended (Object value)
specifier|public
specifier|static
name|boolean
name|isSuspended
parameter_list|(
name|Object
name|value
parameter_list|)
block|{
if|if
condition|(
name|value
operator|instanceof
name|StatefulService
condition|)
block|{
name|StatefulService
name|service
init|=
operator|(
name|StatefulService
operator|)
name|value
decl_stmt|;
if|if
condition|(
name|service
operator|.
name|isSuspending
argument_list|()
operator|||
name|service
operator|.
name|isSuspended
argument_list|()
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
comment|/**      * Gathers all child services by navigating the service to recursively gather all child services.      *<p/>      * The returned set does<b>not</b> include the children being error handler.      *      * @param service the service      * @return the services, including the parent service, and all its children      */
DECL|method|getChildServices (Service service)
specifier|public
specifier|static
name|Set
argument_list|<
name|Service
argument_list|>
name|getChildServices
parameter_list|(
name|Service
name|service
parameter_list|)
block|{
return|return
name|getChildServices
argument_list|(
name|service
argument_list|,
literal|false
argument_list|)
return|;
block|}
comment|/**      * Gathers all child services by navigating the service to recursively gather all child services.      *      * @param service the service      * @param includeErrorHandler whether to include error handlers      * @return the services, including the parent service, and all its children      */
DECL|method|getChildServices (Service service, boolean includeErrorHandler)
specifier|public
specifier|static
name|Set
argument_list|<
name|Service
argument_list|>
name|getChildServices
parameter_list|(
name|Service
name|service
parameter_list|,
name|boolean
name|includeErrorHandler
parameter_list|)
block|{
name|Set
argument_list|<
name|Service
argument_list|>
name|answer
init|=
operator|new
name|LinkedHashSet
argument_list|<>
argument_list|()
decl_stmt|;
name|doGetChildServices
argument_list|(
name|answer
argument_list|,
name|service
argument_list|,
name|includeErrorHandler
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
DECL|method|doGetChildServices (Set<Service> services, Service service, boolean includeErrorHandler)
specifier|private
specifier|static
name|void
name|doGetChildServices
parameter_list|(
name|Set
argument_list|<
name|Service
argument_list|>
name|services
parameter_list|,
name|Service
name|service
parameter_list|,
name|boolean
name|includeErrorHandler
parameter_list|)
block|{
name|services
operator|.
name|add
argument_list|(
name|service
argument_list|)
expr_stmt|;
if|if
condition|(
name|service
operator|instanceof
name|Navigate
condition|)
block|{
name|Navigate
argument_list|<
name|?
argument_list|>
name|nav
init|=
operator|(
name|Navigate
argument_list|<
name|?
argument_list|>
operator|)
name|service
decl_stmt|;
if|if
condition|(
name|nav
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|List
argument_list|<
name|?
argument_list|>
name|children
init|=
name|nav
operator|.
name|next
argument_list|()
decl_stmt|;
for|for
control|(
name|Object
name|child
range|:
name|children
control|)
block|{
if|if
condition|(
name|child
operator|instanceof
name|Channel
condition|)
block|{
if|if
condition|(
name|includeErrorHandler
condition|)
block|{
comment|// special for error handler as they are tied to the Channel
name|Processor
name|errorHandler
init|=
operator|(
operator|(
name|Channel
operator|)
name|child
operator|)
operator|.
name|getErrorHandler
argument_list|()
decl_stmt|;
if|if
condition|(
name|errorHandler
operator|instanceof
name|Service
condition|)
block|{
name|services
operator|.
name|add
argument_list|(
operator|(
name|Service
operator|)
name|errorHandler
argument_list|)
expr_stmt|;
block|}
block|}
name|Processor
name|next
init|=
operator|(
operator|(
name|Channel
operator|)
name|child
operator|)
operator|.
name|getNextProcessor
argument_list|()
decl_stmt|;
if|if
condition|(
name|next
operator|instanceof
name|Service
condition|)
block|{
name|services
operator|.
name|add
argument_list|(
operator|(
name|Service
operator|)
name|next
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|child
operator|instanceof
name|Service
condition|)
block|{
name|doGetChildServices
argument_list|(
name|services
argument_list|,
operator|(
name|Service
operator|)
name|child
argument_list|,
name|includeErrorHandler
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
block|}
end_class

end_unit

