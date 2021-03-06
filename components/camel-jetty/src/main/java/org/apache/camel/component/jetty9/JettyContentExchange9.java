begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jetty9
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jetty9
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
name|lang
operator|.
name|reflect
operator|.
name|Method
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|MalformedURLException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|ByteBuffer
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
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
name|java
operator|.
name|util
operator|.
name|TreeMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|CountDownLatch
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeoutException
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
name|CamelExchangeException
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
name|ExchangeTimedOutException
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
name|StreamCache
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
name|jetty
operator|.
name|JettyContentExchange
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
name|jetty
operator|.
name|JettyHttpBinding
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
name|support
operator|.
name|builder
operator|.
name|OutputStreamBuilder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|client
operator|.
name|HttpClient
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|client
operator|.
name|api
operator|.
name|Request
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|client
operator|.
name|api
operator|.
name|Response
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|client
operator|.
name|api
operator|.
name|Result
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|client
operator|.
name|util
operator|.
name|BytesContentProvider
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|client
operator|.
name|util
operator|.
name|InputStreamContentProvider
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|client
operator|.
name|util
operator|.
name|InputStreamResponseListener
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|client
operator|.
name|util
operator|.
name|StringContentProvider
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|http
operator|.
name|HttpFields
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|util
operator|.
name|Callback
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_comment
comment|/**  * Jetty specific exchange which keeps track of the request and response.  */
end_comment

begin_class
DECL|class|JettyContentExchange9
specifier|public
class|class
name|JettyContentExchange9
implements|implements
name|JettyContentExchange
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|JettyContentExchange9
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|exchange
specifier|private
specifier|volatile
name|Exchange
name|exchange
decl_stmt|;
DECL|field|callback
specifier|private
specifier|volatile
name|AsyncCallback
name|callback
decl_stmt|;
DECL|field|jettyBinding
specifier|private
specifier|volatile
name|JettyHttpBinding
name|jettyBinding
decl_stmt|;
DECL|field|client
specifier|private
specifier|volatile
name|HttpClient
name|client
decl_stmt|;
DECL|field|done
specifier|private
specifier|final
name|CountDownLatch
name|done
init|=
operator|new
name|CountDownLatch
argument_list|(
literal|1
argument_list|)
decl_stmt|;
DECL|field|request
specifier|private
name|Request
name|request
decl_stmt|;
DECL|field|response
specifier|private
name|Response
name|response
decl_stmt|;
DECL|field|responseContent
specifier|private
name|byte
index|[]
name|responseContent
decl_stmt|;
DECL|field|requestContentType
specifier|private
name|String
name|requestContentType
decl_stmt|;
DECL|field|supportRedirect
specifier|private
name|boolean
name|supportRedirect
decl_stmt|;
annotation|@
name|Override
DECL|method|init (Exchange exchange, JettyHttpBinding jettyBinding, final HttpClient client, AsyncCallback callback)
specifier|public
name|void
name|init
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|JettyHttpBinding
name|jettyBinding
parameter_list|,
specifier|final
name|HttpClient
name|client
parameter_list|,
name|AsyncCallback
name|callback
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
name|jettyBinding
operator|=
name|jettyBinding
expr_stmt|;
name|this
operator|.
name|client
operator|=
name|client
expr_stmt|;
name|this
operator|.
name|callback
operator|=
name|callback
expr_stmt|;
block|}
DECL|method|onRequestComplete ()
specifier|protected
name|void
name|onRequestComplete
parameter_list|()
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"onRequestComplete"
argument_list|)
expr_stmt|;
name|closeRequestContentSource
argument_list|()
expr_stmt|;
block|}
DECL|method|onResponseComplete (Result result, byte[] content)
specifier|protected
name|void
name|onResponseComplete
parameter_list|(
name|Result
name|result
parameter_list|,
name|byte
index|[]
name|content
parameter_list|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"onResponseComplete"
argument_list|)
expr_stmt|;
name|done
operator|.
name|countDown
argument_list|()
expr_stmt|;
name|this
operator|.
name|response
operator|=
name|result
operator|.
name|getResponse
argument_list|()
expr_stmt|;
name|this
operator|.
name|responseContent
operator|=
name|content
expr_stmt|;
if|if
condition|(
name|callback
operator|==
literal|null
condition|)
block|{
comment|// this is only for the async callback
return|return;
block|}
try|try
block|{
name|jettyBinding
operator|.
name|populateResponse
argument_list|(
name|exchange
argument_list|,
name|this
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
name|callback
operator|.
name|done
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|onExpire ()
specifier|protected
name|void
name|onExpire
parameter_list|()
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"onExpire"
argument_list|)
expr_stmt|;
comment|// need to close the request input stream
name|closeRequestContentSource
argument_list|()
expr_stmt|;
name|doTaskCompleted
argument_list|(
operator|new
name|ExchangeTimedOutException
argument_list|(
name|exchange
argument_list|,
name|client
operator|.
name|getConnectTimeout
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|onException (Throwable ex)
specifier|protected
name|void
name|onException
parameter_list|(
name|Throwable
name|ex
parameter_list|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"onException {}"
argument_list|,
name|ex
argument_list|)
expr_stmt|;
comment|// need to close the request input stream
name|closeRequestContentSource
argument_list|()
expr_stmt|;
name|doTaskCompleted
argument_list|(
name|ex
argument_list|)
expr_stmt|;
block|}
DECL|method|onConnectionFailed (Throwable ex)
specifier|protected
name|void
name|onConnectionFailed
parameter_list|(
name|Throwable
name|ex
parameter_list|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"onConnectionFailed {}"
argument_list|,
name|ex
argument_list|)
expr_stmt|;
comment|// need to close the request input stream
name|closeRequestContentSource
argument_list|()
expr_stmt|;
name|doTaskCompleted
argument_list|(
name|ex
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getBody ()
specifier|public
name|byte
index|[]
name|getBody
parameter_list|()
block|{
comment|// must return the content as raw bytes
return|return
name|getResponseContentBytes
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getUrl ()
specifier|public
name|String
name|getUrl
parameter_list|()
block|{
try|try
block|{
return|return
name|this
operator|.
name|request
operator|.
name|getURI
argument_list|()
operator|.
name|toURL
argument_list|()
operator|.
name|toExternalForm
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|MalformedURLException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalStateException
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
DECL|method|closeRequestContentSource ()
specifier|protected
name|void
name|closeRequestContentSource
parameter_list|()
block|{
name|tryClose
argument_list|(
name|this
operator|.
name|request
operator|.
name|getContent
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|tryClose (Object obj)
specifier|private
name|void
name|tryClose
parameter_list|(
name|Object
name|obj
parameter_list|)
block|{
if|if
condition|(
name|obj
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
name|obj
operator|)
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|// Ignore
block|}
block|}
block|}
DECL|method|doTaskCompleted (Throwable ex)
specifier|protected
name|void
name|doTaskCompleted
parameter_list|(
name|Throwable
name|ex
parameter_list|)
block|{
if|if
condition|(
name|ex
operator|instanceof
name|TimeoutException
condition|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
operator|new
name|ExchangeTimedOutException
argument_list|(
name|exchange
argument_list|,
name|request
operator|.
name|getTimeout
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|exchange
operator|.
name|setException
argument_list|(
operator|new
name|CamelExchangeException
argument_list|(
literal|"JettyClient failed cause by: "
operator|+
name|ex
operator|.
name|getMessage
argument_list|()
argument_list|,
name|exchange
argument_list|,
name|ex
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|done
operator|.
name|countDown
argument_list|()
expr_stmt|;
if|if
condition|(
name|callback
operator|!=
literal|null
condition|)
block|{
comment|// now invoke callback to indicate we are done async
name|callback
operator|.
name|done
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|setRequestContentType (String contentType)
specifier|public
name|void
name|setRequestContentType
parameter_list|(
name|String
name|contentType
parameter_list|)
block|{
name|this
operator|.
name|requestContentType
operator|=
name|contentType
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getResponseStatus ()
specifier|public
name|int
name|getResponseStatus
parameter_list|()
block|{
return|return
name|this
operator|.
name|response
operator|.
name|getStatus
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|setMethod (String method)
specifier|public
name|void
name|setMethod
parameter_list|(
name|String
name|method
parameter_list|)
block|{
name|this
operator|.
name|request
operator|.
name|method
argument_list|(
name|method
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|setTimeout (long timeout)
specifier|public
name|void
name|setTimeout
parameter_list|(
name|long
name|timeout
parameter_list|)
block|{
name|this
operator|.
name|request
operator|.
name|timeout
argument_list|(
name|timeout
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|setURL (String url)
specifier|public
name|void
name|setURL
parameter_list|(
name|String
name|url
parameter_list|)
block|{
name|this
operator|.
name|request
operator|=
name|client
operator|.
name|newRequest
argument_list|(
name|url
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|setRequestContent (byte[] byteArray)
specifier|public
name|void
name|setRequestContent
parameter_list|(
name|byte
index|[]
name|byteArray
parameter_list|)
block|{
name|this
operator|.
name|request
operator|.
name|content
argument_list|(
operator|new
name|BytesContentProvider
argument_list|(
name|byteArray
argument_list|)
argument_list|,
name|this
operator|.
name|requestContentType
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|setRequestContent (String data, String charset)
specifier|public
name|void
name|setRequestContent
parameter_list|(
name|String
name|data
parameter_list|,
name|String
name|charset
parameter_list|)
throws|throws
name|UnsupportedEncodingException
block|{
name|StringContentProvider
name|cp
init|=
name|charset
operator|!=
literal|null
condition|?
operator|new
name|StringContentProvider
argument_list|(
name|data
argument_list|,
name|charset
argument_list|)
else|:
operator|new
name|StringContentProvider
argument_list|(
name|data
argument_list|)
decl_stmt|;
name|this
operator|.
name|request
operator|.
name|content
argument_list|(
name|cp
argument_list|,
name|this
operator|.
name|requestContentType
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|setRequestContent (InputStream ins)
specifier|public
name|void
name|setRequestContent
parameter_list|(
name|InputStream
name|ins
parameter_list|)
block|{
name|this
operator|.
name|request
operator|.
name|content
argument_list|(
operator|new
name|InputStreamContentProvider
argument_list|(
name|ins
argument_list|)
argument_list|,
name|this
operator|.
name|requestContentType
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|setRequestContent (InputStream ins, int contentLength)
specifier|public
name|void
name|setRequestContent
parameter_list|(
name|InputStream
name|ins
parameter_list|,
name|int
name|contentLength
parameter_list|)
block|{
name|this
operator|.
name|request
operator|.
name|content
argument_list|(
operator|new
name|CamelInputStreamContentProvider
argument_list|(
name|ins
argument_list|,
name|contentLength
argument_list|)
argument_list|,
name|this
operator|.
name|requestContentType
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|addRequestHeader (String key, String s)
specifier|public
name|void
name|addRequestHeader
parameter_list|(
name|String
name|key
parameter_list|,
name|String
name|s
parameter_list|)
block|{
name|this
operator|.
name|request
operator|.
name|header
argument_list|(
name|key
argument_list|,
name|s
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|send (HttpClient client)
specifier|public
name|void
name|send
parameter_list|(
name|HttpClient
name|client
parameter_list|)
throws|throws
name|IOException
block|{
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|client
operator|.
name|api
operator|.
name|Request
operator|.
name|Listener
name|listener
init|=
operator|new
name|Request
operator|.
name|Listener
operator|.
name|Adapter
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onSuccess
parameter_list|(
name|Request
name|request
parameter_list|)
block|{
name|onRequestComplete
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|onFailure
parameter_list|(
name|Request
name|request
parameter_list|,
name|Throwable
name|failure
parameter_list|)
block|{
name|onConnectionFailed
argument_list|(
name|failure
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
name|InputStreamResponseListener
name|responseListener
init|=
operator|new
name|InputStreamResponseListener
argument_list|()
block|{
name|OutputStreamBuilder
name|osb
init|=
name|OutputStreamBuilder
operator|.
name|withExchange
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
annotation|@
name|Override
specifier|public
name|void
name|onContent
parameter_list|(
name|Response
name|response
parameter_list|,
name|ByteBuffer
name|content
parameter_list|,
name|Callback
name|callback
parameter_list|)
block|{
name|byte
index|[]
name|buffer
init|=
operator|new
name|byte
index|[
name|content
operator|.
name|limit
argument_list|()
index|]
decl_stmt|;
name|content
operator|.
name|get
argument_list|(
name|buffer
argument_list|)
expr_stmt|;
try|try
block|{
name|osb
operator|.
name|write
argument_list|(
name|buffer
argument_list|)
expr_stmt|;
name|callback
operator|.
name|succeeded
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|callback
operator|.
name|failed
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|onComplete
parameter_list|(
name|Result
name|result
parameter_list|)
block|{
if|if
condition|(
name|result
operator|.
name|isFailed
argument_list|()
condition|)
block|{
name|doTaskCompleted
argument_list|(
name|result
operator|.
name|getFailure
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
try|try
block|{
name|Object
name|content
init|=
name|osb
operator|.
name|build
argument_list|()
decl_stmt|;
if|if
condition|(
name|content
operator|instanceof
name|byte
index|[]
condition|)
block|{
name|onResponseComplete
argument_list|(
name|result
argument_list|,
operator|(
name|byte
index|[]
operator|)
name|content
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|StreamCache
name|cos
init|=
operator|(
name|StreamCache
operator|)
name|content
decl_stmt|;
name|ByteArrayOutputStream
name|baos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|cos
operator|.
name|writeTo
argument_list|(
name|baos
argument_list|)
expr_stmt|;
name|onResponseComplete
argument_list|(
name|result
argument_list|,
name|baos
operator|.
name|toByteArray
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|doTaskCompleted
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
decl_stmt|;
name|request
operator|.
name|followRedirects
argument_list|(
name|supportRedirect
argument_list|)
operator|.
name|listener
argument_list|(
name|listener
argument_list|)
operator|.
name|send
argument_list|(
name|responseListener
argument_list|)
expr_stmt|;
block|}
DECL|method|setResponse (Response response)
specifier|protected
name|void
name|setResponse
parameter_list|(
name|Response
name|response
parameter_list|)
block|{
name|this
operator|.
name|response
operator|=
name|response
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getResponseContentBytes ()
specifier|public
name|byte
index|[]
name|getResponseContentBytes
parameter_list|()
block|{
return|return
name|responseContent
return|;
block|}
DECL|method|getFieldsAsMap (HttpFields fields)
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Collection
argument_list|<
name|String
argument_list|>
argument_list|>
name|getFieldsAsMap
parameter_list|(
name|HttpFields
name|fields
parameter_list|)
block|{
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Collection
argument_list|<
name|String
argument_list|>
argument_list|>
name|result
init|=
operator|new
name|TreeMap
argument_list|<>
argument_list|(
name|String
operator|.
name|CASE_INSENSITIVE_ORDER
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|name
range|:
name|getFieldNamesCollection
argument_list|(
name|fields
argument_list|)
control|)
block|{
name|result
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|fields
operator|.
name|getValuesList
argument_list|(
name|name
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|getFieldNamesCollection (HttpFields fields)
specifier|private
name|Collection
argument_list|<
name|String
argument_list|>
name|getFieldNamesCollection
parameter_list|(
name|HttpFields
name|fields
parameter_list|)
block|{
try|try
block|{
return|return
name|fields
operator|.
name|getFieldNamesCollection
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|NoSuchMethodError
name|e
parameter_list|)
block|{
try|try
block|{
comment|// In newer versions of Jetty the return type has been changed
comment|// to Set.
comment|// This causes problems at byte-code level. Try recovering.
name|Method
name|reflGetFieldNamesCollection
init|=
name|HttpFields
operator|.
name|class
operator|.
name|getMethod
argument_list|(
literal|"getFieldNamesCollection"
argument_list|)
decl_stmt|;
name|Object
name|result
init|=
name|reflGetFieldNamesCollection
operator|.
name|invoke
argument_list|(
name|fields
argument_list|)
decl_stmt|;
return|return
operator|(
name|Collection
argument_list|<
name|String
argument_list|>
operator|)
name|result
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|reflectionException
parameter_list|)
block|{
comment|// Suppress, throwing the original exception
throw|throw
name|e
throw|;
block|}
block|}
block|}
annotation|@
name|Override
DECL|method|getRequestHeaders ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Collection
argument_list|<
name|String
argument_list|>
argument_list|>
name|getRequestHeaders
parameter_list|()
block|{
return|return
name|getFieldsAsMap
argument_list|(
name|request
operator|.
name|getHeaders
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getResponseHeaders ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Collection
argument_list|<
name|String
argument_list|>
argument_list|>
name|getResponseHeaders
parameter_list|()
block|{
return|return
name|getFieldsAsMap
argument_list|(
name|response
operator|.
name|getHeaders
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|setSupportRedirect (boolean supportRedirect)
specifier|public
name|void
name|setSupportRedirect
parameter_list|(
name|boolean
name|supportRedirect
parameter_list|)
block|{
name|this
operator|.
name|supportRedirect
operator|=
name|supportRedirect
expr_stmt|;
block|}
block|}
end_class

end_unit

