begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|ArrayList
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
name|TimeoutMap
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
name|service
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

begin_import
import|import static
name|java
operator|.
name|util
operator|.
name|Comparator
operator|.
name|comparing
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|TimeoutMap
operator|.
name|Listener
operator|.
name|Type
operator|.
name|Evict
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|TimeoutMap
operator|.
name|Listener
operator|.
name|Type
operator|.
name|Put
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|TimeoutMap
operator|.
name|Listener
operator|.
name|Type
operator|.
name|Remove
import|;
end_import

begin_comment
comment|/**  * Default implementation of the {@link TimeoutMap}.  *<p/>  * This implementation supports thread safe and non thread safe, in the manner you can enable locking or not.  * By default locking is enabled and thus we are thread safe.  *<p/>  * You must provide a {@link java.util.concurrent.ScheduledExecutorService} in the constructor which is used  * to schedule a background task which check for old entries to purge. This implementation will shutdown the scheduler  * if its being stopped.  * You must also invoke {@link #start()} to startup the timeout map, before its ready to be used.  * And you must invoke {@link #stop()} to stop the map when no longer in use.  */
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
extends|extends
name|ServiceSupport
implements|implements
name|TimeoutMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
block|{
DECL|field|log
specifier|protected
specifier|final
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
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
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|executor
specifier|private
specifier|final
name|ScheduledExecutorService
name|executor
decl_stmt|;
DECL|field|future
specifier|private
specifier|volatile
name|ScheduledFuture
argument_list|<
name|?
argument_list|>
name|future
decl_stmt|;
DECL|field|purgePollTime
specifier|private
specifier|final
name|long
name|purgePollTime
decl_stmt|;
DECL|field|lock
specifier|private
specifier|final
name|Lock
name|lock
decl_stmt|;
DECL|field|listeners
specifier|private
specifier|final
name|List
argument_list|<
name|Listener
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|listeners
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
literal|2
argument_list|)
decl_stmt|;
DECL|method|DefaultTimeoutMap (ScheduledExecutorService executor)
specifier|public
name|DefaultTimeoutMap
parameter_list|(
name|ScheduledExecutorService
name|executor
parameter_list|)
block|{
name|this
argument_list|(
name|executor
argument_list|,
literal|1000
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
argument_list|(
name|executor
argument_list|,
name|requestMapPollTimeMillis
argument_list|,
name|useLock
condition|?
operator|new
name|ReentrantLock
argument_list|()
else|:
name|NoLock
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
block|}
DECL|method|DefaultTimeoutMap (ScheduledExecutorService executor, long requestMapPollTimeMillis, Lock lock)
specifier|public
name|DefaultTimeoutMap
parameter_list|(
name|ScheduledExecutorService
name|executor
parameter_list|,
name|long
name|requestMapPollTimeMillis
parameter_list|,
name|Lock
name|lock
parameter_list|)
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|executor
argument_list|,
literal|"ScheduledExecutorService"
argument_list|)
expr_stmt|;
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
name|lock
operator|=
name|lock
expr_stmt|;
block|}
annotation|@
name|Override
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
name|lock
operator|.
name|lock
argument_list|()
expr_stmt|;
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
name|lock
operator|.
name|unlock
argument_list|()
expr_stmt|;
block|}
return|return
name|entry
operator|.
name|getValue
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|put (K key, V value, long timeoutMillis)
specifier|public
name|V
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
argument_list|<>
argument_list|(
name|key
argument_list|,
name|value
argument_list|,
name|timeoutMillis
argument_list|)
decl_stmt|;
name|lock
operator|.
name|lock
argument_list|()
expr_stmt|;
try|try
block|{
name|updateExpireTime
argument_list|(
name|entry
argument_list|)
expr_stmt|;
name|TimeoutMapEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|result
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
return|return
name|unwrap
argument_list|(
name|result
argument_list|)
return|;
block|}
finally|finally
block|{
name|lock
operator|.
name|unlock
argument_list|()
expr_stmt|;
name|emitEvent
argument_list|(
name|Put
argument_list|,
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|putIfAbsent (K key, V value, long timeoutMillis)
specifier|public
name|V
name|putIfAbsent
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
argument_list|<>
argument_list|(
name|key
argument_list|,
name|value
argument_list|,
name|timeoutMillis
argument_list|)
decl_stmt|;
name|TimeoutMapEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|result
init|=
literal|null
decl_stmt|;
name|lock
operator|.
name|lock
argument_list|()
expr_stmt|;
try|try
block|{
name|updateExpireTime
argument_list|(
name|entry
argument_list|)
expr_stmt|;
comment|//Just make sure we don't override the old entry
name|result
operator|=
name|map
operator|.
name|putIfAbsent
argument_list|(
name|key
argument_list|,
name|entry
argument_list|)
expr_stmt|;
return|return
name|unwrap
argument_list|(
name|result
argument_list|)
return|;
block|}
finally|finally
block|{
name|lock
operator|.
name|unlock
argument_list|()
expr_stmt|;
if|if
condition|(
name|result
operator|!=
name|entry
condition|)
block|{
name|emitEvent
argument_list|(
name|Put
argument_list|,
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
comment|// conditional on map being changed
block|}
block|}
block|}
annotation|@
name|Override
DECL|method|remove (K key)
specifier|public
name|V
name|remove
parameter_list|(
name|K
name|key
parameter_list|)
block|{
name|V
name|value
init|=
literal|null
decl_stmt|;
name|lock
operator|.
name|lock
argument_list|()
expr_stmt|;
try|try
block|{
name|value
operator|=
name|unwrap
argument_list|(
name|map
operator|.
name|remove
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|value
return|;
block|}
finally|finally
block|{
name|lock
operator|.
name|unlock
argument_list|()
expr_stmt|;
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
name|emitEvent
argument_list|(
name|Remove
argument_list|,
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
comment|// conditional on map being changed
block|}
block|}
block|}
annotation|@
name|Override
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
DECL|method|purgeTask ()
specifier|private
name|void
name|purgeTask
parameter_list|()
block|{
comment|// only purge if allowed
if|if
condition|(
operator|!
name|isRunAllowed
argument_list|()
condition|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Purge task not allowed to run"
argument_list|)
expr_stmt|;
return|return;
block|}
name|log
operator|.
name|trace
argument_list|(
literal|"Running purge task to see if any entries have been timed out"
argument_list|)
expr_stmt|;
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
comment|// must catch and log exception otherwise the executor will now schedule next purgeTask
name|log
operator|.
name|warn
argument_list|(
literal|"Exception occurred during purge task. This exception will be ignored."
argument_list|,
name|t
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|purge ()
specifier|protected
name|void
name|purge
parameter_list|()
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"There are {} in the timeout map"
argument_list|,
name|map
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|map
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return;
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
argument_list|<>
argument_list|(
name|map
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
name|lock
operator|.
name|lock
argument_list|()
expr_stmt|;
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
name|log
operator|.
name|debug
argument_list|(
literal|"Evicting inactive entry ID: {}"
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
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
name|expired
operator|.
name|sort
argument_list|(
name|comparing
argument_list|(
name|TimeoutMapEntry
operator|::
name|getExpireTime
argument_list|)
argument_list|)
expr_stmt|;
comment|// and must remove from list after we have fired the notifications
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
block|}
block|}
block|}
finally|finally
block|{
name|lock
operator|.
name|unlock
argument_list|()
expr_stmt|;
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
name|emitEvent
argument_list|(
name|Evict
argument_list|,
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
DECL|method|unwrap (TimeoutMapEntry<K, V> entry)
specifier|private
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|V
name|unwrap
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
name|entry
operator|==
literal|null
condition|?
literal|null
else|:
name|entry
operator|.
name|getValue
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|addListener (Listener<K, V> listener)
specifier|public
name|void
name|addListener
parameter_list|(
name|Listener
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|listener
parameter_list|)
block|{
name|this
operator|.
name|listeners
operator|.
name|add
argument_list|(
name|listener
argument_list|)
expr_stmt|;
block|}
DECL|method|emitEvent (Listener.Type type, K key, V value)
specifier|private
name|void
name|emitEvent
parameter_list|(
name|Listener
operator|.
name|Type
name|type
parameter_list|,
name|K
name|key
parameter_list|,
name|V
name|value
parameter_list|)
block|{
for|for
control|(
name|Listener
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|listener
range|:
name|listeners
control|)
block|{
try|try
block|{
name|listener
operator|.
name|timeoutMapEvent
argument_list|(
name|type
argument_list|,
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
comment|// Ignore
block|}
block|}
block|}
comment|/**      * lets schedule each time to allow folks to change the time at runtime      */
DECL|method|schedulePoll ()
specifier|protected
name|void
name|schedulePoll
parameter_list|()
block|{
name|future
operator|=
name|executor
operator|.
name|scheduleWithFixedDelay
argument_list|(
name|this
operator|::
name|purgeTask
argument_list|,
literal|0
argument_list|,
name|purgePollTime
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
expr_stmt|;
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
DECL|method|updateExpireTime (TimeoutMapEntry<K, V> entry)
specifier|protected
name|void
name|updateExpireTime
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
if|if
condition|(
name|executor
operator|.
name|isShutdown
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"The ScheduledExecutorService is shutdown"
argument_list|)
throw|;
block|}
name|schedulePoll
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
if|if
condition|(
name|future
operator|!=
literal|null
condition|)
block|{
name|future
operator|.
name|cancel
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|future
operator|=
literal|null
expr_stmt|;
block|}
comment|// clear map if we stop
name|map
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

