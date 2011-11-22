begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.stax
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|stax
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

begin_class
DECL|class|StAXBuilder
specifier|public
specifier|final
class|class
name|StAXBuilder
block|{
DECL|method|StAXBuilder ()
specifier|private
name|StAXBuilder
parameter_list|()
block|{
comment|// no-op
block|}
DECL|method|stax (Class<T> clazz)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|Expression
name|stax
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|clazz
parameter_list|)
block|{
return|return
operator|new
name|StAXJAXBIteratorExpression
argument_list|<
name|T
argument_list|>
argument_list|(
name|clazz
argument_list|)
return|;
block|}
block|}
end_class

end_unit

