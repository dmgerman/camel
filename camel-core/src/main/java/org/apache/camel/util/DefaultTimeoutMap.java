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
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
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
name|Set
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|SortedSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|TreeSet
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
name|TimeUnit
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
comment|/**  * Default implementation of the {@link TimeoutMap}.  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|DefaultTimeoutMap
specifier|public
class|class
name|DefaultTimeoutMap
implements|implements
name|TimeoutMap
implements|,
name|Runnable
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|DefaultTimeoutMap
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|map
specifier|private
name|Map
name|map
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
DECL|field|index
specifier|private
name|SortedSet
name|index
init|=
operator|new
name|TreeSet
argument_list|()
decl_stmt|;
DECL|field|executor
specifier|private
name|ScheduledExecutorService
name|executor
decl_stmt|;
DECL|field|purgePollTime
specifier|private
name|long
name|purgePollTime
decl_stmt|;
DECL|method|DefaultTimeoutMap ()
specifier|public
name|DefaultTimeoutMap
parameter_list|()
block|{
name|this
argument_list|(
literal|null
argument_list|,
literal|1000L
argument_list|)
expr_stmt|;
block|}
DECL|method|DefaultTimeoutMap (ScheduledExecutorService executor, long requestMapPollTimeMillis)
specifier|public
name|DefaultTimeoutMap
parameter_list|(
name|ScheduledExecutorService
name|executor
parameter_list|,
name|long
name|requestMapPollTimeMillis
parameter_list|)
block|{
name|this
operator|.
name|executor
operator|=
name|executor
expr_stmt|;
name|this
operator|.
name|purgePollTime
operator|=
name|requestMapPollTimeMillis
expr_stmt|;
name|schedulePoll
argument_list|()
expr_stmt|;
block|}
DECL|method|get (Object key)
specifier|public
name|Object
name|get
parameter_list|(
name|Object
name|key
parameter_list|)
block|{
name|TimeoutMapEntry
name|entry
init|=
literal|null
decl_stmt|;
synchronized|synchronized
init|(
name|map
init|)
block|{
name|entry
operator|=
operator|(
name|TimeoutMapEntry
operator|)
name|map
operator|.
name|get
argument_list|(
name|key
argument_list|)
expr_stmt|;
if|if
condition|(
name|entry
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|index
operator|.
name|remove
argument_list|(
name|entry
argument_list|)
expr_stmt|;
name|updateExpireTime
argument_list|(
name|entry
argument_list|)
expr_stmt|;
name|index
operator|.
name|add
argument_list|(
name|entry
argument_list|)
expr_stmt|;
block|}
return|return
name|entry
operator|.
name|getValue
argument_list|()
return|;
block|}
DECL|method|put (Object key, Object value, long timeoutMillis)
specifier|public
name|void
name|put
parameter_list|(
name|Object
name|key
parameter_list|,
name|Object
name|value
parameter_list|,
name|long
name|timeoutMillis
parameter_list|)
block|{
name|TimeoutMapEntry
name|entry
init|=
operator|new
name|TimeoutMapEntry
argument_list|(
name|key
argument_list|,
name|value
argument_list|,
name|timeoutMillis
argument_list|)
decl_stmt|;
synchronized|synchronized
init|(
name|map
init|)
block|{
name|Object
name|oldValue
init|=
name|map
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|entry
argument_list|)
decl_stmt|;
if|if
condition|(
name|oldValue
operator|!=
literal|null
condition|)
block|{
name|index
operator|.
name|remove
argument_list|(
name|oldValue
argument_list|)
expr_stmt|;
block|}
name|updateExpireTime
argument_list|(
name|entry
argument_list|)
expr_stmt|;
name|index
operator|.
name|add
argument_list|(
name|entry
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|remove (Object id)
specifier|public
name|void
name|remove
parameter_list|(
name|Object
name|id
parameter_list|)
block|{
synchronized|synchronized
init|(
name|map
init|)
block|{
name|TimeoutMapEntry
name|entry
init|=
operator|(
name|TimeoutMapEntry
operator|)
name|map
operator|.
name|remove
argument_list|(
name|id
argument_list|)
decl_stmt|;
if|if
condition|(
name|entry
operator|!=
literal|null
condition|)
block|{
name|index
operator|.
name|remove
argument_list|(
name|entry
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|getKeys ()
specifier|public
name|Object
index|[]
name|getKeys
parameter_list|()
block|{
name|Object
index|[]
name|keys
init|=
literal|null
decl_stmt|;
synchronized|synchronized
init|(
name|map
init|)
block|{
name|Set
name|keySet
init|=
name|map
operator|.
name|keySet
argument_list|()
decl_stmt|;
name|keys
operator|=
operator|new
name|Object
index|[
name|keySet
operator|.
name|size
argument_list|()
index|]
expr_stmt|;
name|keySet
operator|.
name|toArray
argument_list|(
name|keys
argument_list|)
expr_stmt|;
block|}
return|return
name|keys
return|;
block|}
DECL|method|size ()
specifier|public
name|int
name|size
parameter_list|()
block|{
synchronized|synchronized
init|(
name|map
init|)
block|{
return|return
name|map
operator|.
name|size
argument_list|()
return|;
block|}
block|}
comment|/**      * The timer task which purges old requests and schedules another poll      */
DECL|method|run ()
specifier|public
name|void
name|run
parameter_list|()
block|{
name|purge
argument_list|()
expr_stmt|;
name|schedulePoll
argument_list|()
expr_stmt|;
block|}
DECL|method|purge ()
specifier|public
name|void
name|purge
parameter_list|()
block|{
name|long
name|now
init|=
name|currentTime
argument_list|()
decl_stmt|;
synchronized|synchronized
init|(
name|map
init|)
block|{
for|for
control|(
name|Iterator
name|iter
init|=
name|index
operator|.
name|iterator
argument_list|()
init|;
name|iter
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|TimeoutMapEntry
name|entry
init|=
operator|(
name|TimeoutMapEntry
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|entry
operator|==
literal|null
condition|)
block|{
break|break;
block|}
if|if
condition|(
name|entry
operator|.
name|getExpireTime
argument_list|()
operator|<
name|now
condition|)
block|{
if|if
condition|(
name|isValidForEviction
argument_list|(
name|entry
argument_list|)
condition|)
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
literal|"Evicting inactive request for correlationID: "
operator|+
name|entry
argument_list|)
expr_stmt|;
block|}
name|map
operator|.
name|remove
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
name|iter
operator|.
name|remove
argument_list|()
expr_stmt|;
block|}
block|}
else|else
block|{
break|break;
block|}
block|}
block|}
block|}
comment|// Properties
comment|// -------------------------------------------------------------------------
DECL|method|getPurgePollTime ()
specifier|public
name|long
name|getPurgePollTime
parameter_list|()
block|{
return|return
name|purgePollTime
return|;
block|}
comment|/**      * Sets the next purge poll time in milliseconds      */
DECL|method|setPurgePollTime (long purgePollTime)
specifier|public
name|void
name|setPurgePollTime
parameter_list|(
name|long
name|purgePollTime
parameter_list|)
block|{
name|this
operator|.
name|purgePollTime
operator|=
name|purgePollTime
expr_stmt|;
block|}
DECL|method|getExecutor ()
specifier|public
name|ScheduledExecutorService
name|getExecutor
parameter_list|()
block|{
return|return
name|executor
return|;
block|}
comment|/**      * Sets the executor used to schedule purge events of inactive requests      */
DECL|method|setExecutor (ScheduledExecutorService executor)
specifier|public
name|void
name|setExecutor
parameter_list|(
name|ScheduledExecutorService
name|executor
parameter_list|)
block|{
name|this
operator|.
name|executor
operator|=
name|executor
expr_stmt|;
block|}
comment|// Implementation methods
comment|// -------------------------------------------------------------------------
comment|/**      * lets schedule each time to allow folks to change the time at runtime      */
DECL|method|schedulePoll ()
specifier|protected
name|void
name|schedulePoll
parameter_list|()
block|{
if|if
condition|(
name|executor
operator|!=
literal|null
condition|)
block|{
name|executor
operator|.
name|schedule
argument_list|(
name|this
argument_list|,
name|purgePollTime
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * A hook to allow derivations to avoid evicting the current entry      */
DECL|method|isValidForEviction (TimeoutMapEntry entry)
specifier|protected
name|boolean
name|isValidForEviction
parameter_list|(
name|TimeoutMapEntry
name|entry
parameter_list|)
block|{
return|return
literal|true
return|;
block|}
DECL|method|updateExpireTime (TimeoutMapEntry entry)
specifier|protected
name|void
name|updateExpireTime
parameter_list|(
name|TimeoutMapEntry
name|entry
parameter_list|)
block|{
name|long
name|now
init|=
name|currentTime
argument_list|()
decl_stmt|;
name|entry
operator|.
name|setExpireTime
argument_list|(
name|entry
operator|.
name|getTimeout
argument_list|()
operator|+
name|now
argument_list|)
expr_stmt|;
block|}
DECL|method|currentTime ()
specifier|protected
name|long
name|currentTime
parameter_list|()
block|{
return|return
name|System
operator|.
name|currentTimeMillis
argument_list|()
return|;
block|}
block|}
end_class

end_unit

