begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
package|package
name|$
block|{
package|package
block|}
end_package

begin_expr_stmt
operator|.
name|incident
expr_stmt|;
end_expr_stmt

begin_comment
comment|/**  * Input report message  */
end_comment

begin_class
DECL|class|InputReportIncident
specifier|public
class|class
name|InputReportIncident
block|{
DECL|field|incidentId
specifier|private
name|String
name|incidentId
decl_stmt|;
DECL|field|incidentDate
specifier|private
name|String
name|incidentDate
decl_stmt|;
DECL|field|givenName
specifier|private
name|String
name|givenName
decl_stmt|;
DECL|field|familyName
specifier|private
name|String
name|familyName
decl_stmt|;
DECL|field|summary
specifier|private
name|String
name|summary
decl_stmt|;
DECL|field|details
specifier|private
name|String
name|details
decl_stmt|;
DECL|field|email
specifier|private
name|String
name|email
decl_stmt|;
DECL|field|phone
specifier|private
name|String
name|phone
decl_stmt|;
DECL|method|getIncidentId ()
specifier|public
name|String
name|getIncidentId
parameter_list|()
block|{
return|return
name|incidentId
return|;
block|}
DECL|method|setIncidentId (String incidentId)
specifier|public
name|void
name|setIncidentId
parameter_list|(
name|String
name|incidentId
parameter_list|)
block|{
name|this
operator|.
name|incidentId
operator|=
name|incidentId
expr_stmt|;
block|}
DECL|method|getIncidentDate ()
specifier|public
name|String
name|getIncidentDate
parameter_list|()
block|{
return|return
name|incidentDate
return|;
block|}
DECL|method|setIncidentDate (String incidentDate)
specifier|public
name|void
name|setIncidentDate
parameter_list|(
name|String
name|incidentDate
parameter_list|)
block|{
name|this
operator|.
name|incidentDate
operator|=
name|incidentDate
expr_stmt|;
block|}
DECL|method|getGivenName ()
specifier|public
name|String
name|getGivenName
parameter_list|()
block|{
return|return
name|givenName
return|;
block|}
DECL|method|setGivenName (String givenName)
specifier|public
name|void
name|setGivenName
parameter_list|(
name|String
name|givenName
parameter_list|)
block|{
name|this
operator|.
name|givenName
operator|=
name|givenName
expr_stmt|;
block|}
DECL|method|getFamilyName ()
specifier|public
name|String
name|getFamilyName
parameter_list|()
block|{
return|return
name|familyName
return|;
block|}
DECL|method|setFamilyName (String familyName)
specifier|public
name|void
name|setFamilyName
parameter_list|(
name|String
name|familyName
parameter_list|)
block|{
name|this
operator|.
name|familyName
operator|=
name|familyName
expr_stmt|;
block|}
DECL|method|getSummary ()
specifier|public
name|String
name|getSummary
parameter_list|()
block|{
return|return
name|summary
return|;
block|}
DECL|method|setSummary (String summary)
specifier|public
name|void
name|setSummary
parameter_list|(
name|String
name|summary
parameter_list|)
block|{
name|this
operator|.
name|summary
operator|=
name|summary
expr_stmt|;
block|}
DECL|method|getDetails ()
specifier|public
name|String
name|getDetails
parameter_list|()
block|{
return|return
name|details
return|;
block|}
DECL|method|setDetails (String details)
specifier|public
name|void
name|setDetails
parameter_list|(
name|String
name|details
parameter_list|)
block|{
name|this
operator|.
name|details
operator|=
name|details
expr_stmt|;
block|}
DECL|method|getEmail ()
specifier|public
name|String
name|getEmail
parameter_list|()
block|{
return|return
name|email
return|;
block|}
DECL|method|setEmail (String email)
specifier|public
name|void
name|setEmail
parameter_list|(
name|String
name|email
parameter_list|)
block|{
name|this
operator|.
name|email
operator|=
name|email
expr_stmt|;
block|}
DECL|method|getPhone ()
specifier|public
name|String
name|getPhone
parameter_list|()
block|{
return|return
name|phone
return|;
block|}
DECL|method|setPhone (String phone)
specifier|public
name|void
name|setPhone
parameter_list|(
name|String
name|phone
parameter_list|)
block|{
name|this
operator|.
name|phone
operator|=
name|phone
expr_stmt|;
block|}
block|}
end_class

end_unit

