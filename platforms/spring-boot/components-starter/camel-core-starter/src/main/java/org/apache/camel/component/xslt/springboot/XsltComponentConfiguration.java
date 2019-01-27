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
comment|/**      * Whether to use Saxon as the transformerFactoryClass. If enabled then the      * class net.sf.saxon.TransformerFactoryImpl. You would need to add Saxon to      * the classpath.      */
DECL|field|saxon
specifier|private
name|Boolean
name|saxon
init|=
literal|false
decl_stmt|;
comment|/**      * Allows you to use a custom net.sf.saxon.lib.ExtensionFunctionDefinition.      * You would need to add camel-saxon to the classpath. The function is      * looked up in the registry, where you can comma to separate multiple      * values to lookup.      */
DECL|field|saxonExtensionFunctions
specifier|private
name|String
name|saxonExtensionFunctions
decl_stmt|;
comment|/**      * To use a custom Saxon configuration. The option is a java.lang.Object      * type.      */
DECL|field|saxonConfiguration
specifier|private
name|String
name|saxonConfiguration
decl_stmt|;
comment|/**      * To set custom Saxon configuration properties. The option is a      * java.util.Map<java.lang.String,java.lang.Object> type.      */
DECL|field|saxonConfigurationProperties
specifier|private
name|String
name|saxonConfigurationProperties
decl_stmt|;
comment|/**      * Whether the component should resolve property placeholders on itself when      * starting. Only properties which are of String type can use property      * placeholders.      */
DECL|field|resolvePropertyPlaceholders
specifier|private
name|Boolean
name|resolvePropertyPlaceholders
init|=
literal|true
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
name|String
name|getSaxonExtensionFunctions
parameter_list|()
block|{
return|return
name|saxonExtensionFunctions
return|;
block|}
DECL|method|setSaxonExtensionFunctions (String saxonExtensionFunctions)
specifier|public
name|void
name|setSaxonExtensionFunctions
parameter_list|(
name|String
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
name|String
name|getSaxonConfiguration
parameter_list|()
block|{
return|return
name|saxonConfiguration
return|;
block|}
DECL|method|setSaxonConfiguration (String saxonConfiguration)
specifier|public
name|void
name|setSaxonConfiguration
parameter_list|(
name|String
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
name|String
name|getSaxonConfigurationProperties
parameter_list|()
block|{
return|return
name|saxonConfigurationProperties
return|;
block|}
DECL|method|setSaxonConfigurationProperties ( String saxonConfigurationProperties)
specifier|public
name|void
name|setSaxonConfigurationProperties
parameter_list|(
name|String
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
block|}
end_class

end_unit

