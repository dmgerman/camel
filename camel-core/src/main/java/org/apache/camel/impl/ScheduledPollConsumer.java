begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
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
comment|/**  * A useful base class for any consumer which is polling based  *   * @version $Revision$  */
end_comment

begin_class
DECL|class|ScheduledPollConsumer
specifier|public
specifier|abstract
class|class
name|ScheduledPollConsumer
parameter_list|<
name|E
extends|extends
name|Exchange
parameter_list|>
extends|extends
name|DefaultConsumer
argument_list|<
name|E
argument_list|>
implements|implements
name|Runnable
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|ScheduledPollConsumer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|executor
specifier|private
specifier|final
name|ScheduledExecutorService
name|executor
decl_stmt|;
DECL|field|initialDelay
specifier|private
name|long
name|initialDelay
init|=
literal|1000
decl_stmt|;
DECL|field|delay
specifier|private
name|long
name|delay
init|=
literal|500
decl_stmt|;
DECL|field|timeUnit
specifier|private
name|TimeUnit
name|timeUnit
init|=
name|TimeUnit
operator|.
name|MILLISECONDS
decl_stmt|;
DECL|field|useFixedDelay
specifier|private
name|boolean
name|useFixedDelay
decl_stmt|;
DECL|field|future
specifier|private
name|ScheduledFuture
argument_list|<
name|?
argument_list|>
name|future
decl_stmt|;
DECL|method|ScheduledPollConsumer (DefaultEndpoint<E> endpoint, Processor processor)
specifier|public
name|ScheduledPollConsumer
parameter_list|(
name|DefaultEndpoint
argument_list|<
name|E
argument_list|>
name|endpoint
parameter_list|,
name|Processor
name|processor
parameter_list|)
block|{
name|this
argument_list|(
name|endpoint
argument_list|,
name|processor
argument_list|,
name|endpoint
operator|.
name|getExecutorService
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|ScheduledPollConsumer (Endpoint<E> endpoint, Processor processor, ScheduledExecutorService executor)
specifier|public
name|ScheduledPollConsumer
parameter_list|(
name|Endpoint
argument_list|<
name|E
argument_list|>
name|endpoint
parameter_list|,
name|Processor
name|processor
parameter_list|,
name|ScheduledExecutorService
name|executor
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|,
name|processor
argument_list|)
expr_stmt|;
name|this
operator|.
name|executor
operator|=
name|executor
expr_stmt|;
if|if
condition|(
name|executor
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"A non null ScheduledExecutorService must be provided."
argument_list|)
throw|;
block|}
block|}
comment|/**      * Invoked whenever we should be polled      */
DECL|method|run ()
specifier|public
name|void
name|run
parameter_list|()
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
literal|"Starting to poll: "
operator|+
name|this
operator|.
name|getEndpoint
argument_list|()
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|poll
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// TODO: We should not swallow this but handle it better. See CAMEL-501
name|LOG
operator|.
name|warn
argument_list|(
literal|"An exception occured while polling: "
operator|+
name|this
operator|.
name|getEndpoint
argument_list|()
operator|+
literal|": "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
comment|// Properties
comment|// -------------------------------------------------------------------------
DECL|method|getInitialDelay ()
specifier|public
name|long
name|getInitialDelay
parameter_list|()
block|{
return|return
name|initialDelay
return|;
block|}
DECL|method|setInitialDelay (long initialDelay)
specifier|public
name|void
name|setInitialDelay
parameter_list|(
name|long
name|initialDelay
parameter_list|)
block|{
name|this
operator|.
name|initialDelay
operator|=
name|initialDelay
expr_stmt|;
block|}
DECL|method|getDelay ()
specifier|public
name|long
name|getDelay
parameter_list|()
block|{
return|return
name|delay
return|;
block|}
DECL|method|setDelay (long delay)
specifier|public
name|void
name|setDelay
parameter_list|(
name|long
name|delay
parameter_list|)
block|{
name|this
operator|.
name|delay
operator|=
name|delay
expr_stmt|;
block|}
DECL|method|getTimeUnit ()
specifier|public
name|TimeUnit
name|getTimeUnit
parameter_list|()
block|{
return|return
name|timeUnit
return|;
block|}
DECL|method|setTimeUnit (TimeUnit timeUnit)
specifier|public
name|void
name|setTimeUnit
parameter_list|(
name|TimeUnit
name|timeUnit
parameter_list|)
block|{
name|this
operator|.
name|timeUnit
operator|=
name|timeUnit
expr_stmt|;
block|}
DECL|method|isUseFixedDelay ()
specifier|public
name|boolean
name|isUseFixedDelay
parameter_list|()
block|{
return|return
name|useFixedDelay
return|;
block|}
DECL|method|setUseFixedDelay (boolean useFixedDelay)
specifier|public
name|void
name|setUseFixedDelay
parameter_list|(
name|boolean
name|useFixedDelay
parameter_list|)
block|{
name|this
operator|.
name|useFixedDelay
operator|=
name|useFixedDelay
expr_stmt|;
block|}
comment|// Implementation methods
comment|// -------------------------------------------------------------------------
comment|/**      * The polling method which is invoked periodically to poll this consumer      *       * @throws Exception can be thrown if an exception occured during polling      */
DECL|method|poll ()
specifier|protected
specifier|abstract
name|void
name|poll
parameter_list|()
throws|throws
name|Exception
function_decl|;
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
if|if
condition|(
name|isUseFixedDelay
argument_list|()
condition|)
block|{
name|future
operator|=
name|executor
operator|.
name|scheduleWithFixedDelay
argument_list|(
name|this
argument_list|,
name|getInitialDelay
argument_list|()
argument_list|,
name|getDelay
argument_list|()
argument_list|,
name|getTimeUnit
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|future
operator|=
name|executor
operator|.
name|scheduleAtFixedRate
argument_list|(
name|this
argument_list|,
name|getInitialDelay
argument_list|()
argument_list|,
name|getDelay
argument_list|()
argument_list|,
name|getTimeUnit
argument_list|()
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
block|}
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

