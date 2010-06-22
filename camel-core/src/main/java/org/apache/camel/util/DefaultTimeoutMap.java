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
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Comparator
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
name|ConcurrentHashMap
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
name|ConcurrentMap
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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|locks
operator|.
name|Lock
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
name|locks
operator|.
name|ReentrantLock
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
comment|/**  * Default implementation of the {@link TimeoutMap}.  *<p/>  * This implementation supports thread safe and non thread safe, in the manner you can enable locking or not.  * By default locking is enabled and thus we are thread safe.  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|DefaultTimeoutMap
specifier|public
class|class
name|DefaultTimeoutMap
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
implements|implements
name|TimeoutMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
implements|,
name|Runnable
implements|,
name|Service
block|{
DECL|field|log
specifier|protected
specifier|final
specifier|transient
name|Log
name|log
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
DECL|field|map
specifier|private
specifier|final
name|ConcurrentMap
argument_list|<
name|K
argument_list|,
name|TimeoutMapEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|map
init|=
operator|new
name|ConcurrentHashMap
argument_list|<
name|K
argument_list|,
name|TimeoutMapEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|executor
specifier|private
specifier|final
name|ScheduledExecutorService
name|executor
decl_stmt|;
DECL|field|purgePollTime
specifier|private
specifier|final
name|long
name|purgePollTime
decl_stmt|;
DECL|field|initialDelay
specifier|private
specifier|final
name|long
name|initialDelay
init|=
literal|1000L
decl_stmt|;
DECL|field|lock
specifier|private
specifier|final
name|Lock
name|lock
init|=
operator|new
name|ReentrantLock
argument_list|()
decl_stmt|;
DECL|field|useLock
specifier|private
name|boolean
name|useLock
init|=
literal|true
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
DECL|method|DefaultTimeoutMap (boolean useLock)
specifier|public
name|DefaultTimeoutMap
parameter_list|(
name|boolean
name|useLock
parameter_list|)
block|{
name|this
argument_list|(
literal|null
argument_list|,
literal|1000L
argument_list|,
name|useLock
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
argument_list|(
name|executor
argument_list|,
name|requestMapPollTimeMillis
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
DECL|method|DefaultTimeoutMap (ScheduledExecutorService executor, long requestMapPollTimeMillis, boolean useLock)
specifier|public
name|DefaultTimeoutMap
parameter_list|(
name|ScheduledExecutorService
name|executor
parameter_list|,
name|long
name|requestMapPollTimeMillis
parameter_list|,
name|boolean
name|useLock
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
name|this
operator|.
name|useLock
operator|=
name|useLock
expr_stmt|;
name|schedulePoll
argument_list|()
expr_stmt|;
block|}
DECL|method|get (K key)
specifier|public
name|V
name|get
parameter_list|(
name|K
name|key
parameter_list|)
block|{
name|TimeoutMapEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|entry
decl_stmt|;
if|if
condition|(
name|useLock
condition|)
block|{
name|lock
operator|.
name|lock
argument_list|()
expr_stmt|;
block|}
try|try
block|{
name|entry
operator|=
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
name|updateExpireTime
argument_list|(
name|entry
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|useLock
condition|)
block|{
name|lock
operator|.
name|unlock
argument_list|()
expr_stmt|;
block|}
block|}
return|return
name|entry
operator|.
name|getValue
argument_list|()
return|;
block|}
DECL|method|put (K key, V value, long timeoutMillis)
specifier|public
name|void
name|put
parameter_list|(
name|K
name|key
parameter_list|,
name|V
name|value
parameter_list|,
name|long
name|timeoutMillis
parameter_list|)
block|{
name|TimeoutMapEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|entry
init|=
operator|new
name|TimeoutMapEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|(
name|key
argument_list|,
name|value
argument_list|,
name|timeoutMillis
argument_list|)
decl_stmt|;
if|if
condition|(
name|useLock
condition|)
block|{
name|lock
operator|.
name|lock
argument_list|()
expr_stmt|;
block|}
try|try
block|{
name|map
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|entry
argument_list|)
expr_stmt|;
name|updateExpireTime
argument_list|(
name|entry
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|useLock
condition|)
block|{
name|lock
operator|.
name|unlock
argument_list|()
expr_stmt|;
block|}
block|}
block|}
DECL|method|remove (K id)
specifier|public
name|void
name|remove
parameter_list|(
name|K
name|id
parameter_list|)
block|{
if|if
condition|(
name|useLock
condition|)
block|{
name|lock
operator|.
name|lock
argument_list|()
expr_stmt|;
block|}
try|try
block|{
name|map
operator|.
name|remove
argument_list|(
name|id
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|useLock
condition|)
block|{
name|lock
operator|.
name|unlock
argument_list|()
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
decl_stmt|;
if|if
condition|(
name|useLock
condition|)
block|{
name|lock
operator|.
name|lock
argument_list|()
expr_stmt|;
block|}
try|try
block|{
name|Set
argument_list|<
name|K
argument_list|>
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
finally|finally
block|{
if|if
condition|(
name|useLock
condition|)
block|{
name|lock
operator|.
name|unlock
argument_list|()
expr_stmt|;
block|}
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
return|return
name|map
operator|.
name|size
argument_list|()
return|;
block|}
comment|/**      * The timer task which purges old requests and schedules another poll      */
DECL|method|run ()
specifier|public
name|void
name|run
parameter_list|()
block|{
if|if
condition|(
name|log
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Running purge task to see if any entries has been timed out"
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|purge
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
comment|// must catch and log exception otherwise the executor will now schedule next run
name|log
operator|.
name|error
argument_list|(
literal|"Exception occurred during purge task"
argument_list|,
name|t
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|purge ()
specifier|public
name|void
name|purge
parameter_list|()
block|{
if|if
condition|(
name|log
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"There are "
operator|+
name|map
operator|.
name|size
argument_list|()
operator|+
literal|" in the timeout map"
argument_list|)
expr_stmt|;
block|}
name|long
name|now
init|=
name|currentTime
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|TimeoutMapEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|expired
init|=
operator|new
name|ArrayList
argument_list|<
name|TimeoutMapEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
if|if
condition|(
name|useLock
condition|)
block|{
name|lock
operator|.
name|lock
argument_list|()
expr_stmt|;
block|}
try|try
block|{
comment|// need to find the expired entries and add to the expired list
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|K
argument_list|,
name|TimeoutMapEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|entry
range|:
name|map
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
name|entry
operator|.
name|getValue
argument_list|()
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
operator|.
name|getValue
argument_list|()
argument_list|)
condition|)
block|{
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
literal|"Evicting inactive request for correlationID: "
operator|+
name|entry
argument_list|)
expr_stmt|;
block|}
name|expired
operator|.
name|add
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|// if we found any expired then we need to sort, onEviction and remove
if|if
condition|(
operator|!
name|expired
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
comment|// sort according to the expired time so we got the first expired first
name|Collections
operator|.
name|sort
argument_list|(
name|expired
argument_list|,
operator|new
name|Comparator
argument_list|<
name|TimeoutMapEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
argument_list|()
block|{
specifier|public
name|int
name|compare
parameter_list|(
name|TimeoutMapEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|a
parameter_list|,
name|TimeoutMapEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|b
parameter_list|)
block|{
name|long
name|diff
init|=
name|a
operator|.
name|getExpireTime
argument_list|()
operator|-
name|b
operator|.
name|getExpireTime
argument_list|()
decl_stmt|;
if|if
condition|(
name|diff
operator|==
literal|0
condition|)
block|{
return|return
literal|0
return|;
block|}
return|return
name|diff
operator|>
literal|0
condition|?
literal|1
else|:
operator|-
literal|1
return|;
block|}
block|}
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|K
argument_list|>
name|evicts
init|=
operator|new
name|ArrayList
argument_list|<
name|K
argument_list|>
argument_list|(
name|expired
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
try|try
block|{
comment|// now fire eviction notification
for|for
control|(
name|TimeoutMapEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|entry
range|:
name|expired
control|)
block|{
name|boolean
name|evict
init|=
name|onEviction
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|evict
condition|)
block|{
comment|// okay this entry should be evicted
name|evicts
operator|.
name|add
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
finally|finally
block|{
comment|// and must remove from list after we have fired the notifications
for|for
control|(
name|K
name|key
range|:
name|evicts
control|)
block|{
name|map
operator|.
name|remove
argument_list|(
name|key
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
finally|finally
block|{
if|if
condition|(
name|useLock
condition|)
block|{
name|lock
operator|.
name|unlock
argument_list|()
expr_stmt|;
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
name|scheduleWithFixedDelay
argument_list|(
name|this
argument_list|,
name|initialDelay
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
DECL|method|isValidForEviction (TimeoutMapEntry<K, V> entry)
specifier|protected
name|boolean
name|isValidForEviction
parameter_list|(
name|TimeoutMapEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|entry
parameter_list|)
block|{
return|return
literal|true
return|;
block|}
DECL|method|onEviction (K key, V value)
specifier|public
name|boolean
name|onEviction
parameter_list|(
name|K
name|key
parameter_list|,
name|V
name|value
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
DECL|method|start ()
specifier|public
name|void
name|start
parameter_list|()
throws|throws
name|Exception
block|{     }
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
name|executor
operator|!=
literal|null
condition|)
block|{
name|executor
operator|.
name|shutdown
argument_list|()
expr_stmt|;
block|}
name|map
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

