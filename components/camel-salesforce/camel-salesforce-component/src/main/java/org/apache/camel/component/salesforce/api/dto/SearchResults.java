begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.salesforce.api.dto
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
package|;
end_package

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

begin_comment
comment|/**  * DTO for Salesforce SOSL Search results.  */
end_comment

begin_class
annotation|@
name|XStreamAlias
argument_list|(
literal|"SearchResults"
argument_list|)
DECL|class|SearchResults
specifier|public
specifier|final
class|class
name|SearchResults
extends|extends
name|AbstractDTOBase
block|{
annotation|@
name|XStreamImplicit
argument_list|(
name|itemFieldName
operator|=
literal|"SearchResult"
argument_list|)
DECL|field|results
specifier|private
name|List
argument_list|<
name|SearchResult
argument_list|>
name|results
decl_stmt|;
DECL|method|getResults ()
specifier|public
name|List
argument_list|<
name|SearchResult
argument_list|>
name|getResults
parameter_list|()
block|{
return|return
name|results
return|;
block|}
DECL|method|setResults (List<SearchResult> results)
specifier|public
name|void
name|setResults
parameter_list|(
name|List
argument_list|<
name|SearchResult
argument_list|>
name|results
parameter_list|)
block|{
name|this
operator|.
name|results
operator|=
name|results
expr_stmt|;
block|}
block|}
end_class

end_unit

