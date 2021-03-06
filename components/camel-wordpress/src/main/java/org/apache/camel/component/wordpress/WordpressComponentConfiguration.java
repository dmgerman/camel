begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.wordpress
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
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URISyntaxException
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
name|WordpressConstants
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
name|component
operator|.
name|wordpress
operator|.
name|api
operator|.
name|model
operator|.
name|SearchCriteria
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
name|spi
operator|.
name|Metadata
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
name|spi
operator|.
name|UriParam
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
name|spi
operator|.
name|UriParams
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
name|StringHelper
import|;
end_import

begin_class
annotation|@
name|UriParams
DECL|class|WordpressComponentConfiguration
specifier|public
class|class
name|WordpressComponentConfiguration
block|{
DECL|field|uri
specifier|private
name|URI
name|uri
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|description
operator|=
literal|"The Wordpress API URL from your site, e.g. http://myblog.com/wp-json/"
argument_list|)
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|true
argument_list|)
DECL|field|url
specifier|private
name|String
name|url
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|description
operator|=
literal|"The Wordpress REST API version"
argument_list|,
name|defaultValue
operator|=
name|WordpressConstants
operator|.
name|API_VERSION
argument_list|)
DECL|field|apiVersion
specifier|private
name|String
name|apiVersion
init|=
name|WordpressConstants
operator|.
name|API_VERSION
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|description
operator|=
literal|"Authorized user to perform writing operations"
argument_list|)
DECL|field|user
specifier|private
name|String
name|user
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|description
operator|=
literal|"Password from authorized user"
argument_list|)
DECL|field|password
specifier|private
name|String
name|password
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|description
operator|=
literal|"The entity ID. Should be passed when the operation performed requires a specific entity, e.g. deleting a post"
argument_list|)
DECL|field|id
specifier|private
name|Integer
name|id
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|description
operator|=
literal|"The criteria to use with complex searches."
argument_list|,
name|prefix
operator|=
literal|"criteria."
argument_list|,
name|multiValue
operator|=
literal|true
argument_list|)
DECL|field|criteria
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|criteria
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|description
operator|=
literal|"Whether to bypass trash and force deletion."
argument_list|)
DECL|field|force
specifier|private
name|boolean
name|force
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|description
operator|=
literal|"Search criteria"
argument_list|)
DECL|field|searchCriteria
specifier|private
name|SearchCriteria
name|searchCriteria
decl_stmt|;
DECL|method|getUrl ()
specifier|public
name|String
name|getUrl
parameter_list|()
block|{
return|return
name|url
return|;
block|}
DECL|method|getUri ()
specifier|public
name|URI
name|getUri
parameter_list|()
block|{
return|return
name|uri
return|;
block|}
DECL|method|setUrl (String url)
specifier|public
name|void
name|setUrl
parameter_list|(
name|String
name|url
parameter_list|)
block|{
name|this
operator|.
name|url
operator|=
name|url
expr_stmt|;
block|}
DECL|method|getApiVersion ()
specifier|public
name|String
name|getApiVersion
parameter_list|()
block|{
return|return
name|apiVersion
return|;
block|}
DECL|method|setApiVersion (String apiVersion)
specifier|public
name|void
name|setApiVersion
parameter_list|(
name|String
name|apiVersion
parameter_list|)
block|{
name|this
operator|.
name|apiVersion
operator|=
name|apiVersion
expr_stmt|;
block|}
DECL|method|getPassword ()
specifier|public
name|String
name|getPassword
parameter_list|()
block|{
return|return
name|password
return|;
block|}
DECL|method|setPassword (String password)
specifier|public
name|void
name|setPassword
parameter_list|(
name|String
name|password
parameter_list|)
block|{
name|this
operator|.
name|password
operator|=
name|password
expr_stmt|;
block|}
DECL|method|getUser ()
specifier|public
name|String
name|getUser
parameter_list|()
block|{
return|return
name|user
return|;
block|}
DECL|method|setUser (String user)
specifier|public
name|void
name|setUser
parameter_list|(
name|String
name|user
parameter_list|)
block|{
name|this
operator|.
name|user
operator|=
name|user
expr_stmt|;
block|}
DECL|method|getId ()
specifier|public
name|Integer
name|getId
parameter_list|()
block|{
return|return
name|id
return|;
block|}
DECL|method|setId (Integer id)
specifier|public
name|void
name|setId
parameter_list|(
name|Integer
name|id
parameter_list|)
block|{
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
block|}
DECL|method|getCriteria ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getCriteria
parameter_list|()
block|{
return|return
name|criteria
return|;
block|}
DECL|method|setCriteria (Map<String, Object> criteria)
specifier|public
name|void
name|setCriteria
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|criteria
parameter_list|)
block|{
name|this
operator|.
name|criteria
operator|=
name|criteria
expr_stmt|;
block|}
DECL|method|isForce ()
specifier|public
name|boolean
name|isForce
parameter_list|()
block|{
return|return
name|force
return|;
block|}
DECL|method|setForce (boolean force)
specifier|public
name|void
name|setForce
parameter_list|(
name|boolean
name|force
parameter_list|)
block|{
name|this
operator|.
name|force
operator|=
name|force
expr_stmt|;
block|}
DECL|method|getSearchCriteria ()
specifier|public
name|SearchCriteria
name|getSearchCriteria
parameter_list|()
block|{
return|return
name|searchCriteria
return|;
block|}
DECL|method|setSearchCriteria (SearchCriteria searchCriteria)
specifier|public
name|void
name|setSearchCriteria
parameter_list|(
name|SearchCriteria
name|searchCriteria
parameter_list|)
block|{
name|this
operator|.
name|searchCriteria
operator|=
name|searchCriteria
expr_stmt|;
block|}
DECL|method|validate ()
specifier|public
name|void
name|validate
parameter_list|()
block|{
name|StringHelper
operator|.
name|notEmpty
argument_list|(
name|this
operator|.
name|apiVersion
argument_list|,
literal|"apiVersion"
argument_list|)
expr_stmt|;
name|StringHelper
operator|.
name|notEmpty
argument_list|(
name|this
operator|.
name|url
argument_list|,
literal|"url"
argument_list|)
expr_stmt|;
try|try
block|{
name|this
operator|.
name|uri
operator|=
operator|new
name|URI
argument_list|(
name|url
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|URISyntaxException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Impossible to set Wordpress API URL"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

