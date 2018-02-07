begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.xchange.account
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
name|account
package|;
end_package

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
name|xchange
operator|.
name|XChangeComponent
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
name|Assume
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
name|dto
operator|.
name|account
operator|.
name|Balance
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
name|account
operator|.
name|FundingRecord
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
name|account
operator|.
name|Wallet
import|;
end_import

begin_class
DECL|class|AccountProducerTest
specifier|public
class|class
name|AccountProducerTest
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
literal|"direct:balances"
argument_list|)
operator|.
name|to
argument_list|(
literal|"xchange:binance?service=account&method=balances"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:wallets"
argument_list|)
operator|.
name|to
argument_list|(
literal|"xchange:binance?service=account&method=wallets"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:fundingHistory"
argument_list|)
operator|.
name|to
argument_list|(
literal|"xchange:binance?service=account&method=fundingHistory"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
annotation|@
name|Test
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|testBalances ()
specifier|public
name|void
name|testBalances
parameter_list|()
throws|throws
name|Exception
block|{
name|Assume
operator|.
name|assumeTrue
argument_list|(
name|hasAPICredentials
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Balance
argument_list|>
name|balances
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:balances"
argument_list|,
literal|null
argument_list|,
name|List
operator|.
name|class
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertNotNull
argument_list|(
literal|"Balances not null"
argument_list|,
name|balances
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|testWallets ()
specifier|public
name|void
name|testWallets
parameter_list|()
throws|throws
name|Exception
block|{
name|Assume
operator|.
name|assumeTrue
argument_list|(
name|hasAPICredentials
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Wallet
argument_list|>
name|wallets
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:wallets"
argument_list|,
literal|null
argument_list|,
name|List
operator|.
name|class
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertNotNull
argument_list|(
literal|"Wallets not null"
argument_list|,
name|wallets
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|testFundingHistory ()
specifier|public
name|void
name|testFundingHistory
parameter_list|()
throws|throws
name|Exception
block|{
name|Assume
operator|.
name|assumeTrue
argument_list|(
name|hasAPICredentials
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|FundingRecord
argument_list|>
name|records
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:fundingHistory"
argument_list|,
literal|null
argument_list|,
name|List
operator|.
name|class
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertNotNull
argument_list|(
literal|"Funding records not null"
argument_list|,
name|records
argument_list|)
expr_stmt|;
block|}
DECL|method|hasAPICredentials ()
specifier|private
name|boolean
name|hasAPICredentials
parameter_list|()
block|{
name|XChangeComponent
name|component
init|=
name|context
argument_list|()
operator|.
name|getComponent
argument_list|(
literal|"xchange"
argument_list|,
name|XChangeComponent
operator|.
name|class
argument_list|)
decl_stmt|;
return|return
name|component
operator|.
name|getXChange
argument_list|()
operator|.
name|getExchangeSpecification
argument_list|()
operator|.
name|getApiKey
argument_list|()
operator|!=
literal|null
return|;
block|}
block|}
end_class

end_unit

