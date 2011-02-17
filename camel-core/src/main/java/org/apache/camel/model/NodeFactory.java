begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.model
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|model
package|;
end_package

begin_comment
comment|/**  * A simple factory used to create new child nodes which allows pluggable extension points  * such as to add extra DSL helper methods such as for the Groovy or Ruby DSLs  *  * @version   */
end_comment

begin_class
DECL|class|NodeFactory
specifier|public
class|class
name|NodeFactory
block|{
DECL|method|createFilter ()
specifier|public
name|FilterDefinition
name|createFilter
parameter_list|()
block|{
return|return
operator|new
name|FilterDefinition
argument_list|()
return|;
block|}
DECL|method|createLoop ()
specifier|public
name|LoopDefinition
name|createLoop
parameter_list|()
block|{
return|return
operator|new
name|LoopDefinition
argument_list|()
return|;
block|}
DECL|method|createRoute ()
specifier|public
name|RouteDefinition
name|createRoute
parameter_list|()
block|{
return|return
operator|new
name|RouteDefinition
argument_list|()
return|;
block|}
block|}
end_class

end_unit

