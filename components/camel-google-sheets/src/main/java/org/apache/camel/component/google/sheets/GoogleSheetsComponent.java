begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.google.sheets
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
name|sheets
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|api
operator|.
name|services
operator|.
name|sheets
operator|.
name|v4
operator|.
name|Sheets
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
name|google
operator|.
name|sheets
operator|.
name|internal
operator|.
name|GoogleSheetsApiCollection
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
name|sheets
operator|.
name|internal
operator|.
name|GoogleSheetsApiName
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
name|annotations
operator|.
name|Component
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
name|component
operator|.
name|AbstractApiComponent
import|;
end_import

begin_comment
comment|/**  * Represents the component that manages {@link GoogleSheetsEndpoint}.  */
end_comment

begin_class
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"verifiers"
argument_list|,
name|enums
operator|=
literal|"parameters,connectivity"
argument_list|)
annotation|@
name|Component
argument_list|(
literal|"google-sheets"
argument_list|)
DECL|class|GoogleSheetsComponent
specifier|public
class|class
name|GoogleSheetsComponent
extends|extends
name|AbstractApiComponent
argument_list|<
name|GoogleSheetsApiName
argument_list|,
name|GoogleSheetsConfiguration
argument_list|,
name|GoogleSheetsApiCollection
argument_list|>
block|{
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|client
specifier|private
name|Sheets
name|client
decl_stmt|;
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|clientFactory
specifier|private
name|GoogleSheetsClientFactory
name|clientFactory
decl_stmt|;
DECL|method|GoogleSheetsComponent ()
specifier|public
name|GoogleSheetsComponent
parameter_list|()
block|{
name|super
argument_list|(
name|GoogleSheetsEndpoint
operator|.
name|class
argument_list|,
name|GoogleSheetsApiName
operator|.
name|class
argument_list|,
name|GoogleSheetsApiCollection
operator|.
name|getCollection
argument_list|()
argument_list|)
expr_stmt|;
name|registerExtension
argument_list|(
operator|new
name|GoogleSheetsVerifierExtension
argument_list|(
literal|"google-sheets"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|GoogleSheetsComponent (CamelContext context)
specifier|public
name|GoogleSheetsComponent
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
name|super
argument_list|(
name|context
argument_list|,
name|GoogleSheetsEndpoint
operator|.
name|class
argument_list|,
name|GoogleSheetsApiName
operator|.
name|class
argument_list|,
name|GoogleSheetsApiCollection
operator|.
name|getCollection
argument_list|()
argument_list|)
expr_stmt|;
name|registerExtension
argument_list|(
operator|new
name|GoogleSheetsVerifierExtension
argument_list|(
literal|"google-sheets"
argument_list|,
name|context
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getApiName (String apiNameStr)
specifier|protected
name|GoogleSheetsApiName
name|getApiName
parameter_list|(
name|String
name|apiNameStr
parameter_list|)
throws|throws
name|IllegalArgumentException
block|{
return|return
name|GoogleSheetsApiName
operator|.
name|fromValue
argument_list|(
name|apiNameStr
argument_list|)
return|;
block|}
DECL|method|getClient (GoogleSheetsConfiguration config)
specifier|public
name|Sheets
name|getClient
parameter_list|(
name|GoogleSheetsConfiguration
name|config
parameter_list|)
block|{
if|if
condition|(
name|client
operator|==
literal|null
condition|)
block|{
name|client
operator|=
name|getClientFactory
argument_list|()
operator|.
name|makeClient
argument_list|(
name|config
operator|.
name|getClientId
argument_list|()
argument_list|,
name|config
operator|.
name|getClientSecret
argument_list|()
argument_list|,
name|config
operator|.
name|getApplicationName
argument_list|()
argument_list|,
name|config
operator|.
name|getRefreshToken
argument_list|()
argument_list|,
name|config
operator|.
name|getAccessToken
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|client
return|;
block|}
DECL|method|getClientFactory ()
specifier|public
name|GoogleSheetsClientFactory
name|getClientFactory
parameter_list|()
block|{
if|if
condition|(
name|clientFactory
operator|==
literal|null
condition|)
block|{
name|clientFactory
operator|=
operator|new
name|BatchGoogleSheetsClientFactory
argument_list|()
expr_stmt|;
block|}
return|return
name|clientFactory
return|;
block|}
comment|/**      * To use the shared configuration      */
annotation|@
name|Override
DECL|method|setConfiguration (GoogleSheetsConfiguration configuration)
specifier|public
name|void
name|setConfiguration
parameter_list|(
name|GoogleSheetsConfiguration
name|configuration
parameter_list|)
block|{
name|super
operator|.
name|setConfiguration
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getConfiguration ()
specifier|public
name|GoogleSheetsConfiguration
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
name|GoogleSheetsConfiguration
argument_list|()
expr_stmt|;
block|}
return|return
name|super
operator|.
name|getConfiguration
argument_list|()
return|;
block|}
comment|/**      * To use the GoogleSheetsClientFactory as factory for creating the client.      * Will by default use {@link BatchGoogleSheetsClientFactory}      */
DECL|method|setClientFactory (GoogleSheetsClientFactory clientFactory)
specifier|public
name|void
name|setClientFactory
parameter_list|(
name|GoogleSheetsClientFactory
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
annotation|@
name|Override
DECL|method|createEndpoint (String uri, String methodName, GoogleSheetsApiName apiName, GoogleSheetsConfiguration endpointConfiguration)
specifier|protected
name|Endpoint
name|createEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|methodName
parameter_list|,
name|GoogleSheetsApiName
name|apiName
parameter_list|,
name|GoogleSheetsConfiguration
name|endpointConfiguration
parameter_list|)
block|{
name|endpointConfiguration
operator|.
name|setApiName
argument_list|(
name|apiName
argument_list|)
expr_stmt|;
name|endpointConfiguration
operator|.
name|setMethodName
argument_list|(
name|methodName
argument_list|)
expr_stmt|;
return|return
operator|new
name|GoogleSheetsEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|,
name|apiName
argument_list|,
name|methodName
argument_list|,
name|endpointConfiguration
argument_list|)
return|;
block|}
block|}
end_class

end_unit

