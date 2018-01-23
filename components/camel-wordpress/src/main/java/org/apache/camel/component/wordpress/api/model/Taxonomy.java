begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.wordpress.api.model
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|wordpress
operator|.
name|api
operator|.
name|model
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Serializable
import|;
end_import

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
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|annotation
operator|.
name|JsonIgnoreProperties
import|;
end_import

begin_import
import|import
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|annotation
operator|.
name|JsonProperty
import|;
end_import

begin_class
annotation|@
name|JsonIgnoreProperties
argument_list|(
name|ignoreUnknown
operator|=
literal|true
argument_list|)
DECL|class|Taxonomy
specifier|public
class|class
name|Taxonomy
implements|implements
name|Serializable
block|{
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|390452251497218257L
decl_stmt|;
DECL|field|name
specifier|private
name|String
name|name
decl_stmt|;
DECL|field|slug
specifier|private
name|String
name|slug
decl_stmt|;
DECL|field|description
specifier|private
name|String
name|description
decl_stmt|;
DECL|field|hierarchical
specifier|private
name|boolean
name|hierarchical
decl_stmt|;
annotation|@
name|JsonProperty
argument_list|(
literal|"rest_base"
argument_list|)
DECL|field|restBase
specifier|private
name|String
name|restBase
decl_stmt|;
annotation|@
name|JsonProperty
argument_list|(
literal|"show_cloud"
argument_list|)
DECL|field|showCloud
specifier|private
name|boolean
name|showCloud
decl_stmt|;
DECL|field|capabilities
specifier|private
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
name|capabilities
decl_stmt|;
DECL|field|labels
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|labels
decl_stmt|;
DECL|field|types
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|types
decl_stmt|;
DECL|method|Taxonomy ()
specifier|public
name|Taxonomy
parameter_list|()
block|{
name|this
operator|.
name|capabilities
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
name|this
operator|.
name|labels
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
name|this
operator|.
name|types
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
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
DECL|method|getSlug ()
specifier|public
name|String
name|getSlug
parameter_list|()
block|{
return|return
name|slug
return|;
block|}
DECL|method|setSlug (String slug)
specifier|public
name|void
name|setSlug
parameter_list|(
name|String
name|slug
parameter_list|)
block|{
name|this
operator|.
name|slug
operator|=
name|slug
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
DECL|method|isHierarchical ()
specifier|public
name|boolean
name|isHierarchical
parameter_list|()
block|{
return|return
name|hierarchical
return|;
block|}
DECL|method|setHierarchical (boolean hierarchical)
specifier|public
name|void
name|setHierarchical
parameter_list|(
name|boolean
name|hierarchical
parameter_list|)
block|{
name|this
operator|.
name|hierarchical
operator|=
name|hierarchical
expr_stmt|;
block|}
DECL|method|getRestBase ()
specifier|public
name|String
name|getRestBase
parameter_list|()
block|{
return|return
name|restBase
return|;
block|}
DECL|method|setRestBase (String restBase)
specifier|public
name|void
name|setRestBase
parameter_list|(
name|String
name|restBase
parameter_list|)
block|{
name|this
operator|.
name|restBase
operator|=
name|restBase
expr_stmt|;
block|}
DECL|method|isShowCloud ()
specifier|public
name|boolean
name|isShowCloud
parameter_list|()
block|{
return|return
name|showCloud
return|;
block|}
DECL|method|setShowCloud (boolean showCloud)
specifier|public
name|void
name|setShowCloud
parameter_list|(
name|boolean
name|showCloud
parameter_list|)
block|{
name|this
operator|.
name|showCloud
operator|=
name|showCloud
expr_stmt|;
block|}
DECL|method|getCapabilities ()
specifier|public
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
name|getCapabilities
parameter_list|()
block|{
return|return
name|capabilities
return|;
block|}
DECL|method|setCapabilities (List<Map<String, String>> capabilities)
specifier|public
name|void
name|setCapabilities
parameter_list|(
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
name|capabilities
parameter_list|)
block|{
name|this
operator|.
name|capabilities
operator|=
name|capabilities
expr_stmt|;
block|}
DECL|method|getLabels ()
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getLabels
parameter_list|()
block|{
return|return
name|labels
return|;
block|}
DECL|method|setLabels (List<String> labels)
specifier|public
name|void
name|setLabels
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|labels
parameter_list|)
block|{
name|this
operator|.
name|labels
operator|=
name|labels
expr_stmt|;
block|}
DECL|method|getTypes ()
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getTypes
parameter_list|()
block|{
return|return
name|types
return|;
block|}
DECL|method|setTypes (List<String> types)
specifier|public
name|void
name|setTypes
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|types
parameter_list|)
block|{
name|this
operator|.
name|types
operator|=
name|types
expr_stmt|;
block|}
block|}
end_class

end_unit

