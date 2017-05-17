begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.braintree.springboot
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|braintree
operator|.
name|springboot
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Level
import|;
end_import

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
name|component
operator|.
name|braintree
operator|.
name|internal
operator|.
name|BraintreeApiName
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
name|spring
operator|.
name|boot
operator|.
name|ComponentConfigurationPropertiesCommon
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|context
operator|.
name|properties
operator|.
name|ConfigurationProperties
import|;
end_import

begin_comment
comment|/**  * The braintree component is used for integrating with the Braintree Payment  * System.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_class
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.SpringBootAutoConfigurationMojo"
argument_list|)
annotation|@
name|ConfigurationProperties
argument_list|(
name|prefix
operator|=
literal|"camel.component.braintree"
argument_list|)
DECL|class|BraintreeComponentConfiguration
specifier|public
class|class
name|BraintreeComponentConfiguration
extends|extends
name|ComponentConfigurationPropertiesCommon
block|{
comment|/**      * To use the shared configuration      */
DECL|field|configuration
specifier|private
name|BraintreeConfigurationNestedConfiguration
name|configuration
decl_stmt|;
comment|/**      * Whether the component should resolve property placeholders on itself when      * starting. Only properties which are of String type can use property      * placeholders.      */
DECL|field|resolvePropertyPlaceholders
specifier|private
name|Boolean
name|resolvePropertyPlaceholders
init|=
literal|true
decl_stmt|;
DECL|method|getConfiguration ()
specifier|public
name|BraintreeConfigurationNestedConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
DECL|method|setConfiguration ( BraintreeConfigurationNestedConfiguration configuration)
specifier|public
name|void
name|setConfiguration
parameter_list|(
name|BraintreeConfigurationNestedConfiguration
name|configuration
parameter_list|)
block|{
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
block|}
DECL|method|getResolvePropertyPlaceholders ()
specifier|public
name|Boolean
name|getResolvePropertyPlaceholders
parameter_list|()
block|{
return|return
name|resolvePropertyPlaceholders
return|;
block|}
DECL|method|setResolvePropertyPlaceholders ( Boolean resolvePropertyPlaceholders)
specifier|public
name|void
name|setResolvePropertyPlaceholders
parameter_list|(
name|Boolean
name|resolvePropertyPlaceholders
parameter_list|)
block|{
name|this
operator|.
name|resolvePropertyPlaceholders
operator|=
name|resolvePropertyPlaceholders
expr_stmt|;
block|}
DECL|class|BraintreeConfigurationNestedConfiguration
specifier|public
specifier|static
class|class
name|BraintreeConfigurationNestedConfiguration
block|{
DECL|field|CAMEL_NESTED_CLASS
specifier|public
specifier|static
specifier|final
name|Class
name|CAMEL_NESTED_CLASS
init|=
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|braintree
operator|.
name|BraintreeConfiguration
operator|.
name|class
decl_stmt|;
comment|/**          * What kind of operation to perform          */
DECL|field|apiName
specifier|private
name|BraintreeApiName
name|apiName
decl_stmt|;
comment|/**          * What sub operation to use for the selected operation          */
DECL|field|methodName
specifier|private
name|String
name|methodName
decl_stmt|;
comment|/**          * The environment Either SANDBOX or PRODUCTION          */
DECL|field|environment
specifier|private
name|String
name|environment
decl_stmt|;
comment|/**          * The merchant id provided by Braintree.          */
DECL|field|merchantId
specifier|private
name|String
name|merchantId
decl_stmt|;
comment|/**          * The public key provided by Braintree.          */
DECL|field|publicKey
specifier|private
name|String
name|publicKey
decl_stmt|;
comment|/**          * The private key provided by Braintree.          */
DECL|field|privateKey
specifier|private
name|String
name|privateKey
decl_stmt|;
comment|/**          * The proxy host          */
DECL|field|proxyHost
specifier|private
name|String
name|proxyHost
decl_stmt|;
comment|/**          * The proxy port          */
DECL|field|proxyPort
specifier|private
name|Integer
name|proxyPort
decl_stmt|;
comment|/**          * Set logging level for http calls, @see java.util.logging.Level          */
DECL|field|httpLogLevel
specifier|private
name|Level
name|httpLogLevel
decl_stmt|;
comment|/**          * Set log category to use to log http calls, default "Braintree"          */
DECL|field|httpLogName
specifier|private
name|String
name|httpLogName
decl_stmt|;
comment|/**          * Set read timeout for http calls.          */
DECL|field|httpReadTimeout
specifier|private
name|Integer
name|httpReadTimeout
decl_stmt|;
DECL|method|getApiName ()
specifier|public
name|BraintreeApiName
name|getApiName
parameter_list|()
block|{
return|return
name|apiName
return|;
block|}
DECL|method|setApiName (BraintreeApiName apiName)
specifier|public
name|void
name|setApiName
parameter_list|(
name|BraintreeApiName
name|apiName
parameter_list|)
block|{
name|this
operator|.
name|apiName
operator|=
name|apiName
expr_stmt|;
block|}
DECL|method|getMethodName ()
specifier|public
name|String
name|getMethodName
parameter_list|()
block|{
return|return
name|methodName
return|;
block|}
DECL|method|setMethodName (String methodName)
specifier|public
name|void
name|setMethodName
parameter_list|(
name|String
name|methodName
parameter_list|)
block|{
name|this
operator|.
name|methodName
operator|=
name|methodName
expr_stmt|;
block|}
DECL|method|getEnvironment ()
specifier|public
name|String
name|getEnvironment
parameter_list|()
block|{
return|return
name|environment
return|;
block|}
DECL|method|setEnvironment (String environment)
specifier|public
name|void
name|setEnvironment
parameter_list|(
name|String
name|environment
parameter_list|)
block|{
name|this
operator|.
name|environment
operator|=
name|environment
expr_stmt|;
block|}
DECL|method|getMerchantId ()
specifier|public
name|String
name|getMerchantId
parameter_list|()
block|{
return|return
name|merchantId
return|;
block|}
DECL|method|setMerchantId (String merchantId)
specifier|public
name|void
name|setMerchantId
parameter_list|(
name|String
name|merchantId
parameter_list|)
block|{
name|this
operator|.
name|merchantId
operator|=
name|merchantId
expr_stmt|;
block|}
DECL|method|getPublicKey ()
specifier|public
name|String
name|getPublicKey
parameter_list|()
block|{
return|return
name|publicKey
return|;
block|}
DECL|method|setPublicKey (String publicKey)
specifier|public
name|void
name|setPublicKey
parameter_list|(
name|String
name|publicKey
parameter_list|)
block|{
name|this
operator|.
name|publicKey
operator|=
name|publicKey
expr_stmt|;
block|}
DECL|method|getPrivateKey ()
specifier|public
name|String
name|getPrivateKey
parameter_list|()
block|{
return|return
name|privateKey
return|;
block|}
DECL|method|setPrivateKey (String privateKey)
specifier|public
name|void
name|setPrivateKey
parameter_list|(
name|String
name|privateKey
parameter_list|)
block|{
name|this
operator|.
name|privateKey
operator|=
name|privateKey
expr_stmt|;
block|}
DECL|method|getProxyHost ()
specifier|public
name|String
name|getProxyHost
parameter_list|()
block|{
return|return
name|proxyHost
return|;
block|}
DECL|method|setProxyHost (String proxyHost)
specifier|public
name|void
name|setProxyHost
parameter_list|(
name|String
name|proxyHost
parameter_list|)
block|{
name|this
operator|.
name|proxyHost
operator|=
name|proxyHost
expr_stmt|;
block|}
DECL|method|getProxyPort ()
specifier|public
name|Integer
name|getProxyPort
parameter_list|()
block|{
return|return
name|proxyPort
return|;
block|}
DECL|method|setProxyPort (Integer proxyPort)
specifier|public
name|void
name|setProxyPort
parameter_list|(
name|Integer
name|proxyPort
parameter_list|)
block|{
name|this
operator|.
name|proxyPort
operator|=
name|proxyPort
expr_stmt|;
block|}
DECL|method|getHttpLogLevel ()
specifier|public
name|Level
name|getHttpLogLevel
parameter_list|()
block|{
return|return
name|httpLogLevel
return|;
block|}
DECL|method|setHttpLogLevel (Level httpLogLevel)
specifier|public
name|void
name|setHttpLogLevel
parameter_list|(
name|Level
name|httpLogLevel
parameter_list|)
block|{
name|this
operator|.
name|httpLogLevel
operator|=
name|httpLogLevel
expr_stmt|;
block|}
DECL|method|getHttpLogName ()
specifier|public
name|String
name|getHttpLogName
parameter_list|()
block|{
return|return
name|httpLogName
return|;
block|}
DECL|method|setHttpLogName (String httpLogName)
specifier|public
name|void
name|setHttpLogName
parameter_list|(
name|String
name|httpLogName
parameter_list|)
block|{
name|this
operator|.
name|httpLogName
operator|=
name|httpLogName
expr_stmt|;
block|}
DECL|method|getHttpReadTimeout ()
specifier|public
name|Integer
name|getHttpReadTimeout
parameter_list|()
block|{
return|return
name|httpReadTimeout
return|;
block|}
DECL|method|setHttpReadTimeout (Integer httpReadTimeout)
specifier|public
name|void
name|setHttpReadTimeout
parameter_list|(
name|Integer
name|httpReadTimeout
parameter_list|)
block|{
name|this
operator|.
name|httpReadTimeout
operator|=
name|httpReadTimeout
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

