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
DECL|class|CacheValidate
specifier|public
class|class
name|CacheValidate
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
name|CacheValidate
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|isValid (CacheManager cacheManager, String cacheName, String key)
specifier|public
name|boolean
name|isValid
parameter_list|(
name|CacheManager
name|cacheManager
parameter_list|,
name|String
name|cacheName
parameter_list|,
name|String
name|key
parameter_list|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Cache Name: {}"
argument_list|,
name|cacheName
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|cacheManager
operator|.
name|cacheExists
argument_list|(
name|cacheName
argument_list|)
condition|)
block|{
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"No existing Cache found with name: "
operator|+
name|cacheName
operator|+
literal|". Please ensure a cache is first instantiated using a Cache Consumer or Cache Producer."
operator|+
literal|" Replacement will not be performed since the cache "
operator|+
name|cacheName
operator|+
literal|"does not presently exist"
argument_list|)
expr_stmt|;
block|}
return|return
literal|false
return|;
block|}
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Found an existing cache: "
operator|+
name|cacheName
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Cache {} currently contains {} elements"
argument_list|,
name|cacheName
argument_list|,
name|cacheManager
operator|.
name|getCache
argument_list|(
name|cacheName
argument_list|)
operator|.
name|getSize
argument_list|()
argument_list|)
expr_stmt|;
block|}
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
if|if
condition|(
operator|!
name|cache
operator|.
name|isKeyInCache
argument_list|(
name|key
argument_list|)
condition|)
block|{
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"No Key with name: "
operator|+
name|key
operator|+
literal|"presently exists in the cache. It is also possible that the key may have expired in the cache."
operator|+
literal|" Replacement will not be performed until an appropriate key/value pair is added to (or) found in the cache."
argument_list|)
expr_stmt|;
block|}
return|return
literal|false
return|;
block|}
return|return
literal|true
return|;
block|}
block|}
end_class

end_unit

