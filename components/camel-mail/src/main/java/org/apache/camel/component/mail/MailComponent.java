begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.mail
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|mail
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
name|util
operator|.
name|HashSet
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
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|mail
operator|.
name|search
operator|.
name|SearchTerm
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
name|CamelContext
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
name|Endpoint
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
name|SSLContextParametersAware
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
name|impl
operator|.
name|UriEndpointComponent
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
name|util
operator|.
name|IntrospectionSupport
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
name|ObjectHelper
import|;
end_import

begin_comment
comment|/**  * Component for JavaMail.  *  * @version  */
end_comment

begin_class
DECL|class|MailComponent
specifier|public
class|class
name|MailComponent
extends|extends
name|UriEndpointComponent
implements|implements
name|SSLContextParametersAware
block|{
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|configuration
specifier|private
name|MailConfiguration
name|configuration
decl_stmt|;
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|contentTypeResolver
specifier|private
name|ContentTypeResolver
name|contentTypeResolver
decl_stmt|;
DECL|method|MailComponent ()
specifier|public
name|MailComponent
parameter_list|()
block|{
name|super
argument_list|(
name|MailEndpoint
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
DECL|method|MailComponent (MailConfiguration configuration)
specifier|public
name|MailComponent
parameter_list|(
name|MailConfiguration
name|configuration
parameter_list|)
block|{
name|super
argument_list|(
name|MailEndpoint
operator|.
name|class
argument_list|)
expr_stmt|;
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
block|}
DECL|method|MailComponent (CamelContext context)
specifier|public
name|MailComponent
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
name|super
argument_list|(
name|context
argument_list|,
name|MailEndpoint
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createEndpoint (String uri, String remaining, Map<String, Object> parameters)
specifier|protected
name|Endpoint
name|createEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|remaining
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
throws|throws
name|Exception
block|{
name|URI
name|url
init|=
operator|new
name|URI
argument_list|(
name|uri
argument_list|)
decl_stmt|;
comment|// must use copy as each endpoint can have different options
name|MailConfiguration
name|config
init|=
name|getConfiguration
argument_list|()
operator|.
name|copy
argument_list|()
decl_stmt|;
comment|// only configure if we have a url with a known protocol
name|config
operator|.
name|configure
argument_list|(
name|url
argument_list|)
expr_stmt|;
name|configureAdditionalJavaMailProperties
argument_list|(
name|config
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
name|MailEndpoint
name|endpoint
init|=
operator|new
name|MailEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|,
name|config
argument_list|)
decl_stmt|;
comment|// special for search term bean reference
name|Object
name|searchTerm
init|=
name|getAndRemoveOrResolveReferenceParameter
argument_list|(
name|parameters
argument_list|,
literal|"searchTerm"
argument_list|,
name|Object
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|searchTerm
operator|!=
literal|null
condition|)
block|{
name|SearchTerm
name|st
decl_stmt|;
if|if
condition|(
name|searchTerm
operator|instanceof
name|SimpleSearchTerm
condition|)
block|{
comment|// okay its a SimpleSearchTerm then lets convert that to SearchTerm
name|st
operator|=
name|MailConverters
operator|.
name|toSearchTerm
argument_list|(
operator|(
name|SimpleSearchTerm
operator|)
name|searchTerm
argument_list|,
name|getCamelContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|st
operator|=
name|getCamelContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|mandatoryConvertTo
argument_list|(
name|SearchTerm
operator|.
name|class
argument_list|,
name|searchTerm
argument_list|)
expr_stmt|;
block|}
name|endpoint
operator|.
name|setSearchTerm
argument_list|(
name|st
argument_list|)
expr_stmt|;
block|}
name|endpoint
operator|.
name|setContentTypeResolver
argument_list|(
name|contentTypeResolver
argument_list|)
expr_stmt|;
name|setProperties
argument_list|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
name|setProperties
argument_list|(
name|endpoint
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
comment|// special for searchTerm.xxx options
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|sstParams
init|=
name|IntrospectionSupport
operator|.
name|extractProperties
argument_list|(
name|parameters
argument_list|,
literal|"searchTerm."
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|sstParams
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
comment|// use SimpleSearchTerm as POJO to store the configuration and then convert that to the actual SearchTerm
name|SimpleSearchTerm
name|sst
init|=
operator|new
name|SimpleSearchTerm
argument_list|()
decl_stmt|;
name|setProperties
argument_list|(
name|sst
argument_list|,
name|sstParams
argument_list|)
expr_stmt|;
name|SearchTerm
name|st
init|=
name|MailConverters
operator|.
name|toSearchTerm
argument_list|(
name|sst
argument_list|,
name|getCamelContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
argument_list|)
decl_stmt|;
name|endpoint
operator|.
name|setSearchTerm
argument_list|(
name|st
argument_list|)
expr_stmt|;
block|}
comment|// sanity check that we know the mail server
name|ObjectHelper
operator|.
name|notEmpty
argument_list|(
name|config
operator|.
name|getHost
argument_list|()
argument_list|,
literal|"host"
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notEmpty
argument_list|(
name|config
operator|.
name|getProtocol
argument_list|()
argument_list|,
literal|"protocol"
argument_list|)
expr_stmt|;
comment|// Use global ssl if present
if|if
condition|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getSslContextParameters
argument_list|()
operator|==
literal|null
condition|)
block|{
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|setSslContextParameters
argument_list|(
name|getGlobalSSLContextParameters
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|endpoint
return|;
block|}
DECL|method|configureAdditionalJavaMailProperties (MailConfiguration config, Map<String, Object> parameters)
specifier|private
name|void
name|configureAdditionalJavaMailProperties
parameter_list|(
name|MailConfiguration
name|config
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
block|{
comment|// we cannot remove while iterating, as we will get a modification exception
name|Set
argument_list|<
name|Object
argument_list|>
name|toRemove
init|=
operator|new
name|HashSet
argument_list|<
name|Object
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|entry
range|:
name|parameters
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
name|entry
operator|.
name|getKey
argument_list|()
operator|.
name|toString
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"mail."
argument_list|)
condition|)
block|{
name|config
operator|.
name|getAdditionalJavaMailProperties
argument_list|()
operator|.
name|put
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|toRemove
operator|.
name|add
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
for|for
control|(
name|Object
name|key
range|:
name|toRemove
control|)
block|{
name|parameters
operator|.
name|remove
argument_list|(
name|key
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|getConfiguration ()
specifier|public
name|MailConfiguration
name|getConfiguration
parameter_list|()
block|{
if|if
condition|(
name|configuration
operator|==
literal|null
condition|)
block|{
name|configuration
operator|=
operator|new
name|MailConfiguration
argument_list|(
name|getCamelContext
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|configuration
return|;
block|}
comment|/**      * Sets the Mail configuration      *      * @param configuration the configuration to use by default for endpoints      */
DECL|method|setConfiguration (MailConfiguration configuration)
specifier|public
name|void
name|setConfiguration
parameter_list|(
name|MailConfiguration
name|configuration
parameter_list|)
block|{
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
block|}
DECL|method|getContentTypeResolver ()
specifier|public
name|ContentTypeResolver
name|getContentTypeResolver
parameter_list|()
block|{
return|return
name|contentTypeResolver
return|;
block|}
comment|/**      * Resolver to determine Content-Type for file attachments.      */
DECL|method|setContentTypeResolver (ContentTypeResolver contentTypeResolver)
specifier|public
name|void
name|setContentTypeResolver
parameter_list|(
name|ContentTypeResolver
name|contentTypeResolver
parameter_list|)
block|{
name|this
operator|.
name|contentTypeResolver
operator|=
name|contentTypeResolver
expr_stmt|;
block|}
block|}
end_class

end_unit

