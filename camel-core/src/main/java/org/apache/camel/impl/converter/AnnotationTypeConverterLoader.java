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
name|annotation
operator|.
name|Annotation
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
name|ResolverUtil
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
name|WebSphereResolverUtil
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
comment|/**  * A class which will auto-discover converter objects and methods to pre-load  * the registry of converters on startup  *  * @version $Revision$  */
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
specifier|transient
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|AnnotationTypeConverterLoader
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|resolver
specifier|private
name|ResolverUtil
name|resolver
init|=
operator|new
name|ResolverUtil
argument_list|()
decl_stmt|;
DECL|field|visitedClasses
specifier|private
name|Set
argument_list|<
name|Class
argument_list|>
name|visitedClasses
init|=
operator|new
name|HashSet
argument_list|<
name|Class
argument_list|>
argument_list|()
decl_stmt|;
DECL|method|AnnotationTypeConverterLoader ()
specifier|public
name|AnnotationTypeConverterLoader
parameter_list|()
block|{
comment|// use WebSphere specific resolver if running on WebSphere
if|if
condition|(
name|WebSphereResolverUtil
operator|.
name|isWebSphereClassLoader
argument_list|(
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Using WebSphere specific ResolverUtil"
argument_list|)
expr_stmt|;
name|resolver
operator|=
operator|new
name|WebSphereResolverUtil
argument_list|(
name|META_INF_SERVICES
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|load (TypeConverterRegistry registry)
specifier|public
name|void
name|load
parameter_list|(
name|TypeConverterRegistry
name|registry
parameter_list|)
throws|throws
name|Exception
block|{
name|String
index|[]
name|packageNames
init|=
name|findPackageNames
argument_list|()
decl_stmt|;
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
expr_stmt|;
name|Set
argument_list|<
name|Class
argument_list|>
name|classes
init|=
name|resolver
operator|.
name|getClasses
argument_list|()
decl_stmt|;
for|for
control|(
name|Class
name|type
range|:
name|classes
control|)
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
literal|"Loading converter class: "
operator|+
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
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|findPackages
argument_list|(
name|packages
argument_list|,
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
argument_list|)
expr_stmt|;
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
if|if
condition|(
name|url
operator|!=
literal|null
condition|)
block|{
name|BufferedReader
name|reader
init|=
operator|new
name|BufferedReader
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
name|url
operator|.
name|openStream
argument_list|()
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
name|ObjectHelper
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
specifier|protected
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
DECL|method|loadConverterMethods (TypeConverterRegistry registry, Class type)
specifier|protected
name|void
name|loadConverterMethods
parameter_list|(
name|TypeConverterRegistry
name|registry
parameter_list|,
name|Class
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
name|getName
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
name|getName
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
argument_list|(
name|registry
argument_list|,
name|type
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
name|getName
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
block|}
block|}
name|Class
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
name|LOG
operator|.
name|warn
argument_list|(
literal|"Ignoring converter type: "
operator|+
name|type
operator|.
name|getName
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
DECL|method|registerTypeConverter (TypeConverterRegistry registry, Method method, Class toType, Class fromType, TypeConverter typeConverter)
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
name|toType
parameter_list|,
name|Class
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
block|}
end_class

end_unit

