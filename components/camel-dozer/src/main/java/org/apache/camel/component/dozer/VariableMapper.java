begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.dozer
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|dozer
package|;
end_package

begin_comment
comment|/**  * Used to map literal values (e.g. "ACME" or "ABC-123") to a field in the   * target object.  */
end_comment

begin_class
DECL|class|VariableMapper
specifier|public
class|class
name|VariableMapper
extends|extends
name|BaseConverter
block|{
annotation|@
name|Override
DECL|method|convert (Object existingDestinationFieldValue, Object sourceFieldValue, Class<?> destinationClass, Class<?> sourceClass)
specifier|public
name|Object
name|convert
parameter_list|(
name|Object
name|existingDestinationFieldValue
parameter_list|,
name|Object
name|sourceFieldValue
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|destinationClass
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|sourceClass
parameter_list|)
block|{
try|try
block|{
return|return
name|getParameter
argument_list|()
return|;
block|}
finally|finally
block|{
name|done
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * We need at least one field in this class so that we can use it as a      * source for Dozer transformations.      */
DECL|method|getLiteral ()
specifier|public
name|String
name|getLiteral
parameter_list|()
block|{
return|return
name|getParameter
argument_list|()
return|;
block|}
block|}
end_class

end_unit

