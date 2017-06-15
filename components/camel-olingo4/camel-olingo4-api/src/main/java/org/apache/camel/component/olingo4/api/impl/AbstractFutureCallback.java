begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.olingo4.api.impl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|olingo4
operator|.
name|api
operator|.
name|impl
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Closeable
import|;
end_import

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
name|StringWriter
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
name|component
operator|.
name|olingo4
operator|.
name|api
operator|.
name|Olingo4ResponseHandler
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
name|http
operator|.
name|HttpHeaders
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
name|HttpResponse
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
name|StatusLine
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
name|concurrent
operator|.
name|FutureCallback
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|olingo
operator|.
name|client
operator|.
name|api
operator|.
name|ODataClient
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|olingo
operator|.
name|client
operator|.
name|api
operator|.
name|communication
operator|.
name|ODataClientErrorException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|olingo
operator|.
name|client
operator|.
name|api
operator|.
name|serialization
operator|.
name|ODataReader
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|olingo
operator|.
name|client
operator|.
name|core
operator|.
name|ODataClientFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|olingo
operator|.
name|client
operator|.
name|core
operator|.
name|serialization
operator|.
name|ODataReaderImpl
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|olingo
operator|.
name|commons
operator|.
name|api
operator|.
name|ex
operator|.
name|ODataError
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|olingo
operator|.
name|commons
operator|.
name|api
operator|.
name|ex
operator|.
name|ODataException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|olingo
operator|.
name|commons
operator|.
name|api
operator|.
name|format
operator|.
name|ContentType
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|olingo
operator|.
name|commons
operator|.
name|api
operator|.
name|http
operator|.
name|HttpStatusCode
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
name|olingo4
operator|.
name|api
operator|.
name|impl
operator|.
name|Olingo4Helper
operator|.
name|getContentTypeHeader
import|;
end_import

begin_comment
comment|/** * Helper implementation of {@link org.apache.http.concurrent.FutureCallback}  * for {@link org.apache.camel.component.olingo4.api.impl.Olingo4AppImpl} */
end_comment

begin_class
DECL|class|AbstractFutureCallback
specifier|public
specifier|abstract
class|class
name|AbstractFutureCallback
parameter_list|<
name|T
parameter_list|>
implements|implements
name|FutureCallback
argument_list|<
name|HttpResponse
argument_list|>
block|{
DECL|field|ODATA_MIME_TYPE_PATTERN
specifier|public
specifier|static
specifier|final
name|Pattern
name|ODATA_MIME_TYPE_PATTERN
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"application/((atom)|(json)|(xml)).*"
argument_list|)
decl_stmt|;
DECL|field|NETWORK_CONNECT_TIMEOUT_ERROR
specifier|public
specifier|static
specifier|final
name|int
name|NETWORK_CONNECT_TIMEOUT_ERROR
init|=
literal|599
decl_stmt|;
DECL|field|responseHandler
specifier|private
specifier|final
name|Olingo4ResponseHandler
argument_list|<
name|T
argument_list|>
name|responseHandler
decl_stmt|;
DECL|method|AbstractFutureCallback (Olingo4ResponseHandler<T> responseHandler)
name|AbstractFutureCallback
parameter_list|(
name|Olingo4ResponseHandler
argument_list|<
name|T
argument_list|>
name|responseHandler
parameter_list|)
block|{
name|this
operator|.
name|responseHandler
operator|=
name|responseHandler
expr_stmt|;
block|}
DECL|method|checkStatus (HttpResponse response)
specifier|public
specifier|static
name|HttpStatusCode
name|checkStatus
parameter_list|(
name|HttpResponse
name|response
parameter_list|)
throws|throws
name|ODataException
throws|,
name|ODataClientErrorException
block|{
specifier|final
name|StatusLine
name|statusLine
init|=
name|response
operator|.
name|getStatusLine
argument_list|()
decl_stmt|;
name|HttpStatusCode
name|httpStatusCode
init|=
name|HttpStatusCode
operator|.
name|fromStatusCode
argument_list|(
name|statusLine
operator|.
name|getStatusCode
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|HttpStatusCode
operator|.
name|BAD_REQUEST
operator|.
name|getStatusCode
argument_list|()
operator|<=
name|httpStatusCode
operator|.
name|getStatusCode
argument_list|()
operator|&&
name|httpStatusCode
operator|.
name|getStatusCode
argument_list|()
operator|<=
name|NETWORK_CONNECT_TIMEOUT_ERROR
condition|)
block|{
if|if
condition|(
name|response
operator|.
name|getEntity
argument_list|()
operator|!=
literal|null
condition|)
block|{
try|try
block|{
specifier|final
name|ContentType
name|responseContentType
init|=
name|getContentTypeHeader
argument_list|(
name|response
argument_list|)
decl_stmt|;
if|if
condition|(
name|ODATA_MIME_TYPE_PATTERN
operator|.
name|matcher
argument_list|(
name|responseContentType
operator|.
name|toContentTypeString
argument_list|()
argument_list|)
operator|.
name|matches
argument_list|()
condition|)
block|{
specifier|final
name|ODataReader
name|reader
init|=
name|ODataClientFactory
operator|.
name|getClient
argument_list|()
operator|.
name|getReader
argument_list|()
decl_stmt|;
specifier|final
name|ODataError
name|error
init|=
name|reader
operator|.
name|readError
argument_list|(
name|response
operator|.
name|getEntity
argument_list|()
operator|.
name|getContent
argument_list|()
argument_list|,
name|responseContentType
argument_list|)
decl_stmt|;
throw|throw
operator|new
name|ODataClientErrorException
argument_list|(
name|statusLine
argument_list|,
name|error
argument_list|)
throw|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ODataException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
throw|throw
operator|new
name|ODataException
argument_list|(
name|statusLine
operator|.
name|getReasonPhrase
argument_list|()
argument_list|)
throw|;
block|}
return|return
name|httpStatusCode
return|;
block|}
annotation|@
name|Override
DECL|method|completed (HttpResponse result)
specifier|public
specifier|final
name|void
name|completed
parameter_list|(
name|HttpResponse
name|result
parameter_list|)
block|{
try|try
block|{
comment|// check response status
name|checkStatus
argument_list|(
name|result
argument_list|)
expr_stmt|;
name|onCompleted
argument_list|(
name|result
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|failed
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|result
operator|instanceof
name|Closeable
condition|)
block|{
try|try
block|{
operator|(
operator|(
name|Closeable
operator|)
name|result
operator|)
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
specifier|final
name|IOException
name|ignore
parameter_list|)
block|{                 }
block|}
block|}
block|}
DECL|method|onCompleted (HttpResponse result)
specifier|protected
specifier|abstract
name|void
name|onCompleted
parameter_list|(
name|HttpResponse
name|result
parameter_list|)
throws|throws
name|ODataException
throws|,
name|IOException
function_decl|;
annotation|@
name|Override
DECL|method|failed (Exception ex)
specifier|public
specifier|final
name|void
name|failed
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|responseHandler
operator|.
name|onException
argument_list|(
name|ex
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|cancelled ()
specifier|public
specifier|final
name|void
name|cancelled
parameter_list|()
block|{
name|responseHandler
operator|.
name|onCanceled
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

