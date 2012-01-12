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
name|Proxy
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
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Locale
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
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Pattern
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
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
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
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|IntrospectionSupport
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|GETTER_PATTERN
specifier|private
specifier|static
specifier|final
name|Pattern
name|GETTER_PATTERN
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"(get|is)[A-Z].*"
argument_list|)
decl_stmt|;
DECL|field|SETTER_PATTERN
specifier|private
specifier|static
specifier|final
name|Pattern
name|SETTER_PATTERN
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"set[A-Z].*"
argument_list|)
decl_stmt|;
DECL|field|EXCLUDED_METHODS
specifier|private
specifier|static
specifier|final
name|List
argument_list|<
name|Method
argument_list|>
name|EXCLUDED_METHODS
init|=
operator|new
name|ArrayList
argument_list|<
name|Method
argument_list|>
argument_list|()
decl_stmt|;
static|static
block|{
comment|// exclude all java.lang.Object methods as we dont want to invoke them
name|EXCLUDED_METHODS
operator|.
name|addAll
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|Object
operator|.
name|class
operator|.
name|getMethods
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
comment|// exclude all java.lang.reflect.Proxy methods as we dont want to invoke them
name|EXCLUDED_METHODS
operator|.
name|addAll
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|Proxy
operator|.
name|class
operator|.
name|getMethods
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Utility classes should not have a public constructor.      */
DECL|method|IntrospectionSupport ()
specifier|private
name|IntrospectionSupport
parameter_list|()
block|{     }
DECL|method|isGetter (Method method)
specifier|public
specifier|static
name|boolean
name|isGetter
parameter_list|(
name|Method
name|method
parameter_list|)
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
argument_list|<
name|?
argument_list|>
name|type
init|=
name|method
operator|.
name|getReturnType
argument_list|()
decl_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
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
operator|!
name|GETTER_PATTERN
operator|.
name|matcher
argument_list|(
name|name
argument_list|)
operator|.
name|matches
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
comment|// special for isXXX boolean
if|if
condition|(
name|name
operator|.
name|startsWith
argument_list|(
literal|"is"
argument_list|)
condition|)
block|{
return|return
name|params
operator|.
name|length
operator|==
literal|0
operator|&&
name|type
operator|.
name|getSimpleName
argument_list|()
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"boolean"
argument_list|)
return|;
block|}
return|return
name|params
operator|.
name|length
operator|==
literal|0
operator|&&
operator|!
name|type
operator|.
name|equals
argument_list|(
name|Void
operator|.
name|TYPE
argument_list|)
return|;
block|}
DECL|method|getGetterShorthandName (Method method)
specifier|public
specifier|static
name|String
name|getGetterShorthandName
parameter_list|(
name|Method
name|method
parameter_list|)
block|{
if|if
condition|(
operator|!
name|isGetter
argument_list|(
name|method
argument_list|)
condition|)
block|{
return|return
name|method
operator|.
name|getName
argument_list|()
return|;
block|}
name|String
name|name
init|=
name|method
operator|.
name|getName
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
condition|)
block|{
name|name
operator|=
name|name
operator|.
name|substring
argument_list|(
literal|3
argument_list|)
expr_stmt|;
name|name
operator|=
name|name
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
literal|1
argument_list|)
operator|.
name|toLowerCase
argument_list|()
operator|+
name|name
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|name
operator|.
name|startsWith
argument_list|(
literal|"is"
argument_list|)
condition|)
block|{
name|name
operator|=
name|name
operator|.
name|substring
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|name
operator|=
name|name
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
literal|1
argument_list|)
operator|.
name|toLowerCase
argument_list|()
operator|+
name|name
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
return|return
name|name
return|;
block|}
DECL|method|getSetterShorthandName (Method method)
specifier|public
specifier|static
name|String
name|getSetterShorthandName
parameter_list|(
name|Method
name|method
parameter_list|)
block|{
if|if
condition|(
operator|!
name|isSetter
argument_list|(
name|method
argument_list|)
condition|)
block|{
return|return
name|method
operator|.
name|getName
argument_list|()
return|;
block|}
name|String
name|name
init|=
name|method
operator|.
name|getName
argument_list|()
decl_stmt|;
if|if
condition|(
name|name
operator|.
name|startsWith
argument_list|(
literal|"set"
argument_list|)
condition|)
block|{
name|name
operator|=
name|name
operator|.
name|substring
argument_list|(
literal|3
argument_list|)
expr_stmt|;
name|name
operator|=
name|name
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
literal|1
argument_list|)
operator|.
name|toLowerCase
argument_list|()
operator|+
name|name
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
return|return
name|name
return|;
block|}
DECL|method|isSetter (Method method, boolean allowBuilderPattern)
specifier|public
specifier|static
name|boolean
name|isSetter
parameter_list|(
name|Method
name|method
parameter_list|,
name|boolean
name|allowBuilderPattern
parameter_list|)
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
argument_list|<
name|?
argument_list|>
name|type
init|=
name|method
operator|.
name|getReturnType
argument_list|()
decl_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
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
operator|!
name|SETTER_PATTERN
operator|.
name|matcher
argument_list|(
name|name
argument_list|)
operator|.
name|matches
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
name|params
operator|.
name|length
operator|==
literal|1
operator|&&
operator|(
name|type
operator|.
name|equals
argument_list|(
name|Void
operator|.
name|TYPE
argument_list|)
operator|||
operator|(
name|allowBuilderPattern
operator|&&
name|method
operator|.
name|getDeclaringClass
argument_list|()
operator|.
name|isAssignableFrom
argument_list|(
name|type
argument_list|)
operator|)
operator|)
return|;
block|}
DECL|method|isSetter (Method method)
specifier|public
specifier|static
name|boolean
name|isSetter
parameter_list|(
name|Method
name|method
parameter_list|)
block|{
return|return
name|isSetter
argument_list|(
name|method
argument_list|,
literal|false
argument_list|)
return|;
block|}
comment|/**      * Will inspect the target for properties.      *<p/>      * Notice a property must have both a getter/setter method to be included.      *      * @param target         the target bean      * @param properties     the map to fill in found properties      * @param optionPrefix   an optional prefix to append the property key      * @return<tt>true</tt> if any properties was found,<tt>false</tt> otherwise.      */
DECL|method|getProperties (Object target, Map<String, Object> properties, String optionPrefix)
specifier|public
specifier|static
name|boolean
name|getProperties
parameter_list|(
name|Object
name|target
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
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
argument_list|<
name|?
argument_list|>
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
if|if
condition|(
name|EXCLUDED_METHODS
operator|.
name|contains
argument_list|(
name|method
argument_list|)
condition|)
block|{
continue|continue;
block|}
try|try
block|{
comment|// must be properties which have setters
if|if
condition|(
name|isGetter
argument_list|(
name|method
argument_list|)
operator|&&
name|hasSetter
argument_list|(
name|target
argument_list|,
name|method
argument_list|)
condition|)
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
name|String
name|name
init|=
name|getGetterShorthandName
argument_list|(
name|method
argument_list|)
decl_stmt|;
name|properties
operator|.
name|put
argument_list|(
name|optionPrefix
operator|+
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
name|rc
operator|=
literal|true
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// ignore
block|}
block|}
return|return
name|rc
return|;
block|}
DECL|method|hasSetter (Object target, Method getter)
specifier|public
specifier|static
name|boolean
name|hasSetter
parameter_list|(
name|Object
name|target
parameter_list|,
name|Method
name|getter
parameter_list|)
block|{
name|String
name|name
init|=
name|getGetterShorthandName
argument_list|(
name|getter
argument_list|)
decl_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
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
if|if
condition|(
name|EXCLUDED_METHODS
operator|.
name|contains
argument_list|(
name|method
argument_list|)
condition|)
block|{
continue|continue;
block|}
if|if
condition|(
name|isSetter
argument_list|(
name|method
argument_list|)
condition|)
block|{
if|if
condition|(
name|name
operator|.
name|equals
argument_list|(
name|getSetterShorthandName
argument_list|(
name|method
argument_list|)
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
block|}
return|return
literal|false
return|;
block|}
DECL|method|hasProperties (Map<String, Object> properties, String optionPrefix)
specifier|public
specifier|static
name|boolean
name|hasProperties
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
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
argument_list|(
name|Locale
operator|.
name|ENGLISH
argument_list|)
operator|+
name|property
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
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
DECL|method|getPropertyGetter (Class<?> type, String propertyName)
specifier|public
specifier|static
name|Method
name|getPropertyGetter
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|,
name|String
name|propertyName
parameter_list|)
throws|throws
name|NoSuchMethodException
block|{
if|if
condition|(
name|isPropertyIsGetter
argument_list|(
name|type
argument_list|,
name|propertyName
argument_list|)
condition|)
block|{
return|return
name|type
operator|.
name|getMethod
argument_list|(
literal|"is"
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
else|else
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
block|}
DECL|method|getPropertySetter (Class<?> type, String propertyName)
specifier|public
specifier|static
name|Method
name|getPropertySetter
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|,
name|String
name|propertyName
parameter_list|)
throws|throws
name|NoSuchMethodException
block|{
name|String
name|name
init|=
literal|"set"
operator|+
name|ObjectHelper
operator|.
name|capitalize
argument_list|(
name|propertyName
argument_list|)
decl_stmt|;
for|for
control|(
name|Method
name|method
range|:
name|type
operator|.
name|getMethods
argument_list|()
control|)
block|{
if|if
condition|(
name|isSetter
argument_list|(
name|method
argument_list|)
operator|&&
name|method
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
return|return
name|method
return|;
block|}
block|}
throw|throw
operator|new
name|NoSuchMethodException
argument_list|(
name|type
operator|.
name|getCanonicalName
argument_list|()
operator|+
literal|"."
operator|+
name|name
argument_list|)
throw|;
block|}
DECL|method|isPropertyIsGetter (Class<?> type, String propertyName)
specifier|public
specifier|static
name|boolean
name|isPropertyIsGetter
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|,
name|String
name|propertyName
parameter_list|)
block|{
try|try
block|{
name|Method
name|method
init|=
name|type
operator|.
name|getMethod
argument_list|(
literal|"is"
operator|+
name|ObjectHelper
operator|.
name|capitalize
argument_list|(
name|propertyName
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|method
operator|!=
literal|null
condition|)
block|{
return|return
name|method
operator|.
name|getReturnType
argument_list|()
operator|.
name|isAssignableFrom
argument_list|(
name|boolean
operator|.
name|class
argument_list|)
operator|||
name|method
operator|.
name|getReturnType
argument_list|()
operator|.
name|isAssignableFrom
argument_list|(
name|Boolean
operator|.
name|class
argument_list|)
return|;
block|}
block|}
catch|catch
parameter_list|(
name|NoSuchMethodException
name|e
parameter_list|)
block|{
comment|// ignore
block|}
return|return
literal|false
return|;
block|}
DECL|method|setProperties (Object target, Map<String, Object> properties, String optionPrefix, boolean allowBuilderPattern)
specifier|public
specifier|static
name|boolean
name|setProperties
parameter_list|(
name|Object
name|target
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
parameter_list|,
name|String
name|optionPrefix
parameter_list|,
name|boolean
name|allowBuilderPattern
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
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
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
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
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
argument_list|,
name|allowBuilderPattern
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
DECL|method|setProperties (Object target, Map<String, Object> properties, String optionPrefix)
specifier|public
specifier|static
name|boolean
name|setProperties
parameter_list|(
name|Object
name|target
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
parameter_list|,
name|String
name|optionPrefix
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|setProperties
argument_list|(
name|target
argument_list|,
name|properties
argument_list|,
name|optionPrefix
argument_list|,
literal|false
argument_list|)
return|;
block|}
DECL|method|extractProperties (Map<String, Object> properties, String optionPrefix)
specifier|public
specifier|static
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|extractProperties
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
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
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|rc
init|=
operator|new
name|LinkedHashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
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
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
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
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
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
DECL|method|setProperties (TypeConverter typeConverter, Object target, Map<String, Object> properties)
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
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
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
argument_list|<
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|>
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
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|entry
init|=
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
DECL|method|setProperties (Object target, Map<String, Object> properties)
specifier|public
specifier|static
name|boolean
name|setProperties
parameter_list|(
name|Object
name|target
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
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
name|properties
argument_list|)
return|;
block|}
DECL|method|setProperty (TypeConverter typeConverter, Object target, String name, Object value, boolean allowBuilderPattern)
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
parameter_list|,
name|boolean
name|allowBuilderPattern
parameter_list|)
throws|throws
name|Exception
block|{
try|try
block|{
name|Class
argument_list|<
name|?
argument_list|>
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
argument_list|,
name|allowBuilderPattern
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
name|typeConversionFailed
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
name|typeConversionFailed
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
name|typeConversionFailed
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
literal|"Setter \"{}\" with parameter type \"{}\" could not be used for type conversions of {}"
argument_list|,
operator|new
name|Object
index|[]
block|{
name|setter
block|,
name|setter
operator|.
name|getParameterTypes
argument_list|()
index|[
literal|0
index|]
block|,
name|value
block|}
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|// we did not find a setter method to use, and if we did try to use a type converter then throw
comment|// this kind of exception as the caused by will hint this error
if|if
condition|(
name|typeConversionFailed
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
name|typeConversionFailed
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
return|return
name|setProperty
argument_list|(
name|typeConverter
argument_list|,
name|target
argument_list|,
name|name
argument_list|,
name|value
argument_list|,
literal|false
argument_list|)
return|;
block|}
DECL|method|setProperty (Object target, String name, Object value, boolean allowBuilderPattern)
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
parameter_list|,
name|boolean
name|allowBuilderPattern
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
argument_list|,
name|allowBuilderPattern
argument_list|)
return|;
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
name|target
argument_list|,
name|name
argument_list|,
name|value
argument_list|,
literal|false
argument_list|)
return|;
block|}
DECL|method|convert (TypeConverter typeConverter, Class<?> type, Object value)
specifier|private
specifier|static
name|Object
name|convert
parameter_list|(
name|TypeConverter
name|typeConverter
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
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
DECL|method|findSetterMethods (TypeConverter typeConverter, Class<?> clazz, String name, Object value, boolean allowBuilderPattern)
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
argument_list|<
name|?
argument_list|>
name|clazz
parameter_list|,
name|String
name|name
parameter_list|,
name|Object
name|value
parameter_list|,
name|boolean
name|allowBuilderPattern
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
argument_list|<
name|?
argument_list|>
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
argument_list|<
name|?
argument_list|>
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
name|isSetter
argument_list|(
name|method
argument_list|,
name|allowBuilderPattern
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
name|LOG
operator|.
name|trace
argument_list|(
literal|"Found {} suitable setter methods for setting {}"
argument_list|,
name|candidates
operator|.
name|size
argument_list|()
argument_list|,
name|name
argument_list|)
expr_stmt|;
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
name|LOG
operator|.
name|trace
argument_list|(
literal|"Method {} is the best candidate as it has parameter with same instance type"
argument_list|,
name|method
argument_list|)
expr_stmt|;
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
block|}
end_class

end_unit

