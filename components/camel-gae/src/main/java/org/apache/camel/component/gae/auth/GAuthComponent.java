begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.gae.auth
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|gae
operator|.
name|auth
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
name|component
operator|.
name|gae
operator|.
name|bind
operator|.
name|OutboundBinding
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
name|DefaultComponent
import|;
end_import

begin_comment
comment|/**  * The<a href="http://camel.apache.org/gauth.html">GAuth Component</a>  * implements a Google-specific OAuth comsumer. This component supports OAuth  * 1.0a. For background information refer to<a  * href="http://code.google.com/apis/accounts/docs/OAuth.html">OAuth for Web  * Applications</a> and the<a  * href="http://code.google.com/apis/gdata/docs/auth/oauth.html">GData developer  * guide for OAuth.</a>.  */
end_comment

begin_class
DECL|class|GAuthComponent
specifier|public
class|class
name|GAuthComponent
extends|extends
name|DefaultComponent
block|{
DECL|field|consumerKey
specifier|private
name|String
name|consumerKey
decl_stmt|;
DECL|field|consumerSecret
specifier|private
name|String
name|consumerSecret
decl_stmt|;
DECL|field|keyLoader
specifier|private
name|GAuthKeyLoader
name|keyLoader
decl_stmt|;
DECL|method|GAuthComponent ()
specifier|public
name|GAuthComponent
parameter_list|()
block|{
name|this
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|GAuthComponent (CamelContext context)
specifier|public
name|GAuthComponent
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
name|super
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
DECL|method|getConsumerKey ()
specifier|public
name|String
name|getConsumerKey
parameter_list|()
block|{
return|return
name|consumerKey
return|;
block|}
DECL|method|setConsumerKey (String consumerKey)
specifier|public
name|void
name|setConsumerKey
parameter_list|(
name|String
name|consumerKey
parameter_list|)
block|{
name|this
operator|.
name|consumerKey
operator|=
name|consumerKey
expr_stmt|;
block|}
DECL|method|getConsumerSecret ()
specifier|public
name|String
name|getConsumerSecret
parameter_list|()
block|{
return|return
name|consumerSecret
return|;
block|}
DECL|method|setConsumerSecret (String consumerSecret)
specifier|public
name|void
name|setConsumerSecret
parameter_list|(
name|String
name|consumerSecret
parameter_list|)
block|{
name|this
operator|.
name|consumerSecret
operator|=
name|consumerSecret
expr_stmt|;
block|}
DECL|method|getKeyLoader ()
specifier|public
name|GAuthKeyLoader
name|getKeyLoader
parameter_list|()
block|{
return|return
name|keyLoader
return|;
block|}
DECL|method|setKeyLoader (GAuthKeyLoader keyLoader)
specifier|public
name|void
name|setKeyLoader
parameter_list|(
name|GAuthKeyLoader
name|keyLoader
parameter_list|)
block|{
name|this
operator|.
name|keyLoader
operator|=
name|keyLoader
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createEndpoint (String uri)
specifier|public
name|GAuthEndpoint
name|createEndpoint
parameter_list|(
name|String
name|uri
parameter_list|)
throws|throws
name|Exception
block|{
return|return
operator|(
name|GAuthEndpoint
operator|)
name|super
operator|.
name|createEndpoint
argument_list|(
name|uri
argument_list|)
return|;
block|}
annotation|@
name|Override
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
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
name|GAuthEndpoint
name|endpoint
init|=
operator|new
name|GAuthEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|,
name|remaining
argument_list|)
decl_stmt|;
name|OutboundBinding
name|authorizeBinding
init|=
name|resolveAndRemoveReferenceParameter
argument_list|(
name|parameters
argument_list|,
literal|"authorizeBindingRef"
argument_list|,
name|GAuthAuthorizeBinding
operator|.
name|class
argument_list|,
operator|new
name|GAuthAuthorizeBinding
argument_list|()
argument_list|)
decl_stmt|;
name|OutboundBinding
name|upgradeBinding
init|=
name|resolveAndRemoveReferenceParameter
argument_list|(
name|parameters
argument_list|,
literal|"upgradeBindingRef"
argument_list|,
name|GAuthUpgradeBinding
operator|.
name|class
argument_list|,
operator|new
name|GAuthUpgradeBinding
argument_list|()
argument_list|)
decl_stmt|;
name|GAuthService
name|service
init|=
name|resolveAndRemoveReferenceParameter
argument_list|(
name|parameters
argument_list|,
literal|"serviceRef"
argument_list|,
name|GAuthService
operator|.
name|class
argument_list|,
operator|new
name|GAuthServiceImpl
argument_list|(
name|endpoint
argument_list|)
argument_list|)
decl_stmt|;
name|GAuthKeyLoader
name|keyLoader
init|=
name|resolveAndRemoveReferenceParameter
argument_list|(
name|parameters
argument_list|,
literal|"keyLoaderRef"
argument_list|,
name|GAuthKeyLoader
operator|.
name|class
argument_list|)
decl_stmt|;
name|endpoint
operator|.
name|setAuthorizeBinding
argument_list|(
name|authorizeBinding
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|setUpgradeBinding
argument_list|(
name|upgradeBinding
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|setService
argument_list|(
name|service
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|setKeyLoader
argument_list|(
name|keyLoader
argument_list|)
expr_stmt|;
return|return
name|endpoint
return|;
block|}
block|}
end_class

end_unit

