begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.soroushbot
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|soroushbot
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|EOFException
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
name|util
operator|.
name|Arrays
import|;
end_import

begin_class
DECL|class|IOUtils
specifier|public
specifier|final
class|class
name|IOUtils
block|{
DECL|method|IOUtils ()
specifier|private
name|IOUtils
parameter_list|()
block|{     }
DECL|method|readFully (InputStream var0, int var1, boolean var2)
specifier|public
specifier|static
name|byte
index|[]
name|readFully
parameter_list|(
name|InputStream
name|var0
parameter_list|,
name|int
name|var1
parameter_list|,
name|boolean
name|var2
parameter_list|)
throws|throws
name|IOException
block|{
name|byte
index|[]
name|var3
init|=
operator|new
name|byte
index|[
literal|0
index|]
decl_stmt|;
if|if
condition|(
name|var1
operator|==
operator|-
literal|1
condition|)
block|{
name|var1
operator|=
literal|2147483647
expr_stmt|;
block|}
name|int
name|var6
decl_stmt|;
for|for
control|(
name|int
name|var4
init|=
literal|0
init|;
name|var4
operator|<
name|var1
condition|;
name|var4
operator|+=
name|var6
control|)
block|{
name|int
name|var5
decl_stmt|;
if|if
condition|(
name|var4
operator|>=
name|var3
operator|.
name|length
condition|)
block|{
name|var5
operator|=
name|Math
operator|.
name|min
argument_list|(
name|var1
operator|-
name|var4
argument_list|,
name|var3
operator|.
name|length
operator|+
literal|1024
argument_list|)
expr_stmt|;
if|if
condition|(
name|var3
operator|.
name|length
operator|<
name|var4
operator|+
name|var5
condition|)
block|{
name|var3
operator|=
name|Arrays
operator|.
name|copyOf
argument_list|(
name|var3
argument_list|,
name|var4
operator|+
name|var5
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|var5
operator|=
name|var3
operator|.
name|length
operator|-
name|var4
expr_stmt|;
block|}
name|var6
operator|=
name|var0
operator|.
name|read
argument_list|(
name|var3
argument_list|,
name|var4
argument_list|,
name|var5
argument_list|)
expr_stmt|;
if|if
condition|(
name|var6
operator|<
literal|0
condition|)
block|{
if|if
condition|(
name|var2
operator|&&
name|var1
operator|!=
literal|2147483647
condition|)
block|{
throw|throw
operator|new
name|EOFException
argument_list|(
literal|"Detect premature EOF"
argument_list|)
throw|;
block|}
if|if
condition|(
name|var3
operator|.
name|length
operator|!=
name|var4
condition|)
block|{
name|var3
operator|=
name|Arrays
operator|.
name|copyOf
argument_list|(
name|var3
argument_list|,
name|var4
argument_list|)
expr_stmt|;
block|}
break|break;
block|}
block|}
return|return
name|var3
return|;
block|}
block|}
end_class

end_unit
