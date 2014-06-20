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
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|plugins
operator|.
name|annotations
operator|.
name|Parameter
import|;
end_import

begin_comment
comment|/**  * Base class for API based code generation MOJOs.  */
end_comment

begin_class
DECL|class|AbstractSourceGeneratorMojo
specifier|public
specifier|abstract
class|class
name|AbstractSourceGeneratorMojo
extends|extends
name|AbstractGeneratorMojo
block|{
annotation|@
name|Parameter
argument_list|(
name|defaultValue
operator|=
literal|"${project.build.directory}/generated-sources/camel-component"
argument_list|)
DECL|field|generatedSrcDir
specifier|protected
name|File
name|generatedSrcDir
decl_stmt|;
annotation|@
name|Parameter
argument_list|(
name|defaultValue
operator|=
literal|"${project.build.directory}/generated-test-sources/camel-component"
argument_list|)
DECL|field|generatedTestDir
specifier|protected
name|File
name|generatedTestDir
decl_stmt|;
DECL|method|getGeneratedSrcDir ()
specifier|public
name|File
name|getGeneratedSrcDir
parameter_list|()
block|{
return|return
name|generatedSrcDir
return|;
block|}
DECL|method|setGeneratedSrcDir (File generatedSrcDir)
specifier|public
name|void
name|setGeneratedSrcDir
parameter_list|(
name|File
name|generatedSrcDir
parameter_list|)
block|{
name|this
operator|.
name|generatedSrcDir
operator|=
name|generatedSrcDir
expr_stmt|;
block|}
DECL|method|getGeneratedTestDir ()
specifier|public
name|File
name|getGeneratedTestDir
parameter_list|()
block|{
return|return
name|generatedTestDir
return|;
block|}
DECL|method|setGeneratedTestDir (File generatedTestDir)
specifier|public
name|void
name|setGeneratedTestDir
parameter_list|(
name|File
name|generatedTestDir
parameter_list|)
block|{
name|this
operator|.
name|generatedTestDir
operator|=
name|generatedTestDir
expr_stmt|;
block|}
block|}
end_class

end_unit

