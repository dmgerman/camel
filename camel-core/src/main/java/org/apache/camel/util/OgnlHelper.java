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
name|List
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
comment|/**  * Helper for Camel OGNL (Object-Graph Navigation Language) expressions.  */
end_comment

begin_class
DECL|class|OgnlHelper
specifier|public
specifier|final
class|class
name|OgnlHelper
block|{
DECL|field|INDEX_PATTERN
specifier|private
specifier|static
specifier|final
name|Pattern
name|INDEX_PATTERN
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"^(.*)\\[(.*)\\]$"
argument_list|)
decl_stmt|;
DECL|method|OgnlHelper ()
specifier|private
name|OgnlHelper
parameter_list|()
block|{     }
comment|/**      * Tests whether or not the given String is a Camel OGNL expression.      *<p/>      * An expression is considered an OGNL expression when it contains either one of the following chars: . or [      *      * @param expression  the String      * @return<tt>true</tt> if a Camel OGNL expression, otherwise<tt>false</tt>.       */
DECL|method|isValidOgnlExpression (String expression)
specifier|public
specifier|static
name|boolean
name|isValidOgnlExpression
parameter_list|(
name|String
name|expression
parameter_list|)
block|{
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|expression
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
comment|// the brackets should come in a pair
name|int
name|bracketBegin
init|=
name|StringHelper
operator|.
name|countChar
argument_list|(
name|expression
argument_list|,
literal|'['
argument_list|)
decl_stmt|;
name|int
name|bracketEnd
init|=
name|StringHelper
operator|.
name|countChar
argument_list|(
name|expression
argument_list|,
literal|']'
argument_list|)
decl_stmt|;
if|if
condition|(
name|bracketBegin
operator|>
literal|0
operator|&&
name|bracketEnd
operator|>
literal|0
condition|)
block|{
return|return
name|bracketBegin
operator|==
name|bracketEnd
return|;
block|}
return|return
name|expression
operator|.
name|contains
argument_list|(
literal|"."
argument_list|)
return|;
block|}
DECL|method|isInvalidValidOgnlExpression (String expression)
specifier|public
specifier|static
name|boolean
name|isInvalidValidOgnlExpression
parameter_list|(
name|String
name|expression
parameter_list|)
block|{
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|expression
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
operator|!
name|expression
operator|.
name|contains
argument_list|(
literal|"."
argument_list|)
operator|&&
operator|!
name|expression
operator|.
name|contains
argument_list|(
literal|"["
argument_list|)
operator|&&
operator|!
name|expression
operator|.
name|contains
argument_list|(
literal|"]"
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
comment|// the brackets should come in pair
name|int
name|bracketBegin
init|=
name|StringHelper
operator|.
name|countChar
argument_list|(
name|expression
argument_list|,
literal|'['
argument_list|)
decl_stmt|;
name|int
name|bracketEnd
init|=
name|StringHelper
operator|.
name|countChar
argument_list|(
name|expression
argument_list|,
literal|']'
argument_list|)
decl_stmt|;
if|if
condition|(
name|bracketBegin
operator|>
literal|0
operator|||
name|bracketEnd
operator|>
literal|0
condition|)
block|{
return|return
name|bracketBegin
operator|!=
name|bracketEnd
return|;
block|}
comment|// check for double dots
if|if
condition|(
name|expression
operator|.
name|contains
argument_list|(
literal|".."
argument_list|)
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
comment|/**      * Validates whether the method name is using valid java identifiers in the name      * Will throw {@link IllegalArgumentException} if the method name is invalid.      */
DECL|method|validateMethodName (String method)
specifier|public
specifier|static
name|void
name|validateMethodName
parameter_list|(
name|String
name|method
parameter_list|)
block|{
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|method
argument_list|)
condition|)
block|{
return|return;
block|}
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|method
operator|.
name|length
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|char
name|ch
init|=
name|method
operator|.
name|charAt
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|i
operator|==
literal|0
operator|&&
literal|'.'
operator|==
name|ch
condition|)
block|{
comment|// its a dot before a method name
continue|continue;
block|}
if|if
condition|(
name|ch
operator|==
literal|'('
operator|||
name|ch
operator|==
literal|'['
operator|||
name|ch
operator|==
literal|'.'
operator|||
name|ch
operator|==
literal|'?'
condition|)
block|{
comment|// break when method name ends and sub method or arguments begin
break|break;
block|}
if|if
condition|(
name|i
operator|==
literal|0
operator|&&
operator|!
name|Character
operator|.
name|isJavaIdentifierStart
argument_list|(
name|ch
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Method name must start with a valid java identifier at position: 0 in method: "
operator|+
name|method
argument_list|)
throw|;
block|}
elseif|else
if|if
condition|(
operator|!
name|Character
operator|.
name|isJavaIdentifierPart
argument_list|(
name|ch
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Method name must be valid java identifier at position: "
operator|+
name|i
operator|+
literal|" in method: "
operator|+
name|method
argument_list|)
throw|;
block|}
block|}
block|}
comment|/**      * Tests whether or not the given Camel OGNL expression is using the null safe operator or not.      *      * @param ognlExpression the Camel OGNL expression      * @return<tt>true</tt> if the null safe operator is used, otherwise<tt>false</tt>.      */
DECL|method|isNullSafeOperator (String ognlExpression)
specifier|public
specifier|static
name|boolean
name|isNullSafeOperator
parameter_list|(
name|String
name|ognlExpression
parameter_list|)
block|{
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|ognlExpression
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
name|ognlExpression
operator|.
name|startsWith
argument_list|(
literal|"?"
argument_list|)
return|;
block|}
comment|/**      * Removes any leading operators from the Camel OGNL expression.      *<p/>      * Will remove any leading of the following chars: ? or .      *      * @param ognlExpression  the Camel OGNL expression      * @return the Camel OGNL expression without any leading operators.      */
DECL|method|removeLeadingOperators (String ognlExpression)
specifier|public
specifier|static
name|String
name|removeLeadingOperators
parameter_list|(
name|String
name|ognlExpression
parameter_list|)
block|{
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|ognlExpression
argument_list|)
condition|)
block|{
return|return
name|ognlExpression
return|;
block|}
if|if
condition|(
name|ognlExpression
operator|.
name|startsWith
argument_list|(
literal|"?"
argument_list|)
condition|)
block|{
name|ognlExpression
operator|=
name|ognlExpression
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|ognlExpression
operator|.
name|startsWith
argument_list|(
literal|"."
argument_list|)
condition|)
block|{
name|ognlExpression
operator|=
name|ognlExpression
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
return|return
name|ognlExpression
return|;
block|}
comment|/**      * Removes any trailing operators from the Camel OGNL expression.      *      * @param ognlExpression  the Camel OGNL expression      * @return the Camel OGNL expression without any trailing operators.      */
DECL|method|removeTrailingOperators (String ognlExpression)
specifier|public
specifier|static
name|String
name|removeTrailingOperators
parameter_list|(
name|String
name|ognlExpression
parameter_list|)
block|{
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|ognlExpression
argument_list|)
condition|)
block|{
return|return
name|ognlExpression
return|;
block|}
if|if
condition|(
name|ognlExpression
operator|.
name|contains
argument_list|(
literal|"["
argument_list|)
condition|)
block|{
return|return
name|StringHelper
operator|.
name|before
argument_list|(
name|ognlExpression
argument_list|,
literal|"["
argument_list|)
return|;
block|}
return|return
name|ognlExpression
return|;
block|}
DECL|method|removeOperators (String ognlExpression)
specifier|public
specifier|static
name|String
name|removeOperators
parameter_list|(
name|String
name|ognlExpression
parameter_list|)
block|{
return|return
name|removeLeadingOperators
argument_list|(
name|removeTrailingOperators
argument_list|(
name|ognlExpression
argument_list|)
argument_list|)
return|;
block|}
DECL|method|isOgnlIndex (String ognlExpression)
specifier|public
specifier|static
name|KeyValueHolder
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|isOgnlIndex
parameter_list|(
name|String
name|ognlExpression
parameter_list|)
block|{
name|Matcher
name|matcher
init|=
name|INDEX_PATTERN
operator|.
name|matcher
argument_list|(
name|ognlExpression
argument_list|)
decl_stmt|;
if|if
condition|(
name|matcher
operator|.
name|matches
argument_list|()
condition|)
block|{
comment|// to avoid empty strings as we want key/value to be null in such cases
name|String
name|key
init|=
name|matcher
operator|.
name|group
argument_list|(
literal|1
argument_list|)
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|key
argument_list|)
condition|)
block|{
name|key
operator|=
literal|null
expr_stmt|;
block|}
comment|// to avoid empty strings as we want key/value to be null in such cases
name|String
name|value
init|=
name|matcher
operator|.
name|group
argument_list|(
literal|2
argument_list|)
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|value
argument_list|)
condition|)
block|{
name|value
operator|=
literal|null
expr_stmt|;
block|}
return|return
operator|new
name|KeyValueHolder
argument_list|<>
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
comment|/**      * Regular expression with repeating groups is a pain to get right      * and then nobody understands the reg exp afterwards.      * So we use a bit ugly/low-level Java code to split the OGNL into methods.      *      * @param ognl the ognl expression      * @return a list of methods, will return an empty list, if ognl expression has no methods      * @throws IllegalArgumentException if the last method has a missing ending parenthesis      */
DECL|method|splitOgnl (String ognl)
specifier|public
specifier|static
name|List
argument_list|<
name|String
argument_list|>
name|splitOgnl
parameter_list|(
name|String
name|ognl
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|methods
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
comment|// return an empty list if ognl is empty
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|ognl
argument_list|)
condition|)
block|{
return|return
name|methods
return|;
block|}
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|int
name|j
init|=
literal|0
decl_stmt|;
comment|// j is used as counter per method
name|boolean
name|squareBracket
init|=
literal|false
decl_stmt|;
comment|// special to keep track if we are inside a square bracket block, eg: [foo]
name|boolean
name|parenthesisBracket
init|=
literal|false
decl_stmt|;
comment|// special to keep track if we are inside a parenthesis block, eg: bar(${body}, ${header.foo})
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|ognl
operator|.
name|length
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|char
name|ch
init|=
name|ognl
operator|.
name|charAt
argument_list|(
name|i
argument_list|)
decl_stmt|;
comment|// special for starting a new method
if|if
condition|(
name|j
operator|==
literal|0
operator|||
operator|(
name|j
operator|==
literal|1
operator|&&
name|ognl
operator|.
name|charAt
argument_list|(
name|i
operator|-
literal|1
argument_list|)
operator|==
literal|'?'
operator|)
operator|||
operator|(
name|ch
operator|!=
literal|'.'
operator|&&
name|ch
operator|!=
literal|'?'
operator|&&
name|ch
operator|!=
literal|']'
operator|)
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|ch
argument_list|)
expr_stmt|;
comment|// special if we are doing square bracket
if|if
condition|(
name|ch
operator|==
literal|'['
operator|&&
operator|!
name|parenthesisBracket
condition|)
block|{
name|squareBracket
operator|=
literal|true
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|ch
operator|==
literal|'('
condition|)
block|{
name|parenthesisBracket
operator|=
literal|true
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|ch
operator|==
literal|')'
condition|)
block|{
name|parenthesisBracket
operator|=
literal|false
expr_stmt|;
block|}
name|j
operator|++
expr_stmt|;
comment|// advance
block|}
else|else
block|{
if|if
condition|(
name|ch
operator|==
literal|'.'
operator|&&
operator|!
name|squareBracket
operator|&&
operator|!
name|parenthesisBracket
condition|)
block|{
comment|// only treat dot as a method separator if not inside a square bracket block
comment|// as dots can be used in key names when accessing maps
comment|// a dit denotes end of this method and a new method is to be invoked
name|String
name|s
init|=
name|sb
operator|.
name|toString
argument_list|()
decl_stmt|;
comment|// reset sb
name|sb
operator|.
name|setLength
argument_list|(
literal|0
argument_list|)
expr_stmt|;
comment|// pass over ? to the new method
if|if
condition|(
name|s
operator|.
name|endsWith
argument_list|(
literal|"?"
argument_list|)
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|"?"
argument_list|)
expr_stmt|;
name|s
operator|=
name|s
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|s
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
comment|// add the method
name|methods
operator|.
name|add
argument_list|(
name|s
argument_list|)
expr_stmt|;
comment|// reset j to begin a new method
name|j
operator|=
literal|0
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|ch
operator|==
literal|']'
operator|&&
operator|!
name|parenthesisBracket
condition|)
block|{
comment|// append ending ] to method name
name|sb
operator|.
name|append
argument_list|(
name|ch
argument_list|)
expr_stmt|;
name|String
name|s
init|=
name|sb
operator|.
name|toString
argument_list|()
decl_stmt|;
comment|// reset sb
name|sb
operator|.
name|setLength
argument_list|(
literal|0
argument_list|)
expr_stmt|;
comment|// add the method
name|methods
operator|.
name|add
argument_list|(
name|s
argument_list|)
expr_stmt|;
comment|// reset j to begin a new method
name|j
operator|=
literal|0
expr_stmt|;
comment|// no more square bracket
name|squareBracket
operator|=
literal|false
expr_stmt|;
block|}
comment|// and don't lose the char if its not an ] end marker (as we already added that)
if|if
condition|(
name|ch
operator|!=
literal|']'
operator|||
name|parenthesisBracket
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|ch
argument_list|)
expr_stmt|;
block|}
comment|// only advance if already begun on the new method
if|if
condition|(
name|j
operator|>
literal|0
condition|)
block|{
name|j
operator|++
expr_stmt|;
block|}
block|}
block|}
comment|// add remainder in buffer when reached end of data
if|if
condition|(
name|sb
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|methods
operator|.
name|add
argument_list|(
name|sb
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|String
name|last
init|=
name|methods
operator|.
name|isEmpty
argument_list|()
condition|?
literal|null
else|:
name|methods
operator|.
name|get
argument_list|(
name|methods
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
decl_stmt|;
if|if
condition|(
name|parenthesisBracket
operator|&&
name|last
operator|!=
literal|null
condition|)
block|{
comment|// there is an unclosed parenthesis bracket on the last method, so it should end with a parenthesis
if|if
condition|(
name|last
operator|.
name|contains
argument_list|(
literal|"("
argument_list|)
operator|&&
operator|!
name|last
operator|.
name|endsWith
argument_list|(
literal|")"
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Method should end with parenthesis, was "
operator|+
name|last
argument_list|)
throw|;
block|}
block|}
return|return
name|methods
return|;
block|}
block|}
end_class

end_unit

