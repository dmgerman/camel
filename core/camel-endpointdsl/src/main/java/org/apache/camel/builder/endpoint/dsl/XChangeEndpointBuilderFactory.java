begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.builder.endpoint.dsl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
operator|.
name|endpoint
operator|.
name|dsl
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|Generated
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
name|EndpointConsumerBuilder
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
name|EndpointProducerBuilder
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
name|endpoint
operator|.
name|AbstractEndpointBuilder
import|;
end_import

begin_comment
comment|/**  * The camel-xchange component provide access to many bitcoin and altcoin  * exchanges for trading and accessing market data.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_interface
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.EndpointDslMojo"
argument_list|)
DECL|interface|XChangeEndpointBuilderFactory
specifier|public
interface|interface
name|XChangeEndpointBuilderFactory
block|{
comment|/**      * Builder for endpoint for the XChange component.      */
DECL|interface|XChangeEndpointBuilder
specifier|public
interface|interface
name|XChangeEndpointBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|advanced ()
specifier|default
name|AdvancedXChangeEndpointBuilder
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedXChangeEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * The exchange to connect to.          * The option is a<code>java.lang.String</code> type.          * @group producer          */
DECL|method|name (String name)
specifier|default
name|XChangeEndpointBuilder
name|name
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"name"
argument_list|,
name|name
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The currency.          * The option is a<code>org.knowm.xchange.currency.Currency</code>          * type.          * @group producer          */
DECL|method|currency (Object currency)
specifier|default
name|XChangeEndpointBuilder
name|currency
parameter_list|(
name|Object
name|currency
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"currency"
argument_list|,
name|currency
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The currency.          * The option will be converted to a          *<code>org.knowm.xchange.currency.Currency</code> type.          * @group producer          */
DECL|method|currency (String currency)
specifier|default
name|XChangeEndpointBuilder
name|currency
parameter_list|(
name|String
name|currency
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"currency"
argument_list|,
name|currency
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The currency pair.          * The option is a<code>org.knowm.xchange.currency.CurrencyPair</code>          * type.          * @group producer          */
DECL|method|currencyPair (Object currencyPair)
specifier|default
name|XChangeEndpointBuilder
name|currencyPair
parameter_list|(
name|Object
name|currencyPair
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"currencyPair"
argument_list|,
name|currencyPair
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The currency pair.          * The option will be converted to a          *<code>org.knowm.xchange.currency.CurrencyPair</code> type.          * @group producer          */
DECL|method|currencyPair (String currencyPair)
specifier|default
name|XChangeEndpointBuilder
name|currencyPair
parameter_list|(
name|String
name|currencyPair
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"currencyPair"
argument_list|,
name|currencyPair
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The method to execute.          * The option is a          *<code>org.apache.camel.component.xchange.XChangeConfiguration$XChangeMethod</code> type.          * @group producer          */
DECL|method|method (XChangeMethod method)
specifier|default
name|XChangeEndpointBuilder
name|method
parameter_list|(
name|XChangeMethod
name|method
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"method"
argument_list|,
name|method
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The method to execute.          * The option will be converted to a          *<code>org.apache.camel.component.xchange.XChangeConfiguration$XChangeMethod</code> type.          * @group producer          */
DECL|method|method (String method)
specifier|default
name|XChangeEndpointBuilder
name|method
parameter_list|(
name|String
name|method
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"method"
argument_list|,
name|method
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The service to call.          * The option is a          *<code>org.apache.camel.component.xchange.XChangeConfiguration$XChangeService</code> type.          * @group producer          */
DECL|method|service (XChangeService service)
specifier|default
name|XChangeEndpointBuilder
name|service
parameter_list|(
name|XChangeService
name|service
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"service"
argument_list|,
name|service
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The service to call.          * The option will be converted to a          *<code>org.apache.camel.component.xchange.XChangeConfiguration$XChangeService</code> type.          * @group producer          */
DECL|method|service (String service)
specifier|default
name|XChangeEndpointBuilder
name|service
parameter_list|(
name|String
name|service
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"service"
argument_list|,
name|service
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
comment|/**      * Advanced builder for endpoint for the XChange component.      */
DECL|interface|AdvancedXChangeEndpointBuilder
specifier|public
interface|interface
name|AdvancedXChangeEndpointBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|basic ()
specifier|default
name|XChangeEndpointBuilder
name|basic
parameter_list|()
block|{
return|return
operator|(
name|XChangeEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          * The option is a<code>boolean</code> type.          * @group advanced          */
DECL|method|basicPropertyBinding ( boolean basicPropertyBinding)
specifier|default
name|AdvancedXChangeEndpointBuilder
name|basicPropertyBinding
parameter_list|(
name|boolean
name|basicPropertyBinding
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"basicPropertyBinding"
argument_list|,
name|basicPropertyBinding
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          * The option will be converted to a<code>boolean</code> type.          * @group advanced          */
DECL|method|basicPropertyBinding ( String basicPropertyBinding)
specifier|default
name|AdvancedXChangeEndpointBuilder
name|basicPropertyBinding
parameter_list|(
name|String
name|basicPropertyBinding
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"basicPropertyBinding"
argument_list|,
name|basicPropertyBinding
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets whether synchronous processing should be strictly used, or Camel          * is allowed to use asynchronous processing (if supported).          * The option is a<code>boolean</code> type.          * @group advanced          */
DECL|method|synchronous (boolean synchronous)
specifier|default
name|AdvancedXChangeEndpointBuilder
name|synchronous
parameter_list|(
name|boolean
name|synchronous
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"synchronous"
argument_list|,
name|synchronous
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets whether synchronous processing should be strictly used, or Camel          * is allowed to use asynchronous processing (if supported).          * The option will be converted to a<code>boolean</code> type.          * @group advanced          */
DECL|method|synchronous (String synchronous)
specifier|default
name|AdvancedXChangeEndpointBuilder
name|synchronous
parameter_list|(
name|String
name|synchronous
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"synchronous"
argument_list|,
name|synchronous
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
comment|/**      * Proxy enum for      *<code>org.apache.camel.component.xchange.XChangeConfiguration$XChangeMethod</code> enum.      */
DECL|enum|XChangeMethod
enum|enum
name|XChangeMethod
block|{
DECL|enumConstant|balances
name|balances
block|,
DECL|enumConstant|fundingHistory
name|fundingHistory
block|,
DECL|enumConstant|wallets
name|wallets
block|,
DECL|enumConstant|currencies
name|currencies
block|,
DECL|enumConstant|currencyMetaData
name|currencyMetaData
block|,
DECL|enumConstant|currencyPairs
name|currencyPairs
block|,
DECL|enumConstant|currencyPairMetaData
name|currencyPairMetaData
block|,
DECL|enumConstant|ticker
name|ticker
block|;     }
comment|/**      * Proxy enum for      *<code>org.apache.camel.component.xchange.XChangeConfiguration$XChangeService</code> enum.      */
DECL|enum|XChangeService
enum|enum
name|XChangeService
block|{
DECL|enumConstant|marketdata
name|marketdata
block|,
DECL|enumConstant|metadata
name|metadata
block|,
DECL|enumConstant|account
name|account
block|;     }
comment|/**      * The camel-xchange component provide access to many bitcoin and altcoin      * exchanges for trading and accessing market data.      * Maven coordinates: org.apache.camel:camel-xchange      */
DECL|method|xChange (String path)
specifier|default
name|XChangeEndpointBuilder
name|xChange
parameter_list|(
name|String
name|path
parameter_list|)
block|{
class|class
name|XChangeEndpointBuilderImpl
extends|extends
name|AbstractEndpointBuilder
implements|implements
name|XChangeEndpointBuilder
implements|,
name|AdvancedXChangeEndpointBuilder
block|{
specifier|public
name|XChangeEndpointBuilderImpl
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|super
argument_list|(
literal|"xchange"
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|new
name|XChangeEndpointBuilderImpl
argument_list|(
name|path
argument_list|)
return|;
block|}
block|}
end_interface

end_unit

