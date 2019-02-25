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
name|Rule
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
name|rules
operator|.
name|ExpectedException
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
name|containsString
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

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|never
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|verify
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|when
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
DECL|class|DdbStreamEndpointTest
specifier|public
class|class
name|DdbStreamEndpointTest
block|{
annotation|@
name|Rule
DECL|field|expectedException
specifier|public
name|ExpectedException
name|expectedException
init|=
name|ExpectedException
operator|.
name|none
argument_list|()
decl_stmt|;
DECL|field|context
specifier|private
name|CamelContext
name|context
decl_stmt|;
annotation|@
name|Mock
DECL|field|sequenceNumberProvider
specifier|private
name|SequenceNumberProvider
name|sequenceNumberProvider
decl_stmt|;
annotation|@
name|Mock
DECL|field|amazonDynamoDBStreams
specifier|private
name|AmazonDynamoDBStreams
name|amazonDynamoDBStreams
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
name|put
argument_list|(
literal|"someSeqNumProv"
argument_list|,
name|sequenceNumberProvider
argument_list|)
expr_stmt|;
name|registry
operator|.
name|put
argument_list|(
literal|"ddbStreamsClient"
argument_list|,
name|amazonDynamoDBStreams
argument_list|)
expr_stmt|;
name|context
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
DECL|method|itExtractsTheSequenceNumber ()
specifier|public
name|void
name|itExtractsTheSequenceNumber
parameter_list|()
throws|throws
name|Exception
block|{
name|when
argument_list|(
name|sequenceNumberProvider
operator|.
name|getSequenceNumber
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|"seq"
argument_list|)
expr_stmt|;
name|DdbStreamEndpoint
name|undertest
init|=
operator|(
name|DdbStreamEndpoint
operator|)
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"aws-ddbstream://table"
operator|+
literal|"?amazonDynamoDbStreamsClient=#ddbStreamsClient"
operator|+
literal|"&iteratorType=AFTER_SEQUENCE_NUMBER"
operator|+
literal|"&sequenceNumberProvider=#someSeqNumProv"
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|undertest
operator|.
name|getSequenceNumber
argument_list|()
argument_list|,
name|is
argument_list|(
literal|"seq"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|itExtractsTheSequenceNumberFromALiteralString ()
specifier|public
name|void
name|itExtractsTheSequenceNumberFromALiteralString
parameter_list|()
throws|throws
name|Exception
block|{
name|DdbStreamEndpoint
name|undertest
init|=
operator|(
name|DdbStreamEndpoint
operator|)
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"aws-ddbstream://table"
operator|+
literal|"?amazonDynamoDbStreamsClient=#ddbStreamsClient"
operator|+
literal|"&iteratorType=AFTER_SEQUENCE_NUMBER"
operator|+
literal|"&sequenceNumberProvider=seq"
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|undertest
operator|.
name|getSequenceNumber
argument_list|()
argument_list|,
name|is
argument_list|(
literal|"seq"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|onSequenceNumberAgnosticIteratorsTheProviderIsIgnored ()
specifier|public
name|void
name|onSequenceNumberAgnosticIteratorsTheProviderIsIgnored
parameter_list|()
throws|throws
name|Exception
block|{
name|DdbStreamEndpoint
name|undertest
init|=
operator|(
name|DdbStreamEndpoint
operator|)
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"aws-ddbstream://table"
operator|+
literal|"?amazonDynamoDbStreamsClient=#ddbStreamsClient"
operator|+
literal|"&iteratorType=LATEST"
operator|+
literal|"&sequenceNumberProvider=#someSeqNumProv"
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|undertest
operator|.
name|getSequenceNumber
argument_list|()
argument_list|,
name|is
argument_list|(
literal|""
argument_list|)
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|sequenceNumberProvider
argument_list|,
name|never
argument_list|()
argument_list|)
operator|.
name|getSequenceNumber
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|sequenceNumberFetchingThrowsSomethingUsefulIfMisconfigurered ()
specifier|public
name|void
name|sequenceNumberFetchingThrowsSomethingUsefulIfMisconfigurered
parameter_list|()
throws|throws
name|Exception
block|{
name|expectedException
operator|.
name|expectMessage
argument_list|(
name|containsString
argument_list|(
literal|"sequenceNumberProvider"
argument_list|)
argument_list|)
expr_stmt|;
name|DdbStreamEndpoint
name|undertest
init|=
operator|(
name|DdbStreamEndpoint
operator|)
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"aws-ddbstream://table"
operator|+
literal|"?amazonDynamoDbStreamsClient=#ddbStreamsClient"
operator|+
literal|"&iteratorType=AT_SEQUENCE_NUMBER"
argument_list|)
decl_stmt|;
comment|// NOTE: missing sequence number provider parameter
name|undertest
operator|.
name|getSequenceNumber
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

