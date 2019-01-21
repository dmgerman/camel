begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jcache.processor.idempotent
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jcache
operator|.
name|processor
operator|.
name|idempotent
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|cache
operator|.
name|Cache
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
name|api
operator|.
name|management
operator|.
name|ManagedAttribute
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
name|api
operator|.
name|management
operator|.
name|ManagedOperation
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
name|api
operator|.
name|management
operator|.
name|ManagedResource
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
name|jcache
operator|.
name|JCacheConfiguration
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
name|jcache
operator|.
name|JCacheHelper
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
name|jcache
operator|.
name|JCacheManager
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
name|IdempotentRepository
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
name|service
operator|.
name|ServiceSupport
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
name|ObjectHelper
import|;
end_import

begin_class
annotation|@
name|ManagedResource
argument_list|(
name|description
operator|=
literal|"JCache based message id repository"
argument_list|)
DECL|class|JCacheIdempotentRepository
specifier|public
class|class
name|JCacheIdempotentRepository
extends|extends
name|ServiceSupport
implements|implements
name|IdempotentRepository
block|{
DECL|field|configuration
specifier|private
name|JCacheConfiguration
name|configuration
decl_stmt|;
DECL|field|cache
specifier|private
name|Cache
argument_list|<
name|String
argument_list|,
name|Boolean
argument_list|>
name|cache
decl_stmt|;
DECL|field|cacheManager
specifier|private
name|JCacheManager
argument_list|<
name|String
argument_list|,
name|Boolean
argument_list|>
name|cacheManager
decl_stmt|;
DECL|method|JCacheIdempotentRepository ()
specifier|public
name|JCacheIdempotentRepository
parameter_list|()
block|{
name|this
operator|.
name|configuration
operator|=
operator|new
name|JCacheConfiguration
argument_list|()
expr_stmt|;
block|}
DECL|method|getConfiguration ()
specifier|public
name|JCacheConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
DECL|method|setConfiguration (JCacheConfiguration configuration)
specifier|public
name|void
name|setConfiguration
parameter_list|(
name|JCacheConfiguration
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
DECL|method|getCache ()
specifier|public
name|Cache
argument_list|<
name|String
argument_list|,
name|Boolean
argument_list|>
name|getCache
parameter_list|()
block|{
return|return
name|cache
return|;
block|}
DECL|method|setCache (Cache<String, Boolean> cache)
specifier|public
name|void
name|setCache
parameter_list|(
name|Cache
argument_list|<
name|String
argument_list|,
name|Boolean
argument_list|>
name|cache
parameter_list|)
block|{
name|this
operator|.
name|cache
operator|=
name|cache
expr_stmt|;
block|}
annotation|@
name|Override
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Adds the key to the store"
argument_list|)
DECL|method|add (String key)
specifier|public
name|boolean
name|add
parameter_list|(
name|String
name|key
parameter_list|)
block|{
return|return
name|cache
operator|.
name|putIfAbsent
argument_list|(
name|key
argument_list|,
literal|true
argument_list|)
return|;
block|}
annotation|@
name|Override
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Does the store contain the given key"
argument_list|)
DECL|method|contains (String key)
specifier|public
name|boolean
name|contains
parameter_list|(
name|String
name|key
parameter_list|)
block|{
return|return
name|cache
operator|.
name|containsKey
argument_list|(
name|key
argument_list|)
return|;
block|}
annotation|@
name|Override
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Remove the key from the store"
argument_list|)
DECL|method|remove (String key)
specifier|public
name|boolean
name|remove
parameter_list|(
name|String
name|key
parameter_list|)
block|{
return|return
name|cache
operator|.
name|remove
argument_list|(
name|key
argument_list|)
return|;
block|}
annotation|@
name|Override
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Clear the store"
argument_list|)
DECL|method|clear ()
specifier|public
name|void
name|clear
parameter_list|()
block|{
name|cache
operator|.
name|clear
argument_list|()
expr_stmt|;
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
name|configuration
operator|.
name|setCacheName
argument_list|(
name|cacheName
argument_list|)
expr_stmt|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"The processor name"
argument_list|)
DECL|method|getCacheName ()
specifier|public
name|String
name|getCacheName
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getCacheName
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|confirm (String key)
specifier|public
name|boolean
name|confirm
parameter_list|(
name|String
name|key
parameter_list|)
block|{
return|return
name|cache
operator|.
name|replace
argument_list|(
name|key
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|)
return|;
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
if|if
condition|(
name|cache
operator|!=
literal|null
condition|)
block|{
name|cacheManager
operator|=
operator|new
name|JCacheManager
argument_list|<>
argument_list|(
name|cache
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|cacheManager
operator|=
name|JCacheHelper
operator|.
name|createManager
argument_list|(
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|configuration
argument_list|,
literal|"configuration"
argument_list|)
argument_list|)
expr_stmt|;
name|cache
operator|=
name|cacheManager
operator|.
name|getCache
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
name|cacheManager
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

