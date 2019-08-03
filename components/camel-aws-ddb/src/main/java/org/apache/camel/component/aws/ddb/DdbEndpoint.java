begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.aws.ddb
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
name|ddb
package|;
end_package

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|AmazonServiceException
import|;
end_import

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
name|AmazonDynamoDB
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
name|AmazonDynamoDBClientBuilder
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
name|CreateTableRequest
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
name|DescribeTableRequest
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
name|KeySchemaElement
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
name|ProvisionedThroughput
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
name|ResourceNotFoundException
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
name|TableDescription
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
name|TableStatus
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
comment|/**  * The aws-ddb component is used for storing and retrieving data from Amazon's DynamoDB service.  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|firstVersion
operator|=
literal|"2.10.0"
argument_list|,
name|scheme
operator|=
literal|"aws-ddb"
argument_list|,
name|title
operator|=
literal|"AWS DynamoDB"
argument_list|,
name|syntax
operator|=
literal|"aws-ddb:tableName"
argument_list|,
name|producerOnly
operator|=
literal|true
argument_list|,
name|label
operator|=
literal|"cloud,database,nosql"
argument_list|)
DECL|class|DdbEndpoint
specifier|public
class|class
name|DdbEndpoint
extends|extends
name|ScheduledPollEndpoint
block|{
annotation|@
name|UriParam
DECL|field|configuration
specifier|private
name|DdbConfiguration
name|configuration
decl_stmt|;
DECL|field|ddbClient
specifier|private
name|AmazonDynamoDB
name|ddbClient
decl_stmt|;
DECL|method|DdbEndpoint (String uri, Component component, DdbConfiguration configuration)
specifier|public
name|DdbEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|Component
name|component
parameter_list|,
name|DdbConfiguration
name|configuration
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
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"You cannot receive messages from this endpoint"
argument_list|)
throw|;
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
return|return
operator|new
name|DdbProducer
argument_list|(
name|this
argument_list|)
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
name|ddbClient
operator|=
name|configuration
operator|.
name|getAmazonDDBClient
argument_list|()
operator|!=
literal|null
condition|?
name|configuration
operator|.
name|getAmazonDDBClient
argument_list|()
else|:
name|createDdbClient
argument_list|()
expr_stmt|;
name|String
name|tableName
init|=
name|getConfiguration
argument_list|()
operator|.
name|getTableName
argument_list|()
decl_stmt|;
name|log
operator|.
name|trace
argument_list|(
literal|"Querying whether table [{}] already exists..."
argument_list|,
name|tableName
argument_list|)
expr_stmt|;
try|try
block|{
name|DescribeTableRequest
name|request
init|=
operator|new
name|DescribeTableRequest
argument_list|()
operator|.
name|withTableName
argument_list|(
name|tableName
argument_list|)
decl_stmt|;
name|TableDescription
name|tableDescription
init|=
name|ddbClient
operator|.
name|describeTable
argument_list|(
name|request
argument_list|)
operator|.
name|getTable
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|isTableActive
argument_list|(
name|tableDescription
argument_list|)
condition|)
block|{
name|waitForTableToBecomeAvailable
argument_list|(
name|tableName
argument_list|)
expr_stmt|;
block|}
name|log
operator|.
name|trace
argument_list|(
literal|"Table [{}] already exists"
argument_list|,
name|tableName
argument_list|)
expr_stmt|;
return|return;
block|}
catch|catch
parameter_list|(
name|ResourceNotFoundException
name|e
parameter_list|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Table [{}] doesn't exist yet"
argument_list|,
name|tableName
argument_list|)
expr_stmt|;
name|log
operator|.
name|trace
argument_list|(
literal|"Creating table [{}]..."
argument_list|,
name|tableName
argument_list|)
expr_stmt|;
name|TableDescription
name|tableDescription
init|=
name|createTable
argument_list|(
name|tableName
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|isTableActive
argument_list|(
name|tableDescription
argument_list|)
condition|)
block|{
name|waitForTableToBecomeAvailable
argument_list|(
name|tableName
argument_list|)
expr_stmt|;
block|}
name|log
operator|.
name|trace
argument_list|(
literal|"Table [{}] created"
argument_list|,
name|tableName
argument_list|)
expr_stmt|;
block|}
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
name|getAmazonDDBClient
argument_list|()
argument_list|)
condition|)
block|{
if|if
condition|(
name|ddbClient
operator|!=
literal|null
condition|)
block|{
name|ddbClient
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
DECL|method|createTable (String tableName)
specifier|private
name|TableDescription
name|createTable
parameter_list|(
name|String
name|tableName
parameter_list|)
block|{
name|CreateTableRequest
name|createTableRequest
init|=
operator|new
name|CreateTableRequest
argument_list|()
operator|.
name|withTableName
argument_list|(
name|tableName
argument_list|)
operator|.
name|withKeySchema
argument_list|(
operator|new
name|KeySchemaElement
argument_list|()
operator|.
name|withAttributeName
argument_list|(
name|configuration
operator|.
name|getKeyAttributeName
argument_list|()
argument_list|)
operator|.
name|withKeyType
argument_list|(
name|configuration
operator|.
name|getKeyAttributeType
argument_list|()
argument_list|)
argument_list|)
operator|.
name|withProvisionedThroughput
argument_list|(
operator|new
name|ProvisionedThroughput
argument_list|()
operator|.
name|withReadCapacityUnits
argument_list|(
name|configuration
operator|.
name|getReadCapacity
argument_list|()
argument_list|)
operator|.
name|withWriteCapacityUnits
argument_list|(
name|configuration
operator|.
name|getWriteCapacity
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
return|return
name|getDdbClient
argument_list|()
operator|.
name|createTable
argument_list|(
name|createTableRequest
argument_list|)
operator|.
name|getTableDescription
argument_list|()
return|;
block|}
DECL|method|getConfiguration ()
specifier|public
name|DdbConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
DECL|method|getDdbClient ()
specifier|public
name|AmazonDynamoDB
name|getDdbClient
parameter_list|()
block|{
return|return
name|ddbClient
return|;
block|}
DECL|method|createDdbClient ()
name|AmazonDynamoDB
name|createDdbClient
parameter_list|()
block|{
name|AmazonDynamoDB
name|client
init|=
literal|null
decl_stmt|;
name|ClientConfiguration
name|clientConfiguration
init|=
literal|null
decl_stmt|;
name|AmazonDynamoDBClientBuilder
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
name|AmazonDynamoDBClientBuilder
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
name|AmazonDynamoDBClientBuilder
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
name|AmazonDynamoDBClientBuilder
operator|.
name|standard
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|clientBuilder
operator|=
name|AmazonDynamoDBClientBuilder
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
DECL|method|waitForTableToBecomeAvailable (String tableName)
specifier|private
name|void
name|waitForTableToBecomeAvailable
parameter_list|(
name|String
name|tableName
parameter_list|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Waiting for [{}] to become ACTIVE..."
argument_list|,
name|tableName
argument_list|)
expr_stmt|;
name|long
name|waitTime
init|=
literal|5
operator|*
literal|60
operator|*
literal|1000
decl_stmt|;
while|while
condition|(
name|waitTime
operator|>
literal|0
condition|)
block|{
try|try
block|{
name|Thread
operator|.
name|sleep
argument_list|(
literal|1000
operator|*
literal|5
argument_list|)
expr_stmt|;
name|waitTime
operator|-=
literal|5000
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{             }
try|try
block|{
name|DescribeTableRequest
name|request
init|=
operator|new
name|DescribeTableRequest
argument_list|()
operator|.
name|withTableName
argument_list|(
name|tableName
argument_list|)
decl_stmt|;
name|TableDescription
name|tableDescription
init|=
name|getDdbClient
argument_list|()
operator|.
name|describeTable
argument_list|(
name|request
argument_list|)
operator|.
name|getTable
argument_list|()
decl_stmt|;
if|if
condition|(
name|isTableActive
argument_list|(
name|tableDescription
argument_list|)
condition|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Table [{}] became active"
argument_list|,
name|tableName
argument_list|)
expr_stmt|;
return|return;
block|}
name|log
operator|.
name|trace
argument_list|(
literal|"Table [{}] not active yet"
argument_list|,
name|tableName
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|AmazonServiceException
name|ase
parameter_list|)
block|{
if|if
condition|(
operator|!
name|ase
operator|.
name|getErrorCode
argument_list|()
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"ResourceNotFoundException"
argument_list|)
condition|)
block|{
throw|throw
name|ase
throw|;
block|}
block|}
block|}
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Table "
operator|+
name|tableName
operator|+
literal|" never went active"
argument_list|)
throw|;
block|}
DECL|method|isTableActive (TableDescription tableDescription)
specifier|private
name|boolean
name|isTableActive
parameter_list|(
name|TableDescription
name|tableDescription
parameter_list|)
block|{
return|return
name|tableDescription
operator|.
name|getTableStatus
argument_list|()
operator|.
name|equals
argument_list|(
name|TableStatus
operator|.
name|ACTIVE
operator|.
name|toString
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

