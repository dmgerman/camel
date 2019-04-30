begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.dataformat.bindy.csv
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|dataformat
operator|.
name|bindy
operator|.
name|csv
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
name|Produce
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
name|ProducerTemplate
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
name|dataformat
operator|.
name|bindy
operator|.
name|format
operator|.
name|FormatException
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
name|TestSupport
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
name|springframework
operator|.
name|test
operator|.
name|annotation
operator|.
name|DirtiesContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|test
operator|.
name|context
operator|.
name|ContextConfiguration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|test
operator|.
name|context
operator|.
name|junit4
operator|.
name|AbstractJUnit4SpringContextTests
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
name|assertEquals
import|;
end_import

begin_class
annotation|@
name|ContextConfiguration
DECL|class|BindySimpleCsvUnmarshallPositionModifiedTest
specifier|public
class|class
name|BindySimpleCsvUnmarshallPositionModifiedTest
extends|extends
name|AbstractJUnit4SpringContextTests
block|{
DECL|field|URI_MOCK_RESULT
specifier|private
specifier|static
specifier|final
name|String
name|URI_MOCK_RESULT
init|=
literal|"mock:result"
decl_stmt|;
DECL|field|URI_MOCK_ERROR
specifier|private
specifier|static
specifier|final
name|String
name|URI_MOCK_ERROR
init|=
literal|"mock:error"
decl_stmt|;
DECL|field|URI_DIRECT_START
specifier|private
specifier|static
specifier|final
name|String
name|URI_DIRECT_START
init|=
literal|"direct:start"
decl_stmt|;
annotation|@
name|Produce
argument_list|(
name|URI_DIRECT_START
argument_list|)
DECL|field|template
specifier|protected
name|ProducerTemplate
name|template
decl_stmt|;
DECL|field|record
specifier|private
name|String
name|record
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
name|URI_MOCK_RESULT
argument_list|)
DECL|field|result
specifier|private
name|MockEndpoint
name|result
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
name|URI_MOCK_ERROR
argument_list|)
DECL|field|error
specifier|private
name|MockEndpoint
name|error
decl_stmt|;
annotation|@
name|Test
annotation|@
name|DirtiesContext
DECL|method|testUnMarshallMessage ()
specifier|public
name|void
name|testUnMarshallMessage
parameter_list|()
throws|throws
name|Exception
block|{
name|record
operator|=
literal|"1,25,Albert,Cartier,ISIN,BE12345678,SELL,Share,1500,EUR,08-01-2009\r\n"
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
name|record
argument_list|)
expr_stmt|;
name|result
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|result
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
annotation|@
name|DirtiesContext
DECL|method|testUnmarshallErrorMessage ()
specifier|public
name|void
name|testUnmarshallErrorMessage
parameter_list|()
throws|throws
name|Exception
block|{
name|record
operator|=
literal|"1,25,Albert,Cartier,ISIN,BE12345678,SELL,Share,1500,EUR,08-01-2009-01\r\n"
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
name|record
argument_list|)
expr_stmt|;
comment|// We don't expect to have a message as an error will be raised
name|result
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
comment|// Message has been delivered to the mock error
name|error
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|result
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|error
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
comment|// and check that we have the caused exception stored
name|Exception
name|cause
init|=
name|error
operator|.
name|getReceivedExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getProperty
argument_list|(
name|Exchange
operator|.
name|EXCEPTION_CAUGHT
argument_list|,
name|Exception
operator|.
name|class
argument_list|)
decl_stmt|;
name|TestSupport
operator|.
name|assertIsInstanceOf
argument_list|(
name|FormatException
operator|.
name|class
argument_list|,
name|cause
operator|.
name|getCause
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Date provided does not fit the pattern defined, position: 11, line: 1"
argument_list|,
name|cause
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|class|ContextConfig
specifier|public
specifier|static
class|class
name|ContextConfig
extends|extends
name|RouteBuilder
block|{
DECL|field|orderBindyDataFormat
name|BindyCsvDataFormat
name|orderBindyDataFormat
init|=
operator|new
name|BindyCsvDataFormat
argument_list|(
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|dataformat
operator|.
name|bindy
operator|.
name|model
operator|.
name|simple
operator|.
name|oneclassdifferentposition
operator|.
name|Order
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|configure ()
specifier|public
name|void
name|configure
parameter_list|()
block|{
comment|// default should errors go to mock:error
name|errorHandler
argument_list|(
name|deadLetterChannel
argument_list|(
name|URI_MOCK_ERROR
argument_list|)
operator|.
name|redeliveryDelay
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|onException
argument_list|(
name|Exception
operator|.
name|class
argument_list|)
operator|.
name|maximumRedeliveries
argument_list|(
literal|0
argument_list|)
operator|.
name|handled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|from
argument_list|(
name|URI_DIRECT_START
argument_list|)
operator|.
name|unmarshal
argument_list|(
name|orderBindyDataFormat
argument_list|)
operator|.
name|to
argument_list|(
name|URI_MOCK_RESULT
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

