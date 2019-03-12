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
name|CamelContext
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
name|DefaultCamelContext
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
name|SimpleRegistry
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
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

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runner
operator|.
name|RunWith
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mockito
operator|.
name|Mock
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mockito
operator|.
name|junit
operator|.
name|MockitoJUnitRunner
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|CoreMatchers
operator|.
name|is
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertThat
import|;
end_import

begin_class
annotation|@
name|RunWith
argument_list|(
name|MockitoJUnitRunner
operator|.
name|class
argument_list|)
DECL|class|KinesisEndpointTest
specifier|public
class|class
name|KinesisEndpointTest
block|{
annotation|@
name|Mock
DECL|field|amazonKinesisClient
specifier|private
name|AmazonKinesis
name|amazonKinesisClient
decl_stmt|;
DECL|field|camelContext
specifier|private
name|CamelContext
name|camelContext
decl_stmt|;
annotation|@
name|Before
DECL|method|setup ()
specifier|public
name|void
name|setup
parameter_list|()
throws|throws
name|Exception
block|{
name|SimpleRegistry
name|registry
init|=
operator|new
name|SimpleRegistry
argument_list|()
decl_stmt|;
name|registry
operator|.
name|bind
argument_list|(
literal|"kinesisClient"
argument_list|,
name|amazonKinesisClient
argument_list|)
expr_stmt|;
name|camelContext
operator|=
operator|new
name|DefaultCamelContext
argument_list|(
name|registry
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|allTheEndpointParams ()
specifier|public
name|void
name|allTheEndpointParams
parameter_list|()
throws|throws
name|Exception
block|{
name|KinesisEndpoint
name|endpoint
init|=
operator|(
name|KinesisEndpoint
operator|)
name|camelContext
operator|.
name|getEndpoint
argument_list|(
literal|"aws-kinesis://some_stream_name"
operator|+
literal|"?amazonKinesisClient=#kinesisClient"
operator|+
literal|"&maxResultsPerRequest=101"
operator|+
literal|"&iteratorType=latest"
operator|+
literal|"&shardId=abc"
operator|+
literal|"&sequenceNumber=123"
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getAmazonKinesisClient
argument_list|()
argument_list|,
name|is
argument_list|(
name|amazonKinesisClient
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getStreamName
argument_list|()
argument_list|,
name|is
argument_list|(
literal|"some_stream_name"
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getIteratorType
argument_list|()
argument_list|,
name|is
argument_list|(
name|ShardIteratorType
operator|.
name|LATEST
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getMaxResultsPerRequest
argument_list|()
argument_list|,
name|is
argument_list|(
literal|101
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getSequenceNumber
argument_list|()
argument_list|,
name|is
argument_list|(
literal|"123"
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getShardId
argument_list|()
argument_list|,
name|is
argument_list|(
literal|"abc"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|onlyRequiredEndpointParams ()
specifier|public
name|void
name|onlyRequiredEndpointParams
parameter_list|()
throws|throws
name|Exception
block|{
name|KinesisEndpoint
name|endpoint
init|=
operator|(
name|KinesisEndpoint
operator|)
name|camelContext
operator|.
name|getEndpoint
argument_list|(
literal|"aws-kinesis://some_stream_name"
operator|+
literal|"?amazonKinesisClient=#kinesisClient"
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getAmazonKinesisClient
argument_list|()
argument_list|,
name|is
argument_list|(
name|amazonKinesisClient
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getStreamName
argument_list|()
argument_list|,
name|is
argument_list|(
literal|"some_stream_name"
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getIteratorType
argument_list|()
argument_list|,
name|is
argument_list|(
name|ShardIteratorType
operator|.
name|TRIM_HORIZON
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getMaxResultsPerRequest
argument_list|()
argument_list|,
name|is
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|afterSequenceNumberRequiresSequenceNumber ()
specifier|public
name|void
name|afterSequenceNumberRequiresSequenceNumber
parameter_list|()
throws|throws
name|Exception
block|{
name|KinesisEndpoint
name|endpoint
init|=
operator|(
name|KinesisEndpoint
operator|)
name|camelContext
operator|.
name|getEndpoint
argument_list|(
literal|"aws-kinesis://some_stream_name"
operator|+
literal|"?amazonKinesisClient=#kinesisClient"
operator|+
literal|"&iteratorType=AFTER_SEQUENCE_NUMBER"
operator|+
literal|"&shardId=abc"
operator|+
literal|"&sequenceNumber=123"
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getAmazonKinesisClient
argument_list|()
argument_list|,
name|is
argument_list|(
name|amazonKinesisClient
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getStreamName
argument_list|()
argument_list|,
name|is
argument_list|(
literal|"some_stream_name"
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getIteratorType
argument_list|()
argument_list|,
name|is
argument_list|(
name|ShardIteratorType
operator|.
name|AFTER_SEQUENCE_NUMBER
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getShardId
argument_list|()
argument_list|,
name|is
argument_list|(
literal|"abc"
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getSequenceNumber
argument_list|()
argument_list|,
name|is
argument_list|(
literal|"123"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|atSequenceNumberRequiresSequenceNumber ()
specifier|public
name|void
name|atSequenceNumberRequiresSequenceNumber
parameter_list|()
throws|throws
name|Exception
block|{
name|KinesisEndpoint
name|endpoint
init|=
operator|(
name|KinesisEndpoint
operator|)
name|camelContext
operator|.
name|getEndpoint
argument_list|(
literal|"aws-kinesis://some_stream_name"
operator|+
literal|"?amazonKinesisClient=#kinesisClient"
operator|+
literal|"&iteratorType=AT_SEQUENCE_NUMBER"
operator|+
literal|"&shardId=abc"
operator|+
literal|"&sequenceNumber=123"
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getAmazonKinesisClient
argument_list|()
argument_list|,
name|is
argument_list|(
name|amazonKinesisClient
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getStreamName
argument_list|()
argument_list|,
name|is
argument_list|(
literal|"some_stream_name"
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getIteratorType
argument_list|()
argument_list|,
name|is
argument_list|(
name|ShardIteratorType
operator|.
name|AT_SEQUENCE_NUMBER
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getShardId
argument_list|()
argument_list|,
name|is
argument_list|(
literal|"abc"
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getSequenceNumber
argument_list|()
argument_list|,
name|is
argument_list|(
literal|"123"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

