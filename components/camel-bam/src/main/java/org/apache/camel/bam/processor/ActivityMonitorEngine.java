begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.bam.processor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|bam
operator|.
name|processor
package|;
end_package

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
name|javax
operator|.
name|persistence
operator|.
name|EntityManager
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|LockModeType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|PersistenceException
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
name|bam
operator|.
name|QueryUtils
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
name|bam
operator|.
name|model
operator|.
name|ActivityState
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
name|bam
operator|.
name|rules
operator|.
name|ProcessRules
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
name|CastUtils
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
import|import
name|org
operator|.
name|springframework
operator|.
name|orm
operator|.
name|jpa
operator|.
name|JpaCallback
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|orm
operator|.
name|jpa
operator|.
name|JpaTemplate
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|transaction
operator|.
name|TransactionStatus
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|transaction
operator|.
name|support
operator|.
name|TransactionCallbackWithoutResult
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|transaction
operator|.
name|support
operator|.
name|TransactionTemplate
import|;
end_import

begin_comment
comment|/**  * A timer engine to monitor for expired activities and perform whatever actions  * are required.  *   * @version   */
end_comment

begin_class
DECL|class|ActivityMonitorEngine
specifier|public
class|class
name|ActivityMonitorEngine
extends|extends
name|ServiceSupport
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
name|ActivityMonitorEngine
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|template
specifier|private
name|JpaTemplate
name|template
decl_stmt|;
DECL|field|transactionTemplate
specifier|private
name|TransactionTemplate
name|transactionTemplate
decl_stmt|;
DECL|field|rules
specifier|private
name|ProcessRules
name|rules
decl_stmt|;
DECL|field|windowMillis
specifier|private
name|long
name|windowMillis
init|=
literal|1000L
decl_stmt|;
DECL|field|thread
specifier|private
name|Thread
name|thread
decl_stmt|;
DECL|field|useLocking
specifier|private
name|boolean
name|useLocking
decl_stmt|;
DECL|method|ActivityMonitorEngine (JpaTemplate template, TransactionTemplate transactionTemplate, ProcessRules rules)
specifier|public
name|ActivityMonitorEngine
parameter_list|(
name|JpaTemplate
name|template
parameter_list|,
name|TransactionTemplate
name|transactionTemplate
parameter_list|,
name|ProcessRules
name|rules
parameter_list|)
block|{
name|this
operator|.
name|template
operator|=
name|template
expr_stmt|;
name|this
operator|.
name|transactionTemplate
operator|=
name|transactionTemplate
expr_stmt|;
name|this
operator|.
name|rules
operator|=
name|rules
expr_stmt|;
block|}
DECL|method|isUseLocking ()
specifier|public
name|boolean
name|isUseLocking
parameter_list|()
block|{
return|return
name|useLocking
return|;
block|}
DECL|method|setUseLocking (boolean useLocking)
specifier|public
name|void
name|setUseLocking
parameter_list|(
name|boolean
name|useLocking
parameter_list|)
block|{
name|this
operator|.
name|useLocking
operator|=
name|useLocking
expr_stmt|;
block|}
DECL|method|run ()
specifier|public
name|void
name|run
parameter_list|()
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Starting to poll for timeout events"
argument_list|)
expr_stmt|;
while|while
condition|(
operator|!
name|isStopped
argument_list|()
condition|)
block|{
try|try
block|{
name|long
name|now
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
decl_stmt|;
name|long
name|nextPoll
init|=
name|now
operator|+
name|windowMillis
decl_stmt|;
specifier|final
name|Date
name|timeNow
init|=
operator|new
name|Date
argument_list|(
name|now
argument_list|)
decl_stmt|;
name|transactionTemplate
operator|.
name|execute
argument_list|(
operator|new
name|TransactionCallbackWithoutResult
argument_list|()
block|{
specifier|protected
name|void
name|doInTransactionWithoutResult
parameter_list|(
name|TransactionStatus
name|status
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|params
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|params
operator|.
name|put
argument_list|(
literal|"timeNow"
argument_list|,
name|timeNow
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|ActivityState
argument_list|>
name|list
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
name|template
operator|.
name|findByNamedParams
argument_list|(
literal|"select x from "
operator|+
name|QueryUtils
operator|.
name|getTypeName
argument_list|(
name|ActivityState
operator|.
name|class
argument_list|)
operator|+
literal|" x where x.timeOverdue< :timeNow"
argument_list|,
name|params
argument_list|)
argument_list|)
decl_stmt|;
for|for
control|(
name|ActivityState
name|activityState
range|:
name|list
control|)
block|{
name|fireExpiredEvent
argument_list|(
name|activityState
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
name|long
name|timeToSleep
init|=
name|nextPoll
operator|-
name|System
operator|.
name|currentTimeMillis
argument_list|()
decl_stmt|;
if|if
condition|(
name|timeToSleep
operator|>
literal|0
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Sleeping for {} millis"
argument_list|,
name|timeToSleep
argument_list|)
expr_stmt|;
try|try
block|{
name|Thread
operator|.
name|sleep
argument_list|(
name|timeToSleep
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Caught: "
operator|+
name|e
argument_list|,
name|e
argument_list|)
expr_stmt|;
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
literal|"Caught: "
operator|+
name|e
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|fireExpiredEvent (final ActivityState activityState)
specifier|protected
name|void
name|fireExpiredEvent
parameter_list|(
specifier|final
name|ActivityState
name|activityState
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Trying to fire expiration of: {}"
argument_list|,
name|activityState
argument_list|)
expr_stmt|;
name|template
operator|.
name|execute
argument_list|(
operator|new
name|JpaCallback
argument_list|<
name|Object
argument_list|>
argument_list|()
block|{
specifier|public
name|Object
name|doInJpa
parameter_list|(
name|EntityManager
name|entityManager
parameter_list|)
throws|throws
name|PersistenceException
block|{
comment|// let's try locking the object first
if|if
condition|(
name|isUseLocking
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Attempting to lock: "
operator|+
name|activityState
argument_list|)
expr_stmt|;
name|entityManager
operator|.
name|lock
argument_list|(
name|activityState
argument_list|,
name|LockModeType
operator|.
name|WRITE
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Grabbed lock: "
operator|+
name|activityState
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|rules
operator|.
name|processExpired
argument_list|(
name|activityState
argument_list|)
expr_stmt|;
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
literal|"Failed to process expiration of: "
operator|+
name|activityState
operator|+
literal|". Reason: "
operator|+
name|e
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
name|activityState
operator|.
name|setTimeOverdue
argument_list|(
literal|null
argument_list|)
expr_stmt|;
comment|//activityState.setEscalationLevel(escalateLevel + 1);
return|return
literal|null
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
name|rules
operator|.
name|start
argument_list|()
expr_stmt|;
name|thread
operator|=
operator|new
name|Thread
argument_list|(
name|this
argument_list|,
literal|"ActivityMonitorEngine"
argument_list|)
expr_stmt|;
name|thread
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
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
name|thread
operator|!=
literal|null
condition|)
block|{
name|thread
operator|=
literal|null
expr_stmt|;
block|}
name|rules
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

