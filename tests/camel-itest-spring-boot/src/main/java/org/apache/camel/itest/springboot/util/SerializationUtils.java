begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.itest.springboot.util
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|itest
operator|.
name|springboot
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
name|ByteArrayInputStream
import|;
end_import

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

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ObjectInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ObjectOutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|PrintWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|StringWriter
import|;
end_import

begin_comment
comment|/**  * Provide basic serialization utils.  * Cannot rely on common available libraries to not influence the test outcome.  */
end_comment

begin_class
DECL|class|SerializationUtils
specifier|public
specifier|final
class|class
name|SerializationUtils
block|{
DECL|method|SerializationUtils ()
specifier|private
name|SerializationUtils
parameter_list|()
block|{     }
DECL|method|marshal (Object o)
specifier|public
specifier|static
name|byte
index|[]
name|marshal
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
try|try
init|(
name|ByteArrayOutputStream
name|out
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
init|;
name|ObjectOutputStream
name|oout
operator|=
operator|new
name|ObjectOutputStream
argument_list|(
name|out
argument_list|)
init|)
block|{
name|oout
operator|.
name|writeObject
argument_list|(
name|o
argument_list|)
expr_stmt|;
name|oout
operator|.
name|flush
argument_list|()
expr_stmt|;
return|return
name|out
operator|.
name|toByteArray
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
literal|"Unable to marshal the bean "
operator|+
name|o
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|unmarshal (byte[] content)
specifier|public
specifier|static
name|Object
name|unmarshal
parameter_list|(
name|byte
index|[]
name|content
parameter_list|)
block|{
if|if
condition|(
name|content
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
try|try
init|(
name|ByteArrayInputStream
name|in
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|content
argument_list|)
init|;
name|ObjectInputStream
name|oin
operator|=
operator|new
name|ObjectInputStream
argument_list|(
name|in
argument_list|)
init|)
block|{
name|Object
name|bean
init|=
name|oin
operator|.
name|readObject
argument_list|()
decl_stmt|;
return|return
name|bean
return|;
block|}
catch|catch
parameter_list|(
name|IOException
decl||
name|ClassNotFoundException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Unable to unmarshal the bean"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|transferable (Throwable ex)
specifier|public
specifier|static
name|Throwable
name|transferable
parameter_list|(
name|Throwable
name|ex
parameter_list|)
block|{
try|try
init|(
name|StringWriter
name|sw
init|=
operator|new
name|StringWriter
argument_list|()
init|;
name|PrintWriter
name|pw
operator|=
operator|new
name|PrintWriter
argument_list|(
name|sw
argument_list|)
init|)
block|{
name|ex
operator|.
name|printStackTrace
argument_list|(
name|pw
argument_list|)
expr_stmt|;
name|pw
operator|.
name|flush
argument_list|()
expr_stmt|;
return|return
operator|new
name|Exception
argument_list|(
literal|"Error while executing tests. Wrapped exception is: "
operator|+
name|sw
operator|.
name|toString
argument_list|()
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
return|return
operator|new
name|RuntimeException
argument_list|(
literal|"Error while cleaning up the exception. Message="
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

