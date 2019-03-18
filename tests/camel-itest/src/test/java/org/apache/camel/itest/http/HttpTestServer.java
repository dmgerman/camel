begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.itest.http
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|itest
operator|.
name|http
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
name|net
operator|.
name|InetSocketAddress
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|ServerSocket
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|Socket
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
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
name|atomic
operator|.
name|AtomicInteger
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|net
operator|.
name|ssl
operator|.
name|SSLContext
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|net
operator|.
name|ssl
operator|.
name|SSLServerSocketFactory
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
name|test
operator|.
name|AvailablePortFinder
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
name|ConnectionReuseStrategy
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
name|HttpResponseFactory
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
name|HttpResponseInterceptor
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
name|HttpServerConnection
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
name|impl
operator|.
name|DefaultConnectionReuseStrategy
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
name|impl
operator|.
name|DefaultHttpResponseFactory
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
name|impl
operator|.
name|DefaultHttpServerConnection
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
name|localserver
operator|.
name|EchoHandler
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
name|localserver
operator|.
name|RandomHandler
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
name|params
operator|.
name|CoreConnectionPNames
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
name|params
operator|.
name|CoreProtocolPNames
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
name|params
operator|.
name|HttpParams
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
name|params
operator|.
name|SyncBasicHttpParams
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
name|protocol
operator|.
name|BasicHttpContext
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
name|protocol
operator|.
name|BasicHttpProcessor
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
name|protocol
operator|.
name|HttpContext
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
name|protocol
operator|.
name|HttpExpectationVerifier
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
name|protocol
operator|.
name|HttpProcessor
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
name|protocol
operator|.
name|HttpRequestHandler
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
name|protocol
operator|.
name|HttpRequestHandlerRegistry
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
name|protocol
operator|.
name|HttpService
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
name|protocol
operator|.
name|ImmutableHttpProcessor
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
name|protocol
operator|.
name|ResponseConnControl
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
name|protocol
operator|.
name|ResponseContent
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
name|protocol
operator|.
name|ResponseDate
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
name|protocol
operator|.
name|ResponseServer
import|;
end_import

begin_comment
comment|/**  * Copy of org.apache.http.localserver.LocalTestServer to use a specific port.  */
end_comment

begin_class
DECL|class|HttpTestServer
specifier|public
class|class
name|HttpTestServer
block|{
DECL|field|PORT
specifier|public
specifier|static
specifier|final
name|int
name|PORT
init|=
name|AvailablePortFinder
operator|.
name|getNextAvailable
argument_list|(
literal|18080
argument_list|)
decl_stmt|;
comment|/**      * The local address to bind to.      * The host is an IP number rather than "localhost" to avoid surprises      * on hosts that map "localhost" to an IPv6 address or something else.      * The port is 0 to let the system pick one.      */
DECL|field|TEST_SERVER_ADDR
specifier|public
specifier|static
specifier|final
name|InetSocketAddress
name|TEST_SERVER_ADDR
init|=
operator|new
name|InetSocketAddress
argument_list|(
literal|"localhost"
argument_list|,
name|PORT
argument_list|)
decl_stmt|;
comment|/** The request handler registry. */
DECL|field|handlerRegistry
specifier|private
specifier|final
name|HttpRequestHandlerRegistry
name|handlerRegistry
decl_stmt|;
DECL|field|httpservice
specifier|private
specifier|final
name|HttpService
name|httpservice
decl_stmt|;
comment|/** Optional SSL context */
DECL|field|sslcontext
specifier|private
specifier|final
name|SSLContext
name|sslcontext
decl_stmt|;
comment|/** The server socket, while being served. */
DECL|field|servicedSocket
specifier|private
specifier|volatile
name|ServerSocket
name|servicedSocket
decl_stmt|;
comment|/** The request listening thread, while listening. */
DECL|field|listenerThread
specifier|private
specifier|volatile
name|ListenerThread
name|listenerThread
decl_stmt|;
comment|/** Set of active worker threads */
DECL|field|workers
specifier|private
specifier|final
name|Set
argument_list|<
name|Worker
argument_list|>
name|workers
decl_stmt|;
comment|/** The number of connections this accepted. */
DECL|field|acceptedConnections
specifier|private
specifier|final
name|AtomicInteger
name|acceptedConnections
init|=
operator|new
name|AtomicInteger
argument_list|(
literal|0
argument_list|)
decl_stmt|;
static|static
block|{
comment|//set them as system properties so Spring can use the property placeholder
comment|//things to set them into the URL's in the spring contexts
name|System
operator|.
name|setProperty
argument_list|(
literal|"HttpTestServer.Port"
argument_list|,
name|Integer
operator|.
name|toString
argument_list|(
name|PORT
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates a new test server.      *      * @param proc      the HTTP processors to be used by the server, or      *<code>null</code> to use a      *                  {@link #newProcessor default} processor      * @param reuseStrat the connection reuse strategy to be used by the      *                  server, or<code>null</code> to use      *                  {@link #newConnectionReuseStrategy() default}      *                  strategy.      * @param params    the parameters to be used by the server, or      *<code>null</code> to use      *                  {@link #newDefaultParams default} parameters      * @param sslcontext optional SSL context if the server is to leverage      *                   SSL/TLS transport security      */
DECL|method|HttpTestServer ( final BasicHttpProcessor proc, final ConnectionReuseStrategy reuseStrat, final HttpResponseFactory responseFactory, final HttpExpectationVerifier expectationVerifier, final HttpParams params, final SSLContext sslcontext)
specifier|public
name|HttpTestServer
parameter_list|(
specifier|final
name|BasicHttpProcessor
name|proc
parameter_list|,
specifier|final
name|ConnectionReuseStrategy
name|reuseStrat
parameter_list|,
specifier|final
name|HttpResponseFactory
name|responseFactory
parameter_list|,
specifier|final
name|HttpExpectationVerifier
name|expectationVerifier
parameter_list|,
specifier|final
name|HttpParams
name|params
parameter_list|,
specifier|final
name|SSLContext
name|sslcontext
parameter_list|)
block|{
name|this
operator|.
name|handlerRegistry
operator|=
operator|new
name|HttpRequestHandlerRegistry
argument_list|()
expr_stmt|;
name|this
operator|.
name|workers
operator|=
name|Collections
operator|.
name|synchronizedSet
argument_list|(
operator|new
name|HashSet
argument_list|<
name|Worker
argument_list|>
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|httpservice
operator|=
operator|new
name|HttpService
argument_list|(
name|proc
operator|!=
literal|null
condition|?
name|proc
else|:
name|newProcessor
argument_list|()
argument_list|,
name|reuseStrat
operator|!=
literal|null
condition|?
name|reuseStrat
else|:
name|newConnectionReuseStrategy
argument_list|()
argument_list|,
name|responseFactory
operator|!=
literal|null
condition|?
name|responseFactory
else|:
name|newHttpResponseFactory
argument_list|()
argument_list|,
name|handlerRegistry
argument_list|,
name|expectationVerifier
argument_list|,
name|params
operator|!=
literal|null
condition|?
name|params
else|:
name|newDefaultParams
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|sslcontext
operator|=
name|sslcontext
expr_stmt|;
block|}
comment|/**      * Creates a new test server with SSL/TLS encryption.      *      * @param sslcontext SSL context      */
DECL|method|HttpTestServer (final SSLContext sslcontext)
specifier|public
name|HttpTestServer
parameter_list|(
specifier|final
name|SSLContext
name|sslcontext
parameter_list|)
block|{
name|this
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|sslcontext
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates a new test server.      *      * @param proc      the HTTP processors to be used by the server, or      *<code>null</code> to use a      *                  {@link #newProcessor default} processor      * @param params    the parameters to be used by the server, or      *<code>null</code> to use      *                  {@link #newDefaultParams default} parameters      */
DECL|method|HttpTestServer ( BasicHttpProcessor proc, HttpParams params)
specifier|public
name|HttpTestServer
parameter_list|(
name|BasicHttpProcessor
name|proc
parameter_list|,
name|HttpParams
name|params
parameter_list|)
block|{
name|this
argument_list|(
name|proc
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|params
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
comment|/**      * Obtains an HTTP protocol processor with default interceptors.      *      * @return  a protocol processor for server-side use      */
DECL|method|newProcessor ()
specifier|protected
name|HttpProcessor
name|newProcessor
parameter_list|()
block|{
return|return
operator|new
name|ImmutableHttpProcessor
argument_list|(
operator|new
name|HttpResponseInterceptor
index|[]
block|{
operator|new
name|ResponseDate
argument_list|()
block|,
operator|new
name|ResponseServer
argument_list|()
block|,
operator|new
name|ResponseContent
argument_list|()
block|,
operator|new
name|ResponseConnControl
argument_list|()
block|}
argument_list|)
return|;
block|}
comment|/**      * Obtains a set of reasonable default parameters for a server.      *      * @return  default parameters      */
DECL|method|newDefaultParams ()
specifier|protected
name|HttpParams
name|newDefaultParams
parameter_list|()
block|{
name|HttpParams
name|params
init|=
operator|new
name|SyncBasicHttpParams
argument_list|()
decl_stmt|;
name|params
operator|.
name|setIntParameter
argument_list|(
name|CoreConnectionPNames
operator|.
name|SO_TIMEOUT
argument_list|,
literal|60000
argument_list|)
operator|.
name|setIntParameter
argument_list|(
name|CoreConnectionPNames
operator|.
name|SOCKET_BUFFER_SIZE
argument_list|,
literal|8
operator|*
literal|1024
argument_list|)
operator|.
name|setBooleanParameter
argument_list|(
name|CoreConnectionPNames
operator|.
name|STALE_CONNECTION_CHECK
argument_list|,
literal|false
argument_list|)
operator|.
name|setBooleanParameter
argument_list|(
name|CoreConnectionPNames
operator|.
name|TCP_NODELAY
argument_list|,
literal|true
argument_list|)
operator|.
name|setParameter
argument_list|(
name|CoreProtocolPNames
operator|.
name|ORIGIN_SERVER
argument_list|,
literal|"LocalTestServer/1.1"
argument_list|)
expr_stmt|;
return|return
name|params
return|;
block|}
DECL|method|newConnectionReuseStrategy ()
specifier|protected
name|ConnectionReuseStrategy
name|newConnectionReuseStrategy
parameter_list|()
block|{
return|return
operator|new
name|DefaultConnectionReuseStrategy
argument_list|()
return|;
block|}
DECL|method|newHttpResponseFactory ()
specifier|protected
name|HttpResponseFactory
name|newHttpResponseFactory
parameter_list|()
block|{
return|return
operator|new
name|DefaultHttpResponseFactory
argument_list|()
return|;
block|}
comment|/**      * Returns the number of connections this test server has accepted.      */
DECL|method|getAcceptedConnectionCount ()
specifier|public
name|int
name|getAcceptedConnectionCount
parameter_list|()
block|{
return|return
name|acceptedConnections
operator|.
name|get
argument_list|()
return|;
block|}
comment|/**      * {@link #register Registers} a set of default request handlers.      *<pre>      * URI pattern      Handler      * -----------      -------      * /echo/*          {@link EchoHandler EchoHandler}      * /random/*        {@link RandomHandler RandomHandler}      *</pre>      */
DECL|method|registerDefaultHandlers ()
specifier|public
name|void
name|registerDefaultHandlers
parameter_list|()
block|{
name|handlerRegistry
operator|.
name|register
argument_list|(
literal|"/echo/*"
argument_list|,
operator|new
name|EchoHandler
argument_list|()
argument_list|)
expr_stmt|;
name|handlerRegistry
operator|.
name|register
argument_list|(
literal|"/random/*"
argument_list|,
operator|new
name|RandomHandler
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Registers a handler with the local registry.      *      * @param pattern   the URL pattern to match      * @param handler   the handler to apply      */
DECL|method|register (String pattern, HttpRequestHandler handler)
specifier|public
name|void
name|register
parameter_list|(
name|String
name|pattern
parameter_list|,
name|HttpRequestHandler
name|handler
parameter_list|)
block|{
name|handlerRegistry
operator|.
name|register
argument_list|(
name|pattern
argument_list|,
name|handler
argument_list|)
expr_stmt|;
block|}
comment|/**      * Unregisters a handler from the local registry.      *      * @param pattern   the URL pattern      */
DECL|method|unregister (String pattern)
specifier|public
name|void
name|unregister
parameter_list|(
name|String
name|pattern
parameter_list|)
block|{
name|handlerRegistry
operator|.
name|unregister
argument_list|(
name|pattern
argument_list|)
expr_stmt|;
block|}
comment|/**      * Starts this test server.      */
DECL|method|start ()
specifier|public
name|void
name|start
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|servicedSocket
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
name|this
operator|.
name|toString
argument_list|()
operator|+
literal|" already running"
argument_list|)
throw|;
block|}
name|ServerSocket
name|ssock
decl_stmt|;
if|if
condition|(
name|sslcontext
operator|!=
literal|null
condition|)
block|{
name|SSLServerSocketFactory
name|sf
init|=
name|sslcontext
operator|.
name|getServerSocketFactory
argument_list|()
decl_stmt|;
name|ssock
operator|=
name|sf
operator|.
name|createServerSocket
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|ssock
operator|=
operator|new
name|ServerSocket
argument_list|()
expr_stmt|;
block|}
name|ssock
operator|.
name|setReuseAddress
argument_list|(
literal|true
argument_list|)
expr_stmt|;
comment|// probably pointless for port '0'
name|ssock
operator|.
name|bind
argument_list|(
name|TEST_SERVER_ADDR
argument_list|)
expr_stmt|;
name|servicedSocket
operator|=
name|ssock
expr_stmt|;
name|listenerThread
operator|=
operator|new
name|ListenerThread
argument_list|()
expr_stmt|;
name|listenerThread
operator|.
name|setDaemon
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|listenerThread
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
comment|/**      * Stops this test server.      */
DECL|method|stop ()
specifier|public
name|void
name|stop
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|servicedSocket
operator|==
literal|null
condition|)
block|{
return|return;
comment|// not running
block|}
name|ListenerThread
name|t
init|=
name|listenerThread
decl_stmt|;
if|if
condition|(
name|t
operator|!=
literal|null
condition|)
block|{
name|t
operator|.
name|shutdown
argument_list|()
expr_stmt|;
block|}
synchronized|synchronized
init|(
name|workers
init|)
block|{
for|for
control|(
name|Worker
name|worker
range|:
name|workers
control|)
block|{
name|worker
operator|.
name|shutdown
argument_list|()
expr_stmt|;
block|}
block|}
block|}
DECL|method|awaitTermination (long timeMs)
specifier|public
name|void
name|awaitTermination
parameter_list|(
name|long
name|timeMs
parameter_list|)
throws|throws
name|InterruptedException
block|{
if|if
condition|(
name|listenerThread
operator|!=
literal|null
condition|)
block|{
name|listenerThread
operator|.
name|join
argument_list|(
name|timeMs
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
name|ServerSocket
name|ssock
init|=
name|servicedSocket
decl_stmt|;
comment|// avoid synchronization
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|(
literal|80
argument_list|)
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"LocalTestServer/"
argument_list|)
expr_stmt|;
if|if
condition|(
name|ssock
operator|==
literal|null
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|"stopped"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|sb
operator|.
name|append
argument_list|(
name|ssock
operator|.
name|getLocalSocketAddress
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
comment|/**      * Obtains the local address the server is listening on      *      * @return the service address      */
DECL|method|getServiceAddress ()
specifier|public
name|InetSocketAddress
name|getServiceAddress
parameter_list|()
block|{
name|ServerSocket
name|ssock
init|=
name|servicedSocket
decl_stmt|;
comment|// avoid synchronization
if|if
condition|(
name|ssock
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"not running"
argument_list|)
throw|;
block|}
return|return
operator|(
name|InetSocketAddress
operator|)
name|ssock
operator|.
name|getLocalSocketAddress
argument_list|()
return|;
block|}
comment|/**      * The request listener.      * Accepts incoming connections and launches a service thread.      */
DECL|class|ListenerThread
class|class
name|ListenerThread
extends|extends
name|Thread
block|{
DECL|field|exception
specifier|private
specifier|volatile
name|Exception
name|exception
decl_stmt|;
annotation|@
name|Override
DECL|method|run ()
specifier|public
name|void
name|run
parameter_list|()
block|{
try|try
block|{
while|while
condition|(
operator|!
name|interrupted
argument_list|()
condition|)
block|{
name|Socket
name|socket
init|=
name|servicedSocket
operator|.
name|accept
argument_list|()
decl_stmt|;
name|acceptedConnections
operator|.
name|incrementAndGet
argument_list|()
expr_stmt|;
name|DefaultHttpServerConnection
name|conn
init|=
operator|new
name|DefaultHttpServerConnection
argument_list|()
decl_stmt|;
name|conn
operator|.
name|bind
argument_list|(
name|socket
argument_list|,
name|httpservice
operator|.
name|getParams
argument_list|()
argument_list|)
expr_stmt|;
comment|// Start worker thread
name|Worker
name|worker
init|=
operator|new
name|Worker
argument_list|(
name|conn
argument_list|)
decl_stmt|;
name|workers
operator|.
name|add
argument_list|(
name|worker
argument_list|)
expr_stmt|;
name|worker
operator|.
name|setDaemon
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|worker
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|this
operator|.
name|exception
operator|=
name|ex
expr_stmt|;
block|}
finally|finally
block|{
try|try
block|{
name|servicedSocket
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ignore
parameter_list|)
block|{                 }
block|}
block|}
DECL|method|shutdown ()
specifier|public
name|void
name|shutdown
parameter_list|()
block|{
name|interrupt
argument_list|()
expr_stmt|;
try|try
block|{
name|servicedSocket
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ignore
parameter_list|)
block|{             }
block|}
DECL|method|getException ()
specifier|public
name|Exception
name|getException
parameter_list|()
block|{
return|return
name|this
operator|.
name|exception
return|;
block|}
block|}
DECL|class|Worker
class|class
name|Worker
extends|extends
name|Thread
block|{
DECL|field|conn
specifier|private
specifier|final
name|HttpServerConnection
name|conn
decl_stmt|;
DECL|field|exception
specifier|private
specifier|volatile
name|Exception
name|exception
decl_stmt|;
DECL|method|Worker (final HttpServerConnection conn)
name|Worker
parameter_list|(
specifier|final
name|HttpServerConnection
name|conn
parameter_list|)
block|{
name|this
operator|.
name|conn
operator|=
name|conn
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|run ()
specifier|public
name|void
name|run
parameter_list|()
block|{
name|HttpContext
name|context
init|=
operator|new
name|BasicHttpContext
argument_list|()
decl_stmt|;
try|try
block|{
while|while
condition|(
name|this
operator|.
name|conn
operator|.
name|isOpen
argument_list|()
operator|&&
operator|!
name|Thread
operator|.
name|interrupted
argument_list|()
condition|)
block|{
name|httpservice
operator|.
name|handleRequest
argument_list|(
name|this
operator|.
name|conn
argument_list|,
name|context
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|this
operator|.
name|exception
operator|=
name|ex
expr_stmt|;
block|}
finally|finally
block|{
name|workers
operator|.
name|remove
argument_list|(
name|this
argument_list|)
expr_stmt|;
try|try
block|{
name|this
operator|.
name|conn
operator|.
name|shutdown
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ignore
parameter_list|)
block|{                 }
block|}
block|}
DECL|method|shutdown ()
specifier|public
name|void
name|shutdown
parameter_list|()
block|{
name|interrupt
argument_list|()
expr_stmt|;
try|try
block|{
name|this
operator|.
name|conn
operator|.
name|shutdown
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ignore
parameter_list|)
block|{             }
block|}
DECL|method|getException ()
specifier|public
name|Exception
name|getException
parameter_list|()
block|{
return|return
name|this
operator|.
name|exception
return|;
block|}
block|}
block|}
end_class

end_unit

