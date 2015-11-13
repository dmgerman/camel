begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|java
operator|.
name|util
operator|.
name|HashMap
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
name|io
operator|.
name|undertow
operator|.
name|Undertow
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
comment|/**  * This class is used to hold Undertow instances during runtime.  * One of the benefits is reuse of same TCP port for more endpoints.  */
end_comment

begin_class
DECL|class|UndertowRegistry
specifier|public
class|class
name|UndertowRegistry
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
name|UndertowRegistry
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|port
specifier|private
name|int
name|port
decl_stmt|;
DECL|field|sslContext
specifier|private
name|SSLContext
name|sslContext
decl_stmt|;
DECL|field|host
specifier|private
name|String
name|host
decl_stmt|;
DECL|field|server
specifier|private
name|Undertow
name|server
decl_stmt|;
DECL|field|consumersRegistry
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|UndertowConsumer
argument_list|>
name|consumersRegistry
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|UndertowConsumer
argument_list|>
argument_list|()
decl_stmt|;
DECL|method|UndertowRegistry (UndertowConsumer consumer, int port)
specifier|public
name|UndertowRegistry
parameter_list|(
name|UndertowConsumer
name|consumer
parameter_list|,
name|int
name|port
parameter_list|)
block|{
name|registerConsumer
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
name|this
operator|.
name|port
operator|=
name|port
expr_stmt|;
if|if
condition|(
name|consumer
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getSslContext
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|sslContext
operator|=
name|consumer
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getSslContext
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|getPort ()
specifier|public
name|int
name|getPort
parameter_list|()
block|{
return|return
name|port
return|;
block|}
DECL|method|setPort (int port)
specifier|public
name|void
name|setPort
parameter_list|(
name|int
name|port
parameter_list|)
block|{
name|this
operator|.
name|port
operator|=
name|port
expr_stmt|;
block|}
DECL|method|registerConsumer (UndertowConsumer consumer)
specifier|public
name|void
name|registerConsumer
parameter_list|(
name|UndertowConsumer
name|consumer
parameter_list|)
block|{
name|UndertowEndpoint
name|endpoint
init|=
name|consumer
operator|.
name|getEndpoint
argument_list|()
decl_stmt|;
name|URI
name|httpUri
init|=
name|endpoint
operator|.
name|getHttpURI
argument_list|()
decl_stmt|;
if|if
condition|(
name|host
operator|!=
literal|null
operator|&&
operator|!
name|host
operator|.
name|equals
argument_list|(
name|httpUri
operator|.
name|getHost
argument_list|()
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Cannot register UndertowConsumer on different host and same port: {}"
operator|+
name|host
operator|+
literal|" "
operator|+
name|httpUri
operator|.
name|getHost
argument_list|()
argument_list|)
throw|;
block|}
else|else
block|{
name|host
operator|=
name|httpUri
operator|.
name|getHost
argument_list|()
expr_stmt|;
block|}
name|LOG
operator|.
name|info
argument_list|(
literal|"Adding consumer to consumerRegistry: {}"
argument_list|,
name|httpUri
argument_list|)
expr_stmt|;
name|consumersRegistry
operator|.
name|put
argument_list|(
name|endpoint
operator|.
name|getEndpointUri
argument_list|()
argument_list|,
name|consumer
argument_list|)
expr_stmt|;
if|if
condition|(
name|sslContext
operator|!=
literal|null
operator|&&
name|endpoint
operator|.
name|getSslContext
argument_list|()
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Cannot register UndertowConsumer with different SSL config"
argument_list|)
throw|;
block|}
block|}
DECL|method|unregisterConsumer (UndertowConsumer consumer)
specifier|public
name|void
name|unregisterConsumer
parameter_list|(
name|UndertowConsumer
name|consumer
parameter_list|)
block|{
name|UndertowEndpoint
name|endpoint
init|=
name|consumer
operator|.
name|getEndpoint
argument_list|()
decl_stmt|;
name|String
name|endpointUri
init|=
name|endpoint
operator|.
name|getEndpointUri
argument_list|()
decl_stmt|;
if|if
condition|(
name|consumersRegistry
operator|.
name|containsKey
argument_list|(
name|endpointUri
argument_list|)
condition|)
block|{
name|consumersRegistry
operator|.
name|remove
argument_list|(
name|endpointUri
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Cannot unregister consumer {} as it was not registered"
argument_list|,
name|consumer
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|isEmpty ()
specifier|public
name|boolean
name|isEmpty
parameter_list|()
block|{
return|return
name|consumersRegistry
operator|.
name|isEmpty
argument_list|()
return|;
block|}
DECL|method|getServer ()
specifier|public
name|Undertow
name|getServer
parameter_list|()
block|{
return|return
name|server
return|;
block|}
DECL|method|setServer (Undertow server)
specifier|public
name|void
name|setServer
parameter_list|(
name|Undertow
name|server
parameter_list|)
block|{
name|this
operator|.
name|server
operator|=
name|server
expr_stmt|;
block|}
DECL|method|getConsumersRegistry ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|UndertowConsumer
argument_list|>
name|getConsumersRegistry
parameter_list|()
block|{
return|return
name|consumersRegistry
return|;
block|}
DECL|method|getHost ()
specifier|public
name|String
name|getHost
parameter_list|()
block|{
return|return
name|host
return|;
block|}
DECL|method|setHost (String host)
specifier|public
name|void
name|setHost
parameter_list|(
name|String
name|host
parameter_list|)
block|{
name|this
operator|.
name|host
operator|=
name|host
expr_stmt|;
block|}
DECL|method|getSslContext ()
specifier|public
name|SSLContext
name|getSslContext
parameter_list|()
block|{
return|return
name|sslContext
return|;
block|}
DECL|method|setSslContext (SSLContext sslContext)
specifier|public
name|void
name|setSslContext
parameter_list|(
name|SSLContext
name|sslContext
parameter_list|)
block|{
name|this
operator|.
name|sslContext
operator|=
name|sslContext
expr_stmt|;
block|}
block|}
end_class

end_unit

