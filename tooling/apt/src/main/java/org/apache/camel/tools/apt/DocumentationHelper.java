begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.tools.apt
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|tools
operator|.
name|apt
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
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|tools
operator|.
name|apt
operator|.
name|JsonSchemaHelper
operator|.
name|parseJsonSchema
import|;
end_import

begin_comment
comment|/**  * Helper to find documentation for inherited options when a component extends another.  */
end_comment

begin_class
DECL|class|DocumentationHelper
specifier|public
specifier|final
class|class
name|DocumentationHelper
block|{
DECL|method|findComponentJavaDoc (String scheme, String extendsScheme, String fieldName)
specifier|public
specifier|static
name|String
name|findComponentJavaDoc
parameter_list|(
name|String
name|scheme
parameter_list|,
name|String
name|extendsScheme
parameter_list|,
name|String
name|fieldName
parameter_list|)
block|{
name|File
name|file
init|=
name|jsonFile
argument_list|(
name|scheme
argument_list|,
name|extendsScheme
argument_list|)
decl_stmt|;
if|if
condition|(
name|file
operator|!=
literal|null
condition|)
block|{
name|FileInputStream
name|fis
init|=
literal|null
decl_stmt|;
try|try
block|{
name|fis
operator|=
operator|new
name|FileInputStream
argument_list|(
name|file
argument_list|)
expr_stmt|;
name|String
name|json
init|=
name|loadText
argument_list|(
name|fis
argument_list|)
decl_stmt|;
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
name|parseJsonSchema
argument_list|(
literal|"componentProperties"
argument_list|,
name|json
argument_list|,
literal|true
argument_list|)
decl_stmt|;
return|return
name|getPropertyDescription
argument_list|(
name|rows
argument_list|,
name|fieldName
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// ignore
block|}
finally|finally
block|{
name|IOHelper
operator|.
name|close
argument_list|(
name|fis
argument_list|)
expr_stmt|;
block|}
block|}
comment|// not found
return|return
literal|null
return|;
block|}
DECL|method|findEndpointJavaDoc (String scheme, String extendsScheme, String fieldName)
specifier|public
specifier|static
name|String
name|findEndpointJavaDoc
parameter_list|(
name|String
name|scheme
parameter_list|,
name|String
name|extendsScheme
parameter_list|,
name|String
name|fieldName
parameter_list|)
block|{
name|File
name|file
init|=
name|jsonFile
argument_list|(
name|scheme
argument_list|,
name|extendsScheme
argument_list|)
decl_stmt|;
if|if
condition|(
name|file
operator|!=
literal|null
condition|)
block|{
name|FileInputStream
name|fis
init|=
literal|null
decl_stmt|;
try|try
block|{
name|fis
operator|=
operator|new
name|FileInputStream
argument_list|(
name|file
argument_list|)
expr_stmt|;
name|String
name|json
init|=
name|loadText
argument_list|(
name|fis
argument_list|)
decl_stmt|;
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
name|parseJsonSchema
argument_list|(
literal|"properties"
argument_list|,
name|json
argument_list|,
literal|true
argument_list|)
decl_stmt|;
return|return
name|getPropertyDescription
argument_list|(
name|rows
argument_list|,
name|fieldName
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// ignore
block|}
finally|finally
block|{
name|IOHelper
operator|.
name|close
argument_list|(
name|fis
argument_list|)
expr_stmt|;
block|}
block|}
comment|// not found
return|return
literal|null
return|;
block|}
DECL|method|getPropertyDescription (List<Map<String, String>> rows, String name)
specifier|private
specifier|static
name|String
name|getPropertyDescription
parameter_list|(
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
parameter_list|,
name|String
name|name
parameter_list|)
block|{
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
name|String
name|description
init|=
literal|null
decl_stmt|;
name|boolean
name|found
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|row
operator|.
name|containsKey
argument_list|(
literal|"name"
argument_list|)
condition|)
block|{
name|found
operator|=
name|name
operator|.
name|equals
argument_list|(
name|row
operator|.
name|get
argument_list|(
literal|"name"
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|row
operator|.
name|containsKey
argument_list|(
literal|"description"
argument_list|)
condition|)
block|{
name|description
operator|=
name|row
operator|.
name|get
argument_list|(
literal|"description"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|found
condition|)
block|{
return|return
name|description
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
DECL|method|jsonFile (String scheme, String extendsScheme)
specifier|private
specifier|static
name|File
name|jsonFile
parameter_list|(
name|String
name|scheme
parameter_list|,
name|String
name|extendsScheme
parameter_list|)
block|{
comment|// TODO: scan components for each component and find component name from extendsScheme
comment|// and then find the package name where the json file is
if|if
condition|(
literal|"file"
operator|.
name|equals
argument_list|(
name|extendsScheme
argument_list|)
condition|)
block|{
return|return
operator|new
name|File
argument_list|(
literal|"../../camel-core/target/classes/org/apache/camel/component/file/file.json"
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
literal|"atom"
operator|.
name|equals
argument_list|(
name|extendsScheme
argument_list|)
condition|)
block|{
return|return
operator|new
name|File
argument_list|(
literal|"../camel-atom/target/classes/org/apache/camel/component/atom/atom.json"
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
literal|"http"
operator|.
name|equals
argument_list|(
name|extendsScheme
argument_list|)
condition|)
block|{
return|return
operator|new
name|File
argument_list|(
literal|"../camel-http/target/classes/org/apache/camel/component/http/http.json"
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
literal|"https"
operator|.
name|equals
argument_list|(
name|extendsScheme
argument_list|)
condition|)
block|{
return|return
operator|new
name|File
argument_list|(
literal|"../camel-http/target/classes/org/apache/camel/component/http/https.json"
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
literal|"netty"
operator|.
name|equals
argument_list|(
name|extendsScheme
argument_list|)
condition|)
block|{
return|return
operator|new
name|File
argument_list|(
literal|"../camel-netty/target/classes/org/apache/camel/component/netty/netty.json"
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
literal|"netty4"
operator|.
name|equals
argument_list|(
name|extendsScheme
argument_list|)
condition|)
block|{
return|return
operator|new
name|File
argument_list|(
literal|"../camel-netty4/target/classes/org/apache/camel/component/netty4/netty4.json"
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
literal|"servlet"
operator|.
name|equals
argument_list|(
name|extendsScheme
argument_list|)
condition|)
block|{
return|return
operator|new
name|File
argument_list|(
literal|"../camel-servlet/target/classes/org/apache/camel/component/servlet/servlet.json"
argument_list|)
return|;
block|}
comment|// not found
return|return
literal|null
return|;
block|}
comment|/**      * Loads the entire stream into memory as a String and returns it.      *<p/>      *<b>Notice:</b> This implementation appends a<tt>\n</tt> as line      * terminator at the of the text.      *<p/>      * Warning, don't use for crazy big streams :)      */
DECL|method|loadText (InputStream in)
specifier|private
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
block|}
end_class

end_unit

