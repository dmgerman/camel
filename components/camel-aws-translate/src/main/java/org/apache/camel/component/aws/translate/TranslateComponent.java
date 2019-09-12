begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.aws.translate
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|aws
operator|.
name|translate
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
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|translate
operator|.
name|AmazonTranslate
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
name|DefaultComponent
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
comment|/**  * For working with Amazon Translate.  */
end_comment

begin_class
annotation|@
name|Component
argument_list|(
literal|"aws-translate"
argument_list|)
DECL|class|TranslateComponent
specifier|public
class|class
name|TranslateComponent
extends|extends
name|DefaultComponent
block|{
annotation|@
name|Metadata
DECL|field|accessKey
specifier|private
name|String
name|accessKey
decl_stmt|;
annotation|@
name|Metadata
DECL|field|secretKey
specifier|private
name|String
name|secretKey
decl_stmt|;
annotation|@
name|Metadata
DECL|field|region
specifier|private
name|String
name|region
decl_stmt|;
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|configuration
specifier|private
name|TranslateConfiguration
name|configuration
decl_stmt|;
DECL|method|TranslateComponent ()
specifier|public
name|TranslateComponent
parameter_list|()
block|{
name|this
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|TranslateComponent (CamelContext context)
specifier|public
name|TranslateComponent
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
name|this
operator|.
name|configuration
operator|=
operator|new
name|TranslateConfiguration
argument_list|()
expr_stmt|;
name|registerExtension
argument_list|(
operator|new
name|TranslateComponentVerifierExtension
argument_list|()
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
name|TranslateConfiguration
name|configuration
init|=
name|this
operator|.
name|configuration
operator|.
name|copy
argument_list|()
decl_stmt|;
name|setProperties
argument_list|(
name|configuration
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|configuration
operator|.
name|getAccessKey
argument_list|()
argument_list|)
condition|)
block|{
name|setAccessKey
argument_list|(
name|accessKey
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|configuration
operator|.
name|getSecretKey
argument_list|()
argument_list|)
condition|)
block|{
name|setSecretKey
argument_list|(
name|secretKey
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|configuration
operator|.
name|getRegion
argument_list|()
argument_list|)
condition|)
block|{
name|setRegion
argument_list|(
name|region
argument_list|)
expr_stmt|;
block|}
name|checkAndSetRegistryClient
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
if|if
condition|(
name|configuration
operator|.
name|getTranslateClient
argument_list|()
operator|==
literal|null
operator|&&
operator|(
name|configuration
operator|.
name|getAccessKey
argument_list|()
operator|==
literal|null
operator|||
name|configuration
operator|.
name|getSecretKey
argument_list|()
operator|==
literal|null
operator|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Amazon translate client or accessKey and secretKey must be specified"
argument_list|)
throw|;
block|}
name|TranslateEndpoint
name|endpoint
init|=
operator|new
name|TranslateEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|,
name|configuration
argument_list|)
decl_stmt|;
return|return
name|endpoint
return|;
block|}
DECL|method|getConfiguration ()
specifier|public
name|TranslateConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
comment|/**      * The AWS Translate default configuration      */
DECL|method|setConfiguration (TranslateConfiguration configuration)
specifier|public
name|void
name|setConfiguration
parameter_list|(
name|TranslateConfiguration
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
DECL|method|getAccessKey ()
specifier|public
name|String
name|getAccessKey
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getAccessKey
argument_list|()
return|;
block|}
comment|/**      * Amazon AWS Access Key      */
DECL|method|setAccessKey (String accessKey)
specifier|public
name|void
name|setAccessKey
parameter_list|(
name|String
name|accessKey
parameter_list|)
block|{
name|configuration
operator|.
name|setAccessKey
argument_list|(
name|accessKey
argument_list|)
expr_stmt|;
block|}
DECL|method|getSecretKey ()
specifier|public
name|String
name|getSecretKey
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getSecretKey
argument_list|()
return|;
block|}
comment|/**      * Amazon AWS Secret Key      */
DECL|method|setSecretKey (String secretKey)
specifier|public
name|void
name|setSecretKey
parameter_list|(
name|String
name|secretKey
parameter_list|)
block|{
name|configuration
operator|.
name|setSecretKey
argument_list|(
name|secretKey
argument_list|)
expr_stmt|;
block|}
DECL|method|getRegion ()
specifier|public
name|String
name|getRegion
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getRegion
argument_list|()
return|;
block|}
comment|/**      * The region in which Translate client needs to work      */
DECL|method|setRegion (String region)
specifier|public
name|void
name|setRegion
parameter_list|(
name|String
name|region
parameter_list|)
block|{
name|configuration
operator|.
name|setRegion
argument_list|(
name|region
argument_list|)
expr_stmt|;
block|}
DECL|method|checkAndSetRegistryClient (TranslateConfiguration configuration)
specifier|private
name|void
name|checkAndSetRegistryClient
parameter_list|(
name|TranslateConfiguration
name|configuration
parameter_list|)
block|{
name|Set
argument_list|<
name|AmazonTranslate
argument_list|>
name|clients
init|=
name|getCamelContext
argument_list|()
operator|.
name|getRegistry
argument_list|()
operator|.
name|findByType
argument_list|(
name|AmazonTranslate
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|clients
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
name|configuration
operator|.
name|setTranslateClient
argument_list|(
name|clients
operator|.
name|stream
argument_list|()
operator|.
name|findFirst
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

