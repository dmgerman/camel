begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.openapi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|openapi
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|io
operator|.
name|apicurio
operator|.
name|datamodels
operator|.
name|core
operator|.
name|models
operator|.
name|common
operator|.
name|Info
import|;
end_import

begin_import
import|import
name|io
operator|.
name|apicurio
operator|.
name|datamodels
operator|.
name|core
operator|.
name|models
operator|.
name|common
operator|.
name|Server
import|;
end_import

begin_import
import|import
name|io
operator|.
name|apicurio
operator|.
name|datamodels
operator|.
name|openapi
operator|.
name|models
operator|.
name|OasDocument
import|;
end_import

begin_import
import|import
name|io
operator|.
name|apicurio
operator|.
name|datamodels
operator|.
name|openapi
operator|.
name|v2
operator|.
name|models
operator|.
name|Oas20Document
import|;
end_import

begin_import
import|import
name|io
operator|.
name|apicurio
operator|.
name|datamodels
operator|.
name|openapi
operator|.
name|v3
operator|.
name|models
operator|.
name|Oas30Document
import|;
end_import

begin_class
DECL|class|BeanConfig
specifier|public
class|class
name|BeanConfig
block|{
DECL|field|resourcePackage
name|String
name|resourcePackage
decl_stmt|;
DECL|field|schemes
name|String
index|[]
name|schemes
decl_stmt|;
DECL|field|title
name|String
name|title
decl_stmt|;
DECL|field|version
name|String
name|version
decl_stmt|;
DECL|field|description
name|String
name|description
decl_stmt|;
DECL|field|termsOfServiceUrl
name|String
name|termsOfServiceUrl
decl_stmt|;
DECL|field|contact
name|String
name|contact
decl_stmt|;
DECL|field|license
name|String
name|license
decl_stmt|;
DECL|field|licenseUrl
name|String
name|licenseUrl
decl_stmt|;
DECL|field|filterClass
name|String
name|filterClass
decl_stmt|;
DECL|field|info
name|Info
name|info
decl_stmt|;
DECL|field|host
name|String
name|host
decl_stmt|;
DECL|field|basePath
name|String
name|basePath
decl_stmt|;
DECL|field|scannerId
name|String
name|scannerId
decl_stmt|;
DECL|field|configId
name|String
name|configId
decl_stmt|;
DECL|field|contextId
name|String
name|contextId
decl_stmt|;
DECL|field|expandSuperTypes
name|boolean
name|expandSuperTypes
init|=
literal|true
decl_stmt|;
DECL|field|usePathBasedConfig
specifier|private
name|boolean
name|usePathBasedConfig
init|=
literal|false
decl_stmt|;
DECL|method|isUsePathBasedConfig ()
specifier|public
name|boolean
name|isUsePathBasedConfig
parameter_list|()
block|{
return|return
name|usePathBasedConfig
return|;
block|}
DECL|method|setUsePathBasedConfig (boolean usePathBasedConfig)
specifier|public
name|void
name|setUsePathBasedConfig
parameter_list|(
name|boolean
name|usePathBasedConfig
parameter_list|)
block|{
name|this
operator|.
name|usePathBasedConfig
operator|=
name|usePathBasedConfig
expr_stmt|;
block|}
DECL|method|getResourcePackage ()
specifier|public
name|String
name|getResourcePackage
parameter_list|()
block|{
return|return
name|this
operator|.
name|resourcePackage
return|;
block|}
DECL|method|setResourcePackage (String resourcePackage)
specifier|public
name|void
name|setResourcePackage
parameter_list|(
name|String
name|resourcePackage
parameter_list|)
block|{
name|this
operator|.
name|resourcePackage
operator|=
name|resourcePackage
expr_stmt|;
block|}
DECL|method|getSchemes ()
specifier|public
name|String
index|[]
name|getSchemes
parameter_list|()
block|{
return|return
name|schemes
return|;
block|}
DECL|method|setSchemes (String[] schemes)
specifier|public
name|void
name|setSchemes
parameter_list|(
name|String
index|[]
name|schemes
parameter_list|)
block|{
name|this
operator|.
name|schemes
operator|=
name|schemes
expr_stmt|;
block|}
DECL|method|getTitle ()
specifier|public
name|String
name|getTitle
parameter_list|()
block|{
return|return
name|title
return|;
block|}
DECL|method|setTitle (String title)
specifier|public
name|void
name|setTitle
parameter_list|(
name|String
name|title
parameter_list|)
block|{
name|this
operator|.
name|title
operator|=
name|title
expr_stmt|;
block|}
DECL|method|getVersion ()
specifier|public
name|String
name|getVersion
parameter_list|()
block|{
return|return
name|version
return|;
block|}
DECL|method|setVersion (String version)
specifier|public
name|void
name|setVersion
parameter_list|(
name|String
name|version
parameter_list|)
block|{
name|this
operator|.
name|version
operator|=
name|version
expr_stmt|;
block|}
DECL|method|getDescription ()
specifier|public
name|String
name|getDescription
parameter_list|()
block|{
return|return
name|description
return|;
block|}
DECL|method|setDescription (String description)
specifier|public
name|void
name|setDescription
parameter_list|(
name|String
name|description
parameter_list|)
block|{
name|this
operator|.
name|description
operator|=
name|description
expr_stmt|;
block|}
DECL|method|getTermsOfServiceUrl ()
specifier|public
name|String
name|getTermsOfServiceUrl
parameter_list|()
block|{
return|return
name|termsOfServiceUrl
return|;
block|}
DECL|method|setTermsOfServiceUrl (String termsOfServiceUrl)
specifier|public
name|void
name|setTermsOfServiceUrl
parameter_list|(
name|String
name|termsOfServiceUrl
parameter_list|)
block|{
name|this
operator|.
name|termsOfServiceUrl
operator|=
name|termsOfServiceUrl
expr_stmt|;
block|}
DECL|method|getContact ()
specifier|public
name|String
name|getContact
parameter_list|()
block|{
return|return
name|contact
return|;
block|}
DECL|method|setContact (String contact)
specifier|public
name|void
name|setContact
parameter_list|(
name|String
name|contact
parameter_list|)
block|{
name|this
operator|.
name|contact
operator|=
name|contact
expr_stmt|;
block|}
DECL|method|getLicense ()
specifier|public
name|String
name|getLicense
parameter_list|()
block|{
return|return
name|license
return|;
block|}
DECL|method|setLicense (String license)
specifier|public
name|void
name|setLicense
parameter_list|(
name|String
name|license
parameter_list|)
block|{
name|this
operator|.
name|license
operator|=
name|license
expr_stmt|;
block|}
DECL|method|getLicenseUrl ()
specifier|public
name|String
name|getLicenseUrl
parameter_list|()
block|{
return|return
name|licenseUrl
return|;
block|}
DECL|method|setLicenseUrl (String licenseUrl)
specifier|public
name|void
name|setLicenseUrl
parameter_list|(
name|String
name|licenseUrl
parameter_list|)
block|{
name|this
operator|.
name|licenseUrl
operator|=
name|licenseUrl
expr_stmt|;
block|}
DECL|method|getInfo ()
specifier|public
name|Info
name|getInfo
parameter_list|()
block|{
return|return
name|info
return|;
block|}
DECL|method|setInfo (Info info)
specifier|public
name|void
name|setInfo
parameter_list|(
name|Info
name|info
parameter_list|)
block|{
name|this
operator|.
name|info
operator|=
name|info
expr_stmt|;
block|}
DECL|method|getHost ()
specifier|public
name|String
name|getHost
parameter_list|()
block|{
return|return
name|host
return|;
block|}
DECL|method|setHost (String host)
specifier|public
name|void
name|setHost
parameter_list|(
name|String
name|host
parameter_list|)
block|{
name|this
operator|.
name|host
operator|=
name|host
expr_stmt|;
block|}
DECL|method|getContextId ()
specifier|public
name|String
name|getContextId
parameter_list|()
block|{
return|return
name|contextId
return|;
block|}
DECL|method|setContextId (String contextId)
specifier|public
name|void
name|setContextId
parameter_list|(
name|String
name|contextId
parameter_list|)
block|{
name|this
operator|.
name|contextId
operator|=
name|contextId
expr_stmt|;
block|}
DECL|method|getScannerId ()
specifier|public
name|String
name|getScannerId
parameter_list|()
block|{
return|return
name|scannerId
return|;
block|}
DECL|method|setScannerId (String scannerId)
specifier|public
name|void
name|setScannerId
parameter_list|(
name|String
name|scannerId
parameter_list|)
block|{
name|this
operator|.
name|scannerId
operator|=
name|scannerId
expr_stmt|;
block|}
DECL|method|getConfigId ()
specifier|public
name|String
name|getConfigId
parameter_list|()
block|{
return|return
name|configId
return|;
block|}
DECL|method|setConfigId (String configId)
specifier|public
name|void
name|setConfigId
parameter_list|(
name|String
name|configId
parameter_list|)
block|{
name|this
operator|.
name|configId
operator|=
name|configId
expr_stmt|;
block|}
DECL|method|getBasePath ()
specifier|public
name|String
name|getBasePath
parameter_list|()
block|{
return|return
name|basePath
return|;
block|}
DECL|method|getExpandSuperTypes ()
specifier|public
name|boolean
name|getExpandSuperTypes
parameter_list|()
block|{
return|return
name|expandSuperTypes
return|;
block|}
DECL|method|setExpandSuperTypes (boolean expandSuperTypes)
specifier|public
name|void
name|setExpandSuperTypes
parameter_list|(
name|boolean
name|expandSuperTypes
parameter_list|)
block|{
name|this
operator|.
name|expandSuperTypes
operator|=
name|expandSuperTypes
expr_stmt|;
block|}
DECL|method|setBasePath (String basePath)
specifier|public
name|void
name|setBasePath
parameter_list|(
name|String
name|basePath
parameter_list|)
block|{
if|if
condition|(
operator|!
literal|""
operator|.
name|equals
argument_list|(
name|basePath
argument_list|)
operator|&&
name|basePath
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
operator|!
name|basePath
operator|.
name|startsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|this
operator|.
name|basePath
operator|=
literal|"/"
operator|+
name|basePath
expr_stmt|;
block|}
else|else
block|{
name|this
operator|.
name|basePath
operator|=
name|basePath
expr_stmt|;
block|}
block|}
block|}
DECL|method|configure (OasDocument openApi)
specifier|public
name|OasDocument
name|configure
parameter_list|(
name|OasDocument
name|openApi
parameter_list|)
block|{
if|if
condition|(
name|openApi
operator|instanceof
name|Oas20Document
condition|)
block|{
name|configureOas20
argument_list|(
operator|(
name|Oas20Document
operator|)
name|openApi
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|openApi
operator|instanceof
name|Oas30Document
condition|)
block|{
name|configureOas30
argument_list|(
operator|(
name|Oas30Document
operator|)
name|openApi
argument_list|)
expr_stmt|;
block|}
return|return
name|openApi
return|;
block|}
DECL|method|configureOas30 (Oas30Document openApi)
specifier|private
name|void
name|configureOas30
parameter_list|(
name|Oas30Document
name|openApi
parameter_list|)
block|{
name|openApi
operator|.
name|info
operator|=
name|info
expr_stmt|;
name|Server
name|server
init|=
name|openApi
operator|.
name|createServer
argument_list|()
decl_stmt|;
name|String
name|serverUrl
init|=
operator|new
name|StringBuilder
argument_list|()
operator|.
name|append
argument_list|(
name|this
operator|.
name|schemes
index|[
literal|0
index|]
argument_list|)
operator|.
name|append
argument_list|(
literal|"://"
argument_list|)
operator|.
name|append
argument_list|(
name|this
operator|.
name|host
argument_list|)
operator|.
name|append
argument_list|(
name|this
operator|.
name|basePath
argument_list|)
operator|.
name|toString
argument_list|()
decl_stmt|;
name|server
operator|.
name|url
operator|=
name|serverUrl
expr_stmt|;
name|openApi
operator|.
name|addServer
argument_list|(
name|server
argument_list|)
expr_stmt|;
block|}
DECL|method|configureOas20 (Oas20Document openApi)
specifier|private
name|void
name|configureOas20
parameter_list|(
name|Oas20Document
name|openApi
parameter_list|)
block|{
if|if
condition|(
name|schemes
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|openApi
operator|.
name|schemes
operator|==
literal|null
condition|)
block|{
name|openApi
operator|.
name|schemes
operator|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
expr_stmt|;
block|}
for|for
control|(
name|String
name|scheme
range|:
name|schemes
control|)
block|{
name|openApi
operator|.
name|schemes
operator|.
name|add
argument_list|(
name|scheme
argument_list|)
expr_stmt|;
block|}
block|}
name|openApi
operator|.
name|info
operator|=
name|info
expr_stmt|;
name|openApi
operator|.
name|host
operator|=
name|host
expr_stmt|;
name|openApi
operator|.
name|basePath
operator|=
name|basePath
expr_stmt|;
block|}
DECL|method|isOpenApi3 ()
specifier|public
name|boolean
name|isOpenApi3
parameter_list|()
block|{
return|return
name|this
operator|.
name|version
operator|==
literal|null
operator|||
name|this
operator|.
name|version
operator|.
name|startsWith
argument_list|(
literal|"3"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

