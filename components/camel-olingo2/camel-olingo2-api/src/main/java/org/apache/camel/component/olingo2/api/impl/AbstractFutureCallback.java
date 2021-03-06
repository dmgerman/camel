begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.olingo2.api.impl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|olingo2
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
name|olingo2
operator|.
name|api
operator|.
name|Olingo2ResponseHandler
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
name|http
operator|.
name|entity
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
name|odata2
operator|.
name|api
operator|.
name|commons
operator|.
name|HttpStatusCodes
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
name|odata2
operator|.
name|api
operator|.
name|ep
operator|.
name|EntityProvider
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
name|odata2
operator|.
name|api
operator|.
name|ep
operator|.
name|EntityProviderException
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
name|odata2
operator|.
name|api
operator|.
name|exception
operator|.
name|ODataApplicationException
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
name|odata2
operator|.
name|api
operator|.
name|exception
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
name|odata2
operator|.
name|api
operator|.
name|processor
operator|.
name|ODataErrorContext
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
name|olingo2
operator|.
name|api
operator|.
name|impl
operator|.
name|Olingo2Helper
operator|.
name|getContentTypeHeader
import|;
end_import

begin_comment
comment|/**  * Helper implementation of {@link org.apache.http.concurrent.FutureCallback}  * for {@link org.apache.camel.component.olingo2.api.impl.Olingo2AppImpl}  */
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
DECL|field|ODATA_MIME_TYPE
specifier|public
specifier|static
specifier|final
name|Pattern
name|ODATA_MIME_TYPE
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"application/((atom)|(json)|(xml)).*"
argument_list|)
decl_stmt|;
DECL|field|responseHandler
specifier|private
specifier|final
name|Olingo2ResponseHandler
argument_list|<
name|T
argument_list|>
name|responseHandler
decl_stmt|;
DECL|method|AbstractFutureCallback (Olingo2ResponseHandler<T> responseHandler)
name|AbstractFutureCallback
parameter_list|(
name|Olingo2ResponseHandler
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
name|HttpStatusCodes
name|checkStatus
parameter_list|(
name|HttpResponse
name|response
parameter_list|)
throws|throws
name|ODataApplicationException
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
name|HttpStatusCodes
name|httpStatusCode
init|=
name|HttpStatusCodes
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
literal|400
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
literal|599
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
name|responseContentType
operator|!=
literal|null
operator|&&
name|ODATA_MIME_TYPE
operator|.
name|matcher
argument_list|(
name|responseContentType
operator|.
name|getMimeType
argument_list|()
argument_list|)
operator|.
name|matches
argument_list|()
condition|)
block|{
specifier|final
name|ODataErrorContext
name|errorContext
init|=
name|EntityProvider
operator|.
name|readErrorDocument
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
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
throw|throw
operator|new
name|ODataApplicationException
argument_list|(
name|errorContext
operator|.
name|getMessage
argument_list|()
argument_list|,
name|errorContext
operator|.
name|getLocale
argument_list|()
argument_list|,
name|httpStatusCode
argument_list|,
name|errorContext
operator|.
name|getErrorCode
argument_list|()
argument_list|,
name|errorContext
operator|.
name|getException
argument_list|()
argument_list|)
throw|;
block|}
block|}
catch|catch
parameter_list|(
name|EntityProviderException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ODataApplicationException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|response
operator|.
name|getLocale
argument_list|()
argument_list|,
name|httpStatusCode
argument_list|,
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ODataApplicationException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|response
operator|.
name|getLocale
argument_list|()
argument_list|,
name|httpStatusCode
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
throw|throw
operator|new
name|ODataApplicationException
argument_list|(
name|statusLine
operator|.
name|getReasonPhrase
argument_list|()
argument_list|,
name|response
operator|.
name|getLocale
argument_list|()
argument_list|,
name|httpStatusCode
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

