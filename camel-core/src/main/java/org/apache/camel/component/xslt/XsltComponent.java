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
name|ErrorListener
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
name|builder
operator|.
name|xml
operator|.
name|ResultHandlerFactory
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
name|xml
operator|.
name|XsltBuilder
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
name|xml
operator|.
name|XsltUriResolver
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
name|UriEndpointComponent
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
comment|/**  * An<a href="http://camel.apache.org/xslt.html">XSLT Component</a>  * for performing XSLT transforms of messages  */
end_comment

begin_class
DECL|class|XsltComponent
specifier|public
class|class
name|XsltComponent
extends|extends
name|UriEndpointComponent
block|{
DECL|field|SAXON_TRANSFORMER_FACTORY_CLASS_NAME
specifier|private
specifier|static
specifier|final
name|String
name|SAXON_TRANSFORMER_FACTORY_CLASS_NAME
init|=
literal|"net.sf.saxon.TransformerFactoryImpl"
decl_stmt|;
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|XsltComponent
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|xmlConverter
specifier|private
name|XmlConverter
name|xmlConverter
decl_stmt|;
DECL|field|uriResolver
specifier|private
name|URIResolver
name|uriResolver
decl_stmt|;
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
block|{
name|super
argument_list|(
name|XsltEndpoint
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
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
name|String
name|resourceUri
init|=
name|remaining
decl_stmt|;
name|LOG
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
specifier|final
name|XsltBuilder
name|xslt
init|=
name|getCamelContext
argument_list|()
operator|.
name|getInjector
argument_list|()
operator|.
name|newInstance
argument_list|(
name|XsltBuilder
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// lets allow the converter to be configured
name|XmlConverter
name|converter
init|=
name|resolveAndRemoveReferenceParameter
argument_list|(
name|parameters
argument_list|,
literal|"converter"
argument_list|,
name|XmlConverter
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|converter
operator|==
literal|null
condition|)
block|{
name|converter
operator|=
name|getXmlConverter
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|converter
operator|!=
literal|null
condition|)
block|{
name|xslt
operator|.
name|setConverter
argument_list|(
name|converter
argument_list|)
expr_stmt|;
block|}
name|String
name|transformerFactoryClassName
init|=
name|getAndRemoveParameter
argument_list|(
name|parameters
argument_list|,
literal|"transformerFactoryClass"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|Boolean
name|saxon
init|=
name|getAndRemoveParameter
argument_list|(
name|parameters
argument_list|,
literal|"saxon"
argument_list|,
name|Boolean
operator|.
name|class
argument_list|,
name|isSaxon
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|transformerFactoryClassName
operator|==
literal|null
operator|&&
name|saxon
condition|)
block|{
name|transformerFactoryClassName
operator|=
name|SAXON_TRANSFORMER_FACTORY_CLASS_NAME
expr_stmt|;
block|}
name|TransformerFactory
name|factory
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|transformerFactoryClassName
operator|!=
literal|null
condition|)
block|{
comment|// provide the class loader of this component to work in OSGi environments
name|Class
argument_list|<
name|?
argument_list|>
name|factoryClass
init|=
name|getCamelContext
argument_list|()
operator|.
name|getClassResolver
argument_list|()
operator|.
name|resolveMandatoryClass
argument_list|(
name|transformerFactoryClassName
argument_list|,
name|XsltComponent
operator|.
name|class
operator|.
name|getClassLoader
argument_list|()
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Using TransformerFactoryClass {}"
argument_list|,
name|factoryClass
argument_list|)
expr_stmt|;
name|factory
operator|=
operator|(
name|TransformerFactory
operator|)
name|getCamelContext
argument_list|()
operator|.
name|getInjector
argument_list|()
operator|.
name|newInstance
argument_list|(
name|factoryClass
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|parameters
operator|.
name|get
argument_list|(
literal|"transformerFactory"
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|factory
operator|=
name|resolveAndRemoveReferenceParameter
argument_list|(
name|parameters
argument_list|,
literal|"transformerFactory"
argument_list|,
name|TransformerFactory
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|factory
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Using TransformerFactory {}"
argument_list|,
name|factory
argument_list|)
expr_stmt|;
name|xslt
operator|.
name|getConverter
argument_list|()
operator|.
name|setTransformerFactory
argument_list|(
name|factory
argument_list|)
expr_stmt|;
block|}
name|ResultHandlerFactory
name|resultHandlerFactory
init|=
name|resolveAndRemoveReferenceParameter
argument_list|(
name|parameters
argument_list|,
literal|"resultHandlerFactory"
argument_list|,
name|ResultHandlerFactory
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|resultHandlerFactory
operator|!=
literal|null
condition|)
block|{
name|xslt
operator|.
name|setResultHandlerFactory
argument_list|(
name|resultHandlerFactory
argument_list|)
expr_stmt|;
block|}
name|Boolean
name|failOnNullBody
init|=
name|getAndRemoveParameter
argument_list|(
name|parameters
argument_list|,
literal|"failOnNullBody"
argument_list|,
name|Boolean
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|failOnNullBody
operator|!=
literal|null
condition|)
block|{
name|xslt
operator|.
name|setFailOnNullBody
argument_list|(
name|failOnNullBody
argument_list|)
expr_stmt|;
block|}
name|String
name|output
init|=
name|getAndRemoveParameter
argument_list|(
name|parameters
argument_list|,
literal|"output"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|configureOutput
argument_list|(
name|xslt
argument_list|,
name|output
argument_list|)
expr_stmt|;
name|Integer
name|cs
init|=
name|getAndRemoveParameter
argument_list|(
name|parameters
argument_list|,
literal|"transformerCacheSize"
argument_list|,
name|Integer
operator|.
name|class
argument_list|,
literal|0
argument_list|)
decl_stmt|;
name|xslt
operator|.
name|transformerCacheSize
argument_list|(
name|cs
argument_list|)
expr_stmt|;
name|ErrorListener
name|errorListener
init|=
name|resolveAndRemoveReferenceParameter
argument_list|(
name|parameters
argument_list|,
literal|"errorListener"
argument_list|,
name|ErrorListener
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|errorListener
operator|!=
literal|null
condition|)
block|{
name|xslt
operator|.
name|errorListener
argument_list|(
name|errorListener
argument_list|)
expr_stmt|;
block|}
comment|// default to use the cache option from the component if the endpoint did not have the contentCache parameter
name|boolean
name|cache
init|=
name|getAndRemoveParameter
argument_list|(
name|parameters
argument_list|,
literal|"contentCache"
argument_list|,
name|Boolean
operator|.
name|class
argument_list|,
name|contentCache
argument_list|)
decl_stmt|;
comment|// if its a http uri, then append additional parameters as they are part of the uri
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
comment|// fallback to use a Camel specific resolver
name|resolver
operator|=
operator|new
name|XsltUriResolver
argument_list|(
name|getCamelContext
argument_list|()
operator|.
name|getClassResolver
argument_list|()
argument_list|,
name|remaining
argument_list|)
expr_stmt|;
block|}
comment|// set resolver before input stream as resolver is used when loading the input stream
name|xslt
operator|.
name|setUriResolver
argument_list|(
name|resolver
argument_list|)
expr_stmt|;
name|configureXslt
argument_list|(
name|xslt
argument_list|,
name|uri
argument_list|,
name|remaining
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
return|return
operator|new
name|XsltEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|,
name|xslt
argument_list|,
name|resourceUri
argument_list|,
name|cache
argument_list|)
return|;
block|}
DECL|method|configureXslt (XsltBuilder xslt, String uri, String remaining, Map<String, Object> parameters)
specifier|protected
name|void
name|configureXslt
parameter_list|(
name|XsltBuilder
name|xslt
parameter_list|,
name|String
name|uri
parameter_list|,
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
name|setProperties
argument_list|(
name|xslt
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
block|}
DECL|method|configureOutput (XsltBuilder xslt, String output)
specifier|protected
name|void
name|configureOutput
parameter_list|(
name|XsltBuilder
name|xslt
parameter_list|,
name|String
name|output
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|output
argument_list|)
condition|)
block|{
return|return;
block|}
if|if
condition|(
literal|"string"
operator|.
name|equalsIgnoreCase
argument_list|(
name|output
argument_list|)
condition|)
block|{
name|xslt
operator|.
name|outputString
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"bytes"
operator|.
name|equalsIgnoreCase
argument_list|(
name|output
argument_list|)
condition|)
block|{
name|xslt
operator|.
name|outputBytes
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"DOM"
operator|.
name|equalsIgnoreCase
argument_list|(
name|output
argument_list|)
condition|)
block|{
name|xslt
operator|.
name|outputDOM
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"file"
operator|.
name|equalsIgnoreCase
argument_list|(
name|output
argument_list|)
condition|)
block|{
name|xslt
operator|.
name|outputFile
argument_list|()
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unknown output type: "
operator|+
name|output
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

