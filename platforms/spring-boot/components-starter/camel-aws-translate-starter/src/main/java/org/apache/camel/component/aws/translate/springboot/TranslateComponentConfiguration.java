begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.aws.translate.springboot
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|aws
operator|.
name|translate
operator|.
name|springboot
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
name|com
operator|.
name|amazonaws
operator|.
name|Protocol
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|translate
operator|.
name|AmazonTranslate
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
name|aws
operator|.
name|translate
operator|.
name|TranslateOperations
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
comment|/**  * The aws-translate component is used for managing Amazon Translate  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
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
literal|"camel.component.aws-translate"
argument_list|)
DECL|class|TranslateComponentConfiguration
specifier|public
class|class
name|TranslateComponentConfiguration
extends|extends
name|ComponentConfigurationPropertiesCommon
block|{
comment|/**      * Whether to enable auto configuration of the aws-translate component. This      * is enabled by default.      */
DECL|field|enabled
specifier|private
name|Boolean
name|enabled
decl_stmt|;
comment|/**      * The AWS Translate default configuration      */
DECL|field|configuration
specifier|private
name|TranslateConfigurationNestedConfiguration
name|configuration
decl_stmt|;
comment|/**      * Amazon AWS Access Key      */
DECL|field|accessKey
specifier|private
name|String
name|accessKey
decl_stmt|;
comment|/**      * Amazon AWS Secret Key      */
DECL|field|secretKey
specifier|private
name|String
name|secretKey
decl_stmt|;
comment|/**      * The region in which Translate client needs to work      */
DECL|field|region
specifier|private
name|String
name|region
decl_stmt|;
comment|/**      * Whether the component should use basic property binding (Camel 2.x) or      * the newer property binding with additional capabilities      */
DECL|field|basicPropertyBinding
specifier|private
name|Boolean
name|basicPropertyBinding
init|=
literal|false
decl_stmt|;
comment|/**      * Whether the producer should be started lazy (on the first message). By      * starting lazy you can use this to allow CamelContext and routes to      * startup in situations where a producer may otherwise fail during starting      * and cause the route to fail being started. By deferring this startup to      * be lazy then the startup failure can be handled during routing messages      * via Camel's routing error handlers. Beware that when the first message is      * processed then creating and starting the producer may take a little time      * and prolong the total processing time of the processing.      */
DECL|field|lazyStartProducer
specifier|private
name|Boolean
name|lazyStartProducer
init|=
literal|false
decl_stmt|;
comment|/**      * Allows for bridging the consumer to the Camel routing Error Handler,      * which mean any exceptions occurred while the consumer is trying to pickup      * incoming messages, or the likes, will now be processed as a message and      * handled by the routing Error Handler. By default the consumer will use      * the org.apache.camel.spi.ExceptionHandler to deal with exceptions, that      * will be logged at WARN or ERROR level and ignored.      */
DECL|field|bridgeErrorHandler
specifier|private
name|Boolean
name|bridgeErrorHandler
init|=
literal|false
decl_stmt|;
DECL|method|getConfiguration ()
specifier|public
name|TranslateConfigurationNestedConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
DECL|method|setConfiguration ( TranslateConfigurationNestedConfiguration configuration)
specifier|public
name|void
name|setConfiguration
parameter_list|(
name|TranslateConfigurationNestedConfiguration
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
DECL|method|getAccessKey ()
specifier|public
name|String
name|getAccessKey
parameter_list|()
block|{
return|return
name|accessKey
return|;
block|}
DECL|method|setAccessKey (String accessKey)
specifier|public
name|void
name|setAccessKey
parameter_list|(
name|String
name|accessKey
parameter_list|)
block|{
name|this
operator|.
name|accessKey
operator|=
name|accessKey
expr_stmt|;
block|}
DECL|method|getSecretKey ()
specifier|public
name|String
name|getSecretKey
parameter_list|()
block|{
return|return
name|secretKey
return|;
block|}
DECL|method|setSecretKey (String secretKey)
specifier|public
name|void
name|setSecretKey
parameter_list|(
name|String
name|secretKey
parameter_list|)
block|{
name|this
operator|.
name|secretKey
operator|=
name|secretKey
expr_stmt|;
block|}
DECL|method|getRegion ()
specifier|public
name|String
name|getRegion
parameter_list|()
block|{
return|return
name|region
return|;
block|}
DECL|method|setRegion (String region)
specifier|public
name|void
name|setRegion
parameter_list|(
name|String
name|region
parameter_list|)
block|{
name|this
operator|.
name|region
operator|=
name|region
expr_stmt|;
block|}
DECL|method|getBasicPropertyBinding ()
specifier|public
name|Boolean
name|getBasicPropertyBinding
parameter_list|()
block|{
return|return
name|basicPropertyBinding
return|;
block|}
DECL|method|setBasicPropertyBinding (Boolean basicPropertyBinding)
specifier|public
name|void
name|setBasicPropertyBinding
parameter_list|(
name|Boolean
name|basicPropertyBinding
parameter_list|)
block|{
name|this
operator|.
name|basicPropertyBinding
operator|=
name|basicPropertyBinding
expr_stmt|;
block|}
DECL|method|getLazyStartProducer ()
specifier|public
name|Boolean
name|getLazyStartProducer
parameter_list|()
block|{
return|return
name|lazyStartProducer
return|;
block|}
DECL|method|setLazyStartProducer (Boolean lazyStartProducer)
specifier|public
name|void
name|setLazyStartProducer
parameter_list|(
name|Boolean
name|lazyStartProducer
parameter_list|)
block|{
name|this
operator|.
name|lazyStartProducer
operator|=
name|lazyStartProducer
expr_stmt|;
block|}
DECL|method|getBridgeErrorHandler ()
specifier|public
name|Boolean
name|getBridgeErrorHandler
parameter_list|()
block|{
return|return
name|bridgeErrorHandler
return|;
block|}
DECL|method|setBridgeErrorHandler (Boolean bridgeErrorHandler)
specifier|public
name|void
name|setBridgeErrorHandler
parameter_list|(
name|Boolean
name|bridgeErrorHandler
parameter_list|)
block|{
name|this
operator|.
name|bridgeErrorHandler
operator|=
name|bridgeErrorHandler
expr_stmt|;
block|}
DECL|class|TranslateConfigurationNestedConfiguration
specifier|public
specifier|static
class|class
name|TranslateConfigurationNestedConfiguration
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
name|aws
operator|.
name|translate
operator|.
name|TranslateConfiguration
operator|.
name|class
decl_stmt|;
comment|/**          * To use a existing configured AWS Translate as client          */
DECL|field|translateClient
specifier|private
name|AmazonTranslate
name|translateClient
decl_stmt|;
comment|/**          * Amazon AWS Access Key          */
DECL|field|accessKey
specifier|private
name|String
name|accessKey
decl_stmt|;
comment|/**          * Amazon AWS Secret Key          */
DECL|field|secretKey
specifier|private
name|String
name|secretKey
decl_stmt|;
comment|/**          * The operation to perform          */
DECL|field|operation
specifier|private
name|TranslateOperations
name|operation
decl_stmt|;
comment|/**          * To define a proxy protocol when instantiating the Translate client          */
DECL|field|proxyProtocol
specifier|private
name|Protocol
name|proxyProtocol
init|=
name|Protocol
operator|.
name|HTTPS
decl_stmt|;
comment|/**          * To define a proxy host when instantiating the Translate client          */
DECL|field|proxyHost
specifier|private
name|String
name|proxyHost
decl_stmt|;
comment|/**          * To define a proxy port when instantiating the Translate client          */
DECL|field|proxyPort
specifier|private
name|Integer
name|proxyPort
decl_stmt|;
comment|/**          * The region in which Translate client needs to work. When using this          * parameter, the configuration will expect the capitalized name of the          * region (for example AP_EAST_1) You'll need to use the name          * Regions.EU_WEST_1.name()          */
DECL|field|region
specifier|private
name|String
name|region
decl_stmt|;
comment|/**          * Being able to autodetect the source language          */
DECL|field|autodetectSourceLanguage
specifier|private
name|Boolean
name|autodetectSourceLanguage
init|=
literal|false
decl_stmt|;
DECL|method|getTranslateClient ()
specifier|public
name|AmazonTranslate
name|getTranslateClient
parameter_list|()
block|{
return|return
name|translateClient
return|;
block|}
DECL|method|setTranslateClient (AmazonTranslate translateClient)
specifier|public
name|void
name|setTranslateClient
parameter_list|(
name|AmazonTranslate
name|translateClient
parameter_list|)
block|{
name|this
operator|.
name|translateClient
operator|=
name|translateClient
expr_stmt|;
block|}
DECL|method|getAccessKey ()
specifier|public
name|String
name|getAccessKey
parameter_list|()
block|{
return|return
name|accessKey
return|;
block|}
DECL|method|setAccessKey (String accessKey)
specifier|public
name|void
name|setAccessKey
parameter_list|(
name|String
name|accessKey
parameter_list|)
block|{
name|this
operator|.
name|accessKey
operator|=
name|accessKey
expr_stmt|;
block|}
DECL|method|getSecretKey ()
specifier|public
name|String
name|getSecretKey
parameter_list|()
block|{
return|return
name|secretKey
return|;
block|}
DECL|method|setSecretKey (String secretKey)
specifier|public
name|void
name|setSecretKey
parameter_list|(
name|String
name|secretKey
parameter_list|)
block|{
name|this
operator|.
name|secretKey
operator|=
name|secretKey
expr_stmt|;
block|}
DECL|method|getOperation ()
specifier|public
name|TranslateOperations
name|getOperation
parameter_list|()
block|{
return|return
name|operation
return|;
block|}
DECL|method|setOperation (TranslateOperations operation)
specifier|public
name|void
name|setOperation
parameter_list|(
name|TranslateOperations
name|operation
parameter_list|)
block|{
name|this
operator|.
name|operation
operator|=
name|operation
expr_stmt|;
block|}
DECL|method|getProxyProtocol ()
specifier|public
name|Protocol
name|getProxyProtocol
parameter_list|()
block|{
return|return
name|proxyProtocol
return|;
block|}
DECL|method|setProxyProtocol (Protocol proxyProtocol)
specifier|public
name|void
name|setProxyProtocol
parameter_list|(
name|Protocol
name|proxyProtocol
parameter_list|)
block|{
name|this
operator|.
name|proxyProtocol
operator|=
name|proxyProtocol
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
DECL|method|getRegion ()
specifier|public
name|String
name|getRegion
parameter_list|()
block|{
return|return
name|region
return|;
block|}
DECL|method|setRegion (String region)
specifier|public
name|void
name|setRegion
parameter_list|(
name|String
name|region
parameter_list|)
block|{
name|this
operator|.
name|region
operator|=
name|region
expr_stmt|;
block|}
DECL|method|getAutodetectSourceLanguage ()
specifier|public
name|Boolean
name|getAutodetectSourceLanguage
parameter_list|()
block|{
return|return
name|autodetectSourceLanguage
return|;
block|}
DECL|method|setAutodetectSourceLanguage (Boolean autodetectSourceLanguage)
specifier|public
name|void
name|setAutodetectSourceLanguage
parameter_list|(
name|Boolean
name|autodetectSourceLanguage
parameter_list|)
block|{
name|this
operator|.
name|autodetectSourceLanguage
operator|=
name|autodetectSourceLanguage
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

