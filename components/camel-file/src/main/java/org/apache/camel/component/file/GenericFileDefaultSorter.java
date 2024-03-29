begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.file
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|file
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Comparator
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
name|CamelContext
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
name|Exchange
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
name|Expression
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
name|spi
operator|.
name|Language
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
name|support
operator|.
name|ObjectHelper
import|;
end_import

begin_comment
comment|/**  * Default file sorter.  */
end_comment

begin_class
DECL|class|GenericFileDefaultSorter
specifier|public
specifier|final
class|class
name|GenericFileDefaultSorter
block|{
DECL|method|GenericFileDefaultSorter ()
specifier|private
name|GenericFileDefaultSorter
parameter_list|()
block|{     }
comment|/**      * Returns a new sory by file language expression      *      * @param context    the camel context      * @param expression the file language expression      * @param reverse    true to reverse order      * @return the comparator      */
DECL|method|sortByFileLanguage ( CamelContext context, String expression, boolean reverse)
specifier|public
specifier|static
name|Comparator
argument_list|<
name|Exchange
argument_list|>
name|sortByFileLanguage
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|String
name|expression
parameter_list|,
name|boolean
name|reverse
parameter_list|)
block|{
return|return
name|sortByFileLanguage
argument_list|(
name|context
argument_list|,
name|expression
argument_list|,
name|reverse
argument_list|,
literal|false
argument_list|,
literal|null
argument_list|)
return|;
block|}
comment|/**      * Returns a new sory by file language expression      *      * @param context    the camel context      * @param expression the file language expression      * @param reverse    true to reverse order      * @param ignoreCase ignore case if comparing strings      * @return the comparator      */
DECL|method|sortByFileLanguage ( CamelContext context, String expression, boolean reverse, boolean ignoreCase)
specifier|public
specifier|static
name|Comparator
argument_list|<
name|Exchange
argument_list|>
name|sortByFileLanguage
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|String
name|expression
parameter_list|,
name|boolean
name|reverse
parameter_list|,
name|boolean
name|ignoreCase
parameter_list|)
block|{
return|return
name|sortByFileLanguage
argument_list|(
name|context
argument_list|,
name|expression
argument_list|,
name|reverse
argument_list|,
name|ignoreCase
argument_list|,
literal|null
argument_list|)
return|;
block|}
comment|/**      * Returns a new sort by file language expression      *      * @param context    the camel context      * @param expression the file language expression      * @param reverse    true to reverse order      * @param ignoreCase ignore case if comparing strings      * @param nested     nested comparator for sub group sorting, can be null      * @return the comparator      */
DECL|method|sortByFileLanguage ( final CamelContext context, final String expression, final boolean reverse, final boolean ignoreCase, final Comparator<Exchange> nested)
specifier|public
specifier|static
name|Comparator
argument_list|<
name|Exchange
argument_list|>
name|sortByFileLanguage
parameter_list|(
specifier|final
name|CamelContext
name|context
parameter_list|,
specifier|final
name|String
name|expression
parameter_list|,
specifier|final
name|boolean
name|reverse
parameter_list|,
specifier|final
name|boolean
name|ignoreCase
parameter_list|,
specifier|final
name|Comparator
argument_list|<
name|Exchange
argument_list|>
name|nested
parameter_list|)
block|{
comment|// the expression should be enclosed by ${ }
name|String
name|text
init|=
name|expression
decl_stmt|;
if|if
condition|(
operator|!
name|expression
operator|.
name|startsWith
argument_list|(
literal|"${"
argument_list|)
condition|)
block|{
name|text
operator|=
literal|"${"
operator|+
name|text
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|expression
operator|.
name|endsWith
argument_list|(
literal|"}"
argument_list|)
condition|)
block|{
name|text
operator|=
name|text
operator|+
literal|"}"
expr_stmt|;
block|}
name|Language
name|language
init|=
name|context
operator|.
name|resolveLanguage
argument_list|(
literal|"file"
argument_list|)
decl_stmt|;
specifier|final
name|Expression
name|exp
init|=
name|language
operator|.
name|createExpression
argument_list|(
name|text
argument_list|)
decl_stmt|;
return|return
operator|new
name|Comparator
argument_list|<
name|Exchange
argument_list|>
argument_list|()
block|{
specifier|public
name|int
name|compare
parameter_list|(
name|Exchange
name|o1
parameter_list|,
name|Exchange
name|o2
parameter_list|)
block|{
name|Object
name|result1
init|=
name|exp
operator|.
name|evaluate
argument_list|(
name|o1
argument_list|,
name|Object
operator|.
name|class
argument_list|)
decl_stmt|;
name|Object
name|result2
init|=
name|exp
operator|.
name|evaluate
argument_list|(
name|o2
argument_list|,
name|Object
operator|.
name|class
argument_list|)
decl_stmt|;
name|int
name|answer
init|=
name|ObjectHelper
operator|.
name|compare
argument_list|(
name|result1
argument_list|,
name|result2
argument_list|,
name|ignoreCase
argument_list|)
decl_stmt|;
comment|// if equal then sub sort by nested comparator
if|if
condition|(
name|answer
operator|==
literal|0
operator|&&
name|nested
operator|!=
literal|null
condition|)
block|{
name|answer
operator|=
name|nested
operator|.
name|compare
argument_list|(
name|o1
argument_list|,
name|o2
argument_list|)
expr_stmt|;
block|}
return|return
name|reverse
condition|?
operator|-
literal|1
operator|*
name|answer
else|:
name|answer
return|;
block|}
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|expression
operator|+
operator|(
name|nested
operator|!=
literal|null
condition|?
literal|";"
operator|+
name|nested
operator|.
name|toString
argument_list|()
else|:
literal|""
operator|)
return|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

