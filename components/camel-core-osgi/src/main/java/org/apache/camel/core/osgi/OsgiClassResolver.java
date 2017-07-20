begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.core.osgi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|core
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
name|IOException
import|;
end_import

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
name|java
operator|.
name|util
operator|.
name|Vector
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
name|CamelContext
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
name|DefaultClassResolver
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
name|CastUtils
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
name|FileUtil
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

begin_comment
comment|/* Using the bundle of CamelContext to load the class */
end_comment

begin_class
DECL|class|OsgiClassResolver
specifier|public
class|class
name|OsgiClassResolver
extends|extends
name|DefaultClassResolver
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
name|OsgiClassResolver
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|camelContext
specifier|private
specifier|final
name|CamelContext
name|camelContext
decl_stmt|;
DECL|field|bundleContext
specifier|private
specifier|final
name|BundleContext
name|bundleContext
decl_stmt|;
DECL|method|OsgiClassResolver (CamelContext camelContext, BundleContext context)
specifier|public
name|OsgiClassResolver
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|BundleContext
name|context
parameter_list|)
block|{
name|super
argument_list|(
name|camelContext
argument_list|)
expr_stmt|;
name|this
operator|.
name|camelContext
operator|=
name|camelContext
expr_stmt|;
name|this
operator|.
name|bundleContext
operator|=
name|context
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|resolveClass (String name)
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
name|resolveClass
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Resolve class {}"
argument_list|,
name|name
argument_list|)
expr_stmt|;
name|name
operator|=
name|ObjectHelper
operator|.
name|normalizeClassName
argument_list|(
name|name
argument_list|)
expr_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|name
argument_list|)
condition|)
block|{
return|return
literal|null
return|;
block|}
comment|// we need to avoid the NPE issue of loading the class
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
init|=
name|ObjectHelper
operator|.
name|loadSimpleType
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|clazz
operator|==
literal|null
condition|)
block|{
name|clazz
operator|=
name|doLoadClass
argument_list|(
name|name
argument_list|,
name|bundleContext
operator|.
name|getBundle
argument_list|()
argument_list|)
expr_stmt|;
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
literal|"Loading class {} using BundleContext {} -> {}"
argument_list|,
operator|new
name|Object
index|[]
block|{
name|name
block|,
name|bundleContext
operator|.
name|getBundle
argument_list|()
block|,
name|clazz
block|}
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|clazz
operator|==
literal|null
operator|&&
name|camelContext
operator|!=
literal|null
condition|)
block|{
comment|// fallback and load class using the application context classloader
name|clazz
operator|=
name|super
operator|.
name|loadClass
argument_list|(
name|name
argument_list|,
name|camelContext
operator|.
name|getApplicationContextClassLoader
argument_list|()
argument_list|)
expr_stmt|;
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
literal|"Loading class {} using CamelContext {} -> {}"
argument_list|,
operator|new
name|Object
index|[]
block|{
name|name
block|,
name|camelContext
block|,
name|clazz
block|}
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|clazz
return|;
block|}
annotation|@
name|Override
DECL|method|resolveClass (String name, Class<T> type)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|Class
argument_list|<
name|T
argument_list|>
name|resolveClass
parameter_list|(
name|String
name|name
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
return|return
name|CastUtils
operator|.
name|cast
argument_list|(
name|resolveClass
argument_list|(
name|name
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|loadResourceAsStream (String uri)
specifier|public
name|InputStream
name|loadResourceAsStream
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
name|ObjectHelper
operator|.
name|notEmpty
argument_list|(
name|uri
argument_list|,
literal|"uri"
argument_list|)
expr_stmt|;
name|String
name|resolvedName
init|=
name|resolveUriPath
argument_list|(
name|uri
argument_list|)
decl_stmt|;
name|URL
name|url
init|=
name|loadResourceAsURL
argument_list|(
name|resolvedName
argument_list|)
decl_stmt|;
name|InputStream
name|answer
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|url
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|answer
operator|=
name|url
operator|.
name|openStream
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Cannot load resource: "
operator|+
name|uri
argument_list|,
name|ex
argument_list|)
throw|;
block|}
block|}
comment|// fallback to default as OSGi may have issues loading resources
if|if
condition|(
name|answer
operator|==
literal|null
condition|)
block|{
name|answer
operator|=
name|super
operator|.
name|loadResourceAsStream
argument_list|(
name|uri
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
annotation|@
name|Override
DECL|method|loadResourceAsURL (String uri)
specifier|public
name|URL
name|loadResourceAsURL
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
name|ObjectHelper
operator|.
name|notEmpty
argument_list|(
name|uri
argument_list|,
literal|"uri"
argument_list|)
expr_stmt|;
name|String
name|resolvedName
init|=
name|resolveUriPath
argument_list|(
name|uri
argument_list|)
decl_stmt|;
name|URL
name|answer
init|=
name|bundleContext
operator|.
name|getBundle
argument_list|()
operator|.
name|getResource
argument_list|(
name|resolvedName
argument_list|)
decl_stmt|;
comment|// fallback to default as OSGi may have issues loading resources
if|if
condition|(
name|answer
operator|==
literal|null
condition|)
block|{
name|answer
operator|=
name|super
operator|.
name|loadResourceAsURL
argument_list|(
name|uri
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
annotation|@
name|Override
DECL|method|loadResourcesAsURL (String uri)
specifier|public
name|Enumeration
argument_list|<
name|URL
argument_list|>
name|loadResourcesAsURL
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
name|ObjectHelper
operator|.
name|notEmpty
argument_list|(
name|uri
argument_list|,
literal|"uri"
argument_list|)
expr_stmt|;
try|try
block|{
name|String
name|resolvedName
init|=
name|resolveUriPath
argument_list|(
name|uri
argument_list|)
decl_stmt|;
return|return
name|bundleContext
operator|.
name|getBundle
argument_list|()
operator|.
name|getResources
argument_list|(
name|resolvedName
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Cannot load resource: "
operator|+
name|uri
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|loadAllResourcesAsURL (String uri)
specifier|public
name|Enumeration
argument_list|<
name|URL
argument_list|>
name|loadAllResourcesAsURL
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
name|ObjectHelper
operator|.
name|notEmpty
argument_list|(
name|uri
argument_list|,
literal|"uri"
argument_list|)
expr_stmt|;
name|Vector
argument_list|<
name|URL
argument_list|>
name|answer
init|=
operator|new
name|Vector
argument_list|<
name|URL
argument_list|>
argument_list|()
decl_stmt|;
try|try
block|{
name|String
name|resolvedName
init|=
name|resolveUriPath
argument_list|(
name|uri
argument_list|)
decl_stmt|;
name|Enumeration
argument_list|<
name|URL
argument_list|>
name|e
init|=
name|bundleContext
operator|.
name|getBundle
argument_list|()
operator|.
name|getResources
argument_list|(
name|resolvedName
argument_list|)
decl_stmt|;
while|while
condition|(
name|e
operator|!=
literal|null
operator|&&
name|e
operator|.
name|hasMoreElements
argument_list|()
condition|)
block|{
name|answer
operator|.
name|add
argument_list|(
name|e
operator|.
name|nextElement
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|String
name|path
init|=
name|FileUtil
operator|.
name|onlyPath
argument_list|(
name|uri
argument_list|)
decl_stmt|;
name|String
name|name
init|=
name|FileUtil
operator|.
name|stripPath
argument_list|(
name|uri
argument_list|)
decl_stmt|;
if|if
condition|(
name|path
operator|!=
literal|null
operator|&&
name|name
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Bundle
name|bundle
range|:
name|bundleContext
operator|.
name|getBundles
argument_list|()
control|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Finding all entries in path: {} with pattern: {}"
argument_list|,
name|path
argument_list|,
name|name
argument_list|)
expr_stmt|;
name|e
operator|=
name|bundle
operator|.
name|findEntries
argument_list|(
name|path
argument_list|,
name|name
argument_list|,
literal|false
argument_list|)
expr_stmt|;
while|while
condition|(
name|e
operator|!=
literal|null
operator|&&
name|e
operator|.
name|hasMoreElements
argument_list|()
condition|)
block|{
name|answer
operator|.
name|add
argument_list|(
name|e
operator|.
name|nextElement
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Cannot load resource: "
operator|+
name|uri
argument_list|,
name|e
argument_list|)
throw|;
block|}
return|return
name|answer
operator|.
name|elements
argument_list|()
return|;
block|}
DECL|method|doLoadClass (String name, Bundle loader)
specifier|protected
name|Class
argument_list|<
name|?
argument_list|>
name|doLoadClass
parameter_list|(
name|String
name|name
parameter_list|,
name|Bundle
name|loader
parameter_list|)
block|{
name|ObjectHelper
operator|.
name|notEmpty
argument_list|(
name|name
argument_list|,
literal|"name"
argument_list|)
expr_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|answer
init|=
literal|null
decl_stmt|;
comment|// Try to use the camel context's bundle's classloader to load the class
if|if
condition|(
name|loader
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|answer
operator|=
name|loader
operator|.
name|loadClass
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ClassNotFoundException
name|e
parameter_list|)
block|{
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
literal|"Cannot load class: "
operator|+
name|name
operator|+
literal|" using classloader: "
operator|+
name|loader
operator|+
literal|". This exception will be ignored."
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|answer
return|;
block|}
comment|/**      * Helper operation used to remove relative path notation from      * resources.  Most critical for resources on the Classpath      * as resource loaders will not resolve the relative paths correctly.      *      * @param name the name of the resource to load      * @return the modified or unmodified string if there were no changes      */
DECL|method|resolveUriPath (String name)
specifier|private
specifier|static
name|String
name|resolveUriPath
parameter_list|(
name|String
name|name
parameter_list|)
block|{
comment|// compact the path and use / as separator as that's used for loading resources on the classpath
return|return
name|FileUtil
operator|.
name|compactPath
argument_list|(
name|name
argument_list|,
literal|'/'
argument_list|)
return|;
block|}
block|}
end_class

end_unit

