begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.util.component
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|component
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
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Comparator
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
name|List
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
name|regex
operator|.
name|Matcher
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
name|util
operator|.
name|ObjectHelper
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
comment|/**  * Parser base class for generating ApiMethod enumerations.  */
end_comment

begin_class
DECL|class|ApiMethodParser
specifier|public
specifier|abstract
class|class
name|ApiMethodParser
parameter_list|<
name|T
parameter_list|>
block|{
comment|// also used by JavadocApiMethodGeneratorMojo
DECL|field|ARGS_PATTERN
specifier|public
specifier|static
specifier|final
name|Pattern
name|ARGS_PATTERN
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"\\s*([^<\\s]+)\\s*(<[^>]+>)?\\s+([^\\s,]+)\\s*,?"
argument_list|)
decl_stmt|;
DECL|field|METHOD_PREFIX
specifier|private
specifier|static
specifier|final
name|String
name|METHOD_PREFIX
init|=
literal|"^(\\s*(public|final|synchronized|native)\\s+)*(\\s*<[^>]>)?\\s*(\\S+)\\s+([^\\(]+\\s*)\\("
decl_stmt|;
DECL|field|METHOD_PATTERN
specifier|private
specifier|static
specifier|final
name|Pattern
name|METHOD_PATTERN
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"\\s*([^<\\s]+)\\s*(<[^>]+>)?\\s+(\\S+)\\s*\\(\\s*([\\S\\s,]*)\\)\\s*;?\\s*"
argument_list|)
decl_stmt|;
DECL|field|JAVA_LANG
specifier|private
specifier|static
specifier|final
name|String
name|JAVA_LANG
init|=
literal|"java.lang."
decl_stmt|;
DECL|field|PRIMITIVE_TYPES
specifier|private
specifier|static
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|PRIMITIVE_TYPES
decl_stmt|;
static|static
block|{
name|PRIMITIVE_TYPES
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|()
expr_stmt|;
name|PRIMITIVE_TYPES
operator|.
name|put
argument_list|(
literal|"int"
argument_list|,
name|Integer
operator|.
name|TYPE
argument_list|)
expr_stmt|;
name|PRIMITIVE_TYPES
operator|.
name|put
argument_list|(
literal|"long"
argument_list|,
name|Long
operator|.
name|TYPE
argument_list|)
expr_stmt|;
name|PRIMITIVE_TYPES
operator|.
name|put
argument_list|(
literal|"double"
argument_list|,
name|Double
operator|.
name|TYPE
argument_list|)
expr_stmt|;
name|PRIMITIVE_TYPES
operator|.
name|put
argument_list|(
literal|"float"
argument_list|,
name|Float
operator|.
name|TYPE
argument_list|)
expr_stmt|;
name|PRIMITIVE_TYPES
operator|.
name|put
argument_list|(
literal|"boolean"
argument_list|,
name|Boolean
operator|.
name|TYPE
argument_list|)
expr_stmt|;
name|PRIMITIVE_TYPES
operator|.
name|put
argument_list|(
literal|"char"
argument_list|,
name|Character
operator|.
name|TYPE
argument_list|)
expr_stmt|;
name|PRIMITIVE_TYPES
operator|.
name|put
argument_list|(
literal|"byte"
argument_list|,
name|Byte
operator|.
name|TYPE
argument_list|)
expr_stmt|;
name|PRIMITIVE_TYPES
operator|.
name|put
argument_list|(
literal|"void"
argument_list|,
name|Void
operator|.
name|TYPE
argument_list|)
expr_stmt|;
name|PRIMITIVE_TYPES
operator|.
name|put
argument_list|(
literal|"short"
argument_list|,
name|Short
operator|.
name|TYPE
argument_list|)
expr_stmt|;
block|}
DECL|field|log
specifier|private
specifier|final
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
DECL|field|proxyType
specifier|private
specifier|final
name|Class
argument_list|<
name|T
argument_list|>
name|proxyType
decl_stmt|;
DECL|field|signatures
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|signatures
decl_stmt|;
DECL|field|classLoader
specifier|private
name|ClassLoader
name|classLoader
init|=
name|ApiMethodParser
operator|.
name|class
operator|.
name|getClassLoader
argument_list|()
decl_stmt|;
DECL|method|ApiMethodParser (Class<T> proxyType)
specifier|public
name|ApiMethodParser
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|proxyType
parameter_list|)
block|{
name|this
operator|.
name|proxyType
operator|=
name|proxyType
expr_stmt|;
block|}
DECL|method|getProxyType ()
specifier|public
name|Class
argument_list|<
name|T
argument_list|>
name|getProxyType
parameter_list|()
block|{
return|return
name|proxyType
return|;
block|}
DECL|method|getSignatures ()
specifier|public
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|getSignatures
parameter_list|()
block|{
return|return
name|signatures
return|;
block|}
DECL|method|setSignatures (List<String> signatures)
specifier|public
specifier|final
name|void
name|setSignatures
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|signatures
parameter_list|)
block|{
name|this
operator|.
name|signatures
operator|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
expr_stmt|;
name|this
operator|.
name|signatures
operator|.
name|addAll
argument_list|(
name|signatures
argument_list|)
expr_stmt|;
block|}
DECL|method|getClassLoader ()
specifier|public
specifier|final
name|ClassLoader
name|getClassLoader
parameter_list|()
block|{
return|return
name|classLoader
return|;
block|}
DECL|method|setClassLoader (ClassLoader classLoader)
specifier|public
specifier|final
name|void
name|setClassLoader
parameter_list|(
name|ClassLoader
name|classLoader
parameter_list|)
block|{
name|this
operator|.
name|classLoader
operator|=
name|classLoader
expr_stmt|;
block|}
comment|/**      * Parses the method signatures from {@code getSignatures()}.      * @return list of Api methods as {@link ApiMethodModel}      */
DECL|method|parse ()
specifier|public
specifier|final
name|List
argument_list|<
name|ApiMethodModel
argument_list|>
name|parse
parameter_list|()
block|{
comment|// parse sorted signatures and generate descriptions
name|List
argument_list|<
name|ApiMethodModel
argument_list|>
name|result
init|=
operator|new
name|ArrayList
argument_list|<
name|ApiMethodModel
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|signature
range|:
name|signatures
control|)
block|{
comment|// skip comment or empty lines
if|if
condition|(
name|signature
operator|.
name|startsWith
argument_list|(
literal|"##"
argument_list|)
operator|||
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|signature
argument_list|)
condition|)
block|{
continue|continue;
block|}
comment|// remove all modifiers and type parameters for method
name|signature
operator|=
name|signature
operator|.
name|replaceAll
argument_list|(
name|METHOD_PREFIX
argument_list|,
literal|"$4 $5("
argument_list|)
expr_stmt|;
comment|// remove all final modifiers for arguments
name|signature
operator|=
name|signature
operator|.
name|replaceAll
argument_list|(
literal|"(\\(|,\\s*)final\\s+"
argument_list|,
literal|"$1"
argument_list|)
expr_stmt|;
comment|// remove all redundant spaces in generic parameters
name|signature
operator|=
name|signature
operator|.
name|replaceAll
argument_list|(
literal|"\\s*<\\s*"
argument_list|,
literal|"<"
argument_list|)
operator|.
name|replaceAll
argument_list|(
literal|"\\s*>"
argument_list|,
literal|">"
argument_list|)
expr_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Processing "
operator|+
name|signature
argument_list|)
expr_stmt|;
specifier|final
name|Matcher
name|methodMatcher
init|=
name|METHOD_PATTERN
operator|.
name|matcher
argument_list|(
name|signature
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|methodMatcher
operator|.
name|matches
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Invalid method signature "
operator|+
name|signature
argument_list|)
throw|;
block|}
comment|// ignore generic type parameters in result, if any
specifier|final
name|Class
argument_list|<
name|?
argument_list|>
name|resultType
init|=
name|forName
argument_list|(
name|methodMatcher
operator|.
name|group
argument_list|(
literal|1
argument_list|)
argument_list|)
decl_stmt|;
specifier|final
name|String
name|name
init|=
name|methodMatcher
operator|.
name|group
argument_list|(
literal|3
argument_list|)
decl_stmt|;
specifier|final
name|String
name|argSignature
init|=
name|methodMatcher
operator|.
name|group
argument_list|(
literal|4
argument_list|)
decl_stmt|;
specifier|final
name|List
argument_list|<
name|Argument
argument_list|>
name|arguments
init|=
operator|new
name|ArrayList
argument_list|<
name|Argument
argument_list|>
argument_list|()
decl_stmt|;
specifier|final
name|List
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|argTypes
init|=
operator|new
name|ArrayList
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
specifier|final
name|Matcher
name|argsMatcher
init|=
name|ARGS_PATTERN
operator|.
name|matcher
argument_list|(
name|argSignature
argument_list|)
decl_stmt|;
while|while
condition|(
name|argsMatcher
operator|.
name|find
argument_list|()
condition|)
block|{
specifier|final
name|Class
argument_list|<
name|?
argument_list|>
name|type
init|=
name|forName
argument_list|(
name|argsMatcher
operator|.
name|group
argument_list|(
literal|1
argument_list|)
argument_list|)
decl_stmt|;
name|argTypes
operator|.
name|add
argument_list|(
name|type
argument_list|)
expr_stmt|;
specifier|final
name|String
name|typeArgsGroup
init|=
name|argsMatcher
operator|.
name|group
argument_list|(
literal|2
argument_list|)
decl_stmt|;
specifier|final
name|String
name|typeArgs
init|=
name|typeArgsGroup
operator|!=
literal|null
condition|?
name|typeArgsGroup
operator|.
name|substring
argument_list|(
literal|1
argument_list|,
name|typeArgsGroup
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
operator|.
name|replaceAll
argument_list|(
literal|" "
argument_list|,
literal|""
argument_list|)
else|:
literal|null
decl_stmt|;
name|arguments
operator|.
name|add
argument_list|(
operator|new
name|Argument
argument_list|(
name|argsMatcher
operator|.
name|group
argument_list|(
literal|3
argument_list|)
argument_list|,
name|type
argument_list|,
name|typeArgs
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|Method
name|method
decl_stmt|;
try|try
block|{
name|method
operator|=
name|proxyType
operator|.
name|getMethod
argument_list|(
name|name
argument_list|,
name|argTypes
operator|.
name|toArray
argument_list|(
operator|new
name|Class
argument_list|<
name|?
argument_list|>
index|[
name|argTypes
operator|.
name|size
argument_list|()
index|]
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoSuchMethodException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Method not found ["
operator|+
name|signature
operator|+
literal|"] in type "
operator|+
name|proxyType
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
name|result
operator|.
name|add
argument_list|(
operator|new
name|ApiMethodModel
argument_list|(
name|name
argument_list|,
name|resultType
argument_list|,
name|arguments
argument_list|,
name|method
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// allow derived classes to post process
name|result
operator|=
name|processResults
argument_list|(
name|result
argument_list|)
expr_stmt|;
comment|// check that argument names have the same type across methods
name|Map
argument_list|<
name|String
argument_list|,
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|allArguments
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|ApiMethodModel
name|model
range|:
name|result
control|)
block|{
for|for
control|(
name|Argument
name|argument
range|:
name|model
operator|.
name|getArguments
argument_list|()
control|)
block|{
name|String
name|name
init|=
name|argument
operator|.
name|getName
argument_list|()
decl_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|argClass
init|=
name|allArguments
operator|.
name|get
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|type
init|=
name|argument
operator|.
name|getType
argument_list|()
decl_stmt|;
if|if
condition|(
name|argClass
operator|==
literal|null
condition|)
block|{
name|allArguments
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|argClass
operator|!=
name|type
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Argument ["
operator|+
name|name
operator|+
literal|"] is used in multiple methods with different types "
operator|+
name|argClass
operator|.
name|getCanonicalName
argument_list|()
operator|+
literal|", "
operator|+
name|type
operator|.
name|getCanonicalName
argument_list|()
argument_list|)
throw|;
block|}
block|}
block|}
block|}
name|allArguments
operator|.
name|clear
argument_list|()
expr_stmt|;
name|Collections
operator|.
name|sort
argument_list|(
name|result
argument_list|,
operator|new
name|Comparator
argument_list|<
name|ApiMethodModel
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|int
name|compare
parameter_list|(
name|ApiMethodModel
name|model1
parameter_list|,
name|ApiMethodModel
name|model2
parameter_list|)
block|{
specifier|final
name|int
name|nameCompare
init|=
name|model1
operator|.
name|name
operator|.
name|compareTo
argument_list|(
name|model2
operator|.
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|nameCompare
operator|!=
literal|0
condition|)
block|{
return|return
name|nameCompare
return|;
block|}
else|else
block|{
specifier|final
name|int
name|nArgs1
init|=
name|model1
operator|.
name|arguments
operator|.
name|size
argument_list|()
decl_stmt|;
specifier|final
name|int
name|nArgsCompare
init|=
name|nArgs1
operator|-
name|model2
operator|.
name|arguments
operator|.
name|size
argument_list|()
decl_stmt|;
if|if
condition|(
name|nArgsCompare
operator|!=
literal|0
condition|)
block|{
return|return
name|nArgsCompare
return|;
block|}
else|else
block|{
comment|// same number of args, compare arg names, kinda arbitrary to use alphabetized order
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|nArgs1
condition|;
name|i
operator|++
control|)
block|{
specifier|final
name|int
name|argCompare
init|=
name|model1
operator|.
name|arguments
operator|.
name|get
argument_list|(
name|i
argument_list|)
operator|.
name|name
operator|.
name|compareTo
argument_list|(
name|model2
operator|.
name|arguments
operator|.
name|get
argument_list|(
name|i
argument_list|)
operator|.
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|argCompare
operator|!=
literal|0
condition|)
block|{
return|return
name|argCompare
return|;
block|}
block|}
comment|// duplicate methods???
name|log
operator|.
name|warn
argument_list|(
literal|"Duplicate methods found ["
operator|+
name|model1
operator|+
literal|"], ["
operator|+
name|model2
operator|+
literal|"]"
argument_list|)
expr_stmt|;
return|return
literal|0
return|;
block|}
block|}
block|}
block|}
argument_list|)
expr_stmt|;
comment|// assign unique names to every method model
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|dups
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|ApiMethodModel
name|model
range|:
name|result
control|)
block|{
comment|// locale independent upper case conversion
specifier|final
name|String
name|name
init|=
name|model
operator|.
name|getName
argument_list|()
decl_stmt|;
specifier|final
name|char
index|[]
name|upperCase
init|=
operator|new
name|char
index|[
name|name
operator|.
name|length
argument_list|()
index|]
decl_stmt|;
specifier|final
name|char
index|[]
name|lowerCase
init|=
name|name
operator|.
name|toCharArray
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|upperCase
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|upperCase
index|[
name|i
index|]
operator|=
name|Character
operator|.
name|toUpperCase
argument_list|(
name|lowerCase
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
name|String
name|uniqueName
init|=
operator|new
name|String
argument_list|(
name|upperCase
argument_list|)
decl_stmt|;
name|Integer
name|suffix
init|=
name|dups
operator|.
name|get
argument_list|(
name|uniqueName
argument_list|)
decl_stmt|;
if|if
condition|(
name|suffix
operator|==
literal|null
condition|)
block|{
name|dups
operator|.
name|put
argument_list|(
name|uniqueName
argument_list|,
literal|1
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|dups
operator|.
name|put
argument_list|(
name|uniqueName
argument_list|,
name|suffix
operator|+
literal|1
argument_list|)
expr_stmt|;
name|StringBuilder
name|builder
init|=
operator|new
name|StringBuilder
argument_list|(
name|uniqueName
argument_list|)
decl_stmt|;
name|builder
operator|.
name|append
argument_list|(
literal|"_"
argument_list|)
operator|.
name|append
argument_list|(
name|suffix
argument_list|)
expr_stmt|;
name|uniqueName
operator|=
name|builder
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
name|model
operator|.
name|uniqueName
operator|=
name|uniqueName
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
DECL|method|processResults (List<ApiMethodModel> result)
specifier|protected
name|List
argument_list|<
name|ApiMethodModel
argument_list|>
name|processResults
parameter_list|(
name|List
argument_list|<
name|ApiMethodModel
argument_list|>
name|result
parameter_list|)
block|{
return|return
name|result
return|;
block|}
DECL|method|forName (String className)
specifier|protected
name|Class
argument_list|<
name|?
argument_list|>
name|forName
parameter_list|(
name|String
name|className
parameter_list|)
block|{
try|try
block|{
return|return
name|forName
argument_list|(
name|className
argument_list|,
name|classLoader
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|ClassNotFoundException
name|e1
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Error loading class "
operator|+
name|className
argument_list|)
throw|;
block|}
block|}
DECL|method|forName (String className, ClassLoader classLoader)
specifier|public
specifier|static
name|Class
argument_list|<
name|?
argument_list|>
name|forName
parameter_list|(
name|String
name|className
parameter_list|,
name|ClassLoader
name|classLoader
parameter_list|)
throws|throws
name|ClassNotFoundException
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|result
init|=
literal|null
decl_stmt|;
try|try
block|{
comment|// lookup primitive types first
name|result
operator|=
name|PRIMITIVE_TYPES
operator|.
name|get
argument_list|(
name|className
argument_list|)
expr_stmt|;
if|if
condition|(
name|result
operator|==
literal|null
condition|)
block|{
name|result
operator|=
name|Class
operator|.
name|forName
argument_list|(
name|className
argument_list|,
literal|true
argument_list|,
name|classLoader
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|ClassNotFoundException
name|e
parameter_list|)
block|{
comment|// check if array type
if|if
condition|(
name|className
operator|.
name|endsWith
argument_list|(
literal|"[]"
argument_list|)
condition|)
block|{
specifier|final
name|int
name|firstDim
init|=
name|className
operator|.
name|indexOf
argument_list|(
literal|'['
argument_list|)
decl_stmt|;
specifier|final
name|int
name|nDimensions
init|=
operator|(
name|className
operator|.
name|length
argument_list|()
operator|-
name|firstDim
operator|)
operator|/
literal|2
decl_stmt|;
name|result
operator|=
name|Array
operator|.
name|newInstance
argument_list|(
name|forName
argument_list|(
name|className
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|firstDim
argument_list|)
argument_list|,
name|classLoader
argument_list|)
argument_list|,
operator|new
name|int
index|[
name|nDimensions
index|]
argument_list|)
operator|.
name|getClass
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|className
operator|.
name|indexOf
argument_list|(
literal|'.'
argument_list|)
operator|!=
operator|-
literal|1
condition|)
block|{
comment|// try replacing last '.' with $ to look for inner classes
name|String
name|innerClass
init|=
name|className
decl_stmt|;
while|while
condition|(
name|result
operator|==
literal|null
operator|&&
name|innerClass
operator|.
name|indexOf
argument_list|(
literal|'.'
argument_list|)
operator|!=
operator|-
literal|1
condition|)
block|{
name|int
name|endIndex
init|=
name|innerClass
operator|.
name|lastIndexOf
argument_list|(
literal|'.'
argument_list|)
decl_stmt|;
name|innerClass
operator|=
name|innerClass
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|endIndex
argument_list|)
operator|+
literal|"$"
operator|+
name|innerClass
operator|.
name|substring
argument_list|(
name|endIndex
operator|+
literal|1
argument_list|)
expr_stmt|;
try|try
block|{
name|result
operator|=
name|Class
operator|.
name|forName
argument_list|(
name|innerClass
argument_list|,
literal|true
argument_list|,
name|classLoader
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ClassNotFoundException
name|ignore
parameter_list|)
block|{
comment|// ignore
block|}
block|}
block|}
if|if
condition|(
name|result
operator|==
literal|null
operator|&&
operator|!
name|className
operator|.
name|startsWith
argument_list|(
name|JAVA_LANG
argument_list|)
condition|)
block|{
comment|// try loading from default Java package java.lang
try|try
block|{
name|result
operator|=
name|forName
argument_list|(
name|JAVA_LANG
operator|+
name|className
argument_list|,
name|classLoader
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ClassNotFoundException
name|ignore
parameter_list|)
block|{
comment|// ignore
block|}
block|}
block|}
if|if
condition|(
name|result
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|ClassNotFoundException
argument_list|(
name|className
argument_list|)
throw|;
block|}
return|return
name|result
return|;
block|}
DECL|class|ApiMethodModel
specifier|public
specifier|static
specifier|final
class|class
name|ApiMethodModel
block|{
DECL|field|name
specifier|private
specifier|final
name|String
name|name
decl_stmt|;
DECL|field|resultType
specifier|private
specifier|final
name|Class
argument_list|<
name|?
argument_list|>
name|resultType
decl_stmt|;
DECL|field|arguments
specifier|private
specifier|final
name|List
argument_list|<
name|Argument
argument_list|>
name|arguments
decl_stmt|;
DECL|field|method
specifier|private
specifier|final
name|Method
name|method
decl_stmt|;
DECL|field|uniqueName
specifier|private
name|String
name|uniqueName
decl_stmt|;
DECL|method|ApiMethodModel (String name, Class<?> resultType, List<Argument> arguments, Method method)
specifier|protected
name|ApiMethodModel
parameter_list|(
name|String
name|name
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|resultType
parameter_list|,
name|List
argument_list|<
name|Argument
argument_list|>
name|arguments
parameter_list|,
name|Method
name|method
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
name|this
operator|.
name|resultType
operator|=
name|resultType
expr_stmt|;
name|this
operator|.
name|arguments
operator|=
name|arguments
expr_stmt|;
name|this
operator|.
name|method
operator|=
name|method
expr_stmt|;
block|}
DECL|method|ApiMethodModel (String uniqueName, String name, Class<?> resultType, List<Argument> arguments, Method method)
specifier|protected
name|ApiMethodModel
parameter_list|(
name|String
name|uniqueName
parameter_list|,
name|String
name|name
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|resultType
parameter_list|,
name|List
argument_list|<
name|Argument
argument_list|>
name|arguments
parameter_list|,
name|Method
name|method
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
name|this
operator|.
name|uniqueName
operator|=
name|uniqueName
expr_stmt|;
name|this
operator|.
name|resultType
operator|=
name|resultType
expr_stmt|;
name|this
operator|.
name|arguments
operator|=
name|arguments
expr_stmt|;
name|this
operator|.
name|method
operator|=
name|method
expr_stmt|;
block|}
DECL|method|getUniqueName ()
specifier|public
name|String
name|getUniqueName
parameter_list|()
block|{
return|return
name|uniqueName
return|;
block|}
DECL|method|getName ()
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
DECL|method|getResultType ()
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
name|getResultType
parameter_list|()
block|{
return|return
name|resultType
return|;
block|}
DECL|method|getMethod ()
specifier|public
name|Method
name|getMethod
parameter_list|()
block|{
return|return
name|method
return|;
block|}
DECL|method|getArguments ()
specifier|public
name|List
argument_list|<
name|Argument
argument_list|>
name|getArguments
parameter_list|()
block|{
return|return
name|arguments
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
name|StringBuilder
name|builder
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|builder
operator|.
name|append
argument_list|(
name|resultType
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|" "
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
name|name
argument_list|)
operator|.
name|append
argument_list|(
literal|"("
argument_list|)
expr_stmt|;
for|for
control|(
name|Argument
name|argument
range|:
name|arguments
control|)
block|{
name|builder
operator|.
name|append
argument_list|(
name|argument
operator|.
name|getType
argument_list|()
operator|.
name|getCanonicalName
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|" "
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
name|argument
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|", "
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|arguments
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|builder
operator|.
name|delete
argument_list|(
name|builder
operator|.
name|length
argument_list|()
operator|-
literal|2
argument_list|,
name|builder
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|builder
operator|.
name|append
argument_list|(
literal|");"
argument_list|)
expr_stmt|;
return|return
name|builder
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
DECL|class|Argument
specifier|public
specifier|static
specifier|final
class|class
name|Argument
block|{
DECL|field|name
specifier|private
specifier|final
name|String
name|name
decl_stmt|;
DECL|field|type
specifier|private
specifier|final
name|Class
argument_list|<
name|?
argument_list|>
name|type
decl_stmt|;
DECL|field|typeArgs
specifier|private
specifier|final
name|String
name|typeArgs
decl_stmt|;
DECL|method|Argument (String name, Class<?> type, String typeArgs)
specifier|public
name|Argument
parameter_list|(
name|String
name|name
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|,
name|String
name|typeArgs
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
name|this
operator|.
name|typeArgs
operator|=
name|typeArgs
expr_stmt|;
block|}
DECL|method|getName ()
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
DECL|method|getType ()
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
name|getType
parameter_list|()
block|{
return|return
name|type
return|;
block|}
DECL|method|getTypeArgs ()
specifier|public
name|String
name|getTypeArgs
parameter_list|()
block|{
return|return
name|typeArgs
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
name|StringBuilder
name|builder
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|builder
operator|.
name|append
argument_list|(
name|type
operator|.
name|getCanonicalName
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|typeArgs
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|append
argument_list|(
literal|"<"
argument_list|)
operator|.
name|append
argument_list|(
name|typeArgs
argument_list|)
operator|.
name|append
argument_list|(
literal|">"
argument_list|)
expr_stmt|;
block|}
name|builder
operator|.
name|append
argument_list|(
literal|" "
argument_list|)
operator|.
name|append
argument_list|(
name|name
argument_list|)
expr_stmt|;
return|return
name|builder
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
block|}
end_class

end_unit

