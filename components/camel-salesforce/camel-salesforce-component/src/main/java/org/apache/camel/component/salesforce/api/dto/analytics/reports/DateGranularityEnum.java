begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.salesforce.api.dto.analytics.reports
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
name|dto
operator|.
name|analytics
operator|.
name|reports
package|;
end_package

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
name|JsonCreator
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
name|JsonValue
import|;
end_import

begin_comment
comment|/**  * Report results groupings date granularity.  */
end_comment

begin_enum
DECL|enum|DateGranularityEnum
specifier|public
enum|enum
name|DateGranularityEnum
block|{
comment|// None
DECL|enumConstant|NONE
name|NONE
argument_list|(
literal|"None"
argument_list|)
block|,
comment|// Day
DECL|enumConstant|DAY
name|DAY
argument_list|(
literal|"Day"
argument_list|)
block|,
comment|// Calendar Week
DECL|enumConstant|CALENDAR_WEEK
name|CALENDAR_WEEK
argument_list|(
literal|"Calendar Week"
argument_list|)
block|,
comment|// Calendar Month
DECL|enumConstant|CALENDAR_MONTH
name|CALENDAR_MONTH
argument_list|(
literal|"Calendar Month"
argument_list|)
block|,
comment|// Calendar Quarter
DECL|enumConstant|CALENDAR_QUARTER
name|CALENDAR_QUARTER
argument_list|(
literal|"Calendar Quarter"
argument_list|)
block|,
comment|// Calendar Year
DECL|enumConstant|CALENDAR_YEAR
name|CALENDAR_YEAR
argument_list|(
literal|"Calendar Year"
argument_list|)
block|,
comment|// Calendar Month in Year
DECL|enumConstant|CALENDAR_MONTH_IN_YEAR
name|CALENDAR_MONTH_IN_YEAR
argument_list|(
literal|"Calendar Month in Year"
argument_list|)
block|,
comment|// Calendar Day in Month
DECL|enumConstant|CALENDAR_DAY_IN_MONTH
name|CALENDAR_DAY_IN_MONTH
argument_list|(
literal|"Calendar Day in Month"
argument_list|)
block|,
comment|// Fiscal Period
DECL|enumConstant|FISCAL_PERIOD
name|FISCAL_PERIOD
argument_list|(
literal|"Fiscal Period"
argument_list|)
block|,
comment|// Fiscal Week
DECL|enumConstant|FISCAL_WEEK
name|FISCAL_WEEK
argument_list|(
literal|"Fiscal Week"
argument_list|)
block|,
comment|// Fiscal Quarter
DECL|enumConstant|FISCAL_QUARTER
name|FISCAL_QUARTER
argument_list|(
literal|"Fiscal Quarter"
argument_list|)
block|,
comment|// Fiscal Year
DECL|enumConstant|FISCAL_YEAR
name|FISCAL_YEAR
argument_list|(
literal|"Fiscal Year"
argument_list|)
block|;
DECL|field|value
specifier|private
specifier|final
name|String
name|value
decl_stmt|;
DECL|method|DateGranularityEnum (String value)
name|DateGranularityEnum
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|this
operator|.
name|value
operator|=
name|value
expr_stmt|;
block|}
annotation|@
name|JsonValue
DECL|method|value ()
specifier|public
name|String
name|value
parameter_list|()
block|{
return|return
name|value
return|;
block|}
annotation|@
name|JsonCreator
DECL|method|fromValue (String value)
specifier|public
specifier|static
name|DateGranularityEnum
name|fromValue
parameter_list|(
name|String
name|value
parameter_list|)
block|{
for|for
control|(
name|DateGranularityEnum
name|e
range|:
name|DateGranularityEnum
operator|.
name|values
argument_list|()
control|)
block|{
if|if
condition|(
name|e
operator|.
name|value
operator|.
name|equals
argument_list|(
name|value
argument_list|)
condition|)
block|{
return|return
name|e
return|;
block|}
block|}
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|value
argument_list|)
throw|;
block|}
block|}
end_enum

end_unit

