begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.maven
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|maven
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
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertTrue
import|;
end_import

begin_comment
comment|/**  * User: dbokde  * Date: 5/26/14  * Time: 4:49 AM  */
end_comment

begin_class
DECL|class|AbstractGeneratorMojoTest
specifier|public
class|class
name|AbstractGeneratorMojoTest
block|{
DECL|field|OUT_DIR
specifier|protected
specifier|static
specifier|final
name|String
name|OUT_DIR
init|=
literal|"target/generated-test-sources/camelComponent"
decl_stmt|;
DECL|field|PACKAGE_PATH
specifier|protected
specifier|static
specifier|final
name|String
name|PACKAGE_PATH
init|=
name|AbstractGeneratorMojo
operator|.
name|OUT_PACKAGE
operator|.
name|replaceAll
argument_list|(
literal|"\\."
argument_list|,
literal|"/"
argument_list|)
decl_stmt|;
DECL|method|assertExists (File outFile)
specifier|protected
name|void
name|assertExists
parameter_list|(
name|File
name|outFile
parameter_list|)
block|{
name|assertTrue
argument_list|(
literal|"Generated file not found "
operator|+
name|outFile
operator|.
name|getPath
argument_list|()
argument_list|,
name|outFile
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

