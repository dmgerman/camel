begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.salesforce.springboot
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|salesforce
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
name|fasterxml
operator|.
name|jackson
operator|.
name|databind
operator|.
name|ObjectMapper
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
name|salesforce
operator|.
name|SalesforceEndpointConfig
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
name|salesforce
operator|.
name|SalesforceHttpClient
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
name|salesforce
operator|.
name|SalesforceLoginConfig
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
name|salesforce
operator|.
name|api
operator|.
name|dto
operator|.
name|analytics
operator|.
name|reports
operator|.
name|ReportMetadata
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
name|salesforce
operator|.
name|api
operator|.
name|dto
operator|.
name|bulk
operator|.
name|ContentType
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
name|salesforce
operator|.
name|internal
operator|.
name|PayloadFormat
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
name|salesforce
operator|.
name|internal
operator|.
name|dto
operator|.
name|NotifyForFieldsEnum
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
name|salesforce
operator|.
name|internal
operator|.
name|dto
operator|.
name|NotifyForOperationsEnum
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
name|jsse
operator|.
name|SSLContextParameters
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
comment|/**  * The salesforce component is used for integrating Camel with the massive  * Salesforce API.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_class
annotation|@
name|ConfigurationProperties
argument_list|(
name|prefix
operator|=
literal|"camel.component.salesforce"
argument_list|)
DECL|class|SalesforceComponentConfiguration
specifier|public
class|class
name|SalesforceComponentConfiguration
block|{
comment|/**      * To use the shared SalesforceLoginConfig as login configuration.      * Properties of the shared configuration can also be set individually.      */
DECL|field|loginConfig
specifier|private
name|SalesforceLoginConfig
name|loginConfig
decl_stmt|;
comment|/**      * To use the shared SalesforceEndpointConfig as configuration. Properties      * of the shared configuration can also be set individually.      */
DECL|field|config
specifier|private
name|SalesforceEndpointConfig
name|config
decl_stmt|;
comment|/**      * Used for configuring HTTP client properties as key/value pairs      */
DECL|field|httpClientProperties
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|httpClientProperties
decl_stmt|;
comment|/**      * To configure security using SSLContextParameters      */
DECL|field|sslContextParameters
specifier|private
name|SSLContextParameters
name|sslContextParameters
decl_stmt|;
comment|/**      * To configure HTTP proxy host      */
DECL|field|httpProxyHost
specifier|private
name|String
name|httpProxyHost
decl_stmt|;
comment|/**      * To configure HTTP proxy port      */
DECL|field|httpProxyPort
specifier|private
name|Integer
name|httpProxyPort
decl_stmt|;
comment|/**      * To configure HTTP proxy username      */
DECL|field|httpProxyUsername
specifier|private
name|String
name|httpProxyUsername
decl_stmt|;
comment|/**      * To configure HTTP proxy password      */
DECL|field|httpProxyPassword
specifier|private
name|String
name|httpProxyPassword
decl_stmt|;
comment|/**      * Enable for Socks4 proxy false by default      */
DECL|field|isHttpProxySocks4
specifier|private
name|Boolean
name|isHttpProxySocks4
decl_stmt|;
comment|/**      * Enable for TLS connections true by default      */
DECL|field|isHttpProxySecure
specifier|private
name|Boolean
name|isHttpProxySecure
decl_stmt|;
comment|/**      * HTTP proxy included addresses      */
DECL|field|httpProxyIncludedAddresses
specifier|private
name|Set
argument_list|<
name|String
argument_list|>
name|httpProxyIncludedAddresses
decl_stmt|;
comment|/**      * HTTP proxy excluded addresses      */
DECL|field|httpProxyExcludedAddresses
specifier|private
name|Set
argument_list|<
name|String
argument_list|>
name|httpProxyExcludedAddresses
decl_stmt|;
comment|/**      * HTTP proxy authentication URI      */
DECL|field|httpProxyAuthUri
specifier|private
name|String
name|httpProxyAuthUri
decl_stmt|;
comment|/**      * HTTP proxy authentication realm      */
DECL|field|httpProxyRealm
specifier|private
name|String
name|httpProxyRealm
decl_stmt|;
comment|/**      * Use HTTP proxy Digest authentication false by default      */
DECL|field|httpProxyUseDigestAuth
specifier|private
name|Boolean
name|httpProxyUseDigestAuth
decl_stmt|;
comment|/**      * Package names to scan for DTO classes (multiple packages can be separated      * by comma).      */
DECL|field|packages
specifier|private
name|String
index|[]
name|packages
decl_stmt|;
comment|/**      * Salesforce login URL defaults to https://login.salesforce.com      */
DECL|field|loginUrl
specifier|private
name|String
name|loginUrl
decl_stmt|;
comment|/**      * Salesforce connected application Consumer Key      */
DECL|field|clientId
specifier|private
name|String
name|clientId
decl_stmt|;
comment|/**      * Salesforce connected application Consumer Secret      */
DECL|field|clientSecret
specifier|private
name|String
name|clientSecret
decl_stmt|;
comment|/**      * Salesforce account user name      */
DECL|field|userName
specifier|private
name|String
name|userName
decl_stmt|;
comment|/**      * Salesforce account password      */
DECL|field|password
specifier|private
name|String
name|password
decl_stmt|;
comment|/**      * Flag to enable/disable lazy OAuth default is false. When enabled OAuth      * token retrieval or generation is not done until the first API call      */
DECL|field|lazyLogin
specifier|private
name|Boolean
name|lazyLogin
decl_stmt|;
comment|/**      * Payload format to use for Salesforce API calls either JSON or XML      * defaults to JSON      */
DECL|field|format
specifier|private
name|PayloadFormat
name|format
decl_stmt|;
comment|/**      * Salesforce API version defaults to      * SalesforceEndpointConfig.DEFAULT_VERSION      */
DECL|field|apiVersion
specifier|private
name|String
name|apiVersion
decl_stmt|;
comment|/**      * SObject name if required or supported by API      */
DECL|field|sObjectName
specifier|private
name|String
name|sObjectName
decl_stmt|;
comment|/**      * SObject ID if required by API      */
DECL|field|sObjectId
specifier|private
name|String
name|sObjectId
decl_stmt|;
comment|/**      * SObject fields to retrieve      */
DECL|field|sObjectFields
specifier|private
name|String
name|sObjectFields
decl_stmt|;
comment|/**      * SObject external ID field name      */
DECL|field|sObjectIdName
specifier|private
name|String
name|sObjectIdName
decl_stmt|;
comment|/**      * SObject external ID field value      */
DECL|field|sObjectIdValue
specifier|private
name|String
name|sObjectIdValue
decl_stmt|;
comment|/**      * SObject blob field name      */
DECL|field|sObjectBlobFieldName
specifier|private
name|String
name|sObjectBlobFieldName
decl_stmt|;
comment|/**      * Fully qualified SObject class name usually generated using      * camel-salesforce-maven-plugin      */
DECL|field|sObjectClass
specifier|private
name|String
name|sObjectClass
decl_stmt|;
comment|/**      * Salesforce SOQL query string      */
DECL|field|sObjectQuery
specifier|private
name|String
name|sObjectQuery
decl_stmt|;
comment|/**      * Salesforce SOSL search string      */
DECL|field|sObjectSearch
specifier|private
name|String
name|sObjectSearch
decl_stmt|;
comment|/**      * APEX method name      */
DECL|field|apexMethod
specifier|private
name|String
name|apexMethod
decl_stmt|;
comment|/**      * APEX method URL      */
DECL|field|apexUrl
specifier|private
name|String
name|apexUrl
decl_stmt|;
comment|/**      * Query params for APEX method      */
DECL|field|apexQueryParams
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|apexQueryParams
decl_stmt|;
comment|/**      * Bulk API content type one of XML CSV ZIP_XML ZIP_CSV      */
DECL|field|contentType
specifier|private
name|ContentType
name|contentType
decl_stmt|;
comment|/**      * Bulk API Job ID      */
DECL|field|jobId
specifier|private
name|String
name|jobId
decl_stmt|;
comment|/**      * Bulk API Batch ID      */
DECL|field|batchId
specifier|private
name|String
name|batchId
decl_stmt|;
comment|/**      * Bulk API Result ID      */
DECL|field|resultId
specifier|private
name|String
name|resultId
decl_stmt|;
comment|/**      * Whether to update an existing Push Topic when using the Streaming API      * defaults to false      */
DECL|field|updateTopic
specifier|private
name|Boolean
name|updateTopic
decl_stmt|;
comment|/**      * Notify for fields options are ALL REFERENCED SELECT WHERE      */
DECL|field|notifyForFields
specifier|private
name|NotifyForFieldsEnum
name|notifyForFields
decl_stmt|;
comment|/**      * Notify for operations options are ALL CREATE EXTENDED UPDATE (API version      * 29.0)      */
DECL|field|notifyForOperations
specifier|private
name|NotifyForOperationsEnum
name|notifyForOperations
decl_stmt|;
comment|/**      * Notify for create operation defaults to false (API version = 29.0)      */
DECL|field|notifyForOperationCreate
specifier|private
name|Boolean
name|notifyForOperationCreate
decl_stmt|;
comment|/**      * Notify for update operation defaults to false (API version = 29.0)      */
DECL|field|notifyForOperationUpdate
specifier|private
name|Boolean
name|notifyForOperationUpdate
decl_stmt|;
comment|/**      * Notify for delete operation defaults to false (API version = 29.0)      */
DECL|field|notifyForOperationDelete
specifier|private
name|Boolean
name|notifyForOperationDelete
decl_stmt|;
comment|/**      * Notify for un-delete operation defaults to false (API version = 29.0)      */
DECL|field|notifyForOperationUndelete
specifier|private
name|Boolean
name|notifyForOperationUndelete
decl_stmt|;
comment|/**      * Salesforce1 Analytics report Id      */
DECL|field|reportId
specifier|private
name|String
name|reportId
decl_stmt|;
comment|/**      * Include details in Salesforce1 Analytics report defaults to false.      */
DECL|field|includeDetails
specifier|private
name|Boolean
name|includeDetails
decl_stmt|;
comment|/**      * Salesforce1 Analytics report metadata for filtering      */
DECL|field|reportMetadata
specifier|private
name|ReportMetadata
name|reportMetadata
decl_stmt|;
comment|/**      * Salesforce1 Analytics report execution instance ID      */
DECL|field|instanceId
specifier|private
name|String
name|instanceId
decl_stmt|;
comment|/**      * Custom Jetty Http Client to use to connect to Salesforce.      */
DECL|field|httpClient
specifier|private
name|SalesforceHttpClient
name|httpClient
decl_stmt|;
comment|/**      * Custom Jackson ObjectMapper to use when serializing/deserializing      * Salesforce objects.      */
DECL|field|objectMapper
specifier|private
name|ObjectMapper
name|objectMapper
decl_stmt|;
comment|/**      * Default replayId setting if no value is found in link initialReplayIdMap      */
DECL|field|defaultReplayId
specifier|private
name|Integer
name|defaultReplayId
decl_stmt|;
comment|/**      * Replay IDs to start from per channel name.      */
DECL|field|initialReplayIdMap
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|initialReplayIdMap
decl_stmt|;
comment|/**      * Backoff interval increment for Streaming connection restart attempts for      * failures beyond CometD auto-reconnect.      */
DECL|field|backoffIncrement
specifier|private
name|long
name|backoffIncrement
decl_stmt|;
comment|/**      * Maximum backoff interval for Streaming connection restart attempts for      * failures beyond CometD auto-reconnect.      */
DECL|field|maxBackoff
specifier|private
name|long
name|maxBackoff
decl_stmt|;
DECL|method|getLoginConfig ()
specifier|public
name|SalesforceLoginConfig
name|getLoginConfig
parameter_list|()
block|{
return|return
name|loginConfig
return|;
block|}
DECL|method|setLoginConfig (SalesforceLoginConfig loginConfig)
specifier|public
name|void
name|setLoginConfig
parameter_list|(
name|SalesforceLoginConfig
name|loginConfig
parameter_list|)
block|{
name|this
operator|.
name|loginConfig
operator|=
name|loginConfig
expr_stmt|;
block|}
DECL|method|getConfig ()
specifier|public
name|SalesforceEndpointConfig
name|getConfig
parameter_list|()
block|{
return|return
name|config
return|;
block|}
DECL|method|setConfig (SalesforceEndpointConfig config)
specifier|public
name|void
name|setConfig
parameter_list|(
name|SalesforceEndpointConfig
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
DECL|method|getHttpClientProperties ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getHttpClientProperties
parameter_list|()
block|{
return|return
name|httpClientProperties
return|;
block|}
DECL|method|setHttpClientProperties (Map<String, Object> httpClientProperties)
specifier|public
name|void
name|setHttpClientProperties
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|httpClientProperties
parameter_list|)
block|{
name|this
operator|.
name|httpClientProperties
operator|=
name|httpClientProperties
expr_stmt|;
block|}
DECL|method|getSslContextParameters ()
specifier|public
name|SSLContextParameters
name|getSslContextParameters
parameter_list|()
block|{
return|return
name|sslContextParameters
return|;
block|}
DECL|method|setSslContextParameters ( SSLContextParameters sslContextParameters)
specifier|public
name|void
name|setSslContextParameters
parameter_list|(
name|SSLContextParameters
name|sslContextParameters
parameter_list|)
block|{
name|this
operator|.
name|sslContextParameters
operator|=
name|sslContextParameters
expr_stmt|;
block|}
DECL|method|getHttpProxyHost ()
specifier|public
name|String
name|getHttpProxyHost
parameter_list|()
block|{
return|return
name|httpProxyHost
return|;
block|}
DECL|method|setHttpProxyHost (String httpProxyHost)
specifier|public
name|void
name|setHttpProxyHost
parameter_list|(
name|String
name|httpProxyHost
parameter_list|)
block|{
name|this
operator|.
name|httpProxyHost
operator|=
name|httpProxyHost
expr_stmt|;
block|}
DECL|method|getHttpProxyPort ()
specifier|public
name|Integer
name|getHttpProxyPort
parameter_list|()
block|{
return|return
name|httpProxyPort
return|;
block|}
DECL|method|setHttpProxyPort (Integer httpProxyPort)
specifier|public
name|void
name|setHttpProxyPort
parameter_list|(
name|Integer
name|httpProxyPort
parameter_list|)
block|{
name|this
operator|.
name|httpProxyPort
operator|=
name|httpProxyPort
expr_stmt|;
block|}
DECL|method|getHttpProxyUsername ()
specifier|public
name|String
name|getHttpProxyUsername
parameter_list|()
block|{
return|return
name|httpProxyUsername
return|;
block|}
DECL|method|setHttpProxyUsername (String httpProxyUsername)
specifier|public
name|void
name|setHttpProxyUsername
parameter_list|(
name|String
name|httpProxyUsername
parameter_list|)
block|{
name|this
operator|.
name|httpProxyUsername
operator|=
name|httpProxyUsername
expr_stmt|;
block|}
DECL|method|getHttpProxyPassword ()
specifier|public
name|String
name|getHttpProxyPassword
parameter_list|()
block|{
return|return
name|httpProxyPassword
return|;
block|}
DECL|method|setHttpProxyPassword (String httpProxyPassword)
specifier|public
name|void
name|setHttpProxyPassword
parameter_list|(
name|String
name|httpProxyPassword
parameter_list|)
block|{
name|this
operator|.
name|httpProxyPassword
operator|=
name|httpProxyPassword
expr_stmt|;
block|}
DECL|method|getIsHttpProxySocks4 ()
specifier|public
name|Boolean
name|getIsHttpProxySocks4
parameter_list|()
block|{
return|return
name|isHttpProxySocks4
return|;
block|}
DECL|method|setIsHttpProxySocks4 (Boolean isHttpProxySocks4)
specifier|public
name|void
name|setIsHttpProxySocks4
parameter_list|(
name|Boolean
name|isHttpProxySocks4
parameter_list|)
block|{
name|this
operator|.
name|isHttpProxySocks4
operator|=
name|isHttpProxySocks4
expr_stmt|;
block|}
DECL|method|getIsHttpProxySecure ()
specifier|public
name|Boolean
name|getIsHttpProxySecure
parameter_list|()
block|{
return|return
name|isHttpProxySecure
return|;
block|}
DECL|method|setIsHttpProxySecure (Boolean isHttpProxySecure)
specifier|public
name|void
name|setIsHttpProxySecure
parameter_list|(
name|Boolean
name|isHttpProxySecure
parameter_list|)
block|{
name|this
operator|.
name|isHttpProxySecure
operator|=
name|isHttpProxySecure
expr_stmt|;
block|}
DECL|method|getHttpProxyIncludedAddresses ()
specifier|public
name|Set
argument_list|<
name|String
argument_list|>
name|getHttpProxyIncludedAddresses
parameter_list|()
block|{
return|return
name|httpProxyIncludedAddresses
return|;
block|}
DECL|method|setHttpProxyIncludedAddresses ( Set<String> httpProxyIncludedAddresses)
specifier|public
name|void
name|setHttpProxyIncludedAddresses
parameter_list|(
name|Set
argument_list|<
name|String
argument_list|>
name|httpProxyIncludedAddresses
parameter_list|)
block|{
name|this
operator|.
name|httpProxyIncludedAddresses
operator|=
name|httpProxyIncludedAddresses
expr_stmt|;
block|}
DECL|method|getHttpProxyExcludedAddresses ()
specifier|public
name|Set
argument_list|<
name|String
argument_list|>
name|getHttpProxyExcludedAddresses
parameter_list|()
block|{
return|return
name|httpProxyExcludedAddresses
return|;
block|}
DECL|method|setHttpProxyExcludedAddresses ( Set<String> httpProxyExcludedAddresses)
specifier|public
name|void
name|setHttpProxyExcludedAddresses
parameter_list|(
name|Set
argument_list|<
name|String
argument_list|>
name|httpProxyExcludedAddresses
parameter_list|)
block|{
name|this
operator|.
name|httpProxyExcludedAddresses
operator|=
name|httpProxyExcludedAddresses
expr_stmt|;
block|}
DECL|method|getHttpProxyAuthUri ()
specifier|public
name|String
name|getHttpProxyAuthUri
parameter_list|()
block|{
return|return
name|httpProxyAuthUri
return|;
block|}
DECL|method|setHttpProxyAuthUri (String httpProxyAuthUri)
specifier|public
name|void
name|setHttpProxyAuthUri
parameter_list|(
name|String
name|httpProxyAuthUri
parameter_list|)
block|{
name|this
operator|.
name|httpProxyAuthUri
operator|=
name|httpProxyAuthUri
expr_stmt|;
block|}
DECL|method|getHttpProxyRealm ()
specifier|public
name|String
name|getHttpProxyRealm
parameter_list|()
block|{
return|return
name|httpProxyRealm
return|;
block|}
DECL|method|setHttpProxyRealm (String httpProxyRealm)
specifier|public
name|void
name|setHttpProxyRealm
parameter_list|(
name|String
name|httpProxyRealm
parameter_list|)
block|{
name|this
operator|.
name|httpProxyRealm
operator|=
name|httpProxyRealm
expr_stmt|;
block|}
DECL|method|getHttpProxyUseDigestAuth ()
specifier|public
name|Boolean
name|getHttpProxyUseDigestAuth
parameter_list|()
block|{
return|return
name|httpProxyUseDigestAuth
return|;
block|}
DECL|method|setHttpProxyUseDigestAuth (Boolean httpProxyUseDigestAuth)
specifier|public
name|void
name|setHttpProxyUseDigestAuth
parameter_list|(
name|Boolean
name|httpProxyUseDigestAuth
parameter_list|)
block|{
name|this
operator|.
name|httpProxyUseDigestAuth
operator|=
name|httpProxyUseDigestAuth
expr_stmt|;
block|}
DECL|method|getPackages ()
specifier|public
name|String
index|[]
name|getPackages
parameter_list|()
block|{
return|return
name|packages
return|;
block|}
DECL|method|setPackages (String[] packages)
specifier|public
name|void
name|setPackages
parameter_list|(
name|String
index|[]
name|packages
parameter_list|)
block|{
name|this
operator|.
name|packages
operator|=
name|packages
expr_stmt|;
block|}
DECL|method|getLoginUrl ()
specifier|public
name|String
name|getLoginUrl
parameter_list|()
block|{
return|return
name|loginUrl
return|;
block|}
DECL|method|setLoginUrl (String loginUrl)
specifier|public
name|void
name|setLoginUrl
parameter_list|(
name|String
name|loginUrl
parameter_list|)
block|{
name|this
operator|.
name|loginUrl
operator|=
name|loginUrl
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
DECL|method|getUserName ()
specifier|public
name|String
name|getUserName
parameter_list|()
block|{
return|return
name|userName
return|;
block|}
DECL|method|setUserName (String userName)
specifier|public
name|void
name|setUserName
parameter_list|(
name|String
name|userName
parameter_list|)
block|{
name|this
operator|.
name|userName
operator|=
name|userName
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
DECL|method|getLazyLogin ()
specifier|public
name|Boolean
name|getLazyLogin
parameter_list|()
block|{
return|return
name|lazyLogin
return|;
block|}
DECL|method|setLazyLogin (Boolean lazyLogin)
specifier|public
name|void
name|setLazyLogin
parameter_list|(
name|Boolean
name|lazyLogin
parameter_list|)
block|{
name|this
operator|.
name|lazyLogin
operator|=
name|lazyLogin
expr_stmt|;
block|}
DECL|method|getFormat ()
specifier|public
name|PayloadFormat
name|getFormat
parameter_list|()
block|{
return|return
name|format
return|;
block|}
DECL|method|setFormat (PayloadFormat format)
specifier|public
name|void
name|setFormat
parameter_list|(
name|PayloadFormat
name|format
parameter_list|)
block|{
name|this
operator|.
name|format
operator|=
name|format
expr_stmt|;
block|}
DECL|method|getApiVersion ()
specifier|public
name|String
name|getApiVersion
parameter_list|()
block|{
return|return
name|apiVersion
return|;
block|}
DECL|method|setApiVersion (String apiVersion)
specifier|public
name|void
name|setApiVersion
parameter_list|(
name|String
name|apiVersion
parameter_list|)
block|{
name|this
operator|.
name|apiVersion
operator|=
name|apiVersion
expr_stmt|;
block|}
DECL|method|getSObjectName ()
specifier|public
name|String
name|getSObjectName
parameter_list|()
block|{
return|return
name|sObjectName
return|;
block|}
DECL|method|setSObjectName (String sObjectName)
specifier|public
name|void
name|setSObjectName
parameter_list|(
name|String
name|sObjectName
parameter_list|)
block|{
name|this
operator|.
name|sObjectName
operator|=
name|sObjectName
expr_stmt|;
block|}
DECL|method|getSObjectId ()
specifier|public
name|String
name|getSObjectId
parameter_list|()
block|{
return|return
name|sObjectId
return|;
block|}
DECL|method|setSObjectId (String sObjectId)
specifier|public
name|void
name|setSObjectId
parameter_list|(
name|String
name|sObjectId
parameter_list|)
block|{
name|this
operator|.
name|sObjectId
operator|=
name|sObjectId
expr_stmt|;
block|}
DECL|method|getSObjectFields ()
specifier|public
name|String
name|getSObjectFields
parameter_list|()
block|{
return|return
name|sObjectFields
return|;
block|}
DECL|method|setSObjectFields (String sObjectFields)
specifier|public
name|void
name|setSObjectFields
parameter_list|(
name|String
name|sObjectFields
parameter_list|)
block|{
name|this
operator|.
name|sObjectFields
operator|=
name|sObjectFields
expr_stmt|;
block|}
DECL|method|getSObjectIdName ()
specifier|public
name|String
name|getSObjectIdName
parameter_list|()
block|{
return|return
name|sObjectIdName
return|;
block|}
DECL|method|setSObjectIdName (String sObjectIdName)
specifier|public
name|void
name|setSObjectIdName
parameter_list|(
name|String
name|sObjectIdName
parameter_list|)
block|{
name|this
operator|.
name|sObjectIdName
operator|=
name|sObjectIdName
expr_stmt|;
block|}
DECL|method|getSObjectIdValue ()
specifier|public
name|String
name|getSObjectIdValue
parameter_list|()
block|{
return|return
name|sObjectIdValue
return|;
block|}
DECL|method|setSObjectIdValue (String sObjectIdValue)
specifier|public
name|void
name|setSObjectIdValue
parameter_list|(
name|String
name|sObjectIdValue
parameter_list|)
block|{
name|this
operator|.
name|sObjectIdValue
operator|=
name|sObjectIdValue
expr_stmt|;
block|}
DECL|method|getSObjectBlobFieldName ()
specifier|public
name|String
name|getSObjectBlobFieldName
parameter_list|()
block|{
return|return
name|sObjectBlobFieldName
return|;
block|}
DECL|method|setSObjectBlobFieldName (String sObjectBlobFieldName)
specifier|public
name|void
name|setSObjectBlobFieldName
parameter_list|(
name|String
name|sObjectBlobFieldName
parameter_list|)
block|{
name|this
operator|.
name|sObjectBlobFieldName
operator|=
name|sObjectBlobFieldName
expr_stmt|;
block|}
DECL|method|getSObjectClass ()
specifier|public
name|String
name|getSObjectClass
parameter_list|()
block|{
return|return
name|sObjectClass
return|;
block|}
DECL|method|setSObjectClass (String sObjectClass)
specifier|public
name|void
name|setSObjectClass
parameter_list|(
name|String
name|sObjectClass
parameter_list|)
block|{
name|this
operator|.
name|sObjectClass
operator|=
name|sObjectClass
expr_stmt|;
block|}
DECL|method|getSObjectQuery ()
specifier|public
name|String
name|getSObjectQuery
parameter_list|()
block|{
return|return
name|sObjectQuery
return|;
block|}
DECL|method|setSObjectQuery (String sObjectQuery)
specifier|public
name|void
name|setSObjectQuery
parameter_list|(
name|String
name|sObjectQuery
parameter_list|)
block|{
name|this
operator|.
name|sObjectQuery
operator|=
name|sObjectQuery
expr_stmt|;
block|}
DECL|method|getSObjectSearch ()
specifier|public
name|String
name|getSObjectSearch
parameter_list|()
block|{
return|return
name|sObjectSearch
return|;
block|}
DECL|method|setSObjectSearch (String sObjectSearch)
specifier|public
name|void
name|setSObjectSearch
parameter_list|(
name|String
name|sObjectSearch
parameter_list|)
block|{
name|this
operator|.
name|sObjectSearch
operator|=
name|sObjectSearch
expr_stmt|;
block|}
DECL|method|getApexMethod ()
specifier|public
name|String
name|getApexMethod
parameter_list|()
block|{
return|return
name|apexMethod
return|;
block|}
DECL|method|setApexMethod (String apexMethod)
specifier|public
name|void
name|setApexMethod
parameter_list|(
name|String
name|apexMethod
parameter_list|)
block|{
name|this
operator|.
name|apexMethod
operator|=
name|apexMethod
expr_stmt|;
block|}
DECL|method|getApexUrl ()
specifier|public
name|String
name|getApexUrl
parameter_list|()
block|{
return|return
name|apexUrl
return|;
block|}
DECL|method|setApexUrl (String apexUrl)
specifier|public
name|void
name|setApexUrl
parameter_list|(
name|String
name|apexUrl
parameter_list|)
block|{
name|this
operator|.
name|apexUrl
operator|=
name|apexUrl
expr_stmt|;
block|}
DECL|method|getApexQueryParams ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getApexQueryParams
parameter_list|()
block|{
return|return
name|apexQueryParams
return|;
block|}
DECL|method|setApexQueryParams (Map<String, Object> apexQueryParams)
specifier|public
name|void
name|setApexQueryParams
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|apexQueryParams
parameter_list|)
block|{
name|this
operator|.
name|apexQueryParams
operator|=
name|apexQueryParams
expr_stmt|;
block|}
DECL|method|getContentType ()
specifier|public
name|ContentType
name|getContentType
parameter_list|()
block|{
return|return
name|contentType
return|;
block|}
DECL|method|setContentType (ContentType contentType)
specifier|public
name|void
name|setContentType
parameter_list|(
name|ContentType
name|contentType
parameter_list|)
block|{
name|this
operator|.
name|contentType
operator|=
name|contentType
expr_stmt|;
block|}
DECL|method|getJobId ()
specifier|public
name|String
name|getJobId
parameter_list|()
block|{
return|return
name|jobId
return|;
block|}
DECL|method|setJobId (String jobId)
specifier|public
name|void
name|setJobId
parameter_list|(
name|String
name|jobId
parameter_list|)
block|{
name|this
operator|.
name|jobId
operator|=
name|jobId
expr_stmt|;
block|}
DECL|method|getBatchId ()
specifier|public
name|String
name|getBatchId
parameter_list|()
block|{
return|return
name|batchId
return|;
block|}
DECL|method|setBatchId (String batchId)
specifier|public
name|void
name|setBatchId
parameter_list|(
name|String
name|batchId
parameter_list|)
block|{
name|this
operator|.
name|batchId
operator|=
name|batchId
expr_stmt|;
block|}
DECL|method|getResultId ()
specifier|public
name|String
name|getResultId
parameter_list|()
block|{
return|return
name|resultId
return|;
block|}
DECL|method|setResultId (String resultId)
specifier|public
name|void
name|setResultId
parameter_list|(
name|String
name|resultId
parameter_list|)
block|{
name|this
operator|.
name|resultId
operator|=
name|resultId
expr_stmt|;
block|}
DECL|method|getUpdateTopic ()
specifier|public
name|Boolean
name|getUpdateTopic
parameter_list|()
block|{
return|return
name|updateTopic
return|;
block|}
DECL|method|setUpdateTopic (Boolean updateTopic)
specifier|public
name|void
name|setUpdateTopic
parameter_list|(
name|Boolean
name|updateTopic
parameter_list|)
block|{
name|this
operator|.
name|updateTopic
operator|=
name|updateTopic
expr_stmt|;
block|}
DECL|method|getNotifyForFields ()
specifier|public
name|NotifyForFieldsEnum
name|getNotifyForFields
parameter_list|()
block|{
return|return
name|notifyForFields
return|;
block|}
DECL|method|setNotifyForFields (NotifyForFieldsEnum notifyForFields)
specifier|public
name|void
name|setNotifyForFields
parameter_list|(
name|NotifyForFieldsEnum
name|notifyForFields
parameter_list|)
block|{
name|this
operator|.
name|notifyForFields
operator|=
name|notifyForFields
expr_stmt|;
block|}
DECL|method|getNotifyForOperations ()
specifier|public
name|NotifyForOperationsEnum
name|getNotifyForOperations
parameter_list|()
block|{
return|return
name|notifyForOperations
return|;
block|}
DECL|method|setNotifyForOperations ( NotifyForOperationsEnum notifyForOperations)
specifier|public
name|void
name|setNotifyForOperations
parameter_list|(
name|NotifyForOperationsEnum
name|notifyForOperations
parameter_list|)
block|{
name|this
operator|.
name|notifyForOperations
operator|=
name|notifyForOperations
expr_stmt|;
block|}
DECL|method|getNotifyForOperationCreate ()
specifier|public
name|Boolean
name|getNotifyForOperationCreate
parameter_list|()
block|{
return|return
name|notifyForOperationCreate
return|;
block|}
DECL|method|setNotifyForOperationCreate (Boolean notifyForOperationCreate)
specifier|public
name|void
name|setNotifyForOperationCreate
parameter_list|(
name|Boolean
name|notifyForOperationCreate
parameter_list|)
block|{
name|this
operator|.
name|notifyForOperationCreate
operator|=
name|notifyForOperationCreate
expr_stmt|;
block|}
DECL|method|getNotifyForOperationUpdate ()
specifier|public
name|Boolean
name|getNotifyForOperationUpdate
parameter_list|()
block|{
return|return
name|notifyForOperationUpdate
return|;
block|}
DECL|method|setNotifyForOperationUpdate (Boolean notifyForOperationUpdate)
specifier|public
name|void
name|setNotifyForOperationUpdate
parameter_list|(
name|Boolean
name|notifyForOperationUpdate
parameter_list|)
block|{
name|this
operator|.
name|notifyForOperationUpdate
operator|=
name|notifyForOperationUpdate
expr_stmt|;
block|}
DECL|method|getNotifyForOperationDelete ()
specifier|public
name|Boolean
name|getNotifyForOperationDelete
parameter_list|()
block|{
return|return
name|notifyForOperationDelete
return|;
block|}
DECL|method|setNotifyForOperationDelete (Boolean notifyForOperationDelete)
specifier|public
name|void
name|setNotifyForOperationDelete
parameter_list|(
name|Boolean
name|notifyForOperationDelete
parameter_list|)
block|{
name|this
operator|.
name|notifyForOperationDelete
operator|=
name|notifyForOperationDelete
expr_stmt|;
block|}
DECL|method|getNotifyForOperationUndelete ()
specifier|public
name|Boolean
name|getNotifyForOperationUndelete
parameter_list|()
block|{
return|return
name|notifyForOperationUndelete
return|;
block|}
DECL|method|setNotifyForOperationUndelete (Boolean notifyForOperationUndelete)
specifier|public
name|void
name|setNotifyForOperationUndelete
parameter_list|(
name|Boolean
name|notifyForOperationUndelete
parameter_list|)
block|{
name|this
operator|.
name|notifyForOperationUndelete
operator|=
name|notifyForOperationUndelete
expr_stmt|;
block|}
DECL|method|getReportId ()
specifier|public
name|String
name|getReportId
parameter_list|()
block|{
return|return
name|reportId
return|;
block|}
DECL|method|setReportId (String reportId)
specifier|public
name|void
name|setReportId
parameter_list|(
name|String
name|reportId
parameter_list|)
block|{
name|this
operator|.
name|reportId
operator|=
name|reportId
expr_stmt|;
block|}
DECL|method|getIncludeDetails ()
specifier|public
name|Boolean
name|getIncludeDetails
parameter_list|()
block|{
return|return
name|includeDetails
return|;
block|}
DECL|method|setIncludeDetails (Boolean includeDetails)
specifier|public
name|void
name|setIncludeDetails
parameter_list|(
name|Boolean
name|includeDetails
parameter_list|)
block|{
name|this
operator|.
name|includeDetails
operator|=
name|includeDetails
expr_stmt|;
block|}
DECL|method|getReportMetadata ()
specifier|public
name|ReportMetadata
name|getReportMetadata
parameter_list|()
block|{
return|return
name|reportMetadata
return|;
block|}
DECL|method|setReportMetadata (ReportMetadata reportMetadata)
specifier|public
name|void
name|setReportMetadata
parameter_list|(
name|ReportMetadata
name|reportMetadata
parameter_list|)
block|{
name|this
operator|.
name|reportMetadata
operator|=
name|reportMetadata
expr_stmt|;
block|}
DECL|method|getInstanceId ()
specifier|public
name|String
name|getInstanceId
parameter_list|()
block|{
return|return
name|instanceId
return|;
block|}
DECL|method|setInstanceId (String instanceId)
specifier|public
name|void
name|setInstanceId
parameter_list|(
name|String
name|instanceId
parameter_list|)
block|{
name|this
operator|.
name|instanceId
operator|=
name|instanceId
expr_stmt|;
block|}
DECL|method|getHttpClient ()
specifier|public
name|SalesforceHttpClient
name|getHttpClient
parameter_list|()
block|{
return|return
name|httpClient
return|;
block|}
DECL|method|setHttpClient (SalesforceHttpClient httpClient)
specifier|public
name|void
name|setHttpClient
parameter_list|(
name|SalesforceHttpClient
name|httpClient
parameter_list|)
block|{
name|this
operator|.
name|httpClient
operator|=
name|httpClient
expr_stmt|;
block|}
DECL|method|getObjectMapper ()
specifier|public
name|ObjectMapper
name|getObjectMapper
parameter_list|()
block|{
return|return
name|objectMapper
return|;
block|}
DECL|method|setObjectMapper (ObjectMapper objectMapper)
specifier|public
name|void
name|setObjectMapper
parameter_list|(
name|ObjectMapper
name|objectMapper
parameter_list|)
block|{
name|this
operator|.
name|objectMapper
operator|=
name|objectMapper
expr_stmt|;
block|}
DECL|method|getDefaultReplayId ()
specifier|public
name|Integer
name|getDefaultReplayId
parameter_list|()
block|{
return|return
name|defaultReplayId
return|;
block|}
DECL|method|setDefaultReplayId (Integer defaultReplayId)
specifier|public
name|void
name|setDefaultReplayId
parameter_list|(
name|Integer
name|defaultReplayId
parameter_list|)
block|{
name|this
operator|.
name|defaultReplayId
operator|=
name|defaultReplayId
expr_stmt|;
block|}
DECL|method|getInitialReplayIdMap ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|getInitialReplayIdMap
parameter_list|()
block|{
return|return
name|initialReplayIdMap
return|;
block|}
DECL|method|setInitialReplayIdMap (Map<String, Integer> initialReplayIdMap)
specifier|public
name|void
name|setInitialReplayIdMap
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|initialReplayIdMap
parameter_list|)
block|{
name|this
operator|.
name|initialReplayIdMap
operator|=
name|initialReplayIdMap
expr_stmt|;
block|}
DECL|method|getBackoffIncrement ()
specifier|public
name|long
name|getBackoffIncrement
parameter_list|()
block|{
return|return
name|backoffIncrement
return|;
block|}
DECL|method|setBackoffIncrement (long backoffIncrement)
specifier|public
name|void
name|setBackoffIncrement
parameter_list|(
name|long
name|backoffIncrement
parameter_list|)
block|{
name|this
operator|.
name|backoffIncrement
operator|=
name|backoffIncrement
expr_stmt|;
block|}
DECL|method|getMaxBackoff ()
specifier|public
name|long
name|getMaxBackoff
parameter_list|()
block|{
return|return
name|maxBackoff
return|;
block|}
DECL|method|setMaxBackoff (long maxBackoff)
specifier|public
name|void
name|setMaxBackoff
parameter_list|(
name|long
name|maxBackoff
parameter_list|)
block|{
name|this
operator|.
name|maxBackoff
operator|=
name|maxBackoff
expr_stmt|;
block|}
block|}
end_class

end_unit

