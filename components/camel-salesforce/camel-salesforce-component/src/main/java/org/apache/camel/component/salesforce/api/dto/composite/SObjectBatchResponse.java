begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.salesforce.api.dto.composite
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
name|composite
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Serializable
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|JsonCreator
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

begin_comment
comment|/**  * The response of the batch request it contains individual results of each request submitted in a batch at the same  * index. The flag {@link #hasErrors()} indicates if any of the requests in the batch has failed with status 400 or 500.  */
end_comment

begin_class
annotation|@
name|XStreamAlias
argument_list|(
literal|"batchResults"
argument_list|)
DECL|class|SObjectBatchResponse
specifier|public
specifier|final
class|class
name|SObjectBatchResponse
implements|implements
name|Serializable
block|{
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
decl_stmt|;
DECL|field|hasErrors
specifier|private
specifier|final
name|boolean
name|hasErrors
decl_stmt|;
DECL|field|results
specifier|private
specifier|final
name|List
argument_list|<
name|SObjectBatchResult
argument_list|>
name|results
decl_stmt|;
annotation|@
name|JsonCreator
DECL|method|SObjectBatchResponse (@sonPropertyR) final boolean hasErrors, @JsonProperty(R) final List<SObjectBatchResult> results)
specifier|public
name|SObjectBatchResponse
parameter_list|(
annotation|@
name|JsonProperty
argument_list|(
literal|"hasErrors"
argument_list|)
specifier|final
name|boolean
name|hasErrors
parameter_list|,
annotation|@
name|JsonProperty
argument_list|(
literal|"results"
argument_list|)
specifier|final
name|List
argument_list|<
name|SObjectBatchResult
argument_list|>
name|results
parameter_list|)
block|{
name|this
operator|.
name|hasErrors
operator|=
name|hasErrors
expr_stmt|;
name|this
operator|.
name|results
operator|=
name|results
expr_stmt|;
block|}
DECL|method|getResults ()
specifier|public
name|List
argument_list|<
name|SObjectBatchResult
argument_list|>
name|getResults
parameter_list|()
block|{
return|return
name|results
return|;
block|}
DECL|method|hasErrors ()
specifier|public
name|boolean
name|hasErrors
parameter_list|()
block|{
return|return
name|hasErrors
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"hasErrors: "
operator|+
name|hasErrors
operator|+
literal|", results: "
operator|+
name|results
return|;
block|}
block|}
end_class

end_unit

