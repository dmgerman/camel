begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.consul
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|consul
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayInputStream
import|;
end_import

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
name|ObjectInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ObjectOutputStream
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
name|nio
operator|.
name|charset
operator|.
name|StandardCharsets
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Base64
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
name|HashSet
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
name|UUID
import|;
end_import

begin_import
import|import
name|com
operator|.
name|orbitz
operator|.
name|consul
operator|.
name|Consul
import|;
end_import

begin_import
import|import
name|com
operator|.
name|orbitz
operator|.
name|consul
operator|.
name|ConsulException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|orbitz
operator|.
name|consul
operator|.
name|KeyValueClient
import|;
end_import

begin_import
import|import
name|com
operator|.
name|orbitz
operator|.
name|consul
operator|.
name|SessionClient
import|;
end_import

begin_import
import|import
name|com
operator|.
name|orbitz
operator|.
name|consul
operator|.
name|model
operator|.
name|session
operator|.
name|ImmutableSession
import|;
end_import

begin_import
import|import
name|com
operator|.
name|orbitz
operator|.
name|consul
operator|.
name|model
operator|.
name|session
operator|.
name|SessionCreatedResponse
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
name|NoSuchBeanException
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
name|spi
operator|.
name|Registry
import|;
end_import

begin_comment
comment|/**  * Apache Camel Plug-in for Consul Registry (Objects stored under kv/key as well  * as bookmarked under kv/[type]/key to avoid iteration over types)  */
end_comment

begin_class
DECL|class|ConsulRegistry
specifier|public
class|class
name|ConsulRegistry
implements|implements
name|Registry
block|{
DECL|field|hostname
specifier|private
name|String
name|hostname
init|=
literal|"localhost"
decl_stmt|;
DECL|field|port
specifier|private
name|int
name|port
init|=
literal|8500
decl_stmt|;
DECL|field|consul
specifier|private
name|Consul
name|consul
decl_stmt|;
DECL|field|kvClient
specifier|private
name|KeyValueClient
name|kvClient
decl_stmt|;
comment|/* constructor with default port */
DECL|method|ConsulRegistry (String hostname)
specifier|public
name|ConsulRegistry
parameter_list|(
name|String
name|hostname
parameter_list|)
block|{
name|this
argument_list|(
name|hostname
argument_list|,
literal|8500
argument_list|)
expr_stmt|;
block|}
comment|/* constructor (since spring.xml does not support builder pattern) */
DECL|method|ConsulRegistry (String hostname, int port)
specifier|public
name|ConsulRegistry
parameter_list|(
name|String
name|hostname
parameter_list|,
name|int
name|port
parameter_list|)
block|{
name|this
operator|.
name|hostname
operator|=
name|hostname
expr_stmt|;
name|this
operator|.
name|port
operator|=
name|port
expr_stmt|;
name|this
operator|.
name|consul
operator|=
name|Consul
operator|.
name|builder
argument_list|()
operator|.
name|withUrl
argument_list|(
literal|"http://"
operator|+
name|this
operator|.
name|hostname
operator|+
literal|":"
operator|+
name|this
operator|.
name|port
argument_list|)
operator|.
name|build
argument_list|()
expr_stmt|;
block|}
comment|/* builder pattern */
DECL|method|ConsulRegistry (Builder builder)
specifier|private
name|ConsulRegistry
parameter_list|(
name|Builder
name|builder
parameter_list|)
block|{
name|this
operator|.
name|hostname
operator|=
name|builder
operator|.
name|hostname
expr_stmt|;
name|this
operator|.
name|port
operator|=
name|builder
operator|.
name|port
expr_stmt|;
name|this
operator|.
name|consul
operator|=
name|Consul
operator|.
name|builder
argument_list|()
operator|.
name|withUrl
argument_list|(
literal|"http://"
operator|+
name|this
operator|.
name|hostname
operator|+
literal|":"
operator|+
name|this
operator|.
name|port
argument_list|)
operator|.
name|build
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|lookupByName (String key)
specifier|public
name|Object
name|lookupByName
parameter_list|(
name|String
name|key
parameter_list|)
block|{
comment|// Substitute $ character in key
name|key
operator|=
name|key
operator|.
name|replaceAll
argument_list|(
literal|"\\$"
argument_list|,
literal|"/"
argument_list|)
expr_stmt|;
name|kvClient
operator|=
name|consul
operator|.
name|keyValueClient
argument_list|()
expr_stmt|;
return|return
name|kvClient
operator|.
name|getValueAsString
argument_list|(
name|key
argument_list|)
operator|.
name|map
argument_list|(
name|result
lambda|->
block|{
name|byte
index|[]
name|postDecodedValue
init|=
name|ConsulRegistryUtils
operator|.
name|decodeBase64
argument_list|(
name|result
argument_list|)
decl_stmt|;
return|return
name|ConsulRegistryUtils
operator|.
name|deserialize
argument_list|(
name|postDecodedValue
argument_list|)
return|;
block|}
argument_list|)
operator|.
name|orElse
argument_list|(
literal|null
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|lookupByNameAndType (String name, Class<T> type)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|lookupByNameAndType
parameter_list|(
name|String
name|name
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
name|Object
name|object
init|=
name|lookupByName
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|object
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
try|try
block|{
return|return
name|type
operator|.
name|cast
argument_list|(
name|object
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
name|String
name|msg
init|=
literal|"Found bean: "
operator|+
name|name
operator|+
literal|" in Consul Registry: "
operator|+
name|this
operator|+
literal|" of type: "
operator|+
name|object
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|"expected type was: "
operator|+
name|type
decl_stmt|;
throw|throw
operator|new
name|NoSuchBeanException
argument_list|(
name|name
argument_list|,
name|msg
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|findByTypeWithName (Class<T> type)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|Map
argument_list|<
name|String
argument_list|,
name|T
argument_list|>
name|findByTypeWithName
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|T
argument_list|>
name|result
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
comment|// encode $ signs as they occur in subclass types
name|String
name|keyPrefix
init|=
name|type
operator|.
name|getName
argument_list|()
operator|.
name|replaceAll
argument_list|(
literal|"\\$"
argument_list|,
literal|"/"
argument_list|)
decl_stmt|;
name|kvClient
operator|=
name|consul
operator|.
name|keyValueClient
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|keys
decl_stmt|;
try|try
block|{
name|keys
operator|=
name|kvClient
operator|.
name|getKeys
argument_list|(
name|keyPrefix
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ConsulException
name|e
parameter_list|)
block|{
return|return
name|result
return|;
block|}
if|if
condition|(
name|keys
operator|!=
literal|null
condition|)
block|{
name|Object
name|obj
decl_stmt|;
for|for
control|(
name|String
name|key
range|:
name|keys
control|)
block|{
comment|// change bookmark back into actual key
name|key
operator|=
name|key
operator|.
name|substring
argument_list|(
name|key
operator|.
name|lastIndexOf
argument_list|(
literal|'/'
argument_list|)
operator|+
literal|1
argument_list|)
expr_stmt|;
name|obj
operator|=
name|lookupByName
argument_list|(
name|key
operator|.
name|replaceAll
argument_list|(
literal|"\\$"
argument_list|,
literal|"/"
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|type
operator|.
name|isInstance
argument_list|(
name|obj
argument_list|)
condition|)
block|{
name|result
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|type
operator|.
name|cast
argument_list|(
name|obj
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|result
return|;
block|}
annotation|@
name|Override
DECL|method|findByType (Class<T> type)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|Set
argument_list|<
name|T
argument_list|>
name|findByType
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
name|String
name|keyPrefix
init|=
name|type
operator|.
name|getName
argument_list|()
operator|.
name|replaceAll
argument_list|(
literal|"\\$"
argument_list|,
literal|"/"
argument_list|)
decl_stmt|;
name|Set
argument_list|<
name|T
argument_list|>
name|result
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|keys
decl_stmt|;
try|try
block|{
name|keys
operator|=
name|kvClient
operator|.
name|getKeys
argument_list|(
name|keyPrefix
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ConsulException
name|e
parameter_list|)
block|{
return|return
name|result
return|;
block|}
if|if
condition|(
name|keys
operator|!=
literal|null
condition|)
block|{
name|Object
name|obj
decl_stmt|;
for|for
control|(
name|String
name|key
range|:
name|keys
control|)
block|{
comment|// change bookmark back into actual key
name|key
operator|=
name|key
operator|.
name|substring
argument_list|(
name|key
operator|.
name|lastIndexOf
argument_list|(
literal|'/'
argument_list|)
operator|+
literal|1
argument_list|)
expr_stmt|;
name|obj
operator|=
name|lookupByName
argument_list|(
name|key
operator|.
name|replaceAll
argument_list|(
literal|"\\$"
argument_list|,
literal|"/"
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|type
operator|.
name|isInstance
argument_list|(
name|obj
argument_list|)
condition|)
block|{
name|result
operator|.
name|add
argument_list|(
name|type
operator|.
name|cast
argument_list|(
name|obj
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|result
return|;
block|}
annotation|@
name|Override
DECL|method|bind (String id, Class type, Object bean)
specifier|public
name|void
name|bind
parameter_list|(
name|String
name|id
parameter_list|,
name|Class
name|type
parameter_list|,
name|Object
name|bean
parameter_list|)
throws|throws
name|RuntimeCamelException
block|{
name|put
argument_list|(
name|id
argument_list|,
name|bean
argument_list|)
expr_stmt|;
block|}
DECL|method|remove (String key)
specifier|public
name|void
name|remove
parameter_list|(
name|String
name|key
parameter_list|)
block|{
comment|// create session to avoid conflicts (not sure if that is safe enough)
name|SessionClient
name|sessionClient
init|=
name|consul
operator|.
name|sessionClient
argument_list|()
decl_stmt|;
name|String
name|sessionName
init|=
literal|"session_"
operator|+
name|UUID
operator|.
name|randomUUID
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
name|SessionCreatedResponse
name|response
init|=
name|sessionClient
operator|.
name|createSession
argument_list|(
name|ImmutableSession
operator|.
name|builder
argument_list|()
operator|.
name|name
argument_list|(
name|sessionName
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|sessionId
init|=
name|response
operator|.
name|getId
argument_list|()
decl_stmt|;
name|kvClient
operator|=
name|consul
operator|.
name|keyValueClient
argument_list|()
expr_stmt|;
name|String
name|lockKey
init|=
literal|"lock_"
operator|+
name|key
decl_stmt|;
name|kvClient
operator|.
name|acquireLock
argument_list|(
name|lockKey
argument_list|,
name|sessionName
argument_list|,
name|sessionId
argument_list|)
expr_stmt|;
name|Object
name|object
init|=
name|lookupByName
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|object
operator|==
literal|null
condition|)
block|{
name|String
name|msg
init|=
literal|"Bean with key '"
operator|+
name|key
operator|+
literal|"' did not exist in Consul Registry."
decl_stmt|;
throw|throw
operator|new
name|NoSuchBeanException
argument_list|(
name|msg
argument_list|)
throw|;
block|}
name|kvClient
operator|.
name|deleteKey
argument_list|(
name|key
argument_list|)
expr_stmt|;
name|kvClient
operator|.
name|deleteKey
argument_list|(
name|object
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|"/"
operator|+
name|key
argument_list|)
expr_stmt|;
name|kvClient
operator|.
name|releaseLock
argument_list|(
name|lockKey
argument_list|,
name|sessionId
argument_list|)
expr_stmt|;
block|}
DECL|method|put (String key, Object object)
specifier|public
name|void
name|put
parameter_list|(
name|String
name|key
parameter_list|,
name|Object
name|object
parameter_list|)
block|{
comment|// Substitute $ character in key
name|key
operator|=
name|key
operator|.
name|replaceAll
argument_list|(
literal|"\\$"
argument_list|,
literal|"/"
argument_list|)
expr_stmt|;
comment|// create session to avoid conflicts
comment|// (not sure if that is safe enough, again)
name|SessionClient
name|sessionClient
init|=
name|consul
operator|.
name|sessionClient
argument_list|()
decl_stmt|;
name|String
name|sessionName
init|=
literal|"session_"
operator|+
name|UUID
operator|.
name|randomUUID
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
name|SessionCreatedResponse
name|response
init|=
name|sessionClient
operator|.
name|createSession
argument_list|(
name|ImmutableSession
operator|.
name|builder
argument_list|()
operator|.
name|name
argument_list|(
name|sessionName
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|sessionId
init|=
name|response
operator|.
name|getId
argument_list|()
decl_stmt|;
name|kvClient
operator|=
name|consul
operator|.
name|keyValueClient
argument_list|()
expr_stmt|;
name|String
name|lockKey
init|=
literal|"lock_"
operator|+
name|key
decl_stmt|;
name|kvClient
operator|.
name|acquireLock
argument_list|(
name|lockKey
argument_list|,
name|sessionName
argument_list|,
name|sessionId
argument_list|)
expr_stmt|;
comment|// Allow only unique keys, last one wins
if|if
condition|(
name|lookupByName
argument_list|(
name|key
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|remove
argument_list|(
name|key
argument_list|)
expr_stmt|;
block|}
name|Object
name|clone
init|=
name|ConsulRegistryUtils
operator|.
name|clone
argument_list|(
operator|(
name|Serializable
operator|)
name|object
argument_list|)
decl_stmt|;
name|byte
index|[]
name|serializedObject
init|=
name|ConsulRegistryUtils
operator|.
name|serialize
argument_list|(
operator|(
name|Serializable
operator|)
name|clone
argument_list|)
decl_stmt|;
comment|// pre-encode due native encoding issues
name|String
name|value
init|=
name|ConsulRegistryUtils
operator|.
name|encodeBase64
argument_list|(
name|serializedObject
argument_list|)
decl_stmt|;
comment|// store the actual class
name|kvClient
operator|.
name|putValue
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
comment|// store just as a bookmark
name|kvClient
operator|.
name|putValue
argument_list|(
name|object
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|.
name|replaceAll
argument_list|(
literal|"\\$"
argument_list|,
literal|"/"
argument_list|)
operator|+
literal|"/"
operator|+
name|key
argument_list|,
literal|"1"
argument_list|)
expr_stmt|;
name|kvClient
operator|.
name|releaseLock
argument_list|(
name|lockKey
argument_list|,
name|sessionId
argument_list|)
expr_stmt|;
block|}
DECL|class|Builder
specifier|public
specifier|static
class|class
name|Builder
block|{
comment|// required parameter
DECL|field|hostname
name|String
name|hostname
decl_stmt|;
comment|// optional parameter
DECL|field|port
name|Integer
name|port
init|=
literal|8500
decl_stmt|;
DECL|method|Builder (String hostname)
specifier|public
name|Builder
parameter_list|(
name|String
name|hostname
parameter_list|)
block|{
name|this
operator|.
name|hostname
operator|=
name|hostname
expr_stmt|;
block|}
DECL|method|port (Integer port)
specifier|public
name|Builder
name|port
parameter_list|(
name|Integer
name|port
parameter_list|)
block|{
name|this
operator|.
name|port
operator|=
name|port
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|build ()
specifier|public
name|ConsulRegistry
name|build
parameter_list|()
block|{
return|return
operator|new
name|ConsulRegistry
argument_list|(
name|this
argument_list|)
return|;
block|}
block|}
DECL|method|getHostname ()
specifier|public
name|String
name|getHostname
parameter_list|()
block|{
return|return
name|hostname
return|;
block|}
DECL|method|setHostname (String hostname)
specifier|public
name|void
name|setHostname
parameter_list|(
name|String
name|hostname
parameter_list|)
block|{
name|this
operator|.
name|hostname
operator|=
name|hostname
expr_stmt|;
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
DECL|class|ConsulRegistryUtils
specifier|static
class|class
name|ConsulRegistryUtils
block|{
comment|/**          * Decodes using Base64.          *          * @param base64String the {@link String} to decode          * @return a decoded data as a byte array          */
DECL|method|decodeBase64 (final String base64String)
specifier|static
name|byte
index|[]
name|decodeBase64
parameter_list|(
specifier|final
name|String
name|base64String
parameter_list|)
block|{
return|return
name|Base64
operator|.
name|getDecoder
argument_list|()
operator|.
name|decode
argument_list|(
name|base64String
operator|.
name|getBytes
argument_list|(
name|StandardCharsets
operator|.
name|ISO_8859_1
argument_list|)
argument_list|)
return|;
block|}
comment|/**          * Encodes using Base64.          *           * @param binaryData the data to encode          * @return an encoded data as a {@link String}          */
DECL|method|encodeBase64 (final byte[] binaryData)
specifier|static
name|String
name|encodeBase64
parameter_list|(
specifier|final
name|byte
index|[]
name|binaryData
parameter_list|)
block|{
specifier|final
name|byte
index|[]
name|encoded
init|=
name|Base64
operator|.
name|getEncoder
argument_list|()
operator|.
name|encode
argument_list|(
name|binaryData
argument_list|)
decl_stmt|;
return|return
operator|new
name|String
argument_list|(
name|encoded
argument_list|,
name|StandardCharsets
operator|.
name|ISO_8859_1
argument_list|)
return|;
block|}
comment|/**          * Deserializes an object out of the given byte array.          *          * @param bytes the byte array to deserialize from          * @return an {@link Object} deserialized from the given byte array          */
DECL|method|deserialize (byte[] bytes)
specifier|static
name|Object
name|deserialize
parameter_list|(
name|byte
index|[]
name|bytes
parameter_list|)
block|{
try|try
init|(
name|ObjectInputStream
name|in
init|=
operator|new
name|ObjectInputStream
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|bytes
argument_list|)
argument_list|)
init|)
block|{
return|return
name|in
operator|.
name|readObject
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|IOException
decl||
name|ClassNotFoundException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
comment|/**          * A deep serialization based clone          *          * @param object the object to clone          * @return a deep clone          */
DECL|method|clone (Serializable object)
specifier|static
name|Object
name|clone
parameter_list|(
name|Serializable
name|object
parameter_list|)
block|{
return|return
name|deserialize
argument_list|(
name|serialize
argument_list|(
name|object
argument_list|)
argument_list|)
return|;
block|}
comment|/**          * Serializes the given {@code serializable} using Java Serialization          *           * @param serializable          * @return the serialized object as a byte array          */
DECL|method|serialize (Serializable serializable)
specifier|static
name|byte
index|[]
name|serialize
parameter_list|(
name|Serializable
name|serializable
parameter_list|)
block|{
try|try
init|(
name|ByteArrayOutputStream
name|baos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|(
literal|512
argument_list|)
init|;
name|ObjectOutputStream
name|out
operator|=
operator|new
name|ObjectOutputStream
argument_list|(
name|baos
argument_list|)
init|)
block|{
name|out
operator|.
name|writeObject
argument_list|(
name|serializable
argument_list|)
expr_stmt|;
return|return
name|baos
operator|.
name|toByteArray
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
block|}
block|}
end_class

end_unit

