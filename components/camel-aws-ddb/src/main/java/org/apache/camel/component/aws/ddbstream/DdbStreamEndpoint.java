begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|ClientConfiguration
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|auth
operator|.
name|AWSCredentials
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|auth
operator|.
name|AWSCredentialsProvider
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|auth
operator|.
name|AWSStaticCredentialsProvider
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|auth
operator|.
name|BasicAWSCredentials
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|regions
operator|.
name|Regions
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
name|AmazonDynamoDBStreamsClientBuilder
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
name|support
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
name|util
operator|.
name|ObjectHelper
import|;
end_import

begin_comment
comment|/**  * The aws-ddbstream component is used for working with Amazon DynamoDB Streams.  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|firstVersion
operator|=
literal|"2.17.0"
argument_list|,
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
name|UriParam
DECL|field|configuration
name|DdbStreamConfiguration
name|configuration
decl_stmt|;
DECL|field|ddbStreamClient
specifier|private
name|AmazonDynamoDBStreams
name|ddbStreamClient
decl_stmt|;
DECL|method|DdbStreamEndpoint (String uri, DdbStreamConfiguration configuration, DdbStreamComponent component)
specifier|public
name|DdbStreamEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|DdbStreamConfiguration
name|configuration
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
name|configuration
operator|=
name|configuration
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
name|configureConsumer
argument_list|(
name|consumer
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
DECL|method|doStart ()
specifier|public
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
name|ddbStreamClient
operator|=
name|configuration
operator|.
name|getAmazonDynamoDbStreamsClient
argument_list|()
operator|!=
literal|null
condition|?
name|configuration
operator|.
name|getAmazonDynamoDbStreamsClient
argument_list|()
else|:
name|createDdbStreamClient
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|public
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|configuration
operator|.
name|getAmazonDynamoDbStreamsClient
argument_list|()
argument_list|)
condition|)
block|{
if|if
condition|(
name|ddbStreamClient
operator|!=
literal|null
condition|)
block|{
name|ddbStreamClient
operator|.
name|shutdown
argument_list|()
expr_stmt|;
block|}
block|}
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
block|}
DECL|method|getConfiguration ()
specifier|public
name|DdbStreamConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
DECL|method|getClient ()
specifier|public
name|AmazonDynamoDBStreams
name|getClient
parameter_list|()
block|{
return|return
name|ddbStreamClient
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
name|configuration
operator|.
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
name|configuration
operator|.
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
name|configuration
operator|.
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
DECL|method|createDdbStreamClient ()
name|AmazonDynamoDBStreams
name|createDdbStreamClient
parameter_list|()
block|{
name|AmazonDynamoDBStreams
name|client
init|=
literal|null
decl_stmt|;
name|ClientConfiguration
name|clientConfiguration
init|=
literal|null
decl_stmt|;
name|AmazonDynamoDBStreamsClientBuilder
name|clientBuilder
init|=
literal|null
decl_stmt|;
name|boolean
name|isClientConfigFound
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|configuration
operator|.
name|getProxyHost
argument_list|()
argument_list|)
operator|&&
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|configuration
operator|.
name|getProxyPort
argument_list|()
argument_list|)
condition|)
block|{
name|clientConfiguration
operator|=
operator|new
name|ClientConfiguration
argument_list|()
expr_stmt|;
name|clientConfiguration
operator|.
name|setProxyHost
argument_list|(
name|configuration
operator|.
name|getProxyHost
argument_list|()
argument_list|)
expr_stmt|;
name|clientConfiguration
operator|.
name|setProxyPort
argument_list|(
name|configuration
operator|.
name|getProxyPort
argument_list|()
argument_list|)
expr_stmt|;
name|isClientConfigFound
operator|=
literal|true
expr_stmt|;
block|}
if|if
condition|(
name|configuration
operator|.
name|getAccessKey
argument_list|()
operator|!=
literal|null
operator|&&
name|configuration
operator|.
name|getSecretKey
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|AWSCredentials
name|credentials
init|=
operator|new
name|BasicAWSCredentials
argument_list|(
name|configuration
operator|.
name|getAccessKey
argument_list|()
argument_list|,
name|configuration
operator|.
name|getSecretKey
argument_list|()
argument_list|)
decl_stmt|;
name|AWSCredentialsProvider
name|credentialsProvider
init|=
operator|new
name|AWSStaticCredentialsProvider
argument_list|(
name|credentials
argument_list|)
decl_stmt|;
if|if
condition|(
name|isClientConfigFound
condition|)
block|{
name|clientBuilder
operator|=
name|AmazonDynamoDBStreamsClientBuilder
operator|.
name|standard
argument_list|()
operator|.
name|withClientConfiguration
argument_list|(
name|clientConfiguration
argument_list|)
operator|.
name|withCredentials
argument_list|(
name|credentialsProvider
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|clientBuilder
operator|=
name|AmazonDynamoDBStreamsClientBuilder
operator|.
name|standard
argument_list|()
operator|.
name|withCredentials
argument_list|(
name|credentialsProvider
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
if|if
condition|(
name|isClientConfigFound
condition|)
block|{
name|clientBuilder
operator|=
name|AmazonDynamoDBStreamsClientBuilder
operator|.
name|standard
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|clientBuilder
operator|=
name|AmazonDynamoDBStreamsClientBuilder
operator|.
name|standard
argument_list|()
operator|.
name|withClientConfiguration
argument_list|(
name|clientConfiguration
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|configuration
operator|.
name|getRegion
argument_list|()
argument_list|)
condition|)
block|{
name|clientBuilder
operator|=
name|clientBuilder
operator|.
name|withRegion
argument_list|(
name|Regions
operator|.
name|valueOf
argument_list|(
name|configuration
operator|.
name|getRegion
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|client
operator|=
name|clientBuilder
operator|.
name|build
argument_list|()
expr_stmt|;
return|return
name|client
return|;
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
name|configuration
operator|.
name|getTableName
argument_list|()
operator|+
literal|", amazonDynamoDbStreamsClient=[redacted], maxResultsPerRequest="
operator|+
name|configuration
operator|.
name|getMaxResultsPerRequest
argument_list|()
operator|+
literal|", iteratorType="
operator|+
name|configuration
operator|.
name|getIteratorType
argument_list|()
operator|+
literal|", sequenceNumberProvider="
operator|+
name|configuration
operator|.
name|getSequenceNumberProvider
argument_list|()
operator|+
literal|", uri="
operator|+
name|getEndpointUri
argument_list|()
operator|+
literal|'}'
return|;
block|}
block|}
end_class

end_unit

