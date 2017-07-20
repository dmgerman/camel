begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.tools.apt.model
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|tools
operator|.
name|apt
operator|.
name|model
package|;
end_package

begin_class
DECL|class|ComponentModel
specifier|public
specifier|final
class|class
name|ComponentModel
block|{
DECL|field|scheme
specifier|private
name|String
name|scheme
decl_stmt|;
DECL|field|extendsScheme
specifier|private
name|String
name|extendsScheme
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
DECL|field|javaType
specifier|private
name|String
name|javaType
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
DECL|field|versionId
specifier|private
name|String
name|versionId
decl_stmt|;
DECL|field|label
specifier|private
name|String
name|label
decl_stmt|;
DECL|field|verifiers
specifier|private
name|String
name|verifiers
decl_stmt|;
DECL|field|consumerOnly
specifier|private
name|boolean
name|consumerOnly
decl_stmt|;
DECL|field|producerOnly
specifier|private
name|boolean
name|producerOnly
decl_stmt|;
DECL|field|deprecated
specifier|private
name|boolean
name|deprecated
decl_stmt|;
DECL|field|deprecationNode
specifier|private
name|String
name|deprecationNode
decl_stmt|;
DECL|field|lenientProperties
specifier|private
name|boolean
name|lenientProperties
decl_stmt|;
DECL|field|async
specifier|private
name|boolean
name|async
decl_stmt|;
DECL|method|ComponentModel (String scheme)
specifier|public
name|ComponentModel
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
DECL|method|getExtendsScheme ()
specifier|public
name|String
name|getExtendsScheme
parameter_list|()
block|{
return|return
name|extendsScheme
return|;
block|}
DECL|method|setExtendsScheme (String extendsScheme)
specifier|public
name|void
name|setExtendsScheme
parameter_list|(
name|String
name|extendsScheme
parameter_list|)
block|{
name|this
operator|.
name|extendsScheme
operator|=
name|extendsScheme
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
DECL|method|getVersionId ()
specifier|public
name|String
name|getVersionId
parameter_list|()
block|{
return|return
name|versionId
return|;
block|}
DECL|method|setVersionId (String versionId)
specifier|public
name|void
name|setVersionId
parameter_list|(
name|String
name|versionId
parameter_list|)
block|{
name|this
operator|.
name|versionId
operator|=
name|versionId
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
DECL|method|getVerifiers ()
specifier|public
name|String
name|getVerifiers
parameter_list|()
block|{
return|return
name|verifiers
return|;
block|}
DECL|method|setVerifiers (String verifiers)
specifier|public
name|void
name|setVerifiers
parameter_list|(
name|String
name|verifiers
parameter_list|)
block|{
name|this
operator|.
name|verifiers
operator|=
name|verifiers
expr_stmt|;
block|}
DECL|method|isConsumerOnly ()
specifier|public
name|boolean
name|isConsumerOnly
parameter_list|()
block|{
return|return
name|consumerOnly
return|;
block|}
DECL|method|setConsumerOnly (boolean consumerOnly)
specifier|public
name|void
name|setConsumerOnly
parameter_list|(
name|boolean
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
DECL|method|isProducerOnly ()
specifier|public
name|boolean
name|isProducerOnly
parameter_list|()
block|{
return|return
name|producerOnly
return|;
block|}
DECL|method|setProducerOnly (boolean producerOnly)
specifier|public
name|void
name|setProducerOnly
parameter_list|(
name|boolean
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
DECL|method|isDeprecated ()
specifier|public
name|boolean
name|isDeprecated
parameter_list|()
block|{
return|return
name|deprecated
return|;
block|}
DECL|method|setDeprecated (boolean deprecated)
specifier|public
name|void
name|setDeprecated
parameter_list|(
name|boolean
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
DECL|method|getDeprecationNode ()
specifier|public
name|String
name|getDeprecationNode
parameter_list|()
block|{
return|return
name|deprecationNode
return|;
block|}
DECL|method|setDeprecationNode (String deprecationNode)
specifier|public
name|void
name|setDeprecationNode
parameter_list|(
name|String
name|deprecationNode
parameter_list|)
block|{
name|this
operator|.
name|deprecationNode
operator|=
name|deprecationNode
expr_stmt|;
block|}
DECL|method|isLenientProperties ()
specifier|public
name|boolean
name|isLenientProperties
parameter_list|()
block|{
return|return
name|lenientProperties
return|;
block|}
DECL|method|setLenientProperties (boolean lenientProperties)
specifier|public
name|void
name|setLenientProperties
parameter_list|(
name|boolean
name|lenientProperties
parameter_list|)
block|{
name|this
operator|.
name|lenientProperties
operator|=
name|lenientProperties
expr_stmt|;
block|}
DECL|method|isAsync ()
specifier|public
name|boolean
name|isAsync
parameter_list|()
block|{
return|return
name|async
return|;
block|}
DECL|method|setAsync (boolean async)
specifier|public
name|void
name|setAsync
parameter_list|(
name|boolean
name|async
parameter_list|)
block|{
name|this
operator|.
name|async
operator|=
name|async
expr_stmt|;
block|}
block|}
end_class

end_unit

