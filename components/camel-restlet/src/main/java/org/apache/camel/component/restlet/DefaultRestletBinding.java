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
name|dom
operator|.
name|DOMSource
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
name|HeaderFilterStrategyAware
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
name|RuntimeCamelException
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
name|StringSource
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
name|restlet
operator|.
name|data
operator|.
name|ChallengeResponse
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
name|ChallengeScheme
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
name|Form
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
name|MediaType
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
comment|/**  * Default Restlet binding implementation  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|DefaultRestletBinding
specifier|public
class|class
name|DefaultRestletBinding
implements|implements
name|RestletBinding
implements|,
name|HeaderFilterStrategyAware
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
name|DefaultRestletBinding
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|CAMEL_REQUEST
specifier|private
specifier|static
specifier|final
name|String
name|CAMEL_REQUEST
init|=
literal|"camel.request"
decl_stmt|;
DECL|field|headerFilterStrategy
specifier|private
name|HeaderFilterStrategy
name|headerFilterStrategy
decl_stmt|;
comment|/**      * Populate Camel message from Restlet request      *       * @param request message to be copied from      * @param exchange to be populated      * @throws Exception       */
DECL|method|populateExchangeFromRestletRequest (Request request, Exchange exchange)
specifier|public
name|void
name|populateExchangeFromRestletRequest
parameter_list|(
name|Request
name|request
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
comment|// extract headers from restlet
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
name|request
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
argument_list|)
condition|)
block|{
name|exchange
operator|.
name|getIn
argument_list|()
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
literal|"Populate exchange from Restlet request header: "
operator|+
name|entry
operator|.
name|getKey
argument_list|()
operator|+
literal|" value: "
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
comment|// extract our header and body
name|Form
name|form
init|=
operator|new
name|Form
argument_list|(
name|request
operator|.
name|getEntity
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|form
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
name|String
argument_list|>
name|entry
range|:
name|form
operator|.
name|getValuesMap
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
name|CAMEL_REQUEST
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
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
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
literal|"Populate exchange from Restlet request body: "
operator|+
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
else|else
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
argument_list|)
condition|)
block|{
name|exchange
operator|.
name|getIn
argument_list|()
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
literal|"Populate exchange from Restlet request user header: "
operator|+
name|entry
operator|.
name|getKey
argument_list|()
operator|+
literal|" value: "
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
block|}
block|}
comment|/**      * Populate Restlet Request from Camel message      *       * @param request to be populated      * @param exchange message to be copied from      */
DECL|method|populateRestletRequestFromExchange (Request request, Exchange exchange)
specifier|public
name|void
name|populateRestletRequestFromExchange
parameter_list|(
name|Request
name|request
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
name|request
operator|.
name|setReferrerRef
argument_list|(
literal|"camel-restlet"
argument_list|)
expr_stmt|;
name|String
name|body
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|Form
name|form
init|=
operator|new
name|Form
argument_list|()
decl_stmt|;
name|form
operator|.
name|add
argument_list|(
name|CAMEL_REQUEST
argument_list|,
name|body
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
literal|"Populate Restlet request from exchange body: "
operator|+
name|body
argument_list|)
expr_stmt|;
block|}
comment|// login and password are filtered by header filter strategy
name|String
name|login
init|=
operator|(
name|String
operator|)
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|RestletConstants
operator|.
name|LOGIN
argument_list|)
decl_stmt|;
name|String
name|password
init|=
operator|(
name|String
operator|)
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|RestletConstants
operator|.
name|PASSWORD
argument_list|)
decl_stmt|;
if|if
condition|(
name|login
operator|!=
literal|null
operator|&&
name|password
operator|!=
literal|null
condition|)
block|{
name|ChallengeResponse
name|authentication
init|=
operator|new
name|ChallengeResponse
argument_list|(
name|ChallengeScheme
operator|.
name|HTTP_BASIC
argument_list|,
name|login
argument_list|,
name|password
argument_list|)
decl_stmt|;
name|request
operator|.
name|setChallengeResponse
argument_list|(
name|authentication
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
literal|"Basic HTTP Authentication has been applied"
argument_list|)
expr_stmt|;
block|}
block|}
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
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeaders
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
argument_list|)
condition|)
block|{
if|if
condition|(
name|entry
operator|.
name|getKey
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"org.restlet."
argument_list|)
condition|)
block|{
comment|// put the org.restlet headers in attributes
name|request
operator|.
name|getAttributes
argument_list|()
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
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// put the user stuff in the form
name|form
operator|.
name|add
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
literal|"Populate Restlet request from exchange header: "
operator|+
name|entry
operator|.
name|getKey
argument_list|()
operator|+
literal|" value: "
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
name|request
operator|.
name|setEntity
argument_list|(
name|form
operator|.
name|getWebRepresentation
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Populate Restlet request from Camel message      *        * @param exchange message to be copied from       * @param response to be populated      */
DECL|method|populateRestletResponseFromExchange (Exchange exchange, Response response)
specifier|public
name|void
name|populateRestletResponseFromExchange
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Response
name|response
parameter_list|)
block|{
name|Object
name|body
init|=
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
name|MediaType
name|mediaType
init|=
name|MediaType
operator|.
name|TEXT_PLAIN
decl_stmt|;
if|if
condition|(
name|body
operator|instanceof
name|String
condition|)
block|{
name|mediaType
operator|=
name|MediaType
operator|.
name|TEXT_PLAIN
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|body
operator|instanceof
name|StringSource
operator|||
name|body
operator|instanceof
name|DOMSource
condition|)
block|{
name|mediaType
operator|=
name|MediaType
operator|.
name|TEXT_XML
expr_stmt|;
block|}
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
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getHeaders
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
argument_list|)
condition|)
block|{
name|response
operator|.
name|getAttributes
argument_list|()
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
literal|"Populate Restlet response from exchange header: "
operator|+
name|entry
operator|.
name|getKey
argument_list|()
operator|+
literal|" value: "
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
name|String
name|text
init|=
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
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
literal|"Populate Restlet response from exchange body: "
operator|+
name|text
argument_list|)
expr_stmt|;
block|}
name|response
operator|.
name|setEntity
argument_list|(
name|text
argument_list|,
name|mediaType
argument_list|)
expr_stmt|;
block|}
comment|/**      * Populate Camel message from Restlet response      *       * @param exchange to be populated      * @param response message to be copied from      * @throws IOException       */
DECL|method|populateExchangeFromRestletResponse (Exchange exchange, Response response)
specifier|public
name|void
name|populateExchangeFromRestletResponse
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Response
name|response
parameter_list|)
throws|throws
name|IOException
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
name|response
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
argument_list|)
condition|)
block|{
name|exchange
operator|.
name|getOut
argument_list|()
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
literal|"Populate exchange from Restlet response header: "
operator|+
name|entry
operator|.
name|getKey
argument_list|()
operator|+
literal|" value: "
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
name|String
name|text
init|=
name|response
operator|.
name|getEntity
argument_list|()
operator|.
name|getText
argument_list|()
decl_stmt|;
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
literal|"Populate exchange from Restlet response: "
operator|+
name|text
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|exchange
operator|.
name|getPattern
argument_list|()
operator|.
name|isOutCapable
argument_list|()
condition|)
block|{
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|text
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
literal|"Exchange is incapable of receiving response: "
operator|+
name|exchange
argument_list|)
throw|;
block|}
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
block|}
end_class

end_unit

