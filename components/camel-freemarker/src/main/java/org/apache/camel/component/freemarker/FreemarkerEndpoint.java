begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.freemarker
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|freemarker
package|;
end_package

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
name|freemarker
operator|.
name|template
operator|.
name|Configuration
import|;
end_import

begin_import
import|import
name|freemarker
operator|.
name|template
operator|.
name|Template
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
name|ResourceBasedEndpoint
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
name|ExchangeHelper
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

begin_comment
comment|/**  * Freemarker endpoint  */
end_comment

begin_class
DECL|class|FreemarkerEndpoint
specifier|public
class|class
name|FreemarkerEndpoint
extends|extends
name|ResourceBasedEndpoint
block|{
DECL|field|encoding
specifier|private
name|String
name|encoding
decl_stmt|;
DECL|field|configuration
specifier|private
name|Configuration
name|configuration
decl_stmt|;
DECL|method|FreemarkerEndpoint ()
specifier|public
name|FreemarkerEndpoint
parameter_list|()
block|{     }
DECL|method|FreemarkerEndpoint (String uri, Component component, String resourceUri)
specifier|public
name|FreemarkerEndpoint
parameter_list|(
name|String
name|uri
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
name|uri
argument_list|,
name|component
argument_list|,
name|resourceUri
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
literal|true
return|;
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
literal|"freemarker:"
operator|+
name|getResourceUri
argument_list|()
return|;
block|}
comment|/**      * Sets the encoding to be used for loading the template file.      */
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
DECL|method|getConfiguration ()
specifier|public
name|Configuration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
comment|/**      * Sets the Freemarker configuration to use      */
DECL|method|setConfiguration (Configuration configuration)
specifier|public
name|void
name|setConfiguration
parameter_list|(
name|Configuration
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
DECL|method|findOrCreateEndpoint (String uri, String newResourceUri)
specifier|public
name|FreemarkerEndpoint
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
name|getResourceUri
argument_list|()
argument_list|,
name|newResourceUri
argument_list|)
decl_stmt|;
if|if
condition|(
name|log
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Getting endpoint with URI: "
operator|+
name|newUri
argument_list|)
expr_stmt|;
block|}
return|return
operator|(
name|FreemarkerEndpoint
operator|)
name|getCamelContext
argument_list|()
operator|.
name|getEndpoint
argument_list|(
name|newUri
argument_list|)
return|;
block|}
annotation|@
name|Override
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
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
name|path
init|=
name|getResourceUri
argument_list|()
decl_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|configuration
argument_list|,
literal|"configuration"
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|path
argument_list|,
literal|"resourceUri"
argument_list|)
expr_stmt|;
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
name|FreemarkerConstants
operator|.
name|FREEMARKER_RESOURCE_URI
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
name|FreemarkerConstants
operator|.
name|FREEMARKER_RESOURCE_URI
argument_list|)
expr_stmt|;
if|if
condition|(
name|log
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
name|FreemarkerConstants
operator|.
name|FREEMARKER_RESOURCE_URI
operator|+
literal|" set to "
operator|+
name|newResourceUri
operator|+
literal|" creating new endpoint to handle exchange"
argument_list|)
expr_stmt|;
block|}
name|FreemarkerEndpoint
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
name|Reader
name|reader
init|=
literal|null
decl_stmt|;
name|String
name|content
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|FreemarkerConstants
operator|.
name|FREEMARKER_TEMPLATE
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|content
operator|!=
literal|null
condition|)
block|{
comment|// use content from header
name|reader
operator|=
operator|new
name|StringReader
argument_list|(
name|content
argument_list|)
expr_stmt|;
comment|// remove the header to avoid it being propagated in the routing
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|removeHeader
argument_list|(
name|FreemarkerConstants
operator|.
name|FREEMARKER_TEMPLATE
argument_list|)
expr_stmt|;
block|}
name|Map
name|variableMap
init|=
name|ExchangeHelper
operator|.
name|createVariableMap
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
comment|// let freemarker parse and generate the result in buffer
name|Template
name|template
decl_stmt|;
if|if
condition|(
name|reader
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|log
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Freemarker is evaluating template read from header "
operator|+
name|FreemarkerConstants
operator|.
name|FREEMARKER_TEMPLATE
operator|+
literal|" using context: "
operator|+
name|variableMap
argument_list|)
expr_stmt|;
block|}
name|template
operator|=
operator|new
name|Template
argument_list|(
literal|"temp"
argument_list|,
name|reader
argument_list|,
operator|new
name|Configuration
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|log
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Freemarker is evaluating "
operator|+
name|path
operator|+
literal|" using context: "
operator|+
name|variableMap
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|getEncoding
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|template
operator|=
name|configuration
operator|.
name|getTemplate
argument_list|(
name|path
argument_list|,
name|getEncoding
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|template
operator|=
name|configuration
operator|.
name|getTemplate
argument_list|(
name|path
argument_list|)
expr_stmt|;
block|}
block|}
name|StringWriter
name|buffer
init|=
operator|new
name|StringWriter
argument_list|()
decl_stmt|;
name|template
operator|.
name|process
argument_list|(
name|variableMap
argument_list|,
name|buffer
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|flush
argument_list|()
expr_stmt|;
comment|// now lets output the results to the exchange
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
name|buffer
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
init|=
operator|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
operator|)
name|variableMap
operator|.
name|get
argument_list|(
literal|"headers"
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|key
range|:
name|headers
operator|.
name|keySet
argument_list|()
control|)
block|{
name|out
operator|.
name|setHeader
argument_list|(
name|key
argument_list|,
name|headers
operator|.
name|get
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

