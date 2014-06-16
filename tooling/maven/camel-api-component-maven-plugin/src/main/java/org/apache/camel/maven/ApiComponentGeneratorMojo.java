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
name|plugin
operator|.
name|MojoExecutionException
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
name|plugin
operator|.
name|MojoFailureException
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
name|LifecyclePhase
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
name|Mojo
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
name|ResolutionScope
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|velocity
operator|.
name|VelocityContext
import|;
end_import

begin_comment
comment|/**  * Generates Camel Component based on a collection of APIs.  */
end_comment

begin_class
annotation|@
name|Mojo
argument_list|(
name|name
operator|=
literal|"fromApis"
argument_list|,
name|requiresDependencyResolution
operator|=
name|ResolutionScope
operator|.
name|COMPILE_PLUS_RUNTIME
argument_list|,
name|requiresProject
operator|=
literal|true
argument_list|,
name|defaultPhase
operator|=
name|LifecyclePhase
operator|.
name|GENERATE_SOURCES
argument_list|)
DECL|class|ApiComponentGeneratorMojo
specifier|public
class|class
name|ApiComponentGeneratorMojo
extends|extends
name|AbstractSourceGeneratorMojo
block|{
annotation|@
name|Parameter
argument_list|(
name|required
operator|=
literal|true
argument_list|)
DECL|field|apis
specifier|protected
name|ApiProxy
index|[]
name|apis
decl_stmt|;
annotation|@
name|Override
DECL|method|execute ()
specifier|public
name|void
name|execute
parameter_list|()
throws|throws
name|MojoExecutionException
throws|,
name|MojoFailureException
block|{
if|if
condition|(
name|apis
operator|==
literal|null
operator|||
name|apis
operator|.
name|length
operator|==
literal|0
condition|)
block|{
throw|throw
operator|new
name|MojoExecutionException
argument_list|(
literal|"One or more API proxies are required"
argument_list|)
throw|;
block|}
comment|// generate ApiCollection
name|mergeTemplate
argument_list|(
name|getApiContext
argument_list|()
argument_list|,
name|getApiCollectionFile
argument_list|()
argument_list|,
literal|"/api-collection.vm"
argument_list|)
expr_stmt|;
comment|// generate ApiName
name|mergeTemplate
argument_list|(
name|getApiContext
argument_list|()
argument_list|,
name|getApiNameFile
argument_list|()
argument_list|,
literal|"/api-name-enum.vm"
argument_list|)
expr_stmt|;
block|}
DECL|method|getApiContext ()
specifier|private
name|VelocityContext
name|getApiContext
parameter_list|()
block|{
specifier|final
name|VelocityContext
name|context
init|=
operator|new
name|VelocityContext
argument_list|()
decl_stmt|;
name|context
operator|.
name|put
argument_list|(
literal|"componentName"
argument_list|,
name|componentName
argument_list|)
expr_stmt|;
name|context
operator|.
name|put
argument_list|(
literal|"componentPackage"
argument_list|,
name|componentPackage
argument_list|)
expr_stmt|;
name|context
operator|.
name|put
argument_list|(
literal|"apis"
argument_list|,
name|apis
argument_list|)
expr_stmt|;
name|context
operator|.
name|put
argument_list|(
literal|"helper"
argument_list|,
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|put
argument_list|(
literal|"collectionName"
argument_list|,
name|getApiCollectionName
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|put
argument_list|(
literal|"apiNameEnum"
argument_list|,
name|getApiNameEnum
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|context
return|;
block|}
DECL|method|getApiCollectionName ()
specifier|private
name|String
name|getApiCollectionName
parameter_list|()
block|{
return|return
name|componentName
operator|+
literal|"ApiCollection"
return|;
block|}
DECL|method|getApiNameEnum ()
specifier|private
name|String
name|getApiNameEnum
parameter_list|()
block|{
return|return
name|componentName
operator|+
literal|"ApiName"
return|;
block|}
DECL|method|getApiCollectionFile ()
specifier|private
name|File
name|getApiCollectionFile
parameter_list|()
block|{
specifier|final
name|StringBuilder
name|fileName
init|=
name|getFileBuilder
argument_list|()
decl_stmt|;
name|fileName
operator|.
name|append
argument_list|(
name|getApiCollectionName
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|".java"
argument_list|)
expr_stmt|;
return|return
operator|new
name|File
argument_list|(
name|generatedSrcDir
argument_list|,
name|fileName
operator|.
name|toString
argument_list|()
argument_list|)
return|;
block|}
DECL|method|getApiNameFile ()
specifier|private
name|File
name|getApiNameFile
parameter_list|()
block|{
specifier|final
name|StringBuilder
name|fileName
init|=
name|getFileBuilder
argument_list|()
decl_stmt|;
name|fileName
operator|.
name|append
argument_list|(
name|getApiNameEnum
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|".java"
argument_list|)
expr_stmt|;
return|return
operator|new
name|File
argument_list|(
name|generatedSrcDir
argument_list|,
name|fileName
operator|.
name|toString
argument_list|()
argument_list|)
return|;
block|}
DECL|method|getFileBuilder ()
specifier|private
name|StringBuilder
name|getFileBuilder
parameter_list|()
block|{
specifier|final
name|StringBuilder
name|fileName
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|fileName
operator|.
name|append
argument_list|(
name|outPackage
operator|.
name|replaceAll
argument_list|(
literal|"\\."
argument_list|,
name|File
operator|.
name|separator
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
name|File
operator|.
name|separator
argument_list|)
expr_stmt|;
return|return
name|fileName
return|;
block|}
DECL|method|getApiMethod (String proxyClass)
specifier|public
specifier|static
name|String
name|getApiMethod
parameter_list|(
name|String
name|proxyClass
parameter_list|)
block|{
return|return
name|proxyClass
operator|.
name|substring
argument_list|(
name|proxyClass
operator|.
name|lastIndexOf
argument_list|(
literal|'.'
argument_list|)
operator|+
literal|1
argument_list|)
operator|+
literal|"ApiMethod"
return|;
block|}
DECL|method|getEndpointConfig (String proxyClass)
specifier|public
specifier|static
name|String
name|getEndpointConfig
parameter_list|(
name|String
name|proxyClass
parameter_list|)
block|{
return|return
name|proxyClass
operator|.
name|substring
argument_list|(
name|proxyClass
operator|.
name|lastIndexOf
argument_list|(
literal|'.'
argument_list|)
operator|+
literal|1
argument_list|)
operator|+
literal|"EndpointConfiguration"
return|;
block|}
DECL|method|getEnumConstant (String enumValue)
specifier|public
specifier|static
name|String
name|getEnumConstant
parameter_list|(
name|String
name|enumValue
parameter_list|)
block|{
if|if
condition|(
name|enumValue
operator|==
literal|null
operator|||
name|enumValue
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|"DEFAULT"
return|;
block|}
name|StringBuilder
name|builder
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|Character
operator|.
name|isJavaIdentifierStart
argument_list|(
name|enumValue
operator|.
name|charAt
argument_list|(
literal|0
argument_list|)
argument_list|)
condition|)
block|{
name|builder
operator|.
name|append
argument_list|(
literal|'_'
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|char
name|c
range|:
name|enumValue
operator|.
name|toCharArray
argument_list|()
control|)
block|{
name|char
name|upperCase
init|=
name|Character
operator|.
name|toUpperCase
argument_list|(
name|c
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|Character
operator|.
name|isJavaIdentifierPart
argument_list|(
name|upperCase
argument_list|)
condition|)
block|{
name|builder
operator|.
name|append
argument_list|(
literal|'_'
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|builder
operator|.
name|append
argument_list|(
name|upperCase
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|builder
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

