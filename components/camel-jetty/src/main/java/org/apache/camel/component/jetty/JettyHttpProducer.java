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
name|java
operator|.
name|util
operator|.
name|Map
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
name|AsyncProcessor
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
name|Endpoint
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
name|component
operator|.
name|http
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
name|component
operator|.
name|http
operator|.
name|helper
operator|.
name|HttpProducerHelper
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
name|ExchangeHelper
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
name|camel
operator|.
name|util
operator|.
name|URISupport
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
name|io
operator|.
name|ByteArrayBuffer
import|;
end_import

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|JettyHttpProducer
specifier|public
class|class
name|JettyHttpProducer
extends|extends
name|DefaultProducer
implements|implements
name|AsyncProcessor
block|{
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
name|JettyHttpProducer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|client
specifier|private
specifier|final
name|HttpClient
name|client
decl_stmt|;
DECL|field|binding
specifier|private
name|JettyHttpBinding
name|binding
decl_stmt|;
DECL|method|JettyHttpProducer (Endpoint endpoint, HttpClient client)
specifier|public
name|JettyHttpProducer
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|HttpClient
name|client
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
name|client
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getEndpoint ()
specifier|public
name|JettyHttpEndpoint
name|getEndpoint
parameter_list|()
block|{
return|return
operator|(
name|JettyHttpEndpoint
operator|)
name|super
operator|.
name|getEndpoint
argument_list|()
return|;
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
name|HttpClient
name|client
init|=
name|getEndpoint
argument_list|()
operator|.
name|getClient
argument_list|()
decl_stmt|;
name|JettyContentExchange
name|httpExchange
init|=
name|createHttpExchange
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|sendSynchronous
argument_list|(
name|exchange
argument_list|,
name|client
argument_list|,
name|httpExchange
argument_list|)
expr_stmt|;
block|}
DECL|method|process (Exchange exchange, final AsyncCallback callback)
specifier|public
name|boolean
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
specifier|final
name|AsyncCallback
name|callback
parameter_list|)
block|{
name|HttpClient
name|client
init|=
name|getEndpoint
argument_list|()
operator|.
name|getClient
argument_list|()
decl_stmt|;
try|try
block|{
name|JettyContentExchange
name|httpExchange
init|=
name|createHttpExchange
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|sendAsynchronous
argument_list|(
name|exchange
argument_list|,
name|client
argument_list|,
name|httpExchange
argument_list|,
name|callback
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
comment|// we should continue processing this asynchronously
return|return
literal|false
return|;
block|}
DECL|method|sendAsynchronous (final Exchange exchange, final HttpClient client, final JettyContentExchange httpExchange, final AsyncCallback callback)
specifier|protected
name|void
name|sendAsynchronous
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|,
specifier|final
name|HttpClient
name|client
parameter_list|,
specifier|final
name|JettyContentExchange
name|httpExchange
parameter_list|,
specifier|final
name|AsyncCallback
name|callback
parameter_list|)
throws|throws
name|IOException
block|{
comment|// set the callback for the async mode
name|httpExchange
operator|.
name|setCallback
argument_list|(
name|callback
argument_list|)
expr_stmt|;
name|doSendExchange
argument_list|(
name|client
argument_list|,
name|httpExchange
argument_list|)
expr_stmt|;
comment|// the callback will handle all the response handling logic
block|}
DECL|method|sendSynchronous (Exchange exchange, HttpClient client, JettyContentExchange httpExchange)
specifier|protected
name|void
name|sendSynchronous
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|HttpClient
name|client
parameter_list|,
name|JettyContentExchange
name|httpExchange
parameter_list|)
throws|throws
name|Exception
block|{
name|doSendExchange
argument_list|(
name|client
argument_list|,
name|httpExchange
argument_list|)
expr_stmt|;
comment|// we send synchronous so wait for it to be done
comment|// must use our own lock detection as Jettys waitForDone will wait forever in case of connection issues
try|try
block|{
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Waiting for HTTP exchange to be done"
argument_list|)
expr_stmt|;
block|}
name|int
name|exchangeState
init|=
name|httpExchange
operator|.
name|waitForDoneOrFailure
argument_list|()
decl_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"HTTP exchange is done with state "
operator|+
name|exchangeState
argument_list|)
expr_stmt|;
block|}
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
name|getBinding
argument_list|()
operator|.
name|populateResponse
argument_list|(
name|exchange
argument_list|,
name|httpExchange
argument_list|)
expr_stmt|;
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
throw|throw
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
throw|;
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
throw|throw
name|exchange
operator|.
name|getException
argument_list|()
throw|;
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
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
comment|// are we shutting down?
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
literal|"Interrupted waiting for async reply, are we stopping? "
operator|+
operator|(
name|isStopping
argument_list|()
operator|||
name|isStopped
argument_list|()
operator|)
argument_list|)
expr_stmt|;
block|}
name|exchange
operator|.
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|createHttpExchange (Exchange exchange)
specifier|protected
name|JettyContentExchange
name|createHttpExchange
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|url
init|=
name|HttpProducerHelper
operator|.
name|createURL
argument_list|(
name|exchange
argument_list|,
name|getEndpoint
argument_list|()
argument_list|)
decl_stmt|;
name|HttpMethods
name|methodToUse
init|=
name|HttpProducerHelper
operator|.
name|createMethod
argument_list|(
name|exchange
argument_list|,
name|getEndpoint
argument_list|()
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
operator|!=
literal|null
argument_list|)
decl_stmt|;
name|String
name|method
init|=
name|methodToUse
operator|.
name|createMethod
argument_list|(
name|url
argument_list|)
operator|.
name|getName
argument_list|()
decl_stmt|;
name|JettyContentExchange
name|httpExchange
init|=
operator|new
name|JettyContentExchange
argument_list|(
name|exchange
argument_list|,
name|getBinding
argument_list|()
argument_list|,
name|client
argument_list|)
decl_stmt|;
name|httpExchange
operator|.
name|setMethod
argument_list|(
name|method
argument_list|)
expr_stmt|;
name|httpExchange
operator|.
name|setURL
argument_list|(
name|url
argument_list|)
expr_stmt|;
comment|// set query parameters
name|doSetQueryParameters
argument_list|(
name|exchange
argument_list|,
name|httpExchange
argument_list|)
expr_stmt|;
comment|// if we post then set data
if|if
condition|(
name|HttpMethods
operator|.
name|POST
operator|.
name|equals
argument_list|(
name|methodToUse
argument_list|)
condition|)
block|{
name|String
name|contentType
init|=
name|ExchangeHelper
operator|.
name|getContentType
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
if|if
condition|(
name|contentType
operator|!=
literal|null
condition|)
block|{
name|httpExchange
operator|.
name|setRequestContentType
argument_list|(
name|contentType
argument_list|)
expr_stmt|;
block|}
comment|// try with String at first
name|String
name|data
init|=
name|exchange
operator|.
name|getIn
argument_list|()
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
if|if
condition|(
name|charset
operator|!=
literal|null
condition|)
block|{
name|httpExchange
operator|.
name|setRequestContent
argument_list|(
operator|new
name|ByteArrayBuffer
argument_list|(
name|data
argument_list|,
name|charset
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|httpExchange
operator|.
name|setRequestContent
argument_list|(
operator|new
name|ByteArrayBuffer
argument_list|(
name|data
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
comment|// then fallback to input stream
name|InputStream
name|is
init|=
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|mandatoryConvertTo
argument_list|(
name|InputStream
operator|.
name|class
argument_list|,
name|exchange
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
decl_stmt|;
name|httpExchange
operator|.
name|setRequestContentSource
argument_list|(
name|is
argument_list|)
expr_stmt|;
block|}
block|}
comment|// and copy headers from IN message
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
name|getEndpoint
argument_list|()
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
argument_list|,
name|exchange
argument_list|)
condition|)
block|{
name|httpExchange
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
return|return
name|httpExchange
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|doSetQueryParameters (Exchange exchange, JettyContentExchange httpExchange)
specifier|private
name|void
name|doSetQueryParameters
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|JettyContentExchange
name|httpExchange
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
if|if
condition|(
name|queryString
operator|==
literal|null
condition|)
block|{
name|queryString
operator|=
name|getEndpoint
argument_list|()
operator|.
name|getHttpUri
argument_list|()
operator|.
name|getQuery
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|queryString
argument_list|)
condition|)
block|{
return|return;
block|}
comment|// okay we need to add the query string to the URI so we need to juggle a bit with the parameters
name|String
name|uri
init|=
name|httpExchange
operator|.
name|getURI
argument_list|()
decl_stmt|;
name|Map
name|parameters
init|=
name|URISupport
operator|.
name|parseParameters
argument_list|(
operator|new
name|URI
argument_list|(
name|uri
argument_list|)
argument_list|)
decl_stmt|;
name|parameters
operator|.
name|putAll
argument_list|(
name|URISupport
operator|.
name|parseQuery
argument_list|(
name|queryString
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|uri
operator|.
name|contains
argument_list|(
literal|"?"
argument_list|)
condition|)
block|{
name|uri
operator|=
name|ObjectHelper
operator|.
name|before
argument_list|(
name|uri
argument_list|,
literal|"?"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|parameters
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|uri
operator|=
name|uri
operator|+
literal|"?"
operator|+
name|URISupport
operator|.
name|createQueryString
argument_list|(
name|parameters
argument_list|)
expr_stmt|;
name|httpExchange
operator|.
name|setURI
argument_list|(
name|uri
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|doSendExchange (HttpClient client, JettyContentExchange httpExchange)
specifier|protected
specifier|static
name|void
name|doSendExchange
parameter_list|(
name|HttpClient
name|client
parameter_list|,
name|JettyContentExchange
name|httpExchange
parameter_list|)
throws|throws
name|IOException
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
literal|"Sending HTTP request to: "
operator|+
name|httpExchange
operator|.
name|getUrl
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|client
operator|.
name|send
argument_list|(
name|httpExchange
argument_list|)
expr_stmt|;
block|}
DECL|method|getBinding ()
specifier|public
name|JettyHttpBinding
name|getBinding
parameter_list|()
block|{
return|return
name|binding
return|;
block|}
DECL|method|setBinding (JettyHttpBinding binding)
specifier|public
name|void
name|setBinding
parameter_list|(
name|JettyHttpBinding
name|binding
parameter_list|)
block|{
name|this
operator|.
name|binding
operator|=
name|binding
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
name|client
operator|.
name|start
argument_list|()
expr_stmt|;
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
name|client
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

