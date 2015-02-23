begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.salesforce
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
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

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
name|RuntimeCamelException
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
name|spi
operator|.
name|UriParams
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|client
operator|.
name|HttpClient
import|;
end_import

begin_comment
comment|/**  * Salesforce Endpoint configuration.  */
end_comment

begin_class
annotation|@
name|UriParams
DECL|class|SalesforceEndpointConfig
specifier|public
class|class
name|SalesforceEndpointConfig
implements|implements
name|Cloneable
block|{
comment|// default API version
DECL|field|DEFAULT_VERSION
specifier|public
specifier|static
specifier|final
name|String
name|DEFAULT_VERSION
init|=
literal|"33.0"
decl_stmt|;
comment|// general parameter
DECL|field|API_VERSION
specifier|public
specifier|static
specifier|final
name|String
name|API_VERSION
init|=
literal|"apiVersion"
decl_stmt|;
comment|// parameters for Rest API
DECL|field|FORMAT
specifier|public
specifier|static
specifier|final
name|String
name|FORMAT
init|=
literal|"format"
decl_stmt|;
DECL|field|SOBJECT_NAME
specifier|public
specifier|static
specifier|final
name|String
name|SOBJECT_NAME
init|=
literal|"sObjectName"
decl_stmt|;
DECL|field|SOBJECT_ID
specifier|public
specifier|static
specifier|final
name|String
name|SOBJECT_ID
init|=
literal|"sObjectId"
decl_stmt|;
DECL|field|SOBJECT_FIELDS
specifier|public
specifier|static
specifier|final
name|String
name|SOBJECT_FIELDS
init|=
literal|"sObjectFields"
decl_stmt|;
DECL|field|SOBJECT_EXT_ID_NAME
specifier|public
specifier|static
specifier|final
name|String
name|SOBJECT_EXT_ID_NAME
init|=
literal|"sObjectIdName"
decl_stmt|;
DECL|field|SOBJECT_EXT_ID_VALUE
specifier|public
specifier|static
specifier|final
name|String
name|SOBJECT_EXT_ID_VALUE
init|=
literal|"sObjectIdValue"
decl_stmt|;
DECL|field|SOBJECT_BLOB_FIELD_NAME
specifier|public
specifier|static
specifier|final
name|String
name|SOBJECT_BLOB_FIELD_NAME
init|=
literal|"sObjectBlobFieldName"
decl_stmt|;
DECL|field|SOBJECT_CLASS
specifier|public
specifier|static
specifier|final
name|String
name|SOBJECT_CLASS
init|=
literal|"sObjectClass"
decl_stmt|;
DECL|field|SOBJECT_QUERY
specifier|public
specifier|static
specifier|final
name|String
name|SOBJECT_QUERY
init|=
literal|"sObjectQuery"
decl_stmt|;
DECL|field|SOBJECT_SEARCH
specifier|public
specifier|static
specifier|final
name|String
name|SOBJECT_SEARCH
init|=
literal|"sObjectSearch"
decl_stmt|;
DECL|field|APEX_METHOD
specifier|public
specifier|static
specifier|final
name|String
name|APEX_METHOD
init|=
literal|"apexMethod"
decl_stmt|;
DECL|field|APEX_URL
specifier|public
specifier|static
specifier|final
name|String
name|APEX_URL
init|=
literal|"apexUrl"
decl_stmt|;
comment|// prefix for parameters in headers
DECL|field|APEX_QUERY_PARAM_PREFIX
specifier|public
specifier|static
specifier|final
name|String
name|APEX_QUERY_PARAM_PREFIX
init|=
literal|"apexQueryParam."
decl_stmt|;
comment|// parameters for Bulk API
DECL|field|CONTENT_TYPE
specifier|public
specifier|static
specifier|final
name|String
name|CONTENT_TYPE
init|=
literal|"contentType"
decl_stmt|;
DECL|field|JOB_ID
specifier|public
specifier|static
specifier|final
name|String
name|JOB_ID
init|=
literal|"jobId"
decl_stmt|;
DECL|field|BATCH_ID
specifier|public
specifier|static
specifier|final
name|String
name|BATCH_ID
init|=
literal|"batchId"
decl_stmt|;
DECL|field|RESULT_ID
specifier|public
specifier|static
specifier|final
name|String
name|RESULT_ID
init|=
literal|"resultId"
decl_stmt|;
comment|// general properties
annotation|@
name|UriParam
DECL|field|apiVersion
specifier|private
name|String
name|apiVersion
init|=
name|DEFAULT_VERSION
decl_stmt|;
comment|// Rest API properties
annotation|@
name|UriParam
DECL|field|format
specifier|private
name|PayloadFormat
name|format
init|=
name|PayloadFormat
operator|.
name|JSON
decl_stmt|;
annotation|@
name|UriParam
DECL|field|sObjectName
specifier|private
name|String
name|sObjectName
decl_stmt|;
annotation|@
name|UriParam
DECL|field|sObjectId
specifier|private
name|String
name|sObjectId
decl_stmt|;
annotation|@
name|UriParam
DECL|field|sObjectFields
specifier|private
name|String
name|sObjectFields
decl_stmt|;
annotation|@
name|UriParam
DECL|field|sObjectIdName
specifier|private
name|String
name|sObjectIdName
decl_stmt|;
annotation|@
name|UriParam
DECL|field|sObjectIdValue
specifier|private
name|String
name|sObjectIdValue
decl_stmt|;
annotation|@
name|UriParam
DECL|field|sObjectBlobFieldName
specifier|private
name|String
name|sObjectBlobFieldName
decl_stmt|;
annotation|@
name|UriParam
DECL|field|sObjectClass
specifier|private
name|String
name|sObjectClass
decl_stmt|;
annotation|@
name|UriParam
DECL|field|sObjectQuery
specifier|private
name|String
name|sObjectQuery
decl_stmt|;
annotation|@
name|UriParam
DECL|field|sObjectSearch
specifier|private
name|String
name|sObjectSearch
decl_stmt|;
annotation|@
name|UriParam
DECL|field|apexMethod
specifier|private
name|String
name|apexMethod
decl_stmt|;
annotation|@
name|UriParam
DECL|field|apexUrl
specifier|private
name|String
name|apexUrl
decl_stmt|;
annotation|@
name|UriParam
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
comment|// Bulk API properties
annotation|@
name|UriParam
DECL|field|contentType
specifier|private
name|ContentType
name|contentType
decl_stmt|;
annotation|@
name|UriParam
DECL|field|jobId
specifier|private
name|String
name|jobId
decl_stmt|;
annotation|@
name|UriParam
DECL|field|batchId
specifier|private
name|String
name|batchId
decl_stmt|;
annotation|@
name|UriParam
DECL|field|resultId
specifier|private
name|String
name|resultId
decl_stmt|;
comment|// Streaming API properties
annotation|@
name|UriParam
DECL|field|updateTopic
specifier|private
name|boolean
name|updateTopic
decl_stmt|;
annotation|@
name|UriParam
DECL|field|notifyForFields
specifier|private
name|NotifyForFieldsEnum
name|notifyForFields
decl_stmt|;
annotation|@
name|UriParam
DECL|field|notifyForOperations
specifier|private
name|NotifyForOperationsEnum
name|notifyForOperations
decl_stmt|;
annotation|@
name|UriParam
DECL|field|notifyForOperationCreate
specifier|private
name|Boolean
name|notifyForOperationCreate
decl_stmt|;
annotation|@
name|UriParam
DECL|field|notifyForOperationUpdate
specifier|private
name|Boolean
name|notifyForOperationUpdate
decl_stmt|;
annotation|@
name|UriParam
DECL|field|notifyForOperationDelete
specifier|private
name|Boolean
name|notifyForOperationDelete
decl_stmt|;
annotation|@
name|UriParam
DECL|field|notifyForOperationUndelete
specifier|private
name|Boolean
name|notifyForOperationUndelete
decl_stmt|;
comment|// Jetty HttpClient, set using reference
annotation|@
name|UriParam
DECL|field|httpClient
specifier|private
name|HttpClient
name|httpClient
decl_stmt|;
DECL|method|copy ()
specifier|public
name|SalesforceEndpointConfig
name|copy
parameter_list|()
block|{
try|try
block|{
specifier|final
name|SalesforceEndpointConfig
name|copy
init|=
operator|(
name|SalesforceEndpointConfig
operator|)
name|super
operator|.
name|clone
argument_list|()
decl_stmt|;
comment|// nothing to deep copy
return|return
name|copy
return|;
block|}
catch|catch
parameter_list|(
name|CloneNotSupportedException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
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
operator|==
literal|null
condition|?
name|Collections
operator|.
name|EMPTY_MAP
else|:
name|Collections
operator|.
name|unmodifiableMap
argument_list|(
name|apexQueryParams
argument_list|)
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
DECL|method|isUpdateTopic ()
specifier|public
name|boolean
name|isUpdateTopic
parameter_list|()
block|{
return|return
name|updateTopic
return|;
block|}
DECL|method|setUpdateTopic (boolean updateTopic)
specifier|public
name|void
name|setUpdateTopic
parameter_list|(
name|boolean
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
DECL|method|setNotifyForOperations (NotifyForOperationsEnum notifyForOperations)
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
DECL|method|setHttpClient (HttpClient httpClient)
specifier|public
name|void
name|setHttpClient
parameter_list|(
name|HttpClient
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
DECL|method|getHttpClient ()
specifier|public
name|HttpClient
name|getHttpClient
parameter_list|()
block|{
return|return
name|httpClient
return|;
block|}
DECL|method|toValueMap ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|toValueMap
parameter_list|()
block|{
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|valueMap
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|valueMap
operator|.
name|put
argument_list|(
name|FORMAT
argument_list|,
name|format
operator|.
name|toString
argument_list|()
operator|.
name|toLowerCase
argument_list|()
argument_list|)
expr_stmt|;
name|valueMap
operator|.
name|put
argument_list|(
name|API_VERSION
argument_list|,
name|apiVersion
argument_list|)
expr_stmt|;
name|valueMap
operator|.
name|put
argument_list|(
name|SOBJECT_NAME
argument_list|,
name|sObjectName
argument_list|)
expr_stmt|;
name|valueMap
operator|.
name|put
argument_list|(
name|SOBJECT_ID
argument_list|,
name|sObjectId
argument_list|)
expr_stmt|;
name|valueMap
operator|.
name|put
argument_list|(
name|SOBJECT_FIELDS
argument_list|,
name|sObjectFields
argument_list|)
expr_stmt|;
name|valueMap
operator|.
name|put
argument_list|(
name|SOBJECT_EXT_ID_NAME
argument_list|,
name|sObjectIdName
argument_list|)
expr_stmt|;
name|valueMap
operator|.
name|put
argument_list|(
name|SOBJECT_BLOB_FIELD_NAME
argument_list|,
name|sObjectBlobFieldName
argument_list|)
expr_stmt|;
name|valueMap
operator|.
name|put
argument_list|(
name|SOBJECT_EXT_ID_VALUE
argument_list|,
name|sObjectIdValue
argument_list|)
expr_stmt|;
name|valueMap
operator|.
name|put
argument_list|(
name|SOBJECT_CLASS
argument_list|,
name|sObjectClass
argument_list|)
expr_stmt|;
name|valueMap
operator|.
name|put
argument_list|(
name|SOBJECT_QUERY
argument_list|,
name|sObjectQuery
argument_list|)
expr_stmt|;
name|valueMap
operator|.
name|put
argument_list|(
name|SOBJECT_SEARCH
argument_list|,
name|sObjectSearch
argument_list|)
expr_stmt|;
name|valueMap
operator|.
name|put
argument_list|(
name|APEX_METHOD
argument_list|,
name|apexMethod
argument_list|)
expr_stmt|;
name|valueMap
operator|.
name|put
argument_list|(
name|APEX_URL
argument_list|,
name|apexUrl
argument_list|)
expr_stmt|;
comment|// apexQueryParams are handled explicitly in AbstractRestProcessor
comment|// add bulk API properties
if|if
condition|(
name|contentType
operator|!=
literal|null
condition|)
block|{
name|valueMap
operator|.
name|put
argument_list|(
name|CONTENT_TYPE
argument_list|,
name|contentType
operator|.
name|value
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|valueMap
operator|.
name|put
argument_list|(
name|JOB_ID
argument_list|,
name|jobId
argument_list|)
expr_stmt|;
name|valueMap
operator|.
name|put
argument_list|(
name|BATCH_ID
argument_list|,
name|batchId
argument_list|)
expr_stmt|;
name|valueMap
operator|.
name|put
argument_list|(
name|RESULT_ID
argument_list|,
name|resultId
argument_list|)
expr_stmt|;
return|return
name|Collections
operator|.
name|unmodifiableMap
argument_list|(
name|valueMap
argument_list|)
return|;
block|}
block|}
end_class

end_unit

