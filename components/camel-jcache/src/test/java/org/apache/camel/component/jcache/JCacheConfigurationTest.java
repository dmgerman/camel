begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jcache
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
package|;
end_package

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
name|Collection
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
name|cache
operator|.
name|Cache
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|cache
operator|.
name|configuration
operator|.
name|CompleteConfiguration
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|cache
operator|.
name|configuration
operator|.
name|Factory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|cache
operator|.
name|configuration
operator|.
name|FactoryBuilder
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|cache
operator|.
name|expiry
operator|.
name|AccessedExpiryPolicy
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|cache
operator|.
name|expiry
operator|.
name|Duration
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|cache
operator|.
name|expiry
operator|.
name|ExpiryPolicy
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|cache
operator|.
name|integration
operator|.
name|CacheLoader
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|cache
operator|.
name|integration
operator|.
name|CacheLoaderException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|cache
operator|.
name|integration
operator|.
name|CacheWriter
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|cache
operator|.
name|integration
operator|.
name|CacheWriterException
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
name|BindToRegistry
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
name|EndpointInject
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
name|RouteBuilder
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
name|mock
operator|.
name|MockEndpoint
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
name|JndiRegistry
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|JCacheConfigurationTest
specifier|public
class|class
name|JCacheConfigurationTest
extends|extends
name|JCacheComponentTestSupport
block|{
annotation|@
name|BindToRegistry
argument_list|(
literal|"myExpiryPolicyFactory"
argument_list|)
DECL|field|EXPIRY_POLICY_FACTORY
specifier|private
specifier|static
specifier|final
name|Factory
argument_list|<
name|ExpiryPolicy
argument_list|>
name|EXPIRY_POLICY_FACTORY
init|=
name|AccessedExpiryPolicy
operator|.
name|factoryOf
argument_list|(
name|Duration
operator|.
name|ONE_MINUTE
argument_list|)
decl_stmt|;
annotation|@
name|BindToRegistry
argument_list|(
literal|"myCacheWriterFactory"
argument_list|)
DECL|field|CACHE_WRITER_FACTORY
specifier|private
specifier|static
specifier|final
name|Factory
argument_list|<
name|CacheWriter
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
argument_list|>
name|CACHE_WRITER_FACTORY
init|=
name|MyCacheWriter
operator|.
name|factory
argument_list|()
decl_stmt|;
annotation|@
name|BindToRegistry
argument_list|(
literal|"myCacheLoaderFactory"
argument_list|)
DECL|field|CACHE_LOADER_FACTORY
specifier|private
specifier|static
specifier|final
name|Factory
argument_list|<
name|CacheLoader
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
argument_list|>
name|CACHE_LOADER_FACTORY
init|=
name|MyCacheLoader
operator|.
name|factory
argument_list|()
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
name|value
operator|=
literal|"jcache://test-cache"
operator|+
literal|"?expiryPolicyFactory=#myExpiryPolicyFactory"
operator|+
literal|"&cacheWriterFactory=#myCacheWriterFactory"
operator|+
literal|"&cacheLoaderFactory=#myCacheLoaderFactory"
argument_list|)
DECL|field|from
name|JCacheEndpoint
name|from
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
literal|"mock:to"
argument_list|)
DECL|field|to
name|MockEndpoint
name|to
decl_stmt|;
annotation|@
name|Test
DECL|method|testConfigurations ()
specifier|public
name|void
name|testConfigurations
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|Cache
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|cache
init|=
name|from
operator|.
name|getManager
argument_list|()
operator|.
name|getCache
argument_list|()
decl_stmt|;
specifier|final
name|CompleteConfiguration
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|conf
init|=
name|cache
operator|.
name|getConfiguration
argument_list|(
name|CompleteConfiguration
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|EXPIRY_POLICY_FACTORY
argument_list|,
name|conf
operator|.
name|getExpiryPolicyFactory
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|CACHE_WRITER_FACTORY
argument_list|,
name|conf
operator|.
name|getCacheWriterFactory
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|CACHE_LOADER_FACTORY
argument_list|,
name|conf
operator|.
name|getCacheLoaderFactory
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
name|from
argument_list|)
operator|.
name|to
argument_list|(
name|to
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|class|MyCacheLoader
specifier|private
specifier|static
specifier|final
class|class
name|MyCacheLoader
implements|implements
name|CacheLoader
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
implements|,
name|Serializable
block|{
annotation|@
name|Override
DECL|method|load (Object key)
specifier|public
name|Object
name|load
parameter_list|(
name|Object
name|key
parameter_list|)
throws|throws
name|CacheLoaderException
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|loadAll (Iterable<?> keys)
specifier|public
name|Map
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|loadAll
parameter_list|(
name|Iterable
argument_list|<
name|?
argument_list|>
name|keys
parameter_list|)
throws|throws
name|CacheLoaderException
block|{
return|return
literal|null
return|;
block|}
DECL|method|factory ()
specifier|public
specifier|static
name|Factory
argument_list|<
name|CacheLoader
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
argument_list|>
name|factory
parameter_list|()
block|{
return|return
operator|new
name|FactoryBuilder
operator|.
name|SingletonFactory
argument_list|(
operator|new
name|MyCacheLoader
argument_list|()
argument_list|)
return|;
block|}
block|}
DECL|class|MyCacheWriter
specifier|private
specifier|static
specifier|final
class|class
name|MyCacheWriter
implements|implements
name|CacheWriter
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
implements|,
name|Serializable
block|{
annotation|@
name|Override
DECL|method|write (Cache.Entry<?, ?> entry)
specifier|public
name|void
name|write
parameter_list|(
name|Cache
operator|.
name|Entry
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|entry
parameter_list|)
throws|throws
name|CacheWriterException
block|{         }
annotation|@
name|Override
DECL|method|writeAll (Collection<Cache.Entry<?, ?>> entries)
specifier|public
name|void
name|writeAll
parameter_list|(
name|Collection
argument_list|<
name|Cache
operator|.
name|Entry
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
argument_list|>
name|entries
parameter_list|)
throws|throws
name|CacheWriterException
block|{         }
annotation|@
name|Override
DECL|method|delete (Object key)
specifier|public
name|void
name|delete
parameter_list|(
name|Object
name|key
parameter_list|)
throws|throws
name|CacheWriterException
block|{         }
annotation|@
name|Override
DECL|method|deleteAll (Collection<?> keys)
specifier|public
name|void
name|deleteAll
parameter_list|(
name|Collection
argument_list|<
name|?
argument_list|>
name|keys
parameter_list|)
throws|throws
name|CacheWriterException
block|{         }
DECL|method|factory ()
specifier|public
specifier|static
name|Factory
argument_list|<
name|CacheWriter
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
argument_list|>
name|factory
parameter_list|()
block|{
return|return
operator|new
name|FactoryBuilder
operator|.
name|SingletonFactory
argument_list|(
operator|new
name|MyCacheWriter
argument_list|()
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

