begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.tools.apt.helper
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|tools
operator|.
name|apt
operator|.
name|helper
package|;
end_package

begin_comment
comment|/**  * Some String helper methods  */
end_comment

begin_class
DECL|class|Strings
specifier|public
specifier|final
class|class
name|Strings
block|{
DECL|method|Strings ()
specifier|private
name|Strings
parameter_list|()
block|{
comment|//Helper class
block|}
comment|/**      * Returns true if the given text is null or empty string or has<tt>null</tt> as the value      */
DECL|method|isNullOrEmpty (String text)
specifier|public
specifier|static
name|boolean
name|isNullOrEmpty
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
name|length
argument_list|()
operator|==
literal|0
operator|||
literal|"null"
operator|.
name|equals
argument_list|(
name|text
argument_list|)
return|;
block|}
DECL|method|safeNull (String text)
specifier|public
specifier|static
name|String
name|safeNull
parameter_list|(
name|String
name|text
parameter_list|)
block|{
if|if
condition|(
name|isNullOrEmpty
argument_list|(
name|text
argument_list|)
condition|)
block|{
return|return
literal|""
return|;
block|}
else|else
block|{
return|return
name|text
return|;
block|}
block|}
comment|/**      * Returns the value or the defaultValue if it is null      */
DECL|method|getOrElse (String text, String defaultValue)
specifier|public
specifier|static
name|String
name|getOrElse
parameter_list|(
name|String
name|text
parameter_list|,
name|String
name|defaultValue
parameter_list|)
block|{
return|return
operator|(
name|text
operator|!=
literal|null
operator|)
condition|?
name|text
else|:
name|defaultValue
return|;
block|}
comment|/**      * Returns the string after the given token      *      * @param text  the text      * @param after the token      * @return the text after the token, or<tt>null</tt> if text does not contain the token      */
DECL|method|after (String text, String after)
specifier|public
specifier|static
name|String
name|after
parameter_list|(
name|String
name|text
parameter_list|,
name|String
name|after
parameter_list|)
block|{
if|if
condition|(
operator|!
name|text
operator|.
name|contains
argument_list|(
name|after
argument_list|)
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|text
operator|.
name|substring
argument_list|(
name|text
operator|.
name|indexOf
argument_list|(
name|after
argument_list|)
operator|+
name|after
operator|.
name|length
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * Returns the canonical class name by removing any generic type information.      */
DECL|method|canonicalClassName (String className)
specifier|public
specifier|static
name|String
name|canonicalClassName
parameter_list|(
name|String
name|className
parameter_list|)
block|{
comment|// remove generics
name|int
name|pos
init|=
name|className
operator|.
name|indexOf
argument_list|(
literal|'<'
argument_list|)
decl_stmt|;
if|if
condition|(
name|pos
operator|!=
operator|-
literal|1
condition|)
block|{
return|return
name|className
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|pos
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|className
return|;
block|}
block|}
comment|/**      * Returns the text wrapped double quotes      */
DECL|method|doubleQuote (String text)
specifier|public
specifier|static
name|String
name|doubleQuote
parameter_list|(
name|String
name|text
parameter_list|)
block|{
return|return
name|quote
argument_list|(
name|text
argument_list|,
literal|"\""
argument_list|)
return|;
block|}
comment|/**      * Returns the text wrapped single quotes      */
DECL|method|singleQuote (String text)
specifier|public
specifier|static
name|String
name|singleQuote
parameter_list|(
name|String
name|text
parameter_list|)
block|{
return|return
name|quote
argument_list|(
name|text
argument_list|,
literal|"'"
argument_list|)
return|;
block|}
comment|/**      * Wraps the text in the given quote text      *      * @param text the text to wrap in quotes      * @param quote the quote text added to the prefix and postfix of the text      *      * @return the text wrapped in the given quotes      */
DECL|method|quote (String text, String quote)
specifier|public
specifier|static
name|String
name|quote
parameter_list|(
name|String
name|text
parameter_list|,
name|String
name|quote
parameter_list|)
block|{
return|return
name|quote
operator|+
name|text
operator|+
name|quote
return|;
block|}
comment|/**      * Clips the text between the start and end markers      */
DECL|method|between (String text, String start, String end)
specifier|public
specifier|static
name|String
name|between
parameter_list|(
name|String
name|text
parameter_list|,
name|String
name|start
parameter_list|,
name|String
name|end
parameter_list|)
block|{
name|int
name|pos
init|=
name|text
operator|.
name|indexOf
argument_list|(
name|start
argument_list|)
decl_stmt|;
if|if
condition|(
name|pos
operator|>
literal|0
condition|)
block|{
name|text
operator|=
name|text
operator|.
name|substring
argument_list|(
name|pos
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
name|int
name|pos2
init|=
name|text
operator|.
name|lastIndexOf
argument_list|(
name|end
argument_list|)
decl_stmt|;
if|if
condition|(
name|pos2
operator|>
literal|0
condition|)
block|{
name|text
operator|=
name|text
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|pos2
argument_list|)
expr_stmt|;
block|}
return|return
name|text
return|;
block|}
comment|/**      * Capitalizes the name as a title      *      * @param name  the name      * @return as a title      */
DECL|method|asTitle (String name)
specifier|public
specifier|static
name|String
name|asTitle
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|char
name|c
range|:
name|name
operator|.
name|toCharArray
argument_list|()
control|)
block|{
name|boolean
name|upper
init|=
name|Character
operator|.
name|isUpperCase
argument_list|(
name|c
argument_list|)
decl_stmt|;
name|boolean
name|first
init|=
name|sb
operator|.
name|length
argument_list|()
operator|==
literal|0
decl_stmt|;
if|if
condition|(
name|first
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|Character
operator|.
name|toUpperCase
argument_list|(
name|c
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|upper
condition|)
block|{
name|char
name|prev
init|=
name|sb
operator|.
name|charAt
argument_list|(
name|sb
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|Character
operator|.
name|isUpperCase
argument_list|(
name|prev
argument_list|)
condition|)
block|{
comment|// append space if previous is not upper
name|sb
operator|.
name|append
argument_list|(
literal|' '
argument_list|)
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|sb
operator|.
name|append
argument_list|(
name|Character
operator|.
name|toLowerCase
argument_list|(
name|c
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|sb
operator|.
name|toString
argument_list|()
operator|.
name|trim
argument_list|()
return|;
block|}
block|}
end_class

end_unit

