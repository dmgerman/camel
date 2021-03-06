begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.maven
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|maven
package|;
end_package

begin_class
DECL|class|GenerateHelper
specifier|public
specifier|final
class|class
name|GenerateHelper
block|{
DECL|field|VALID_CHARS
specifier|private
specifier|static
specifier|final
name|String
name|VALID_CHARS
init|=
literal|".,-='/\\!&%():;#${}"
decl_stmt|;
DECL|method|GenerateHelper ()
specifier|private
name|GenerateHelper
parameter_list|()
block|{     }
comment|/**      * Sanitizes the javadoc to removed invalid characters so it can be used as json description      *      * @param javadoc  the javadoc      * @return the text that is valid as json      */
DECL|method|sanitizeDescription (String javadoc, boolean summary)
specifier|public
specifier|static
name|String
name|sanitizeDescription
parameter_list|(
name|String
name|javadoc
parameter_list|,
name|boolean
name|summary
parameter_list|)
block|{
if|if
condition|(
name|javadoc
operator|==
literal|null
operator|||
name|javadoc
operator|.
name|trim
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
comment|// lets just use what java accepts as identifiers
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
comment|// split into lines
name|String
index|[]
name|lines
init|=
name|javadoc
operator|.
name|split
argument_list|(
literal|"\n"
argument_list|)
decl_stmt|;
name|boolean
name|first
init|=
literal|true
decl_stmt|;
for|for
control|(
name|String
name|line
range|:
name|lines
control|)
block|{
name|line
operator|=
name|line
operator|.
name|trim
argument_list|()
expr_stmt|;
if|if
condition|(
name|line
operator|.
name|startsWith
argument_list|(
literal|"**"
argument_list|)
condition|)
block|{
continue|continue;
block|}
comment|// remove leading javadoc *
if|if
condition|(
name|line
operator|.
name|startsWith
argument_list|(
literal|"*"
argument_list|)
condition|)
block|{
name|line
operator|=
name|line
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|line
operator|=
name|line
operator|.
name|trim
argument_list|()
expr_stmt|;
block|}
comment|// terminate if we reach @param, @return or @deprecated as we only want the javadoc summary
if|if
condition|(
name|line
operator|.
name|startsWith
argument_list|(
literal|"@param"
argument_list|)
operator|||
name|line
operator|.
name|startsWith
argument_list|(
literal|"@return"
argument_list|)
operator|||
name|line
operator|.
name|startsWith
argument_list|(
literal|"@deprecated"
argument_list|)
condition|)
block|{
break|break;
block|}
comment|// skip lines that are javadoc references
if|if
condition|(
name|line
operator|.
name|startsWith
argument_list|(
literal|"@"
argument_list|)
condition|)
block|{
continue|continue;
block|}
comment|// remove all XML tags
name|line
operator|=
name|line
operator|.
name|replaceAll
argument_list|(
literal|"<.*?>"
argument_list|,
literal|""
argument_list|)
expr_stmt|;
comment|// remove all inlined javadoc links, eg such as {@link org.apache.camel.spi.Registry}
comment|// use #? to remove leading # in case its a local reference
name|line
operator|=
name|line
operator|.
name|replaceAll
argument_list|(
literal|"\\{\\@\\w+\\s#?([\\w.#(\\d,)]+)\\}"
argument_list|,
literal|"$1"
argument_list|)
expr_stmt|;
comment|// we are starting from a new line, so add a whitespace
if|if
condition|(
operator|!
name|first
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|' '
argument_list|)
expr_stmt|;
block|}
comment|// create a new line
name|StringBuilder
name|cb
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
name|line
operator|.
name|toCharArray
argument_list|()
control|)
block|{
if|if
condition|(
name|Character
operator|.
name|isJavaIdentifierPart
argument_list|(
name|c
argument_list|)
operator|||
name|VALID_CHARS
operator|.
name|indexOf
argument_list|(
name|c
argument_list|)
operator|!=
operator|-
literal|1
condition|)
block|{
name|cb
operator|.
name|append
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|Character
operator|.
name|isWhitespace
argument_list|(
name|c
argument_list|)
condition|)
block|{
comment|// always use space as whitespace, also for line feeds etc
name|cb
operator|.
name|append
argument_list|(
literal|' '
argument_list|)
expr_stmt|;
block|}
block|}
comment|// append data
name|String
name|s
init|=
name|cb
operator|.
name|toString
argument_list|()
operator|.
name|trim
argument_list|()
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|s
argument_list|)
expr_stmt|;
name|boolean
name|empty
init|=
name|s
operator|.
name|isEmpty
argument_list|()
decl_stmt|;
name|boolean
name|endWithDot
init|=
name|s
operator|.
name|endsWith
argument_list|(
literal|"."
argument_list|)
decl_stmt|;
name|boolean
name|haveText
init|=
name|sb
operator|.
name|length
argument_list|()
operator|>
literal|0
decl_stmt|;
if|if
condition|(
name|haveText
operator|&&
name|summary
operator|&&
operator|(
name|empty
operator|||
name|endWithDot
operator|)
condition|)
block|{
comment|// if we only want a summary, then skip at first empty line we encounter, or if the sentence ends with a dot
break|break;
block|}
name|first
operator|=
literal|false
expr_stmt|;
block|}
comment|// remove double whitespaces, and trim
name|String
name|s
init|=
name|sb
operator|.
name|toString
argument_list|()
decl_stmt|;
name|s
operator|=
name|s
operator|.
name|replaceAll
argument_list|(
literal|"\\s+"
argument_list|,
literal|" "
argument_list|)
expr_stmt|;
return|return
name|s
operator|.
name|trim
argument_list|()
return|;
block|}
block|}
end_class

end_unit

