begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Collections
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
name|java
operator|.
name|util
operator|.
name|Optional
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Collectors
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
name|XStreamImplicit
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
name|RestError
import|;
end_import

begin_comment
comment|/**  * Response from the SObject tree Composite API invocation.  */
end_comment

begin_class
annotation|@
name|XStreamAlias
argument_list|(
literal|"Result"
argument_list|)
comment|// you might be wondering why `Result` and not `SObjectTreeResponse as in documentation, well,
comment|// the difference between documentation and practice is usually found in practice
DECL|class|SObjectTreeResponse
specifier|public
specifier|final
class|class
name|SObjectTreeResponse
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
annotation|@
name|XStreamImplicit
DECL|field|results
specifier|private
specifier|final
name|List
argument_list|<
name|ReferenceId
argument_list|>
name|results
decl_stmt|;
annotation|@
name|JsonCreator
DECL|method|SObjectTreeResponse (@sonPropertyR) final boolean hasErrors, @JsonProperty(R) final List<ReferenceId> results)
specifier|public
name|SObjectTreeResponse
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
name|ReferenceId
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
name|Optional
operator|.
name|ofNullable
argument_list|(
name|results
argument_list|)
operator|.
name|orElse
argument_list|(
name|Collections
operator|.
name|emptyList
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|getAllErrors ()
specifier|public
name|List
argument_list|<
name|RestError
argument_list|>
name|getAllErrors
parameter_list|()
block|{
return|return
name|results
operator|.
name|stream
argument_list|()
operator|.
name|flatMap
argument_list|(
name|r
lambda|->
name|r
operator|.
name|getErrors
argument_list|()
operator|.
name|stream
argument_list|()
argument_list|)
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toList
argument_list|()
argument_list|)
return|;
block|}
DECL|method|getResults ()
specifier|public
name|List
argument_list|<
name|ReferenceId
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
block|}
end_class

end_unit

