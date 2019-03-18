begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.atomix.client.multimap
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
name|multimap
package|;
end_package

begin_class
DECL|class|AtomixMultiMap
specifier|public
specifier|final
class|class
name|AtomixMultiMap
block|{
DECL|enum|Action
specifier|public
enum|enum
name|Action
block|{
DECL|enumConstant|PUT
name|PUT
block|,
DECL|enumConstant|GET
name|GET
block|,
DECL|enumConstant|CLEAR
name|CLEAR
block|,
DECL|enumConstant|SIZE
name|SIZE
block|,
DECL|enumConstant|CONTAINS_KEY
name|CONTAINS_KEY
block|,
comment|//CONTAINS_VALUE,
comment|//CONTAINS_ENTRY,
DECL|enumConstant|IS_EMPTY
name|IS_EMPTY
block|,
DECL|enumConstant|REMOVE
name|REMOVE
block|,
DECL|enumConstant|REMOVE_VALUE
name|REMOVE_VALUE
block|,     }
DECL|method|AtomixMultiMap ()
specifier|private
name|AtomixMultiMap
parameter_list|()
block|{     }
block|}
end_class

end_unit

