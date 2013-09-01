begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.facebook.data
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|facebook
operator|.
name|data
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
name|Collection
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
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
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
name|Set
import|;
end_import

begin_import
import|import
name|facebook4j
operator|.
name|Facebook
import|;
end_import

begin_import
import|import
name|facebook4j
operator|.
name|FacebookException
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
name|component
operator|.
name|facebook
operator|.
name|FacebookConstants
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
name|component
operator|.
name|facebook
operator|.
name|config
operator|.
name|FacebookNameStyle
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
comment|/**  * Helper class for working with {@link FacebookMethodsType}.  */
end_comment

begin_class
DECL|class|FacebookMethodsTypeHelper
specifier|public
specifier|final
class|class
name|FacebookMethodsTypeHelper
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|FacebookMethodsTypeHelper
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// maps method name to FacebookMethodsType
DECL|field|METHOD_MAP
specifier|private
specifier|static
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|FacebookMethodsType
argument_list|>
argument_list|>
name|METHOD_MAP
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|FacebookMethodsType
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
comment|// maps method name to method arguments of the form Class type1, String name1, Class type2, String name2,...
DECL|field|ARGUMENTS_MAP
specifier|private
specifier|static
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|Object
argument_list|>
argument_list|>
name|ARGUMENTS_MAP
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|Object
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
comment|// maps argument name to argument type
DECL|field|VALID_ARGUMENTS
specifier|private
specifier|static
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Class
argument_list|>
name|VALID_ARGUMENTS
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Class
argument_list|>
argument_list|()
decl_stmt|;
static|static
block|{
specifier|final
name|FacebookMethodsType
index|[]
name|methods
init|=
name|FacebookMethodsType
operator|.
name|values
argument_list|()
decl_stmt|;
comment|// load lookup maps for FacebookMethodsType
for|for
control|(
name|FacebookMethodsType
name|method
range|:
name|methods
control|)
block|{
comment|// map method name to Enum
specifier|final
name|String
name|name
init|=
name|method
operator|.
name|getName
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|FacebookMethodsType
argument_list|>
name|overloads
init|=
name|METHOD_MAP
operator|.
name|get
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|overloads
operator|==
literal|null
condition|)
block|{
name|overloads
operator|=
operator|new
name|ArrayList
argument_list|<
name|FacebookMethodsType
argument_list|>
argument_list|()
expr_stmt|;
name|METHOD_MAP
operator|.
name|put
argument_list|(
name|method
operator|.
name|getName
argument_list|()
argument_list|,
name|overloads
argument_list|)
expr_stmt|;
block|}
name|overloads
operator|.
name|add
argument_list|(
name|method
argument_list|)
expr_stmt|;
comment|// add arguments for this method
name|List
argument_list|<
name|Object
argument_list|>
name|arguments
init|=
name|ARGUMENTS_MAP
operator|.
name|get
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|arguments
operator|==
literal|null
condition|)
block|{
name|arguments
operator|=
operator|new
name|ArrayList
argument_list|<
name|Object
argument_list|>
argument_list|()
expr_stmt|;
name|ARGUMENTS_MAP
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|arguments
argument_list|)
expr_stmt|;
block|}
comment|// process all arguments for this method
specifier|final
name|int
name|nArgs
init|=
name|method
operator|.
name|getArgNames
argument_list|()
operator|.
name|size
argument_list|()
decl_stmt|;
specifier|final
name|String
index|[]
name|argNames
init|=
name|method
operator|.
name|getArgNames
argument_list|()
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
name|nArgs
index|]
argument_list|)
decl_stmt|;
specifier|final
name|Class
index|[]
name|argTypes
init|=
name|method
operator|.
name|getArgTypes
argument_list|()
operator|.
name|toArray
argument_list|(
operator|new
name|Class
index|[
name|nArgs
index|]
argument_list|)
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
name|nArgs
condition|;
name|i
operator|++
control|)
block|{
specifier|final
name|String
name|argName
init|=
name|argNames
index|[
name|i
index|]
decl_stmt|;
specifier|final
name|Class
name|argType
init|=
name|argTypes
index|[
name|i
index|]
decl_stmt|;
if|if
condition|(
operator|!
name|arguments
operator|.
name|contains
argument_list|(
name|argName
argument_list|)
condition|)
block|{
name|arguments
operator|.
name|add
argument_list|(
name|argType
argument_list|)
expr_stmt|;
name|arguments
operator|.
name|add
argument_list|(
name|argName
argument_list|)
expr_stmt|;
block|}
comment|// also collect argument names for all methods, also detect clashes here
specifier|final
name|Class
name|previousType
init|=
name|VALID_ARGUMENTS
operator|.
name|get
argument_list|(
name|argName
argument_list|)
decl_stmt|;
if|if
condition|(
name|previousType
operator|!=
literal|null
operator|&&
name|previousType
operator|!=
name|argType
condition|)
block|{
throw|throw
operator|new
name|ExceptionInInitializerError
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Argument %s has ambiguous types (%s, %s) across methods!"
argument_list|,
name|name
argument_list|,
name|previousType
argument_list|,
name|argType
argument_list|)
argument_list|)
throw|;
block|}
elseif|else
if|if
condition|(
name|previousType
operator|==
literal|null
condition|)
block|{
name|VALID_ARGUMENTS
operator|.
name|put
argument_list|(
name|argName
argument_list|,
name|argType
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|// add endpoint parameter inBody for producers
name|VALID_ARGUMENTS
operator|.
name|put
argument_list|(
name|FacebookConstants
operator|.
name|IN_BODY_PROPERTY
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Found {} unique method names in {} methods"
argument_list|,
name|METHOD_MAP
operator|.
name|size
argument_list|()
argument_list|,
name|methods
operator|.
name|length
argument_list|)
expr_stmt|;
block|}
DECL|method|FacebookMethodsTypeHelper ()
specifier|private
name|FacebookMethodsTypeHelper
parameter_list|()
block|{     }
comment|/**      * Gets methods that match the given name and arguments.<p/>      * Note that the args list is a required subset of arguments for returned methods.      * @param name case sensitive full method name to lookup      * @param argNames unordered required argument names      * @return non-null unmodifiable list of methods that take all of the given arguments, empty if there is no match      */
DECL|method|getCandidateMethods (String name, String... argNames)
specifier|public
specifier|static
name|List
argument_list|<
name|FacebookMethodsType
argument_list|>
name|getCandidateMethods
parameter_list|(
name|String
name|name
parameter_list|,
name|String
modifier|...
name|argNames
parameter_list|)
block|{
specifier|final
name|List
argument_list|<
name|FacebookMethodsType
argument_list|>
name|methods
init|=
name|METHOD_MAP
operator|.
name|get
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|methods
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"No matching method for method {}"
argument_list|,
name|name
argument_list|)
expr_stmt|;
return|return
name|Collections
operator|.
name|emptyList
argument_list|()
return|;
block|}
name|int
name|nArgs
init|=
name|argNames
operator|!=
literal|null
condition|?
name|argNames
operator|.
name|length
else|:
literal|0
decl_stmt|;
if|if
condition|(
name|nArgs
operator|==
literal|0
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Found {} methods for method {}"
argument_list|,
name|methods
operator|.
name|size
argument_list|()
argument_list|,
name|name
argument_list|)
expr_stmt|;
return|return
name|Collections
operator|.
name|unmodifiableList
argument_list|(
name|methods
argument_list|)
return|;
block|}
else|else
block|{
specifier|final
name|List
argument_list|<
name|FacebookMethodsType
argument_list|>
name|filteredSet
init|=
name|filterMethods
argument_list|(
name|methods
argument_list|,
name|MatchType
operator|.
name|SUBSET
argument_list|,
name|argNames
argument_list|)
decl_stmt|;
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
literal|"Found {} filtered methods for {}"
argument_list|,
name|filteredSet
operator|.
name|size
argument_list|()
argument_list|,
name|name
operator|+
name|Arrays
operator|.
name|toString
argument_list|(
name|argNames
argument_list|)
operator|.
name|replace
argument_list|(
literal|'['
argument_list|,
literal|'('
argument_list|)
operator|.
name|replace
argument_list|(
literal|']'
argument_list|,
literal|')'
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|filteredSet
return|;
block|}
block|}
comment|/**      * Filters a list of methods to those that take the given set of arguments.      *      * @param methods list of methods to filter      * @param matchType whether the arguments are an exact match, a subset or a super set of method args      * @param argNames argument names to filter the list      * @return methods with arguments that satisfy the match type.<p/>      * For SUPER_SET match, if methods with exact match are found, methods that take a subset are ignored      */
DECL|method|filterMethods (List<FacebookMethodsType> methods, MatchType matchType, String... argNames)
specifier|public
specifier|static
name|List
argument_list|<
name|FacebookMethodsType
argument_list|>
name|filterMethods
parameter_list|(
name|List
argument_list|<
name|FacebookMethodsType
argument_list|>
name|methods
parameter_list|,
name|MatchType
name|matchType
parameter_list|,
name|String
modifier|...
name|argNames
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|argsList
init|=
name|Arrays
operator|.
name|asList
argument_list|(
name|argNames
argument_list|)
decl_stmt|;
comment|// list of methods that have all args in the given names
specifier|final
name|List
argument_list|<
name|FacebookMethodsType
argument_list|>
name|result
init|=
operator|new
name|ArrayList
argument_list|<
name|FacebookMethodsType
argument_list|>
argument_list|()
decl_stmt|;
specifier|final
name|List
argument_list|<
name|FacebookMethodsType
argument_list|>
name|extraArgs
init|=
operator|new
name|ArrayList
argument_list|<
name|FacebookMethodsType
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|FacebookMethodsType
name|method
range|:
name|methods
control|)
block|{
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|methodArgs
init|=
name|method
operator|.
name|getArgNames
argument_list|()
decl_stmt|;
switch|switch
condition|(
name|matchType
condition|)
block|{
case|case
name|EXACT
case|:
comment|// method must take all args, and no more
if|if
condition|(
name|methodArgs
operator|.
name|containsAll
argument_list|(
name|argsList
argument_list|)
operator|&&
name|argsList
operator|.
name|containsAll
argument_list|(
name|methodArgs
argument_list|)
condition|)
block|{
name|result
operator|.
name|add
argument_list|(
name|method
argument_list|)
expr_stmt|;
block|}
break|break;
case|case
name|SUBSET
case|:
comment|// all args are required, method may take more
if|if
condition|(
name|methodArgs
operator|.
name|containsAll
argument_list|(
name|argsList
argument_list|)
condition|)
block|{
name|result
operator|.
name|add
argument_list|(
name|method
argument_list|)
expr_stmt|;
block|}
break|break;
default|default:
case|case
name|SUPER_SET
case|:
comment|// all method args must be present
if|if
condition|(
name|argsList
operator|.
name|containsAll
argument_list|(
name|methodArgs
argument_list|)
condition|)
block|{
if|if
condition|(
name|methodArgs
operator|.
name|containsAll
argument_list|(
name|argsList
argument_list|)
condition|)
block|{
comment|// prefer exact match to avoid unused args
name|result
operator|.
name|add
argument_list|(
name|method
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// method takes a subset, unused args
name|extraArgs
operator|.
name|add
argument_list|(
name|method
argument_list|)
expr_stmt|;
block|}
block|}
break|break;
block|}
block|}
return|return
name|Collections
operator|.
name|unmodifiableList
argument_list|(
name|result
operator|.
name|isEmpty
argument_list|()
condition|?
name|extraArgs
else|:
name|result
argument_list|)
return|;
block|}
comment|/**      * Gets argument types and names for all overloaded methods with the given name.      * @param name method name, must be a long form (i.e. get*, or search*)      * @return list of arguments of the form Class type1, String name1, Class type2, String name2,...      */
DECL|method|getArguments (String name)
specifier|public
specifier|static
name|List
argument_list|<
name|Object
argument_list|>
name|getArguments
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|IllegalArgumentException
block|{
specifier|final
name|List
argument_list|<
name|Object
argument_list|>
name|arguments
init|=
name|ARGUMENTS_MAP
operator|.
name|get
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|arguments
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|name
argument_list|)
throw|;
block|}
return|return
name|Collections
operator|.
name|unmodifiableList
argument_list|(
name|arguments
argument_list|)
return|;
block|}
comment|/**      * Gets argument types and names for all overloaded methods with the given short form name.      * @param name method name, may be a short form      * @param style name style      * @return list of arguments of the form Class type1, String name1, Class type2, String name2,...      */
DECL|method|getArgumentsForNameStyle (String name, FacebookNameStyle style)
specifier|public
specifier|static
name|List
argument_list|<
name|Object
argument_list|>
name|getArgumentsForNameStyle
parameter_list|(
name|String
name|name
parameter_list|,
name|FacebookNameStyle
name|style
parameter_list|)
throws|throws
name|IllegalArgumentException
block|{
if|if
condition|(
name|style
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Parameters style cannot be null"
argument_list|)
throw|;
block|}
switch|switch
condition|(
name|style
condition|)
block|{
case|case
name|EXACT
case|:
return|return
name|getArguments
argument_list|(
name|name
argument_list|)
return|;
case|case
name|GET
case|:
return|return
name|getArguments
argument_list|(
name|convertToGetMethod
argument_list|(
name|name
argument_list|)
argument_list|)
return|;
case|case
name|SEARCH
case|:
return|return
name|getArguments
argument_list|(
name|convertToSearchMethod
argument_list|(
name|name
argument_list|)
argument_list|)
return|;
case|case
name|GET_AND_SEARCH
case|:
default|default:
specifier|final
name|List
argument_list|<
name|Object
argument_list|>
name|arguments
init|=
operator|new
name|ArrayList
argument_list|<
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|arguments
operator|.
name|addAll
argument_list|(
name|getArguments
argument_list|(
name|convertToGetMethod
argument_list|(
name|name
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|arguments
operator|.
name|addAll
argument_list|(
name|getArguments
argument_list|(
name|convertToSearchMethod
argument_list|(
name|name
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|Collections
operator|.
name|unmodifiableList
argument_list|(
name|arguments
argument_list|)
return|;
block|}
block|}
comment|/**      * Get missing properties.      * @param methodName method name      * @param nameStyle method name style      * @param argNames available arguments      * @return Set of missing argument names      */
DECL|method|getMissingProperties (String methodName, FacebookNameStyle nameStyle, Set<String> argNames)
specifier|public
specifier|static
name|Set
argument_list|<
name|String
argument_list|>
name|getMissingProperties
parameter_list|(
name|String
name|methodName
parameter_list|,
name|FacebookNameStyle
name|nameStyle
parameter_list|,
name|Set
argument_list|<
name|String
argument_list|>
name|argNames
parameter_list|)
block|{
specifier|final
name|List
argument_list|<
name|Object
argument_list|>
name|argsWithTypes
init|=
name|getArgumentsForNameStyle
argument_list|(
name|methodName
argument_list|,
name|nameStyle
argument_list|)
decl_stmt|;
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|missingArgs
init|=
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|1
init|;
name|i
operator|<
name|argsWithTypes
operator|.
name|size
argument_list|()
condition|;
name|i
operator|+=
literal|2
control|)
block|{
specifier|final
name|String
name|name
init|=
operator|(
name|String
operator|)
name|argsWithTypes
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|argNames
operator|.
name|contains
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|missingArgs
operator|.
name|add
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|missingArgs
return|;
block|}
comment|/**      * Get argument types and names used by all methods.      * @return map with argument names as keys, and types as values      */
DECL|method|allArguments ()
specifier|public
specifier|static
name|Map
argument_list|<
name|String
argument_list|,
name|Class
argument_list|>
name|allArguments
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|unmodifiableMap
argument_list|(
name|VALID_ARGUMENTS
argument_list|)
return|;
block|}
comment|/**      * Get the type for the given argument name.      * @param argName argument name      * @return argument type      */
DECL|method|getType (String argName)
specifier|public
specifier|static
name|Class
name|getType
parameter_list|(
name|String
name|argName
parameter_list|)
throws|throws
name|IllegalArgumentException
block|{
specifier|final
name|Class
name|type
init|=
name|VALID_ARGUMENTS
operator|.
name|get
argument_list|(
name|argName
argument_list|)
decl_stmt|;
if|if
condition|(
name|type
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|argName
argument_list|)
throw|;
block|}
return|return
name|type
return|;
block|}
DECL|method|convertToGetMethod (String name)
specifier|public
specifier|static
name|String
name|convertToGetMethod
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|IllegalArgumentException
block|{
if|if
condition|(
name|name
operator|==
literal|null
operator|||
name|name
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Name cannot be null or empty"
argument_list|)
throw|;
block|}
return|return
literal|"get"
operator|+
name|Character
operator|.
name|toUpperCase
argument_list|(
name|name
operator|.
name|charAt
argument_list|(
literal|0
argument_list|)
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
DECL|method|convertToSearchMethod (String name)
specifier|public
specifier|static
name|String
name|convertToSearchMethod
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|IllegalArgumentException
block|{
if|if
condition|(
name|name
operator|==
literal|null
operator|||
name|name
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Name cannot be null or empty"
argument_list|)
throw|;
block|}
return|return
literal|"search"
operator|+
name|Character
operator|.
name|toUpperCase
argument_list|(
name|name
operator|.
name|charAt
argument_list|(
literal|0
argument_list|)
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
DECL|method|getHighestPriorityMethod (List<FacebookMethodsType> filteredMethods)
specifier|public
specifier|static
name|FacebookMethodsType
name|getHighestPriorityMethod
parameter_list|(
name|List
argument_list|<
name|FacebookMethodsType
argument_list|>
name|filteredMethods
parameter_list|)
block|{
name|FacebookMethodsType
name|highest
init|=
literal|null
decl_stmt|;
for|for
control|(
name|FacebookMethodsType
name|method
range|:
name|filteredMethods
control|)
block|{
if|if
condition|(
name|highest
operator|==
literal|null
operator|||
name|method
operator|.
name|ordinal
argument_list|()
operator|>
name|highest
operator|.
name|ordinal
argument_list|()
condition|)
block|{
name|highest
operator|=
name|method
expr_stmt|;
block|}
block|}
return|return
name|highest
return|;
block|}
comment|/**      * Invokes given method with argument values from given properties.      *      * @param facebook Facebook4J target object for invoke      * @param method method to invoke      * @param properties Map of arguments      * @return result of method invocation      * @throws RuntimeCamelException on errors      */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|invokeMethod (Facebook facebook, FacebookMethodsType method, Map<String, Object> properties)
specifier|public
specifier|static
name|Object
name|invokeMethod
parameter_list|(
name|Facebook
name|facebook
parameter_list|,
name|FacebookMethodsType
name|method
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
name|RuntimeCamelException
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Invoking {} with arguments {}"
argument_list|,
name|method
operator|.
name|getName
argument_list|()
argument_list|,
name|properties
argument_list|)
expr_stmt|;
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|argNames
init|=
name|method
operator|.
name|getArgNames
argument_list|()
decl_stmt|;
specifier|final
name|Object
index|[]
name|values
init|=
operator|new
name|Object
index|[
name|argNames
operator|.
name|size
argument_list|()
index|]
decl_stmt|;
specifier|final
name|List
argument_list|<
name|Class
argument_list|>
name|argTypes
init|=
name|method
operator|.
name|getArgTypes
argument_list|()
decl_stmt|;
specifier|final
name|Class
index|[]
name|types
init|=
name|argTypes
operator|.
name|toArray
argument_list|(
operator|new
name|Class
index|[
name|argTypes
operator|.
name|size
argument_list|()
index|]
argument_list|)
decl_stmt|;
name|int
name|index
init|=
literal|0
decl_stmt|;
for|for
control|(
name|String
name|name
range|:
name|argNames
control|)
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
comment|// is the parameter an array type?
if|if
condition|(
name|value
operator|!=
literal|null
operator|&&
name|types
index|[
name|index
index|]
operator|.
name|isArray
argument_list|()
condition|)
block|{
name|Class
name|type
init|=
name|types
index|[
name|index
index|]
decl_stmt|;
if|if
condition|(
name|value
operator|instanceof
name|Collection
condition|)
block|{
comment|// convert collection to array
name|Collection
name|collection
init|=
operator|(
name|Collection
operator|)
name|value
decl_stmt|;
name|Object
name|array
init|=
name|Array
operator|.
name|newInstance
argument_list|(
name|type
operator|.
name|getComponentType
argument_list|()
argument_list|,
name|collection
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|array
operator|instanceof
name|Object
index|[]
condition|)
block|{
name|collection
operator|.
name|toArray
argument_list|(
operator|(
name|Object
index|[]
operator|)
name|array
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|int
name|i
init|=
literal|0
decl_stmt|;
for|for
control|(
name|Object
name|el
range|:
name|collection
control|)
block|{
name|Array
operator|.
name|set
argument_list|(
name|array
argument_list|,
name|i
operator|++
argument_list|,
name|el
argument_list|)
expr_stmt|;
block|}
block|}
name|value
operator|=
name|array
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|value
operator|.
name|getClass
argument_list|()
operator|.
name|isArray
argument_list|()
operator|&&
name|type
operator|.
name|getComponentType
argument_list|()
operator|.
name|isAssignableFrom
argument_list|(
name|value
operator|.
name|getClass
argument_list|()
operator|.
name|getComponentType
argument_list|()
argument_list|)
condition|)
block|{
comment|// convert derived array to super array
specifier|final
name|int
name|size
init|=
name|Array
operator|.
name|getLength
argument_list|(
name|value
argument_list|)
decl_stmt|;
name|Object
name|array
init|=
name|Array
operator|.
name|newInstance
argument_list|(
name|type
operator|.
name|getComponentType
argument_list|()
argument_list|,
name|size
argument_list|)
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
name|size
condition|;
name|i
operator|++
control|)
block|{
name|Array
operator|.
name|set
argument_list|(
name|array
argument_list|,
name|i
argument_list|,
name|Array
operator|.
name|get
argument_list|(
name|value
argument_list|,
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|value
operator|=
name|array
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Cannot convert %s to %s"
argument_list|,
name|value
operator|.
name|getClass
argument_list|()
argument_list|,
name|type
argument_list|)
argument_list|)
throw|;
block|}
block|}
name|values
index|[
name|index
operator|++
index|]
operator|=
name|value
expr_stmt|;
block|}
try|try
block|{
return|return
name|method
operator|.
name|getMethod
argument_list|()
operator|.
name|invoke
argument_list|(
name|facebook
argument_list|,
name|values
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
comment|// skip wrapper exception to simplify stack
name|String
name|msg
decl_stmt|;
if|if
condition|(
name|e
operator|.
name|getCause
argument_list|()
operator|!=
literal|null
operator|&&
name|e
operator|.
name|getCause
argument_list|()
operator|instanceof
name|FacebookException
condition|)
block|{
name|e
operator|=
name|e
operator|.
name|getCause
argument_list|()
expr_stmt|;
name|msg
operator|=
operator|(
operator|(
name|FacebookException
operator|)
name|e
operator|)
operator|.
name|getErrorMessage
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|msg
operator|=
name|e
operator|.
name|getMessage
argument_list|()
expr_stmt|;
block|}
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Error invoking %s with %s: %s"
argument_list|,
name|method
operator|.
name|getName
argument_list|()
argument_list|,
name|properties
argument_list|,
name|msg
argument_list|)
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
DECL|enum|MatchType
specifier|public
specifier|static
enum|enum
name|MatchType
block|{
DECL|enumConstant|EXACT
DECL|enumConstant|SUBSET
DECL|enumConstant|SUPER_SET
name|EXACT
block|,
name|SUBSET
block|,
name|SUPER_SET
block|}
block|}
end_class

end_unit

