begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.yammer
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|yammer
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
name|impl
operator|.
name|DefaultComponent
import|;
end_import

begin_comment
comment|/**  * Represents the component that manages {@link YammerEndpoint}.  */
end_comment

begin_class
DECL|class|YammerComponent
specifier|public
class|class
name|YammerComponent
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
DECL|field|accessToken
specifier|private
name|String
name|accessToken
decl_stmt|;
DECL|field|config
specifier|private
name|YammerConfiguration
name|config
decl_stmt|;
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
comment|// by default use config for each endpoint; use from component level if one has been explicitly set
name|YammerConfiguration
name|endpointConfig
init|=
name|getConfig
argument_list|()
decl_stmt|;
if|if
condition|(
name|endpointConfig
operator|==
literal|null
condition|)
block|{
name|endpointConfig
operator|=
operator|new
name|YammerConfiguration
argument_list|()
expr_stmt|;
block|}
comment|// set options from component
name|endpointConfig
operator|.
name|setConsumerKey
argument_list|(
name|consumerKey
argument_list|)
expr_stmt|;
name|endpointConfig
operator|.
name|setConsumerSecret
argument_list|(
name|consumerSecret
argument_list|)
expr_stmt|;
name|endpointConfig
operator|.
name|setAccessToken
argument_list|(
name|accessToken
argument_list|)
expr_stmt|;
name|endpointConfig
operator|.
name|setFunction
argument_list|(
name|remaining
argument_list|)
expr_stmt|;
name|endpointConfig
operator|.
name|setFunctionType
argument_list|(
name|YammerFunctionType
operator|.
name|fromUri
argument_list|(
name|remaining
argument_list|)
argument_list|)
expr_stmt|;
comment|// and then override from parameters
name|setProperties
argument_list|(
name|endpointConfig
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
name|Endpoint
name|endpoint
init|=
operator|new
name|YammerEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|,
name|endpointConfig
argument_list|)
decl_stmt|;
name|setProperties
argument_list|(
name|endpoint
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
return|return
name|endpoint
return|;
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
comment|/**      * The consumer key      */
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
comment|/**      * The consumer secret      */
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
DECL|method|getAccessToken ()
specifier|public
name|String
name|getAccessToken
parameter_list|()
block|{
return|return
name|accessToken
return|;
block|}
comment|/**      * The access token      */
DECL|method|setAccessToken (String accessToken)
specifier|public
name|void
name|setAccessToken
parameter_list|(
name|String
name|accessToken
parameter_list|)
block|{
name|this
operator|.
name|accessToken
operator|=
name|accessToken
expr_stmt|;
block|}
DECL|method|getConfig ()
specifier|public
name|YammerConfiguration
name|getConfig
parameter_list|()
block|{
return|return
name|config
return|;
block|}
comment|/**      * To use a shared yammer configuration      */
DECL|method|setConfig (YammerConfiguration config)
specifier|public
name|void
name|setConfig
parameter_list|(
name|YammerConfiguration
name|config
parameter_list|)
block|{
name|this
operator|.
name|config
operator|=
name|config
expr_stmt|;
block|}
block|}
end_class

end_unit

