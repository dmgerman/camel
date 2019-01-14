begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cmis
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|cmis
package|;
end_package

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
name|Consumer
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
name|Processor
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
name|Producer
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
name|support
operator|.
name|DefaultEndpoint
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
name|UriEndpoint
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
name|UriPath
import|;
end_import

begin_comment
comment|/**  * The cmis component uses the Apache Chemistry client API and allows you to add/read nodes to/from a CMIS compliant content repositories.  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|firstVersion
operator|=
literal|"2.11.0"
argument_list|,
name|scheme
operator|=
literal|"cmis"
argument_list|,
name|title
operator|=
literal|"CMIS"
argument_list|,
name|syntax
operator|=
literal|"cmis:cmsUrl"
argument_list|,
name|label
operator|=
literal|"cms,database"
argument_list|)
DECL|class|CMISEndpoint
specifier|public
class|class
name|CMISEndpoint
extends|extends
name|DefaultEndpoint
block|{
annotation|@
name|UriPath
argument_list|(
name|description
operator|=
literal|"URL to the cmis repository"
argument_list|)
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|true
argument_list|)
DECL|field|cmsUrl
specifier|private
specifier|final
name|String
name|cmsUrl
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|)
DECL|field|queryMode
specifier|private
name|boolean
name|queryMode
decl_stmt|;
annotation|@
name|UriParam
DECL|field|sessionFacade
specifier|private
name|CMISSessionFacade
name|sessionFacade
decl_stmt|;
comment|// to include in component documentation
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|sessionFacadeFactory
specifier|private
name|CMISSessionFacadeFactory
name|sessionFacadeFactory
decl_stmt|;
DECL|field|properties
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
decl_stmt|;
comment|// properties for each session facade instance being created
DECL|method|CMISEndpoint (String uri, CMISComponent component, String cmsUrl)
specifier|public
name|CMISEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|CMISComponent
name|component
parameter_list|,
name|String
name|cmsUrl
parameter_list|)
block|{
name|this
argument_list|(
name|uri
argument_list|,
name|component
argument_list|,
name|cmsUrl
argument_list|,
operator|new
name|DefaultCMISSessionFacadeFactory
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|CMISEndpoint (String uri, CMISComponent component, String cmsUrl, CMISSessionFacadeFactory sessionFacadeFactory)
specifier|public
name|CMISEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|CMISComponent
name|component
parameter_list|,
name|String
name|cmsUrl
parameter_list|,
name|CMISSessionFacadeFactory
name|sessionFacadeFactory
parameter_list|)
block|{
name|super
argument_list|(
name|uri
argument_list|,
name|component
argument_list|)
expr_stmt|;
name|this
operator|.
name|cmsUrl
operator|=
name|cmsUrl
expr_stmt|;
name|this
operator|.
name|sessionFacadeFactory
operator|=
name|sessionFacadeFactory
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|this
operator|.
name|queryMode
condition|?
operator|new
name|CMISQueryProducer
argument_list|(
name|this
argument_list|,
name|sessionFacadeFactory
argument_list|)
else|:
operator|new
name|CMISProducer
argument_list|(
name|this
argument_list|,
name|sessionFacadeFactory
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createConsumer (Processor processor)
specifier|public
name|Consumer
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
name|CMISConsumer
name|consumer
init|=
operator|new
name|CMISConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|,
name|sessionFacadeFactory
argument_list|)
decl_stmt|;
name|configureConsumer
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
return|return
name|consumer
return|;
block|}
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
DECL|method|isQueryMode ()
specifier|public
name|boolean
name|isQueryMode
parameter_list|()
block|{
return|return
name|queryMode
return|;
block|}
comment|/**      * If true, will execute the cmis query from the message body and return result, otherwise will create a node in the cmis repository      */
DECL|method|setQueryMode (boolean queryMode)
specifier|public
name|void
name|setQueryMode
parameter_list|(
name|boolean
name|queryMode
parameter_list|)
block|{
name|this
operator|.
name|queryMode
operator|=
name|queryMode
expr_stmt|;
block|}
DECL|method|getCmsUrl ()
specifier|public
name|String
name|getCmsUrl
parameter_list|()
block|{
return|return
name|cmsUrl
return|;
block|}
DECL|method|getSessionFacade ()
specifier|public
name|CMISSessionFacade
name|getSessionFacade
parameter_list|()
block|{
return|return
name|sessionFacade
return|;
block|}
comment|/**      * Session configuration      */
DECL|method|setSessionFacade (CMISSessionFacade sessionFacade)
specifier|public
name|void
name|setSessionFacade
parameter_list|(
name|CMISSessionFacade
name|sessionFacade
parameter_list|)
block|{
name|this
operator|.
name|sessionFacade
operator|=
name|sessionFacade
expr_stmt|;
block|}
DECL|method|getProperties ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getProperties
parameter_list|()
block|{
return|return
name|properties
return|;
block|}
DECL|method|setProperties (Map<String, Object> properties)
specifier|public
name|void
name|setProperties
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
parameter_list|)
block|{
name|this
operator|.
name|properties
operator|=
name|properties
expr_stmt|;
block|}
DECL|method|getSessionFacadeFactory ()
specifier|public
name|CMISSessionFacadeFactory
name|getSessionFacadeFactory
parameter_list|()
block|{
return|return
name|sessionFacadeFactory
return|;
block|}
comment|/**      * To use a custom CMISSessionFacadeFactory to create the CMISSessionFacade instances      */
DECL|method|setSessionFacadeFactory (CMISSessionFacadeFactory sessionFacadeFactory)
specifier|public
name|void
name|setSessionFacadeFactory
parameter_list|(
name|CMISSessionFacadeFactory
name|sessionFacadeFactory
parameter_list|)
block|{
name|this
operator|.
name|sessionFacadeFactory
operator|=
name|sessionFacadeFactory
expr_stmt|;
block|}
block|}
end_class

end_unit

