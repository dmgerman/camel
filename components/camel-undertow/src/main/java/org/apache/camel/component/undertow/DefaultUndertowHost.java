begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.undertow
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|undertow
package|;
end_package

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
name|io
operator|.
name|undertow
operator|.
name|Undertow
import|;
end_import

begin_import
import|import
name|io
operator|.
name|undertow
operator|.
name|UndertowOptions
import|;
end_import

begin_import
import|import
name|io
operator|.
name|undertow
operator|.
name|server
operator|.
name|HttpHandler
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
name|undertow
operator|.
name|handlers
operator|.
name|CamelRootHandler
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
name|undertow
operator|.
name|handlers
operator|.
name|NotFoundHandler
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
comment|/**  * The default UndertowHost which manages standalone Undertow server.  */
end_comment

begin_class
DECL|class|DefaultUndertowHost
specifier|public
class|class
name|DefaultUndertowHost
implements|implements
name|UndertowHost
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
name|DefaultUndertowHost
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|key
specifier|private
specifier|final
name|UndertowHostKey
name|key
decl_stmt|;
DECL|field|options
specifier|private
specifier|final
name|UndertowHostOptions
name|options
decl_stmt|;
DECL|field|rootHandler
specifier|private
specifier|final
name|CamelRootHandler
name|rootHandler
decl_stmt|;
DECL|field|undertow
specifier|private
name|Undertow
name|undertow
decl_stmt|;
DECL|field|hostString
specifier|private
name|String
name|hostString
decl_stmt|;
DECL|method|DefaultUndertowHost (UndertowHostKey key)
specifier|public
name|DefaultUndertowHost
parameter_list|(
name|UndertowHostKey
name|key
parameter_list|)
block|{
name|this
argument_list|(
name|key
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|DefaultUndertowHost (UndertowHostKey key, UndertowHostOptions options)
specifier|public
name|DefaultUndertowHost
parameter_list|(
name|UndertowHostKey
name|key
parameter_list|,
name|UndertowHostOptions
name|options
parameter_list|)
block|{
name|this
operator|.
name|key
operator|=
name|key
expr_stmt|;
name|this
operator|.
name|options
operator|=
name|options
expr_stmt|;
name|rootHandler
operator|=
operator|new
name|CamelRootHandler
argument_list|(
operator|new
name|NotFoundHandler
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|validateEndpointURI (URI httpURI)
specifier|public
name|void
name|validateEndpointURI
parameter_list|(
name|URI
name|httpURI
parameter_list|)
block|{
comment|// all URIs are good
block|}
annotation|@
name|Override
DECL|method|registerHandler (HttpHandlerRegistrationInfo registrationInfo, HttpHandler handler)
specifier|public
specifier|synchronized
name|HttpHandler
name|registerHandler
parameter_list|(
name|HttpHandlerRegistrationInfo
name|registrationInfo
parameter_list|,
name|HttpHandler
name|handler
parameter_list|)
block|{
if|if
condition|(
name|undertow
operator|==
literal|null
condition|)
block|{
name|Undertow
operator|.
name|Builder
name|builder
init|=
name|Undertow
operator|.
name|builder
argument_list|()
decl_stmt|;
if|if
condition|(
name|key
operator|.
name|getSslContext
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|addHttpsListener
argument_list|(
name|key
operator|.
name|getPort
argument_list|()
argument_list|,
name|key
operator|.
name|getHost
argument_list|()
argument_list|,
name|key
operator|.
name|getSslContext
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|builder
operator|.
name|addHttpListener
argument_list|(
name|key
operator|.
name|getPort
argument_list|()
argument_list|,
name|key
operator|.
name|getHost
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|options
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|options
operator|.
name|getIoThreads
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|setIoThreads
argument_list|(
name|options
operator|.
name|getIoThreads
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|options
operator|.
name|getWorkerThreads
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|setWorkerThreads
argument_list|(
name|options
operator|.
name|getWorkerThreads
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|options
operator|.
name|getBufferSize
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|setBufferSize
argument_list|(
name|options
operator|.
name|getBufferSize
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|options
operator|.
name|getDirectBuffers
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|setDirectBuffers
argument_list|(
name|options
operator|.
name|getDirectBuffers
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|options
operator|.
name|getHttp2Enabled
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|setServerOption
argument_list|(
name|UndertowOptions
operator|.
name|ENABLE_HTTP2
argument_list|,
name|options
operator|.
name|getHttp2Enabled
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|undertow
operator|=
name|builder
operator|.
name|setHandler
argument_list|(
name|rootHandler
argument_list|)
operator|.
name|build
argument_list|()
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Starting Undertow server on {}://{}:{}"
argument_list|,
name|key
operator|.
name|getSslContext
argument_list|()
operator|!=
literal|null
condition|?
literal|"https"
else|:
literal|"http"
argument_list|,
name|key
operator|.
name|getHost
argument_list|()
argument_list|,
name|key
operator|.
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
comment|// If there is an exception while starting up, Undertow wraps it
comment|// as RuntimeException which leaves the consumer in an inconsistent
comment|// state as a subsequent start if the route (i.e. manually) won't
comment|// start the Undertow instance as undertow is not null.
name|undertow
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RuntimeException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Failed to start Undertow server on {}://{}:{}, reason: {}"
argument_list|,
name|key
operator|.
name|getSslContext
argument_list|()
operator|!=
literal|null
condition|?
literal|"https"
else|:
literal|"http"
argument_list|,
name|key
operator|.
name|getHost
argument_list|()
argument_list|,
name|key
operator|.
name|getPort
argument_list|()
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
comment|// Cleanup any resource that may have been created during start
comment|// and reset the instance so a subsequent start will trigger the
comment|// initialization again.
name|undertow
operator|.
name|stop
argument_list|()
expr_stmt|;
name|undertow
operator|=
literal|null
expr_stmt|;
throw|throw
name|e
throw|;
block|}
block|}
return|return
name|rootHandler
operator|.
name|add
argument_list|(
name|registrationInfo
operator|.
name|getUri
argument_list|()
operator|.
name|getPath
argument_list|()
argument_list|,
name|registrationInfo
operator|.
name|getMethodRestrict
argument_list|()
argument_list|,
name|registrationInfo
operator|.
name|isMatchOnUriPrefix
argument_list|()
argument_list|,
name|handler
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|unregisterHandler (HttpHandlerRegistrationInfo registrationInfo)
specifier|public
specifier|synchronized
name|void
name|unregisterHandler
parameter_list|(
name|HttpHandlerRegistrationInfo
name|registrationInfo
parameter_list|)
block|{
if|if
condition|(
name|undertow
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|rootHandler
operator|.
name|remove
argument_list|(
name|registrationInfo
operator|.
name|getUri
argument_list|()
operator|.
name|getPath
argument_list|()
argument_list|,
name|registrationInfo
operator|.
name|getMethodRestrict
argument_list|()
argument_list|,
name|registrationInfo
operator|.
name|isMatchOnUriPrefix
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|rootHandler
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Stopping Undertow server on {}://{}:{}"
argument_list|,
name|key
operator|.
name|getSslContext
argument_list|()
operator|!=
literal|null
condition|?
literal|"https"
else|:
literal|"http"
argument_list|,
name|key
operator|.
name|getHost
argument_list|()
argument_list|,
name|key
operator|.
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
name|undertow
operator|.
name|stop
argument_list|()
expr_stmt|;
name|undertow
operator|=
literal|null
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
if|if
condition|(
name|hostString
operator|==
literal|null
condition|)
block|{
name|hostString
operator|=
name|String
operator|.
name|format
argument_list|(
literal|"DefaultUndertowHost[%s://%s:%s]"
argument_list|,
name|key
operator|.
name|getSslContext
argument_list|()
operator|!=
literal|null
condition|?
literal|"https"
else|:
literal|"http"
argument_list|,
name|key
operator|.
name|getHost
argument_list|()
argument_list|,
name|key
operator|.
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|hostString
return|;
block|}
block|}
end_class

end_unit

