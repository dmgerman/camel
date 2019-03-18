begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.wordpress.api.model
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|wordpress
operator|.
name|api
operator|.
name|model
package|;
end_package

begin_comment
comment|/**  * Sort collection by object attribute.  */
end_comment

begin_enum
DECL|enum|PostOrderBy
specifier|public
enum|enum
name|PostOrderBy
block|{
DECL|enumConstant|author
DECL|enumConstant|date
DECL|enumConstant|id
DECL|enumConstant|include
DECL|enumConstant|modified
DECL|enumConstant|parent
DECL|enumConstant|relevance
DECL|enumConstant|slug
DECL|enumConstant|title
name|author
block|,
name|date
block|,
name|id
block|,
name|include
block|,
name|modified
block|,
name|parent
block|,
name|relevance
block|,
name|slug
block|,
name|title
block|;  }
end_enum

end_unit

