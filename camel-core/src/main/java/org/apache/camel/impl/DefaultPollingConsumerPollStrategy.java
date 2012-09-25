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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Consumer
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
name|spi
operator|.
name|PollingConsumerPollStrategy
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
comment|/**  * A default implementation that will not retry on rollback.  *  * @version   */
end_comment

begin_class
DECL|class|DefaultPollingConsumerPollStrategy
specifier|public
class|class
name|DefaultPollingConsumerPollStrategy
implements|implements
name|PollingConsumerPollStrategy
block|{
DECL|field|log
specifier|protected
specifier|final
specifier|transient
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
DECL|method|begin (Consumer consumer, Endpoint endpoint)
specifier|public
name|boolean
name|begin
parameter_list|(
name|Consumer
name|consumer
parameter_list|,
name|Endpoint
name|endpoint
parameter_list|)
block|{
return|return
literal|true
return|;
block|}
DECL|method|commit (Consumer consumer, Endpoint endpoint, int polledMessages)
specifier|public
name|void
name|commit
parameter_list|(
name|Consumer
name|consumer
parameter_list|,
name|Endpoint
name|endpoint
parameter_list|,
name|int
name|polledMessages
parameter_list|)
block|{
comment|// noop
block|}
DECL|method|rollback (Consumer consumer, Endpoint endpoint, int retryCounter, Exception e)
specifier|public
name|boolean
name|rollback
parameter_list|(
name|Consumer
name|consumer
parameter_list|,
name|Endpoint
name|endpoint
parameter_list|,
name|int
name|retryCounter
parameter_list|,
name|Exception
name|e
parameter_list|)
throws|throws
name|Exception
block|{
comment|// we do not want to retry
return|return
literal|false
return|;
block|}
block|}
end_class

end_unit

