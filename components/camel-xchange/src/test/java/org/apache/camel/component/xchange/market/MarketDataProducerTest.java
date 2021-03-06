begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.xchange.market
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|xchange
operator|.
name|market
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
name|Assert
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
name|knowm
operator|.
name|xchange
operator|.
name|currency
operator|.
name|CurrencyPair
import|;
end_import

begin_import
import|import
name|org
operator|.
name|knowm
operator|.
name|xchange
operator|.
name|dto
operator|.
name|marketdata
operator|.
name|Ticker
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
name|xchange
operator|.
name|XChangeConfiguration
operator|.
name|HEADER_CURRENCY_PAIR
import|;
end_import

begin_class
DECL|class|MarketDataProducerTest
specifier|public
class|class
name|MarketDataProducerTest
extends|extends
name|CamelTestSupport
block|{
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
name|from
argument_list|(
literal|"direct:ticker"
argument_list|)
operator|.
name|to
argument_list|(
literal|"xchange:binance?service=marketdata&method=ticker"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:tickerBTCUSDT"
argument_list|)
operator|.
name|to
argument_list|(
literal|"xchange:binance?service=marketdata&method=ticker&currencyPair=BTC/USDT"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
annotation|@
name|Test
DECL|method|testTicker ()
specifier|public
name|void
name|testTicker
parameter_list|()
throws|throws
name|Exception
block|{
name|Ticker
name|ticker
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:ticker"
argument_list|,
name|CurrencyPair
operator|.
name|EOS_ETH
argument_list|,
name|Ticker
operator|.
name|class
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertNotNull
argument_list|(
literal|"Ticker not null"
argument_list|,
name|ticker
argument_list|)
expr_stmt|;
name|ticker
operator|=
name|template
operator|.
name|requestBodyAndHeader
argument_list|(
literal|"direct:ticker"
argument_list|,
literal|null
argument_list|,
name|HEADER_CURRENCY_PAIR
argument_list|,
name|CurrencyPair
operator|.
name|EOS_ETH
argument_list|,
name|Ticker
operator|.
name|class
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertNotNull
argument_list|(
literal|"Ticker not null"
argument_list|,
name|ticker
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testTickerBTCUSDT ()
specifier|public
name|void
name|testTickerBTCUSDT
parameter_list|()
throws|throws
name|Exception
block|{
name|Ticker
name|ticker
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:tickerBTCUSDT"
argument_list|,
literal|null
argument_list|,
name|Ticker
operator|.
name|class
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertNotNull
argument_list|(
literal|"Ticker not null"
argument_list|,
name|ticker
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

