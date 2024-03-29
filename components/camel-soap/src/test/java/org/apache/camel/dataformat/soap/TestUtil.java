begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.dataformat.soap
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|dataformat
operator|.
name|soap
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

begin_class
DECL|class|TestUtil
specifier|public
specifier|final
class|class
name|TestUtil
block|{
DECL|method|TestUtil ()
specifier|private
name|TestUtil
parameter_list|()
block|{     }
DECL|method|readStream (InputStream is)
specifier|public
specifier|static
name|String
name|readStream
parameter_list|(
name|InputStream
name|is
parameter_list|)
throws|throws
name|IOException
block|{
try|try
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
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
name|is
argument_list|,
literal|"UTF-8"
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|line
decl_stmt|;
name|boolean
name|comment
init|=
literal|false
decl_stmt|;
while|while
condition|(
operator|(
name|line
operator|=
name|reader
operator|.
name|readLine
argument_list|()
operator|)
operator|!=
literal|null
condition|)
block|{
comment|// skip comments such as ASF license headers
if|if
condition|(
name|line
operator|.
name|startsWith
argument_list|(
literal|"<!--"
argument_list|)
condition|)
block|{
name|comment
operator|=
literal|true
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|line
operator|.
name|startsWith
argument_list|(
literal|"-->"
argument_list|)
condition|)
block|{
name|comment
operator|=
literal|false
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
operator|!
name|comment
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|line
argument_list|)
operator|.
name|append
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
finally|finally
block|{
name|is
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

