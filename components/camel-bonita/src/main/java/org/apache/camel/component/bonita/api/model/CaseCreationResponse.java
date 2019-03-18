begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.bonita.api.model
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|bonita
operator|.
name|api
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
name|JsonProperty
import|;
end_import

begin_class
DECL|class|CaseCreationResponse
specifier|public
class|class
name|CaseCreationResponse
block|{
annotation|@
name|JsonProperty
argument_list|(
literal|"caseId"
argument_list|)
DECL|field|caseId
specifier|private
name|String
name|caseId
decl_stmt|;
DECL|method|getCaseId ()
specifier|public
name|String
name|getCaseId
parameter_list|()
block|{
return|return
name|caseId
return|;
block|}
DECL|method|setCaseId (String caseId)
specifier|public
name|void
name|setCaseId
parameter_list|(
name|String
name|caseId
parameter_list|)
block|{
name|this
operator|.
name|caseId
operator|=
name|caseId
expr_stmt|;
block|}
block|}
end_class

end_unit

