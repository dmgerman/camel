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
name|concurrent
operator|.
name|CopyOnWriteArrayList
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

begin_comment
comment|/**  * Base class to control lifecycle for a set of child {@link org.apache.camel.Service}s.  */
end_comment

begin_class
DECL|class|ChildServiceSupport
specifier|public
specifier|abstract
class|class
name|ChildServiceSupport
extends|extends
name|ServiceSupport
block|{
DECL|field|childServices
specifier|protected
specifier|volatile
name|List
argument_list|<
name|Service
argument_list|>
name|childServices
decl_stmt|;
DECL|method|start ()
specifier|public
name|void
name|start
parameter_list|()
throws|throws
name|Exception
block|{
synchronized|synchronized
init|(
name|lock
init|)
block|{
if|if
condition|(
name|status
operator|==
name|STARTED
condition|)
block|{
name|log
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
name|status
operator|==
name|STARTING
condition|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Service already starting"
argument_list|)
expr_stmt|;
return|return;
block|}
name|status
operator|=
name|STARTING
expr_stmt|;
name|log
operator|.
name|trace
argument_list|(
literal|"Starting service"
argument_list|)
expr_stmt|;
try|try
block|{
name|ServiceHelper
operator|.
name|startService
argument_list|(
name|childServices
argument_list|)
expr_stmt|;
name|doStart
argument_list|()
expr_stmt|;
name|status
operator|=
name|STARTED
expr_stmt|;
name|log
operator|.
name|trace
argument_list|(
literal|"Service started"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|status
operator|=
name|FAILED
expr_stmt|;
name|log
operator|.
name|trace
argument_list|(
literal|"Error while starting service"
argument_list|,
name|e
argument_list|)
expr_stmt|;
name|ServiceHelper
operator|.
name|stopService
argument_list|(
name|childServices
argument_list|)
expr_stmt|;
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
synchronized|synchronized
init|(
name|lock
init|)
block|{
if|if
condition|(
name|status
operator|==
name|STOPPED
operator|||
name|status
operator|==
name|SHUTTINGDOWN
operator|||
name|status
operator|==
name|SHUTDOWN
condition|)
block|{
name|log
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
name|status
operator|==
name|STOPPING
condition|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Service already stopping"
argument_list|)
expr_stmt|;
return|return;
block|}
name|status
operator|=
name|STOPPING
expr_stmt|;
name|log
operator|.
name|trace
argument_list|(
literal|"Stopping service"
argument_list|)
expr_stmt|;
try|try
block|{
name|doStop
argument_list|()
expr_stmt|;
name|ServiceHelper
operator|.
name|stopService
argument_list|(
name|childServices
argument_list|)
expr_stmt|;
name|status
operator|=
name|STOPPED
expr_stmt|;
name|log
operator|.
name|trace
argument_list|(
literal|"Service stopped service"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|status
operator|=
name|FAILED
expr_stmt|;
name|log
operator|.
name|trace
argument_list|(
literal|"Error while stopping service"
argument_list|,
name|e
argument_list|)
expr_stmt|;
throw|throw
name|e
throw|;
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
synchronized|synchronized
init|(
name|lock
init|)
block|{
if|if
condition|(
name|status
operator|==
name|SHUTDOWN
condition|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Service already shut down"
argument_list|)
expr_stmt|;
return|return;
block|}
if|if
condition|(
name|status
operator|==
name|SHUTTINGDOWN
condition|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Service already shutting down"
argument_list|)
expr_stmt|;
return|return;
block|}
name|stop
argument_list|()
expr_stmt|;
name|status
operator|=
name|SHUTDOWN
expr_stmt|;
name|log
operator|.
name|trace
argument_list|(
literal|"Shutting down service"
argument_list|)
expr_stmt|;
try|try
block|{
name|doShutdown
argument_list|()
expr_stmt|;
name|ServiceHelper
operator|.
name|stopAndShutdownServices
argument_list|(
name|childServices
argument_list|)
expr_stmt|;
name|log
operator|.
name|trace
argument_list|(
literal|"Service shut down"
argument_list|)
expr_stmt|;
name|status
operator|=
name|SHUTDOWN
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|status
operator|=
name|FAILED
expr_stmt|;
name|log
operator|.
name|trace
argument_list|(
literal|"Error shutting down service"
argument_list|,
name|e
argument_list|)
expr_stmt|;
throw|throw
name|e
throw|;
block|}
block|}
block|}
DECL|method|addChildService (Object childService)
specifier|protected
name|void
name|addChildService
parameter_list|(
name|Object
name|childService
parameter_list|)
block|{
if|if
condition|(
name|childService
operator|instanceof
name|Service
condition|)
block|{
if|if
condition|(
name|childServices
operator|==
literal|null
condition|)
block|{
synchronized|synchronized
init|(
name|lock
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
name|CopyOnWriteArrayList
argument_list|<>
argument_list|()
expr_stmt|;
block|}
block|}
block|}
name|childServices
operator|.
name|add
argument_list|(
operator|(
name|Service
operator|)
name|childService
argument_list|)
expr_stmt|;
block|}
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
block|}
end_class

end_unit

