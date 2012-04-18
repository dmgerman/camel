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
name|HashMap
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|CamelContext
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
name|Component
import|;
end_import

begin_comment
comment|/**  * A base class for {@link org.apache.camel.Endpoint} which creates a {@link ScheduledPollConsumer}  *  * @version   */
end_comment

begin_class
DECL|class|ScheduledPollEndpoint
specifier|public
specifier|abstract
class|class
name|ScheduledPollEndpoint
extends|extends
name|DefaultEndpoint
block|{
DECL|method|ScheduledPollEndpoint (String endpointUri, Component component)
specifier|protected
name|ScheduledPollEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|Component
name|component
parameter_list|)
block|{
name|super
argument_list|(
name|endpointUri
argument_list|,
name|component
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Deprecated
DECL|method|ScheduledPollEndpoint (String endpointUri, CamelContext context)
specifier|protected
name|ScheduledPollEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|CamelContext
name|context
parameter_list|)
block|{
name|super
argument_list|(
name|endpointUri
argument_list|,
name|context
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Deprecated
DECL|method|ScheduledPollEndpoint (String endpointUri)
specifier|protected
name|ScheduledPollEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|)
block|{
name|super
argument_list|(
name|endpointUri
argument_list|)
expr_stmt|;
block|}
DECL|method|ScheduledPollEndpoint ()
specifier|protected
name|ScheduledPollEndpoint
parameter_list|()
block|{     }
DECL|method|configureProperties (Map<String, Object> options)
specifier|public
name|void
name|configureProperties
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|options
parameter_list|)
block|{
name|super
operator|.
name|configureProperties
argument_list|(
name|options
argument_list|)
expr_stmt|;
name|configureScheduledPollConsumerProperties
argument_list|(
name|options
argument_list|,
name|getConsumerProperties
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|configureScheduledPollConsumerProperties (Map<String, Object> options, Map<String, Object> consumerProperties)
specifier|private
name|void
name|configureScheduledPollConsumerProperties
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|options
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|consumerProperties
parameter_list|)
block|{
comment|// special for scheduled poll consumers as we want to allow end users to configure its options
comment|// from the URI parameters without the consumer. prefix
name|Object
name|startScheduler
init|=
name|options
operator|.
name|remove
argument_list|(
literal|"startScheduler"
argument_list|)
decl_stmt|;
name|Object
name|initialDelay
init|=
name|options
operator|.
name|remove
argument_list|(
literal|"initialDelay"
argument_list|)
decl_stmt|;
name|Object
name|delay
init|=
name|options
operator|.
name|remove
argument_list|(
literal|"delay"
argument_list|)
decl_stmt|;
name|Object
name|timeUnit
init|=
name|options
operator|.
name|remove
argument_list|(
literal|"timeUnit"
argument_list|)
decl_stmt|;
name|Object
name|useFixedDelay
init|=
name|options
operator|.
name|remove
argument_list|(
literal|"useFixedDelay"
argument_list|)
decl_stmt|;
name|Object
name|pollStrategy
init|=
name|options
operator|.
name|remove
argument_list|(
literal|"pollStrategy"
argument_list|)
decl_stmt|;
name|Object
name|runLoggingLevel
init|=
name|options
operator|.
name|remove
argument_list|(
literal|"runLoggingLevel"
argument_list|)
decl_stmt|;
name|Object
name|sendEmptyMessageWhenIdle
init|=
name|options
operator|.
name|remove
argument_list|(
literal|"sendEmptyMessageWhenIdle"
argument_list|)
decl_stmt|;
name|boolean
name|setConsumerProperties
init|=
literal|false
decl_stmt|;
comment|// the following is split into two if statements to satisfy the checkstyle max complexity constraint
if|if
condition|(
name|initialDelay
operator|!=
literal|null
operator|||
name|delay
operator|!=
literal|null
operator|||
name|timeUnit
operator|!=
literal|null
operator|||
name|useFixedDelay
operator|!=
literal|null
operator|||
name|pollStrategy
operator|!=
literal|null
condition|)
block|{
name|setConsumerProperties
operator|=
literal|true
expr_stmt|;
block|}
if|if
condition|(
name|runLoggingLevel
operator|!=
literal|null
operator|||
name|startScheduler
operator|!=
literal|null
operator|||
name|sendEmptyMessageWhenIdle
operator|!=
literal|null
condition|)
block|{
name|setConsumerProperties
operator|=
literal|true
expr_stmt|;
block|}
if|if
condition|(
name|setConsumerProperties
condition|)
block|{
if|if
condition|(
name|consumerProperties
operator|==
literal|null
condition|)
block|{
name|consumerProperties
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|initialDelay
operator|!=
literal|null
condition|)
block|{
name|consumerProperties
operator|.
name|put
argument_list|(
literal|"initialDelay"
argument_list|,
name|initialDelay
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|startScheduler
operator|!=
literal|null
condition|)
block|{
name|consumerProperties
operator|.
name|put
argument_list|(
literal|"startScheduler"
argument_list|,
name|startScheduler
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|delay
operator|!=
literal|null
condition|)
block|{
name|consumerProperties
operator|.
name|put
argument_list|(
literal|"delay"
argument_list|,
name|delay
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|timeUnit
operator|!=
literal|null
condition|)
block|{
name|consumerProperties
operator|.
name|put
argument_list|(
literal|"timeUnit"
argument_list|,
name|timeUnit
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|useFixedDelay
operator|!=
literal|null
condition|)
block|{
name|consumerProperties
operator|.
name|put
argument_list|(
literal|"useFixedDelay"
argument_list|,
name|useFixedDelay
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|pollStrategy
operator|!=
literal|null
condition|)
block|{
name|consumerProperties
operator|.
name|put
argument_list|(
literal|"pollStrategy"
argument_list|,
name|pollStrategy
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|runLoggingLevel
operator|!=
literal|null
condition|)
block|{
name|consumerProperties
operator|.
name|put
argument_list|(
literal|"runLoggingLevel"
argument_list|,
name|runLoggingLevel
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|sendEmptyMessageWhenIdle
operator|!=
literal|null
condition|)
block|{
name|consumerProperties
operator|.
name|put
argument_list|(
literal|"sendEmptyMessageWhenIdle"
argument_list|,
name|sendEmptyMessageWhenIdle
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

