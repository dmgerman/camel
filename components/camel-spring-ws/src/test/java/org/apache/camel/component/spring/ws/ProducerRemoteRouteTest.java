begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.spring.ws
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|spring
operator|.
name|ws
package|;
end_package

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
name|Ignore
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
name|assertNotNull
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
name|assertTrue
import|;
end_import

begin_class
annotation|@
name|Ignore
argument_list|(
literal|"Run manually, makes connection to external webservice"
argument_list|)
annotation|@
name|ContextConfiguration
DECL|class|ProducerRemoteRouteTest
specifier|public
class|class
name|ProducerRemoteRouteTest
extends|extends
name|AbstractJUnit4SpringContextTests
block|{
DECL|field|stockQuoteWebserviceUri
specifier|private
specifier|final
name|String
name|stockQuoteWebserviceUri
init|=
literal|"http://www.webservicex.net/stockquote.asmx"
decl_stmt|;
DECL|field|xmlRequestForGoogleStockQuote
specifier|private
specifier|final
name|String
name|xmlRequestForGoogleStockQuote
init|=
literal|"<GetQuote xmlns=\"http://www.webserviceX.NET/\"><symbol>GOOG</symbol></GetQuote>"
decl_stmt|;
annotation|@
name|Produce
DECL|field|template
specifier|private
name|ProducerTemplate
name|template
decl_stmt|;
annotation|@
name|Test
argument_list|(
name|timeout
operator|=
literal|5000
argument_list|)
DECL|method|consumeStockQuoteWebserviceWithDefaultTemplate ()
specifier|public
name|void
name|consumeStockQuoteWebserviceWithDefaultTemplate
parameter_list|()
throws|throws
name|Exception
block|{
name|Object
name|result
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:stockQuoteWebserviceWithDefaultTemplate"
argument_list|,
name|xmlRequestForGoogleStockQuote
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|result
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|result
operator|instanceof
name|Source
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|timeout
operator|=
literal|5000
argument_list|)
DECL|method|consumeStockQuoteWebservice ()
specifier|public
name|void
name|consumeStockQuoteWebservice
parameter_list|()
throws|throws
name|Exception
block|{
name|Object
name|result
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:stockQuoteWebservice"
argument_list|,
name|xmlRequestForGoogleStockQuote
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|result
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|result
operator|instanceof
name|Source
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|timeout
operator|=
literal|5000
argument_list|)
DECL|method|consumeStockQuoteWebserviceWithCamelStringSourceInput ()
specifier|public
name|void
name|consumeStockQuoteWebserviceWithCamelStringSourceInput
parameter_list|()
throws|throws
name|Exception
block|{
name|Object
name|result
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:stockQuoteWebservice"
argument_list|,
operator|new
name|StringSource
argument_list|(
name|xmlRequestForGoogleStockQuote
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|result
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|result
operator|instanceof
name|Source
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|timeout
operator|=
literal|5000
argument_list|)
DECL|method|consumeStockQuoteWebserviceWithNonDefaultMessageFactory ()
specifier|public
name|void
name|consumeStockQuoteWebserviceWithNonDefaultMessageFactory
parameter_list|()
throws|throws
name|Exception
block|{
name|Object
name|result
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:stockQuoteWebserviceWithNonDefaultMessageFactory"
argument_list|,
name|xmlRequestForGoogleStockQuote
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|result
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|result
operator|instanceof
name|Source
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|timeout
operator|=
literal|5000
argument_list|)
DECL|method|consumeStockQuoteWebserviceAndConvertResult ()
specifier|public
name|void
name|consumeStockQuoteWebserviceAndConvertResult
parameter_list|()
throws|throws
name|Exception
block|{
name|Object
name|result
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:stockQuoteWebserviceAsString"
argument_list|,
name|xmlRequestForGoogleStockQuote
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|result
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|result
operator|instanceof
name|String
argument_list|)
expr_stmt|;
name|String
name|resultMessage
init|=
operator|(
name|String
operator|)
name|result
decl_stmt|;
name|assertTrue
argument_list|(
name|resultMessage
operator|.
name|contains
argument_list|(
literal|"Google Inc."
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|timeout
operator|=
literal|5000
argument_list|)
DECL|method|consumeStockQuoteWebserviceAndProvideEndpointUriByHeader ()
specifier|public
name|void
name|consumeStockQuoteWebserviceAndProvideEndpointUriByHeader
parameter_list|()
throws|throws
name|Exception
block|{
name|Object
name|result
init|=
name|template
operator|.
name|requestBodyAndHeader
argument_list|(
literal|"direct:stockQuoteWebserviceWithoutDefaultUri"
argument_list|,
name|xmlRequestForGoogleStockQuote
argument_list|,
name|SpringWebserviceConstants
operator|.
name|SPRING_WS_ENDPOINT_URI
argument_list|,
name|stockQuoteWebserviceUri
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|result
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|result
operator|instanceof
name|String
argument_list|)
expr_stmt|;
name|String
name|resultMessage
init|=
operator|(
name|String
operator|)
name|result
decl_stmt|;
name|assertTrue
argument_list|(
name|resultMessage
operator|.
name|contains
argument_list|(
literal|"Google Inc."
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

