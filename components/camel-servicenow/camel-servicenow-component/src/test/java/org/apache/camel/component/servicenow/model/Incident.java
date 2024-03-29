begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.servicenow.model
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|servicenow
operator|.
name|model
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
name|JsonIgnoreProperties
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
name|JsonInclude
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

begin_class
annotation|@
name|JsonIgnoreProperties
argument_list|(
name|ignoreUnknown
operator|=
literal|true
argument_list|)
annotation|@
name|JsonInclude
argument_list|(
name|JsonInclude
operator|.
name|Include
operator|.
name|NON_NULL
argument_list|)
DECL|class|Incident
specifier|public
class|class
name|Incident
block|{
annotation|@
name|JsonProperty
argument_list|(
literal|"sys_id"
argument_list|)
DECL|field|id
specifier|private
name|String
name|id
decl_stmt|;
annotation|@
name|JsonProperty
argument_list|(
literal|"number"
argument_list|)
DECL|field|number
specifier|private
name|String
name|number
decl_stmt|;
annotation|@
name|JsonProperty
argument_list|(
literal|"description"
argument_list|)
DECL|field|description
specifier|private
name|String
name|description
decl_stmt|;
annotation|@
name|JsonProperty
argument_list|(
literal|"short_description"
argument_list|)
DECL|field|shortDescription
specifier|private
name|String
name|shortDescription
decl_stmt|;
annotation|@
name|JsonProperty
argument_list|(
literal|"severity"
argument_list|)
DECL|field|severity
specifier|private
name|int
name|severity
decl_stmt|;
annotation|@
name|JsonProperty
argument_list|(
literal|"impact"
argument_list|)
DECL|field|impact
specifier|private
name|int
name|impact
decl_stmt|;
DECL|method|Incident ()
specifier|public
name|Incident
parameter_list|()
block|{     }
DECL|method|getId ()
specifier|public
name|String
name|getId
parameter_list|()
block|{
return|return
name|id
return|;
block|}
DECL|method|setId (String id)
specifier|public
name|void
name|setId
parameter_list|(
name|String
name|id
parameter_list|)
block|{
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
block|}
DECL|method|getNumber ()
specifier|public
name|String
name|getNumber
parameter_list|()
block|{
return|return
name|number
return|;
block|}
DECL|method|setNumber (String number)
specifier|public
name|void
name|setNumber
parameter_list|(
name|String
name|number
parameter_list|)
block|{
name|this
operator|.
name|number
operator|=
name|number
expr_stmt|;
block|}
DECL|method|getDescription ()
specifier|public
name|String
name|getDescription
parameter_list|()
block|{
return|return
name|description
return|;
block|}
DECL|method|setDescription (String description)
specifier|public
name|void
name|setDescription
parameter_list|(
name|String
name|description
parameter_list|)
block|{
name|this
operator|.
name|description
operator|=
name|description
expr_stmt|;
block|}
DECL|method|getShortDescription ()
specifier|public
name|String
name|getShortDescription
parameter_list|()
block|{
return|return
name|shortDescription
return|;
block|}
DECL|method|setShortDescription (String shortDescription)
specifier|public
name|void
name|setShortDescription
parameter_list|(
name|String
name|shortDescription
parameter_list|)
block|{
name|this
operator|.
name|shortDescription
operator|=
name|shortDescription
expr_stmt|;
block|}
DECL|method|getSeverity ()
specifier|public
name|int
name|getSeverity
parameter_list|()
block|{
return|return
name|severity
return|;
block|}
DECL|method|setSeverity (int severity)
specifier|public
name|void
name|setSeverity
parameter_list|(
name|int
name|severity
parameter_list|)
block|{
name|this
operator|.
name|severity
operator|=
name|severity
expr_stmt|;
block|}
DECL|method|getImpact ()
specifier|public
name|int
name|getImpact
parameter_list|()
block|{
return|return
name|impact
return|;
block|}
DECL|method|setImpact (int impact)
specifier|public
name|void
name|setImpact
parameter_list|(
name|int
name|impact
parameter_list|)
block|{
name|this
operator|.
name|impact
operator|=
name|impact
expr_stmt|;
block|}
block|}
end_class

end_unit

