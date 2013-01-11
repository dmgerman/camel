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
name|net
operator|.
name|sf
operator|.
name|ehcache
operator|.
name|Cache
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
name|net
operator|.
name|sf
operator|.
name|ehcache
operator|.
name|Status
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
name|event
operator|.
name|CacheEventListener
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
name|loader
operator|.
name|CacheLoader
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
name|Message
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
name|Produce
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
name|ProducerTemplate
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
name|BaseCacheTest
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
DECL|class|CacheRegistryRefTest
specifier|public
class|class
name|CacheRegistryRefTest
extends|extends
name|BaseCacheTest
block|{
DECL|field|CACHE_ENDPOINT_URI
specifier|private
specifier|static
specifier|final
name|String
name|CACHE_ENDPOINT_URI
init|=
literal|"cache://foo"
operator|+
literal|"?eventListenerRegistry=#eventListenerRegistry&cacheLoaderRegistry=#cacheLoaderRegistry"
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
name|CACHE_ENDPOINT_URI
argument_list|)
DECL|field|cacheEndpoint
specifier|protected
name|CacheEndpoint
name|cacheEndpoint
decl_stmt|;
annotation|@
name|Produce
argument_list|(
name|uri
operator|=
literal|"direct:start"
argument_list|)
DECL|field|producerTemplate
specifier|protected
name|ProducerTemplate
name|producerTemplate
decl_stmt|;
DECL|field|eventListenerRegistry
specifier|private
name|CacheEventListenerRegistry
name|eventListenerRegistry
init|=
operator|new
name|CacheEventListenerRegistry
argument_list|()
decl_stmt|;
DECL|field|loaderRegistry
specifier|private
name|CacheLoaderRegistry
name|loaderRegistry
init|=
operator|new
name|CacheLoaderRegistry
argument_list|()
decl_stmt|;
annotation|@
name|Override
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|eventListenerRegistry
operator|.
name|addCacheEventListener
argument_list|(
operator|new
name|TestCacheEventListener
argument_list|()
argument_list|)
expr_stmt|;
name|loaderRegistry
operator|.
name|addCacheLoader
argument_list|(
operator|new
name|TestLoader
argument_list|()
argument_list|)
expr_stmt|;
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createRegistry ()
specifier|protected
name|JndiRegistry
name|createRegistry
parameter_list|()
throws|throws
name|Exception
block|{
name|JndiRegistry
name|jndi
init|=
name|super
operator|.
name|createRegistry
argument_list|()
decl_stmt|;
name|jndi
operator|.
name|bind
argument_list|(
literal|"eventListenerRegistry"
argument_list|,
name|eventListenerRegistry
argument_list|)
expr_stmt|;
name|jndi
operator|.
name|bind
argument_list|(
literal|"cacheLoaderRegistry"
argument_list|,
name|loaderRegistry
argument_list|)
expr_stmt|;
return|return
name|jndi
return|;
block|}
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|public
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
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|to
argument_list|(
name|CACHE_ENDPOINT_URI
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
annotation|@
name|Test
DECL|method|testConfig ()
specifier|public
name|void
name|testConfig
parameter_list|()
throws|throws
name|Exception
block|{
name|producerTemplate
operator|.
name|send
argument_list|(
operator|new
name|Processor
argument_list|()
block|{
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
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|CHARSET_NAME
argument_list|,
literal|"UTF-8"
argument_list|)
expr_stmt|;
name|Message
name|in
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|in
operator|.
name|setHeader
argument_list|(
name|CacheConstants
operator|.
name|CACHE_OPERATION
argument_list|,
name|CacheConstants
operator|.
name|CACHE_OPERATION_ADD
argument_list|)
expr_stmt|;
name|in
operator|.
name|setHeader
argument_list|(
name|CacheConstants
operator|.
name|CACHE_KEY
argument_list|,
literal|"greeting"
argument_list|)
expr_stmt|;
name|in
operator|.
name|setBody
argument_list|(
literal|"Hello World"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|CacheManager
name|cm
init|=
name|cacheEndpoint
operator|.
name|getCacheManagerFactory
argument_list|()
operator|.
name|getInstance
argument_list|()
decl_stmt|;
name|Cache
name|cache
init|=
name|cm
operator|.
name|getCache
argument_list|(
name|cacheEndpoint
operator|.
name|getConfig
argument_list|()
operator|.
name|getCacheName
argument_list|()
argument_list|)
decl_stmt|;
name|Set
argument_list|<
name|CacheEventListener
argument_list|>
name|ehcacheEventListners
init|=
name|cache
operator|.
name|getCacheEventNotificationService
argument_list|()
operator|.
name|getCacheEventListeners
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|CacheLoader
argument_list|>
name|cacheLoaders
init|=
name|cache
operator|.
name|getRegisteredCacheLoaders
argument_list|()
decl_stmt|;
name|CacheEventListenerRegistry
name|configuredEventRegistry
init|=
name|cacheEndpoint
operator|.
name|getConfig
argument_list|()
operator|.
name|getEventListenerRegistry
argument_list|()
decl_stmt|;
name|CacheLoaderRegistry
name|configuredLoaderRegistry
init|=
name|cacheEndpoint
operator|.
name|getConfig
argument_list|()
operator|.
name|getCacheLoaderRegistry
argument_list|()
decl_stmt|;
comment|//Test if CacheEventListenerRegistry was referenced correctly
name|assertEquals
argument_list|(
literal|"CacheEventListenerRegistry size"
argument_list|,
literal|1
argument_list|,
name|configuredEventRegistry
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|//Expecting 1 loader to be configured via spring
name|assertEquals
argument_list|(
literal|"configuredLoaderRegistry size"
argument_list|,
literal|1
argument_list|,
name|configuredLoaderRegistry
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|//Expecting 2 listeners- one added by us: TestCacheEventListener and
comment|//one added by ehcache by cfg file.
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"EventListenser is "
operator|+
name|ehcacheEventListners
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Number of registered listeners"
argument_list|,
literal|2
argument_list|,
name|ehcacheEventListners
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Number of registered loaders"
argument_list|,
literal|1
argument_list|,
name|cacheLoaders
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|//Is our TestCacheEventListener really invoked?
name|int
name|puts
init|=
literal|0
decl_stmt|;
for|for
control|(
name|Object
name|listener
range|:
name|ehcacheEventListners
control|)
block|{
if|if
condition|(
name|listener
operator|instanceof
name|TestCacheEventListener
condition|)
block|{
name|puts
operator|=
operator|(
operator|(
name|TestCacheEventListener
operator|)
name|listener
operator|)
operator|.
name|getNumberOfPuts
argument_list|()
expr_stmt|;
break|break;
block|}
block|}
name|assertEquals
argument_list|(
literal|"TestCacheEventListener put invocations"
argument_list|,
literal|1
argument_list|,
name|puts
argument_list|)
expr_stmt|;
comment|//Is cache loader initialized by ehcache
name|assertEquals
argument_list|(
literal|"loader initialized"
argument_list|,
name|cacheLoaders
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getStatus
argument_list|()
argument_list|,
name|Status
operator|.
name|STATUS_ALIVE
argument_list|)
expr_stmt|;
block|}
DECL|class|TestingCacheManagerFactory
specifier|public
specifier|static
class|class
name|TestingCacheManagerFactory
extends|extends
name|CacheManagerFactory
block|{
annotation|@
name|Override
DECL|method|createCacheManagerInstance ()
specifier|protected
name|CacheManager
name|createCacheManagerInstance
parameter_list|()
block|{
return|return
name|CacheManager
operator|.
name|create
argument_list|(
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"/ehcache.xml"
argument_list|)
argument_list|)
return|;
block|}
block|}
DECL|class|TestLoader
specifier|public
class|class
name|TestLoader
implements|implements
name|CacheLoaderWrapper
block|{
DECL|field|cache
specifier|protected
name|Ehcache
name|cache
decl_stmt|;
DECL|field|status
specifier|private
name|Status
name|status
decl_stmt|;
DECL|method|TestLoader ()
specifier|public
name|TestLoader
parameter_list|()
block|{
name|status
operator|=
name|Status
operator|.
name|STATUS_UNINITIALISED
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|clone (Ehcache arg0)
specifier|public
name|CacheLoader
name|clone
parameter_list|(
name|Ehcache
name|arg0
parameter_list|)
throws|throws
name|CloneNotSupportedException
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|dispose ()
specifier|public
name|void
name|dispose
parameter_list|()
throws|throws
name|CacheException
block|{
name|status
operator|=
name|Status
operator|.
name|STATUS_SHUTDOWN
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getName ()
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
literal|"Testing cache loader"
return|;
block|}
annotation|@
name|Override
DECL|method|getStatus ()
specifier|public
name|Status
name|getStatus
parameter_list|()
block|{
return|return
name|status
return|;
block|}
annotation|@
name|Override
DECL|method|init ()
specifier|public
name|void
name|init
parameter_list|()
block|{
name|status
operator|=
name|Status
operator|.
name|STATUS_ALIVE
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|load (Object arg0)
specifier|public
name|Object
name|load
parameter_list|(
name|Object
name|arg0
parameter_list|)
throws|throws
name|CacheException
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|load (Object arg0, Object arg1)
specifier|public
name|Object
name|load
parameter_list|(
name|Object
name|arg0
parameter_list|,
name|Object
name|arg1
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
annotation|@
name|SuppressWarnings
argument_list|(
literal|"rawtypes"
argument_list|)
DECL|method|loadAll (Collection arg0)
specifier|public
name|Map
name|loadAll
parameter_list|(
name|Collection
name|arg0
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
annotation|@
name|SuppressWarnings
argument_list|(
literal|"rawtypes"
argument_list|)
DECL|method|loadAll (Collection arg0, Object arg1)
specifier|public
name|Map
name|loadAll
parameter_list|(
name|Collection
name|arg0
parameter_list|,
name|Object
name|arg1
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|init (Ehcache cache)
specifier|public
name|void
name|init
parameter_list|(
name|Ehcache
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
block|}
comment|//Test event lister that will help us to count put method invocations.
DECL|class|TestCacheEventListener
specifier|public
class|class
name|TestCacheEventListener
implements|implements
name|CacheEventListener
block|{
DECL|field|numberOfPuts
specifier|private
name|int
name|numberOfPuts
decl_stmt|;
annotation|@
name|Override
DECL|method|dispose ()
specifier|public
name|void
name|dispose
parameter_list|()
block|{         }
annotation|@
name|Override
DECL|method|notifyElementEvicted (Ehcache arg0, Element arg1)
specifier|public
name|void
name|notifyElementEvicted
parameter_list|(
name|Ehcache
name|arg0
parameter_list|,
name|Element
name|arg1
parameter_list|)
block|{         }
annotation|@
name|Override
DECL|method|notifyElementExpired (Ehcache arg0, Element arg1)
specifier|public
name|void
name|notifyElementExpired
parameter_list|(
name|Ehcache
name|arg0
parameter_list|,
name|Element
name|arg1
parameter_list|)
block|{         }
annotation|@
name|Override
DECL|method|notifyElementPut (Ehcache arg0, Element arg1)
specifier|public
name|void
name|notifyElementPut
parameter_list|(
name|Ehcache
name|arg0
parameter_list|,
name|Element
name|arg1
parameter_list|)
throws|throws
name|CacheException
block|{
name|numberOfPuts
operator|++
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|notifyElementRemoved (Ehcache arg0, Element arg1)
specifier|public
name|void
name|notifyElementRemoved
parameter_list|(
name|Ehcache
name|arg0
parameter_list|,
name|Element
name|arg1
parameter_list|)
throws|throws
name|CacheException
block|{         }
annotation|@
name|Override
DECL|method|notifyElementUpdated (Ehcache arg0, Element arg1)
specifier|public
name|void
name|notifyElementUpdated
parameter_list|(
name|Ehcache
name|arg0
parameter_list|,
name|Element
name|arg1
parameter_list|)
throws|throws
name|CacheException
block|{         }
annotation|@
name|Override
DECL|method|notifyRemoveAll (Ehcache arg0)
specifier|public
name|void
name|notifyRemoveAll
parameter_list|(
name|Ehcache
name|arg0
parameter_list|)
block|{         }
annotation|@
name|Override
DECL|method|clone ()
specifier|public
name|TestCacheEventListener
name|clone
parameter_list|()
block|{
return|return
name|this
operator|.
name|clone
argument_list|()
return|;
block|}
DECL|method|getNumberOfPuts ()
specifier|public
name|int
name|getNumberOfPuts
parameter_list|()
block|{
return|return
name|numberOfPuts
return|;
block|}
block|}
block|}
end_class

end_unit

