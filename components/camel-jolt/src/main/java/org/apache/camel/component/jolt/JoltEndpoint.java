begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jolt
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jolt
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
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
name|bazaarvoice
operator|.
name|jolt
operator|.
name|Chainr
import|;
end_import

begin_import
import|import
name|com
operator|.
name|bazaarvoice
operator|.
name|jolt
operator|.
name|ContextualTransform
import|;
end_import

begin_import
import|import
name|com
operator|.
name|bazaarvoice
operator|.
name|jolt
operator|.
name|Defaultr
import|;
end_import

begin_import
import|import
name|com
operator|.
name|bazaarvoice
operator|.
name|jolt
operator|.
name|JoltTransform
import|;
end_import

begin_import
import|import
name|com
operator|.
name|bazaarvoice
operator|.
name|jolt
operator|.
name|JsonUtils
import|;
end_import

begin_import
import|import
name|com
operator|.
name|bazaarvoice
operator|.
name|jolt
operator|.
name|Removr
import|;
end_import

begin_import
import|import
name|com
operator|.
name|bazaarvoice
operator|.
name|jolt
operator|.
name|Shiftr
import|;
end_import

begin_import
import|import
name|com
operator|.
name|bazaarvoice
operator|.
name|jolt
operator|.
name|Sortr
import|;
end_import

begin_import
import|import
name|com
operator|.
name|bazaarvoice
operator|.
name|jolt
operator|.
name|Transform
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
name|util
operator|.
name|ObjectHelper
import|;
end_import

begin_comment
comment|/**  * The jolt component allows you to process a JSON messages using an JOLT specification (such as JSON-JSON transformation).  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|firstVersion
operator|=
literal|"2.16.0"
argument_list|,
name|scheme
operator|=
literal|"jolt"
argument_list|,
name|title
operator|=
literal|"JOLT"
argument_list|,
name|syntax
operator|=
literal|"jolt:resourceUri"
argument_list|,
name|producerOnly
operator|=
literal|true
argument_list|,
name|label
operator|=
literal|"transformation"
argument_list|)
DECL|class|JoltEndpoint
specifier|public
class|class
name|JoltEndpoint
extends|extends
name|ResourceEndpoint
block|{
DECL|field|transform
specifier|private
name|JoltTransform
name|transform
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"Hydrated"
argument_list|)
DECL|field|outputType
specifier|private
name|JoltInputOutputType
name|outputType
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"Hydrated"
argument_list|)
DECL|field|inputType
specifier|private
name|JoltInputOutputType
name|inputType
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"Chainr"
argument_list|)
DECL|field|transformDsl
specifier|private
name|JoltTransformType
name|transformDsl
init|=
name|JoltTransformType
operator|.
name|Chainr
decl_stmt|;
DECL|method|JoltEndpoint ()
specifier|public
name|JoltEndpoint
parameter_list|()
block|{     }
DECL|method|JoltEndpoint (String uri, JoltComponent component, String resourceUri)
specifier|public
name|JoltEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|JoltComponent
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
literal|"jolt:"
operator|+
name|getResourceUri
argument_list|()
return|;
block|}
DECL|method|getTransform ()
specifier|private
specifier|synchronized
name|JoltTransform
name|getTransform
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|transform
operator|==
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
name|String
name|path
init|=
name|getResourceUri
argument_list|()
decl_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Jolt content read from resource {} with resourceUri: {} for endpoint {}"
argument_list|,
name|getResourceUri
argument_list|()
argument_list|,
name|path
argument_list|,
name|getEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// Sortr does not require a spec
if|if
condition|(
name|this
operator|.
name|transformDsl
operator|==
name|JoltTransformType
operator|.
name|Sortr
condition|)
block|{
name|this
operator|.
name|transform
operator|=
operator|new
name|Sortr
argument_list|()
expr_stmt|;
block|}
else|else
block|{
comment|// getResourceAsInputStream also considers the content cache
name|Object
name|spec
init|=
name|JsonUtils
operator|.
name|jsonToObject
argument_list|(
name|getResourceAsInputStream
argument_list|()
argument_list|)
decl_stmt|;
switch|switch
condition|(
name|this
operator|.
name|transformDsl
condition|)
block|{
case|case
name|Shiftr
case|:
name|this
operator|.
name|transform
operator|=
operator|new
name|Shiftr
argument_list|(
name|spec
argument_list|)
expr_stmt|;
break|break;
case|case
name|Defaultr
case|:
name|this
operator|.
name|transform
operator|=
operator|new
name|Defaultr
argument_list|(
name|spec
argument_list|)
expr_stmt|;
break|break;
case|case
name|Removr
case|:
name|this
operator|.
name|transform
operator|=
operator|new
name|Removr
argument_list|(
name|spec
argument_list|)
expr_stmt|;
break|break;
case|case
name|Chainr
case|:
default|default:
name|this
operator|.
name|transform
operator|=
name|Chainr
operator|.
name|fromSpec
argument_list|(
name|spec
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
block|}
return|return
name|transform
return|;
block|}
comment|/**      * Sets the Transform to use. If not set a Transform specified by the transformDsl will be created      */
DECL|method|setTransform (JoltTransform transform)
specifier|public
name|void
name|setTransform
parameter_list|(
name|JoltTransform
name|transform
parameter_list|)
block|{
name|this
operator|.
name|transform
operator|=
name|transform
expr_stmt|;
block|}
DECL|method|getOutputType ()
specifier|public
name|JoltInputOutputType
name|getOutputType
parameter_list|()
block|{
return|return
name|outputType
return|;
block|}
comment|/**      * Specifies if the output should be hydrated JSON or a JSON String.      */
DECL|method|setOutputType (JoltInputOutputType outputType)
specifier|public
name|void
name|setOutputType
parameter_list|(
name|JoltInputOutputType
name|outputType
parameter_list|)
block|{
name|this
operator|.
name|outputType
operator|=
name|outputType
expr_stmt|;
block|}
DECL|method|getInputType ()
specifier|public
name|JoltInputOutputType
name|getInputType
parameter_list|()
block|{
return|return
name|inputType
return|;
block|}
comment|/**      * Specifies if the input is hydrated JSON or a JSON String.      */
DECL|method|setInputType (JoltInputOutputType inputType)
specifier|public
name|void
name|setInputType
parameter_list|(
name|JoltInputOutputType
name|inputType
parameter_list|)
block|{
name|this
operator|.
name|inputType
operator|=
name|inputType
expr_stmt|;
block|}
DECL|method|getTransformDsl ()
specifier|public
name|JoltTransformType
name|getTransformDsl
parameter_list|()
block|{
return|return
name|transformDsl
return|;
block|}
comment|/**      * Specifies the Transform DSL of the endpoint resource. If none is specified<code>Chainr</code> will be used.      */
DECL|method|setTransformDsl (JoltTransformType transformType)
specifier|public
name|void
name|setTransformDsl
parameter_list|(
name|JoltTransformType
name|transformType
parameter_list|)
block|{
name|this
operator|.
name|transformDsl
operator|=
name|transformType
expr_stmt|;
block|}
DECL|method|findOrCreateEndpoint (String uri, String newResourceUri)
specifier|public
name|JoltEndpoint
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
name|log
operator|.
name|debug
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
name|JoltEndpoint
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
name|path
init|=
name|getResourceUri
argument_list|()
decl_stmt|;
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
name|JoltConstants
operator|.
name|JOLT_RESOURCE_URI
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
name|JoltConstants
operator|.
name|JOLT_RESOURCE_URI
argument_list|)
expr_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"{} set to {} creating new endpoint to handle exchange"
argument_list|,
name|JoltConstants
operator|.
name|JOLT_RESOURCE_URI
argument_list|,
name|newResourceUri
argument_list|)
expr_stmt|;
name|JoltEndpoint
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
name|Object
name|input
decl_stmt|;
if|if
condition|(
name|getInputType
argument_list|()
operator|==
name|JoltInputOutputType
operator|.
name|JsonString
condition|)
block|{
name|input
operator|=
name|JsonUtils
operator|.
name|jsonToObject
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|InputStream
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|input
operator|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
expr_stmt|;
block|}
name|Object
name|output
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|inputContextMap
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|JoltConstants
operator|.
name|JOLT_CONTEXT
argument_list|,
name|Map
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|inputContextMap
operator|!=
literal|null
condition|)
block|{
name|output
operator|=
operator|(
operator|(
name|ContextualTransform
operator|)
name|getTransform
argument_list|()
operator|)
operator|.
name|transform
argument_list|(
name|input
argument_list|,
name|inputContextMap
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|output
operator|=
operator|(
operator|(
name|Transform
operator|)
name|getTransform
argument_list|()
operator|)
operator|.
name|transform
argument_list|(
name|input
argument_list|)
expr_stmt|;
block|}
comment|// now lets output the results to the exchange
name|Message
name|out
init|=
name|exchange
operator|.
name|getOut
argument_list|()
decl_stmt|;
if|if
condition|(
name|getOutputType
argument_list|()
operator|==
name|JoltInputOutputType
operator|.
name|JsonString
condition|)
block|{
name|out
operator|.
name|setBody
argument_list|(
name|JsonUtils
operator|.
name|toJsonString
argument_list|(
name|output
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|out
operator|.
name|setBody
argument_list|(
name|output
argument_list|)
expr_stmt|;
block|}
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
block|}
end_class

end_unit

