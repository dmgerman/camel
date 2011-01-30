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
name|ObjectInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ObjectOutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|OutputStream
import|;
end_import

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
name|javax
operator|.
name|servlet
operator|.
name|ServletResponse
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
name|http4
operator|.
name|HttpConstants
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
name|HttpConverter
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
name|converter
operator|.
name|IOConverter
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
name|stream
operator|.
name|CachedOutputStream
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
name|IOHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|HttpVersion
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|ProtocolException
import|;
end_import

begin_class
DECL|class|HttpHelper
specifier|public
specifier|final
class|class
name|HttpHelper
block|{
DECL|method|HttpHelper ()
specifier|private
name|HttpHelper
parameter_list|()
block|{
comment|// Helper class
block|}
DECL|method|setCharsetFromContentType (String contentType, Exchange exchange)
specifier|public
specifier|static
name|void
name|setCharsetFromContentType
parameter_list|(
name|String
name|contentType
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
if|if
condition|(
name|contentType
operator|!=
literal|null
condition|)
block|{
comment|// find the charset and set it to the Exchange
name|int
name|index
init|=
name|contentType
operator|.
name|indexOf
argument_list|(
literal|"charset="
argument_list|)
decl_stmt|;
if|if
condition|(
name|index
operator|>
literal|0
condition|)
block|{
name|String
name|charset
init|=
name|contentType
operator|.
name|substring
argument_list|(
name|index
operator|+
literal|8
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|CHARSET_NAME
argument_list|,
name|IOConverter
operator|.
name|normalizeCharset
argument_list|(
name|charset
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Writes the given object as response body to the servlet response      *<p/>      * The content type will be set to {@link HttpConstants#CONTENT_TYPE_JAVA_SERIALIZED_OBJECT}      *      * @param response servlet response      * @param target   object to write      * @throws IOException is thrown if error writing      */
DECL|method|writeObjectToServletResponse (ServletResponse response, Object target)
specifier|public
specifier|static
name|void
name|writeObjectToServletResponse
parameter_list|(
name|ServletResponse
name|response
parameter_list|,
name|Object
name|target
parameter_list|)
throws|throws
name|IOException
block|{
name|response
operator|.
name|setContentType
argument_list|(
name|HttpConstants
operator|.
name|CONTENT_TYPE_JAVA_SERIALIZED_OBJECT
argument_list|)
expr_stmt|;
name|writeObjectToStream
argument_list|(
name|response
operator|.
name|getOutputStream
argument_list|()
argument_list|,
name|target
argument_list|)
expr_stmt|;
block|}
comment|/**      * Writes the given object as response body to the output stream      *      * @param stream output stream      * @param target object to write      * @throws IOException is thrown if error writing      */
DECL|method|writeObjectToStream (OutputStream stream, Object target)
specifier|public
specifier|static
name|void
name|writeObjectToStream
parameter_list|(
name|OutputStream
name|stream
parameter_list|,
name|Object
name|target
parameter_list|)
throws|throws
name|IOException
block|{
name|ObjectOutputStream
name|oos
init|=
operator|new
name|ObjectOutputStream
argument_list|(
name|stream
argument_list|)
decl_stmt|;
name|oos
operator|.
name|writeObject
argument_list|(
name|target
argument_list|)
expr_stmt|;
name|oos
operator|.
name|flush
argument_list|()
expr_stmt|;
name|IOHelper
operator|.
name|close
argument_list|(
name|oos
argument_list|)
expr_stmt|;
block|}
comment|/**      * Deserializes the input stream to a Java object      *      * @param is input stream for the Java object      * @return the java object, or<tt>null</tt> if input stream was<tt>null</tt>      * @throws ClassNotFoundException is thrown if class not found      * @throws IOException can be thrown      */
DECL|method|deserializeJavaObjectFromStream (InputStream is)
specifier|public
specifier|static
name|Object
name|deserializeJavaObjectFromStream
parameter_list|(
name|InputStream
name|is
parameter_list|)
throws|throws
name|ClassNotFoundException
throws|,
name|IOException
block|{
if|if
condition|(
name|is
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|Object
name|answer
init|=
literal|null
decl_stmt|;
name|ObjectInputStream
name|ois
init|=
operator|new
name|ObjectInputStream
argument_list|(
name|is
argument_list|)
decl_stmt|;
try|try
block|{
name|answer
operator|=
name|ois
operator|.
name|readObject
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
name|IOHelper
operator|.
name|close
argument_list|(
name|ois
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
comment|/**      * Reads the response body from the given http servlet request.      *      * @param request  http servlet request      * @param exchange the exchange      * @return the response body, can be<tt>null</tt> if no body      * @throws IOException is thrown if error reading response body      */
DECL|method|readResponseBodyFromServletRequest (HttpServletRequest request, Exchange exchange)
specifier|public
specifier|static
name|Object
name|readResponseBodyFromServletRequest
parameter_list|(
name|HttpServletRequest
name|request
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|IOException
block|{
name|InputStream
name|is
init|=
name|HttpConverter
operator|.
name|toInputStream
argument_list|(
name|request
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
return|return
name|readResponseBodyFromInputStream
argument_list|(
name|is
argument_list|,
name|exchange
argument_list|)
return|;
block|}
comment|/**      * Reads the response body from the given input stream.      *      * @param is       the input stream      * @param exchange the exchange      * @return the response body, can be<tt>null</tt> if no body      * @throws IOException is thrown if error reading response body      */
DECL|method|readResponseBodyFromInputStream (InputStream is, Exchange exchange)
specifier|public
specifier|static
name|Object
name|readResponseBodyFromInputStream
parameter_list|(
name|InputStream
name|is
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|is
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
comment|// convert the input stream to StreamCache if the stream cache is not disabled
if|if
condition|(
name|exchange
operator|.
name|getProperty
argument_list|(
name|Exchange
operator|.
name|DISABLE_HTTP_STREAM_CACHE
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
name|is
return|;
block|}
else|else
block|{
name|CachedOutputStream
name|cos
init|=
operator|new
name|CachedOutputStream
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|IOHelper
operator|.
name|copyAndCloseInput
argument_list|(
name|is
argument_list|,
name|cos
argument_list|)
expr_stmt|;
return|return
name|cos
operator|.
name|getStreamCache
argument_list|()
return|;
block|}
block|}
comment|/**      * Creates the URL to invoke.      *      * @param exchange the exchange      * @param endpoint the endpoint      * @return the URL to invoke      */
DECL|method|createURL (Exchange exchange, HttpEndpoint endpoint)
specifier|public
specifier|static
name|String
name|createURL
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|HttpEndpoint
name|endpoint
parameter_list|)
block|{
name|String
name|uri
init|=
literal|null
decl_stmt|;
if|if
condition|(
operator|!
operator|(
name|endpoint
operator|.
name|isBridgeEndpoint
argument_list|()
operator|)
condition|)
block|{
name|uri
operator|=
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
expr_stmt|;
block|}
if|if
condition|(
name|uri
operator|==
literal|null
condition|)
block|{
name|uri
operator|=
name|endpoint
operator|.
name|getHttpUri
argument_list|()
operator|.
name|toASCIIString
argument_list|()
expr_stmt|;
block|}
comment|// append HTTP_PATH to HTTP_URI if it is provided in the header
name|String
name|path
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
name|HTTP_PATH
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|path
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|path
operator|.
name|startsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|URI
name|baseURI
decl_stmt|;
name|String
name|baseURIString
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
name|HTTP_BASE_URI
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
try|try
block|{
if|if
condition|(
name|baseURIString
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|exchange
operator|.
name|getFromEndpoint
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|baseURIString
operator|=
name|exchange
operator|.
name|getFromEndpoint
argument_list|()
operator|.
name|getEndpointUri
argument_list|()
expr_stmt|;
block|}
else|else
block|{
comment|// will set a default one for it
name|baseURIString
operator|=
literal|"/"
expr_stmt|;
block|}
block|}
name|baseURI
operator|=
operator|new
name|URI
argument_list|(
name|baseURIString
argument_list|)
expr_stmt|;
name|String
name|basePath
init|=
name|baseURI
operator|.
name|getRawPath
argument_list|()
decl_stmt|;
if|if
condition|(
name|path
operator|.
name|startsWith
argument_list|(
name|basePath
argument_list|)
condition|)
block|{
name|path
operator|=
name|path
operator|.
name|substring
argument_list|(
name|basePath
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|path
operator|.
name|startsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|path
operator|=
name|path
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
literal|"Cannot analyze the Exchange.HTTP_PATH header, due to: cannot find the right HTTP_BASE_URI"
argument_list|)
throw|;
block|}
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
literal|"Cannot analyze the Exchange.HTTP_PATH header, due to: "
operator|+
name|t
operator|.
name|getMessage
argument_list|()
argument_list|,
name|t
argument_list|)
throw|;
block|}
block|}
if|if
condition|(
name|path
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
comment|// make sure that there is exactly one "/" between HTTP_URI and
comment|// HTTP_PATH
if|if
condition|(
operator|!
name|uri
operator|.
name|endsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|uri
operator|=
name|uri
operator|+
literal|"/"
expr_stmt|;
block|}
name|uri
operator|=
name|uri
operator|.
name|concat
argument_list|(
name|path
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|uri
return|;
block|}
comment|/**      * Creates the HttpMethod to use to call the remote server, often either its GET or POST.      *      * @param exchange the exchange      * @return the created method      */
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
DECL|method|parserHttpVersion (String s)
specifier|public
specifier|static
name|HttpVersion
name|parserHttpVersion
parameter_list|(
name|String
name|s
parameter_list|)
throws|throws
name|ProtocolException
block|{
name|int
name|major
decl_stmt|;
name|int
name|minor
decl_stmt|;
if|if
condition|(
name|s
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"String may not be null"
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
name|s
operator|.
name|startsWith
argument_list|(
literal|"HTTP/"
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|ProtocolException
argument_list|(
literal|"Invalid HTTP version string: "
operator|+
name|s
argument_list|)
throw|;
block|}
name|int
name|i1
init|=
literal|"HTTP/"
operator|.
name|length
argument_list|()
decl_stmt|;
name|int
name|i2
init|=
name|s
operator|.
name|indexOf
argument_list|(
literal|"."
argument_list|,
name|i1
argument_list|)
decl_stmt|;
if|if
condition|(
name|i2
operator|==
operator|-
literal|1
condition|)
block|{
throw|throw
operator|new
name|ProtocolException
argument_list|(
literal|"Invalid HTTP version number: "
operator|+
name|s
argument_list|)
throw|;
block|}
try|try
block|{
name|major
operator|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|s
operator|.
name|substring
argument_list|(
name|i1
argument_list|,
name|i2
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NumberFormatException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ProtocolException
argument_list|(
literal|"Invalid HTTP major version number: "
operator|+
name|s
argument_list|)
throw|;
block|}
name|i1
operator|=
name|i2
operator|+
literal|1
expr_stmt|;
name|i2
operator|=
name|s
operator|.
name|length
argument_list|()
expr_stmt|;
try|try
block|{
name|minor
operator|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|s
operator|.
name|substring
argument_list|(
name|i1
argument_list|,
name|i2
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NumberFormatException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ProtocolException
argument_list|(
literal|"Invalid HTTP minor version number: "
operator|+
name|s
argument_list|)
throw|;
block|}
return|return
operator|new
name|HttpVersion
argument_list|(
name|major
argument_list|,
name|minor
argument_list|)
return|;
block|}
block|}
end_class

end_unit

