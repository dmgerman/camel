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
name|java
operator|.
name|util
operator|.
name|List
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
comment|/**  * Transforms the message using a XSLT template.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_interface
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.EndpointDslMojo"
argument_list|)
DECL|interface|XsltEndpointBuilderFactory
specifier|public
interface|interface
name|XsltEndpointBuilderFactory
block|{
comment|/**      * Builder for endpoint for the XSLT component.      */
DECL|interface|XsltEndpointBuilder
specifier|public
interface|interface
name|XsltEndpointBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|advanced ()
specifier|default
name|AdvancedXsltEndpointBuilder
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedXsltEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Path to the template. The following is supported by the default          * URIResolver. You can prefix with: classpath, file, http, ref, or          * bean. classpath, file and http loads the resource using these          * protocols (classpath is default). ref will lookup the resource in the          * registry. bean will call a method on a bean to be used as the          * resource. For bean you can specify the method name after dot, eg          * bean:myBean.myMethod.          * The option is a<code>java.lang.String</code> type.          * @group producer          */
DECL|method|resourceUri (String resourceUri)
specifier|default
name|XsltEndpointBuilder
name|resourceUri
parameter_list|(
name|String
name|resourceUri
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"resourceUri"
argument_list|,
name|resourceUri
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether to allow using StAX as the javax.xml.transform.Source.          * The option is a<code>boolean</code> type.          * @group producer          */
DECL|method|allowStAX (boolean allowStAX)
specifier|default
name|XsltEndpointBuilder
name|allowStAX
parameter_list|(
name|boolean
name|allowStAX
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"allowStAX"
argument_list|,
name|allowStAX
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether to allow using StAX as the javax.xml.transform.Source.          * The option will be converted to a<code>boolean</code> type.          * @group producer          */
DECL|method|allowStAX (String allowStAX)
specifier|default
name|XsltEndpointBuilder
name|allowStAX
parameter_list|(
name|String
name|allowStAX
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"allowStAX"
argument_list|,
name|allowStAX
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Cache for the resource content (the stylesheet file) when it is          * loaded. If set to false Camel will reload the stylesheet file on each          * message processing. This is good for development. A cached stylesheet          * can be forced to reload at runtime via JMX using the          * clearCachedStylesheet operation.          * The option is a<code>boolean</code> type.          * @group producer          */
DECL|method|contentCache (boolean contentCache)
specifier|default
name|XsltEndpointBuilder
name|contentCache
parameter_list|(
name|boolean
name|contentCache
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"contentCache"
argument_list|,
name|contentCache
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Cache for the resource content (the stylesheet file) when it is          * loaded. If set to false Camel will reload the stylesheet file on each          * message processing. This is good for development. A cached stylesheet          * can be forced to reload at runtime via JMX using the          * clearCachedStylesheet operation.          * The option will be converted to a<code>boolean</code> type.          * @group producer          */
DECL|method|contentCache (String contentCache)
specifier|default
name|XsltEndpointBuilder
name|contentCache
parameter_list|(
name|String
name|contentCache
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"contentCache"
argument_list|,
name|contentCache
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * If you have output=file then this option dictates whether or not the          * output file should be deleted when the Exchange is done processing.          * For example suppose the output file is a temporary file, then it can          * be a good idea to delete it after use.          * The option is a<code>boolean</code> type.          * @group producer          */
DECL|method|deleteOutputFile (boolean deleteOutputFile)
specifier|default
name|XsltEndpointBuilder
name|deleteOutputFile
parameter_list|(
name|boolean
name|deleteOutputFile
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"deleteOutputFile"
argument_list|,
name|deleteOutputFile
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * If you have output=file then this option dictates whether or not the          * output file should be deleted when the Exchange is done processing.          * For example suppose the output file is a temporary file, then it can          * be a good idea to delete it after use.          * The option will be converted to a<code>boolean</code> type.          * @group producer          */
DECL|method|deleteOutputFile (String deleteOutputFile)
specifier|default
name|XsltEndpointBuilder
name|deleteOutputFile
parameter_list|(
name|String
name|deleteOutputFile
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"deleteOutputFile"
argument_list|,
name|deleteOutputFile
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether or not to throw an exception if the input body is null.          * The option is a<code>boolean</code> type.          * @group producer          */
DECL|method|failOnNullBody (boolean failOnNullBody)
specifier|default
name|XsltEndpointBuilder
name|failOnNullBody
parameter_list|(
name|boolean
name|failOnNullBody
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"failOnNullBody"
argument_list|,
name|failOnNullBody
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether or not to throw an exception if the input body is null.          * The option will be converted to a<code>boolean</code> type.          * @group producer          */
DECL|method|failOnNullBody (String failOnNullBody)
specifier|default
name|XsltEndpointBuilder
name|failOnNullBody
parameter_list|(
name|String
name|failOnNullBody
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"failOnNullBody"
argument_list|,
name|failOnNullBody
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Option to specify which output type to use. Possible values are:          * string, bytes, DOM, file. The first three options are all in memory          * based, where as file is streamed directly to a java.io.File. For file          * you must specify the filename in the IN header with the key          * Exchange.XSLT_FILE_NAME which is also CamelXsltFileName. Also any          * paths leading to the filename must be created beforehand, otherwise          * an exception is thrown at runtime.          * The option is a          *<code>org.apache.camel.component.xslt.XsltOutput</code> type.          * @group producer          */
DECL|method|output (XsltOutput output)
specifier|default
name|XsltEndpointBuilder
name|output
parameter_list|(
name|XsltOutput
name|output
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"output"
argument_list|,
name|output
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Option to specify which output type to use. Possible values are:          * string, bytes, DOM, file. The first three options are all in memory          * based, where as file is streamed directly to a java.io.File. For file          * you must specify the filename in the IN header with the key          * Exchange.XSLT_FILE_NAME which is also CamelXsltFileName. Also any          * paths leading to the filename must be created beforehand, otherwise          * an exception is thrown at runtime.          * The option will be converted to a          *<code>org.apache.camel.component.xslt.XsltOutput</code> type.          * @group producer          */
DECL|method|output (String output)
specifier|default
name|XsltEndpointBuilder
name|output
parameter_list|(
name|String
name|output
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"output"
argument_list|,
name|output
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether to use Saxon as the transformerFactoryClass. If enabled then          * the class net.sf.saxon.TransformerFactoryImpl. You would need to add          * Saxon to the classpath.          * The option is a<code>boolean</code> type.          * @group producer          */
DECL|method|saxon (boolean saxon)
specifier|default
name|XsltEndpointBuilder
name|saxon
parameter_list|(
name|boolean
name|saxon
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"saxon"
argument_list|,
name|saxon
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether to use Saxon as the transformerFactoryClass. If enabled then          * the class net.sf.saxon.TransformerFactoryImpl. You would need to add          * Saxon to the classpath.          * The option will be converted to a<code>boolean</code> type.          * @group producer          */
DECL|method|saxon (String saxon)
specifier|default
name|XsltEndpointBuilder
name|saxon
parameter_list|(
name|String
name|saxon
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"saxon"
argument_list|,
name|saxon
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The number of javax.xml.transform.Transformer object that are cached          * for reuse to avoid calls to Template.newTransformer().          * The option is a<code>int</code> type.          * @group producer          */
DECL|method|transformerCacheSize ( int transformerCacheSize)
specifier|default
name|XsltEndpointBuilder
name|transformerCacheSize
parameter_list|(
name|int
name|transformerCacheSize
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"transformerCacheSize"
argument_list|,
name|transformerCacheSize
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The number of javax.xml.transform.Transformer object that are cached          * for reuse to avoid calls to Template.newTransformer().          * The option will be converted to a<code>int</code> type.          * @group producer          */
DECL|method|transformerCacheSize ( String transformerCacheSize)
specifier|default
name|XsltEndpointBuilder
name|transformerCacheSize
parameter_list|(
name|String
name|transformerCacheSize
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"transformerCacheSize"
argument_list|,
name|transformerCacheSize
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
comment|/**      * Advanced builder for endpoint for the XSLT component.      */
DECL|interface|AdvancedXsltEndpointBuilder
specifier|public
interface|interface
name|AdvancedXsltEndpointBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|basic ()
specifier|default
name|XsltEndpointBuilder
name|basic
parameter_list|()
block|{
return|return
operator|(
name|XsltEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          * The option is a<code>boolean</code> type.          * @group advanced          */
DECL|method|basicPropertyBinding ( boolean basicPropertyBinding)
specifier|default
name|AdvancedXsltEndpointBuilder
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
name|AdvancedXsltEndpointBuilder
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
comment|/**          * To use a custom org.xml.sax.EntityResolver with          * javax.xml.transform.sax.SAXSource.          * The option is a<code>org.xml.sax.EntityResolver</code> type.          * @group advanced          */
DECL|method|entityResolver (Object entityResolver)
specifier|default
name|AdvancedXsltEndpointBuilder
name|entityResolver
parameter_list|(
name|Object
name|entityResolver
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"entityResolver"
argument_list|,
name|entityResolver
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * To use a custom org.xml.sax.EntityResolver with          * javax.xml.transform.sax.SAXSource.          * The option will be converted to a          *<code>org.xml.sax.EntityResolver</code> type.          * @group advanced          */
DECL|method|entityResolver (String entityResolver)
specifier|default
name|AdvancedXsltEndpointBuilder
name|entityResolver
parameter_list|(
name|String
name|entityResolver
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"entityResolver"
argument_list|,
name|entityResolver
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Allows to configure to use a custom          * javax.xml.transform.ErrorListener. Beware when doing this then the          * default error listener which captures any errors or fatal errors and          * store information on the Exchange as properties is not in use. So          * only use this option for special use-cases.          * The option is a<code>javax.xml.transform.ErrorListener</code> type.          * @group advanced          */
DECL|method|errorListener (Object errorListener)
specifier|default
name|AdvancedXsltEndpointBuilder
name|errorListener
parameter_list|(
name|Object
name|errorListener
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"errorListener"
argument_list|,
name|errorListener
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Allows to configure to use a custom          * javax.xml.transform.ErrorListener. Beware when doing this then the          * default error listener which captures any errors or fatal errors and          * store information on the Exchange as properties is not in use. So          * only use this option for special use-cases.          * The option will be converted to a          *<code>javax.xml.transform.ErrorListener</code> type.          * @group advanced          */
DECL|method|errorListener (String errorListener)
specifier|default
name|AdvancedXsltEndpointBuilder
name|errorListener
parameter_list|(
name|String
name|errorListener
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"errorListener"
argument_list|,
name|errorListener
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Allows you to use a custom          * org.apache.camel.builder.xml.ResultHandlerFactory which is capable of          * using custom org.apache.camel.builder.xml.ResultHandler types.          * The option is a          *<code>org.apache.camel.component.xslt.ResultHandlerFactory</code>          * type.          * @group advanced          */
DECL|method|resultHandlerFactory ( Object resultHandlerFactory)
specifier|default
name|AdvancedXsltEndpointBuilder
name|resultHandlerFactory
parameter_list|(
name|Object
name|resultHandlerFactory
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"resultHandlerFactory"
argument_list|,
name|resultHandlerFactory
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Allows you to use a custom          * org.apache.camel.builder.xml.ResultHandlerFactory which is capable of          * using custom org.apache.camel.builder.xml.ResultHandler types.          * The option will be converted to a          *<code>org.apache.camel.component.xslt.ResultHandlerFactory</code>          * type.          * @group advanced          */
DECL|method|resultHandlerFactory ( String resultHandlerFactory)
specifier|default
name|AdvancedXsltEndpointBuilder
name|resultHandlerFactory
parameter_list|(
name|String
name|resultHandlerFactory
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"resultHandlerFactory"
argument_list|,
name|resultHandlerFactory
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * To use a custom Saxon configuration.          * The option is a<code>java.lang.Object</code> type.          * @group advanced          */
DECL|method|saxonConfiguration ( Object saxonConfiguration)
specifier|default
name|AdvancedXsltEndpointBuilder
name|saxonConfiguration
parameter_list|(
name|Object
name|saxonConfiguration
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"saxonConfiguration"
argument_list|,
name|saxonConfiguration
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * To use a custom Saxon configuration.          * The option will be converted to a<code>java.lang.Object</code> type.          * @group advanced          */
DECL|method|saxonConfiguration ( String saxonConfiguration)
specifier|default
name|AdvancedXsltEndpointBuilder
name|saxonConfiguration
parameter_list|(
name|String
name|saxonConfiguration
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"saxonConfiguration"
argument_list|,
name|saxonConfiguration
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Allows you to use a custom          * net.sf.saxon.lib.ExtensionFunctionDefinition. You would need to add          * camel-saxon to the classpath. The function is looked up in the          * registry, where you can comma to separate multiple values to lookup.          * The option is a<code>java.util.List&lt;java.lang.Object&gt;</code>          * type.          * @group advanced          */
DECL|method|saxonExtensionFunctions ( List<Object> saxonExtensionFunctions)
specifier|default
name|AdvancedXsltEndpointBuilder
name|saxonExtensionFunctions
parameter_list|(
name|List
argument_list|<
name|Object
argument_list|>
name|saxonExtensionFunctions
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"saxonExtensionFunctions"
argument_list|,
name|saxonExtensionFunctions
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Allows you to use a custom          * net.sf.saxon.lib.ExtensionFunctionDefinition. You would need to add          * camel-saxon to the classpath. The function is looked up in the          * registry, where you can comma to separate multiple values to lookup.          * The option will be converted to a          *<code>java.util.List&lt;java.lang.Object&gt;</code> type.          * @group advanced          */
DECL|method|saxonExtensionFunctions ( String saxonExtensionFunctions)
specifier|default
name|AdvancedXsltEndpointBuilder
name|saxonExtensionFunctions
parameter_list|(
name|String
name|saxonExtensionFunctions
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"saxonExtensionFunctions"
argument_list|,
name|saxonExtensionFunctions
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets whether synchronous processing should be strictly used, or Camel          * is allowed to use asynchronous processing (if supported).          * The option is a<code>boolean</code> type.          * @group advanced          */
DECL|method|synchronous (boolean synchronous)
specifier|default
name|AdvancedXsltEndpointBuilder
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
name|AdvancedXsltEndpointBuilder
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
comment|/**          * To use a custom XSLT transformer factory.          * The option is a<code>javax.xml.transform.TransformerFactory</code>          * type.          * @group advanced          */
DECL|method|transformerFactory ( Object transformerFactory)
specifier|default
name|AdvancedXsltEndpointBuilder
name|transformerFactory
parameter_list|(
name|Object
name|transformerFactory
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"transformerFactory"
argument_list|,
name|transformerFactory
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * To use a custom XSLT transformer factory.          * The option will be converted to a          *<code>javax.xml.transform.TransformerFactory</code> type.          * @group advanced          */
DECL|method|transformerFactory ( String transformerFactory)
specifier|default
name|AdvancedXsltEndpointBuilder
name|transformerFactory
parameter_list|(
name|String
name|transformerFactory
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"transformerFactory"
argument_list|,
name|transformerFactory
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * To use a custom XSLT transformer factory, specified as a FQN class          * name.          * The option is a<code>java.lang.String</code> type.          * @group advanced          */
DECL|method|transformerFactoryClass ( String transformerFactoryClass)
specifier|default
name|AdvancedXsltEndpointBuilder
name|transformerFactoryClass
parameter_list|(
name|String
name|transformerFactoryClass
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"transformerFactoryClass"
argument_list|,
name|transformerFactoryClass
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * To use a custom javax.xml.transform.URIResolver.          * The option is a<code>javax.xml.transform.URIResolver</code> type.          * @group advanced          */
DECL|method|uriResolver (Object uriResolver)
specifier|default
name|AdvancedXsltEndpointBuilder
name|uriResolver
parameter_list|(
name|Object
name|uriResolver
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"uriResolver"
argument_list|,
name|uriResolver
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * To use a custom javax.xml.transform.URIResolver.          * The option will be converted to a          *<code>javax.xml.transform.URIResolver</code> type.          * @group advanced          */
DECL|method|uriResolver (String uriResolver)
specifier|default
name|AdvancedXsltEndpointBuilder
name|uriResolver
parameter_list|(
name|String
name|uriResolver
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"uriResolver"
argument_list|,
name|uriResolver
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
comment|/**      * Proxy enum for<code>org.apache.camel.component.xslt.XsltOutput</code>      * enum.      */
DECL|enum|XsltOutput
specifier|static
enum|enum
name|XsltOutput
block|{
DECL|enumConstant|string
DECL|enumConstant|bytes
DECL|enumConstant|DOM
DECL|enumConstant|file
name|string
block|,
name|bytes
block|,
name|DOM
block|,
name|file
block|;     }
comment|/**      * Transforms the message using a XSLT template. Creates a builder to build      * endpoints for the XSLT component.      */
DECL|method|xslt (String path)
specifier|default
name|XsltEndpointBuilder
name|xslt
parameter_list|(
name|String
name|path
parameter_list|)
block|{
class|class
name|XsltEndpointBuilderImpl
extends|extends
name|AbstractEndpointBuilder
implements|implements
name|XsltEndpointBuilder
implements|,
name|AdvancedXsltEndpointBuilder
block|{
specifier|public
name|XsltEndpointBuilderImpl
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|super
argument_list|(
literal|"xslt"
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|new
name|XsltEndpointBuilderImpl
argument_list|(
name|path
argument_list|)
return|;
block|}
block|}
end_interface

end_unit

