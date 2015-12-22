begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.catalog
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|catalog
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Serializable
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
name|LinkedHashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedHashSet
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

begin_comment
comment|/**  * Details result of validating endpoint uri.  */
end_comment

begin_class
DECL|class|EndpointValidationResult
specifier|public
class|class
name|EndpointValidationResult
implements|implements
name|Serializable
block|{
DECL|field|uri
specifier|private
specifier|final
name|String
name|uri
decl_stmt|;
DECL|field|errors
specifier|private
name|int
name|errors
decl_stmt|;
comment|// component
DECL|field|syntaxError
specifier|private
name|String
name|syntaxError
decl_stmt|;
DECL|field|unknownComponent
specifier|private
name|String
name|unknownComponent
decl_stmt|;
comment|// options
DECL|field|unknown
specifier|private
name|Set
argument_list|<
name|String
argument_list|>
name|unknown
decl_stmt|;
DECL|field|required
specifier|private
name|Set
argument_list|<
name|String
argument_list|>
name|required
decl_stmt|;
DECL|field|invalidEnum
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|invalidEnum
decl_stmt|;
DECL|field|invalidEnumChoices
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
index|[]
argument_list|>
name|invalidEnumChoices
decl_stmt|;
DECL|field|invalidReference
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|invalidReference
decl_stmt|;
DECL|field|invalidBoolean
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|invalidBoolean
decl_stmt|;
DECL|field|invalidInteger
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|invalidInteger
decl_stmt|;
DECL|field|invalidNumber
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|invalidNumber
decl_stmt|;
DECL|method|EndpointValidationResult (String uri)
specifier|public
name|EndpointValidationResult
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
name|this
operator|.
name|uri
operator|=
name|uri
expr_stmt|;
block|}
DECL|method|getUri ()
specifier|public
name|String
name|getUri
parameter_list|()
block|{
return|return
name|uri
return|;
block|}
DECL|method|getNumberOfErrors ()
specifier|public
name|int
name|getNumberOfErrors
parameter_list|()
block|{
return|return
name|errors
return|;
block|}
DECL|method|isSuccess ()
specifier|public
name|boolean
name|isSuccess
parameter_list|()
block|{
name|boolean
name|ok
init|=
name|syntaxError
operator|==
literal|null
operator|&&
name|unknownComponent
operator|==
literal|null
operator|&&
name|unknown
operator|==
literal|null
operator|&&
name|required
operator|==
literal|null
decl_stmt|;
if|if
condition|(
name|ok
condition|)
block|{
name|ok
operator|=
name|invalidEnum
operator|==
literal|null
operator|&&
name|invalidEnumChoices
operator|==
literal|null
operator|&&
name|invalidReference
operator|==
literal|null
operator|&&
name|invalidBoolean
operator|==
literal|null
operator|&&
name|invalidInteger
operator|==
literal|null
operator|&&
name|invalidNumber
operator|==
literal|null
expr_stmt|;
block|}
return|return
name|ok
return|;
block|}
DECL|method|addSyntaxError (String syntaxError)
specifier|public
name|void
name|addSyntaxError
parameter_list|(
name|String
name|syntaxError
parameter_list|)
block|{
name|this
operator|.
name|syntaxError
operator|=
name|syntaxError
expr_stmt|;
name|errors
operator|++
expr_stmt|;
block|}
DECL|method|addUnknownComponent (String name)
specifier|public
name|void
name|addUnknownComponent
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|unknownComponent
operator|=
name|name
expr_stmt|;
name|errors
operator|++
expr_stmt|;
block|}
DECL|method|addUnknown (String name)
specifier|public
name|void
name|addUnknown
parameter_list|(
name|String
name|name
parameter_list|)
block|{
if|if
condition|(
name|unknown
operator|==
literal|null
condition|)
block|{
name|unknown
operator|=
operator|new
name|LinkedHashSet
argument_list|<
name|String
argument_list|>
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|unknown
operator|.
name|contains
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|unknown
operator|.
name|add
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|errors
operator|++
expr_stmt|;
block|}
block|}
DECL|method|addRequired (String name)
specifier|public
name|void
name|addRequired
parameter_list|(
name|String
name|name
parameter_list|)
block|{
if|if
condition|(
name|required
operator|==
literal|null
condition|)
block|{
name|required
operator|=
operator|new
name|LinkedHashSet
argument_list|<
name|String
argument_list|>
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|required
operator|.
name|contains
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|required
operator|.
name|add
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|errors
operator|++
expr_stmt|;
block|}
block|}
DECL|method|addInvalidEnum (String name, String value)
specifier|public
name|void
name|addInvalidEnum
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|value
parameter_list|)
block|{
if|if
condition|(
name|invalidEnum
operator|==
literal|null
condition|)
block|{
name|invalidEnum
operator|=
operator|new
name|LinkedHashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|invalidEnum
operator|.
name|containsKey
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|invalidEnum
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
name|errors
operator|++
expr_stmt|;
block|}
block|}
DECL|method|addInvalidEnumChoices (String name, String[] choices)
specifier|public
name|void
name|addInvalidEnumChoices
parameter_list|(
name|String
name|name
parameter_list|,
name|String
index|[]
name|choices
parameter_list|)
block|{
if|if
condition|(
name|invalidEnumChoices
operator|==
literal|null
condition|)
block|{
name|invalidEnumChoices
operator|=
operator|new
name|LinkedHashMap
argument_list|<
name|String
argument_list|,
name|String
index|[]
argument_list|>
argument_list|()
expr_stmt|;
block|}
name|invalidEnumChoices
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|choices
argument_list|)
expr_stmt|;
block|}
DECL|method|addInvalidReference (String name, String value)
specifier|public
name|void
name|addInvalidReference
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|value
parameter_list|)
block|{
if|if
condition|(
name|invalidReference
operator|==
literal|null
condition|)
block|{
name|invalidReference
operator|=
operator|new
name|LinkedHashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|invalidReference
operator|.
name|containsKey
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|invalidReference
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
name|errors
operator|++
expr_stmt|;
block|}
block|}
DECL|method|addInvalidBoolean (String name, String value)
specifier|public
name|void
name|addInvalidBoolean
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|value
parameter_list|)
block|{
if|if
condition|(
name|invalidBoolean
operator|==
literal|null
condition|)
block|{
name|invalidBoolean
operator|=
operator|new
name|LinkedHashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|invalidBoolean
operator|.
name|containsKey
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|invalidBoolean
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
name|errors
operator|++
expr_stmt|;
block|}
block|}
DECL|method|addInvalidInteger (String name, String value)
specifier|public
name|void
name|addInvalidInteger
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|value
parameter_list|)
block|{
if|if
condition|(
name|invalidInteger
operator|==
literal|null
condition|)
block|{
name|invalidInteger
operator|=
operator|new
name|LinkedHashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|invalidInteger
operator|.
name|containsKey
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|invalidInteger
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
name|errors
operator|++
expr_stmt|;
block|}
block|}
DECL|method|addInvalidNumber (String name, String value)
specifier|public
name|void
name|addInvalidNumber
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|value
parameter_list|)
block|{
if|if
condition|(
name|invalidNumber
operator|==
literal|null
condition|)
block|{
name|invalidNumber
operator|=
operator|new
name|LinkedHashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|invalidNumber
operator|.
name|containsKey
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|invalidNumber
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
name|errors
operator|++
expr_stmt|;
block|}
block|}
DECL|method|getSyntaxError ()
specifier|public
name|String
name|getSyntaxError
parameter_list|()
block|{
return|return
name|syntaxError
return|;
block|}
DECL|method|getUnknown ()
specifier|public
name|Set
argument_list|<
name|String
argument_list|>
name|getUnknown
parameter_list|()
block|{
return|return
name|unknown
return|;
block|}
DECL|method|getUnknownComponent ()
specifier|public
name|String
name|getUnknownComponent
parameter_list|()
block|{
return|return
name|unknownComponent
return|;
block|}
DECL|method|getRequired ()
specifier|public
name|Set
argument_list|<
name|String
argument_list|>
name|getRequired
parameter_list|()
block|{
return|return
name|required
return|;
block|}
DECL|method|getInvalidEnum ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getInvalidEnum
parameter_list|()
block|{
return|return
name|invalidEnum
return|;
block|}
DECL|method|getInvalidReference ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getInvalidReference
parameter_list|()
block|{
return|return
name|invalidReference
return|;
block|}
DECL|method|getInvalidBoolean ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getInvalidBoolean
parameter_list|()
block|{
return|return
name|invalidBoolean
return|;
block|}
DECL|method|getInvalidInteger ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getInvalidInteger
parameter_list|()
block|{
return|return
name|invalidInteger
return|;
block|}
DECL|method|getInvalidNumber ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getInvalidNumber
parameter_list|()
block|{
return|return
name|invalidNumber
return|;
block|}
comment|/**      * A human readable summary of the validation errors.      *      * @return the summary, or<tt>null</tt> if no validation errors      */
DECL|method|summaryErrorMessage ()
specifier|public
name|String
name|summaryErrorMessage
parameter_list|()
block|{
if|if
condition|(
name|isSuccess
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
if|if
condition|(
name|syntaxError
operator|!=
literal|null
condition|)
block|{
return|return
literal|"Syntax error "
operator|+
name|syntaxError
return|;
block|}
elseif|else
if|if
condition|(
name|unknownComponent
operator|!=
literal|null
condition|)
block|{
return|return
literal|"Unknown component "
operator|+
name|unknownComponent
return|;
block|}
comment|// for each invalid option build a reason message
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|options
init|=
operator|new
name|LinkedHashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
if|if
condition|(
name|unknown
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|String
name|name
range|:
name|unknown
control|)
block|{
name|options
operator|.
name|put
argument_list|(
name|name
argument_list|,
literal|"Unknown field"
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|required
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|String
name|name
range|:
name|required
control|)
block|{
name|options
operator|.
name|put
argument_list|(
name|name
argument_list|,
literal|"Missing required field"
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|invalidEnum
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|entry
range|:
name|invalidEnum
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|String
index|[]
name|choices
init|=
name|invalidEnumChoices
operator|.
name|get
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|str
init|=
name|Arrays
operator|.
name|asList
argument_list|(
name|choices
argument_list|)
operator|.
name|toString
argument_list|()
decl_stmt|;
name|options
operator|.
name|put
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
literal|"Invalid enum value: "
operator|+
name|entry
operator|.
name|getValue
argument_list|()
operator|+
literal|". Possible values: "
operator|+
name|str
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|invalidReference
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|entry
range|:
name|invalidReference
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
operator|!
name|entry
operator|.
name|getValue
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"#"
argument_list|)
condition|)
block|{
name|options
operator|.
name|put
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
literal|"Invalid reference value: "
operator|+
name|entry
operator|.
name|getValue
argument_list|()
operator|+
literal|" must start with #"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|options
operator|.
name|put
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
literal|"Invalid reference value: "
operator|+
name|entry
operator|.
name|getValue
argument_list|()
operator|+
literal|" must not be empty"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|invalidBoolean
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|entry
range|:
name|invalidBoolean
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|options
operator|.
name|put
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
literal|"Invalid boolean value: "
operator|+
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|invalidInteger
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|entry
range|:
name|invalidInteger
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|options
operator|.
name|put
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
literal|"Invalid integer value: "
operator|+
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|invalidNumber
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|entry
range|:
name|invalidNumber
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|options
operator|.
name|put
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
literal|"Invalid number value: "
operator|+
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|// build a table with the error summary nicely formatted
comment|// lets use 24 as min length
name|int
name|maxLen
init|=
literal|24
decl_stmt|;
for|for
control|(
name|String
name|key
range|:
name|options
operator|.
name|keySet
argument_list|()
control|)
block|{
name|maxLen
operator|=
name|Math
operator|.
name|max
argument_list|(
name|maxLen
argument_list|,
name|key
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|String
name|format
init|=
literal|"%"
operator|+
name|maxLen
operator|+
literal|"s    %s"
decl_stmt|;
comment|// build the human error summary
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"Endpoint validator error\n"
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"---------------------------------------------------------------------------------------------------------------------------------------\n"
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"\n\t"
argument_list|)
operator|.
name|append
argument_list|(
name|uri
argument_list|)
operator|.
name|append
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|option
range|:
name|options
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|String
name|out
init|=
name|String
operator|.
name|format
argument_list|(
name|format
argument_list|,
name|option
operator|.
name|getKey
argument_list|()
argument_list|,
name|option
operator|.
name|getValue
argument_list|()
argument_list|)
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"\n\t"
argument_list|)
operator|.
name|append
argument_list|(
name|out
argument_list|)
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
literal|"\n\n"
argument_list|)
expr_stmt|;
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

