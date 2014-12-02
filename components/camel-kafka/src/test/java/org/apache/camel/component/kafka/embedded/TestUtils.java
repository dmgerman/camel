begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.kafka.embedded
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|kafka
operator|.
name|embedded
package|;
end_package

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
name|net
operator|.
name|ServerSocket
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Random
import|;
end_import

begin_class
DECL|class|TestUtils
specifier|final
class|class
name|TestUtils
block|{
DECL|field|RANDOM
specifier|private
specifier|static
specifier|final
name|Random
name|RANDOM
init|=
operator|new
name|Random
argument_list|()
decl_stmt|;
DECL|method|TestUtils ()
specifier|private
name|TestUtils
parameter_list|()
block|{     }
DECL|method|constructTempDir (String dirPrefix)
specifier|public
specifier|static
name|File
name|constructTempDir
parameter_list|(
name|String
name|dirPrefix
parameter_list|)
block|{
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"java.io.tmpdir"
argument_list|)
argument_list|,
name|dirPrefix
operator|+
name|RANDOM
operator|.
name|nextInt
argument_list|(
literal|10000000
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|file
operator|.
name|mkdirs
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"could not create temp directory: "
operator|+
name|file
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
throw|;
block|}
name|file
operator|.
name|deleteOnExit
argument_list|()
expr_stmt|;
return|return
name|file
return|;
block|}
DECL|method|getAvailablePort ()
specifier|public
specifier|static
name|int
name|getAvailablePort
parameter_list|()
block|{
try|try
block|{
name|ServerSocket
name|socket
init|=
operator|new
name|ServerSocket
argument_list|(
literal|0
argument_list|)
decl_stmt|;
try|try
block|{
return|return
name|socket
operator|.
name|getLocalPort
argument_list|()
return|;
block|}
finally|finally
block|{
name|socket
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Cannot find available port: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|deleteFile (File path)
specifier|public
specifier|static
name|boolean
name|deleteFile
parameter_list|(
name|File
name|path
parameter_list|)
throws|throws
name|FileNotFoundException
block|{
if|if
condition|(
operator|!
name|path
operator|.
name|exists
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|FileNotFoundException
argument_list|(
name|path
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
throw|;
block|}
name|boolean
name|ret
init|=
literal|true
decl_stmt|;
if|if
condition|(
name|path
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
for|for
control|(
name|File
name|f
range|:
name|path
operator|.
name|listFiles
argument_list|()
control|)
block|{
name|ret
operator|=
name|ret
operator|&&
name|deleteFile
argument_list|(
name|f
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|ret
operator|&&
name|path
operator|.
name|delete
argument_list|()
return|;
block|}
block|}
end_class

end_unit

