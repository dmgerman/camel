begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.http
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
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
name|InetAddress
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
name|net
operator|.
name|UnknownHostException
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
name|jsse
operator|.
name|SSLContextParameters
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
name|httpclient
operator|.
name|ConnectTimeoutException
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
name|httpclient
operator|.
name|params
operator|.
name|HttpConnectionParams
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
name|httpclient
operator|.
name|protocol
operator|.
name|ControllerThreadSocketFactory
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
name|httpclient
operator|.
name|protocol
operator|.
name|SecureProtocolSocketFactory
import|;
end_import

begin_comment
comment|/**  * A {@code SecureProtocolSocketFactory} implementation to allow configuration  * of Commons HTTP SSL/TLS options based on a JSSEClientParameters  * instance or a provided {@code SSLSocketFactory} instance.  */
end_comment

begin_class
DECL|class|SSLContextParametersSecureProtocolSocketFactory
specifier|public
class|class
name|SSLContextParametersSecureProtocolSocketFactory
implements|implements
name|SecureProtocolSocketFactory
block|{
DECL|field|factory
specifier|protected
name|SSLSocketFactory
name|factory
decl_stmt|;
DECL|field|context
specifier|protected
name|SSLContext
name|context
decl_stmt|;
comment|/**      * Creates a new instance using the provided factory.      *      * @param factory the factory to use      */
DECL|method|SSLContextParametersSecureProtocolSocketFactory (SSLSocketFactory factory)
specifier|public
name|SSLContextParametersSecureProtocolSocketFactory
parameter_list|(
name|SSLSocketFactory
name|factory
parameter_list|)
block|{
name|this
operator|.
name|factory
operator|=
name|factory
expr_stmt|;
block|}
comment|/**      * Creates a new instance using a factory created by the provided client configuration      * parameters.      *      * @param params the configuration parameters to use when creating the socket factory      */
DECL|method|SSLContextParametersSecureProtocolSocketFactory (SSLContextParameters params)
specifier|public
name|SSLContextParametersSecureProtocolSocketFactory
parameter_list|(
name|SSLContextParameters
name|params
parameter_list|)
block|{
try|try
block|{
name|this
operator|.
name|context
operator|=
name|params
operator|.
name|createSSLContext
argument_list|()
expr_stmt|;
name|this
operator|.
name|factory
operator|=
name|this
operator|.
name|context
operator|.
name|getSocketFactory
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
literal|"Error creating the SSLContext."
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|createSocket (String host, int port, InetAddress localAddress, int localPort)
specifier|public
name|Socket
name|createSocket
parameter_list|(
name|String
name|host
parameter_list|,
name|int
name|port
parameter_list|,
name|InetAddress
name|localAddress
parameter_list|,
name|int
name|localPort
parameter_list|)
throws|throws
name|IOException
throws|,
name|UnknownHostException
block|{
return|return
name|this
operator|.
name|factory
operator|.
name|createSocket
argument_list|(
name|host
argument_list|,
name|port
argument_list|,
name|localAddress
argument_list|,
name|localPort
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createSocket (String host, int port, InetAddress localAddress, int localPort, HttpConnectionParams params)
specifier|public
name|Socket
name|createSocket
parameter_list|(
name|String
name|host
parameter_list|,
name|int
name|port
parameter_list|,
name|InetAddress
name|localAddress
parameter_list|,
name|int
name|localPort
parameter_list|,
name|HttpConnectionParams
name|params
parameter_list|)
throws|throws
name|IOException
throws|,
name|UnknownHostException
throws|,
name|ConnectTimeoutException
block|{
if|if
condition|(
name|params
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Parameters may not be null"
argument_list|)
throw|;
block|}
name|int
name|timeout
init|=
name|params
operator|.
name|getConnectionTimeout
argument_list|()
decl_stmt|;
if|if
condition|(
name|timeout
operator|==
literal|0
condition|)
block|{
return|return
name|createSocket
argument_list|(
name|host
argument_list|,
name|port
argument_list|,
name|localAddress
argument_list|,
name|localPort
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|ControllerThreadSocketFactory
operator|.
name|createSocket
argument_list|(
name|this
argument_list|,
name|host
argument_list|,
name|port
argument_list|,
name|localAddress
argument_list|,
name|localPort
argument_list|,
name|timeout
argument_list|)
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|createSocket (String host, int port)
specifier|public
name|Socket
name|createSocket
parameter_list|(
name|String
name|host
parameter_list|,
name|int
name|port
parameter_list|)
throws|throws
name|IOException
throws|,
name|UnknownHostException
block|{
return|return
name|this
operator|.
name|factory
operator|.
name|createSocket
argument_list|(
name|host
argument_list|,
name|port
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createSocket (Socket socket, String host, int port, boolean autoClose)
specifier|public
name|Socket
name|createSocket
parameter_list|(
name|Socket
name|socket
parameter_list|,
name|String
name|host
parameter_list|,
name|int
name|port
parameter_list|,
name|boolean
name|autoClose
parameter_list|)
throws|throws
name|IOException
throws|,
name|UnknownHostException
block|{
return|return
name|this
operator|.
name|factory
operator|.
name|createSocket
argument_list|(
name|socket
argument_list|,
name|host
argument_list|,
name|port
argument_list|,
name|autoClose
argument_list|)
return|;
block|}
block|}
end_class

end_unit

