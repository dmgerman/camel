begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.google.drive.springboot
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|google
operator|.
name|drive
operator|.
name|springboot
package|;
end_package

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
name|javax
operator|.
name|annotation
operator|.
name|Generated
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
name|google
operator|.
name|drive
operator|.
name|internal
operator|.
name|GoogleDriveApiName
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

begin_comment
comment|/**  * The google-drive component provides access to Google Drive file storage  * service.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
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
literal|"camel.component.google-drive"
argument_list|)
DECL|class|GoogleDriveComponentConfiguration
specifier|public
class|class
name|GoogleDriveComponentConfiguration
extends|extends
name|ComponentConfigurationPropertiesCommon
block|{
comment|/**      * Whether to enable auto configuration of the google-drive component. This      * is enabled by default.      */
DECL|field|enabled
specifier|private
name|Boolean
name|enabled
decl_stmt|;
comment|/**      * To use the shared configuration      */
DECL|field|configuration
specifier|private
name|GoogleDriveConfigurationNestedConfiguration
name|configuration
decl_stmt|;
comment|/**      * To use the GoogleCalendarClientFactory as factory for creating the      * client. Will by default use BatchGoogleDriveClientFactory. The option is      * a org.apache.camel.component.google.drive.GoogleDriveClientFactory type.      */
DECL|field|clientFactory
specifier|private
name|String
name|clientFactory
decl_stmt|;
comment|/**      * Whether the component should use basic property binding (Camel 2.x) or      * the newer property binding with additional capabilities      */
DECL|field|basicPropertyBinding
specifier|private
name|Boolean
name|basicPropertyBinding
init|=
literal|false
decl_stmt|;
comment|/**      * Whether the producer should be started lazy (on the first message). By      * starting lazy you can use this to allow CamelContext and routes to      * startup in situations where a producer may otherwise fail during starting      * and cause the route to fail being started. By deferring this startup to      * be lazy then the startup failure can be handled during routing messages      * via Camel's routing error handlers. Beware that when the first message is      * processed then creating and starting the producer may take a little time      * and prolong the total processing time of the processing.      */
DECL|field|lazyStartProducer
specifier|private
name|Boolean
name|lazyStartProducer
init|=
literal|false
decl_stmt|;
comment|/**      * Allows for bridging the consumer to the Camel routing Error Handler,      * which mean any exceptions occurred while the consumer is trying to pickup      * incoming messages, or the likes, will now be processed as a message and      * handled by the routing Error Handler. By default the consumer will use      * the org.apache.camel.spi.ExceptionHandler to deal with exceptions, that      * will be logged at WARN or ERROR level and ignored.      */
DECL|field|bridgeErrorHandler
specifier|private
name|Boolean
name|bridgeErrorHandler
init|=
literal|false
decl_stmt|;
DECL|method|getConfiguration ()
specifier|public
name|GoogleDriveConfigurationNestedConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
DECL|method|setConfiguration ( GoogleDriveConfigurationNestedConfiguration configuration)
specifier|public
name|void
name|setConfiguration
parameter_list|(
name|GoogleDriveConfigurationNestedConfiguration
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
DECL|method|getClientFactory ()
specifier|public
name|String
name|getClientFactory
parameter_list|()
block|{
return|return
name|clientFactory
return|;
block|}
DECL|method|setClientFactory (String clientFactory)
specifier|public
name|void
name|setClientFactory
parameter_list|(
name|String
name|clientFactory
parameter_list|)
block|{
name|this
operator|.
name|clientFactory
operator|=
name|clientFactory
expr_stmt|;
block|}
DECL|method|getBasicPropertyBinding ()
specifier|public
name|Boolean
name|getBasicPropertyBinding
parameter_list|()
block|{
return|return
name|basicPropertyBinding
return|;
block|}
DECL|method|setBasicPropertyBinding (Boolean basicPropertyBinding)
specifier|public
name|void
name|setBasicPropertyBinding
parameter_list|(
name|Boolean
name|basicPropertyBinding
parameter_list|)
block|{
name|this
operator|.
name|basicPropertyBinding
operator|=
name|basicPropertyBinding
expr_stmt|;
block|}
DECL|method|getLazyStartProducer ()
specifier|public
name|Boolean
name|getLazyStartProducer
parameter_list|()
block|{
return|return
name|lazyStartProducer
return|;
block|}
DECL|method|setLazyStartProducer (Boolean lazyStartProducer)
specifier|public
name|void
name|setLazyStartProducer
parameter_list|(
name|Boolean
name|lazyStartProducer
parameter_list|)
block|{
name|this
operator|.
name|lazyStartProducer
operator|=
name|lazyStartProducer
expr_stmt|;
block|}
DECL|method|getBridgeErrorHandler ()
specifier|public
name|Boolean
name|getBridgeErrorHandler
parameter_list|()
block|{
return|return
name|bridgeErrorHandler
return|;
block|}
DECL|method|setBridgeErrorHandler (Boolean bridgeErrorHandler)
specifier|public
name|void
name|setBridgeErrorHandler
parameter_list|(
name|Boolean
name|bridgeErrorHandler
parameter_list|)
block|{
name|this
operator|.
name|bridgeErrorHandler
operator|=
name|bridgeErrorHandler
expr_stmt|;
block|}
DECL|class|GoogleDriveConfigurationNestedConfiguration
specifier|public
specifier|static
class|class
name|GoogleDriveConfigurationNestedConfiguration
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
name|google
operator|.
name|drive
operator|.
name|GoogleDriveConfiguration
operator|.
name|class
decl_stmt|;
comment|/**          * What kind of operation to perform          */
DECL|field|apiName
specifier|private
name|GoogleDriveApiName
name|apiName
decl_stmt|;
comment|/**          * What sub operation to use for the selected operation          */
DECL|field|methodName
specifier|private
name|String
name|methodName
decl_stmt|;
comment|/**          * Client ID of the drive application          */
DECL|field|clientId
specifier|private
name|String
name|clientId
decl_stmt|;
comment|/**          * Client secret of the drive application          */
DECL|field|clientSecret
specifier|private
name|String
name|clientSecret
decl_stmt|;
comment|/**          * OAuth 2 access token. This typically expires after an hour so          * refreshToken is recommended for long term usage.          */
DECL|field|accessToken
specifier|private
name|String
name|accessToken
decl_stmt|;
comment|/**          * OAuth 2 refresh token. Using this, the Google Calendar component can          * obtain a new accessToken whenever the current one expires - a          * necessity if the application is long-lived.          */
DECL|field|refreshToken
specifier|private
name|String
name|refreshToken
decl_stmt|;
comment|/**          * Google drive application name. Example would be          * "camel-google-drive/1.0"          */
DECL|field|applicationName
specifier|private
name|String
name|applicationName
decl_stmt|;
comment|/**          * Specifies the level of permissions you want a drive application to          * have to a user account. See          * https://developers.google.com/drive/web/scopes for more info.          */
DECL|field|scopes
specifier|private
name|List
name|scopes
decl_stmt|;
DECL|method|getApiName ()
specifier|public
name|GoogleDriveApiName
name|getApiName
parameter_list|()
block|{
return|return
name|apiName
return|;
block|}
DECL|method|setApiName (GoogleDriveApiName apiName)
specifier|public
name|void
name|setApiName
parameter_list|(
name|GoogleDriveApiName
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
DECL|method|getClientId ()
specifier|public
name|String
name|getClientId
parameter_list|()
block|{
return|return
name|clientId
return|;
block|}
DECL|method|setClientId (String clientId)
specifier|public
name|void
name|setClientId
parameter_list|(
name|String
name|clientId
parameter_list|)
block|{
name|this
operator|.
name|clientId
operator|=
name|clientId
expr_stmt|;
block|}
DECL|method|getClientSecret ()
specifier|public
name|String
name|getClientSecret
parameter_list|()
block|{
return|return
name|clientSecret
return|;
block|}
DECL|method|setClientSecret (String clientSecret)
specifier|public
name|void
name|setClientSecret
parameter_list|(
name|String
name|clientSecret
parameter_list|)
block|{
name|this
operator|.
name|clientSecret
operator|=
name|clientSecret
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
DECL|method|getRefreshToken ()
specifier|public
name|String
name|getRefreshToken
parameter_list|()
block|{
return|return
name|refreshToken
return|;
block|}
DECL|method|setRefreshToken (String refreshToken)
specifier|public
name|void
name|setRefreshToken
parameter_list|(
name|String
name|refreshToken
parameter_list|)
block|{
name|this
operator|.
name|refreshToken
operator|=
name|refreshToken
expr_stmt|;
block|}
DECL|method|getApplicationName ()
specifier|public
name|String
name|getApplicationName
parameter_list|()
block|{
return|return
name|applicationName
return|;
block|}
DECL|method|setApplicationName (String applicationName)
specifier|public
name|void
name|setApplicationName
parameter_list|(
name|String
name|applicationName
parameter_list|)
block|{
name|this
operator|.
name|applicationName
operator|=
name|applicationName
expr_stmt|;
block|}
DECL|method|getScopes ()
specifier|public
name|List
name|getScopes
parameter_list|()
block|{
return|return
name|scopes
return|;
block|}
DECL|method|setScopes (List scopes)
specifier|public
name|void
name|setScopes
parameter_list|(
name|List
name|scopes
parameter_list|)
block|{
name|this
operator|.
name|scopes
operator|=
name|scopes
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

