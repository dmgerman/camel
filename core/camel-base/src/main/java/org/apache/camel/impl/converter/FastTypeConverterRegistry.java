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
name|Enumeration
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
name|StringTokenizer
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
name|RuntimeCamelException
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
comment|/**  * An optimized {@link org.apache.camel.spi.TypeConverterRegistry} which loads  * the type converters up-front on startup in a faster way by leveraging  * source generated type converter loaders (<tt>@Converter(loader = true)</tt>,  * as well as invoking the type converters without reflection,  * and will not perform classpath scanning.  */
end_comment

begin_class
DECL|class|FastTypeConverterRegistry
specifier|public
class|class
name|FastTypeConverterRegistry
extends|extends
name|BaseTypeConverterRegistry
block|{
DECL|field|META_INF_SERVICES
specifier|public
specifier|static
specifier|final
name|String
name|META_INF_SERVICES
init|=
literal|"META-INF/services/org/apache/camel/TypeConverterLoader"
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
name|FastTypeConverterRegistry
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
DECL|method|FastTypeConverterRegistry ()
specifier|public
name|FastTypeConverterRegistry
parameter_list|()
block|{
name|super
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
comment|// pass in null to base class as we load all type converters without package scanning
block|}
annotation|@
name|Override
DECL|method|allowNull ()
specifier|public
name|boolean
name|allowNull
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|isRunAllowed ()
specifier|public
name|boolean
name|isRunAllowed
parameter_list|()
block|{
comment|// as type converter is used during initialization then allow it to always run
return|return
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|doInit ()
specifier|protected
name|void
name|doInit
parameter_list|()
block|{
try|try
block|{
comment|// core type converters is always loaded which does not use any classpath scanning and therefore is fast
name|loadCoreTypeConverters
argument_list|()
expr_stmt|;
name|int
name|core
init|=
name|typeMappings
operator|.
name|size
argument_list|()
decl_stmt|;
comment|// load type converters up front
name|log
operator|.
name|debug
argument_list|(
literal|"Initializing FastTypeConverterRegistry - requires converters to be annotated with @Converter(loader = true)"
argument_list|)
expr_stmt|;
name|loadTypeConverters
argument_list|()
expr_stmt|;
name|int
name|additional
init|=
name|typeMappings
operator|.
name|size
argument_list|()
operator|-
name|core
decl_stmt|;
comment|// report how many type converters we have loaded
name|log
operator|.
name|info
argument_list|(
literal|"Type converters loaded (core: {}, classpath: {})"
argument_list|,
name|core
argument_list|,
name|additional
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
name|RuntimeCamelException
operator|.
name|wrapRuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
comment|// we are using backwards compatible legacy mode to detect additional converters
if|if
condition|(
name|camelContext
operator|.
name|isLoadTypeConverters
argument_list|()
condition|)
block|{
try|try
block|{
comment|// we need an injector for annotation scanning
name|setInjector
argument_list|(
name|camelContext
operator|.
name|getInjector
argument_list|()
argument_list|)
expr_stmt|;
name|int
name|fast
init|=
name|typeMappings
operator|.
name|size
argument_list|()
decl_stmt|;
comment|// load type converters up front
name|TypeConverterLoader
name|loader
init|=
operator|new
name|FastAnnotationTypeConverterLoader
argument_list|(
name|camelContext
operator|.
name|getPackageScanClassResolver
argument_list|()
argument_list|)
decl_stmt|;
name|loader
operator|.
name|load
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|int
name|additional
init|=
name|typeMappings
operator|.
name|size
argument_list|()
operator|-
name|fast
decl_stmt|;
comment|// report how many type converters we have loaded
if|if
condition|(
name|additional
operator|>
literal|0
condition|)
block|{
name|log
operator|.
name|info
argument_list|(
literal|"Type converters loaded (fast: {}, scanned: {})"
argument_list|,
name|fast
argument_list|,
name|additional
argument_list|)
expr_stmt|;
name|log
operator|.
name|warn
argument_list|(
literal|"Annotation scanning mode loaded {} type converters. Its recommended to migrate to @Converter(loader = true) for fast type converter mode."
argument_list|,
name|additional
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
name|RuntimeCamelException
operator|.
name|wrapRuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
block|}
annotation|@
name|Override
DECL|method|loadTypeConverters ()
specifier|public
name|void
name|loadTypeConverters
parameter_list|()
throws|throws
name|Exception
block|{
name|String
index|[]
name|lines
init|=
name|findTypeConverterLoaderClasses
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|line
range|:
name|lines
control|)
block|{
name|String
name|name
init|=
name|StringHelper
operator|.
name|after
argument_list|(
name|line
argument_list|,
literal|"class="
argument_list|)
decl_stmt|;
if|if
condition|(
name|name
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Resolving TypeConverterLoader: {}"
argument_list|,
name|name
argument_list|)
expr_stmt|;
name|Class
name|clazz
init|=
name|getCamelContext
argument_list|()
operator|.
name|getClassResolver
argument_list|()
operator|.
name|resolveMandatoryClass
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|Object
name|obj
init|=
name|getCamelContext
argument_list|()
operator|.
name|getInjector
argument_list|()
operator|.
name|newInstance
argument_list|(
name|clazz
argument_list|)
decl_stmt|;
if|if
condition|(
name|obj
operator|instanceof
name|TypeConverterLoader
condition|)
block|{
name|TypeConverterLoader
name|loader
init|=
operator|(
name|TypeConverterLoader
operator|)
name|obj
decl_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"TypeConverterLoader: {} loading converters"
argument_list|,
name|name
argument_list|)
expr_stmt|;
name|loader
operator|.
name|load
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
comment|/**      * Finds the type converter loader classes from the classpath looking      * for text files on the classpath at the {@link #META_INF_SERVICES} location.      */
DECL|method|findTypeConverterLoaderClasses ()
specifier|protected
name|String
index|[]
name|findTypeConverterLoaderClasses
parameter_list|()
throws|throws
name|IOException
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|classes
init|=
operator|new
name|LinkedHashSet
argument_list|<>
argument_list|()
decl_stmt|;
name|findLoaders
argument_list|(
name|classes
argument_list|,
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|classes
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
name|classes
operator|.
name|size
argument_list|()
index|]
argument_list|)
return|;
block|}
DECL|method|findLoaders (Set<String> packages, ClassLoader classLoader)
specifier|protected
name|void
name|findLoaders
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
name|LOG
operator|.
name|debug
argument_list|(
literal|"Loading file {} to retrieve list of type converters, from url: {}"
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
block|}
end_class

end_unit

