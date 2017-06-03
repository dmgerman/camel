begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.twilio.springboot
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|twilio
operator|.
name|springboot
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|Generated
import|;
end_import

begin_import
import|import
name|com
operator|.
name|twilio
operator|.
name|http
operator|.
name|TwilioRestClient
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
name|twilio
operator|.
name|internal
operator|.
name|TwilioApiName
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
name|spring
operator|.
name|boot
operator|.
name|ComponentConfigurationPropertiesCommon
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
name|NestedConfigurationProperty
import|;
end_import

begin_comment
comment|/**  * The Twilio component allows you to interact with the Twilio REST APIs using  * Twilio Java SDK.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_class
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.SpringBootAutoConfigurationMojo"
argument_list|)
annotation|@
name|ConfigurationProperties
argument_list|(
name|prefix
operator|=
literal|"camel.component.twilio"
argument_list|)
DECL|class|TwilioComponentConfiguration
specifier|public
class|class
name|TwilioComponentConfiguration
extends|extends
name|ComponentConfigurationPropertiesCommon
block|{
comment|/**      * To use the shared configuration      */
DECL|field|configuration
specifier|private
name|TwilioConfigurationNestedConfiguration
name|configuration
decl_stmt|;
comment|/**      * To use the shared REST client      */
annotation|@
name|NestedConfigurationProperty
DECL|field|restClient
specifier|private
name|TwilioRestClient
name|restClient
decl_stmt|;
comment|/**      * Whether the component should resolve property placeholders on itself when      * starting. Only properties which are of String type can use property      * placeholders.      */
DECL|field|resolvePropertyPlaceholders
specifier|private
name|Boolean
name|resolvePropertyPlaceholders
init|=
literal|true
decl_stmt|;
DECL|method|getConfiguration ()
specifier|public
name|TwilioConfigurationNestedConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
DECL|method|setConfiguration ( TwilioConfigurationNestedConfiguration configuration)
specifier|public
name|void
name|setConfiguration
parameter_list|(
name|TwilioConfigurationNestedConfiguration
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
DECL|method|getRestClient ()
specifier|public
name|TwilioRestClient
name|getRestClient
parameter_list|()
block|{
return|return
name|restClient
return|;
block|}
DECL|method|setRestClient (TwilioRestClient restClient)
specifier|public
name|void
name|setRestClient
parameter_list|(
name|TwilioRestClient
name|restClient
parameter_list|)
block|{
name|this
operator|.
name|restClient
operator|=
name|restClient
expr_stmt|;
block|}
DECL|method|getResolvePropertyPlaceholders ()
specifier|public
name|Boolean
name|getResolvePropertyPlaceholders
parameter_list|()
block|{
return|return
name|resolvePropertyPlaceholders
return|;
block|}
DECL|method|setResolvePropertyPlaceholders ( Boolean resolvePropertyPlaceholders)
specifier|public
name|void
name|setResolvePropertyPlaceholders
parameter_list|(
name|Boolean
name|resolvePropertyPlaceholders
parameter_list|)
block|{
name|this
operator|.
name|resolvePropertyPlaceholders
operator|=
name|resolvePropertyPlaceholders
expr_stmt|;
block|}
DECL|class|TwilioConfigurationNestedConfiguration
specifier|public
specifier|static
class|class
name|TwilioConfigurationNestedConfiguration
block|{
DECL|field|CAMEL_NESTED_CLASS
specifier|public
specifier|static
specifier|final
name|Class
name|CAMEL_NESTED_CLASS
init|=
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|twilio
operator|.
name|TwilioConfiguration
operator|.
name|class
decl_stmt|;
comment|/**          * What kind of operation to perform          *           * @param apiNamethe          *            API Name to set          */
DECL|field|apiName
specifier|private
name|TwilioApiName
name|apiName
decl_stmt|;
comment|/**          * What sub operation to use for the selected operation          *           * @param methodNamethe          *            methodName to set          */
DECL|field|methodName
specifier|private
name|String
name|methodName
decl_stmt|;
comment|/**          * The account to use.          */
DECL|field|username
specifier|private
name|String
name|username
decl_stmt|;
comment|/**          * Auth token for the account.          */
DECL|field|password
specifier|private
name|String
name|password
decl_stmt|;
comment|/**          * The account SID to use.          */
DECL|field|accountSid
specifier|private
name|String
name|accountSid
decl_stmt|;
DECL|method|getApiName ()
specifier|public
name|TwilioApiName
name|getApiName
parameter_list|()
block|{
return|return
name|apiName
return|;
block|}
DECL|method|setApiName (TwilioApiName apiName)
specifier|public
name|void
name|setApiName
parameter_list|(
name|TwilioApiName
name|apiName
parameter_list|)
block|{
name|this
operator|.
name|apiName
operator|=
name|apiName
expr_stmt|;
block|}
DECL|method|getMethodName ()
specifier|public
name|String
name|getMethodName
parameter_list|()
block|{
return|return
name|methodName
return|;
block|}
DECL|method|setMethodName (String methodName)
specifier|public
name|void
name|setMethodName
parameter_list|(
name|String
name|methodName
parameter_list|)
block|{
name|this
operator|.
name|methodName
operator|=
name|methodName
expr_stmt|;
block|}
DECL|method|getUsername ()
specifier|public
name|String
name|getUsername
parameter_list|()
block|{
return|return
name|username
return|;
block|}
DECL|method|setUsername (String username)
specifier|public
name|void
name|setUsername
parameter_list|(
name|String
name|username
parameter_list|)
block|{
name|this
operator|.
name|username
operator|=
name|username
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
DECL|method|getAccountSid ()
specifier|public
name|String
name|getAccountSid
parameter_list|()
block|{
return|return
name|accountSid
return|;
block|}
DECL|method|setAccountSid (String accountSid)
specifier|public
name|void
name|setAccountSid
parameter_list|(
name|String
name|accountSid
parameter_list|)
block|{
name|this
operator|.
name|accountSid
operator|=
name|accountSid
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

