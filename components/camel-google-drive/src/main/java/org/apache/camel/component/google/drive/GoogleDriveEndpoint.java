begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.google.drive
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
name|com
operator|.
name|google
operator|.
name|api
operator|.
name|services
operator|.
name|drive
operator|.
name|Drive
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
name|component
operator|.
name|google
operator|.
name|drive
operator|.
name|internal
operator|.
name|GoogleDriveApiCollection
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
name|component
operator|.
name|google
operator|.
name|drive
operator|.
name|internal
operator|.
name|GoogleDriveConstants
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
name|GoogleDrivePropertiesHelper
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
name|util
operator|.
name|component
operator|.
name|AbstractApiEndpoint
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
name|component
operator|.
name|ApiMethod
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
name|component
operator|.
name|ApiMethodPropertiesHelper
import|;
end_import

begin_comment
comment|/**  * Represents a GoogleDrive endpoint.  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|scheme
operator|=
literal|"google-drive"
argument_list|,
name|syntax
operator|=
literal|"google:drive:apiName/methodName"
argument_list|,
name|consumerClass
operator|=
name|GoogleDriveConsumer
operator|.
name|class
argument_list|,
name|consumerPrefix
operator|=
literal|"consumer"
argument_list|,
name|label
operator|=
literal|"api,cloud,file"
argument_list|)
DECL|class|GoogleDriveEndpoint
specifier|public
class|class
name|GoogleDriveEndpoint
extends|extends
name|AbstractApiEndpoint
argument_list|<
name|GoogleDriveApiName
argument_list|,
name|GoogleDriveConfiguration
argument_list|>
block|{
DECL|field|apiProxy
specifier|private
name|Object
name|apiProxy
decl_stmt|;
annotation|@
name|UriParam
DECL|field|configuration
specifier|private
name|GoogleDriveConfiguration
name|configuration
decl_stmt|;
DECL|method|GoogleDriveEndpoint (String uri, GoogleDriveComponent component, GoogleDriveApiName apiName, String methodName, GoogleDriveConfiguration endpointConfiguration)
specifier|public
name|GoogleDriveEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|GoogleDriveComponent
name|component
parameter_list|,
name|GoogleDriveApiName
name|apiName
parameter_list|,
name|String
name|methodName
parameter_list|,
name|GoogleDriveConfiguration
name|endpointConfiguration
parameter_list|)
block|{
name|super
argument_list|(
name|uri
argument_list|,
name|component
argument_list|,
name|apiName
argument_list|,
name|methodName
argument_list|,
name|GoogleDriveApiCollection
operator|.
name|getCollection
argument_list|()
operator|.
name|getHelper
argument_list|(
name|apiName
argument_list|)
argument_list|,
name|endpointConfiguration
argument_list|)
expr_stmt|;
name|this
operator|.
name|configuration
operator|=
name|endpointConfiguration
expr_stmt|;
block|}
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|GoogleDriveProducer
argument_list|(
name|this
argument_list|)
return|;
block|}
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
comment|// make sure inBody is not set for consumers
if|if
condition|(
name|inBody
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Option inBody is not supported for consumer endpoint"
argument_list|)
throw|;
block|}
specifier|final
name|GoogleDriveConsumer
name|consumer
init|=
operator|new
name|GoogleDriveConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|)
decl_stmt|;
comment|// also set consumer.* properties
name|configureConsumer
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
return|return
name|consumer
return|;
block|}
annotation|@
name|Override
DECL|method|getPropertiesHelper ()
specifier|protected
name|ApiMethodPropertiesHelper
argument_list|<
name|GoogleDriveConfiguration
argument_list|>
name|getPropertiesHelper
parameter_list|()
block|{
return|return
name|GoogleDrivePropertiesHelper
operator|.
name|getHelper
argument_list|()
return|;
block|}
DECL|method|getThreadProfileName ()
specifier|protected
name|String
name|getThreadProfileName
parameter_list|()
block|{
return|return
name|GoogleDriveConstants
operator|.
name|THREAD_PROFILE_NAME
return|;
block|}
annotation|@
name|Override
DECL|method|afterConfigureProperties ()
specifier|protected
name|void
name|afterConfigureProperties
parameter_list|()
block|{
switch|switch
condition|(
name|apiName
condition|)
block|{
case|case
name|DRIVE_FILES
case|:
name|apiProxy
operator|=
name|getClient
argument_list|()
operator|.
name|files
argument_list|()
expr_stmt|;
break|break;
case|case
name|DRIVE_ABOUT
case|:
name|apiProxy
operator|=
name|getClient
argument_list|()
operator|.
name|about
argument_list|()
expr_stmt|;
break|break;
case|case
name|DRIVE_APPS
case|:
name|apiProxy
operator|=
name|getClient
argument_list|()
operator|.
name|apps
argument_list|()
expr_stmt|;
break|break;
case|case
name|DRIVE_CHANNELS
case|:
name|apiProxy
operator|=
name|getClient
argument_list|()
operator|.
name|channels
argument_list|()
expr_stmt|;
break|break;
case|case
name|DRIVE_CHANGES
case|:
name|apiProxy
operator|=
name|getClient
argument_list|()
operator|.
name|changes
argument_list|()
expr_stmt|;
break|break;
case|case
name|DRIVE_COMMENTS
case|:
name|apiProxy
operator|=
name|getClient
argument_list|()
operator|.
name|comments
argument_list|()
expr_stmt|;
break|break;
case|case
name|DRIVE_PERMISSIONS
case|:
name|apiProxy
operator|=
name|getClient
argument_list|()
operator|.
name|permissions
argument_list|()
expr_stmt|;
break|break;
case|case
name|DRIVE_PROPERTIES
case|:
name|apiProxy
operator|=
name|getClient
argument_list|()
operator|.
name|properties
argument_list|()
expr_stmt|;
break|break;
case|case
name|DRIVE_REALTIME
case|:
name|apiProxy
operator|=
name|getClient
argument_list|()
operator|.
name|realtime
argument_list|()
expr_stmt|;
break|break;
case|case
name|DRIVE_REPLIES
case|:
name|apiProxy
operator|=
name|getClient
argument_list|()
operator|.
name|replies
argument_list|()
expr_stmt|;
break|break;
case|case
name|DRIVE_REVISIONS
case|:
name|apiProxy
operator|=
name|getClient
argument_list|()
operator|.
name|revisions
argument_list|()
expr_stmt|;
break|break;
case|case
name|DRIVE_CHILDREN
case|:
name|apiProxy
operator|=
name|getClient
argument_list|()
operator|.
name|children
argument_list|()
expr_stmt|;
break|break;
case|case
name|DRIVE_PARENTS
case|:
name|apiProxy
operator|=
name|getClient
argument_list|()
operator|.
name|parents
argument_list|()
expr_stmt|;
break|break;
default|default:
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Invalid API name "
operator|+
name|apiName
argument_list|)
throw|;
block|}
block|}
DECL|method|getClient ()
specifier|public
name|Drive
name|getClient
parameter_list|()
block|{
return|return
operator|(
operator|(
name|GoogleDriveComponent
operator|)
name|getComponent
argument_list|()
operator|)
operator|.
name|getClient
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getApiProxy (ApiMethod method, Map<String, Object> args)
specifier|public
name|Object
name|getApiProxy
parameter_list|(
name|ApiMethod
name|method
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|args
parameter_list|)
block|{
return|return
name|apiProxy
return|;
block|}
block|}
end_class

end_unit

