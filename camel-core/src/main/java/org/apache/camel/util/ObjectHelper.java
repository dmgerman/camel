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
name|io
operator|.
name|Closeable
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
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
name|nio
operator|.
name|charset
operator|.
name|Charset
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

begin_comment
comment|/**  * A number of useful helper methods for working with Objects  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|ObjectHelper
specifier|public
specifier|final
class|class
name|ObjectHelper
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
name|ObjectHelper
operator|.
name|class
argument_list|)
decl_stmt|;
comment|/**      * Utility classes should not have a public constructor.      */
DECL|method|ObjectHelper ()
specifier|private
name|ObjectHelper
parameter_list|()
block|{     }
comment|/**      * @deprecated use the equal method instead      *      * @see #equal(Object, Object)      */
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
return|return
name|equal
argument_list|(
name|a
argument_list|,
name|b
argument_list|)
return|;
block|}
comment|/**      * A helper method for comparing objects for equality while handling nulls      */
DECL|method|equal (Object a, Object b)
specifier|public
specifier|static
name|boolean
name|equal
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
if|if
condition|(
name|a
operator|instanceof
name|byte
index|[]
operator|&&
name|b
operator|instanceof
name|byte
index|[]
condition|)
block|{
return|return
name|equalByteArray
argument_list|(
operator|(
name|byte
index|[]
operator|)
name|a
argument_list|,
operator|(
name|byte
index|[]
operator|)
name|b
argument_list|)
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
comment|/**      * A helper method for comparing byte arrays for equality while handling nulls      */
DECL|method|equalByteArray (byte[] a, byte[] b)
specifier|public
specifier|static
name|boolean
name|equalByteArray
parameter_list|(
name|byte
index|[]
name|a
parameter_list|,
name|byte
index|[]
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
comment|// loop and compare each byte
if|if
condition|(
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
name|length
operator|==
name|b
operator|.
name|length
condition|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|a
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|a
index|[
name|i
index|]
operator|!=
name|b
index|[
name|i
index|]
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
comment|// all bytes are equal
return|return
literal|true
return|;
block|}
return|return
literal|false
return|;
block|}
comment|/**      * Returns true if the given object is equal to any of the expected value      */
DECL|method|isEqualToAny (Object object, Object... values)
specifier|public
specifier|static
name|boolean
name|isEqualToAny
parameter_list|(
name|Object
name|object
parameter_list|,
name|Object
modifier|...
name|values
parameter_list|)
block|{
for|for
control|(
name|Object
name|value
range|:
name|values
control|)
block|{
if|if
condition|(
name|equal
argument_list|(
name|object
argument_list|,
name|value
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
comment|/**      * A helper method for performing an ordered comparsion on the objects      * handling nulls and objects which do not handle sorting gracefully      */
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
name|IllegalArgumentException
argument_list|(
name|name
operator|+
literal|" must be specified"
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
comment|/**      * Removes any starting characters on the given text which match the given      * character      *      * @param text the string      * @param ch the initial characters to remove      * @return either the original string or the new substring      */
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
DECL|method|capitalize (String text)
specifier|public
specifier|static
name|String
name|capitalize
parameter_list|(
name|String
name|text
parameter_list|)
block|{
if|if
condition|(
name|text
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|int
name|length
init|=
name|text
operator|.
name|length
argument_list|()
decl_stmt|;
if|if
condition|(
name|length
operator|==
literal|0
condition|)
block|{
return|return
name|text
return|;
block|}
name|String
name|answer
init|=
name|text
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
decl_stmt|;
if|if
condition|(
name|length
operator|>
literal|1
condition|)
block|{
name|answer
operator|+=
name|text
operator|.
name|substring
argument_list|(
literal|1
argument_list|,
name|length
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
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
elseif|else
if|if
condition|(
name|collectionOrArray
operator|instanceof
name|String
operator|&&
name|value
operator|instanceof
name|String
condition|)
block|{
name|String
name|str
init|=
operator|(
name|String
operator|)
name|collectionOrArray
decl_stmt|;
name|String
name|subStr
init|=
operator|(
name|String
operator|)
name|value
decl_stmt|;
return|return
name|str
operator|.
name|contains
argument_list|(
name|subStr
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
name|collectionOrArray
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
name|equal
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
block|}
return|return
literal|false
return|;
block|}
comment|/**      * Returns the predicate matching boolean on a {@link List} result set where      * if the first element is a boolean its value is used otherwise this method      * returns true if the collection is not empty      *      * @return<tt>true</tt> if the first element is a boolean and its value is true or      *          if the list is non empty      */
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
DECL|method|isNotNullAndNonEmpty (String text)
specifier|public
specifier|static
name|boolean
name|isNotNullAndNonEmpty
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
DECL|method|isNullOrBlank (String text)
specifier|public
specifier|static
name|boolean
name|isNullOrBlank
parameter_list|(
name|String
name|text
parameter_list|)
block|{
return|return
name|text
operator|==
literal|null
operator|||
name|text
operator|.
name|trim
argument_list|()
operator|.
name|length
argument_list|()
operator|<=
literal|0
return|;
block|}
comment|/**      * A helper method to access a system property, catching any security      * exceptions      *      * @param name the name of the system property required      * @param defaultValue the default value to use if the property is not      *                available or a security exception prevents access      * @return the system property value or the default value if the property is      *         not available or security does not allow its access      */
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
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
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
comment|/**      * Returns the type name of the given type or null if the type variable is      * null      */
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
comment|/**      * Attempts to load the given class name using the thread context class      * loader or the class loader used to load this class      *      * @param name the name of the class to load      * @return the class or null if it could not be loaded      */
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
comment|/**      * Attempts to load the given class name using the thread context class      * loader or the given class loader      *      * @param name the name of the class to load      * @param loader the class loader to use after the thread context class      *                loader      * @return the class or null if it could not be loaded      */
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
name|LOG
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
comment|/**      * A helper method to invoke a method via reflection and wrap any exceptions      * as {@link RuntimeCamelException} instances      *      * @param method the method to invoke      * @param instance the object instance (or null for static methods)      * @param parameters the parameters to the method      * @return the result of the method invocation      */
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
comment|/**      * Returns a list of methods which are annotated with the given annotation      *      * @param type the type to reflect on      * @param annotationType the annotation type      * @return a list of the methods found      */
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
comment|/**      * Turns the given object arrays into a meaningful string      *      * @param objects an array of objects or null      * @return a meaningful string      */
DECL|method|asString (Object[] objects)
specifier|public
specifier|static
name|String
name|asString
parameter_list|(
name|Object
index|[]
name|objects
parameter_list|)
block|{
if|if
condition|(
name|objects
operator|==
literal|null
condition|)
block|{
return|return
literal|"null"
return|;
block|}
else|else
block|{
name|StringBuffer
name|buffer
init|=
operator|new
name|StringBuffer
argument_list|(
literal|"{"
argument_list|)
decl_stmt|;
name|int
name|counter
init|=
literal|0
decl_stmt|;
for|for
control|(
name|Object
name|object
range|:
name|objects
control|)
block|{
if|if
condition|(
name|counter
operator|++
operator|>
literal|0
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|", "
argument_list|)
expr_stmt|;
block|}
name|String
name|text
init|=
operator|(
name|object
operator|==
literal|null
operator|)
condition|?
literal|"null"
else|:
name|object
operator|.
name|toString
argument_list|()
decl_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|text
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
block|}
comment|/**      * Returns true if a class is assignable from another class like the      * {@link Class#isAssignableFrom(Class)} method but which also includes      * coercion between primitive types to deal with Java 5 primitive type      * wrapping      */
DECL|method|isAssignableFrom (Class a, Class b)
specifier|public
specifier|static
name|boolean
name|isAssignableFrom
parameter_list|(
name|Class
name|a
parameter_list|,
name|Class
name|b
parameter_list|)
block|{
name|a
operator|=
name|convertPrimitiveTypeToWrapperType
argument_list|(
name|a
argument_list|)
expr_stmt|;
name|b
operator|=
name|convertPrimitiveTypeToWrapperType
argument_list|(
name|b
argument_list|)
expr_stmt|;
return|return
name|a
operator|.
name|isAssignableFrom
argument_list|(
name|b
argument_list|)
return|;
block|}
comment|/**      * Converts primitive types such as int to its wrapper type like      * {@link Integer}      */
DECL|method|convertPrimitiveTypeToWrapperType (Class type)
specifier|public
specifier|static
name|Class
name|convertPrimitiveTypeToWrapperType
parameter_list|(
name|Class
name|type
parameter_list|)
block|{
name|Class
name|rc
init|=
name|type
decl_stmt|;
if|if
condition|(
name|type
operator|.
name|isPrimitive
argument_list|()
condition|)
block|{
if|if
condition|(
name|type
operator|==
name|int
operator|.
name|class
condition|)
block|{
name|rc
operator|=
name|Integer
operator|.
name|class
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|type
operator|==
name|long
operator|.
name|class
condition|)
block|{
name|rc
operator|=
name|Long
operator|.
name|class
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|type
operator|==
name|double
operator|.
name|class
condition|)
block|{
name|rc
operator|=
name|Double
operator|.
name|class
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|type
operator|==
name|float
operator|.
name|class
condition|)
block|{
name|rc
operator|=
name|Float
operator|.
name|class
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|type
operator|==
name|short
operator|.
name|class
condition|)
block|{
name|rc
operator|=
name|Short
operator|.
name|class
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|type
operator|==
name|byte
operator|.
name|class
condition|)
block|{
name|rc
operator|=
name|Byte
operator|.
name|class
expr_stmt|;
comment|// TODO: Why is boolean disabled
comment|/*             } else if (type == boolean.class) {                 rc = Boolean.class; */
block|}
block|}
return|return
name|rc
return|;
block|}
comment|/**      * Helper method to return the default character set name      */
DECL|method|getDefaultCharacterSet ()
specifier|public
specifier|static
name|String
name|getDefaultCharacterSet
parameter_list|()
block|{
return|return
name|Charset
operator|.
name|defaultCharset
argument_list|()
operator|.
name|name
argument_list|()
return|;
block|}
comment|/**      * Returns the Java Bean property name of the given method, if it is a setter      */
DECL|method|getPropertyName (Method method)
specifier|public
specifier|static
name|String
name|getPropertyName
parameter_list|(
name|Method
name|method
parameter_list|)
block|{
name|String
name|propertyName
init|=
name|method
operator|.
name|getName
argument_list|()
decl_stmt|;
if|if
condition|(
name|propertyName
operator|.
name|startsWith
argument_list|(
literal|"set"
argument_list|)
operator|&&
name|method
operator|.
name|getParameterTypes
argument_list|()
operator|.
name|length
operator|==
literal|1
condition|)
block|{
name|propertyName
operator|=
name|propertyName
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
name|propertyName
operator|.
name|substring
argument_list|(
literal|4
argument_list|)
expr_stmt|;
block|}
return|return
name|propertyName
return|;
block|}
comment|/**      * Returns true if the given collection of annotations matches the given type      */
DECL|method|hasAnnotation (Annotation[] annotations, Class<?> type)
specifier|public
specifier|static
name|boolean
name|hasAnnotation
parameter_list|(
name|Annotation
index|[]
name|annotations
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|)
block|{
for|for
control|(
name|Annotation
name|annotation
range|:
name|annotations
control|)
block|{
if|if
condition|(
name|type
operator|.
name|isInstance
argument_list|(
name|annotation
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
comment|/**      * Closes the given resource if it is available, logging any closing exceptions to the given log      *      * @param closeable the object to close      * @param name the name of the resource      * @param log the log to use when reporting closure warnings      */
DECL|method|close (Closeable closeable, String name, Log log)
specifier|public
specifier|static
name|void
name|close
parameter_list|(
name|Closeable
name|closeable
parameter_list|,
name|String
name|name
parameter_list|,
name|Log
name|log
parameter_list|)
block|{
if|if
condition|(
name|closeable
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|closeable
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Could not close "
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
block|}
block|}
comment|/**      * Converts the given value to the required type or throw a meaningful exception      */
DECL|method|cast (Class<T> toType, Object value)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
name|cast
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|toType
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
if|if
condition|(
name|toType
operator|==
name|boolean
operator|.
name|class
condition|)
block|{
return|return
operator|(
name|T
operator|)
name|cast
argument_list|(
name|Boolean
operator|.
name|class
argument_list|,
name|value
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|toType
operator|.
name|isPrimitive
argument_list|()
condition|)
block|{
name|Class
name|newType
init|=
name|convertPrimitiveTypeToWrapperType
argument_list|(
name|toType
argument_list|)
decl_stmt|;
if|if
condition|(
name|newType
operator|!=
name|toType
condition|)
block|{
return|return
operator|(
name|T
operator|)
name|cast
argument_list|(
name|newType
argument_list|,
name|value
argument_list|)
return|;
block|}
block|}
try|try
block|{
return|return
name|toType
operator|.
name|cast
argument_list|(
name|value
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|ClassCastException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Failed to convert: "
operator|+
name|value
operator|+
literal|" to type: "
operator|+
name|toType
operator|.
name|getName
argument_list|()
operator|+
literal|" due to: "
operator|+
name|e
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
comment|/**      * A helper method to create a new instance of a type using the default constructor arguments.      */
DECL|method|newInstance (Class<T> type)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
name|newInstance
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
try|try
block|{
return|return
name|type
operator|.
name|newInstance
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|InstantiationException
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
block|}
comment|/**      * A helper method to create a new instance of a type using the default constructor arguments.      */
DECL|method|newInstance (Class<?> actualType, Class<T> expectedType)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
name|newInstance
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|actualType
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|expectedType
parameter_list|)
block|{
try|try
block|{
name|Object
name|value
init|=
name|actualType
operator|.
name|newInstance
argument_list|()
decl_stmt|;
return|return
name|cast
argument_list|(
name|expectedType
argument_list|,
name|value
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|InstantiationException
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
block|}
comment|/**      * Returns true if the given name is a valid java identifier      */
DECL|method|isJavaIdentifier (String name)
specifier|public
specifier|static
name|boolean
name|isJavaIdentifier
parameter_list|(
name|String
name|name
parameter_list|)
block|{
if|if
condition|(
name|name
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
name|int
name|size
init|=
name|name
operator|.
name|length
argument_list|()
decl_stmt|;
if|if
condition|(
name|size
operator|<
literal|1
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|Character
operator|.
name|isJavaIdentifierStart
argument_list|(
name|name
operator|.
name|charAt
argument_list|(
literal|0
argument_list|)
argument_list|)
condition|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|1
init|;
name|i
operator|<
name|size
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
operator|!
name|Character
operator|.
name|isJavaIdentifierPart
argument_list|(
name|name
operator|.
name|charAt
argument_list|(
name|i
argument_list|)
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
return|return
literal|true
return|;
block|}
return|return
literal|false
return|;
block|}
comment|/**      * Returns the type of the given object or null if the value is null      */
DECL|method|type (Object bean)
specifier|public
specifier|static
name|Object
name|type
parameter_list|(
name|Object
name|bean
parameter_list|)
block|{
return|return
name|bean
operator|!=
literal|null
condition|?
name|bean
operator|.
name|getClass
argument_list|()
else|:
literal|null
return|;
block|}
block|}
end_class

end_unit

