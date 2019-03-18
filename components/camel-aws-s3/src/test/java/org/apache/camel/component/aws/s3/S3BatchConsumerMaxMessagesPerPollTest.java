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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|BindToRegistry
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
name|EndpointInject
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
DECL|class|S3BatchConsumerMaxMessagesPerPollTest
specifier|public
class|class
name|S3BatchConsumerMaxMessagesPerPollTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|BindToRegistry
argument_list|(
literal|"amazonS3Client"
argument_list|)
DECL|field|clientMock
name|AmazonS3ClientMock
name|clientMock
init|=
operator|new
name|AmazonS3ClientMock
argument_list|()
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
literal|"mock:result"
argument_list|)
DECL|field|mock
specifier|private
name|MockEndpoint
name|mock
decl_stmt|;
annotation|@
name|Test
DECL|method|receiveBatch ()
specifier|public
name|void
name|receiveBatch
parameter_list|()
throws|throws
name|Exception
block|{
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|20
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|mock
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|exchangeProperty
argument_list|(
name|Exchange
operator|.
name|BATCH_INDEX
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mock
operator|.
name|message
argument_list|(
literal|1
argument_list|)
operator|.
name|exchangeProperty
argument_list|(
name|Exchange
operator|.
name|BATCH_INDEX
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|mock
operator|.
name|message
argument_list|(
literal|2
argument_list|)
operator|.
name|exchangeProperty
argument_list|(
name|Exchange
operator|.
name|BATCH_INDEX
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|3
argument_list|)
expr_stmt|;
name|mock
operator|.
name|message
argument_list|(
literal|3
argument_list|)
operator|.
name|exchangeProperty
argument_list|(
name|Exchange
operator|.
name|BATCH_INDEX
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|4
argument_list|)
expr_stmt|;
name|mock
operator|.
name|message
argument_list|(
literal|4
argument_list|)
operator|.
name|exchangeProperty
argument_list|(
name|Exchange
operator|.
name|BATCH_INDEX
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|5
argument_list|)
expr_stmt|;
name|mock
operator|.
name|message
argument_list|(
literal|5
argument_list|)
operator|.
name|exchangeProperty
argument_list|(
name|Exchange
operator|.
name|BATCH_INDEX
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|6
argument_list|)
expr_stmt|;
name|mock
operator|.
name|message
argument_list|(
literal|6
argument_list|)
operator|.
name|exchangeProperty
argument_list|(
name|Exchange
operator|.
name|BATCH_INDEX
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|7
argument_list|)
expr_stmt|;
name|mock
operator|.
name|message
argument_list|(
literal|7
argument_list|)
operator|.
name|exchangeProperty
argument_list|(
name|Exchange
operator|.
name|BATCH_INDEX
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|8
argument_list|)
expr_stmt|;
name|mock
operator|.
name|message
argument_list|(
literal|8
argument_list|)
operator|.
name|exchangeProperty
argument_list|(
name|Exchange
operator|.
name|BATCH_INDEX
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|9
argument_list|)
expr_stmt|;
name|mock
operator|.
name|message
argument_list|(
literal|9
argument_list|)
operator|.
name|exchangeProperty
argument_list|(
name|Exchange
operator|.
name|BATCH_INDEX
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|10
argument_list|)
expr_stmt|;
name|mock
operator|.
name|message
argument_list|(
literal|10
argument_list|)
operator|.
name|exchangeProperty
argument_list|(
name|Exchange
operator|.
name|BATCH_INDEX
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|11
argument_list|)
expr_stmt|;
name|mock
operator|.
name|message
argument_list|(
literal|11
argument_list|)
operator|.
name|exchangeProperty
argument_list|(
name|Exchange
operator|.
name|BATCH_INDEX
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|12
argument_list|)
expr_stmt|;
name|mock
operator|.
name|message
argument_list|(
literal|12
argument_list|)
operator|.
name|exchangeProperty
argument_list|(
name|Exchange
operator|.
name|BATCH_INDEX
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|13
argument_list|)
expr_stmt|;
name|mock
operator|.
name|message
argument_list|(
literal|13
argument_list|)
operator|.
name|exchangeProperty
argument_list|(
name|Exchange
operator|.
name|BATCH_INDEX
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|14
argument_list|)
expr_stmt|;
name|mock
operator|.
name|message
argument_list|(
literal|14
argument_list|)
operator|.
name|exchangeProperty
argument_list|(
name|Exchange
operator|.
name|BATCH_INDEX
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|15
argument_list|)
expr_stmt|;
name|mock
operator|.
name|message
argument_list|(
literal|15
argument_list|)
operator|.
name|exchangeProperty
argument_list|(
name|Exchange
operator|.
name|BATCH_INDEX
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|16
argument_list|)
expr_stmt|;
name|mock
operator|.
name|message
argument_list|(
literal|16
argument_list|)
operator|.
name|exchangeProperty
argument_list|(
name|Exchange
operator|.
name|BATCH_INDEX
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|17
argument_list|)
expr_stmt|;
name|mock
operator|.
name|message
argument_list|(
literal|17
argument_list|)
operator|.
name|exchangeProperty
argument_list|(
name|Exchange
operator|.
name|BATCH_INDEX
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|18
argument_list|)
expr_stmt|;
name|mock
operator|.
name|message
argument_list|(
literal|18
argument_list|)
operator|.
name|exchangeProperty
argument_list|(
name|Exchange
operator|.
name|BATCH_INDEX
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|19
argument_list|)
expr_stmt|;
name|mock
operator|.
name|message
argument_list|(
literal|19
argument_list|)
operator|.
name|exchangeProperty
argument_list|(
name|Exchange
operator|.
name|BATCH_INDEX
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|20
argument_list|)
expr_stmt|;
name|mock
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|exchangeProperty
argument_list|(
name|Exchange
operator|.
name|BATCH_COMPLETE
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|mock
operator|.
name|message
argument_list|(
literal|1
argument_list|)
operator|.
name|exchangeProperty
argument_list|(
name|Exchange
operator|.
name|BATCH_COMPLETE
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|mock
operator|.
name|message
argument_list|(
literal|2
argument_list|)
operator|.
name|exchangeProperty
argument_list|(
name|Exchange
operator|.
name|BATCH_COMPLETE
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|mock
operator|.
name|message
argument_list|(
literal|3
argument_list|)
operator|.
name|exchangeProperty
argument_list|(
name|Exchange
operator|.
name|BATCH_COMPLETE
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|mock
operator|.
name|message
argument_list|(
literal|4
argument_list|)
operator|.
name|exchangeProperty
argument_list|(
name|Exchange
operator|.
name|BATCH_COMPLETE
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|mock
operator|.
name|message
argument_list|(
literal|5
argument_list|)
operator|.
name|exchangeProperty
argument_list|(
name|Exchange
operator|.
name|BATCH_COMPLETE
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|mock
operator|.
name|message
argument_list|(
literal|6
argument_list|)
operator|.
name|exchangeProperty
argument_list|(
name|Exchange
operator|.
name|BATCH_COMPLETE
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|mock
operator|.
name|message
argument_list|(
literal|7
argument_list|)
operator|.
name|exchangeProperty
argument_list|(
name|Exchange
operator|.
name|BATCH_COMPLETE
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|mock
operator|.
name|message
argument_list|(
literal|8
argument_list|)
operator|.
name|exchangeProperty
argument_list|(
name|Exchange
operator|.
name|BATCH_COMPLETE
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|mock
operator|.
name|message
argument_list|(
literal|9
argument_list|)
operator|.
name|exchangeProperty
argument_list|(
name|Exchange
operator|.
name|BATCH_COMPLETE
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|mock
operator|.
name|message
argument_list|(
literal|10
argument_list|)
operator|.
name|exchangeProperty
argument_list|(
name|Exchange
operator|.
name|BATCH_COMPLETE
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|mock
operator|.
name|message
argument_list|(
literal|11
argument_list|)
operator|.
name|exchangeProperty
argument_list|(
name|Exchange
operator|.
name|BATCH_COMPLETE
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|mock
operator|.
name|message
argument_list|(
literal|12
argument_list|)
operator|.
name|exchangeProperty
argument_list|(
name|Exchange
operator|.
name|BATCH_COMPLETE
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|mock
operator|.
name|message
argument_list|(
literal|13
argument_list|)
operator|.
name|exchangeProperty
argument_list|(
name|Exchange
operator|.
name|BATCH_COMPLETE
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|mock
operator|.
name|message
argument_list|(
literal|14
argument_list|)
operator|.
name|exchangeProperty
argument_list|(
name|Exchange
operator|.
name|BATCH_COMPLETE
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|mock
operator|.
name|message
argument_list|(
literal|15
argument_list|)
operator|.
name|exchangeProperty
argument_list|(
name|Exchange
operator|.
name|BATCH_COMPLETE
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|mock
operator|.
name|message
argument_list|(
literal|16
argument_list|)
operator|.
name|exchangeProperty
argument_list|(
name|Exchange
operator|.
name|BATCH_COMPLETE
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|mock
operator|.
name|message
argument_list|(
literal|17
argument_list|)
operator|.
name|exchangeProperty
argument_list|(
name|Exchange
operator|.
name|BATCH_COMPLETE
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|mock
operator|.
name|message
argument_list|(
literal|18
argument_list|)
operator|.
name|exchangeProperty
argument_list|(
name|Exchange
operator|.
name|BATCH_COMPLETE
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|mock
operator|.
name|message
argument_list|(
literal|19
argument_list|)
operator|.
name|exchangeProperty
argument_list|(
name|Exchange
operator|.
name|BATCH_COMPLETE
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedPropertyReceived
argument_list|(
name|Exchange
operator|.
name|BATCH_SIZE
argument_list|,
literal|20
argument_list|)
expr_stmt|;
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
for|for
control|(
name|int
name|counter
init|=
literal|0
init|;
name|counter
operator|<
literal|20
condition|;
name|counter
operator|++
control|)
block|{
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
literal|"mycamelbucket"
argument_list|)
expr_stmt|;
name|s3Object
operator|.
name|setKey
argument_list|(
literal|"counter-"
operator|+
name|counter
argument_list|)
expr_stmt|;
name|clientMock
operator|.
name|objects
operator|.
name|add
argument_list|(
name|s3Object
argument_list|)
expr_stmt|;
block|}
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
literal|"aws-s3://mycamelbucket?amazonS3Client=#amazonS3Client&delay=5000&maxMessagesPerPoll=0"
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
block|}
end_class

end_unit

