begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileInputStream
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
name|lang
operator|.
name|annotation
operator|.
name|Annotation
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URISyntaxException
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
name|net
operator|.
name|URLConnection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URLDecoder
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
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
name|LinkedHashSet
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
name|java
operator|.
name|util
operator|.
name|jar
operator|.
name|JarEntry
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|jar
operator|.
name|JarInputStream
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
name|scan
operator|.
name|AnnotatedWithAnyPackageScanFilter
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
name|scan
operator|.
name|AnnotatedWithPackageScanFilter
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
name|scan
operator|.
name|AssignableToPackageScanFilter
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
name|scan
operator|.
name|CompositePackageScanFilter
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
name|PackageScanClassResolver
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
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
import|;
end_import

begin_comment
comment|/**  * Default implement of {@link org.apache.camel.spi.PackageScanClassResolver}  */
end_comment

begin_class
DECL|class|DefaultPackageScanClassResolver
specifier|public
class|class
name|DefaultPackageScanClassResolver
implements|implements
name|PackageScanClassResolver
block|{
DECL|field|log
specifier|protected
specifier|final
specifier|transient
name|Log
name|log
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
DECL|field|classLoaders
specifier|private
name|Set
argument_list|<
name|ClassLoader
argument_list|>
name|classLoaders
decl_stmt|;
DECL|field|scanFilters
specifier|private
name|Set
argument_list|<
name|PackageScanFilter
argument_list|>
name|scanFilters
decl_stmt|;
DECL|method|addClassLoader (ClassLoader classLoader)
specifier|public
name|void
name|addClassLoader
parameter_list|(
name|ClassLoader
name|classLoader
parameter_list|)
block|{
try|try
block|{
name|getClassLoaders
argument_list|()
operator|.
name|add
argument_list|(
name|classLoader
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|ex
parameter_list|)
block|{
comment|// Ignore this exception as the PackageScanClassResolver
comment|// don't want use any other classloader
block|}
block|}
DECL|method|addFilter (PackageScanFilter filter)
specifier|public
name|void
name|addFilter
parameter_list|(
name|PackageScanFilter
name|filter
parameter_list|)
block|{
if|if
condition|(
name|scanFilters
operator|==
literal|null
condition|)
block|{
name|scanFilters
operator|=
operator|new
name|LinkedHashSet
argument_list|<
name|PackageScanFilter
argument_list|>
argument_list|()
expr_stmt|;
block|}
name|scanFilters
operator|.
name|add
argument_list|(
name|filter
argument_list|)
expr_stmt|;
block|}
DECL|method|removeFilter (PackageScanFilter filter)
specifier|public
name|void
name|removeFilter
parameter_list|(
name|PackageScanFilter
name|filter
parameter_list|)
block|{
if|if
condition|(
name|scanFilters
operator|!=
literal|null
condition|)
block|{
name|scanFilters
operator|.
name|remove
argument_list|(
name|filter
argument_list|)
expr_stmt|;
block|}
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
if|if
condition|(
name|classLoaders
operator|==
literal|null
condition|)
block|{
name|classLoaders
operator|=
operator|new
name|HashSet
argument_list|<
name|ClassLoader
argument_list|>
argument_list|()
expr_stmt|;
name|ClassLoader
name|ccl
init|=
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
decl_stmt|;
if|if
condition|(
name|ccl
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|log
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"The thread context class loader: "
operator|+
name|ccl
operator|+
literal|"  is used to load the class"
argument_list|)
expr_stmt|;
block|}
name|classLoaders
operator|.
name|add
argument_list|(
name|ccl
argument_list|)
expr_stmt|;
block|}
name|classLoaders
operator|.
name|add
argument_list|(
name|DefaultPackageScanClassResolver
operator|.
name|class
operator|.
name|getClassLoader
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|classLoaders
return|;
block|}
DECL|method|setClassLoaders (Set<ClassLoader> classLoaders)
specifier|public
name|void
name|setClassLoaders
parameter_list|(
name|Set
argument_list|<
name|ClassLoader
argument_list|>
name|classLoaders
parameter_list|)
block|{
name|this
operator|.
name|classLoaders
operator|=
name|classLoaders
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|findAnnotated (Class<? extends Annotation> annotation, String... packageNames)
specifier|public
name|Set
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|findAnnotated
parameter_list|(
name|Class
argument_list|<
name|?
extends|extends
name|Annotation
argument_list|>
name|annotation
parameter_list|,
name|String
modifier|...
name|packageNames
parameter_list|)
block|{
if|if
condition|(
name|packageNames
operator|==
literal|null
condition|)
block|{
return|return
name|Collections
operator|.
name|EMPTY_SET
return|;
block|}
if|if
condition|(
name|log
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Searching for annotations of "
operator|+
name|annotation
operator|.
name|getName
argument_list|()
operator|+
literal|" in packages: "
operator|+
name|Arrays
operator|.
name|asList
argument_list|(
name|packageNames
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|PackageScanFilter
name|test
init|=
name|getCompositeFilter
argument_list|(
operator|new
name|AnnotatedWithPackageScanFilter
argument_list|(
name|annotation
argument_list|,
literal|true
argument_list|)
argument_list|)
decl_stmt|;
name|Set
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|classes
init|=
operator|new
name|LinkedHashSet
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|pkg
range|:
name|packageNames
control|)
block|{
name|find
argument_list|(
name|test
argument_list|,
name|pkg
argument_list|,
name|classes
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|log
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Found: "
operator|+
name|classes
argument_list|)
expr_stmt|;
block|}
return|return
name|classes
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|findAnnotated (Set<Class<? extends Annotation>> annotations, String... packageNames)
specifier|public
name|Set
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|findAnnotated
parameter_list|(
name|Set
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|Annotation
argument_list|>
argument_list|>
name|annotations
parameter_list|,
name|String
modifier|...
name|packageNames
parameter_list|)
block|{
if|if
condition|(
name|packageNames
operator|==
literal|null
condition|)
block|{
return|return
name|Collections
operator|.
name|EMPTY_SET
return|;
block|}
if|if
condition|(
name|log
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Searching for annotations of "
operator|+
name|annotations
operator|+
literal|" in packages: "
operator|+
name|Arrays
operator|.
name|asList
argument_list|(
name|packageNames
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|PackageScanFilter
name|test
init|=
name|getCompositeFilter
argument_list|(
operator|new
name|AnnotatedWithAnyPackageScanFilter
argument_list|(
name|annotations
argument_list|,
literal|true
argument_list|)
argument_list|)
decl_stmt|;
name|Set
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|classes
init|=
operator|new
name|LinkedHashSet
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|pkg
range|:
name|packageNames
control|)
block|{
name|find
argument_list|(
name|test
argument_list|,
name|pkg
argument_list|,
name|classes
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|log
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Found: "
operator|+
name|classes
argument_list|)
expr_stmt|;
block|}
return|return
name|classes
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|findImplementations (Class parent, String... packageNames)
specifier|public
name|Set
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|findImplementations
parameter_list|(
name|Class
name|parent
parameter_list|,
name|String
modifier|...
name|packageNames
parameter_list|)
block|{
if|if
condition|(
name|packageNames
operator|==
literal|null
condition|)
block|{
return|return
name|Collections
operator|.
name|EMPTY_SET
return|;
block|}
if|if
condition|(
name|log
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Searching for implementations of "
operator|+
name|parent
operator|.
name|getName
argument_list|()
operator|+
literal|" in packages: "
operator|+
name|Arrays
operator|.
name|asList
argument_list|(
name|packageNames
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|PackageScanFilter
name|test
init|=
name|getCompositeFilter
argument_list|(
operator|new
name|AssignableToPackageScanFilter
argument_list|(
name|parent
argument_list|)
argument_list|)
decl_stmt|;
name|Set
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|classes
init|=
operator|new
name|LinkedHashSet
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|pkg
range|:
name|packageNames
control|)
block|{
name|find
argument_list|(
name|test
argument_list|,
name|pkg
argument_list|,
name|classes
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|log
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Found: "
operator|+
name|classes
argument_list|)
expr_stmt|;
block|}
return|return
name|classes
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|findByFilter (PackageScanFilter filter, String... packageNames)
specifier|public
name|Set
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|findByFilter
parameter_list|(
name|PackageScanFilter
name|filter
parameter_list|,
name|String
modifier|...
name|packageNames
parameter_list|)
block|{
if|if
condition|(
name|packageNames
operator|==
literal|null
condition|)
block|{
return|return
name|Collections
operator|.
name|EMPTY_SET
return|;
block|}
name|Set
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|classes
init|=
operator|new
name|LinkedHashSet
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|pkg
range|:
name|packageNames
control|)
block|{
name|find
argument_list|(
name|filter
argument_list|,
name|pkg
argument_list|,
name|classes
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|log
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Found: "
operator|+
name|classes
argument_list|)
expr_stmt|;
block|}
return|return
name|classes
return|;
block|}
DECL|method|find (PackageScanFilter test, String packageName, Set<Class<?>> classes)
specifier|protected
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
argument_list|<
name|?
argument_list|>
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
for|for
control|(
name|ClassLoader
name|classLoader
range|:
name|set
control|)
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
DECL|method|find (PackageScanFilter test, String packageName, ClassLoader loader, Set<Class<?>> classes)
specifier|protected
name|void
name|find
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
name|Set
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|classes
parameter_list|)
block|{
if|if
condition|(
name|log
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Searching for: "
operator|+
name|test
operator|+
literal|" in package: "
operator|+
name|packageName
operator|+
literal|" using classloader: "
operator|+
name|loader
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|Enumeration
argument_list|<
name|URL
argument_list|>
name|urls
decl_stmt|;
try|try
block|{
name|urls
operator|=
name|getResources
argument_list|(
name|loader
argument_list|,
name|packageName
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|urls
operator|.
name|hasMoreElements
argument_list|()
condition|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"No URLs returned by classloader"
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|ioe
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Cannot read package: "
operator|+
name|packageName
argument_list|,
name|ioe
argument_list|)
expr_stmt|;
return|return;
block|}
while|while
condition|(
name|urls
operator|.
name|hasMoreElements
argument_list|()
condition|)
block|{
name|URL
name|url
init|=
literal|null
decl_stmt|;
try|try
block|{
name|url
operator|=
name|urls
operator|.
name|nextElement
argument_list|()
expr_stmt|;
if|if
condition|(
name|log
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"URL from classloader: "
operator|+
name|url
argument_list|)
expr_stmt|;
block|}
name|url
operator|=
name|customResourceLocator
argument_list|(
name|url
argument_list|)
expr_stmt|;
name|String
name|urlPath
init|=
name|url
operator|.
name|getFile
argument_list|()
decl_stmt|;
name|urlPath
operator|=
name|URLDecoder
operator|.
name|decode
argument_list|(
name|urlPath
argument_list|,
literal|"UTF-8"
argument_list|)
expr_stmt|;
if|if
condition|(
name|log
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Decoded urlPath: "
operator|+
name|urlPath
operator|+
literal|" with protocol: "
operator|+
name|url
operator|.
name|getProtocol
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// If it's a file in a directory, trim the stupid file: spec
if|if
condition|(
name|urlPath
operator|.
name|startsWith
argument_list|(
literal|"file:"
argument_list|)
condition|)
block|{
comment|// file path can be temporary folder which uses characters that the URLDecoder decodes wrong
comment|// for example + being decoded to something else (+ can be used in temp folders on Mac OS)
comment|// to remedy this then create new path without using the URLDecoder
try|try
block|{
name|urlPath
operator|=
operator|new
name|URI
argument_list|(
name|url
operator|.
name|getFile
argument_list|()
argument_list|)
operator|.
name|getPath
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|URISyntaxException
name|e
parameter_list|)
block|{
comment|// fallback to use as it was given from the URLDecoder
comment|// this allows us to work on Windows if users have spaces in paths
block|}
if|if
condition|(
name|urlPath
operator|.
name|startsWith
argument_list|(
literal|"file:"
argument_list|)
condition|)
block|{
name|urlPath
operator|=
name|urlPath
operator|.
name|substring
argument_list|(
literal|5
argument_list|)
expr_stmt|;
block|}
block|}
comment|// osgi bundles should be skipped
if|if
condition|(
name|url
operator|.
name|toString
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"bundle:"
argument_list|)
operator|||
name|urlPath
operator|.
name|startsWith
argument_list|(
literal|"bundle:"
argument_list|)
condition|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"It's a virtual osgi bundle, skipping"
argument_list|)
expr_stmt|;
continue|continue;
block|}
comment|// Else it's in a JAR, grab the path to the jar
if|if
condition|(
name|urlPath
operator|.
name|indexOf
argument_list|(
literal|'!'
argument_list|)
operator|>
literal|0
condition|)
block|{
name|urlPath
operator|=
name|urlPath
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|urlPath
operator|.
name|indexOf
argument_list|(
literal|'!'
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|log
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Scanning for classes in ["
operator|+
name|urlPath
operator|+
literal|"] matching criteria: "
operator|+
name|test
argument_list|)
expr_stmt|;
block|}
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|urlPath
argument_list|)
decl_stmt|;
if|if
condition|(
name|file
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
if|if
condition|(
name|log
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Loading from directory using file: "
operator|+
name|file
argument_list|)
expr_stmt|;
block|}
name|loadImplementationsInDirectory
argument_list|(
name|test
argument_list|,
name|packageName
argument_list|,
name|file
argument_list|,
name|classes
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|InputStream
name|stream
decl_stmt|;
if|if
condition|(
name|urlPath
operator|.
name|startsWith
argument_list|(
literal|"http:"
argument_list|)
operator|||
name|urlPath
operator|.
name|startsWith
argument_list|(
literal|"https:"
argument_list|)
operator|||
name|urlPath
operator|.
name|startsWith
argument_list|(
literal|"sonicfs:"
argument_list|)
condition|)
block|{
comment|// load resources using http/https
comment|// sonic ESB requires to be loaded using a regular URLConnection
if|if
condition|(
name|log
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Loading from jar using http/https: "
operator|+
name|urlPath
argument_list|)
expr_stmt|;
block|}
name|URL
name|urlStream
init|=
operator|new
name|URL
argument_list|(
name|urlPath
argument_list|)
decl_stmt|;
name|URLConnection
name|con
init|=
name|urlStream
operator|.
name|openConnection
argument_list|()
decl_stmt|;
comment|// disable cache mainly to avoid jar file locking on Windows
name|con
operator|.
name|setUseCaches
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|stream
operator|=
name|con
operator|.
name|getInputStream
argument_list|()
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|log
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Loading from jar using file: "
operator|+
name|file
argument_list|)
expr_stmt|;
block|}
name|stream
operator|=
operator|new
name|FileInputStream
argument_list|(
name|file
argument_list|)
expr_stmt|;
block|}
name|loadImplementationsInJar
argument_list|(
name|test
argument_list|,
name|packageName
argument_list|,
name|stream
argument_list|,
name|urlPath
argument_list|,
name|classes
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|// use debug logging to avoid being to noisy in logs
name|log
operator|.
name|debug
argument_list|(
literal|"Cannot read entries in url: "
operator|+
name|url
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|// We can override this method to support the custom ResourceLocator
DECL|method|customResourceLocator (URL url)
specifier|protected
name|URL
name|customResourceLocator
parameter_list|(
name|URL
name|url
parameter_list|)
throws|throws
name|IOException
block|{
comment|// Do nothing here
return|return
name|url
return|;
block|}
comment|/**      * Strategy to get the resources by the given classloader.      *<p/>      * Notice that in WebSphere platforms there is a {@link WebSpherePackageScanClassResolver}      * to take care of WebSphere's odditiy of resource loading.      *      * @param loader  the classloader      * @param packageName   the packagename for the package to load      * @return  URL's for the given package      * @throws IOException is thrown by the classloader      */
DECL|method|getResources (ClassLoader loader, String packageName)
specifier|protected
name|Enumeration
argument_list|<
name|URL
argument_list|>
name|getResources
parameter_list|(
name|ClassLoader
name|loader
parameter_list|,
name|String
name|packageName
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|log
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Getting resource URL for package: "
operator|+
name|packageName
operator|+
literal|" with classloader: "
operator|+
name|loader
argument_list|)
expr_stmt|;
block|}
comment|// If the URL is a jar, the URLClassloader.getResources() seems to require a trailing slash.  The
comment|// trailing slash is harmless for other URLs
if|if
condition|(
operator|!
name|packageName
operator|.
name|endsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|packageName
operator|=
name|packageName
operator|+
literal|"/"
expr_stmt|;
block|}
return|return
name|loader
operator|.
name|getResources
argument_list|(
name|packageName
argument_list|)
return|;
block|}
DECL|method|getCompositeFilter (PackageScanFilter filter)
specifier|private
name|PackageScanFilter
name|getCompositeFilter
parameter_list|(
name|PackageScanFilter
name|filter
parameter_list|)
block|{
if|if
condition|(
name|scanFilters
operator|!=
literal|null
condition|)
block|{
name|CompositePackageScanFilter
name|composite
init|=
operator|new
name|CompositePackageScanFilter
argument_list|(
name|scanFilters
argument_list|)
decl_stmt|;
name|composite
operator|.
name|addFilter
argument_list|(
name|filter
argument_list|)
expr_stmt|;
return|return
name|composite
return|;
block|}
return|return
name|filter
return|;
block|}
comment|/**      * Finds matches in a physical directory on a filesystem. Examines all files      * within a directory - if the File object is not a directory, and ends with      *<i>.class</i> the file is loaded and tested to see if it is acceptable      * according to the Test. Operates recursively to find classes within a      * folder structure matching the package structure.      *      * @param test     a Test used to filter the classes that are discovered      * @param parent   the package name up to this directory in the package      *                 hierarchy. E.g. if /classes is in the classpath and we wish to      *                 examine files in /classes/org/apache then the values of      *<i>parent</i> would be<i>org/apache</i>      * @param location a File object representing a directory      */
DECL|method|loadImplementationsInDirectory (PackageScanFilter test, String parent, File location, Set<Class<?>> classes)
specifier|private
name|void
name|loadImplementationsInDirectory
parameter_list|(
name|PackageScanFilter
name|test
parameter_list|,
name|String
name|parent
parameter_list|,
name|File
name|location
parameter_list|,
name|Set
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|classes
parameter_list|)
block|{
name|File
index|[]
name|files
init|=
name|location
operator|.
name|listFiles
argument_list|()
decl_stmt|;
name|StringBuilder
name|builder
init|=
literal|null
decl_stmt|;
for|for
control|(
name|File
name|file
range|:
name|files
control|)
block|{
name|builder
operator|=
operator|new
name|StringBuilder
argument_list|(
literal|100
argument_list|)
expr_stmt|;
name|String
name|name
init|=
name|file
operator|.
name|getName
argument_list|()
decl_stmt|;
if|if
condition|(
name|name
operator|!=
literal|null
condition|)
block|{
name|name
operator|=
name|name
operator|.
name|trim
argument_list|()
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
name|parent
argument_list|)
operator|.
name|append
argument_list|(
literal|"/"
argument_list|)
operator|.
name|append
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|String
name|packageOrClass
init|=
name|parent
operator|==
literal|null
condition|?
name|name
else|:
name|builder
operator|.
name|toString
argument_list|()
decl_stmt|;
if|if
condition|(
name|file
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
name|loadImplementationsInDirectory
argument_list|(
name|test
argument_list|,
name|packageOrClass
argument_list|,
name|file
argument_list|,
name|classes
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|name
operator|.
name|endsWith
argument_list|(
literal|".class"
argument_list|)
condition|)
block|{
name|addIfMatching
argument_list|(
name|test
argument_list|,
name|packageOrClass
argument_list|,
name|classes
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
comment|/**      * Finds matching classes within a jar files that contains a folder      * structure matching the package structure. If the File is not a JarFile or      * does not exist a warning will be logged, but no error will be raised.      *      * @param test    a Test used to filter the classes that are discovered      * @param parent  the parent package under which classes must be in order to      *                be considered      * @param stream  the inputstream of the jar file to be examined for classes      * @param urlPath the url of the jar file to be examined for classes      */
DECL|method|loadImplementationsInJar (PackageScanFilter test, String parent, InputStream stream, String urlPath, Set<Class<?>> classes)
specifier|private
name|void
name|loadImplementationsInJar
parameter_list|(
name|PackageScanFilter
name|test
parameter_list|,
name|String
name|parent
parameter_list|,
name|InputStream
name|stream
parameter_list|,
name|String
name|urlPath
parameter_list|,
name|Set
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|classes
parameter_list|)
block|{
name|JarInputStream
name|jarStream
init|=
literal|null
decl_stmt|;
try|try
block|{
name|jarStream
operator|=
operator|new
name|JarInputStream
argument_list|(
name|stream
argument_list|)
expr_stmt|;
name|JarEntry
name|entry
decl_stmt|;
while|while
condition|(
operator|(
name|entry
operator|=
name|jarStream
operator|.
name|getNextJarEntry
argument_list|()
operator|)
operator|!=
literal|null
condition|)
block|{
name|String
name|name
init|=
name|entry
operator|.
name|getName
argument_list|()
decl_stmt|;
if|if
condition|(
name|name
operator|!=
literal|null
condition|)
block|{
name|name
operator|=
name|name
operator|.
name|trim
argument_list|()
expr_stmt|;
if|if
condition|(
operator|!
name|entry
operator|.
name|isDirectory
argument_list|()
operator|&&
name|name
operator|.
name|startsWith
argument_list|(
name|parent
argument_list|)
operator|&&
name|name
operator|.
name|endsWith
argument_list|(
literal|".class"
argument_list|)
condition|)
block|{
name|addIfMatching
argument_list|(
name|test
argument_list|,
name|name
argument_list|,
name|classes
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|ioe
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Cannot search jar file '"
operator|+
name|urlPath
operator|+
literal|"' for classes matching criteria: "
operator|+
name|test
operator|+
literal|" due to an IOException: "
operator|+
name|ioe
operator|.
name|getMessage
argument_list|()
argument_list|,
name|ioe
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|IOHelper
operator|.
name|close
argument_list|(
name|jarStream
argument_list|,
name|urlPath
argument_list|,
name|log
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Add the class designated by the fully qualified class name provided to      * the set of resolved classes if and only if it is approved by the Test      * supplied.      *      * @param test the test used to determine if the class matches      * @param fqn  the fully qualified name of a class      */
DECL|method|addIfMatching (PackageScanFilter test, String fqn, Set<Class<?>> classes)
specifier|protected
name|void
name|addIfMatching
parameter_list|(
name|PackageScanFilter
name|test
parameter_list|,
name|String
name|fqn
parameter_list|,
name|Set
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|classes
parameter_list|)
block|{
try|try
block|{
name|String
name|externalName
init|=
name|fqn
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|fqn
operator|.
name|indexOf
argument_list|(
literal|'.'
argument_list|)
argument_list|)
operator|.
name|replace
argument_list|(
literal|'/'
argument_list|,
literal|'.'
argument_list|)
decl_stmt|;
name|Set
argument_list|<
name|ClassLoader
argument_list|>
name|set
init|=
name|getClassLoaders
argument_list|()
decl_stmt|;
name|boolean
name|found
init|=
literal|false
decl_stmt|;
for|for
control|(
name|ClassLoader
name|classLoader
range|:
name|set
control|)
block|{
if|if
condition|(
name|log
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Testing for class "
operator|+
name|externalName
operator|+
literal|" matches criteria ["
operator|+
name|test
operator|+
literal|"] using classloader:"
operator|+
name|classLoader
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|type
init|=
name|classLoader
operator|.
name|loadClass
argument_list|(
name|externalName
argument_list|)
decl_stmt|;
if|if
condition|(
name|log
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Loaded the class: "
operator|+
name|type
operator|+
literal|" in classloader: "
operator|+
name|classLoader
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|test
operator|.
name|matches
argument_list|(
name|type
argument_list|)
condition|)
block|{
if|if
condition|(
name|log
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Found class: "
operator|+
name|type
operator|+
literal|" which matches the filter in classloader: "
operator|+
name|classLoader
argument_list|)
expr_stmt|;
block|}
name|classes
operator|.
name|add
argument_list|(
name|type
argument_list|)
expr_stmt|;
block|}
name|found
operator|=
literal|true
expr_stmt|;
break|break;
block|}
catch|catch
parameter_list|(
name|ClassNotFoundException
name|e
parameter_list|)
block|{
if|if
condition|(
name|log
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Cannot find class '"
operator|+
name|fqn
operator|+
literal|"' in classloader: "
operator|+
name|classLoader
operator|+
literal|". Reason: "
operator|+
name|e
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|NoClassDefFoundError
name|e
parameter_list|)
block|{
if|if
condition|(
name|log
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Cannot find the class definition '"
operator|+
name|fqn
operator|+
literal|"' in classloader: "
operator|+
name|classLoader
operator|+
literal|". Reason: "
operator|+
name|e
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
operator|!
name|found
condition|)
block|{
if|if
condition|(
name|log
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
comment|// use debug to avoid being noisy in logs
name|log
operator|.
name|debug
argument_list|(
literal|"Cannot find class '"
operator|+
name|fqn
operator|+
literal|"' in any classloaders: "
operator|+
name|set
argument_list|)
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
if|if
condition|(
name|log
operator|.
name|isWarnEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Cannot examine class '"
operator|+
name|fqn
operator|+
literal|"' due to a "
operator|+
name|e
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|" with message: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

