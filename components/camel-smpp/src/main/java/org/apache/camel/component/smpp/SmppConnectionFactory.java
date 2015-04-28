begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_comment
comment|/**  * The connectProxy() method implementation is inspired from   * com.jcraft.jsch.ProxyHTTP available under a BSD style license (below).  *   * Copyright (c) 2002-2010 ymnk, JCraft,Inc. All rights reserved.   * Redistribution and use in source and binary forms, with or without  * modification, are permitted provided that the following conditions are met:   * 1. Redistributions of source code must retain the above copyright notice,  * this list of conditions and the following disclaimer.   * 2. Redistributions in binary form must reproduce the above copyright  * notice, this list of conditions and the following disclaimer in  * the documentation and/or other materials provided with the distribution.   * 3. The names of the authors may not be used to endorse or promote products  * derived from this software without specific prior written permission.   * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,  * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND  * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL JCRAFT,  * INC. OR ANY CONTRIBUTORS TO THIS SOFTWARE BE LIABLE FOR ANY DIRECT, INDIRECT,  * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT  * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,  * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF  * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING  * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,  * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. */
end_comment

begin_package
DECL|package|org.apache.camel.component.smpp
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|smpp
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|BufferedReader
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
name|InputStreamReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|OutputStream
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
name|javax
operator|.
name|net
operator|.
name|SocketFactory
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
name|SSLSocket
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
name|SSLSocketFactory
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
name|RuntimeCamelException
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
name|commons
operator|.
name|codec
operator|.
name|binary
operator|.
name|Base64
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jsmpp
operator|.
name|session
operator|.
name|connection
operator|.
name|Connection
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jsmpp
operator|.
name|session
operator|.
name|connection
operator|.
name|ConnectionFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jsmpp
operator|.
name|session
operator|.
name|connection
operator|.
name|socket
operator|.
name|SocketConnection
import|;
end_import

begin_comment
comment|/**  * A Jsmpp ConnectionFactory that creates SSL Sockets.  *   * @version   */
end_comment

begin_class
DECL|class|SmppConnectionFactory
specifier|public
specifier|final
class|class
name|SmppConnectionFactory
implements|implements
name|ConnectionFactory
block|{
DECL|field|config
specifier|private
name|SmppConfiguration
name|config
decl_stmt|;
DECL|method|SmppConnectionFactory (SmppConfiguration config)
specifier|private
name|SmppConnectionFactory
parameter_list|(
name|SmppConfiguration
name|config
parameter_list|)
block|{
name|this
operator|.
name|config
operator|=
name|config
expr_stmt|;
block|}
DECL|method|getInstance (SmppConfiguration config)
specifier|public
specifier|static
name|SmppConnectionFactory
name|getInstance
parameter_list|(
name|SmppConfiguration
name|config
parameter_list|)
block|{
return|return
operator|new
name|SmppConnectionFactory
argument_list|(
name|config
argument_list|)
return|;
block|}
DECL|method|createConnection (String host, int port)
specifier|public
name|Connection
name|createConnection
parameter_list|(
name|String
name|host
parameter_list|,
name|int
name|port
parameter_list|)
throws|throws
name|IOException
block|{
try|try
block|{
name|Socket
name|socket
decl_stmt|;
name|SocketFactory
name|socketFactory
decl_stmt|;
name|socketFactory
operator|=
name|config
operator|.
name|getUsingSSL
argument_list|()
operator|&&
name|config
operator|.
name|getHttpProxyHost
argument_list|()
operator|==
literal|null
condition|?
name|SSLSocketFactory
operator|.
name|getDefault
argument_list|()
else|:
name|SocketFactory
operator|.
name|getDefault
argument_list|()
expr_stmt|;
if|if
condition|(
name|config
operator|.
name|getHttpProxyHost
argument_list|()
operator|!=
literal|null
condition|)
block|{
comment|// setup the proxy tunnel
name|socket
operator|=
name|socketFactory
operator|.
name|createSocket
argument_list|(
name|config
operator|.
name|getHttpProxyHost
argument_list|()
argument_list|,
name|config
operator|.
name|getHttpProxyPort
argument_list|()
argument_list|)
expr_stmt|;
name|connectProxy
argument_list|(
name|host
argument_list|,
name|port
argument_list|,
name|socket
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|socket
operator|=
name|socketFactory
operator|.
name|createSocket
argument_list|(
name|host
argument_list|,
name|port
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|config
operator|.
name|getUsingSSL
argument_list|()
operator|&&
name|config
operator|.
name|getHttpProxyHost
argument_list|()
operator|!=
literal|null
condition|)
block|{
comment|// Init the SSL socket which is based on the proxy socket
name|SSLSocketFactory
name|sslSocketFactory
init|=
operator|(
name|SSLSocketFactory
operator|)
name|SSLSocketFactory
operator|.
name|getDefault
argument_list|()
decl_stmt|;
name|SSLSocket
name|sslSocket
init|=
operator|(
name|SSLSocket
operator|)
name|sslSocketFactory
operator|.
name|createSocket
argument_list|(
name|socket
argument_list|,
name|host
argument_list|,
name|port
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|sslSocket
operator|.
name|startHandshake
argument_list|()
expr_stmt|;
name|socket
operator|=
name|sslSocket
expr_stmt|;
block|}
return|return
operator|new
name|SocketConnection
argument_list|(
name|socket
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
block|}
DECL|method|connectProxy (String host, int port, Socket socket)
specifier|private
name|void
name|connectProxy
parameter_list|(
name|String
name|host
parameter_list|,
name|int
name|port
parameter_list|,
name|Socket
name|socket
parameter_list|)
throws|throws
name|IOException
block|{
try|try
block|{
name|OutputStream
name|out
init|=
name|socket
operator|.
name|getOutputStream
argument_list|()
decl_stmt|;
name|InputStream
name|in
init|=
name|socket
operator|.
name|getInputStream
argument_list|()
decl_stmt|;
name|String
name|connectString
init|=
literal|"CONNECT "
operator|+
name|host
operator|+
literal|":"
operator|+
name|port
operator|+
literal|" HTTP/1.0\r\n"
decl_stmt|;
name|out
operator|.
name|write
argument_list|(
name|connectString
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|username
init|=
name|config
operator|.
name|getHttpProxyUsername
argument_list|()
decl_stmt|;
name|String
name|password
init|=
name|config
operator|.
name|getHttpProxyPassword
argument_list|()
decl_stmt|;
if|if
condition|(
name|username
operator|!=
literal|null
operator|&&
name|password
operator|!=
literal|null
condition|)
block|{
name|String
name|usernamePassword
init|=
name|username
operator|+
literal|":"
operator|+
name|password
decl_stmt|;
name|byte
index|[]
name|code
init|=
name|Base64
operator|.
name|encodeBase64
argument_list|(
name|usernamePassword
operator|.
name|getBytes
argument_list|()
argument_list|)
decl_stmt|;
name|out
operator|.
name|write
argument_list|(
literal|"Proxy-Authorization: Basic "
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|out
operator|.
name|write
argument_list|(
name|code
argument_list|)
expr_stmt|;
name|out
operator|.
name|write
argument_list|(
literal|"\r\n"
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|out
operator|.
name|write
argument_list|(
literal|"\r\n"
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|out
operator|.
name|flush
argument_list|()
expr_stmt|;
name|int
name|ch
init|=
literal|0
decl_stmt|;
name|BufferedReader
name|reader
init|=
name|IOHelper
operator|.
name|buffered
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
name|in
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|response
init|=
name|reader
operator|.
name|readLine
argument_list|()
decl_stmt|;
if|if
condition|(
name|response
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
literal|"Empty response to CONNECT request to host "
operator|+
name|host
operator|+
literal|":"
operator|+
name|port
argument_list|)
throw|;
block|}
name|String
name|reason
init|=
literal|"Unknown reason"
decl_stmt|;
name|int
name|code
init|=
operator|-
literal|1
decl_stmt|;
try|try
block|{
name|ch
operator|=
name|response
operator|.
name|indexOf
argument_list|(
literal|' '
argument_list|)
expr_stmt|;
name|int
name|bar
init|=
name|response
operator|.
name|indexOf
argument_list|(
literal|' '
argument_list|,
name|ch
operator|+
literal|1
argument_list|)
decl_stmt|;
name|code
operator|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|response
operator|.
name|substring
argument_list|(
name|ch
operator|+
literal|1
argument_list|,
name|bar
argument_list|)
argument_list|)
expr_stmt|;
name|reason
operator|=
name|response
operator|.
name|substring
argument_list|(
name|bar
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NumberFormatException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
literal|"Invalid response to CONNECT request to host "
operator|+
name|host
operator|+
literal|":"
operator|+
name|port
operator|+
literal|" - cannot parse code from response string: "
operator|+
name|response
argument_list|)
throw|;
block|}
if|if
condition|(
name|code
operator|!=
literal|200
condition|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
literal|"Proxy error: "
operator|+
name|reason
argument_list|)
throw|;
block|}
comment|// read until empty line
for|for
control|(
init|;
name|response
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|;
control|)
block|{
name|response
operator|=
name|reader
operator|.
name|readLine
argument_list|()
expr_stmt|;
if|if
condition|(
name|response
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
literal|"Proxy error: reached end of stream"
argument_list|)
throw|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|RuntimeException
name|re
parameter_list|)
block|{
name|closeSocket
argument_list|(
name|socket
argument_list|)
expr_stmt|;
throw|throw
name|re
throw|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|closeSocket
argument_list|(
name|socket
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"SmppConnectionFactory: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
block|}
DECL|method|closeSocket (Socket s)
specifier|private
specifier|static
name|void
name|closeSocket
parameter_list|(
name|Socket
name|s
parameter_list|)
block|{
if|if
condition|(
name|s
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|s
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
comment|// ignore
block|}
block|}
block|}
block|}
end_class

end_unit

