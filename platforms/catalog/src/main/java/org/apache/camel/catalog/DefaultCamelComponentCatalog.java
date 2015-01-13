begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.catalog
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|catalog
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
name|LineNumberReader
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
name|List
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
name|Set
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|SortedSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|TreeSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|PatternSyntaxException
import|;
end_import

begin_comment
comment|/**  * Default {@link org.apache.camel.catalog.CamelComponentCatalog}.  */
end_comment

begin_class
DECL|class|DefaultCamelComponentCatalog
specifier|public
class|class
name|DefaultCamelComponentCatalog
implements|implements
name|CamelComponentCatalog
block|{
DECL|field|MODELS_CATALOG
specifier|private
specifier|static
specifier|final
name|String
name|MODELS_CATALOG
init|=
literal|"org/apache/camel/catalog/models.properties"
decl_stmt|;
DECL|field|COMPONENTS_CATALOG
specifier|private
specifier|static
specifier|final
name|String
name|COMPONENTS_CATALOG
init|=
literal|"org/apache/camel/catalog/components.properties"
decl_stmt|;
DECL|field|DATA_FORMATS_CATALOG
specifier|private
specifier|static
specifier|final
name|String
name|DATA_FORMATS_CATALOG
init|=
literal|"org/apache/camel/catalog/dataformats.properties"
decl_stmt|;
DECL|field|MODEL_JSON
specifier|private
specifier|static
specifier|final
name|String
name|MODEL_JSON
init|=
literal|"org/apache/camel/catalog/models"
decl_stmt|;
DECL|field|COMPONENTS_JSON
specifier|private
specifier|static
specifier|final
name|String
name|COMPONENTS_JSON
init|=
literal|"org/apache/camel/catalog/components"
decl_stmt|;
DECL|field|DATA_FORMATS_JSON
specifier|private
specifier|static
specifier|final
name|String
name|DATA_FORMATS_JSON
init|=
literal|"org/apache/camel/catalog/dataformats"
decl_stmt|;
annotation|@
name|Override
DECL|method|findComponentNames ()
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|findComponentNames
parameter_list|()
block|{
name|List
argument_list|<
name|String
argument_list|>
name|names
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|InputStream
name|is
init|=
name|DefaultCamelComponentCatalog
operator|.
name|class
operator|.
name|getClassLoader
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
name|COMPONENTS_CATALOG
argument_list|)
decl_stmt|;
if|if
condition|(
name|is
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|loadLines
argument_list|(
name|is
argument_list|,
name|names
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|// ignore
block|}
block|}
return|return
name|names
return|;
block|}
annotation|@
name|Override
DECL|method|findDataFormatNames ()
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|findDataFormatNames
parameter_list|()
block|{
name|List
argument_list|<
name|String
argument_list|>
name|names
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|InputStream
name|is
init|=
name|DefaultCamelComponentCatalog
operator|.
name|class
operator|.
name|getClassLoader
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
name|DATA_FORMATS_CATALOG
argument_list|)
decl_stmt|;
if|if
condition|(
name|is
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|loadLines
argument_list|(
name|is
argument_list|,
name|names
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|// ignore
block|}
block|}
return|return
name|names
return|;
block|}
annotation|@
name|Override
DECL|method|findModelNames ()
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|findModelNames
parameter_list|()
block|{
name|List
argument_list|<
name|String
argument_list|>
name|names
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|InputStream
name|is
init|=
name|DefaultCamelComponentCatalog
operator|.
name|class
operator|.
name|getClassLoader
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
name|MODELS_CATALOG
argument_list|)
decl_stmt|;
if|if
condition|(
name|is
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|loadLines
argument_list|(
name|is
argument_list|,
name|names
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|// ignore
block|}
block|}
return|return
name|names
return|;
block|}
annotation|@
name|Override
DECL|method|findModelNames (String filter)
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|findModelNames
parameter_list|(
name|String
name|filter
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|answer
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|names
init|=
name|findModelNames
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|name
range|:
name|names
control|)
block|{
name|String
name|json
init|=
name|modelJSonSchema
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|json
operator|!=
literal|null
condition|)
block|{
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
name|rows
init|=
name|JSonSchemaHelper
operator|.
name|parseJsonSchema
argument_list|(
literal|"model"
argument_list|,
name|json
argument_list|,
literal|false
argument_list|)
decl_stmt|;
for|for
control|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|row
range|:
name|rows
control|)
block|{
if|if
condition|(
name|row
operator|.
name|containsKey
argument_list|(
literal|"label"
argument_list|)
condition|)
block|{
name|String
name|label
init|=
name|row
operator|.
name|get
argument_list|(
literal|"label"
argument_list|)
decl_stmt|;
name|String
index|[]
name|parts
init|=
name|label
operator|.
name|split
argument_list|(
literal|","
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|part
range|:
name|parts
control|)
block|{
try|try
block|{
if|if
condition|(
name|part
operator|.
name|equalsIgnoreCase
argument_list|(
name|filter
argument_list|)
operator|||
name|matchWildcard
argument_list|(
name|part
argument_list|,
name|filter
argument_list|)
operator|||
name|part
operator|.
name|matches
argument_list|(
name|filter
argument_list|)
condition|)
block|{
name|answer
operator|.
name|add
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|PatternSyntaxException
name|e
parameter_list|)
block|{
comment|// ignore as filter is maybe not a pattern
block|}
block|}
block|}
block|}
block|}
block|}
return|return
name|answer
return|;
block|}
annotation|@
name|Override
DECL|method|findComponentNames (String filter)
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|findComponentNames
parameter_list|(
name|String
name|filter
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|answer
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|names
init|=
name|findComponentNames
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|name
range|:
name|names
control|)
block|{
name|String
name|json
init|=
name|componentJSonSchema
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|json
operator|!=
literal|null
condition|)
block|{
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
name|rows
init|=
name|JSonSchemaHelper
operator|.
name|parseJsonSchema
argument_list|(
literal|"component"
argument_list|,
name|json
argument_list|,
literal|false
argument_list|)
decl_stmt|;
for|for
control|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|row
range|:
name|rows
control|)
block|{
if|if
condition|(
name|row
operator|.
name|containsKey
argument_list|(
literal|"label"
argument_list|)
condition|)
block|{
name|String
name|label
init|=
name|row
operator|.
name|get
argument_list|(
literal|"label"
argument_list|)
decl_stmt|;
name|String
index|[]
name|parts
init|=
name|label
operator|.
name|split
argument_list|(
literal|","
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|part
range|:
name|parts
control|)
block|{
try|try
block|{
if|if
condition|(
name|part
operator|.
name|equalsIgnoreCase
argument_list|(
name|filter
argument_list|)
operator|||
name|matchWildcard
argument_list|(
name|part
argument_list|,
name|filter
argument_list|)
operator|||
name|part
operator|.
name|matches
argument_list|(
name|filter
argument_list|)
condition|)
block|{
name|answer
operator|.
name|add
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|PatternSyntaxException
name|e
parameter_list|)
block|{
comment|// ignore as filter is maybe not a pattern
block|}
block|}
block|}
block|}
block|}
block|}
return|return
name|answer
return|;
block|}
annotation|@
name|Override
DECL|method|findDataFormatNames (String filter)
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|findDataFormatNames
parameter_list|(
name|String
name|filter
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|answer
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|names
init|=
name|findDataFormatNames
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|name
range|:
name|names
control|)
block|{
name|String
name|json
init|=
name|dataFormatJSonSchema
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|json
operator|!=
literal|null
condition|)
block|{
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
name|rows
init|=
name|JSonSchemaHelper
operator|.
name|parseJsonSchema
argument_list|(
literal|"model"
argument_list|,
name|json
argument_list|,
literal|false
argument_list|)
decl_stmt|;
for|for
control|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|row
range|:
name|rows
control|)
block|{
if|if
condition|(
name|row
operator|.
name|containsKey
argument_list|(
literal|"label"
argument_list|)
condition|)
block|{
name|String
name|label
init|=
name|row
operator|.
name|get
argument_list|(
literal|"label"
argument_list|)
decl_stmt|;
name|String
index|[]
name|parts
init|=
name|label
operator|.
name|split
argument_list|(
literal|","
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|part
range|:
name|parts
control|)
block|{
try|try
block|{
if|if
condition|(
name|part
operator|.
name|equalsIgnoreCase
argument_list|(
name|filter
argument_list|)
operator|||
name|matchWildcard
argument_list|(
name|part
argument_list|,
name|filter
argument_list|)
operator|||
name|part
operator|.
name|matches
argument_list|(
name|filter
argument_list|)
condition|)
block|{
name|answer
operator|.
name|add
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|PatternSyntaxException
name|e
parameter_list|)
block|{
comment|// ignore as filter is maybe not a pattern
block|}
block|}
block|}
block|}
block|}
block|}
return|return
name|answer
return|;
block|}
annotation|@
name|Override
DECL|method|modelJSonSchema (String name)
specifier|public
name|String
name|modelJSonSchema
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|String
name|file
init|=
name|MODEL_JSON
operator|+
literal|"/"
operator|+
name|name
operator|+
literal|".json"
decl_stmt|;
name|InputStream
name|is
init|=
name|DefaultCamelComponentCatalog
operator|.
name|class
operator|.
name|getClassLoader
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
name|file
argument_list|)
decl_stmt|;
if|if
condition|(
name|is
operator|!=
literal|null
condition|)
block|{
try|try
block|{
return|return
name|loadText
argument_list|(
name|is
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|// ignore
block|}
block|}
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|componentJSonSchema (String name)
specifier|public
name|String
name|componentJSonSchema
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|String
name|file
init|=
name|COMPONENTS_JSON
operator|+
literal|"/"
operator|+
name|name
operator|+
literal|".json"
decl_stmt|;
name|InputStream
name|is
init|=
name|DefaultCamelComponentCatalog
operator|.
name|class
operator|.
name|getClassLoader
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
name|file
argument_list|)
decl_stmt|;
if|if
condition|(
name|is
operator|!=
literal|null
condition|)
block|{
try|try
block|{
return|return
name|loadText
argument_list|(
name|is
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|// ignore
block|}
block|}
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|dataFormatJSonSchema (String name)
specifier|public
name|String
name|dataFormatJSonSchema
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|String
name|file
init|=
name|DATA_FORMATS_JSON
operator|+
literal|"/"
operator|+
name|name
operator|+
literal|".json"
decl_stmt|;
name|InputStream
name|is
init|=
name|DefaultCamelComponentCatalog
operator|.
name|class
operator|.
name|getClassLoader
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
name|file
argument_list|)
decl_stmt|;
if|if
condition|(
name|is
operator|!=
literal|null
condition|)
block|{
try|try
block|{
return|return
name|loadText
argument_list|(
name|is
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|// ignore
block|}
block|}
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|findModelLabels ()
specifier|public
name|Set
argument_list|<
name|String
argument_list|>
name|findModelLabels
parameter_list|()
block|{
name|SortedSet
argument_list|<
name|String
argument_list|>
name|answer
init|=
operator|new
name|TreeSet
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|names
init|=
name|findModelNames
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|name
range|:
name|names
control|)
block|{
name|String
name|json
init|=
name|modelJSonSchema
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|json
operator|!=
literal|null
condition|)
block|{
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
name|rows
init|=
name|JSonSchemaHelper
operator|.
name|parseJsonSchema
argument_list|(
literal|"model"
argument_list|,
name|json
argument_list|,
literal|false
argument_list|)
decl_stmt|;
for|for
control|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|row
range|:
name|rows
control|)
block|{
if|if
condition|(
name|row
operator|.
name|containsKey
argument_list|(
literal|"label"
argument_list|)
condition|)
block|{
name|String
name|label
init|=
name|row
operator|.
name|get
argument_list|(
literal|"label"
argument_list|)
decl_stmt|;
name|String
index|[]
name|parts
init|=
name|label
operator|.
name|split
argument_list|(
literal|","
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|part
range|:
name|parts
control|)
block|{
name|answer
operator|.
name|add
argument_list|(
name|part
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
return|return
name|answer
return|;
block|}
annotation|@
name|Override
DECL|method|findComponentLabels ()
specifier|public
name|Set
argument_list|<
name|String
argument_list|>
name|findComponentLabels
parameter_list|()
block|{
name|SortedSet
argument_list|<
name|String
argument_list|>
name|answer
init|=
operator|new
name|TreeSet
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|names
init|=
name|findComponentNames
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|name
range|:
name|names
control|)
block|{
name|String
name|json
init|=
name|componentJSonSchema
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|json
operator|!=
literal|null
condition|)
block|{
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
name|rows
init|=
name|JSonSchemaHelper
operator|.
name|parseJsonSchema
argument_list|(
literal|"component"
argument_list|,
name|json
argument_list|,
literal|false
argument_list|)
decl_stmt|;
for|for
control|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|row
range|:
name|rows
control|)
block|{
if|if
condition|(
name|row
operator|.
name|containsKey
argument_list|(
literal|"label"
argument_list|)
condition|)
block|{
name|String
name|label
init|=
name|row
operator|.
name|get
argument_list|(
literal|"label"
argument_list|)
decl_stmt|;
name|String
index|[]
name|parts
init|=
name|label
operator|.
name|split
argument_list|(
literal|","
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|part
range|:
name|parts
control|)
block|{
name|answer
operator|.
name|add
argument_list|(
name|part
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
return|return
name|answer
return|;
block|}
annotation|@
name|Override
DECL|method|findDataFormatLabels ()
specifier|public
name|Set
argument_list|<
name|String
argument_list|>
name|findDataFormatLabels
parameter_list|()
block|{
name|SortedSet
argument_list|<
name|String
argument_list|>
name|answer
init|=
operator|new
name|TreeSet
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|names
init|=
name|findDataFormatNames
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|name
range|:
name|names
control|)
block|{
name|String
name|json
init|=
name|dataFormatJSonSchema
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|json
operator|!=
literal|null
condition|)
block|{
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
name|rows
init|=
name|JSonSchemaHelper
operator|.
name|parseJsonSchema
argument_list|(
literal|"model"
argument_list|,
name|json
argument_list|,
literal|false
argument_list|)
decl_stmt|;
for|for
control|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|row
range|:
name|rows
control|)
block|{
if|if
condition|(
name|row
operator|.
name|containsKey
argument_list|(
literal|"label"
argument_list|)
condition|)
block|{
name|String
name|label
init|=
name|row
operator|.
name|get
argument_list|(
literal|"label"
argument_list|)
decl_stmt|;
name|String
index|[]
name|parts
init|=
name|label
operator|.
name|split
argument_list|(
literal|","
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|part
range|:
name|parts
control|)
block|{
name|answer
operator|.
name|add
argument_list|(
name|part
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
return|return
name|answer
return|;
block|}
comment|/**      * Loads the entire stream into memory as a String and returns it.      *<p/>      *<b>Notice:</b> This implementation appends a<tt>\n</tt> as line      * terminator at the of the text.      *<p/>      * Warning, don't use for crazy big streams :)      */
DECL|method|loadLines (InputStream in, List<String> lines)
specifier|private
specifier|static
name|void
name|loadLines
parameter_list|(
name|InputStream
name|in
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|lines
parameter_list|)
throws|throws
name|IOException
block|{
name|InputStreamReader
name|isr
init|=
operator|new
name|InputStreamReader
argument_list|(
name|in
argument_list|)
decl_stmt|;
try|try
block|{
name|BufferedReader
name|reader
init|=
operator|new
name|LineNumberReader
argument_list|(
name|isr
argument_list|)
decl_stmt|;
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
operator|!=
literal|null
condition|)
block|{
name|lines
operator|.
name|add
argument_list|(
name|line
argument_list|)
expr_stmt|;
block|}
else|else
block|{
break|break;
block|}
block|}
block|}
finally|finally
block|{
name|isr
operator|.
name|close
argument_list|()
expr_stmt|;
name|in
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * Loads the entire stream into memory as a String and returns it.      *<p/>      *<b>Notice:</b> This implementation appends a<tt>\n</tt> as line      * terminator at the of the text.      *<p/>      * Warning, don't use for crazy big streams :)      */
DECL|method|loadText (InputStream in)
specifier|public
specifier|static
name|String
name|loadText
parameter_list|(
name|InputStream
name|in
parameter_list|)
throws|throws
name|IOException
block|{
name|StringBuilder
name|builder
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|InputStreamReader
name|isr
init|=
operator|new
name|InputStreamReader
argument_list|(
name|in
argument_list|)
decl_stmt|;
try|try
block|{
name|BufferedReader
name|reader
init|=
operator|new
name|LineNumberReader
argument_list|(
name|isr
argument_list|)
decl_stmt|;
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
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|append
argument_list|(
name|line
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
break|break;
block|}
block|}
return|return
name|builder
operator|.
name|toString
argument_list|()
return|;
block|}
finally|finally
block|{
name|isr
operator|.
name|close
argument_list|()
expr_stmt|;
name|in
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|matchWildcard (String name, String pattern)
specifier|private
specifier|static
name|boolean
name|matchWildcard
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|pattern
parameter_list|)
block|{
comment|// we have wildcard support in that hence you can match with: file* to match any file endpoints
if|if
condition|(
name|pattern
operator|.
name|endsWith
argument_list|(
literal|"*"
argument_list|)
operator|&&
name|name
operator|.
name|startsWith
argument_list|(
name|pattern
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|pattern
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
return|return
literal|false
return|;
block|}
block|}
end_class

end_unit

