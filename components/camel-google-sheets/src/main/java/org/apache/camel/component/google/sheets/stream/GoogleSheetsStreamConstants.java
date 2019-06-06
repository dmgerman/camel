begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.google.sheets.stream
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
name|stream
package|;
end_package

begin_comment
comment|/**  * Constants used in Camel Google Sheets Stream  */
end_comment

begin_class
DECL|class|GoogleSheetsStreamConstants
specifier|public
specifier|final
class|class
name|GoogleSheetsStreamConstants
block|{
DECL|field|PROPERTY_PREFIX
specifier|private
specifier|static
specifier|final
name|String
name|PROPERTY_PREFIX
init|=
literal|"CamelGoogleSheets"
decl_stmt|;
DECL|field|SPREADSHEET_ID
specifier|public
specifier|static
specifier|final
name|String
name|SPREADSHEET_ID
init|=
name|PROPERTY_PREFIX
operator|+
literal|"SpreadsheetId"
decl_stmt|;
DECL|field|SPREADSHEET_URL
specifier|public
specifier|static
specifier|final
name|String
name|SPREADSHEET_URL
init|=
name|PROPERTY_PREFIX
operator|+
literal|"SpreadsheetUrl"
decl_stmt|;
DECL|field|MAJOR_DIMENSION
specifier|public
specifier|static
specifier|final
name|String
name|MAJOR_DIMENSION
init|=
name|PROPERTY_PREFIX
operator|+
literal|"MajorDimension"
decl_stmt|;
DECL|field|RANGE
specifier|public
specifier|static
specifier|final
name|String
name|RANGE
init|=
name|PROPERTY_PREFIX
operator|+
literal|"Range"
decl_stmt|;
DECL|field|RANGE_INDEX
specifier|public
specifier|static
specifier|final
name|String
name|RANGE_INDEX
init|=
name|PROPERTY_PREFIX
operator|+
literal|"RangeIndex"
decl_stmt|;
DECL|field|VALUE_INDEX
specifier|public
specifier|static
specifier|final
name|String
name|VALUE_INDEX
init|=
name|PROPERTY_PREFIX
operator|+
literal|"ValueIndex"
decl_stmt|;
comment|/**      * Prevent instantiation.      */
DECL|method|GoogleSheetsStreamConstants ()
specifier|private
name|GoogleSheetsStreamConstants
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

