begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cache
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|cache
package|;
end_package

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
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|net
operator|.
name|sf
operator|.
name|ehcache
operator|.
name|CacheException
import|;
end_import

begin_import
import|import
name|net
operator|.
name|sf
operator|.
name|ehcache
operator|.
name|Ehcache
import|;
end_import

begin_import
import|import
name|net
operator|.
name|sf
operator|.
name|ehcache
operator|.
name|Element
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
name|NoTypeConversionAvailableException
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
name|DefaultProducer
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

begin_class
DECL|class|CacheProducer
specifier|public
class|class
name|CacheProducer
extends|extends
name|DefaultProducer
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
name|CacheProducer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|config
specifier|private
name|CacheConfiguration
name|config
decl_stmt|;
DECL|field|cache
specifier|private
name|Ehcache
name|cache
decl_stmt|;
DECL|method|CacheProducer (CacheEndpoint endpoint, CacheConfiguration config)
specifier|public
name|CacheProducer
parameter_list|(
name|CacheEndpoint
name|endpoint
parameter_list|,
name|CacheConfiguration
name|config
parameter_list|)
throws|throws
name|Exception
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|this
operator|.
name|config
operator|=
name|config
expr_stmt|;
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
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
name|cache
operator|=
name|getEndpoint
argument_list|()
operator|.
name|initializeCache
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getEndpoint ()
specifier|public
name|CacheEndpoint
name|getEndpoint
parameter_list|()
block|{
return|return
operator|(
name|CacheEndpoint
operator|)
name|super
operator|.
name|getEndpoint
argument_list|()
return|;
block|}
DECL|method|process (Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Cache Name: {}"
argument_list|,
name|config
operator|.
name|getCacheName
argument_list|()
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeaders
argument_list|()
decl_stmt|;
name|String
name|key
init|=
operator|(
name|headers
operator|.
name|containsKey
argument_list|(
name|CacheConstants
operator|.
name|CACHE_KEY
argument_list|)
operator|)
condition|?
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|CacheConstants
operator|.
name|CACHE_KEY
argument_list|,
name|String
operator|.
name|class
argument_list|)
else|:
name|getEndpoint
argument_list|()
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|String
name|operation
init|=
operator|(
name|headers
operator|.
name|containsKey
argument_list|(
name|CacheConstants
operator|.
name|CACHE_OPERATION
argument_list|)
operator|)
condition|?
operator|(
name|String
operator|)
name|headers
operator|.
name|get
argument_list|(
name|CacheConstants
operator|.
name|CACHE_OPERATION
argument_list|)
else|:
name|getEndpoint
argument_list|()
operator|.
name|getOperation
argument_list|()
decl_stmt|;
if|if
condition|(
name|operation
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CacheException
argument_list|(
name|CacheConstants
operator|.
name|CACHE_OPERATION
operator|+
literal|" header not specified in message"
argument_list|)
throw|;
block|}
if|if
condition|(
operator|(
name|key
operator|==
literal|null
operator|)
operator|&&
operator|(
operator|!
name|checkIsEqual
argument_list|(
name|operation
argument_list|,
name|CacheConstants
operator|.
name|CACHE_OPERATION_DELETEALL
argument_list|)
operator|)
condition|)
block|{
throw|throw
operator|new
name|CacheException
argument_list|(
name|CacheConstants
operator|.
name|CACHE_KEY
operator|+
literal|" is not specified in message header or endpoint URL."
argument_list|)
throw|;
block|}
name|performCacheOperation
argument_list|(
name|exchange
argument_list|,
name|operation
argument_list|,
name|key
argument_list|)
expr_stmt|;
comment|//cleanup the cache headers
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|removeHeader
argument_list|(
name|CacheConstants
operator|.
name|CACHE_KEY
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|removeHeader
argument_list|(
name|CacheConstants
operator|.
name|CACHE_OPERATION
argument_list|)
expr_stmt|;
block|}
DECL|method|performCacheOperation (Exchange exchange, String operation, String key)
specifier|private
name|void
name|performCacheOperation
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|String
name|operation
parameter_list|,
name|String
name|key
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|checkIsEqual
argument_list|(
name|operation
argument_list|,
name|CacheConstants
operator|.
name|CACHE_OPERATION_URL_ADD
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Adding an element with key {} into the Cache"
argument_list|,
name|key
argument_list|)
expr_stmt|;
name|Element
name|element
init|=
name|createElementFromBody
argument_list|(
name|key
argument_list|,
name|exchange
argument_list|,
name|CacheConstants
operator|.
name|CACHE_OPERATION_ADD
argument_list|)
decl_stmt|;
name|cache
operator|.
name|put
argument_list|(
name|element
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|checkIsEqual
argument_list|(
name|operation
argument_list|,
name|CacheConstants
operator|.
name|CACHE_OPERATION_URL_UPDATE
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Updating an element with key {} into the Cache"
argument_list|,
name|key
argument_list|)
expr_stmt|;
name|Element
name|element
init|=
name|createElementFromBody
argument_list|(
name|key
argument_list|,
name|exchange
argument_list|,
name|CacheConstants
operator|.
name|CACHE_OPERATION_UPDATE
argument_list|)
decl_stmt|;
name|cache
operator|.
name|put
argument_list|(
name|element
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|checkIsEqual
argument_list|(
name|operation
argument_list|,
name|CacheConstants
operator|.
name|CACHE_OPERATION_URL_DELETEALL
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Deleting All elements from the Cache"
argument_list|)
expr_stmt|;
name|cache
operator|.
name|removeAll
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|checkIsEqual
argument_list|(
name|operation
argument_list|,
name|CacheConstants
operator|.
name|CACHE_OPERATION_URL_DELETE
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Deleting an element with key {} into the Cache"
argument_list|,
name|key
argument_list|)
expr_stmt|;
name|cache
operator|.
name|remove
argument_list|(
name|key
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|checkIsEqual
argument_list|(
name|operation
argument_list|,
name|CacheConstants
operator|.
name|CACHE_OPERATION_URL_GET
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Quering an element with key {} from the Cache"
argument_list|,
name|key
argument_list|)
expr_stmt|;
if|if
condition|(
name|cache
operator|.
name|get
argument_list|(
name|key
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|CacheConstants
operator|.
name|CACHE_ELEMENT_WAS_FOUND
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|cache
operator|.
name|get
argument_list|(
name|key
argument_list|)
operator|.
name|getObjectValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|removeHeader
argument_list|(
name|CacheConstants
operator|.
name|CACHE_ELEMENT_WAS_FOUND
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|checkIsEqual
argument_list|(
name|operation
argument_list|,
name|CacheConstants
operator|.
name|CACHE_OPERATION_URL_CHECK
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Querying an element with key {} from the Cache"
argument_list|,
name|key
argument_list|)
expr_stmt|;
name|Element
name|element
init|=
name|cache
operator|.
name|getQuiet
argument_list|(
name|key
argument_list|)
decl_stmt|;
comment|// getQuiet checks for element expiry
if|if
condition|(
name|element
operator|!=
literal|null
condition|)
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|CacheConstants
operator|.
name|CACHE_ELEMENT_WAS_FOUND
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|removeHeader
argument_list|(
name|CacheConstants
operator|.
name|CACHE_ELEMENT_WAS_FOUND
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
throw|throw
operator|new
name|CacheException
argument_list|(
name|CacheConstants
operator|.
name|CACHE_OPERATION
operator|+
literal|" "
operator|+
name|operation
operator|+
literal|" is not supported."
argument_list|)
throw|;
block|}
block|}
DECL|method|checkIsEqual (String operation, String constant)
specifier|private
name|boolean
name|checkIsEqual
parameter_list|(
name|String
name|operation
parameter_list|,
name|String
name|constant
parameter_list|)
block|{
return|return
name|operation
operator|.
name|equalsIgnoreCase
argument_list|(
name|constant
argument_list|)
operator|||
name|operation
operator|.
name|equalsIgnoreCase
argument_list|(
name|CacheConstants
operator|.
name|CACHE_HEADER_PREFIX
operator|+
name|constant
argument_list|)
return|;
block|}
DECL|method|createElementFromBody (String key, Exchange exchange, String cacheOperation)
specifier|private
name|Element
name|createElementFromBody
parameter_list|(
name|String
name|key
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|String
name|cacheOperation
parameter_list|)
throws|throws
name|NoTypeConversionAvailableException
block|{
name|Element
name|element
decl_stmt|;
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
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CacheException
argument_list|(
literal|"Body cannot be null for operation "
operator|+
name|cacheOperation
argument_list|)
throw|;
block|}
elseif|else
if|if
condition|(
name|body
operator|instanceof
name|Serializable
condition|)
block|{
name|element
operator|=
operator|new
name|Element
argument_list|(
name|key
argument_list|,
name|body
argument_list|)
expr_stmt|;
block|}
else|else
block|{
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
name|body
argument_list|)
decl_stmt|;
comment|// Read InputStream into a byte[] buffer
name|element
operator|=
operator|new
name|Element
argument_list|(
name|key
argument_list|,
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
name|byte
index|[]
operator|.
expr|class
argument_list|,
name|is
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// set overrides for the cache expiration and such
specifier|final
name|Integer
name|ttl
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|CacheConstants
operator|.
name|CACHE_ELEMENT_EXPIRY_TTL
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|ttl
operator|!=
literal|null
condition|)
block|{
name|element
operator|.
name|setTimeToLive
argument_list|(
name|ttl
argument_list|)
expr_stmt|;
block|}
specifier|final
name|Integer
name|idle
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|CacheConstants
operator|.
name|CACHE_ELEMENT_EXPIRY_IDLE
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|idle
operator|!=
literal|null
condition|)
block|{
name|element
operator|.
name|setTimeToIdle
argument_list|(
name|idle
argument_list|)
expr_stmt|;
block|}
specifier|final
name|Boolean
name|flag
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|CacheConstants
operator|.
name|CACHE_ELEMENT_EXPIRY_ETERNAL
argument_list|,
name|Boolean
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|flag
operator|!=
literal|null
condition|)
block|{
name|element
operator|.
name|setEternal
argument_list|(
name|flag
argument_list|)
expr_stmt|;
block|}
return|return
name|element
return|;
block|}
block|}
end_class

end_unit

