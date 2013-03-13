begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.dataformat.xmljson
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|dataformat
operator|.
name|xmljson
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
name|concurrent
operator|.
name|Callable
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
name|ExecutorService
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
name|Executors
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

begin_comment
comment|/**  * Stress tests for the XML JSON data format: concurrency and large JSON and XML documents  */
end_comment

begin_class
DECL|class|XmlJsonStressTest
specifier|public
class|class
name|XmlJsonStressTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|Test
DECL|method|testNoConcurrentProducers ()
specifier|public
name|void
name|testNoConcurrentProducers
parameter_list|()
throws|throws
name|Exception
block|{
name|doSendMessages
argument_list|(
literal|1
argument_list|,
literal|1
argument_list|,
literal|"direct:startFromXML"
argument_list|,
literal|"org/apache/camel/dataformat/xmljson/testMessage1.xml"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testConcurrentProducers ()
specifier|public
name|void
name|testConcurrentProducers
parameter_list|()
throws|throws
name|Exception
block|{
name|doSendMessages
argument_list|(
literal|1000
argument_list|,
literal|5
argument_list|,
literal|"direct:startFromXML"
argument_list|,
literal|"org/apache/camel/dataformat/xmljson/testMessage1.xml"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testLargeMessages ()
specifier|public
name|void
name|testLargeMessages
parameter_list|()
throws|throws
name|Exception
block|{
name|doSendMessages
argument_list|(
literal|5
argument_list|,
literal|2
argument_list|,
literal|"direct:startFromXML"
argument_list|,
literal|"org/apache/camel/dataformat/xmljson/testMessage3-large.xml"
argument_list|)
expr_stmt|;
name|resetMocks
argument_list|()
expr_stmt|;
name|doSendMessages
argument_list|(
literal|5
argument_list|,
literal|2
argument_list|,
literal|"direct:startFromJson"
argument_list|,
literal|"org/apache/camel/dataformat/xmljson/testMessage3-large.json"
argument_list|)
expr_stmt|;
block|}
DECL|method|doSendMessages (int files, int poolSize, final String endpointURI, String resourceURI)
specifier|private
name|void
name|doSendMessages
parameter_list|(
name|int
name|files
parameter_list|,
name|int
name|poolSize
parameter_list|,
specifier|final
name|String
name|endpointURI
parameter_list|,
name|String
name|resourceURI
parameter_list|)
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mockJSON
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:json"
argument_list|)
decl_stmt|;
name|MockEndpoint
name|mockXML
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:xml"
argument_list|)
decl_stmt|;
name|mockJSON
operator|.
name|expectedMessageCount
argument_list|(
name|files
argument_list|)
expr_stmt|;
name|mockXML
operator|.
name|expectedMessageCount
argument_list|(
name|files
argument_list|)
expr_stmt|;
name|InputStream
name|inStream
init|=
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
name|resourceURI
argument_list|)
decl_stmt|;
specifier|final
name|String
name|in
init|=
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|inStream
argument_list|)
decl_stmt|;
name|ExecutorService
name|executor
init|=
name|Executors
operator|.
name|newFixedThreadPool
argument_list|(
name|poolSize
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|files
condition|;
name|i
operator|++
control|)
block|{
name|executor
operator|.
name|submit
argument_list|(
operator|new
name|Callable
argument_list|<
name|Object
argument_list|>
argument_list|()
block|{
specifier|public
name|Object
name|call
parameter_list|()
throws|throws
name|Exception
block|{
name|template
operator|.
name|sendBody
argument_list|(
name|endpointURI
argument_list|,
name|in
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
comment|// give much time as slow servers may take long time
name|assertMockEndpointsSatisfied
argument_list|(
literal|60
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
comment|// test that all messages are equal
name|Object
name|jsonBody
init|=
name|mockJSON
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
decl_stmt|;
name|Object
name|xmlBody
init|=
name|mockXML
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
decl_stmt|;
for|for
control|(
name|Exchange
name|e
range|:
name|mockJSON
operator|.
name|getExchanges
argument_list|()
control|)
block|{
name|assertEquals
argument_list|(
literal|"Bodies are expected to be equal (json mock endpoint)"
argument_list|,
name|jsonBody
argument_list|,
name|e
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
for|for
control|(
name|Exchange
name|e
range|:
name|mockXML
operator|.
name|getExchanges
argument_list|()
control|)
block|{
name|assertEquals
argument_list|(
literal|"Bodies are expected to be equal (xml mock endpoint)"
argument_list|,
name|xmlBody
argument_list|,
name|e
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
name|mockJSON
operator|.
name|reset
argument_list|()
expr_stmt|;
name|mockXML
operator|.
name|reset
argument_list|()
expr_stmt|;
name|executor
operator|.
name|shutdownNow
argument_list|()
expr_stmt|;
block|}
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
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
name|XmlJsonDataFormat
name|format
init|=
operator|new
name|XmlJsonDataFormat
argument_list|()
decl_stmt|;
comment|// from XML to JSON
name|from
argument_list|(
literal|"direct:startFromXML"
argument_list|)
operator|.
name|marshal
argument_list|(
name|format
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:json"
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct:unmarshalToJson"
argument_list|)
expr_stmt|;
comment|// from JSON to XML
name|from
argument_list|(
literal|"direct:unmarshalToJson"
argument_list|)
operator|.
name|unmarshal
argument_list|(
name|format
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:xml"
argument_list|)
expr_stmt|;
comment|// from JSON to XML
name|from
argument_list|(
literal|"direct:startFromJson"
argument_list|)
operator|.
name|unmarshal
argument_list|(
name|format
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:json"
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct:marshalToXML"
argument_list|)
expr_stmt|;
comment|// from XML to JSON
name|from
argument_list|(
literal|"direct:marshalToXML"
argument_list|)
operator|.
name|marshal
argument_list|(
name|format
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:xml"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

