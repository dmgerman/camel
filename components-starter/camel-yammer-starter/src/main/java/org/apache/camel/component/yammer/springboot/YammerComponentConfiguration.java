begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.yammer.springboot
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
operator|.
name|springboot
package|;
end_package

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
name|yammer
operator|.
name|YammerConfiguration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|context
operator|.
name|properties
operator|.
name|ConfigurationProperties
import|;
end_import

begin_comment
comment|/**  * The yammer component allows you to interact with the Yammer enterprise social  * network.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_class
annotation|@
name|ConfigurationProperties
argument_list|(
name|prefix
operator|=
literal|"camel.component.yammer"
argument_list|)
DECL|class|YammerComponentConfiguration
specifier|public
class|class
name|YammerComponentConfiguration
block|{
comment|/**      * The consumer key      */
DECL|field|consumerKey
specifier|private
name|String
name|consumerKey
decl_stmt|;
comment|/**      * The consumer secret      */
DECL|field|consumerSecret
specifier|private
name|String
name|consumerSecret
decl_stmt|;
comment|/**      * The access token      */
DECL|field|accessToken
specifier|private
name|String
name|accessToken
decl_stmt|;
comment|/**      * To use a shared yammer configuration      */
DECL|field|config
specifier|private
name|YammerConfiguration
name|config
decl_stmt|;
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

