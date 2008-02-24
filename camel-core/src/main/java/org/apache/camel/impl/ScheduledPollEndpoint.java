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
name|Component
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
name|util
operator|.
name|IntrospectionSupport
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

begin_comment
comment|/**  * A base class for {@link Endpoint} which creates a {@link ScheduledPollConsumer}  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|ScheduledPollEndpoint
specifier|public
specifier|abstract
class|class
name|ScheduledPollEndpoint
parameter_list|<
name|E
extends|extends
name|Exchange
parameter_list|>
extends|extends
name|DefaultEndpoint
argument_list|<
name|E
argument_list|>
block|{
DECL|field|consumerProperties
specifier|private
name|Map
name|consumerProperties
decl_stmt|;
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
DECL|method|getConsumerProperties ()
specifier|public
name|Map
name|getConsumerProperties
parameter_list|()
block|{
return|return
name|consumerProperties
return|;
block|}
DECL|method|setConsumerProperties (Map consumerProperties)
specifier|public
name|void
name|setConsumerProperties
parameter_list|(
name|Map
name|consumerProperties
parameter_list|)
block|{
name|this
operator|.
name|consumerProperties
operator|=
name|consumerProperties
expr_stmt|;
block|}
DECL|method|configureConsumer (Consumer<E> consumer)
specifier|protected
name|void
name|configureConsumer
parameter_list|(
name|Consumer
argument_list|<
name|E
argument_list|>
name|consumer
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|consumerProperties
operator|!=
literal|null
condition|)
block|{
comment|// TODO pass in type converter
name|IntrospectionSupport
operator|.
name|setProperties
argument_list|(
name|getContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
argument_list|,
name|consumer
argument_list|,
name|consumerProperties
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|configureProperties (Map options)
specifier|public
name|void
name|configureProperties
parameter_list|(
name|Map
name|options
parameter_list|)
block|{
name|Map
name|consumerProperties
init|=
name|IntrospectionSupport
operator|.
name|extractProperties
argument_list|(
name|options
argument_list|,
literal|"consumer."
argument_list|)
decl_stmt|;
if|if
condition|(
name|consumerProperties
operator|!=
literal|null
condition|)
block|{
name|setConsumerProperties
argument_list|(
name|consumerProperties
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

