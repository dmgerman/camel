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
DECL|class|CacheEventListener
specifier|public
class|class
name|CacheEventListener
implements|implements
name|net
operator|.
name|sf
operator|.
name|ehcache
operator|.
name|event
operator|.
name|CacheEventListener
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|CacheEventListener
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|cacheConsumer
name|CacheConsumer
name|cacheConsumer
decl_stmt|;
DECL|method|CacheEventListener ()
specifier|public
name|CacheEventListener
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
block|}
DECL|method|CacheEventListener (CacheConsumer cacheConsumer)
specifier|public
name|CacheEventListener
parameter_list|(
name|CacheConsumer
name|cacheConsumer
parameter_list|)
block|{
name|super
argument_list|()
expr_stmt|;
name|this
operator|.
name|cacheConsumer
operator|=
name|cacheConsumer
expr_stmt|;
block|}
DECL|method|notifyElementEvicted (Ehcache cache, Element element)
specifier|public
name|void
name|notifyElementEvicted
parameter_list|(
name|Ehcache
name|cache
parameter_list|,
name|Element
name|element
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Element {} is being evicted from cache {}"
argument_list|,
name|element
argument_list|,
name|cache
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|notifyElementExpired (Ehcache cache, Element element)
specifier|public
name|void
name|notifyElementExpired
parameter_list|(
name|Ehcache
name|cache
parameter_list|,
name|Element
name|element
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Element {} has expired in cache {}"
argument_list|,
name|element
argument_list|,
name|cache
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|notifyElementPut (Ehcache cache, Element element)
specifier|public
name|void
name|notifyElementPut
parameter_list|(
name|Ehcache
name|cache
parameter_list|,
name|Element
name|element
parameter_list|)
throws|throws
name|CacheException
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Element {} has just been added/put in cache {}"
argument_list|,
name|element
argument_list|,
name|cache
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|dispatchExchange
argument_list|(
name|cache
argument_list|,
name|element
argument_list|,
literal|"ADD"
argument_list|)
expr_stmt|;
block|}
DECL|method|notifyElementRemoved (Ehcache cache, Element element)
specifier|public
name|void
name|notifyElementRemoved
parameter_list|(
name|Ehcache
name|cache
parameter_list|,
name|Element
name|element
parameter_list|)
throws|throws
name|CacheException
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Element {} has just been removed from cache {}"
argument_list|,
name|element
argument_list|,
name|cache
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|dispatchExchange
argument_list|(
name|cache
argument_list|,
name|element
argument_list|,
literal|"DELETE"
argument_list|)
expr_stmt|;
block|}
DECL|method|notifyElementUpdated (Ehcache cache, Element element)
specifier|public
name|void
name|notifyElementUpdated
parameter_list|(
name|Ehcache
name|cache
parameter_list|,
name|Element
name|element
parameter_list|)
throws|throws
name|CacheException
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Element {} has just been updated in cache {}"
argument_list|,
name|element
argument_list|,
name|cache
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|dispatchExchange
argument_list|(
name|cache
argument_list|,
name|element
argument_list|,
literal|"UPDATE"
argument_list|)
expr_stmt|;
block|}
DECL|method|notifyRemoveAll (Ehcache cache)
specifier|public
name|void
name|notifyRemoveAll
parameter_list|(
name|Ehcache
name|cache
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Cache {} is being emptied and all elements removed"
argument_list|,
name|cache
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|dispatchExchange
argument_list|(
name|cache
argument_list|,
literal|null
argument_list|,
literal|"DELETEALL"
argument_list|)
expr_stmt|;
block|}
DECL|method|dispatchExchange (Ehcache cache, Element element, String operation)
specifier|private
name|void
name|dispatchExchange
parameter_list|(
name|Ehcache
name|cache
parameter_list|,
name|Element
name|element
parameter_list|,
name|String
name|operation
parameter_list|)
block|{
name|Exchange
name|exchange
decl_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Consumer Dispatching the Exchange containing the Element {} in cache {}"
argument_list|,
name|element
argument_list|,
name|cache
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|element
operator|==
literal|null
condition|)
block|{
name|exchange
operator|=
name|cacheConsumer
operator|.
name|getEndpoint
argument_list|()
operator|.
name|createCacheExchange
argument_list|(
name|operation
argument_list|,
literal|""
argument_list|,
literal|""
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|exchange
operator|=
name|cacheConsumer
operator|.
name|getEndpoint
argument_list|()
operator|.
name|createCacheExchange
argument_list|(
name|operation
argument_list|,
operator|(
name|String
operator|)
name|element
operator|.
name|getObjectKey
argument_list|()
argument_list|,
name|element
operator|.
name|getObjectValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|cacheConsumer
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
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|CacheException
argument_list|(
literal|"Error in consumer while dispatching exchange containing key "
operator|+
operator|(
name|element
operator|!=
literal|null
condition|?
name|element
operator|.
name|getObjectKey
argument_list|()
else|:
literal|null
operator|)
operator|+
literal|" for further processing"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|getCacheConsumer ()
specifier|public
name|CacheConsumer
name|getCacheConsumer
parameter_list|()
block|{
return|return
name|cacheConsumer
return|;
block|}
DECL|method|setCacheConsumer (CacheConsumer cacheConsumer)
specifier|public
name|void
name|setCacheConsumer
parameter_list|(
name|CacheConsumer
name|cacheConsumer
parameter_list|)
block|{
name|this
operator|.
name|cacheConsumer
operator|=
name|cacheConsumer
expr_stmt|;
block|}
DECL|method|dispose ()
specifier|public
name|void
name|dispose
parameter_list|()
block|{
comment|// noop
block|}
DECL|method|clone ()
specifier|public
name|Object
name|clone
parameter_list|()
throws|throws
name|CloneNotSupportedException
block|{
return|return
name|super
operator|.
name|clone
argument_list|()
return|;
block|}
block|}
end_class

end_unit

