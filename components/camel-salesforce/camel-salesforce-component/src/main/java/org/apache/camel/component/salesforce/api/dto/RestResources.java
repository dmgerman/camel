begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.salesforce.api.dto
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|salesforce
operator|.
name|api
operator|.
name|dto
package|;
end_package

begin_import
import|import
name|com
operator|.
name|thoughtworks
operator|.
name|xstream
operator|.
name|annotations
operator|.
name|XStreamAlias
import|;
end_import

begin_comment
comment|/**  * DTO for Salesforce Resources.  */
end_comment

begin_class
annotation|@
name|XStreamAlias
argument_list|(
literal|"urls"
argument_list|)
DECL|class|RestResources
specifier|public
class|class
name|RestResources
extends|extends
name|AbstractDTOBase
block|{
DECL|field|sobjects
specifier|private
name|String
name|sobjects
decl_stmt|;
DECL|field|identity
specifier|private
name|String
name|identity
decl_stmt|;
DECL|field|connect
specifier|private
name|String
name|connect
decl_stmt|;
DECL|field|search
specifier|private
name|String
name|search
decl_stmt|;
DECL|field|query
specifier|private
name|String
name|query
decl_stmt|;
DECL|field|chatter
specifier|private
name|String
name|chatter
decl_stmt|;
DECL|field|recent
specifier|private
name|String
name|recent
decl_stmt|;
DECL|field|tooling
specifier|private
name|String
name|tooling
decl_stmt|;
DECL|field|licensing
specifier|private
name|String
name|licensing
decl_stmt|;
DECL|field|analytics
specifier|private
name|String
name|analytics
decl_stmt|;
DECL|method|getSobjects ()
specifier|public
name|String
name|getSobjects
parameter_list|()
block|{
return|return
name|sobjects
return|;
block|}
DECL|method|setSobjects (String sobjects)
specifier|public
name|void
name|setSobjects
parameter_list|(
name|String
name|sobjects
parameter_list|)
block|{
name|this
operator|.
name|sobjects
operator|=
name|sobjects
expr_stmt|;
block|}
DECL|method|getIdentity ()
specifier|public
name|String
name|getIdentity
parameter_list|()
block|{
return|return
name|identity
return|;
block|}
DECL|method|setIdentity (String identity)
specifier|public
name|void
name|setIdentity
parameter_list|(
name|String
name|identity
parameter_list|)
block|{
name|this
operator|.
name|identity
operator|=
name|identity
expr_stmt|;
block|}
DECL|method|getConnect ()
specifier|public
name|String
name|getConnect
parameter_list|()
block|{
return|return
name|connect
return|;
block|}
DECL|method|setConnect (String connect)
specifier|public
name|void
name|setConnect
parameter_list|(
name|String
name|connect
parameter_list|)
block|{
name|this
operator|.
name|connect
operator|=
name|connect
expr_stmt|;
block|}
DECL|method|getSearch ()
specifier|public
name|String
name|getSearch
parameter_list|()
block|{
return|return
name|search
return|;
block|}
DECL|method|setSearch (String search)
specifier|public
name|void
name|setSearch
parameter_list|(
name|String
name|search
parameter_list|)
block|{
name|this
operator|.
name|search
operator|=
name|search
expr_stmt|;
block|}
DECL|method|getQuery ()
specifier|public
name|String
name|getQuery
parameter_list|()
block|{
return|return
name|query
return|;
block|}
DECL|method|setQuery (String query)
specifier|public
name|void
name|setQuery
parameter_list|(
name|String
name|query
parameter_list|)
block|{
name|this
operator|.
name|query
operator|=
name|query
expr_stmt|;
block|}
DECL|method|getChatter ()
specifier|public
name|String
name|getChatter
parameter_list|()
block|{
return|return
name|chatter
return|;
block|}
DECL|method|setChatter (String chatter)
specifier|public
name|void
name|setChatter
parameter_list|(
name|String
name|chatter
parameter_list|)
block|{
name|this
operator|.
name|chatter
operator|=
name|chatter
expr_stmt|;
block|}
DECL|method|getRecent ()
specifier|public
name|String
name|getRecent
parameter_list|()
block|{
return|return
name|recent
return|;
block|}
DECL|method|setRecent (String recent)
specifier|public
name|void
name|setRecent
parameter_list|(
name|String
name|recent
parameter_list|)
block|{
name|this
operator|.
name|recent
operator|=
name|recent
expr_stmt|;
block|}
DECL|method|getTooling ()
specifier|public
name|String
name|getTooling
parameter_list|()
block|{
return|return
name|tooling
return|;
block|}
DECL|method|setTooling (String tooling)
specifier|public
name|void
name|setTooling
parameter_list|(
name|String
name|tooling
parameter_list|)
block|{
name|this
operator|.
name|tooling
operator|=
name|tooling
expr_stmt|;
block|}
DECL|method|getLicensing ()
specifier|public
name|String
name|getLicensing
parameter_list|()
block|{
return|return
name|licensing
return|;
block|}
DECL|method|setLicensing (String licensing)
specifier|public
name|void
name|setLicensing
parameter_list|(
name|String
name|licensing
parameter_list|)
block|{
name|this
operator|.
name|licensing
operator|=
name|licensing
expr_stmt|;
block|}
DECL|method|getAnalytics ()
specifier|public
name|String
name|getAnalytics
parameter_list|()
block|{
return|return
name|analytics
return|;
block|}
DECL|method|setAnalytics (String analytics)
specifier|public
name|void
name|setAnalytics
parameter_list|(
name|String
name|analytics
parameter_list|)
block|{
name|this
operator|.
name|analytics
operator|=
name|analytics
expr_stmt|;
block|}
block|}
end_class

end_unit

