begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cxf.cxfbean
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|cxf
operator|.
name|cxfbean
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
name|component
operator|.
name|cxf
operator|.
name|CxfSoapBinding
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
name|HeaderFilterStrategy
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|helpers
operator|.
name|CastUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|Message
import|;
end_import

begin_comment
comment|/**  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|DefaultCxfBeanBinding
specifier|public
class|class
name|DefaultCxfBeanBinding
implements|implements
name|CxfBeanBinding
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|DefaultCxfBeanBinding
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|createCxfMessageFromCamelExchange (Exchange camelExchange, HeaderFilterStrategy headerFilterStrategy)
specifier|public
name|Message
name|createCxfMessageFromCamelExchange
parameter_list|(
name|Exchange
name|camelExchange
parameter_list|,
name|HeaderFilterStrategy
name|headerFilterStrategy
parameter_list|)
block|{
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Message
name|camelMessage
init|=
name|camelExchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
comment|// request content types
name|String
name|requestContentType
init|=
name|getRequestContentType
argument_list|(
name|camelMessage
argument_list|)
decl_stmt|;
comment|// accept content types
name|String
name|acceptContentTypes
init|=
name|camelMessage
operator|.
name|getHeader
argument_list|(
literal|"Accept"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|acceptContentTypes
operator|==
literal|null
condition|)
block|{
name|acceptContentTypes
operator|=
literal|"*/*"
expr_stmt|;
block|}
name|String
name|enc
init|=
name|getCharacterEncoding
argument_list|(
name|camelMessage
argument_list|)
decl_stmt|;
comment|// path
name|String
name|path
init|=
name|getPath
argument_list|(
name|camelMessage
argument_list|)
decl_stmt|;
comment|// base path
name|String
name|basePath
init|=
name|getBasePath
argument_list|(
name|camelExchange
argument_list|)
decl_stmt|;
comment|// verb
name|String
name|verb
init|=
name|getVerb
argument_list|(
name|camelMessage
argument_list|)
decl_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Processing "
operator|+
name|camelExchange
operator|+
literal|", requestContentType = "
operator|+
name|requestContentType
operator|+
literal|", acceptContentTypes = "
operator|+
name|acceptContentTypes
operator|+
literal|", encoding = "
operator|+
name|enc
operator|+
literal|", path = "
operator|+
name|path
operator|+
literal|", basePath = "
operator|+
name|basePath
operator|+
literal|", verb = "
operator|+
name|verb
argument_list|)
expr_stmt|;
block|}
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|Message
name|answer
init|=
name|CxfSoapBinding
operator|.
name|getCxfInMessage
argument_list|(
name|headerFilterStrategy
argument_list|,
name|camelExchange
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|answer
operator|.
name|put
argument_list|(
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|Message
operator|.
name|REQUEST_URI
argument_list|,
name|path
argument_list|)
expr_stmt|;
name|answer
operator|.
name|put
argument_list|(
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|Message
operator|.
name|BASE_PATH
argument_list|,
name|basePath
argument_list|)
expr_stmt|;
name|answer
operator|.
name|put
argument_list|(
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|Message
operator|.
name|HTTP_REQUEST_METHOD
argument_list|,
name|verb
argument_list|)
expr_stmt|;
name|answer
operator|.
name|put
argument_list|(
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|Message
operator|.
name|PATH_INFO
argument_list|,
name|path
argument_list|)
expr_stmt|;
name|answer
operator|.
name|put
argument_list|(
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|Message
operator|.
name|CONTENT_TYPE
argument_list|,
name|requestContentType
argument_list|)
expr_stmt|;
name|answer
operator|.
name|put
argument_list|(
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|Message
operator|.
name|ACCEPT_CONTENT_TYPE
argument_list|,
name|acceptContentTypes
argument_list|)
expr_stmt|;
name|answer
operator|.
name|put
argument_list|(
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|Message
operator|.
name|ENCODING
argument_list|,
name|enc
argument_list|)
expr_stmt|;
comment|// TODO propagate security context
return|return
name|answer
return|;
block|}
DECL|method|propagateResponseHeadersToCamel (Message cxfMessage, Exchange exchange, HeaderFilterStrategy strategy)
specifier|public
name|void
name|propagateResponseHeadersToCamel
parameter_list|(
name|Message
name|cxfMessage
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|HeaderFilterStrategy
name|strategy
parameter_list|)
block|{
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Propagating response headers from CXF message "
operator|+
name|cxfMessage
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|strategy
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|camelHeaders
init|=
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getHeaders
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|cxfHeaders
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|Map
operator|)
name|cxfMessage
operator|.
name|get
argument_list|(
name|Message
operator|.
name|PROTOCOL_HEADERS
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|cxfHeaders
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|entry
range|:
name|cxfHeaders
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
operator|!
name|strategy
operator|.
name|applyFilterToExternalHeaders
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|,
name|exchange
argument_list|)
condition|)
block|{
name|camelHeaders
operator|.
name|put
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Populate header from CXF header="
operator|+
name|entry
operator|.
name|getKey
argument_list|()
operator|+
literal|" value="
operator|+
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
comment|// propagate HTTP RESPONSE_CODE
name|String
name|key
init|=
name|Message
operator|.
name|RESPONSE_CODE
decl_stmt|;
name|Object
name|value
init|=
name|cxfMessage
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|!=
literal|null
operator|&&
operator|!
name|strategy
operator|.
name|applyFilterToExternalHeaders
argument_list|(
name|key
argument_list|,
name|value
argument_list|,
name|exchange
argument_list|)
condition|)
block|{
name|camelHeaders
operator|.
name|put
argument_list|(
name|Exchange
operator|.
name|HTTP_RESPONSE_CODE
argument_list|,
name|value
argument_list|)
expr_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Populate header from CXF header="
operator|+
name|key
operator|+
literal|" value="
operator|+
name|value
operator|+
literal|" as "
operator|+
name|Exchange
operator|.
name|HTTP_RESPONSE_CODE
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|getPath (org.apache.camel.Message camelMessage)
specifier|protected
name|String
name|getPath
parameter_list|(
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Message
name|camelMessage
parameter_list|)
block|{
name|String
name|answer
init|=
name|camelMessage
operator|.
name|getHeader
argument_list|(
name|Exchange
operator|.
name|HTTP_PATH
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
return|return
name|answer
return|;
block|}
DECL|method|getBasePath (Exchange camelExchange)
specifier|protected
name|String
name|getBasePath
parameter_list|(
name|Exchange
name|camelExchange
parameter_list|)
block|{
name|String
name|answer
init|=
name|camelExchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|CxfBeanHeaderNames
operator|.
name|BASE_PATH
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|answer
operator|==
literal|null
condition|)
block|{
name|answer
operator|=
name|camelExchange
operator|.
name|getFromEndpoint
argument_list|()
operator|.
name|getEndpointUri
argument_list|()
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
DECL|method|getVerb (org.apache.camel.Message camelMessage)
specifier|protected
name|String
name|getVerb
parameter_list|(
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Message
name|camelMessage
parameter_list|)
block|{
name|String
name|answer
init|=
name|camelMessage
operator|.
name|getHeader
argument_list|(
name|Exchange
operator|.
name|HTTP_METHOD
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
return|return
name|answer
return|;
block|}
DECL|method|getCharacterEncoding (org.apache.camel.Message camelMessage)
specifier|protected
name|String
name|getCharacterEncoding
parameter_list|(
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Message
name|camelMessage
parameter_list|)
block|{
name|String
name|answer
init|=
name|camelMessage
operator|.
name|getHeader
argument_list|(
name|Exchange
operator|.
name|HTTP_CHARACTER_ENCODING
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|answer
operator|==
literal|null
condition|)
block|{
name|answer
operator|=
name|camelMessage
operator|.
name|getHeader
argument_list|(
name|Exchange
operator|.
name|CHARSET_NAME
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
DECL|method|getRequestContentType (org.apache.camel.Message camelMessage)
specifier|protected
name|String
name|getRequestContentType
parameter_list|(
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Message
name|camelMessage
parameter_list|)
block|{
name|String
name|answer
init|=
name|camelMessage
operator|.
name|getHeader
argument_list|(
name|Exchange
operator|.
name|CONTENT_TYPE
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|answer
operator|!=
literal|null
condition|)
block|{
return|return
name|answer
return|;
block|}
comment|// return default
return|return
literal|"*/*"
return|;
block|}
block|}
end_class

end_unit

