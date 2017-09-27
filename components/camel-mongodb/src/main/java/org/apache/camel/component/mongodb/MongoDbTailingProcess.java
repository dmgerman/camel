begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.mongodb
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|mongodb
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|CountDownLatch
import|;
end_import

begin_import
import|import
name|com
operator|.
name|mongodb
operator|.
name|BasicDBObject
import|;
end_import

begin_import
import|import
name|com
operator|.
name|mongodb
operator|.
name|CursorType
import|;
end_import

begin_import
import|import
name|com
operator|.
name|mongodb
operator|.
name|DBObject
import|;
end_import

begin_import
import|import
name|com
operator|.
name|mongodb
operator|.
name|MongoCursorNotFoundException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|mongodb
operator|.
name|client
operator|.
name|MongoCollection
import|;
end_import

begin_import
import|import
name|com
operator|.
name|mongodb
operator|.
name|client
operator|.
name|MongoCursor
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

begin_class
DECL|class|MongoDbTailingProcess
specifier|public
class|class
name|MongoDbTailingProcess
implements|implements
name|Runnable
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
name|MongoDbTailingProcess
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|CAPPED_KEY
specifier|private
specifier|static
specifier|final
name|String
name|CAPPED_KEY
init|=
literal|"capped"
decl_stmt|;
DECL|field|keepRunning
specifier|public
specifier|volatile
name|boolean
name|keepRunning
init|=
literal|true
decl_stmt|;
DECL|field|stopped
specifier|public
specifier|volatile
name|boolean
name|stopped
decl_stmt|;
comment|// = false
DECL|field|stoppedLatch
specifier|private
specifier|volatile
name|CountDownLatch
name|stoppedLatch
decl_stmt|;
DECL|field|dbCol
specifier|private
specifier|final
name|MongoCollection
argument_list|<
name|BasicDBObject
argument_list|>
name|dbCol
decl_stmt|;
DECL|field|endpoint
specifier|private
specifier|final
name|MongoDbEndpoint
name|endpoint
decl_stmt|;
DECL|field|consumer
specifier|private
specifier|final
name|MongoDbTailableCursorConsumer
name|consumer
decl_stmt|;
comment|// create local, final copies of these variables for increased performance
DECL|field|cursorRegenerationDelay
specifier|private
specifier|final
name|long
name|cursorRegenerationDelay
decl_stmt|;
DECL|field|cursorRegenerationDelayEnabled
specifier|private
specifier|final
name|boolean
name|cursorRegenerationDelayEnabled
decl_stmt|;
DECL|field|cursor
specifier|private
name|MongoCursor
argument_list|<
name|BasicDBObject
argument_list|>
name|cursor
decl_stmt|;
DECL|field|tailTracking
specifier|private
name|MongoDbTailTrackingManager
name|tailTracking
decl_stmt|;
DECL|method|MongoDbTailingProcess (MongoDbEndpoint endpoint, MongoDbTailableCursorConsumer consumer, MongoDbTailTrackingManager tailTrack)
specifier|public
name|MongoDbTailingProcess
parameter_list|(
name|MongoDbEndpoint
name|endpoint
parameter_list|,
name|MongoDbTailableCursorConsumer
name|consumer
parameter_list|,
name|MongoDbTailTrackingManager
name|tailTrack
parameter_list|)
block|{
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
name|this
operator|.
name|consumer
operator|=
name|consumer
expr_stmt|;
name|this
operator|.
name|dbCol
operator|=
name|endpoint
operator|.
name|getMongoCollection
argument_list|()
expr_stmt|;
name|this
operator|.
name|tailTracking
operator|=
name|tailTrack
expr_stmt|;
name|this
operator|.
name|cursorRegenerationDelay
operator|=
name|endpoint
operator|.
name|getCursorRegenerationDelay
argument_list|()
expr_stmt|;
name|this
operator|.
name|cursorRegenerationDelayEnabled
operator|=
operator|!
operator|(
name|this
operator|.
name|cursorRegenerationDelay
operator|==
literal|0
operator|)
expr_stmt|;
block|}
DECL|method|getCursor ()
specifier|public
name|MongoCursor
argument_list|<
name|BasicDBObject
argument_list|>
name|getCursor
parameter_list|()
block|{
return|return
name|cursor
return|;
block|}
comment|/**      * Initialise the tailing process, the cursor and if persistent tail tracking is enabled, recover the cursor from the persisted point.      * As part of the initialisation process, the component will validate that the collection we are targeting is 'capped'.      *      * @throws Exception      */
DECL|method|initializeProcess ()
specifier|public
name|void
name|initializeProcess
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|LOG
operator|.
name|isInfoEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Starting MongoDB Tailable Cursor consumer, binding to collection: {}"
argument_list|,
literal|"db: "
operator|+
name|endpoint
operator|.
name|getMongoDatabase
argument_list|()
operator|+
literal|", col: "
operator|+
name|endpoint
operator|.
name|getCollection
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|isCollectionCapped
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|CamelMongoDbException
argument_list|(
literal|"Tailable cursors are only compatible with capped collections, and collection "
operator|+
name|endpoint
operator|.
name|getCollection
argument_list|()
operator|+
literal|" is not capped"
argument_list|)
throw|;
block|}
try|try
block|{
comment|// recover the last value from the store if it exists
name|tailTracking
operator|.
name|recoverFromStore
argument_list|()
expr_stmt|;
name|cursor
operator|=
name|initializeCursor
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|CamelMongoDbException
argument_list|(
literal|"Exception occurred while initializing tailable cursor"
argument_list|,
name|e
argument_list|)
throw|;
block|}
if|if
condition|(
name|cursor
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CamelMongoDbException
argument_list|(
literal|"Tailable cursor was not initialized, or cursor returned is dead on arrival"
argument_list|)
throw|;
block|}
block|}
DECL|method|isCollectionCapped ()
specifier|private
name|Boolean
name|isCollectionCapped
parameter_list|()
block|{
comment|// A non-capped collection does not return a "capped" key/value, so we have to deal with null here
name|Boolean
name|result
init|=
name|endpoint
operator|.
name|getMongoDatabase
argument_list|()
operator|.
name|runCommand
argument_list|(
name|createCollStatsCommand
argument_list|()
argument_list|)
operator|.
name|getBoolean
argument_list|(
name|CAPPED_KEY
argument_list|)
decl_stmt|;
return|return
name|result
operator|!=
literal|null
condition|?
name|result
else|:
literal|false
return|;
block|}
DECL|method|createCollStatsCommand ()
specifier|private
name|BasicDBObject
name|createCollStatsCommand
parameter_list|()
block|{
return|return
operator|new
name|BasicDBObject
argument_list|(
literal|"collStats"
argument_list|,
name|endpoint
operator|.
name|getCollection
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * The heart of the tailing process.      */
annotation|@
name|Override
DECL|method|run ()
specifier|public
name|void
name|run
parameter_list|()
block|{
name|stoppedLatch
operator|=
operator|new
name|CountDownLatch
argument_list|(
literal|1
argument_list|)
expr_stmt|;
while|while
condition|(
name|keepRunning
condition|)
block|{
name|doRun
argument_list|()
expr_stmt|;
comment|// if the previous call didn't return because we have stopped running, then regenerate the cursor
if|if
condition|(
name|keepRunning
condition|)
block|{
name|cursor
operator|.
name|close
argument_list|()
expr_stmt|;
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
literal|"Regenerating cursor with lastVal: {}, waiting {}ms first"
argument_list|,
name|tailTracking
operator|.
name|lastVal
argument_list|,
name|cursorRegenerationDelay
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|cursorRegenerationDelayEnabled
condition|)
block|{
try|try
block|{
name|Thread
operator|.
name|sleep
argument_list|(
name|cursorRegenerationDelay
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
comment|// ignore
block|}
block|}
name|cursor
operator|=
name|initializeCursor
argument_list|()
expr_stmt|;
block|}
block|}
name|stopped
operator|=
literal|true
expr_stmt|;
name|stoppedLatch
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
DECL|method|stop ()
specifier|protected
name|void
name|stop
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|LOG
operator|.
name|isInfoEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Stopping MongoDB Tailable Cursor consumer, bound to collection: {}"
argument_list|,
literal|"db: "
operator|+
name|endpoint
operator|.
name|getDatabase
argument_list|()
operator|+
literal|", col: "
operator|+
name|endpoint
operator|.
name|getCollection
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|keepRunning
operator|=
literal|false
expr_stmt|;
comment|// close the cursor if it's open, so if it is blocked on hasNext() it will return immediately
if|if
condition|(
name|cursor
operator|!=
literal|null
condition|)
block|{
name|cursor
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
name|awaitStopped
argument_list|()
expr_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isInfoEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Stopped MongoDB Tailable Cursor consumer, bound to collection: {}"
argument_list|,
literal|"db: "
operator|+
name|endpoint
operator|.
name|getDatabase
argument_list|()
operator|+
literal|", col: "
operator|+
name|endpoint
operator|.
name|getCollection
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * The heart of the tailing process.      */
DECL|method|doRun ()
specifier|private
name|void
name|doRun
parameter_list|()
block|{
name|int
name|counter
init|=
literal|0
decl_stmt|;
name|int
name|persistRecords
init|=
name|endpoint
operator|.
name|getPersistRecords
argument_list|()
decl_stmt|;
name|boolean
name|persistRegularly
init|=
name|persistRecords
operator|>
literal|0
decl_stmt|;
comment|// while the cursor has more values, keepRunning is true and the cursorId is not 0, which symbolizes that the cursor is dead
try|try
block|{
while|while
condition|(
name|cursor
operator|.
name|hasNext
argument_list|()
operator|&&
name|keepRunning
condition|)
block|{
comment|//cursor.getCursorId() != 0&&
name|DBObject
name|dbObj
init|=
name|cursor
operator|.
name|next
argument_list|()
decl_stmt|;
name|Exchange
name|exchange
init|=
name|endpoint
operator|.
name|createMongoDbExchange
argument_list|(
name|dbObj
argument_list|)
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
literal|"Sending exchange: {}, ObjectId: {}"
argument_list|,
name|exchange
argument_list|,
name|dbObj
operator|.
name|get
argument_list|(
literal|"_id"
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|consumer
operator|.
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
comment|// do nothing
block|}
name|tailTracking
operator|.
name|setLastVal
argument_list|(
name|dbObj
argument_list|)
expr_stmt|;
if|if
condition|(
name|persistRegularly
operator|&&
name|counter
operator|++
operator|%
name|persistRecords
operator|==
literal|0
condition|)
block|{
name|tailTracking
operator|.
name|persistToStore
argument_list|()
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|MongoCursorNotFoundException
name|e
parameter_list|)
block|{
comment|// we only log the warning if we are not stopping, otherwise it is expected because the stop() method kills the cursor just in case it is blocked
comment|// waiting for more data to arrive
if|if
condition|(
name|keepRunning
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Cursor not found exception from MongoDB, will regenerate cursor. This is normal behaviour with tailable cursors."
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
comment|// the loop finished, persist the lastValue just in case we are shutting down
comment|// TODO: perhaps add a functionality to persist every N records
name|tailTracking
operator|.
name|persistToStore
argument_list|()
expr_stmt|;
block|}
comment|// no arguments, will ask DB what the last updated Id was (checking persistent storage)
DECL|method|initializeCursor ()
specifier|private
name|MongoCursor
argument_list|<
name|BasicDBObject
argument_list|>
name|initializeCursor
parameter_list|()
block|{
name|Object
name|lastVal
init|=
name|tailTracking
operator|.
name|lastVal
decl_stmt|;
comment|// lastVal can be null if we are initializing and there is no persistence enabled
name|MongoCursor
argument_list|<
name|BasicDBObject
argument_list|>
name|answer
decl_stmt|;
if|if
condition|(
name|lastVal
operator|==
literal|null
condition|)
block|{
name|answer
operator|=
name|dbCol
operator|.
name|find
argument_list|()
operator|.
name|cursorType
argument_list|(
name|CursorType
operator|.
name|TailableAwait
argument_list|)
operator|.
name|iterator
argument_list|()
expr_stmt|;
block|}
else|else
block|{
specifier|final
name|String
name|increasingFieldName
init|=
name|tailTracking
operator|.
name|getIncreasingFieldName
argument_list|()
decl_stmt|;
name|BasicDBObject
name|queryObj
init|=
name|endpoint
operator|.
name|getTailTrackingStrategy
argument_list|()
operator|.
name|createQuery
argument_list|(
name|lastVal
argument_list|,
name|increasingFieldName
argument_list|)
decl_stmt|;
name|answer
operator|=
name|dbCol
operator|.
name|find
argument_list|(
name|queryObj
argument_list|)
operator|.
name|cursorType
argument_list|(
name|CursorType
operator|.
name|TailableAwait
argument_list|)
operator|.
name|iterator
argument_list|()
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
DECL|method|awaitStopped ()
specifier|private
name|void
name|awaitStopped
parameter_list|()
throws|throws
name|InterruptedException
block|{
if|if
condition|(
operator|!
name|stopped
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Going to wait for stopping"
argument_list|)
expr_stmt|;
name|stoppedLatch
operator|.
name|await
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

