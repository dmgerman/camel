begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.aws.kinesis
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
name|kinesis
package|;
end_package

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|kinesis
operator|.
name|AmazonKinesis
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|kinesis
operator|.
name|model
operator|.
name|Record
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|kinesis
operator|.
name|model
operator|.
name|ShardIteratorType
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
name|impl
operator|.
name|ScheduledPollEndpoint
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
name|Metadata
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
name|UriPath
import|;
end_import

begin_comment
comment|/**  * The aws-kinesis component is for consuming records from Amazon Kinesis Streams  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|scheme
operator|=
literal|"aws-kinesis"
argument_list|,
name|title
operator|=
literal|"AWS Kinesis"
argument_list|,
name|syntax
operator|=
literal|"aws-kinesis:streamName"
argument_list|,
name|consumerOnly
operator|=
literal|true
argument_list|,
name|consumerClass
operator|=
name|KinesisConsumer
operator|.
name|class
argument_list|,
name|label
operator|=
literal|"cloud,messaging"
argument_list|)
DECL|class|KinesisEndpoint
specifier|public
class|class
name|KinesisEndpoint
extends|extends
name|ScheduledPollEndpoint
block|{
annotation|@
name|UriPath
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|,
name|description
operator|=
literal|"Name of the stream"
argument_list|)
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|"true"
argument_list|)
DECL|field|streamName
specifier|private
name|String
name|streamName
decl_stmt|;
comment|// For now, always assume that we've been supplied a client in the Camel registry.
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|,
name|description
operator|=
literal|"Amazon Kinesis client to use for all requests for this endpoint"
argument_list|)
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|"true"
argument_list|)
DECL|field|amazonKinesisClient
specifier|private
name|AmazonKinesis
name|amazonKinesisClient
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|,
name|description
operator|=
literal|"Maximum number of records that will be fetched in each poll"
argument_list|,
name|defaultValue
operator|=
literal|"1"
argument_list|)
DECL|field|maxResultsPerRequest
specifier|private
name|int
name|maxResultsPerRequest
init|=
literal|1
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|,
name|description
operator|=
literal|"Defines where in the Kinesis stream to start getting records"
argument_list|)
DECL|field|iteratorType
specifier|private
name|ShardIteratorType
name|iteratorType
init|=
name|ShardIteratorType
operator|.
name|TRIM_HORIZON
decl_stmt|;
DECL|method|KinesisEndpoint (String uri, String streamName, KinesisComponent component)
specifier|public
name|KinesisEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|streamName
parameter_list|,
name|KinesisComponent
name|component
parameter_list|)
block|{
name|super
argument_list|(
name|uri
argument_list|,
name|component
argument_list|)
expr_stmt|;
name|this
operator|.
name|streamName
operator|=
name|streamName
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Not supported yet."
argument_list|)
throw|;
block|}
annotation|@
name|Override
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
specifier|final
name|KinesisConsumer
name|consumer
init|=
operator|new
name|KinesisConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|)
decl_stmt|;
name|consumer
operator|.
name|setSchedulerProperties
argument_list|(
name|getSchedulerProperties
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|consumer
return|;
block|}
DECL|method|createExchange (Record record)
specifier|public
name|Exchange
name|createExchange
parameter_list|(
name|Record
name|record
parameter_list|)
block|{
name|Exchange
name|exchange
init|=
name|super
operator|.
name|createExchange
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|record
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|KinesisConstants
operator|.
name|APPROX_ARRIVAL_TIME
argument_list|,
name|record
operator|.
name|getApproximateArrivalTimestamp
argument_list|()
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|KinesisConstants
operator|.
name|PARTITION_KEY
argument_list|,
name|record
operator|.
name|getPartitionKey
argument_list|()
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|KinesisConstants
operator|.
name|SEQUENCE_NUMBER
argument_list|,
name|record
operator|.
name|getSequenceNumber
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|exchange
return|;
block|}
annotation|@
name|Override
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
DECL|method|getClient ()
name|AmazonKinesis
name|getClient
parameter_list|()
block|{
return|return
name|amazonKinesisClient
return|;
block|}
comment|// required for injection.
DECL|method|getAmazonKinesisClient ()
specifier|public
name|AmazonKinesis
name|getAmazonKinesisClient
parameter_list|()
block|{
return|return
name|amazonKinesisClient
return|;
block|}
DECL|method|setAmazonKinesisClient (AmazonKinesis amazonKinesisClient)
specifier|public
name|void
name|setAmazonKinesisClient
parameter_list|(
name|AmazonKinesis
name|amazonKinesisClient
parameter_list|)
block|{
name|this
operator|.
name|amazonKinesisClient
operator|=
name|amazonKinesisClient
expr_stmt|;
block|}
DECL|method|getMaxResultsPerRequest ()
specifier|public
name|int
name|getMaxResultsPerRequest
parameter_list|()
block|{
return|return
name|maxResultsPerRequest
return|;
block|}
DECL|method|setMaxResultsPerRequest (int maxResultsPerRequest)
specifier|public
name|void
name|setMaxResultsPerRequest
parameter_list|(
name|int
name|maxResultsPerRequest
parameter_list|)
block|{
name|this
operator|.
name|maxResultsPerRequest
operator|=
name|maxResultsPerRequest
expr_stmt|;
block|}
DECL|method|getStreamName ()
specifier|public
name|String
name|getStreamName
parameter_list|()
block|{
return|return
name|streamName
return|;
block|}
DECL|method|setStreamName (String streamName)
specifier|public
name|void
name|setStreamName
parameter_list|(
name|String
name|streamName
parameter_list|)
block|{
name|this
operator|.
name|streamName
operator|=
name|streamName
expr_stmt|;
block|}
DECL|method|getIteratorType ()
specifier|public
name|ShardIteratorType
name|getIteratorType
parameter_list|()
block|{
return|return
name|iteratorType
return|;
block|}
DECL|method|setIteratorType (ShardIteratorType iteratorType)
specifier|public
name|void
name|setIteratorType
parameter_list|(
name|ShardIteratorType
name|iteratorType
parameter_list|)
block|{
name|this
operator|.
name|iteratorType
operator|=
name|iteratorType
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
literal|"KinesisEndpoint{amazonKinesisClient=[redacted], maxResultsPerRequest="
operator|+
name|maxResultsPerRequest
operator|+
literal|", iteratorType="
operator|+
name|iteratorType
operator|+
literal|", streamName="
operator|+
name|streamName
operator|+
literal|'}'
return|;
block|}
block|}
end_class

end_unit

