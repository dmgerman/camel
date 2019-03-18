begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.splunk
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|splunk
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|com
operator|.
name|splunk
operator|.
name|Job
import|;
end_import

begin_import
import|import
name|com
operator|.
name|splunk
operator|.
name|JobCollection
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
name|component
operator|.
name|splunk
operator|.
name|event
operator|.
name|SplunkEvent
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
name|mockito
operator|.
name|ArgumentMatchers
operator|.
name|any
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|ArgumentMatchers
operator|.
name|anyString
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
DECL|class|ConsumerTest
specifier|public
class|class
name|ConsumerTest
extends|extends
name|SplunkMockTestSupport
block|{
annotation|@
name|Mock
DECL|field|jobCollection
name|JobCollection
name|jobCollection
decl_stmt|;
annotation|@
name|Mock
DECL|field|jobMock
name|Job
name|jobMock
decl_stmt|;
annotation|@
name|Test
DECL|method|testSearch ()
specifier|public
name|void
name|testSearch
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|searchMock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:search-result"
argument_list|)
decl_stmt|;
name|searchMock
operator|.
name|expectedMessageCount
argument_list|(
literal|3
argument_list|)
expr_stmt|;
name|searchMock
operator|.
name|expectedPropertyReceived
argument_list|(
name|Exchange
operator|.
name|BATCH_SIZE
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|service
operator|.
name|getJobs
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|jobCollection
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|jobCollection
operator|.
name|create
argument_list|(
name|anyString
argument_list|()
argument_list|,
name|any
argument_list|()
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|jobMock
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|jobMock
operator|.
name|isDone
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|InputStream
name|stream
init|=
name|ConsumerTest
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
literal|"/resultsreader_test_data.json"
argument_list|)
decl_stmt|;
name|when
argument_list|(
name|jobMock
operator|.
name|getResults
argument_list|(
name|any
argument_list|()
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|stream
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|SplunkEvent
name|recieved
init|=
name|searchMock
operator|.
name|getReceivedExchanges
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
name|SplunkEvent
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|recieved
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|data
init|=
name|recieved
operator|.
name|getEventData
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"indexertpool"
argument_list|,
name|data
operator|.
name|get
argument_list|(
literal|"name"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|searchMock
operator|.
name|getReceivedExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|2
argument_list|)
operator|.
name|getProperty
argument_list|(
name|Exchange
operator|.
name|BATCH_COMPLETE
argument_list|,
name|Boolean
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|stream
operator|.
name|close
argument_list|()
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
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
literal|"splunk://normal?delay=5s&username=foo&password=bar&initEarliestTime=-10s&latestTime=now&search=search index=myindex&sourceType=testSource"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:search-result"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

