begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.nitrite.operation.common
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|nitrite
operator|.
name|operation
operator|.
name|common
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Exchange
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
name|nitrite
operator|.
name|AbstractNitriteOperation
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
name|nitrite
operator|.
name|NitriteEndpoint
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
name|nitrite
operator|.
name|operation
operator|.
name|CommonOperation
import|;
end_import

begin_import
import|import
name|org
operator|.
name|dizitart
operator|.
name|no2
operator|.
name|IndexOptions
import|;
end_import

begin_comment
comment|/**  * Create index with IndexOptions on field  */
end_comment

begin_class
DECL|class|CreateIndexOperation
specifier|public
class|class
name|CreateIndexOperation
extends|extends
name|AbstractNitriteOperation
implements|implements
name|CommonOperation
block|{
DECL|field|field
specifier|private
name|String
name|field
decl_stmt|;
DECL|field|indexOptions
specifier|private
name|IndexOptions
name|indexOptions
decl_stmt|;
DECL|method|CreateIndexOperation (String field, IndexOptions indexOptions)
specifier|public
name|CreateIndexOperation
parameter_list|(
name|String
name|field
parameter_list|,
name|IndexOptions
name|indexOptions
parameter_list|)
block|{
name|this
operator|.
name|field
operator|=
name|field
expr_stmt|;
name|this
operator|.
name|indexOptions
operator|=
name|indexOptions
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|execute (Exchange exchange, NitriteEndpoint endpoint)
specifier|protected
name|void
name|execute
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|NitriteEndpoint
name|endpoint
parameter_list|)
throws|throws
name|Exception
block|{
name|endpoint
operator|.
name|getNitriteCollection
argument_list|()
operator|.
name|createIndex
argument_list|(
name|field
argument_list|,
name|indexOptions
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

