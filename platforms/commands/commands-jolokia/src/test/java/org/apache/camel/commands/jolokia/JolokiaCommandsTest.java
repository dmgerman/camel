begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.commands.jolokia
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|commands
operator|.
name|jolokia
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|commands
operator|.
name|ContextInfoCommand
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
name|commands
operator|.
name|ContextListCommand
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Ignore
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_class
annotation|@
name|Ignore
argument_list|(
literal|"used for manual testing"
argument_list|)
DECL|class|JolokiaCommandsTest
specifier|public
class|class
name|JolokiaCommandsTest
block|{
DECL|field|url
specifier|private
name|String
name|url
init|=
literal|"http://localhost:8080/jolokia"
decl_stmt|;
DECL|field|controller
specifier|private
name|JolokiaCamelController
name|controller
decl_stmt|;
annotation|@
name|Test
DECL|method|testContextList ()
specifier|public
name|void
name|testContextList
parameter_list|()
throws|throws
name|Exception
block|{
name|controller
operator|=
operator|new
name|DefaultJolokiaCamelController
argument_list|()
expr_stmt|;
name|controller
operator|.
name|connect
argument_list|(
name|url
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|controller
operator|.
name|ping
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Error connecting to "
operator|+
name|url
argument_list|)
throw|;
block|}
name|ContextListCommand
name|cmd
init|=
operator|new
name|ContextListCommand
argument_list|()
decl_stmt|;
name|cmd
operator|.
name|execute
argument_list|(
name|controller
argument_list|,
name|System
operator|.
name|out
argument_list|,
name|System
operator|.
name|err
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testContextInfo ()
specifier|public
name|void
name|testContextInfo
parameter_list|()
throws|throws
name|Exception
block|{
name|controller
operator|=
operator|new
name|DefaultJolokiaCamelController
argument_list|()
expr_stmt|;
name|controller
operator|.
name|connect
argument_list|(
name|url
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|controller
operator|.
name|ping
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Error connecting to "
operator|+
name|url
argument_list|)
throw|;
block|}
name|ContextInfoCommand
name|cmd
init|=
operator|new
name|ContextInfoCommand
argument_list|(
literal|"myCamel"
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|cmd
operator|.
name|setStringEscape
argument_list|(
operator|new
name|NoopStringEscape
argument_list|()
argument_list|)
expr_stmt|;
name|cmd
operator|.
name|execute
argument_list|(
name|controller
argument_list|,
name|System
operator|.
name|out
argument_list|,
name|System
operator|.
name|err
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

