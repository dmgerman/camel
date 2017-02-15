begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor.cache
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
operator|.
name|cache
package|;
end_package

begin_import
import|import
name|net
operator|.
name|sf
operator|.
name|ehcache
operator|.
name|CacheManager
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
name|Expression
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
name|Service
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
name|builder
operator|.
name|ExpressionBuilder
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
name|cache
operator|.
name|CacheConstants
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
name|cache
operator|.
name|DefaultCacheManagerFactory
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
DECL|class|CacheBasedMessageBodyReplacer
specifier|public
class|class
name|CacheBasedMessageBodyReplacer
extends|extends
name|CacheValidate
implements|implements
name|Processor
implements|,
name|Service
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
name|CacheBasedMessageBodyReplacer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|cacheManager
specifier|private
name|CacheManager
name|cacheManager
decl_stmt|;
DECL|field|cacheName
specifier|private
name|String
name|cacheName
decl_stmt|;
DECL|field|key
specifier|private
name|Expression
name|key
decl_stmt|;
DECL|method|CacheBasedMessageBodyReplacer (String cacheName, String key)
specifier|public
name|CacheBasedMessageBodyReplacer
parameter_list|(
name|String
name|cacheName
parameter_list|,
name|String
name|key
parameter_list|)
block|{
name|this
argument_list|(
name|cacheName
argument_list|,
name|ExpressionBuilder
operator|.
name|constantExpression
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|CacheBasedMessageBodyReplacer (String cacheName, Expression key)
specifier|public
name|CacheBasedMessageBodyReplacer
parameter_list|(
name|String
name|cacheName
parameter_list|,
name|Expression
name|key
parameter_list|)
block|{
if|if
condition|(
name|cacheName
operator|.
name|contains
argument_list|(
literal|"cache://"
argument_list|)
condition|)
block|{
name|this
operator|.
name|setCacheName
argument_list|(
name|cacheName
operator|.
name|replace
argument_list|(
literal|"cache://"
argument_list|,
literal|""
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|this
operator|.
name|setCacheName
argument_list|(
name|cacheName
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|setKey
argument_list|(
name|key
argument_list|)
expr_stmt|;
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
name|String
name|cacheKey
init|=
name|key
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|isValid
argument_list|(
name|cacheManager
argument_list|,
name|cacheName
argument_list|,
name|cacheKey
argument_list|)
condition|)
block|{
name|Ehcache
name|cache
init|=
name|cacheManager
operator|.
name|getCache
argument_list|(
name|cacheName
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Replacing Message Body from CacheName {} for key {}"
argument_list|,
name|cacheName
argument_list|,
name|cacheKey
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|CacheConstants
operator|.
name|CACHE_KEY
argument_list|,
name|cacheKey
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
name|cacheKey
argument_list|)
operator|.
name|getObjectValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|getCacheName ()
specifier|public
name|String
name|getCacheName
parameter_list|()
block|{
return|return
name|cacheName
return|;
block|}
DECL|method|setCacheName (String cacheName)
specifier|public
name|void
name|setCacheName
parameter_list|(
name|String
name|cacheName
parameter_list|)
block|{
name|this
operator|.
name|cacheName
operator|=
name|cacheName
expr_stmt|;
block|}
DECL|method|getKey ()
specifier|public
name|Expression
name|getKey
parameter_list|()
block|{
return|return
name|key
return|;
block|}
DECL|method|setKey (String key)
specifier|public
name|void
name|setKey
parameter_list|(
name|String
name|key
parameter_list|)
block|{
name|this
operator|.
name|key
operator|=
name|ExpressionBuilder
operator|.
name|constantExpression
argument_list|(
name|key
argument_list|)
expr_stmt|;
block|}
DECL|method|setKey (Expression key)
specifier|public
name|void
name|setKey
parameter_list|(
name|Expression
name|key
parameter_list|)
block|{
name|this
operator|.
name|key
operator|=
name|key
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|start ()
specifier|public
name|void
name|start
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Cache the buffer to the specified Cache against the specified key
if|if
condition|(
name|cacheManager
operator|==
literal|null
condition|)
block|{
name|cacheManager
operator|=
operator|new
name|DefaultCacheManagerFactory
argument_list|()
operator|.
name|getInstance
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|stop ()
specifier|public
name|void
name|stop
parameter_list|()
throws|throws
name|Exception
block|{
comment|// noop
block|}
block|}
end_class

end_unit

