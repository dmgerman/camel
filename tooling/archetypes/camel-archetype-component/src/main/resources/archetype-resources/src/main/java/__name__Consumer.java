begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
package|package
name|$
block|{
package|package
block|}
end_package

begin_empty_stmt
empty_stmt|;
end_empty_stmt

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
name|ScheduledPollConsumer
import|;
end_import

begin_comment
comment|/**  * The ${name} consumer.  */
end_comment

begin_class
DECL|class|$
specifier|public
class|class
name|$
block|{
name|name
block|}
end_class

begin_expr_stmt
DECL|class|$
name|Consumer
expr|extends
name|ScheduledPollConsumer
block|{
specifier|private
name|final
name|$
block|{
name|name
block|}
name|Endpoint
name|endpoint
block|;
specifier|public
name|$
block|{
name|name
block|}
name|Consumer
argument_list|(
name|$
block|{
name|name
block|}
name|Endpoint
name|endpoint
argument_list|,
name|Processor
name|processor
argument_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|,
name|processor
argument_list|)
block|;
name|this
operator|.
name|endpoint
operator|=
name|endpoint
block|;     }
expr|@
name|Override
specifier|protected
name|int
name|poll
argument_list|()
throws|throws
name|Exception
block|{
name|Exchange
name|exchange
operator|=
name|endpoint
operator|.
name|createExchange
argument_list|()
block|;
comment|// create a message body
name|Date
name|now
operator|=
operator|new
name|Date
argument_list|()
block|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"Hello World! The time is "
operator|+
name|now
argument_list|)
block|;
try|try
block|{
comment|// send message to next processor in the route
name|getProcessor
argument_list|()
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
return|return
literal|1
return|;
comment|// number of messages polled
block|}
end_expr_stmt

begin_finally
finally|finally
block|{
comment|// log exception if an exception occurred and was not handled
if|if
condition|(
name|exchange
operator|.
name|getException
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|getExceptionHandler
argument_list|()
operator|.
name|handleException
argument_list|(
literal|"Error processing exchange"
argument_list|,
name|exchange
argument_list|,
name|exchange
operator|.
name|getException
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_finally

unit|} }
end_unit

