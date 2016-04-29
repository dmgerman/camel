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

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|jackson
operator|.
name|annotate
operator|.
name|JsonProperty
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
DECL|field|limits
specifier|private
name|String
name|limits
decl_stmt|;
DECL|field|theme
specifier|private
name|String
name|theme
decl_stmt|;
DECL|field|queryAll
specifier|private
name|String
name|queryAll
decl_stmt|;
DECL|field|knowledgeManagement
specifier|private
name|String
name|knowledgeManagement
decl_stmt|;
DECL|field|process
specifier|private
name|String
name|process
decl_stmt|;
DECL|field|flexiPage
specifier|private
name|String
name|flexiPage
decl_stmt|;
DECL|field|quickActions
specifier|private
name|String
name|quickActions
decl_stmt|;
DECL|field|appMenu
specifier|private
name|String
name|appMenu
decl_stmt|;
DECL|field|compactLayouts
specifier|private
name|String
name|compactLayouts
decl_stmt|;
DECL|field|actions
specifier|private
name|String
name|actions
decl_stmt|;
DECL|field|tabs
specifier|private
name|String
name|tabs
decl_stmt|;
DECL|field|wave
specifier|private
name|String
name|wave
decl_stmt|;
annotation|@
name|JsonProperty
argument_list|(
literal|"async-queries"
argument_list|)
annotation|@
name|XStreamAlias
argument_list|(
literal|"async-queries"
argument_list|)
DECL|field|asyncQueries
specifier|private
name|String
name|asyncQueries
decl_stmt|;
annotation|@
name|JsonProperty
argument_list|(
literal|"exchange-connect"
argument_list|)
annotation|@
name|XStreamAlias
argument_list|(
literal|"exchange-connect"
argument_list|)
DECL|field|exchangeConnect
specifier|private
name|String
name|exchangeConnect
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
DECL|method|getLimits ()
specifier|public
name|String
name|getLimits
parameter_list|()
block|{
return|return
name|limits
return|;
block|}
DECL|method|setLimits (String limits)
specifier|public
name|void
name|setLimits
parameter_list|(
name|String
name|limits
parameter_list|)
block|{
name|this
operator|.
name|limits
operator|=
name|limits
expr_stmt|;
block|}
DECL|method|getTheme ()
specifier|public
name|String
name|getTheme
parameter_list|()
block|{
return|return
name|theme
return|;
block|}
DECL|method|setTheme (String theme)
specifier|public
name|void
name|setTheme
parameter_list|(
name|String
name|theme
parameter_list|)
block|{
name|this
operator|.
name|theme
operator|=
name|theme
expr_stmt|;
block|}
DECL|method|getQueryAll ()
specifier|public
name|String
name|getQueryAll
parameter_list|()
block|{
return|return
name|queryAll
return|;
block|}
DECL|method|setQueryAll (String queryAll)
specifier|public
name|void
name|setQueryAll
parameter_list|(
name|String
name|queryAll
parameter_list|)
block|{
name|this
operator|.
name|queryAll
operator|=
name|queryAll
expr_stmt|;
block|}
DECL|method|getKnowledgeManagement ()
specifier|public
name|String
name|getKnowledgeManagement
parameter_list|()
block|{
return|return
name|knowledgeManagement
return|;
block|}
DECL|method|setKnowledgeManagement (String knowledgeManagement)
specifier|public
name|void
name|setKnowledgeManagement
parameter_list|(
name|String
name|knowledgeManagement
parameter_list|)
block|{
name|this
operator|.
name|knowledgeManagement
operator|=
name|knowledgeManagement
expr_stmt|;
block|}
DECL|method|getProcess ()
specifier|public
name|String
name|getProcess
parameter_list|()
block|{
return|return
name|process
return|;
block|}
DECL|method|setProcess (String process)
specifier|public
name|void
name|setProcess
parameter_list|(
name|String
name|process
parameter_list|)
block|{
name|this
operator|.
name|process
operator|=
name|process
expr_stmt|;
block|}
DECL|method|getFlexiPage ()
specifier|public
name|String
name|getFlexiPage
parameter_list|()
block|{
return|return
name|flexiPage
return|;
block|}
DECL|method|setFlexiPage (String flexiPage)
specifier|public
name|void
name|setFlexiPage
parameter_list|(
name|String
name|flexiPage
parameter_list|)
block|{
name|this
operator|.
name|flexiPage
operator|=
name|flexiPage
expr_stmt|;
block|}
DECL|method|getQuickActions ()
specifier|public
name|String
name|getQuickActions
parameter_list|()
block|{
return|return
name|quickActions
return|;
block|}
DECL|method|setQuickActions (String quickActions)
specifier|public
name|void
name|setQuickActions
parameter_list|(
name|String
name|quickActions
parameter_list|)
block|{
name|this
operator|.
name|quickActions
operator|=
name|quickActions
expr_stmt|;
block|}
DECL|method|getAppMenu ()
specifier|public
name|String
name|getAppMenu
parameter_list|()
block|{
return|return
name|appMenu
return|;
block|}
DECL|method|setAppMenu (String appMenu)
specifier|public
name|void
name|setAppMenu
parameter_list|(
name|String
name|appMenu
parameter_list|)
block|{
name|this
operator|.
name|appMenu
operator|=
name|appMenu
expr_stmt|;
block|}
DECL|method|getCompactLayouts ()
specifier|public
name|String
name|getCompactLayouts
parameter_list|()
block|{
return|return
name|compactLayouts
return|;
block|}
DECL|method|setCompactLayouts (String compactLayouts)
specifier|public
name|void
name|setCompactLayouts
parameter_list|(
name|String
name|compactLayouts
parameter_list|)
block|{
name|this
operator|.
name|compactLayouts
operator|=
name|compactLayouts
expr_stmt|;
block|}
DECL|method|getActions ()
specifier|public
name|String
name|getActions
parameter_list|()
block|{
return|return
name|actions
return|;
block|}
DECL|method|setActions (String actions)
specifier|public
name|void
name|setActions
parameter_list|(
name|String
name|actions
parameter_list|)
block|{
name|this
operator|.
name|actions
operator|=
name|actions
expr_stmt|;
block|}
DECL|method|getTabs ()
specifier|public
name|String
name|getTabs
parameter_list|()
block|{
return|return
name|tabs
return|;
block|}
DECL|method|setTabs (String tabs)
specifier|public
name|void
name|setTabs
parameter_list|(
name|String
name|tabs
parameter_list|)
block|{
name|this
operator|.
name|tabs
operator|=
name|tabs
expr_stmt|;
block|}
DECL|method|getWave ()
specifier|public
name|String
name|getWave
parameter_list|()
block|{
return|return
name|wave
return|;
block|}
DECL|method|setWave (String wave)
specifier|public
name|void
name|setWave
parameter_list|(
name|String
name|wave
parameter_list|)
block|{
name|this
operator|.
name|wave
operator|=
name|wave
expr_stmt|;
block|}
DECL|method|getAsyncQueries ()
specifier|public
name|String
name|getAsyncQueries
parameter_list|()
block|{
return|return
name|asyncQueries
return|;
block|}
DECL|method|setAsyncQueries (String asyncQueries)
specifier|public
name|void
name|setAsyncQueries
parameter_list|(
name|String
name|asyncQueries
parameter_list|)
block|{
name|this
operator|.
name|asyncQueries
operator|=
name|asyncQueries
expr_stmt|;
block|}
DECL|method|getExchangeConnect ()
specifier|public
name|String
name|getExchangeConnect
parameter_list|()
block|{
return|return
name|exchangeConnect
return|;
block|}
DECL|method|setExchangeConnect (String exchangeConnect)
specifier|public
name|void
name|setExchangeConnect
parameter_list|(
name|String
name|exchangeConnect
parameter_list|)
block|{
name|this
operator|.
name|exchangeConnect
operator|=
name|exchangeConnect
expr_stmt|;
block|}
block|}
end_class

end_unit

