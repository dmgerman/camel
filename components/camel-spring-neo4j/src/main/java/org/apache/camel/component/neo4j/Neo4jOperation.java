begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.neo4j
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|neo4j
package|;
end_package

begin_enum
DECL|enum|Neo4jOperation
specifier|public
enum|enum
name|Neo4jOperation
block|{
DECL|enumConstant|CREATE_NODE
name|CREATE_NODE
block|,
DECL|enumConstant|REMOVE_NODE
name|REMOVE_NODE
block|,
DECL|enumConstant|CREATE_RELATIONSHIP
name|CREATE_RELATIONSHIP
block|,
DECL|enumConstant|REMOVE_RELATIONSHIP
name|REMOVE_RELATIONSHIP
block|}
end_enum

end_unit

