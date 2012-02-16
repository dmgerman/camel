begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.mongodb
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|mongodb
package|;
end_package

begin_enum
DECL|enum|MongoDbOperation
specifier|public
enum|enum
name|MongoDbOperation
block|{
comment|// read operations
DECL|enumConstant|findById
name|findById
block|,
DECL|enumConstant|findOneByQuery
name|findOneByQuery
block|,
DECL|enumConstant|findAll
name|findAll
block|,
comment|// group, // future
comment|// mapReduce, // future
comment|// create/update operations
DECL|enumConstant|insert
name|insert
block|,
DECL|enumConstant|save
name|save
block|,
DECL|enumConstant|update
name|update
block|,
comment|// delete operations
DECL|enumConstant|remove
name|remove
block|,
comment|// others
DECL|enumConstant|getDbStats
name|getDbStats
block|,
DECL|enumConstant|getColStats
name|getColStats
block|,
DECL|enumConstant|count
name|count
block|,       }
end_enum

end_unit

