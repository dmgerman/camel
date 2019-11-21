begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.xslt.springboot
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|xslt
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
comment|/**  * Transforms the message using a XSLT template.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
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
literal|"camel.component.xslt"
argument_list|)
DECL|class|XsltComponentConfiguration
specifier|public
class|class
name|XsltComponentConfiguration
extends|extends
name|ComponentConfigurationPropertiesCommon
block|{
comment|/**      * Whether to enable auto configuration of the xslt component. This is      * enabled by default.      */
DECL|field|enabled
specifier|private
name|Boolean
name|enabled
decl_stmt|;
comment|/**      * To use a custom UriResolver which depends on a dynamic endpoint resource      * URI. Should not be used together with the option 'uriResolver'. The      * option is a org.apache.camel.component.xslt.XsltUriResolverFactory type.      */
DECL|field|uriResolverFactory
specifier|private
name|String
name|uriResolverFactory
decl_stmt|;
comment|/**      * To use a custom UriResolver. Should not be used together with the option      * 'uriResolverFactory'. The option is a javax.xml.transform.URIResolver      * type.      */
DECL|field|uriResolver
specifier|private
name|String
name|uriResolver
decl_stmt|;
comment|/**      * Cache for the resource content (the stylesheet file) when it is loaded.      * If set to false Camel will reload the stylesheet file on each message      * processing. This is good for development. A cached stylesheet can be      * forced to reload at runtime via JMX using the clearCachedStylesheet      * operation.      */
DECL|field|contentCache
specifier|private
name|Boolean
name|contentCache
init|=
literal|true
decl_stmt|;
comment|/**      * A configuration strategy to apply on freshly created instances of      * TransformerFactory. The option is a      * org.apache.camel.component.xslt.TransformerFactoryConfigurationStrategy      * type.      */
DECL|field|transformerFactoryConfigurationStrategy
specifier|private
name|String
name|transformerFactoryConfigurationStrategy
decl_stmt|;
comment|/**      * To use a custom XSLT transformer factory, specified as a FQN class name      */
DECL|field|transformerFactoryClass
specifier|private
name|String
name|transformerFactoryClass
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
DECL|method|getUriResolverFactory ()
specifier|public
name|String
name|getUriResolverFactory
parameter_list|()
block|{
return|return
name|uriResolverFactory
return|;
block|}
DECL|method|setUriResolverFactory (String uriResolverFactory)
specifier|public
name|void
name|setUriResolverFactory
parameter_list|(
name|String
name|uriResolverFactory
parameter_list|)
block|{
name|this
operator|.
name|uriResolverFactory
operator|=
name|uriResolverFactory
expr_stmt|;
block|}
DECL|method|getUriResolver ()
specifier|public
name|String
name|getUriResolver
parameter_list|()
block|{
return|return
name|uriResolver
return|;
block|}
DECL|method|setUriResolver (String uriResolver)
specifier|public
name|void
name|setUriResolver
parameter_list|(
name|String
name|uriResolver
parameter_list|)
block|{
name|this
operator|.
name|uriResolver
operator|=
name|uriResolver
expr_stmt|;
block|}
DECL|method|getContentCache ()
specifier|public
name|Boolean
name|getContentCache
parameter_list|()
block|{
return|return
name|contentCache
return|;
block|}
DECL|method|setContentCache (Boolean contentCache)
specifier|public
name|void
name|setContentCache
parameter_list|(
name|Boolean
name|contentCache
parameter_list|)
block|{
name|this
operator|.
name|contentCache
operator|=
name|contentCache
expr_stmt|;
block|}
DECL|method|getTransformerFactoryConfigurationStrategy ()
specifier|public
name|String
name|getTransformerFactoryConfigurationStrategy
parameter_list|()
block|{
return|return
name|transformerFactoryConfigurationStrategy
return|;
block|}
DECL|method|setTransformerFactoryConfigurationStrategy ( String transformerFactoryConfigurationStrategy)
specifier|public
name|void
name|setTransformerFactoryConfigurationStrategy
parameter_list|(
name|String
name|transformerFactoryConfigurationStrategy
parameter_list|)
block|{
name|this
operator|.
name|transformerFactoryConfigurationStrategy
operator|=
name|transformerFactoryConfigurationStrategy
expr_stmt|;
block|}
DECL|method|getTransformerFactoryClass ()
specifier|public
name|String
name|getTransformerFactoryClass
parameter_list|()
block|{
return|return
name|transformerFactoryClass
return|;
block|}
DECL|method|setTransformerFactoryClass (String transformerFactoryClass)
specifier|public
name|void
name|setTransformerFactoryClass
parameter_list|(
name|String
name|transformerFactoryClass
parameter_list|)
block|{
name|this
operator|.
name|transformerFactoryClass
operator|=
name|transformerFactoryClass
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
block|}
end_class

end_unit

