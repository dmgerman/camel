begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.test.util
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|test
operator|.
name|util
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayOutputStream
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

begin_class
DECL|class|PayloadBuilder
specifier|public
class|class
name|PayloadBuilder
block|{
DECL|field|builderStream
name|ByteArrayOutputStream
name|builderStream
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
DECL|method|PayloadBuilder ()
specifier|public
name|PayloadBuilder
parameter_list|()
block|{     }
DECL|method|PayloadBuilder (byte b)
specifier|public
name|PayloadBuilder
parameter_list|(
name|byte
name|b
parameter_list|)
throws|throws
name|IOException
block|{
name|this
operator|.
name|append
argument_list|(
name|b
argument_list|)
expr_stmt|;
block|}
DECL|method|PayloadBuilder (byte[] bytes)
specifier|public
name|PayloadBuilder
parameter_list|(
name|byte
index|[]
name|bytes
parameter_list|)
throws|throws
name|IOException
block|{
name|this
operator|.
name|append
argument_list|(
name|bytes
argument_list|)
expr_stmt|;
block|}
DECL|method|PayloadBuilder (char... chars)
specifier|public
name|PayloadBuilder
parameter_list|(
name|char
modifier|...
name|chars
parameter_list|)
throws|throws
name|IOException
block|{
name|this
operator|.
name|append
argument_list|(
name|chars
argument_list|)
expr_stmt|;
block|}
DECL|method|PayloadBuilder (String... strings)
specifier|public
name|PayloadBuilder
parameter_list|(
name|String
modifier|...
name|strings
parameter_list|)
throws|throws
name|IOException
block|{
name|this
operator|.
name|append
argument_list|(
name|strings
argument_list|)
expr_stmt|;
block|}
DECL|method|build (byte b)
specifier|public
specifier|static
name|byte
index|[]
name|build
parameter_list|(
name|byte
name|b
parameter_list|)
block|{
try|try
block|{
return|return
operator|new
name|PayloadBuilder
argument_list|(
name|b
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"PayloadBuilder.build(byte) failure"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|build (byte b, byte... bytes)
specifier|public
specifier|static
name|byte
index|[]
name|build
parameter_list|(
name|byte
name|b
parameter_list|,
name|byte
modifier|...
name|bytes
parameter_list|)
block|{
try|try
block|{
return|return
operator|new
name|PayloadBuilder
argument_list|(
name|b
argument_list|)
operator|.
name|append
argument_list|(
name|bytes
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"PayloadBuilder.build(byte) failure"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|build (byte[] bytes)
specifier|public
specifier|static
name|byte
index|[]
name|build
parameter_list|(
name|byte
index|[]
name|bytes
parameter_list|)
block|{
try|try
block|{
return|return
operator|new
name|PayloadBuilder
argument_list|(
name|bytes
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"PayloadBuilder.build(byte[]) failure"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|build (char c)
specifier|public
specifier|static
name|byte
index|[]
name|build
parameter_list|(
name|char
name|c
parameter_list|)
block|{
try|try
block|{
return|return
operator|new
name|PayloadBuilder
argument_list|(
name|c
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"PayloadBuilder.build(char...) failure"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|build (char c, char... chars)
specifier|public
specifier|static
name|byte
index|[]
name|build
parameter_list|(
name|char
name|c
parameter_list|,
name|char
modifier|...
name|chars
parameter_list|)
block|{
try|try
block|{
return|return
operator|new
name|PayloadBuilder
argument_list|(
name|c
argument_list|)
operator|.
name|append
argument_list|(
name|chars
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"PayloadBuilder.build(char...) failure"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|build (char[] chars)
specifier|public
specifier|static
name|byte
index|[]
name|build
parameter_list|(
name|char
index|[]
name|chars
parameter_list|)
block|{
try|try
block|{
return|return
operator|new
name|PayloadBuilder
argument_list|(
name|chars
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"PayloadBuilder.build(char...) failure"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|build (String s)
specifier|public
specifier|static
name|byte
index|[]
name|build
parameter_list|(
name|String
name|s
parameter_list|)
block|{
try|try
block|{
return|return
operator|new
name|PayloadBuilder
argument_list|(
name|s
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"PayloadBuilder.build(String) failure"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|build (String[] strings)
specifier|public
specifier|static
name|byte
index|[]
name|build
parameter_list|(
name|String
index|[]
name|strings
parameter_list|)
block|{
try|try
block|{
return|return
operator|new
name|PayloadBuilder
argument_list|(
name|strings
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"PayloadBuilder.build(String[]) failure"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|build (char start, String s)
specifier|public
specifier|static
name|byte
index|[]
name|build
parameter_list|(
name|char
name|start
parameter_list|,
name|String
name|s
parameter_list|)
block|{
try|try
block|{
return|return
operator|new
name|PayloadBuilder
argument_list|(
name|start
argument_list|)
operator|.
name|append
argument_list|(
name|s
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"PayloadBuilder.build(String) failure"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|build (char start, String s, char... end)
specifier|public
specifier|static
name|byte
index|[]
name|build
parameter_list|(
name|char
name|start
parameter_list|,
name|String
name|s
parameter_list|,
name|char
modifier|...
name|end
parameter_list|)
block|{
try|try
block|{
return|return
operator|new
name|PayloadBuilder
argument_list|(
name|start
argument_list|)
operator|.
name|append
argument_list|(
name|s
argument_list|)
operator|.
name|append
argument_list|(
name|end
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"PayloadBuilder.build(char, String, char...) failure"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|build (char start, byte[] bytes, char... end)
specifier|public
specifier|static
name|byte
index|[]
name|build
parameter_list|(
name|char
name|start
parameter_list|,
name|byte
index|[]
name|bytes
parameter_list|,
name|char
modifier|...
name|end
parameter_list|)
block|{
try|try
block|{
return|return
operator|new
name|PayloadBuilder
argument_list|(
name|start
argument_list|)
operator|.
name|append
argument_list|(
name|bytes
argument_list|)
operator|.
name|append
argument_list|(
name|end
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"PayloadBuilder.build(char, byte[], char...) failure"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|build (String s, char... end)
specifier|public
specifier|static
name|byte
index|[]
name|build
parameter_list|(
name|String
name|s
parameter_list|,
name|char
modifier|...
name|end
parameter_list|)
block|{
try|try
block|{
return|return
operator|new
name|PayloadBuilder
argument_list|(
name|s
argument_list|)
operator|.
name|append
argument_list|(
name|end
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"PayloadBuilder.build(String, char...) failure"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|build (byte[] bytes, char... end)
specifier|public
specifier|static
name|byte
index|[]
name|build
parameter_list|(
name|byte
index|[]
name|bytes
parameter_list|,
name|char
modifier|...
name|end
parameter_list|)
block|{
try|try
block|{
return|return
operator|new
name|PayloadBuilder
argument_list|(
name|bytes
argument_list|)
operator|.
name|append
argument_list|(
name|end
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"PayloadBuilder.build(byte[], char...) failure"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|build (byte[] bytes, String s)
specifier|public
specifier|static
name|byte
index|[]
name|build
parameter_list|(
name|byte
index|[]
name|bytes
parameter_list|,
name|String
name|s
parameter_list|)
block|{
try|try
block|{
return|return
operator|new
name|PayloadBuilder
argument_list|(
name|bytes
argument_list|)
operator|.
name|append
argument_list|(
name|s
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"PayloadBuilder.build(byte[], String) failure"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|append (byte b)
specifier|public
name|PayloadBuilder
name|append
parameter_list|(
name|byte
name|b
parameter_list|)
throws|throws
name|IOException
block|{
name|builderStream
operator|.
name|write
argument_list|(
name|b
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|append (byte[] bytes)
specifier|public
name|PayloadBuilder
name|append
parameter_list|(
name|byte
index|[]
name|bytes
parameter_list|)
throws|throws
name|IOException
block|{
name|builderStream
operator|.
name|write
argument_list|(
name|bytes
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|append (char... chars)
specifier|public
name|PayloadBuilder
name|append
parameter_list|(
name|char
modifier|...
name|chars
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|chars
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|char
name|c
range|:
name|chars
control|)
block|{
name|builderStream
operator|.
name|write
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|this
return|;
block|}
DECL|method|append (String... strings)
specifier|public
name|PayloadBuilder
name|append
parameter_list|(
name|String
modifier|...
name|strings
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|strings
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|String
name|s
range|:
name|strings
control|)
block|{
name|builderStream
operator|.
name|write
argument_list|(
name|s
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|this
return|;
block|}
DECL|method|append (byte[] payload, int startPosition, int length)
specifier|public
name|PayloadBuilder
name|append
parameter_list|(
name|byte
index|[]
name|payload
parameter_list|,
name|int
name|startPosition
parameter_list|,
name|int
name|length
parameter_list|)
throws|throws
name|IOException
block|{
name|builderStream
operator|.
name|write
argument_list|(
name|payload
argument_list|,
name|startPosition
argument_list|,
name|length
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|build ()
specifier|public
name|byte
index|[]
name|build
parameter_list|()
block|{
name|byte
index|[]
name|answer
init|=
name|builderStream
operator|.
name|toByteArray
argument_list|()
decl_stmt|;
name|builderStream
operator|.
name|reset
argument_list|()
expr_stmt|;
return|return
name|answer
return|;
block|}
block|}
end_class

end_unit

