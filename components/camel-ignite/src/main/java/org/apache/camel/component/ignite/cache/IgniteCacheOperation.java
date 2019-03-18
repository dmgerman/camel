begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.ignite.cache
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|ignite
operator|.
name|cache
package|;
end_package

begin_comment
comment|/**  * Enumeration of Ignite Cache operations.  */
end_comment

begin_enum
DECL|enum|IgniteCacheOperation
specifier|public
enum|enum
name|IgniteCacheOperation
block|{
DECL|enumConstant|GET
DECL|enumConstant|PUT
DECL|enumConstant|REMOVE
DECL|enumConstant|SIZE
DECL|enumConstant|REBALANCE
DECL|enumConstant|QUERY
DECL|enumConstant|CLEAR
name|GET
block|,
name|PUT
block|,
name|REMOVE
block|,
name|SIZE
block|,
name|REBALANCE
block|,
name|QUERY
block|,
name|CLEAR
block|}
end_enum

end_unit

