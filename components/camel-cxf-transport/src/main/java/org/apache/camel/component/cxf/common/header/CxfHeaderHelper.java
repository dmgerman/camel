begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cxf.common.header
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
name|common
operator|.
name|header
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

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
name|java
operator|.
name|util
operator|.
name|TreeMap
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
name|common
operator|.
name|message
operator|.
name|CxfConstants
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
name|cxf
operator|.
name|endpoint
operator|.
name|Client
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
comment|/**  * Utility class to propagate headers to and from CXF message.  */
end_comment

begin_class
DECL|class|CxfHeaderHelper
specifier|public
specifier|final
class|class
name|CxfHeaderHelper
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
name|CxfHeaderHelper
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|CAMEL_TO_CXF_HEADERS
specifier|private
specifier|static
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|CAMEL_TO_CXF_HEADERS
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|CXF_TO_CAMEL_HEADERS
specifier|private
specifier|static
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|CXF_TO_CAMEL_HEADERS
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
static|static
block|{
comment|// initialize mappings between Camel and CXF header names
name|defineMapping
argument_list|(
name|Exchange
operator|.
name|HTTP_URI
argument_list|,
name|Message
operator|.
name|REQUEST_URI
argument_list|)
expr_stmt|;
name|defineMapping
argument_list|(
name|Exchange
operator|.
name|HTTP_METHOD
argument_list|,
name|Message
operator|.
name|HTTP_REQUEST_METHOD
argument_list|)
expr_stmt|;
name|defineMapping
argument_list|(
name|Exchange
operator|.
name|HTTP_PATH
argument_list|,
name|Message
operator|.
name|PATH_INFO
argument_list|)
expr_stmt|;
name|defineMapping
argument_list|(
name|Exchange
operator|.
name|CONTENT_TYPE
argument_list|,
name|Message
operator|.
name|CONTENT_TYPE
argument_list|)
expr_stmt|;
name|defineMapping
argument_list|(
name|Exchange
operator|.
name|HTTP_CHARACTER_ENCODING
argument_list|,
name|Message
operator|.
name|ENCODING
argument_list|)
expr_stmt|;
name|defineMapping
argument_list|(
name|Exchange
operator|.
name|HTTP_QUERY
argument_list|,
name|Message
operator|.
name|QUERY_STRING
argument_list|)
expr_stmt|;
name|defineMapping
argument_list|(
name|Exchange
operator|.
name|ACCEPT_CONTENT_TYPE
argument_list|,
name|Message
operator|.
name|ACCEPT_CONTENT_TYPE
argument_list|)
expr_stmt|;
name|defineMapping
argument_list|(
name|Exchange
operator|.
name|HTTP_RESPONSE_CODE
argument_list|,
name|Message
operator|.
name|RESPONSE_CODE
argument_list|)
expr_stmt|;
block|}
comment|/**      * Utility class does not have public constructor      */
DECL|method|CxfHeaderHelper ()
specifier|private
name|CxfHeaderHelper
parameter_list|()
block|{     }
DECL|method|defineMapping (String camelHeader, String cxfHeader)
specifier|private
specifier|static
name|void
name|defineMapping
parameter_list|(
name|String
name|camelHeader
parameter_list|,
name|String
name|cxfHeader
parameter_list|)
block|{
name|CAMEL_TO_CXF_HEADERS
operator|.
name|put
argument_list|(
name|camelHeader
argument_list|,
name|cxfHeader
argument_list|)
expr_stmt|;
name|CXF_TO_CAMEL_HEADERS
operator|.
name|put
argument_list|(
name|cxfHeader
argument_list|,
name|camelHeader
argument_list|)
expr_stmt|;
block|}
comment|/**      * Propagates Camel headers to CXF headers.      *      * @param strategy header filter strategy      * @param camelHeaders Camel headers      * @param requestHeaders CXF request headers      * @param camelExchange provides context for filtering      */
DECL|method|propagateCamelHeadersToCxfHeaders (HeaderFilterStrategy strategy, Map<String, Object> camelHeaders, Map<String, List<String>> requestHeaders, Exchange camelExchange)
specifier|public
specifier|static
name|void
name|propagateCamelHeadersToCxfHeaders
parameter_list|(
name|HeaderFilterStrategy
name|strategy
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|camelHeaders
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|requestHeaders
parameter_list|,
name|Exchange
name|camelExchange
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|strategy
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|camelHeaders
operator|.
name|entrySet
argument_list|()
operator|.
name|forEach
argument_list|(
name|entry
lambda|->
block|{
comment|// Need to make sure the cxf needed header will not be filtered
if|if
condition|(
name|strategy
operator|.
name|applyFilterToCamelHeaders
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
name|camelExchange
argument_list|)
operator|&&
name|CAMEL_TO_CXF_HEADERS
operator|.
name|get
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Drop Camel header: {}={}"
argument_list|,
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
return|return;
block|}
comment|// we need to make sure the entry value is not null
if|if
condition|(
name|entry
operator|.
name|getValue
argument_list|()
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Drop Camel header: {}={}"
argument_list|,
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
return|return;
block|}
name|String
name|cxfHeaderName
init|=
name|CAMEL_TO_CXF_HEADERS
operator|.
name|getOrDefault
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"Propagate Camel header: {}={} as {}"
argument_list|,
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
name|cxfHeaderName
argument_list|)
expr_stmt|;
name|requestHeaders
operator|.
name|put
argument_list|(
name|cxfHeaderName
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
argument_list|)
expr_stmt|;
block|}
comment|/**      * Propagates Camel headers to CXF message.      *      * @param strategy header filter strategy      * @param camelHeaders Camel header      * @param cxfMessage CXF message      * @param exchange provides context for filtering      */
DECL|method|propagateCamelToCxf (HeaderFilterStrategy strategy, Map<String, Object> camelHeaders, Message cxfMessage, Exchange exchange)
specifier|public
specifier|static
name|void
name|propagateCamelToCxf
parameter_list|(
name|HeaderFilterStrategy
name|strategy
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|camelHeaders
parameter_list|,
name|Message
name|cxfMessage
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
comment|// use copyProtocolHeadersFromCxfToCamel treemap to keep ordering and ignore key case
name|cxfMessage
operator|.
name|putIfAbsent
argument_list|(
name|Message
operator|.
name|PROTOCOL_HEADERS
argument_list|,
operator|new
name|TreeMap
argument_list|<>
argument_list|(
name|String
operator|.
name|CASE_INSENSITIVE_ORDER
argument_list|)
argument_list|)
expr_stmt|;
specifier|final
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
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
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
name|strategy
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|camelHeaders
operator|.
name|entrySet
argument_list|()
operator|.
name|forEach
argument_list|(
name|entry
lambda|->
block|{
comment|// Need to make sure the cxf needed header will not be filtered
if|if
condition|(
name|strategy
operator|.
name|applyFilterToCamelHeaders
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
name|LOG
operator|.
name|trace
argument_list|(
literal|"Drop external header: {}={}"
argument_list|,
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
return|return;
block|}
comment|// we need to make sure the entry value is not null
if|if
condition|(
name|entry
operator|.
name|getValue
argument_list|()
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Drop Camel header: {}={}"
argument_list|,
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
return|return;
block|}
name|String
name|cxfHeaderName
init|=
name|CAMEL_TO_CXF_HEADERS
operator|.
name|getOrDefault
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"Propagate Camel header: {}={} as {}"
argument_list|,
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
name|cxfHeaderName
argument_list|)
expr_stmt|;
if|if
condition|(
name|Exchange
operator|.
name|CONTENT_TYPE
operator|.
name|equals
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
condition|)
block|{
name|cxfMessage
operator|.
name|put
argument_list|(
name|cxfHeaderName
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|Exchange
operator|.
name|HTTP_RESPONSE_CODE
operator|.
name|equals
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
operator|||
name|Client
operator|.
name|REQUEST_CONTEXT
operator|.
name|equals
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
operator|||
name|Client
operator|.
name|RESPONSE_CONTEXT
operator|.
name|equals
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
condition|)
block|{
name|cxfMessage
operator|.
name|put
argument_list|(
name|cxfHeaderName
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|Object
name|values
init|=
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
if|if
condition|(
name|values
operator|instanceof
name|List
argument_list|<
name|?
argument_list|>
argument_list|)
block|{
name|cxfHeaders
operator|.
name|put
argument_list|(
name|cxfHeaderName
argument_list|,
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|List
argument_list|<
name|?
argument_list|>
operator|)
name|values
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|)
block|;                 }
else|else
block|{
name|List
argument_list|<
name|String
argument_list|>
name|listValue
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|listValue
operator|.
name|add
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|cxfHeaders
operator|.
name|put
argument_list|(
name|cxfHeaderName
argument_list|,
name|listValue
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

begin_empty_stmt
unit|)
empty_stmt|;
end_empty_stmt

begin_comment
unit|}
comment|/**      * Propagates CXF headers to Camel headers.      *      * @param strategy header filter strategy      * @param responseHeaders CXF response headers      * @param camelHeaders Camel headers      * @param camelExchange provides context for filtering      */
end_comment

begin_function
DECL|method|propagateCxfHeadersToCamelHeaders (HeaderFilterStrategy strategy, Map<String, List<Object>> responseHeaders, Map<String, Object> camelHeaders, Exchange camelExchange)
unit|public
specifier|static
name|void
name|propagateCxfHeadersToCamelHeaders
parameter_list|(
name|HeaderFilterStrategy
name|strategy
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|Object
argument_list|>
argument_list|>
name|responseHeaders
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|camelHeaders
parameter_list|,
name|Exchange
name|camelExchange
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|strategy
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|responseHeaders
operator|.
name|entrySet
argument_list|()
operator|.
name|forEach
argument_list|(
name|entry
lambda|->
block|{
if|if
condition|(
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
name|camelExchange
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Drop external header: {}={}"
argument_list|,
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
return|return;
block|}
name|String
name|camelHeaderName
init|=
name|CXF_TO_CAMEL_HEADERS
operator|.
name|getOrDefault
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"Populate external header: {}={} as {}"
argument_list|,
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
name|camelHeaderName
argument_list|)
expr_stmt|;
name|camelHeaders
operator|.
name|put
argument_list|(
name|camelHeaderName
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
block|}
argument_list|)
expr_stmt|;
block|}
end_function

begin_comment
comment|/**      * Propagates CXF headers to Camel message.      *      * @param strategy header filter strategy      * @param cxfMessage CXF message      * @param camelMessage Camel message      * @param exchange provides context for filtering      */
end_comment

begin_function
DECL|method|propagateCxfToCamel (HeaderFilterStrategy strategy, Message cxfMessage, org.apache.camel.Message camelMessage, Exchange exchange)
specifier|public
specifier|static
name|void
name|propagateCxfToCamel
parameter_list|(
name|HeaderFilterStrategy
name|strategy
parameter_list|,
name|Message
name|cxfMessage
parameter_list|,
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Message
name|camelMessage
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
if|if
condition|(
name|strategy
operator|==
literal|null
condition|)
block|{
return|return;
block|}
comment|// Copy the CXF protocol headers to the camel headers
name|copyProtocolHeadersFromCxfToCamel
argument_list|(
name|strategy
argument_list|,
name|exchange
argument_list|,
name|cxfMessage
argument_list|,
name|camelMessage
argument_list|)
expr_stmt|;
comment|// Copy the CXF HTTP headers to the camel headers
name|copyHttpHeadersFromCxfToCamel
argument_list|(
name|strategy
argument_list|,
name|cxfMessage
argument_list|,
name|camelMessage
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
comment|// propagate request context
name|copyCxfHeaderToCamel
argument_list|(
name|strategy
argument_list|,
name|exchange
argument_list|,
name|cxfMessage
argument_list|,
name|camelMessage
argument_list|,
name|Client
operator|.
name|REQUEST_CONTEXT
argument_list|)
expr_stmt|;
comment|// propagate response context
name|copyCxfHeaderToCamel
argument_list|(
name|strategy
argument_list|,
name|exchange
argument_list|,
name|cxfMessage
argument_list|,
name|camelMessage
argument_list|,
name|Client
operator|.
name|RESPONSE_CONTEXT
argument_list|)
expr_stmt|;
block|}
end_function

begin_function
DECL|method|copyProtocolHeadersFromCxfToCamel (HeaderFilterStrategy strategy, Exchange exchange, Message cxfMessage, org.apache.camel.Message camelMessage)
specifier|private
specifier|static
name|void
name|copyProtocolHeadersFromCxfToCamel
parameter_list|(
name|HeaderFilterStrategy
name|strategy
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|Message
name|cxfMessage
parameter_list|,
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
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
operator|)
name|cxfMessage
operator|.
name|getOrDefault
argument_list|(
name|Message
operator|.
name|PROTOCOL_HEADERS
argument_list|,
name|Collections
operator|.
name|emptyMap
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|cxfHeaders
operator|.
name|entrySet
argument_list|()
operator|.
name|forEach
argument_list|(
name|cxfHeader
lambda|->
block|{
name|String
name|camelHeaderName
init|=
name|CXF_TO_CAMEL_HEADERS
operator|.
name|getOrDefault
argument_list|(
name|cxfHeader
operator|.
name|getKey
argument_list|()
argument_list|,
name|cxfHeader
operator|.
name|getKey
argument_list|()
argument_list|)
decl_stmt|;
name|Object
name|value
init|=
name|convertCxfProtocolHeaderValues
argument_list|(
name|cxfHeader
operator|.
name|getValue
argument_list|()
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
name|copyCxfHeaderToCamel
argument_list|(
name|strategy
argument_list|,
name|exchange
argument_list|,
name|cxfMessage
argument_list|,
name|camelMessage
argument_list|,
name|cxfHeader
operator|.
name|getKey
argument_list|()
argument_list|,
name|camelHeaderName
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
argument_list|)
expr_stmt|;
block|}
end_function

begin_function
DECL|method|convertCxfProtocolHeaderValues (List<String> values, Exchange exchange)
specifier|private
specifier|static
name|Object
name|convertCxfProtocolHeaderValues
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|values
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
if|if
condition|(
name|values
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
return|return
name|values
operator|.
name|get
argument_list|(
literal|0
argument_list|)
return|;
block|}
if|if
condition|(
name|exchange
operator|.
name|getProperty
argument_list|(
name|CxfConstants
operator|.
name|CAMEL_CXF_PROTOCOL_HEADERS_MERGED
argument_list|,
name|Boolean
operator|.
name|FALSE
argument_list|,
name|Boolean
operator|.
name|class
argument_list|)
condition|)
block|{
return|return
name|String
operator|.
name|join
argument_list|(
literal|", "
argument_list|,
name|values
argument_list|)
return|;
block|}
return|return
name|values
return|;
block|}
end_function

begin_function
DECL|method|copyHttpHeadersFromCxfToCamel (HeaderFilterStrategy strategy, Message cxfMessage, org.apache.camel.Message camelMessage, Exchange exchange)
specifier|public
specifier|static
name|void
name|copyHttpHeadersFromCxfToCamel
parameter_list|(
name|HeaderFilterStrategy
name|strategy
parameter_list|,
name|Message
name|cxfMessage
parameter_list|,
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Message
name|camelMessage
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
name|CXF_TO_CAMEL_HEADERS
operator|.
name|entrySet
argument_list|()
operator|.
name|forEach
argument_list|(
name|entry
lambda|->
name|copyCxfHeaderToCamel
argument_list|(
name|strategy
argument_list|,
name|exchange
argument_list|,
name|cxfMessage
argument_list|,
name|camelMessage
argument_list|,
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
end_function

begin_function
DECL|method|copyCxfHeaderToCamel (HeaderFilterStrategy strategy, Exchange exchange, Message cxfMessage, org.apache.camel.Message camelMessage, String key)
specifier|private
specifier|static
name|void
name|copyCxfHeaderToCamel
parameter_list|(
name|HeaderFilterStrategy
name|strategy
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|Message
name|cxfMessage
parameter_list|,
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Message
name|camelMessage
parameter_list|,
name|String
name|key
parameter_list|)
block|{
name|copyCxfHeaderToCamel
argument_list|(
name|strategy
argument_list|,
name|exchange
argument_list|,
name|cxfMessage
argument_list|,
name|camelMessage
argument_list|,
name|key
argument_list|,
name|key
argument_list|)
expr_stmt|;
block|}
end_function

begin_function
DECL|method|copyCxfHeaderToCamel (HeaderFilterStrategy strategy, Exchange exchange, Message cxfMessage, org.apache.camel.Message camelMessage, String cxfKey, String camelKey)
specifier|private
specifier|static
name|void
name|copyCxfHeaderToCamel
parameter_list|(
name|HeaderFilterStrategy
name|strategy
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|Message
name|cxfMessage
parameter_list|,
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Message
name|camelMessage
parameter_list|,
name|String
name|cxfKey
parameter_list|,
name|String
name|camelKey
parameter_list|)
block|{
name|copyCxfHeaderToCamel
argument_list|(
name|strategy
argument_list|,
name|exchange
argument_list|,
name|cxfMessage
argument_list|,
name|camelMessage
argument_list|,
name|cxfKey
argument_list|,
name|camelKey
argument_list|,
name|cxfMessage
operator|.
name|get
argument_list|(
name|cxfKey
argument_list|)
argument_list|)
expr_stmt|;
block|}
end_function

begin_function
DECL|method|copyCxfHeaderToCamel (HeaderFilterStrategy strategy, Exchange exchange, Message cxfMessage, org.apache.camel.Message camelMessage, String cxfKey, String camelKey, Object initialValue)
specifier|private
specifier|static
name|void
name|copyCxfHeaderToCamel
parameter_list|(
name|HeaderFilterStrategy
name|strategy
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|Message
name|cxfMessage
parameter_list|,
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Message
name|camelMessage
parameter_list|,
name|String
name|cxfKey
parameter_list|,
name|String
name|camelKey
parameter_list|,
name|Object
name|initialValue
parameter_list|)
block|{
name|Object
name|value
init|=
name|initialValue
decl_stmt|;
if|if
condition|(
name|Message
operator|.
name|PATH_INFO
operator|.
name|equals
argument_list|(
name|cxfKey
argument_list|)
condition|)
block|{
comment|// We need remove the BASE_PATH from the PATH_INFO
name|value
operator|=
name|convertPathInfo
argument_list|(
name|cxfMessage
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|Message
operator|.
name|CONTENT_TYPE
operator|.
name|equals
argument_list|(
name|cxfKey
argument_list|)
condition|)
block|{
comment|// propagate content type with the encoding information
comment|// We need to do it as the CXF does this kind of thing in transport level
name|value
operator|=
name|determineContentType
argument_list|(
name|cxfMessage
argument_list|)
expr_stmt|;
block|}
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
name|cxfKey
argument_list|,
name|value
argument_list|,
name|exchange
argument_list|)
condition|)
block|{
name|camelMessage
operator|.
name|setHeader
argument_list|(
name|camelKey
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
end_function

begin_function
DECL|method|convertPathInfo (Message message)
specifier|private
specifier|static
name|String
name|convertPathInfo
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|String
name|pathInfo
init|=
name|findHeaderValue
argument_list|(
name|message
argument_list|,
name|Message
operator|.
name|PATH_INFO
argument_list|)
decl_stmt|;
name|String
name|basePath
init|=
name|findHeaderValue
argument_list|(
name|message
argument_list|,
name|Message
operator|.
name|BASE_PATH
argument_list|)
decl_stmt|;
if|if
condition|(
name|pathInfo
operator|!=
literal|null
operator|&&
name|basePath
operator|!=
literal|null
operator|&&
name|pathInfo
operator|.
name|startsWith
argument_list|(
name|basePath
argument_list|)
condition|)
block|{
return|return
name|pathInfo
operator|.
name|substring
argument_list|(
name|basePath
operator|.
name|length
argument_list|()
argument_list|)
return|;
block|}
return|return
name|pathInfo
return|;
block|}
end_function

begin_function
DECL|method|determineContentType (Message message)
specifier|private
specifier|static
name|String
name|determineContentType
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|String
name|ct
init|=
name|findHeaderValue
argument_list|(
name|message
argument_list|,
name|Message
operator|.
name|CONTENT_TYPE
argument_list|)
decl_stmt|;
name|String
name|enc
init|=
name|findHeaderValue
argument_list|(
name|message
argument_list|,
name|Message
operator|.
name|ENCODING
argument_list|)
decl_stmt|;
if|if
condition|(
literal|null
operator|!=
name|ct
condition|)
block|{
if|if
condition|(
name|enc
operator|!=
literal|null
operator|&&
operator|!
name|ct
operator|.
name|contains
argument_list|(
literal|"charset="
argument_list|)
operator|&&
operator|!
name|ct
operator|.
name|toLowerCase
argument_list|()
operator|.
name|contains
argument_list|(
literal|"multipart/related"
argument_list|)
condition|)
block|{
name|ct
operator|=
name|ct
operator|+
literal|"; charset="
operator|+
name|enc
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|enc
operator|!=
literal|null
condition|)
block|{
name|ct
operator|=
literal|"text/xml; charset="
operator|+
name|enc
expr_stmt|;
block|}
else|else
block|{
name|ct
operator|=
literal|"text/xml"
expr_stmt|;
block|}
comment|// update the content_type value in the message
name|message
operator|.
name|put
argument_list|(
name|Message
operator|.
name|CONTENT_TYPE
argument_list|,
name|ct
argument_list|)
expr_stmt|;
return|return
name|ct
return|;
block|}
end_function

begin_function
DECL|method|findHeaderValue (Message message, String key)
specifier|private
specifier|static
name|String
name|findHeaderValue
parameter_list|(
name|Message
name|message
parameter_list|,
name|String
name|key
parameter_list|)
block|{
name|String
name|value
init|=
operator|(
name|String
operator|)
name|message
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
condition|)
block|{
return|return
name|value
return|;
block|}
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|protocolHeaders
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
operator|)
name|message
operator|.
name|getOrDefault
argument_list|(
name|Message
operator|.
name|PROTOCOL_HEADERS
argument_list|,
name|Collections
operator|.
name|emptyMap
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
return|return
name|protocolHeaders
operator|.
name|getOrDefault
argument_list|(
name|key
argument_list|,
name|Collections
operator|.
name|singletonList
argument_list|(
literal|null
argument_list|)
argument_list|)
operator|.
name|get
argument_list|(
literal|0
argument_list|)
return|;
block|}
end_function

unit|}
end_unit

