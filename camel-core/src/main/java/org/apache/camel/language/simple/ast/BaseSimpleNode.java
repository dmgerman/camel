begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.language.simple.ast
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|language
operator|.
name|simple
operator|.
name|ast
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
name|language
operator|.
name|simple
operator|.
name|SimpleToken
import|;
end_import

begin_comment
comment|/**  * Base class for {@link SimpleNode} nodes.  */
end_comment

begin_class
DECL|class|BaseSimpleNode
specifier|public
specifier|abstract
class|class
name|BaseSimpleNode
implements|implements
name|SimpleNode
block|{
DECL|field|token
specifier|protected
specifier|final
name|SimpleToken
name|token
decl_stmt|;
DECL|method|BaseSimpleNode (SimpleToken token)
specifier|protected
name|BaseSimpleNode
parameter_list|(
name|SimpleToken
name|token
parameter_list|)
block|{
name|this
operator|.
name|token
operator|=
name|token
expr_stmt|;
block|}
DECL|method|getToken ()
specifier|public
name|SimpleToken
name|getToken
parameter_list|()
block|{
return|return
name|token
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|token
operator|.
name|getText
argument_list|()
return|;
block|}
block|}
end_class

end_unit

