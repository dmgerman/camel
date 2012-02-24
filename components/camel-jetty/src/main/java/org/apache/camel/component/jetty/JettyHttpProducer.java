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
name|ByteArrayOutputStream
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
name|Serializable
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
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|HttpHelper
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
name|AsyncProcessorHelper
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
name|IOHelper
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
name|io
operator|.
name|ByteArrayBuffer
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
comment|/**  * @version   */
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
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
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
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|client
argument_list|,
literal|"HttpClient"
argument_list|,
name|this
argument_list|)
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
name|AsyncProcessorHelper
operator|.
name|process
argument_list|(
name|this
argument_list|,
name|exchange
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
try|try
block|{
name|JettyContentExchange
name|httpExchange
init|=
name|createHttpExchange
argument_list|(
name|exchange
argument_list|,
name|callback
argument_list|)
decl_stmt|;
name|doSendExchange
argument_list|(
name|client
argument_list|,
name|httpExchange
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// error occurred before we had a chance to go async
comment|// so set exception and invoke callback true
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
comment|// we should continue processing this asynchronously
return|return
literal|false
return|;
block|}
DECL|method|createHttpExchange (Exchange exchange, AsyncCallback callback)
specifier|protected
name|JettyContentExchange
name|createHttpExchange
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|AsyncCallback
name|callback
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|url
init|=
name|HttpHelper
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
name|HttpHelper
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
name|LOG
operator|.
name|trace
argument_list|(
literal|"Using URL: {} with method: {}"
argument_list|,
name|url
argument_list|,
name|method
argument_list|)
expr_stmt|;
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
comment|// if we post or put then set data
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
operator|||
name|HttpMethods
operator|.
name|PUT
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
if|if
condition|(
name|contentType
operator|!=
literal|null
operator|&&
name|HttpConstants
operator|.
name|CONTENT_TYPE_JAVA_SERIALIZED_OBJECT
operator|.
name|equals
argument_list|(
name|contentType
argument_list|)
condition|)
block|{
comment|// serialized java object
name|Serializable
name|obj
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getMandatoryBody
argument_list|(
name|Serializable
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// write object to output stream
name|ByteArrayOutputStream
name|bos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|HttpHelper
operator|.
name|writeObjectToStream
argument_list|(
name|bos
argument_list|,
name|obj
argument_list|)
expr_stmt|;
name|httpExchange
operator|.
name|setRequestContent
argument_list|(
operator|new
name|ByteArrayBuffer
argument_list|(
name|bos
operator|.
name|toByteArray
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|IOHelper
operator|.
name|close
argument_list|(
name|bos
argument_list|)
expr_stmt|;
block|}
else|else
block|{
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
block|}
comment|// if we bridge endpoint then we need to skip matching headers with the HTTP_QUERY to avoid sending
comment|// duplicated headers to the receiver, so use this skipRequestHeaders as the list of headers to skip
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|skipRequestHeaders
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|getEndpoint
argument_list|()
operator|.
name|isBridgeEndpoint
argument_list|()
condition|)
block|{
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|SKIP_GZIP_ENCODING
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
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
operator|!=
literal|null
condition|)
block|{
name|skipRequestHeaders
operator|=
name|URISupport
operator|.
name|parseQuery
argument_list|(
name|queryString
argument_list|)
expr_stmt|;
block|}
block|}
comment|// propagate headers as HTTP headers
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
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|entry
range|:
name|in
operator|.
name|getHeaders
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|String
name|key
init|=
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|Object
name|headerValue
init|=
name|in
operator|.
name|getHeader
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|headerValue
operator|!=
literal|null
condition|)
block|{
comment|// use an iterator as there can be multiple values. (must not use a delimiter, and allow empty values)
specifier|final
name|Iterator
argument_list|<
name|?
argument_list|>
name|it
init|=
name|ObjectHelper
operator|.
name|createIterator
argument_list|(
name|headerValue
argument_list|,
literal|null
argument_list|,
literal|true
argument_list|)
decl_stmt|;
comment|// the values to add as a request header
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|values
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
comment|// if its a multi value then check each value if we can add it and for multi values they
comment|// should be combined into a single value
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|String
name|value
init|=
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|it
operator|.
name|next
argument_list|()
argument_list|)
decl_stmt|;
comment|// we should not add headers for the parameters in the uri if we bridge the endpoint
comment|// as then we would duplicate headers on both the endpoint uri, and in HTTP headers as well
if|if
condition|(
name|skipRequestHeaders
operator|!=
literal|null
operator|&&
name|skipRequestHeaders
operator|.
name|containsKey
argument_list|(
name|key
argument_list|)
condition|)
block|{
name|Object
name|skipValue
init|=
name|skipRequestHeaders
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|equal
argument_list|(
name|skipValue
argument_list|,
name|value
argument_list|)
condition|)
block|{
continue|continue;
block|}
block|}
if|if
condition|(
name|value
operator|!=
literal|null
operator|&&
name|strategy
operator|!=
literal|null
operator|&&
operator|!
name|strategy
operator|.
name|applyFilterToCamelHeaders
argument_list|(
name|key
argument_list|,
name|value
argument_list|,
name|exchange
argument_list|)
condition|)
block|{
name|values
operator|.
name|add
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
block|}
comment|// add the value(s) as a http request header
if|if
condition|(
name|values
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
comment|// use the default toString of a ArrayList to create in the form [xxx, yyy]
comment|// if multi valued, for a single value, then just output the value as is
name|String
name|s
init|=
name|values
operator|.
name|size
argument_list|()
operator|>
literal|1
condition|?
name|values
operator|.
name|toString
argument_list|()
else|:
name|values
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|httpExchange
operator|.
name|addRequestHeader
argument_list|(
name|key
argument_list|,
name|s
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|// set the callback, which will handle all the response logic
name|httpExchange
operator|.
name|setCallback
argument_list|(
name|callback
argument_list|)
expr_stmt|;
return|return
name|httpExchange
return|;
block|}
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
name|getRequestURI
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
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
name|setRequestURI
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
literal|"Sending HTTP request to: {}"
argument_list|,
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

