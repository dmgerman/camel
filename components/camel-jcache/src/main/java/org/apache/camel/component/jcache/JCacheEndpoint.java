begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Consumer
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
name|Producer
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
name|DefaultEndpoint
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
name|Metadata
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
name|UriEndpoint
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
name|UriParam
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
name|UriPath
import|;
end_import

begin_comment
comment|/**  * Represents a JCache endpoint.  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|scheme
operator|=
literal|"jcache"
argument_list|,
name|title
operator|=
literal|"JCache"
argument_list|,
name|syntax
operator|=
literal|"jcache:cacheName"
argument_list|,
name|consumerClass
operator|=
name|JCacheConsumer
operator|.
name|class
argument_list|,
name|label
operator|=
literal|"cache,datagrid,clustering"
argument_list|)
DECL|class|JCacheEndpoint
specifier|public
class|class
name|JCacheEndpoint
extends|extends
name|DefaultEndpoint
block|{
annotation|@
name|UriPath
argument_list|(
name|description
operator|=
literal|"the cache name"
argument_list|)
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|"true"
argument_list|)
DECL|field|cacheName
specifier|private
specifier|final
name|String
name|cacheName
decl_stmt|;
annotation|@
name|UriParam
DECL|field|cacheConfiguration
specifier|private
specifier|final
name|JCacheConfiguration
name|cacheConfiguration
decl_stmt|;
DECL|field|cacheManager
specifier|private
specifier|final
name|JCacheManager
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|cacheManager
decl_stmt|;
DECL|method|JCacheEndpoint (String uri, JCacheComponent component, JCacheConfiguration configuration, String cacheName)
specifier|public
name|JCacheEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|JCacheComponent
name|component
parameter_list|,
name|JCacheConfiguration
name|configuration
parameter_list|,
name|String
name|cacheName
parameter_list|)
block|{
name|super
argument_list|(
name|uri
argument_list|,
name|component
argument_list|)
expr_stmt|;
name|this
operator|.
name|cacheName
operator|=
name|cacheName
expr_stmt|;
name|this
operator|.
name|cacheConfiguration
operator|=
name|configuration
expr_stmt|;
name|this
operator|.
name|cacheManager
operator|=
operator|new
name|JCacheManager
argument_list|<>
argument_list|(
name|configuration
argument_list|,
name|cacheName
argument_list|,
name|component
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getApplicationContextClassLoader
argument_list|()
argument_list|,
name|component
operator|.
name|getCamelContext
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|JCacheProducer
argument_list|(
name|this
argument_list|,
name|cacheConfiguration
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createConsumer (Processor processor)
specifier|public
name|Consumer
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
return|return
operator|new
name|JCacheConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
literal|true
return|;
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
DECL|method|getManager ()
name|JCacheManager
name|getManager
parameter_list|()
block|{
return|return
name|cacheManager
return|;
block|}
block|}
end_class

end_unit

