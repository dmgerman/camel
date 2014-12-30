begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.tools.apt
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
package|;
end_package

begin_comment
comment|/**  * Some String helper methods  */
end_comment

begin_class
DECL|class|Strings
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
block|}
end_class

end_unit

