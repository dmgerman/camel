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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_comment
comment|/**  * Base class for definitions which does not support outputs.  */
end_comment

begin_class
DECL|class|NoOutputDefinition
specifier|public
specifier|abstract
class|class
name|NoOutputDefinition
parameter_list|<
name|Type
extends|extends
name|ProcessorDefinition
parameter_list|<
name|Type
parameter_list|>
parameter_list|>
extends|extends
name|ProcessorDefinition
argument_list|<
name|Type
argument_list|>
block|{
DECL|method|getOutputs ()
specifier|public
name|List
argument_list|<
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
argument_list|>
name|getOutputs
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|emptyList
argument_list|()
return|;
block|}
DECL|method|isOutputSupported ()
specifier|public
name|boolean
name|isOutputSupported
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
block|}
end_class

end_unit

