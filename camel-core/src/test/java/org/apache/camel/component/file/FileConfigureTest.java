begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_comment
comment|/**  * @version $Revision: 1.1 $  */
end_comment

begin_class
DECL|class|FileConfigureTest
specifier|public
class|class
name|FileConfigureTest
extends|extends
name|ContextTestSupport
block|{
DECL|method|testUriConfigurations ()
specifier|public
name|void
name|testUriConfigurations
parameter_list|()
throws|throws
name|Exception
block|{
name|assertFileEndpoint
argument_list|(
literal|"file://target/foo/bar"
argument_list|,
literal|"target/foo/bar"
argument_list|)
expr_stmt|;
name|assertFileEndpoint
argument_list|(
literal|"file://target/foo/bar?delete=true"
argument_list|,
literal|"target/foo/bar"
argument_list|)
expr_stmt|;
name|assertFileEndpoint
argument_list|(
literal|"file:target/foo/bar?delete=true"
argument_list|,
literal|"target/foo/bar"
argument_list|)
expr_stmt|;
name|assertFileEndpoint
argument_list|(
literal|"file:target/foo/bar"
argument_list|,
literal|"target/foo/bar"
argument_list|)
expr_stmt|;
block|}
DECL|method|assertFileEndpoint (String endpointUri, String expectedPath)
specifier|private
name|void
name|assertFileEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|String
name|expectedPath
parameter_list|)
block|{
name|FileEndpoint
name|endpoint
init|=
name|resolveMandatoryEndpoint
argument_list|(
name|endpointUri
argument_list|,
name|FileEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Could not find endpoint: "
operator|+
name|endpointUri
argument_list|,
name|endpoint
argument_list|)
expr_stmt|;
name|File
name|file
init|=
name|endpoint
operator|.
name|getFile
argument_list|()
decl_stmt|;
name|String
name|path
init|=
name|file
operator|.
name|getPath
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"For uri: "
operator|+
name|endpointUri
operator|+
literal|" the file is not equal"
argument_list|,
name|expectedPath
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

