begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.mustache
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|mustache
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
name|io
operator|.
name|InputStreamReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Reader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|StringReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|StringWriter
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
name|com
operator|.
name|github
operator|.
name|mustachejava
operator|.
name|DefaultMustacheFactory
import|;
end_import

begin_import
import|import
name|com
operator|.
name|github
operator|.
name|mustachejava
operator|.
name|Mustache
import|;
end_import

begin_import
import|import
name|com
operator|.
name|github
operator|.
name|mustachejava
operator|.
name|MustacheFactory
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
name|Component
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
name|ExchangePattern
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
name|Message
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
name|ResourceEndpoint
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
name|UriEndpoint
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
name|UriParam
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
name|ExchangeHelper
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|mustache
operator|.
name|MustacheConstants
operator|.
name|MUSTACHE_ENDPOINT_URI_PREFIX
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|mustache
operator|.
name|MustacheConstants
operator|.
name|MUSTACHE_RESOURCE_URI
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|mustache
operator|.
name|MustacheConstants
operator|.
name|MUSTACHE_TEMPLATE
import|;
end_import

begin_comment
comment|/**  * Transforms the message using a Mustache template.  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|firstVersion
operator|=
literal|"2.12.0"
argument_list|,
name|scheme
operator|=
literal|"mustache"
argument_list|,
name|title
operator|=
literal|"Mustache"
argument_list|,
name|syntax
operator|=
literal|"mustache:resourceUri"
argument_list|,
name|producerOnly
operator|=
literal|true
argument_list|,
name|label
operator|=
literal|"transformation"
argument_list|)
DECL|class|MustacheEndpoint
specifier|public
class|class
name|MustacheEndpoint
extends|extends
name|ResourceEndpoint
block|{
DECL|field|mustacheFactory
specifier|private
name|MustacheFactory
name|mustacheFactory
decl_stmt|;
DECL|field|mustache
specifier|private
name|Mustache
name|mustache
decl_stmt|;
annotation|@
name|UriParam
DECL|field|encoding
specifier|private
name|String
name|encoding
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"{{"
argument_list|)
DECL|field|startDelimiter
specifier|private
name|String
name|startDelimiter
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"}}"
argument_list|)
DECL|field|endDelimiter
specifier|private
name|String
name|endDelimiter
decl_stmt|;
DECL|method|MustacheEndpoint ()
specifier|public
name|MustacheEndpoint
parameter_list|()
block|{     }
DECL|method|MustacheEndpoint (String endpointUri, Component component, String resourceUri)
specifier|public
name|MustacheEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|Component
name|component
parameter_list|,
name|String
name|resourceUri
parameter_list|)
block|{
name|super
argument_list|(
name|endpointUri
argument_list|,
name|component
argument_list|,
name|resourceUri
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getExchangePattern ()
specifier|public
name|ExchangePattern
name|getExchangePattern
parameter_list|()
block|{
return|return
name|ExchangePattern
operator|.
name|InOut
return|;
block|}
annotation|@
name|Override
DECL|method|createEndpointUri ()
specifier|protected
name|String
name|createEndpointUri
parameter_list|()
block|{
return|return
name|MUSTACHE_ENDPOINT_URI_PREFIX
operator|+
name|getResourceUri
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|clearContentCache ()
specifier|public
name|void
name|clearContentCache
parameter_list|()
block|{
name|this
operator|.
name|mustache
operator|=
literal|null
expr_stmt|;
name|super
operator|.
name|clearContentCache
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onExchange (Exchange exchange)
specifier|protected
name|void
name|onExchange
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|newResourceUri
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|MUSTACHE_RESOURCE_URI
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|newResourceUri
operator|==
literal|null
condition|)
block|{
comment|// Get Mustache
name|String
name|newTemplate
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|MUSTACHE_TEMPLATE
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|Mustache
name|newMustache
decl_stmt|;
if|if
condition|(
name|newTemplate
operator|==
literal|null
condition|)
block|{
name|newMustache
operator|=
name|getOrCreateMustache
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|newMustache
operator|=
name|createMustache
argument_list|(
operator|new
name|StringReader
argument_list|(
name|newTemplate
argument_list|)
argument_list|,
literal|"mustache:temp#"
operator|+
name|newTemplate
operator|.
name|hashCode
argument_list|()
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|removeHeader
argument_list|(
name|MUSTACHE_TEMPLATE
argument_list|)
expr_stmt|;
block|}
comment|// Execute Mustache
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|variableMap
init|=
name|ExchangeHelper
operator|.
name|createVariableMap
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|StringWriter
name|writer
init|=
operator|new
name|StringWriter
argument_list|()
decl_stmt|;
name|newMustache
operator|.
name|execute
argument_list|(
name|writer
argument_list|,
name|variableMap
argument_list|)
expr_stmt|;
name|writer
operator|.
name|flush
argument_list|()
expr_stmt|;
comment|// Fill out message
name|Message
name|out
init|=
name|exchange
operator|.
name|getOut
argument_list|()
decl_stmt|;
name|out
operator|.
name|setBody
argument_list|(
name|writer
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|out
operator|.
name|setHeaders
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeaders
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|removeHeader
argument_list|(
name|MustacheConstants
operator|.
name|MUSTACHE_RESOURCE_URI
argument_list|)
expr_stmt|;
name|MustacheEndpoint
name|newEndpoint
init|=
name|getCamelContext
argument_list|()
operator|.
name|getEndpoint
argument_list|(
name|MUSTACHE_ENDPOINT_URI_PREFIX
operator|+
name|newResourceUri
argument_list|,
name|MustacheEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|newEndpoint
operator|.
name|onExchange
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Read and compile a Mustache template      *      * @param resourceReader Reader used to get template      * @param resourceUri    Template Id      * @return Template      */
DECL|method|createMustache (Reader resourceReader, String resourceUri)
specifier|private
name|Mustache
name|createMustache
parameter_list|(
name|Reader
name|resourceReader
parameter_list|,
name|String
name|resourceUri
parameter_list|)
throws|throws
name|IOException
block|{
name|ClassLoader
name|oldcl
init|=
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
decl_stmt|;
try|try
block|{
name|ClassLoader
name|apcl
init|=
name|getCamelContext
argument_list|()
operator|.
name|getApplicationContextClassLoader
argument_list|()
decl_stmt|;
if|if
condition|(
name|apcl
operator|!=
literal|null
condition|)
block|{
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|setContextClassLoader
argument_list|(
name|apcl
argument_list|)
expr_stmt|;
block|}
name|Mustache
name|newMustache
decl_stmt|;
if|if
condition|(
name|startDelimiter
operator|!=
literal|null
operator|&&
name|endDelimiter
operator|!=
literal|null
operator|&&
name|mustacheFactory
operator|instanceof
name|DefaultMustacheFactory
condition|)
block|{
name|DefaultMustacheFactory
name|defaultMustacheFactory
init|=
operator|(
name|DefaultMustacheFactory
operator|)
name|mustacheFactory
decl_stmt|;
name|newMustache
operator|=
name|defaultMustacheFactory
operator|.
name|compile
argument_list|(
name|resourceReader
argument_list|,
name|resourceUri
argument_list|,
name|startDelimiter
argument_list|,
name|endDelimiter
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|newMustache
operator|=
name|mustacheFactory
operator|.
name|compile
argument_list|(
name|resourceReader
argument_list|,
name|resourceUri
argument_list|)
expr_stmt|;
block|}
return|return
name|newMustache
return|;
block|}
finally|finally
block|{
name|resourceReader
operator|.
name|close
argument_list|()
expr_stmt|;
if|if
condition|(
name|oldcl
operator|!=
literal|null
condition|)
block|{
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|setContextClassLoader
argument_list|(
name|oldcl
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|getOrCreateMustache ()
specifier|private
name|Mustache
name|getOrCreateMustache
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
name|mustache
operator|==
literal|null
condition|)
block|{
name|mustache
operator|=
name|createMustache
argument_list|(
name|getResourceAsReader
argument_list|()
argument_list|,
name|getResourceUri
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|mustache
return|;
block|}
annotation|@
name|Override
DECL|method|getResourceUri ()
specifier|public
name|String
name|getResourceUri
parameter_list|()
block|{
comment|// do not have leading slash as mustache cannot find the resource, as that entails classpath root
name|String
name|uri
init|=
name|super
operator|.
name|getResourceUri
argument_list|()
decl_stmt|;
if|if
condition|(
name|uri
operator|!=
literal|null
operator|&&
operator|(
name|uri
operator|.
name|startsWith
argument_list|(
literal|"/"
argument_list|)
operator|||
name|uri
operator|.
name|startsWith
argument_list|(
literal|"\\"
argument_list|)
operator|)
condition|)
block|{
return|return
name|uri
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|uri
return|;
block|}
block|}
DECL|method|getMustacheFactory ()
specifier|public
name|MustacheFactory
name|getMustacheFactory
parameter_list|()
block|{
return|return
name|mustacheFactory
return|;
block|}
comment|/**      * To use a custom {@link MustacheFactory}      */
DECL|method|setMustacheFactory (MustacheFactory mustacheFactory)
specifier|public
name|void
name|setMustacheFactory
parameter_list|(
name|MustacheFactory
name|mustacheFactory
parameter_list|)
block|{
name|this
operator|.
name|mustacheFactory
operator|=
name|mustacheFactory
expr_stmt|;
block|}
DECL|method|getEncoding ()
specifier|public
name|String
name|getEncoding
parameter_list|()
block|{
return|return
name|encoding
return|;
block|}
comment|/**      * Character encoding of the resource content.      */
DECL|method|setEncoding (String encoding)
specifier|public
name|void
name|setEncoding
parameter_list|(
name|String
name|encoding
parameter_list|)
block|{
name|this
operator|.
name|encoding
operator|=
name|encoding
expr_stmt|;
block|}
DECL|method|getResourceAsReader ()
specifier|private
name|Reader
name|getResourceAsReader
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|encoding
operator|==
literal|null
condition|?
operator|new
name|InputStreamReader
argument_list|(
name|getResourceAsInputStream
argument_list|()
argument_list|)
else|:
operator|new
name|InputStreamReader
argument_list|(
name|getResourceAsInputStream
argument_list|()
argument_list|,
name|encoding
argument_list|)
return|;
block|}
DECL|method|getStartDelimiter ()
specifier|public
name|String
name|getStartDelimiter
parameter_list|()
block|{
return|return
name|startDelimiter
return|;
block|}
comment|/**      * Characters used to mark template code beginning.      */
DECL|method|setStartDelimiter (String startDelimiter)
specifier|public
name|void
name|setStartDelimiter
parameter_list|(
name|String
name|startDelimiter
parameter_list|)
block|{
name|this
operator|.
name|startDelimiter
operator|=
name|startDelimiter
expr_stmt|;
block|}
DECL|method|getEndDelimiter ()
specifier|public
name|String
name|getEndDelimiter
parameter_list|()
block|{
return|return
name|endDelimiter
return|;
block|}
comment|/**      * Characters used to mark template code end.      */
DECL|method|setEndDelimiter (String endDelimiter)
specifier|public
name|void
name|setEndDelimiter
parameter_list|(
name|String
name|endDelimiter
parameter_list|)
block|{
name|this
operator|.
name|endDelimiter
operator|=
name|endDelimiter
expr_stmt|;
block|}
block|}
end_class

end_unit

