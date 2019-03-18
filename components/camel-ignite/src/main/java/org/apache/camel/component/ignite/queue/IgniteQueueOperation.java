begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.ignite.queue
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
name|queue
package|;
end_package

begin_comment
comment|/**  * Enumeration of queue operations.  */
end_comment

begin_enum
DECL|enum|IgniteQueueOperation
specifier|public
enum|enum
name|IgniteQueueOperation
block|{
DECL|enumConstant|CONTAINS
DECL|enumConstant|ADD
DECL|enumConstant|SIZE
DECL|enumConstant|REMOVE
DECL|enumConstant|ITERATOR
DECL|enumConstant|CLEAR
DECL|enumConstant|RETAIN_ALL
DECL|enumConstant|ARRAY
DECL|enumConstant|DRAIN
DECL|enumConstant|ELEMENT
DECL|enumConstant|PEEK
DECL|enumConstant|OFFER
DECL|enumConstant|POLL
DECL|enumConstant|TAKE
DECL|enumConstant|PUT
name|CONTAINS
block|,
name|ADD
block|,
name|SIZE
block|,
name|REMOVE
block|,
name|ITERATOR
block|,
name|CLEAR
block|,
name|RETAIN_ALL
block|,
name|ARRAY
block|,
name|DRAIN
block|,
name|ELEMENT
block|,
name|PEEK
block|,
name|OFFER
block|,
name|POLL
block|,
name|TAKE
block|,
name|PUT
block|}
end_enum

end_unit

