begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.olingo2
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|olingo2
package|;
end_package

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
name|AvailablePortFinder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|olingo
operator|.
name|odata2
operator|.
name|api
operator|.
name|ep
operator|.
name|entry
operator|.
name|ODataEntry
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|olingo
operator|.
name|odata2
operator|.
name|api
operator|.
name|ep
operator|.
name|feed
operator|.
name|ODataFeed
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|AfterClass
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|BeforeClass
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
comment|/**  * Test class for {@link org.apache.camel.component.olingo2.api.Olingo2App}  * APIs.  *<p>  * The integration test runs against Apache Olingo 2.0 sample server which is  * dynamically installed and started during the test.  *</p>  */
end_comment

begin_class
DECL|class|Olingo2ComponentConsumerTest
specifier|public
class|class
name|Olingo2ComponentConsumerTest
extends|extends
name|AbstractOlingo2TestSupport
block|{
DECL|field|PORT
specifier|private
specifier|static
specifier|final
name|int
name|PORT
init|=
name|AvailablePortFinder
operator|.
name|getNextAvailable
argument_list|()
decl_stmt|;
DECL|field|TEST_SERVICE_URL
specifier|private
specifier|static
specifier|final
name|String
name|TEST_SERVICE_URL
init|=
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/MyFormula.svc"
decl_stmt|;
DECL|field|server
specifier|private
specifier|static
name|Olingo2SampleServer
name|server
decl_stmt|;
DECL|method|Olingo2ComponentConsumerTest ()
specifier|public
name|Olingo2ComponentConsumerTest
parameter_list|()
block|{
name|setDefaultTestProperty
argument_list|(
literal|"serviceUri"
argument_list|,
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/MyFormula.svc"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|BeforeClass
DECL|method|beforeClass ()
specifier|public
specifier|static
name|void
name|beforeClass
parameter_list|()
throws|throws
name|Exception
block|{
name|startServers
argument_list|(
name|PORT
argument_list|)
expr_stmt|;
name|Olingo2SampleServer
operator|.
name|generateSampleData
argument_list|(
name|TEST_SERVICE_URL
argument_list|)
expr_stmt|;
block|}
annotation|@
name|AfterClass
DECL|method|afterClass ()
specifier|public
specifier|static
name|void
name|afterClass
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|server
operator|!=
literal|null
condition|)
block|{
name|server
operator|.
name|stop
argument_list|()
expr_stmt|;
name|server
operator|.
name|destroy
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|startServers (int port)
specifier|protected
specifier|static
name|void
name|startServers
parameter_list|(
name|int
name|port
parameter_list|)
throws|throws
name|Exception
block|{
name|server
operator|=
operator|new
name|Olingo2SampleServer
argument_list|(
name|port
argument_list|,
literal|"/olingo2_ref"
argument_list|)
expr_stmt|;
name|server
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
DECL|method|addRouteAndStartContext (RouteBuilder builder)
specifier|private
name|void
name|addRouteAndStartContext
parameter_list|(
name|RouteBuilder
name|builder
parameter_list|)
throws|throws
name|Exception
block|{
name|context
argument_list|()
operator|.
name|addRoutes
argument_list|(
name|builder
argument_list|)
expr_stmt|;
name|startCamelContext
argument_list|()
expr_stmt|;
block|}
comment|/**      * Read entity set of the People object and filter already seen items on      * subsequent exchanges Use a delay since the mock endpoint does not always      * get the correct number of exchanges before being satisfied. Note: -      * consumer.splitResults is set to false since this ensures the first      * returned message contains all the results. This is preferred for the      * purposes of this test. The default will mean the first n messages contain      * the results (where n is the result total) then subsequent messages will      * be empty      */
annotation|@
name|Test
DECL|method|testConsumerReadFilterAlreadySeen ()
specifier|public
name|void
name|testConsumerReadFilterAlreadySeen
parameter_list|()
throws|throws
name|Exception
block|{
name|int
name|expectedMsgCount
init|=
literal|3
decl_stmt|;
name|MockEndpoint
name|mockEndpoint
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:consumer-alreadyseen"
argument_list|)
decl_stmt|;
name|mockEndpoint
operator|.
name|expectedMessageCount
argument_list|(
name|expectedMsgCount
argument_list|)
expr_stmt|;
name|mockEndpoint
operator|.
name|setResultWaitTime
argument_list|(
literal|60000
argument_list|)
expr_stmt|;
name|RouteBuilder
name|builder
init|=
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
literal|"olingo2://read/Manufacturers?filterAlreadySeen=true&"
operator|+
literal|"delay=2&consumer.sendEmptyMessageWhenIdle=true&"
operator|+
literal|"consumer.splitResult=false"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:consumer-alreadyseen"
argument_list|)
expr_stmt|;
block|}
empty_stmt|;
block|}
decl_stmt|;
name|addRouteAndStartContext
argument_list|(
name|builder
argument_list|)
expr_stmt|;
name|mockEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|expectedMsgCount
condition|;
operator|++
name|i
control|)
block|{
name|Object
name|body
init|=
name|mockEndpoint
operator|.
name|getExchanges
argument_list|()
operator|.
name|get
argument_list|(
name|i
argument_list|)
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
if|if
condition|(
name|i
operator|==
literal|0
condition|)
block|{
comment|//
comment|// First polled messages contained all the manufacturers
comment|//
name|assertTrue
argument_list|(
name|body
operator|instanceof
name|ODataFeed
argument_list|)
expr_stmt|;
name|ODataFeed
name|set
init|=
operator|(
name|ODataFeed
operator|)
name|body
decl_stmt|;
name|assertTrue
argument_list|(
name|set
operator|.
name|getEntries
argument_list|()
operator|.
name|size
argument_list|()
operator|>
literal|0
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|//
comment|// Subsequent polling messages should be empty
comment|// since the filterAlreadySeen property is true
comment|//
name|assertNull
argument_list|(
name|body
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Read entity set of the People object and filter already seen items on      * subsequent exchanges Use a delay since the mock endpoint does not always      * get the correct number of exchanges before being satisfied. Note: -      * consumer.splitResults is set to false since this ensures the first      * returned message contains all the results. -      * consumer.sendEmptyMessageWhenIdle is set to false so only 1 message      * should even be returned.      */
annotation|@
name|Test
DECL|method|testConsumerReadFilterAlreadySeenNoEmptyMsgs ()
specifier|public
name|void
name|testConsumerReadFilterAlreadySeenNoEmptyMsgs
parameter_list|()
throws|throws
name|Exception
block|{
name|int
name|expectedMsgCount
init|=
literal|1
decl_stmt|;
name|MockEndpoint
name|mockEndpoint
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:consumer-alreadyseen"
argument_list|)
decl_stmt|;
comment|//
comment|// Add 1 to count since we want to wait for full result time
comment|// before asserting that only 1 message has been delivered
comment|//
name|mockEndpoint
operator|.
name|expectedMinimumMessageCount
argument_list|(
name|expectedMsgCount
operator|+
literal|1
argument_list|)
expr_stmt|;
name|mockEndpoint
operator|.
name|setResultWaitTime
argument_list|(
literal|6000L
argument_list|)
expr_stmt|;
name|RouteBuilder
name|builder
init|=
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
literal|"olingo2://read/Manufacturers?filterAlreadySeen=true&"
operator|+
literal|"delay=2&consumer.sendEmptyMessageWhenIdle=false&"
operator|+
literal|"consumer.splitResult=false"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:consumer-alreadyseen"
argument_list|)
expr_stmt|;
block|}
empty_stmt|;
block|}
decl_stmt|;
name|addRouteAndStartContext
argument_list|(
name|builder
argument_list|)
expr_stmt|;
comment|//
comment|// Want to wait for entire result time& there should
comment|// be exactly 1 exchange transmitted to the endpoint
comment|//
name|mockEndpoint
operator|.
name|assertIsNotSatisfied
argument_list|()
expr_stmt|;
comment|// Only 1 exchange so this is good!
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|mockEndpoint
operator|.
name|getExchanges
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Object
name|body
init|=
name|mockEndpoint
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
argument_list|()
decl_stmt|;
comment|//
comment|// Only polled message contains all the entities
comment|//
name|assertTrue
argument_list|(
name|body
operator|instanceof
name|ODataFeed
argument_list|)
expr_stmt|;
name|ODataFeed
name|set
init|=
operator|(
name|ODataFeed
operator|)
name|body
decl_stmt|;
name|assertTrue
argument_list|(
name|set
operator|.
name|getEntries
argument_list|()
operator|.
name|size
argument_list|()
operator|>
literal|0
argument_list|)
expr_stmt|;
block|}
comment|/**      * WithPredicate in address FilterAlreadySeen: true SplitResults: true      * consumer.sendEmptyMessageWhenIdle: true      *      * @throws Exception      */
annotation|@
name|Test
DECL|method|testConsumerReadFilterAlreadySeenWithPredicate1 ()
specifier|public
name|void
name|testConsumerReadFilterAlreadySeenWithPredicate1
parameter_list|()
throws|throws
name|Exception
block|{
name|int
name|expectedMsgCount
init|=
literal|3
decl_stmt|;
name|MockEndpoint
name|mockEndpoint
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:consumer-splitresult-kp-manufacturer"
argument_list|)
decl_stmt|;
name|mockEndpoint
operator|.
name|expectedMinimumMessageCount
argument_list|(
name|expectedMsgCount
argument_list|)
expr_stmt|;
name|RouteBuilder
name|builder
init|=
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
literal|"olingo2://read/Manufacturers('1')?filterAlreadySeen=true&"
operator|+
literal|"delay=2&consumer.sendEmptyMessageWhenIdle=true&"
operator|+
literal|"consumer.splitResult=true"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:consumer-splitresult-kp-manufacturer"
argument_list|)
expr_stmt|;
block|}
empty_stmt|;
block|}
decl_stmt|;
name|addRouteAndStartContext
argument_list|(
name|builder
argument_list|)
expr_stmt|;
name|mockEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|expectedMsgCount
condition|;
operator|++
name|i
control|)
block|{
name|Object
name|body
init|=
name|mockEndpoint
operator|.
name|getExchanges
argument_list|()
operator|.
name|get
argument_list|(
name|i
argument_list|)
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
if|if
condition|(
name|i
operator|==
literal|0
condition|)
block|{
comment|//
comment|// First polled messages contained all the entities
comment|//
name|assertTrue
argument_list|(
name|body
operator|instanceof
name|ODataEntry
argument_list|)
expr_stmt|;
name|ODataEntry
name|entry
init|=
operator|(
name|ODataEntry
operator|)
name|body
decl_stmt|;
name|Object
name|nameValue
init|=
name|entry
operator|.
name|getProperties
argument_list|()
operator|.
name|get
argument_list|(
literal|"Name"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|nameValue
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Star Powered Racing"
argument_list|,
name|nameValue
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|//
comment|// Subsequent polling messages should be empty
comment|// since the filterAlreadySeen property is true
comment|//
name|assertNull
argument_list|(
name|body
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * WithPredicate in address FilterAlreadySeen: true SplitResults: true      * consumer.sendEmptyMessageWhenIdle: false      *      * @throws Exception      */
annotation|@
name|Test
DECL|method|testConsumerReadFilterAlreadySeenWithPredicate2 ()
specifier|public
name|void
name|testConsumerReadFilterAlreadySeenWithPredicate2
parameter_list|()
throws|throws
name|Exception
block|{
name|int
name|expectedMsgCount
init|=
literal|1
decl_stmt|;
name|MockEndpoint
name|mockEndpoint
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:consumer-splitresult-kp-manufacturer"
argument_list|)
decl_stmt|;
comment|//
comment|// Add 1 to count since we want to wait for full result time
comment|// before asserting that only 1 message has been delivered
comment|//
name|mockEndpoint
operator|.
name|expectedMinimumMessageCount
argument_list|(
name|expectedMsgCount
operator|+
literal|1
argument_list|)
expr_stmt|;
name|mockEndpoint
operator|.
name|setResultWaitTime
argument_list|(
literal|6000L
argument_list|)
expr_stmt|;
name|RouteBuilder
name|builder
init|=
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
literal|"olingo2://read/Manufacturers('1')?filterAlreadySeen=true&"
operator|+
literal|"delay=2&consumer.sendEmptyMessageWhenIdle=false&"
operator|+
literal|"consumer.splitResult=true"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:consumer-splitresult-kp-manufacturer"
argument_list|)
expr_stmt|;
block|}
empty_stmt|;
block|}
decl_stmt|;
name|addRouteAndStartContext
argument_list|(
name|builder
argument_list|)
expr_stmt|;
comment|//
comment|// Want to wait for entire result time& there should
comment|// be exactly 1 exchange transmitted to the endpoint
comment|//
name|mockEndpoint
operator|.
name|assertIsNotSatisfied
argument_list|()
expr_stmt|;
comment|// Only 1 exchange so this is good!
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|mockEndpoint
operator|.
name|getExchanges
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Object
name|body
init|=
name|mockEndpoint
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
argument_list|()
decl_stmt|;
comment|//
comment|// Only polled message contains the entity
comment|//
name|assertTrue
argument_list|(
name|body
operator|instanceof
name|ODataEntry
argument_list|)
expr_stmt|;
name|ODataEntry
name|entry
init|=
operator|(
name|ODataEntry
operator|)
name|body
decl_stmt|;
name|Object
name|nameValue
init|=
name|entry
operator|.
name|getProperties
argument_list|()
operator|.
name|get
argument_list|(
literal|"Name"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|nameValue
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Star Powered Racing"
argument_list|,
name|nameValue
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Read value of the People object and split the results into individual      * messages      */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|Test
DECL|method|testConsumerReadClientValuesSplitResults ()
specifier|public
name|void
name|testConsumerReadClientValuesSplitResults
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mockEndpoint
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:consumer-value"
argument_list|)
decl_stmt|;
name|mockEndpoint
operator|.
name|expectedMinimumMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mockEndpoint
operator|.
name|setResultWaitTime
argument_list|(
literal|60000
argument_list|)
expr_stmt|;
name|RouteBuilder
name|builder
init|=
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
literal|"olingo2://read/Manufacturers('1')/Address?consumer.splitResult=true"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:consumer-value"
argument_list|)
expr_stmt|;
block|}
empty_stmt|;
block|}
decl_stmt|;
name|addRouteAndStartContext
argument_list|(
name|builder
argument_list|)
expr_stmt|;
name|mockEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
comment|//
comment|// 1 individual message in the exchange
comment|//
name|Object
name|body
init|=
name|mockEndpoint
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
argument_list|()
decl_stmt|;
name|assertIsInstanceOf
argument_list|(
name|Map
operator|.
name|class
argument_list|,
name|body
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|value
init|=
operator|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
operator|)
name|body
decl_stmt|;
name|Object
name|addrObj
init|=
name|value
operator|.
name|get
argument_list|(
literal|"Address"
argument_list|)
decl_stmt|;
name|assertIsInstanceOf
argument_list|(
name|Map
operator|.
name|class
argument_list|,
name|addrObj
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|addrMap
init|=
operator|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
operator|)
name|addrObj
decl_stmt|;
name|assertEquals
argument_list|(
literal|"70173"
argument_list|,
name|addrMap
operator|.
name|get
argument_list|(
literal|"ZipCode"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Star Street 137"
argument_list|,
name|addrMap
operator|.
name|get
argument_list|(
literal|"Street"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Germany"
argument_list|,
name|addrMap
operator|.
name|get
argument_list|(
literal|"Country"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Stuttgart"
argument_list|,
name|addrMap
operator|.
name|get
argument_list|(
literal|"City"
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Read entity set of the Manufacturers object and split the results into      * individual messages      */
annotation|@
name|Test
DECL|method|testConsumerReadSplitResults ()
specifier|public
name|void
name|testConsumerReadSplitResults
parameter_list|()
throws|throws
name|Exception
block|{
name|int
name|expectedMsgCount
init|=
literal|2
decl_stmt|;
name|MockEndpoint
name|mockEndpoint
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:consumer-splitresult"
argument_list|)
decl_stmt|;
name|mockEndpoint
operator|.
name|expectedMinimumMessageCount
argument_list|(
name|expectedMsgCount
argument_list|)
expr_stmt|;
name|RouteBuilder
name|builder
init|=
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
literal|"olingo2://read/Manufacturers?consumer.splitResult=true"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:consumer-splitresult"
argument_list|)
expr_stmt|;
block|}
empty_stmt|;
block|}
decl_stmt|;
name|addRouteAndStartContext
argument_list|(
name|builder
argument_list|)
expr_stmt|;
name|mockEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
comment|//
comment|// 2 individual messages in the exchange,
comment|// each containing a different entity.
comment|//
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|expectedMsgCount
condition|;
operator|++
name|i
control|)
block|{
name|Object
name|body
init|=
name|mockEndpoint
operator|.
name|getExchanges
argument_list|()
operator|.
name|get
argument_list|(
name|i
argument_list|)
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|body
operator|instanceof
name|ODataEntry
argument_list|)
expr_stmt|;
name|ODataEntry
name|entry
init|=
operator|(
name|ODataEntry
operator|)
name|body
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
init|=
name|entry
operator|.
name|getProperties
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|properties
argument_list|)
expr_stmt|;
name|Object
name|name
init|=
name|properties
operator|.
name|get
argument_list|(
literal|"Name"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|name
operator|.
name|toString
argument_list|()
operator|.
name|contains
argument_list|(
literal|"Powered Racing"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

