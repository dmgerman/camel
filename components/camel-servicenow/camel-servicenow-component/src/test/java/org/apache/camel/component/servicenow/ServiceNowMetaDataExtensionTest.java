begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.servicenow
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|servicenow
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
name|extension
operator|.
name|MetaDataExtension
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
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
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_class
DECL|class|ServiceNowMetaDataExtensionTest
specifier|public
class|class
name|ServiceNowMetaDataExtensionTest
extends|extends
name|ServiceNowTestSupport
block|{
DECL|field|LOGGER
specifier|private
specifier|static
specifier|final
name|Logger
name|LOGGER
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|ServiceNowMetaDataExtensionTest
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|ServiceNowMetaDataExtensionTest ()
specifier|public
name|ServiceNowMetaDataExtensionTest
parameter_list|()
block|{
name|super
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|isUseRouteBuilder ()
specifier|public
name|boolean
name|isUseRouteBuilder
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
DECL|method|getComponent ()
specifier|protected
name|ServiceNowComponent
name|getComponent
parameter_list|()
block|{
return|return
name|context
argument_list|()
operator|.
name|getComponent
argument_list|(
literal|"servicenow"
argument_list|,
name|ServiceNowComponent
operator|.
name|class
argument_list|)
return|;
block|}
DECL|method|getExtension ()
specifier|protected
name|MetaDataExtension
name|getExtension
parameter_list|()
block|{
return|return
name|getComponent
argument_list|()
operator|.
name|getExtension
argument_list|(
name|MetaDataExtension
operator|.
name|class
argument_list|)
operator|.
name|orElseThrow
argument_list|(
name|UnsupportedOperationException
operator|::
operator|new
argument_list|)
return|;
block|}
comment|// *********************************
comment|//
comment|// *********************************
annotation|@
name|Test
DECL|method|testMetaData ()
specifier|public
name|void
name|testMetaData
parameter_list|()
throws|throws
name|Exception
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
init|=
name|getParameters
argument_list|()
decl_stmt|;
name|parameters
operator|.
name|put
argument_list|(
literal|"objectType"
argument_list|,
literal|"table"
argument_list|)
expr_stmt|;
name|parameters
operator|.
name|put
argument_list|(
literal|"objectName"
argument_list|,
literal|"incident"
argument_list|)
expr_stmt|;
comment|//parameters.put("object.sys_user.fields", "first_name,last_name");
comment|//parameters.put("object.incident.fields", "caller_id,severity,resolved_at,sys_id");
comment|//parameters.put("object.incident.fields", "^sys_.*$");
comment|//parameters.put("object.task.fields", "");
name|MetaDataExtension
operator|.
name|MetaData
name|result
init|=
name|getExtension
argument_list|()
operator|.
name|meta
argument_list|(
name|parameters
argument_list|)
operator|.
name|orElseThrow
argument_list|(
name|RuntimeException
operator|::
operator|new
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"application/schema+json"
argument_list|,
name|result
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
expr_stmt|;
name|Assert
operator|.
name|assertNotNull
argument_list|(
name|result
operator|.
name|getAttribute
argument_list|(
literal|"date.format"
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertNotNull
argument_list|(
name|result
operator|.
name|getAttribute
argument_list|(
literal|"time.format"
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertNotNull
argument_list|(
name|result
operator|.
name|getAttribute
argument_list|(
literal|"date-time.format"
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|JsonNode
operator|.
name|class
argument_list|,
name|result
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
expr_stmt|;
name|Assert
operator|.
name|assertNotNull
argument_list|(
name|result
operator|.
name|getPayload
argument_list|(
name|JsonNode
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertNotNull
argument_list|(
name|result
operator|.
name|getPayload
argument_list|(
name|JsonNode
operator|.
name|class
argument_list|)
operator|.
name|get
argument_list|(
literal|"properties"
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertNotNull
argument_list|(
name|result
operator|.
name|getPayload
argument_list|(
name|JsonNode
operator|.
name|class
argument_list|)
operator|.
name|get
argument_list|(
literal|"$schema"
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"http://json-schema.org/schema#"
argument_list|,
name|result
operator|.
name|getPayload
argument_list|(
name|JsonNode
operator|.
name|class
argument_list|)
operator|.
name|get
argument_list|(
literal|"$schema"
argument_list|)
operator|.
name|asText
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertNotNull
argument_list|(
name|result
operator|.
name|getPayload
argument_list|(
name|JsonNode
operator|.
name|class
argument_list|)
operator|.
name|get
argument_list|(
literal|"id"
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertNotNull
argument_list|(
name|result
operator|.
name|getPayload
argument_list|(
name|JsonNode
operator|.
name|class
argument_list|)
operator|.
name|get
argument_list|(
literal|"type"
argument_list|)
argument_list|)
expr_stmt|;
name|LOGGER
operator|.
name|debug
argument_list|(
operator|new
name|ObjectMapper
argument_list|()
operator|.
name|writerWithDefaultPrettyPrinter
argument_list|()
operator|.
name|writeValueAsString
argument_list|(
name|result
operator|.
name|getPayload
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|UnsupportedOperationException
operator|.
name|class
argument_list|)
DECL|method|testInvalidObjectType ()
specifier|public
name|void
name|testInvalidObjectType
parameter_list|()
throws|throws
name|Exception
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
init|=
name|getParameters
argument_list|()
decl_stmt|;
name|parameters
operator|.
name|put
argument_list|(
literal|"objectType"
argument_list|,
literal|"test"
argument_list|)
expr_stmt|;
name|parameters
operator|.
name|put
argument_list|(
literal|"objectName"
argument_list|,
literal|"incident"
argument_list|)
expr_stmt|;
name|getExtension
argument_list|()
operator|.
name|meta
argument_list|(
name|parameters
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

