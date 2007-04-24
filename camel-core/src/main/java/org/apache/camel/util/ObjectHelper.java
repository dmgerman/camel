begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|RuntimeCamelException
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
name|converter
operator|.
name|ObjectConverter
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

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|Annotation
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
name|Collection
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

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|ObjectHelper
specifier|public
class|class
name|ObjectHelper
block|{
DECL|field|log
specifier|private
specifier|static
specifier|final
specifier|transient
name|Log
name|log
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|ObjectHelper
operator|.
name|class
argument_list|)
decl_stmt|;
comment|/**      * A helper method for comparing objects for equality while handling nulls      */
DECL|method|equals (Object a, Object b)
specifier|public
specifier|static
name|boolean
name|equals
parameter_list|(
name|Object
name|a
parameter_list|,
name|Object
name|b
parameter_list|)
block|{
if|if
condition|(
name|a
operator|==
name|b
condition|)
block|{
return|return
literal|true
return|;
block|}
return|return
name|a
operator|!=
literal|null
operator|&&
name|b
operator|!=
literal|null
operator|&&
name|a
operator|.
name|equals
argument_list|(
name|b
argument_list|)
return|;
block|}
comment|/**      * A helper method for performing an ordered comparsion on the objects      * handling nulls and objects which do not      * handle sorting gracefully      */
DECL|method|compare (Object a, Object b)
specifier|public
specifier|static
name|int
name|compare
parameter_list|(
name|Object
name|a
parameter_list|,
name|Object
name|b
parameter_list|)
block|{
if|if
condition|(
name|a
operator|==
name|b
condition|)
block|{
return|return
literal|0
return|;
block|}
if|if
condition|(
name|a
operator|==
literal|null
condition|)
block|{
return|return
operator|-
literal|1
return|;
block|}
if|if
condition|(
name|b
operator|==
literal|null
condition|)
block|{
return|return
literal|1
return|;
block|}
if|if
condition|(
name|a
operator|instanceof
name|Comparable
condition|)
block|{
name|Comparable
name|comparable
init|=
operator|(
name|Comparable
operator|)
name|a
decl_stmt|;
return|return
name|comparable
operator|.
name|compareTo
argument_list|(
name|b
argument_list|)
return|;
block|}
else|else
block|{
name|int
name|answer
init|=
name|a
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|.
name|compareTo
argument_list|(
name|b
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|answer
operator|==
literal|0
condition|)
block|{
name|answer
operator|=
name|a
operator|.
name|hashCode
argument_list|()
operator|-
name|b
operator|.
name|hashCode
argument_list|()
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
block|}
DECL|method|notNull (Object value, String name)
specifier|public
specifier|static
name|void
name|notNull
parameter_list|(
name|Object
name|value
parameter_list|,
name|String
name|name
parameter_list|)
block|{
if|if
condition|(
name|value
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|(
literal|"No "
operator|+
name|name
operator|+
literal|" specified"
argument_list|)
throw|;
block|}
block|}
DECL|method|splitOnCharacter (String value, String needle, int count)
specifier|public
specifier|static
name|String
index|[]
name|splitOnCharacter
parameter_list|(
name|String
name|value
parameter_list|,
name|String
name|needle
parameter_list|,
name|int
name|count
parameter_list|)
block|{
name|String
name|rc
index|[]
init|=
operator|new
name|String
index|[
name|count
index|]
decl_stmt|;
name|rc
index|[
literal|0
index|]
operator|=
name|value
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|1
init|;
name|i
operator|<
name|count
condition|;
name|i
operator|++
control|)
block|{
name|String
name|v
init|=
name|rc
index|[
name|i
operator|-
literal|1
index|]
decl_stmt|;
name|int
name|p
init|=
name|v
operator|.
name|indexOf
argument_list|(
name|needle
argument_list|)
decl_stmt|;
if|if
condition|(
name|p
operator|<
literal|0
condition|)
block|{
return|return
name|rc
return|;
block|}
name|rc
index|[
name|i
operator|-
literal|1
index|]
operator|=
name|v
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|p
argument_list|)
expr_stmt|;
name|rc
index|[
name|i
index|]
operator|=
name|v
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
name|rc
return|;
block|}
comment|/**      * Removes any starting characters on the given text which match the given character      *      * @param text the string      * @param ch the initial characters to remove      * @return either the original string or the new substring      */
DECL|method|removeStartingCharacters (String text, char ch)
specifier|public
specifier|static
name|String
name|removeStartingCharacters
parameter_list|(
name|String
name|text
parameter_list|,
name|char
name|ch
parameter_list|)
block|{
name|int
name|idx
init|=
literal|0
decl_stmt|;
while|while
condition|(
name|text
operator|.
name|charAt
argument_list|(
name|idx
argument_list|)
operator|==
name|ch
condition|)
block|{
name|idx
operator|++
expr_stmt|;
block|}
if|if
condition|(
name|idx
operator|>
literal|0
condition|)
block|{
return|return
name|text
operator|.
name|substring
argument_list|(
name|idx
argument_list|)
return|;
block|}
return|return
name|text
return|;
block|}
comment|/**      * Returns true if the collection contains the specified value      */
DECL|method|contains (Object collectionOrArray, Object value)
specifier|public
specifier|static
name|boolean
name|contains
parameter_list|(
name|Object
name|collectionOrArray
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
if|if
condition|(
name|collectionOrArray
operator|instanceof
name|Collection
condition|)
block|{
name|Collection
name|collection
init|=
operator|(
name|Collection
operator|)
name|collectionOrArray
decl_stmt|;
return|return
name|collection
operator|.
name|contains
argument_list|(
name|value
argument_list|)
return|;
block|}
else|else
block|{
name|Iterator
name|iter
init|=
name|ObjectConverter
operator|.
name|iterator
argument_list|(
name|value
argument_list|)
decl_stmt|;
while|while
condition|(
name|iter
operator|.
name|hasNext
argument_list|()
condition|)
block|{
if|if
condition|(
name|equals
argument_list|(
name|value
argument_list|,
name|iter
operator|.
name|next
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
block|}
comment|/**      * Returns the predicate matching boolean on a {@link List} result set      * where if the first element is a boolean its value is used      * otherwise this method returns true if the collection is not empty      *      * @returns true if the first element is a boolean and its value is true or if the list is non empty      */
DECL|method|matches (List list)
specifier|public
specifier|static
name|boolean
name|matches
parameter_list|(
name|List
name|list
parameter_list|)
block|{
if|if
condition|(
operator|!
name|list
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|Object
name|value
init|=
name|list
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|instanceof
name|Boolean
condition|)
block|{
name|Boolean
name|flag
init|=
operator|(
name|Boolean
operator|)
name|value
decl_stmt|;
return|return
name|flag
operator|.
name|booleanValue
argument_list|()
return|;
block|}
else|else
block|{
comment|// lets assume non-empty results are true
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
DECL|method|isNotNullOrBlank (String text)
specifier|public
specifier|static
name|boolean
name|isNotNullOrBlank
parameter_list|(
name|String
name|text
parameter_list|)
block|{
return|return
name|text
operator|!=
literal|null
operator|&&
name|text
operator|.
name|trim
argument_list|()
operator|.
name|length
argument_list|()
operator|>
literal|0
return|;
block|}
comment|/**      * A helper method to access a system property, catching any security exceptions      *      * @param name         the name of the system property required      * @param defaultValue the default value to use if the property is not available or a security exception prevents access      * @return the system property value or the default value if the property is not available or security does not allow its access      */
DECL|method|getSystemProperty (String name, String defaultValue)
specifier|public
specifier|static
name|String
name|getSystemProperty
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|defaultValue
parameter_list|)
block|{
try|try
block|{
return|return
name|System
operator|.
name|getProperty
argument_list|(
name|name
argument_list|,
name|defaultValue
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
if|if
condition|(
name|log
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Caught security exception accessing system property: "
operator|+
name|name
operator|+
literal|". Reason: "
operator|+
name|e
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
return|return
name|defaultValue
return|;
block|}
block|}
comment|/**      * Returns the type name of the given type or null if the type variable is null      */
DECL|method|name (Class type)
specifier|public
specifier|static
name|String
name|name
parameter_list|(
name|Class
name|type
parameter_list|)
block|{
return|return
name|type
operator|!=
literal|null
condition|?
name|type
operator|.
name|getName
argument_list|()
else|:
literal|null
return|;
block|}
comment|/**      * Returns the type name of the given value      */
DECL|method|className (Object value)
specifier|public
specifier|static
name|String
name|className
parameter_list|(
name|Object
name|value
parameter_list|)
block|{
return|return
name|name
argument_list|(
name|value
operator|!=
literal|null
condition|?
name|value
operator|.
name|getClass
argument_list|()
else|:
literal|null
argument_list|)
return|;
block|}
comment|/**      * Attempts to load the given class name using the thread context class loader      * or the class loader used to load this class      *      * @param name the name of the class to load      * @return the class or null if it could not be loaded      */
DECL|method|loadClass (String name)
specifier|public
specifier|static
name|Class
argument_list|<
name|?
argument_list|>
name|loadClass
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|loadClass
argument_list|(
name|name
argument_list|,
name|ObjectHelper
operator|.
name|class
operator|.
name|getClassLoader
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * Attempts to load the given class name using the thread context class loader or the given class loader      *      * @param name   the name of the class to load      * @param loader the class loader to use after the thread context class loader      * @return the class or null if it could not be loaded      */
DECL|method|loadClass (String name, ClassLoader loader)
specifier|public
specifier|static
name|Class
argument_list|<
name|?
argument_list|>
name|loadClass
parameter_list|(
name|String
name|name
parameter_list|,
name|ClassLoader
name|loader
parameter_list|)
block|{
name|ClassLoader
name|contextClassLoader
init|=
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
decl_stmt|;
if|if
condition|(
name|contextClassLoader
operator|!=
literal|null
condition|)
block|{
try|try
block|{
return|return
name|contextClassLoader
operator|.
name|loadClass
argument_list|(
name|name
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|ClassNotFoundException
name|e
parameter_list|)
block|{
try|try
block|{
return|return
name|loader
operator|.
name|loadClass
argument_list|(
name|name
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|ClassNotFoundException
name|e1
parameter_list|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Could not find class: "
operator|+
name|name
operator|+
literal|". Reason: "
operator|+
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
literal|null
return|;
block|}
comment|/**      * A helper method to invoke a method via reflection and wrap any exceptions      * as {@link RuntimeCamelException} instances      *      * @param method     the method to invoke      * @param instance   the object instance (or null for static methods)      * @param parameters the parameters to the method      * @return the result of the method invocation      */
DECL|method|invokeMethod (Method method, Object instance, Object... parameters)
specifier|public
specifier|static
name|Object
name|invokeMethod
parameter_list|(
name|Method
name|method
parameter_list|,
name|Object
name|instance
parameter_list|,
name|Object
modifier|...
name|parameters
parameter_list|)
block|{
try|try
block|{
return|return
name|method
operator|.
name|invoke
argument_list|(
name|instance
argument_list|,
name|parameters
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|IllegalAccessException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|InvocationTargetException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
name|e
operator|.
name|getCause
argument_list|()
argument_list|)
throw|;
block|}
block|}
comment|/**      * Returns a list of methods which are annotated with the given annotation      *      * @param type           the type to reflect on      * @param annotationType the annotation type      * @return a list of the methods found      */
DECL|method|findMethodsWithAnnotation (Class<?> type, Class<? extends Annotation> annotationType)
specifier|public
specifier|static
name|List
argument_list|<
name|Method
argument_list|>
name|findMethodsWithAnnotation
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|,
name|Class
argument_list|<
name|?
extends|extends
name|Annotation
argument_list|>
name|annotationType
parameter_list|)
block|{
name|List
argument_list|<
name|Method
argument_list|>
name|answer
init|=
operator|new
name|ArrayList
argument_list|<
name|Method
argument_list|>
argument_list|()
decl_stmt|;
do|do
block|{
name|Method
index|[]
name|methods
init|=
name|type
operator|.
name|getDeclaredMethods
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
name|method
operator|.
name|getAnnotation
argument_list|(
name|annotationType
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|add
argument_list|(
name|method
argument_list|)
expr_stmt|;
block|}
block|}
name|type
operator|=
name|type
operator|.
name|getSuperclass
argument_list|()
expr_stmt|;
block|}
do|while
condition|(
name|type
operator|!=
literal|null
condition|)
do|;
return|return
name|answer
return|;
block|}
block|}
end_class

end_unit

