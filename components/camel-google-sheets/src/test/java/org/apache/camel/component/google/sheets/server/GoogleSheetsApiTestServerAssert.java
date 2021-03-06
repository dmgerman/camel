begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.google.sheets.server
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
operator|.
name|server
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
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
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
name|Optional
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|CompletableFuture
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ExecutionException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|Executors
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ScheduledFuture
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeoutException
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
name|consol
operator|.
name|citrus
operator|.
name|message
operator|.
name|MessageType
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
name|annotation
operator|.
name|JsonInclude
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
name|JsonParser
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
name|SerializationFeature
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
name|util
operator|.
name|ObjectHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|assertj
operator|.
name|core
operator|.
name|api
operator|.
name|AbstractAssert
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|http
operator|.
name|HttpStatus
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|http
operator|.
name|MediaType
import|;
end_import

begin_class
DECL|class|GoogleSheetsApiTestServerAssert
specifier|public
specifier|final
class|class
name|GoogleSheetsApiTestServerAssert
extends|extends
name|AbstractAssert
argument_list|<
name|GoogleSheetsApiTestServerAssert
argument_list|,
name|GoogleSheetsApiTestServer
argument_list|>
block|{
DECL|field|mapper
specifier|private
name|ObjectMapper
name|mapper
init|=
operator|new
name|ObjectMapper
argument_list|()
operator|.
name|setDefaultPropertyInclusion
argument_list|(
name|JsonInclude
operator|.
name|Value
operator|.
name|construct
argument_list|(
name|JsonInclude
operator|.
name|Include
operator|.
name|NON_EMPTY
argument_list|,
name|JsonInclude
operator|.
name|Include
operator|.
name|NON_EMPTY
argument_list|)
argument_list|)
operator|.
name|disable
argument_list|(
name|DeserializationFeature
operator|.
name|FAIL_ON_UNKNOWN_PROPERTIES
argument_list|)
operator|.
name|enable
argument_list|(
name|DeserializationFeature
operator|.
name|READ_ENUMS_USING_TO_STRING
argument_list|)
operator|.
name|enable
argument_list|(
name|SerializationFeature
operator|.
name|WRITE_ENUMS_USING_TO_STRING
argument_list|)
operator|.
name|disable
argument_list|(
name|JsonParser
operator|.
name|Feature
operator|.
name|AUTO_CLOSE_SOURCE
argument_list|)
decl_stmt|;
DECL|method|GoogleSheetsApiTestServerAssert (GoogleSheetsApiTestServer server)
specifier|private
name|GoogleSheetsApiTestServerAssert
parameter_list|(
name|GoogleSheetsApiTestServer
name|server
parameter_list|)
block|{
name|super
argument_list|(
name|server
argument_list|,
name|GoogleSheetsApiTestServerAssert
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
comment|/**      * A fluent entry point to the assertion class.      *       * @param server the target server to perform assertions to.      * @return      */
DECL|method|assertThatGoogleApi (GoogleSheetsApiTestServer server)
specifier|public
specifier|static
name|GoogleSheetsApiTestServerAssert
name|assertThatGoogleApi
parameter_list|(
name|GoogleSheetsApiTestServer
name|server
parameter_list|)
block|{
return|return
operator|new
name|GoogleSheetsApiTestServerAssert
argument_list|(
name|server
argument_list|)
return|;
block|}
DECL|method|getSpreadsheetRequest (String spreadsheetId)
specifier|public
name|GetSpreadsheetAssert
name|getSpreadsheetRequest
parameter_list|(
name|String
name|spreadsheetId
parameter_list|)
block|{
return|return
operator|new
name|GetSpreadsheetAssert
argument_list|(
name|spreadsheetId
argument_list|)
return|;
block|}
DECL|method|isRunning ()
specifier|public
name|void
name|isRunning
parameter_list|()
block|{
name|isRunning
argument_list|(
literal|5000
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
expr_stmt|;
block|}
DECL|method|isRunning (long timeout, TimeUnit timeUnit)
specifier|public
name|void
name|isRunning
parameter_list|(
name|long
name|timeout
parameter_list|,
name|TimeUnit
name|timeUnit
parameter_list|)
block|{
name|ScheduledFuture
argument_list|<
name|?
argument_list|>
name|schedule
init|=
literal|null
decl_stmt|;
try|try
block|{
name|CompletableFuture
argument_list|<
name|Boolean
argument_list|>
name|runningProbe
init|=
operator|new
name|CompletableFuture
argument_list|<>
argument_list|()
decl_stmt|;
name|schedule
operator|=
name|Executors
operator|.
name|newSingleThreadScheduledExecutor
argument_list|()
operator|.
name|scheduleAtFixedRate
argument_list|(
parameter_list|()
lambda|->
block|{
if|if
condition|(
name|actual
operator|.
name|getHttpServer
argument_list|()
operator|.
name|isRunning
argument_list|()
condition|)
block|{
name|runningProbe
operator|.
name|complete
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|,
literal|0
argument_list|,
name|timeout
operator|/
literal|10
argument_list|,
name|timeUnit
argument_list|)
expr_stmt|;
name|runningProbe
operator|.
name|get
argument_list|(
name|timeout
argument_list|,
name|timeUnit
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
decl||
name|ExecutionException
decl||
name|TimeoutException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
name|e
argument_list|)
throw|;
block|}
finally|finally
block|{
name|Optional
operator|.
name|ofNullable
argument_list|(
name|schedule
argument_list|)
operator|.
name|ifPresent
argument_list|(
name|future
lambda|->
name|future
operator|.
name|cancel
argument_list|(
literal|true
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|class|GetSpreadsheetAssert
specifier|public
class|class
name|GetSpreadsheetAssert
block|{
DECL|method|GetSpreadsheetAssert (String spreadsheetId)
name|GetSpreadsheetAssert
parameter_list|(
name|String
name|spreadsheetId
parameter_list|)
block|{
name|actual
operator|.
name|getRunner
argument_list|()
operator|.
name|createVariable
argument_list|(
literal|"spreadsheetId"
argument_list|,
name|spreadsheetId
argument_list|)
expr_stmt|;
block|}
DECL|method|andReturnSpreadsheet (Spreadsheet spreadsheet)
specifier|public
name|void
name|andReturnSpreadsheet
parameter_list|(
name|Spreadsheet
name|spreadsheet
parameter_list|)
throws|throws
name|IOException
block|{
name|String
name|spreadsheetJson
init|=
name|spreadsheet
operator|.
name|toPrettyString
argument_list|()
decl_stmt|;
name|actual
operator|.
name|getRunner
argument_list|()
operator|.
name|async
argument_list|()
operator|.
name|actions
argument_list|(
name|actual
operator|.
name|getRunner
argument_list|()
operator|.
name|http
argument_list|(
name|action
lambda|->
name|action
operator|.
name|server
argument_list|(
name|actual
operator|.
name|getHttpServer
argument_list|()
argument_list|)
operator|.
name|receive
argument_list|()
operator|.
name|get
argument_list|(
literal|"/v4/spreadsheets/${spreadsheetId}"
argument_list|)
argument_list|)
argument_list|,
name|actual
operator|.
name|getRunner
argument_list|()
operator|.
name|http
argument_list|(
name|action
lambda|->
name|action
operator|.
name|server
argument_list|(
name|actual
operator|.
name|getHttpServer
argument_list|()
argument_list|)
operator|.
name|send
argument_list|()
operator|.
name|response
argument_list|(
name|HttpStatus
operator|.
name|OK
argument_list|)
operator|.
name|contentType
argument_list|(
name|MediaType
operator|.
name|APPLICATION_JSON_VALUE
argument_list|)
operator|.
name|payload
argument_list|(
name|spreadsheetJson
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|clearValuesRequest (String spreadsheetId, String range)
specifier|public
name|ClearValuesAssert
name|clearValuesRequest
parameter_list|(
name|String
name|spreadsheetId
parameter_list|,
name|String
name|range
parameter_list|)
block|{
return|return
operator|new
name|ClearValuesAssert
argument_list|(
name|spreadsheetId
argument_list|,
name|range
argument_list|)
return|;
block|}
DECL|class|ClearValuesAssert
specifier|public
class|class
name|ClearValuesAssert
block|{
DECL|method|ClearValuesAssert (String spreadsheetId, String range)
name|ClearValuesAssert
parameter_list|(
name|String
name|spreadsheetId
parameter_list|,
name|String
name|range
parameter_list|)
block|{
name|actual
operator|.
name|getRunner
argument_list|()
operator|.
name|createVariable
argument_list|(
literal|"spreadsheetId"
argument_list|,
name|spreadsheetId
argument_list|)
expr_stmt|;
name|actual
operator|.
name|getRunner
argument_list|()
operator|.
name|createVariable
argument_list|(
literal|"range"
argument_list|,
name|range
argument_list|)
expr_stmt|;
block|}
DECL|method|andReturnClearResponse (String clearedRange)
specifier|public
name|void
name|andReturnClearResponse
parameter_list|(
name|String
name|clearedRange
parameter_list|)
throws|throws
name|IOException
block|{
name|actual
operator|.
name|getRunner
argument_list|()
operator|.
name|async
argument_list|()
operator|.
name|actions
argument_list|(
name|actual
operator|.
name|getRunner
argument_list|()
operator|.
name|http
argument_list|(
name|action
lambda|->
name|action
operator|.
name|server
argument_list|(
name|actual
operator|.
name|getHttpServer
argument_list|()
argument_list|)
operator|.
name|receive
argument_list|()
operator|.
name|post
argument_list|(
literal|"/v4/spreadsheets/${spreadsheetId}/values/${range}:clear"
argument_list|)
argument_list|)
argument_list|,
name|actual
operator|.
name|getRunner
argument_list|()
operator|.
name|http
argument_list|(
name|action
lambda|->
name|action
operator|.
name|server
argument_list|(
name|actual
operator|.
name|getHttpServer
argument_list|()
argument_list|)
operator|.
name|send
argument_list|()
operator|.
name|response
argument_list|(
name|HttpStatus
operator|.
name|OK
argument_list|)
operator|.
name|contentType
argument_list|(
name|MediaType
operator|.
name|APPLICATION_JSON_VALUE
argument_list|)
operator|.
name|payload
argument_list|(
literal|"{"
operator|+
literal|"\"spreadsheetId\": \"${spreadsheetId}\","
operator|+
literal|"\"clearedRange\": \""
operator|+
name|clearedRange
operator|+
literal|"\""
operator|+
literal|"}"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|updateValuesRequest (String spreadsheetId, String range, List<List<Object>> data)
specifier|public
name|UpdateValuesAssert
name|updateValuesRequest
parameter_list|(
name|String
name|spreadsheetId
parameter_list|,
name|String
name|range
parameter_list|,
name|List
argument_list|<
name|List
argument_list|<
name|Object
argument_list|>
argument_list|>
name|data
parameter_list|)
block|{
return|return
operator|new
name|UpdateValuesAssert
argument_list|(
name|spreadsheetId
argument_list|,
name|range
argument_list|,
name|data
argument_list|)
return|;
block|}
DECL|class|UpdateValuesAssert
specifier|public
class|class
name|UpdateValuesAssert
block|{
DECL|field|data
specifier|private
specifier|final
name|List
argument_list|<
name|List
argument_list|<
name|Object
argument_list|>
argument_list|>
name|data
decl_stmt|;
DECL|method|UpdateValuesAssert (String spreadsheetId, String range, List<List<Object>> data)
name|UpdateValuesAssert
parameter_list|(
name|String
name|spreadsheetId
parameter_list|,
name|String
name|range
parameter_list|,
name|List
argument_list|<
name|List
argument_list|<
name|Object
argument_list|>
argument_list|>
name|data
parameter_list|)
block|{
name|actual
operator|.
name|getRunner
argument_list|()
operator|.
name|createVariable
argument_list|(
literal|"spreadsheetId"
argument_list|,
name|spreadsheetId
argument_list|)
expr_stmt|;
name|actual
operator|.
name|getRunner
argument_list|()
operator|.
name|createVariable
argument_list|(
literal|"range"
argument_list|,
name|range
argument_list|)
expr_stmt|;
name|this
operator|.
name|data
operator|=
name|data
expr_stmt|;
block|}
DECL|method|andReturnUpdateResponse ()
specifier|public
name|void
name|andReturnUpdateResponse
parameter_list|()
throws|throws
name|IOException
block|{
name|String
name|valuesJson
init|=
name|mapper
operator|.
name|writer
argument_list|()
operator|.
name|writeValueAsString
argument_list|(
name|data
argument_list|)
decl_stmt|;
name|actual
operator|.
name|getRunner
argument_list|()
operator|.
name|async
argument_list|()
operator|.
name|actions
argument_list|(
name|actual
operator|.
name|getRunner
argument_list|()
operator|.
name|http
argument_list|(
name|action
lambda|->
name|action
operator|.
name|server
argument_list|(
name|actual
operator|.
name|getHttpServer
argument_list|()
argument_list|)
operator|.
name|receive
argument_list|()
operator|.
name|put
argument_list|(
literal|"/v4/spreadsheets/${spreadsheetId}/values/${range}"
argument_list|)
operator|.
name|validate
argument_list|(
literal|"$.values.toString()"
argument_list|,
name|valuesJson
argument_list|)
argument_list|)
argument_list|,
name|actual
operator|.
name|getRunner
argument_list|()
operator|.
name|http
argument_list|(
name|action
lambda|->
name|action
operator|.
name|server
argument_list|(
name|actual
operator|.
name|getHttpServer
argument_list|()
argument_list|)
operator|.
name|send
argument_list|()
operator|.
name|response
argument_list|(
name|HttpStatus
operator|.
name|OK
argument_list|)
operator|.
name|contentType
argument_list|(
name|MediaType
operator|.
name|APPLICATION_JSON_VALUE
argument_list|)
operator|.
name|payload
argument_list|(
literal|"{"
operator|+
literal|"\"spreadsheetId\": \"${spreadsheetId}\","
operator|+
literal|"\"updatedRange\": \"${range}\","
operator|+
literal|"\"updatedRows\": "
operator|+
name|data
operator|.
name|size
argument_list|()
operator|+
literal|","
operator|+
literal|"\"updatedColumns\": "
operator|+
name|Optional
operator|.
name|ofNullable
argument_list|(
name|data
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
operator|.
name|map
argument_list|(
name|Collection
operator|::
name|size
argument_list|)
operator|.
name|orElse
argument_list|(
literal|0
argument_list|)
operator|+
literal|","
operator|+
literal|"\"updatedCells\": "
operator|+
name|data
operator|.
name|size
argument_list|()
operator|*
name|Optional
operator|.
name|ofNullable
argument_list|(
name|data
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
operator|.
name|map
argument_list|(
name|Collection
operator|::
name|size
argument_list|)
operator|.
name|orElse
argument_list|(
literal|0
argument_list|)
operator|+
literal|"}"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|appendValuesRequest (String spreadsheetId, String range, List<List<Object>> data)
specifier|public
name|AppendValuesAssert
name|appendValuesRequest
parameter_list|(
name|String
name|spreadsheetId
parameter_list|,
name|String
name|range
parameter_list|,
name|List
argument_list|<
name|List
argument_list|<
name|Object
argument_list|>
argument_list|>
name|data
parameter_list|)
block|{
return|return
operator|new
name|AppendValuesAssert
argument_list|(
name|spreadsheetId
argument_list|,
name|range
argument_list|,
name|data
argument_list|)
return|;
block|}
DECL|class|AppendValuesAssert
specifier|public
class|class
name|AppendValuesAssert
block|{
DECL|field|data
specifier|private
specifier|final
name|List
argument_list|<
name|List
argument_list|<
name|Object
argument_list|>
argument_list|>
name|data
decl_stmt|;
DECL|method|AppendValuesAssert (String spreadsheetId, String range, List<List<Object>> data)
name|AppendValuesAssert
parameter_list|(
name|String
name|spreadsheetId
parameter_list|,
name|String
name|range
parameter_list|,
name|List
argument_list|<
name|List
argument_list|<
name|Object
argument_list|>
argument_list|>
name|data
parameter_list|)
block|{
name|actual
operator|.
name|getRunner
argument_list|()
operator|.
name|createVariable
argument_list|(
literal|"spreadsheetId"
argument_list|,
name|spreadsheetId
argument_list|)
expr_stmt|;
name|actual
operator|.
name|getRunner
argument_list|()
operator|.
name|createVariable
argument_list|(
literal|"range"
argument_list|,
name|range
argument_list|)
expr_stmt|;
name|this
operator|.
name|data
operator|=
name|data
expr_stmt|;
block|}
DECL|method|andReturnAppendResponse (String updatedRange)
specifier|public
name|void
name|andReturnAppendResponse
parameter_list|(
name|String
name|updatedRange
parameter_list|)
throws|throws
name|IOException
block|{
name|String
name|valuesJson
init|=
name|mapper
operator|.
name|writer
argument_list|()
operator|.
name|writeValueAsString
argument_list|(
name|data
argument_list|)
decl_stmt|;
name|actual
operator|.
name|getRunner
argument_list|()
operator|.
name|async
argument_list|()
operator|.
name|actions
argument_list|(
name|actual
operator|.
name|getRunner
argument_list|()
operator|.
name|http
argument_list|(
name|action
lambda|->
name|action
operator|.
name|server
argument_list|(
name|actual
operator|.
name|getHttpServer
argument_list|()
argument_list|)
operator|.
name|receive
argument_list|()
operator|.
name|post
argument_list|(
literal|"/v4/spreadsheets/${spreadsheetId}/values/${range}:append"
argument_list|)
operator|.
name|validate
argument_list|(
literal|"$.values.toString()"
argument_list|,
name|valuesJson
argument_list|)
argument_list|)
argument_list|,
name|actual
operator|.
name|getRunner
argument_list|()
operator|.
name|http
argument_list|(
name|action
lambda|->
name|action
operator|.
name|server
argument_list|(
name|actual
operator|.
name|getHttpServer
argument_list|()
argument_list|)
operator|.
name|send
argument_list|()
operator|.
name|response
argument_list|(
name|HttpStatus
operator|.
name|OK
argument_list|)
operator|.
name|contentType
argument_list|(
name|MediaType
operator|.
name|APPLICATION_JSON_VALUE
argument_list|)
operator|.
name|payload
argument_list|(
literal|"{"
operator|+
literal|"\"spreadsheetId\": \"${spreadsheetId}\","
operator|+
literal|"\"updates\":"
operator|+
literal|"{"
operator|+
literal|"\"spreadsheetId\": \"${spreadsheetId}\","
operator|+
literal|"\"updatedRange\": \""
operator|+
name|updatedRange
operator|+
literal|"\","
operator|+
literal|"\"updatedRows\": "
operator|+
name|data
operator|.
name|size
argument_list|()
operator|+
literal|","
operator|+
literal|"\"updatedColumns\": "
operator|+
name|Optional
operator|.
name|ofNullable
argument_list|(
name|data
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
operator|.
name|map
argument_list|(
name|Collection
operator|::
name|size
argument_list|)
operator|.
name|orElse
argument_list|(
literal|0
argument_list|)
operator|+
literal|","
operator|+
literal|"\"updatedCells\": "
operator|+
name|data
operator|.
name|size
argument_list|()
operator|*
name|Optional
operator|.
name|ofNullable
argument_list|(
name|data
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
operator|.
name|map
argument_list|(
name|Collection
operator|::
name|size
argument_list|)
operator|.
name|orElse
argument_list|(
literal|0
argument_list|)
operator|+
literal|"}"
operator|+
literal|"}"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|getValuesRequest (String spreadsheetId, String range)
specifier|public
name|GetValuesAssert
name|getValuesRequest
parameter_list|(
name|String
name|spreadsheetId
parameter_list|,
name|String
name|range
parameter_list|)
block|{
return|return
operator|new
name|GetValuesAssert
argument_list|(
name|spreadsheetId
argument_list|,
name|range
argument_list|)
return|;
block|}
DECL|class|GetValuesAssert
specifier|public
class|class
name|GetValuesAssert
block|{
DECL|method|GetValuesAssert (String spreadsheetId, String range)
name|GetValuesAssert
parameter_list|(
name|String
name|spreadsheetId
parameter_list|,
name|String
name|range
parameter_list|)
block|{
name|actual
operator|.
name|getRunner
argument_list|()
operator|.
name|createVariable
argument_list|(
literal|"spreadsheetId"
argument_list|,
name|spreadsheetId
argument_list|)
expr_stmt|;
name|actual
operator|.
name|getRunner
argument_list|()
operator|.
name|createVariable
argument_list|(
literal|"range"
argument_list|,
name|range
argument_list|)
expr_stmt|;
block|}
DECL|method|andReturnValueRange (ValueRange valueRange)
specifier|public
name|void
name|andReturnValueRange
parameter_list|(
name|ValueRange
name|valueRange
parameter_list|)
throws|throws
name|IOException
block|{
name|String
name|valueJson
init|=
name|valueRange
operator|.
name|toPrettyString
argument_list|()
decl_stmt|;
name|actual
operator|.
name|getRunner
argument_list|()
operator|.
name|async
argument_list|()
operator|.
name|actions
argument_list|(
name|actual
operator|.
name|getRunner
argument_list|()
operator|.
name|http
argument_list|(
name|action
lambda|->
name|action
operator|.
name|server
argument_list|(
name|actual
operator|.
name|getHttpServer
argument_list|()
argument_list|)
operator|.
name|receive
argument_list|()
operator|.
name|get
argument_list|(
literal|"/v4/spreadsheets/${spreadsheetId}/values/${range}"
argument_list|)
argument_list|)
argument_list|,
name|actual
operator|.
name|getRunner
argument_list|()
operator|.
name|http
argument_list|(
name|action
lambda|->
name|action
operator|.
name|server
argument_list|(
name|actual
operator|.
name|getHttpServer
argument_list|()
argument_list|)
operator|.
name|send
argument_list|()
operator|.
name|response
argument_list|(
name|HttpStatus
operator|.
name|OK
argument_list|)
operator|.
name|contentType
argument_list|(
name|MediaType
operator|.
name|APPLICATION_JSON_VALUE
argument_list|)
operator|.
name|payload
argument_list|(
name|valueJson
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|andReturnValues (List<List<Object>> data)
specifier|public
name|void
name|andReturnValues
parameter_list|(
name|List
argument_list|<
name|List
argument_list|<
name|Object
argument_list|>
argument_list|>
name|data
parameter_list|)
throws|throws
name|JsonProcessingException
block|{
name|String
name|valueRangeJson
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|data
argument_list|)
condition|)
block|{
name|valueRangeJson
operator|=
literal|"{"
operator|+
literal|"\"range\": \"${range}\","
operator|+
literal|"\"majorDimension\": \"ROWS\""
operator|+
literal|"}"
expr_stmt|;
block|}
else|else
block|{
name|valueRangeJson
operator|=
literal|"{"
operator|+
literal|"\"range\": \"${range}\","
operator|+
literal|"\"majorDimension\": \"ROWS\","
operator|+
literal|"\"values\":"
operator|+
name|mapper
operator|.
name|writer
argument_list|()
operator|.
name|writeValueAsString
argument_list|(
name|data
argument_list|)
operator|+
literal|"}"
expr_stmt|;
block|}
name|actual
operator|.
name|getRunner
argument_list|()
operator|.
name|async
argument_list|()
operator|.
name|actions
argument_list|(
name|actual
operator|.
name|getRunner
argument_list|()
operator|.
name|http
argument_list|(
name|action
lambda|->
name|action
operator|.
name|server
argument_list|(
name|actual
operator|.
name|getHttpServer
argument_list|()
argument_list|)
operator|.
name|receive
argument_list|()
operator|.
name|get
argument_list|(
literal|"/v4/spreadsheets/${spreadsheetId}/values/${range}"
argument_list|)
argument_list|)
argument_list|,
name|actual
operator|.
name|getRunner
argument_list|()
operator|.
name|http
argument_list|(
name|action
lambda|->
name|action
operator|.
name|server
argument_list|(
name|actual
operator|.
name|getHttpServer
argument_list|()
argument_list|)
operator|.
name|send
argument_list|()
operator|.
name|response
argument_list|(
name|HttpStatus
operator|.
name|OK
argument_list|)
operator|.
name|contentType
argument_list|(
name|MediaType
operator|.
name|APPLICATION_JSON_VALUE
argument_list|)
operator|.
name|payload
argument_list|(
name|valueRangeJson
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|batchGetValuesRequest (String spreadsheetId, String range)
specifier|public
name|BatchGetValuesAssert
name|batchGetValuesRequest
parameter_list|(
name|String
name|spreadsheetId
parameter_list|,
name|String
name|range
parameter_list|)
block|{
return|return
operator|new
name|BatchGetValuesAssert
argument_list|(
name|spreadsheetId
argument_list|,
name|range
argument_list|)
return|;
block|}
DECL|class|BatchGetValuesAssert
specifier|public
class|class
name|BatchGetValuesAssert
block|{
DECL|method|BatchGetValuesAssert (String spreadsheetId, String range)
name|BatchGetValuesAssert
parameter_list|(
name|String
name|spreadsheetId
parameter_list|,
name|String
name|range
parameter_list|)
block|{
name|actual
operator|.
name|getRunner
argument_list|()
operator|.
name|createVariable
argument_list|(
literal|"spreadsheetId"
argument_list|,
name|spreadsheetId
argument_list|)
expr_stmt|;
name|actual
operator|.
name|getRunner
argument_list|()
operator|.
name|createVariable
argument_list|(
literal|"range"
argument_list|,
name|range
argument_list|)
expr_stmt|;
block|}
DECL|method|andReturnValues (List<List<Object>> data)
specifier|public
name|void
name|andReturnValues
parameter_list|(
name|List
argument_list|<
name|List
argument_list|<
name|Object
argument_list|>
argument_list|>
name|data
parameter_list|)
throws|throws
name|JsonProcessingException
block|{
name|String
name|valueRangeJson
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|data
argument_list|)
condition|)
block|{
name|valueRangeJson
operator|=
literal|"{\"spreadsheetId\": \"${spreadsheetId}\","
operator|+
literal|"\"valueRanges\": ["
operator|+
literal|"{"
operator|+
literal|"\"range\": \"${range}\","
operator|+
literal|"\"majorDimension\": \"ROWS\""
operator|+
literal|"}"
operator|+
literal|"]}"
expr_stmt|;
block|}
else|else
block|{
name|valueRangeJson
operator|=
literal|"{\"spreadsheetId\": \"${spreadsheetId}\","
operator|+
literal|"\"valueRanges\": ["
operator|+
literal|"{"
operator|+
literal|"\"range\": \"${range}\","
operator|+
literal|"\"majorDimension\": \"ROWS\","
operator|+
literal|"\"values\":"
operator|+
name|mapper
operator|.
name|writer
argument_list|()
operator|.
name|writeValueAsString
argument_list|(
name|data
argument_list|)
operator|+
literal|"}"
operator|+
literal|"]}"
expr_stmt|;
block|}
name|actual
operator|.
name|getRunner
argument_list|()
operator|.
name|async
argument_list|()
operator|.
name|actions
argument_list|(
name|actual
operator|.
name|getRunner
argument_list|()
operator|.
name|http
argument_list|(
name|action
lambda|->
name|action
operator|.
name|server
argument_list|(
name|actual
operator|.
name|getHttpServer
argument_list|()
argument_list|)
operator|.
name|receive
argument_list|()
operator|.
name|get
argument_list|(
literal|"/v4/spreadsheets/${spreadsheetId}/values:batchGet"
argument_list|)
argument_list|)
argument_list|,
name|actual
operator|.
name|getRunner
argument_list|()
operator|.
name|http
argument_list|(
name|action
lambda|->
name|action
operator|.
name|server
argument_list|(
name|actual
operator|.
name|getHttpServer
argument_list|()
argument_list|)
operator|.
name|send
argument_list|()
operator|.
name|response
argument_list|(
name|HttpStatus
operator|.
name|OK
argument_list|)
operator|.
name|contentType
argument_list|(
name|MediaType
operator|.
name|APPLICATION_JSON_VALUE
argument_list|)
operator|.
name|payload
argument_list|(
name|valueRangeJson
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|createSpreadsheetRequest ()
specifier|public
name|CreateSpreadsheetAssert
name|createSpreadsheetRequest
parameter_list|()
block|{
return|return
operator|new
name|CreateSpreadsheetAssert
argument_list|()
return|;
block|}
DECL|class|CreateSpreadsheetAssert
specifier|public
class|class
name|CreateSpreadsheetAssert
block|{
DECL|field|title
specifier|private
name|String
name|title
init|=
literal|"@ignore@"
decl_stmt|;
DECL|field|sheetTitle
specifier|private
name|String
name|sheetTitle
decl_stmt|;
DECL|method|hasTitle (String title)
specifier|public
name|CreateSpreadsheetAssert
name|hasTitle
parameter_list|(
name|String
name|title
parameter_list|)
block|{
name|this
operator|.
name|title
operator|=
name|title
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|hasSheetTitle (String sheetTitle)
specifier|public
name|CreateSpreadsheetAssert
name|hasSheetTitle
parameter_list|(
name|String
name|sheetTitle
parameter_list|)
block|{
name|this
operator|.
name|sheetTitle
operator|=
name|sheetTitle
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|andReturnRandomSpreadsheet ()
specifier|public
name|void
name|andReturnRandomSpreadsheet
parameter_list|()
block|{
name|andReturnSpreadsheet
argument_list|(
literal|"citrus:randomString(44)"
argument_list|)
expr_stmt|;
block|}
DECL|method|andReturnSpreadsheet (String spreadsheetId)
specifier|public
name|void
name|andReturnSpreadsheet
parameter_list|(
name|String
name|spreadsheetId
parameter_list|)
block|{
name|actual
operator|.
name|getRunner
argument_list|()
operator|.
name|createVariable
argument_list|(
literal|"spreadsheetId"
argument_list|,
name|spreadsheetId
argument_list|)
expr_stmt|;
name|actual
operator|.
name|getRunner
argument_list|()
operator|.
name|createVariable
argument_list|(
literal|"title"
argument_list|,
name|title
argument_list|)
expr_stmt|;
name|String
name|spreadsheetJson
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|sheetTitle
argument_list|)
condition|)
block|{
name|actual
operator|.
name|getRunner
argument_list|()
operator|.
name|createVariable
argument_list|(
literal|"sheetTitle"
argument_list|,
name|sheetTitle
argument_list|)
expr_stmt|;
name|spreadsheetJson
operator|=
literal|"{\"properties\":{\"title\":\"${title}\"},\"sheets\":[{\"properties\":{\"title\":\"${sheetTitle}\"}}]}"
expr_stmt|;
block|}
else|else
block|{
name|spreadsheetJson
operator|=
literal|"{\"properties\":{\"title\":\"${title}\"}}"
expr_stmt|;
block|}
name|actual
operator|.
name|getRunner
argument_list|()
operator|.
name|async
argument_list|()
operator|.
name|actions
argument_list|(
name|actual
operator|.
name|getRunner
argument_list|()
operator|.
name|http
argument_list|(
name|action
lambda|->
name|action
operator|.
name|server
argument_list|(
name|actual
operator|.
name|getHttpServer
argument_list|()
argument_list|)
operator|.
name|receive
argument_list|()
operator|.
name|post
argument_list|(
literal|"/v4/spreadsheets"
argument_list|)
operator|.
name|name
argument_list|(
literal|"create.request"
argument_list|)
operator|.
name|messageType
argument_list|(
name|MessageType
operator|.
name|JSON
argument_list|)
operator|.
name|payload
argument_list|(
name|spreadsheetJson
argument_list|)
argument_list|)
argument_list|,
name|actual
operator|.
name|getRunner
argument_list|()
operator|.
name|http
argument_list|(
name|action
lambda|->
name|action
operator|.
name|server
argument_list|(
name|actual
operator|.
name|getHttpServer
argument_list|()
argument_list|)
operator|.
name|send
argument_list|()
operator|.
name|response
argument_list|(
name|HttpStatus
operator|.
name|OK
argument_list|)
operator|.
name|contentType
argument_list|(
name|MediaType
operator|.
name|APPLICATION_JSON_VALUE
argument_list|)
operator|.
name|payload
argument_list|(
literal|"{\"spreadsheetId\":\"${spreadsheetId}\",\"properties\":{\"title\":\"citrus:jsonPath(citrus:message(create.request.payload()), '$.properties.title')\"}}"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|batchUpdateSpreadsheetRequest (String spreadsheetId)
specifier|public
name|BatchUpdateSpreadsheetAssert
name|batchUpdateSpreadsheetRequest
parameter_list|(
name|String
name|spreadsheetId
parameter_list|)
block|{
return|return
operator|new
name|BatchUpdateSpreadsheetAssert
argument_list|(
name|spreadsheetId
argument_list|)
return|;
block|}
DECL|class|BatchUpdateSpreadsheetAssert
specifier|public
class|class
name|BatchUpdateSpreadsheetAssert
block|{
DECL|field|fields
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|fields
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
DECL|method|BatchUpdateSpreadsheetAssert (String spreadsheetId)
name|BatchUpdateSpreadsheetAssert
parameter_list|(
name|String
name|spreadsheetId
parameter_list|)
block|{
name|actual
operator|.
name|getRunner
argument_list|()
operator|.
name|createVariable
argument_list|(
literal|"spreadsheetId"
argument_list|,
name|spreadsheetId
argument_list|)
expr_stmt|;
block|}
DECL|method|updateTitle (String title)
specifier|public
name|BatchUpdateSpreadsheetAssert
name|updateTitle
parameter_list|(
name|String
name|title
parameter_list|)
block|{
name|actual
operator|.
name|getRunner
argument_list|()
operator|.
name|createVariable
argument_list|(
literal|"title"
argument_list|,
name|title
argument_list|)
expr_stmt|;
name|fields
operator|.
name|add
argument_list|(
literal|"title"
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|andReturnUpdated ()
specifier|public
name|void
name|andReturnUpdated
parameter_list|()
block|{
name|actual
operator|.
name|getRunner
argument_list|()
operator|.
name|async
argument_list|()
operator|.
name|actions
argument_list|(
name|actual
operator|.
name|getRunner
argument_list|()
operator|.
name|http
argument_list|(
name|action
lambda|->
name|action
operator|.
name|server
argument_list|(
name|actual
operator|.
name|getHttpServer
argument_list|()
argument_list|)
operator|.
name|receive
argument_list|()
operator|.
name|post
argument_list|(
literal|"/v4/spreadsheets/${spreadsheetId}:batchUpdate"
argument_list|)
operator|.
name|messageType
argument_list|(
name|MessageType
operator|.
name|JSON
argument_list|)
operator|.
name|payload
argument_list|(
literal|"{"
operator|+
literal|"\"includeSpreadsheetInResponse\":true,"
operator|+
literal|"\"requests\":["
operator|+
literal|"{"
operator|+
literal|"\"updateSpreadsheetProperties\": {"
operator|+
literal|"\"fields\":\""
operator|+
name|String
operator|.
name|join
argument_list|(
literal|","
argument_list|,
name|fields
argument_list|)
operator|+
literal|"\","
operator|+
literal|"\"properties\":{"
operator|+
name|fields
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|field
lambda|->
name|String
operator|.
name|format
argument_list|(
literal|"\"%s\":\"${%s}\""
argument_list|,
name|field
argument_list|,
name|field
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
operator|+
literal|"}"
operator|+
literal|"}"
operator|+
literal|"}"
operator|+
literal|"]}"
argument_list|)
argument_list|)
argument_list|,
name|actual
operator|.
name|getRunner
argument_list|()
operator|.
name|http
argument_list|(
name|action
lambda|->
name|action
operator|.
name|server
argument_list|(
name|actual
operator|.
name|getHttpServer
argument_list|()
argument_list|)
operator|.
name|send
argument_list|()
operator|.
name|response
argument_list|(
name|HttpStatus
operator|.
name|OK
argument_list|)
operator|.
name|contentType
argument_list|(
name|MediaType
operator|.
name|APPLICATION_JSON_VALUE
argument_list|)
operator|.
name|payload
argument_list|(
literal|"{\"spreadsheetId\":\"${spreadsheetId}\",\"updatedSpreadsheet\":{\"properties\":{\"title\":\"${title}\"},\"spreadsheetId\":\"${spreadsheetId}\"}}"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

