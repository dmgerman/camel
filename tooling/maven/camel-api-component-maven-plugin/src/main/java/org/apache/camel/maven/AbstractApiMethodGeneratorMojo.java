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
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|TreeMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Matcher
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Pattern
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
name|util
operator|.
name|component
operator|.
name|ApiMethodArg
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
name|util
operator|.
name|component
operator|.
name|ApiMethodParser
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
name|util
operator|.
name|component
operator|.
name|ArgumentSubstitutionParser
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|lang
operator|.
name|ClassUtils
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
name|Parameter
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
comment|/**  * Base Mojo class for ApiMethod generators.  */
end_comment

begin_class
DECL|class|AbstractApiMethodGeneratorMojo
specifier|public
specifier|abstract
class|class
name|AbstractApiMethodGeneratorMojo
extends|extends
name|AbstractApiMethodBaseMojo
block|{
DECL|field|PRIMITIVE_VALUES
specifier|private
specifier|static
specifier|final
name|Map
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|String
argument_list|>
name|PRIMITIVE_VALUES
decl_stmt|;
annotation|@
name|Parameter
argument_list|(
name|required
operator|=
literal|true
argument_list|,
name|property
operator|=
name|PREFIX
operator|+
literal|"proxyClass"
argument_list|)
DECL|field|proxyClass
specifier|protected
name|String
name|proxyClass
decl_stmt|;
comment|// cached fields
DECL|field|proxyType
specifier|private
name|Class
argument_list|<
name|?
argument_list|>
name|proxyType
decl_stmt|;
DECL|field|propertyNamePattern
specifier|private
name|Pattern
name|propertyNamePattern
decl_stmt|;
DECL|field|propertyTypePattern
specifier|private
name|Pattern
name|propertyTypePattern
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
name|setCompileSourceRoots
argument_list|()
expr_stmt|;
comment|// load proxy class and get enumeration file to generate
specifier|final
name|Class
name|proxyType
init|=
name|getProxyType
argument_list|()
decl_stmt|;
comment|// parse pattern for excluded endpoint properties
if|if
condition|(
name|excludeConfigNames
operator|!=
literal|null
condition|)
block|{
name|propertyNamePattern
operator|=
name|Pattern
operator|.
name|compile
argument_list|(
name|excludeConfigNames
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|excludeConfigTypes
operator|!=
literal|null
condition|)
block|{
name|propertyTypePattern
operator|=
name|Pattern
operator|.
name|compile
argument_list|(
name|excludeConfigTypes
argument_list|)
expr_stmt|;
block|}
comment|// create parser
name|ApiMethodParser
name|parser
init|=
name|createAdapterParser
argument_list|(
name|proxyType
argument_list|)
decl_stmt|;
name|parser
operator|.
name|setSignatures
argument_list|(
name|getSignatureList
argument_list|()
argument_list|)
expr_stmt|;
name|parser
operator|.
name|setClassLoader
argument_list|(
name|getProjectClassLoader
argument_list|()
argument_list|)
expr_stmt|;
comment|// parse signatures
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|final
name|List
argument_list|<
name|ApiMethodParser
operator|.
name|ApiMethodModel
argument_list|>
name|models
init|=
name|parser
operator|.
name|parse
argument_list|()
decl_stmt|;
comment|// generate enumeration from model
name|mergeTemplate
argument_list|(
name|getApiMethodContext
argument_list|(
name|models
argument_list|)
argument_list|,
name|getApiMethodFile
argument_list|()
argument_list|,
literal|"/api-method-enum.vm"
argument_list|)
expr_stmt|;
comment|// generate EndpointConfiguration for this Api
name|mergeTemplate
argument_list|(
name|getEndpointContext
argument_list|(
name|models
argument_list|)
argument_list|,
name|getConfigurationFile
argument_list|()
argument_list|,
literal|"/api-endpoint-config.vm"
argument_list|)
expr_stmt|;
comment|// generate junit test if it doesn't already exist under test source directory
comment|// i.e. it may have been generated then moved there and populated with test values
specifier|final
name|String
name|testFilePath
init|=
name|getTestFilePath
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
operator|new
name|File
argument_list|(
name|project
operator|.
name|getBuild
argument_list|()
operator|.
name|getTestSourceDirectory
argument_list|()
argument_list|,
name|testFilePath
argument_list|)
operator|.
name|exists
argument_list|()
condition|)
block|{
name|mergeTemplate
argument_list|(
name|getApiTestContext
argument_list|(
name|models
argument_list|)
argument_list|,
operator|new
name|File
argument_list|(
name|generatedTestDir
argument_list|,
name|testFilePath
argument_list|)
argument_list|,
literal|"/api-route-test.vm"
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|createAdapterParser (Class proxyType)
specifier|protected
name|ApiMethodParser
name|createAdapterParser
parameter_list|(
name|Class
name|proxyType
parameter_list|)
block|{
return|return
operator|new
name|ArgumentSubstitutionParser
argument_list|(
name|proxyType
argument_list|,
name|getArgumentSubstitutions
argument_list|()
argument_list|)
return|;
block|}
DECL|method|getSignatureList ()
specifier|public
specifier|abstract
name|List
argument_list|<
name|String
argument_list|>
name|getSignatureList
parameter_list|()
throws|throws
name|MojoExecutionException
function_decl|;
DECL|method|getProxyType ()
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
name|getProxyType
parameter_list|()
throws|throws
name|MojoExecutionException
block|{
if|if
condition|(
name|proxyType
operator|==
literal|null
condition|)
block|{
comment|// load proxy class from Project runtime dependencies
try|try
block|{
name|proxyType
operator|=
name|getProjectClassLoader
argument_list|()
operator|.
name|loadClass
argument_list|(
name|proxyClass
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
name|MojoExecutionException
argument_list|(
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
return|return
name|proxyType
return|;
block|}
DECL|method|getApiMethodContext (List<ApiMethodParser.ApiMethodModel> models)
specifier|private
name|VelocityContext
name|getApiMethodContext
parameter_list|(
name|List
argument_list|<
name|ApiMethodParser
operator|.
name|ApiMethodModel
argument_list|>
name|models
parameter_list|)
throws|throws
name|MojoExecutionException
block|{
name|VelocityContext
name|context
init|=
name|getCommonContext
argument_list|(
name|models
argument_list|)
decl_stmt|;
name|context
operator|.
name|put
argument_list|(
literal|"enumName"
argument_list|,
name|getEnumName
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|context
return|;
block|}
DECL|method|getApiMethodFile ()
specifier|public
name|File
name|getApiMethodFile
parameter_list|()
throws|throws
name|MojoExecutionException
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
name|Matcher
operator|.
name|quoteReplacement
argument_list|(
name|File
operator|.
name|separator
argument_list|)
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
name|fileName
operator|.
name|append
argument_list|(
name|getEnumName
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
DECL|method|getEnumName ()
specifier|private
name|String
name|getEnumName
parameter_list|()
throws|throws
name|MojoExecutionException
block|{
name|String
name|proxyClassWithCanonicalName
init|=
name|getProxyClassWithCanonicalName
argument_list|(
name|proxyClass
argument_list|)
decl_stmt|;
return|return
name|proxyClassWithCanonicalName
operator|.
name|substring
argument_list|(
name|proxyClassWithCanonicalName
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
DECL|method|getApiTestContext (List<ApiMethodParser.ApiMethodModel> models)
specifier|private
name|VelocityContext
name|getApiTestContext
parameter_list|(
name|List
argument_list|<
name|ApiMethodParser
operator|.
name|ApiMethodModel
argument_list|>
name|models
parameter_list|)
throws|throws
name|MojoExecutionException
block|{
name|VelocityContext
name|context
init|=
name|getCommonContext
argument_list|(
name|models
argument_list|)
decl_stmt|;
name|context
operator|.
name|put
argument_list|(
literal|"testName"
argument_list|,
name|getUnitTestName
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|put
argument_list|(
literal|"scheme"
argument_list|,
name|scheme
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
literal|"componentName"
argument_list|,
name|componentName
argument_list|)
expr_stmt|;
name|context
operator|.
name|put
argument_list|(
literal|"enumName"
argument_list|,
name|getEnumName
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|context
return|;
block|}
DECL|method|getTestFilePath ()
specifier|private
name|String
name|getTestFilePath
parameter_list|()
throws|throws
name|MojoExecutionException
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
name|componentPackage
operator|.
name|replaceAll
argument_list|(
literal|"\\."
argument_list|,
name|Matcher
operator|.
name|quoteReplacement
argument_list|(
name|File
operator|.
name|separator
argument_list|)
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
name|fileName
operator|.
name|append
argument_list|(
name|getUnitTestName
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|".java"
argument_list|)
expr_stmt|;
return|return
name|fileName
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|method|getUnitTestName ()
specifier|private
name|String
name|getUnitTestName
parameter_list|()
throws|throws
name|MojoExecutionException
block|{
name|String
name|proxyClassWithCanonicalName
init|=
name|getProxyClassWithCanonicalName
argument_list|(
name|proxyClass
argument_list|)
decl_stmt|;
return|return
name|proxyClassWithCanonicalName
operator|.
name|substring
argument_list|(
name|proxyClassWithCanonicalName
operator|.
name|lastIndexOf
argument_list|(
literal|'.'
argument_list|)
operator|+
literal|1
argument_list|)
operator|+
literal|"IntegrationTest"
return|;
block|}
DECL|method|getEndpointContext (List<ApiMethodParser.ApiMethodModel> models)
specifier|private
name|VelocityContext
name|getEndpointContext
parameter_list|(
name|List
argument_list|<
name|ApiMethodParser
operator|.
name|ApiMethodModel
argument_list|>
name|models
parameter_list|)
throws|throws
name|MojoExecutionException
block|{
name|VelocityContext
name|context
init|=
name|getCommonContext
argument_list|(
name|models
argument_list|)
decl_stmt|;
name|context
operator|.
name|put
argument_list|(
literal|"configName"
argument_list|,
name|getConfigName
argument_list|()
argument_list|)
expr_stmt|;
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
comment|// generate parameter names and types for configuration, sorted by parameter name
name|Map
argument_list|<
name|String
argument_list|,
name|ApiMethodArg
argument_list|>
name|parameters
init|=
operator|new
name|TreeMap
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|ApiMethodParser
operator|.
name|ApiMethodModel
name|model
range|:
name|models
control|)
block|{
for|for
control|(
name|ApiMethodArg
name|argument
range|:
name|model
operator|.
name|getArguments
argument_list|()
control|)
block|{
specifier|final
name|String
name|name
init|=
name|argument
operator|.
name|getName
argument_list|()
decl_stmt|;
specifier|final
name|Class
argument_list|<
name|?
argument_list|>
name|type
init|=
name|argument
operator|.
name|getType
argument_list|()
decl_stmt|;
specifier|final
name|String
name|typeName
init|=
name|type
operator|.
name|getCanonicalName
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|parameters
operator|.
name|containsKey
argument_list|(
name|name
argument_list|)
operator|&&
operator|(
name|propertyNamePattern
operator|==
literal|null
operator|||
operator|!
name|propertyNamePattern
operator|.
name|matcher
argument_list|(
name|name
argument_list|)
operator|.
name|matches
argument_list|()
operator|)
operator|&&
operator|(
name|propertyTypePattern
operator|==
literal|null
operator|||
operator|!
name|propertyTypePattern
operator|.
name|matcher
argument_list|(
name|typeName
argument_list|)
operator|.
name|matches
argument_list|()
operator|)
condition|)
block|{
name|parameters
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|argument
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|// add custom parameters
if|if
condition|(
name|extraOptions
operator|!=
literal|null
operator|&&
name|extraOptions
operator|.
name|length
operator|>
literal|0
condition|)
block|{
for|for
control|(
name|ExtraOption
name|option
range|:
name|extraOptions
control|)
block|{
specifier|final
name|String
name|name
init|=
name|option
operator|.
name|getName
argument_list|()
decl_stmt|;
specifier|final
name|String
name|argWithTypes
init|=
name|option
operator|.
name|getType
argument_list|()
operator|.
name|replaceAll
argument_list|(
literal|" "
argument_list|,
literal|""
argument_list|)
decl_stmt|;
specifier|final
name|int
name|rawEnd
init|=
name|argWithTypes
operator|.
name|indexOf
argument_list|(
literal|'<'
argument_list|)
decl_stmt|;
name|String
name|typeArgs
init|=
literal|null
decl_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|argType
decl_stmt|;
try|try
block|{
if|if
condition|(
name|rawEnd
operator|!=
operator|-
literal|1
condition|)
block|{
name|argType
operator|=
name|getProjectClassLoader
argument_list|()
operator|.
name|loadClass
argument_list|(
name|argWithTypes
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|rawEnd
argument_list|)
argument_list|)
expr_stmt|;
name|typeArgs
operator|=
name|argWithTypes
operator|.
name|substring
argument_list|(
name|rawEnd
operator|+
literal|1
argument_list|,
name|argWithTypes
operator|.
name|lastIndexOf
argument_list|(
literal|'>'
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|argType
operator|=
name|getProjectClassLoader
argument_list|()
operator|.
name|loadClass
argument_list|(
name|argWithTypes
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|ClassNotFoundException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|MojoExecutionException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Error loading extra option [%s %s] : %s"
argument_list|,
name|argWithTypes
argument_list|,
name|name
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
argument_list|,
name|e
argument_list|)
throw|;
block|}
name|parameters
operator|.
name|put
argument_list|(
name|name
argument_list|,
operator|new
name|ApiMethodArg
argument_list|(
name|name
argument_list|,
name|argType
argument_list|,
name|typeArgs
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
name|context
operator|.
name|put
argument_list|(
literal|"parameters"
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
return|return
name|context
return|;
block|}
DECL|method|getConfigurationFile ()
specifier|private
name|File
name|getConfigurationFile
parameter_list|()
throws|throws
name|MojoExecutionException
block|{
specifier|final
name|StringBuilder
name|fileName
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
comment|// endpoint configuration goes in component package
name|fileName
operator|.
name|append
argument_list|(
name|componentPackage
operator|.
name|replaceAll
argument_list|(
literal|"\\."
argument_list|,
name|Matcher
operator|.
name|quoteReplacement
argument_list|(
name|File
operator|.
name|separator
argument_list|)
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
name|fileName
operator|.
name|append
argument_list|(
name|getConfigName
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
DECL|method|getConfigName ()
specifier|private
name|String
name|getConfigName
parameter_list|()
throws|throws
name|MojoExecutionException
block|{
name|String
name|proxyClassWithCanonicalName
init|=
name|getProxyClassWithCanonicalName
argument_list|(
name|proxyClass
argument_list|)
decl_stmt|;
return|return
name|proxyClassWithCanonicalName
operator|.
name|substring
argument_list|(
name|proxyClassWithCanonicalName
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
DECL|method|getProxyClassWithCanonicalName (String proxyClass)
specifier|private
name|String
name|getProxyClassWithCanonicalName
parameter_list|(
name|String
name|proxyClass
parameter_list|)
block|{
return|return
name|proxyClass
operator|.
name|replace
argument_list|(
literal|"$"
argument_list|,
literal|""
argument_list|)
return|;
block|}
DECL|method|getCommonContext (List<ApiMethodParser.ApiMethodModel> models)
specifier|private
name|VelocityContext
name|getCommonContext
parameter_list|(
name|List
argument_list|<
name|ApiMethodParser
operator|.
name|ApiMethodModel
argument_list|>
name|models
parameter_list|)
throws|throws
name|MojoExecutionException
block|{
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
literal|"models"
argument_list|,
name|models
argument_list|)
expr_stmt|;
name|context
operator|.
name|put
argument_list|(
literal|"proxyType"
argument_list|,
name|getProxyType
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|put
argument_list|(
literal|"helper"
argument_list|,
name|this
argument_list|)
expr_stmt|;
return|return
name|context
return|;
block|}
DECL|method|getArgumentSubstitutions ()
specifier|public
name|ArgumentSubstitutionParser
operator|.
name|Substitution
index|[]
name|getArgumentSubstitutions
parameter_list|()
block|{
name|ArgumentSubstitutionParser
operator|.
name|Substitution
index|[]
name|subs
init|=
operator|new
name|ArgumentSubstitutionParser
operator|.
name|Substitution
index|[
name|substitutions
operator|.
name|length
index|]
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|substitutions
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
specifier|final
name|Substitution
name|substitution
init|=
name|substitutions
index|[
name|i
index|]
decl_stmt|;
name|subs
index|[
name|i
index|]
operator|=
operator|new
name|ArgumentSubstitutionParser
operator|.
name|Substitution
argument_list|(
name|substitution
operator|.
name|getMethod
argument_list|()
argument_list|,
name|substitution
operator|.
name|getArgName
argument_list|()
argument_list|,
name|substitution
operator|.
name|getArgType
argument_list|()
argument_list|,
name|substitution
operator|.
name|getReplacement
argument_list|()
argument_list|,
name|substitution
operator|.
name|isReplaceWithType
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|subs
return|;
block|}
DECL|method|getType (Class<?> clazz)
specifier|public
specifier|static
name|String
name|getType
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
parameter_list|)
block|{
if|if
condition|(
name|clazz
operator|.
name|isArray
argument_list|()
condition|)
block|{
comment|// create a zero length array and get the class from the instance
return|return
literal|"new "
operator|+
name|getCanonicalName
argument_list|(
name|clazz
argument_list|)
operator|.
name|replaceAll
argument_list|(
literal|"\\[\\]"
argument_list|,
literal|"[0]"
argument_list|)
operator|+
literal|".getClass()"
return|;
block|}
else|else
block|{
return|return
name|getCanonicalName
argument_list|(
name|clazz
argument_list|)
operator|+
literal|".class"
return|;
block|}
block|}
DECL|method|getTestName (ApiMethodParser.ApiMethodModel model)
specifier|public
specifier|static
name|String
name|getTestName
parameter_list|(
name|ApiMethodParser
operator|.
name|ApiMethodModel
name|model
parameter_list|)
block|{
specifier|final
name|StringBuilder
name|builder
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
specifier|final
name|String
name|name
init|=
name|model
operator|.
name|getMethod
argument_list|()
operator|.
name|getName
argument_list|()
decl_stmt|;
name|builder
operator|.
name|append
argument_list|(
name|Character
operator|.
name|toUpperCase
argument_list|(
name|name
operator|.
name|charAt
argument_list|(
literal|0
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
name|name
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
comment|// find overloaded method suffix from unique name
specifier|final
name|String
name|uniqueName
init|=
name|model
operator|.
name|getUniqueName
argument_list|()
decl_stmt|;
if|if
condition|(
name|uniqueName
operator|.
name|length
argument_list|()
operator|>
name|name
operator|.
name|length
argument_list|()
condition|)
block|{
name|builder
operator|.
name|append
argument_list|(
name|uniqueName
operator|.
name|substring
argument_list|(
name|name
operator|.
name|length
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|builder
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|method|isVoidType (Class<?> resultType)
specifier|public
specifier|static
name|boolean
name|isVoidType
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|resultType
parameter_list|)
block|{
return|return
name|resultType
operator|==
name|Void
operator|.
name|TYPE
return|;
block|}
DECL|method|getExchangePropertyPrefix ()
specifier|public
name|String
name|getExchangePropertyPrefix
parameter_list|()
block|{
comment|// exchange property prefix
return|return
literal|"Camel"
operator|+
name|componentName
operator|+
literal|"."
return|;
block|}
DECL|method|getResultDeclaration (Class<?> resultType)
specifier|public
specifier|static
name|String
name|getResultDeclaration
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|resultType
parameter_list|)
block|{
if|if
condition|(
name|resultType
operator|.
name|isPrimitive
argument_list|()
condition|)
block|{
return|return
name|ClassUtils
operator|.
name|primitiveToWrapper
argument_list|(
name|resultType
argument_list|)
operator|.
name|getSimpleName
argument_list|()
return|;
block|}
else|else
block|{
return|return
name|getCanonicalName
argument_list|(
name|resultType
argument_list|)
return|;
block|}
block|}
static|static
block|{
name|PRIMITIVE_VALUES
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
name|PRIMITIVE_VALUES
operator|.
name|put
argument_list|(
name|Boolean
operator|.
name|TYPE
argument_list|,
literal|"Boolean.FALSE"
argument_list|)
expr_stmt|;
name|PRIMITIVE_VALUES
operator|.
name|put
argument_list|(
name|Byte
operator|.
name|TYPE
argument_list|,
literal|"(byte) 0"
argument_list|)
expr_stmt|;
name|PRIMITIVE_VALUES
operator|.
name|put
argument_list|(
name|Character
operator|.
name|TYPE
argument_list|,
literal|"(char) 0"
argument_list|)
expr_stmt|;
name|PRIMITIVE_VALUES
operator|.
name|put
argument_list|(
name|Short
operator|.
name|TYPE
argument_list|,
literal|"(short) 0"
argument_list|)
expr_stmt|;
name|PRIMITIVE_VALUES
operator|.
name|put
argument_list|(
name|Integer
operator|.
name|TYPE
argument_list|,
literal|"0"
argument_list|)
expr_stmt|;
name|PRIMITIVE_VALUES
operator|.
name|put
argument_list|(
name|Long
operator|.
name|TYPE
argument_list|,
literal|"0L"
argument_list|)
expr_stmt|;
name|PRIMITIVE_VALUES
operator|.
name|put
argument_list|(
name|Float
operator|.
name|TYPE
argument_list|,
literal|"0.0f"
argument_list|)
expr_stmt|;
name|PRIMITIVE_VALUES
operator|.
name|put
argument_list|(
name|Double
operator|.
name|TYPE
argument_list|,
literal|"0.0d"
argument_list|)
expr_stmt|;
block|}
DECL|method|getDefaultArgValue (Class<?> aClass)
specifier|public
specifier|static
name|String
name|getDefaultArgValue
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|aClass
parameter_list|)
block|{
if|if
condition|(
name|aClass
operator|.
name|isPrimitive
argument_list|()
condition|)
block|{
comment|// lookup default primitive value string
return|return
name|PRIMITIVE_VALUES
operator|.
name|get
argument_list|(
name|aClass
argument_list|)
return|;
block|}
else|else
block|{
comment|// return type cast null string
return|return
literal|"null"
return|;
block|}
block|}
DECL|method|getBeanPropertySuffix (String parameter)
specifier|public
specifier|static
name|String
name|getBeanPropertySuffix
parameter_list|(
name|String
name|parameter
parameter_list|)
block|{
comment|// capitalize first character
name|StringBuilder
name|builder
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|builder
operator|.
name|append
argument_list|(
name|Character
operator|.
name|toUpperCase
argument_list|(
name|parameter
operator|.
name|charAt
argument_list|(
literal|0
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
name|parameter
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|builder
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|method|getCanonicalName (ApiMethodArg argument)
specifier|public
name|String
name|getCanonicalName
parameter_list|(
name|ApiMethodArg
name|argument
parameter_list|)
throws|throws
name|MojoExecutionException
block|{
comment|// replace primitives with wrapper classes
specifier|final
name|Class
argument_list|<
name|?
argument_list|>
name|type
init|=
name|argument
operator|.
name|getType
argument_list|()
decl_stmt|;
if|if
condition|(
name|type
operator|.
name|isPrimitive
argument_list|()
condition|)
block|{
return|return
name|getCanonicalName
argument_list|(
name|ClassUtils
operator|.
name|primitiveToWrapper
argument_list|(
name|type
argument_list|)
argument_list|)
return|;
block|}
comment|// get default name prefix
name|String
name|canonicalName
init|=
name|getCanonicalName
argument_list|(
name|type
argument_list|)
decl_stmt|;
specifier|final
name|String
name|typeArgs
init|=
name|argument
operator|.
name|getTypeArgs
argument_list|()
decl_stmt|;
if|if
condition|(
name|typeArgs
operator|!=
literal|null
condition|)
block|{
comment|// add generic type arguments
name|StringBuilder
name|parameterizedType
init|=
operator|new
name|StringBuilder
argument_list|(
name|canonicalName
argument_list|)
decl_stmt|;
name|parameterizedType
operator|.
name|append
argument_list|(
literal|'<'
argument_list|)
expr_stmt|;
comment|// Note: its ok to split, since we don't support parsing nested type arguments
specifier|final
name|String
index|[]
name|argTypes
init|=
name|typeArgs
operator|.
name|split
argument_list|(
literal|","
argument_list|)
decl_stmt|;
name|boolean
name|ignore
init|=
literal|false
decl_stmt|;
specifier|final
name|int
name|nTypes
init|=
name|argTypes
operator|.
name|length
decl_stmt|;
name|int
name|i
init|=
literal|0
decl_stmt|;
for|for
control|(
name|String
name|argType
range|:
name|argTypes
control|)
block|{
comment|// try loading as is first
try|try
block|{
name|parameterizedType
operator|.
name|append
argument_list|(
name|getCanonicalName
argument_list|(
name|getProjectClassLoader
argument_list|()
operator|.
name|loadClass
argument_list|(
name|argType
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ClassNotFoundException
name|e
parameter_list|)
block|{
comment|// try loading with default java.lang package prefix
try|try
block|{
if|if
condition|(
name|log
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Could not load "
operator|+
name|argType
operator|+
literal|", trying to load java.lang."
operator|+
name|argType
argument_list|)
expr_stmt|;
block|}
name|parameterizedType
operator|.
name|append
argument_list|(
name|getCanonicalName
argument_list|(
name|getProjectClassLoader
argument_list|()
operator|.
name|loadClass
argument_list|(
literal|"java.lang."
operator|+
name|argType
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ClassNotFoundException
name|e1
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Ignoring type parameters<"
operator|+
name|typeArgs
operator|+
literal|"> for argument "
operator|+
name|argument
operator|.
name|getName
argument_list|()
operator|+
literal|", unable to load parametric type argument "
operator|+
name|argType
argument_list|,
name|e1
argument_list|)
expr_stmt|;
name|ignore
operator|=
literal|true
expr_stmt|;
block|}
block|}
if|if
condition|(
name|ignore
condition|)
block|{
comment|// give up
break|break;
block|}
elseif|else
if|if
condition|(
operator|++
name|i
operator|<
name|nTypes
condition|)
block|{
name|parameterizedType
operator|.
name|append
argument_list|(
literal|","
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
operator|!
name|ignore
condition|)
block|{
name|parameterizedType
operator|.
name|append
argument_list|(
literal|'>'
argument_list|)
expr_stmt|;
name|canonicalName
operator|=
name|parameterizedType
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
block|}
return|return
name|canonicalName
return|;
block|}
block|}
end_class

end_unit

