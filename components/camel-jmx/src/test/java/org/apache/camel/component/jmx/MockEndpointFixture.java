begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jmx
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jmx
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|TimeUnit
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|Source
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
name|Message
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
name|xmlunit
operator|.
name|xpath
operator|.
name|JAXPXPathEngine
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|jupiter
operator|.
name|api
operator|.
name|Assertions
operator|.
name|assertEquals
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|jupiter
operator|.
name|api
operator|.
name|Assertions
operator|.
name|assertNotNull
import|;
end_import

begin_comment
comment|/**  * Waits for messages to arrive on the mock endpoint and performs assertions on the message bodies.  */
end_comment

begin_class
DECL|class|MockEndpointFixture
specifier|public
class|class
name|MockEndpointFixture
block|{
DECL|field|mMockEndpoint
name|MockEndpoint
name|mMockEndpoint
decl_stmt|;
DECL|method|MockEndpointFixture (MockEndpoint aMockEndpoint)
specifier|public
name|MockEndpointFixture
parameter_list|(
name|MockEndpoint
name|aMockEndpoint
parameter_list|)
block|{
name|setMockEndpoint
argument_list|(
name|aMockEndpoint
argument_list|)
expr_stmt|;
block|}
DECL|method|waitForMessages ()
specifier|protected
name|void
name|waitForMessages
parameter_list|()
throws|throws
name|InterruptedException
block|{
name|waitForMessages
argument_list|(
name|mMockEndpoint
argument_list|)
expr_stmt|;
block|}
DECL|method|waitForMessages (MockEndpoint aMockEndpoint)
specifier|protected
name|void
name|waitForMessages
parameter_list|(
name|MockEndpoint
name|aMockEndpoint
parameter_list|)
throws|throws
name|InterruptedException
block|{
name|mMockEndpoint
operator|.
name|await
argument_list|(
literal|10
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|aMockEndpoint
operator|.
name|getExpectedCount
argument_list|()
argument_list|,
name|aMockEndpoint
operator|.
name|getReceivedCounter
argument_list|()
argument_list|,
literal|"Expected number of messages didn't arrive before timeout"
argument_list|)
expr_stmt|;
block|}
DECL|method|getMockEndpoint ()
specifier|protected
name|MockEndpoint
name|getMockEndpoint
parameter_list|()
block|{
return|return
name|mMockEndpoint
return|;
block|}
DECL|method|setMockEndpoint (MockEndpoint aMockEndpoint)
specifier|protected
name|void
name|setMockEndpoint
parameter_list|(
name|MockEndpoint
name|aMockEndpoint
parameter_list|)
block|{
name|mMockEndpoint
operator|=
name|aMockEndpoint
expr_stmt|;
block|}
comment|/**      * Assert that we've received the message and resets the mock endpoint      */
DECL|method|assertMessageReceived (File aExpectedFile)
specifier|protected
name|void
name|assertMessageReceived
parameter_list|(
name|File
name|aExpectedFile
parameter_list|)
throws|throws
name|Exception
block|{
name|Source
name|expectedDoc
init|=
name|XmlFixture
operator|.
name|toSource
argument_list|(
name|aExpectedFile
argument_list|)
decl_stmt|;
name|assertMessageReceived
argument_list|(
name|expectedDoc
argument_list|)
expr_stmt|;
block|}
DECL|method|assertMessageReceived (Source aExpectedDoc)
specifier|protected
name|void
name|assertMessageReceived
parameter_list|(
name|Source
name|aExpectedDoc
parameter_list|)
throws|throws
name|Exception
block|{
name|Source
name|actual
init|=
name|XmlFixture
operator|.
name|toSource
argument_list|(
name|getBody
argument_list|(
literal|0
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
name|assertMessageReceived
argument_list|(
name|aExpectedDoc
argument_list|,
name|actual
argument_list|)
expr_stmt|;
block|}
DECL|method|assertMessageReceived (Source aExpectedDoc, Source aActual)
specifier|protected
name|void
name|assertMessageReceived
parameter_list|(
name|Source
name|aExpectedDoc
parameter_list|,
name|Source
name|aActual
parameter_list|)
throws|throws
name|Exception
block|{
name|Source
name|noTime
init|=
name|XmlFixture
operator|.
name|stripTimestamp
argument_list|(
name|aActual
argument_list|)
decl_stmt|;
name|Source
name|noUUID
init|=
name|XmlFixture
operator|.
name|stripUUID
argument_list|(
name|noTime
argument_list|)
decl_stmt|;
name|XmlFixture
operator|.
name|assertXMLIgnorePrefix
argument_list|(
literal|"failed to match"
argument_list|,
name|aExpectedDoc
argument_list|,
name|noUUID
argument_list|)
expr_stmt|;
comment|// assert that we have a timestamp and datetime
comment|// can't rely on the datetime being the same due to timezone differences
comment|// instead, we'll assert that the values exist.
name|JAXPXPathEngine
name|xp
init|=
operator|new
name|JAXPXPathEngine
argument_list|()
decl_stmt|;
name|xp
operator|.
name|setNamespaceContext
argument_list|(
name|Collections
operator|.
name|singletonMap
argument_list|(
literal|"jmx"
argument_list|,
literal|"urn:org.apache.camel.component:jmx"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"1"
argument_list|,
name|xp
operator|.
name|evaluate
argument_list|(
literal|"count(//jmx:timestamp)"
argument_list|,
name|aActual
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"1"
argument_list|,
name|xp
operator|.
name|evaluate
argument_list|(
literal|"count(//jmx:dateTime)"
argument_list|,
name|aActual
argument_list|)
argument_list|)
expr_stmt|;
name|resetMockEndpoint
argument_list|()
expr_stmt|;
block|}
comment|/**      * Resets the mock endpoint so we can run another test. This will clear out any      * previously received messages.      */
DECL|method|resetMockEndpoint ()
specifier|protected
name|void
name|resetMockEndpoint
parameter_list|()
block|{
name|getMockEndpoint
argument_list|()
operator|.
name|reset
argument_list|()
expr_stmt|;
block|}
comment|/**      * Gets the body of the received message at the specified index      */
DECL|method|getBody (int aIndex, Class<T> aType)
specifier|protected
parameter_list|<
name|T
parameter_list|>
name|T
name|getBody
parameter_list|(
name|int
name|aIndex
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|aType
parameter_list|)
block|{
name|Message
name|in
init|=
name|getMessage
argument_list|(
name|aIndex
argument_list|)
decl_stmt|;
name|T
name|body
init|=
name|in
operator|.
name|getBody
argument_list|(
name|aType
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|body
argument_list|)
expr_stmt|;
return|return
name|body
return|;
block|}
comment|/**      * Gets the received message at the specified index      */
DECL|method|getMessage (int aIndex)
specifier|protected
name|Message
name|getMessage
parameter_list|(
name|int
name|aIndex
parameter_list|)
block|{
name|Exchange
name|exchange
init|=
name|getExchange
argument_list|(
name|aIndex
argument_list|)
decl_stmt|;
name|Message
name|in
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
return|return
name|in
return|;
block|}
comment|/**      * Gets the received exchange at the specified index      */
DECL|method|getExchange (int aIndex)
specifier|protected
name|Exchange
name|getExchange
parameter_list|(
name|int
name|aIndex
parameter_list|)
block|{
name|List
argument_list|<
name|Exchange
argument_list|>
name|exchanges
init|=
name|mMockEndpoint
operator|.
name|getReceivedExchanges
argument_list|()
decl_stmt|;
name|Exchange
name|exchange
init|=
name|exchanges
operator|.
name|get
argument_list|(
name|aIndex
argument_list|)
decl_stmt|;
return|return
name|exchange
return|;
block|}
block|}
end_class

end_unit

