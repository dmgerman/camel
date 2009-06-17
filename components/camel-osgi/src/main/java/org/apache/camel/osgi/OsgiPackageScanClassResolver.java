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
name|lang
operator|.
name|reflect
operator|.
name|Method
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
name|HashSet
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
operator|.
name|DefaultPackageScanClassResolver
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
name|PackageScanFilter
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
name|springframework
operator|.
name|osgi
operator|.
name|util
operator|.
name|BundleDelegatingClassLoader
import|;
end_import

begin_class
DECL|class|OsgiPackageScanClassResolver
specifier|public
class|class
name|OsgiPackageScanClassResolver
extends|extends
name|DefaultPackageScanClassResolver
block|{
DECL|field|bundle
specifier|private
name|Bundle
name|bundle
decl_stmt|;
DECL|method|OsgiPackageScanClassResolver (BundleContext context)
specifier|public
name|OsgiPackageScanClassResolver
parameter_list|(
name|BundleContext
name|context
parameter_list|)
block|{
name|bundle
operator|=
name|context
operator|.
name|getBundle
argument_list|()
expr_stmt|;
block|}
DECL|method|getClassLoaders ()
specifier|public
name|Set
argument_list|<
name|ClassLoader
argument_list|>
name|getClassLoaders
parameter_list|()
block|{
name|Set
argument_list|<
name|ClassLoader
argument_list|>
name|classLoaders
init|=
name|super
operator|.
name|getClassLoaders
argument_list|()
decl_stmt|;
comment|// Using the Activator's bundle to make up a class loader
name|ClassLoader
name|osgiLoader
init|=
name|BundleDelegatingClassLoader
operator|.
name|createBundleClassLoaderFor
argument_list|(
name|bundle
argument_list|)
decl_stmt|;
name|classLoaders
operator|.
name|add
argument_list|(
name|osgiLoader
argument_list|)
expr_stmt|;
return|return
name|classLoaders
return|;
block|}
DECL|method|find (PackageScanFilter test, String packageName, Set<Class> classes)
specifier|public
name|void
name|find
parameter_list|(
name|PackageScanFilter
name|test
parameter_list|,
name|String
name|packageName
parameter_list|,
name|Set
argument_list|<
name|Class
argument_list|>
name|classes
parameter_list|)
block|{
name|packageName
operator|=
name|packageName
operator|.
name|replace
argument_list|(
literal|'.'
argument_list|,
literal|'/'
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|ClassLoader
argument_list|>
name|set
init|=
name|getClassLoaders
argument_list|()
decl_stmt|;
name|ClassLoader
name|osgiClassLoader
init|=
name|getOsgiClassLoader
argument_list|(
name|set
argument_list|)
decl_stmt|;
name|int
name|classesSize
init|=
name|classes
operator|.
name|size
argument_list|()
decl_stmt|;
if|if
condition|(
name|osgiClassLoader
operator|!=
literal|null
condition|)
block|{
comment|// if we have an osgi bundle loader use this one first
name|LOG
operator|.
name|debug
argument_list|(
literal|"Using only osgi bundle classloader"
argument_list|)
expr_stmt|;
name|findInOsgiClassLoader
argument_list|(
name|test
argument_list|,
name|packageName
argument_list|,
name|osgiClassLoader
argument_list|,
name|classes
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|classes
operator|.
name|size
argument_list|()
operator|==
name|classesSize
condition|)
block|{
comment|// Using the regular classloaders as a fallback
name|LOG
operator|.
name|debug
argument_list|(
literal|"Using only regular classloaders"
argument_list|)
expr_stmt|;
for|for
control|(
name|ClassLoader
name|classLoader
range|:
name|set
operator|.
name|toArray
argument_list|(
operator|new
name|ClassLoader
index|[
name|set
operator|.
name|size
argument_list|()
index|]
argument_list|)
control|)
block|{
if|if
condition|(
operator|!
name|isOsgiClassloader
argument_list|(
name|classLoader
argument_list|)
condition|)
block|{
name|find
argument_list|(
name|test
argument_list|,
name|packageName
argument_list|,
name|classLoader
argument_list|,
name|classes
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
DECL|method|findInOsgiClassLoader (PackageScanFilter test, String packageName, ClassLoader osgiClassLoader, Set<Class> classes)
specifier|private
name|void
name|findInOsgiClassLoader
parameter_list|(
name|PackageScanFilter
name|test
parameter_list|,
name|String
name|packageName
parameter_list|,
name|ClassLoader
name|osgiClassLoader
parameter_list|,
name|Set
argument_list|<
name|Class
argument_list|>
name|classes
parameter_list|)
block|{
try|try
block|{
name|Method
name|mth
init|=
name|osgiClassLoader
operator|.
name|getClass
argument_list|()
operator|.
name|getMethod
argument_list|(
literal|"getBundle"
argument_list|,
operator|new
name|Class
index|[]
block|{}
argument_list|)
decl_stmt|;
if|if
condition|(
name|mth
operator|!=
literal|null
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
literal|"Loading from osgi bundle using classloader: "
operator|+
name|osgiClassLoader
argument_list|)
expr_stmt|;
block|}
name|loadImplementationsInBundle
argument_list|(
name|test
argument_list|,
name|packageName
argument_list|,
name|osgiClassLoader
argument_list|,
name|mth
argument_list|,
name|classes
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|NoSuchMethodException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"It's not an osgi bundle classloader: "
operator|+
name|osgiClassLoader
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Gets the osgi classloader if any in the given set      */
DECL|method|getOsgiClassLoader (Set<ClassLoader> set)
specifier|private
specifier|static
name|ClassLoader
name|getOsgiClassLoader
parameter_list|(
name|Set
argument_list|<
name|ClassLoader
argument_list|>
name|set
parameter_list|)
block|{
for|for
control|(
name|ClassLoader
name|loader
range|:
name|set
control|)
block|{
if|if
condition|(
name|isOsgiClassloader
argument_list|(
name|loader
argument_list|)
condition|)
block|{
return|return
name|loader
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
comment|/**      * Is it an osgi classloader      */
DECL|method|isOsgiClassloader (ClassLoader loader)
specifier|private
specifier|static
name|boolean
name|isOsgiClassloader
parameter_list|(
name|ClassLoader
name|loader
parameter_list|)
block|{
try|try
block|{
name|Method
name|mth
init|=
name|loader
operator|.
name|getClass
argument_list|()
operator|.
name|getMethod
argument_list|(
literal|"getBundle"
argument_list|,
operator|new
name|Class
index|[]
block|{}
argument_list|)
decl_stmt|;
if|if
condition|(
name|mth
operator|!=
literal|null
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
catch|catch
parameter_list|(
name|NoSuchMethodException
name|e
parameter_list|)
block|{
comment|// ignore its not an osgi loader
block|}
return|return
literal|false
return|;
block|}
DECL|method|loadImplementationsInBundle (PackageScanFilter test, String packageName, ClassLoader loader, Method mth, Set<Class> classes)
specifier|private
name|void
name|loadImplementationsInBundle
parameter_list|(
name|PackageScanFilter
name|test
parameter_list|,
name|String
name|packageName
parameter_list|,
name|ClassLoader
name|loader
parameter_list|,
name|Method
name|mth
parameter_list|,
name|Set
argument_list|<
name|Class
argument_list|>
name|classes
parameter_list|)
block|{
comment|// Use an inner class to avoid a NoClassDefFoundError when used in a non-osgi env
name|Set
argument_list|<
name|String
argument_list|>
name|urls
init|=
name|OsgiUtil
operator|.
name|getImplementationsInBundle
argument_list|(
name|test
argument_list|,
name|packageName
argument_list|,
name|loader
argument_list|,
name|mth
argument_list|)
decl_stmt|;
if|if
condition|(
name|urls
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|String
name|url
range|:
name|urls
control|)
block|{
comment|// substring to avoid leading slashes
name|addIfMatching
argument_list|(
name|test
argument_list|,
name|url
argument_list|,
name|classes
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|class|OsgiUtil
specifier|private
specifier|static
specifier|final
class|class
name|OsgiUtil
block|{
DECL|method|OsgiUtil ()
specifier|private
name|OsgiUtil
parameter_list|()
block|{
comment|// Helper class
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|getImplementationsInBundle (PackageScanFilter test, String packageName, ClassLoader loader, Method mth)
specifier|static
name|Set
argument_list|<
name|String
argument_list|>
name|getImplementationsInBundle
parameter_list|(
name|PackageScanFilter
name|test
parameter_list|,
name|String
name|packageName
parameter_list|,
name|ClassLoader
name|loader
parameter_list|,
name|Method
name|mth
parameter_list|)
block|{
try|try
block|{
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|Bundle
name|bundle
init|=
operator|(
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|Bundle
operator|)
name|mth
operator|.
name|invoke
argument_list|(
name|loader
argument_list|)
decl_stmt|;
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|Bundle
index|[]
name|bundles
init|=
name|bundle
operator|.
name|getBundleContext
argument_list|()
operator|.
name|getBundles
argument_list|()
decl_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|urls
init|=
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|Bundle
name|bd
range|:
name|bundles
control|)
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
literal|"Searching in bundle:"
operator|+
name|bd
argument_list|)
expr_stmt|;
block|}
name|Enumeration
argument_list|<
name|URL
argument_list|>
name|paths
init|=
name|bd
operator|.
name|findEntries
argument_list|(
literal|"/"
operator|+
name|packageName
argument_list|,
literal|"*.class"
argument_list|,
literal|true
argument_list|)
decl_stmt|;
while|while
condition|(
name|paths
operator|!=
literal|null
operator|&&
name|paths
operator|.
name|hasMoreElements
argument_list|()
condition|)
block|{
name|URL
name|path
init|=
name|paths
operator|.
name|nextElement
argument_list|()
decl_stmt|;
name|String
name|pathString
init|=
name|path
operator|.
name|getPath
argument_list|()
decl_stmt|;
name|String
name|urlString
init|=
name|pathString
operator|.
name|substring
argument_list|(
name|pathString
operator|.
name|indexOf
argument_list|(
name|packageName
argument_list|)
argument_list|)
decl_stmt|;
name|urls
operator|.
name|add
argument_list|(
name|urlString
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
literal|"Added url: "
operator|+
name|urlString
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|urls
return|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
name|LOG
operator|.
name|error
argument_list|(
literal|"Could not search osgi bundles for classes matching criteria: "
operator|+
name|test
operator|+
literal|"due to an Exception: "
operator|+
name|t
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
block|}
block|}
end_class

end_unit

