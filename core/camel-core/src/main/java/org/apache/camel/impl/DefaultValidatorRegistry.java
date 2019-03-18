begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|CamelContext
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
name|validator
operator|.
name|ValidatorKey
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
name|model
operator|.
name|validator
operator|.
name|ValidatorDefinition
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
name|reifier
operator|.
name|validator
operator|.
name|ValidatorReifier
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
name|spi
operator|.
name|DataType
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
name|spi
operator|.
name|Validator
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
name|spi
operator|.
name|ValidatorRegistry
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
name|support
operator|.
name|CamelContextHelper
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
name|util
operator|.
name|ObjectHelper
import|;
end_import

begin_comment
comment|/**  * Default implementation of {@link org.apache.camel.spi.ValidatorRegistry}.  */
end_comment

begin_class
DECL|class|DefaultValidatorRegistry
specifier|public
class|class
name|DefaultValidatorRegistry
extends|extends
name|AbstractDynamicRegistry
argument_list|<
name|ValidatorKey
argument_list|,
name|Validator
argument_list|>
implements|implements
name|ValidatorRegistry
argument_list|<
name|ValidatorKey
argument_list|>
block|{
DECL|method|DefaultValidatorRegistry (CamelContext context)
specifier|public
name|DefaultValidatorRegistry
parameter_list|(
name|CamelContext
name|context
parameter_list|)
throws|throws
name|Exception
block|{
name|super
argument_list|(
name|context
argument_list|,
name|CamelContextHelper
operator|.
name|getMaximumValidatorCacheSize
argument_list|(
name|context
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|DefaultValidatorRegistry (CamelContext context, List<ValidatorDefinition> definitions)
specifier|public
name|DefaultValidatorRegistry
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|List
argument_list|<
name|ValidatorDefinition
argument_list|>
name|definitions
parameter_list|)
throws|throws
name|Exception
block|{
name|this
argument_list|(
name|context
argument_list|)
expr_stmt|;
for|for
control|(
name|ValidatorDefinition
name|def
range|:
name|definitions
control|)
block|{
name|Validator
name|validator
init|=
name|ValidatorReifier
operator|.
name|reifier
argument_list|(
name|def
argument_list|)
operator|.
name|createValidator
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|context
operator|.
name|addService
argument_list|(
name|validator
argument_list|)
expr_stmt|;
name|put
argument_list|(
name|createKey
argument_list|(
name|def
argument_list|)
argument_list|,
name|validator
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|resolveValidator (ValidatorKey key)
specifier|public
name|Validator
name|resolveValidator
parameter_list|(
name|ValidatorKey
name|key
parameter_list|)
block|{
name|Validator
name|answer
init|=
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|answer
operator|==
literal|null
operator|&&
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|key
operator|.
name|getType
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|answer
operator|=
name|get
argument_list|(
operator|new
name|ValidatorKey
argument_list|(
operator|new
name|DataType
argument_list|(
name|key
operator|.
name|getType
argument_list|()
operator|.
name|getModel
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
annotation|@
name|Override
DECL|method|isStatic (DataType type)
specifier|public
name|boolean
name|isStatic
parameter_list|(
name|DataType
name|type
parameter_list|)
block|{
return|return
name|isStatic
argument_list|(
operator|new
name|ValidatorKey
argument_list|(
name|type
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|isDynamic (DataType type)
specifier|public
name|boolean
name|isDynamic
parameter_list|(
name|DataType
name|type
parameter_list|)
block|{
return|return
name|isDynamic
argument_list|(
operator|new
name|ValidatorKey
argument_list|(
name|type
argument_list|)
argument_list|)
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
literal|"ValidatorRegistry for "
operator|+
name|context
operator|.
name|getName
argument_list|()
operator|+
literal|", capacity: "
operator|+
name|maxCacheSize
return|;
block|}
DECL|method|createKey (ValidatorDefinition def)
specifier|private
name|ValidatorKey
name|createKey
parameter_list|(
name|ValidatorDefinition
name|def
parameter_list|)
block|{
return|return
operator|new
name|ValidatorKey
argument_list|(
operator|new
name|DataType
argument_list|(
name|def
operator|.
name|getType
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit

