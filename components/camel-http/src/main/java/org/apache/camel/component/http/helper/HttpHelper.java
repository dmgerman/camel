begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.http.helper
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
name|ObjectOutputStream
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
name|component
operator|.
name|http
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
name|http
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
name|ObjectOutputStream
name|oos
init|=
operator|new
name|ObjectOutputStream
argument_list|(
name|response
operator|.
name|getOutputStream
argument_list|()
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
block|}
end_class

end_unit

