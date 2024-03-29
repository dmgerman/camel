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
comment|/**  * Report results details column info.  */
end_comment

begin_class
DECL|class|DetailColumnInfo
specifier|public
class|class
name|DetailColumnInfo
extends|extends
name|AbstractDTOBase
block|{
DECL|field|label
specifier|private
name|String
name|label
decl_stmt|;
DECL|field|dataType
specifier|private
name|String
name|dataType
decl_stmt|;
DECL|method|getLabel ()
specifier|public
name|String
name|getLabel
parameter_list|()
block|{
return|return
name|label
return|;
block|}
DECL|method|setLabel (String label)
specifier|public
name|void
name|setLabel
parameter_list|(
name|String
name|label
parameter_list|)
block|{
name|this
operator|.
name|label
operator|=
name|label
expr_stmt|;
block|}
DECL|method|getDataType ()
specifier|public
name|String
name|getDataType
parameter_list|()
block|{
return|return
name|dataType
return|;
block|}
DECL|method|setDataType (String dataType)
specifier|public
name|void
name|setDataType
parameter_list|(
name|String
name|dataType
parameter_list|)
block|{
name|this
operator|.
name|dataType
operator|=
name|dataType
expr_stmt|;
block|}
block|}
end_class

end_unit

