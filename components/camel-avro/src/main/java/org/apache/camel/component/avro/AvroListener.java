begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.avro
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|avro
package|;
end_package

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
name|util
operator|.
name|concurrent
operator|.
name|ConcurrentHashMap
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
name|ConcurrentMap
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|avro
operator|.
name|Protocol
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|avro
operator|.
name|Schema
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|avro
operator|.
name|ipc
operator|.
name|HttpServer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|avro
operator|.
name|ipc
operator|.
name|NettyServer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|avro
operator|.
name|ipc
operator|.
name|Server
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|avro
operator|.
name|ipc
operator|.
name|specific
operator|.
name|SpecificResponder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|avro
operator|.
name|specific
operator|.
name|SpecificData
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
name|support
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
name|commons
operator|.
name|lang
operator|.
name|StringUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mortbay
operator|.
name|log
operator|.
name|Log
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|avro
operator|.
name|AvroConstants
operator|.
name|AVRO_HTTP_TRANSPORT
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|avro
operator|.
name|AvroConstants
operator|.
name|AVRO_NETTY_TRANSPORT
import|;
end_import

begin_comment
comment|/**  * This class holds server that listen to given protocol:host:port combination and dispatches messages to  * different routes mapped.  */
end_comment

begin_class
DECL|class|AvroListener
specifier|public
class|class
name|AvroListener
block|{
DECL|field|consumerRegistry
specifier|private
name|ConcurrentMap
argument_list|<
name|String
argument_list|,
name|AvroConsumer
argument_list|>
name|consumerRegistry
init|=
operator|new
name|ConcurrentHashMap
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|defaultConsumer
specifier|private
name|AvroConsumer
name|defaultConsumer
decl_stmt|;
DECL|field|server
specifier|private
specifier|final
name|Server
name|server
decl_stmt|;
DECL|method|AvroListener (AvroEndpoint endpoint)
specifier|public
name|AvroListener
parameter_list|(
name|AvroEndpoint
name|endpoint
parameter_list|)
throws|throws
name|Exception
block|{
name|server
operator|=
name|initAndStartServer
argument_list|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Initializes and starts http or netty server on basis of transport protocol from configuration.      *      *      * @param configuration      * @return Initialized and started server      * @throws java.io.IOException      */
DECL|method|initAndStartServer (AvroConfiguration configuration)
specifier|private
name|Server
name|initAndStartServer
parameter_list|(
name|AvroConfiguration
name|configuration
parameter_list|)
throws|throws
name|Exception
block|{
name|SpecificResponder
name|responder
decl_stmt|;
name|Server
name|server
decl_stmt|;
if|if
condition|(
name|configuration
operator|.
name|isReflectionProtocol
argument_list|()
condition|)
block|{
name|responder
operator|=
operator|new
name|AvroReflectResponder
argument_list|(
name|configuration
operator|.
name|getProtocol
argument_list|()
argument_list|,
name|this
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|responder
operator|=
operator|new
name|AvroSpecificResponder
argument_list|(
name|configuration
operator|.
name|getProtocol
argument_list|()
argument_list|,
name|this
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|AVRO_HTTP_TRANSPORT
operator|.
name|equalsIgnoreCase
argument_list|(
name|configuration
operator|.
name|getTransport
argument_list|()
operator|.
name|name
argument_list|()
argument_list|)
condition|)
block|{
name|server
operator|=
operator|new
name|HttpServer
argument_list|(
name|responder
argument_list|,
name|configuration
operator|.
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|AVRO_NETTY_TRANSPORT
operator|.
name|equalsIgnoreCase
argument_list|(
name|configuration
operator|.
name|getTransport
argument_list|()
operator|.
name|name
argument_list|()
argument_list|)
condition|)
block|{
name|server
operator|=
operator|new
name|NettyServer
argument_list|(
name|responder
argument_list|,
operator|new
name|InetSocketAddress
argument_list|(
name|configuration
operator|.
name|getHost
argument_list|()
argument_list|,
name|configuration
operator|.
name|getPort
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unknown transport "
operator|+
name|configuration
operator|.
name|getTransport
argument_list|()
argument_list|)
throw|;
block|}
name|server
operator|.
name|start
argument_list|()
expr_stmt|;
return|return
name|server
return|;
block|}
comment|/**      * Registers consumer by appropriate message name as key in registry.      *      * @param messageName message name      * @param consumer avro consumer      * @throws AvroComponentException      */
DECL|method|register (String messageName, AvroConsumer consumer)
specifier|public
name|void
name|register
parameter_list|(
name|String
name|messageName
parameter_list|,
name|AvroConsumer
name|consumer
parameter_list|)
throws|throws
name|AvroComponentException
block|{
if|if
condition|(
name|messageName
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|this
operator|.
name|defaultConsumer
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|AvroComponentException
argument_list|(
literal|"Default consumer already registered for uri: "
operator|+
name|consumer
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
throw|;
block|}
name|this
operator|.
name|defaultConsumer
operator|=
name|consumer
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|consumerRegistry
operator|.
name|putIfAbsent
argument_list|(
name|messageName
argument_list|,
name|consumer
argument_list|)
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|AvroComponentException
argument_list|(
literal|"Consumer already registered for message: "
operator|+
name|messageName
operator|+
literal|" and uri: "
operator|+
name|consumer
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
throw|;
block|}
block|}
block|}
comment|/**      * Unregisters consumer by message name.      * Stops server in case if all consumers are unregistered and default consumer is absent or stopped.      *      * @param messageName message name      * @return true if all consumers are unregistered and defaultConsumer is absent or null.      *         It means that this responder can be unregistered.      */
DECL|method|unregister (String messageName)
specifier|public
name|boolean
name|unregister
parameter_list|(
name|String
name|messageName
parameter_list|)
block|{
if|if
condition|(
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|messageName
argument_list|)
condition|)
block|{
if|if
condition|(
name|consumerRegistry
operator|.
name|remove
argument_list|(
name|messageName
argument_list|)
operator|==
literal|null
condition|)
block|{
name|Log
operator|.
name|warn
argument_list|(
literal|"Consumer with message name {} was already unregistered."
argument_list|,
name|messageName
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|defaultConsumer
operator|=
literal|null
expr_stmt|;
block|}
if|if
condition|(
operator|(
name|defaultConsumer
operator|==
literal|null
operator|)
operator|&&
operator|(
name|consumerRegistry
operator|.
name|isEmpty
argument_list|()
operator|)
condition|)
block|{
if|if
condition|(
name|server
operator|!=
literal|null
condition|)
block|{
name|server
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
return|return
literal|true
return|;
block|}
return|return
literal|false
return|;
block|}
DECL|method|respond (Protocol.Message message, Object request, SpecificData data)
specifier|public
name|Object
name|respond
parameter_list|(
name|Protocol
operator|.
name|Message
name|message
parameter_list|,
name|Object
name|request
parameter_list|,
name|SpecificData
name|data
parameter_list|)
throws|throws
name|Exception
block|{
name|AvroConsumer
name|consumer
init|=
name|this
operator|.
name|defaultConsumer
decl_stmt|;
if|if
condition|(
name|this
operator|.
name|consumerRegistry
operator|.
name|containsKey
argument_list|(
name|message
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|consumer
operator|=
name|this
operator|.
name|consumerRegistry
operator|.
name|get
argument_list|(
name|message
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|consumer
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|AvroComponentException
argument_list|(
literal|"No consumer defined for message: "
operator|+
name|message
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
name|Object
name|params
init|=
name|extractParams
argument_list|(
name|message
argument_list|,
name|request
argument_list|,
name|consumer
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getConfiguration
argument_list|()
operator|.
name|isSingleParameter
argument_list|()
argument_list|,
name|data
argument_list|)
decl_stmt|;
return|return
name|processExchange
argument_list|(
name|consumer
argument_list|,
name|message
argument_list|,
name|params
argument_list|)
return|;
block|}
comment|/**      * Extracts parameters from RPC call to List or converts to object of appropriate type      * if only one parameter set.      *      * @param message Avro message      * @param request Avro request      * @param singleParameter Indicates that called method has single parameter      * @param dataResolver Extracts type of parameters in call      * @return Parameters of RPC method invocation      */
DECL|method|extractParams (Protocol.Message message, Object request, boolean singleParameter, SpecificData dataResolver)
specifier|private
specifier|static
name|Object
name|extractParams
parameter_list|(
name|Protocol
operator|.
name|Message
name|message
parameter_list|,
name|Object
name|request
parameter_list|,
name|boolean
name|singleParameter
parameter_list|,
name|SpecificData
name|dataResolver
parameter_list|)
block|{
if|if
condition|(
name|singleParameter
condition|)
block|{
name|Schema
operator|.
name|Field
name|field
init|=
name|message
operator|.
name|getRequest
argument_list|()
operator|.
name|getFields
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
return|return
name|dataResolver
operator|.
name|getField
argument_list|(
name|request
argument_list|,
name|field
operator|.
name|name
argument_list|()
argument_list|,
name|field
operator|.
name|pos
argument_list|()
argument_list|)
return|;
block|}
else|else
block|{
name|int
name|i
init|=
literal|0
decl_stmt|;
name|Object
index|[]
name|params
init|=
operator|new
name|Object
index|[
name|message
operator|.
name|getRequest
argument_list|()
operator|.
name|getFields
argument_list|()
operator|.
name|size
argument_list|()
index|]
decl_stmt|;
for|for
control|(
name|Schema
operator|.
name|Field
name|param
range|:
name|message
operator|.
name|getRequest
argument_list|()
operator|.
name|getFields
argument_list|()
control|)
block|{
name|params
index|[
name|i
index|]
operator|=
name|dataResolver
operator|.
name|getField
argument_list|(
name|request
argument_list|,
name|param
operator|.
name|name
argument_list|()
argument_list|,
name|param
operator|.
name|pos
argument_list|()
argument_list|)
expr_stmt|;
name|i
operator|++
expr_stmt|;
block|}
return|return
name|params
return|;
block|}
block|}
comment|/**      * Creates exchange and processes it.      *      * @param consumer Holds processor and exception handler      * @param message Message on which exchange is created      * @param params Params of exchange      * @return Response of exchange processing      * @throws Exception      */
DECL|method|processExchange (AvroConsumer consumer, Protocol.Message message, Object params)
specifier|private
specifier|static
name|Object
name|processExchange
parameter_list|(
name|AvroConsumer
name|consumer
parameter_list|,
name|Protocol
operator|.
name|Message
name|message
parameter_list|,
name|Object
name|params
parameter_list|)
throws|throws
name|Exception
block|{
name|Object
name|response
decl_stmt|;
name|Exchange
name|exchange
init|=
name|consumer
operator|.
name|getEndpoint
argument_list|()
operator|.
name|createExchange
argument_list|(
name|message
argument_list|,
name|params
argument_list|)
decl_stmt|;
try|try
block|{
name|consumer
operator|.
name|getProcessor
argument_list|()
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
name|consumer
operator|.
name|getExceptionHandler
argument_list|()
operator|.
name|handleException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|ExchangeHelper
operator|.
name|isOutCapable
argument_list|(
name|exchange
argument_list|)
condition|)
block|{
name|response
operator|=
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getBody
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|response
operator|=
literal|null
expr_stmt|;
block|}
name|boolean
name|failed
init|=
name|exchange
operator|.
name|isFailed
argument_list|()
decl_stmt|;
if|if
condition|(
name|failed
condition|)
block|{
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
comment|// failed and no exception, must be a fault
throw|throw
operator|new
name|AvroComponentException
argument_list|(
literal|"Camel processing error."
argument_list|)
throw|;
block|}
block|}
return|return
name|response
return|;
block|}
block|}
end_class

end_unit

