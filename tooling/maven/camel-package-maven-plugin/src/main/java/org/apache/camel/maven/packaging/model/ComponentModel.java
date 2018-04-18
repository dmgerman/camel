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
DECL|class|ComponentModel
specifier|public
class|class
name|ComponentModel
block|{
DECL|field|coreOnly
specifier|private
specifier|final
name|boolean
name|coreOnly
decl_stmt|;
DECL|field|kind
specifier|private
name|String
name|kind
decl_stmt|;
DECL|field|scheme
specifier|private
name|String
name|scheme
decl_stmt|;
DECL|field|syntax
specifier|private
name|String
name|syntax
decl_stmt|;
DECL|field|alternativeSyntax
specifier|private
name|String
name|alternativeSyntax
decl_stmt|;
DECL|field|alternativeSchemes
specifier|private
name|String
name|alternativeSchemes
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
DECL|field|deprecationNote
specifier|private
name|String
name|deprecationNote
decl_stmt|;
DECL|field|consumerOnly
specifier|private
name|String
name|consumerOnly
decl_stmt|;
DECL|field|producerOnly
specifier|private
name|String
name|producerOnly
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
DECL|field|componentOptions
specifier|private
specifier|final
name|List
argument_list|<
name|ComponentOptionModel
argument_list|>
name|componentOptions
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|endpointPathOptions
specifier|private
specifier|final
name|List
argument_list|<
name|EndpointOptionModel
argument_list|>
name|endpointPathOptions
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|endpointOptions
specifier|private
specifier|final
name|List
argument_list|<
name|EndpointOptionModel
argument_list|>
name|endpointOptions
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
DECL|method|ComponentModel (boolean coreOnly)
specifier|public
name|ComponentModel
parameter_list|(
name|boolean
name|coreOnly
parameter_list|)
block|{
name|this
operator|.
name|coreOnly
operator|=
name|coreOnly
expr_stmt|;
block|}
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
DECL|method|getScheme ()
specifier|public
name|String
name|getScheme
parameter_list|()
block|{
return|return
name|scheme
return|;
block|}
DECL|method|setScheme (String scheme)
specifier|public
name|void
name|setScheme
parameter_list|(
name|String
name|scheme
parameter_list|)
block|{
name|this
operator|.
name|scheme
operator|=
name|scheme
expr_stmt|;
block|}
DECL|method|getSyntax ()
specifier|public
name|String
name|getSyntax
parameter_list|()
block|{
return|return
name|syntax
return|;
block|}
DECL|method|setSyntax (String syntax)
specifier|public
name|void
name|setSyntax
parameter_list|(
name|String
name|syntax
parameter_list|)
block|{
name|this
operator|.
name|syntax
operator|=
name|syntax
expr_stmt|;
block|}
DECL|method|getAlternativeSyntax ()
specifier|public
name|String
name|getAlternativeSyntax
parameter_list|()
block|{
return|return
name|alternativeSyntax
return|;
block|}
DECL|method|setAlternativeSyntax (String alternativeSyntax)
specifier|public
name|void
name|setAlternativeSyntax
parameter_list|(
name|String
name|alternativeSyntax
parameter_list|)
block|{
name|this
operator|.
name|alternativeSyntax
operator|=
name|alternativeSyntax
expr_stmt|;
block|}
DECL|method|getAlternativeSchemes ()
specifier|public
name|String
name|getAlternativeSchemes
parameter_list|()
block|{
return|return
name|alternativeSchemes
return|;
block|}
DECL|method|setAlternativeSchemes (String alternativeSchemes)
specifier|public
name|void
name|setAlternativeSchemes
parameter_list|(
name|String
name|alternativeSchemes
parameter_list|)
block|{
name|this
operator|.
name|alternativeSchemes
operator|=
name|alternativeSchemes
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
DECL|method|getDeprecationNote ()
specifier|public
name|String
name|getDeprecationNote
parameter_list|()
block|{
return|return
name|deprecationNote
return|;
block|}
DECL|method|setDeprecationNote (String deprecationNote)
specifier|public
name|void
name|setDeprecationNote
parameter_list|(
name|String
name|deprecationNote
parameter_list|)
block|{
name|this
operator|.
name|deprecationNote
operator|=
name|deprecationNote
expr_stmt|;
block|}
DECL|method|getConsumerOnly ()
specifier|public
name|String
name|getConsumerOnly
parameter_list|()
block|{
return|return
name|consumerOnly
return|;
block|}
DECL|method|setConsumerOnly (String consumerOnly)
specifier|public
name|void
name|setConsumerOnly
parameter_list|(
name|String
name|consumerOnly
parameter_list|)
block|{
name|this
operator|.
name|consumerOnly
operator|=
name|consumerOnly
expr_stmt|;
block|}
DECL|method|getProducerOnly ()
specifier|public
name|String
name|getProducerOnly
parameter_list|()
block|{
return|return
name|producerOnly
return|;
block|}
DECL|method|setProducerOnly (String producerOnly)
specifier|public
name|void
name|setProducerOnly
parameter_list|(
name|String
name|producerOnly
parameter_list|)
block|{
name|this
operator|.
name|producerOnly
operator|=
name|producerOnly
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
DECL|method|getComponentOptions ()
specifier|public
name|List
argument_list|<
name|ComponentOptionModel
argument_list|>
name|getComponentOptions
parameter_list|()
block|{
return|return
name|componentOptions
return|;
block|}
DECL|method|addComponentOption (ComponentOptionModel option)
specifier|public
name|void
name|addComponentOption
parameter_list|(
name|ComponentOptionModel
name|option
parameter_list|)
block|{
name|componentOptions
operator|.
name|add
argument_list|(
name|option
argument_list|)
expr_stmt|;
block|}
DECL|method|getEndpointOptions ()
specifier|public
name|List
argument_list|<
name|EndpointOptionModel
argument_list|>
name|getEndpointOptions
parameter_list|()
block|{
return|return
name|endpointOptions
return|;
block|}
DECL|method|getEndpointPathOptions ()
specifier|public
name|List
argument_list|<
name|EndpointOptionModel
argument_list|>
name|getEndpointPathOptions
parameter_list|()
block|{
return|return
name|endpointPathOptions
return|;
block|}
DECL|method|addEndpointOption (EndpointOptionModel option)
specifier|public
name|void
name|addEndpointOption
parameter_list|(
name|EndpointOptionModel
name|option
parameter_list|)
block|{
name|endpointOptions
operator|.
name|add
argument_list|(
name|option
argument_list|)
expr_stmt|;
block|}
DECL|method|addEndpointPathOption (EndpointOptionModel option)
specifier|public
name|void
name|addEndpointPathOption
parameter_list|(
name|EndpointOptionModel
name|option
parameter_list|)
block|{
name|endpointPathOptions
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
comment|// special for these components
if|if
condition|(
literal|"camel-box"
operator|.
name|equals
argument_list|(
name|artifactId
argument_list|)
condition|)
block|{
return|return
literal|"camel-box/camel-box-component/src/main/docs"
return|;
block|}
elseif|else
if|if
condition|(
literal|"camel-linkedin"
operator|.
name|equals
argument_list|(
name|artifactId
argument_list|)
condition|)
block|{
return|return
literal|"camel-linkedin/camel-linkedin-component/src/main/docs"
return|;
block|}
elseif|else
if|if
condition|(
literal|"camel-olingo2"
operator|.
name|equals
argument_list|(
name|artifactId
argument_list|)
condition|)
block|{
return|return
literal|"camel-olingo2/camel-olingo2-component/src/main/docs"
return|;
block|}
elseif|else
if|if
condition|(
literal|"camel-olingo4"
operator|.
name|equals
argument_list|(
name|artifactId
argument_list|)
condition|)
block|{
return|return
literal|"camel-olingo4/camel-olingo4-component/src/main/docs"
return|;
block|}
elseif|else
if|if
condition|(
literal|"camel-salesforce"
operator|.
name|equals
argument_list|(
name|artifactId
argument_list|)
condition|)
block|{
return|return
literal|"camel-salesforce/camel-salesforce-component/src/main/docs"
return|;
block|}
elseif|else
if|if
condition|(
literal|"camel-servicenow"
operator|.
name|equals
argument_list|(
name|artifactId
argument_list|)
condition|)
block|{
return|return
literal|"camel-servicenow/camel-servicenow-component/src/main/docs"
return|;
block|}
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
name|coreOnly
condition|?
literal|"src/main/docs"
else|:
literal|"../camel-core/src/main/docs"
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

