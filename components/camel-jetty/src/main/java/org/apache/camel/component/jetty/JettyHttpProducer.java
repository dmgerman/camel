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
name|DefaultAsyncProducer
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
name|util
operator|.
name|component
operator|.
name|LifeCycle
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
name|DefaultAsyncProducer
implements|implements
name|AsyncProcessor
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
name|JettyHttpProducer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|client
specifier|private
name|HttpClient
name|client
decl_stmt|;
DECL|field|sharedClient
specifier|private
name|boolean
name|sharedClient
decl_stmt|;
DECL|field|binding
specifier|private
name|JettyHttpBinding
name|binding
decl_stmt|;
comment|/**      * Creates this producer.      *<p/>      * A client must be set before use, eg either {@link #setClient(org.eclipse.jetty.client.HttpClient)}      * or {@link #setSharedClient(org.eclipse.jetty.client.HttpClient)}.      *      * @param endpoint  the endpoint      */
DECL|method|JettyHttpProducer (Endpoint endpoint)
specifier|public
name|JettyHttpProducer
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates this producer      *      * @param endpoint  the endpoint      * @param client    the non-shared client to use      */
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
name|setClient
argument_list|(
name|client
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
name|processInternal
argument_list|(
name|exchange
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
DECL|method|processInternal (Exchange exchange, AsyncCallback callback)
specifier|private
name|void
name|processInternal
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
comment|// creating the url to use takes 2-steps
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
name|URI
name|uri
init|=
name|HttpHelper
operator|.
name|createURI
argument_list|(
name|exchange
argument_list|,
name|url
argument_list|,
name|getEndpoint
argument_list|()
argument_list|)
decl_stmt|;
comment|// get the url from the uri
name|url
operator|=
name|uri
operator|.
name|toASCIIString
argument_list|()
expr_stmt|;
comment|// execute any custom url rewrite
name|String
name|rewriteUrl
init|=
name|HttpHelper
operator|.
name|urlRewrite
argument_list|(
name|exchange
argument_list|,
name|url
argument_list|,
name|getEndpoint
argument_list|()
argument_list|,
name|this
argument_list|)
decl_stmt|;
if|if
condition|(
name|rewriteUrl
operator|!=
literal|null
condition|)
block|{
comment|// update url and query string from the rewritten url
name|url
operator|=
name|rewriteUrl
expr_stmt|;
block|}
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
name|JettyContentExchange
name|httpExchange
init|=
name|getEndpoint
argument_list|()
operator|.
name|createContentExchange
argument_list|()
decl_stmt|;
name|httpExchange
operator|.
name|init
argument_list|(
name|exchange
argument_list|,
name|getBinding
argument_list|()
argument_list|,
name|client
argument_list|,
name|callback
argument_list|)
expr_stmt|;
name|httpExchange
operator|.
name|setURL
argument_list|(
name|url
argument_list|)
expr_stmt|;
comment|// Url has to be set first
name|httpExchange
operator|.
name|setMethod
argument_list|(
name|method
argument_list|)
expr_stmt|;
if|if
condition|(
name|getEndpoint
argument_list|()
operator|.
name|getHttpClientParameters
argument_list|()
operator|!=
literal|null
condition|)
block|{
comment|// For jetty 9 these parameters can not be set on the client
comment|// so we need to set them on the httpExchange
name|String
name|timeout
init|=
operator|(
name|String
operator|)
name|getEndpoint
argument_list|()
operator|.
name|getHttpClientParameters
argument_list|()
operator|.
name|get
argument_list|(
literal|"timeout"
argument_list|)
decl_stmt|;
if|if
condition|(
name|timeout
operator|!=
literal|null
condition|)
block|{
name|httpExchange
operator|.
name|setTimeout
argument_list|(
operator|new
name|Long
argument_list|(
name|timeout
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|String
name|supportRedirect
init|=
operator|(
name|String
operator|)
name|getEndpoint
argument_list|()
operator|.
name|getHttpClientParameters
argument_list|()
operator|.
name|get
argument_list|(
literal|"supportRedirect"
argument_list|)
decl_stmt|;
if|if
condition|(
name|supportRedirect
operator|!=
literal|null
condition|)
block|{
name|httpExchange
operator|.
name|setSupportRedirect
argument_list|(
operator|new
name|Boolean
argument_list|(
name|supportRedirect
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
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
try|try
block|{
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
name|bos
operator|.
name|toByteArray
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|IOHelper
operator|.
name|close
argument_list|(
name|bos
argument_list|,
literal|"body"
argument_list|,
name|LOG
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|Object
name|body
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
if|if
condition|(
name|body
operator|instanceof
name|String
condition|)
block|{
name|String
name|data
init|=
operator|(
name|String
operator|)
name|body
decl_stmt|;
comment|// be a bit careful with String as any type can most likely be converted to String
comment|// so we only do an instanceof check and accept String if the body is really a String
comment|// do not fallback to use the default charset as it can influence the request
comment|// (for example application/x-www-form-urlencoded forms being sent)
name|String
name|charset
init|=
name|IOHelper
operator|.
name|getCharsetName
argument_list|(
name|exchange
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|httpExchange
operator|.
name|setRequestContent
argument_list|(
name|data
argument_list|,
name|charset
argument_list|)
expr_stmt|;
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
name|setRequestContent
argument_list|(
name|is
argument_list|)
expr_stmt|;
comment|// setup the content length if it is possible
name|String
name|length
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
name|CONTENT_LENGTH
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|length
argument_list|)
condition|)
block|{
name|httpExchange
operator|.
name|addRequestHeader
argument_list|(
name|Exchange
operator|.
name|CONTENT_LENGTH
argument_list|,
name|length
argument_list|)
expr_stmt|;
block|}
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
comment|// Need to remove the Host key as it should be not used
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeaders
argument_list|()
operator|.
name|remove
argument_list|(
literal|"host"
argument_list|)
expr_stmt|;
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
continue|continue;
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
name|httpExchange
operator|.
name|send
argument_list|(
name|client
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
DECL|method|getClient ()
specifier|public
name|HttpClient
name|getClient
parameter_list|()
block|{
return|return
name|client
return|;
block|}
DECL|method|setClient (HttpClient client)
specifier|public
name|void
name|setClient
parameter_list|(
name|HttpClient
name|client
parameter_list|)
block|{
name|this
operator|.
name|client
operator|=
name|client
expr_stmt|;
name|this
operator|.
name|sharedClient
operator|=
literal|false
expr_stmt|;
block|}
DECL|method|getSharedClient ()
specifier|public
name|HttpClient
name|getSharedClient
parameter_list|()
block|{
if|if
condition|(
name|sharedClient
condition|)
block|{
return|return
name|client
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
DECL|method|setSharedClient (HttpClient sharedClient)
specifier|public
name|void
name|setSharedClient
parameter_list|(
name|HttpClient
name|sharedClient
parameter_list|)
block|{
name|this
operator|.
name|client
operator|=
name|sharedClient
expr_stmt|;
name|this
operator|.
name|sharedClient
operator|=
literal|true
expr_stmt|;
block|}
DECL|method|getClientThreadPool ()
specifier|private
name|Object
name|getClientThreadPool
parameter_list|()
block|{
try|try
block|{
return|return
name|client
operator|.
name|getClass
argument_list|()
operator|.
name|getMethod
argument_list|(
literal|"getThreadPool"
argument_list|)
operator|.
name|invoke
argument_list|(
name|client
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
comment|// not found in Jetty 9 which is OK as the threadpool is auto started on Jetty 9
block|}
return|return
literal|null
return|;
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
comment|// only start non-shared client
if|if
condition|(
operator|!
name|sharedClient
operator|&&
name|client
operator|!=
literal|null
condition|)
block|{
name|client
operator|.
name|start
argument_list|()
expr_stmt|;
comment|// start the thread pool
name|Object
name|tp
init|=
name|getClientThreadPool
argument_list|()
decl_stmt|;
if|if
condition|(
name|tp
operator|instanceof
name|LifeCycle
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Starting client thread pool {}"
argument_list|,
name|tp
argument_list|)
expr_stmt|;
operator|(
operator|(
name|LifeCycle
operator|)
name|tp
operator|)
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
block|}
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
comment|// only stop non-shared client
if|if
condition|(
operator|!
name|sharedClient
operator|&&
name|client
operator|!=
literal|null
condition|)
block|{
name|client
operator|.
name|stop
argument_list|()
expr_stmt|;
comment|// stop thread pool
name|Object
name|tp
init|=
name|getClientThreadPool
argument_list|()
decl_stmt|;
if|if
condition|(
name|tp
operator|instanceof
name|LifeCycle
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Stopping client thread pool {}"
argument_list|,
name|tp
argument_list|)
expr_stmt|;
operator|(
operator|(
name|LifeCycle
operator|)
name|tp
operator|)
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

