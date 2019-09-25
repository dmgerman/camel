begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.builder
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
package|;
end_package

begin_comment
comment|/**  * A helper class for working with predicates  */
end_comment

begin_class
DECL|class|PredicateBuilder
specifier|public
specifier|final
class|class
name|PredicateBuilder
extends|extends
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|support
operator|.
name|builder
operator|.
name|PredicateBuilder
block|{
comment|// this class is included in camel-core to be backwards compatible by
comment|// extending from camel-support
comment|/**      * Utility classes should not have a public constructor.      */
DECL|method|PredicateBuilder ()
specifier|private
name|PredicateBuilder
parameter_list|()
block|{     }
block|}
end_class

end_unit

