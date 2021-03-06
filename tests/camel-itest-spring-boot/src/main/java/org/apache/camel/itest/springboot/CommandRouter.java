begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.itest.springboot
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
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|Future
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|ApplicationContext
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
name|itest
operator|.
name|springboot
operator|.
name|util
operator|.
name|SerializationUtils
operator|.
name|marshal
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
name|itest
operator|.
name|springboot
operator|.
name|util
operator|.
name|SerializationUtils
operator|.
name|transferable
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
name|itest
operator|.
name|springboot
operator|.
name|util
operator|.
name|SerializationUtils
operator|.
name|unmarshal
import|;
end_import

begin_comment
comment|/**  * Routes a command coming from another classloader to the appropriate spring bean.  */
end_comment

begin_class
DECL|class|CommandRouter
specifier|public
specifier|final
class|class
name|CommandRouter
block|{
DECL|method|CommandRouter ()
specifier|private
name|CommandRouter
parameter_list|()
block|{     }
DECL|method|execute (String commandStr, byte[] params)
specifier|public
specifier|static
name|byte
index|[]
name|execute
parameter_list|(
name|String
name|commandStr
parameter_list|,
name|byte
index|[]
name|params
parameter_list|)
block|{
try|try
block|{
name|ApplicationContext
name|context
init|=
name|ApplicationContextHolder
operator|.
name|getApplicationContext
argument_list|()
decl_stmt|;
name|Command
name|command
init|=
operator|(
name|Command
operator|)
name|context
operator|.
name|getBean
argument_list|(
name|commandStr
argument_list|)
decl_stmt|;
name|Object
index|[]
name|args
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|params
operator|!=
literal|null
condition|)
block|{
name|args
operator|=
operator|(
name|Object
index|[]
operator|)
name|unmarshal
argument_list|(
name|params
argument_list|)
expr_stmt|;
block|}
name|Future
argument_list|<
name|Object
argument_list|>
name|futResult
init|=
name|command
operator|.
name|execute
argument_list|(
name|args
argument_list|)
decl_stmt|;
name|Object
name|result
init|=
name|futResult
operator|.
name|get
argument_list|()
decl_stmt|;
return|return
name|marshal
argument_list|(
name|result
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
return|return
name|marshal
argument_list|(
name|transferable
argument_list|(
name|t
argument_list|)
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

