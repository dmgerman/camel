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
comment|/**  * Report results Attributes.  */
end_comment

begin_class
DECL|class|SyncAttributes
specifier|public
class|class
name|SyncAttributes
extends|extends
name|AbstractAttributesBase
block|{
DECL|field|describeUrl
specifier|private
name|String
name|describeUrl
decl_stmt|;
DECL|field|instancesUrl
specifier|private
name|String
name|instancesUrl
decl_stmt|;
DECL|method|getDescribeUrl ()
specifier|public
name|String
name|getDescribeUrl
parameter_list|()
block|{
return|return
name|describeUrl
return|;
block|}
DECL|method|setDescribeUrl (String describeUrl)
specifier|public
name|void
name|setDescribeUrl
parameter_list|(
name|String
name|describeUrl
parameter_list|)
block|{
name|this
operator|.
name|describeUrl
operator|=
name|describeUrl
expr_stmt|;
block|}
DECL|method|getInstancesUrl ()
specifier|public
name|String
name|getInstancesUrl
parameter_list|()
block|{
return|return
name|instancesUrl
return|;
block|}
DECL|method|setInstancesUrl (String instancesUrl)
specifier|public
name|void
name|setInstancesUrl
parameter_list|(
name|String
name|instancesUrl
parameter_list|)
block|{
name|this
operator|.
name|instancesUrl
operator|=
name|instancesUrl
expr_stmt|;
block|}
block|}
end_class

end_unit

