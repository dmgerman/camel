begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|AppendValuesResponse
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
name|ClearValuesRequest
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
name|ClearValuesResponse
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
name|UpdateValuesResponse
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
name|builder
operator|.
name|RouteBuilder
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
name|GoogleSheetsApiCollection
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
name|SheetsSpreadsheetsValuesApiMethod
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

begin_comment
comment|/**  * Test class for {@link com.google.api.services.sheets.v4.Sheets.Spreadsheets.Values} APIs.  */
end_comment

begin_class
DECL|class|SheetsSpreadsheetsValuesIntegrationTest
specifier|public
class|class
name|SheetsSpreadsheetsValuesIntegrationTest
extends|extends
name|AbstractGoogleSheetsTestSupport
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|SheetsSpreadsheetsValuesIntegrationTest
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|PATH_PREFIX
specifier|private
specifier|static
specifier|final
name|String
name|PATH_PREFIX
init|=
name|GoogleSheetsApiCollection
operator|.
name|getCollection
argument_list|()
operator|.
name|getApiName
argument_list|(
name|SheetsSpreadsheetsValuesApiMethod
operator|.
name|class
argument_list|)
operator|.
name|getName
argument_list|()
decl_stmt|;
annotation|@
name|Test
DECL|method|testGet ()
specifier|public
name|void
name|testGet
parameter_list|()
throws|throws
name|Exception
block|{
name|Spreadsheet
name|testSheet
init|=
name|getSpreadsheet
argument_list|()
decl_stmt|;
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
literal|"CamelGoogleSheets.spreadsheetId"
argument_list|,
name|testSheet
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
literal|"CamelGoogleSheets.range"
argument_list|,
name|TEST_SHEET
operator|+
literal|"!A1:B2"
argument_list|)
expr_stmt|;
specifier|final
name|ValueRange
name|result
init|=
name|requestBodyAndHeaders
argument_list|(
literal|"direct://GET"
argument_list|,
literal|null
argument_list|,
name|headers
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"get result is null"
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|TEST_SHEET
operator|+
literal|"!A1:B2"
argument_list|,
name|result
operator|.
name|getRange
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
literal|"expected empty value range but found entries"
argument_list|,
name|result
operator|.
name|getValues
argument_list|()
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"get: "
operator|+
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testUpdate ()
specifier|public
name|void
name|testUpdate
parameter_list|()
throws|throws
name|Exception
block|{
name|Spreadsheet
name|testSheet
init|=
name|getSpreadsheet
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|List
argument_list|<
name|Object
argument_list|>
argument_list|>
name|data
init|=
name|Arrays
operator|.
name|asList
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
literal|"A1"
argument_list|,
literal|"B1"
argument_list|)
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
literal|"A2"
argument_list|,
literal|"B2"
argument_list|)
argument_list|)
decl_stmt|;
name|ValueRange
name|values
init|=
operator|new
name|ValueRange
argument_list|()
decl_stmt|;
name|values
operator|.
name|setValues
argument_list|(
name|data
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
literal|"CamelGoogleSheets.spreadsheetId"
argument_list|,
name|testSheet
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
literal|"CamelGoogleSheets.range"
argument_list|,
name|TEST_SHEET
operator|+
literal|"!A1:B2"
argument_list|)
expr_stmt|;
comment|// parameter type is com.google.api.services.sheets.v4.model.ValueRange
name|headers
operator|.
name|put
argument_list|(
literal|"CamelGoogleSheets.values"
argument_list|,
name|values
argument_list|)
expr_stmt|;
comment|// parameter type is String
name|headers
operator|.
name|put
argument_list|(
literal|"CamelGoogleSheets.valueInputOption"
argument_list|,
literal|"USER_ENTERED"
argument_list|)
expr_stmt|;
specifier|final
name|UpdateValuesResponse
name|result
init|=
name|requestBodyAndHeaders
argument_list|(
literal|"direct://UPDATE"
argument_list|,
literal|null
argument_list|,
name|headers
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"update result is null"
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|testSheet
operator|.
name|getSpreadsheetId
argument_list|()
argument_list|,
name|result
operator|.
name|getSpreadsheetId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|TEST_SHEET
operator|+
literal|"!A1:B2"
argument_list|,
name|result
operator|.
name|getUpdatedRange
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Integer
operator|.
name|valueOf
argument_list|(
literal|2
argument_list|)
argument_list|,
name|result
operator|.
name|getUpdatedRows
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Integer
operator|.
name|valueOf
argument_list|(
literal|4
argument_list|)
argument_list|,
name|result
operator|.
name|getUpdatedCells
argument_list|()
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"update: "
operator|+
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testAppend ()
specifier|public
name|void
name|testAppend
parameter_list|()
throws|throws
name|Exception
block|{
name|Spreadsheet
name|testSheet
init|=
name|getSpreadsheet
argument_list|()
decl_stmt|;
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
literal|"CamelGoogleSheets.spreadsheetId"
argument_list|,
name|testSheet
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
literal|"CamelGoogleSheets.range"
argument_list|,
name|TEST_SHEET
operator|+
literal|"!A10"
argument_list|)
expr_stmt|;
comment|// parameter type is com.google.api.services.sheets.v4.model.ValueRange
name|headers
operator|.
name|put
argument_list|(
literal|"CamelGoogleSheets.values"
argument_list|,
operator|new
name|ValueRange
argument_list|()
operator|.
name|setValues
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
literal|"A10"
argument_list|,
literal|"B10"
argument_list|,
literal|"C10"
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
comment|// parameter type is String
name|headers
operator|.
name|put
argument_list|(
literal|"CamelGoogleSheets.valueInputOption"
argument_list|,
literal|"USER_ENTERED"
argument_list|)
expr_stmt|;
specifier|final
name|AppendValuesResponse
name|result
init|=
name|requestBodyAndHeaders
argument_list|(
literal|"direct://APPEND"
argument_list|,
literal|null
argument_list|,
name|headers
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"append result is null"
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|testSheet
operator|.
name|getSpreadsheetId
argument_list|()
argument_list|,
name|result
operator|.
name|getSpreadsheetId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|TEST_SHEET
operator|+
literal|"!A10:C10"
argument_list|,
name|result
operator|.
name|getUpdates
argument_list|()
operator|.
name|getUpdatedRange
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Integer
operator|.
name|valueOf
argument_list|(
literal|1
argument_list|)
argument_list|,
name|result
operator|.
name|getUpdates
argument_list|()
operator|.
name|getUpdatedRows
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Integer
operator|.
name|valueOf
argument_list|(
literal|3
argument_list|)
argument_list|,
name|result
operator|.
name|getUpdates
argument_list|()
operator|.
name|getUpdatedCells
argument_list|()
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"append: "
operator|+
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testClear ()
specifier|public
name|void
name|testClear
parameter_list|()
throws|throws
name|Exception
block|{
name|Spreadsheet
name|testSheet
init|=
name|getSpreadsheetWithTestData
argument_list|()
decl_stmt|;
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
literal|"CamelGoogleSheets.spreadsheetId"
argument_list|,
name|testSheet
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
literal|"CamelGoogleSheets.range"
argument_list|,
name|TEST_SHEET
operator|+
literal|"!A1:B2"
argument_list|)
expr_stmt|;
comment|// parameter type is com.google.api.services.sheets.v4.model.ClearValuesRequest
name|headers
operator|.
name|put
argument_list|(
literal|"CamelGoogleSheets.clearValuesRequest"
argument_list|,
operator|new
name|ClearValuesRequest
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|ClearValuesResponse
name|result
init|=
name|requestBodyAndHeaders
argument_list|(
literal|"direct://CLEAR"
argument_list|,
literal|null
argument_list|,
name|headers
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"clear result is null"
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|testSheet
operator|.
name|getSpreadsheetId
argument_list|()
argument_list|,
name|result
operator|.
name|getSpreadsheetId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|TEST_SHEET
operator|+
literal|"!A1:B2"
argument_list|,
name|result
operator|.
name|getClearedRange
argument_list|()
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"clear: "
operator|+
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
block|{
comment|// test route for append
name|from
argument_list|(
literal|"direct://APPEND"
argument_list|)
operator|.
name|to
argument_list|(
literal|"google-sheets://"
operator|+
name|PATH_PREFIX
operator|+
literal|"/append"
argument_list|)
expr_stmt|;
comment|// test route for clear
name|from
argument_list|(
literal|"direct://CLEAR"
argument_list|)
operator|.
name|to
argument_list|(
literal|"google-sheets://"
operator|+
name|PATH_PREFIX
operator|+
literal|"/clear"
argument_list|)
expr_stmt|;
comment|// test route for get
name|from
argument_list|(
literal|"direct://GET"
argument_list|)
operator|.
name|to
argument_list|(
literal|"google-sheets://"
operator|+
name|PATH_PREFIX
operator|+
literal|"/get"
argument_list|)
expr_stmt|;
comment|// test route for update
name|from
argument_list|(
literal|"direct://UPDATE"
argument_list|)
operator|.
name|to
argument_list|(
literal|"google-sheets://"
operator|+
name|PATH_PREFIX
operator|+
literal|"/update"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

