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
name|List
import|;
end_import

begin_class
DECL|class|CatalogHelper
specifier|public
specifier|final
class|class
name|CatalogHelper
block|{
DECL|method|CatalogHelper ()
specifier|private
name|CatalogHelper
parameter_list|()
block|{     }
comment|/**      * Loads the entire stream into memory as a String and returns it.      *<p/>      *<b>Notice:</b> This implementation appends a<tt>\n</tt> as line      * terminator at the of the text.      *<p/>      * Warning, don't use for crazy big streams :)      */
DECL|method|loadLines (InputStream in, List<String> lines)
specifier|public
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
comment|/**      * Matches the name with the pattern.      *      * @param name  the name      * @param pattern the pattern      * @return<tt>true</tt> if matched, or<tt>false</tt> if not      */
DECL|method|matchWildcard (String name, String pattern)
specifier|public
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

