begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.restlet
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|restlet
package|;
end_package

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
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Matcher
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Pattern
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
name|CamelExchangeException
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
name|impl
operator|.
name|DefaultProducer
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
name|restlet
operator|.
name|Client
import|;
end_import

begin_import
import|import
name|org
operator|.
name|restlet
operator|.
name|Context
import|;
end_import

begin_import
import|import
name|org
operator|.
name|restlet
operator|.
name|data
operator|.
name|Request
import|;
end_import

begin_import
import|import
name|org
operator|.
name|restlet
operator|.
name|data
operator|.
name|Response
import|;
end_import

begin_comment
comment|/**  * A Camel producer that acts as a client to Restlet server.  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|RestletProducer
specifier|public
class|class
name|RestletProducer
extends|extends
name|DefaultProducer
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
name|RestletProducer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|PATTERN
specifier|private
specifier|static
specifier|final
name|Pattern
name|PATTERN
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"\\{([\\w\\.]*)\\}"
argument_list|)
decl_stmt|;
DECL|field|client
specifier|private
name|Client
name|client
decl_stmt|;
DECL|field|throwException
specifier|private
name|boolean
name|throwException
decl_stmt|;
DECL|method|RestletProducer (RestletEndpoint endpoint)
specifier|public
name|RestletProducer
parameter_list|(
name|RestletEndpoint
name|endpoint
parameter_list|)
throws|throws
name|Exception
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|this
operator|.
name|throwException
operator|=
name|endpoint
operator|.
name|isThrowExceptionOnFailure
argument_list|()
expr_stmt|;
name|client
operator|=
operator|new
name|Client
argument_list|(
name|endpoint
operator|.
name|getProtocol
argument_list|()
argument_list|)
expr_stmt|;
name|client
operator|.
name|setContext
argument_list|(
operator|new
name|Context
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doStart ()
specifier|public
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
name|client
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|public
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
name|client
operator|.
name|stop
argument_list|()
expr_stmt|;
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
block|}
DECL|method|process (Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|RestletEndpoint
name|endpoint
init|=
operator|(
name|RestletEndpoint
operator|)
name|getEndpoint
argument_list|()
decl_stmt|;
name|String
name|resourceUri
init|=
name|buildUri
argument_list|(
name|endpoint
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
name|Request
name|request
init|=
operator|new
name|Request
argument_list|(
name|endpoint
operator|.
name|getRestletMethod
argument_list|()
argument_list|,
name|resourceUri
argument_list|)
decl_stmt|;
name|RestletBinding
name|binding
init|=
name|endpoint
operator|.
name|getRestletBinding
argument_list|()
decl_stmt|;
name|binding
operator|.
name|populateRestletRequestFromExchange
argument_list|(
name|request
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Client sends a request (method: "
operator|+
name|request
operator|.
name|getMethod
argument_list|()
operator|+
literal|", uri: "
operator|+
name|resourceUri
operator|+
literal|")"
argument_list|)
expr_stmt|;
block|}
name|Response
name|response
init|=
name|client
operator|.
name|handle
argument_list|(
name|request
argument_list|)
decl_stmt|;
if|if
condition|(
name|throwException
condition|)
block|{
if|if
condition|(
name|response
operator|instanceof
name|Response
condition|)
block|{
name|Integer
name|respCode
init|=
name|response
operator|.
name|getStatus
argument_list|()
operator|.
name|getCode
argument_list|()
decl_stmt|;
if|if
condition|(
name|respCode
operator|>
literal|207
condition|)
block|{
throw|throw
name|populateRestletProducerException
argument_list|(
name|exchange
argument_list|,
name|response
argument_list|,
name|respCode
argument_list|)
throw|;
block|}
block|}
block|}
name|binding
operator|.
name|populateExchangeFromRestletResponse
argument_list|(
name|exchange
argument_list|,
name|response
argument_list|)
expr_stmt|;
block|}
DECL|method|buildUri (RestletEndpoint endpoint, Exchange exchange)
specifier|private
specifier|static
name|String
name|buildUri
parameter_list|(
name|RestletEndpoint
name|endpoint
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|CamelExchangeException
block|{
name|String
name|uri
init|=
name|endpoint
operator|.
name|getProtocol
argument_list|()
operator|+
literal|"://"
operator|+
name|endpoint
operator|.
name|getHost
argument_list|()
operator|+
literal|":"
operator|+
name|endpoint
operator|.
name|getPort
argument_list|()
operator|+
name|endpoint
operator|.
name|getUriPattern
argument_list|()
decl_stmt|;
comment|// substitute { } placeholders in uri and use mandatory headers
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
literal|"Substituting { } placeholders in uri: "
operator|+
name|uri
argument_list|)
expr_stmt|;
block|}
name|Matcher
name|matcher
init|=
name|PATTERN
operator|.
name|matcher
argument_list|(
name|uri
argument_list|)
decl_stmt|;
while|while
condition|(
name|matcher
operator|.
name|find
argument_list|()
condition|)
block|{
name|String
name|key
init|=
name|matcher
operator|.
name|group
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|String
name|header
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|key
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// header should be mandatory
if|if
condition|(
name|header
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CamelExchangeException
argument_list|(
literal|"Header with key: "
operator|+
name|key
operator|+
literal|" not found in Exchange"
argument_list|,
name|exchange
argument_list|)
throw|;
block|}
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
literal|"Replacing: "
operator|+
name|matcher
operator|.
name|group
argument_list|(
literal|0
argument_list|)
operator|+
literal|" with header value: "
operator|+
name|header
argument_list|)
expr_stmt|;
block|}
name|uri
operator|=
name|matcher
operator|.
name|replaceFirst
argument_list|(
name|header
argument_list|)
expr_stmt|;
comment|// we replaced uri so reset and go again
name|matcher
operator|.
name|reset
argument_list|(
name|uri
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Using uri: "
operator|+
name|uri
argument_list|)
expr_stmt|;
block|}
return|return
name|uri
return|;
block|}
DECL|method|populateRestletProducerException (Exchange exchange, Response response, int responseCode)
specifier|protected
name|RestletOperationException
name|populateRestletProducerException
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Response
name|response
parameter_list|,
name|int
name|responseCode
parameter_list|)
block|{
name|RestletOperationException
name|exception
decl_stmt|;
name|String
name|uri
init|=
name|exchange
operator|.
name|getFromEndpoint
argument_list|()
operator|.
name|getEndpointUri
argument_list|()
decl_stmt|;
name|String
name|statusText
init|=
name|response
operator|.
name|getStatus
argument_list|()
operator|.
name|getDescription
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|headers
init|=
name|parseResponseHeaders
argument_list|(
name|response
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
name|String
name|copy
init|=
name|response
operator|.
name|toString
argument_list|()
decl_stmt|;
name|LOG
operator|.
name|warn
argument_list|(
name|headers
argument_list|)
expr_stmt|;
if|if
condition|(
name|responseCode
operator|>=
literal|300
operator|&&
name|responseCode
operator|<
literal|400
condition|)
block|{
name|String
name|redirectLocation
decl_stmt|;
if|if
condition|(
name|response
operator|.
name|getStatus
argument_list|()
operator|.
name|isRedirection
argument_list|()
condition|)
block|{
name|redirectLocation
operator|=
name|response
operator|.
name|getLocationRef
argument_list|()
operator|.
name|getHostIdentifier
argument_list|()
expr_stmt|;
name|exception
operator|=
operator|new
name|RestletOperationException
argument_list|(
name|uri
argument_list|,
name|responseCode
argument_list|,
name|statusText
argument_list|,
name|redirectLocation
argument_list|,
name|headers
argument_list|,
name|copy
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|//no redirect location
name|exception
operator|=
operator|new
name|RestletOperationException
argument_list|(
name|uri
argument_list|,
name|responseCode
argument_list|,
name|statusText
argument_list|,
literal|null
argument_list|,
name|headers
argument_list|,
name|copy
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
comment|//internal server error(error code 500)
name|exception
operator|=
operator|new
name|RestletOperationException
argument_list|(
name|uri
argument_list|,
name|responseCode
argument_list|,
name|statusText
argument_list|,
literal|null
argument_list|,
name|headers
argument_list|,
name|copy
argument_list|)
expr_stmt|;
block|}
return|return
name|exception
return|;
block|}
DECL|method|parseResponseHeaders (Object response, Exchange camelExchange)
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|parseResponseHeaders
parameter_list|(
name|Object
name|response
parameter_list|,
name|Exchange
name|camelExchange
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|answer
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
name|Object
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
name|getAttributes
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
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
literal|"Parse external header "
operator|+
name|entry
operator|.
name|getKey
argument_list|()
operator|+
literal|"="
operator|+
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|LOG
operator|.
name|info
argument_list|(
literal|"Parse external header "
operator|+
name|entry
operator|.
name|getKey
argument_list|()
operator|+
literal|"="
operator|+
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|answer
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
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|answer
return|;
block|}
block|}
end_class

end_unit

