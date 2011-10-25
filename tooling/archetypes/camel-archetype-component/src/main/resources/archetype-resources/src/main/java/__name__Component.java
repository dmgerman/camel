begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
package|package
name|$
block|{
package|package
block|}
end_package

begin_empty_stmt
empty_stmt|;
end_empty_stmt

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Endpoint
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
operator|.
name|DefaultComponent
import|;
end_import

begin_comment
comment|/**  * Represents the component that manages {@link ${name}Endpoint}.  */
end_comment

begin_class
DECL|class|$
specifier|public
class|class
name|$
block|{
name|name
block|}
end_class

begin_expr_stmt
DECL|class|$
name|Component
expr|extends
name|DefaultComponent
block|{
specifier|protected
name|Endpoint
name|createEndpoint
argument_list|(
name|String
name|uri
argument_list|,
name|String
name|remaining
argument_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
argument_list|)
throws|throws
name|Exception
block|{
name|Endpoint
name|endpoint
operator|=
operator|new
name|$
block|{
name|name
block|}
name|Endpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|)
block|;
name|setProperties
argument_list|(
name|endpoint
argument_list|,
name|parameters
argument_list|)
block|;
return|return
name|endpoint
return|;
block|}
end_expr_stmt

unit|}
end_unit

