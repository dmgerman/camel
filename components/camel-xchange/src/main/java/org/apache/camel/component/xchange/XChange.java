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
name|List
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
name|Exchange
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
name|ExchangeSpecification
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
name|exceptions
operator|.
name|ExchangeException
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
name|TradeService
import|;
end_import

begin_import
import|import
name|si
operator|.
name|mazi
operator|.
name|rescu
operator|.
name|SynchronizedValueFactory
import|;
end_import

begin_comment
comment|// Wraps the exchange to avoid anem collision with the camel exchange
end_comment

begin_class
DECL|class|XChange
specifier|public
class|class
name|XChange
implements|implements
name|Exchange
block|{
DECL|field|delegate
specifier|private
specifier|final
name|Exchange
name|delegate
decl_stmt|;
DECL|method|XChange (Exchange delegate)
specifier|public
name|XChange
parameter_list|(
name|Exchange
name|delegate
parameter_list|)
block|{
name|this
operator|.
name|delegate
operator|=
name|delegate
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getExchangeSpecification ()
specifier|public
name|ExchangeSpecification
name|getExchangeSpecification
parameter_list|()
block|{
return|return
name|delegate
operator|.
name|getExchangeSpecification
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getExchangeMetaData ()
specifier|public
name|ExchangeMetaData
name|getExchangeMetaData
parameter_list|()
block|{
return|return
name|delegate
operator|.
name|getExchangeMetaData
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getExchangeSymbols ()
specifier|public
name|List
argument_list|<
name|CurrencyPair
argument_list|>
name|getExchangeSymbols
parameter_list|()
block|{
return|return
name|delegate
operator|.
name|getExchangeSymbols
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getNonceFactory ()
specifier|public
name|SynchronizedValueFactory
argument_list|<
name|Long
argument_list|>
name|getNonceFactory
parameter_list|()
block|{
return|return
name|delegate
operator|.
name|getNonceFactory
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getDefaultExchangeSpecification ()
specifier|public
name|ExchangeSpecification
name|getDefaultExchangeSpecification
parameter_list|()
block|{
return|return
name|delegate
operator|.
name|getDefaultExchangeSpecification
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|applySpecification (ExchangeSpecification exchangeSpecification)
specifier|public
name|void
name|applySpecification
parameter_list|(
name|ExchangeSpecification
name|exchangeSpecification
parameter_list|)
block|{
name|delegate
operator|.
name|applySpecification
argument_list|(
name|exchangeSpecification
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getMarketDataService ()
specifier|public
name|MarketDataService
name|getMarketDataService
parameter_list|()
block|{
return|return
name|delegate
operator|.
name|getMarketDataService
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getTradeService ()
specifier|public
name|TradeService
name|getTradeService
parameter_list|()
block|{
return|return
name|delegate
operator|.
name|getTradeService
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getAccountService ()
specifier|public
name|AccountService
name|getAccountService
parameter_list|()
block|{
return|return
name|delegate
operator|.
name|getAccountService
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|remoteInit ()
specifier|public
name|void
name|remoteInit
parameter_list|()
throws|throws
name|IOException
throws|,
name|ExchangeException
block|{
name|delegate
operator|.
name|remoteInit
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

