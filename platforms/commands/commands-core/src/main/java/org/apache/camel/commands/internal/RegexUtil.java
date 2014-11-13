begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.commands.internal
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|commands
operator|.
name|internal
package|;
end_package

begin_comment
comment|/**  * Util class.  */
end_comment

begin_class
DECL|class|RegexUtil
specifier|public
specifier|final
class|class
name|RegexUtil
block|{
DECL|method|RegexUtil ()
specifier|private
name|RegexUtil
parameter_list|()
block|{     }
comment|/**      * convert a wild card containing * and ? to the equivalent regex      *      * @param wildcard wildcard string describing a file.      * @return regex string that could be fed to Pattern.compile      */
DECL|method|wildcardAsRegex (String wildcard)
specifier|public
specifier|static
name|String
name|wildcardAsRegex
parameter_list|(
name|String
name|wildcard
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
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|wildcard
operator|.
name|length
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
specifier|final
name|char
name|c
init|=
name|wildcard
operator|.
name|charAt
argument_list|(
name|i
argument_list|)
decl_stmt|;
switch|switch
condition|(
name|c
condition|)
block|{
case|case
literal|'*'
case|:
name|sb
operator|.
name|append
argument_list|(
literal|".*?"
argument_list|)
expr_stmt|;
break|break;
case|case
literal|'?'
case|:
name|sb
operator|.
name|append
argument_list|(
literal|"."
argument_list|)
expr_stmt|;
break|break;
comment|// chars that have magic regex meaning. They need quoting to be taken literally
case|case
literal|'$'
case|:
case|case
literal|'('
case|:
case|case
literal|')'
case|:
case|case
literal|'+'
case|:
case|case
literal|'-'
case|:
case|case
literal|'.'
case|:
case|case
literal|'['
case|:
case|case
literal|'\\'
case|:
case|case
literal|']'
case|:
case|case
literal|'^'
case|:
case|case
literal|'{'
case|:
case|case
literal|'|'
case|:
case|case
literal|'}'
case|:
name|sb
operator|.
name|append
argument_list|(
literal|'\\'
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|c
argument_list|)
expr_stmt|;
break|break;
default|default:
name|sb
operator|.
name|append
argument_list|(
name|c
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

