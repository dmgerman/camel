begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.salesforce.api.utils
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
name|api
operator|.
name|utils
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Modifier
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
name|HashSet
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
name|Set
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
import|import static
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Collectors
operator|.
name|joining
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
name|core
operator|.
name|JsonProcessingException
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
name|BeanDescription
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
name|DeserializationFeature
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
name|JsonMappingException
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
name|JsonSerializer
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
name|fasterxml
operator|.
name|jackson
operator|.
name|databind
operator|.
name|SerializationConfig
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
name|SerializationFeature
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
name|jsonFormatVisitors
operator|.
name|JsonValueFormat
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
name|ser
operator|.
name|BeanPropertyWriter
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
name|ser
operator|.
name|BeanSerializerFactory
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
name|ser
operator|.
name|BeanSerializerModifier
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
name|ser
operator|.
name|PropertyWriter
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
name|ser
operator|.
name|SerializerFactory
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
name|ser
operator|.
name|std
operator|.
name|NullSerializer
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
name|JsonSchemaGenerator
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
name|ArraySchema
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
name|BooleanSchema
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
name|IntegerSchema
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
name|NullSchema
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
name|NumberSchema
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
name|SimpleTypeSchema
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
name|StringSchema
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
name|AbstractDTOBase
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
name|AbstractQueryRecordsBase
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
name|Address
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
name|GeoLocation
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
name|GlobalObjects
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
name|PickListValue
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
name|SObject
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
name|SObjectDescription
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
name|SObjectField
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
name|engine
operator|.
name|DefaultPackageScanClassResolver
import|;
end_import

begin_comment
comment|/**  * Factory class for creating {@linkplain com.fasterxml.jackson.databind.ObjectMapper}  */
end_comment

begin_class
DECL|class|JsonUtils
specifier|public
specifier|abstract
class|class
name|JsonUtils
block|{
DECL|field|SCHEMA4
specifier|public
specifier|static
specifier|final
name|String
name|SCHEMA4
init|=
literal|"http://json-schema.org/draft-04/schema#"
decl_stmt|;
DECL|field|DEFAULT_ID_PREFIX
specifier|public
specifier|static
specifier|final
name|String
name|DEFAULT_ID_PREFIX
init|=
literal|"urn:jsonschema:org:apache:camel:component:salesforce:dto"
decl_stmt|;
DECL|field|API_DTO_ID
specifier|private
specifier|static
specifier|final
name|String
name|API_DTO_ID
init|=
literal|"org:urn:jsonschema:org:apache:camel:component:salesforce:api:dto"
decl_stmt|;
DECL|method|createObjectMapper ()
specifier|public
specifier|static
name|ObjectMapper
name|createObjectMapper
parameter_list|()
block|{
comment|// enable date time support including Java 1.8 ZonedDateTime
name|ObjectMapper
name|objectMapper
init|=
operator|new
name|ObjectMapper
argument_list|()
decl_stmt|;
name|objectMapper
operator|.
name|configure
argument_list|(
name|SerializationFeature
operator|.
name|WRITE_DATES_AS_TIMESTAMPS
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|objectMapper
operator|.
name|configure
argument_list|(
name|DeserializationFeature
operator|.
name|ADJUST_DATES_TO_CONTEXT_TIME_ZONE
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|objectMapper
operator|.
name|registerModule
argument_list|(
operator|new
name|TimeModule
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|objectMapper
return|;
block|}
DECL|method|getBasicApiJsonSchema ()
specifier|public
specifier|static
name|String
name|getBasicApiJsonSchema
parameter_list|()
throws|throws
name|JsonProcessingException
block|{
name|ObjectMapper
name|mapper
init|=
name|createSchemaObjectMapper
argument_list|()
decl_stmt|;
name|JsonSchemaGenerator
name|schemaGen
init|=
operator|new
name|JsonSchemaGenerator
argument_list|(
name|mapper
argument_list|)
decl_stmt|;
name|DefaultPackageScanClassResolver
name|packageScanClassResolver
init|=
operator|new
name|DefaultPackageScanClassResolver
argument_list|()
decl_stmt|;
name|Set
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|schemaClasses
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
comment|// get non-abstract extensions of AbstractDTOBase
name|schemaClasses
operator|.
name|addAll
argument_list|(
name|packageScanClassResolver
operator|.
name|findByFilter
argument_list|(
name|type
lambda|->
operator|!
name|Modifier
operator|.
name|isAbstract
argument_list|(
name|type
operator|.
name|getModifiers
argument_list|()
argument_list|)
operator|&&
name|AbstractDTOBase
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|type
argument_list|)
argument_list|,
literal|"org.apache.camel.component.salesforce.api.dto"
argument_list|)
argument_list|)
expr_stmt|;
comment|// get non-abstract extensions of AbstractDTOBase
name|schemaClasses
operator|.
name|addAll
argument_list|(
name|packageScanClassResolver
operator|.
name|findByFilter
argument_list|(
name|type
lambda|->
operator|!
name|Modifier
operator|.
name|isAbstract
argument_list|(
name|type
operator|.
name|getModifiers
argument_list|()
argument_list|)
operator|&&
name|AbstractDTOBase
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|type
argument_list|)
argument_list|,
literal|"org.apache.camel.component.salesforce.api.dto"
argument_list|)
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|JsonSchema
argument_list|>
name|allSchemas
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Class
argument_list|<
name|?
argument_list|>
name|aClass
range|:
name|schemaClasses
control|)
block|{
name|JsonSchema
name|jsonSchema
init|=
name|schemaGen
operator|.
name|generateSchema
argument_list|(
name|aClass
argument_list|)
decl_stmt|;
name|allSchemas
operator|.
name|add
argument_list|(
name|jsonSchema
argument_list|)
expr_stmt|;
block|}
return|return
name|getJsonSchemaString
argument_list|(
name|mapper
argument_list|,
name|allSchemas
argument_list|,
name|API_DTO_ID
argument_list|)
return|;
block|}
DECL|method|getJsonSchemaString (ObjectMapper mapper, Set<JsonSchema> allSchemas, String id)
specifier|public
specifier|static
name|String
name|getJsonSchemaString
parameter_list|(
name|ObjectMapper
name|mapper
parameter_list|,
name|Set
argument_list|<
name|JsonSchema
argument_list|>
name|allSchemas
parameter_list|,
name|String
name|id
parameter_list|)
throws|throws
name|JsonProcessingException
block|{
name|JsonSchema
name|rootSchema
init|=
name|getJsonSchemaAsSchema
argument_list|(
name|allSchemas
argument_list|,
name|id
argument_list|)
decl_stmt|;
return|return
name|mapper
operator|.
name|writeValueAsString
argument_list|(
name|rootSchema
argument_list|)
return|;
block|}
DECL|method|getJsonSchemaAsSchema (Set<JsonSchema> allSchemas, String id)
specifier|public
specifier|static
name|JsonSchema
name|getJsonSchemaAsSchema
parameter_list|(
name|Set
argument_list|<
name|JsonSchema
argument_list|>
name|allSchemas
parameter_list|,
name|String
name|id
parameter_list|)
block|{
name|ObjectSchema
name|rootSchema
init|=
operator|new
name|ObjectSchema
argument_list|()
decl_stmt|;
name|rootSchema
operator|.
name|set$schema
argument_list|(
name|SCHEMA4
argument_list|)
expr_stmt|;
name|rootSchema
operator|.
name|setId
argument_list|(
name|id
argument_list|)
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
name|Set
argument_list|<
name|Object
argument_list|>
name|tmp
init|=
operator|(
name|Set
operator|)
name|allSchemas
decl_stmt|;
name|rootSchema
operator|.
name|setOneOf
argument_list|(
name|tmp
argument_list|)
expr_stmt|;
return|return
name|rootSchema
return|;
block|}
DECL|method|getSObjectJsonSchema (SObjectDescription description)
specifier|public
specifier|static
name|String
name|getSObjectJsonSchema
parameter_list|(
name|SObjectDescription
name|description
parameter_list|)
throws|throws
name|JsonProcessingException
block|{
return|return
name|getSObjectJsonSchema
argument_list|(
name|description
argument_list|,
literal|true
argument_list|)
return|;
block|}
DECL|method|getSObjectJsonSchemaAsJson (SObjectDescription description)
specifier|public
specifier|static
name|JsonSchema
name|getSObjectJsonSchemaAsJson
parameter_list|(
name|SObjectDescription
name|description
parameter_list|)
throws|throws
name|JsonProcessingException
block|{
return|return
name|getSObjectJsonSchemaAsSchema
argument_list|(
name|description
argument_list|,
literal|true
argument_list|)
return|;
block|}
DECL|method|getSObjectJsonSchema (SObjectDescription description, boolean addQuerySchema)
specifier|public
specifier|static
name|String
name|getSObjectJsonSchema
parameter_list|(
name|SObjectDescription
name|description
parameter_list|,
name|boolean
name|addQuerySchema
parameter_list|)
throws|throws
name|JsonProcessingException
block|{
name|ObjectMapper
name|schemaObjectMapper
init|=
name|createSchemaObjectMapper
argument_list|()
decl_stmt|;
return|return
name|getJsonSchemaString
argument_list|(
name|schemaObjectMapper
argument_list|,
name|getSObjectJsonSchema
argument_list|(
name|schemaObjectMapper
argument_list|,
name|description
argument_list|,
name|DEFAULT_ID_PREFIX
argument_list|,
name|addQuerySchema
argument_list|)
argument_list|,
name|DEFAULT_ID_PREFIX
argument_list|)
return|;
block|}
DECL|method|getSObjectJsonSchemaAsSchema (SObjectDescription description, boolean addQuerySchema)
specifier|public
specifier|static
name|JsonSchema
name|getSObjectJsonSchemaAsSchema
parameter_list|(
name|SObjectDescription
name|description
parameter_list|,
name|boolean
name|addQuerySchema
parameter_list|)
throws|throws
name|JsonProcessingException
block|{
name|ObjectMapper
name|schemaObjectMapper
init|=
name|createSchemaObjectMapper
argument_list|()
decl_stmt|;
return|return
name|getJsonSchemaAsSchema
argument_list|(
name|getSObjectJsonSchema
argument_list|(
name|schemaObjectMapper
argument_list|,
name|description
argument_list|,
name|DEFAULT_ID_PREFIX
argument_list|,
name|addQuerySchema
argument_list|)
argument_list|,
name|DEFAULT_ID_PREFIX
argument_list|)
return|;
block|}
DECL|method|getSObjectJsonSchema (ObjectMapper objectMapper, SObjectDescription description, String idPrefix, boolean addQuerySchema)
specifier|public
specifier|static
name|Set
argument_list|<
name|JsonSchema
argument_list|>
name|getSObjectJsonSchema
parameter_list|(
name|ObjectMapper
name|objectMapper
parameter_list|,
name|SObjectDescription
name|description
parameter_list|,
name|String
name|idPrefix
parameter_list|,
name|boolean
name|addQuerySchema
parameter_list|)
throws|throws
name|JsonProcessingException
block|{
name|Set
argument_list|<
name|JsonSchema
argument_list|>
name|allSchemas
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
comment|// generate SObject schema from description
name|ObjectSchema
name|sobjectSchema
init|=
operator|new
name|ObjectSchema
argument_list|()
decl_stmt|;
name|sobjectSchema
operator|.
name|setId
argument_list|(
name|idPrefix
operator|+
literal|":"
operator|+
name|description
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|sobjectSchema
operator|.
name|setTitle
argument_list|(
name|description
operator|.
name|getLabel
argument_list|()
argument_list|)
expr_stmt|;
name|SimpleTypeSchema
name|addressSchema
init|=
literal|null
decl_stmt|;
name|SimpleTypeSchema
name|geoLocationSchema
init|=
literal|null
decl_stmt|;
for|for
control|(
name|SObjectField
name|field
range|:
name|description
operator|.
name|getFields
argument_list|()
control|)
block|{
name|SimpleTypeSchema
name|fieldSchema
init|=
operator|new
name|NullSchema
argument_list|()
decl_stmt|;
name|String
name|soapType
init|=
name|field
operator|.
name|getSoapType
argument_list|()
decl_stmt|;
switch|switch
condition|(
name|soapType
operator|.
name|substring
argument_list|(
name|soapType
operator|.
name|indexOf
argument_list|(
literal|':'
argument_list|)
operator|+
literal|1
argument_list|)
condition|)
block|{
case|case
literal|"ID"
case|:
comment|// mapping for tns:ID SOAP type
case|case
literal|"string"
case|:
case|case
literal|"base64Binary"
case|:
comment|// Salesforce maps any types like string, picklist, reference, etc. to string
case|case
literal|"anyType"
case|:
name|fieldSchema
operator|=
operator|new
name|StringSchema
argument_list|()
expr_stmt|;
break|break;
case|case
literal|"integer"
case|:
case|case
literal|"int"
case|:
case|case
literal|"long"
case|:
case|case
literal|"short"
case|:
case|case
literal|"byte"
case|:
case|case
literal|"unsignedInt"
case|:
case|case
literal|"unsignedShort"
case|:
case|case
literal|"unsignedByte"
case|:
name|fieldSchema
operator|=
operator|new
name|IntegerSchema
argument_list|()
expr_stmt|;
break|break;
case|case
literal|"decimal"
case|:
case|case
literal|"float"
case|:
case|case
literal|"double"
case|:
name|fieldSchema
operator|=
operator|new
name|NumberSchema
argument_list|()
expr_stmt|;
break|break;
case|case
literal|"boolean"
case|:
name|fieldSchema
operator|=
operator|new
name|BooleanSchema
argument_list|()
expr_stmt|;
break|break;
case|case
literal|"date"
case|:
name|fieldSchema
operator|=
operator|new
name|StringSchema
argument_list|()
expr_stmt|;
operator|(
operator|(
name|StringSchema
operator|)
name|fieldSchema
operator|)
operator|.
name|setFormat
argument_list|(
name|JsonValueFormat
operator|.
name|DATE
argument_list|)
expr_stmt|;
break|break;
case|case
literal|"dateTime"
case|:
case|case
literal|"g"
case|:
name|fieldSchema
operator|=
operator|new
name|StringSchema
argument_list|()
expr_stmt|;
operator|(
operator|(
name|StringSchema
operator|)
name|fieldSchema
operator|)
operator|.
name|setFormat
argument_list|(
name|JsonValueFormat
operator|.
name|DATE_TIME
argument_list|)
expr_stmt|;
break|break;
case|case
literal|"time"
case|:
name|fieldSchema
operator|=
operator|new
name|StringSchema
argument_list|()
expr_stmt|;
operator|(
operator|(
name|StringSchema
operator|)
name|fieldSchema
operator|)
operator|.
name|setFormat
argument_list|(
name|JsonValueFormat
operator|.
name|TIME
argument_list|)
expr_stmt|;
break|break;
case|case
literal|"address"
case|:
if|if
condition|(
name|addressSchema
operator|==
literal|null
condition|)
block|{
name|addressSchema
operator|=
name|getSchemaFromClass
argument_list|(
name|objectMapper
argument_list|,
name|Address
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
name|fieldSchema
operator|=
name|addressSchema
expr_stmt|;
break|break;
case|case
literal|"location"
case|:
if|if
condition|(
name|geoLocationSchema
operator|==
literal|null
condition|)
block|{
name|geoLocationSchema
operator|=
name|getSchemaFromClass
argument_list|(
name|objectMapper
argument_list|,
name|GeoLocation
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
name|fieldSchema
operator|=
name|geoLocationSchema
expr_stmt|;
break|break;
default|default:
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unsupported type "
operator|+
name|soapType
argument_list|)
throw|;
block|}
name|List
argument_list|<
name|PickListValue
argument_list|>
name|picklistValues
init|=
name|field
operator|.
name|getPicklistValues
argument_list|()
decl_stmt|;
switch|switch
condition|(
name|field
operator|.
name|getType
argument_list|()
condition|)
block|{
case|case
literal|"picklist"
case|:
name|fieldSchema
operator|.
name|asStringSchema
argument_list|()
operator|.
name|setEnums
argument_list|(
name|picklistValues
operator|==
literal|null
condition|?
name|Collections
operator|.
name|emptySet
argument_list|()
else|:
name|picklistValues
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|PickListValue
operator|::
name|getValue
argument_list|)
operator|.
name|distinct
argument_list|()
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toSet
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
break|break;
case|case
literal|"multipicklist"
case|:
comment|// TODO regex needs more work to not allow values not separated by ','
name|fieldSchema
operator|.
name|asStringSchema
argument_list|()
operator|.
name|setPattern
argument_list|(
name|picklistValues
operator|==
literal|null
condition|?
literal|""
else|:
name|picklistValues
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|val
lambda|->
literal|"(,?("
operator|+
name|val
operator|.
name|getValue
argument_list|()
operator|+
literal|"))"
argument_list|)
operator|.
name|distinct
argument_list|()
operator|.
name|collect
argument_list|(
name|joining
argument_list|(
literal|"|"
argument_list|,
literal|"("
argument_list|,
literal|")"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
break|break;
default|default:
comment|// nothing to fix
block|}
comment|// additional field properties
name|fieldSchema
operator|.
name|setTitle
argument_list|(
name|field
operator|.
name|getLabel
argument_list|()
argument_list|)
expr_stmt|;
name|fieldSchema
operator|.
name|setDefault
argument_list|(
name|field
operator|.
name|getDefaultValue
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|field
operator|.
name|isUpdateable
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|fieldSchema
operator|.
name|setReadonly
argument_list|(
operator|!
name|field
operator|.
name|isUpdateable
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|final
name|String
name|descriptionText
init|=
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|Object
index|[]
block|{
literal|"unique"
block|,
name|field
operator|.
name|isUnique
argument_list|()
block|}
argument_list|,
operator|new
name|Object
index|[]
block|{
literal|"idLookup"
block|,
name|field
operator|.
name|isIdLookup
argument_list|()
block|}
argument_list|,
operator|new
name|Object
index|[]
block|{
literal|"autoNumber"
block|,
name|field
operator|.
name|isAutoNumber
argument_list|()
block|}
argument_list|,
operator|new
name|Object
index|[]
block|{
literal|"calculated"
block|,
name|field
operator|.
name|isCalculated
argument_list|()
block|}
argument_list|)
operator|.
name|stream
argument_list|()
operator|.
name|filter
argument_list|(
name|ary
lambda|->
name|Boolean
operator|.
name|TRUE
operator|.
name|equals
argument_list|(
name|ary
index|[
literal|1
index|]
argument_list|)
argument_list|)
operator|.
name|map
argument_list|(
name|ary
lambda|->
name|String
operator|.
name|valueOf
argument_list|(
name|ary
index|[
literal|0
index|]
argument_list|)
argument_list|)
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|joining
argument_list|(
literal|","
argument_list|)
argument_list|)
decl_stmt|;
comment|// JSON schema currently does not support the above attributes so we'll store this information
comment|// in the description
name|fieldSchema
operator|.
name|setDescription
argument_list|(
name|descriptionText
argument_list|)
expr_stmt|;
comment|// add property to sobject schema
if|if
condition|(
name|field
operator|.
name|isNillable
argument_list|()
condition|)
block|{
name|sobjectSchema
operator|.
name|putOptionalProperty
argument_list|(
name|field
operator|.
name|getName
argument_list|()
argument_list|,
name|fieldSchema
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|sobjectSchema
operator|.
name|putProperty
argument_list|(
name|field
operator|.
name|getName
argument_list|()
argument_list|,
name|fieldSchema
argument_list|)
expr_stmt|;
block|}
block|}
comment|// add sobject schema to root schema
name|allSchemas
operator|.
name|add
argument_list|(
name|sobjectSchema
argument_list|)
expr_stmt|;
if|if
condition|(
name|addQuerySchema
condition|)
block|{
comment|// add a simple query schema for this sobject for lookups, etc.
name|ObjectSchema
name|queryRecords
init|=
name|getSchemaFromClass
argument_list|(
name|objectMapper
argument_list|,
name|AbstractQueryRecordsBase
operator|.
name|class
argument_list|)
decl_stmt|;
name|queryRecords
operator|.
name|setId
argument_list|(
name|idPrefix
operator|+
literal|":QueryRecords"
operator|+
name|description
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|ObjectSchema
name|refSchema
init|=
operator|new
name|ObjectSchema
argument_list|()
decl_stmt|;
name|refSchema
operator|.
name|set$ref
argument_list|(
name|sobjectSchema
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|ArraySchema
name|recordsProperty
init|=
operator|new
name|ArraySchema
argument_list|()
decl_stmt|;
name|recordsProperty
operator|.
name|setItems
argument_list|(
operator|new
name|ArraySchema
operator|.
name|SingleItems
argument_list|(
name|refSchema
argument_list|)
argument_list|)
expr_stmt|;
name|queryRecords
operator|.
name|putProperty
argument_list|(
literal|"records"
argument_list|,
name|recordsProperty
argument_list|)
expr_stmt|;
name|allSchemas
operator|.
name|add
argument_list|(
name|queryRecords
argument_list|)
expr_stmt|;
block|}
return|return
name|allSchemas
return|;
block|}
DECL|method|createSchemaObjectMapper ()
specifier|public
specifier|static
name|ObjectMapper
name|createSchemaObjectMapper
parameter_list|()
block|{
name|ObjectMapper
name|objectMapper
init|=
name|createObjectMapper
argument_list|()
decl_stmt|;
name|objectMapper
operator|.
name|configure
argument_list|(
name|SerializationFeature
operator|.
name|WRITE_ENUMS_USING_TO_STRING
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|objectMapper
operator|.
name|configure
argument_list|(
name|SerializationFeature
operator|.
name|INDENT_OUTPUT
argument_list|,
literal|true
argument_list|)
expr_stmt|;
return|return
name|objectMapper
return|;
block|}
DECL|method|getSchemaFromClass (ObjectMapper objectMapper, Class<?> type)
specifier|private
specifier|static
name|ObjectSchema
name|getSchemaFromClass
parameter_list|(
name|ObjectMapper
name|objectMapper
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|)
throws|throws
name|JsonMappingException
block|{
return|return
operator|new
name|JsonSchemaGenerator
argument_list|(
name|objectMapper
argument_list|)
operator|.
name|generateSchema
argument_list|(
name|type
argument_list|)
operator|.
name|asObjectSchema
argument_list|()
return|;
block|}
DECL|method|getGlobalObjectsJsonSchemaAsSchema (final GlobalObjects globalObjects)
specifier|public
specifier|static
name|JsonSchema
name|getGlobalObjectsJsonSchemaAsSchema
parameter_list|(
specifier|final
name|GlobalObjects
name|globalObjects
parameter_list|)
block|{
specifier|final
name|Set
argument_list|<
name|JsonSchema
argument_list|>
name|allSchemas
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|SObject
name|sobject
range|:
name|globalObjects
operator|.
name|getSobjects
argument_list|()
control|)
block|{
comment|// generate SObject schema from description
name|ObjectSchema
name|sobjectSchema
init|=
operator|new
name|ObjectSchema
argument_list|()
decl_stmt|;
name|sobjectSchema
operator|.
name|setId
argument_list|(
name|DEFAULT_ID_PREFIX
operator|+
literal|":"
operator|+
name|sobject
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|sobjectSchema
operator|.
name|setTitle
argument_list|(
name|sobject
operator|.
name|getLabel
argument_list|()
argument_list|)
expr_stmt|;
name|allSchemas
operator|.
name|add
argument_list|(
name|sobjectSchema
argument_list|)
expr_stmt|;
block|}
return|return
name|getJsonSchemaAsSchema
argument_list|(
name|allSchemas
argument_list|,
name|DEFAULT_ID_PREFIX
operator|+
literal|":GlobalObjects"
argument_list|)
return|;
block|}
DECL|method|withNullSerialization (final ObjectMapper objectMapper)
specifier|public
specifier|static
name|ObjectMapper
name|withNullSerialization
parameter_list|(
specifier|final
name|ObjectMapper
name|objectMapper
parameter_list|)
block|{
specifier|final
name|SerializerFactory
name|factory
init|=
name|BeanSerializerFactory
operator|.
name|instance
operator|.
name|withSerializerModifier
argument_list|(
operator|new
name|BeanSerializerModifier
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|JsonSerializer
argument_list|<
name|?
argument_list|>
name|modifySerializer
parameter_list|(
specifier|final
name|SerializationConfig
name|config
parameter_list|,
specifier|final
name|BeanDescription
name|beanDesc
parameter_list|,
specifier|final
name|JsonSerializer
argument_list|<
name|?
argument_list|>
name|serializer
parameter_list|)
block|{
for|for
control|(
specifier|final
name|PropertyWriter
name|writer
range|:
operator|(
name|Iterable
argument_list|<
name|PropertyWriter
argument_list|>
operator|)
name|serializer
operator|::
name|properties
control|)
block|{
if|if
condition|(
name|writer
operator|instanceof
name|BeanPropertyWriter
condition|)
block|{
operator|(
operator|(
name|BeanPropertyWriter
operator|)
name|writer
operator|)
operator|.
name|assignNullSerializer
argument_list|(
name|NullSerializer
operator|.
name|instance
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|serializer
return|;
block|}
block|}
argument_list|)
decl_stmt|;
return|return
name|objectMapper
operator|.
name|copy
argument_list|()
operator|.
name|setSerializerFactory
argument_list|(
name|factory
argument_list|)
return|;
block|}
block|}
end_class

end_unit

