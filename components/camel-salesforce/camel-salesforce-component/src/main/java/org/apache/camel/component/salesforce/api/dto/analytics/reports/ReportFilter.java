begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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

begin_comment
comment|/**  * Report filter details DTO.  */
end_comment

begin_class
DECL|class|ReportFilter
specifier|public
class|class
name|ReportFilter
extends|extends
name|AbstractDTOBase
block|{
DECL|field|value
specifier|private
name|String
name|value
decl_stmt|;
DECL|field|column
specifier|private
name|String
name|column
decl_stmt|;
DECL|field|operator
specifier|private
name|String
name|operator
decl_stmt|;
DECL|method|getValue ()
specifier|public
name|String
name|getValue
parameter_list|()
block|{
return|return
name|value
return|;
block|}
DECL|method|setValue (String value)
specifier|public
name|void
name|setValue
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
DECL|method|getColumn ()
specifier|public
name|String
name|getColumn
parameter_list|()
block|{
return|return
name|column
return|;
block|}
DECL|method|setColumn (String column)
specifier|public
name|void
name|setColumn
parameter_list|(
name|String
name|column
parameter_list|)
block|{
name|this
operator|.
name|column
operator|=
name|column
expr_stmt|;
block|}
DECL|method|getOperator ()
specifier|public
name|String
name|getOperator
parameter_list|()
block|{
return|return
name|operator
return|;
block|}
DECL|method|setOperator (String operator)
specifier|public
name|void
name|setOperator
parameter_list|(
name|String
name|operator
parameter_list|)
block|{
name|this
operator|.
name|operator
operator|=
name|operator
expr_stmt|;
block|}
block|}
end_class

end_unit

