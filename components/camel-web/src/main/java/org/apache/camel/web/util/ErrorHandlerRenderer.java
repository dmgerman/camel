begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.web.util
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|web
operator|.
name|util
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
name|builder
operator|.
name|DeadLetterChannelBuilder
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
name|ErrorHandlerBuilder
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
name|processor
operator|.
name|RedeliveryPolicy
import|;
end_import

begin_comment
comment|/**  *   */
end_comment

begin_class
DECL|class|ErrorHandlerRenderer
specifier|public
specifier|final
class|class
name|ErrorHandlerRenderer
block|{
DECL|method|ErrorHandlerRenderer ()
specifier|private
name|ErrorHandlerRenderer
parameter_list|()
block|{
comment|// Utility class, no public or protected default constructor
block|}
DECL|method|render (StringBuilder buffer, ErrorHandlerBuilder errorHandler)
specifier|public
specifier|static
name|void
name|render
parameter_list|(
name|StringBuilder
name|buffer
parameter_list|,
name|ErrorHandlerBuilder
name|errorHandler
parameter_list|)
block|{
if|if
condition|(
name|errorHandler
operator|instanceof
name|DeadLetterChannelBuilder
condition|)
block|{
name|DeadLetterChannelBuilder
name|deadLetter
init|=
operator|(
name|DeadLetterChannelBuilder
operator|)
name|errorHandler
decl_stmt|;
name|buffer
operator|.
name|append
argument_list|(
literal|"errorHandler(deadLetterChannel(\""
argument_list|)
operator|.
name|append
argument_list|(
name|deadLetter
operator|.
name|getDeadLetterUri
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|"\")"
argument_list|)
expr_stmt|;
name|int
name|maxRediliveries
init|=
name|deadLetter
operator|.
name|getRedeliveryPolicy
argument_list|()
operator|.
name|getMaximumRedeliveries
argument_list|()
decl_stmt|;
if|if
condition|(
name|maxRediliveries
operator|!=
literal|0
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|".maximumRedeliveries("
argument_list|)
operator|.
name|append
argument_list|(
name|maxRediliveries
argument_list|)
operator|.
name|append
argument_list|(
literal|")"
argument_list|)
expr_stmt|;
block|}
name|RedeliveryPolicy
name|redelivery
init|=
name|deadLetter
operator|.
name|getRedeliveryPolicy
argument_list|()
decl_stmt|;
name|long
name|redeliverDelay
init|=
name|redelivery
operator|.
name|getRedeliverDelay
argument_list|()
decl_stmt|;
if|if
condition|(
name|redeliverDelay
operator|!=
literal|1000
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|".redeliverDelay("
argument_list|)
operator|.
name|append
argument_list|(
name|redeliverDelay
argument_list|)
operator|.
name|append
argument_list|(
literal|")"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|redelivery
operator|.
name|isLogStackTrace
argument_list|()
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|".logStackTrace(true)"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|deadLetter
operator|.
name|getHandledPolicy
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|String
name|handledPolicy
init|=
name|deadLetter
operator|.
name|getHandledPolicy
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
if|if
condition|(
name|handledPolicy
operator|.
name|equals
argument_list|(
literal|"false"
argument_list|)
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|".handled("
argument_list|)
operator|.
name|append
argument_list|(
name|handledPolicy
argument_list|)
operator|.
name|append
argument_list|(
literal|")"
argument_list|)
expr_stmt|;
block|}
block|}
name|buffer
operator|.
name|append
argument_list|(
literal|");"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

