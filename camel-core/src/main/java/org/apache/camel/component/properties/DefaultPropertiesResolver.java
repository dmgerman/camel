begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.properties
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|properties
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
name|FileInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileNotFoundException
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
name|io
operator|.
name|InputStreamReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Reader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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

begin_comment
comment|/**  * Default {@link org.apache.camel.component.properties.PropertiesResolver} which can resolve properties  * from file and classpath.  *<p/>  * You can denote<tt>classpath:</tt> or<tt>file:</tt> as prefix in the uri to select whether the file  * is located in the classpath or on the file system.  *  * @version   */
end_comment

begin_class
DECL|class|DefaultPropertiesResolver
specifier|public
class|class
name|DefaultPropertiesResolver
implements|implements
name|PropertiesResolver
block|{
DECL|field|propertiesComponent
specifier|private
specifier|final
name|PropertiesComponent
name|propertiesComponent
decl_stmt|;
DECL|method|DefaultPropertiesResolver (PropertiesComponent propertiesComponent)
specifier|public
name|DefaultPropertiesResolver
parameter_list|(
name|PropertiesComponent
name|propertiesComponent
parameter_list|)
block|{
name|this
operator|.
name|propertiesComponent
operator|=
name|propertiesComponent
expr_stmt|;
block|}
DECL|method|resolveProperties (CamelContext context, boolean ignoreMissingLocation, String... uri)
specifier|public
name|Properties
name|resolveProperties
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|boolean
name|ignoreMissingLocation
parameter_list|,
name|String
modifier|...
name|uri
parameter_list|)
throws|throws
name|Exception
block|{
name|Properties
name|answer
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|path
range|:
name|uri
control|)
block|{
if|if
condition|(
name|path
operator|.
name|startsWith
argument_list|(
literal|"ref:"
argument_list|)
condition|)
block|{
name|Properties
name|prop
init|=
name|loadPropertiesFromRegistry
argument_list|(
name|context
argument_list|,
name|ignoreMissingLocation
argument_list|,
name|path
argument_list|)
decl_stmt|;
name|prop
operator|=
name|prepareLoadedProperties
argument_list|(
name|prop
argument_list|)
expr_stmt|;
name|answer
operator|.
name|putAll
argument_list|(
name|prop
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|path
operator|.
name|startsWith
argument_list|(
literal|"file:"
argument_list|)
condition|)
block|{
name|Properties
name|prop
init|=
name|loadPropertiesFromFilePath
argument_list|(
name|context
argument_list|,
name|ignoreMissingLocation
argument_list|,
name|path
argument_list|)
decl_stmt|;
name|prop
operator|=
name|prepareLoadedProperties
argument_list|(
name|prop
argument_list|)
expr_stmt|;
name|answer
operator|.
name|putAll
argument_list|(
name|prop
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// default to classpath
name|Properties
name|prop
init|=
name|loadPropertiesFromClasspath
argument_list|(
name|context
argument_list|,
name|ignoreMissingLocation
argument_list|,
name|path
argument_list|)
decl_stmt|;
name|prop
operator|=
name|prepareLoadedProperties
argument_list|(
name|prop
argument_list|)
expr_stmt|;
name|answer
operator|.
name|putAll
argument_list|(
name|prop
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|answer
return|;
block|}
DECL|method|loadPropertiesFromFilePath (CamelContext context, boolean ignoreMissingLocation, String path)
specifier|protected
name|Properties
name|loadPropertiesFromFilePath
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|boolean
name|ignoreMissingLocation
parameter_list|,
name|String
name|path
parameter_list|)
throws|throws
name|IOException
block|{
name|Properties
name|answer
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
if|if
condition|(
name|path
operator|.
name|startsWith
argument_list|(
literal|"file:"
argument_list|)
condition|)
block|{
name|path
operator|=
name|ObjectHelper
operator|.
name|after
argument_list|(
name|path
argument_list|,
literal|"file:"
argument_list|)
expr_stmt|;
block|}
name|InputStream
name|is
init|=
literal|null
decl_stmt|;
name|Reader
name|reader
init|=
literal|null
decl_stmt|;
try|try
block|{
name|is
operator|=
operator|new
name|FileInputStream
argument_list|(
name|path
argument_list|)
expr_stmt|;
if|if
condition|(
name|propertiesComponent
operator|.
name|getEncoding
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|reader
operator|=
operator|new
name|BufferedReader
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
name|is
argument_list|,
name|propertiesComponent
operator|.
name|getEncoding
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|answer
operator|.
name|load
argument_list|(
name|reader
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|answer
operator|.
name|load
argument_list|(
name|is
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|FileNotFoundException
name|e
parameter_list|)
block|{
if|if
condition|(
operator|!
name|ignoreMissingLocation
condition|)
block|{
throw|throw
name|e
throw|;
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
name|is
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
DECL|method|loadPropertiesFromClasspath (CamelContext context, boolean ignoreMissingLocation, String path)
specifier|protected
name|Properties
name|loadPropertiesFromClasspath
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|boolean
name|ignoreMissingLocation
parameter_list|,
name|String
name|path
parameter_list|)
throws|throws
name|IOException
block|{
name|Properties
name|answer
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
if|if
condition|(
name|path
operator|.
name|startsWith
argument_list|(
literal|"classpath:"
argument_list|)
condition|)
block|{
name|path
operator|=
name|ObjectHelper
operator|.
name|after
argument_list|(
name|path
argument_list|,
literal|"classpath:"
argument_list|)
expr_stmt|;
block|}
name|InputStream
name|is
init|=
name|context
operator|.
name|getClassResolver
argument_list|()
operator|.
name|loadResourceAsStream
argument_list|(
name|path
argument_list|)
decl_stmt|;
name|Reader
name|reader
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|is
operator|==
literal|null
condition|)
block|{
if|if
condition|(
operator|!
name|ignoreMissingLocation
condition|)
block|{
throw|throw
operator|new
name|FileNotFoundException
argument_list|(
literal|"Properties file "
operator|+
name|path
operator|+
literal|" not found in classpath"
argument_list|)
throw|;
block|}
block|}
else|else
block|{
try|try
block|{
if|if
condition|(
name|propertiesComponent
operator|.
name|getEncoding
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|reader
operator|=
operator|new
name|BufferedReader
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
name|is
argument_list|,
name|propertiesComponent
operator|.
name|getEncoding
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|answer
operator|.
name|load
argument_list|(
name|reader
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|answer
operator|.
name|load
argument_list|(
name|is
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
name|is
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|answer
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
block|{
literal|"rawtypes"
block|,
literal|"unchecked"
block|}
argument_list|)
DECL|method|loadPropertiesFromRegistry (CamelContext context, boolean ignoreMissingLocation, String path)
specifier|protected
name|Properties
name|loadPropertiesFromRegistry
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|boolean
name|ignoreMissingLocation
parameter_list|,
name|String
name|path
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|path
operator|.
name|startsWith
argument_list|(
literal|"ref:"
argument_list|)
condition|)
block|{
name|path
operator|=
name|ObjectHelper
operator|.
name|after
argument_list|(
name|path
argument_list|,
literal|"ref:"
argument_list|)
expr_stmt|;
block|}
name|Properties
name|answer
decl_stmt|;
try|try
block|{
name|answer
operator|=
name|context
operator|.
name|getRegistry
argument_list|()
operator|.
name|lookupByNameAndType
argument_list|(
name|path
argument_list|,
name|Properties
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
comment|// just look up the Map as a fault back
name|Map
name|map
init|=
name|context
operator|.
name|getRegistry
argument_list|()
operator|.
name|lookupByNameAndType
argument_list|(
name|path
argument_list|,
name|Map
operator|.
name|class
argument_list|)
decl_stmt|;
name|answer
operator|=
operator|new
name|Properties
argument_list|()
expr_stmt|;
name|answer
operator|.
name|putAll
argument_list|(
name|map
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|answer
operator|==
literal|null
operator|&&
operator|(
operator|!
name|ignoreMissingLocation
operator|)
condition|)
block|{
throw|throw
operator|new
name|FileNotFoundException
argument_list|(
literal|"Properties "
operator|+
name|path
operator|+
literal|" not found in registry"
argument_list|)
throw|;
block|}
return|return
name|answer
operator|!=
literal|null
condition|?
name|answer
else|:
operator|new
name|Properties
argument_list|()
return|;
block|}
comment|/**      * Strategy to prepare loaded properties before being used by Camel.      *<p/>      * This implementation will ensure values are trimmed, as loading properties from      * a file with values having trailing spaces is not automatic trimmed by the Properties API      * from the JDK.      *      * @param properties  the properties      * @return the prepared properties      */
DECL|method|prepareLoadedProperties (Properties properties)
specifier|protected
name|Properties
name|prepareLoadedProperties
parameter_list|(
name|Properties
name|properties
parameter_list|)
block|{
name|Properties
name|answer
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|entry
range|:
name|properties
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|Object
name|key
init|=
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|Object
name|value
init|=
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
if|if
condition|(
name|value
operator|instanceof
name|String
condition|)
block|{
name|String
name|s
init|=
operator|(
name|String
operator|)
name|value
decl_stmt|;
comment|// trim any trailing spaces which can be a problem when loading from
comment|// a properties file, note that java.util.Properties does already this
comment|// for any potential leading spaces so there's nothing to do there
name|value
operator|=
name|trimTrailingWhitespaces
argument_list|(
name|s
argument_list|)
expr_stmt|;
block|}
name|answer
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
DECL|method|trimTrailingWhitespaces (String s)
specifier|private
specifier|static
name|String
name|trimTrailingWhitespaces
parameter_list|(
name|String
name|s
parameter_list|)
block|{
name|int
name|endIndex
init|=
name|s
operator|.
name|length
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|index
init|=
name|s
operator|.
name|length
argument_list|()
operator|-
literal|1
init|;
name|index
operator|>=
literal|0
condition|;
name|index
operator|--
control|)
block|{
if|if
condition|(
name|s
operator|.
name|charAt
argument_list|(
name|index
argument_list|)
operator|==
literal|' '
condition|)
block|{
name|endIndex
operator|=
name|index
expr_stmt|;
block|}
else|else
block|{
break|break;
block|}
block|}
name|String
name|answer
init|=
name|s
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|endIndex
argument_list|)
decl_stmt|;
return|return
name|answer
return|;
block|}
block|}
end_class

end_unit

