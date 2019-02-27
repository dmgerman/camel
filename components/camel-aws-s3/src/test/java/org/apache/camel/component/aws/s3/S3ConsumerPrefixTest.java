begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.aws.s3
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
name|s3
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|UnsupportedEncodingException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|atomic
operator|.
name|AtomicInteger
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|AmazonClientException
import|;
end_import

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
name|services
operator|.
name|s3
operator|.
name|AmazonS3Client
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
name|s3
operator|.
name|model
operator|.
name|ListObjectsRequest
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
name|s3
operator|.
name|model
operator|.
name|ObjectListing
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
name|s3
operator|.
name|model
operator|.
name|S3Object
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
name|s3
operator|.
name|model
operator|.
name|S3ObjectSummary
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|util
operator|.
name|StringInputStream
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
name|builder
operator|.
name|RouteBuilder
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
name|mock
operator|.
name|MockEndpoint
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
name|JndiRegistry
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
name|test
operator|.
name|junit4
operator|.
name|CamelTestSupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_comment
comment|/**  * Test to verify that the polling consumer delivers an empty Exchange when the  * sendEmptyMessageWhenIdle property is set and a polling event yields no results.  */
end_comment

begin_class
DECL|class|S3ConsumerPrefixTest
specifier|public
class|class
name|S3ConsumerPrefixTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|Test
DECL|method|testConsumePrefixedMessages ()
specifier|public
name|void
name|testConsumePrefixedMessages
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Camel rocks!"
argument_list|,
name|mock
operator|.
name|getExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
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
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createRegistry ()
specifier|protected
name|JndiRegistry
name|createRegistry
parameter_list|()
throws|throws
name|Exception
block|{
name|JndiRegistry
name|registry
init|=
name|super
operator|.
name|createRegistry
argument_list|()
decl_stmt|;
name|registry
operator|.
name|bind
argument_list|(
literal|"amazonS3Client"
argument_list|,
operator|new
name|DummyAmazonS3Client
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|registry
return|;
block|}
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"aws-s3://mycamelbucket?amazonS3Client=#amazonS3Client&region=us-west-1&delay=50"
operator|+
literal|"&maxMessagesPerPoll=5&prefix=confidential"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|class|DummyAmazonS3Client
class|class
name|DummyAmazonS3Client
extends|extends
name|AmazonS3Client
block|{
DECL|field|requestCount
specifier|private
name|AtomicInteger
name|requestCount
init|=
operator|new
name|AtomicInteger
argument_list|(
literal|0
argument_list|)
decl_stmt|;
DECL|method|DummyAmazonS3Client ()
name|DummyAmazonS3Client
parameter_list|()
block|{
name|super
argument_list|(
operator|new
name|BasicAWSCredentials
argument_list|(
literal|"myAccessKey"
argument_list|,
literal|"mySecretKey"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|listObjects (ListObjectsRequest request)
specifier|public
name|ObjectListing
name|listObjects
parameter_list|(
name|ListObjectsRequest
name|request
parameter_list|)
throws|throws
name|AmazonClientException
throws|,
name|AmazonServiceException
block|{
name|int
name|currentRequestCount
init|=
name|requestCount
operator|.
name|incrementAndGet
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"mycamelbucket"
argument_list|,
name|request
operator|.
name|getBucketName
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|currentRequestCount
operator|==
literal|2
condition|)
block|{
name|assertEquals
argument_list|(
literal|"confidential"
argument_list|,
name|request
operator|.
name|getPrefix
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|ObjectListing
name|response
init|=
operator|new
name|ObjectListing
argument_list|()
decl_stmt|;
name|response
operator|.
name|setBucketName
argument_list|(
name|request
operator|.
name|getBucketName
argument_list|()
argument_list|)
expr_stmt|;
name|response
operator|.
name|setPrefix
argument_list|(
name|request
operator|.
name|getPrefix
argument_list|()
argument_list|)
expr_stmt|;
name|S3ObjectSummary
name|s3ObjectSummary
init|=
operator|new
name|S3ObjectSummary
argument_list|()
decl_stmt|;
name|s3ObjectSummary
operator|.
name|setBucketName
argument_list|(
name|request
operator|.
name|getBucketName
argument_list|()
argument_list|)
expr_stmt|;
name|s3ObjectSummary
operator|.
name|setKey
argument_list|(
literal|"key"
argument_list|)
expr_stmt|;
name|response
operator|.
name|getObjectSummaries
argument_list|()
operator|.
name|add
argument_list|(
name|s3ObjectSummary
argument_list|)
expr_stmt|;
return|return
name|response
return|;
block|}
annotation|@
name|Override
DECL|method|getObject (String bucketName, String key)
specifier|public
name|S3Object
name|getObject
parameter_list|(
name|String
name|bucketName
parameter_list|,
name|String
name|key
parameter_list|)
throws|throws
name|AmazonClientException
throws|,
name|AmazonServiceException
block|{
name|assertEquals
argument_list|(
literal|"mycamelbucket"
argument_list|,
name|bucketName
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"key"
argument_list|,
name|key
argument_list|)
expr_stmt|;
name|S3Object
name|s3Object
init|=
operator|new
name|S3Object
argument_list|()
decl_stmt|;
name|s3Object
operator|.
name|setBucketName
argument_list|(
name|bucketName
argument_list|)
expr_stmt|;
name|s3Object
operator|.
name|setKey
argument_list|(
name|key
argument_list|)
expr_stmt|;
try|try
block|{
name|s3Object
operator|.
name|setObjectContent
argument_list|(
operator|new
name|StringInputStream
argument_list|(
literal|"Camel rocks!"
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedEncodingException
name|e
parameter_list|)
block|{
comment|// noop
block|}
return|return
name|s3Object
return|;
block|}
annotation|@
name|Override
DECL|method|deleteObject (String bucketName, String key)
specifier|public
name|void
name|deleteObject
parameter_list|(
name|String
name|bucketName
parameter_list|,
name|String
name|key
parameter_list|)
throws|throws
name|AmazonClientException
throws|,
name|AmazonServiceException
block|{
comment|//noop
block|}
block|}
block|}
end_class

end_unit
