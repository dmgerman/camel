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

begin_comment
comment|/**  * Report results aggregate column info.  */
end_comment

begin_class
DECL|class|AggregateColumnInfo
specifier|public
class|class
name|AggregateColumnInfo
extends|extends
name|DetailColumnInfo
block|{
DECL|field|acrossGroupingContext
specifier|private
name|String
name|acrossGroupingContext
decl_stmt|;
DECL|field|downGroupingContext
specifier|private
name|String
name|downGroupingContext
decl_stmt|;
DECL|method|getAcrossGroupingContext ()
specifier|public
name|String
name|getAcrossGroupingContext
parameter_list|()
block|{
return|return
name|acrossGroupingContext
return|;
block|}
DECL|method|setAcrossGroupingContext (String acrossGroupingContext)
specifier|public
name|void
name|setAcrossGroupingContext
parameter_list|(
name|String
name|acrossGroupingContext
parameter_list|)
block|{
name|this
operator|.
name|acrossGroupingContext
operator|=
name|acrossGroupingContext
expr_stmt|;
block|}
DECL|method|getDownGroupingContext ()
specifier|public
name|String
name|getDownGroupingContext
parameter_list|()
block|{
return|return
name|downGroupingContext
return|;
block|}
DECL|method|setDownGroupingContext (String downGroupingContext)
specifier|public
name|void
name|setDownGroupingContext
parameter_list|(
name|String
name|downGroupingContext
parameter_list|)
block|{
name|this
operator|.
name|downGroupingContext
operator|=
name|downGroupingContext
expr_stmt|;
block|}
block|}
end_class

end_unit

