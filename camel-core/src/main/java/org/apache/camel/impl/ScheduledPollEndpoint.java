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
name|ResolveEndpointFailedException
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
name|EndpointHelper
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

begin_comment
comment|/**  * A base class for {@link org.apache.camel.Endpoint} which creates a {@link ScheduledPollConsumer}  *  * @version $Revision$  */
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
DECL|method|configureConsumer (Consumer consumer)
specifier|protected
name|void
name|configureConsumer
parameter_list|(
name|Consumer
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
comment|// set reference properties first as they use # syntax that fools the regular properties setter
name|EndpointHelper
operator|.
name|setReferenceProperties
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
name|consumer
argument_list|,
name|consumerProperties
argument_list|)
expr_stmt|;
name|EndpointHelper
operator|.
name|setProperties
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
name|consumer
argument_list|,
name|consumerProperties
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|this
operator|.
name|isLenientProperties
argument_list|()
operator|&&
name|consumerProperties
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
throw|throw
operator|new
name|ResolveEndpointFailedException
argument_list|(
name|this
operator|.
name|getEndpointUri
argument_list|()
argument_list|,
literal|"There are "
operator|+
name|consumerProperties
operator|.
name|size
argument_list|()
operator|+
literal|" parameters that couldn't be set on the endpoint consumer."
operator|+
literal|" Check the uri if the parameters are spelt correctly and that they are properties of the endpoint."
operator|+
literal|" Unknown consumer parameters=["
operator|+
name|consumerProperties
operator|+
literal|"]"
argument_list|)
throw|;
block|}
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
name|configureScheduledPollConsumerProperties
argument_list|(
name|options
argument_list|,
name|consumerProperties
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|configureScheduledPollConsumerProperties (Map options, Map consumerProperties)
specifier|private
name|void
name|configureScheduledPollConsumerProperties
parameter_list|(
name|Map
name|options
parameter_list|,
name|Map
name|consumerProperties
parameter_list|)
block|{
comment|// special for scheduled poll consumers as we want to allow end users to configure its options
comment|// from the URI parameters without the consumer. prefix
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
block|}
block|}
block|}
end_class

end_unit

