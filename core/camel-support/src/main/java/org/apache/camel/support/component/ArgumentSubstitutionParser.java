begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.support.component
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|support
operator|.
name|component
package|;
end_package

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
name|LinkedHashMap
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

begin_comment
comment|/**  * Adds support for parameter name substitutions.  */
end_comment

begin_class
DECL|class|ArgumentSubstitutionParser
specifier|public
class|class
name|ArgumentSubstitutionParser
parameter_list|<
name|T
parameter_list|>
extends|extends
name|ApiMethodParser
argument_list|<
name|T
argument_list|>
block|{
DECL|field|methodMap
specifier|private
specifier|final
name|Map
argument_list|<
name|Pattern
argument_list|,
name|Map
argument_list|<
name|Pattern
argument_list|,
name|List
argument_list|<
name|NameReplacement
argument_list|>
argument_list|>
argument_list|>
name|methodMap
decl_stmt|;
comment|/**      * Create a parser using regular expressions to adapt parameter names.      * @param proxyType Proxy class.      * @param substitutions an array of<b>ordered</b> Argument adapters.      */
DECL|method|ArgumentSubstitutionParser (Class<T> proxyType, Substitution[] substitutions)
specifier|public
name|ArgumentSubstitutionParser
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|proxyType
parameter_list|,
name|Substitution
index|[]
name|substitutions
parameter_list|)
block|{
name|super
argument_list|(
name|proxyType
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|NameReplacement
argument_list|>
argument_list|>
argument_list|>
name|regexMap
init|=
operator|new
name|LinkedHashMap
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Substitution
name|substitution
range|:
name|substitutions
control|)
block|{
name|substitution
operator|.
name|validate
argument_list|()
expr_stmt|;
specifier|final
name|NameReplacement
name|nameReplacement
init|=
operator|new
name|NameReplacement
argument_list|()
decl_stmt|;
name|nameReplacement
operator|.
name|replacement
operator|=
name|substitution
operator|.
name|replacement
expr_stmt|;
if|if
condition|(
name|substitution
operator|.
name|argType
operator|!=
literal|null
condition|)
block|{
name|nameReplacement
operator|.
name|typePattern
operator|=
name|Pattern
operator|.
name|compile
argument_list|(
name|substitution
operator|.
name|argType
argument_list|)
expr_stmt|;
block|}
name|nameReplacement
operator|.
name|replaceWithType
operator|=
name|substitution
operator|.
name|replaceWithType
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|NameReplacement
argument_list|>
argument_list|>
name|replacementMap
init|=
name|regexMap
operator|.
name|get
argument_list|(
name|substitution
operator|.
name|method
argument_list|)
decl_stmt|;
if|if
condition|(
name|replacementMap
operator|==
literal|null
condition|)
block|{
name|replacementMap
operator|=
operator|new
name|LinkedHashMap
argument_list|<>
argument_list|()
expr_stmt|;
name|regexMap
operator|.
name|put
argument_list|(
name|substitution
operator|.
name|method
argument_list|,
name|replacementMap
argument_list|)
expr_stmt|;
block|}
name|List
argument_list|<
name|NameReplacement
argument_list|>
name|replacements
init|=
name|replacementMap
operator|.
name|get
argument_list|(
name|substitution
operator|.
name|argName
argument_list|)
decl_stmt|;
if|if
condition|(
name|replacements
operator|==
literal|null
condition|)
block|{
name|replacements
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
name|replacementMap
operator|.
name|put
argument_list|(
name|substitution
operator|.
name|argName
argument_list|,
name|replacements
argument_list|)
expr_stmt|;
block|}
name|replacements
operator|.
name|add
argument_list|(
name|nameReplacement
argument_list|)
expr_stmt|;
block|}
comment|// now compile the patterns, all this because Pattern doesn't override equals()!!!
name|this
operator|.
name|methodMap
operator|=
operator|new
name|LinkedHashMap
argument_list|<>
argument_list|()
expr_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|NameReplacement
argument_list|>
argument_list|>
argument_list|>
name|method
range|:
name|regexMap
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|Map
argument_list|<
name|Pattern
argument_list|,
name|List
argument_list|<
name|NameReplacement
argument_list|>
argument_list|>
name|argMap
init|=
operator|new
name|LinkedHashMap
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|NameReplacement
argument_list|>
argument_list|>
name|arg
range|:
name|method
operator|.
name|getValue
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|argMap
operator|.
name|put
argument_list|(
name|Pattern
operator|.
name|compile
argument_list|(
name|arg
operator|.
name|getKey
argument_list|()
argument_list|)
argument_list|,
name|arg
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|methodMap
operator|.
name|put
argument_list|(
name|Pattern
operator|.
name|compile
argument_list|(
name|method
operator|.
name|getKey
argument_list|()
argument_list|)
argument_list|,
name|argMap
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|processResults (List<ApiMethodModel> parseResult)
specifier|public
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
name|parseResult
parameter_list|)
block|{
specifier|final
name|List
argument_list|<
name|ApiMethodModel
argument_list|>
name|result
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|ApiMethodModel
name|model
range|:
name|parseResult
control|)
block|{
comment|// look for method name matches
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|Pattern
argument_list|,
name|Map
argument_list|<
name|Pattern
argument_list|,
name|List
argument_list|<
name|NameReplacement
argument_list|>
argument_list|>
argument_list|>
name|methodEntry
range|:
name|methodMap
operator|.
name|entrySet
argument_list|()
control|)
block|{
comment|// match the whole method name
if|if
condition|(
name|methodEntry
operator|.
name|getKey
argument_list|()
operator|.
name|matcher
argument_list|(
name|model
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|matches
argument_list|()
condition|)
block|{
comment|// look for arg name matches
specifier|final
name|List
argument_list|<
name|ApiMethodArg
argument_list|>
name|updatedArguments
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
specifier|final
name|Map
argument_list|<
name|Pattern
argument_list|,
name|List
argument_list|<
name|NameReplacement
argument_list|>
argument_list|>
name|argMap
init|=
name|methodEntry
operator|.
name|getValue
argument_list|()
decl_stmt|;
for|for
control|(
name|ApiMethodArg
name|argument
range|:
name|model
operator|.
name|getArguments
argument_list|()
control|)
block|{
specifier|final
name|Class
argument_list|<
name|?
argument_list|>
name|argType
init|=
name|argument
operator|.
name|getType
argument_list|()
decl_stmt|;
specifier|final
name|String
name|typeArgs
init|=
name|argument
operator|.
name|getTypeArgs
argument_list|()
decl_stmt|;
specifier|final
name|String
name|argTypeName
init|=
name|argType
operator|.
name|getCanonicalName
argument_list|()
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|Pattern
argument_list|,
name|List
argument_list|<
name|NameReplacement
argument_list|>
argument_list|>
name|argEntry
range|:
name|argMap
operator|.
name|entrySet
argument_list|()
control|)
block|{
specifier|final
name|Matcher
name|matcher
init|=
name|argEntry
operator|.
name|getKey
argument_list|()
operator|.
name|matcher
argument_list|(
name|argument
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
comment|// match argument name substring
if|if
condition|(
name|matcher
operator|.
name|find
argument_list|()
condition|)
block|{
specifier|final
name|List
argument_list|<
name|NameReplacement
argument_list|>
name|adapters
init|=
name|argEntry
operator|.
name|getValue
argument_list|()
decl_stmt|;
for|for
control|(
name|NameReplacement
name|adapter
range|:
name|adapters
control|)
block|{
if|if
condition|(
name|adapter
operator|.
name|typePattern
operator|==
literal|null
condition|)
block|{
comment|// no type pattern
specifier|final
name|String
name|newName
init|=
name|getJavaArgName
argument_list|(
name|matcher
operator|.
name|replaceAll
argument_list|(
name|adapter
operator|.
name|replacement
argument_list|)
argument_list|)
decl_stmt|;
name|argument
operator|=
operator|new
name|ApiMethodArg
argument_list|(
name|newName
argument_list|,
name|argType
argument_list|,
name|typeArgs
argument_list|)
expr_stmt|;
block|}
else|else
block|{
specifier|final
name|Matcher
name|typeMatcher
init|=
name|adapter
operator|.
name|typePattern
operator|.
name|matcher
argument_list|(
name|argTypeName
argument_list|)
decl_stmt|;
if|if
condition|(
name|typeMatcher
operator|.
name|find
argument_list|()
condition|)
block|{
if|if
condition|(
operator|!
name|adapter
operator|.
name|replaceWithType
condition|)
block|{
comment|// replace argument name
specifier|final
name|String
name|newName
init|=
name|getJavaArgName
argument_list|(
name|matcher
operator|.
name|replaceAll
argument_list|(
name|adapter
operator|.
name|replacement
argument_list|)
argument_list|)
decl_stmt|;
name|argument
operator|=
operator|new
name|ApiMethodArg
argument_list|(
name|newName
argument_list|,
name|argType
argument_list|,
name|typeArgs
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// replace name with argument type name
specifier|final
name|String
name|newName
init|=
name|getJavaArgName
argument_list|(
name|typeMatcher
operator|.
name|replaceAll
argument_list|(
name|adapter
operator|.
name|replacement
argument_list|)
argument_list|)
decl_stmt|;
name|argument
operator|=
operator|new
name|ApiMethodArg
argument_list|(
name|newName
argument_list|,
name|argType
argument_list|,
name|typeArgs
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
block|}
name|updatedArguments
operator|.
name|add
argument_list|(
name|argument
argument_list|)
expr_stmt|;
block|}
name|model
operator|=
operator|new
name|ApiMethodModel
argument_list|(
name|model
operator|.
name|getUniqueName
argument_list|()
argument_list|,
name|model
operator|.
name|getName
argument_list|()
argument_list|,
name|model
operator|.
name|getResultType
argument_list|()
argument_list|,
name|updatedArguments
argument_list|,
name|model
operator|.
name|getMethod
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|result
operator|.
name|add
argument_list|(
name|model
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
DECL|method|getJavaArgName (String name)
specifier|private
name|String
name|getJavaArgName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
comment|// make sure the first character is lowercase
comment|// useful for replacement using type names
name|char
name|firstChar
init|=
name|name
operator|.
name|charAt
argument_list|(
literal|0
argument_list|)
decl_stmt|;
if|if
condition|(
name|Character
operator|.
name|isLowerCase
argument_list|(
name|firstChar
argument_list|)
condition|)
block|{
return|return
name|name
return|;
block|}
else|else
block|{
return|return
name|Character
operator|.
name|toLowerCase
argument_list|(
name|firstChar
argument_list|)
operator|+
name|name
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
return|;
block|}
block|}
DECL|class|Substitution
specifier|public
specifier|static
class|class
name|Substitution
block|{
DECL|field|method
specifier|private
name|String
name|method
decl_stmt|;
DECL|field|argName
specifier|private
name|String
name|argName
decl_stmt|;
DECL|field|argType
specifier|private
name|String
name|argType
decl_stmt|;
DECL|field|replacement
specifier|private
name|String
name|replacement
decl_stmt|;
DECL|field|replaceWithType
specifier|private
name|boolean
name|replaceWithType
decl_stmt|;
comment|/**          * Creates a substitution for all argument types.          * @param method regex to match method name          * @param argName regex to match argument name          * @param replacement replacement text for argument name          */
DECL|method|Substitution (String method, String argName, String replacement)
specifier|public
name|Substitution
parameter_list|(
name|String
name|method
parameter_list|,
name|String
name|argName
parameter_list|,
name|String
name|replacement
parameter_list|)
block|{
name|this
operator|.
name|method
operator|=
name|method
expr_stmt|;
name|this
operator|.
name|argName
operator|=
name|argName
expr_stmt|;
name|this
operator|.
name|replacement
operator|=
name|replacement
expr_stmt|;
block|}
comment|/**          * Creates a substitution for a specific argument type.          * @param method regex to match method name          * @param argName regex to match argument name          * @param argType argument type as String          * @param replacement replacement text for argument name          */
DECL|method|Substitution (String method, String argName, String argType, String replacement)
specifier|public
name|Substitution
parameter_list|(
name|String
name|method
parameter_list|,
name|String
name|argName
parameter_list|,
name|String
name|argType
parameter_list|,
name|String
name|replacement
parameter_list|)
block|{
name|this
argument_list|(
name|method
argument_list|,
name|argName
argument_list|,
name|replacement
argument_list|)
expr_stmt|;
name|this
operator|.
name|argType
operator|=
name|argType
expr_stmt|;
block|}
comment|/**          * Create a substitution for a specific argument type and flag to indicate whether the replacement uses          * @param method          * @param argName          * @param argType          * @param replacement          * @param replaceWithType          */
DECL|method|Substitution (String method, String argName, String argType, String replacement, boolean replaceWithType)
specifier|public
name|Substitution
parameter_list|(
name|String
name|method
parameter_list|,
name|String
name|argName
parameter_list|,
name|String
name|argType
parameter_list|,
name|String
name|replacement
parameter_list|,
name|boolean
name|replaceWithType
parameter_list|)
block|{
name|this
argument_list|(
name|method
argument_list|,
name|argName
argument_list|,
name|argType
argument_list|,
name|replacement
argument_list|)
expr_stmt|;
name|this
operator|.
name|replaceWithType
operator|=
name|replaceWithType
expr_stmt|;
block|}
DECL|method|validate ()
specifier|public
name|void
name|validate
parameter_list|()
block|{
if|if
condition|(
name|method
operator|==
literal|null
operator|||
name|argName
operator|==
literal|null
operator|||
name|replacement
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Properties method, argName and replacement MUST be provided"
argument_list|)
throw|;
block|}
block|}
block|}
DECL|class|NameReplacement
specifier|private
specifier|static
class|class
name|NameReplacement
block|{
DECL|field|replacement
specifier|private
name|String
name|replacement
decl_stmt|;
DECL|field|typePattern
specifier|private
name|Pattern
name|typePattern
decl_stmt|;
DECL|field|replaceWithType
specifier|private
name|boolean
name|replaceWithType
decl_stmt|;
block|}
block|}
end_class

end_unit

