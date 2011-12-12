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
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
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
name|TransformerConfigurationException
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
name|api
operator|.
name|management
operator|.
name|ManagedAttribute
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
name|api
operator|.
name|management
operator|.
name|ManagedOperation
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
name|api
operator|.
name|management
operator|.
name|ManagedResource
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
name|impl
operator|.
name|ProcessorEndpoint
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

begin_class
annotation|@
name|ManagedResource
argument_list|(
name|description
operator|=
literal|"XSLT Endpoint"
argument_list|)
DECL|class|XsltEndpoint
specifier|public
class|class
name|XsltEndpoint
extends|extends
name|ProcessorEndpoint
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|XsltEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|xslt
specifier|private
name|XsltBuilder
name|xslt
decl_stmt|;
DECL|field|resourceUri
specifier|private
name|String
name|resourceUri
decl_stmt|;
DECL|field|cacheStylesheet
specifier|private
name|boolean
name|cacheStylesheet
decl_stmt|;
DECL|field|cacheCleared
specifier|private
specifier|volatile
name|boolean
name|cacheCleared
decl_stmt|;
DECL|method|XsltEndpoint (String endpointUri, Component component, XsltBuilder xslt, String resourceUri, boolean cacheStylesheet)
specifier|public
name|XsltEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|Component
name|component
parameter_list|,
name|XsltBuilder
name|xslt
parameter_list|,
name|String
name|resourceUri
parameter_list|,
name|boolean
name|cacheStylesheet
parameter_list|)
throws|throws
name|Exception
block|{
name|super
argument_list|(
name|endpointUri
argument_list|,
name|component
argument_list|,
name|xslt
argument_list|)
expr_stmt|;
name|this
operator|.
name|xslt
operator|=
name|xslt
expr_stmt|;
name|this
operator|.
name|resourceUri
operator|=
name|resourceUri
expr_stmt|;
name|this
operator|.
name|cacheStylesheet
operator|=
name|cacheStylesheet
expr_stmt|;
name|loadResource
argument_list|(
name|xslt
argument_list|,
name|resourceUri
argument_list|)
expr_stmt|;
block|}
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Clears the cached XSLT stylesheet, forcing to re-load the stylesheet on next request"
argument_list|)
DECL|method|clearCachedStylesheet ()
specifier|public
name|void
name|clearCachedStylesheet
parameter_list|()
block|{
name|this
operator|.
name|cacheCleared
operator|=
literal|true
expr_stmt|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Whether the XSLT stylesheet is cached"
argument_list|)
DECL|method|isCacheStylesheet ()
specifier|public
name|boolean
name|isCacheStylesheet
parameter_list|()
block|{
return|return
name|cacheStylesheet
return|;
block|}
DECL|method|loadResource (XsltBuilder xslt, String resourceUri)
specifier|private
specifier|synchronized
name|void
name|loadResource
parameter_list|(
name|XsltBuilder
name|xslt
parameter_list|,
name|String
name|resourceUri
parameter_list|)
throws|throws
name|TransformerConfigurationException
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
comment|// prefer to use URL over InputStream as it loads better with http
name|URL
name|url
init|=
name|ResourceHelper
operator|.
name|resolveMandatoryResourceAsUrl
argument_list|(
name|getCamelContext
argument_list|()
operator|.
name|getClassResolver
argument_list|()
argument_list|,
name|resourceUri
argument_list|)
decl_stmt|;
name|xslt
operator|.
name|setTransformerURL
argument_list|(
name|url
argument_list|)
expr_stmt|;
comment|// now loaded so clear flag
name|cacheCleared
operator|=
literal|false
expr_stmt|;
block|}
DECL|method|findOrCreateEndpoint (String uri, String newResourceUri)
specifier|public
name|XsltEndpoint
name|findOrCreateEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|newResourceUri
parameter_list|)
block|{
name|String
name|newUri
init|=
name|uri
operator|.
name|replace
argument_list|(
name|resourceUri
argument_list|,
name|newResourceUri
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"Getting endpoint with URI: {}"
argument_list|,
name|newUri
argument_list|)
expr_stmt|;
return|return
name|getCamelContext
argument_list|()
operator|.
name|getEndpoint
argument_list|(
name|newUri
argument_list|,
name|XsltEndpoint
operator|.
name|class
argument_list|)
return|;
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
name|XsltConstants
operator|.
name|XSLT_RESOURCE_URI
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|newResourceUri
operator|!=
literal|null
condition|)
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|removeHeader
argument_list|(
name|XsltConstants
operator|.
name|XSLT_RESOURCE_URI
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"{} set to {} creating new endpoint to handle exchange"
argument_list|,
name|XsltConstants
operator|.
name|XSLT_RESOURCE_URI
argument_list|,
name|newResourceUri
argument_list|)
expr_stmt|;
name|XsltEndpoint
name|newEndpoint
init|=
name|findOrCreateEndpoint
argument_list|(
name|getEndpointUri
argument_list|()
argument_list|,
name|newResourceUri
argument_list|)
decl_stmt|;
name|newEndpoint
operator|.
name|onExchange
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
return|return;
block|}
else|else
block|{
if|if
condition|(
operator|!
name|cacheStylesheet
operator|||
name|cacheCleared
condition|)
block|{
name|loadResource
argument_list|(
name|xslt
argument_list|,
name|resourceUri
argument_list|)
expr_stmt|;
block|}
name|super
operator|.
name|onExchange
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

