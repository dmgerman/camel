begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.atomix.client.set
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|atomix
operator|.
name|client
operator|.
name|set
package|;
end_package

begin_class
DECL|class|AtomixSet
specifier|public
specifier|final
class|class
name|AtomixSet
block|{
DECL|enum|Action
specifier|public
enum|enum
name|Action
block|{
DECL|enumConstant|ADD
name|ADD
block|,
DECL|enumConstant|CLEAR
name|CLEAR
block|,
DECL|enumConstant|CONTAINS
name|CONTAINS
block|,
DECL|enumConstant|IS_EMPTY
name|IS_EMPTY
block|,
DECL|enumConstant|REMOVE
name|REMOVE
block|,
DECL|enumConstant|SIZE
name|SIZE
block|}
DECL|method|AtomixSet ()
specifier|private
name|AtomixSet
parameter_list|()
block|{     }
block|}
end_class

end_unit

