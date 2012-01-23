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
name|BufferedInputStream
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
name|Properties
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
name|NoFactoryAvailableException
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
name|DefaultFactoryFinder
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
name|ClassResolver
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
name|IOHelper
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

begin_class
DECL|class|OsgiFactoryFinder
specifier|public
class|class
name|OsgiFactoryFinder
extends|extends
name|DefaultFactoryFinder
block|{
DECL|field|bundleContext
specifier|private
name|BundleContext
name|bundleContext
decl_stmt|;
DECL|method|OsgiFactoryFinder (BundleContext bundleContext, ClassResolver classResolver, String resourcePath)
specifier|public
name|OsgiFactoryFinder
parameter_list|(
name|BundleContext
name|bundleContext
parameter_list|,
name|ClassResolver
name|classResolver
parameter_list|,
name|String
name|resourcePath
parameter_list|)
block|{
name|super
argument_list|(
name|classResolver
argument_list|,
name|resourcePath
argument_list|)
expr_stmt|;
name|this
operator|.
name|bundleContext
operator|=
name|bundleContext
expr_stmt|;
block|}
DECL|class|BundleEntry
specifier|private
specifier|static
class|class
name|BundleEntry
block|{
DECL|field|url
name|URL
name|url
decl_stmt|;
DECL|field|bundle
name|Bundle
name|bundle
decl_stmt|;
block|}
annotation|@
name|Override
DECL|method|findClass (String key, String propertyPrefix, Class<?> checkClass)
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
name|findClass
parameter_list|(
name|String
name|key
parameter_list|,
name|String
name|propertyPrefix
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|checkClass
parameter_list|)
throws|throws
name|ClassNotFoundException
throws|,
name|IOException
block|{
if|if
condition|(
name|propertyPrefix
operator|==
literal|null
condition|)
block|{
name|propertyPrefix
operator|=
literal|""
expr_stmt|;
block|}
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
init|=
name|classMap
operator|.
name|get
argument_list|(
name|propertyPrefix
operator|+
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|clazz
operator|==
literal|null
condition|)
block|{
name|BundleEntry
name|entry
init|=
name|getResource
argument_list|(
name|key
argument_list|,
name|checkClass
argument_list|)
decl_stmt|;
if|if
condition|(
name|entry
operator|!=
literal|null
condition|)
block|{
name|URL
name|url
init|=
name|entry
operator|.
name|url
decl_stmt|;
name|InputStream
name|in
init|=
name|url
operator|.
name|openStream
argument_list|()
decl_stmt|;
comment|// lets load the file
name|BufferedInputStream
name|reader
init|=
literal|null
decl_stmt|;
try|try
block|{
name|reader
operator|=
name|IOHelper
operator|.
name|buffered
argument_list|(
name|in
argument_list|)
expr_stmt|;
name|Properties
name|properties
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|properties
operator|.
name|load
argument_list|(
name|reader
argument_list|)
expr_stmt|;
name|String
name|className
init|=
name|properties
operator|.
name|getProperty
argument_list|(
name|propertyPrefix
operator|+
literal|"class"
argument_list|)
decl_stmt|;
if|if
condition|(
name|className
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Expected property is missing: "
operator|+
name|propertyPrefix
operator|+
literal|"class"
argument_list|)
throw|;
block|}
name|clazz
operator|=
name|entry
operator|.
name|bundle
operator|.
name|loadClass
argument_list|(
name|className
argument_list|)
expr_stmt|;
name|classMap
operator|.
name|put
argument_list|(
name|propertyPrefix
operator|+
name|key
argument_list|,
name|clazz
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|IOHelper
operator|.
name|close
argument_list|(
name|reader
argument_list|,
name|key
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|IOHelper
operator|.
name|close
argument_list|(
name|in
argument_list|,
name|key
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
throw|throw
operator|new
name|NoFactoryAvailableException
argument_list|(
name|propertyPrefix
operator|+
name|key
argument_list|)
throw|;
block|}
block|}
return|return
name|clazz
return|;
block|}
annotation|@
name|Override
DECL|method|findClass (String key, String propertyPrefix)
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
name|findClass
parameter_list|(
name|String
name|key
parameter_list|,
name|String
name|propertyPrefix
parameter_list|)
throws|throws
name|ClassNotFoundException
throws|,
name|IOException
block|{
return|return
name|findClass
argument_list|(
name|key
argument_list|,
name|propertyPrefix
argument_list|,
literal|null
argument_list|)
return|;
block|}
comment|// As the META-INF of the Factory could not be export,
comment|// we need to go through the bundles to look for it
comment|// NOTE, the first found factory will be return
DECL|method|getResource (String name)
specifier|public
name|BundleEntry
name|getResource
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|getResource
argument_list|(
name|name
argument_list|,
literal|null
argument_list|)
return|;
block|}
comment|// The clazz can make sure we get right version of class that we need
DECL|method|getResource (String name, Class<?> clazz)
specifier|public
name|BundleEntry
name|getResource
parameter_list|(
name|String
name|name
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
parameter_list|)
block|{
name|BundleEntry
name|entry
init|=
literal|null
decl_stmt|;
name|Bundle
index|[]
name|bundles
init|=
literal|null
decl_stmt|;
name|bundles
operator|=
name|bundleContext
operator|.
name|getBundles
argument_list|()
expr_stmt|;
name|URL
name|url
decl_stmt|;
for|for
control|(
name|Bundle
name|bundle
range|:
name|bundles
control|)
block|{
name|url
operator|=
name|bundle
operator|.
name|getEntry
argument_list|(
name|getResourcePath
argument_list|()
operator|+
name|name
argument_list|)
expr_stmt|;
if|if
condition|(
name|url
operator|!=
literal|null
operator|&&
name|checkCompat
argument_list|(
name|bundle
argument_list|,
name|clazz
argument_list|)
condition|)
block|{
name|entry
operator|=
operator|new
name|BundleEntry
argument_list|()
expr_stmt|;
name|entry
operator|.
name|url
operator|=
name|url
expr_stmt|;
name|entry
operator|.
name|bundle
operator|=
name|bundle
expr_stmt|;
break|break;
block|}
block|}
return|return
name|entry
return|;
block|}
DECL|method|checkCompat (Bundle bundle, Class<?> clazz)
specifier|private
name|boolean
name|checkCompat
parameter_list|(
name|Bundle
name|bundle
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
parameter_list|)
block|{
if|if
condition|(
name|clazz
operator|==
literal|null
condition|)
block|{
return|return
literal|true
return|;
block|}
comment|// Check bundle compatibility
try|try
block|{
if|if
condition|(
name|bundle
operator|.
name|loadClass
argument_list|(
name|clazz
operator|.
name|getName
argument_list|()
argument_list|)
operator|!=
name|clazz
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
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

