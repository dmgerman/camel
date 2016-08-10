begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jcache.osgi
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
name|osgi
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|BufferedReader
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
name|InputStreamReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Enumeration
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
name|component
operator|.
name|jcache
operator|.
name|JCacheProvider
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
name|JCacheProviders
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

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|Bundle
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|BundleContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|FrameworkUtil
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|wiring
operator|.
name|BundleWiring
import|;
end_import

begin_class
DECL|class|OSGiCacheManager
specifier|public
specifier|final
class|class
name|OSGiCacheManager
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
extends|extends
name|JCacheManager
block|{
DECL|method|OSGiCacheManager (JCacheConfiguration configuration)
specifier|public
name|OSGiCacheManager
parameter_list|(
name|JCacheConfiguration
name|configuration
parameter_list|)
block|{
name|super
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doGetCache (JCacheProvider provider)
specifier|public
specifier|synchronized
name|Cache
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|doGetCache
parameter_list|(
name|JCacheProvider
name|provider
parameter_list|)
throws|throws
name|Exception
block|{
specifier|final
name|ClassLoader
name|jcl
init|=
name|getClassLoader
argument_list|(
name|provider
operator|.
name|className
argument_list|()
argument_list|)
decl_stmt|;
specifier|final
name|ClassLoader
name|tccl
init|=
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
decl_stmt|;
try|try
block|{
if|if
condition|(
name|jcl
operator|!=
literal|null
condition|)
block|{
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|setContextClassLoader
argument_list|(
name|jcl
argument_list|)
expr_stmt|;
block|}
name|Cache
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|cache
init|=
name|super
operator|.
name|doGetCache
argument_list|(
name|provider
argument_list|)
decl_stmt|;
if|if
condition|(
name|provider
operator|==
name|JCacheProviders
operator|.
name|hazelcast
operator|&&
name|jcl
operator|!=
literal|null
condition|)
block|{
name|cache
operator|=
name|JCacheHelper
operator|.
name|tcclProxy
argument_list|(
name|cache
argument_list|,
name|Cache
operator|.
name|class
argument_list|,
name|jcl
argument_list|)
expr_stmt|;
block|}
return|return
name|cache
return|;
block|}
finally|finally
block|{
if|if
condition|(
name|jcl
operator|!=
literal|null
condition|)
block|{
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|setContextClassLoader
argument_list|(
name|tccl
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|getClassLoader (String providerName)
specifier|private
name|ClassLoader
name|getClassLoader
parameter_list|(
name|String
name|providerName
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|providerName
operator|==
literal|null
operator|||
operator|!
name|getConfiguration
argument_list|()
operator|.
name|isLookupProviders
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
specifier|final
name|BundleContext
name|bc
init|=
name|FrameworkUtil
operator|.
name|getBundle
argument_list|(
name|JCacheHelper
operator|.
name|class
argument_list|)
operator|.
name|getBundleContext
argument_list|()
decl_stmt|;
specifier|final
name|ClassLoader
name|bcl
init|=
name|bc
operator|.
name|getBundle
argument_list|()
operator|.
name|adapt
argument_list|(
name|BundleWiring
operator|.
name|class
argument_list|)
operator|.
name|getClassLoader
argument_list|()
decl_stmt|;
specifier|final
name|ClassLoader
name|acl
init|=
name|getConfiguration
argument_list|()
operator|.
name|getApplicationContextClassLoader
argument_list|()
decl_stmt|;
for|for
control|(
specifier|final
name|Bundle
name|bundle
range|:
name|bc
operator|.
name|getBundles
argument_list|()
control|)
block|{
name|URL
name|spi
init|=
name|bundle
operator|.
name|getResource
argument_list|(
literal|"META-INF/services/javax.cache.spi.CachingProvider"
argument_list|)
decl_stmt|;
if|if
condition|(
name|spi
operator|!=
literal|null
condition|)
block|{
try|try
init|(
name|BufferedReader
name|in
init|=
operator|new
name|BufferedReader
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
name|spi
operator|.
name|openStream
argument_list|()
argument_list|)
argument_list|)
init|)
block|{
if|if
condition|(
name|ObjectHelper
operator|.
name|equal
argument_list|(
name|providerName
argument_list|,
name|in
operator|.
name|readLine
argument_list|()
argument_list|)
condition|)
block|{
return|return
operator|new
name|ClassLoader
argument_list|(
name|bcl
argument_list|)
block|{
annotation|@
name|Override
specifier|protected
name|Class
argument_list|<
name|?
argument_list|>
name|findClass
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|ClassNotFoundException
block|{
try|try
block|{
return|return
name|acl
operator|.
name|loadClass
argument_list|(
name|name
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|ClassNotFoundException
name|e
parameter_list|)
block|{
return|return
name|bundle
operator|.
name|loadClass
argument_list|(
name|name
argument_list|)
return|;
block|}
block|}
annotation|@
name|Override
specifier|protected
name|URL
name|findResource
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|URL
name|resource
init|=
name|acl
operator|.
name|getResource
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|resource
operator|==
literal|null
condition|)
block|{
name|resource
operator|=
name|bundle
operator|.
name|getResource
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
return|return
name|resource
return|;
block|}
annotation|@
name|Override
specifier|protected
name|Enumeration
name|findResources
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|IOException
block|{
try|try
block|{
return|return
name|acl
operator|.
name|getResources
argument_list|(
name|name
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
return|return
name|bundle
operator|.
name|getResources
argument_list|(
name|name
argument_list|)
return|;
block|}
block|}
block|}
return|;
block|}
block|}
block|}
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

