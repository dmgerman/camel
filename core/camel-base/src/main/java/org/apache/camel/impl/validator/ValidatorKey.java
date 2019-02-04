begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl.validator
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
operator|.
name|validator
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
name|ValueHolder
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

begin_comment
comment|/**  * Key used in {@link org.apache.camel.spi.ValidatorRegistry} in {@link CamelContext},  * to ensure a consistent lookup.  */
end_comment

begin_class
DECL|class|ValidatorKey
specifier|public
specifier|final
class|class
name|ValidatorKey
extends|extends
name|ValueHolder
argument_list|<
name|String
argument_list|>
block|{
DECL|field|type
specifier|private
name|DataType
name|type
decl_stmt|;
DECL|method|ValidatorKey (DataType type)
specifier|public
name|ValidatorKey
parameter_list|(
name|DataType
name|type
parameter_list|)
block|{
name|super
argument_list|(
name|type
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
block|}
DECL|method|getType ()
specifier|public
name|DataType
name|getType
parameter_list|()
block|{
return|return
name|type
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
name|get
argument_list|()
return|;
block|}
block|}
end_class

end_unit

