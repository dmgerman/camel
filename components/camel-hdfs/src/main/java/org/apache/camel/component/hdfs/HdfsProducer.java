begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.hdfs
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|hdfs
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
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
name|atomic
operator|.
name|AtomicBoolean
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
name|builder
operator|.
name|ThreadPoolBuilder
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
name|DefaultProducer
import|;
end_import

begin_class
DECL|class|HdfsProducer
specifier|public
class|class
name|HdfsProducer
extends|extends
name|DefaultProducer
block|{
DECL|field|config
specifier|private
specifier|final
name|HdfsConfiguration
name|config
decl_stmt|;
DECL|field|hdfsPath
specifier|private
specifier|final
name|StringBuilder
name|hdfsPath
decl_stmt|;
DECL|field|idle
specifier|private
specifier|final
name|AtomicBoolean
name|idle
init|=
operator|new
name|AtomicBoolean
argument_list|(
literal|false
argument_list|)
decl_stmt|;
DECL|field|scheduler
specifier|private
name|ScheduledExecutorService
name|scheduler
decl_stmt|;
DECL|field|ostream
specifier|private
name|HdfsOutputStream
name|ostream
decl_stmt|;
DECL|field|splitNum
specifier|private
name|long
name|splitNum
decl_stmt|;
DECL|class|SplitStrategy
specifier|public
specifier|static
specifier|final
class|class
name|SplitStrategy
block|{
DECL|field|type
specifier|private
name|SplitStrategyType
name|type
decl_stmt|;
DECL|field|value
specifier|private
name|long
name|value
decl_stmt|;
DECL|method|SplitStrategy (SplitStrategyType type, long value)
specifier|public
name|SplitStrategy
parameter_list|(
name|SplitStrategyType
name|type
parameter_list|,
name|long
name|value
parameter_list|)
block|{
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
name|this
operator|.
name|value
operator|=
name|value
expr_stmt|;
block|}
DECL|method|getType ()
specifier|public
name|SplitStrategyType
name|getType
parameter_list|()
block|{
return|return
name|type
return|;
block|}
DECL|method|getValue ()
specifier|public
name|long
name|getValue
parameter_list|()
block|{
return|return
name|value
return|;
block|}
block|}
DECL|enum|SplitStrategyType
specifier|public
enum|enum
name|SplitStrategyType
block|{
DECL|enumConstant|BYTES
name|BYTES
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|split
parameter_list|(
name|HdfsOutputStream
name|oldOstream
parameter_list|,
name|long
name|value
parameter_list|,
name|HdfsProducer
name|producer
parameter_list|)
block|{
return|return
name|oldOstream
operator|.
name|getNumOfWrittenBytes
argument_list|()
operator|>=
name|value
return|;
block|}
block|}
block|,
DECL|enumConstant|MESSAGES
name|MESSAGES
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|split
parameter_list|(
name|HdfsOutputStream
name|oldOstream
parameter_list|,
name|long
name|value
parameter_list|,
name|HdfsProducer
name|producer
parameter_list|)
block|{
return|return
name|oldOstream
operator|.
name|getNumOfWrittenMessages
argument_list|()
operator|>=
name|value
return|;
block|}
block|}
block|,
DECL|enumConstant|IDLE
name|IDLE
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|split
parameter_list|(
name|HdfsOutputStream
name|oldOstream
parameter_list|,
name|long
name|value
parameter_list|,
name|HdfsProducer
name|producer
parameter_list|)
block|{
return|return
name|producer
operator|.
name|idle
operator|.
name|get
argument_list|()
return|;
block|}
block|}
block|;
DECL|method|split (HdfsOutputStream oldOstream, long value, HdfsProducer producer)
specifier|public
specifier|abstract
name|boolean
name|split
parameter_list|(
name|HdfsOutputStream
name|oldOstream
parameter_list|,
name|long
name|value
parameter_list|,
name|HdfsProducer
name|producer
parameter_list|)
function_decl|;
block|}
DECL|method|HdfsProducer (HdfsEndpoint endpoint, HdfsConfiguration config)
specifier|public
name|HdfsProducer
parameter_list|(
name|HdfsEndpoint
name|endpoint
parameter_list|,
name|HdfsConfiguration
name|config
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|this
operator|.
name|config
operator|=
name|config
expr_stmt|;
name|this
operator|.
name|hdfsPath
operator|=
name|config
operator|.
name|getFileSystemType
argument_list|()
operator|.
name|getHdfsPath
argument_list|(
name|config
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
name|StringBuilder
name|actualPath
init|=
operator|new
name|StringBuilder
argument_list|(
name|hdfsPath
argument_list|)
decl_stmt|;
if|if
condition|(
name|config
operator|.
name|getSplitStrategies
argument_list|()
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|actualPath
operator|=
name|newFileName
argument_list|()
expr_stmt|;
block|}
name|ostream
operator|=
name|HdfsOutputStream
operator|.
name|createOutputStream
argument_list|(
name|actualPath
operator|.
name|toString
argument_list|()
argument_list|,
name|config
argument_list|)
expr_stmt|;
name|SplitStrategy
name|idleStrategy
init|=
literal|null
decl_stmt|;
for|for
control|(
name|SplitStrategy
name|strategy
range|:
name|config
operator|.
name|getSplitStrategies
argument_list|()
control|)
block|{
if|if
condition|(
name|strategy
operator|.
name|type
operator|==
name|SplitStrategyType
operator|.
name|IDLE
condition|)
block|{
name|idleStrategy
operator|=
name|strategy
expr_stmt|;
break|break;
block|}
block|}
if|if
condition|(
name|idleStrategy
operator|!=
literal|null
condition|)
block|{
name|scheduler
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
name|getScheduledExecutorService
argument_list|(
name|ThreadPoolBuilder
operator|.
name|singleThreadExecutor
argument_list|(
literal|"IdleCheck"
argument_list|)
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Creating IdleCheck task scheduled to run every {} millis"
argument_list|,
name|config
operator|.
name|getCheckIdleInterval
argument_list|()
argument_list|)
expr_stmt|;
name|scheduler
operator|.
name|scheduleAtFixedRate
argument_list|(
operator|new
name|IdleCheck
argument_list|(
name|idleStrategy
argument_list|)
argument_list|,
literal|1000
argument_list|,
name|config
operator|.
name|getCheckIdleInterval
argument_list|()
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
expr_stmt|;
block|}
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
name|ostream
operator|.
name|close
argument_list|()
expr_stmt|;
if|if
condition|(
name|scheduler
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
name|scheduler
argument_list|)
expr_stmt|;
name|scheduler
operator|=
literal|null
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|process (Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|Object
name|body
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
name|Object
name|key
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|HdfsHeader
operator|.
name|KEY
operator|.
name|name
argument_list|()
argument_list|)
decl_stmt|;
name|boolean
name|split
init|=
literal|false
decl_stmt|;
name|List
argument_list|<
name|SplitStrategy
argument_list|>
name|strategies
init|=
name|config
operator|.
name|getSplitStrategies
argument_list|()
decl_stmt|;
for|for
control|(
name|SplitStrategy
name|splitStrategy
range|:
name|strategies
control|)
block|{
name|split
operator||=
name|splitStrategy
operator|.
name|getType
argument_list|()
operator|.
name|split
argument_list|(
name|ostream
argument_list|,
name|splitStrategy
operator|.
name|value
argument_list|,
name|this
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|split
condition|)
block|{
name|ostream
operator|.
name|close
argument_list|()
expr_stmt|;
name|StringBuilder
name|actualPath
init|=
name|newFileName
argument_list|()
decl_stmt|;
name|ostream
operator|=
name|HdfsOutputStream
operator|.
name|createOutputStream
argument_list|(
name|actualPath
operator|.
name|toString
argument_list|()
argument_list|,
name|config
argument_list|)
expr_stmt|;
block|}
name|ostream
operator|.
name|append
argument_list|(
name|key
argument_list|,
name|body
argument_list|,
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
argument_list|)
expr_stmt|;
name|idle
operator|.
name|set
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
DECL|method|getOstream ()
specifier|public
name|HdfsOutputStream
name|getOstream
parameter_list|()
block|{
return|return
name|ostream
return|;
block|}
DECL|method|newFileName ()
specifier|private
name|StringBuilder
name|newFileName
parameter_list|()
block|{
name|StringBuilder
name|actualPath
init|=
operator|new
name|StringBuilder
argument_list|(
name|hdfsPath
argument_list|)
decl_stmt|;
name|actualPath
operator|.
name|append
argument_list|(
name|splitNum
argument_list|)
expr_stmt|;
name|splitNum
operator|++
expr_stmt|;
return|return
name|actualPath
return|;
block|}
DECL|method|isIdle ()
specifier|public
specifier|final
name|AtomicBoolean
name|isIdle
parameter_list|()
block|{
return|return
name|idle
return|;
block|}
comment|/**      * Idle check background task      */
DECL|class|IdleCheck
specifier|private
specifier|final
class|class
name|IdleCheck
implements|implements
name|Runnable
block|{
DECL|field|strategy
specifier|private
specifier|final
name|SplitStrategy
name|strategy
decl_stmt|;
DECL|method|IdleCheck (SplitStrategy strategy)
specifier|private
name|IdleCheck
parameter_list|(
name|SplitStrategy
name|strategy
parameter_list|)
block|{
name|this
operator|.
name|strategy
operator|=
name|strategy
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
name|HdfsProducer
operator|.
name|this
operator|.
name|log
operator|.
name|trace
argument_list|(
literal|"IdleCheck running"
argument_list|)
expr_stmt|;
if|if
condition|(
name|System
operator|.
name|currentTimeMillis
argument_list|()
operator|-
name|ostream
operator|.
name|getLastAccess
argument_list|()
operator|>
name|strategy
operator|.
name|value
operator|&&
operator|!
name|idle
operator|.
name|get
argument_list|()
operator|&&
operator|!
name|ostream
operator|.
name|isBusy
argument_list|()
operator|.
name|get
argument_list|()
condition|)
block|{
name|idle
operator|.
name|set
argument_list|(
literal|true
argument_list|)
expr_stmt|;
try|try
block|{
name|ostream
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|// ignore
block|}
block|}
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"IdleCheck"
return|;
block|}
block|}
block|}
end_class

end_unit

