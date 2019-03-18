begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.aws.xray.decorators.messaging
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|aws
operator|.
name|xray
operator|.
name|decorators
operator|.
name|messaging
package|;
end_package

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
name|com
operator|.
name|amazonaws
operator|.
name|xray
operator|.
name|entities
operator|.
name|Entity
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

begin_class
DECL|class|KafkaSegmentDecorator
specifier|public
class|class
name|KafkaSegmentDecorator
extends|extends
name|AbstractMessagingSegmentDecorator
block|{
DECL|field|KAFKA_PARTITION_TAG
specifier|public
specifier|static
specifier|final
name|String
name|KAFKA_PARTITION_TAG
init|=
literal|"kafka.partition"
decl_stmt|;
DECL|field|KAFKA_PARTITION_KEY_TAG
specifier|public
specifier|static
specifier|final
name|String
name|KAFKA_PARTITION_KEY_TAG
init|=
literal|"kafka.partition.key"
decl_stmt|;
DECL|field|KAFKA_KEY_TAG
specifier|public
specifier|static
specifier|final
name|String
name|KAFKA_KEY_TAG
init|=
literal|"kafka.key"
decl_stmt|;
DECL|field|KAFKA_OFFSET_TAG
specifier|public
specifier|static
specifier|final
name|String
name|KAFKA_OFFSET_TAG
init|=
literal|"kafka.offset"
decl_stmt|;
comment|/**      * Constants copied from {@link org.apache.camel.component.kafka.KafkaConstants}      */
DECL|field|PARTITION_KEY
specifier|protected
specifier|static
specifier|final
name|String
name|PARTITION_KEY
init|=
literal|"kafka.PARTITION_KEY"
decl_stmt|;
DECL|field|PARTITION
specifier|protected
specifier|static
specifier|final
name|String
name|PARTITION
init|=
literal|"kafka.PARTITION"
decl_stmt|;
DECL|field|KEY
specifier|protected
specifier|static
specifier|final
name|String
name|KEY
init|=
literal|"kafka.KEY"
decl_stmt|;
DECL|field|TOPIC
specifier|protected
specifier|static
specifier|final
name|String
name|TOPIC
init|=
literal|"kafka.TOPIC"
decl_stmt|;
DECL|field|OFFSET
specifier|protected
specifier|static
specifier|final
name|String
name|OFFSET
init|=
literal|"kafka.OFFSET"
decl_stmt|;
annotation|@
name|Override
DECL|method|getComponent ()
specifier|public
name|String
name|getComponent
parameter_list|()
block|{
return|return
literal|"kafka"
return|;
block|}
annotation|@
name|Override
DECL|method|getDestination (Exchange exchange, Endpoint endpoint)
specifier|public
name|String
name|getDestination
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Endpoint
name|endpoint
parameter_list|)
block|{
name|String
name|topic
init|=
operator|(
name|String
operator|)
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|TOPIC
argument_list|)
decl_stmt|;
if|if
condition|(
name|topic
operator|==
literal|null
condition|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|queryParameters
init|=
name|toQueryParameters
argument_list|(
name|endpoint
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
decl_stmt|;
name|topic
operator|=
name|queryParameters
operator|.
name|get
argument_list|(
literal|"topic"
argument_list|)
expr_stmt|;
block|}
return|return
name|topic
operator|!=
literal|null
condition|?
name|topic
else|:
name|super
operator|.
name|getDestination
argument_list|(
name|exchange
argument_list|,
name|endpoint
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|pre (Entity segment, Exchange exchange, Endpoint endpoint)
specifier|public
name|void
name|pre
parameter_list|(
name|Entity
name|segment
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|Endpoint
name|endpoint
parameter_list|)
block|{
name|super
operator|.
name|pre
argument_list|(
name|segment
argument_list|,
name|exchange
argument_list|,
name|endpoint
argument_list|)
expr_stmt|;
name|String
name|partition
init|=
operator|(
name|String
operator|)
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|PARTITION
argument_list|)
decl_stmt|;
if|if
condition|(
name|partition
operator|!=
literal|null
condition|)
block|{
name|segment
operator|.
name|putMetadata
argument_list|(
name|KAFKA_PARTITION_TAG
argument_list|,
name|partition
argument_list|)
expr_stmt|;
block|}
name|String
name|partitionKey
init|=
operator|(
name|String
operator|)
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|PARTITION_KEY
argument_list|)
decl_stmt|;
if|if
condition|(
name|partitionKey
operator|!=
literal|null
condition|)
block|{
name|segment
operator|.
name|putMetadata
argument_list|(
name|KAFKA_PARTITION_KEY_TAG
argument_list|,
name|partitionKey
argument_list|)
expr_stmt|;
block|}
name|String
name|key
init|=
operator|(
name|String
operator|)
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|KEY
argument_list|)
decl_stmt|;
if|if
condition|(
name|key
operator|!=
literal|null
condition|)
block|{
name|segment
operator|.
name|putMetadata
argument_list|(
name|KAFKA_KEY_TAG
argument_list|,
name|key
argument_list|)
expr_stmt|;
block|}
name|String
name|offset
init|=
operator|(
name|String
operator|)
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|OFFSET
argument_list|)
decl_stmt|;
if|if
condition|(
name|offset
operator|!=
literal|null
condition|)
block|{
name|segment
operator|.
name|putMetadata
argument_list|(
name|KAFKA_OFFSET_TAG
argument_list|,
name|offset
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

