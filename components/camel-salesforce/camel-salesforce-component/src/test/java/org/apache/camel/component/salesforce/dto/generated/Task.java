begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.salesforce.dto.generated
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
name|dto
operator|.
name|generated
package|;
end_package

begin_import
import|import
name|java
operator|.
name|time
operator|.
name|ZonedDateTime
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
name|JsonProperty
import|;
end_import

begin_import
import|import
name|com
operator|.
name|thoughtworks
operator|.
name|xstream
operator|.
name|annotations
operator|.
name|XStreamAlias
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
name|component
operator|.
name|salesforce
operator|.
name|api
operator|.
name|dto
operator|.
name|AbstractSObjectBase
import|;
end_import

begin_comment
comment|/**  * Salesforce DTO for SObject Task  */
end_comment

begin_comment
comment|//CHECKSTYLE:OFF
end_comment

begin_class
annotation|@
name|XStreamAlias
argument_list|(
literal|"Task"
argument_list|)
DECL|class|Task
specifier|public
class|class
name|Task
extends|extends
name|AbstractSObjectBase
block|{
DECL|method|Task ()
specifier|public
name|Task
parameter_list|()
block|{
name|getAttributes
argument_list|()
operator|.
name|setType
argument_list|(
literal|"Task"
argument_list|)
expr_stmt|;
block|}
DECL|field|ActivityDate
specifier|private
name|ZonedDateTime
name|ActivityDate
decl_stmt|;
DECL|field|Description
specifier|private
name|String
name|Description
decl_stmt|;
annotation|@
name|JsonProperty
argument_list|(
literal|"ActivityDate"
argument_list|)
DECL|method|getActivityDate ()
specifier|public
name|ZonedDateTime
name|getActivityDate
parameter_list|()
block|{
return|return
name|ActivityDate
return|;
block|}
annotation|@
name|JsonProperty
argument_list|(
literal|"Description"
argument_list|)
DECL|method|getDescription ()
specifier|public
name|String
name|getDescription
parameter_list|()
block|{
return|return
name|Description
return|;
block|}
annotation|@
name|JsonProperty
argument_list|(
literal|"ActivityDate"
argument_list|)
DECL|method|setActivityDate (final ZonedDateTime given)
specifier|public
name|void
name|setActivityDate
parameter_list|(
specifier|final
name|ZonedDateTime
name|given
parameter_list|)
block|{
name|ActivityDate
operator|=
name|given
expr_stmt|;
block|}
annotation|@
name|JsonProperty
argument_list|(
literal|"Description"
argument_list|)
DECL|method|setDescription (final String description)
specifier|public
name|void
name|setDescription
parameter_list|(
specifier|final
name|String
name|description
parameter_list|)
block|{
name|Description
operator|=
name|description
expr_stmt|;
block|}
annotation|@
name|XStreamAlias
argument_list|(
literal|"What"
argument_list|)
DECL|field|What
specifier|private
name|AbstractSObjectBase
name|What
decl_stmt|;
annotation|@
name|JsonProperty
argument_list|(
literal|"What"
argument_list|)
DECL|method|getWhat ()
specifier|public
name|AbstractSObjectBase
name|getWhat
parameter_list|()
block|{
return|return
name|this
operator|.
name|What
return|;
block|}
annotation|@
name|JsonProperty
argument_list|(
literal|"What"
argument_list|)
DECL|method|setWhat (AbstractSObjectBase What)
specifier|public
name|void
name|setWhat
parameter_list|(
name|AbstractSObjectBase
name|What
parameter_list|)
block|{
name|this
operator|.
name|What
operator|=
name|What
expr_stmt|;
block|}
block|}
end_class

begin_comment
comment|//CHECKSTYLE:ON
end_comment

end_unit

