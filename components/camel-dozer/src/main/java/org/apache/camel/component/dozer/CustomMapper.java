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
name|Array
import|;
end_import

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
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
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
DECL|method|invokeFunction (Method method, Object customObj, Object source, String[][] parameters)
specifier|private
name|Object
name|invokeFunction
parameter_list|(
name|Method
name|method
parameter_list|,
name|Object
name|customObj
parameter_list|,
name|Object
name|source
parameter_list|,
name|String
index|[]
index|[]
name|parameters
parameter_list|)
throws|throws
name|Exception
block|{
name|Class
argument_list|<
name|?
argument_list|>
index|[]
name|prmTypes
init|=
name|method
operator|.
name|getParameterTypes
argument_list|()
decl_stmt|;
name|Object
index|[]
name|methodPrms
init|=
operator|new
name|Object
index|[
name|prmTypes
operator|.
name|length
index|]
decl_stmt|;
name|methodPrms
index|[
literal|0
index|]
operator|=
name|source
expr_stmt|;
for|for
control|(
name|int
name|parameterNdx
init|=
literal|0
init|,
name|methodPrmNdx
init|=
literal|1
init|;
name|parameterNdx
operator|<
name|parameters
operator|.
name|length
condition|;
name|parameterNdx
operator|++
operator|,
name|methodPrmNdx
operator|++
control|)
block|{
if|if
condition|(
name|method
operator|.
name|isVarArgs
argument_list|()
operator|&&
name|methodPrmNdx
operator|==
name|prmTypes
operator|.
name|length
operator|-
literal|1
condition|)
block|{
name|Object
name|array
init|=
name|Array
operator|.
name|newInstance
argument_list|(
name|prmTypes
index|[
name|methodPrmNdx
index|]
operator|.
name|getComponentType
argument_list|()
argument_list|,
name|parameters
operator|.
name|length
operator|-
name|parameterNdx
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|arrayNdx
init|=
literal|0
init|;
name|parameterNdx
operator|<
name|parameters
operator|.
name|length
condition|;
name|parameterNdx
operator|++
operator|,
name|arrayNdx
operator|++
control|)
block|{
name|String
index|[]
name|parts
init|=
name|parameters
index|[
name|parameterNdx
index|]
decl_stmt|;
name|Array
operator|.
name|set
argument_list|(
name|array
argument_list|,
name|arrayNdx
argument_list|,
name|resolver
operator|.
name|resolveClass
argument_list|(
name|parts
index|[
literal|0
index|]
argument_list|)
operator|.
name|getConstructor
argument_list|(
name|String
operator|.
name|class
argument_list|)
operator|.
name|newInstance
argument_list|(
name|parts
index|[
literal|1
index|]
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|methodPrms
index|[
name|methodPrmNdx
index|]
operator|=
name|array
expr_stmt|;
block|}
else|else
block|{
name|String
index|[]
name|parts
init|=
name|parameters
index|[
name|parameterNdx
index|]
decl_stmt|;
name|methodPrms
index|[
name|methodPrmNdx
index|]
operator|=
name|resolver
operator|.
name|resolveClass
argument_list|(
name|parts
index|[
literal|0
index|]
argument_list|)
operator|.
name|getConstructor
argument_list|(
name|String
operator|.
name|class
argument_list|)
operator|.
name|newInstance
argument_list|(
name|parts
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|method
operator|.
name|invoke
argument_list|(
name|customObj
argument_list|,
name|methodPrms
argument_list|)
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
comment|// The converter parameter is stored in a thread local variable, so
comment|// we need to parse the parameter on each invocation
comment|// ex: custom-converter-param="org.example.MyMapping,map"
comment|// className = org.example.MyMapping
comment|// operation = map
name|String
index|[]
name|prms
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
name|prms
index|[
literal|0
index|]
decl_stmt|;
name|String
name|operation
init|=
name|prms
operator|.
name|length
operator|>
literal|1
condition|?
name|prms
index|[
literal|1
index|]
else|:
literal|null
decl_stmt|;
comment|// now attempt to process any additional parameters passed along
comment|// ex: custom-converter-param="org.example.MyMapping,substring,java.lang.Integer=3,java.lang.Integer=10"
comment|// className = org.example.MyMapping
comment|// operation = substring
comment|// parameters = ["java.lang.Integer=3","java.lang.Integer=10"]
name|String
index|[]
index|[]
name|prmTypesAndValues
decl_stmt|;
if|if
condition|(
name|prms
operator|.
name|length
operator|>
literal|2
condition|)
block|{
comment|// Break parameters down into types and values
name|prmTypesAndValues
operator|=
operator|new
name|String
index|[
name|prms
operator|.
name|length
operator|-
literal|2
index|]
index|[
literal|2
index|]
expr_stmt|;
for|for
control|(
name|int
name|ndx
init|=
literal|0
init|;
name|ndx
operator|<
name|prmTypesAndValues
operator|.
name|length
condition|;
name|ndx
operator|++
control|)
block|{
name|String
name|prm
init|=
name|prms
index|[
name|ndx
operator|+
literal|2
index|]
decl_stmt|;
name|String
index|[]
name|parts
init|=
name|prm
operator|.
name|split
argument_list|(
literal|"="
argument_list|)
decl_stmt|;
if|if
condition|(
name|parts
operator|.
name|length
operator|!=
literal|2
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Value missing for parameter "
operator|+
name|prm
argument_list|)
throw|;
block|}
name|prmTypesAndValues
index|[
name|ndx
index|]
index|[
literal|0
index|]
operator|=
name|parts
index|[
literal|0
index|]
expr_stmt|;
name|prmTypesAndValues
index|[
name|ndx
index|]
index|[
literal|1
index|]
operator|=
name|parts
index|[
literal|1
index|]
expr_stmt|;
block|}
block|}
else|else
block|{
name|prmTypesAndValues
operator|=
literal|null
expr_stmt|;
block|}
name|Object
name|customObj
decl_stmt|;
name|Method
name|method
init|=
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
name|customObj
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
operator|&&
name|prmTypesAndValues
operator|!=
literal|null
condition|)
block|{
name|method
operator|=
name|selectMethod
argument_list|(
name|customClass
argument_list|,
name|operation
argument_list|,
name|source
argument_list|,
name|prmTypesAndValues
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|operation
operator|!=
literal|null
condition|)
block|{
name|method
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
name|method
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
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Failed to load custom function"
argument_list|,
name|e
argument_list|)
throw|;
block|}
comment|// Verify that we found a matching method
if|if
condition|(
name|method
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"No eligible custom function methods in "
operator|+
name|className
argument_list|)
throw|;
block|}
comment|// Invoke the custom mapping method
try|try
block|{
if|if
condition|(
name|prmTypesAndValues
operator|!=
literal|null
condition|)
block|{
return|return
name|invokeFunction
argument_list|(
name|method
argument_list|,
name|customObj
argument_list|,
name|source
argument_list|,
name|prmTypesAndValues
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|method
operator|.
name|invoke
argument_list|(
name|customObj
argument_list|,
name|source
argument_list|)
return|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Error while invoking custom function"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|parametersMatchParameterList (Class<?>[] prmTypes, String[][] parameters)
specifier|private
name|boolean
name|parametersMatchParameterList
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
index|[]
name|prmTypes
parameter_list|,
name|String
index|[]
index|[]
name|parameters
parameter_list|)
block|{
name|int
name|ndx
init|=
literal|0
decl_stmt|;
while|while
condition|(
name|ndx
operator|<
name|prmTypes
operator|.
name|length
condition|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|prmType
init|=
name|prmTypes
index|[
name|ndx
index|]
decl_stmt|;
if|if
condition|(
name|ndx
operator|>=
name|parameters
operator|.
name|length
condition|)
block|{
return|return
name|ndx
operator|==
name|prmTypes
operator|.
name|length
operator|-
literal|1
operator|&&
name|prmType
operator|.
name|isArray
argument_list|()
return|;
block|}
if|if
condition|(
name|ndx
operator|==
name|prmTypes
operator|.
name|length
operator|-
literal|1
operator|&&
name|prmType
operator|.
name|isArray
argument_list|()
condition|)
block|{
comment|// Assume this only occurs for functions with var args
name|Class
argument_list|<
name|?
argument_list|>
name|varArgClass
init|=
name|prmType
operator|.
name|getComponentType
argument_list|()
decl_stmt|;
while|while
condition|(
name|ndx
operator|<
name|parameters
operator|.
name|length
condition|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|prmClass
init|=
name|resolver
operator|.
name|resolveClass
argument_list|(
name|parameters
index|[
name|ndx
index|]
index|[
literal|0
index|]
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|varArgClass
operator|.
name|isAssignableFrom
argument_list|(
name|prmClass
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|ndx
operator|++
expr_stmt|;
block|}
block|}
else|else
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|prmClass
init|=
name|resolver
operator|.
name|resolveClass
argument_list|(
name|parameters
index|[
name|ndx
index|]
index|[
literal|0
index|]
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|prmTypes
index|[
name|ndx
index|]
operator|.
name|isAssignableFrom
argument_list|(
name|prmClass
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
name|ndx
operator|++
expr_stmt|;
block|}
return|return
literal|true
return|;
block|}
DECL|method|selectMethod (Class<?> customClass, Object source)
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
name|source
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
name|source
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
comment|// Assumes source is a separate parameter in method even if it has var args and that there are no
comment|// ambiguous calls based upon number and types of parameters
DECL|method|selectMethod (Class<?> customClass, String operation, Object source, String[][] parameters)
specifier|private
name|Method
name|selectMethod
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|customClass
parameter_list|,
name|String
name|operation
parameter_list|,
name|Object
name|source
parameter_list|,
name|String
index|[]
index|[]
name|parameters
parameter_list|)
block|{
comment|// Create list of potential methods
name|List
argument_list|<
name|Method
argument_list|>
name|methods
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Method
name|method
range|:
name|customClass
operator|.
name|getDeclaredMethods
argument_list|()
control|)
block|{
name|methods
operator|.
name|add
argument_list|(
name|method
argument_list|)
expr_stmt|;
block|}
comment|// Remove methods that are not applicable
for|for
control|(
name|Iterator
argument_list|<
name|Method
argument_list|>
name|iter
init|=
name|methods
operator|.
name|iterator
argument_list|()
init|;
name|iter
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|Method
name|method
init|=
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
index|[]
name|prmTypes
init|=
name|method
operator|.
name|getParameterTypes
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|method
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|operation
argument_list|)
operator|||
name|method
operator|.
name|getReturnType
argument_list|()
operator|==
literal|null
operator|||
operator|!
name|prmTypes
index|[
literal|0
index|]
operator|.
name|isAssignableFrom
argument_list|(
name|source
operator|.
name|getClass
argument_list|()
argument_list|)
condition|)
block|{
name|iter
operator|.
name|remove
argument_list|()
expr_stmt|;
continue|continue;
block|}
name|prmTypes
operator|=
name|Arrays
operator|.
name|copyOfRange
argument_list|(
name|prmTypes
argument_list|,
literal|1
argument_list|,
name|prmTypes
operator|.
name|length
argument_list|)
expr_stmt|;
comment|// Remove source from type list
if|if
condition|(
operator|!
name|method
operator|.
name|isVarArgs
argument_list|()
operator|&&
name|prmTypes
operator|.
name|length
operator|!=
name|parameters
operator|.
name|length
condition|)
block|{
name|iter
operator|.
name|remove
argument_list|()
expr_stmt|;
continue|continue;
block|}
if|if
condition|(
operator|!
name|parametersMatchParameterList
argument_list|(
name|prmTypes
argument_list|,
name|parameters
argument_list|)
condition|)
block|{
name|iter
operator|.
name|remove
argument_list|()
expr_stmt|;
continue|continue;
block|}
block|}
comment|// If more than one method is applicable, return the method whose prm list exactly matches the parameters
comment|// if possible
if|if
condition|(
name|methods
operator|.
name|size
argument_list|()
operator|>
literal|1
condition|)
block|{
for|for
control|(
name|Method
name|method
range|:
name|methods
control|)
block|{
if|if
condition|(
operator|!
name|method
operator|.
name|isVarArgs
argument_list|()
condition|)
block|{
return|return
name|method
return|;
block|}
block|}
block|}
return|return
name|methods
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|?
name|methods
operator|.
name|get
argument_list|(
literal|0
argument_list|)
else|:
literal|null
return|;
block|}
block|}
end_class

end_unit

