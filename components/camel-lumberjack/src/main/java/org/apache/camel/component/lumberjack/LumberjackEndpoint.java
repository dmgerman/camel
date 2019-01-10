begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.lumberjack
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|lumberjack
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
name|security
operator|.
name|GeneralSecurityException
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Processor
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
name|Producer
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
name|DefaultEndpoint
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
name|Metadata
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
name|UriEndpoint
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
name|UriParam
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
name|UriPath
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
name|jsse
operator|.
name|SSLContextParameters
import|;
end_import

begin_comment
comment|/**  * The lumberjack retrieves logs sent over the network using the Lumberjack protocol.  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|firstVersion
operator|=
literal|"2.18.0"
argument_list|,
name|scheme
operator|=
literal|"lumberjack"
argument_list|,
name|title
operator|=
literal|"Lumberjack"
argument_list|,
name|syntax
operator|=
literal|"lumberjack:host:port"
argument_list|,
name|consumerOnly
operator|=
literal|true
argument_list|,
name|consumerClass
operator|=
name|LumberjackConsumer
operator|.
name|class
argument_list|,
name|label
operator|=
literal|"log"
argument_list|)
DECL|class|LumberjackEndpoint
specifier|public
class|class
name|LumberjackEndpoint
extends|extends
name|DefaultEndpoint
block|{
annotation|@
name|UriPath
argument_list|(
name|description
operator|=
literal|"Network interface on which to listen for Lumberjack"
argument_list|)
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|true
argument_list|)
DECL|field|host
specifier|private
specifier|final
name|String
name|host
decl_stmt|;
annotation|@
name|UriPath
argument_list|(
name|description
operator|=
literal|"Network port on which to listen for Lumberjack"
argument_list|,
name|defaultValue
operator|=
literal|""
operator|+
name|LumberjackComponent
operator|.
name|DEFAULT_PORT
argument_list|)
DECL|field|port
specifier|private
specifier|final
name|int
name|port
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|description
operator|=
literal|"SSL configuration"
argument_list|)
DECL|field|sslContextParameters
specifier|private
name|SSLContextParameters
name|sslContextParameters
decl_stmt|;
DECL|method|LumberjackEndpoint (String endpointUri, LumberjackComponent component, String host, int port)
name|LumberjackEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|LumberjackComponent
name|component
parameter_list|,
name|String
name|host
parameter_list|,
name|int
name|port
parameter_list|)
block|{
name|super
argument_list|(
name|endpointUri
argument_list|,
name|component
argument_list|)
expr_stmt|;
name|this
operator|.
name|host
operator|=
name|host
expr_stmt|;
name|this
operator|.
name|port
operator|=
name|port
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getComponent ()
specifier|public
name|LumberjackComponent
name|getComponent
parameter_list|()
block|{
return|return
operator|(
name|LumberjackComponent
operator|)
name|super
operator|.
name|getComponent
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"The Lumberjack component cannot be used as a producer"
argument_list|)
throw|;
block|}
annotation|@
name|Override
DECL|method|createConsumer (Processor processor)
specifier|public
name|LumberjackConsumer
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
return|return
operator|new
name|LumberjackConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|,
name|host
argument_list|,
name|port
argument_list|,
name|provideSSLContext
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
DECL|method|getSslContextParameters ()
specifier|public
name|SSLContextParameters
name|getSslContextParameters
parameter_list|()
block|{
return|return
name|sslContextParameters
return|;
block|}
DECL|method|setSslContextParameters (SSLContextParameters sslContextParameters)
specifier|public
name|void
name|setSslContextParameters
parameter_list|(
name|SSLContextParameters
name|sslContextParameters
parameter_list|)
block|{
name|this
operator|.
name|sslContextParameters
operator|=
name|sslContextParameters
expr_stmt|;
block|}
DECL|method|provideSSLContext ()
specifier|private
name|SSLContext
name|provideSSLContext
parameter_list|()
throws|throws
name|GeneralSecurityException
throws|,
name|IOException
block|{
if|if
condition|(
name|sslContextParameters
operator|!=
literal|null
condition|)
block|{
return|return
name|sslContextParameters
operator|.
name|createSSLContext
argument_list|(
name|getCamelContext
argument_list|()
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|getComponent
argument_list|()
operator|.
name|getSslContextParameters
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
name|getComponent
argument_list|()
operator|.
name|getSslContextParameters
argument_list|()
operator|.
name|createSSLContext
argument_list|(
name|getCamelContext
argument_list|()
argument_list|)
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
block|}
end_class

end_unit

