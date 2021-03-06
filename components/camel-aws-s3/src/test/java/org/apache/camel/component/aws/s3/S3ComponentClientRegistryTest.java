begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
DECL|class|S3ComponentClientRegistryTest
specifier|public
class|class
name|S3ComponentClientRegistryTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|Test
DECL|method|createEndpointWithMinimalS3ClientConfiguration ()
specifier|public
name|void
name|createEndpointWithMinimalS3ClientConfiguration
parameter_list|()
throws|throws
name|Exception
block|{
name|AmazonS3ClientMock
name|clientMock
init|=
operator|new
name|AmazonS3ClientMock
argument_list|()
decl_stmt|;
name|context
operator|.
name|getRegistry
argument_list|()
operator|.
name|bind
argument_list|(
literal|"amazonS3Client"
argument_list|,
name|clientMock
argument_list|)
expr_stmt|;
name|S3Component
name|component
init|=
name|context
operator|.
name|getComponent
argument_list|(
literal|"aws-s3"
argument_list|,
name|S3Component
operator|.
name|class
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
literal|"aws-s3://MyBucket"
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
name|assertNotNull
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
name|getPolicy
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
name|getPrefix
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
name|isIncludeBody
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
DECL|method|createEndpointWithMinimalS3ClientMisconfiguration ()
specifier|public
name|void
name|createEndpointWithMinimalS3ClientMisconfiguration
parameter_list|()
throws|throws
name|Exception
block|{
name|S3Component
name|component
init|=
name|context
operator|.
name|getComponent
argument_list|(
literal|"aws-s3"
argument_list|,
name|S3Component
operator|.
name|class
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
literal|"aws-s3://MyBucket"
argument_list|)
decl_stmt|;
block|}
block|}
end_class

end_unit

