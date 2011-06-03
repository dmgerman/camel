begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.ahc
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|ahc
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayOutputStream
import|;
end_import

begin_import
import|import
name|com
operator|.
name|ning
operator|.
name|http
operator|.
name|client
operator|.
name|AsyncHandler
import|;
end_import

begin_import
import|import
name|com
operator|.
name|ning
operator|.
name|http
operator|.
name|client
operator|.
name|AsyncHttpClient
import|;
end_import

begin_import
import|import
name|com
operator|.
name|ning
operator|.
name|http
operator|.
name|client
operator|.
name|HttpResponseBodyPart
import|;
end_import

begin_import
import|import
name|com
operator|.
name|ning
operator|.
name|http
operator|.
name|client
operator|.
name|HttpResponseHeaders
import|;
end_import

begin_import
import|import
name|com
operator|.
name|ning
operator|.
name|http
operator|.
name|client
operator|.
name|HttpResponseStatus
import|;
end_import

begin_import
import|import
name|com
operator|.
name|ning
operator|.
name|http
operator|.
name|client
operator|.
name|Request
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
name|AsyncCallback
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
name|DefaultAsyncProducer
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
DECL|class|AhcProducer
specifier|public
class|class
name|AhcProducer
extends|extends
name|DefaultAsyncProducer
block|{
DECL|field|client
specifier|private
specifier|final
name|AsyncHttpClient
name|client
decl_stmt|;
DECL|method|AhcProducer (AhcEndpoint endpoint)
specifier|public
name|AhcProducer
parameter_list|(
name|AhcEndpoint
name|endpoint
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|this
operator|.
name|client
operator|=
name|endpoint
operator|.
name|getClient
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getEndpoint ()
specifier|public
name|AhcEndpoint
name|getEndpoint
parameter_list|()
block|{
return|return
operator|(
name|AhcEndpoint
operator|)
name|super
operator|.
name|getEndpoint
argument_list|()
return|;
block|}
annotation|@
name|Override
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|process (Exchange exchange, AsyncCallback callback)
specifier|public
name|boolean
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|AsyncCallback
name|callback
parameter_list|)
block|{
try|try
block|{
comment|// AHC supports async processing
name|Request
name|request
init|=
name|getEndpoint
argument_list|()
operator|.
name|getBinding
argument_list|()
operator|.
name|prepareRequest
argument_list|(
name|getEndpoint
argument_list|()
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Executing request {} "
argument_list|,
name|request
argument_list|)
expr_stmt|;
name|client
operator|.
name|prepareRequest
argument_list|(
name|request
argument_list|)
operator|.
name|execute
argument_list|(
operator|new
name|AhcAsyncHandler
argument_list|(
name|exchange
argument_list|,
name|callback
argument_list|,
name|request
operator|.
name|getUrl
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
name|callback
operator|.
name|done
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
block|}
comment|/**      * Camel {@link AsyncHandler} to receive callbacks during the processing of the request.      */
DECL|class|AhcAsyncHandler
specifier|private
specifier|final
class|class
name|AhcAsyncHandler
implements|implements
name|AsyncHandler
argument_list|<
name|Exchange
argument_list|>
block|{
DECL|field|exchange
specifier|private
specifier|final
name|Exchange
name|exchange
decl_stmt|;
DECL|field|callback
specifier|private
specifier|final
name|AsyncCallback
name|callback
decl_stmt|;
DECL|field|url
specifier|private
specifier|final
name|String
name|url
decl_stmt|;
DECL|field|os
specifier|private
specifier|final
name|ByteArrayOutputStream
name|os
decl_stmt|;
DECL|field|contentLength
specifier|private
name|int
name|contentLength
decl_stmt|;
DECL|field|statusCode
specifier|private
name|int
name|statusCode
decl_stmt|;
DECL|field|statusText
specifier|private
name|String
name|statusText
decl_stmt|;
DECL|method|AhcAsyncHandler (Exchange exchange, AsyncCallback callback, String url)
specifier|private
name|AhcAsyncHandler
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|AsyncCallback
name|callback
parameter_list|,
name|String
name|url
parameter_list|)
block|{
name|this
operator|.
name|exchange
operator|=
name|exchange
expr_stmt|;
name|this
operator|.
name|callback
operator|=
name|callback
expr_stmt|;
name|this
operator|.
name|url
operator|=
name|url
expr_stmt|;
name|this
operator|.
name|os
operator|=
operator|new
name|ByteArrayOutputStream
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onThrowable (Throwable t)
specifier|public
name|void
name|onThrowable
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"{} onThrowable {}"
argument_list|,
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|,
name|t
argument_list|)
expr_stmt|;
try|try
block|{
name|getEndpoint
argument_list|()
operator|.
name|getBinding
argument_list|()
operator|.
name|onThrowable
argument_list|(
name|getEndpoint
argument_list|()
argument_list|,
name|exchange
argument_list|,
name|t
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
name|callback
operator|.
name|done
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onBodyPartReceived (HttpResponseBodyPart bodyPart)
specifier|public
name|STATE
name|onBodyPartReceived
parameter_list|(
name|HttpResponseBodyPart
name|bodyPart
parameter_list|)
throws|throws
name|Exception
block|{
comment|// write body parts to stream, which we will bind to the Camel Exchange in onComplete
name|int
name|wrote
init|=
name|bodyPart
operator|.
name|writeTo
argument_list|(
name|os
argument_list|)
decl_stmt|;
name|log
operator|.
name|trace
argument_list|(
literal|"{} onBodyPartReceived {} bytes"
argument_list|,
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|,
name|wrote
argument_list|)
expr_stmt|;
name|contentLength
operator|+=
name|wrote
expr_stmt|;
return|return
name|STATE
operator|.
name|CONTINUE
return|;
block|}
annotation|@
name|Override
DECL|method|onStatusReceived (HttpResponseStatus responseStatus)
specifier|public
name|STATE
name|onStatusReceived
parameter_list|(
name|HttpResponseStatus
name|responseStatus
parameter_list|)
throws|throws
name|Exception
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"{} onStatusReceived {}"
argument_list|,
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|,
name|responseStatus
argument_list|)
expr_stmt|;
try|try
block|{
name|statusCode
operator|=
name|responseStatus
operator|.
name|getStatusCode
argument_list|()
expr_stmt|;
name|statusText
operator|=
name|responseStatus
operator|.
name|getStatusText
argument_list|()
expr_stmt|;
name|getEndpoint
argument_list|()
operator|.
name|getBinding
argument_list|()
operator|.
name|onStatusReceived
argument_list|(
name|getEndpoint
argument_list|()
argument_list|,
name|exchange
argument_list|,
name|responseStatus
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
return|return
name|STATE
operator|.
name|CONTINUE
return|;
block|}
annotation|@
name|Override
DECL|method|onHeadersReceived (HttpResponseHeaders headers)
specifier|public
name|STATE
name|onHeadersReceived
parameter_list|(
name|HttpResponseHeaders
name|headers
parameter_list|)
throws|throws
name|Exception
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"{} onHeadersReceived {}"
argument_list|,
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|,
name|headers
argument_list|)
expr_stmt|;
try|try
block|{
name|getEndpoint
argument_list|()
operator|.
name|getBinding
argument_list|()
operator|.
name|onHeadersReceived
argument_list|(
name|getEndpoint
argument_list|()
argument_list|,
name|exchange
argument_list|,
name|headers
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
return|return
name|STATE
operator|.
name|CONTINUE
return|;
block|}
annotation|@
name|Override
DECL|method|onCompleted ()
specifier|public
name|Exchange
name|onCompleted
parameter_list|()
throws|throws
name|Exception
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"{} onCompleted"
argument_list|,
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|getEndpoint
argument_list|()
operator|.
name|getBinding
argument_list|()
operator|.
name|onComplete
argument_list|(
name|getEndpoint
argument_list|()
argument_list|,
name|exchange
argument_list|,
name|url
argument_list|,
name|os
argument_list|,
name|contentLength
argument_list|,
name|statusCode
argument_list|,
name|statusText
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
comment|// signal we are done
name|callback
operator|.
name|done
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
return|return
name|exchange
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"AhcAsyncHandler for exchangeId: "
operator|+
name|exchange
operator|.
name|getExchangeId
argument_list|()
operator|+
literal|" -> "
operator|+
name|url
return|;
block|}
block|}
block|}
end_class

end_unit

