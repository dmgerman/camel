begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.exec
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|exec
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
name|IOException
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

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|core
operator|.
name|io
operator|.
name|ClassPathResource
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|core
operator|.
name|io
operator|.
name|Resource
import|;
end_import

begin_class
DECL|class|ExecTestUtils
specifier|public
specifier|final
class|class
name|ExecTestUtils
block|{
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
name|ExecTestUtils
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|ExecTestUtils ()
specifier|private
name|ExecTestUtils
parameter_list|()
block|{     }
comment|/**      * Where on the file system is located the<code>classpathResource</code>?      *       * @param classpathResource a resource in the classpath      * @return null if the resource does not exist in the classpath. If the file      *         is not null the resource is guaranteed to exist on the file      *         system      */
DECL|method|getClasspathResourceFileOrNull (String classpathResource)
specifier|public
specifier|static
name|File
name|getClasspathResourceFileOrNull
parameter_list|(
name|String
name|classpathResource
parameter_list|)
block|{
if|if
condition|(
name|classpathResource
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
try|try
block|{
name|Resource
name|resource
init|=
operator|new
name|ClassPathResource
argument_list|(
name|classpathResource
argument_list|)
decl_stmt|;
name|File
name|resourceFile
init|=
name|resource
operator|.
name|getFile
argument_list|()
decl_stmt|;
return|return
name|resourceFile
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ioe
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"The resource  "
operator|+
name|classpathResource
operator|+
literal|" does not exist!"
argument_list|,
name|ioe
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
comment|/**      * @return the java executable in a system independent way.      */
DECL|method|buildJavaExecutablePath ()
specifier|public
specifier|static
name|String
name|buildJavaExecutablePath
parameter_list|()
block|{
name|String
name|javaHome
init|=
name|System
operator|.
name|getenv
argument_list|(
literal|"JAVA_HOME"
argument_list|)
decl_stmt|;
if|if
condition|(
name|javaHome
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"The Exec component tests will fail, because the environment variable JAVA_HOME is not set!"
argument_list|)
throw|;
block|}
name|File
name|java
init|=
operator|new
name|File
argument_list|(
name|javaHome
operator|+
name|File
operator|.
name|separator
operator|+
literal|"bin"
operator|+
name|File
operator|.
name|separator
operator|+
literal|"java"
argument_list|)
decl_stmt|;
return|return
name|java
operator|.
name|getAbsolutePath
argument_list|()
return|;
block|}
block|}
end_class

end_unit

