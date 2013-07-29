begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|lang
operator|.
name|reflect
operator|.
name|Field
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
name|Collections
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
name|reflect
operator|.
name|ReflectData
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
name|CamelContext
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
name|impl
operator|.
name|DefaultComponent
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

begin_class
DECL|class|AvroComponent
specifier|public
class|class
name|AvroComponent
extends|extends
name|DefaultComponent
block|{
DECL|field|configuration
specifier|private
name|AvroConfiguration
name|configuration
decl_stmt|;
DECL|field|listenerRegistry
specifier|private
name|ConcurrentMap
argument_list|<
name|String
argument_list|,
name|AvroListener
argument_list|>
name|listenerRegistry
init|=
operator|new
name|ConcurrentHashMap
argument_list|<
name|String
argument_list|,
name|AvroListener
argument_list|>
argument_list|()
decl_stmt|;
DECL|method|AvroComponent ()
specifier|public
name|AvroComponent
parameter_list|()
block|{     }
DECL|method|AvroComponent (CamelContext context)
specifier|public
name|AvroComponent
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
name|super
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
comment|/**      * A factory method allowing derived components to create a new endpoint      * from the given URI, remaining path and optional parameters      *      * @param uri        the full URI of the endpoint      * @param remaining  the remaining part of the URI without the query      *                   parameters or component prefix      * @param parameters the optional parameters passed in      * @return a newly created endpoint or null if the endpoint cannot be      *         created based on the inputs      */
annotation|@
name|Override
DECL|method|createEndpoint (String uri, String remaining, Map<String, Object> parameters)
specifier|protected
name|Endpoint
name|createEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|remaining
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
throws|throws
name|Exception
block|{
name|AvroConfiguration
name|config
decl_stmt|;
if|if
condition|(
name|configuration
operator|!=
literal|null
condition|)
block|{
name|config
operator|=
name|configuration
operator|.
name|copy
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|config
operator|=
operator|new
name|AvroConfiguration
argument_list|()
expr_stmt|;
block|}
name|URI
name|enpointUri
init|=
operator|new
name|URI
argument_list|(
name|URISupport
operator|.
name|normalizeUri
argument_list|(
name|remaining
argument_list|)
argument_list|)
decl_stmt|;
name|applyToConfiguration
argument_list|(
name|config
argument_list|,
name|enpointUri
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
if|if
condition|(
name|AvroConstants
operator|.
name|AVRO_NETTY_TRANSPORT
operator|.
name|equals
argument_list|(
name|enpointUri
operator|.
name|getScheme
argument_list|()
argument_list|)
condition|)
block|{
return|return
operator|new
name|AvroNettyEndpoint
argument_list|(
name|remaining
argument_list|,
name|this
argument_list|,
name|config
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|AvroConstants
operator|.
name|AVRO_HTTP_TRANSPORT
operator|.
name|equals
argument_list|(
name|enpointUri
operator|.
name|getScheme
argument_list|()
argument_list|)
condition|)
block|{
return|return
operator|new
name|AvroHttpEndpoint
argument_list|(
name|remaining
argument_list|,
name|this
argument_list|,
name|config
argument_list|)
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unknown avro scheme. Should use either netty or http."
argument_list|)
throw|;
block|}
block|}
comment|/**      * Applies endpoint parameters to configuration& resolves protocol and other required configuration properties.      */
DECL|method|applyToConfiguration (AvroConfiguration config, URI endpointUri, Map<String, Object> parameters)
specifier|private
name|void
name|applyToConfiguration
parameter_list|(
name|AvroConfiguration
name|config
parameter_list|,
name|URI
name|endpointUri
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
throws|throws
name|Exception
block|{
name|config
operator|.
name|parseURI
argument_list|(
name|endpointUri
argument_list|,
name|parameters
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|setProperties
argument_list|(
name|config
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
if|if
condition|(
name|config
operator|.
name|getProtocol
argument_list|()
operator|==
literal|null
operator|&&
name|config
operator|.
name|getProtocolClassName
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|protocolClass
init|=
name|getCamelContext
argument_list|()
operator|.
name|getClassResolver
argument_list|()
operator|.
name|resolveClass
argument_list|(
name|config
operator|.
name|getProtocolClassName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|protocolClass
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|Field
name|f
init|=
name|protocolClass
operator|.
name|getField
argument_list|(
literal|"PROTOCOL"
argument_list|)
decl_stmt|;
if|if
condition|(
name|f
operator|!=
literal|null
condition|)
block|{
name|Protocol
name|protocol
init|=
operator|(
name|Protocol
operator|)
name|f
operator|.
name|get
argument_list|(
literal|null
argument_list|)
decl_stmt|;
name|config
operator|.
name|setProtocol
argument_list|(
name|protocol
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|NoSuchFieldException
name|e
parameter_list|)
block|{
name|ReflectData
name|reflectData
init|=
name|ReflectData
operator|.
name|get
argument_list|()
decl_stmt|;
name|config
operator|.
name|setProtocol
argument_list|(
name|reflectData
operator|.
name|getProtocol
argument_list|(
name|protocolClass
argument_list|)
argument_list|)
expr_stmt|;
name|config
operator|.
name|setReflectionProtocol
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|config
operator|.
name|getProtocol
argument_list|()
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Avro configuration does not contain protocol"
argument_list|)
throw|;
block|}
if|if
condition|(
name|config
operator|.
name|getMessageName
argument_list|()
operator|!=
literal|null
operator|&&
operator|!
name|config
operator|.
name|getProtocol
argument_list|()
operator|.
name|getMessages
argument_list|()
operator|.
name|containsKey
argument_list|(
name|config
operator|.
name|getMessageName
argument_list|()
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Message "
operator|+
name|config
operator|.
name|getMessageName
argument_list|()
operator|+
literal|" is not defined in protocol"
argument_list|)
throw|;
block|}
if|if
condition|(
name|config
operator|.
name|isSingleParameter
argument_list|()
condition|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Protocol
operator|.
name|Message
argument_list|>
name|messageMap
init|=
name|config
operator|.
name|getProtocol
argument_list|()
operator|.
name|getMessages
argument_list|()
decl_stmt|;
name|Iterable
argument_list|<
name|Protocol
operator|.
name|Message
argument_list|>
name|messagesToCheck
init|=
name|config
operator|.
name|getMessageName
argument_list|()
operator|==
literal|null
condition|?
name|messageMap
operator|.
name|values
argument_list|()
else|:
name|Collections
operator|.
name|singleton
argument_list|(
name|messageMap
operator|.
name|get
argument_list|(
name|config
operator|.
name|getMessageName
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
for|for
control|(
name|Protocol
operator|.
name|Message
name|message
range|:
name|messagesToCheck
control|)
block|{
if|if
condition|(
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
operator|!=
literal|1
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Single parameter option can't be used with message "
operator|+
name|message
operator|.
name|getName
argument_list|()
operator|+
literal|" because it has "
operator|+
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
operator|+
literal|" parameters defined"
argument_list|)
throw|;
block|}
block|}
block|}
block|}
comment|/**      * Registers new responder with uri as key. Registers consumer in responder.      * In case if responder is already registered by this uri then just      * registers consumer.      *       * @param uri URI of the endpoint without message name      * @param messageName message name      * @param consumer consumer that will be registered in providers` registry      * @throws Exception      */
DECL|method|register (String uri, String messageName, AvroConsumer consumer)
specifier|public
name|void
name|register
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|messageName
parameter_list|,
name|AvroConsumer
name|consumer
parameter_list|)
throws|throws
name|Exception
block|{
name|AvroListener
name|listener
init|=
name|listenerRegistry
operator|.
name|get
argument_list|(
name|uri
argument_list|)
decl_stmt|;
if|if
condition|(
name|listener
operator|==
literal|null
condition|)
block|{
name|listener
operator|=
operator|new
name|AvroListener
argument_list|(
name|consumer
operator|.
name|getEndpoint
argument_list|()
argument_list|)
expr_stmt|;
name|listenerRegistry
operator|.
name|put
argument_list|(
name|uri
argument_list|,
name|listener
argument_list|)
expr_stmt|;
block|}
name|listener
operator|.
name|register
argument_list|(
name|messageName
argument_list|,
name|consumer
argument_list|)
expr_stmt|;
block|}
comment|/**      * Calls unregister of consumer by appropriate message name.      * In case if all consumers are unregistered then it removes responder from the registry.      *      * @param uri URI of the endpoint without message name      * @param messageName message name      */
DECL|method|unregister (String uri, String messageName)
specifier|public
name|void
name|unregister
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|messageName
parameter_list|)
block|{
if|if
condition|(
name|listenerRegistry
operator|.
name|get
argument_list|(
name|uri
argument_list|)
operator|.
name|unregister
argument_list|(
name|messageName
argument_list|)
condition|)
block|{
name|listenerRegistry
operator|.
name|remove
argument_list|(
name|uri
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|getConfiguration ()
specifier|public
name|AvroConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
DECL|method|setConfiguration (AvroConfiguration configuration)
specifier|public
name|void
name|setConfiguration
parameter_list|(
name|AvroConfiguration
name|configuration
parameter_list|)
block|{
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
block|}
block|}
end_class

end_unit

