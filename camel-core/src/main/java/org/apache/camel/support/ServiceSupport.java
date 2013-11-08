begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.support
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|support
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|atomic
operator|.
name|AtomicBoolean
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
name|ServiceStatus
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
name|util
operator|.
name|IOHelper
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
comment|/**  * A useful base class which ensures that a service is only initialized once and  * provides some helper methods for enquiring of its status.  *<p/>  * Implementations can extend this base class and implement {@link org.apache.camel.SuspendableService}  * in case they support suspend/resume.  *  * @version   */
end_comment

begin_class
DECL|class|ServiceSupport
specifier|public
specifier|abstract
class|class
name|ServiceSupport
implements|implements
name|StatefulService
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
name|ServiceSupport
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|started
specifier|protected
specifier|final
name|AtomicBoolean
name|started
init|=
operator|new
name|AtomicBoolean
argument_list|(
literal|false
argument_list|)
decl_stmt|;
DECL|field|starting
specifier|protected
specifier|final
name|AtomicBoolean
name|starting
init|=
operator|new
name|AtomicBoolean
argument_list|(
literal|false
argument_list|)
decl_stmt|;
DECL|field|stopping
specifier|protected
specifier|final
name|AtomicBoolean
name|stopping
init|=
operator|new
name|AtomicBoolean
argument_list|(
literal|false
argument_list|)
decl_stmt|;
DECL|field|stopped
specifier|protected
specifier|final
name|AtomicBoolean
name|stopped
init|=
operator|new
name|AtomicBoolean
argument_list|(
literal|false
argument_list|)
decl_stmt|;
DECL|field|suspending
specifier|protected
specifier|final
name|AtomicBoolean
name|suspending
init|=
operator|new
name|AtomicBoolean
argument_list|(
literal|false
argument_list|)
decl_stmt|;
DECL|field|suspended
specifier|protected
specifier|final
name|AtomicBoolean
name|suspended
init|=
operator|new
name|AtomicBoolean
argument_list|(
literal|false
argument_list|)
decl_stmt|;
DECL|field|shuttingdown
specifier|protected
specifier|final
name|AtomicBoolean
name|shuttingdown
init|=
operator|new
name|AtomicBoolean
argument_list|(
literal|false
argument_list|)
decl_stmt|;
DECL|field|shutdown
specifier|protected
specifier|final
name|AtomicBoolean
name|shutdown
init|=
operator|new
name|AtomicBoolean
argument_list|(
literal|false
argument_list|)
decl_stmt|;
DECL|field|version
specifier|private
name|String
name|version
decl_stmt|;
DECL|method|start ()
specifier|public
name|void
name|start
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|isStarting
argument_list|()
operator|||
name|isStarted
argument_list|()
condition|)
block|{
comment|// only start service if not already started
name|LOG
operator|.
name|trace
argument_list|(
literal|"Service already started"
argument_list|)
expr_stmt|;
return|return;
block|}
if|if
condition|(
name|starting
operator|.
name|compareAndSet
argument_list|(
literal|false
argument_list|,
literal|true
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Starting service"
argument_list|)
expr_stmt|;
try|try
block|{
name|doStart
argument_list|()
expr_stmt|;
name|started
operator|.
name|set
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|starting
operator|.
name|set
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|stopping
operator|.
name|set
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|stopped
operator|.
name|set
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|suspending
operator|.
name|set
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|suspended
operator|.
name|set
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|shutdown
operator|.
name|set
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|shuttingdown
operator|.
name|set
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
try|try
block|{
name|stop
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e2
parameter_list|)
block|{
comment|// Ignore exceptions as we want to show the original exception
block|}
finally|finally
block|{
comment|// ensure flags get reset to stopped as we failed during starting
name|stopping
operator|.
name|set
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|stopped
operator|.
name|set
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|starting
operator|.
name|set
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|started
operator|.
name|set
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|suspending
operator|.
name|set
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|suspended
operator|.
name|set
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|shutdown
operator|.
name|set
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|shuttingdown
operator|.
name|set
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
throw|throw
name|e
throw|;
block|}
block|}
block|}
DECL|method|stop ()
specifier|public
name|void
name|stop
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|isStopped
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Service already stopped"
argument_list|)
expr_stmt|;
return|return;
block|}
if|if
condition|(
name|isStopping
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Service already stopping"
argument_list|)
expr_stmt|;
return|return;
block|}
name|stopping
operator|.
name|set
argument_list|(
literal|true
argument_list|)
expr_stmt|;
try|try
block|{
name|doStop
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
name|stopping
operator|.
name|set
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|stopped
operator|.
name|set
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|starting
operator|.
name|set
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|started
operator|.
name|set
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|suspending
operator|.
name|set
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|suspended
operator|.
name|set
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|shutdown
operator|.
name|set
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|shuttingdown
operator|.
name|set
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|suspend ()
specifier|public
name|void
name|suspend
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|suspended
operator|.
name|get
argument_list|()
condition|)
block|{
if|if
condition|(
name|suspending
operator|.
name|compareAndSet
argument_list|(
literal|false
argument_list|,
literal|true
argument_list|)
condition|)
block|{
try|try
block|{
name|starting
operator|.
name|set
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|stopping
operator|.
name|set
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|doSuspend
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
name|stopped
operator|.
name|set
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|stopping
operator|.
name|set
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|starting
operator|.
name|set
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|started
operator|.
name|set
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|suspending
operator|.
name|set
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|suspended
operator|.
name|set
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|shutdown
operator|.
name|set
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|shuttingdown
operator|.
name|set
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
annotation|@
name|Override
DECL|method|resume ()
specifier|public
name|void
name|resume
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|suspended
operator|.
name|get
argument_list|()
condition|)
block|{
if|if
condition|(
name|starting
operator|.
name|compareAndSet
argument_list|(
literal|false
argument_list|,
literal|true
argument_list|)
condition|)
block|{
try|try
block|{
name|doResume
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
name|started
operator|.
name|set
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|starting
operator|.
name|set
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|stopping
operator|.
name|set
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|stopped
operator|.
name|set
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|suspending
operator|.
name|set
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|suspended
operator|.
name|set
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|shutdown
operator|.
name|set
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|shuttingdown
operator|.
name|set
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
annotation|@
name|Override
DECL|method|shutdown ()
specifier|public
name|void
name|shutdown
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|shutdown
operator|.
name|get
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Service already shut down"
argument_list|)
expr_stmt|;
return|return;
block|}
comment|// ensure we are stopped first
name|stop
argument_list|()
expr_stmt|;
if|if
condition|(
name|shuttingdown
operator|.
name|compareAndSet
argument_list|(
literal|false
argument_list|,
literal|true
argument_list|)
condition|)
block|{
try|try
block|{
name|doShutdown
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
comment|// shutdown is also stopped so only set shutdown flags
name|shutdown
operator|.
name|set
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|shuttingdown
operator|.
name|set
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Override
DECL|method|getStatus ()
specifier|public
name|ServiceStatus
name|getStatus
parameter_list|()
block|{
comment|// we should check the ---ing states first, as this indicate the state is in the middle of doing that
if|if
condition|(
name|isStarting
argument_list|()
condition|)
block|{
return|return
name|ServiceStatus
operator|.
name|Starting
return|;
block|}
if|if
condition|(
name|isStopping
argument_list|()
condition|)
block|{
return|return
name|ServiceStatus
operator|.
name|Stopping
return|;
block|}
if|if
condition|(
name|isSuspending
argument_list|()
condition|)
block|{
return|return
name|ServiceStatus
operator|.
name|Suspending
return|;
block|}
comment|// then check for the regular states
if|if
condition|(
name|isStarted
argument_list|()
condition|)
block|{
return|return
name|ServiceStatus
operator|.
name|Started
return|;
block|}
if|if
condition|(
name|isStopped
argument_list|()
condition|)
block|{
return|return
name|ServiceStatus
operator|.
name|Stopped
return|;
block|}
if|if
condition|(
name|isSuspended
argument_list|()
condition|)
block|{
return|return
name|ServiceStatus
operator|.
name|Suspended
return|;
block|}
comment|// use stopped as fallback
return|return
name|ServiceStatus
operator|.
name|Stopped
return|;
block|}
annotation|@
name|Override
DECL|method|isStarted ()
specifier|public
name|boolean
name|isStarted
parameter_list|()
block|{
return|return
name|started
operator|.
name|get
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|isStarting ()
specifier|public
name|boolean
name|isStarting
parameter_list|()
block|{
return|return
name|starting
operator|.
name|get
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|isStopping ()
specifier|public
name|boolean
name|isStopping
parameter_list|()
block|{
return|return
name|stopping
operator|.
name|get
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|isStopped ()
specifier|public
name|boolean
name|isStopped
parameter_list|()
block|{
return|return
name|stopped
operator|.
name|get
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|isSuspending ()
specifier|public
name|boolean
name|isSuspending
parameter_list|()
block|{
return|return
name|suspending
operator|.
name|get
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|isSuspended ()
specifier|public
name|boolean
name|isSuspended
parameter_list|()
block|{
return|return
name|suspended
operator|.
name|get
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|isRunAllowed ()
specifier|public
name|boolean
name|isRunAllowed
parameter_list|()
block|{
return|return
operator|!
name|isStoppingOrStopped
argument_list|()
return|;
block|}
DECL|method|isStoppingOrStopped ()
specifier|public
name|boolean
name|isStoppingOrStopped
parameter_list|()
block|{
return|return
name|stopping
operator|.
name|get
argument_list|()
operator|||
name|stopped
operator|.
name|get
argument_list|()
return|;
block|}
comment|/**      * Implementations override this method to support customized start/stop.      *<p/>      *<b>Important:</b> See {@link #doStop()} for more details.      *       * @see #doStop()      */
DECL|method|doStart ()
specifier|protected
specifier|abstract
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
function_decl|;
comment|/**      * Implementations override this method to support customized start/stop.      *<p/>      *<b>Important:</b> Camel will invoke this {@link #doStop()} method when      * the service is being stopped. This method will<b>also</b> be invoked      * if the service is still in<i>uninitialized</i> state (eg has not      * been started). The method is<b>always</b> called to allow the service      * to do custom logic when the service is being stopped, such as when      * {@link org.apache.camel.CamelContext} is shutting down.      *       * @see #doStart()       */
DECL|method|doStop ()
specifier|protected
specifier|abstract
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
function_decl|;
comment|/**      * Implementations override this method to support customized suspend/resume.      */
DECL|method|doSuspend ()
specifier|protected
name|void
name|doSuspend
parameter_list|()
throws|throws
name|Exception
block|{
comment|// noop
block|}
comment|/**      * Implementations override this method to support customized suspend/resume.      */
DECL|method|doResume ()
specifier|protected
name|void
name|doResume
parameter_list|()
throws|throws
name|Exception
block|{
comment|// noop
block|}
comment|/**      * Implementations override this method to perform customized shutdown.      */
DECL|method|doShutdown ()
specifier|protected
name|void
name|doShutdown
parameter_list|()
throws|throws
name|Exception
block|{
comment|// noop
block|}
annotation|@
name|Override
DECL|method|getVersion ()
specifier|public
specifier|synchronized
name|String
name|getVersion
parameter_list|()
block|{
if|if
condition|(
name|version
operator|!=
literal|null
condition|)
block|{
return|return
name|version
return|;
block|}
name|InputStream
name|is
init|=
literal|null
decl_stmt|;
comment|// try to load from maven properties first
try|try
block|{
name|Properties
name|p
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|is
operator|=
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"/META-INF/maven/org.apache.camel/camel-core/pom.properties"
argument_list|)
expr_stmt|;
if|if
condition|(
name|is
operator|!=
literal|null
condition|)
block|{
name|p
operator|.
name|load
argument_list|(
name|is
argument_list|)
expr_stmt|;
name|version
operator|=
name|p
operator|.
name|getProperty
argument_list|(
literal|"version"
argument_list|,
literal|""
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// ignore
block|}
finally|finally
block|{
if|if
condition|(
name|is
operator|!=
literal|null
condition|)
block|{
name|IOHelper
operator|.
name|close
argument_list|(
name|is
argument_list|)
expr_stmt|;
block|}
block|}
comment|// fallback to using Java API
if|if
condition|(
name|version
operator|==
literal|null
condition|)
block|{
name|Package
name|aPackage
init|=
name|getClass
argument_list|()
operator|.
name|getPackage
argument_list|()
decl_stmt|;
if|if
condition|(
name|aPackage
operator|!=
literal|null
condition|)
block|{
name|version
operator|=
name|aPackage
operator|.
name|getImplementationVersion
argument_list|()
expr_stmt|;
if|if
condition|(
name|version
operator|==
literal|null
condition|)
block|{
name|version
operator|=
name|aPackage
operator|.
name|getSpecificationVersion
argument_list|()
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|version
operator|==
literal|null
condition|)
block|{
comment|// we could not compute the version so use a blank
name|version
operator|=
literal|""
expr_stmt|;
block|}
return|return
name|version
return|;
block|}
block|}
end_class

end_unit

