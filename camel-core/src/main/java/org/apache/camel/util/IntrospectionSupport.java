begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.util
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
package|;
end_package

begin_import
import|import
name|java
operator|.
name|beans
operator|.
name|PropertyEditor
import|;
end_import

begin_import
import|import
name|java
operator|.
name|beans
operator|.
name|PropertyEditorManager
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
name|Field
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
name|InvocationTargetException
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
name|lang
operator|.
name|reflect
operator|.
name|Modifier
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URISyntaxException
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
name|HashMap
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
name|LinkedHashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedHashSet
import|;
end_import

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
name|java
operator|.
name|util
operator|.
name|Set
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
name|NoTypeConversionAvailableException
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
name|TypeConverter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
import|;
end_import

begin_comment
comment|/**  * Helper for introspections of beans.  */
end_comment

begin_class
DECL|class|IntrospectionSupport
specifier|public
specifier|final
class|class
name|IntrospectionSupport
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|IntrospectionSupport
operator|.
name|class
argument_list|)
decl_stmt|;
comment|/**      * Utility classes should not have a public constructor.      */
DECL|method|IntrospectionSupport ()
specifier|private
name|IntrospectionSupport
parameter_list|()
block|{     }
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|getProperties (Object target, Map properties, String optionPrefix)
specifier|public
specifier|static
name|boolean
name|getProperties
parameter_list|(
name|Object
name|target
parameter_list|,
name|Map
name|properties
parameter_list|,
name|String
name|optionPrefix
parameter_list|)
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|target
argument_list|,
literal|"target"
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|properties
argument_list|,
literal|"properties"
argument_list|)
expr_stmt|;
name|boolean
name|rc
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|optionPrefix
operator|==
literal|null
condition|)
block|{
name|optionPrefix
operator|=
literal|""
expr_stmt|;
block|}
name|Class
name|clazz
init|=
name|target
operator|.
name|getClass
argument_list|()
decl_stmt|;
name|Method
index|[]
name|methods
init|=
name|clazz
operator|.
name|getMethods
argument_list|()
decl_stmt|;
for|for
control|(
name|Method
name|method
range|:
name|methods
control|)
block|{
name|String
name|name
init|=
name|method
operator|.
name|getName
argument_list|()
decl_stmt|;
name|Class
name|type
init|=
name|method
operator|.
name|getReturnType
argument_list|()
decl_stmt|;
name|Class
name|params
index|[]
init|=
name|method
operator|.
name|getParameterTypes
argument_list|()
decl_stmt|;
if|if
condition|(
name|name
operator|.
name|startsWith
argument_list|(
literal|"get"
argument_list|)
operator|&&
name|params
operator|.
name|length
operator|==
literal|0
operator|&&
name|type
operator|!=
literal|null
operator|&&
name|isSettableType
argument_list|(
name|type
argument_list|)
condition|)
block|{
try|try
block|{
name|Object
name|value
init|=
name|method
operator|.
name|invoke
argument_list|(
name|target
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|==
literal|null
condition|)
block|{
continue|continue;
block|}
name|String
name|strValue
init|=
name|convertToString
argument_list|(
name|value
argument_list|,
name|type
argument_list|)
decl_stmt|;
if|if
condition|(
name|strValue
operator|==
literal|null
condition|)
block|{
continue|continue;
block|}
name|name
operator|=
name|name
operator|.
name|substring
argument_list|(
literal|3
argument_list|,
literal|4
argument_list|)
operator|.
name|toLowerCase
argument_list|()
operator|+
name|name
operator|.
name|substring
argument_list|(
literal|4
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
name|optionPrefix
operator|+
name|name
argument_list|,
name|strValue
argument_list|)
expr_stmt|;
name|rc
operator|=
literal|true
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ignore
parameter_list|)
block|{
comment|// ignore
block|}
block|}
block|}
return|return
name|rc
return|;
block|}
DECL|method|hasProperties (Map properties, String optionPrefix)
specifier|public
specifier|static
name|boolean
name|hasProperties
parameter_list|(
name|Map
name|properties
parameter_list|,
name|String
name|optionPrefix
parameter_list|)
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|properties
argument_list|,
literal|"properties"
argument_list|)
expr_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|optionPrefix
argument_list|)
condition|)
block|{
for|for
control|(
name|Object
name|o
range|:
name|properties
operator|.
name|keySet
argument_list|()
control|)
block|{
name|String
name|name
init|=
operator|(
name|String
operator|)
name|o
decl_stmt|;
if|if
condition|(
name|name
operator|.
name|startsWith
argument_list|(
name|optionPrefix
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
comment|// no parameters with this prefix
return|return
literal|false
return|;
block|}
else|else
block|{
return|return
operator|!
name|properties
operator|.
name|isEmpty
argument_list|()
return|;
block|}
block|}
DECL|method|getProperty (Object target, String property)
specifier|public
specifier|static
name|Object
name|getProperty
parameter_list|(
name|Object
name|target
parameter_list|,
name|String
name|property
parameter_list|)
throws|throws
name|NoSuchMethodException
throws|,
name|IllegalAccessException
throws|,
name|InvocationTargetException
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|target
argument_list|,
literal|"target"
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|property
argument_list|,
literal|"property"
argument_list|)
expr_stmt|;
name|property
operator|=
name|property
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
literal|1
argument_list|)
operator|.
name|toUpperCase
argument_list|()
operator|+
name|property
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|Class
name|clazz
init|=
name|target
operator|.
name|getClass
argument_list|()
decl_stmt|;
name|Method
name|method
init|=
name|getPropertyGetter
argument_list|(
name|clazz
argument_list|,
name|property
argument_list|)
decl_stmt|;
return|return
name|method
operator|.
name|invoke
argument_list|(
name|target
argument_list|)
return|;
block|}
DECL|method|getPropertyGetter (Class type, String propertyName)
specifier|public
specifier|static
name|Method
name|getPropertyGetter
parameter_list|(
name|Class
name|type
parameter_list|,
name|String
name|propertyName
parameter_list|)
throws|throws
name|NoSuchMethodException
block|{
return|return
name|type
operator|.
name|getMethod
argument_list|(
literal|"get"
operator|+
name|ObjectHelper
operator|.
name|capitalize
argument_list|(
name|propertyName
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|setProperties (Object target, Map properties, String optionPrefix)
specifier|public
specifier|static
name|boolean
name|setProperties
parameter_list|(
name|Object
name|target
parameter_list|,
name|Map
name|properties
parameter_list|,
name|String
name|optionPrefix
parameter_list|)
throws|throws
name|Exception
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|target
argument_list|,
literal|"target"
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|properties
argument_list|,
literal|"properties"
argument_list|)
expr_stmt|;
name|boolean
name|rc
init|=
literal|false
decl_stmt|;
for|for
control|(
name|Iterator
argument_list|<
name|Map
operator|.
name|Entry
argument_list|>
name|it
init|=
name|properties
operator|.
name|entrySet
argument_list|()
operator|.
name|iterator
argument_list|()
init|;
name|it
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|Map
operator|.
name|Entry
name|entry
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|String
name|name
init|=
name|entry
operator|.
name|getKey
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
if|if
condition|(
name|name
operator|.
name|startsWith
argument_list|(
name|optionPrefix
argument_list|)
condition|)
block|{
name|Object
name|value
init|=
name|properties
operator|.
name|get
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|name
operator|=
name|name
operator|.
name|substring
argument_list|(
name|optionPrefix
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|setProperty
argument_list|(
name|target
argument_list|,
name|name
argument_list|,
name|value
argument_list|)
condition|)
block|{
name|it
operator|.
name|remove
argument_list|()
expr_stmt|;
name|rc
operator|=
literal|true
expr_stmt|;
block|}
block|}
block|}
return|return
name|rc
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|extractProperties (Map properties, String optionPrefix)
specifier|public
specifier|static
name|Map
name|extractProperties
parameter_list|(
name|Map
name|properties
parameter_list|,
name|String
name|optionPrefix
parameter_list|)
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|properties
argument_list|,
literal|"properties"
argument_list|)
expr_stmt|;
name|HashMap
name|rc
init|=
operator|new
name|LinkedHashMap
argument_list|(
name|properties
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Iterator
argument_list|<
name|Map
operator|.
name|Entry
argument_list|>
name|it
init|=
name|properties
operator|.
name|entrySet
argument_list|()
operator|.
name|iterator
argument_list|()
init|;
name|it
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|Map
operator|.
name|Entry
name|entry
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|String
name|name
init|=
name|entry
operator|.
name|getKey
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
if|if
condition|(
name|name
operator|.
name|startsWith
argument_list|(
name|optionPrefix
argument_list|)
condition|)
block|{
name|Object
name|value
init|=
name|properties
operator|.
name|get
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|name
operator|=
name|name
operator|.
name|substring
argument_list|(
name|optionPrefix
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
name|rc
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
name|it
operator|.
name|remove
argument_list|()
expr_stmt|;
block|}
block|}
return|return
name|rc
return|;
block|}
DECL|method|setProperties (TypeConverter typeConverter, Object target, Map properties)
specifier|public
specifier|static
name|boolean
name|setProperties
parameter_list|(
name|TypeConverter
name|typeConverter
parameter_list|,
name|Object
name|target
parameter_list|,
name|Map
name|properties
parameter_list|)
throws|throws
name|Exception
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|target
argument_list|,
literal|"target"
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|properties
argument_list|,
literal|"properties"
argument_list|)
expr_stmt|;
name|boolean
name|rc
init|=
literal|false
decl_stmt|;
for|for
control|(
name|Iterator
name|iter
init|=
name|properties
operator|.
name|entrySet
argument_list|()
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
name|Map
operator|.
name|Entry
name|entry
init|=
operator|(
name|Map
operator|.
name|Entry
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|setProperty
argument_list|(
name|typeConverter
argument_list|,
name|target
argument_list|,
operator|(
name|String
operator|)
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
condition|)
block|{
name|iter
operator|.
name|remove
argument_list|()
expr_stmt|;
name|rc
operator|=
literal|true
expr_stmt|;
block|}
block|}
return|return
name|rc
return|;
block|}
DECL|method|setProperties (Object target, Map props)
specifier|public
specifier|static
name|boolean
name|setProperties
parameter_list|(
name|Object
name|target
parameter_list|,
name|Map
name|props
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|setProperties
argument_list|(
literal|null
argument_list|,
name|target
argument_list|,
name|props
argument_list|)
return|;
block|}
DECL|method|setProperty (TypeConverter typeConverter, Object target, String name, Object value)
specifier|public
specifier|static
name|boolean
name|setProperty
parameter_list|(
name|TypeConverter
name|typeConverter
parameter_list|,
name|Object
name|target
parameter_list|,
name|String
name|name
parameter_list|,
name|Object
name|value
parameter_list|)
throws|throws
name|Exception
block|{
try|try
block|{
name|Class
name|clazz
init|=
name|target
operator|.
name|getClass
argument_list|()
decl_stmt|;
comment|// find candidates of setter methods as there can be overloaded setters
name|Set
argument_list|<
name|Method
argument_list|>
name|setters
init|=
name|findSetterMethods
argument_list|(
name|typeConverter
argument_list|,
name|clazz
argument_list|,
name|name
argument_list|,
name|value
argument_list|)
decl_stmt|;
if|if
condition|(
name|setters
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
comment|// loop and execute the best setter method
name|Exception
name|typeConvertionFailed
init|=
literal|null
decl_stmt|;
for|for
control|(
name|Method
name|setter
range|:
name|setters
control|)
block|{
comment|// If the type is null or it matches the needed type, just use the value directly
if|if
condition|(
name|value
operator|==
literal|null
operator|||
name|setter
operator|.
name|getParameterTypes
argument_list|()
index|[
literal|0
index|]
operator|.
name|isAssignableFrom
argument_list|(
name|value
operator|.
name|getClass
argument_list|()
argument_list|)
condition|)
block|{
name|setter
operator|.
name|invoke
argument_list|(
name|target
argument_list|,
name|value
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
else|else
block|{
comment|// We need to convert it
try|try
block|{
comment|// ignore exceptions as there could be another setter method where we could type convert successfully
name|Object
name|convertedValue
init|=
name|convert
argument_list|(
name|typeConverter
argument_list|,
name|setter
operator|.
name|getParameterTypes
argument_list|()
index|[
literal|0
index|]
argument_list|,
name|value
argument_list|)
decl_stmt|;
name|setter
operator|.
name|invoke
argument_list|(
name|target
argument_list|,
name|convertedValue
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
catch|catch
parameter_list|(
name|NoTypeConversionAvailableException
name|e
parameter_list|)
block|{
name|typeConvertionFailed
operator|=
name|e
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
name|typeConvertionFailed
operator|=
name|e
expr_stmt|;
block|}
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Setter \""
operator|+
name|setter
operator|+
literal|"\" with parameter type \""
operator|+
name|setter
operator|.
name|getParameterTypes
argument_list|()
index|[
literal|0
index|]
operator|+
literal|"\" could not be used for type conversions of "
operator|+
name|value
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|// we did not find a setter method to use, and if we did try to use a type converter then throw
comment|// this kind of exception as the caused by will hint this error
if|if
condition|(
name|typeConvertionFailed
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Could not find a suitable setter for property: "
operator|+
name|name
operator|+
literal|" as there isn't a setter method with same type: "
operator|+
name|value
operator|.
name|getClass
argument_list|()
operator|.
name|getCanonicalName
argument_list|()
operator|+
literal|" nor type conversion possible: "
operator|+
name|typeConvertionFailed
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
else|else
block|{
return|return
literal|false
return|;
block|}
block|}
catch|catch
parameter_list|(
name|InvocationTargetException
name|e
parameter_list|)
block|{
comment|// lets unwrap the exception
name|Throwable
name|throwable
init|=
name|e
operator|.
name|getCause
argument_list|()
decl_stmt|;
if|if
condition|(
name|throwable
operator|instanceof
name|Exception
condition|)
block|{
name|Exception
name|exception
init|=
operator|(
name|Exception
operator|)
name|throwable
decl_stmt|;
throw|throw
name|exception
throw|;
block|}
else|else
block|{
name|Error
name|error
init|=
operator|(
name|Error
operator|)
name|throwable
decl_stmt|;
throw|throw
name|error
throw|;
block|}
block|}
block|}
DECL|method|setProperty (Object target, String name, Object value)
specifier|public
specifier|static
name|boolean
name|setProperty
parameter_list|(
name|Object
name|target
parameter_list|,
name|String
name|name
parameter_list|,
name|Object
name|value
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|setProperty
argument_list|(
literal|null
argument_list|,
name|target
argument_list|,
name|name
argument_list|,
name|value
argument_list|)
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|convert (TypeConverter typeConverter, Class type, Object value)
specifier|private
specifier|static
name|Object
name|convert
parameter_list|(
name|TypeConverter
name|typeConverter
parameter_list|,
name|Class
name|type
parameter_list|,
name|Object
name|value
parameter_list|)
throws|throws
name|URISyntaxException
throws|,
name|NoTypeConversionAvailableException
block|{
if|if
condition|(
name|typeConverter
operator|!=
literal|null
condition|)
block|{
return|return
name|typeConverter
operator|.
name|mandatoryConvertTo
argument_list|(
name|type
argument_list|,
name|value
argument_list|)
return|;
block|}
name|PropertyEditor
name|editor
init|=
name|PropertyEditorManager
operator|.
name|findEditor
argument_list|(
name|type
argument_list|)
decl_stmt|;
if|if
condition|(
name|editor
operator|!=
literal|null
condition|)
block|{
name|editor
operator|.
name|setAsText
argument_list|(
name|value
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|editor
operator|.
name|getValue
argument_list|()
return|;
block|}
if|if
condition|(
name|type
operator|==
name|URI
operator|.
name|class
condition|)
block|{
return|return
operator|new
name|URI
argument_list|(
name|value
operator|.
name|toString
argument_list|()
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
DECL|method|convertToString (Object value, Class type)
specifier|private
specifier|static
name|String
name|convertToString
parameter_list|(
name|Object
name|value
parameter_list|,
name|Class
name|type
parameter_list|)
throws|throws
name|URISyntaxException
block|{
name|PropertyEditor
name|editor
init|=
name|PropertyEditorManager
operator|.
name|findEditor
argument_list|(
name|type
argument_list|)
decl_stmt|;
if|if
condition|(
name|editor
operator|!=
literal|null
condition|)
block|{
name|editor
operator|.
name|setValue
argument_list|(
name|value
argument_list|)
expr_stmt|;
return|return
name|editor
operator|.
name|getAsText
argument_list|()
return|;
block|}
if|if
condition|(
name|type
operator|==
name|URI
operator|.
name|class
condition|)
block|{
return|return
name|value
operator|.
name|toString
argument_list|()
return|;
block|}
return|return
literal|null
return|;
block|}
DECL|method|findSetterMethods (TypeConverter typeConverter, Class clazz, String name, Object value)
specifier|private
specifier|static
name|Set
argument_list|<
name|Method
argument_list|>
name|findSetterMethods
parameter_list|(
name|TypeConverter
name|typeConverter
parameter_list|,
name|Class
name|clazz
parameter_list|,
name|String
name|name
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
name|Set
argument_list|<
name|Method
argument_list|>
name|candidates
init|=
operator|new
name|LinkedHashSet
argument_list|<
name|Method
argument_list|>
argument_list|()
decl_stmt|;
comment|// Build the method name.
name|name
operator|=
literal|"set"
operator|+
name|ObjectHelper
operator|.
name|capitalize
argument_list|(
name|name
argument_list|)
expr_stmt|;
while|while
condition|(
name|clazz
operator|!=
name|Object
operator|.
name|class
condition|)
block|{
comment|// Since Object.class.isInstance all the objects,
comment|// here we just make sure it will be add to the bottom of the set.
name|Method
name|objectSetMethod
init|=
literal|null
decl_stmt|;
name|Method
index|[]
name|methods
init|=
name|clazz
operator|.
name|getMethods
argument_list|()
decl_stmt|;
for|for
control|(
name|Method
name|method
range|:
name|methods
control|)
block|{
name|Class
name|params
index|[]
init|=
name|method
operator|.
name|getParameterTypes
argument_list|()
decl_stmt|;
if|if
condition|(
name|method
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|name
argument_list|)
operator|&&
name|params
operator|.
name|length
operator|==
literal|1
condition|)
block|{
name|Class
name|paramType
init|=
name|params
index|[
literal|0
index|]
decl_stmt|;
if|if
condition|(
name|paramType
operator|.
name|equals
argument_list|(
name|Object
operator|.
name|class
argument_list|)
condition|)
block|{
name|objectSetMethod
operator|=
name|method
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|typeConverter
operator|!=
literal|null
operator|||
name|isSettableType
argument_list|(
name|paramType
argument_list|)
operator|||
name|paramType
operator|.
name|isInstance
argument_list|(
name|value
argument_list|)
condition|)
block|{
name|candidates
operator|.
name|add
argument_list|(
name|method
argument_list|)
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|objectSetMethod
operator|!=
literal|null
condition|)
block|{
name|candidates
operator|.
name|add
argument_list|(
name|objectSetMethod
argument_list|)
expr_stmt|;
block|}
name|clazz
operator|=
name|clazz
operator|.
name|getSuperclass
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|candidates
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|candidates
return|;
block|}
elseif|else
if|if
condition|(
name|candidates
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
comment|// only one
return|return
name|candidates
return|;
block|}
else|else
block|{
comment|// find the best match if possible
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Found "
operator|+
name|candidates
operator|.
name|size
argument_list|()
operator|+
literal|" suitable setter methods for setting "
operator|+
name|name
argument_list|)
expr_stmt|;
block|}
comment|// prefer to use the one with the same instance if any exists
for|for
control|(
name|Method
name|method
range|:
name|candidates
control|)
block|{
if|if
condition|(
name|method
operator|.
name|getParameterTypes
argument_list|()
index|[
literal|0
index|]
operator|.
name|isInstance
argument_list|(
name|value
argument_list|)
condition|)
block|{
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Method "
operator|+
name|method
operator|+
literal|" is the best candidate as it has parameter with same instance type"
argument_list|)
expr_stmt|;
block|}
comment|// retain only this method in the answer
name|candidates
operator|.
name|clear
argument_list|()
expr_stmt|;
name|candidates
operator|.
name|add
argument_list|(
name|method
argument_list|)
expr_stmt|;
return|return
name|candidates
return|;
block|}
block|}
comment|// fallback to return what we have found as candidates so far
return|return
name|candidates
return|;
block|}
block|}
DECL|method|isSettableType (Class clazz)
specifier|private
specifier|static
name|boolean
name|isSettableType
parameter_list|(
name|Class
name|clazz
parameter_list|)
block|{
if|if
condition|(
name|PropertyEditorManager
operator|.
name|findEditor
argument_list|(
name|clazz
argument_list|)
operator|!=
literal|null
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
name|clazz
operator|==
name|URI
operator|.
name|class
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
name|clazz
operator|==
name|Boolean
operator|.
name|class
condition|)
block|{
return|return
literal|true
return|;
block|}
return|return
literal|false
return|;
block|}
DECL|method|toString (Object target)
specifier|public
specifier|static
name|String
name|toString
parameter_list|(
name|Object
name|target
parameter_list|)
block|{
return|return
name|toString
argument_list|(
name|target
argument_list|,
name|Object
operator|.
name|class
argument_list|)
return|;
block|}
DECL|method|toString (Object target, Class stopClass)
specifier|public
specifier|static
name|String
name|toString
parameter_list|(
name|Object
name|target
parameter_list|,
name|Class
name|stopClass
parameter_list|)
block|{
name|LinkedHashMap
name|map
init|=
operator|new
name|LinkedHashMap
argument_list|()
decl_stmt|;
name|addFields
argument_list|(
name|target
argument_list|,
name|target
operator|.
name|getClass
argument_list|()
argument_list|,
name|stopClass
argument_list|,
name|map
argument_list|)
expr_stmt|;
name|StringBuffer
name|buffer
init|=
operator|new
name|StringBuffer
argument_list|(
name|simpleName
argument_list|(
name|target
operator|.
name|getClass
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|buffer
operator|.
name|append
argument_list|(
literal|" {"
argument_list|)
expr_stmt|;
name|Set
name|entrySet
init|=
name|map
operator|.
name|entrySet
argument_list|()
decl_stmt|;
name|boolean
name|first
init|=
literal|true
decl_stmt|;
for|for
control|(
name|Iterator
name|iter
init|=
name|entrySet
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
name|Map
operator|.
name|Entry
name|entry
init|=
operator|(
name|Map
operator|.
name|Entry
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|first
condition|)
block|{
name|first
operator|=
literal|false
expr_stmt|;
block|}
else|else
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|", "
argument_list|)
expr_stmt|;
block|}
name|buffer
operator|.
name|append
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
literal|" = "
argument_list|)
expr_stmt|;
name|appendToString
argument_list|(
name|buffer
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|buffer
operator|.
name|append
argument_list|(
literal|"}"
argument_list|)
expr_stmt|;
return|return
name|buffer
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|method|appendToString (StringBuffer buffer, Object value)
specifier|protected
specifier|static
name|void
name|appendToString
parameter_list|(
name|StringBuffer
name|buffer
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
name|buffer
operator|.
name|append
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
DECL|method|simpleName (Class clazz)
specifier|public
specifier|static
name|String
name|simpleName
parameter_list|(
name|Class
name|clazz
parameter_list|)
block|{
name|String
name|name
init|=
name|clazz
operator|.
name|getName
argument_list|()
decl_stmt|;
name|int
name|p
init|=
name|name
operator|.
name|lastIndexOf
argument_list|(
literal|'.'
argument_list|)
decl_stmt|;
if|if
condition|(
name|p
operator|>=
literal|0
condition|)
block|{
name|name
operator|=
name|name
operator|.
name|substring
argument_list|(
name|p
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
return|return
name|name
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|addFields (Object target, Class startClass, Class stopClass, LinkedHashMap map)
specifier|private
specifier|static
name|void
name|addFields
parameter_list|(
name|Object
name|target
parameter_list|,
name|Class
name|startClass
parameter_list|,
name|Class
name|stopClass
parameter_list|,
name|LinkedHashMap
name|map
parameter_list|)
block|{
if|if
condition|(
name|startClass
operator|!=
name|stopClass
condition|)
block|{
name|addFields
argument_list|(
name|target
argument_list|,
name|startClass
operator|.
name|getSuperclass
argument_list|()
argument_list|,
name|stopClass
argument_list|,
name|map
argument_list|)
expr_stmt|;
block|}
name|Field
index|[]
name|fields
init|=
name|startClass
operator|.
name|getDeclaredFields
argument_list|()
decl_stmt|;
for|for
control|(
name|Field
name|field
range|:
name|fields
control|)
block|{
if|if
condition|(
name|Modifier
operator|.
name|isStatic
argument_list|(
name|field
operator|.
name|getModifiers
argument_list|()
argument_list|)
operator|||
name|Modifier
operator|.
name|isTransient
argument_list|(
name|field
operator|.
name|getModifiers
argument_list|()
argument_list|)
operator|||
name|Modifier
operator|.
name|isPrivate
argument_list|(
name|field
operator|.
name|getModifiers
argument_list|()
argument_list|)
condition|)
block|{
continue|continue;
block|}
try|try
block|{
name|field
operator|.
name|setAccessible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|Object
name|o
init|=
name|field
operator|.
name|get
argument_list|(
name|target
argument_list|)
decl_stmt|;
if|if
condition|(
name|o
operator|!=
literal|null
operator|&&
name|o
operator|.
name|getClass
argument_list|()
operator|.
name|isArray
argument_list|()
condition|)
block|{
try|try
block|{
name|o
operator|=
name|Arrays
operator|.
name|asList
argument_list|(
operator|(
name|Object
index|[]
operator|)
name|o
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
comment|// ignore
block|}
block|}
name|map
operator|.
name|put
argument_list|(
name|field
operator|.
name|getName
argument_list|()
argument_list|,
name|o
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
throw|throw
name|ObjectHelper
operator|.
name|wrapRuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
block|}
block|}
end_class

end_unit

