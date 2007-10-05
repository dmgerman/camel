begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
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
name|concurrent
operator|.
name|CountDownLatch
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
name|AlreadyStoppedException
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
comment|/**  * A useful base class for any processor which provides some kind of throttling  * or delayed processing  *   * @version $Revision: $  */
end_comment

begin_class
DECL|class|DelayProcessorSupport
specifier|public
specifier|abstract
class|class
name|DelayProcessorSupport
extends|extends
name|DelegateProcessor
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
name|Delayer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|stoppedLatch
specifier|private
name|CountDownLatch
name|stoppedLatch
init|=
operator|new
name|CountDownLatch
argument_list|(
literal|1
argument_list|)
decl_stmt|;
DECL|field|fastStop
specifier|private
name|boolean
name|fastStop
init|=
literal|true
decl_stmt|;
DECL|method|DelayProcessorSupport (Processor processor)
specifier|public
name|DelayProcessorSupport
parameter_list|(
name|Processor
name|processor
parameter_list|)
block|{
name|super
argument_list|(
name|processor
argument_list|)
expr_stmt|;
block|}
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
name|delay
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|super
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
DECL|method|isFastStop ()
specifier|public
name|boolean
name|isFastStop
parameter_list|()
block|{
return|return
name|fastStop
return|;
block|}
comment|/**      * Enables& disables a fast stop; basically to avoid waiting a possibly      * long time for delays to complete before the context shuts down; instead      * the current processing method throws      * {@link org.apache.camel.AlreadyStoppedException} to terminate processing.      */
DECL|method|setFastStop (boolean fastStop)
specifier|public
name|void
name|setFastStop
parameter_list|(
name|boolean
name|fastStop
parameter_list|)
block|{
name|this
operator|.
name|fastStop
operator|=
name|fastStop
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
name|stoppedLatch
operator|.
name|countDown
argument_list|()
expr_stmt|;
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
block|}
DECL|method|delay (Exchange exchange)
specifier|protected
specifier|abstract
name|void
name|delay
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Wait until the given system time before continuing      *       * @param time the system time to wait for      * @param exchange the exchange being processed      */
DECL|method|waitUntil (long time, Exchange exchange)
specifier|protected
name|void
name|waitUntil
parameter_list|(
name|long
name|time
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
while|while
condition|(
literal|true
condition|)
block|{
name|long
name|delay
init|=
name|time
operator|-
name|currentSystemTime
argument_list|()
decl_stmt|;
if|if
condition|(
name|delay
operator|<
literal|0
condition|)
block|{
return|return;
block|}
else|else
block|{
if|if
condition|(
name|isFastStop
argument_list|()
operator|&&
operator|!
name|isRunAllowed
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|AlreadyStoppedException
argument_list|()
throw|;
block|}
try|try
block|{
name|sleep
argument_list|(
name|delay
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
name|handleSleepInteruptedException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
DECL|method|sleep (long delay)
specifier|protected
name|void
name|sleep
parameter_list|(
name|long
name|delay
parameter_list|)
throws|throws
name|InterruptedException
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
literal|"Sleeping for: "
operator|+
name|delay
operator|+
literal|" millis"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|isFastStop
argument_list|()
condition|)
block|{
name|stoppedLatch
operator|.
name|await
argument_list|(
name|delay
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|Thread
operator|.
name|sleep
argument_list|(
name|delay
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Called when a sleep is interupted; allows derived classes to handle this      * case differently      */
DECL|method|handleSleepInteruptedException (InterruptedException e)
specifier|protected
name|void
name|handleSleepInteruptedException
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Sleep interupted: "
operator|+
name|e
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
DECL|method|currentSystemTime ()
specifier|protected
name|long
name|currentSystemTime
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

