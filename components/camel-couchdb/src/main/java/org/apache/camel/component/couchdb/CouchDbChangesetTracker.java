begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.couchdb
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|couchdb
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gson
operator|.
name|JsonObject
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
name|lightcouch
operator|.
name|Changes
import|;
end_import

begin_import
import|import
name|org
operator|.
name|lightcouch
operator|.
name|ChangesResult
import|;
end_import

begin_import
import|import
name|org
operator|.
name|lightcouch
operator|.
name|CouchDbException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|lightcouch
operator|.
name|CouchDbInfo
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
DECL|class|CouchDbChangesetTracker
specifier|public
class|class
name|CouchDbChangesetTracker
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
name|CouchDbChangesetTracker
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|MAX_DB_ERROR_REPEATS
specifier|private
specifier|static
specifier|final
name|int
name|MAX_DB_ERROR_REPEATS
init|=
literal|8
decl_stmt|;
DECL|field|stopped
specifier|private
specifier|volatile
name|boolean
name|stopped
decl_stmt|;
DECL|field|couchClient
specifier|private
specifier|final
name|CouchDbClientWrapper
name|couchClient
decl_stmt|;
DECL|field|endpoint
specifier|private
specifier|final
name|CouchDbEndpoint
name|endpoint
decl_stmt|;
DECL|field|consumer
specifier|private
specifier|final
name|CouchDbConsumer
name|consumer
decl_stmt|;
DECL|field|changes
specifier|private
name|Changes
name|changes
decl_stmt|;
DECL|method|CouchDbChangesetTracker (CouchDbEndpoint endpoint, CouchDbConsumer consumer, CouchDbClientWrapper couchClient)
specifier|public
name|CouchDbChangesetTracker
parameter_list|(
name|CouchDbEndpoint
name|endpoint
parameter_list|,
name|CouchDbConsumer
name|consumer
parameter_list|,
name|CouchDbClientWrapper
name|couchClient
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
name|couchClient
operator|=
name|couchClient
expr_stmt|;
name|initChanges
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|initChanges (final String sequence)
specifier|private
name|void
name|initChanges
parameter_list|(
specifier|final
name|String
name|sequence
parameter_list|)
block|{
name|String
name|since
init|=
name|sequence
decl_stmt|;
if|if
condition|(
literal|null
operator|==
name|since
condition|)
block|{
name|CouchDbInfo
name|dbInfo
init|=
name|couchClient
operator|.
name|context
argument_list|()
operator|.
name|info
argument_list|()
decl_stmt|;
name|since
operator|=
name|dbInfo
operator|.
name|getUpdateSeq
argument_list|()
expr_stmt|;
comment|// get latest update seq
block|}
name|LOG
operator|.
name|debug
argument_list|(
literal|"Last sequence [{}]"
argument_list|,
name|since
argument_list|)
expr_stmt|;
name|changes
operator|=
name|couchClient
operator|.
name|changes
argument_list|()
operator|.
name|style
argument_list|(
name|endpoint
operator|.
name|getStyle
argument_list|()
argument_list|)
operator|.
name|includeDocs
argument_list|(
literal|true
argument_list|)
operator|.
name|since
argument_list|(
name|since
argument_list|)
operator|.
name|heartBeat
argument_list|(
name|endpoint
operator|.
name|getHeartbeat
argument_list|()
argument_list|)
operator|.
name|continuousChanges
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|run ()
specifier|public
name|void
name|run
parameter_list|()
block|{
name|String
name|lastSequence
init|=
literal|null
decl_stmt|;
try|try
block|{
while|while
condition|(
operator|!
name|stopped
condition|)
block|{
try|try
block|{
while|while
condition|(
name|changes
operator|.
name|hasNext
argument_list|()
condition|)
block|{
comment|// blocks until a feed is received
name|ChangesResult
operator|.
name|Row
name|feed
init|=
name|changes
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|feed
operator|.
name|isDeleted
argument_list|()
operator|&&
operator|!
name|endpoint
operator|.
name|isDeletes
argument_list|()
condition|)
block|{
continue|continue;
block|}
if|if
condition|(
operator|!
name|feed
operator|.
name|isDeleted
argument_list|()
operator|&&
operator|!
name|endpoint
operator|.
name|isUpdates
argument_list|()
condition|)
block|{
continue|continue;
block|}
name|lastSequence
operator|=
name|feed
operator|.
name|getSeq
argument_list|()
expr_stmt|;
name|JsonObject
name|doc
init|=
name|feed
operator|.
name|getDoc
argument_list|()
decl_stmt|;
name|Exchange
name|exchange
init|=
name|endpoint
operator|.
name|createExchange
argument_list|(
name|lastSequence
argument_list|,
name|feed
operator|.
name|getId
argument_list|()
argument_list|,
name|doc
argument_list|,
name|feed
operator|.
name|isDeleted
argument_list|()
argument_list|)
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
literal|"Created exchange [exchange={}, _id={}, seq={}"
argument_list|,
operator|new
name|Object
index|[]
block|{
name|exchange
block|,
name|feed
operator|.
name|getId
argument_list|()
block|,
name|lastSequence
block|}
argument_list|)
expr_stmt|;
block|}
try|try
block|{
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
name|consumer
operator|.
name|getExceptionHandler
argument_list|()
operator|.
name|handleException
argument_list|(
literal|"Error processing exchange."
argument_list|,
name|exchange
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
name|stopped
operator|=
literal|true
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CouchDbException
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
literal|"CouchDb Exception encountered waiting for changes!  Attempting to recover..."
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|waitForStability
argument_list|(
name|lastSequence
argument_list|)
condition|)
block|{
throw|throw
name|e
throw|;
block|}
block|}
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|error
argument_list|(
literal|"Unexpected error causing CouchDb change tracker to exit!"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|waitForStability (final String lastSequence)
specifier|private
name|boolean
name|waitForStability
parameter_list|(
specifier|final
name|String
name|lastSequence
parameter_list|)
block|{
name|boolean
name|problems
init|=
literal|true
decl_stmt|;
name|int
name|repeatDbErrorCount
init|=
literal|0
decl_stmt|;
while|while
condition|(
name|problems
condition|)
block|{
if|if
condition|(
operator|++
name|repeatDbErrorCount
operator|>
name|MAX_DB_ERROR_REPEATS
condition|)
block|{
name|LOG
operator|.
name|error
argument_list|(
literal|"CouchDb change set listener fatal error!  Retry attempts exceeded, listener must exit."
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
try|try
block|{
name|Thread
operator|.
name|sleep
argument_list|(
call|(
name|int
call|)
argument_list|(
operator|(
name|Math
operator|.
name|random
argument_list|()
operator|*
literal|2000
operator|)
operator|+
literal|5000
argument_list|)
argument_list|)
expr_stmt|;
comment|//<2000ms,5000ms)
block|}
catch|catch
parameter_list|(
name|InterruptedException
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
literal|"CouchDb change set listener interrupted waiting for stability!!"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
try|try
block|{
comment|// Fail fast operation
name|couchClient
operator|.
name|context
argument_list|()
operator|.
name|serverVersion
argument_list|()
expr_stmt|;
comment|// reset change listener
name|initChanges
argument_list|(
name|lastSequence
argument_list|)
expr_stmt|;
name|problems
operator|=
literal|false
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
literal|"Failed to get CouchDb server version and/or reset change listener!  Attempt: "
operator|+
name|repeatDbErrorCount
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
literal|true
return|;
block|}
DECL|method|stop ()
specifier|public
name|void
name|stop
parameter_list|()
block|{
name|changes
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
DECL|method|isStopped ()
specifier|public
name|boolean
name|isStopped
parameter_list|()
block|{
return|return
name|stopped
return|;
block|}
block|}
end_class

end_unit

