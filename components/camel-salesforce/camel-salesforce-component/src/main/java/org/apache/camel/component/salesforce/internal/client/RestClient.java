begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.salesforce.internal.client
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
name|internal
operator|.
name|client
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
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
name|SalesforceException
import|;
end_import

begin_interface
DECL|interface|RestClient
specifier|public
interface|interface
name|RestClient
block|{
DECL|interface|ResponseCallback
specifier|public
specifier|static
interface|interface
name|ResponseCallback
block|{
DECL|method|onResponse (InputStream response, SalesforceException exception)
name|void
name|onResponse
parameter_list|(
name|InputStream
name|response
parameter_list|,
name|SalesforceException
name|exception
parameter_list|)
function_decl|;
block|}
comment|/**      * Lists summary information about each API version currently available,      * including the version, label, and a link to each version's root.      *      * @param callback {@link ResponseCallback} to handle response or exception      */
DECL|method|getVersions (ResponseCallback callback)
name|void
name|getVersions
parameter_list|(
name|ResponseCallback
name|callback
parameter_list|)
function_decl|;
comment|/**      * Lists available resources for the specified API version, including resource name and URI.      *      * @param callback {@link ResponseCallback} to handle response or exception      */
DECL|method|getResources (ResponseCallback callback)
name|void
name|getResources
parameter_list|(
name|ResponseCallback
name|callback
parameter_list|)
function_decl|;
comment|/**      * Lists the available objects and their metadata for your organization's data.      *      * @param callback {@link ResponseCallback} to handle response or exception      */
DECL|method|getGlobalObjects (ResponseCallback callback)
name|void
name|getGlobalObjects
parameter_list|(
name|ResponseCallback
name|callback
parameter_list|)
function_decl|;
comment|/**      * Describes the individual metadata for the specified object.      *      * @param sObjectName specified object name      * @param callback    {@link ResponseCallback} to handle response or exception      */
DECL|method|getBasicInfo (String sObjectName, ResponseCallback callback)
name|void
name|getBasicInfo
parameter_list|(
name|String
name|sObjectName
parameter_list|,
name|ResponseCallback
name|callback
parameter_list|)
function_decl|;
comment|/**      * Completely describes the individual metadata at all levels for the specified object.      *      * @param sObjectName specified object name      * @param callback    {@link ResponseCallback} to handle response or exception      */
DECL|method|getDescription (String sObjectName, ResponseCallback callback)
name|void
name|getDescription
parameter_list|(
name|String
name|sObjectName
parameter_list|,
name|ResponseCallback
name|callback
parameter_list|)
function_decl|;
comment|/**      * Retrieves a record for the specified object ID.      *      * @param sObjectName specified object name      * @param id          object id      * @param callback    {@link ResponseCallback} to handle response or exception      */
DECL|method|getSObject (String sObjectName, String id, String[] fields, ResponseCallback callback)
name|void
name|getSObject
parameter_list|(
name|String
name|sObjectName
parameter_list|,
name|String
name|id
parameter_list|,
name|String
index|[]
name|fields
parameter_list|,
name|ResponseCallback
name|callback
parameter_list|)
function_decl|;
comment|/**      * Creates a record for the specified object.      *      * @param sObjectName specified object name      * @param sObject     request entity      * @param callback    {@link ResponseCallback} to handle response or exception      */
DECL|method|createSObject (String sObjectName, InputStream sObject, ResponseCallback callback)
name|void
name|createSObject
parameter_list|(
name|String
name|sObjectName
parameter_list|,
name|InputStream
name|sObject
parameter_list|,
name|ResponseCallback
name|callback
parameter_list|)
function_decl|;
comment|/**      * Updates a record for the specified object ID.      *      * @param sObjectName specified object name      * @param id          object id      * @param sObject     request entity      * @param callback    {@link ResponseCallback} to handle response or exception      */
DECL|method|updateSObject (String sObjectName, String id, InputStream sObject, ResponseCallback callback)
name|void
name|updateSObject
parameter_list|(
name|String
name|sObjectName
parameter_list|,
name|String
name|id
parameter_list|,
name|InputStream
name|sObject
parameter_list|,
name|ResponseCallback
name|callback
parameter_list|)
function_decl|;
comment|/**      * Deletes a record for the specified object ID.      *      * @param sObjectName specified object name      * @param id          object id      * @param callback    {@link ResponseCallback} to handle response or exception      */
DECL|method|deleteSObject (String sObjectName, String id, ResponseCallback callback)
name|void
name|deleteSObject
parameter_list|(
name|String
name|sObjectName
parameter_list|,
name|String
name|id
parameter_list|,
name|ResponseCallback
name|callback
parameter_list|)
function_decl|;
comment|/**      * Retrieves a record for the specified external ID.      *      * @param sObjectName specified object name      * @param fieldName   external field name      * @param fieldValue  external field value      * @param callback    {@link ResponseCallback} to handle response or exception      */
DECL|method|getSObjectWithId (String sObjectName, String fieldName, String fieldValue, ResponseCallback callback)
name|void
name|getSObjectWithId
parameter_list|(
name|String
name|sObjectName
parameter_list|,
name|String
name|fieldName
parameter_list|,
name|String
name|fieldValue
parameter_list|,
name|ResponseCallback
name|callback
parameter_list|)
function_decl|;
comment|/**      * Creates or updates a record based on the value of a specified external ID field.      *      * @param sObjectName specified object name      * @param fieldName   external field name      * @param fieldValue  external field value      * @param sObject     input object to insert or update      * @param callback    {@link ResponseCallback} to handle response or exception      */
DECL|method|upsertSObject (String sObjectName, String fieldName, String fieldValue, InputStream sObject, ResponseCallback callback)
name|void
name|upsertSObject
parameter_list|(
name|String
name|sObjectName
parameter_list|,
name|String
name|fieldName
parameter_list|,
name|String
name|fieldValue
parameter_list|,
name|InputStream
name|sObject
parameter_list|,
name|ResponseCallback
name|callback
parameter_list|)
function_decl|;
comment|/**      * Deletes a record based on the value of a specified external ID field.      *      * @param sObjectName specified object name      * @param fieldName   external field name      * @param fieldValue  external field value      * @param callback    {@link ResponseCallback} to handle response or exception      */
DECL|method|deleteSObjectWithId (String sObjectName, String fieldName, String fieldValue, ResponseCallback callback)
name|void
name|deleteSObjectWithId
parameter_list|(
name|String
name|sObjectName
parameter_list|,
name|String
name|fieldName
parameter_list|,
name|String
name|fieldValue
parameter_list|,
name|ResponseCallback
name|callback
parameter_list|)
function_decl|;
comment|/**      * Retrieves the specified blob field from an individual record.      */
DECL|method|getBlobField (String sObjectName, String id, String blobFieldName, ResponseCallback callback)
name|void
name|getBlobField
parameter_list|(
name|String
name|sObjectName
parameter_list|,
name|String
name|id
parameter_list|,
name|String
name|blobFieldName
parameter_list|,
name|ResponseCallback
name|callback
parameter_list|)
function_decl|;
comment|/*     TODO     SObject User Password     /vXX.X/sobjects/User/user id/password     /vXX.X/sobjects/SelfServiceUser/self service user id/password      These methods set, reset, or get information about a user password. */
comment|/**      * Executes the specified SOQL query.      *      * @param soqlQuery SOQL query      * @param callback  {@link ResponseCallback} to handle response or exception      */
DECL|method|query (String soqlQuery, ResponseCallback callback)
name|void
name|query
parameter_list|(
name|String
name|soqlQuery
parameter_list|,
name|ResponseCallback
name|callback
parameter_list|)
function_decl|;
comment|/**      * Get SOQL query results using nextRecordsUrl.      *      * @param nextRecordsUrl URL for next records to fetch, returned by query()      * @param callback       {@link ResponseCallback} to handle response or exception      */
DECL|method|queryMore (String nextRecordsUrl, ResponseCallback callback)
name|void
name|queryMore
parameter_list|(
name|String
name|nextRecordsUrl
parameter_list|,
name|ResponseCallback
name|callback
parameter_list|)
function_decl|;
comment|/**      * Executes the specified SOSL search.      *      * @param soslQuery SOSL query      * @param callback  {@link ResponseCallback} to handle response or exception      */
DECL|method|search (String soslQuery, ResponseCallback callback)
name|void
name|search
parameter_list|(
name|String
name|soslQuery
parameter_list|,
name|ResponseCallback
name|callback
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

