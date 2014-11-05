begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.timer
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|timer
package|;
end_package

begin_import
import|import
name|java
operator|.
name|text
operator|.
name|SimpleDateFormat
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
name|Date
import|;
end_import

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
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Timer
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
name|AtomicInteger
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
name|impl
operator|.
name|UriEndpointComponent
import|;
end_import

begin_comment
comment|/**  * Represents the component that manages {@link TimerEndpoint}.  It holds the  * list of {@link TimerConsumer} objects that are started.  *  * @version   */
end_comment

begin_class
DECL|class|TimerComponent
specifier|public
class|class
name|TimerComponent
extends|extends
name|UriEndpointComponent
block|{
DECL|field|timers
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Timer
argument_list|>
name|timers
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Timer
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|refCounts
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|AtomicInteger
argument_list|>
name|refCounts
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|AtomicInteger
argument_list|>
argument_list|()
decl_stmt|;
DECL|method|TimerComponent ()
specifier|public
name|TimerComponent
parameter_list|()
block|{
name|super
argument_list|(
name|TimerEndpoint
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
DECL|method|getTimer (TimerConsumer consumer)
specifier|public
name|Timer
name|getTimer
parameter_list|(
name|TimerConsumer
name|consumer
parameter_list|)
block|{
name|String
name|key
init|=
name|consumer
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getTimerName
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|consumer
operator|.
name|getEndpoint
argument_list|()
operator|.
name|isDaemon
argument_list|()
condition|)
block|{
name|key
operator|=
literal|"nonDaemon:"
operator|+
name|key
expr_stmt|;
block|}
name|Timer
name|answer
decl_stmt|;
synchronized|synchronized
init|(
name|timers
init|)
block|{
name|answer
operator|=
name|timers
operator|.
name|get
argument_list|(
name|key
argument_list|)
expr_stmt|;
if|if
condition|(
name|answer
operator|==
literal|null
condition|)
block|{
comment|// the timer name is also the thread name, so lets resolve a name to be used
name|String
name|name
init|=
name|consumer
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getExecutorServiceManager
argument_list|()
operator|.
name|resolveThreadName
argument_list|(
literal|"timer://"
operator|+
name|consumer
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getTimerName
argument_list|()
argument_list|)
decl_stmt|;
name|answer
operator|=
operator|new
name|Timer
argument_list|(
name|name
argument_list|,
name|consumer
operator|.
name|getEndpoint
argument_list|()
operator|.
name|isDaemon
argument_list|()
argument_list|)
expr_stmt|;
name|timers
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|answer
argument_list|)
expr_stmt|;
comment|// store new reference counter
name|refCounts
operator|.
name|put
argument_list|(
name|key
argument_list|,
operator|new
name|AtomicInteger
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// increase reference counter
name|AtomicInteger
name|counter
init|=
name|refCounts
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|counter
operator|!=
literal|null
condition|)
block|{
name|counter
operator|.
name|incrementAndGet
argument_list|()
expr_stmt|;
block|}
block|}
block|}
return|return
name|answer
return|;
block|}
DECL|method|removeTimer (TimerConsumer consumer)
specifier|public
name|void
name|removeTimer
parameter_list|(
name|TimerConsumer
name|consumer
parameter_list|)
block|{
name|String
name|key
init|=
name|consumer
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getTimerName
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|consumer
operator|.
name|getEndpoint
argument_list|()
operator|.
name|isDaemon
argument_list|()
condition|)
block|{
name|key
operator|=
literal|"nonDaemon:"
operator|+
name|key
expr_stmt|;
block|}
synchronized|synchronized
init|(
name|timers
init|)
block|{
comment|// decrease reference counter
name|AtomicInteger
name|counter
init|=
name|refCounts
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|counter
operator|!=
literal|null
operator|&&
name|counter
operator|.
name|decrementAndGet
argument_list|()
operator|<=
literal|0
condition|)
block|{
name|refCounts
operator|.
name|remove
argument_list|(
name|key
argument_list|)
expr_stmt|;
comment|// remove timer as its no longer in use
name|Timer
name|timer
init|=
name|timers
operator|.
name|remove
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|timer
operator|!=
literal|null
condition|)
block|{
name|timer
operator|.
name|cancel
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
annotation|@
name|Override
DECL|method|createEndpoint (String uri, String remaining, Map<String, Object> parameters)
specifier|protected
name|Endpoint
name|createEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|remaining
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
throws|throws
name|Exception
block|{
name|TimerEndpoint
name|answer
init|=
operator|new
name|TimerEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|,
name|remaining
argument_list|)
decl_stmt|;
comment|// convert time from String to a java.util.Date using the supported patterns
name|String
name|time
init|=
name|getAndRemoveParameter
argument_list|(
name|parameters
argument_list|,
literal|"time"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|String
name|pattern
init|=
name|getAndRemoveParameter
argument_list|(
name|parameters
argument_list|,
literal|"pattern"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|time
operator|!=
literal|null
condition|)
block|{
name|SimpleDateFormat
name|sdf
decl_stmt|;
if|if
condition|(
name|pattern
operator|!=
literal|null
condition|)
block|{
name|sdf
operator|=
operator|new
name|SimpleDateFormat
argument_list|(
name|pattern
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|time
operator|.
name|contains
argument_list|(
literal|"T"
argument_list|)
condition|)
block|{
name|sdf
operator|=
operator|new
name|SimpleDateFormat
argument_list|(
literal|"yyyy-MM-dd'T'HH:mm:ss"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|sdf
operator|=
operator|new
name|SimpleDateFormat
argument_list|(
literal|"yyyy-MM-dd HH:mm:ss"
argument_list|)
expr_stmt|;
block|}
name|Date
name|date
init|=
name|sdf
operator|.
name|parse
argument_list|(
name|time
argument_list|)
decl_stmt|;
name|answer
operator|.
name|setTime
argument_list|(
name|date
argument_list|)
expr_stmt|;
block|}
name|setProperties
argument_list|(
name|answer
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
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
name|Collection
argument_list|<
name|Timer
argument_list|>
name|collection
init|=
name|timers
operator|.
name|values
argument_list|()
decl_stmt|;
for|for
control|(
name|Timer
name|timer
range|:
name|collection
control|)
block|{
name|timer
operator|.
name|cancel
argument_list|()
expr_stmt|;
block|}
name|timers
operator|.
name|clear
argument_list|()
expr_stmt|;
name|refCounts
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

