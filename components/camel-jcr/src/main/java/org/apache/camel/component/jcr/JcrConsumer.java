begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jcr
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jcr
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
name|concurrent
operator|.
name|ScheduledExecutorService
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
name|ScheduledFuture
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
name|TimeUnit
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jcr
operator|.
name|RepositoryException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jcr
operator|.
name|Session
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jcr
operator|.
name|observation
operator|.
name|EventListener
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
name|support
operator|.
name|DefaultConsumer
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

begin_comment
comment|/**  * A {@link org.apache.camel.Consumer} to consume JCR events.  */
end_comment

begin_class
DECL|class|JcrConsumer
specifier|public
class|class
name|JcrConsumer
extends|extends
name|DefaultConsumer
block|{
DECL|field|session
specifier|private
name|Session
name|session
decl_stmt|;
DECL|field|eventListener
specifier|private
name|EventListener
name|eventListener
decl_stmt|;
DECL|field|sessionListenerCheckerScheduledFuture
specifier|private
name|ScheduledFuture
argument_list|<
name|?
argument_list|>
name|sessionListenerCheckerScheduledFuture
decl_stmt|;
DECL|method|JcrConsumer (JcrEndpoint endpoint, Processor processor)
specifier|public
name|JcrConsumer
parameter_list|(
name|JcrEndpoint
name|endpoint
parameter_list|,
name|Processor
name|processor
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|,
name|processor
argument_list|)
expr_stmt|;
block|}
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
name|scheduleSessionListenerChecker
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
name|cancelSessionListenerChecker
argument_list|()
expr_stmt|;
name|unregisterListenerAndLogoutSession
argument_list|()
expr_stmt|;
block|}
DECL|method|getJcrEndpoint ()
specifier|protected
name|JcrEndpoint
name|getJcrEndpoint
parameter_list|()
block|{
name|JcrEndpoint
name|endpoint
init|=
operator|(
name|JcrEndpoint
operator|)
name|getEndpoint
argument_list|()
decl_stmt|;
return|return
name|endpoint
return|;
block|}
DECL|method|createSessionAndRegisterListener ()
specifier|private
specifier|synchronized
name|void
name|createSessionAndRegisterListener
parameter_list|()
throws|throws
name|RepositoryException
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"createSessionAndRegisterListener START"
argument_list|)
expr_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|getJcrEndpoint
argument_list|()
operator|.
name|getWorkspaceName
argument_list|()
argument_list|)
condition|)
block|{
name|session
operator|=
name|getJcrEndpoint
argument_list|()
operator|.
name|getRepository
argument_list|()
operator|.
name|login
argument_list|(
name|getJcrEndpoint
argument_list|()
operator|.
name|getCredentials
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|session
operator|=
name|getJcrEndpoint
argument_list|()
operator|.
name|getRepository
argument_list|()
operator|.
name|login
argument_list|(
name|getJcrEndpoint
argument_list|()
operator|.
name|getCredentials
argument_list|()
argument_list|,
name|getJcrEndpoint
argument_list|()
operator|.
name|getWorkspaceName
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|int
name|eventTypes
init|=
name|getJcrEndpoint
argument_list|()
operator|.
name|getEventTypes
argument_list|()
decl_stmt|;
name|String
name|absPath
init|=
name|getJcrEndpoint
argument_list|()
operator|.
name|getBase
argument_list|()
decl_stmt|;
if|if
condition|(
name|absPath
operator|==
literal|null
condition|)
block|{
name|absPath
operator|=
literal|"/"
expr_stmt|;
block|}
elseif|else
if|if
condition|(
operator|!
name|absPath
operator|.
name|startsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|absPath
operator|=
literal|"/"
operator|+
name|absPath
expr_stmt|;
block|}
name|boolean
name|isDeep
init|=
name|getJcrEndpoint
argument_list|()
operator|.
name|isDeep
argument_list|()
decl_stmt|;
name|String
index|[]
name|uuid
init|=
literal|null
decl_stmt|;
name|String
name|uuids
init|=
name|getJcrEndpoint
argument_list|()
operator|.
name|getUuids
argument_list|()
decl_stmt|;
if|if
condition|(
name|uuids
operator|!=
literal|null
condition|)
block|{
name|uuids
operator|=
name|uuids
operator|.
name|trim
argument_list|()
expr_stmt|;
if|if
condition|(
operator|!
literal|""
operator|.
name|equals
argument_list|(
name|uuids
argument_list|)
condition|)
block|{
name|uuid
operator|=
name|uuids
operator|.
name|split
argument_list|(
literal|","
argument_list|)
expr_stmt|;
block|}
block|}
name|String
index|[]
name|nodeTypeName
init|=
literal|null
decl_stmt|;
name|String
name|nodeTypeNames
init|=
name|getJcrEndpoint
argument_list|()
operator|.
name|getNodeTypeNames
argument_list|()
decl_stmt|;
if|if
condition|(
name|nodeTypeNames
operator|!=
literal|null
condition|)
block|{
name|nodeTypeNames
operator|=
name|nodeTypeNames
operator|.
name|trim
argument_list|()
expr_stmt|;
if|if
condition|(
operator|!
literal|""
operator|.
name|equals
argument_list|(
name|nodeTypeNames
argument_list|)
condition|)
block|{
name|nodeTypeName
operator|=
name|nodeTypeNames
operator|.
name|split
argument_list|(
literal|","
argument_list|)
expr_stmt|;
block|}
block|}
name|boolean
name|noLocal
init|=
name|getJcrEndpoint
argument_list|()
operator|.
name|isNoLocal
argument_list|()
decl_stmt|;
name|eventListener
operator|=
operator|new
name|EndpointEventListener
argument_list|(
name|getJcrEndpoint
argument_list|()
argument_list|,
name|getProcessor
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|log
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Adding JCR Event Listener, {}, on {}. eventTypes="
operator|+
name|eventTypes
operator|+
literal|", isDeep="
operator|+
name|isDeep
operator|+
literal|", uuid="
operator|+
name|Arrays
operator|.
name|toString
argument_list|(
name|uuid
argument_list|)
operator|+
literal|", nodeTypeName="
operator|+
name|Arrays
operator|.
name|toString
argument_list|(
name|nodeTypeName
argument_list|)
operator|+
literal|", noLocal="
operator|+
name|noLocal
argument_list|,
name|eventListener
argument_list|,
name|absPath
argument_list|)
expr_stmt|;
block|}
name|session
operator|.
name|getWorkspace
argument_list|()
operator|.
name|getObservationManager
argument_list|()
operator|.
name|addEventListener
argument_list|(
name|eventListener
argument_list|,
name|eventTypes
argument_list|,
name|absPath
argument_list|,
name|isDeep
argument_list|,
name|uuid
argument_list|,
name|nodeTypeName
argument_list|,
name|noLocal
argument_list|)
expr_stmt|;
name|log
operator|.
name|trace
argument_list|(
literal|"createSessionAndRegisterListener END"
argument_list|)
expr_stmt|;
block|}
DECL|method|unregisterListenerAndLogoutSession ()
specifier|private
specifier|synchronized
name|void
name|unregisterListenerAndLogoutSession
parameter_list|()
throws|throws
name|RepositoryException
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"unregisterListenerAndLogoutSession START"
argument_list|)
expr_stmt|;
if|if
condition|(
name|session
operator|!=
literal|null
condition|)
block|{
try|try
block|{
if|if
condition|(
operator|!
name|session
operator|.
name|isLive
argument_list|()
condition|)
block|{
name|log
operator|.
name|info
argument_list|(
literal|"Session was is no more live."
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|eventListener
operator|!=
literal|null
condition|)
block|{
name|session
operator|.
name|getWorkspace
argument_list|()
operator|.
name|getObservationManager
argument_list|()
operator|.
name|removeEventListener
argument_list|(
name|eventListener
argument_list|)
expr_stmt|;
name|eventListener
operator|=
literal|null
expr_stmt|;
block|}
name|session
operator|.
name|logout
argument_list|()
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|eventListener
operator|=
literal|null
expr_stmt|;
name|session
operator|=
literal|null
expr_stmt|;
block|}
block|}
name|log
operator|.
name|trace
argument_list|(
literal|"unregisterListenerAndLogoutSession END"
argument_list|)
expr_stmt|;
block|}
DECL|method|cancelSessionListenerChecker ()
specifier|private
name|void
name|cancelSessionListenerChecker
parameter_list|()
block|{
if|if
condition|(
name|sessionListenerCheckerScheduledFuture
operator|!=
literal|null
condition|)
block|{
name|sessionListenerCheckerScheduledFuture
operator|.
name|cancel
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|scheduleSessionListenerChecker ()
specifier|private
name|void
name|scheduleSessionListenerChecker
parameter_list|()
block|{
name|String
name|name
init|=
literal|"JcrConsumerSessionChecker["
operator|+
name|getJcrEndpoint
argument_list|()
operator|.
name|getEndpointConfiguredDestinationName
argument_list|()
operator|+
literal|"]"
decl_stmt|;
name|ScheduledExecutorService
name|executor
init|=
name|getJcrEndpoint
argument_list|()
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getExecutorServiceManager
argument_list|()
operator|.
name|newSingleThreadScheduledExecutor
argument_list|(
name|this
argument_list|,
name|name
argument_list|)
decl_stmt|;
name|JcrConsumerSessionListenerChecker
name|sessionListenerChecker
init|=
operator|new
name|JcrConsumerSessionListenerChecker
argument_list|()
decl_stmt|;
name|long
name|sessionLiveCheckIntervalOnStart
init|=
name|JcrConsumer
operator|.
name|this
operator|.
name|getJcrEndpoint
argument_list|()
operator|.
name|getSessionLiveCheckIntervalOnStart
argument_list|()
decl_stmt|;
name|long
name|sessionLiveCheckInterval
init|=
name|JcrConsumer
operator|.
name|this
operator|.
name|getJcrEndpoint
argument_list|()
operator|.
name|getSessionLiveCheckInterval
argument_list|()
decl_stmt|;
name|sessionListenerCheckerScheduledFuture
operator|=
name|executor
operator|.
name|scheduleWithFixedDelay
argument_list|(
name|sessionListenerChecker
argument_list|,
name|sessionLiveCheckIntervalOnStart
argument_list|,
name|sessionLiveCheckInterval
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
expr_stmt|;
block|}
DECL|class|JcrConsumerSessionListenerChecker
specifier|private
class|class
name|JcrConsumerSessionListenerChecker
implements|implements
name|Runnable
block|{
DECL|method|run ()
specifier|public
name|void
name|run
parameter_list|()
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"JcrConsumerSessionListenerChecker starts."
argument_list|)
expr_stmt|;
name|boolean
name|isSessionLive
init|=
literal|false
decl_stmt|;
synchronized|synchronized
init|(
name|this
init|)
block|{
if|if
condition|(
name|JcrConsumer
operator|.
name|this
operator|.
name|session
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|isSessionLive
operator|=
name|JcrConsumer
operator|.
name|this
operator|.
name|session
operator|.
name|isLive
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Exception while checking jcr session"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
operator|!
name|isSessionLive
condition|)
block|{
try|try
block|{
name|createSessionAndRegisterListener
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RepositoryException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Failed to create session and register listener"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
name|log
operator|.
name|debug
argument_list|(
literal|"JcrConsumerSessionListenerChecker stops."
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

