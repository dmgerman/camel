begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.elasticsearch
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|elasticsearch
package|;
end_package

begin_comment
comment|/**  * The ElasticSearch server operations list which are implemented  *   * Index        - Index a document associated with a given index and type  * Update       - Updates a document based on a script  * Bulk         - Executes a bulk of index / delete operations  * BulkIndex   - Executes a bulk of index / delete operations  * GetById    - Gets the document that was indexed from an index with a type and id  * MultiGet     - Multiple get documents  * Delete       - Deletes a document from the index based on the index, type and id  * Search       - Search across one or more indices and one or more types with a query  * Exists       - Checks the index exists or not (using search with size=0 and terminate_after=1 parameters)  */
end_comment

begin_enum
DECL|enum|ElasticsearchOperation
specifier|public
enum|enum
name|ElasticsearchOperation
block|{
DECL|enumConstant|Index
name|Index
argument_list|(
literal|"Index"
argument_list|)
block|,
DECL|enumConstant|Update
name|Update
argument_list|(
literal|"Update"
argument_list|)
block|,
DECL|enumConstant|Bulk
name|Bulk
argument_list|(
literal|"Bulk"
argument_list|)
block|,
DECL|enumConstant|BulkIndex
name|BulkIndex
argument_list|(
literal|"BulkIndex"
argument_list|)
block|,
DECL|enumConstant|GetById
name|GetById
argument_list|(
literal|"GetById"
argument_list|)
block|,
DECL|enumConstant|MultiGet
name|MultiGet
argument_list|(
literal|"MultiGet"
argument_list|)
block|,
DECL|enumConstant|Delete
name|Delete
argument_list|(
literal|"Delete"
argument_list|)
block|,
DECL|enumConstant|DeleteIndex
name|DeleteIndex
argument_list|(
literal|"DeleteIndex"
argument_list|)
block|,
DECL|enumConstant|Search
name|Search
argument_list|(
literal|"Search"
argument_list|)
block|,
DECL|enumConstant|Exists
name|Exists
argument_list|(
literal|"Exists"
argument_list|)
block|,
DECL|enumConstant|Ping
name|Ping
argument_list|(
literal|"Ping"
argument_list|)
block|,
DECL|enumConstant|Info
name|Info
argument_list|(
literal|"Info"
argument_list|)
block|;
DECL|field|text
specifier|private
specifier|final
name|String
name|text
decl_stmt|;
DECL|method|ElasticsearchOperation (final String text)
name|ElasticsearchOperation
parameter_list|(
specifier|final
name|String
name|text
parameter_list|)
block|{
name|this
operator|.
name|text
operator|=
name|text
expr_stmt|;
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
name|text
return|;
block|}
block|}
end_enum

end_unit

