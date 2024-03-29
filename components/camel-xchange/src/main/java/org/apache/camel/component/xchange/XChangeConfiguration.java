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
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
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
name|Metadata
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
name|spi
operator|.
name|UriParams
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
name|UriPath
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
name|ObjectHelper
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

begin_class
annotation|@
name|UriParams
DECL|class|XChangeConfiguration
specifier|public
class|class
name|XChangeConfiguration
block|{
comment|// Available service
DECL|enum|XChangeService
DECL|enumConstant|marketdata
DECL|enumConstant|metadata
DECL|enumConstant|account
specifier|public
enum|enum
name|XChangeService
block|{
name|marketdata
block|,
name|metadata
block|,
name|account
block|}
comment|// Available methods
DECL|enum|XChangeMethod
specifier|public
enum|enum
name|XChangeMethod
block|{
comment|// Account service methods
DECL|enumConstant|balances
DECL|enumConstant|fundingHistory
DECL|enumConstant|wallets
name|balances
block|,
name|fundingHistory
block|,
name|wallets
block|,
comment|// Metadata service methods
DECL|enumConstant|currencies
DECL|enumConstant|currencyMetaData
DECL|enumConstant|currencyPairs
DECL|enumConstant|currencyPairMetaData
name|currencies
block|,
name|currencyMetaData
block|,
name|currencyPairs
block|,
name|currencyPairMetaData
block|,
comment|// Marketdata service methods
DECL|enumConstant|ticker
name|ticker
block|}
DECL|field|HEADER_CURRENCY
specifier|public
specifier|static
specifier|final
name|String
name|HEADER_CURRENCY
init|=
literal|"Currency"
decl_stmt|;
DECL|field|HEADER_CURRENCY_PAIR
specifier|public
specifier|static
specifier|final
name|String
name|HEADER_CURRENCY_PAIR
init|=
literal|"CurrencyPair"
decl_stmt|;
DECL|field|xchangeMapping
specifier|static
name|Map
argument_list|<
name|String
argument_list|,
name|Class
argument_list|<
name|?
extends|extends
name|Exchange
argument_list|>
argument_list|>
name|xchangeMapping
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
annotation|@
name|UriPath
argument_list|(
name|description
operator|=
literal|"The exchange to connect to"
argument_list|)
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|true
argument_list|)
DECL|field|name
specifier|private
name|String
name|name
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|description
operator|=
literal|"The service to call"
argument_list|)
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|true
argument_list|)
DECL|field|service
specifier|private
name|XChangeService
name|service
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|description
operator|=
literal|"The method to execute"
argument_list|)
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|true
argument_list|)
DECL|field|method
specifier|private
name|XChangeMethod
name|method
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|description
operator|=
literal|"The currency"
argument_list|)
DECL|field|currency
specifier|private
name|Currency
name|currency
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|description
operator|=
literal|"The currency pair"
argument_list|)
DECL|field|currencyPair
specifier|private
name|String
name|currencyPair
decl_stmt|;
DECL|method|XChangeConfiguration (XChangeComponent component)
specifier|public
name|XChangeConfiguration
parameter_list|(
name|XChangeComponent
name|component
parameter_list|)
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|component
argument_list|,
literal|"component"
argument_list|)
expr_stmt|;
block|}
DECL|method|getName ()
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
DECL|method|setName (String name)
specifier|public
name|void
name|setName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
DECL|method|getService ()
specifier|public
name|XChangeService
name|getService
parameter_list|()
block|{
return|return
name|service
return|;
block|}
DECL|method|setService (XChangeService service)
specifier|public
name|void
name|setService
parameter_list|(
name|XChangeService
name|service
parameter_list|)
block|{
name|this
operator|.
name|service
operator|=
name|service
expr_stmt|;
block|}
DECL|method|getMethod ()
specifier|public
name|XChangeMethod
name|getMethod
parameter_list|()
block|{
return|return
name|method
return|;
block|}
DECL|method|setMethod (XChangeMethod method)
specifier|public
name|void
name|setMethod
parameter_list|(
name|XChangeMethod
name|method
parameter_list|)
block|{
name|this
operator|.
name|method
operator|=
name|method
expr_stmt|;
block|}
DECL|method|getCurrency ()
specifier|public
name|Currency
name|getCurrency
parameter_list|()
block|{
return|return
name|currency
return|;
block|}
DECL|method|setCurrency (Currency currency)
specifier|public
name|void
name|setCurrency
parameter_list|(
name|Currency
name|currency
parameter_list|)
block|{
name|this
operator|.
name|currency
operator|=
name|currency
expr_stmt|;
block|}
DECL|method|setCurrency (String curr)
specifier|public
name|void
name|setCurrency
parameter_list|(
name|String
name|curr
parameter_list|)
block|{
name|this
operator|.
name|currency
operator|=
name|Currency
operator|.
name|getInstanceNoCreate
argument_list|(
name|curr
argument_list|)
expr_stmt|;
block|}
DECL|method|getAsCurrencyPair ()
specifier|public
name|CurrencyPair
name|getAsCurrencyPair
parameter_list|()
block|{
if|if
condition|(
name|currencyPair
operator|!=
literal|null
condition|)
block|{
return|return
operator|new
name|CurrencyPair
argument_list|(
name|currencyPair
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
DECL|method|getCurrencyPair ()
specifier|public
name|String
name|getCurrencyPair
parameter_list|()
block|{
return|return
name|currencyPair
return|;
block|}
DECL|method|setCurrencyPair (String currencyPair)
specifier|public
name|void
name|setCurrencyPair
parameter_list|(
name|String
name|currencyPair
parameter_list|)
block|{
name|this
operator|.
name|currencyPair
operator|=
name|currencyPair
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|getXChangeClass ()
specifier|public
name|Class
argument_list|<
name|?
extends|extends
name|Exchange
argument_list|>
name|getXChangeClass
parameter_list|()
block|{
name|Class
argument_list|<
name|?
extends|extends
name|Exchange
argument_list|>
name|xchangeClass
init|=
name|xchangeMapping
operator|.
name|get
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|xchangeClass
operator|==
literal|null
condition|)
block|{
name|String
name|firstUpper
init|=
name|name
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
literal|1
argument_list|)
operator|.
name|toUpperCase
argument_list|()
operator|+
name|name
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|String
name|className
init|=
literal|"org.knowm.xchange."
operator|+
name|name
operator|+
literal|"."
operator|+
name|firstUpper
operator|+
literal|"Exchange"
decl_stmt|;
name|ClassLoader
name|classLoader
init|=
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
decl_stmt|;
try|try
block|{
name|xchangeClass
operator|=
operator|(
name|Class
argument_list|<
name|?
extends|extends
name|Exchange
argument_list|>
operator|)
name|classLoader
operator|.
name|loadClass
argument_list|(
name|className
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ClassNotFoundException
name|e
parameter_list|)
block|{
comment|// ignore
block|}
block|}
return|return
name|xchangeClass
return|;
block|}
DECL|method|getSupportedXChangeNames ()
specifier|public
name|Set
argument_list|<
name|String
argument_list|>
name|getSupportedXChangeNames
parameter_list|()
block|{
return|return
name|xchangeMapping
operator|.
name|keySet
argument_list|()
return|;
block|}
block|}
end_class

end_unit

