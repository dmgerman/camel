begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.util.json
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|json
package|;
end_package

begin_comment
comment|/**  * DeserializationException explains how and where the problem occurs in the  * source JSON text during deserialization.  *   * @since 2.0.0  */
end_comment

begin_class
DECL|class|DeserializationException
specifier|public
class|class
name|DeserializationException
extends|extends
name|Exception
block|{
comment|/** The kinds of exceptions that can trigger a DeserializationException. */
DECL|enum|Problems
enum|enum
name|Problems
block|{
DECL|enumConstant|SuppressWarnings
annotation|@
name|SuppressWarnings
argument_list|(
literal|"javadoc"
argument_list|)
DECL|enumConstant|DISALLOWED_TOKEN
DECL|enumConstant|SuppressWarnings
name|DISALLOWED_TOKEN
block|,
annotation|@
name|SuppressWarnings
argument_list|(
literal|"javadoc"
argument_list|)
DECL|enumConstant|UNEXPECTED_CHARACTER
DECL|enumConstant|SuppressWarnings
name|UNEXPECTED_CHARACTER
block|,
annotation|@
name|SuppressWarnings
argument_list|(
literal|"javadoc"
argument_list|)
DECL|enumConstant|UNEXPECTED_EXCEPTION
DECL|enumConstant|SuppressWarnings
name|UNEXPECTED_EXCEPTION
block|,
annotation|@
name|SuppressWarnings
argument_list|(
literal|"javadoc"
argument_list|)
DECL|enumConstant|UNEXPECTED_TOKEN
name|UNEXPECTED_TOKEN
decl_stmt|;
block|}
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
operator|-
literal|7880698968187728547L
decl_stmt|;
DECL|field|position
specifier|private
specifier|final
name|int
name|position
decl_stmt|;
DECL|field|problemType
specifier|private
specifier|final
name|Problems
name|problemType
decl_stmt|;
DECL|field|unexpectedObject
specifier|private
specifier|final
name|Object
name|unexpectedObject
decl_stmt|;
comment|/**      * @param position where the exception occurred.      * @param problemType how the exception occurred.      * @param unexpectedObject what caused the exception.      */
DECL|method|DeserializationException (final int position, final Problems problemType, final Object unexpectedObject)
specifier|public
name|DeserializationException
parameter_list|(
specifier|final
name|int
name|position
parameter_list|,
specifier|final
name|Problems
name|problemType
parameter_list|,
specifier|final
name|Object
name|unexpectedObject
parameter_list|)
block|{
name|this
operator|.
name|position
operator|=
name|position
expr_stmt|;
name|this
operator|.
name|problemType
operator|=
name|problemType
expr_stmt|;
name|this
operator|.
name|unexpectedObject
operator|=
name|unexpectedObject
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getMessage ()
specifier|public
name|String
name|getMessage
parameter_list|()
block|{
specifier|final
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
switch|switch
condition|(
name|this
operator|.
name|problemType
condition|)
block|{
case|case
name|DISALLOWED_TOKEN
case|:
name|sb
operator|.
name|append
argument_list|(
literal|"The disallowed token ("
argument_list|)
operator|.
name|append
argument_list|(
name|this
operator|.
name|unexpectedObject
argument_list|)
operator|.
name|append
argument_list|(
literal|") was found at position "
argument_list|)
operator|.
name|append
argument_list|(
name|this
operator|.
name|position
argument_list|)
operator|.
name|append
argument_list|(
literal|". If this is in error, try again with a parse that allows the token instead. Otherwise, fix the parsable string and try again."
argument_list|)
expr_stmt|;
break|break;
case|case
name|UNEXPECTED_CHARACTER
case|:
name|sb
operator|.
name|append
argument_list|(
literal|"The unexpected character ("
argument_list|)
operator|.
name|append
argument_list|(
name|this
operator|.
name|unexpectedObject
argument_list|)
operator|.
name|append
argument_list|(
literal|") was found at position "
argument_list|)
operator|.
name|append
argument_list|(
name|this
operator|.
name|position
argument_list|)
operator|.
name|append
argument_list|(
literal|". Fix the parsable string and try again."
argument_list|)
expr_stmt|;
break|break;
case|case
name|UNEXPECTED_TOKEN
case|:
name|sb
operator|.
name|append
argument_list|(
literal|"The unexpected token "
argument_list|)
operator|.
name|append
argument_list|(
name|this
operator|.
name|unexpectedObject
argument_list|)
operator|.
name|append
argument_list|(
literal|" was found at position "
argument_list|)
operator|.
name|append
argument_list|(
name|this
operator|.
name|position
argument_list|)
operator|.
name|append
argument_list|(
literal|". Fix the parsable string and try again."
argument_list|)
expr_stmt|;
break|break;
case|case
name|UNEXPECTED_EXCEPTION
case|:
name|sb
operator|.
name|append
argument_list|(
literal|"Please report this to the library's maintainer. The unexpected exception that should be addressed before trying again occurred at position "
argument_list|)
operator|.
name|append
argument_list|(
name|this
operator|.
name|position
argument_list|)
operator|.
name|append
argument_list|(
literal|": "
argument_list|)
operator|.
name|append
argument_list|(
name|this
operator|.
name|unexpectedObject
argument_list|)
expr_stmt|;
break|break;
default|default:
name|sb
operator|.
name|append
argument_list|(
literal|"Please report this to the library's maintainer. An error at position "
argument_list|)
operator|.
name|append
argument_list|(
name|this
operator|.
name|position
argument_list|)
operator|.
name|append
argument_list|(
literal|" occurred. There are no recovery recommendations available."
argument_list|)
expr_stmt|;
break|break;
block|}
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
comment|/** @return an index of the string character the error type occurred at. */
DECL|method|getPosition ()
specifier|public
name|int
name|getPosition
parameter_list|()
block|{
return|return
name|this
operator|.
name|position
return|;
block|}
comment|/** @return the enumeration for how the exception occurred. */
DECL|method|getProblemType ()
specifier|public
name|Problems
name|getProblemType
parameter_list|()
block|{
return|return
name|this
operator|.
name|problemType
return|;
block|}
comment|/** @return a representation of what caused the exception. */
DECL|method|getUnexpectedObject ()
specifier|public
name|Object
name|getUnexpectedObject
parameter_list|()
block|{
return|return
name|this
operator|.
name|unexpectedObject
return|;
block|}
block|}
end_class

end_unit

