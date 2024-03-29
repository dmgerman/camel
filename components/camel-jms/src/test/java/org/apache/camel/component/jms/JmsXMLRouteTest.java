begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jms
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jms
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileInputStream
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|ConnectionFactory
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
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|stream
operator|.
name|StreamSource
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
name|Processor
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
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|xml
operator|.
name|StringSource
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
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jms
operator|.
name|JmsComponent
operator|.
name|jmsComponentAutoAcknowledge
import|;
end_import

begin_comment
comment|/**  * For unit testing with XML streams that can be troublesome with the StreamCache  */
end_comment

begin_class
DECL|class|JmsXMLRouteTest
specifier|public
class|class
name|JmsXMLRouteTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|TEST_LONDON
specifier|private
specifier|static
specifier|final
name|String
name|TEST_LONDON
init|=
literal|"src/test/data/message1.xml"
decl_stmt|;
DECL|field|TEST_TAMPA
specifier|private
specifier|static
specifier|final
name|String
name|TEST_TAMPA
init|=
literal|"src/test/data/message2.xml"
decl_stmt|;
annotation|@
name|Test
DECL|method|testLondonWithFileStreamAsObject ()
specifier|public
name|void
name|testLondonWithFileStreamAsObject
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:london"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mock
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|body
argument_list|(
name|String
operator|.
name|class
argument_list|)
operator|.
name|contains
argument_list|(
literal|"James"
argument_list|)
expr_stmt|;
name|Source
name|source
init|=
operator|new
name|StreamSource
argument_list|(
operator|new
name|FileInputStream
argument_list|(
name|TEST_LONDON
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|source
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:object"
argument_list|,
name|source
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testLondonWithFileStreamAsBytes ()
specifier|public
name|void
name|testLondonWithFileStreamAsBytes
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:london"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mock
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|body
argument_list|(
name|String
operator|.
name|class
argument_list|)
operator|.
name|contains
argument_list|(
literal|"James"
argument_list|)
expr_stmt|;
name|Source
name|source
init|=
operator|new
name|StreamSource
argument_list|(
operator|new
name|FileInputStream
argument_list|(
name|TEST_LONDON
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|source
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:bytes"
argument_list|,
name|source
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testLondonWithFileStreamAsDefault ()
specifier|public
name|void
name|testLondonWithFileStreamAsDefault
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:london"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mock
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|body
argument_list|(
name|String
operator|.
name|class
argument_list|)
operator|.
name|contains
argument_list|(
literal|"James"
argument_list|)
expr_stmt|;
name|Source
name|source
init|=
operator|new
name|StreamSource
argument_list|(
operator|new
name|FileInputStream
argument_list|(
name|TEST_LONDON
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|source
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:default"
argument_list|,
name|source
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testTampaWithFileStreamAsObject ()
specifier|public
name|void
name|testTampaWithFileStreamAsObject
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:tampa"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mock
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|body
argument_list|(
name|String
operator|.
name|class
argument_list|)
operator|.
name|contains
argument_list|(
literal|"Hiram"
argument_list|)
expr_stmt|;
name|Source
name|source
init|=
operator|new
name|StreamSource
argument_list|(
operator|new
name|FileInputStream
argument_list|(
name|TEST_TAMPA
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|source
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:object"
argument_list|,
name|source
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testTampaWithFileStreamAsBytes ()
specifier|public
name|void
name|testTampaWithFileStreamAsBytes
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:tampa"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mock
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|body
argument_list|(
name|String
operator|.
name|class
argument_list|)
operator|.
name|contains
argument_list|(
literal|"Hiram"
argument_list|)
expr_stmt|;
name|Source
name|source
init|=
operator|new
name|StreamSource
argument_list|(
operator|new
name|FileInputStream
argument_list|(
name|TEST_TAMPA
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|source
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:bytes"
argument_list|,
name|source
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testTampaWithFileStreamAsDefault ()
specifier|public
name|void
name|testTampaWithFileStreamAsDefault
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:tampa"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mock
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|body
argument_list|(
name|String
operator|.
name|class
argument_list|)
operator|.
name|contains
argument_list|(
literal|"Hiram"
argument_list|)
expr_stmt|;
name|Source
name|source
init|=
operator|new
name|StreamSource
argument_list|(
operator|new
name|FileInputStream
argument_list|(
name|TEST_TAMPA
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|source
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:default"
argument_list|,
name|source
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testLondonWithStringSourceAsObject ()
specifier|public
name|void
name|testLondonWithStringSourceAsObject
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:london"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mock
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|body
argument_list|(
name|String
operator|.
name|class
argument_list|)
operator|.
name|contains
argument_list|(
literal|"James"
argument_list|)
expr_stmt|;
name|Source
name|source
init|=
operator|new
name|StringSource
argument_list|(
literal|"<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
operator|+
literal|"<person user=\"james\">\n"
operator|+
literal|"<firstName>James</firstName>\n"
operator|+
literal|"<lastName>Strachan</lastName>\n"
operator|+
literal|"<city>London</city>\n"
operator|+
literal|"</person>"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|source
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:object"
argument_list|,
name|source
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testLondonWithStringSourceAsBytes ()
specifier|public
name|void
name|testLondonWithStringSourceAsBytes
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:london"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mock
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|body
argument_list|(
name|String
operator|.
name|class
argument_list|)
operator|.
name|contains
argument_list|(
literal|"James"
argument_list|)
expr_stmt|;
name|Source
name|source
init|=
operator|new
name|StringSource
argument_list|(
literal|"<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
operator|+
literal|"<person user=\"james\">\n"
operator|+
literal|"<firstName>James</firstName>\n"
operator|+
literal|"<lastName>Strachan</lastName>\n"
operator|+
literal|"<city>London</city>\n"
operator|+
literal|"</person>"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|source
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:bytes"
argument_list|,
name|source
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testLondonWithStringSourceAsDefault ()
specifier|public
name|void
name|testLondonWithStringSourceAsDefault
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:london"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mock
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|body
argument_list|(
name|String
operator|.
name|class
argument_list|)
operator|.
name|contains
argument_list|(
literal|"James"
argument_list|)
expr_stmt|;
name|Source
name|source
init|=
operator|new
name|StringSource
argument_list|(
literal|"<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
operator|+
literal|"<person user=\"james\">\n"
operator|+
literal|"<firstName>James</firstName>\n"
operator|+
literal|"<lastName>Strachan</lastName>\n"
operator|+
literal|"<city>London</city>\n"
operator|+
literal|"</person>"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|source
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:default"
argument_list|,
name|source
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createCamelContext ()
specifier|protected
name|CamelContext
name|createCamelContext
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContext
name|camelContext
init|=
name|super
operator|.
name|createCamelContext
argument_list|()
decl_stmt|;
name|ConnectionFactory
name|connectionFactory
init|=
name|CamelJmsTestHelper
operator|.
name|createConnectionFactory
argument_list|()
decl_stmt|;
name|camelContext
operator|.
name|addComponent
argument_list|(
literal|"activemq"
argument_list|,
name|jmsComponentAutoAcknowledge
argument_list|(
name|connectionFactory
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|camelContext
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
comment|// enable stream caching
name|context
operator|.
name|setStreamCaching
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|errorHandler
argument_list|(
name|deadLetterChannel
argument_list|(
literal|"mock:error"
argument_list|)
operator|.
name|redeliveryDelay
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
comment|// no need to convert to String as JMS producer can handle XML streams now
name|from
argument_list|(
literal|"direct:object"
argument_list|)
operator|.
name|to
argument_list|(
literal|"activemq:queue:object?jmsMessageType=Object"
argument_list|)
expr_stmt|;
comment|// no need to convert to String as JMS producer can handle XML streams now
name|from
argument_list|(
literal|"direct:bytes"
argument_list|)
operator|.
name|to
argument_list|(
literal|"activemq:queue:bytes?jmsMessageType=Bytes"
argument_list|)
expr_stmt|;
comment|// no need to convert to String as JMS producer can handle XML streams now
name|from
argument_list|(
literal|"direct:default"
argument_list|)
operator|.
name|to
argument_list|(
literal|"activemq:queue:default"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"activemq:queue:object"
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|Object
name|body
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
comment|// should preserve the object as Source
name|assertIsInstanceOf
argument_list|(
name|Source
operator|.
name|class
argument_list|,
name|body
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
operator|.
name|to
argument_list|(
literal|"seda:choice"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"activemq:queue:bytes"
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|Object
name|body
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
comment|// should be a byte array by default
name|assertIsInstanceOf
argument_list|(
name|byte
index|[]
operator|.
expr|class
argument_list|,
name|body
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
operator|.
name|to
argument_list|(
literal|"seda:choice"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"activemq:queue:default"
argument_list|)
operator|.
name|to
argument_list|(
literal|"seda:choice"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"seda:choice"
argument_list|)
operator|.
name|choice
argument_list|()
operator|.
name|when
argument_list|()
operator|.
name|xpath
argument_list|(
literal|"/person/city = 'London'"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:london"
argument_list|)
operator|.
name|when
argument_list|()
operator|.
name|xpath
argument_list|(
literal|"/person/city = 'Tampa'"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:tampa"
argument_list|)
operator|.
name|otherwise
argument_list|()
operator|.
name|to
argument_list|(
literal|"mock:unknown"
argument_list|)
operator|.
name|end
argument_list|()
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

