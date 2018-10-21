begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.mybatis
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|mybatis
package|;
end_package

begin_comment
comment|/**  * Statement types to instruct which MyBatis operation to use.  */
end_comment

begin_enum
DECL|enum|StatementType
specifier|public
enum|enum
name|StatementType
block|{
DECL|enumConstant|SelectOne
DECL|enumConstant|SelectList
DECL|enumConstant|Insert
DECL|enumConstant|InsertList
DECL|enumConstant|Update
DECL|enumConstant|UpdateList
DECL|enumConstant|Delete
DECL|enumConstant|DeleteList
name|SelectOne
block|,
name|SelectList
block|,
name|Insert
block|,
name|InsertList
block|,
name|Update
block|,
name|UpdateList
block|,
name|Delete
block|,
name|DeleteList
block|}
end_enum

end_unit

