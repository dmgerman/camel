begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.salesforce.internal.dto
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
name|internal
operator|.
name|dto
package|;
end_package

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|jackson
operator|.
name|annotate
operator|.
name|JsonProperty
import|;
end_import

begin_comment
comment|/**  * DTO for Salesforce login error  */
end_comment

begin_class
DECL|class|LoginError
specifier|public
class|class
name|LoginError
block|{
DECL|field|error
specifier|private
name|String
name|error
decl_stmt|;
DECL|field|errorDescription
specifier|private
name|String
name|errorDescription
decl_stmt|;
DECL|method|getError ()
specifier|public
name|String
name|getError
parameter_list|()
block|{
return|return
name|error
return|;
block|}
DECL|method|setError (String error)
specifier|public
name|void
name|setError
parameter_list|(
name|String
name|error
parameter_list|)
block|{
name|this
operator|.
name|error
operator|=
name|error
expr_stmt|;
block|}
annotation|@
name|JsonProperty
argument_list|(
literal|"error_description"
argument_list|)
DECL|method|getErrorDescription ()
specifier|public
name|String
name|getErrorDescription
parameter_list|()
block|{
return|return
name|errorDescription
return|;
block|}
annotation|@
name|JsonProperty
argument_list|(
literal|"error_description"
argument_list|)
DECL|method|setErrorDescription (String errorDescription)
specifier|public
name|void
name|setErrorDescription
parameter_list|(
name|String
name|errorDescription
parameter_list|)
block|{
name|this
operator|.
name|errorDescription
operator|=
name|errorDescription
expr_stmt|;
block|}
block|}
end_class

end_unit

