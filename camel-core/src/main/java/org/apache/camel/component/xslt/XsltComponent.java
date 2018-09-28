begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.xslt
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
name|List
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
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|URIResolver
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
name|Endpoint
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
name|converter
operator|.
name|jaxp
operator|.
name|XmlConverter
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
name|DefaultComponent
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
name|util
operator|.
name|EndpointHelper
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
name|ResourceHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_comment
comment|/**  * The<a href="http://camel.apache.org/xslt.html">XSLT Component</a> is for performing XSLT transformations of messages  */
end_comment

begin_class
DECL|class|XsltComponent
specifier|public
class|class
name|XsltComponent
extends|extends
name|DefaultComponent
block|{
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|xmlConverter
specifier|private
name|XmlConverter
name|xmlConverter
decl_stmt|;
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|uriResolver
specifier|private
name|URIResolver
name|uriResolver
decl_stmt|;
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|uriResolverFactory
specifier|private
name|XsltUriResolverFactory
name|uriResolverFactory
decl_stmt|;
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|saxonConfiguration
specifier|private
name|Object
name|saxonConfiguration
decl_stmt|;
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|saxonConfigurationProperties
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|saxonConfigurationProperties
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|,
name|javaType
operator|=
literal|"java.lang.String"
argument_list|)
DECL|field|saxonExtensionFunctions
specifier|private
name|List
argument_list|<
name|Object
argument_list|>
name|saxonExtensionFunctions
decl_stmt|;
annotation|@
name|Metadata
argument_list|(
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|contentCache
specifier|private
name|boolean
name|contentCache
init|=
literal|true
decl_stmt|;
DECL|field|saxon
specifier|private
name|boolean
name|saxon
decl_stmt|;
DECL|method|XsltComponent ()
specifier|public
name|XsltComponent
parameter_list|()
block|{     }
DECL|method|getXmlConverter ()
specifier|public
name|XmlConverter
name|getXmlConverter
parameter_list|()
block|{
return|return
name|xmlConverter
return|;
block|}
comment|/**      * To use a custom implementation of {@link org.apache.camel.converter.jaxp.XmlConverter}      */
DECL|method|setXmlConverter (XmlConverter xmlConverter)
specifier|public
name|void
name|setXmlConverter
parameter_list|(
name|XmlConverter
name|xmlConverter
parameter_list|)
block|{
name|this
operator|.
name|xmlConverter
operator|=
name|xmlConverter
expr_stmt|;
block|}
DECL|method|getUriResolverFactory ()
specifier|public
name|XsltUriResolverFactory
name|getUriResolverFactory
parameter_list|()
block|{
return|return
name|uriResolverFactory
return|;
block|}
comment|/**      * To use a custom UriResolver which depends on a dynamic endpoint resource URI. Should not be used together with the option 'uriResolver'.      */
DECL|method|setUriResolverFactory (XsltUriResolverFactory uriResolverFactory)
specifier|public
name|void
name|setUriResolverFactory
parameter_list|(
name|XsltUriResolverFactory
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
name|URIResolver
name|getUriResolver
parameter_list|()
block|{
return|return
name|uriResolver
return|;
block|}
comment|/**      * To use a custom UriResolver. Should not be used together with the option 'uriResolverFactory'.      */
DECL|method|setUriResolver (URIResolver uriResolver)
specifier|public
name|void
name|setUriResolver
parameter_list|(
name|URIResolver
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
DECL|method|isContentCache ()
specifier|public
name|boolean
name|isContentCache
parameter_list|()
block|{
return|return
name|contentCache
return|;
block|}
comment|/**      * Cache for the resource content (the stylesheet file) when it is loaded.      * If set to false Camel will reload the stylesheet file on each message processing. This is good for development.      * A cached stylesheet can be forced to reload at runtime via JMX using the clearCachedStylesheet operation.      */
DECL|method|setContentCache (boolean contentCache)
specifier|public
name|void
name|setContentCache
parameter_list|(
name|boolean
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
DECL|method|isSaxon ()
specifier|public
name|boolean
name|isSaxon
parameter_list|()
block|{
return|return
name|saxon
return|;
block|}
comment|/**      * Whether to use Saxon as the transformerFactoryClass.      * If enabled then the class net.sf.saxon.TransformerFactoryImpl. You would need to add Saxon to the classpath.      */
DECL|method|setSaxon (boolean saxon)
specifier|public
name|void
name|setSaxon
parameter_list|(
name|boolean
name|saxon
parameter_list|)
block|{
name|this
operator|.
name|saxon
operator|=
name|saxon
expr_stmt|;
block|}
DECL|method|getSaxonExtensionFunctions ()
specifier|public
name|List
argument_list|<
name|Object
argument_list|>
name|getSaxonExtensionFunctions
parameter_list|()
block|{
return|return
name|saxonExtensionFunctions
return|;
block|}
comment|/**      * Allows you to use a custom net.sf.saxon.lib.ExtensionFunctionDefinition.      * You would need to add camel-saxon to the classpath.      * The function is looked up in the registry, where you can comma to separate multiple values to lookup.      */
DECL|method|setSaxonExtensionFunctions (List<Object> extensionFunctions)
specifier|public
name|void
name|setSaxonExtensionFunctions
parameter_list|(
name|List
argument_list|<
name|Object
argument_list|>
name|extensionFunctions
parameter_list|)
block|{
name|this
operator|.
name|saxonExtensionFunctions
operator|=
name|extensionFunctions
expr_stmt|;
block|}
comment|/**      * Allows you to use a custom net.sf.saxon.lib.ExtensionFunctionDefinition.      * You would need to add camel-saxon to the classpath.      * The function is looked up in the registry, where you can comma to separate multiple values to lookup.      */
DECL|method|setSaxonExtensionFunctions (String extensionFunctions)
specifier|public
name|void
name|setSaxonExtensionFunctions
parameter_list|(
name|String
name|extensionFunctions
parameter_list|)
block|{
name|this
operator|.
name|saxonExtensionFunctions
operator|=
name|EndpointHelper
operator|.
name|resolveReferenceListParameter
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
name|extensionFunctions
argument_list|,
name|Object
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
DECL|method|getSaxonConfiguration ()
specifier|public
name|Object
name|getSaxonConfiguration
parameter_list|()
block|{
return|return
name|saxonConfiguration
return|;
block|}
comment|/**      * To use a custom Saxon configuration      */
DECL|method|setSaxonConfiguration (Object saxonConfiguration)
specifier|public
name|void
name|setSaxonConfiguration
parameter_list|(
name|Object
name|saxonConfiguration
parameter_list|)
block|{
name|this
operator|.
name|saxonConfiguration
operator|=
name|saxonConfiguration
expr_stmt|;
block|}
DECL|method|getSaxonConfigurationProperties ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getSaxonConfigurationProperties
parameter_list|()
block|{
return|return
name|saxonConfigurationProperties
return|;
block|}
comment|/**      * To set custom Saxon configuration properties      */
DECL|method|setSaxonConfigurationProperties (Map<String, Object> configurationProperties)
specifier|public
name|void
name|setSaxonConfigurationProperties
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|configurationProperties
parameter_list|)
block|{
name|this
operator|.
name|saxonConfigurationProperties
operator|=
name|configurationProperties
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createEndpoint (String uri, final String remaining, Map<String, Object> parameters)
specifier|protected
name|Endpoint
name|createEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
specifier|final
name|String
name|remaining
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
throws|throws
name|Exception
block|{
name|XsltEndpoint
name|endpoint
init|=
operator|new
name|XsltEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|)
decl_stmt|;
name|endpoint
operator|.
name|setConverter
argument_list|(
name|getXmlConverter
argument_list|()
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|setContentCache
argument_list|(
name|isContentCache
argument_list|()
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|setSaxon
argument_list|(
name|isSaxon
argument_list|()
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|setSaxonConfiguration
argument_list|(
name|saxonConfiguration
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|setSaxonConfigurationProperties
argument_list|(
name|saxonConfigurationProperties
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|setSaxonExtensionFunctions
argument_list|(
name|saxonExtensionFunctions
argument_list|)
expr_stmt|;
comment|// lookup custom resolver to use
name|URIResolver
name|resolver
init|=
name|resolveAndRemoveReferenceParameter
argument_list|(
name|parameters
argument_list|,
literal|"uriResolver"
argument_list|,
name|URIResolver
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|resolver
operator|==
literal|null
condition|)
block|{
comment|// not in endpoint then use component specific resolver
name|resolver
operator|=
name|getUriResolver
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|resolver
operator|==
literal|null
condition|)
block|{
comment|// lookup custom resolver factory to use
name|XsltUriResolverFactory
name|resolverFactory
init|=
name|resolveAndRemoveReferenceParameter
argument_list|(
name|parameters
argument_list|,
literal|"uriResolverFactory"
argument_list|,
name|XsltUriResolverFactory
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|resolverFactory
operator|==
literal|null
condition|)
block|{
comment|// not in endpoint then use component specific resolver factory
name|resolverFactory
operator|=
name|getUriResolverFactory
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|resolverFactory
operator|==
literal|null
condition|)
block|{
comment|// fallback to use the Default URI resolver factory
name|resolverFactory
operator|=
operator|new
name|DefaultXsltUriResolverFactory
argument_list|()
expr_stmt|;
block|}
name|resolver
operator|=
name|resolverFactory
operator|.
name|createUriResolver
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
name|remaining
argument_list|)
expr_stmt|;
block|}
name|endpoint
operator|.
name|setUriResolver
argument_list|(
name|resolver
argument_list|)
expr_stmt|;
name|setProperties
argument_list|(
name|endpoint
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
name|String
name|resourceUri
init|=
name|remaining
decl_stmt|;
if|if
condition|(
name|ResourceHelper
operator|.
name|isHttpUri
argument_list|(
name|resourceUri
argument_list|)
condition|)
block|{
comment|// if its a http uri, then append additional parameters as they are part of the uri
name|resourceUri
operator|=
name|ResourceHelper
operator|.
name|appendParameters
argument_list|(
name|resourceUri
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
block|}
name|log
operator|.
name|debug
argument_list|(
literal|"{} using schema resource: {}"
argument_list|,
name|this
argument_list|,
name|resourceUri
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|setResourceUri
argument_list|(
name|resourceUri
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|parameters
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
comment|// additional parameters need to be stored on endpoint as they can be used to configure xslt builder additionally
name|endpoint
operator|.
name|setParameters
argument_list|(
name|parameters
argument_list|)
expr_stmt|;
block|}
return|return
name|endpoint
return|;
block|}
block|}
end_class

end_unit

