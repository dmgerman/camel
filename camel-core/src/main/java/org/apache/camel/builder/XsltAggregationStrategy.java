begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.builder
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|RejectedExecutionException
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
name|Source
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
name|TransformerException
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
name|w3c
operator|.
name|dom
operator|.
name|Document
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
name|AggregationStrategy
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
name|CamelContext
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
name|CamelContextAware
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
name|Exchange
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
name|component
operator|.
name|xslt
operator|.
name|XsltEndpoint
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
name|XsltOutput
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
name|support
operator|.
name|ServiceSupport
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
comment|/**  * The XSLT Aggregation Strategy enables you to use XSL stylesheets to aggregate messages.  *<p>  * Since XSLT does not directly support providing multiple XML payloads as an input, this aggregator injects  * the new incoming XML document (<tt>newExchange</tt>) into the<tt>oldExchange</tt> as an exchange property of  * type {@link Document}. The old exchange therefore remains accessible as the root context.  * This exchange property can then be accessed from your XSLT by declaring an {@code<xsl:param />} at the top  * of your stylesheet:  *  *<code>  *<xsl:param name="new-exchange" />  *  *<xsl:template match="/">  *<abc>  *<xsl:copy-of select="/ElementFromOldExchange" />  *<xsl:copy-of select="$new-exchange/ElementFromNewExchange" />  *</abc>  *</xsl:template>  *</code>  *  * The exchange property name defaults to<tt>new-exchange</tt> but can be  * changed through {@link #setPropertyName(String)}.  *<p>  * Some code bits have been copied from the {@link org.apache.camel.component.xslt.XsltEndpoint}.  */
end_comment

begin_class
DECL|class|XsltAggregationStrategy
specifier|public
class|class
name|XsltAggregationStrategy
extends|extends
name|ServiceSupport
implements|implements
name|AggregationStrategy
implements|,
name|CamelContextAware
block|{
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
name|XsltAggregationStrategy
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|DEFAULT_PROPERTY_NAME
specifier|private
specifier|static
specifier|final
name|String
name|DEFAULT_PROPERTY_NAME
init|=
literal|"new-exchange"
decl_stmt|;
DECL|field|xslt
specifier|private
specifier|volatile
name|XsltBuilder
name|xslt
decl_stmt|;
DECL|field|uriResolver
specifier|private
specifier|volatile
name|URIResolver
name|uriResolver
decl_stmt|;
DECL|field|camelContext
specifier|private
name|CamelContext
name|camelContext
decl_stmt|;
DECL|field|propertyName
specifier|private
name|String
name|propertyName
decl_stmt|;
DECL|field|xslFile
specifier|private
name|String
name|xslFile
decl_stmt|;
DECL|field|transformerFactoryClass
specifier|private
name|String
name|transformerFactoryClass
decl_stmt|;
DECL|field|output
specifier|private
name|XsltOutput
name|output
init|=
name|XsltOutput
operator|.
name|string
decl_stmt|;
comment|/**      * Constructor.      *      * @param xslFileLocation location of the XSL transformation      */
DECL|method|XsltAggregationStrategy (String xslFileLocation)
specifier|public
name|XsltAggregationStrategy
parameter_list|(
name|String
name|xslFileLocation
parameter_list|)
block|{
name|this
operator|.
name|xslFile
operator|=
name|xslFileLocation
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getCamelContext ()
specifier|public
name|CamelContext
name|getCamelContext
parameter_list|()
block|{
return|return
name|camelContext
return|;
block|}
annotation|@
name|Override
DECL|method|setCamelContext (CamelContext camelContext)
specifier|public
name|void
name|setCamelContext
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|this
operator|.
name|camelContext
operator|=
name|camelContext
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|aggregate (Exchange oldExchange, Exchange newExchange)
specifier|public
name|Exchange
name|aggregate
parameter_list|(
name|Exchange
name|oldExchange
parameter_list|,
name|Exchange
name|newExchange
parameter_list|)
block|{
comment|// guard against unlikely NPE
if|if
condition|(
name|newExchange
operator|==
literal|null
condition|)
block|{
return|return
name|oldExchange
return|;
block|}
comment|// in the first call to this aggregation, do not call the XSLT but instead store the
comment|// incoming exchange
if|if
condition|(
name|oldExchange
operator|==
literal|null
condition|)
block|{
return|return
name|newExchange
return|;
block|}
if|if
condition|(
operator|!
name|isRunAllowed
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|RejectedExecutionException
argument_list|()
throw|;
block|}
try|try
block|{
name|oldExchange
operator|.
name|setProperty
argument_list|(
name|propertyName
argument_list|,
name|newExchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|Document
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|xslt
operator|.
name|process
argument_list|(
name|oldExchange
argument_list|)
expr_stmt|;
return|return
name|oldExchange
return|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
name|oldExchange
operator|.
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
return|return
name|oldExchange
return|;
block|}
DECL|method|setOutput (XsltOutput output)
specifier|public
name|void
name|setOutput
parameter_list|(
name|XsltOutput
name|output
parameter_list|)
block|{
name|this
operator|.
name|output
operator|=
name|output
expr_stmt|;
block|}
DECL|method|setXslt (XsltBuilder xslt)
specifier|public
name|void
name|setXslt
parameter_list|(
name|XsltBuilder
name|xslt
parameter_list|)
block|{
name|this
operator|.
name|xslt
operator|=
name|xslt
expr_stmt|;
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
DECL|method|getPropertyName ()
specifier|public
name|String
name|getPropertyName
parameter_list|()
block|{
return|return
name|propertyName
return|;
block|}
DECL|method|setPropertyName (String propertyName)
specifier|public
name|void
name|setPropertyName
parameter_list|(
name|String
name|propertyName
parameter_list|)
block|{
name|this
operator|.
name|propertyName
operator|=
name|propertyName
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
comment|/**      * Loads the resource.      *      * @param resourceUri the resource to load      * @throws TransformerException is thrown if error loading resource      * @throws IOException          is thrown if error loading resource      */
DECL|method|loadResource (String resourceUri)
specifier|protected
name|void
name|loadResource
parameter_list|(
name|String
name|resourceUri
parameter_list|)
throws|throws
name|TransformerException
throws|,
name|IOException
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"{} loading schema resource: {}"
argument_list|,
name|this
argument_list|,
name|resourceUri
argument_list|)
expr_stmt|;
name|Source
name|source
init|=
name|xslt
operator|.
name|getUriResolver
argument_list|()
operator|.
name|resolve
argument_list|(
name|resourceUri
argument_list|,
literal|null
argument_list|)
decl_stmt|;
if|if
condition|(
name|source
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Cannot load schema resource "
operator|+
name|resourceUri
argument_list|)
throw|;
block|}
else|else
block|{
name|xslt
operator|.
name|setTransformerSource
argument_list|(
name|source
argument_list|)
expr_stmt|;
block|}
block|}
comment|// --- fluent builders ---
DECL|method|create (String xslFile)
specifier|public
specifier|static
name|XsltAggregationStrategy
name|create
parameter_list|(
name|String
name|xslFile
parameter_list|)
block|{
return|return
operator|new
name|XsltAggregationStrategy
argument_list|(
name|xslFile
argument_list|)
return|;
block|}
DECL|method|withPropertyName (String propertyName)
specifier|public
name|XsltAggregationStrategy
name|withPropertyName
parameter_list|(
name|String
name|propertyName
parameter_list|)
block|{
name|setPropertyName
argument_list|(
name|propertyName
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|withOutput (XsltOutput output)
specifier|public
name|XsltAggregationStrategy
name|withOutput
parameter_list|(
name|XsltOutput
name|output
parameter_list|)
block|{
name|setOutput
argument_list|(
name|output
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|withUriResolver (URIResolver resolver)
specifier|public
name|XsltAggregationStrategy
name|withUriResolver
parameter_list|(
name|URIResolver
name|resolver
parameter_list|)
block|{
name|setUriResolver
argument_list|(
name|resolver
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|withTransformerFactoryClass (String clazz)
specifier|public
name|XsltAggregationStrategy
name|withTransformerFactoryClass
parameter_list|(
name|String
name|clazz
parameter_list|)
block|{
name|setTransformerFactoryClass
argument_list|(
name|clazz
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|withSaxon ()
specifier|public
name|XsltAggregationStrategy
name|withSaxon
parameter_list|()
block|{
name|setTransformerFactoryClass
argument_list|(
name|XsltEndpoint
operator|.
name|SAXON_TRANSFORMER_FACTORY_CLASS_NAME
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|camelContext
argument_list|,
literal|"CamelContext"
argument_list|,
name|this
argument_list|)
expr_stmt|;
comment|// set the default property name if not set
name|this
operator|.
name|propertyName
operator|=
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|propertyName
argument_list|)
condition|?
name|propertyName
else|:
name|DEFAULT_PROPERTY_NAME
expr_stmt|;
comment|// initialize the XsltBuilder
name|this
operator|.
name|xslt
operator|=
name|camelContext
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
expr_stmt|;
if|if
condition|(
name|transformerFactoryClass
operator|!=
literal|null
condition|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|factoryClass
init|=
name|camelContext
operator|.
name|getClassResolver
argument_list|()
operator|.
name|resolveMandatoryClass
argument_list|(
name|transformerFactoryClass
argument_list|,
name|XsltAggregationStrategy
operator|.
name|class
operator|.
name|getClassLoader
argument_list|()
argument_list|)
decl_stmt|;
name|TransformerFactory
name|factory
init|=
operator|(
name|TransformerFactory
operator|)
name|camelContext
operator|.
name|getInjector
argument_list|()
operator|.
name|newInstance
argument_list|(
name|factoryClass
argument_list|)
decl_stmt|;
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
if|if
condition|(
name|uriResolver
operator|==
literal|null
condition|)
block|{
name|uriResolver
operator|=
operator|new
name|XsltUriResolver
argument_list|(
name|camelContext
argument_list|,
name|xslFile
argument_list|)
expr_stmt|;
block|}
name|xslt
operator|.
name|setUriResolver
argument_list|(
name|uriResolver
argument_list|)
expr_stmt|;
name|xslt
operator|.
name|setFailOnNullBody
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|xslt
operator|.
name|transformerCacheSize
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|xslt
operator|.
name|setAllowStAX
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|configureOutput
argument_list|(
name|xslt
argument_list|,
name|output
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
name|loadResource
argument_list|(
name|xslFile
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
comment|// noop
block|}
block|}
end_class

end_unit

