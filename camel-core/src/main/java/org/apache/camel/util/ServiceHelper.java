begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.util
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
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
name|List
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
name|SuspendableService
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
name|ServiceSupport
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

begin_comment
comment|/**  * A collection of helper methods for working with {@link Service} objects  *  * @version $Revision$  */
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
specifier|transient
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
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
comment|/**      * Starts all of the given services      */
DECL|method|startService (Object value)
specifier|public
specifier|static
name|void
name|startService
parameter_list|(
name|Object
name|value
parameter_list|)
throws|throws
name|Exception
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
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Starting service: "
operator|+
name|service
argument_list|)
expr_stmt|;
block|}
name|service
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
name|Collection
condition|)
block|{
name|startServices
argument_list|(
operator|(
name|Collection
argument_list|<
name|?
argument_list|>
operator|)
name|value
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Starts all of the given services      */
DECL|method|startServices (Object... services)
specifier|public
specifier|static
name|void
name|startServices
parameter_list|(
name|Object
modifier|...
name|services
parameter_list|)
throws|throws
name|Exception
block|{
for|for
control|(
name|Object
name|value
range|:
name|services
control|)
block|{
name|startService
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Starts all of the given services      */
DECL|method|startServices (Collection<?> services)
specifier|public
specifier|static
name|void
name|startServices
parameter_list|(
name|Collection
argument_list|<
name|?
argument_list|>
name|services
parameter_list|)
throws|throws
name|Exception
block|{
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
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Starting service: "
operator|+
name|service
argument_list|)
expr_stmt|;
block|}
name|service
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Stops all of the given services, throwing the first exception caught      */
DECL|method|stopServices (Object... services)
specifier|public
specifier|static
name|void
name|stopServices
parameter_list|(
name|Object
modifier|...
name|services
parameter_list|)
throws|throws
name|Exception
block|{
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
name|stopServices
argument_list|(
name|list
argument_list|)
expr_stmt|;
block|}
comment|/**      * Stops all of the given services, throwing the first exception caught      */
DECL|method|stopService (Object value)
specifier|public
specifier|static
name|void
name|stopService
parameter_list|(
name|Object
name|value
parameter_list|)
throws|throws
name|Exception
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
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Stopping service "
operator|+
name|value
argument_list|)
expr_stmt|;
block|}
name|service
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
name|Collection
condition|)
block|{
name|stopServices
argument_list|(
operator|(
name|Collection
argument_list|<
name|?
argument_list|>
operator|)
name|value
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Stops all of the given services, throwing the first exception caught      */
DECL|method|stopServices (Collection<?> services)
specifier|public
specifier|static
name|void
name|stopServices
parameter_list|(
name|Collection
argument_list|<
name|?
argument_list|>
name|services
parameter_list|)
throws|throws
name|Exception
block|{
name|Exception
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
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Stopping service: "
operator|+
name|service
argument_list|)
expr_stmt|;
block|}
name|service
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
literal|"Caught exception stopping service: "
operator|+
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
comment|/**      * Stops and shutdowns all of the given services, throwing the first exception caught      */
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
throws|throws
name|Exception
block|{
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
comment|/**      * Stops and shutdowns all of the given services, throwing the first exception caught      */
DECL|method|stopAndShutdownService (Object value)
specifier|public
specifier|static
name|void
name|stopAndShutdownService
parameter_list|(
name|Object
name|value
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|value
operator|instanceof
name|Service
condition|)
block|{
comment|// must stop it first
name|stopService
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
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
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Shutting down service "
operator|+
name|value
argument_list|)
expr_stmt|;
block|}
name|service
operator|.
name|shutdown
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|value
operator|instanceof
name|Collection
condition|)
block|{
name|stopAndShutdownServices
argument_list|(
operator|(
name|Collection
argument_list|<
name|?
argument_list|>
operator|)
name|value
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Stops and shutdowns all of the given services, throwing the first exception caught      */
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
throws|throws
name|Exception
block|{
name|Exception
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
try|try
block|{
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Shutting down service: "
operator|+
name|service
argument_list|)
expr_stmt|;
block|}
name|service
operator|.
name|shutdown
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
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
literal|"Caught exception shutting down service: "
operator|+
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
comment|/**      * Resumes the given service.      *<p/>      * If the service is a {@link org.apache.camel.SuspendableService} then the<tt>resume</tt>      * operation is<b>only</b> invoked if the service is suspended.      *<p/>      * If the service is a {@link org.apache.camel.impl.ServiceSupport} then the<tt>start</tt>      * operation is<b>only</b> invoked if the service is startable.      *<p/>      * Otherwise the service is started.      *      * @param service the service      * @return<tt>true</tt> if either<tt>resume</tt> or<tt>start</tt> was invoked,      *<tt>false</tt> if the service is already in the desired state.      * @throws Exception is thrown if error occurred      */
DECL|method|resumeService (Service service)
specifier|public
specifier|static
name|boolean
name|resumeService
parameter_list|(
name|Service
name|service
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
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
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Resuming service "
operator|+
name|service
argument_list|)
expr_stmt|;
block|}
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
elseif|else
if|if
condition|(
name|service
operator|instanceof
name|ServiceSupport
condition|)
block|{
name|ServiceSupport
name|ss
init|=
operator|(
name|ServiceSupport
operator|)
name|service
decl_stmt|;
if|if
condition|(
name|ss
operator|.
name|getStatus
argument_list|()
operator|.
name|isStartable
argument_list|()
condition|)
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
comment|/**      * Suspends the given service.      *<p/>      * If the service is a {@link org.apache.camel.SuspendableService} then the<tt>suspend</tt>      * operation is<b>only</b> invoked if the service is<b>not</b> suspended.      *<p/>      * If the service is a {@link org.apache.camel.impl.ServiceSupport} then the<tt>stop</tt>      * operation is<b>only</b> invoked if the service is stopable.      *<p/>      * Otherwise the service is stopped.      *      * @param service the service      * @return<tt>true</tt> if either<tt>suspend</tt> or<tt>stop</tt> was invoked,      *<tt>false</tt> if the service is already in the desired state.      * @throws Exception is thrown if error occurred      */
DECL|method|suspendService (Service service)
specifier|public
specifier|static
name|boolean
name|suspendService
parameter_list|(
name|Service
name|service
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
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
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Suspending service "
operator|+
name|service
argument_list|)
expr_stmt|;
block|}
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
elseif|else
if|if
condition|(
name|service
operator|instanceof
name|ServiceSupport
condition|)
block|{
name|ServiceSupport
name|ss
init|=
operator|(
name|ServiceSupport
operator|)
name|service
decl_stmt|;
if|if
condition|(
name|ss
operator|.
name|getStatus
argument_list|()
operator|.
name|isStoppable
argument_list|()
condition|)
block|{
name|stopServices
argument_list|(
name|service
argument_list|)
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
block|}
end_class

end_unit

