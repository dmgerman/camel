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
name|LinkedHashMap
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
name|concurrent
operator|.
name|CountDownLatch
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
name|util
operator|.
name|IOHelper
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
name|ContentExchange
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
name|HttpExchange
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
name|HttpHeaders
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
name|io
operator|.
name|Buffer
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
comment|/**  * Jetty specific exchange which keeps track of the the request and response.  *  * @version   */
end_comment

begin_class
DECL|class|JettyContentExchange
specifier|public
class|class
name|JettyContentExchange
extends|extends
name|ContentExchange
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|JettyContentExchange
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|headers
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|headers
init|=
operator|new
name|LinkedHashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
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
DECL|method|JettyContentExchange (Exchange exchange, JettyHttpBinding jettyBinding, HttpClient client)
specifier|public
name|JettyContentExchange
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|JettyHttpBinding
name|jettyBinding
parameter_list|,
name|HttpClient
name|client
parameter_list|)
block|{
name|super
argument_list|(
literal|true
argument_list|)
expr_stmt|;
comment|// keep headers by default
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
block|}
DECL|method|setCallback (AsyncCallback callback)
specifier|public
name|void
name|setCallback
parameter_list|(
name|AsyncCallback
name|callback
parameter_list|)
block|{
name|this
operator|.
name|callback
operator|=
name|callback
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onResponseHeader (Buffer name, Buffer value)
specifier|protected
name|void
name|onResponseHeader
parameter_list|(
name|Buffer
name|name
parameter_list|,
name|Buffer
name|value
parameter_list|)
throws|throws
name|IOException
block|{
name|super
operator|.
name|onResponseHeader
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|name
operator|.
name|toString
argument_list|()
argument_list|,
name|value
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onRequestComplete ()
specifier|protected
name|void
name|onRequestComplete
parameter_list|()
throws|throws
name|IOException
block|{
comment|// close the input stream when its not needed anymore
name|InputStream
name|is
init|=
name|getRequestContentSource
argument_list|()
decl_stmt|;
if|if
condition|(
name|is
operator|!=
literal|null
condition|)
block|{
name|IOHelper
operator|.
name|close
argument_list|(
name|is
argument_list|,
literal|"RequestContentSource"
argument_list|,
name|LOG
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|onResponseComplete ()
specifier|protected
name|void
name|onResponseComplete
parameter_list|()
throws|throws
name|IOException
block|{
try|try
block|{
name|super
operator|.
name|onResponseComplete
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
name|doTaskCompleted
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|onExpire ()
specifier|protected
name|void
name|onExpire
parameter_list|()
block|{
try|try
block|{
name|super
operator|.
name|onExpire
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
name|doTaskCompleted
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|onException (Throwable ex)
specifier|protected
name|void
name|onException
parameter_list|(
name|Throwable
name|ex
parameter_list|)
block|{
try|try
block|{
name|super
operator|.
name|onException
argument_list|(
name|ex
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|doTaskCompleted
argument_list|(
name|ex
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|onConnectionFailed (Throwable ex)
specifier|protected
name|void
name|onConnectionFailed
parameter_list|(
name|Throwable
name|ex
parameter_list|)
block|{
try|try
block|{
name|super
operator|.
name|onConnectionFailed
argument_list|(
name|ex
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|doTaskCompleted
argument_list|(
name|ex
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|getHeaders ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getHeaders
parameter_list|()
block|{
return|return
name|headers
return|;
block|}
DECL|method|getBody ()
specifier|public
name|String
name|getBody
parameter_list|()
throws|throws
name|UnsupportedEncodingException
block|{
return|return
name|super
operator|.
name|getResponseContent
argument_list|()
return|;
block|}
DECL|method|getUrl ()
specifier|public
name|String
name|getUrl
parameter_list|()
block|{
name|String
name|params
init|=
name|getRequestFields
argument_list|()
operator|.
name|getStringField
argument_list|(
name|HttpHeaders
operator|.
name|CONTENT_ENCODING
argument_list|)
decl_stmt|;
return|return
name|getScheme
argument_list|()
operator|+
literal|"//"
operator|+
name|getAddress
argument_list|()
operator|.
name|toString
argument_list|()
operator|+
name|getURI
argument_list|()
operator|+
operator|(
name|params
operator|!=
literal|null
condition|?
literal|"?"
operator|+
name|params
else|:
literal|""
operator|)
return|;
block|}
DECL|method|doTaskCompleted ()
specifier|protected
name|void
name|doTaskCompleted
parameter_list|()
block|{
comment|// make sure to lower the latch
name|done
operator|.
name|countDown
argument_list|()
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
name|int
name|exchangeState
init|=
name|getStatus
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
literal|"TaskComplete with state {} for url: {}"
argument_list|,
name|exchangeState
argument_list|,
name|getUrl
argument_list|()
argument_list|)
expr_stmt|;
block|}
try|try
block|{
if|if
condition|(
name|exchangeState
operator|==
name|HttpExchange
operator|.
name|STATUS_COMPLETED
condition|)
block|{
comment|// process the response as the state is ok
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
block|}
elseif|else
if|if
condition|(
name|exchangeState
operator|==
name|HttpExchange
operator|.
name|STATUS_EXPIRED
condition|)
block|{
comment|// we did timeout
name|exchange
operator|.
name|setException
argument_list|(
operator|new
name|ExchangeTimedOutException
argument_list|(
name|exchange
argument_list|,
name|client
operator|.
name|getTimeout
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// some kind of other error
if|if
condition|(
name|exchange
operator|.
name|getException
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
operator|new
name|CamelExchangeException
argument_list|(
literal|"JettyClient failed with state "
operator|+
name|exchangeState
argument_list|,
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
finally|finally
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
DECL|method|doTaskCompleted (Throwable ex)
specifier|protected
name|void
name|doTaskCompleted
parameter_list|(
name|Throwable
name|ex
parameter_list|)
block|{
try|try
block|{
comment|// some kind of other error
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
finally|finally
block|{
comment|// make sure to lower the latch
name|done
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
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
block|}
end_class

end_unit

