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
comment|/**  * Input status message  */
end_comment

begin_class
DECL|class|InputStatusIncident
specifier|public
class|class
name|InputStatusIncident
block|{
DECL|field|incidentId
specifier|private
name|String
name|incidentId
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
block|}
end_class

end_unit

