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
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|StringQuoteHelper
operator|.
name|doubleQuote
import|;
end_import

begin_comment
comment|/**  * Helper methods for working with Strings.  */
end_comment

begin_class
DECL|class|StringHelper
specifier|public
specifier|final
class|class
name|StringHelper
block|{
comment|/**      * Constructor of utility class should be private.      */
DECL|method|StringHelper ()
specifier|private
name|StringHelper
parameter_list|()
block|{     }
comment|/**      * Ensures that<code>s</code> is friendly for a URL or file system.      *      * @param s String to be sanitized.      * @return sanitized version of<code>s</code>.      * @throws NullPointerException if<code>s</code> is<code>null</code>.      */
DECL|method|sanitize (String s)
specifier|public
specifier|static
name|String
name|sanitize
parameter_list|(
name|String
name|s
parameter_list|)
block|{
return|return
name|s
operator|.
name|replace
argument_list|(
literal|':'
argument_list|,
literal|'-'
argument_list|)
operator|.
name|replace
argument_list|(
literal|'_'
argument_list|,
literal|'-'
argument_list|)
operator|.
name|replace
argument_list|(
literal|'.'
argument_list|,
literal|'-'
argument_list|)
operator|.
name|replace
argument_list|(
literal|'/'
argument_list|,
literal|'-'
argument_list|)
operator|.
name|replace
argument_list|(
literal|'\\'
argument_list|,
literal|'-'
argument_list|)
return|;
block|}
comment|/**      * Counts the number of times the given char is in the string      *      * @param s  the string      * @param ch the char      * @return number of times char is located in the string      */
DECL|method|countChar (String s, char ch)
specifier|public
specifier|static
name|int
name|countChar
parameter_list|(
name|String
name|s
parameter_list|,
name|char
name|ch
parameter_list|)
block|{
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|s
argument_list|)
condition|)
block|{
return|return
literal|0
return|;
block|}
name|int
name|matches
init|=
literal|0
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
name|s
operator|.
name|length
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|char
name|c
init|=
name|s
operator|.
name|charAt
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|ch
operator|==
name|c
condition|)
block|{
name|matches
operator|++
expr_stmt|;
block|}
block|}
return|return
name|matches
return|;
block|}
comment|/**      * Limits the length of a string      *       * @param s the string      * @param maxLength the maximum length of the returned string      * @return s if the length of s is less than maxLength or the first maxLength characters of s      */
DECL|method|limitLenght (String s, int maxLength)
specifier|public
specifier|static
name|String
name|limitLenght
parameter_list|(
name|String
name|s
parameter_list|,
name|int
name|maxLength
parameter_list|)
block|{
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|s
argument_list|)
condition|)
block|{
return|return
name|s
return|;
block|}
return|return
name|s
operator|.
name|length
argument_list|()
operator|<=
name|maxLength
condition|?
name|s
else|:
name|s
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|maxLength
argument_list|)
return|;
block|}
DECL|method|removeQuotes (String s)
specifier|public
specifier|static
name|String
name|removeQuotes
parameter_list|(
name|String
name|s
parameter_list|)
block|{
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|s
argument_list|)
condition|)
block|{
return|return
name|s
return|;
block|}
name|s
operator|=
name|replaceAll
argument_list|(
name|s
argument_list|,
literal|"'"
argument_list|,
literal|""
argument_list|)
expr_stmt|;
name|s
operator|=
name|replaceAll
argument_list|(
name|s
argument_list|,
literal|"\""
argument_list|,
literal|""
argument_list|)
expr_stmt|;
return|return
name|s
return|;
block|}
DECL|method|removeLeadingAndEndingQuotes (String s)
specifier|public
specifier|static
name|String
name|removeLeadingAndEndingQuotes
parameter_list|(
name|String
name|s
parameter_list|)
block|{
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|s
argument_list|)
condition|)
block|{
return|return
name|s
return|;
block|}
name|String
name|copy
init|=
name|s
operator|.
name|trim
argument_list|()
decl_stmt|;
if|if
condition|(
name|copy
operator|.
name|startsWith
argument_list|(
literal|"'"
argument_list|)
operator|&&
name|copy
operator|.
name|endsWith
argument_list|(
literal|"'"
argument_list|)
condition|)
block|{
return|return
name|copy
operator|.
name|substring
argument_list|(
literal|1
argument_list|,
name|copy
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
return|;
block|}
if|if
condition|(
name|copy
operator|.
name|startsWith
argument_list|(
literal|"\""
argument_list|)
operator|&&
name|copy
operator|.
name|endsWith
argument_list|(
literal|"\""
argument_list|)
condition|)
block|{
return|return
name|copy
operator|.
name|substring
argument_list|(
literal|1
argument_list|,
name|copy
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
return|;
block|}
comment|// no quotes, so return as-is
return|return
name|s
return|;
block|}
DECL|method|isQuoted (String s)
specifier|public
specifier|static
name|boolean
name|isQuoted
parameter_list|(
name|String
name|s
parameter_list|)
block|{
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|s
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|s
operator|.
name|startsWith
argument_list|(
literal|"'"
argument_list|)
operator|&&
name|s
operator|.
name|endsWith
argument_list|(
literal|"'"
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
name|s
operator|.
name|startsWith
argument_list|(
literal|"\""
argument_list|)
operator|&&
name|s
operator|.
name|endsWith
argument_list|(
literal|"\""
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
comment|/**      * Encodes the text into safe XML by replacing<> and& with XML tokens      *      * @param text  the text      * @return the encoded text      */
DECL|method|xmlEncode (String text)
specifier|public
specifier|static
name|String
name|xmlEncode
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
literal|""
return|;
block|}
comment|// must replace amp first, so we dont replace&lt; to amp later
name|text
operator|=
name|replaceAll
argument_list|(
name|text
argument_list|,
literal|"&"
argument_list|,
literal|"&amp;"
argument_list|)
expr_stmt|;
name|text
operator|=
name|replaceAll
argument_list|(
name|text
argument_list|,
literal|"\""
argument_list|,
literal|"&quot;"
argument_list|)
expr_stmt|;
name|text
operator|=
name|replaceAll
argument_list|(
name|text
argument_list|,
literal|"<"
argument_list|,
literal|"&lt;"
argument_list|)
expr_stmt|;
name|text
operator|=
name|replaceAll
argument_list|(
name|text
argument_list|,
literal|">"
argument_list|,
literal|"&gt;"
argument_list|)
expr_stmt|;
return|return
name|text
return|;
block|}
comment|/**      * Determines if the string has at least one letter in upper case      * @param text the text      * @return<tt>true</tt> if at least one letter is upper case,<tt>false</tt> otherwise      */
DECL|method|hasUpperCase (String text)
specifier|public
specifier|static
name|boolean
name|hasUpperCase
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
literal|false
return|;
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
name|text
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
name|text
operator|.
name|charAt
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|Character
operator|.
name|isUpperCase
argument_list|(
name|ch
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
comment|/**      * Determines if the string is a fully qualified class name      */
DECL|method|isClassName (String text)
specifier|public
specifier|static
name|boolean
name|isClassName
parameter_list|(
name|String
name|text
parameter_list|)
block|{
name|boolean
name|result
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|text
operator|!=
literal|null
condition|)
block|{
name|String
index|[]
name|split
init|=
name|text
operator|.
name|split
argument_list|(
literal|"\\."
argument_list|)
decl_stmt|;
if|if
condition|(
name|split
operator|.
name|length
operator|>
literal|0
condition|)
block|{
name|String
name|lastToken
init|=
name|split
index|[
name|split
operator|.
name|length
operator|-
literal|1
index|]
decl_stmt|;
if|if
condition|(
name|lastToken
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|result
operator|=
name|Character
operator|.
name|isUpperCase
argument_list|(
name|lastToken
operator|.
name|charAt
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|result
return|;
block|}
comment|/**      * Does the expression have the language start token?      *      * @param expression the expression      * @param language the name of the language, such as simple      * @return<tt>true</tt> if the expression contains the start token,<tt>false</tt> otherwise      */
DECL|method|hasStartToken (String expression, String language)
specifier|public
specifier|static
name|boolean
name|hasStartToken
parameter_list|(
name|String
name|expression
parameter_list|,
name|String
name|language
parameter_list|)
block|{
if|if
condition|(
name|expression
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
comment|// for the simple language the expression start token could be "${"
if|if
condition|(
literal|"simple"
operator|.
name|equalsIgnoreCase
argument_list|(
name|language
argument_list|)
operator|&&
name|expression
operator|.
name|contains
argument_list|(
literal|"${"
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
name|language
operator|!=
literal|null
operator|&&
name|expression
operator|.
name|contains
argument_list|(
literal|"$"
operator|+
name|language
operator|+
literal|"{"
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
comment|/**      * Replaces all the from tokens in the given input string.      *<p/>      * This implementation is not recursive, not does it check for tokens in the replacement string.      *      * @param input  the input string      * @param from   the from string, must<b>not</b> be<tt>null</tt> or empty      * @param to     the replacement string, must<b>not</b> be empty      * @return the replaced string, or the input string if no replacement was needed      * @throws IllegalArgumentException if the input arguments is invalid      */
DECL|method|replaceAll (String input, String from, String to)
specifier|public
specifier|static
name|String
name|replaceAll
parameter_list|(
name|String
name|input
parameter_list|,
name|String
name|from
parameter_list|,
name|String
name|to
parameter_list|)
block|{
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|input
argument_list|)
condition|)
block|{
return|return
name|input
return|;
block|}
if|if
condition|(
name|from
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"from cannot be null"
argument_list|)
throw|;
block|}
if|if
condition|(
name|to
operator|==
literal|null
condition|)
block|{
comment|// to can be empty, so only check for null
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"to cannot be null"
argument_list|)
throw|;
block|}
comment|// fast check if there is any from at all
if|if
condition|(
operator|!
name|input
operator|.
name|contains
argument_list|(
name|from
argument_list|)
condition|)
block|{
return|return
name|input
return|;
block|}
specifier|final
name|int
name|len
init|=
name|from
operator|.
name|length
argument_list|()
decl_stmt|;
specifier|final
name|int
name|max
init|=
name|input
operator|.
name|length
argument_list|()
decl_stmt|;
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|(
name|max
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
name|max
condition|;
control|)
block|{
if|if
condition|(
name|i
operator|+
name|len
operator|<=
name|max
condition|)
block|{
name|String
name|token
init|=
name|input
operator|.
name|substring
argument_list|(
name|i
argument_list|,
name|i
operator|+
name|len
argument_list|)
decl_stmt|;
if|if
condition|(
name|from
operator|.
name|equals
argument_list|(
name|token
argument_list|)
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|to
argument_list|)
expr_stmt|;
comment|// fast forward
name|i
operator|=
name|i
operator|+
name|len
expr_stmt|;
continue|continue;
block|}
block|}
comment|// append single char
name|sb
operator|.
name|append
argument_list|(
name|input
operator|.
name|charAt
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
comment|// forward to next
name|i
operator|++
expr_stmt|;
block|}
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
comment|/**      * Creates a json tuple with the given name/value pair.      *      * @param name  the name      * @param value the value      * @param isMap whether the tuple should be map      * @return the json      */
DECL|method|toJson (String name, String value, boolean isMap)
specifier|public
specifier|static
name|String
name|toJson
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|value
parameter_list|,
name|boolean
name|isMap
parameter_list|)
block|{
if|if
condition|(
name|isMap
condition|)
block|{
return|return
literal|"{ "
operator|+
name|doubleQuote
argument_list|(
name|name
argument_list|)
operator|+
literal|": "
operator|+
name|doubleQuote
argument_list|(
name|value
argument_list|)
operator|+
literal|" }"
return|;
block|}
else|else
block|{
return|return
name|doubleQuote
argument_list|(
name|name
argument_list|)
operator|+
literal|": "
operator|+
name|doubleQuote
argument_list|(
name|value
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

