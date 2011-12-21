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
name|CacheManager
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
name|impl
operator|.
name|JndiRegistry
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
name|test
operator|.
name|junit4
operator|.
name|CamelTestSupport
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
DECL|class|CacheManagerFactoryRefTest
specifier|public
class|class
name|CacheManagerFactoryRefTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|CACHE_ENDPOINT_URI
specifier|private
specifier|static
specifier|final
name|String
name|CACHE_ENDPOINT_URI
init|=
literal|"cache://myname1?cacheManagerFactory=#testCacheManagerFactory"
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
DECL|field|testingCacheManagerFactory
specifier|protected
name|CacheManagerFactory
name|testingCacheManagerFactory
decl_stmt|;
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
name|testingCacheManagerFactory
operator|=
operator|new
name|TestingCacheManagerFactory
argument_list|()
expr_stmt|;
name|jndi
operator|.
name|bind
argument_list|(
literal|"testCacheManagerFactory"
argument_list|,
name|testingCacheManagerFactory
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
name|assertEquals
argument_list|(
literal|"Is CacheManagerFactory set"
argument_list|,
name|testingCacheManagerFactory
argument_list|,
name|cacheEndpoint
operator|.
name|getCacheManagerFactory
argument_list|()
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
block|}
end_class

end_unit

