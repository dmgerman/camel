begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.http4.helper
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|http4
operator|.
name|helper
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URISyntaxException
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
name|RuntimeExchangeException
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
name|http4
operator|.
name|HttpEndpoint
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
name|http4
operator|.
name|HttpMethods
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
name|UnsafeUriCharactersEncoder
import|;
end_import

begin_class
DECL|class|HttpMethodHelper
specifier|public
specifier|final
class|class
name|HttpMethodHelper
block|{
DECL|method|HttpMethodHelper ()
specifier|private
name|HttpMethodHelper
parameter_list|()
block|{
comment|// Helper class
block|}
comment|/**      * Creates the HttpMethod to use to call the remote server, often either its GET or POST.      *      * @param exchange the exchange      * @return the created method      * @throws URISyntaxException       */
DECL|method|createMethod (Exchange exchange, HttpEndpoint endpoint, boolean hasPayload)
specifier|public
specifier|static
name|HttpMethods
name|createMethod
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|HttpEndpoint
name|endpoint
parameter_list|,
name|boolean
name|hasPayload
parameter_list|)
throws|throws
name|URISyntaxException
block|{
comment|// is a query string provided in the endpoint URI or in a header (header
comment|// overrules endpoint)
name|String
name|queryString
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|Exchange
operator|.
name|HTTP_QUERY
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// We need also check the HTTP_URI header query part
name|String
name|uriString
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|Exchange
operator|.
name|HTTP_URI
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// resolve placeholders in uriString
try|try
block|{
name|uriString
operator|=
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|resolvePropertyPlaceholders
argument_list|(
name|uriString
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeExchangeException
argument_list|(
literal|"Cannot resolve property placeholders with uri: "
operator|+
name|uriString
argument_list|,
name|exchange
argument_list|,
name|e
argument_list|)
throw|;
block|}
if|if
condition|(
name|uriString
operator|!=
literal|null
condition|)
block|{
comment|// in case the URI string contains unsafe characters
name|uriString
operator|=
name|UnsafeUriCharactersEncoder
operator|.
name|encodeHttpURI
argument_list|(
name|uriString
argument_list|)
expr_stmt|;
name|URI
name|uri
init|=
operator|new
name|URI
argument_list|(
name|uriString
argument_list|)
decl_stmt|;
name|queryString
operator|=
name|uri
operator|.
name|getQuery
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|queryString
operator|==
literal|null
condition|)
block|{
name|queryString
operator|=
name|endpoint
operator|.
name|getHttpUri
argument_list|()
operator|.
name|getRawQuery
argument_list|()
expr_stmt|;
block|}
comment|// compute what method to use either GET or POST
name|HttpMethods
name|answer
decl_stmt|;
name|HttpMethods
name|m
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|Exchange
operator|.
name|HTTP_METHOD
argument_list|,
name|HttpMethods
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|m
operator|!=
literal|null
condition|)
block|{
comment|// always use what end-user provides in a header
name|answer
operator|=
name|m
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|queryString
operator|!=
literal|null
condition|)
block|{
comment|// if a query string is provided then use GET
name|answer
operator|=
name|HttpMethods
operator|.
name|GET
expr_stmt|;
block|}
else|else
block|{
comment|// fallback to POST if we have payload, otherwise GET
name|answer
operator|=
name|hasPayload
condition|?
name|HttpMethods
operator|.
name|POST
else|:
name|HttpMethods
operator|.
name|GET
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
block|}
end_class

end_unit

