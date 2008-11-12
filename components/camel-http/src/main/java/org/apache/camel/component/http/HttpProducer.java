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
name|io
operator|.
name|UnsupportedEncodingException
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
name|NoTypeConversionAvailableException
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
name|component
operator|.
name|http
operator|.
name|helper
operator|.
name|LoadingByteArrayOutputStream
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
name|util
operator|.
name|ObjectHelper
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
name|httpclient
operator|.
name|Header
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
name|httpclient
operator|.
name|HttpClient
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
name|httpclient
operator|.
name|HttpMethod
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
name|httpclient
operator|.
name|methods
operator|.
name|EntityEnclosingMethod
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
name|httpclient
operator|.
name|methods
operator|.
name|RequestEntity
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
name|httpclient
operator|.
name|methods
operator|.
name|StringRequestEntity
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
name|io
operator|.
name|IOUtils
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
import|import static
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
name|HttpMethods
operator|.
name|HTTP_METHOD
import|;
end_import

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|HttpProducer
specifier|public
class|class
name|HttpProducer
extends|extends
name|DefaultProducer
block|{
DECL|field|HTTP_URI
specifier|public
specifier|static
specifier|final
name|String
name|HTTP_URI
init|=
literal|"http.uri"
decl_stmt|;
DECL|field|HTTP_RESPONSE_CODE
specifier|public
specifier|static
specifier|final
name|String
name|HTTP_RESPONSE_CODE
init|=
literal|"http.responseCode"
decl_stmt|;
DECL|field|QUERY
specifier|public
specifier|static
specifier|final
name|String
name|QUERY
init|=
literal|"org.apache.camel.component.http.query"
decl_stmt|;
comment|// This should be a set of lower-case strings
annotation|@
name|Deprecated
DECL|field|HEADERS_TO_SKIP
specifier|public
specifier|static
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|HEADERS_TO_SKIP
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
name|HTTP_RESPONSE_CODE
operator|.
name|toLowerCase
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
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
name|HttpProducer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|httpClient
specifier|private
name|HttpClient
name|httpClient
decl_stmt|;
DECL|method|HttpProducer (HttpEndpoint endpoint)
specifier|public
name|HttpProducer
parameter_list|(
name|HttpEndpoint
name|endpoint
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|httpClient
operator|=
name|endpoint
operator|.
name|createHttpClient
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
name|HttpMethod
name|method
init|=
name|createMethod
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|Message
name|in
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|HeaderFilterStrategy
name|strategy
init|=
operator|(
operator|(
name|HttpEndpoint
operator|)
name|getEndpoint
argument_list|()
operator|)
operator|.
name|getHeaderFilterStrategy
argument_list|()
decl_stmt|;
comment|// propagate headers as HTTP headers
for|for
control|(
name|String
name|headerName
range|:
name|in
operator|.
name|getHeaders
argument_list|()
operator|.
name|keySet
argument_list|()
control|)
block|{
name|String
name|headerValue
init|=
name|in
operator|.
name|getHeader
argument_list|(
name|headerName
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|strategy
operator|!=
literal|null
operator|&&
operator|!
name|strategy
operator|.
name|applyFilterToCamelHeaders
argument_list|(
name|headerName
argument_list|,
name|headerValue
argument_list|)
condition|)
block|{
name|method
operator|.
name|addRequestHeader
argument_list|(
name|headerName
argument_list|,
name|headerValue
argument_list|)
expr_stmt|;
block|}
block|}
comment|// lets store the result in the output message.
try|try
block|{
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
literal|"Executing http "
operator|+
name|method
operator|.
name|getName
argument_list|()
operator|+
literal|" method: "
operator|+
name|method
operator|.
name|getURI
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|int
name|responseCode
init|=
name|executeMethod
argument_list|(
name|method
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
literal|"Http responseCode: "
operator|+
name|responseCode
argument_list|)
expr_stmt|;
block|}
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
name|Message
name|answer
init|=
name|exchange
operator|.
name|getOut
argument_list|(
literal|true
argument_list|)
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
name|HTTP_RESPONSE_CODE
argument_list|,
name|responseCode
argument_list|)
expr_stmt|;
name|answer
operator|.
name|setBody
argument_list|(
name|extractResponseBody
argument_list|(
name|method
argument_list|)
argument_list|)
expr_stmt|;
comment|// propagate HTTP response headers
name|Header
index|[]
name|headers
init|=
name|method
operator|.
name|getResponseHeaders
argument_list|()
decl_stmt|;
for|for
control|(
name|Header
name|header
range|:
name|headers
control|)
block|{
name|String
name|name
init|=
name|header
operator|.
name|getName
argument_list|()
decl_stmt|;
name|String
name|value
init|=
name|header
operator|.
name|getValue
argument_list|()
decl_stmt|;
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
else|else
block|{
name|HttpOperationFailedException
name|exception
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|responseCode
operator|<
literal|400
operator|&&
name|responseCode
operator|>=
literal|300
condition|)
block|{
name|String
name|redirectLocation
decl_stmt|;
name|Header
name|locationHeader
init|=
name|method
operator|.
name|getResponseHeader
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
name|redirectLocation
operator|=
name|locationHeader
operator|.
name|getValue
argument_list|()
expr_stmt|;
name|exception
operator|=
operator|new
name|HttpOperationFailedException
argument_list|(
name|responseCode
argument_list|,
name|method
operator|.
name|getStatusLine
argument_list|()
argument_list|,
name|redirectLocation
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|exception
operator|=
operator|new
name|HttpOperationFailedException
argument_list|(
name|responseCode
argument_list|,
name|method
operator|.
name|getStatusLine
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|exception
operator|!=
literal|null
condition|)
block|{
throw|throw
name|exception
throw|;
block|}
block|}
block|}
finally|finally
block|{
name|method
operator|.
name|releaseConnection
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|getHttpClient ()
specifier|public
name|HttpClient
name|getHttpClient
parameter_list|()
block|{
return|return
name|httpClient
return|;
block|}
DECL|method|setHttpClient (HttpClient httpClient)
specifier|public
name|void
name|setHttpClient
parameter_list|(
name|HttpClient
name|httpClient
parameter_list|)
block|{
name|this
operator|.
name|httpClient
operator|=
name|httpClient
expr_stmt|;
block|}
comment|/**      * Strategy when executing the method (calling the remote server).      *      * @param method    the method to execute      * @return the response code      * @throws IOException can be thrown      */
DECL|method|executeMethod (HttpMethod method)
specifier|protected
name|int
name|executeMethod
parameter_list|(
name|HttpMethod
name|method
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|httpClient
operator|.
name|executeMethod
argument_list|(
name|method
argument_list|)
return|;
block|}
DECL|method|extractResponseBody (HttpMethod method)
specifier|protected
specifier|static
name|InputStream
name|extractResponseBody
parameter_list|(
name|HttpMethod
name|method
parameter_list|)
throws|throws
name|IOException
block|{
name|LoadingByteArrayOutputStream
name|bos
init|=
literal|null
decl_stmt|;
name|InputStream
name|is
init|=
literal|null
decl_stmt|;
try|try
block|{
name|bos
operator|=
operator|new
name|LoadingByteArrayOutputStream
argument_list|()
expr_stmt|;
name|is
operator|=
name|method
operator|.
name|getResponseBodyAsStream
argument_list|()
expr_stmt|;
name|IOUtils
operator|.
name|copy
argument_list|(
name|is
argument_list|,
name|bos
argument_list|)
expr_stmt|;
name|bos
operator|.
name|flush
argument_list|()
expr_stmt|;
return|return
name|bos
operator|.
name|createInputStream
argument_list|()
return|;
block|}
finally|finally
block|{
name|ObjectHelper
operator|.
name|close
argument_list|(
name|is
argument_list|,
literal|"Extracting response body"
argument_list|,
name|LOG
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|close
argument_list|(
name|bos
argument_list|,
literal|"Extracting response body"
argument_list|,
name|LOG
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|createMethod (Exchange exchange)
specifier|protected
name|HttpMethod
name|createMethod
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
comment|// is a query string provided in the endpoint URI or in a header (header overrules endpoint)
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
name|QUERY
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|queryString
operator|==
literal|null
condition|)
block|{
name|queryString
operator|=
operator|(
operator|(
name|HttpEndpoint
operator|)
name|getEndpoint
argument_list|()
operator|)
operator|.
name|getHttpUri
argument_list|()
operator|.
name|getQuery
argument_list|()
expr_stmt|;
block|}
name|RequestEntity
name|requestEntity
init|=
name|createRequestEntity
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
comment|// compute what method to use either GET or POST
name|HttpMethods
name|methodToUse
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
name|methodToUse
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
name|methodToUse
operator|=
name|HttpMethods
operator|.
name|GET
expr_stmt|;
block|}
else|else
block|{
comment|// fallback to POST if data, otherwise GET
name|methodToUse
operator|=
name|requestEntity
operator|!=
literal|null
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
name|String
name|uri
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|HTTP_URI
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|uri
operator|==
literal|null
condition|)
block|{
name|uri
operator|=
operator|(
operator|(
name|HttpEndpoint
operator|)
name|getEndpoint
argument_list|()
operator|)
operator|.
name|getHttpUri
argument_list|()
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
name|HttpMethod
name|method
init|=
name|methodToUse
operator|.
name|createMethod
argument_list|(
name|uri
argument_list|)
decl_stmt|;
if|if
condition|(
name|queryString
operator|!=
literal|null
condition|)
block|{
name|method
operator|.
name|setQueryString
argument_list|(
name|queryString
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|methodToUse
operator|.
name|isEntityEnclosing
argument_list|()
condition|)
block|{
operator|(
operator|(
name|EntityEnclosingMethod
operator|)
name|method
operator|)
operator|.
name|setRequestEntity
argument_list|(
name|requestEntity
argument_list|)
expr_stmt|;
block|}
return|return
name|method
return|;
block|}
DECL|method|createRequestEntity (Exchange exchange)
specifier|protected
name|RequestEntity
name|createRequestEntity
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
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
name|in
operator|.
name|getBody
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
try|try
block|{
return|return
name|in
operator|.
name|getBody
argument_list|(
name|RequestEntity
operator|.
name|class
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|NoTypeConversionAvailableException
name|ex
parameter_list|)
block|{
try|try
block|{
name|String
name|data
init|=
name|in
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
name|String
name|contentType
init|=
name|in
operator|.
name|getHeader
argument_list|(
literal|"Content-Type"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|String
name|charset
init|=
name|exchange
operator|.
name|getProperty
argument_list|(
name|Exchange
operator|.
name|CHARSET_NAME
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
return|return
operator|new
name|StringRequestEntity
argument_list|(
name|data
argument_list|,
name|contentType
argument_list|,
name|charset
argument_list|)
return|;
block|}
else|else
block|{
comment|// no data
return|return
literal|null
return|;
block|}
block|}
catch|catch
parameter_list|(
name|UnsupportedEncodingException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
block|}
block|}
end_class

end_unit

