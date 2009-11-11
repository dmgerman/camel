begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jetty
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jetty
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
name|http
operator|.
name|HttpHeaderFilterStrategy
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

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|DefaultJettyHttpBinding
specifier|public
class|class
name|DefaultJettyHttpBinding
implements|implements
name|JettyHttpBinding
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|DefaultJettyHttpBinding
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|headerFilterStrategy
specifier|private
name|HeaderFilterStrategy
name|headerFilterStrategy
init|=
operator|new
name|HttpHeaderFilterStrategy
argument_list|()
decl_stmt|;
DECL|field|throwExceptionOnFailure
specifier|private
name|boolean
name|throwExceptionOnFailure
decl_stmt|;
DECL|method|populateResponse (Exchange exchange, JettyContentExchange httpExchange)
specifier|public
name|void
name|populateResponse
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|JettyContentExchange
name|httpExchange
parameter_list|)
throws|throws
name|Exception
block|{
name|int
name|responseCode
init|=
name|httpExchange
operator|.
name|getResponseStatus
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
literal|"HTTP responseCode: "
operator|+
name|responseCode
argument_list|)
expr_stmt|;
block|}
name|Message
name|in
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|isThrowExceptionOnFailure
argument_list|()
condition|)
block|{
comment|// if we do not use failed exception then populate response for all response codes
name|populateResponse
argument_list|(
name|exchange
argument_list|,
name|httpExchange
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
argument_list|,
name|getHeaderFilterStrategy
argument_list|()
argument_list|,
name|responseCode
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|responseCode
operator|>=
literal|100
operator|&&
name|responseCode
operator|<
literal|300
condition|)
block|{
comment|// only populate response for OK response
name|populateResponse
argument_list|(
name|exchange
argument_list|,
name|httpExchange
argument_list|,
name|in
argument_list|,
name|getHeaderFilterStrategy
argument_list|()
argument_list|,
name|responseCode
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// operation failed so populate exception to throw
throw|throw
name|populateHttpOperationFailedException
argument_list|(
name|exchange
argument_list|,
name|httpExchange
argument_list|,
name|responseCode
argument_list|)
throw|;
block|}
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
DECL|method|setHeaderFilterStrategy (HeaderFilterStrategy headerFilterStrategy)
specifier|public
name|void
name|setHeaderFilterStrategy
parameter_list|(
name|HeaderFilterStrategy
name|headerFilterStrategy
parameter_list|)
block|{
name|this
operator|.
name|headerFilterStrategy
operator|=
name|headerFilterStrategy
expr_stmt|;
block|}
DECL|method|isThrowExceptionOnFailure ()
specifier|public
name|boolean
name|isThrowExceptionOnFailure
parameter_list|()
block|{
return|return
name|throwExceptionOnFailure
return|;
block|}
DECL|method|setThrowExceptionOnFailure (boolean throwExceptionOnFailure)
specifier|public
name|void
name|setThrowExceptionOnFailure
parameter_list|(
name|boolean
name|throwExceptionOnFailure
parameter_list|)
block|{
name|this
operator|.
name|throwExceptionOnFailure
operator|=
name|throwExceptionOnFailure
expr_stmt|;
block|}
DECL|method|populateResponse (Exchange exchange, JettyContentExchange httpExchange, Message in, HeaderFilterStrategy strategy, int responseCode)
specifier|protected
name|void
name|populateResponse
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|JettyContentExchange
name|httpExchange
parameter_list|,
name|Message
name|in
parameter_list|,
name|HeaderFilterStrategy
name|strategy
parameter_list|,
name|int
name|responseCode
parameter_list|)
throws|throws
name|IOException
block|{
name|Message
name|answer
init|=
name|exchange
operator|.
name|getOut
argument_list|()
decl_stmt|;
name|answer
operator|.
name|setHeaders
argument_list|(
name|in
operator|.
name|getHeaders
argument_list|()
argument_list|)
expr_stmt|;
name|answer
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|HTTP_RESPONSE_CODE
argument_list|,
name|responseCode
argument_list|)
expr_stmt|;
name|answer
operator|.
name|setBody
argument_list|(
name|httpExchange
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
comment|// propagate HTTP response headers
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
name|httpExchange
operator|.
name|getHeaders
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|String
name|name
init|=
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|Object
name|value
init|=
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
if|if
condition|(
name|name
operator|.
name|toLowerCase
argument_list|()
operator|.
name|equals
argument_list|(
literal|"content-type"
argument_list|)
condition|)
block|{
name|name
operator|=
name|Exchange
operator|.
name|CONTENT_TYPE
expr_stmt|;
block|}
if|if
condition|(
name|strategy
operator|!=
literal|null
operator|&&
operator|!
name|strategy
operator|.
name|applyFilterToExternalHeaders
argument_list|(
name|name
argument_list|,
name|value
argument_list|,
name|exchange
argument_list|)
condition|)
block|{
name|answer
operator|.
name|setHeader
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|populateHttpOperationFailedException (Exchange exchange, JettyContentExchange httpExchange, int responseCode)
specifier|protected
name|JettyHttpOperationFailedException
name|populateHttpOperationFailedException
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|JettyContentExchange
name|httpExchange
parameter_list|,
name|int
name|responseCode
parameter_list|)
throws|throws
name|IOException
block|{
name|JettyHttpOperationFailedException
name|exception
decl_stmt|;
name|String
name|uri
init|=
name|httpExchange
operator|.
name|getUrl
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
init|=
name|httpExchange
operator|.
name|getHeaders
argument_list|()
decl_stmt|;
name|String
name|body
init|=
name|httpExchange
operator|.
name|getBody
argument_list|()
decl_stmt|;
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
name|locationHeader
init|=
name|httpExchange
operator|.
name|getResponseFields
argument_list|()
operator|.
name|getStringField
argument_list|(
literal|"location"
argument_list|)
decl_stmt|;
if|if
condition|(
name|locationHeader
operator|!=
literal|null
condition|)
block|{
name|exception
operator|=
operator|new
name|JettyHttpOperationFailedException
argument_list|(
name|uri
argument_list|,
name|responseCode
argument_list|,
name|locationHeader
argument_list|,
name|headers
argument_list|,
name|body
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// no redirect location
name|exception
operator|=
operator|new
name|JettyHttpOperationFailedException
argument_list|(
name|uri
argument_list|,
name|responseCode
argument_list|,
name|headers
argument_list|,
name|body
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
comment|// internal server error (error code 500)
name|exception
operator|=
operator|new
name|JettyHttpOperationFailedException
argument_list|(
name|uri
argument_list|,
name|responseCode
argument_list|,
name|headers
argument_list|,
name|body
argument_list|)
expr_stmt|;
block|}
return|return
name|exception
return|;
block|}
block|}
end_class

end_unit

