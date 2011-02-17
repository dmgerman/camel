begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
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
name|Set
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
name|ServiceHelper
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
name|Service
implements|,
name|ShutdownableService
block|{
DECL|field|started
specifier|private
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
specifier|private
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
specifier|private
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
specifier|private
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
specifier|private
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
specifier|private
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
specifier|private
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
specifier|private
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
DECL|field|childServices
specifier|private
name|Set
argument_list|<
name|Object
argument_list|>
name|childServices
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
name|start
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
DECL|method|start (boolean startChildren)
specifier|public
name|void
name|start
parameter_list|(
name|boolean
name|startChildren
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|started
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
name|boolean
name|childrenStarted
init|=
literal|false
decl_stmt|;
name|Exception
name|ex
init|=
literal|null
decl_stmt|;
try|try
block|{
if|if
condition|(
name|childServices
operator|!=
literal|null
operator|&&
name|startChildren
condition|)
block|{
name|ServiceHelper
operator|.
name|startServices
argument_list|(
name|childServices
argument_list|)
expr_stmt|;
block|}
name|childrenStarted
operator|=
literal|true
expr_stmt|;
name|doStart
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|ex
operator|=
name|e
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|ex
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|stop
argument_list|(
name|childrenStarted
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// Ignore exceptions as we want to show the original exception
block|}
throw|throw
name|ex
throw|;
block|}
else|else
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
block|}
DECL|method|stop (boolean childrenStarted)
specifier|private
name|void
name|stop
parameter_list|(
name|boolean
name|childrenStarted
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|stopping
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
try|try
block|{
name|starting
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
if|if
condition|(
name|childrenStarted
condition|)
block|{
name|doStop
argument_list|()
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|started
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
if|if
condition|(
name|childServices
operator|!=
literal|null
condition|)
block|{
name|ServiceHelper
operator|.
name|stopServices
argument_list|(
name|childServices
argument_list|)
expr_stmt|;
block|}
block|}
block|}
finally|finally
block|{
name|stopped
operator|.
name|set
argument_list|(
literal|true
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
operator|!
name|stopped
operator|.
name|get
argument_list|()
condition|)
block|{
name|stop
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
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
DECL|method|shutdown ()
specifier|public
name|void
name|shutdown
parameter_list|()
throws|throws
name|Exception
block|{
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
try|try
block|{
name|doShutdown
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|childServices
operator|!=
literal|null
condition|)
block|{
name|ServiceHelper
operator|.
name|stopAndShutdownService
argument_list|(
name|childServices
argument_list|)
expr_stmt|;
block|}
block|}
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
comment|/**      * Returns the current status      */
DECL|method|getStatus ()
specifier|public
name|ServiceStatus
name|getStatus
parameter_list|()
block|{
comment|// lets check these in oldest first as these flags can be changing in a concurrent world
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
comment|/**      * @return true if this service has been started      */
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
comment|/**      * @return true if this service is being started      */
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
comment|/**      * @return true if this service is in the process of stopping      */
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
comment|/**      * @return true if this service is stopped      */
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
comment|/**      * @return true if this service is in the process of suspending      */
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
comment|/**      * @return true if this service is suspended      */
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
comment|/**      * Helper methods so the service knows if it should keep running.      * Returns<tt>false</tt> if the service is being stopped or is stopped.      *      * @return<tt>true</tt> if the service should continue to run.      */
DECL|method|isRunAllowed ()
specifier|public
name|boolean
name|isRunAllowed
parameter_list|()
block|{
return|return
operator|!
operator|(
name|stopping
operator|.
name|get
argument_list|()
operator|||
name|stopped
operator|.
name|get
argument_list|()
operator|)
return|;
block|}
DECL|method|doStart ()
specifier|protected
specifier|abstract
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
function_decl|;
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
block|{     }
comment|/**      * Implementations override this method to support customized suspend/resume.      */
DECL|method|doResume ()
specifier|protected
name|void
name|doResume
parameter_list|()
throws|throws
name|Exception
block|{     }
comment|/**      * Implementations override this method to perform customized shutdown      */
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
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|addChildService (Object childService)
specifier|protected
name|void
name|addChildService
parameter_list|(
name|Object
name|childService
parameter_list|)
block|{
synchronized|synchronized
init|(
name|this
init|)
block|{
if|if
condition|(
name|childServices
operator|==
literal|null
condition|)
block|{
name|childServices
operator|=
operator|new
name|LinkedHashSet
argument_list|()
expr_stmt|;
block|}
block|}
name|childServices
operator|.
name|add
argument_list|(
name|childService
argument_list|)
expr_stmt|;
block|}
DECL|method|removeChildService (Object childService)
specifier|protected
name|boolean
name|removeChildService
parameter_list|(
name|Object
name|childService
parameter_list|)
block|{
return|return
name|childServices
operator|!=
literal|null
operator|&&
name|childServices
operator|.
name|remove
argument_list|(
name|childService
argument_list|)
return|;
block|}
comment|/**      * Returns the version of this service      */
DECL|method|getVersion ()
specifier|public
specifier|synchronized
name|String
name|getVersion
parameter_list|()
block|{
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|version
argument_list|)
condition|)
block|{
return|return
name|version
return|;
block|}
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
return|return
name|version
operator|!=
literal|null
condition|?
name|version
else|:
literal|""
return|;
block|}
block|}
end_class

end_unit

