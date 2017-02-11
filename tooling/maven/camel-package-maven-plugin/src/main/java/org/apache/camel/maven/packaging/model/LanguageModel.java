begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.maven.packaging.model
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|maven
operator|.
name|packaging
operator|.
name|model
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
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
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|maven
operator|.
name|packaging
operator|.
name|StringHelper
operator|.
name|cutLastZeroDigit
import|;
end_import

begin_class
DECL|class|LanguageModel
specifier|public
class|class
name|LanguageModel
block|{
DECL|field|kind
specifier|private
name|String
name|kind
decl_stmt|;
DECL|field|name
specifier|private
name|String
name|name
decl_stmt|;
DECL|field|modelName
specifier|private
name|String
name|modelName
decl_stmt|;
DECL|field|title
specifier|private
name|String
name|title
decl_stmt|;
DECL|field|description
specifier|private
name|String
name|description
decl_stmt|;
DECL|field|firstVersion
specifier|private
name|String
name|firstVersion
decl_stmt|;
DECL|field|label
specifier|private
name|String
name|label
decl_stmt|;
DECL|field|deprecated
specifier|private
name|String
name|deprecated
decl_stmt|;
DECL|field|javaType
specifier|private
name|String
name|javaType
decl_stmt|;
DECL|field|groupId
specifier|private
name|String
name|groupId
decl_stmt|;
DECL|field|artifactId
specifier|private
name|String
name|artifactId
decl_stmt|;
DECL|field|version
specifier|private
name|String
name|version
decl_stmt|;
DECL|field|languageOptions
specifier|private
specifier|final
name|List
argument_list|<
name|LanguageOptionModel
argument_list|>
name|languageOptions
init|=
operator|new
name|ArrayList
argument_list|<
name|LanguageOptionModel
argument_list|>
argument_list|()
decl_stmt|;
DECL|method|getKind ()
specifier|public
name|String
name|getKind
parameter_list|()
block|{
return|return
name|kind
return|;
block|}
DECL|method|setKind (String kind)
specifier|public
name|void
name|setKind
parameter_list|(
name|String
name|kind
parameter_list|)
block|{
name|this
operator|.
name|kind
operator|=
name|kind
expr_stmt|;
block|}
DECL|method|getName ()
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
DECL|method|setName (String name)
specifier|public
name|void
name|setName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
DECL|method|getModelName ()
specifier|public
name|String
name|getModelName
parameter_list|()
block|{
return|return
name|modelName
return|;
block|}
DECL|method|setModelName (String modelName)
specifier|public
name|void
name|setModelName
parameter_list|(
name|String
name|modelName
parameter_list|)
block|{
name|this
operator|.
name|modelName
operator|=
name|modelName
expr_stmt|;
block|}
DECL|method|getTitle ()
specifier|public
name|String
name|getTitle
parameter_list|()
block|{
return|return
name|title
return|;
block|}
DECL|method|setTitle (String title)
specifier|public
name|void
name|setTitle
parameter_list|(
name|String
name|title
parameter_list|)
block|{
name|this
operator|.
name|title
operator|=
name|title
expr_stmt|;
block|}
DECL|method|getDescription ()
specifier|public
name|String
name|getDescription
parameter_list|()
block|{
return|return
name|description
return|;
block|}
DECL|method|setDescription (String description)
specifier|public
name|void
name|setDescription
parameter_list|(
name|String
name|description
parameter_list|)
block|{
name|this
operator|.
name|description
operator|=
name|description
expr_stmt|;
block|}
DECL|method|getFirstVersion ()
specifier|public
name|String
name|getFirstVersion
parameter_list|()
block|{
return|return
name|firstVersion
return|;
block|}
DECL|method|setFirstVersion (String firstVersion)
specifier|public
name|void
name|setFirstVersion
parameter_list|(
name|String
name|firstVersion
parameter_list|)
block|{
name|this
operator|.
name|firstVersion
operator|=
name|firstVersion
expr_stmt|;
block|}
DECL|method|getLabel ()
specifier|public
name|String
name|getLabel
parameter_list|()
block|{
return|return
name|label
return|;
block|}
DECL|method|setLabel (String label)
specifier|public
name|void
name|setLabel
parameter_list|(
name|String
name|label
parameter_list|)
block|{
name|this
operator|.
name|label
operator|=
name|label
expr_stmt|;
block|}
DECL|method|getDeprecated ()
specifier|public
name|String
name|getDeprecated
parameter_list|()
block|{
return|return
name|deprecated
return|;
block|}
DECL|method|setDeprecated (String deprecated)
specifier|public
name|void
name|setDeprecated
parameter_list|(
name|String
name|deprecated
parameter_list|)
block|{
name|this
operator|.
name|deprecated
operator|=
name|deprecated
expr_stmt|;
block|}
DECL|method|getJavaType ()
specifier|public
name|String
name|getJavaType
parameter_list|()
block|{
return|return
name|javaType
return|;
block|}
DECL|method|setJavaType (String javaType)
specifier|public
name|void
name|setJavaType
parameter_list|(
name|String
name|javaType
parameter_list|)
block|{
name|this
operator|.
name|javaType
operator|=
name|javaType
expr_stmt|;
block|}
DECL|method|getGroupId ()
specifier|public
name|String
name|getGroupId
parameter_list|()
block|{
return|return
name|groupId
return|;
block|}
DECL|method|setGroupId (String groupId)
specifier|public
name|void
name|setGroupId
parameter_list|(
name|String
name|groupId
parameter_list|)
block|{
name|this
operator|.
name|groupId
operator|=
name|groupId
expr_stmt|;
block|}
DECL|method|getArtifactId ()
specifier|public
name|String
name|getArtifactId
parameter_list|()
block|{
return|return
name|artifactId
return|;
block|}
DECL|method|setArtifactId (String artifactId)
specifier|public
name|void
name|setArtifactId
parameter_list|(
name|String
name|artifactId
parameter_list|)
block|{
name|this
operator|.
name|artifactId
operator|=
name|artifactId
expr_stmt|;
block|}
DECL|method|getVersion ()
specifier|public
name|String
name|getVersion
parameter_list|()
block|{
return|return
name|version
return|;
block|}
DECL|method|setVersion (String version)
specifier|public
name|void
name|setVersion
parameter_list|(
name|String
name|version
parameter_list|)
block|{
name|this
operator|.
name|version
operator|=
name|version
expr_stmt|;
block|}
DECL|method|getLanguageOptions ()
specifier|public
name|List
argument_list|<
name|LanguageOptionModel
argument_list|>
name|getLanguageOptions
parameter_list|()
block|{
return|return
name|languageOptions
return|;
block|}
DECL|method|addLanguageOption (LanguageOptionModel option)
specifier|public
name|void
name|addLanguageOption
parameter_list|(
name|LanguageOptionModel
name|option
parameter_list|)
block|{
name|languageOptions
operator|.
name|add
argument_list|(
name|option
argument_list|)
expr_stmt|;
block|}
DECL|method|getShortJavaType ()
specifier|public
name|String
name|getShortJavaType
parameter_list|()
block|{
if|if
condition|(
name|javaType
operator|.
name|startsWith
argument_list|(
literal|"java.util.Map"
argument_list|)
condition|)
block|{
return|return
literal|"Map"
return|;
block|}
elseif|else
if|if
condition|(
name|javaType
operator|.
name|startsWith
argument_list|(
literal|"java.util.Set"
argument_list|)
condition|)
block|{
return|return
literal|"Set"
return|;
block|}
elseif|else
if|if
condition|(
name|javaType
operator|.
name|startsWith
argument_list|(
literal|"java.util.List"
argument_list|)
condition|)
block|{
return|return
literal|"List"
return|;
block|}
name|int
name|pos
init|=
name|javaType
operator|.
name|lastIndexOf
argument_list|(
literal|"."
argument_list|)
decl_stmt|;
if|if
condition|(
name|pos
operator|!=
operator|-
literal|1
condition|)
block|{
return|return
name|javaType
operator|.
name|substring
argument_list|(
name|pos
operator|+
literal|1
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|javaType
return|;
block|}
block|}
DECL|method|getDocLink ()
specifier|public
name|String
name|getDocLink
parameter_list|()
block|{
if|if
condition|(
literal|"camel-core"
operator|.
name|equals
argument_list|(
name|artifactId
argument_list|)
condition|)
block|{
return|return
literal|"src/main/docs"
return|;
block|}
else|else
block|{
return|return
name|artifactId
operator|+
literal|"/src/main/docs"
return|;
block|}
block|}
DECL|method|getFirstVersionShort ()
specifier|public
name|String
name|getFirstVersionShort
parameter_list|()
block|{
return|return
name|cutLastZeroDigit
argument_list|(
name|firstVersion
argument_list|)
return|;
block|}
block|}
end_class

end_unit

