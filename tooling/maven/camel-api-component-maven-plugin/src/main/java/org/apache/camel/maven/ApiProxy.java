begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Collections
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

begin_comment
comment|/**  * Represents an API to use for generating Camel Component.  */
end_comment

begin_class
DECL|class|ApiProxy
specifier|public
class|class
name|ApiProxy
block|{
DECL|field|apiName
specifier|private
name|String
name|apiName
decl_stmt|;
DECL|field|proxyClass
specifier|private
name|String
name|proxyClass
decl_stmt|;
DECL|field|fromSignatureFile
specifier|private
name|File
name|fromSignatureFile
decl_stmt|;
DECL|field|fromJavadoc
specifier|private
name|FromJavadoc
name|fromJavadoc
decl_stmt|;
DECL|field|substitutions
specifier|private
name|Substitution
index|[]
name|substitutions
init|=
operator|new
name|Substitution
index|[
literal|0
index|]
decl_stmt|;
DECL|field|excludeConfigNames
specifier|private
name|String
name|excludeConfigNames
decl_stmt|;
DECL|field|excludeConfigTypes
specifier|private
name|String
name|excludeConfigTypes
decl_stmt|;
DECL|field|extraOptions
specifier|private
name|ExtraOption
index|[]
name|extraOptions
decl_stmt|;
DECL|field|nullableOptions
specifier|private
name|String
index|[]
name|nullableOptions
decl_stmt|;
DECL|field|aliases
specifier|private
name|List
argument_list|<
name|ApiMethodAlias
argument_list|>
name|aliases
init|=
name|Collections
operator|.
name|emptyList
argument_list|()
decl_stmt|;
DECL|method|getApiName ()
specifier|public
name|String
name|getApiName
parameter_list|()
block|{
return|return
name|apiName
return|;
block|}
DECL|method|setApiName (String apiName)
specifier|public
name|void
name|setApiName
parameter_list|(
name|String
name|apiName
parameter_list|)
block|{
name|this
operator|.
name|apiName
operator|=
name|apiName
expr_stmt|;
block|}
DECL|method|getProxyClass ()
specifier|public
name|String
name|getProxyClass
parameter_list|()
block|{
return|return
name|proxyClass
return|;
block|}
DECL|method|setProxyClass (String proxyClass)
specifier|public
name|void
name|setProxyClass
parameter_list|(
name|String
name|proxyClass
parameter_list|)
block|{
name|this
operator|.
name|proxyClass
operator|=
name|proxyClass
expr_stmt|;
block|}
DECL|method|getFromSignatureFile ()
specifier|public
name|File
name|getFromSignatureFile
parameter_list|()
block|{
return|return
name|fromSignatureFile
return|;
block|}
DECL|method|setFromSignatureFile (File fromSignatureFile)
specifier|public
name|void
name|setFromSignatureFile
parameter_list|(
name|File
name|fromSignatureFile
parameter_list|)
block|{
name|this
operator|.
name|fromSignatureFile
operator|=
name|fromSignatureFile
expr_stmt|;
block|}
DECL|method|getFromJavadoc ()
specifier|public
name|FromJavadoc
name|getFromJavadoc
parameter_list|()
block|{
return|return
name|fromJavadoc
return|;
block|}
DECL|method|setFromJavadoc (FromJavadoc fromJavadoc)
specifier|public
name|void
name|setFromJavadoc
parameter_list|(
name|FromJavadoc
name|fromJavadoc
parameter_list|)
block|{
name|this
operator|.
name|fromJavadoc
operator|=
name|fromJavadoc
expr_stmt|;
block|}
DECL|method|getSubstitutions ()
specifier|public
name|Substitution
index|[]
name|getSubstitutions
parameter_list|()
block|{
return|return
name|substitutions
return|;
block|}
DECL|method|setSubstitutions (Substitution[] substitutions)
specifier|public
name|void
name|setSubstitutions
parameter_list|(
name|Substitution
index|[]
name|substitutions
parameter_list|)
block|{
name|this
operator|.
name|substitutions
operator|=
name|substitutions
expr_stmt|;
block|}
DECL|method|getExcludeConfigNames ()
specifier|public
name|String
name|getExcludeConfigNames
parameter_list|()
block|{
return|return
name|excludeConfigNames
return|;
block|}
DECL|method|setExcludeConfigNames (String excludeConfigNames)
specifier|public
name|void
name|setExcludeConfigNames
parameter_list|(
name|String
name|excludeConfigNames
parameter_list|)
block|{
name|this
operator|.
name|excludeConfigNames
operator|=
name|excludeConfigNames
expr_stmt|;
block|}
DECL|method|getExcludeConfigTypes ()
specifier|public
name|String
name|getExcludeConfigTypes
parameter_list|()
block|{
return|return
name|excludeConfigTypes
return|;
block|}
DECL|method|setExcludeConfigTypes (String excludeConfigTypes)
specifier|public
name|void
name|setExcludeConfigTypes
parameter_list|(
name|String
name|excludeConfigTypes
parameter_list|)
block|{
name|this
operator|.
name|excludeConfigTypes
operator|=
name|excludeConfigTypes
expr_stmt|;
block|}
DECL|method|getExtraOptions ()
specifier|public
name|ExtraOption
index|[]
name|getExtraOptions
parameter_list|()
block|{
return|return
name|extraOptions
return|;
block|}
DECL|method|setExtraOptions (ExtraOption[] extraOptions)
specifier|public
name|void
name|setExtraOptions
parameter_list|(
name|ExtraOption
index|[]
name|extraOptions
parameter_list|)
block|{
name|this
operator|.
name|extraOptions
operator|=
name|extraOptions
expr_stmt|;
block|}
DECL|method|getNullableOptions ()
specifier|public
name|String
index|[]
name|getNullableOptions
parameter_list|()
block|{
return|return
name|nullableOptions
return|;
block|}
DECL|method|setNullableOptions (String[] nullableOptions)
specifier|public
name|void
name|setNullableOptions
parameter_list|(
name|String
index|[]
name|nullableOptions
parameter_list|)
block|{
name|this
operator|.
name|nullableOptions
operator|=
name|nullableOptions
expr_stmt|;
block|}
DECL|method|getAliases ()
specifier|public
name|List
argument_list|<
name|ApiMethodAlias
argument_list|>
name|getAliases
parameter_list|()
block|{
return|return
name|aliases
return|;
block|}
DECL|method|setAliases (List<ApiMethodAlias> aliases)
specifier|public
name|void
name|setAliases
parameter_list|(
name|List
argument_list|<
name|ApiMethodAlias
argument_list|>
name|aliases
parameter_list|)
block|{
name|this
operator|.
name|aliases
operator|=
name|aliases
expr_stmt|;
block|}
DECL|method|validate ()
specifier|public
name|void
name|validate
parameter_list|()
throws|throws
name|MojoExecutionException
block|{
if|if
condition|(
name|apiName
operator|==
literal|null
operator|||
name|proxyClass
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|MojoExecutionException
argument_list|(
literal|"Properties apiName and proxyClass are required"
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

