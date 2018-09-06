begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.file
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|file
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
name|ContextTestSupport
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
name|ResolveEndpointFailedException
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

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|FileInvalidStartingPathTest
specifier|public
class|class
name|FileInvalidStartingPathTest
extends|extends
name|ContextTestSupport
block|{
annotation|@
name|Test
DECL|method|testInvalidStartingPath ()
specifier|public
name|void
name|testInvalidStartingPath
parameter_list|()
block|{
try|try
block|{
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"file://target/path/${date:now:yyyyMMdd}/${in.header.messageType}-${date:now:hhmmss}.txt"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown an exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ResolveEndpointFailedException
name|e
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|e
operator|.
name|getCause
argument_list|()
operator|.
name|getMessage
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"Invalid directory"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testValidStartingPath ()
specifier|public
name|void
name|testValidStartingPath
parameter_list|()
block|{
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"file://target/path/?fileName=${date:now:yyyyMMdd}/${in.header.messageType}-${date:now:hhmmss}.txt"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

