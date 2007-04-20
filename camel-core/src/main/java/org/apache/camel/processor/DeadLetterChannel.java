begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|camel
operator|.
name|impl
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
name|ServiceHelper
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
comment|/**  * Implements a  *<a href="http://activemq.apache.org/camel/dead-letter-channel.html">Dead Letter Channel</a>  * after attempting to redeliver the message using the {@link RedeliveryPolicy}  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|DeadLetterChannel
specifier|public
class|class
name|DeadLetterChannel
parameter_list|<
name|E
extends|extends
name|Exchange
parameter_list|>
extends|extends
name|ServiceSupport
implements|implements
name|ErrorHandler
argument_list|<
name|E
argument_list|>
block|{
DECL|field|REDELIVERY_COUNTER
specifier|public
specifier|static
specifier|final
name|String
name|REDELIVERY_COUNTER
init|=
literal|"org.apache.camel.RedeliveryCounter"
decl_stmt|;
DECL|field|REDELIVERED
specifier|public
specifier|static
specifier|final
name|String
name|REDELIVERED
init|=
literal|"org.apache.camel.Redelivered"
decl_stmt|;
DECL|field|log
specifier|private
specifier|static
specifier|final
specifier|transient
name|Log
name|log
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|DeadLetterChannel
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|output
specifier|private
name|Processor
argument_list|<
name|E
argument_list|>
name|output
decl_stmt|;
DECL|field|deadLetter
specifier|private
name|Processor
argument_list|<
name|E
argument_list|>
name|deadLetter
decl_stmt|;
DECL|field|redeliveryPolicy
specifier|private
name|RedeliveryPolicy
name|redeliveryPolicy
decl_stmt|;
DECL|method|DeadLetterChannel (Processor<E> output, Processor<E> deadLetter)
specifier|public
name|DeadLetterChannel
parameter_list|(
name|Processor
argument_list|<
name|E
argument_list|>
name|output
parameter_list|,
name|Processor
argument_list|<
name|E
argument_list|>
name|deadLetter
parameter_list|)
block|{
name|this
argument_list|(
name|output
argument_list|,
name|deadLetter
argument_list|,
operator|new
name|RedeliveryPolicy
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|DeadLetterChannel (Processor<E> output, Processor<E> deadLetter, RedeliveryPolicy redeliveryPolicy)
specifier|public
name|DeadLetterChannel
parameter_list|(
name|Processor
argument_list|<
name|E
argument_list|>
name|output
parameter_list|,
name|Processor
argument_list|<
name|E
argument_list|>
name|deadLetter
parameter_list|,
name|RedeliveryPolicy
name|redeliveryPolicy
parameter_list|)
block|{
name|this
operator|.
name|deadLetter
operator|=
name|deadLetter
expr_stmt|;
name|this
operator|.
name|output
operator|=
name|output
expr_stmt|;
name|this
operator|.
name|redeliveryPolicy
operator|=
name|redeliveryPolicy
expr_stmt|;
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
literal|"DeadLetterChannel["
operator|+
name|output
operator|+
literal|", "
operator|+
name|deadLetter
operator|+
literal|", "
operator|+
name|redeliveryPolicy
operator|+
literal|"]"
return|;
block|}
DECL|method|process (E exchange)
specifier|public
name|void
name|process
parameter_list|(
name|E
name|exchange
parameter_list|)
block|{
name|int
name|redeliveryCounter
init|=
literal|0
decl_stmt|;
name|long
name|redeliveryDelay
init|=
literal|0
decl_stmt|;
do|do
block|{
if|if
condition|(
name|redeliveryCounter
operator|>
literal|0
condition|)
block|{
comment|// Figure out how long we should wait to resend this message.
name|redeliveryDelay
operator|=
name|redeliveryPolicy
operator|.
name|getRedeliveryDelay
argument_list|(
name|redeliveryDelay
argument_list|)
expr_stmt|;
name|sleep
argument_list|(
name|redeliveryDelay
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|output
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
return|return;
block|}
catch|catch
parameter_list|(
name|RuntimeException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"On delivery attempt: "
operator|+
name|redeliveryCounter
operator|+
literal|" caught: "
operator|+
name|e
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
name|redeliveryCounter
operator|=
name|incrementRedeliveryCounter
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
do|while
condition|(
name|redeliveryPolicy
operator|.
name|shouldRedeliver
argument_list|(
name|redeliveryCounter
argument_list|)
condition|)
do|;
comment|// now lets send to the dead letter queue
name|deadLetter
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
comment|// Properties
comment|//-------------------------------------------------------------------------
comment|/**      * Returns the output processor      */
DECL|method|getOutput ()
specifier|public
name|Processor
argument_list|<
name|E
argument_list|>
name|getOutput
parameter_list|()
block|{
return|return
name|output
return|;
block|}
comment|/**      * Returns the dead letter that message exchanges will be sent to if the redelivery attempts fail      */
DECL|method|getDeadLetter ()
specifier|public
name|Processor
argument_list|<
name|E
argument_list|>
name|getDeadLetter
parameter_list|()
block|{
return|return
name|deadLetter
return|;
block|}
DECL|method|getRedeliveryPolicy ()
specifier|public
name|RedeliveryPolicy
name|getRedeliveryPolicy
parameter_list|()
block|{
return|return
name|redeliveryPolicy
return|;
block|}
comment|/**      * Sets the redelivery policy      */
DECL|method|setRedeliveryPolicy (RedeliveryPolicy redeliveryPolicy)
specifier|public
name|void
name|setRedeliveryPolicy
parameter_list|(
name|RedeliveryPolicy
name|redeliveryPolicy
parameter_list|)
block|{
name|this
operator|.
name|redeliveryPolicy
operator|=
name|redeliveryPolicy
expr_stmt|;
block|}
comment|// Implementation methods
comment|//-------------------------------------------------------------------------
comment|/**      * Increments the redelivery counter and adds the redelivered flag if the message has been redelivered      */
DECL|method|incrementRedeliveryCounter (E exchange)
specifier|protected
name|int
name|incrementRedeliveryCounter
parameter_list|(
name|E
name|exchange
parameter_list|)
block|{
name|Integer
name|counter
init|=
name|exchange
operator|.
name|getProperty
argument_list|(
name|REDELIVERY_COUNTER
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
decl_stmt|;
name|int
name|next
init|=
literal|1
decl_stmt|;
if|if
condition|(
name|counter
operator|!=
literal|null
condition|)
block|{
name|next
operator|=
name|counter
operator|+
literal|1
expr_stmt|;
block|}
name|exchange
operator|.
name|setProperty
argument_list|(
name|REDELIVERY_COUNTER
argument_list|,
name|next
argument_list|)
expr_stmt|;
if|if
condition|(
name|next
operator|>
literal|1
condition|)
block|{
name|exchange
operator|.
name|setProperty
argument_list|(
name|REDELIVERED
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
return|return
name|next
return|;
block|}
DECL|method|sleep (long redeliveryDelay)
specifier|protected
name|void
name|sleep
parameter_list|(
name|long
name|redeliveryDelay
parameter_list|)
block|{
if|if
condition|(
name|redeliveryDelay
operator|>
literal|0
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
literal|"Sleeping for: "
operator|+
name|redeliveryDelay
operator|+
literal|" until attempting redelivery"
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|Thread
operator|.
name|sleep
argument_list|(
name|redeliveryDelay
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
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
literal|"Thread interupted: "
operator|+
name|e
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
name|ServiceHelper
operator|.
name|startServices
argument_list|(
name|output
argument_list|,
name|deadLetter
argument_list|)
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
name|ServiceHelper
operator|.
name|stopServices
argument_list|(
name|deadLetter
argument_list|,
name|output
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

