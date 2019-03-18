begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.gora
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|gora
package|;
end_package

begin_comment
comment|/**  * Camel-Gora attributes  */
end_comment

begin_enum
DECL|enum|GoraAttribute
specifier|public
enum|enum
name|GoraAttribute
block|{
comment|/**      * Gora KEY attribute      */
DECL|enumConstant|GORA_KEY
name|GORA_KEY
argument_list|(
literal|"goraKey"
argument_list|)
block|,
comment|/**      * Gora operation header name      */
DECL|enumConstant|GORA_OPERATION
name|GORA_OPERATION
argument_list|(
literal|"goraOperation"
argument_list|)
block|,
comment|/**      * Gora Query Start Time attribute      */
DECL|enumConstant|GORA_QUERY_START_TIME
name|GORA_QUERY_START_TIME
argument_list|(
literal|"startTime"
argument_list|)
block|,
comment|/**      * Gora Query End Time attribute      */
DECL|enumConstant|GORA_QUERY_END_TIME
name|GORA_QUERY_END_TIME
argument_list|(
literal|"endTime"
argument_list|)
block|,
comment|/**      * Gora Query Start Key attribute      */
DECL|enumConstant|GORA_QUERY_START_KEY
name|GORA_QUERY_START_KEY
argument_list|(
literal|"startKey"
argument_list|)
block|,
comment|/**      * Gora Query End Key attribute      */
DECL|enumConstant|GORA_QUERY_END_KEY
name|GORA_QUERY_END_KEY
argument_list|(
literal|"endKey"
argument_list|)
block|,
comment|/**      * Gora Query Key Range From attribute      */
DECL|enumConstant|GORA_QUERY_KEY_RANGE_FROM
name|GORA_QUERY_KEY_RANGE_FROM
argument_list|(
literal|"keyRangeFrom"
argument_list|)
block|,
comment|/**      * Gora Query Key Range To attribute      */
DECL|enumConstant|GORA_QUERY_KEY_RANGE_TO
name|GORA_QUERY_KEY_RANGE_TO
argument_list|(
literal|"keyRangeTo"
argument_list|)
block|,
comment|/**      * Gora Query Time Range From attribute      */
DECL|enumConstant|GORA_QUERY_TIME_RANGE_FROM
name|GORA_QUERY_TIME_RANGE_FROM
argument_list|(
literal|"timeRangeFrom"
argument_list|)
block|,
comment|/**      * Gora Query Key Range To attribute      */
DECL|enumConstant|GORA_QUERY_TIME_RANGE_TO
name|GORA_QUERY_TIME_RANGE_TO
argument_list|(
literal|"timeRangeTo"
argument_list|)
block|,
comment|/**      * Gora Query Limit attribute      */
DECL|enumConstant|GORA_QUERY_LIMIT
name|GORA_QUERY_LIMIT
argument_list|(
literal|"limit"
argument_list|)
block|,
comment|/**      * Gora Query Timestamp attribute      */
DECL|enumConstant|GORA_QUERY_TIMESTAMP
name|GORA_QUERY_TIMESTAMP
argument_list|(
literal|"timestamp"
argument_list|)
block|,
comment|/**      * Gora Query Fields attribute      */
DECL|enumConstant|GORA_QUERY_FIELDS
name|GORA_QUERY_FIELDS
argument_list|(
literal|"fields"
argument_list|)
block|;
comment|/**      * Enum value      */
DECL|field|value
specifier|public
specifier|final
name|String
name|value
decl_stmt|;
comment|/**      * Enum constructor      *      * @param str Operation Value      */
DECL|method|GoraAttribute (final String str)
name|GoraAttribute
parameter_list|(
specifier|final
name|String
name|str
parameter_list|)
block|{
name|value
operator|=
name|str
expr_stmt|;
block|}
block|}
end_enum

end_unit

