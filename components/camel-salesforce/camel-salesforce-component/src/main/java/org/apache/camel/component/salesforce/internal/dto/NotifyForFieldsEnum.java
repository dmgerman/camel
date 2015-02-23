begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.salesforce.internal.dto
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
name|internal
operator|.
name|dto
package|;
end_package

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|jackson
operator|.
name|annotate
operator|.
name|JsonCreator
import|;
end_import

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|jackson
operator|.
name|annotate
operator|.
name|JsonValue
import|;
end_import

begin_comment
comment|/**  * Salesforce Enumeration DTO for picklist NotifyForFields  */
end_comment

begin_enum
DECL|enum|NotifyForFieldsEnum
specifier|public
enum|enum
name|NotifyForFieldsEnum
block|{
comment|// All
DECL|enumConstant|ALL
name|ALL
argument_list|(
literal|"All"
argument_list|)
block|,
comment|// Referenced
DECL|enumConstant|REFERENCED
name|REFERENCED
argument_list|(
literal|"Referenced"
argument_list|)
block|,
comment|// Select
DECL|enumConstant|SELECT
name|SELECT
argument_list|(
literal|"Select"
argument_list|)
block|,
comment|// Where
DECL|enumConstant|WHERE
name|WHERE
argument_list|(
literal|"Where"
argument_list|)
block|;
DECL|field|value
specifier|final
name|String
name|value
decl_stmt|;
DECL|method|NotifyForFieldsEnum (String value)
specifier|private
name|NotifyForFieldsEnum
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
name|this
operator|.
name|value
return|;
block|}
annotation|@
name|JsonCreator
DECL|method|fromValue (String value)
specifier|public
specifier|static
name|NotifyForFieldsEnum
name|fromValue
parameter_list|(
name|String
name|value
parameter_list|)
block|{
for|for
control|(
name|NotifyForFieldsEnum
name|e
range|:
name|NotifyForFieldsEnum
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

