begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.google.sheets
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
name|sheets
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
name|Random
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
name|http
operator|.
name|javanet
operator|.
name|NetHttpTransport
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
name|json
operator|.
name|jackson2
operator|.
name|JacksonFactory
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
name|sheets
operator|.
name|v4
operator|.
name|Sheets
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
name|sheets
operator|.
name|v4
operator|.
name|model
operator|.
name|Sheet
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
name|sheets
operator|.
name|v4
operator|.
name|model
operator|.
name|SheetProperties
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
name|sheets
operator|.
name|v4
operator|.
name|model
operator|.
name|Spreadsheet
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
name|sheets
operator|.
name|v4
operator|.
name|model
operator|.
name|SpreadsheetProperties
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
name|sheets
operator|.
name|v4
operator|.
name|model
operator|.
name|ValueRange
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
name|CamelExecutionException
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
name|sheets
operator|.
name|internal
operator|.
name|GoogleSheetsConstants
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
name|sheets
operator|.
name|server
operator|.
name|GoogleSheetsApiTestServer
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
name|sheets
operator|.
name|server
operator|.
name|GoogleSheetsApiTestServerRule
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
name|PropertyBindingSupport
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

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|ClassRule
import|;
end_import

begin_comment
comment|/**  * Abstract base class for GoogleSheets Integration tests generated by Camel API  * component maven plugin.  */
end_comment

begin_class
DECL|class|AbstractGoogleSheetsTestSupport
specifier|public
class|class
name|AbstractGoogleSheetsTestSupport
extends|extends
name|CamelTestSupport
block|{
DECL|field|TEST_SHEET
specifier|protected
specifier|static
specifier|final
name|String
name|TEST_SHEET
init|=
literal|"TestData"
decl_stmt|;
DECL|field|TEST_OPTIONS_PROPERTIES
specifier|private
specifier|static
specifier|final
name|String
name|TEST_OPTIONS_PROPERTIES
init|=
literal|"/test-options.properties"
decl_stmt|;
annotation|@
name|ClassRule
DECL|field|googleSheetsApiTestServerRule
specifier|public
specifier|static
name|GoogleSheetsApiTestServerRule
name|googleSheetsApiTestServerRule
init|=
operator|new
name|GoogleSheetsApiTestServerRule
argument_list|(
name|TEST_OPTIONS_PROPERTIES
argument_list|)
decl_stmt|;
DECL|field|spreadsheet
specifier|private
name|Spreadsheet
name|spreadsheet
decl_stmt|;
comment|/**      * Create test spreadsheet that is used throughout all tests.      */
DECL|method|createTestSpreadsheet ()
specifier|private
name|void
name|createTestSpreadsheet
parameter_list|()
block|{
name|Spreadsheet
name|spreadsheet
init|=
operator|new
name|Spreadsheet
argument_list|()
decl_stmt|;
name|SpreadsheetProperties
name|spreadsheetProperties
init|=
operator|new
name|SpreadsheetProperties
argument_list|()
decl_stmt|;
name|spreadsheetProperties
operator|.
name|setTitle
argument_list|(
literal|"camel-sheets-"
operator|+
operator|new
name|Random
argument_list|()
operator|.
name|nextInt
argument_list|(
name|Integer
operator|.
name|MAX_VALUE
argument_list|)
argument_list|)
expr_stmt|;
name|spreadsheet
operator|.
name|setProperties
argument_list|(
name|spreadsheetProperties
argument_list|)
expr_stmt|;
name|Sheet
name|sheet
init|=
operator|new
name|Sheet
argument_list|()
decl_stmt|;
name|SheetProperties
name|sheetProperties
init|=
operator|new
name|SheetProperties
argument_list|()
decl_stmt|;
name|sheetProperties
operator|.
name|setTitle
argument_list|(
name|TEST_SHEET
argument_list|)
expr_stmt|;
name|sheet
operator|.
name|setProperties
argument_list|(
name|sheetProperties
argument_list|)
expr_stmt|;
name|spreadsheet
operator|.
name|setSheets
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|sheet
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|spreadsheet
operator|=
name|requestBody
argument_list|(
literal|"google-sheets://spreadsheets/create?inBody=content"
argument_list|,
name|spreadsheet
argument_list|)
expr_stmt|;
block|}
comment|/**      * Add some initial test data to test spreadsheet.      */
DECL|method|createTestData ()
specifier|private
name|void
name|createTestData
parameter_list|()
block|{
if|if
condition|(
name|spreadsheet
operator|==
literal|null
condition|)
block|{
name|createTestSpreadsheet
argument_list|()
expr_stmt|;
block|}
name|ValueRange
name|valueRange
init|=
operator|new
name|ValueRange
argument_list|()
decl_stmt|;
name|valueRange
operator|.
name|setValues
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
literal|"a1"
argument_list|,
literal|"b1"
argument_list|)
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
literal|"a2"
argument_list|,
literal|"b2"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
comment|// parameter type is String
name|headers
operator|.
name|put
argument_list|(
name|GoogleSheetsConstants
operator|.
name|PROPERTY_PREFIX
operator|+
literal|"spreadsheetId"
argument_list|,
name|spreadsheet
operator|.
name|getSpreadsheetId
argument_list|()
argument_list|)
expr_stmt|;
comment|// parameter type is String
name|headers
operator|.
name|put
argument_list|(
name|GoogleSheetsConstants
operator|.
name|PROPERTY_PREFIX
operator|+
literal|"range"
argument_list|,
name|TEST_SHEET
operator|+
literal|"!A1:B2"
argument_list|)
expr_stmt|;
comment|// parameter type is String
name|headers
operator|.
name|put
argument_list|(
name|GoogleSheetsConstants
operator|.
name|PROPERTY_PREFIX
operator|+
literal|"valueInputOption"
argument_list|,
literal|"USER_ENTERED"
argument_list|)
expr_stmt|;
name|requestBodyAndHeaders
argument_list|(
literal|"google-sheets://data/update?inBody=values"
argument_list|,
name|valueRange
argument_list|,
name|headers
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
specifier|final
name|CamelContext
name|context
init|=
name|super
operator|.
name|createCamelContext
argument_list|()
decl_stmt|;
specifier|final
name|GoogleSheetsConfiguration
name|configuration
init|=
operator|new
name|GoogleSheetsConfiguration
argument_list|()
decl_stmt|;
name|PropertyBindingSupport
operator|.
name|bindProperties
argument_list|(
name|context
argument_list|,
name|configuration
argument_list|,
name|getTestOptions
argument_list|()
argument_list|)
expr_stmt|;
comment|// add GoogleSheetsComponent to Camel context and use localhost url
specifier|final
name|GoogleSheetsComponent
name|component
init|=
operator|new
name|GoogleSheetsComponent
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|component
operator|.
name|setClientFactory
argument_list|(
operator|new
name|BatchGoogleSheetsClientFactory
argument_list|(
operator|new
name|NetHttpTransport
operator|.
name|Builder
argument_list|()
operator|.
name|trustCertificatesFromJavaKeyStore
argument_list|(
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"/"
operator|+
name|GoogleSheetsApiTestServerRule
operator|.
name|SERVER_KEYSTORE
argument_list|)
argument_list|,
name|GoogleSheetsApiTestServerRule
operator|.
name|SERVER_KEYSTORE_PASSWORD
argument_list|)
operator|.
name|build
argument_list|()
argument_list|,
operator|new
name|JacksonFactory
argument_list|()
argument_list|)
block|{
annotation|@
name|Override
specifier|protected
name|void
name|configure
parameter_list|(
name|Sheets
operator|.
name|Builder
name|clientBuilder
parameter_list|)
block|{
name|clientBuilder
operator|.
name|setRootUrl
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"https://localhost:%s/"
argument_list|,
name|googleSheetsApiTestServerRule
operator|.
name|getServerPort
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|component
operator|.
name|setConfiguration
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
name|context
operator|.
name|addComponent
argument_list|(
literal|"google-sheets"
argument_list|,
name|component
argument_list|)
expr_stmt|;
return|return
name|context
return|;
block|}
comment|/**      * Read component configuration from TEST_OPTIONS_PROPERTIES.      *       * @return Map of component options.      * @throws IOException when TEST_OPTIONS_PROPERTIES could not be loaded.      */
DECL|method|getTestOptions ()
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getTestOptions
parameter_list|()
throws|throws
name|IOException
block|{
specifier|final
name|Properties
name|properties
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
try|try
block|{
name|properties
operator|.
name|load
argument_list|(
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
name|TEST_OPTIONS_PROPERTIES
argument_list|)
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
name|IOException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"%s could not be loaded: %s"
argument_list|,
name|TEST_OPTIONS_PROPERTIES
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
argument_list|,
name|e
argument_list|)
throw|;
block|}
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|options
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|entry
range|:
name|properties
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|options
operator|.
name|put
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|options
return|;
block|}
annotation|@
name|Override
DECL|method|isCreateCamelContextPerClass ()
specifier|public
name|boolean
name|isCreateCamelContextPerClass
parameter_list|()
block|{
comment|// only create the context once for this class
return|return
literal|true
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|requestBodyAndHeaders (String endpointUri, Object body, Map<String, Object> headers)
specifier|protected
parameter_list|<
name|T
parameter_list|>
name|T
name|requestBodyAndHeaders
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|Object
name|body
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
parameter_list|)
throws|throws
name|CamelExecutionException
block|{
return|return
operator|(
name|T
operator|)
name|template
argument_list|()
operator|.
name|requestBodyAndHeaders
argument_list|(
name|endpointUri
argument_list|,
name|body
argument_list|,
name|headers
argument_list|)
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|requestBody (String endpoint, Object body)
specifier|protected
parameter_list|<
name|T
parameter_list|>
name|T
name|requestBody
parameter_list|(
name|String
name|endpoint
parameter_list|,
name|Object
name|body
parameter_list|)
throws|throws
name|CamelExecutionException
block|{
return|return
operator|(
name|T
operator|)
name|template
argument_list|()
operator|.
name|requestBody
argument_list|(
name|endpoint
argument_list|,
name|body
argument_list|)
return|;
block|}
DECL|method|getSpreadsheet ()
specifier|public
name|Spreadsheet
name|getSpreadsheet
parameter_list|()
block|{
if|if
condition|(
name|spreadsheet
operator|==
literal|null
condition|)
block|{
name|createTestSpreadsheet
argument_list|()
expr_stmt|;
block|}
return|return
name|spreadsheet
return|;
block|}
DECL|method|applyTestData (Spreadsheet spreadsheet)
specifier|public
name|Spreadsheet
name|applyTestData
parameter_list|(
name|Spreadsheet
name|spreadsheet
parameter_list|)
block|{
name|createTestData
argument_list|()
expr_stmt|;
return|return
name|spreadsheet
return|;
block|}
DECL|method|setSpreadsheet (Spreadsheet sheet)
specifier|public
name|void
name|setSpreadsheet
parameter_list|(
name|Spreadsheet
name|sheet
parameter_list|)
block|{
name|this
operator|.
name|spreadsheet
operator|=
name|sheet
expr_stmt|;
block|}
DECL|method|getGoogleApiTestServer ()
specifier|public
name|GoogleSheetsApiTestServer
name|getGoogleApiTestServer
parameter_list|()
block|{
return|return
name|googleSheetsApiTestServerRule
operator|.
name|getGoogleApiTestServer
argument_list|()
return|;
block|}
block|}
end_class

end_unit

