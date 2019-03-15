begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl.converter
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
operator|.
name|converter
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
name|nio
operator|.
name|charset
operator|.
name|Charset
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
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
name|List
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
name|StringTokenizer
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Modifier
operator|.
name|isAbstract
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Modifier
operator|.
name|isPublic
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Modifier
operator|.
name|isStatic
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
name|Converter
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
name|TypeConverter
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
name|TypeConverterLoaderException
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
name|TypeConverterLoader
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
name|TypeConverterRegistry
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
name|IOHelper
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
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|StringHelper
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
comment|/**  * A class which will auto-discover {@link Converter} objects and methods to pre-load  * the {@link TypeConverterRegistry} of converters on startup.  *<p/>  * This implementation supports scanning for type converters in JAR files. The {@link #META_INF_SERVICES}  * contains a list of packages or FQN class names for {@link Converter} classes. The FQN class names  * is loaded first and directly by the class loader.  *<p/>  * The {@link PackageScanClassResolver} is being used to scan packages for {@link Converter} classes and  * this procedure is slower than loading the {@link Converter} classes directly by its FQN class name.  * Therefore its recommended to specify FQN class names in the {@link #META_INF_SERVICES} file.  * Likewise the procedure for scanning using {@link PackageScanClassResolver} may require custom implementations  * to work in various containers such as JBoss, OSGi, etc.  */
end_comment

begin_class
DECL|class|AnnotationTypeConverterLoader
specifier|public
class|class
name|AnnotationTypeConverterLoader
implements|implements
name|TypeConverterLoader
block|{
DECL|field|META_INF_SERVICES
specifier|public
specifier|static
specifier|final
name|String
name|META_INF_SERVICES
init|=
literal|"META-INF/services/org/apache/camel/TypeConverter"
decl_stmt|;
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
name|AnnotationTypeConverterLoader
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|UTF8
specifier|private
specifier|static
specifier|final
name|Charset
name|UTF8
init|=
name|Charset
operator|.
name|forName
argument_list|(
literal|"UTF-8"
argument_list|)
decl_stmt|;
DECL|field|resolver
specifier|protected
name|PackageScanClassResolver
name|resolver
decl_stmt|;
DECL|field|visitedClasses
specifier|protected
name|Set
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|visitedClasses
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|visitedURIs
specifier|protected
name|Set
argument_list|<
name|String
argument_list|>
name|visitedURIs
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
DECL|method|AnnotationTypeConverterLoader (PackageScanClassResolver resolver)
specifier|public
name|AnnotationTypeConverterLoader
parameter_list|(
name|PackageScanClassResolver
name|resolver
parameter_list|)
block|{
name|this
operator|.
name|resolver
operator|=
name|resolver
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|load (TypeConverterRegistry registry)
specifier|public
name|void
name|load
parameter_list|(
name|TypeConverterRegistry
name|registry
parameter_list|)
throws|throws
name|TypeConverterLoaderException
block|{
name|String
index|[]
name|packageNames
decl_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"Searching for {} services"
argument_list|,
name|META_INF_SERVICES
argument_list|)
expr_stmt|;
try|try
block|{
name|packageNames
operator|=
name|findPackageNames
argument_list|()
expr_stmt|;
if|if
condition|(
name|packageNames
operator|==
literal|null
operator|||
name|packageNames
operator|.
name|length
operator|==
literal|0
condition|)
block|{
throw|throw
operator|new
name|TypeConverterLoaderException
argument_list|(
literal|"Cannot find package names to be used for classpath scanning for annotated type converters."
argument_list|)
throw|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|TypeConverterLoaderException
argument_list|(
literal|"Cannot find package names to be used for classpath scanning for annotated type converters."
argument_list|,
name|e
argument_list|)
throw|;
block|}
comment|// if we only have camel-core on the classpath then we have already pre-loaded all its type converters
comment|// but we exposed the "org.apache.camel.core" package in camel-core. This ensures there is at least one
comment|// packageName to scan, which triggers the scanning process. That allows us to ensure that we look for
comment|// META-INF/services in all the JARs.
if|if
condition|(
name|packageNames
operator|.
name|length
operator|==
literal|1
operator|&&
literal|"org.apache.camel.core"
operator|.
name|equals
argument_list|(
name|packageNames
index|[
literal|0
index|]
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"No additional package names found in classpath for annotated type converters."
argument_list|)
expr_stmt|;
comment|// no additional package names found to load type converters so break out
return|return;
block|}
comment|// now filter out org.apache.camel.core as its not needed anymore (it was just a dummy)
name|packageNames
operator|=
name|filterUnwantedPackage
argument_list|(
literal|"org.apache.camel.core"
argument_list|,
name|packageNames
argument_list|)
expr_stmt|;
comment|// filter out package names which can be loaded as a class directly so we avoid package scanning which
comment|// is much slower and does not work 100% in all runtime containers
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
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
name|packageNames
operator|=
name|filterPackageNamesOnly
argument_list|(
name|resolver
argument_list|,
name|packageNames
argument_list|,
name|classes
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|classes
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Loaded {} @Converter classes"
argument_list|,
name|classes
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// if there is any packages to scan and load @Converter classes, then do it
if|if
condition|(
name|packageNames
operator|!=
literal|null
operator|&&
name|packageNames
operator|.
name|length
operator|>
literal|0
condition|)
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
literal|"Found converter packages to scan: {}"
argument_list|,
name|String
operator|.
name|join
argument_list|(
literal|", "
argument_list|,
name|packageNames
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|Set
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|scannedClasses
init|=
name|resolver
operator|.
name|findAnnotated
argument_list|(
name|Converter
operator|.
name|class
argument_list|,
name|packageNames
argument_list|)
decl_stmt|;
if|if
condition|(
name|scannedClasses
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|TypeConverterLoaderException
argument_list|(
literal|"Cannot find any type converter classes from the following packages: "
operator|+
name|Arrays
operator|.
name|asList
argument_list|(
name|packageNames
argument_list|)
argument_list|)
throw|;
block|}
name|LOG
operator|.
name|debug
argument_list|(
literal|"Found {} packages with {} @Converter classes to load"
argument_list|,
name|packageNames
operator|.
name|length
argument_list|,
name|scannedClasses
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|classes
operator|.
name|addAll
argument_list|(
name|scannedClasses
argument_list|)
expr_stmt|;
block|}
comment|// load all the found classes into the type converter registry
for|for
control|(
name|Class
argument_list|<
name|?
argument_list|>
name|type
range|:
name|classes
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
literal|"Loading converter class: {}"
argument_list|,
name|ObjectHelper
operator|.
name|name
argument_list|(
name|type
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|loadConverterMethods
argument_list|(
name|registry
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
comment|// now clear the maps so we do not hold references
name|visitedClasses
operator|.
name|clear
argument_list|()
expr_stmt|;
name|visitedURIs
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
comment|/**      * Filters the given list of packages and returns an array of<b>only</b> package names.      *<p/>      * This implementation will check the given list of packages, and if it contains a class name,      * that class will be loaded directly and added to the list of classes. This optimizes the      * type converter to avoid excessive file scanning for .class files.      *      * @param resolver the class resolver      * @param packageNames the package names      * @param classes to add loaded @Converter classes      * @return the filtered package names      */
DECL|method|filterPackageNamesOnly (PackageScanClassResolver resolver, String[] packageNames, Set<Class<?>> classes)
specifier|protected
name|String
index|[]
name|filterPackageNamesOnly
parameter_list|(
name|PackageScanClassResolver
name|resolver
parameter_list|,
name|String
index|[]
name|packageNames
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
name|packageNames
operator|==
literal|null
operator|||
name|packageNames
operator|.
name|length
operator|==
literal|0
condition|)
block|{
return|return
name|packageNames
return|;
block|}
comment|// optimize for CorePackageScanClassResolver
if|if
condition|(
name|resolver
operator|.
name|getClassLoaders
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|packageNames
return|;
block|}
comment|// the filtered packages to return
name|List
argument_list|<
name|String
argument_list|>
name|packages
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
comment|// try to load it as a class first
for|for
control|(
name|String
name|name
range|:
name|packageNames
control|)
block|{
comment|// must be a FQN class name by having an upper case letter
if|if
condition|(
name|StringHelper
operator|.
name|isClassName
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
init|=
literal|null
decl_stmt|;
for|for
control|(
name|ClassLoader
name|loader
range|:
name|resolver
operator|.
name|getClassLoaders
argument_list|()
control|)
block|{
try|try
block|{
name|clazz
operator|=
name|ObjectHelper
operator|.
name|loadClass
argument_list|(
name|name
argument_list|,
name|loader
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"Loaded {} as class {}"
argument_list|,
name|name
argument_list|,
name|clazz
argument_list|)
expr_stmt|;
name|classes
operator|.
name|add
argument_list|(
name|clazz
argument_list|)
expr_stmt|;
comment|// class founder, so no need to load it with another class loader
break|break;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
comment|// do nothing here
block|}
block|}
if|if
condition|(
name|clazz
operator|==
literal|null
condition|)
block|{
comment|// ignore as its not a class (will be package scan afterwards)
name|packages
operator|.
name|add
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
comment|// ignore as its not a class (will be package scan afterwards)
name|packages
operator|.
name|add
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
block|}
comment|// return the packages which is not FQN classes
return|return
name|packages
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
name|packages
operator|.
name|size
argument_list|()
index|]
argument_list|)
return|;
block|}
comment|/**      * Finds the names of the packages to search for on the classpath looking      * for text files on the classpath at the {@link #META_INF_SERVICES} location.      *      * @return a collection of packages to search for      * @throws IOException is thrown for IO related errors      */
DECL|method|findPackageNames ()
specifier|protected
name|String
index|[]
name|findPackageNames
parameter_list|()
throws|throws
name|IOException
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|packages
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
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
name|findPackages
argument_list|(
name|packages
argument_list|,
name|ccl
argument_list|)
expr_stmt|;
block|}
name|findPackages
argument_list|(
name|packages
argument_list|,
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|packages
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
name|packages
operator|.
name|size
argument_list|()
index|]
argument_list|)
return|;
block|}
DECL|method|findPackages (Set<String> packages, ClassLoader classLoader)
specifier|protected
name|void
name|findPackages
parameter_list|(
name|Set
argument_list|<
name|String
argument_list|>
name|packages
parameter_list|,
name|ClassLoader
name|classLoader
parameter_list|)
throws|throws
name|IOException
block|{
name|Enumeration
argument_list|<
name|URL
argument_list|>
name|resources
init|=
name|classLoader
operator|.
name|getResources
argument_list|(
name|META_INF_SERVICES
argument_list|)
decl_stmt|;
while|while
condition|(
name|resources
operator|.
name|hasMoreElements
argument_list|()
condition|)
block|{
name|URL
name|url
init|=
name|resources
operator|.
name|nextElement
argument_list|()
decl_stmt|;
name|String
name|path
init|=
name|url
operator|.
name|getPath
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|visitedURIs
operator|.
name|contains
argument_list|(
name|path
argument_list|)
condition|)
block|{
comment|// remember we have visited this uri so we wont read it twice
name|visitedURIs
operator|.
name|add
argument_list|(
name|path
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Loading file {} to retrieve list of packages, from url: {}"
argument_list|,
name|META_INF_SERVICES
argument_list|,
name|url
argument_list|)
expr_stmt|;
name|BufferedReader
name|reader
init|=
name|IOHelper
operator|.
name|buffered
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
name|url
operator|.
name|openStream
argument_list|()
argument_list|,
name|UTF8
argument_list|)
argument_list|)
decl_stmt|;
try|try
block|{
while|while
condition|(
literal|true
condition|)
block|{
name|String
name|line
init|=
name|reader
operator|.
name|readLine
argument_list|()
decl_stmt|;
if|if
condition|(
name|line
operator|==
literal|null
condition|)
block|{
break|break;
block|}
name|line
operator|=
name|line
operator|.
name|trim
argument_list|()
expr_stmt|;
if|if
condition|(
name|line
operator|.
name|startsWith
argument_list|(
literal|"#"
argument_list|)
operator|||
name|line
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
continue|continue;
block|}
name|tokenize
argument_list|(
name|packages
argument_list|,
name|line
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|IOHelper
operator|.
name|close
argument_list|(
name|reader
argument_list|,
literal|null
argument_list|,
name|LOG
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
comment|/**      * Tokenizes the line from the META-IN/services file using commas and      * ignoring whitespace between packages      */
DECL|method|tokenize (Set<String> packages, String line)
specifier|private
name|void
name|tokenize
parameter_list|(
name|Set
argument_list|<
name|String
argument_list|>
name|packages
parameter_list|,
name|String
name|line
parameter_list|)
block|{
name|StringTokenizer
name|iter
init|=
operator|new
name|StringTokenizer
argument_list|(
name|line
argument_list|,
literal|","
argument_list|)
decl_stmt|;
while|while
condition|(
name|iter
operator|.
name|hasMoreTokens
argument_list|()
condition|)
block|{
name|String
name|name
init|=
name|iter
operator|.
name|nextToken
argument_list|()
operator|.
name|trim
argument_list|()
decl_stmt|;
if|if
condition|(
name|name
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|packages
operator|.
name|add
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Loads all of the converter methods for the given type      */
DECL|method|loadConverterMethods (TypeConverterRegistry registry, Class<?> type)
specifier|protected
name|void
name|loadConverterMethods
parameter_list|(
name|TypeConverterRegistry
name|registry
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|)
block|{
if|if
condition|(
name|visitedClasses
operator|.
name|contains
argument_list|(
name|type
argument_list|)
condition|)
block|{
return|return;
block|}
name|visitedClasses
operator|.
name|add
argument_list|(
name|type
argument_list|)
expr_stmt|;
try|try
block|{
name|Method
index|[]
name|methods
init|=
name|type
operator|.
name|getDeclaredMethods
argument_list|()
decl_stmt|;
name|CachingInjector
argument_list|<
name|?
argument_list|>
name|injector
init|=
literal|null
decl_stmt|;
for|for
control|(
name|Method
name|method
range|:
name|methods
control|)
block|{
comment|// this may be prone to ClassLoader or packaging problems when the same class is defined
comment|// in two different jars (as is the case sometimes with specs).
if|if
condition|(
name|ObjectHelper
operator|.
name|hasAnnotation
argument_list|(
name|method
argument_list|,
name|Converter
operator|.
name|class
argument_list|,
literal|true
argument_list|)
condition|)
block|{
name|boolean
name|allowNull
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|method
operator|.
name|getAnnotation
argument_list|(
name|Converter
operator|.
name|class
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|allowNull
operator|=
name|method
operator|.
name|getAnnotation
argument_list|(
name|Converter
operator|.
name|class
argument_list|)
operator|.
name|allowNull
argument_list|()
expr_stmt|;
block|}
name|boolean
name|fallback
init|=
name|method
operator|.
name|getAnnotation
argument_list|(
name|Converter
operator|.
name|class
argument_list|)
operator|.
name|fallback
argument_list|()
decl_stmt|;
if|if
condition|(
name|fallback
condition|)
block|{
name|injector
operator|=
name|handleHasFallbackConverterAnnotation
argument_list|(
name|registry
argument_list|,
name|type
argument_list|,
name|injector
argument_list|,
name|method
argument_list|,
name|allowNull
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|injector
operator|=
name|handleHasConverterAnnotation
argument_list|(
name|registry
argument_list|,
name|type
argument_list|,
name|injector
argument_list|,
name|method
argument_list|,
name|allowNull
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|Class
argument_list|<
name|?
argument_list|>
name|superclass
init|=
name|type
operator|.
name|getSuperclass
argument_list|()
decl_stmt|;
if|if
condition|(
name|superclass
operator|!=
literal|null
operator|&&
operator|!
name|superclass
operator|.
name|equals
argument_list|(
name|Object
operator|.
name|class
argument_list|)
condition|)
block|{
name|loadConverterMethods
argument_list|(
name|registry
argument_list|,
name|superclass
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
name|boolean
name|ignore
init|=
literal|false
decl_stmt|;
comment|// does the class allow to ignore the type converter when having load errors
if|if
condition|(
name|ObjectHelper
operator|.
name|hasAnnotation
argument_list|(
name|type
argument_list|,
name|Converter
operator|.
name|class
argument_list|,
literal|true
argument_list|)
condition|)
block|{
if|if
condition|(
name|type
operator|.
name|getAnnotation
argument_list|(
name|Converter
operator|.
name|class
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|ignore
operator|=
name|type
operator|.
name|getAnnotation
argument_list|(
name|Converter
operator|.
name|class
argument_list|)
operator|.
name|ignoreOnLoadError
argument_list|()
expr_stmt|;
block|}
block|}
comment|// if we should ignore then only log at debug level
if|if
condition|(
name|ignore
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Ignoring converter type: "
operator|+
name|type
operator|.
name|getCanonicalName
argument_list|()
operator|+
literal|" as a dependent class could not be found: "
operator|+
name|e
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Ignoring converter type: "
operator|+
name|type
operator|.
name|getCanonicalName
argument_list|()
operator|+
literal|" as a dependent class could not be found: "
operator|+
name|e
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|handleHasConverterAnnotation (TypeConverterRegistry registry, Class<?> type, CachingInjector<?> injector, Method method, boolean allowNull)
specifier|private
name|CachingInjector
argument_list|<
name|?
argument_list|>
name|handleHasConverterAnnotation
parameter_list|(
name|TypeConverterRegistry
name|registry
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|,
name|CachingInjector
argument_list|<
name|?
argument_list|>
name|injector
parameter_list|,
name|Method
name|method
parameter_list|,
name|boolean
name|allowNull
parameter_list|)
block|{
if|if
condition|(
name|isValidConverterMethod
argument_list|(
name|method
argument_list|)
condition|)
block|{
name|int
name|modifiers
init|=
name|method
operator|.
name|getModifiers
argument_list|()
decl_stmt|;
if|if
condition|(
name|isAbstract
argument_list|(
name|modifiers
argument_list|)
operator|||
operator|!
name|isPublic
argument_list|(
name|modifiers
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Ignoring bad converter on type: "
operator|+
name|type
operator|.
name|getCanonicalName
argument_list|()
operator|+
literal|" method: "
operator|+
name|method
operator|+
literal|" as a converter method is not a public and concrete method"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|toType
init|=
name|method
operator|.
name|getReturnType
argument_list|()
decl_stmt|;
if|if
condition|(
name|toType
operator|.
name|equals
argument_list|(
name|Void
operator|.
name|class
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Ignoring bad converter on type: "
operator|+
name|type
operator|.
name|getCanonicalName
argument_list|()
operator|+
literal|" method: "
operator|+
name|method
operator|+
literal|" as a converter method returns a void method"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|fromType
init|=
name|method
operator|.
name|getParameterTypes
argument_list|()
index|[
literal|0
index|]
decl_stmt|;
if|if
condition|(
name|isStatic
argument_list|(
name|modifiers
argument_list|)
condition|)
block|{
name|registerTypeConverter
argument_list|(
name|registry
argument_list|,
name|method
argument_list|,
name|toType
argument_list|,
name|fromType
argument_list|,
operator|new
name|StaticMethodTypeConverter
argument_list|(
name|method
argument_list|,
name|allowNull
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|injector
operator|==
literal|null
condition|)
block|{
name|injector
operator|=
operator|new
name|CachingInjector
argument_list|<>
argument_list|(
name|registry
argument_list|,
name|CastUtils
operator|.
name|cast
argument_list|(
name|type
argument_list|,
name|Object
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|registerTypeConverter
argument_list|(
name|registry
argument_list|,
name|method
argument_list|,
name|toType
argument_list|,
name|fromType
argument_list|,
operator|new
name|InstanceMethodTypeConverter
argument_list|(
name|injector
argument_list|,
name|method
argument_list|,
name|registry
argument_list|,
name|allowNull
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
else|else
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Ignoring bad converter on type: "
operator|+
name|type
operator|.
name|getCanonicalName
argument_list|()
operator|+
literal|" method: "
operator|+
name|method
operator|+
literal|" as a converter method should have one parameter"
argument_list|)
expr_stmt|;
block|}
return|return
name|injector
return|;
block|}
DECL|method|handleHasFallbackConverterAnnotation (TypeConverterRegistry registry, Class<?> type, CachingInjector<?> injector, Method method, boolean allowNull)
specifier|private
name|CachingInjector
argument_list|<
name|?
argument_list|>
name|handleHasFallbackConverterAnnotation
parameter_list|(
name|TypeConverterRegistry
name|registry
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|,
name|CachingInjector
argument_list|<
name|?
argument_list|>
name|injector
parameter_list|,
name|Method
name|method
parameter_list|,
name|boolean
name|allowNull
parameter_list|)
block|{
if|if
condition|(
name|isValidFallbackConverterMethod
argument_list|(
name|method
argument_list|)
condition|)
block|{
name|int
name|modifiers
init|=
name|method
operator|.
name|getModifiers
argument_list|()
decl_stmt|;
if|if
condition|(
name|isAbstract
argument_list|(
name|modifiers
argument_list|)
operator|||
operator|!
name|isPublic
argument_list|(
name|modifiers
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Ignoring bad fallback converter on type: "
operator|+
name|type
operator|.
name|getCanonicalName
argument_list|()
operator|+
literal|" method: "
operator|+
name|method
operator|+
literal|" as a fallback converter method is not a public and concrete method"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|toType
init|=
name|method
operator|.
name|getReturnType
argument_list|()
decl_stmt|;
if|if
condition|(
name|toType
operator|.
name|equals
argument_list|(
name|Void
operator|.
name|class
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Ignoring bad fallback converter on type: "
operator|+
name|type
operator|.
name|getCanonicalName
argument_list|()
operator|+
literal|" method: "
operator|+
name|method
operator|+
literal|" as a fallback converter method returns a void method"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|isStatic
argument_list|(
name|modifiers
argument_list|)
condition|)
block|{
name|registerFallbackTypeConverter
argument_list|(
name|registry
argument_list|,
operator|new
name|StaticMethodFallbackTypeConverter
argument_list|(
name|method
argument_list|,
name|registry
argument_list|,
name|allowNull
argument_list|)
argument_list|,
name|method
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|injector
operator|==
literal|null
condition|)
block|{
name|injector
operator|=
operator|new
name|CachingInjector
argument_list|<>
argument_list|(
name|registry
argument_list|,
name|CastUtils
operator|.
name|cast
argument_list|(
name|type
argument_list|,
name|Object
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|registerFallbackTypeConverter
argument_list|(
name|registry
argument_list|,
operator|new
name|InstanceMethodFallbackTypeConverter
argument_list|(
name|injector
argument_list|,
name|method
argument_list|,
name|registry
argument_list|,
name|allowNull
argument_list|)
argument_list|,
name|method
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
else|else
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Ignoring bad fallback converter on type: "
operator|+
name|type
operator|.
name|getCanonicalName
argument_list|()
operator|+
literal|" method: "
operator|+
name|method
operator|+
literal|" as a fallback converter method should have one parameter"
argument_list|)
expr_stmt|;
block|}
return|return
name|injector
return|;
block|}
DECL|method|registerTypeConverter (TypeConverterRegistry registry, Method method, Class<?> toType, Class<?> fromType, TypeConverter typeConverter)
specifier|protected
name|void
name|registerTypeConverter
parameter_list|(
name|TypeConverterRegistry
name|registry
parameter_list|,
name|Method
name|method
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|toType
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|fromType
parameter_list|,
name|TypeConverter
name|typeConverter
parameter_list|)
block|{
name|registry
operator|.
name|addTypeConverter
argument_list|(
name|toType
argument_list|,
name|fromType
argument_list|,
name|typeConverter
argument_list|)
expr_stmt|;
block|}
DECL|method|isValidConverterMethod (Method method)
specifier|protected
name|boolean
name|isValidConverterMethod
parameter_list|(
name|Method
name|method
parameter_list|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
index|[]
name|parameterTypes
init|=
name|method
operator|.
name|getParameterTypes
argument_list|()
decl_stmt|;
return|return
operator|(
name|parameterTypes
operator|!=
literal|null
operator|)
operator|&&
operator|(
name|parameterTypes
operator|.
name|length
operator|==
literal|1
operator|||
operator|(
name|parameterTypes
operator|.
name|length
operator|==
literal|2
operator|&&
name|Exchange
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|parameterTypes
index|[
literal|1
index|]
argument_list|)
operator|)
operator|)
return|;
block|}
DECL|method|registerFallbackTypeConverter (TypeConverterRegistry registry, TypeConverter typeConverter, Method method)
specifier|protected
name|void
name|registerFallbackTypeConverter
parameter_list|(
name|TypeConverterRegistry
name|registry
parameter_list|,
name|TypeConverter
name|typeConverter
parameter_list|,
name|Method
name|method
parameter_list|)
block|{
name|boolean
name|canPromote
init|=
literal|false
decl_stmt|;
comment|// check whether the annotation may indicate it can promote
if|if
condition|(
name|method
operator|.
name|getAnnotation
argument_list|(
name|Converter
operator|.
name|class
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|canPromote
operator|=
name|method
operator|.
name|getAnnotation
argument_list|(
name|Converter
operator|.
name|class
argument_list|)
operator|.
name|fallbackCanPromote
argument_list|()
expr_stmt|;
block|}
name|registry
operator|.
name|addFallbackTypeConverter
argument_list|(
name|typeConverter
argument_list|,
name|canPromote
argument_list|)
expr_stmt|;
block|}
DECL|method|isValidFallbackConverterMethod (Method method)
specifier|protected
name|boolean
name|isValidFallbackConverterMethod
parameter_list|(
name|Method
name|method
parameter_list|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
index|[]
name|parameterTypes
init|=
name|method
operator|.
name|getParameterTypes
argument_list|()
decl_stmt|;
return|return
operator|(
name|parameterTypes
operator|!=
literal|null
operator|)
operator|&&
operator|(
name|parameterTypes
operator|.
name|length
operator|==
literal|3
operator|||
operator|(
name|parameterTypes
operator|.
name|length
operator|==
literal|4
operator|&&
name|Exchange
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|parameterTypes
index|[
literal|1
index|]
argument_list|)
operator|)
operator|&&
operator|(
name|TypeConverterRegistry
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|parameterTypes
index|[
name|parameterTypes
operator|.
name|length
operator|-
literal|1
index|]
argument_list|)
operator|)
operator|)
return|;
block|}
comment|/**      * Filters the given list of packages      *      * @param name  the name to filter out      * @param packageNames the packages      * @return he packages without the given name      */
DECL|method|filterUnwantedPackage (String name, String[] packageNames)
specifier|protected
specifier|static
name|String
index|[]
name|filterUnwantedPackage
parameter_list|(
name|String
name|name
parameter_list|,
name|String
index|[]
name|packageNames
parameter_list|)
block|{
comment|// the filtered packages to return
name|List
argument_list|<
name|String
argument_list|>
name|packages
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|s
range|:
name|packageNames
control|)
block|{
if|if
condition|(
operator|!
name|name
operator|.
name|equals
argument_list|(
name|s
argument_list|)
condition|)
block|{
name|packages
operator|.
name|add
argument_list|(
name|s
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|packages
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
name|packages
operator|.
name|size
argument_list|()
index|]
argument_list|)
return|;
block|}
block|}
end_class

end_unit

