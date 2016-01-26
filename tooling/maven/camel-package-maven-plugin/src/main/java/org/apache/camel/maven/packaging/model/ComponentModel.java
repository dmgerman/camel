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
name|List
import|;
end_import

begin_class
DECL|class|ComponentModel
specifier|public
class|class
name|ComponentModel
block|{
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
name|List
argument_list|<
name|ComponentOptionModel
argument_list|>
name|componentOptions
decl_stmt|;
DECL|field|endpointOptions
specifier|private
name|List
argument_list|<
name|EndpointOptionModel
argument_list|>
name|endpointOptions
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
DECL|method|setComponentOptions (List<ComponentOptionModel> componentOptions)
specifier|public
name|void
name|setComponentOptions
parameter_list|(
name|List
argument_list|<
name|ComponentOptionModel
argument_list|>
name|componentOptions
parameter_list|)
block|{
name|this
operator|.
name|componentOptions
operator|=
name|componentOptions
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
DECL|method|setEndpointOptions (List<EndpointOptionModel> endpointOptions)
specifier|public
name|void
name|setEndpointOptions
parameter_list|(
name|List
argument_list|<
name|EndpointOptionModel
argument_list|>
name|endpointOptions
parameter_list|)
block|{
name|this
operator|.
name|endpointOptions
operator|=
name|endpointOptions
expr_stmt|;
block|}
block|}
end_class

end_unit

