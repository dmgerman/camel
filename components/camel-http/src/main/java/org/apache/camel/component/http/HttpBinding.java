begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.http
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|http
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
name|InputStream
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
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|ServletOutputStream
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletRequest
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletResponse
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

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|HttpBinding
specifier|public
class|class
name|HttpBinding
block|{
comment|// This should be a set of lower-case strings
DECL|field|DEFAULT_HEADERS_TO_IGNORE
specifier|public
specifier|static
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|DEFAULT_HEADERS_TO_IGNORE
init|=
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
literal|"content-length"
argument_list|,
literal|"content-type"
argument_list|,
name|HttpProducer
operator|.
name|HTTP_RESPONSE_CODE
operator|.
name|toLowerCase
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
DECL|field|ignoredHeaders
specifier|private
name|Set
argument_list|<
name|String
argument_list|>
name|ignoredHeaders
init|=
name|DEFAULT_HEADERS_TO_IGNORE
decl_stmt|;
comment|/**      * Writes the exchange to the servlet response      *       * @param response      * @throws IOException      */
DECL|method|writeResponse (HttpExchange exchange, HttpServletResponse response)
specifier|public
name|void
name|writeResponse
parameter_list|(
name|HttpExchange
name|exchange
parameter_list|,
name|HttpServletResponse
name|response
parameter_list|)
throws|throws
name|IOException
block|{
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
name|out
operator|!=
literal|null
condition|)
block|{
comment|// Write out the headers...
for|for
control|(
name|String
name|key
range|:
name|out
operator|.
name|getHeaders
argument_list|()
operator|.
name|keySet
argument_list|()
control|)
block|{
name|String
name|value
init|=
name|out
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
if|if
condition|(
name|shouldHeaderBePropagated
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
condition|)
block|{
name|response
operator|.
name|setHeader
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
comment|// Write out the body.
if|if
condition|(
name|out
operator|.
name|getBody
argument_list|()
operator|!=
literal|null
condition|)
block|{
comment|// Try to stream the body since that would be the most
comment|// efficient..
name|InputStream
name|is
init|=
name|out
operator|.
name|getBody
argument_list|(
name|InputStream
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|is
operator|!=
literal|null
condition|)
block|{
name|ServletOutputStream
name|os
init|=
name|response
operator|.
name|getOutputStream
argument_list|()
decl_stmt|;
name|int
name|c
decl_stmt|;
while|while
condition|(
operator|(
name|c
operator|=
name|is
operator|.
name|read
argument_list|()
operator|)
operator|>=
literal|0
condition|)
block|{
name|os
operator|.
name|write
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|String
name|data
init|=
name|out
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
name|data
operator|!=
literal|null
condition|)
block|{
name|response
operator|.
name|getWriter
argument_list|()
operator|.
name|print
argument_list|(
name|data
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
comment|/**      * Parses the body from a HTTP message      */
DECL|method|parseBody (HttpMessage httpMessage)
specifier|public
name|Object
name|parseBody
parameter_list|(
name|HttpMessage
name|httpMessage
parameter_list|)
throws|throws
name|IOException
block|{
comment|// lets assume the body is a reader
name|HttpServletRequest
name|request
init|=
name|httpMessage
operator|.
name|getRequest
argument_list|()
decl_stmt|;
return|return
name|request
operator|.
name|getReader
argument_list|()
return|;
block|}
comment|/*      * Exclude a set of headers from responses and new requests as all headers get      * propagated between exchanges by default      */
DECL|method|shouldHeaderBePropagated (String headerName, String headerValue)
specifier|public
name|boolean
name|shouldHeaderBePropagated
parameter_list|(
name|String
name|headerName
parameter_list|,
name|String
name|headerValue
parameter_list|)
block|{
if|if
condition|(
name|headerValue
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|headerName
operator|.
name|startsWith
argument_list|(
literal|"org.apache.camel"
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|getIgnoredHeaders
argument_list|()
operator|.
name|contains
argument_list|(
name|headerName
operator|.
name|toLowerCase
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
literal|true
return|;
block|}
comment|/*      * override the set of headers to ignore for responses and new requests      * @param headersToIgnore should be a set of lower-case strings      */
DECL|method|setIgnoredHeaders (Set<String> headersToIgnore)
specifier|public
name|void
name|setIgnoredHeaders
parameter_list|(
name|Set
argument_list|<
name|String
argument_list|>
name|headersToIgnore
parameter_list|)
block|{
name|ignoredHeaders
operator|=
name|headersToIgnore
expr_stmt|;
block|}
DECL|method|getIgnoredHeaders ()
specifier|public
name|Set
argument_list|<
name|String
argument_list|>
name|getIgnoredHeaders
parameter_list|()
block|{
return|return
name|ignoredHeaders
return|;
block|}
block|}
end_class

end_unit

