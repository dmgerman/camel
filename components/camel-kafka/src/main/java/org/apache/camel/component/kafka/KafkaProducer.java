begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.kafka
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|kafka
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Properties
import|;
end_import

begin_import
import|import
name|kafka
operator|.
name|javaapi
operator|.
name|producer
operator|.
name|Producer
import|;
end_import

begin_import
import|import
name|kafka
operator|.
name|producer
operator|.
name|KeyedMessage
import|;
end_import

begin_import
import|import
name|kafka
operator|.
name|producer
operator|.
name|ProducerConfig
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
name|CamelException
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
name|CamelExchangeException
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
name|impl
operator|.
name|DefaultProducer
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
DECL|class|KafkaProducer
specifier|public
class|class
name|KafkaProducer
extends|extends
name|DefaultProducer
block|{
DECL|field|producer
specifier|protected
name|Producer
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|producer
decl_stmt|;
DECL|field|endpoint
specifier|private
specifier|final
name|KafkaEndpoint
name|endpoint
decl_stmt|;
DECL|method|KafkaProducer (KafkaEndpoint endpoint)
specifier|public
name|KafkaProducer
parameter_list|(
name|KafkaEndpoint
name|endpoint
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|producer
operator|!=
literal|null
condition|)
block|{
name|producer
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|getProps ()
name|Properties
name|getProps
parameter_list|()
block|{
name|Properties
name|props
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|props
operator|.
name|put
argument_list|(
literal|"metadata.broker.list"
argument_list|,
name|endpoint
operator|.
name|getBrokers
argument_list|()
argument_list|)
expr_stmt|;
name|props
operator|.
name|put
argument_list|(
literal|"serializer.class"
argument_list|,
literal|"kafka.serializer.StringEncoder"
argument_list|)
expr_stmt|;
name|props
operator|.
name|put
argument_list|(
literal|"partitioner.class"
argument_list|,
name|endpoint
operator|.
name|getPartitioner
argument_list|()
argument_list|)
expr_stmt|;
name|props
operator|.
name|put
argument_list|(
literal|"request.required.acks"
argument_list|,
literal|"1"
argument_list|)
expr_stmt|;
return|return
name|props
return|;
block|}
annotation|@
name|Override
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
name|Properties
name|props
init|=
name|getProps
argument_list|()
decl_stmt|;
name|ProducerConfig
name|config
init|=
operator|new
name|ProducerConfig
argument_list|(
name|props
argument_list|)
decl_stmt|;
name|producer
operator|=
operator|new
name|Producer
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|(
name|config
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|process (Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|CamelException
block|{
name|Object
name|partitionKey
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|KafkaConstants
operator|.
name|PARTITION_KEY
argument_list|)
decl_stmt|;
if|if
condition|(
name|partitionKey
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CamelExchangeException
argument_list|(
literal|"No partition key set"
argument_list|,
name|exchange
argument_list|)
throw|;
block|}
name|String
name|topic
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|KafkaConstants
operator|.
name|TOPIC
argument_list|,
name|endpoint
operator|.
name|getTopic
argument_list|()
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|topic
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CamelExchangeException
argument_list|(
literal|"No topic key set"
argument_list|,
name|exchange
argument_list|)
throw|;
block|}
name|String
name|msg
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|KeyedMessage
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|data
init|=
operator|new
name|KeyedMessage
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|(
name|topic
argument_list|,
name|partitionKey
operator|.
name|toString
argument_list|()
argument_list|,
name|msg
argument_list|)
decl_stmt|;
name|producer
operator|.
name|send
argument_list|(
name|data
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

