begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.beanstalk
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|beanstalk
package|;
end_package

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
name|Callable
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
name|ExecutorService
import|;
end_import

begin_import
import|import
name|com
operator|.
name|surftools
operator|.
name|BeanstalkClient
operator|.
name|BeanstalkException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|surftools
operator|.
name|BeanstalkClient
operator|.
name|Client
import|;
end_import

begin_import
import|import
name|com
operator|.
name|surftools
operator|.
name|BeanstalkClient
operator|.
name|Job
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
name|ExchangePattern
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
name|RuntimeCamelException
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
name|component
operator|.
name|beanstalk
operator|.
name|processors
operator|.
name|BuryCommand
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
name|component
operator|.
name|beanstalk
operator|.
name|processors
operator|.
name|Command
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
name|component
operator|.
name|beanstalk
operator|.
name|processors
operator|.
name|DeleteCommand
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
name|component
operator|.
name|beanstalk
operator|.
name|processors
operator|.
name|ReleaseCommand
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spi
operator|.
name|Synchronization
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

begin_comment
comment|/**  * PollingConsumer to read Beanstalk jobs.  *<p/>  * The consumer may delete the job immediately or based on successful {@link Exchange}  * completion. The behavior is configurable by<code>consumer.awaitJob</code>  * flag (by default<code>true</code>)  *<p/>  * This consumer will add a {@link Synchronization} object to every {@link Exchange}  * object it creates in order to react on successful exchange completion or failure.  *<p/>  * In the case of successful completion, Beanstalk's<code>delete</code> method is  * called upon the job. In the case of failure the default reaction is to call  *<code>bury</code>.  *<p/>  * The reaction on failures is configurable: possible variants are "bury", "release" or "delete"  */
end_comment

begin_class
DECL|class|BeanstalkConsumer
specifier|public
class|class
name|BeanstalkConsumer
extends|extends
name|ScheduledPollConsumer
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
name|BeanstalkConsumer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|STATS_KEY_STR
specifier|private
specifier|static
specifier|final
name|String
index|[]
name|STATS_KEY_STR
init|=
operator|new
name|String
index|[]
block|{
literal|"tube"
block|,
literal|"state"
block|}
decl_stmt|;
DECL|field|STATS_KEY_INT
specifier|private
specifier|static
specifier|final
name|String
index|[]
name|STATS_KEY_INT
init|=
operator|new
name|String
index|[]
block|{
literal|"age"
block|,
literal|"time-left"
block|,
literal|"timeouts"
block|,
literal|"releases"
block|,
literal|"buries"
block|,
literal|"kicks"
block|}
decl_stmt|;
DECL|field|onFailure
specifier|private
name|String
name|onFailure
init|=
name|BeanstalkComponent
operator|.
name|COMMAND_BURY
decl_stmt|;
DECL|field|useBlockIO
specifier|private
name|boolean
name|useBlockIO
init|=
literal|true
decl_stmt|;
DECL|field|deleteImmediately
specifier|private
name|boolean
name|deleteImmediately
decl_stmt|;
DECL|field|client
specifier|private
name|Client
name|client
decl_stmt|;
DECL|field|executor
specifier|private
name|ExecutorService
name|executor
decl_stmt|;
DECL|field|sync
specifier|private
name|Synchronization
name|sync
decl_stmt|;
DECL|field|initTask
specifier|private
specifier|final
name|Runnable
name|initTask
init|=
operator|new
name|Runnable
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|run
parameter_list|()
block|{
name|client
operator|=
name|getEndpoint
argument_list|()
operator|.
name|getConnection
argument_list|()
operator|.
name|newReadingClient
argument_list|(
name|useBlockIO
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
DECL|field|pollTask
specifier|private
specifier|final
name|Callable
argument_list|<
name|Exchange
argument_list|>
name|pollTask
init|=
operator|new
name|Callable
argument_list|<
name|Exchange
argument_list|>
argument_list|()
block|{
specifier|final
name|Integer
name|noWait
init|=
literal|0
decl_stmt|;
annotation|@
name|Override
specifier|public
name|Exchange
name|call
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|client
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
literal|"Beanstalk client not initialized"
argument_list|)
throw|;
block|}
try|try
block|{
specifier|final
name|Job
name|job
init|=
name|client
operator|.
name|reserve
argument_list|(
name|noWait
argument_list|)
decl_stmt|;
if|if
condition|(
name|job
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
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
name|String
operator|.
name|format
argument_list|(
literal|"Received job ID %d (data length %d)"
argument_list|,
name|job
operator|.
name|getJobId
argument_list|()
argument_list|,
name|job
operator|.
name|getData
argument_list|()
operator|.
name|length
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|final
name|Exchange
name|exchange
init|=
name|getEndpoint
argument_list|()
operator|.
name|createExchange
argument_list|(
name|ExchangePattern
operator|.
name|InOnly
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|Headers
operator|.
name|JOB_ID
argument_list|,
name|job
operator|.
name|getJobId
argument_list|()
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|job
operator|.
name|getData
argument_list|()
argument_list|,
name|byte
index|[]
operator|.
expr|class
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|jobStats
init|=
name|client
operator|.
name|statsJob
argument_list|(
name|job
operator|.
name|getJobId
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|jobStats
operator|!=
literal|null
operator|&&
operator|!
name|jobStats
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
for|for
control|(
name|String
name|key
range|:
name|STATS_KEY_STR
control|)
block|{
if|if
condition|(
name|jobStats
operator|.
name|containsKey
argument_list|(
name|key
argument_list|)
condition|)
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|Headers
operator|.
name|PREFIX
operator|+
name|key
argument_list|,
name|jobStats
operator|.
name|get
argument_list|(
name|key
argument_list|)
operator|.
name|trim
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|jobStats
operator|.
name|containsKey
argument_list|(
literal|"pri"
argument_list|)
condition|)
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|Headers
operator|.
name|PRIORITY
argument_list|,
name|Long
operator|.
name|parseLong
argument_list|(
name|jobStats
operator|.
name|get
argument_list|(
literal|"pri"
argument_list|)
operator|.
name|trim
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|String
name|key
range|:
name|STATS_KEY_INT
control|)
block|{
if|if
condition|(
name|jobStats
operator|.
name|containsKey
argument_list|(
name|key
argument_list|)
condition|)
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|Headers
operator|.
name|PREFIX
operator|+
name|key
argument_list|,
name|Integer
operator|.
name|parseInt
argument_list|(
name|jobStats
operator|.
name|get
argument_list|(
name|key
argument_list|)
operator|.
name|trim
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|deleteImmediately
condition|)
block|{
name|client
operator|.
name|delete
argument_list|(
name|job
operator|.
name|getJobId
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|exchange
operator|.
name|addOnCompletion
argument_list|(
name|sync
argument_list|)
expr_stmt|;
block|}
return|return
name|exchange
return|;
block|}
catch|catch
parameter_list|(
name|BeanstalkException
name|e
parameter_list|)
block|{
name|getExceptionHandler
argument_list|()
operator|.
name|handleException
argument_list|(
literal|"Beanstalk client error"
argument_list|,
name|e
argument_list|)
expr_stmt|;
name|resetClient
argument_list|()
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
block|}
decl_stmt|;
DECL|method|BeanstalkConsumer (final BeanstalkEndpoint endpoint, final Processor processor)
specifier|public
name|BeanstalkConsumer
parameter_list|(
specifier|final
name|BeanstalkEndpoint
name|endpoint
parameter_list|,
specifier|final
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
DECL|method|poll ()
specifier|protected
name|int
name|poll
parameter_list|()
throws|throws
name|Exception
block|{
name|int
name|messagesPolled
init|=
literal|0
decl_stmt|;
while|while
condition|(
name|isPollAllowed
argument_list|()
condition|)
block|{
specifier|final
name|Exchange
name|exchange
init|=
name|executor
operator|.
name|submit
argument_list|(
name|pollTask
argument_list|)
operator|.
name|get
argument_list|()
decl_stmt|;
if|if
condition|(
name|exchange
operator|==
literal|null
condition|)
block|{
break|break;
block|}
operator|++
name|messagesPolled
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
return|return
name|messagesPolled
return|;
block|}
DECL|method|getOnFailure ()
specifier|public
name|String
name|getOnFailure
parameter_list|()
block|{
return|return
name|onFailure
return|;
block|}
DECL|method|setOnFailure (String onFailure)
specifier|public
name|void
name|setOnFailure
parameter_list|(
name|String
name|onFailure
parameter_list|)
block|{
name|this
operator|.
name|onFailure
operator|=
name|onFailure
expr_stmt|;
block|}
DECL|method|getUseBlockIO ()
specifier|public
name|boolean
name|getUseBlockIO
parameter_list|()
block|{
return|return
name|useBlockIO
return|;
block|}
DECL|method|setUseBlockIO (boolean useBlockIO)
specifier|public
name|void
name|setUseBlockIO
parameter_list|(
name|boolean
name|useBlockIO
parameter_list|)
block|{
name|this
operator|.
name|useBlockIO
operator|=
name|useBlockIO
expr_stmt|;
block|}
DECL|method|getAwaitJob ()
specifier|public
name|boolean
name|getAwaitJob
parameter_list|()
block|{
return|return
operator|!
name|deleteImmediately
return|;
block|}
DECL|method|setAwaitJob (boolean awaitingCompletion)
specifier|public
name|void
name|setAwaitJob
parameter_list|(
name|boolean
name|awaitingCompletion
parameter_list|)
block|{
name|this
operator|.
name|deleteImmediately
operator|=
operator|!
name|awaitingCompletion
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getEndpoint ()
specifier|public
name|BeanstalkEndpoint
name|getEndpoint
parameter_list|()
block|{
return|return
operator|(
name|BeanstalkEndpoint
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
name|executor
operator|=
name|getEndpoint
argument_list|()
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getExecutorServiceManager
argument_list|()
operator|.
name|newSingleThreadExecutor
argument_list|(
name|this
argument_list|,
literal|"Beanstalk-Consumer"
argument_list|)
expr_stmt|;
name|executor
operator|.
name|execute
argument_list|(
name|initTask
argument_list|)
expr_stmt|;
name|sync
operator|=
operator|new
name|Sync
argument_list|()
expr_stmt|;
name|super
operator|.
name|doStart
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
if|if
condition|(
name|executor
operator|!=
literal|null
condition|)
block|{
name|getEndpoint
argument_list|()
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getExecutorServiceManager
argument_list|()
operator|.
name|shutdown
argument_list|(
name|executor
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|resetClient ()
specifier|protected
name|void
name|resetClient
parameter_list|()
block|{
if|if
condition|(
name|client
operator|!=
literal|null
condition|)
block|{
name|client
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
name|initTask
operator|.
name|run
argument_list|()
expr_stmt|;
block|}
DECL|class|Sync
class|class
name|Sync
implements|implements
name|Synchronization
block|{
DECL|field|successCommand
specifier|protected
specifier|final
name|Command
name|successCommand
decl_stmt|;
DECL|field|failureCommand
specifier|protected
specifier|final
name|Command
name|failureCommand
decl_stmt|;
DECL|method|Sync ()
specifier|public
name|Sync
parameter_list|()
block|{
name|successCommand
operator|=
operator|new
name|DeleteCommand
argument_list|(
name|getEndpoint
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|BeanstalkComponent
operator|.
name|COMMAND_BURY
operator|.
name|equals
argument_list|(
name|onFailure
argument_list|)
condition|)
block|{
name|failureCommand
operator|=
operator|new
name|BuryCommand
argument_list|(
name|getEndpoint
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|BeanstalkComponent
operator|.
name|COMMAND_RELEASE
operator|.
name|equals
argument_list|(
name|onFailure
argument_list|)
condition|)
block|{
name|failureCommand
operator|=
operator|new
name|ReleaseCommand
argument_list|(
name|getEndpoint
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|BeanstalkComponent
operator|.
name|COMMAND_DELETE
operator|.
name|equals
argument_list|(
name|onFailure
argument_list|)
condition|)
block|{
name|failureCommand
operator|=
operator|new
name|DeleteCommand
argument_list|(
name|getEndpoint
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Unknown failure command: %s"
argument_list|,
name|onFailure
argument_list|)
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|onComplete (final Exchange exchange)
specifier|public
name|void
name|onComplete
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|)
block|{
try|try
block|{
name|executor
operator|.
name|submit
argument_list|(
operator|new
name|RunCommand
argument_list|(
name|successCommand
argument_list|,
name|exchange
argument_list|)
argument_list|)
operator|.
name|get
argument_list|()
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
name|String
operator|.
name|format
argument_list|(
literal|"Could not run completion of exchange %s"
argument_list|,
name|exchange
argument_list|)
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|onFailure (final Exchange exchange)
specifier|public
name|void
name|onFailure
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|)
block|{
try|try
block|{
name|executor
operator|.
name|submit
argument_list|(
operator|new
name|RunCommand
argument_list|(
name|failureCommand
argument_list|,
name|exchange
argument_list|)
argument_list|)
operator|.
name|get
argument_list|()
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
name|String
operator|.
name|format
argument_list|(
literal|"%s could not run failure of exchange %s"
argument_list|,
name|failureCommand
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|exchange
argument_list|)
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
DECL|class|RunCommand
class|class
name|RunCommand
implements|implements
name|Runnable
block|{
DECL|field|command
specifier|private
specifier|final
name|Command
name|command
decl_stmt|;
DECL|field|exchange
specifier|private
specifier|final
name|Exchange
name|exchange
decl_stmt|;
DECL|method|RunCommand (final Command command, final Exchange exchange)
specifier|public
name|RunCommand
parameter_list|(
specifier|final
name|Command
name|command
parameter_list|,
specifier|final
name|Exchange
name|exchange
parameter_list|)
block|{
name|this
operator|.
name|command
operator|=
name|command
expr_stmt|;
name|this
operator|.
name|exchange
operator|=
name|exchange
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
try|try
block|{
try|try
block|{
name|command
operator|.
name|act
argument_list|(
name|client
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|BeanstalkException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Post-processing %s of exchange %s failed, retrying."
argument_list|,
name|command
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|exchange
argument_list|)
argument_list|,
name|e
argument_list|)
expr_stmt|;
name|resetClient
argument_list|()
expr_stmt|;
name|command
operator|.
name|act
argument_list|(
name|client
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
specifier|final
name|Exception
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|error
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"%s could not post-process exchange %s"
argument_list|,
name|command
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|exchange
argument_list|)
argument_list|,
name|e
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
end_class

end_unit

