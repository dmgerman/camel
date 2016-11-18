begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.firebase.data
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|firebase
operator|.
name|data
package|;
end_package

begin_comment
comment|/**  * Operations associated with the messages.  */
end_comment

begin_enum
DECL|enum|Operation
specifier|public
enum|enum
name|Operation
block|{
DECL|enumConstant|CHILD_ADD
name|CHILD_ADD
block|,
DECL|enumConstant|CHILD_CHANGED
name|CHILD_CHANGED
block|,
DECL|enumConstant|CHILD_REMOVED
name|CHILD_REMOVED
block|,
DECL|enumConstant|CHILD_MOVED
name|CHILD_MOVED
block|,
DECL|enumConstant|CANCELLED
name|CANCELLED
block|}
end_enum

end_unit

