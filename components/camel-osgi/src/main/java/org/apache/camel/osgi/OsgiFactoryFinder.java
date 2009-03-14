begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.osgi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
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
name|FactoryFinder
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

begin_class
DECL|class|OsgiFactoryFinder
specifier|public
class|class
name|OsgiFactoryFinder
extends|extends
name|DefaultFactoryFinder
block|{
DECL|method|OsgiFactoryFinder (ClassResolver classResolver, String resourcePath)
specifier|public
name|OsgiFactoryFinder
parameter_list|(
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
block|}
DECL|class|BundleEntry
specifier|private
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
DECL|method|findClass (String key, String propertyPrefix)
specifier|public
name|Class
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
operator|new
name|BufferedInputStream
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
name|ObjectHelper
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
name|ObjectHelper
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
DECL|method|getResource (String name)
specifier|public
name|BundleEntry
name|getResource
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|BundleEntry
name|entry
init|=
literal|null
decl_stmt|;
name|BundleContext
name|bundleContext
init|=
name|Activator
operator|.
name|getBundle
argument_list|()
operator|.
name|getBundleContext
argument_list|()
decl_stmt|;
name|URL
name|url
decl_stmt|;
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
block|}
end_class

end_unit

