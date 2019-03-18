begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.olingo4.api.batch
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|olingo4
operator|.
name|api
operator|.
name|batch
package|;
end_package

begin_comment
comment|/** * OData operation used by {@link org.apache.camel.component.olingo4.api.batch.Olingo4BatchChangeRequest}. */
end_comment

begin_enum
DECL|enum|Operation
specifier|public
enum|enum
name|Operation
block|{
DECL|enumConstant|CREATE
name|CREATE
argument_list|(
literal|"POST"
argument_list|)
block|,
DECL|enumConstant|UPDATE
name|UPDATE
argument_list|(
literal|"PUT"
argument_list|)
block|,
DECL|enumConstant|PATCH
name|PATCH
argument_list|(
literal|"PATCH"
argument_list|)
block|,
DECL|enumConstant|MERGE
name|MERGE
argument_list|(
literal|"MERGE"
argument_list|)
block|,
DECL|enumConstant|DELETE
name|DELETE
argument_list|(
literal|"DELETE"
argument_list|)
block|;
DECL|field|httpMethod
specifier|private
specifier|final
name|String
name|httpMethod
decl_stmt|;
DECL|method|Operation (String httpMethod)
name|Operation
parameter_list|(
name|String
name|httpMethod
parameter_list|)
block|{
name|this
operator|.
name|httpMethod
operator|=
name|httpMethod
expr_stmt|;
block|}
DECL|method|getHttpMethod ()
specifier|public
name|String
name|getHttpMethod
parameter_list|()
block|{
return|return
name|httpMethod
return|;
block|}
block|}
end_enum

end_unit

