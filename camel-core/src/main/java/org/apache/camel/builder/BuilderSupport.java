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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Expression
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
name|Exchange
import|;
end_import

begin_comment
comment|/**  * Base class for implementation inheritence  *  * @version $Revision: $  */
end_comment

begin_class
DECL|class|BuilderSupport
specifier|public
specifier|abstract
class|class
name|BuilderSupport
parameter_list|<
name|E
extends|extends
name|Exchange
parameter_list|>
block|{
comment|/**      * Returns a predicate and value builder for headers on an exchange      */
DECL|method|header (String name)
specifier|public
name|ValueBuilder
argument_list|<
name|E
argument_list|>
name|header
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|Expression
argument_list|<
name|E
argument_list|>
name|expression
init|=
name|ExpressionBuilder
operator|.
name|headerExpression
argument_list|(
name|name
argument_list|)
decl_stmt|;
return|return
operator|new
name|ValueBuilder
argument_list|<
name|E
argument_list|>
argument_list|(
name|expression
argument_list|)
return|;
block|}
block|}
end_class

end_unit

