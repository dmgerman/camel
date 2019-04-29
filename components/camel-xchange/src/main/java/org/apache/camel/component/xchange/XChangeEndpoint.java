begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.xchange
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
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Comparator
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
name|stream
operator|.
name|Collectors
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
name|Consumer
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
name|Producer
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
name|XChangeConfiguration
operator|.
name|XChangeService
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
name|spi
operator|.
name|UriEndpoint
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
name|spi
operator|.
name|UriParam
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
name|DefaultEndpoint
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
name|Currency
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
name|account
operator|.
name|AccountInfo
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
import|import
name|org
operator|.
name|knowm
operator|.
name|xchange
operator|.
name|dto
operator|.
name|meta
operator|.
name|CurrencyMetaData
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
name|meta
operator|.
name|CurrencyPairMetaData
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
name|meta
operator|.
name|ExchangeMetaData
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
name|service
operator|.
name|account
operator|.
name|AccountService
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
name|service
operator|.
name|marketdata
operator|.
name|MarketDataService
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
name|service
operator|.
name|trade
operator|.
name|params
operator|.
name|TradeHistoryParams
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
name|utils
operator|.
name|Assert
import|;
end_import

begin_comment
comment|/**  * The camel-xchange component provide access to many bitcoin and altcoin exchanges for trading and accessing market data.  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|firstVersion
operator|=
literal|"2.21.0"
argument_list|,
name|scheme
operator|=
literal|"xchange"
argument_list|,
name|title
operator|=
literal|"XChange"
argument_list|,
name|syntax
operator|=
literal|"xchange:name"
argument_list|,
name|producerOnly
operator|=
literal|true
argument_list|,
name|label
operator|=
literal|"bitcoin,blockchain"
argument_list|)
DECL|class|XChangeEndpoint
specifier|public
class|class
name|XChangeEndpoint
extends|extends
name|DefaultEndpoint
block|{
annotation|@
name|UriParam
DECL|field|configuration
specifier|private
specifier|final
name|XChangeConfiguration
name|configuration
decl_stmt|;
DECL|field|exchange
specifier|private
specifier|final
name|XChange
name|exchange
decl_stmt|;
DECL|method|XChangeEndpoint (String uri, XChangeComponent component, XChangeConfiguration configuration, XChange exchange)
specifier|public
name|XChangeEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|XChangeComponent
name|component
parameter_list|,
name|XChangeConfiguration
name|configuration
parameter_list|,
name|XChange
name|exchange
parameter_list|)
block|{
name|super
argument_list|(
name|uri
argument_list|,
name|component
argument_list|)
expr_stmt|;
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
name|this
operator|.
name|exchange
operator|=
name|exchange
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getComponent ()
specifier|public
name|XChangeComponent
name|getComponent
parameter_list|()
block|{
return|return
operator|(
name|XChangeComponent
operator|)
name|super
operator|.
name|getComponent
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|createConsumer (Processor processor)
specifier|public
name|Consumer
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
name|Producer
name|producer
init|=
literal|null
decl_stmt|;
name|XChangeService
name|service
init|=
name|getConfiguration
argument_list|()
operator|.
name|getService
argument_list|()
decl_stmt|;
if|if
condition|(
name|XChangeService
operator|.
name|account
operator|==
name|service
condition|)
block|{
name|producer
operator|=
operator|new
name|XChangeAccountProducer
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|XChangeService
operator|.
name|marketdata
operator|==
name|service
condition|)
block|{
name|producer
operator|=
operator|new
name|XChangeMarketDataProducer
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|XChangeService
operator|.
name|metadata
operator|==
name|service
condition|)
block|{
name|producer
operator|=
operator|new
name|XChangeMetaDataProducer
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
name|Assert
operator|.
name|notNull
argument_list|(
name|producer
argument_list|,
literal|"Unsupported service: "
operator|+
name|service
argument_list|)
expr_stmt|;
return|return
name|producer
return|;
block|}
DECL|method|getConfiguration ()
specifier|public
name|XChangeConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
DECL|method|getCurrencies ()
specifier|public
name|List
argument_list|<
name|Currency
argument_list|>
name|getCurrencies
parameter_list|()
block|{
name|ExchangeMetaData
name|metaData
init|=
name|exchange
operator|.
name|getExchangeMetaData
argument_list|()
decl_stmt|;
return|return
name|metaData
operator|.
name|getCurrencies
argument_list|()
operator|.
name|keySet
argument_list|()
operator|.
name|stream
argument_list|()
operator|.
name|sorted
argument_list|()
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toList
argument_list|()
argument_list|)
return|;
block|}
DECL|method|getCurrencyMetaData (Currency curr)
specifier|public
name|CurrencyMetaData
name|getCurrencyMetaData
parameter_list|(
name|Currency
name|curr
parameter_list|)
block|{
name|Assert
operator|.
name|notNull
argument_list|(
name|curr
argument_list|,
literal|"Null currency"
argument_list|)
expr_stmt|;
name|ExchangeMetaData
name|metaData
init|=
name|exchange
operator|.
name|getExchangeMetaData
argument_list|()
decl_stmt|;
return|return
name|metaData
operator|.
name|getCurrencies
argument_list|()
operator|.
name|get
argument_list|(
name|curr
argument_list|)
return|;
block|}
DECL|method|getCurrencyPairs ()
specifier|public
name|List
argument_list|<
name|CurrencyPair
argument_list|>
name|getCurrencyPairs
parameter_list|()
block|{
name|ExchangeMetaData
name|metaData
init|=
name|exchange
operator|.
name|getExchangeMetaData
argument_list|()
decl_stmt|;
return|return
name|metaData
operator|.
name|getCurrencyPairs
argument_list|()
operator|.
name|keySet
argument_list|()
operator|.
name|stream
argument_list|()
operator|.
name|sorted
argument_list|()
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toList
argument_list|()
argument_list|)
return|;
block|}
DECL|method|getCurrencyPairMetaData (CurrencyPair pair)
specifier|public
name|CurrencyPairMetaData
name|getCurrencyPairMetaData
parameter_list|(
name|CurrencyPair
name|pair
parameter_list|)
block|{
name|Assert
operator|.
name|notNull
argument_list|(
name|pair
argument_list|,
literal|"Null currency"
argument_list|)
expr_stmt|;
name|ExchangeMetaData
name|metaData
init|=
name|exchange
operator|.
name|getExchangeMetaData
argument_list|()
decl_stmt|;
return|return
name|metaData
operator|.
name|getCurrencyPairs
argument_list|()
operator|.
name|get
argument_list|(
name|pair
argument_list|)
return|;
block|}
DECL|method|getBalances ()
specifier|public
name|List
argument_list|<
name|Balance
argument_list|>
name|getBalances
parameter_list|()
throws|throws
name|IOException
block|{
name|List
argument_list|<
name|Balance
argument_list|>
name|balances
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|getWallets
argument_list|()
operator|.
name|stream
argument_list|()
operator|.
name|forEach
argument_list|(
name|w
lambda|->
block|{
for|for
control|(
name|Balance
name|aux
range|:
name|w
operator|.
name|getBalances
argument_list|()
operator|.
name|values
argument_list|()
control|)
block|{
name|Currency
name|curr
init|=
name|aux
operator|.
name|getCurrency
argument_list|()
decl_stmt|;
name|CurrencyMetaData
name|metaData
init|=
name|getCurrencyMetaData
argument_list|(
name|curr
argument_list|)
decl_stmt|;
if|if
condition|(
name|metaData
operator|!=
literal|null
condition|)
block|{
name|int
name|scale
init|=
name|metaData
operator|.
name|getScale
argument_list|()
decl_stmt|;
name|double
name|total
init|=
name|aux
operator|.
name|getTotal
argument_list|()
operator|.
name|doubleValue
argument_list|()
decl_stmt|;
name|double
name|scaledTotal
init|=
name|total
operator|*
name|Math
operator|.
name|pow
argument_list|(
literal|10
argument_list|,
name|scale
operator|/
literal|2
argument_list|)
decl_stmt|;
if|if
condition|(
literal|1
operator|<=
name|scaledTotal
condition|)
block|{
name|balances
operator|.
name|add
argument_list|(
name|aux
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
argument_list|)
expr_stmt|;
return|return
name|balances
operator|.
name|stream
argument_list|()
operator|.
name|sorted
argument_list|(
operator|new
name|Comparator
argument_list|<
name|Balance
argument_list|>
argument_list|()
block|{
specifier|public
name|int
name|compare
parameter_list|(
name|Balance
name|o1
parameter_list|,
name|Balance
name|o2
parameter_list|)
block|{
return|return
name|o1
operator|.
name|getCurrency
argument_list|()
operator|.
name|compareTo
argument_list|(
name|o2
operator|.
name|getCurrency
argument_list|()
argument_list|)
return|;
block|}
block|}
argument_list|)
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toList
argument_list|()
argument_list|)
return|;
block|}
DECL|method|getFundingHistory ()
specifier|public
name|List
argument_list|<
name|FundingRecord
argument_list|>
name|getFundingHistory
parameter_list|()
throws|throws
name|IOException
block|{
name|AccountService
name|accountService
init|=
name|exchange
operator|.
name|getAccountService
argument_list|()
decl_stmt|;
name|TradeHistoryParams
name|fundingHistoryParams
init|=
name|accountService
operator|.
name|createFundingHistoryParams
argument_list|()
decl_stmt|;
return|return
name|accountService
operator|.
name|getFundingHistory
argument_list|(
name|fundingHistoryParams
argument_list|)
operator|.
name|stream
argument_list|()
operator|.
name|sorted
argument_list|(
operator|new
name|Comparator
argument_list|<
name|FundingRecord
argument_list|>
argument_list|()
block|{
specifier|public
name|int
name|compare
parameter_list|(
name|FundingRecord
name|o1
parameter_list|,
name|FundingRecord
name|o2
parameter_list|)
block|{
return|return
name|o1
operator|.
name|getDate
argument_list|()
operator|.
name|compareTo
argument_list|(
name|o2
operator|.
name|getDate
argument_list|()
argument_list|)
return|;
block|}
block|}
argument_list|)
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toList
argument_list|()
argument_list|)
return|;
block|}
DECL|method|getWallets ()
specifier|public
name|List
argument_list|<
name|Wallet
argument_list|>
name|getWallets
parameter_list|()
throws|throws
name|IOException
block|{
name|AccountService
name|accountService
init|=
name|exchange
operator|.
name|getAccountService
argument_list|()
decl_stmt|;
name|AccountInfo
name|accountInfo
init|=
name|accountService
operator|.
name|getAccountInfo
argument_list|()
decl_stmt|;
return|return
name|accountInfo
operator|.
name|getWallets
argument_list|()
operator|.
name|values
argument_list|()
operator|.
name|stream
argument_list|()
operator|.
name|sorted
argument_list|(
operator|new
name|Comparator
argument_list|<
name|Wallet
argument_list|>
argument_list|()
block|{
specifier|public
name|int
name|compare
parameter_list|(
name|Wallet
name|o1
parameter_list|,
name|Wallet
name|o2
parameter_list|)
block|{
return|return
name|o1
operator|.
name|getName
argument_list|()
operator|.
name|compareTo
argument_list|(
name|o2
operator|.
name|getName
argument_list|()
argument_list|)
return|;
block|}
block|}
argument_list|)
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toList
argument_list|()
argument_list|)
return|;
block|}
DECL|method|getTicker (CurrencyPair pair)
specifier|public
name|Ticker
name|getTicker
parameter_list|(
name|CurrencyPair
name|pair
parameter_list|)
throws|throws
name|IOException
block|{
name|Assert
operator|.
name|notNull
argument_list|(
name|pair
argument_list|,
literal|"Null currency pair"
argument_list|)
expr_stmt|;
name|MarketDataService
name|marketService
init|=
name|exchange
operator|.
name|getMarketDataService
argument_list|()
decl_stmt|;
return|return
name|marketService
operator|.
name|getTicker
argument_list|(
name|pair
argument_list|)
return|;
block|}
block|}
end_class

end_unit

