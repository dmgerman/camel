begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.aws.ddbstream
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
name|ddbstream
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
name|dynamodbv2
operator|.
name|AmazonDynamoDBStreams
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
name|dynamodbv2
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
name|dynamodbv2
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

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|scheme
operator|=
literal|"aws-ddbstream"
argument_list|,
name|title
operator|=
literal|"AWS DynamoDB Streams"
argument_list|,
name|consumerOnly
operator|=
literal|true
argument_list|,
name|syntax
operator|=
literal|"aws-ddbstream:tableName"
argument_list|,
name|consumerClass
operator|=
name|DdbStreamConsumer
operator|.
name|class
argument_list|,
name|label
operator|=
literal|"cloud,messaging,streams"
argument_list|)
DECL|class|DdbStreamEndpoint
specifier|public
class|class
name|DdbStreamEndpoint
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
literal|"Name of the dynamodb table"
argument_list|)
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|"true"
argument_list|)
DECL|field|tableName
specifier|private
name|String
name|tableName
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
literal|"Amazon DynamoDB client to use for all requests for this endpoint"
argument_list|)
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|"true"
argument_list|)
DECL|field|amazonDynamoDbStreamsClient
specifier|private
name|AmazonDynamoDBStreams
name|amazonDynamoDbStreamsClient
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
argument_list|)
DECL|field|maxResultsPerRequest
specifier|private
name|int
name|maxResultsPerRequest
init|=
literal|100
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
literal|"Defines where in the DynaboDB stream"
operator|+
literal|" to start getting records. Note that using TRIM_HORIZON can cause a"
operator|+
literal|" significant delay before the stream has caught up to real-time."
operator|+
literal|" if {AT,AFTER}_SEQUENCE_NUMBER are used, then a sequenceNumberProvider"
operator|+
literal|" MUST be supplied."
argument_list|,
name|defaultValue
operator|=
literal|"LATEST"
argument_list|)
DECL|field|iteratorType
specifier|private
name|ShardIteratorType
name|iteratorType
init|=
name|ShardIteratorType
operator|.
name|LATEST
decl_stmt|;
comment|// TODO add the ability to use ShardIteratorType.{AT,AFTER}_SEQUENCE_NUMBER
comment|// by specifying either a sequence number itself or a bean to fetch the
comment|// sequence number from persistant storage or somewhere else.
comment|// This can be done by having the type of the parameter an interface
comment|// and supplying a default implementation and a converter from a long/String
comment|// to an instance of this interface.
comment|// Note that the shard list needs to have the ability to start at the shard
comment|// that includes the supplied sequence number
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|,
name|description
operator|=
literal|"Provider for the sequence number when"
operator|+
literal|" using one of the two ShardIteratorType.{AT,AFTER}_SEQUENCE_NUMBER"
operator|+
literal|" iterator types. Can be a registry reference or a literal sequence number."
argument_list|)
DECL|field|sequenceNumberProvider
specifier|private
name|SequenceNumberProvider
name|sequenceNumberProvider
decl_stmt|;
DECL|method|DdbStreamEndpoint (String uri, String tableName, DdbStreamComponent component)
specifier|public
name|DdbStreamEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|tableName
parameter_list|,
name|DdbStreamComponent
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
name|tableName
operator|=
name|tableName
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
name|DdbStreamConsumer
name|consumer
init|=
operator|new
name|DdbStreamConsumer
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
name|consumer
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getSchedulerProperties
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|consumer
return|;
block|}
DECL|method|createExchange (Record record)
name|Exchange
name|createExchange
parameter_list|(
name|Record
name|record
parameter_list|)
block|{
name|Exchange
name|ex
init|=
name|super
operator|.
name|createExchange
argument_list|()
decl_stmt|;
name|ex
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|record
argument_list|,
name|Record
operator|.
name|class
argument_list|)
expr_stmt|;
return|return
name|ex
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
DECL|method|getSequenceNumber ()
specifier|public
name|String
name|getSequenceNumber
parameter_list|()
block|{
switch|switch
condition|(
name|getIteratorType
argument_list|()
condition|)
block|{
case|case
name|AFTER_SEQUENCE_NUMBER
case|:
case|case
name|AT_SEQUENCE_NUMBER
case|:
if|if
condition|(
literal|null
operator|==
name|getSequenceNumberProvider
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"sequenceNumberProvider must be"
operator|+
literal|" provided, either as an implementation of"
operator|+
literal|" SequenceNumberProvider or a literal String."
argument_list|)
throw|;
block|}
else|else
block|{
return|return
name|getSequenceNumberProvider
argument_list|()
operator|.
name|getSequenceNumber
argument_list|()
return|;
block|}
default|default:
return|return
literal|""
return|;
block|}
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
literal|"DdbStreamEndpoint{"
operator|+
literal|"tableName="
operator|+
name|tableName
operator|+
literal|", amazonDynamoDbStreamsClient=[redacted], maxResultsPerRequest="
operator|+
name|maxResultsPerRequest
operator|+
literal|", iteratorType="
operator|+
name|iteratorType
operator|+
literal|", sequenceNumberProvider="
operator|+
name|sequenceNumberProvider
operator|+
literal|", uri="
operator|+
name|getEndpointUri
argument_list|()
operator|+
literal|'}'
return|;
block|}
DECL|method|getClient ()
name|AmazonDynamoDBStreams
name|getClient
parameter_list|()
block|{
return|return
name|amazonDynamoDbStreamsClient
return|;
block|}
DECL|method|getAmazonDynamoDBStreamsClient ()
specifier|public
name|AmazonDynamoDBStreams
name|getAmazonDynamoDBStreamsClient
parameter_list|()
block|{
return|return
name|amazonDynamoDbStreamsClient
return|;
block|}
DECL|method|setAmazonDynamoDbStreamsClient (AmazonDynamoDBStreams amazonDynamoDbStreamsClient)
specifier|public
name|void
name|setAmazonDynamoDbStreamsClient
parameter_list|(
name|AmazonDynamoDBStreams
name|amazonDynamoDbStreamsClient
parameter_list|)
block|{
name|this
operator|.
name|amazonDynamoDbStreamsClient
operator|=
name|amazonDynamoDbStreamsClient
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
DECL|method|getTableName ()
specifier|public
name|String
name|getTableName
parameter_list|()
block|{
return|return
name|tableName
return|;
block|}
DECL|method|setTableName (String tableName)
specifier|public
name|void
name|setTableName
parameter_list|(
name|String
name|tableName
parameter_list|)
block|{
name|this
operator|.
name|tableName
operator|=
name|tableName
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
DECL|method|getSequenceNumberProvider ()
specifier|public
name|SequenceNumberProvider
name|getSequenceNumberProvider
parameter_list|()
block|{
return|return
name|sequenceNumberProvider
return|;
block|}
DECL|method|setSequenceNumberProvider (SequenceNumberProvider sequenceNumberProvider)
specifier|public
name|void
name|setSequenceNumberProvider
parameter_list|(
name|SequenceNumberProvider
name|sequenceNumberProvider
parameter_list|)
block|{
name|this
operator|.
name|sequenceNumberProvider
operator|=
name|sequenceNumberProvider
expr_stmt|;
block|}
block|}
end_class

end_unit

