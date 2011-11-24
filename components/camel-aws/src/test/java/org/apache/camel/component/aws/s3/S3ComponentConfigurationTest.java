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
name|impl
operator|.
name|PropertyPlaceholderDelegateRegistry
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

begin_class
DECL|class|S3ComponentConfigurationTest
specifier|public
class|class
name|S3ComponentConfigurationTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|Test
DECL|method|createEndpointWithMinimalConfiguration ()
specifier|public
name|void
name|createEndpointWithMinimalConfiguration
parameter_list|()
throws|throws
name|Exception
block|{
name|S3Component
name|component
init|=
operator|new
name|S3Component
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|S3Endpoint
name|endpoint
init|=
operator|(
name|S3Endpoint
operator|)
name|component
operator|.
name|createEndpoint
argument_list|(
literal|"aws-s3://MyBucket?accessKey=xxx&secretKey=yyy"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"MyBucket"
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getBucketName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"xxx"
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getAccessKey
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"yyy"
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getSecretKey
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getAmazonS3Client
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getRegion
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|isDeleteAfterRead
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|10
argument_list|,
name|endpoint
operator|.
name|getMaxMessagesPerPoll
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getAmazonS3Endpoint
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getPolicy
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createEndpointWithMinimalConfigurationAndProvidedClient ()
specifier|public
name|void
name|createEndpointWithMinimalConfigurationAndProvidedClient
parameter_list|()
throws|throws
name|Exception
block|{
name|AmazonS3ClientMock
name|mock
init|=
operator|new
name|AmazonS3ClientMock
argument_list|()
decl_stmt|;
operator|(
call|(
name|JndiRegistry
call|)
argument_list|(
operator|(
name|PropertyPlaceholderDelegateRegistry
operator|)
name|context
operator|.
name|getRegistry
argument_list|()
argument_list|)
operator|.
name|getRegistry
argument_list|()
operator|)
operator|.
name|bind
argument_list|(
literal|"amazonS3Client"
argument_list|,
name|mock
argument_list|)
expr_stmt|;
name|S3Component
name|component
init|=
operator|new
name|S3Component
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|S3Endpoint
name|endpoint
init|=
operator|(
name|S3Endpoint
operator|)
name|component
operator|.
name|createEndpoint
argument_list|(
literal|"aws-s3://MyBucket?amazonS3Client=#amazonS3Client"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"MyBucket"
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getBucketName
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getAccessKey
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getSecretKey
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|mock
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getAmazonS3Client
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getRegion
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|isDeleteAfterRead
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|10
argument_list|,
name|endpoint
operator|.
name|getMaxMessagesPerPoll
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getAmazonS3Endpoint
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getPolicy
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createEndpointWithMaximalConfiguration ()
specifier|public
name|void
name|createEndpointWithMaximalConfiguration
parameter_list|()
throws|throws
name|Exception
block|{
name|S3Component
name|component
init|=
operator|new
name|S3Component
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|S3Endpoint
name|endpoint
init|=
operator|(
name|S3Endpoint
operator|)
name|component
operator|.
name|createEndpoint
argument_list|(
literal|"aws-s3://MyBucket?amazonS3Endpoint=sns.eu-west-1.amazonaws.com"
operator|+
literal|"&accessKey=xxx&secretKey=yyy&region=us-west-1&deleteAfterRead=false&maxMessagesPerPoll=1&policy=%7B%22Version%22%3A%222008-10-17%22,%22Id%22%3A%22Policy4324355464%22,"
operator|+
literal|"%22Statement%22%3A%5B%7B%22Sid%22%3A%22Stmt456464646477%22,%22Action%22%3A%5B%22s3%3AGetObject%22%5D,%22Effect%22%3A%22Allow%22,"
operator|+
literal|"%22Resource%22%3A%5B%22arn%3Aaws%3As3%3A%3A%3Amybucket/some/path/*%22%5D,%22Principal%22%3A%7B%22AWS%22%3A%5B%22*%22%5D%7D%7D%5D%7D&storageClass=REDUCED_REDUNDANCY"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"MyBucket"
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getBucketName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"xxx"
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getAccessKey
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"yyy"
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getSecretKey
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getAmazonS3Client
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"us-west-1"
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getRegion
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|isDeleteAfterRead
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|endpoint
operator|.
name|getMaxMessagesPerPoll
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"sns.eu-west-1.amazonaws.com"
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getAmazonS3Endpoint
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"{\"Version\":\"2008-10-17\",\"Id\":\"Policy4324355464\",\"Statement\":[{\"Sid\":\"Stmt456464646477\",\"Action\":[\"s3:GetObject\"],\"Effect\":\"Allow\",\"Resource\":"
operator|+
literal|"[\"arn:aws:s3:::mybucket/some/path/*\"],\"Principal\":{\"AWS\":[\"*\"]}}]}"
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getPolicy
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"REDUCED_REDUNDANCY"
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getStorageClass
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|IllegalArgumentException
operator|.
name|class
argument_list|)
DECL|method|createEndpointWithoutBucketName ()
specifier|public
name|void
name|createEndpointWithoutBucketName
parameter_list|()
throws|throws
name|Exception
block|{
name|S3Component
name|component
init|=
operator|new
name|S3Component
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|component
operator|.
name|createEndpoint
argument_list|(
literal|"aws-s3:// "
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|IllegalArgumentException
operator|.
name|class
argument_list|)
DECL|method|createEndpointWithoutAccessKeyConfiguration ()
specifier|public
name|void
name|createEndpointWithoutAccessKeyConfiguration
parameter_list|()
throws|throws
name|Exception
block|{
name|S3Component
name|component
init|=
operator|new
name|S3Component
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|component
operator|.
name|createEndpoint
argument_list|(
literal|"aws-sns://MyTopic?secretKey=yyy"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|IllegalArgumentException
operator|.
name|class
argument_list|)
DECL|method|createEndpointWithoutSecretKeyConfiguration ()
specifier|public
name|void
name|createEndpointWithoutSecretKeyConfiguration
parameter_list|()
throws|throws
name|Exception
block|{
name|S3Component
name|component
init|=
operator|new
name|S3Component
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|component
operator|.
name|createEndpoint
argument_list|(
literal|"aws-sns://MyTopic?accessKey=xxx"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

