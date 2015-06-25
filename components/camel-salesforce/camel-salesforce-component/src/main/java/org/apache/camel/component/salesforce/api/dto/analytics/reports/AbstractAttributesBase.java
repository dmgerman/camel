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
comment|/**  * Base DTO for report attributes.  */
end_comment

begin_class
DECL|class|AbstractAttributesBase
specifier|public
specifier|abstract
class|class
name|AbstractAttributesBase
extends|extends
name|AbstractDTOBase
block|{
DECL|field|type
specifier|private
name|String
name|type
decl_stmt|;
DECL|field|reportName
specifier|private
name|String
name|reportName
decl_stmt|;
DECL|field|reportId
specifier|private
name|String
name|reportId
decl_stmt|;
DECL|method|getType ()
specifier|public
name|String
name|getType
parameter_list|()
block|{
return|return
name|type
return|;
block|}
DECL|method|setType (String type)
specifier|public
name|void
name|setType
parameter_list|(
name|String
name|type
parameter_list|)
block|{
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
block|}
DECL|method|getReportName ()
specifier|public
name|String
name|getReportName
parameter_list|()
block|{
return|return
name|reportName
return|;
block|}
DECL|method|setReportName (String reportName)
specifier|public
name|void
name|setReportName
parameter_list|(
name|String
name|reportName
parameter_list|)
block|{
name|this
operator|.
name|reportName
operator|=
name|reportName
expr_stmt|;
block|}
DECL|method|getReportId ()
specifier|public
name|String
name|getReportId
parameter_list|()
block|{
return|return
name|reportId
return|;
block|}
DECL|method|setReportId (String reportId)
specifier|public
name|void
name|setReportId
parameter_list|(
name|String
name|reportId
parameter_list|)
block|{
name|this
operator|.
name|reportId
operator|=
name|reportId
expr_stmt|;
block|}
block|}
end_class

end_unit

