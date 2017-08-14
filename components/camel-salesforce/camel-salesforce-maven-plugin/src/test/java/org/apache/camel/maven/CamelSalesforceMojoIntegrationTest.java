begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.maven
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|maven
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileNotFoundException
import|;
end_import

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
name|Properties
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
name|maven
operator|.
name|plugin
operator|.
name|logging
operator|.
name|SystemStreamLog
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

begin_class
DECL|class|CamelSalesforceMojoIntegrationTest
specifier|public
class|class
name|CamelSalesforceMojoIntegrationTest
block|{
DECL|field|TEST_LOGIN_PROPERTIES
specifier|private
specifier|static
specifier|final
name|String
name|TEST_LOGIN_PROPERTIES
init|=
literal|"../test-salesforce-login.properties"
decl_stmt|;
annotation|@
name|Test
DECL|method|testExecute ()
specifier|public
name|void
name|testExecute
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelSalesforceMojo
name|mojo
init|=
name|createMojo
argument_list|()
decl_stmt|;
comment|// generate code
name|mojo
operator|.
name|execute
argument_list|()
expr_stmt|;
comment|// validate generated code
comment|// check that it was generated
name|Assert
operator|.
name|assertTrue
argument_list|(
literal|"Output directory was not created"
argument_list|,
name|mojo
operator|.
name|outputDirectory
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
comment|// TODO check that the generated code compiles
block|}
annotation|@
name|Test
DECL|method|testExecuteJsonSchema ()
specifier|public
name|void
name|testExecuteJsonSchema
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelSalesforceMojo
name|mojo
init|=
name|createMojo
argument_list|()
decl_stmt|;
name|mojo
operator|.
name|jsonSchema
operator|=
literal|true
expr_stmt|;
name|mojo
operator|.
name|jsonSchemaFilename
operator|=
literal|"test-schema.json"
expr_stmt|;
name|mojo
operator|.
name|jsonSchemaId
operator|=
name|JsonUtils
operator|.
name|DEFAULT_ID_PREFIX
expr_stmt|;
comment|// generate code
name|mojo
operator|.
name|execute
argument_list|()
expr_stmt|;
comment|// validate generated schema
name|File
name|schemaFile
init|=
name|mojo
operator|.
name|outputDirectory
operator|.
name|toPath
argument_list|()
operator|.
name|resolve
argument_list|(
literal|"test-schema.json"
argument_list|)
operator|.
name|toFile
argument_list|()
decl_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
literal|"Output file was not created"
argument_list|,
name|schemaFile
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|ObjectMapper
name|objectMapper
init|=
name|JsonUtils
operator|.
name|createObjectMapper
argument_list|()
decl_stmt|;
name|JsonSchema
name|jsonSchema
init|=
name|objectMapper
operator|.
name|readValue
argument_list|(
name|schemaFile
argument_list|,
name|JsonSchema
operator|.
name|class
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
literal|"Expected root JSON schema with oneOf element"
argument_list|,
name|jsonSchema
operator|.
name|isObjectSchema
argument_list|()
operator|&&
operator|!
operator|(
operator|(
name|ObjectSchema
operator|)
name|jsonSchema
operator|)
operator|.
name|getOneOf
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|createMojo ()
specifier|protected
name|CamelSalesforceMojo
name|createMojo
parameter_list|()
throws|throws
name|IOException
block|{
name|CamelSalesforceMojo
name|mojo
init|=
operator|new
name|CamelSalesforceMojo
argument_list|()
decl_stmt|;
name|mojo
operator|.
name|setLog
argument_list|(
operator|new
name|SystemStreamLog
argument_list|()
argument_list|)
expr_stmt|;
comment|// set login properties
name|setLoginProperties
argument_list|(
name|mojo
argument_list|)
expr_stmt|;
comment|// set defaults
name|mojo
operator|.
name|version
operator|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"apiVersion"
argument_list|,
name|SalesforceEndpointConfig
operator|.
name|DEFAULT_VERSION
argument_list|)
expr_stmt|;
name|mojo
operator|.
name|loginUrl
operator|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"loginUrl"
argument_list|,
name|SalesforceLoginConfig
operator|.
name|DEFAULT_LOGIN_URL
argument_list|)
expr_stmt|;
name|mojo
operator|.
name|outputDirectory
operator|=
operator|new
name|File
argument_list|(
literal|"target/generated-sources/camel-salesforce"
argument_list|)
expr_stmt|;
name|mojo
operator|.
name|packageName
operator|=
literal|"org.apache.camel.salesforce.dto"
expr_stmt|;
comment|// set code generation properties
name|mojo
operator|.
name|includePattern
operator|=
literal|"(.*__c)|(PushTopic)|(Document)|(Account)"
expr_stmt|;
comment|// remove generated code directory
if|if
condition|(
name|mojo
operator|.
name|outputDirectory
operator|.
name|exists
argument_list|()
condition|)
block|{
comment|// remove old files
for|for
control|(
name|File
name|file
range|:
name|mojo
operator|.
name|outputDirectory
operator|.
name|listFiles
argument_list|()
control|)
block|{
name|file
operator|.
name|delete
argument_list|()
expr_stmt|;
block|}
name|mojo
operator|.
name|outputDirectory
operator|.
name|delete
argument_list|()
expr_stmt|;
block|}
return|return
name|mojo
return|;
block|}
DECL|method|setLoginProperties (CamelSalesforceMojo mojo)
specifier|private
name|void
name|setLoginProperties
parameter_list|(
name|CamelSalesforceMojo
name|mojo
parameter_list|)
throws|throws
name|IOException
block|{
comment|// load test-salesforce-login properties
name|Properties
name|properties
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|InputStream
name|stream
init|=
literal|null
decl_stmt|;
try|try
block|{
name|stream
operator|=
operator|new
name|FileInputStream
argument_list|(
name|TEST_LOGIN_PROPERTIES
argument_list|)
expr_stmt|;
name|properties
operator|.
name|load
argument_list|(
name|stream
argument_list|)
expr_stmt|;
name|mojo
operator|.
name|clientId
operator|=
name|properties
operator|.
name|getProperty
argument_list|(
literal|"salesforce.client.id"
argument_list|)
expr_stmt|;
name|mojo
operator|.
name|clientSecret
operator|=
name|properties
operator|.
name|getProperty
argument_list|(
literal|"salesforce.client.secret"
argument_list|)
expr_stmt|;
name|mojo
operator|.
name|userName
operator|=
name|properties
operator|.
name|getProperty
argument_list|(
literal|"salesforce.username"
argument_list|)
expr_stmt|;
name|mojo
operator|.
name|password
operator|=
name|properties
operator|.
name|getProperty
argument_list|(
literal|"salesforce.password"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|FileNotFoundException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|FileNotFoundException
argument_list|(
literal|"Create a properties file named "
operator|+
name|TEST_LOGIN_PROPERTIES
operator|+
literal|" with clientId, clientSecret, userName, password"
operator|+
literal|" for a Salesforce account with Merchandise and Invoice objects from Salesforce Guides."
argument_list|)
throw|;
block|}
finally|finally
block|{
if|if
condition|(
name|stream
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|stream
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ignore
parameter_list|)
block|{
comment|// noop
block|}
block|}
block|}
block|}
block|}
end_class

end_unit

