begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|lang
operator|.
name|reflect
operator|.
name|Method
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
name|itest
operator|.
name|springboot
operator|.
name|ITestConfig
import|;
end_import

begin_comment
comment|/**  * Provide access to testing methods defined in the Spring-Boot context (that uses a different classloader).  */
end_comment

begin_class
DECL|class|SpringBootContainerFacade
specifier|public
class|class
name|SpringBootContainerFacade
block|{
DECL|field|delegateClass
specifier|private
name|Class
argument_list|<
name|?
argument_list|>
name|delegateClass
decl_stmt|;
DECL|method|SpringBootContainerFacade (ClassLoader springClassloader)
specifier|public
name|SpringBootContainerFacade
parameter_list|(
name|ClassLoader
name|springClassloader
parameter_list|)
block|{
try|try
block|{
name|this
operator|.
name|delegateClass
operator|=
name|springClassloader
operator|.
name|loadClass
argument_list|(
literal|"org.apache.camel.itest.springboot.CommandRouter"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ClassNotFoundException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Exception while loading target class"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|executeTest (String test, ITestConfig config, String component)
specifier|public
name|Object
name|executeTest
parameter_list|(
name|String
name|test
parameter_list|,
name|ITestConfig
name|config
parameter_list|,
name|String
name|component
parameter_list|)
throws|throws
name|Exception
block|{
name|Object
name|resObj
init|=
name|execute
argument_list|(
name|test
argument_list|,
name|config
argument_list|,
name|component
argument_list|)
decl_stmt|;
return|return
name|resObj
return|;
block|}
DECL|method|execute (String command, Object... args)
specifier|private
name|Object
name|execute
parameter_list|(
name|String
name|command
parameter_list|,
name|Object
modifier|...
name|args
parameter_list|)
throws|throws
name|Exception
block|{
name|Method
name|method
init|=
name|delegateClass
operator|.
name|getMethod
argument_list|(
literal|"execute"
argument_list|,
name|String
operator|.
name|class
argument_list|,
name|byte
index|[]
operator|.
expr|class
argument_list|)
decl_stmt|;
name|byte
index|[]
name|argsSer
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|args
operator|!=
literal|null
condition|)
block|{
name|argsSer
operator|=
name|SerializationUtils
operator|.
name|marshal
argument_list|(
name|args
argument_list|)
expr_stmt|;
block|}
name|byte
index|[]
name|resByte
init|=
operator|(
name|byte
index|[]
operator|)
name|method
operator|.
name|invoke
argument_list|(
literal|null
argument_list|,
name|command
argument_list|,
name|argsSer
argument_list|)
decl_stmt|;
if|if
condition|(
name|resByte
operator|!=
literal|null
condition|)
block|{
name|Object
name|res
init|=
name|SerializationUtils
operator|.
name|unmarshal
argument_list|(
name|resByte
argument_list|)
decl_stmt|;
if|if
condition|(
name|res
operator|instanceof
name|Exception
condition|)
block|{
throw|throw
operator|(
name|Exception
operator|)
name|res
throw|;
block|}
elseif|else
if|if
condition|(
name|res
operator|instanceof
name|Throwable
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
operator|(
name|Throwable
operator|)
name|res
argument_list|)
throw|;
block|}
else|else
block|{
return|return
name|res
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

