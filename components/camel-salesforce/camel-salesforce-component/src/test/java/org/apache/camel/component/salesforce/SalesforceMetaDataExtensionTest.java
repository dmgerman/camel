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
name|io
operator|.
name|IOException
import|;
end_import

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
name|Arrays
import|;
end_import

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
name|Optional
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
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|Predicate
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
name|StreamSupport
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
name|JsonNode
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
name|module
operator|.
name|jsonSchema
operator|.
name|JsonSchema
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
name|module
operator|.
name|jsonSchema
operator|.
name|types
operator|.
name|ObjectSchema
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
name|extension
operator|.
name|MetaDataExtension
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
name|extension
operator|.
name|MetaDataExtension
operator|.
name|MetaData
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
name|utils
operator|.
name|JsonUtils
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
name|client
operator|.
name|RestClient
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
name|client
operator|.
name|RestClient
operator|.
name|ResponseCallback
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
name|impl
operator|.
name|DefaultCamelContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mockito
operator|.
name|stubbing
operator|.
name|Answer
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|assertj
operator|.
name|core
operator|.
name|api
operator|.
name|Assertions
operator|.
name|assertThat
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Matchers
operator|.
name|any
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Matchers
operator|.
name|eq
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|doAnswer
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|mock
import|;
end_import

begin_class
DECL|class|SalesforceMetaDataExtensionTest
specifier|public
class|class
name|SalesforceMetaDataExtensionTest
block|{
DECL|field|component
specifier|final
name|SalesforceComponent
name|component
init|=
operator|new
name|SalesforceComponent
argument_list|()
decl_stmt|;
DECL|field|metadata
specifier|final
name|MetaDataExtension
name|metadata
decl_stmt|;
DECL|field|restClient
specifier|final
name|RestClient
name|restClient
init|=
name|mock
argument_list|(
name|RestClient
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|SalesforceMetaDataExtensionTest ()
specifier|public
name|SalesforceMetaDataExtensionTest
parameter_list|()
block|{
name|component
operator|.
name|setCamelContext
argument_list|(
operator|new
name|DefaultCamelContext
argument_list|()
argument_list|)
expr_stmt|;
name|SalesforceClientTemplate
operator|.
name|restClientSupplier
operator|=
parameter_list|(
name|c
parameter_list|,
name|p
parameter_list|)
lambda|->
name|restClient
expr_stmt|;
name|metadata
operator|=
name|component
operator|.
name|getExtension
argument_list|(
name|MetaDataExtension
operator|.
name|class
argument_list|)
operator|.
name|get
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|componentShouldProvideMetadataExtension ()
specifier|public
name|void
name|componentShouldProvideMetadataExtension
parameter_list|()
block|{
name|assertThat
argument_list|(
name|component
operator|.
name|getExtension
argument_list|(
name|MetaDataExtension
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|isPresent
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldProvideSalesforceObjectFields ()
specifier|public
name|void
name|shouldProvideSalesforceObjectFields
parameter_list|()
throws|throws
name|IOException
block|{
specifier|final
name|Optional
argument_list|<
name|MetaData
argument_list|>
name|maybeMeta
decl_stmt|;
try|try
init|(
name|InputStream
name|stream
init|=
name|resource
argument_list|(
literal|"/objectDescription.json"
argument_list|)
init|)
block|{
name|doAnswer
argument_list|(
name|provideStreamToCallback
argument_list|(
name|stream
argument_list|)
argument_list|)
operator|.
name|when
argument_list|(
name|restClient
argument_list|)
operator|.
name|getDescription
argument_list|(
name|eq
argument_list|(
literal|"Account"
argument_list|)
argument_list|,
name|any
argument_list|(
name|ResponseCallback
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|maybeMeta
operator|=
name|metadata
operator|.
name|meta
argument_list|(
name|Collections
operator|.
name|singletonMap
argument_list|(
name|SalesforceEndpointConfig
operator|.
name|SOBJECT_NAME
argument_list|,
literal|"Account"
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|assertThat
argument_list|(
name|maybeMeta
argument_list|)
operator|.
name|isPresent
argument_list|()
expr_stmt|;
specifier|final
name|MetaData
name|meta
init|=
name|maybeMeta
operator|.
name|get
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|meta
operator|.
name|getAttribute
argument_list|(
name|MetaDataExtension
operator|.
name|MetaData
operator|.
name|JAVA_TYPE
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|JsonNode
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|meta
operator|.
name|getAttribute
argument_list|(
name|MetaDataExtension
operator|.
name|MetaData
operator|.
name|CONTENT_TYPE
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"application/schema+json"
argument_list|)
expr_stmt|;
specifier|final
name|ObjectSchema
name|payload
init|=
name|meta
operator|.
name|getPayload
argument_list|(
name|ObjectSchema
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|payload
argument_list|)
operator|.
name|isNotNull
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|schemaFor
argument_list|(
name|payload
argument_list|,
literal|"Merchandise__c"
argument_list|)
argument_list|)
operator|.
name|isPresent
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|schemaFor
argument_list|(
name|payload
argument_list|,
literal|"QueryRecordsMerchandise__c"
argument_list|)
argument_list|)
operator|.
name|isPresent
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldProvideSalesforceObjectTypes ()
specifier|public
name|void
name|shouldProvideSalesforceObjectTypes
parameter_list|()
throws|throws
name|IOException
block|{
specifier|final
name|Optional
argument_list|<
name|MetaData
argument_list|>
name|maybeMeta
decl_stmt|;
try|try
init|(
name|InputStream
name|stream
init|=
name|resource
argument_list|(
literal|"/globalObjects.json"
argument_list|)
init|)
block|{
name|doAnswer
argument_list|(
name|provideStreamToCallback
argument_list|(
name|stream
argument_list|)
argument_list|)
operator|.
name|when
argument_list|(
name|restClient
argument_list|)
operator|.
name|getGlobalObjects
argument_list|(
name|any
argument_list|(
name|ResponseCallback
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|maybeMeta
operator|=
name|metadata
operator|.
name|meta
argument_list|(
name|Collections
operator|.
name|emptyMap
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|assertThat
argument_list|(
name|maybeMeta
argument_list|)
operator|.
name|isPresent
argument_list|()
expr_stmt|;
specifier|final
name|MetaData
name|meta
init|=
name|maybeMeta
operator|.
name|get
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|meta
operator|.
name|getAttribute
argument_list|(
name|MetaDataExtension
operator|.
name|MetaData
operator|.
name|JAVA_TYPE
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|JsonNode
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|meta
operator|.
name|getAttribute
argument_list|(
name|MetaDataExtension
operator|.
name|MetaData
operator|.
name|CONTENT_TYPE
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"application/schema+json"
argument_list|)
expr_stmt|;
specifier|final
name|ObjectSchema
name|payload
init|=
name|meta
operator|.
name|getPayload
argument_list|(
name|ObjectSchema
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|payload
argument_list|)
operator|.
name|isNotNull
argument_list|()
expr_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
block|{
literal|"unchecked"
block|,
literal|"rawtypes"
block|}
argument_list|)
specifier|final
name|Set
argument_list|<
name|JsonSchema
argument_list|>
name|oneOf
init|=
operator|(
name|Set
operator|)
name|payload
operator|.
name|getOneOf
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|oneOf
argument_list|)
operator|.
name|hasSize
argument_list|(
literal|4
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|schemaFor
argument_list|(
name|payload
argument_list|,
literal|"AcceptedEventRelation"
argument_list|)
argument_list|)
operator|.
name|isPresent
argument_list|()
operator|.
name|hasValueSatisfying
argument_list|(
name|schema
lambda|->
name|assertThat
argument_list|(
name|schema
operator|.
name|getTitle
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"Accepted Event Relation"
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|schemaFor
argument_list|(
name|payload
argument_list|,
literal|"Account"
argument_list|)
argument_list|)
operator|.
name|isPresent
argument_list|()
operator|.
name|hasValueSatisfying
argument_list|(
name|schema
lambda|->
name|assertThat
argument_list|(
name|schema
operator|.
name|getTitle
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"Account"
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|schemaFor
argument_list|(
name|payload
argument_list|,
literal|"AccountCleanInfo"
argument_list|)
argument_list|)
operator|.
name|isPresent
argument_list|()
operator|.
name|hasValueSatisfying
argument_list|(
name|schema
lambda|->
name|assertThat
argument_list|(
name|schema
operator|.
name|getTitle
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"Account Clean Info"
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|schemaFor
argument_list|(
name|payload
argument_list|,
literal|"AccountContactRole"
argument_list|)
argument_list|)
operator|.
name|isPresent
argument_list|()
operator|.
name|hasValueSatisfying
argument_list|(
name|schema
lambda|->
name|assertThat
argument_list|(
name|schema
operator|.
name|getTitle
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"Account Contact Role"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|provideStreamToCallback (final InputStream stream)
specifier|static
name|Answer
argument_list|<
name|Void
argument_list|>
name|provideStreamToCallback
parameter_list|(
specifier|final
name|InputStream
name|stream
parameter_list|)
block|{
return|return
name|invocation
lambda|->
block|{
specifier|final
name|ResponseCallback
name|callback
init|=
operator|(
name|ResponseCallback
operator|)
name|Arrays
operator|.
name|stream
argument_list|(
name|invocation
operator|.
name|getArguments
argument_list|()
argument_list|)
operator|.
name|filter
argument_list|(
name|ResponseCallback
operator|.
name|class
operator|::
name|isInstance
argument_list|)
operator|.
name|findFirst
argument_list|()
operator|.
name|get
argument_list|()
decl_stmt|;
name|callback
operator|.
name|onResponse
argument_list|(
name|stream
argument_list|,
literal|null
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
return|;
block|}
DECL|method|resource (final String path)
specifier|static
name|InputStream
name|resource
parameter_list|(
specifier|final
name|String
name|path
parameter_list|)
block|{
return|return
name|SalesforceMetaDataExtensionTest
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
name|path
argument_list|)
return|;
block|}
DECL|method|schemaFor (final ObjectSchema schema, final String sObjectName)
specifier|static
name|Optional
argument_list|<
name|ObjectSchema
argument_list|>
name|schemaFor
parameter_list|(
specifier|final
name|ObjectSchema
name|schema
parameter_list|,
specifier|final
name|String
name|sObjectName
parameter_list|)
block|{
annotation|@
name|SuppressWarnings
argument_list|(
block|{
literal|"unchecked"
block|,
literal|"rawtypes"
block|}
argument_list|)
specifier|final
name|Set
argument_list|<
name|ObjectSchema
argument_list|>
name|oneOf
init|=
operator|(
name|Set
operator|)
name|schema
operator|.
name|getOneOf
argument_list|()
decl_stmt|;
return|return
name|StreamSupport
operator|.
name|stream
argument_list|(
name|oneOf
operator|.
name|spliterator
argument_list|()
argument_list|,
literal|false
argument_list|)
operator|.
name|filter
argument_list|(
name|idMatches
argument_list|(
name|JsonUtils
operator|.
name|DEFAULT_ID_PREFIX
operator|+
literal|":"
operator|+
name|sObjectName
argument_list|)
argument_list|)
operator|.
name|findAny
argument_list|()
return|;
block|}
DECL|method|valueAt (final JsonNode payload, final int idx, final String name)
specifier|static
name|String
name|valueAt
parameter_list|(
specifier|final
name|JsonNode
name|payload
parameter_list|,
specifier|final
name|int
name|idx
parameter_list|,
specifier|final
name|String
name|name
parameter_list|)
block|{
return|return
name|payload
operator|.
name|get
argument_list|(
name|idx
argument_list|)
operator|.
name|get
argument_list|(
name|name
argument_list|)
operator|.
name|asText
argument_list|()
return|;
block|}
DECL|method|idMatches (final String wantedId)
specifier|private
specifier|static
name|Predicate
argument_list|<
name|JsonSchema
argument_list|>
name|idMatches
parameter_list|(
specifier|final
name|String
name|wantedId
parameter_list|)
block|{
return|return
name|schema
lambda|->
name|wantedId
operator|.
name|equals
argument_list|(
name|schema
operator|.
name|getId
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

