begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Method
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
name|ClassResolver
import|;
end_import

begin_comment
comment|/**  * Allows a user to customize a field mapping using a POJO that is not  * required to extend/implement Dozer-specific classes.  */
end_comment

begin_class
DECL|class|CustomMapper
specifier|public
class|class
name|CustomMapper
extends|extends
name|BaseConverter
block|{
DECL|field|resolver
specifier|private
name|ClassResolver
name|resolver
decl_stmt|;
DECL|method|CustomMapper (ClassResolver resolver)
specifier|public
name|CustomMapper
parameter_list|(
name|ClassResolver
name|resolver
parameter_list|)
block|{
name|this
operator|.
name|resolver
operator|=
name|resolver
expr_stmt|;
block|}
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
name|mapCustom
argument_list|(
name|sourceFieldValue
argument_list|)
return|;
block|}
finally|finally
block|{
name|done
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|selectMethod (Class<?> customClass, Object fromType)
name|Method
name|selectMethod
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|customClass
parameter_list|,
name|Object
name|fromType
parameter_list|)
block|{
name|Method
name|method
init|=
literal|null
decl_stmt|;
for|for
control|(
name|Method
name|m
range|:
name|customClass
operator|.
name|getDeclaredMethods
argument_list|()
control|)
block|{
if|if
condition|(
name|m
operator|.
name|getReturnType
argument_list|()
operator|!=
literal|null
operator|&&
name|m
operator|.
name|getParameterTypes
argument_list|()
operator|.
name|length
operator|==
literal|1
operator|&&
name|m
operator|.
name|getParameterTypes
argument_list|()
index|[
literal|0
index|]
operator|.
name|isAssignableFrom
argument_list|(
name|fromType
operator|.
name|getClass
argument_list|()
argument_list|)
condition|)
block|{
name|method
operator|=
name|m
expr_stmt|;
break|break;
block|}
block|}
return|return
name|method
return|;
block|}
DECL|method|mapCustom (Object source)
name|Object
name|mapCustom
parameter_list|(
name|Object
name|source
parameter_list|)
block|{
name|Object
name|customMapObj
decl_stmt|;
name|Method
name|mapMethod
decl_stmt|;
comment|// The converter parameter is stored in a thread local variable, so
comment|// we need to parse the parameter on each invocation
name|String
index|[]
name|params
init|=
name|getParameter
argument_list|()
operator|.
name|split
argument_list|(
literal|","
argument_list|)
decl_stmt|;
name|String
name|className
init|=
name|params
index|[
literal|0
index|]
decl_stmt|;
name|String
name|operation
init|=
name|params
operator|.
name|length
operator|>
literal|1
condition|?
name|params
index|[
literal|1
index|]
else|:
literal|null
decl_stmt|;
try|try
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|customClass
init|=
name|resolver
operator|.
name|resolveClass
argument_list|(
name|className
argument_list|)
decl_stmt|;
name|customMapObj
operator|=
name|customClass
operator|.
name|newInstance
argument_list|()
expr_stmt|;
comment|// If a specific mapping operation has been supplied use that
if|if
condition|(
name|operation
operator|!=
literal|null
condition|)
block|{
name|mapMethod
operator|=
name|customClass
operator|.
name|getMethod
argument_list|(
name|operation
argument_list|,
name|source
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|mapMethod
operator|=
name|selectMethod
argument_list|(
name|customClass
argument_list|,
name|source
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|cnfEx
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Failed to load custom mapping"
argument_list|,
name|cnfEx
argument_list|)
throw|;
block|}
comment|// Verify that we found a matching method
if|if
condition|(
name|mapMethod
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"No eligible custom mapping methods in "
operator|+
name|className
argument_list|)
throw|;
block|}
comment|// Invoke the custom mapping method
try|try
block|{
return|return
name|mapMethod
operator|.
name|invoke
argument_list|(
name|customMapObj
argument_list|,
name|source
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Error while invoking custom mapping"
argument_list|,
name|ex
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

