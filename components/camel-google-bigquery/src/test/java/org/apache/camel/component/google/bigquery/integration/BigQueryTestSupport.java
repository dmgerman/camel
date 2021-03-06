begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.google.bigquery.integration
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
name|bigquery
operator|.
name|integration
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
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

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
name|Properties
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Collectors
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
name|com
operator|.
name|google
operator|.
name|api
operator|.
name|client
operator|.
name|googleapis
operator|.
name|json
operator|.
name|GoogleJsonResponseException
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
name|bigquery
operator|.
name|model
operator|.
name|QueryRequest
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
name|bigquery
operator|.
name|model
operator|.
name|QueryResponse
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
name|bigquery
operator|.
name|model
operator|.
name|Table
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
name|bigquery
operator|.
name|model
operator|.
name|TableFieldSchema
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
name|bigquery
operator|.
name|model
operator|.
name|TableReference
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
name|bigquery
operator|.
name|model
operator|.
name|TableSchema
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
name|BindToRegistry
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
name|component
operator|.
name|google
operator|.
name|bigquery
operator|.
name|GoogleBigQueryComponent
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
name|bigquery
operator|.
name|GoogleBigQueryConnectionFactory
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
name|test
operator|.
name|junit4
operator|.
name|CamelTestSupport
import|;
end_import

begin_class
DECL|class|BigQueryTestSupport
specifier|public
class|class
name|BigQueryTestSupport
extends|extends
name|CamelTestSupport
block|{
DECL|field|SERVICE_KEY
specifier|public
specifier|static
specifier|final
name|String
name|SERVICE_KEY
decl_stmt|;
DECL|field|SERVICE_ACCOUNT
specifier|public
specifier|static
specifier|final
name|String
name|SERVICE_ACCOUNT
decl_stmt|;
DECL|field|PROJECT_ID
specifier|public
specifier|static
specifier|final
name|String
name|PROJECT_ID
decl_stmt|;
DECL|field|DATASET_ID
specifier|public
specifier|static
specifier|final
name|String
name|DATASET_ID
decl_stmt|;
DECL|field|SERVICE_URL
specifier|public
specifier|static
specifier|final
name|String
name|SERVICE_URL
decl_stmt|;
DECL|field|CREDENTIALS_FILE_LOCATION
specifier|public
specifier|static
specifier|final
name|String
name|CREDENTIALS_FILE_LOCATION
decl_stmt|;
DECL|field|connectionFactory
specifier|private
name|GoogleBigQueryConnectionFactory
name|connectionFactory
decl_stmt|;
static|static
block|{
name|Properties
name|testProperties
init|=
name|loadProperties
argument_list|()
decl_stmt|;
name|SERVICE_KEY
operator|=
name|testProperties
operator|.
name|getProperty
argument_list|(
literal|"service.key"
argument_list|)
expr_stmt|;
name|SERVICE_ACCOUNT
operator|=
name|testProperties
operator|.
name|getProperty
argument_list|(
literal|"service.account"
argument_list|)
expr_stmt|;
name|PROJECT_ID
operator|=
name|testProperties
operator|.
name|getProperty
argument_list|(
literal|"project.id"
argument_list|)
expr_stmt|;
name|DATASET_ID
operator|=
name|testProperties
operator|.
name|getProperty
argument_list|(
literal|"bigquery.datasetId"
argument_list|)
expr_stmt|;
name|SERVICE_URL
operator|=
name|testProperties
operator|.
name|getProperty
argument_list|(
literal|"test.serviceURL"
argument_list|)
expr_stmt|;
name|CREDENTIALS_FILE_LOCATION
operator|=
name|testProperties
operator|.
name|getProperty
argument_list|(
literal|"service.credentialsFileLocation"
argument_list|)
expr_stmt|;
block|}
DECL|method|loadProperties ()
specifier|private
specifier|static
name|Properties
name|loadProperties
parameter_list|()
block|{
name|Properties
name|testProperties
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|InputStream
name|fileIn
init|=
name|BigQueryTestSupport
operator|.
name|class
operator|.
name|getClassLoader
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"simple.properties"
argument_list|)
decl_stmt|;
try|try
block|{
name|testProperties
operator|.
name|load
argument_list|(
name|fileIn
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
return|return
name|testProperties
return|;
block|}
DECL|method|addBigqueryComponent (CamelContext context)
specifier|protected
name|void
name|addBigqueryComponent
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
name|connectionFactory
operator|=
operator|new
name|GoogleBigQueryConnectionFactory
argument_list|()
operator|.
name|setServiceAccount
argument_list|(
name|SERVICE_ACCOUNT
argument_list|)
operator|.
name|setServiceAccountKey
argument_list|(
name|SERVICE_KEY
argument_list|)
operator|.
name|setServiceURL
argument_list|(
name|SERVICE_URL
argument_list|)
expr_stmt|;
name|GoogleBigQueryComponent
name|component
init|=
operator|new
name|GoogleBigQueryComponent
argument_list|()
decl_stmt|;
name|component
operator|.
name|setConnectionFactory
argument_list|(
name|connectionFactory
argument_list|)
expr_stmt|;
name|context
operator|.
name|addComponent
argument_list|(
literal|"google-bigquery"
argument_list|,
name|component
argument_list|)
expr_stmt|;
name|context
operator|.
name|getPropertiesComponent
argument_list|()
operator|.
name|setLocation
argument_list|(
literal|"ref:prop"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createCamelContext ()
specifier|protected
name|CamelContext
name|createCamelContext
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContext
name|context
init|=
name|super
operator|.
name|createCamelContext
argument_list|()
decl_stmt|;
name|addBigqueryComponent
argument_list|(
name|context
argument_list|)
expr_stmt|;
return|return
name|context
return|;
block|}
annotation|@
name|BindToRegistry
argument_list|(
literal|"prop"
argument_list|)
DECL|method|loadRegProperties ()
specifier|public
name|Properties
name|loadRegProperties
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|loadProperties
argument_list|()
return|;
block|}
DECL|method|getConnectionFactory ()
specifier|public
name|GoogleBigQueryConnectionFactory
name|getConnectionFactory
parameter_list|()
block|{
return|return
name|connectionFactory
return|;
block|}
DECL|method|assertRowExist (String tableName, Map<String, String> row)
specifier|protected
name|void
name|assertRowExist
parameter_list|(
name|String
name|tableName
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|row
parameter_list|)
throws|throws
name|Exception
block|{
name|QueryRequest
name|queryRequest
init|=
operator|new
name|QueryRequest
argument_list|()
decl_stmt|;
name|String
name|query
init|=
literal|"SELECT * FROM "
operator|+
name|DATASET_ID
operator|+
literal|"."
operator|+
name|tableName
operator|+
literal|" WHERE "
operator|+
name|row
operator|.
name|entrySet
argument_list|()
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|e
lambda|->
name|e
operator|.
name|getKey
argument_list|()
operator|+
literal|" = '"
operator|+
name|e
operator|.
name|getValue
argument_list|()
operator|+
literal|"'"
argument_list|)
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|joining
argument_list|(
literal|" AND "
argument_list|)
argument_list|)
decl_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Query: {}"
argument_list|,
name|query
argument_list|)
expr_stmt|;
name|queryRequest
operator|.
name|setQuery
argument_list|(
name|query
argument_list|)
expr_stmt|;
name|QueryResponse
name|queryResponse
init|=
name|getConnectionFactory
argument_list|()
operator|.
name|getDefaultClient
argument_list|()
operator|.
name|jobs
argument_list|()
operator|.
name|query
argument_list|(
name|PROJECT_ID
argument_list|,
name|queryRequest
argument_list|)
operator|.
name|execute
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|queryResponse
operator|.
name|getRows
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|createBqTable (String tableId)
specifier|protected
name|void
name|createBqTable
parameter_list|(
name|String
name|tableId
parameter_list|)
throws|throws
name|Exception
block|{
name|TableReference
name|reference
init|=
operator|new
name|TableReference
argument_list|()
operator|.
name|setTableId
argument_list|(
name|tableId
argument_list|)
operator|.
name|setDatasetId
argument_list|(
name|DATASET_ID
argument_list|)
operator|.
name|setProjectId
argument_list|(
name|PROJECT_ID
argument_list|)
decl_stmt|;
name|InputStream
name|in
init|=
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"/schema/simple-table.json"
argument_list|)
decl_stmt|;
name|TableSchema
name|schema
init|=
name|readDefinition
argument_list|(
name|in
argument_list|)
decl_stmt|;
name|Table
name|table
init|=
operator|new
name|Table
argument_list|()
operator|.
name|setTableReference
argument_list|(
name|reference
argument_list|)
operator|.
name|setSchema
argument_list|(
name|schema
argument_list|)
decl_stmt|;
try|try
block|{
name|getConnectionFactory
argument_list|()
operator|.
name|getDefaultClient
argument_list|()
operator|.
name|tables
argument_list|()
operator|.
name|insert
argument_list|(
name|PROJECT_ID
argument_list|,
name|DATASET_ID
argument_list|,
name|table
argument_list|)
operator|.
name|execute
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|GoogleJsonResponseException
name|e
parameter_list|)
block|{
if|if
condition|(
name|e
operator|.
name|getDetails
argument_list|()
operator|.
name|getCode
argument_list|()
operator|==
literal|409
condition|)
block|{
name|log
operator|.
name|info
argument_list|(
literal|"Table {} already exist"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
name|e
throw|;
block|}
block|}
block|}
DECL|method|readDefinition (InputStream schemaInputStream)
specifier|private
name|TableSchema
name|readDefinition
parameter_list|(
name|InputStream
name|schemaInputStream
parameter_list|)
throws|throws
name|Exception
block|{
name|TableSchema
name|schema
init|=
operator|new
name|TableSchema
argument_list|()
decl_stmt|;
name|ObjectMapper
name|mapper
init|=
operator|new
name|ObjectMapper
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|TableFieldSchema
argument_list|>
name|fields
init|=
name|mapper
operator|.
name|readValue
argument_list|(
name|schemaInputStream
argument_list|,
name|ArrayList
operator|.
name|class
argument_list|)
decl_stmt|;
name|schema
operator|.
name|setFields
argument_list|(
name|fields
argument_list|)
expr_stmt|;
return|return
name|schema
return|;
block|}
block|}
end_class

end_unit

