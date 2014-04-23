begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.openshift
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|openshift
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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
name|com
operator|.
name|openshift
operator|.
name|client
operator|.
name|IApplication
import|;
end_import

begin_import
import|import
name|com
operator|.
name|openshift
operator|.
name|client
operator|.
name|IDomain
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
name|Exchange
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
name|impl
operator|.
name|ScheduledPollConsumer
import|;
end_import

begin_class
DECL|class|OpenShiftConsumer
specifier|public
class|class
name|OpenShiftConsumer
extends|extends
name|ScheduledPollConsumer
block|{
DECL|field|oldState
specifier|private
specifier|final
name|Map
argument_list|<
name|ApplicationState
argument_list|,
name|ApplicationState
argument_list|>
name|oldState
init|=
operator|new
name|HashMap
argument_list|<
name|ApplicationState
argument_list|,
name|ApplicationState
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|initialPoll
specifier|private
specifier|volatile
name|boolean
name|initialPoll
decl_stmt|;
DECL|method|OpenShiftConsumer (Endpoint endpoint, Processor processor)
specifier|public
name|OpenShiftConsumer
parameter_list|(
name|Endpoint
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
DECL|method|getEndpoint ()
specifier|public
name|OpenShiftEndpoint
name|getEndpoint
parameter_list|()
block|{
return|return
operator|(
name|OpenShiftEndpoint
operator|)
name|super
operator|.
name|getEndpoint
argument_list|()
return|;
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
name|initialPoll
operator|=
literal|true
expr_stmt|;
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|poll ()
specifier|protected
name|int
name|poll
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|openshiftServer
init|=
name|OpenShiftHelper
operator|.
name|getOpenShiftServer
argument_list|(
name|getEndpoint
argument_list|()
argument_list|)
decl_stmt|;
name|IDomain
name|domain
init|=
name|OpenShiftHelper
operator|.
name|loginAndGetDomain
argument_list|(
name|getEndpoint
argument_list|()
argument_list|,
name|openshiftServer
argument_list|)
decl_stmt|;
if|if
condition|(
name|domain
operator|==
literal|null
condition|)
block|{
return|return
literal|0
return|;
block|}
if|if
condition|(
name|getEndpoint
argument_list|()
operator|.
name|getPollMode
argument_list|()
operator|.
name|equals
argument_list|(
name|OpenShiftPollMode
operator|.
name|all
operator|.
name|name
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|doPollAll
argument_list|(
name|domain
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|doPollOnChange
argument_list|(
name|domain
argument_list|)
return|;
block|}
block|}
DECL|method|doPollAll (IDomain domain)
specifier|protected
name|int
name|doPollAll
parameter_list|(
name|IDomain
name|domain
parameter_list|)
block|{
name|List
argument_list|<
name|IApplication
argument_list|>
name|apps
init|=
name|domain
operator|.
name|getApplications
argument_list|()
decl_stmt|;
for|for
control|(
name|IApplication
name|app
range|:
name|apps
control|)
block|{
name|Exchange
name|exchange
init|=
name|getEndpoint
argument_list|()
operator|.
name|createExchange
argument_list|(
name|app
argument_list|)
decl_stmt|;
try|try
block|{
name|getProcessor
argument_list|()
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|exchange
operator|.
name|getException
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|getExceptionHandler
argument_list|()
operator|.
name|handleException
argument_list|(
literal|"Error during processing exchange."
argument_list|,
name|exchange
argument_list|,
name|exchange
operator|.
name|getException
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|apps
operator|.
name|size
argument_list|()
return|;
block|}
DECL|method|doPollOnChange (IDomain domain)
specifier|protected
name|int
name|doPollOnChange
parameter_list|(
name|IDomain
name|domain
parameter_list|)
block|{
comment|// an app can either be
comment|// - added
comment|// - removed
comment|// - state changed
name|Map
argument_list|<
name|ApplicationState
argument_list|,
name|ApplicationState
argument_list|>
name|newState
init|=
operator|new
name|HashMap
argument_list|<
name|ApplicationState
argument_list|,
name|ApplicationState
argument_list|>
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|IApplication
argument_list|>
name|apps
init|=
name|domain
operator|.
name|getApplications
argument_list|()
decl_stmt|;
for|for
control|(
name|IApplication
name|app
range|:
name|apps
control|)
block|{
name|ApplicationState
name|state
init|=
operator|new
name|ApplicationState
argument_list|(
name|app
operator|.
name|getUUID
argument_list|()
argument_list|,
name|app
argument_list|,
name|OpenShiftHelper
operator|.
name|getStateForApplication
argument_list|(
name|app
argument_list|)
argument_list|)
decl_stmt|;
name|newState
operator|.
name|put
argument_list|(
name|state
argument_list|,
name|state
argument_list|)
expr_stmt|;
block|}
comment|// compute what is the delta from last time
comment|// so we split up into 3 groups, of added/removed/changed
name|Map
argument_list|<
name|ApplicationState
argument_list|,
name|ApplicationState
argument_list|>
name|added
init|=
operator|new
name|HashMap
argument_list|<
name|ApplicationState
argument_list|,
name|ApplicationState
argument_list|>
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|ApplicationState
argument_list|,
name|ApplicationState
argument_list|>
name|removed
init|=
operator|new
name|HashMap
argument_list|<
name|ApplicationState
argument_list|,
name|ApplicationState
argument_list|>
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|ApplicationState
argument_list|,
name|ApplicationState
argument_list|>
name|changed
init|=
operator|new
name|HashMap
argument_list|<
name|ApplicationState
argument_list|,
name|ApplicationState
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|ApplicationState
name|state
range|:
name|newState
operator|.
name|keySet
argument_list|()
control|)
block|{
if|if
condition|(
operator|!
name|oldState
operator|.
name|containsKey
argument_list|(
name|state
argument_list|)
condition|)
block|{
comment|// its a new app added
name|added
operator|.
name|put
argument_list|(
name|state
argument_list|,
name|state
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|ApplicationState
name|old
init|=
name|oldState
operator|.
name|get
argument_list|(
name|state
argument_list|)
decl_stmt|;
if|if
condition|(
name|old
operator|!=
literal|null
operator|&&
operator|!
name|old
operator|.
name|getState
argument_list|()
operator|.
name|equals
argument_list|(
name|state
operator|.
name|getState
argument_list|()
argument_list|)
condition|)
block|{
comment|// the state changed
name|state
operator|.
name|setOldState
argument_list|(
name|old
operator|.
name|getState
argument_list|()
argument_list|)
expr_stmt|;
name|changed
operator|.
name|put
argument_list|(
name|state
argument_list|,
name|state
argument_list|)
expr_stmt|;
block|}
block|}
block|}
for|for
control|(
name|ApplicationState
name|state
range|:
name|oldState
operator|.
name|keySet
argument_list|()
control|)
block|{
if|if
condition|(
operator|!
name|newState
operator|.
name|containsKey
argument_list|(
name|state
argument_list|)
condition|)
block|{
comment|// its a app removed
name|removed
operator|.
name|put
argument_list|(
name|state
argument_list|,
name|state
argument_list|)
expr_stmt|;
block|}
block|}
comment|// only emit if needed
name|int
name|processed
init|=
literal|0
decl_stmt|;
if|if
condition|(
operator|!
name|initialPoll
operator|||
name|initialPoll
operator|&&
name|getEndpoint
argument_list|()
operator|.
name|getPollMode
argument_list|()
operator|.
name|equals
argument_list|(
name|OpenShiftPollMode
operator|.
name|onChangeWithInitial
operator|.
name|name
argument_list|()
argument_list|)
condition|)
block|{
for|for
control|(
name|ApplicationState
name|add
range|:
name|added
operator|.
name|keySet
argument_list|()
control|)
block|{
name|Exchange
name|exchange
init|=
name|getEndpoint
argument_list|()
operator|.
name|createExchange
argument_list|(
name|add
operator|.
name|getApplication
argument_list|()
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|OpenShiftConstants
operator|.
name|EVENT_TYPE
argument_list|,
literal|"added"
argument_list|)
expr_stmt|;
try|try
block|{
name|processed
operator|++
expr_stmt|;
name|getProcessor
argument_list|()
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|exchange
operator|.
name|getException
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|getExceptionHandler
argument_list|()
operator|.
name|handleException
argument_list|(
literal|"Error during processing exchange."
argument_list|,
name|exchange
argument_list|,
name|exchange
operator|.
name|getException
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
for|for
control|(
name|ApplicationState
name|remove
range|:
name|removed
operator|.
name|keySet
argument_list|()
control|)
block|{
name|Exchange
name|exchange
init|=
name|getEndpoint
argument_list|()
operator|.
name|createExchange
argument_list|(
name|remove
operator|.
name|getApplication
argument_list|()
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|OpenShiftConstants
operator|.
name|EVENT_TYPE
argument_list|,
literal|"removed"
argument_list|)
expr_stmt|;
try|try
block|{
name|processed
operator|++
expr_stmt|;
name|getProcessor
argument_list|()
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|exchange
operator|.
name|getException
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|getExceptionHandler
argument_list|()
operator|.
name|handleException
argument_list|(
literal|"Error during processing exchange."
argument_list|,
name|exchange
argument_list|,
name|exchange
operator|.
name|getException
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
for|for
control|(
name|ApplicationState
name|change
range|:
name|changed
operator|.
name|keySet
argument_list|()
control|)
block|{
name|Exchange
name|exchange
init|=
name|getEndpoint
argument_list|()
operator|.
name|createExchange
argument_list|(
name|change
operator|.
name|getApplication
argument_list|()
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|OpenShiftConstants
operator|.
name|EVENT_TYPE
argument_list|,
literal|"changed"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|OpenShiftConstants
operator|.
name|EVENT_OLD_STATE
argument_list|,
name|change
operator|.
name|getOldState
argument_list|()
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|OpenShiftConstants
operator|.
name|EVENT_NEW_STATE
argument_list|,
name|change
operator|.
name|getState
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|processed
operator|++
expr_stmt|;
name|getProcessor
argument_list|()
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|exchange
operator|.
name|getException
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|getExceptionHandler
argument_list|()
operator|.
name|handleException
argument_list|(
literal|"Error during processing exchange."
argument_list|,
name|exchange
argument_list|,
name|exchange
operator|.
name|getException
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|// update old state with latest state for next poll
name|oldState
operator|.
name|clear
argument_list|()
expr_stmt|;
name|oldState
operator|.
name|putAll
argument_list|(
name|newState
argument_list|)
expr_stmt|;
name|initialPoll
operator|=
literal|false
expr_stmt|;
return|return
name|processed
return|;
block|}
DECL|class|ApplicationState
specifier|private
specifier|static
specifier|final
class|class
name|ApplicationState
block|{
DECL|field|uuid
specifier|private
specifier|final
name|String
name|uuid
decl_stmt|;
DECL|field|application
specifier|private
specifier|final
name|IApplication
name|application
decl_stmt|;
DECL|field|state
specifier|private
specifier|final
name|String
name|state
decl_stmt|;
DECL|field|oldState
specifier|private
name|String
name|oldState
decl_stmt|;
DECL|method|ApplicationState (String uuid, IApplication application, String state)
specifier|private
name|ApplicationState
parameter_list|(
name|String
name|uuid
parameter_list|,
name|IApplication
name|application
parameter_list|,
name|String
name|state
parameter_list|)
block|{
name|this
operator|.
name|uuid
operator|=
name|uuid
expr_stmt|;
name|this
operator|.
name|application
operator|=
name|application
expr_stmt|;
name|this
operator|.
name|state
operator|=
name|state
expr_stmt|;
block|}
DECL|method|getUuid ()
specifier|public
name|String
name|getUuid
parameter_list|()
block|{
return|return
name|uuid
return|;
block|}
DECL|method|getApplication ()
specifier|public
name|IApplication
name|getApplication
parameter_list|()
block|{
return|return
name|application
return|;
block|}
DECL|method|getState ()
specifier|public
name|String
name|getState
parameter_list|()
block|{
return|return
name|state
return|;
block|}
DECL|method|getOldState ()
specifier|public
name|String
name|getOldState
parameter_list|()
block|{
return|return
name|oldState
return|;
block|}
DECL|method|setOldState (String oldState)
specifier|public
name|void
name|setOldState
parameter_list|(
name|String
name|oldState
parameter_list|)
block|{
name|this
operator|.
name|oldState
operator|=
name|oldState
expr_stmt|;
block|}
comment|// only use uuid and state for equals as that is what we want to use for state change detection
annotation|@
name|Override
DECL|method|equals (Object o)
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
if|if
condition|(
name|this
operator|==
name|o
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
name|o
operator|==
literal|null
operator|||
name|getClass
argument_list|()
operator|!=
name|o
operator|.
name|getClass
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
name|ApplicationState
name|that
init|=
operator|(
name|ApplicationState
operator|)
name|o
decl_stmt|;
if|if
condition|(
operator|!
name|state
operator|.
name|equals
argument_list|(
name|that
operator|.
name|state
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
operator|!
name|uuid
operator|.
name|equals
argument_list|(
name|that
operator|.
name|uuid
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|hashCode ()
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
name|int
name|result
init|=
name|uuid
operator|.
name|hashCode
argument_list|()
decl_stmt|;
name|result
operator|=
literal|31
operator|*
name|result
operator|+
name|state
operator|.
name|hashCode
argument_list|()
expr_stmt|;
return|return
name|result
return|;
block|}
block|}
block|}
end_class

end_unit

