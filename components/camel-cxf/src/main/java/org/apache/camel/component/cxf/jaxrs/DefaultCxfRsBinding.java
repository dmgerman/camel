begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cxf.jaxrs
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
name|jaxrs
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Method
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
name|javax
operator|.
name|security
operator|.
name|auth
operator|.
name|Subject
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|MultivaluedMap
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|Response
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
name|component
operator|.
name|cxf
operator|.
name|util
operator|.
name|CxfUtils
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
name|camel
operator|.
name|spi
operator|.
name|HeaderFilterStrategyAware
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
name|jaxrs
operator|.
name|impl
operator|.
name|MetadataMap
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
name|jaxrs
operator|.
name|model
operator|.
name|OperationResourceInfoStack
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
name|MessageContentsList
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
name|security
operator|.
name|SecurityContext
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
comment|/**  * Default strategy  to bind between Camel and CXF exchange for RESTful resources.  *  *  * @version   */
end_comment

begin_class
DECL|class|DefaultCxfRsBinding
specifier|public
class|class
name|DefaultCxfRsBinding
implements|implements
name|CxfRsBinding
implements|,
name|HeaderFilterStrategyAware
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
name|DefaultCxfRsBinding
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|camelToCxfHeaderMap
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|camelToCxfHeaderMap
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|cxfToCamelHeaderMap
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|cxfToCamelHeaderMap
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|headerFilterStrategy
specifier|private
name|HeaderFilterStrategy
name|headerFilterStrategy
decl_stmt|;
DECL|method|DefaultCxfRsBinding ()
specifier|public
name|DefaultCxfRsBinding
parameter_list|()
block|{
comment|// initialize mappings between Camel and CXF header names
name|camelToCxfHeaderMap
operator|.
name|put
argument_list|(
name|Exchange
operator|.
name|HTTP_URI
argument_list|,
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
argument_list|)
expr_stmt|;
name|camelToCxfHeaderMap
operator|.
name|put
argument_list|(
name|Exchange
operator|.
name|HTTP_METHOD
argument_list|,
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
argument_list|)
expr_stmt|;
name|camelToCxfHeaderMap
operator|.
name|put
argument_list|(
name|Exchange
operator|.
name|HTTP_PATH
argument_list|,
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
argument_list|)
expr_stmt|;
name|camelToCxfHeaderMap
operator|.
name|put
argument_list|(
name|Exchange
operator|.
name|HTTP_CHARACTER_ENCODING
argument_list|,
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
argument_list|)
expr_stmt|;
name|camelToCxfHeaderMap
operator|.
name|put
argument_list|(
name|Exchange
operator|.
name|HTTP_QUERY
argument_list|,
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
name|QUERY_STRING
argument_list|)
expr_stmt|;
name|camelToCxfHeaderMap
operator|.
name|put
argument_list|(
name|Exchange
operator|.
name|ACCEPT_CONTENT_TYPE
argument_list|,
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
argument_list|)
expr_stmt|;
name|cxfToCamelHeaderMap
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
name|Exchange
operator|.
name|HTTP_URI
argument_list|)
expr_stmt|;
name|cxfToCamelHeaderMap
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
name|Exchange
operator|.
name|HTTP_METHOD
argument_list|)
expr_stmt|;
name|cxfToCamelHeaderMap
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
name|Exchange
operator|.
name|HTTP_PATH
argument_list|)
expr_stmt|;
name|cxfToCamelHeaderMap
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
name|Exchange
operator|.
name|CONTENT_TYPE
argument_list|)
expr_stmt|;
name|cxfToCamelHeaderMap
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
name|Exchange
operator|.
name|HTTP_CHARACTER_ENCODING
argument_list|)
expr_stmt|;
name|cxfToCamelHeaderMap
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
name|QUERY_STRING
argument_list|,
name|Exchange
operator|.
name|HTTP_QUERY
argument_list|)
expr_stmt|;
name|cxfToCamelHeaderMap
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
name|Exchange
operator|.
name|ACCEPT_CONTENT_TYPE
argument_list|)
expr_stmt|;
block|}
DECL|method|populateCxfRsResponseFromExchange (Exchange camelExchange, org.apache.cxf.message.Exchange cxfExchange)
specifier|public
name|Object
name|populateCxfRsResponseFromExchange
parameter_list|(
name|Exchange
name|camelExchange
parameter_list|,
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|Exchange
name|cxfExchange
parameter_list|)
throws|throws
name|Exception
block|{
comment|// Need to check if the exchange has the exception
if|if
condition|(
name|camelExchange
operator|.
name|isFailed
argument_list|()
operator|&&
name|camelExchange
operator|.
name|getException
argument_list|()
operator|!=
literal|null
condition|)
block|{
throw|throw
name|camelExchange
operator|.
name|getException
argument_list|()
throw|;
block|}
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Message
name|response
decl_stmt|;
if|if
condition|(
name|camelExchange
operator|.
name|getPattern
argument_list|()
operator|.
name|isOutCapable
argument_list|()
condition|)
block|{
if|if
condition|(
name|camelExchange
operator|.
name|hasOut
argument_list|()
condition|)
block|{
name|response
operator|=
name|camelExchange
operator|.
name|getOut
argument_list|()
expr_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"Get the response from the out message"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|response
operator|=
name|camelExchange
operator|.
name|getIn
argument_list|()
expr_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"Get the response from the in message as a fallback"
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|response
operator|=
name|camelExchange
operator|.
name|getIn
argument_list|()
expr_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"Get the response from the in message"
argument_list|)
expr_stmt|;
block|}
return|return
name|response
operator|.
name|getBody
argument_list|()
return|;
block|}
DECL|method|populateExchangeFromCxfRsRequest (org.apache.cxf.message.Exchange cxfExchange, Exchange camelExchange, Method method, Object[] paramArray)
specifier|public
name|void
name|populateExchangeFromCxfRsRequest
parameter_list|(
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|Exchange
name|cxfExchange
parameter_list|,
name|Exchange
name|camelExchange
parameter_list|,
name|Method
name|method
parameter_list|,
name|Object
index|[]
name|paramArray
parameter_list|)
block|{
name|Message
name|camelMessage
init|=
name|camelExchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
comment|//Copy the CXF message header into the Camel inMessage
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|Message
name|cxfMessage
init|=
name|cxfExchange
operator|.
name|getInMessage
argument_list|()
decl_stmt|;
comment|// TODO use header filter strategy and cxfToCamelHeaderMap
name|CxfUtils
operator|.
name|copyHttpHeadersFromCxfToCamel
argument_list|(
name|cxfMessage
argument_list|,
name|camelMessage
argument_list|)
expr_stmt|;
comment|//copy the protocol header
name|copyProtocolHeader
argument_list|(
name|cxfMessage
argument_list|,
name|camelMessage
argument_list|,
name|camelMessage
operator|.
name|getExchange
argument_list|()
argument_list|)
expr_stmt|;
name|camelMessage
operator|.
name|setHeader
argument_list|(
name|CxfConstants
operator|.
name|CAMEL_CXF_RS_RESPONSE_CLASS
argument_list|,
name|method
operator|.
name|getReturnType
argument_list|()
argument_list|)
expr_stmt|;
name|camelMessage
operator|.
name|setHeader
argument_list|(
name|CxfConstants
operator|.
name|CAMEL_CXF_RS_RESPONSE_GENERIC_TYPE
argument_list|,
name|method
operator|.
name|getGenericReturnType
argument_list|()
argument_list|)
expr_stmt|;
name|copyOperationResourceInfoStack
argument_list|(
name|cxfMessage
argument_list|,
name|camelMessage
argument_list|)
expr_stmt|;
name|camelMessage
operator|.
name|setHeader
argument_list|(
name|CxfConstants
operator|.
name|OPERATION_NAME
argument_list|,
name|method
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|camelMessage
operator|.
name|setHeader
argument_list|(
name|CxfConstants
operator|.
name|CAMEL_CXF_MESSAGE
argument_list|,
name|cxfMessage
argument_list|)
expr_stmt|;
name|camelMessage
operator|.
name|setBody
argument_list|(
operator|new
name|MessageContentsList
argument_list|(
name|paramArray
argument_list|)
argument_list|)
expr_stmt|;
comment|// propagate the security subject from CXF security context
name|SecurityContext
name|securityContext
init|=
name|cxfMessage
operator|.
name|get
argument_list|(
name|SecurityContext
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|securityContext
operator|!=
literal|null
operator|&&
name|securityContext
operator|.
name|getUserPrincipal
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|Subject
name|subject
init|=
operator|new
name|Subject
argument_list|()
decl_stmt|;
name|subject
operator|.
name|getPrincipals
argument_list|()
operator|.
name|add
argument_list|(
name|securityContext
operator|.
name|getUserPrincipal
argument_list|()
argument_list|)
expr_stmt|;
name|camelExchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeaders
argument_list|()
operator|.
name|put
argument_list|(
name|Exchange
operator|.
name|AUTHENTICATION
argument_list|,
name|subject
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|bindCamelHeadersToRequestHeaders (Map<String, Object> camelHeaders, Exchange camelExchange)
specifier|public
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|bindCamelHeadersToRequestHeaders
parameter_list|(
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
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|answer
init|=
operator|new
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|entry
range|:
name|camelHeaders
operator|.
name|entrySet
argument_list|()
control|)
block|{
comment|// Need to make sure the cxf needed header will not be filtered
if|if
condition|(
name|headerFilterStrategy
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
name|camelToCxfHeaderMap
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
continue|continue;
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
continue|continue;
block|}
name|String
name|mappedHeaderName
init|=
name|camelToCxfHeaderMap
operator|.
name|get
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|mappedHeaderName
operator|==
literal|null
condition|)
block|{
name|mappedHeaderName
operator|=
name|entry
operator|.
name|getKey
argument_list|()
expr_stmt|;
block|}
name|LOG
operator|.
name|trace
argument_list|(
literal|"Propagate Camel header: {}={} as {}"
argument_list|,
operator|new
name|Object
index|[]
block|{
name|entry
operator|.
name|getKey
argument_list|()
block|,
name|entry
operator|.
name|getValue
argument_list|()
block|,
name|mappedHeaderName
block|}
argument_list|)
expr_stmt|;
name|answer
operator|.
name|putSingle
argument_list|(
name|mappedHeaderName
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
comment|/**      * This method call Message.getBody({@link MessageContentsList}) to allow      * an appropriate converter to kick in even through we only read the first      * element off the MessageContextList.  If that returns null, we check        * the body to see if it is a List or an array and then return the first       * element.  If that fails, we will simply return the object.      */
DECL|method|bindCamelMessageBodyToRequestBody (Message camelMessage, Exchange camelExchange)
specifier|public
name|Object
name|bindCamelMessageBodyToRequestBody
parameter_list|(
name|Message
name|camelMessage
parameter_list|,
name|Exchange
name|camelExchange
parameter_list|)
throws|throws
name|Exception
block|{
name|Object
name|request
init|=
name|camelMessage
operator|.
name|getBody
argument_list|(
name|MessageContentsList
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|request
operator|!=
literal|null
condition|)
block|{
return|return
operator|(
operator|(
name|MessageContentsList
operator|)
name|request
operator|)
operator|.
name|get
argument_list|(
literal|0
argument_list|)
return|;
block|}
name|request
operator|=
name|camelMessage
operator|.
name|getBody
argument_list|()
expr_stmt|;
if|if
condition|(
name|request
operator|instanceof
name|List
condition|)
block|{
name|request
operator|=
operator|(
operator|(
name|List
argument_list|<
name|?
argument_list|>
operator|)
name|request
operator|)
operator|.
name|get
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|request
operator|!=
literal|null
operator|&&
name|request
operator|.
name|getClass
argument_list|()
operator|.
name|isArray
argument_list|()
condition|)
block|{
name|request
operator|=
operator|(
operator|(
name|Object
index|[]
operator|)
name|request
operator|)
index|[
literal|0
index|]
expr_stmt|;
block|}
return|return
name|request
return|;
block|}
comment|/**      * We will return an empty Map unless the response parameter is a {@link Response} object.       */
DECL|method|bindResponseHeadersToCamelHeaders (Object response, Exchange camelExchange)
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|bindResponseHeadersToCamelHeaders
parameter_list|(
name|Object
name|response
parameter_list|,
name|Exchange
name|camelExchange
parameter_list|)
throws|throws
name|Exception
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|answer
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
if|if
condition|(
name|response
operator|instanceof
name|Response
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
name|Object
argument_list|>
argument_list|>
name|entry
range|:
operator|(
operator|(
name|Response
operator|)
name|response
operator|)
operator|.
name|getMetadata
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
operator|!
name|headerFilterStrategy
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
name|String
name|mappedHeaderName
init|=
name|cxfToCamelHeaderMap
operator|.
name|get
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|mappedHeaderName
operator|==
literal|null
condition|)
block|{
name|mappedHeaderName
operator|=
name|entry
operator|.
name|getKey
argument_list|()
expr_stmt|;
block|}
name|LOG
operator|.
name|trace
argument_list|(
literal|"Populate external header {}={} as {}"
argument_list|,
operator|new
name|Object
index|[]
block|{
name|entry
operator|.
name|getKey
argument_list|()
block|,
name|entry
operator|.
name|getValue
argument_list|()
block|,
name|mappedHeaderName
block|}
argument_list|)
expr_stmt|;
name|answer
operator|.
name|put
argument_list|(
name|mappedHeaderName
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
else|else
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Drop external header {}={}"
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
block|}
block|}
block|}
return|return
name|answer
return|;
block|}
comment|/**      *  By default, we just return the response object.       */
DECL|method|bindResponseToCamelBody (Object response, Exchange camelExchange)
specifier|public
name|Object
name|bindResponseToCamelBody
parameter_list|(
name|Object
name|response
parameter_list|,
name|Exchange
name|camelExchange
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|response
return|;
block|}
DECL|method|getHeaderFilterStrategy ()
specifier|public
name|HeaderFilterStrategy
name|getHeaderFilterStrategy
parameter_list|()
block|{
return|return
name|headerFilterStrategy
return|;
block|}
DECL|method|setHeaderFilterStrategy (HeaderFilterStrategy strategy)
specifier|public
name|void
name|setHeaderFilterStrategy
parameter_list|(
name|HeaderFilterStrategy
name|strategy
parameter_list|)
block|{
name|headerFilterStrategy
operator|=
name|strategy
expr_stmt|;
block|}
DECL|method|getCamelToCxfHeaderMap ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getCamelToCxfHeaderMap
parameter_list|()
block|{
return|return
name|camelToCxfHeaderMap
return|;
block|}
DECL|method|setCamelToCxfHeaderMap (Map<String, String> camelToCxfHeaderMap)
specifier|public
name|void
name|setCamelToCxfHeaderMap
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|camelToCxfHeaderMap
parameter_list|)
block|{
name|this
operator|.
name|camelToCxfHeaderMap
operator|=
name|camelToCxfHeaderMap
expr_stmt|;
block|}
DECL|method|getCxfToCamelHeaderMap ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getCxfToCamelHeaderMap
parameter_list|()
block|{
return|return
name|cxfToCamelHeaderMap
return|;
block|}
DECL|method|setCxfToCamelHeaderMap (Map<String, String> cxfToCamelHeaderMap)
specifier|public
name|void
name|setCxfToCamelHeaderMap
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|cxfToCamelHeaderMap
parameter_list|)
block|{
name|this
operator|.
name|cxfToCamelHeaderMap
operator|=
name|cxfToCamelHeaderMap
expr_stmt|;
block|}
DECL|method|copyMessageHeader (org.apache.cxf.message.Message cxfMessage, Message camelMessage, String cxfKey, String camelKey)
specifier|protected
name|void
name|copyMessageHeader
parameter_list|(
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|Message
name|cxfMessage
parameter_list|,
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
if|if
condition|(
name|cxfMessage
operator|.
name|get
argument_list|(
name|cxfKey
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|camelMessage
operator|.
name|setHeader
argument_list|(
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
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|copyProtocolHeader (org.apache.cxf.message.Message cxfMessage, Message camelMessage, Exchange camelExchange)
specifier|protected
name|void
name|copyProtocolHeader
parameter_list|(
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|Message
name|cxfMessage
parameter_list|,
name|Message
name|camelMessage
parameter_list|,
name|Exchange
name|camelExchange
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
name|headers
init|=
operator|(
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
operator|)
name|cxfMessage
operator|.
name|get
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
name|PROTOCOL_HEADERS
argument_list|)
decl_stmt|;
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
name|headers
operator|.
name|entrySet
argument_list|()
control|)
block|{
comment|// just make sure the first String element is not null
if|if
condition|(
name|headerFilterStrategy
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
operator|||
name|entry
operator|.
name|getValue
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Drop CXF message protocol header: {}={}"
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
block|}
else|else
block|{
comment|// just put the first String element, as the complex one is filtered
name|camelMessage
operator|.
name|setHeader
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
block|}
continue|continue;
block|}
block|}
DECL|method|copyOperationResourceInfoStack (org.apache.cxf.message.Message cxfMessage, Message camelMessage)
specifier|protected
name|void
name|copyOperationResourceInfoStack
parameter_list|(
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|Message
name|cxfMessage
parameter_list|,
name|Message
name|camelMessage
parameter_list|)
block|{
name|OperationResourceInfoStack
name|stack
init|=
name|cxfMessage
operator|.
name|get
argument_list|(
name|OperationResourceInfoStack
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|stack
operator|!=
literal|null
condition|)
block|{
comment|// make a copy of the operation resource info for looking up the sub resource location
name|OperationResourceInfoStack
name|copyStack
init|=
operator|(
name|OperationResourceInfoStack
operator|)
name|stack
operator|.
name|clone
argument_list|()
decl_stmt|;
name|camelMessage
operator|.
name|setHeader
argument_list|(
name|CxfConstants
operator|.
name|CAMEL_CXF_RS_OPERATION_RESOURCE_INFO_STACK
argument_list|,
name|copyStack
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

