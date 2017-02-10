begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|parsers
operator|.
name|DocumentBuilderFactory
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
name|TransformerFactory
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
name|component
operator|.
name|xslt
operator|.
name|XsltUriResolverFactory
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
name|NestedConfigurationProperty
import|;
end_import

begin_comment
comment|/**  * Transforms the message using a XSLT template.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_class
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
block|{
comment|/**      * To use a custom implementation of      * org.apache.camel.converter.jaxp.XmlConverter      */
DECL|field|xmlConverter
specifier|private
name|XmlConverterNestedConfiguration
name|xmlConverter
decl_stmt|;
comment|/**      * To use a custom javax.xml.transform.URIResolver which depends on a      * dynamic endpoint resource URI or which is a subclass of XsltUriResolver.      * Do not use in combination with uriResolver. See also link      * setUriResolver(URIResolver).      */
annotation|@
name|NestedConfigurationProperty
DECL|field|uriResolverFactory
specifier|private
name|XsltUriResolverFactory
name|uriResolverFactory
decl_stmt|;
comment|/**      * To use a custom javax.xml.transform.URIResolver. Do not use in      * combination with uriResolverFactory. See also link      * setUriResolverFactory(XsltUriResolverFactory).      */
DECL|field|uriResolver
specifier|private
name|URIResolver
name|uriResolver
decl_stmt|;
comment|/**      * Cache for the resource content (the stylesheet file) when it is loaded.      * If set to false Camel will reload the stylesheet file on each message      * processing. This is good for development. A cached stylesheet can be      * forced to reload at runtime via JMX using the clearCachedStylesheet      * operation.      */
DECL|field|contentCache
specifier|private
name|Boolean
name|contentCache
init|=
literal|false
decl_stmt|;
comment|/**      * Whether to use Saxon as the transformerFactoryClass. If enabled then the      * class net.sf.saxon.TransformerFactoryImpl. You would need to add Saxon to      * the classpath.      */
DECL|field|saxon
specifier|private
name|Boolean
name|saxon
init|=
literal|false
decl_stmt|;
comment|/**      * Allows you to use a custom net.sf.saxon.lib.ExtensionFunctionDefinition.      * You would need to add camel-saxon to the classpath. The function is      * looked up in the registry where you can comma to separate multiple values      * to lookup.      */
DECL|field|saxonExtensionFunctions
specifier|private
name|List
argument_list|<
name|Object
argument_list|>
name|saxonExtensionFunctions
decl_stmt|;
comment|/**      * To use a custom Saxon configuration      */
DECL|field|saxonConfiguration
specifier|private
name|Object
name|saxonConfiguration
decl_stmt|;
comment|/**      * To set custom Saxon configuration properties      */
DECL|field|saxonConfigurationProperties
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|saxonConfigurationProperties
decl_stmt|;
DECL|method|getXmlConverter ()
specifier|public
name|XmlConverterNestedConfiguration
name|getXmlConverter
parameter_list|()
block|{
return|return
name|xmlConverter
return|;
block|}
DECL|method|setXmlConverter (XmlConverterNestedConfiguration xmlConverter)
specifier|public
name|void
name|setXmlConverter
parameter_list|(
name|XmlConverterNestedConfiguration
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
DECL|method|getSaxon ()
specifier|public
name|Boolean
name|getSaxon
parameter_list|()
block|{
return|return
name|saxon
return|;
block|}
DECL|method|setSaxon (Boolean saxon)
specifier|public
name|void
name|setSaxon
parameter_list|(
name|Boolean
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
DECL|method|setSaxonExtensionFunctions (List<Object> saxonExtensionFunctions)
specifier|public
name|void
name|setSaxonExtensionFunctions
parameter_list|(
name|List
argument_list|<
name|Object
argument_list|>
name|saxonExtensionFunctions
parameter_list|)
block|{
name|this
operator|.
name|saxonExtensionFunctions
operator|=
name|saxonExtensionFunctions
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
DECL|method|setSaxonConfigurationProperties ( Map<String, Object> saxonConfigurationProperties)
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
name|saxonConfigurationProperties
parameter_list|)
block|{
name|this
operator|.
name|saxonConfigurationProperties
operator|=
name|saxonConfigurationProperties
expr_stmt|;
block|}
DECL|class|XmlConverterNestedConfiguration
specifier|public
specifier|static
class|class
name|XmlConverterNestedConfiguration
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
name|converter
operator|.
name|jaxp
operator|.
name|XmlConverter
operator|.
name|class
decl_stmt|;
DECL|field|documentBuilderFactory
specifier|private
name|DocumentBuilderFactory
name|documentBuilderFactory
decl_stmt|;
DECL|field|transformerFactory
specifier|private
name|TransformerFactory
name|transformerFactory
decl_stmt|;
DECL|method|getDocumentBuilderFactory ()
specifier|public
name|DocumentBuilderFactory
name|getDocumentBuilderFactory
parameter_list|()
block|{
return|return
name|documentBuilderFactory
return|;
block|}
DECL|method|setDocumentBuilderFactory ( DocumentBuilderFactory documentBuilderFactory)
specifier|public
name|void
name|setDocumentBuilderFactory
parameter_list|(
name|DocumentBuilderFactory
name|documentBuilderFactory
parameter_list|)
block|{
name|this
operator|.
name|documentBuilderFactory
operator|=
name|documentBuilderFactory
expr_stmt|;
block|}
DECL|method|getTransformerFactory ()
specifier|public
name|TransformerFactory
name|getTransformerFactory
parameter_list|()
block|{
return|return
name|transformerFactory
return|;
block|}
DECL|method|setTransformerFactory (TransformerFactory transformerFactory)
specifier|public
name|void
name|setTransformerFactory
parameter_list|(
name|TransformerFactory
name|transformerFactory
parameter_list|)
block|{
name|this
operator|.
name|transformerFactory
operator|=
name|transformerFactory
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

