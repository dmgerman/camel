begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.salesforce
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|salesforce
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
name|Producer
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
name|component
operator|.
name|salesforce
operator|.
name|internal
operator|.
name|OperationName
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
name|impl
operator|.
name|SynchronousDelegateProducer
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
name|UriEndpoint
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
name|UriParam
import|;
end_import

begin_comment
comment|/**  * Represents a Salesforce endpoint.  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|scheme
operator|=
literal|"salesforce"
argument_list|,
name|consumerClass
operator|=
name|SalesforceConsumer
operator|.
name|class
argument_list|)
DECL|class|SalesforceEndpoint
specifier|public
class|class
name|SalesforceEndpoint
extends|extends
name|DefaultEndpoint
block|{
annotation|@
name|UriParam
DECL|field|config
specifier|private
specifier|final
name|SalesforceEndpointConfig
name|config
decl_stmt|;
DECL|field|operationName
specifier|private
specifier|final
name|OperationName
name|operationName
decl_stmt|;
DECL|field|topicName
specifier|private
specifier|final
name|String
name|topicName
decl_stmt|;
DECL|method|SalesforceEndpoint (String uri, SalesforceComponent salesforceComponent, SalesforceEndpointConfig config, OperationName operationName, String topicName)
specifier|public
name|SalesforceEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|SalesforceComponent
name|salesforceComponent
parameter_list|,
name|SalesforceEndpointConfig
name|config
parameter_list|,
name|OperationName
name|operationName
parameter_list|,
name|String
name|topicName
parameter_list|)
block|{
name|super
argument_list|(
name|uri
argument_list|,
name|salesforceComponent
argument_list|)
expr_stmt|;
name|this
operator|.
name|config
operator|=
name|config
expr_stmt|;
name|this
operator|.
name|operationName
operator|=
name|operationName
expr_stmt|;
name|this
operator|.
name|topicName
operator|=
name|topicName
expr_stmt|;
block|}
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
comment|// producer requires an operation, topicName must be the invalid operation name
if|if
condition|(
name|operationName
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Invalid Operation %s"
argument_list|,
name|topicName
argument_list|)
argument_list|)
throw|;
block|}
name|SalesforceProducer
name|producer
init|=
operator|new
name|SalesforceProducer
argument_list|(
name|this
argument_list|)
decl_stmt|;
if|if
condition|(
name|isSynchronous
argument_list|()
condition|)
block|{
return|return
operator|new
name|SynchronousDelegateProducer
argument_list|(
name|producer
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|producer
return|;
block|}
block|}
DECL|method|createConsumer (Processor processor)
specifier|public
name|Consumer
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
comment|// consumer requires a topicName, operation name must be the invalid topic name
if|if
condition|(
name|topicName
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Invalid topic name %s, matches a producer operation name"
argument_list|,
name|operationName
operator|.
name|value
argument_list|()
argument_list|)
argument_list|)
throw|;
block|}
specifier|final
name|SalesforceConsumer
name|consumer
init|=
operator|new
name|SalesforceConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|,
name|getComponent
argument_list|()
operator|.
name|getSubscriptionHelper
argument_list|()
argument_list|)
decl_stmt|;
name|configureConsumer
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
return|return
name|consumer
return|;
block|}
annotation|@
name|Override
DECL|method|getComponent ()
specifier|public
name|SalesforceComponent
name|getComponent
parameter_list|()
block|{
return|return
operator|(
name|SalesforceComponent
operator|)
name|super
operator|.
name|getComponent
argument_list|()
return|;
block|}
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
comment|// re-use endpoint instance across multiple threads
comment|// the description of this method is a little confusing
return|return
literal|true
return|;
block|}
DECL|method|getConfiguration ()
specifier|public
name|SalesforceEndpointConfig
name|getConfiguration
parameter_list|()
block|{
return|return
name|config
return|;
block|}
DECL|method|getOperationName ()
specifier|public
name|OperationName
name|getOperationName
parameter_list|()
block|{
return|return
name|operationName
return|;
block|}
DECL|method|getTopicName ()
specifier|public
name|String
name|getTopicName
parameter_list|()
block|{
return|return
name|topicName
return|;
block|}
block|}
end_class

end_unit

