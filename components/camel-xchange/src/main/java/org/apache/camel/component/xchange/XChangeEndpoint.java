begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|impl
operator|.
name|DefaultEndpoint
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
name|utils
operator|.
name|Assert
import|;
end_import

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
literal|"blockchain"
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
name|XChangeConfiguration
name|configuration
decl_stmt|;
DECL|field|exchange
specifier|private
specifier|final
name|XChange
name|exchange
decl_stmt|;
DECL|method|XChangeEndpoint (String uri, XChangeComponent component, XChangeConfiguration properties, XChange exchange)
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
name|properties
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
name|properties
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
annotation|@
name|Override
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
literal|true
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
DECL|method|getXChange ()
specifier|public
name|XChange
name|getXChange
parameter_list|()
block|{
return|return
name|exchange
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
block|}
end_class

end_unit

