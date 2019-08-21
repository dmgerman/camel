begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|ScheduledThreadPoolExecutor
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
name|support
operator|.
name|DefaultEndpoint
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
name|ScheduledPollConsumer
import|;
end_import

begin_class
DECL|class|MockScheduledPollConsumer
specifier|public
class|class
name|MockScheduledPollConsumer
extends|extends
name|ScheduledPollConsumer
block|{
DECL|field|exceptionToThrowOnPoll
specifier|private
name|Exception
name|exceptionToThrowOnPoll
decl_stmt|;
DECL|method|MockScheduledPollConsumer (DefaultEndpoint endpoint, Processor processor)
specifier|public
name|MockScheduledPollConsumer
parameter_list|(
name|DefaultEndpoint
name|endpoint
parameter_list|,
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
comment|// dummy constructor here - we just want to test the run() method, which
comment|// calls poll()
DECL|method|MockScheduledPollConsumer (Endpoint endpoint, Exception exceptionToThrowOnPoll)
specifier|public
name|MockScheduledPollConsumer
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|Exception
name|exceptionToThrowOnPoll
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|,
literal|null
argument_list|,
operator|new
name|ScheduledThreadPoolExecutor
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|exceptionToThrowOnPoll
operator|=
name|exceptionToThrowOnPoll
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
if|if
condition|(
name|exceptionToThrowOnPoll
operator|!=
literal|null
condition|)
block|{
throw|throw
name|exceptionToThrowOnPoll
throw|;
block|}
return|return
literal|0
return|;
block|}
DECL|method|setExceptionToThrowOnPoll (Exception exceptionToThrowOnPoll)
specifier|public
name|void
name|setExceptionToThrowOnPoll
parameter_list|(
name|Exception
name|exceptionToThrowOnPoll
parameter_list|)
block|{
name|this
operator|.
name|exceptionToThrowOnPoll
operator|=
name|exceptionToThrowOnPoll
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
literal|"MockScheduled"
return|;
block|}
block|}
end_class

end_unit

