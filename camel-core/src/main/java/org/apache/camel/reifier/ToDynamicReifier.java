begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.reifier
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|reifier
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
name|NoSuchLanguageException
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
name|Processor
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
name|builder
operator|.
name|ExpressionBuilder
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
name|model
operator|.
name|ProcessorDefinition
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
name|model
operator|.
name|ToDynamicDefinition
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
name|processor
operator|.
name|SendDynamicProcessor
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
name|spi
operator|.
name|RouteContext
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
name|util
operator|.
name|Pair
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
name|util
operator|.
name|StringHelper
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
name|util
operator|.
name|URISupport
import|;
end_import

begin_class
DECL|class|ToDynamicReifier
class|class
name|ToDynamicReifier
parameter_list|<
name|T
extends|extends
name|ToDynamicDefinition
parameter_list|>
extends|extends
name|ProcessorReifier
argument_list|<
name|T
argument_list|>
block|{
DECL|method|ToDynamicReifier (ProcessorDefinition<?> definition)
name|ToDynamicReifier
parameter_list|(
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
name|definition
parameter_list|)
block|{
name|super
argument_list|(
operator|(
name|T
operator|)
name|definition
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createProcessor (RouteContext routeContext)
specifier|public
name|Processor
name|createProcessor
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|)
throws|throws
name|Exception
block|{
name|StringHelper
operator|.
name|notEmpty
argument_list|(
name|definition
operator|.
name|getUri
argument_list|()
argument_list|,
literal|"uri"
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|Expression
name|exp
init|=
name|createExpression
argument_list|(
name|routeContext
argument_list|)
decl_stmt|;
name|SendDynamicProcessor
name|processor
init|=
operator|new
name|SendDynamicProcessor
argument_list|(
name|definition
operator|.
name|getUri
argument_list|()
argument_list|,
name|exp
argument_list|)
decl_stmt|;
name|processor
operator|.
name|setCamelContext
argument_list|(
name|routeContext
operator|.
name|getCamelContext
argument_list|()
argument_list|)
expr_stmt|;
name|processor
operator|.
name|setPattern
argument_list|(
name|definition
operator|.
name|getPattern
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|definition
operator|.
name|getCacheSize
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|processor
operator|.
name|setCacheSize
argument_list|(
name|definition
operator|.
name|getCacheSize
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|definition
operator|.
name|getIgnoreInvalidEndpoint
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|processor
operator|.
name|setIgnoreInvalidEndpoint
argument_list|(
name|definition
operator|.
name|getIgnoreInvalidEndpoint
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|processor
return|;
block|}
DECL|method|createExpression (RouteContext routeContext)
specifier|protected
name|Expression
name|createExpression
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|)
block|{
name|List
argument_list|<
name|Expression
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|String
index|[]
name|parts
init|=
name|safeSplitRaw
argument_list|(
name|definition
operator|.
name|getUri
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|part
range|:
name|parts
control|)
block|{
comment|// the part may have optional language to use, so you can mix languages
name|String
name|value
init|=
name|StringHelper
operator|.
name|after
argument_list|(
name|part
argument_list|,
literal|"language:"
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
name|String
name|before
init|=
name|StringHelper
operator|.
name|before
argument_list|(
name|value
argument_list|,
literal|":"
argument_list|)
decl_stmt|;
name|String
name|after
init|=
name|StringHelper
operator|.
name|after
argument_list|(
name|value
argument_list|,
literal|":"
argument_list|)
decl_stmt|;
if|if
condition|(
name|before
operator|!=
literal|null
operator|&&
name|after
operator|!=
literal|null
condition|)
block|{
comment|// maybe its a language, must have language: as prefix
try|try
block|{
name|Language
name|partLanguage
init|=
name|routeContext
operator|.
name|getCamelContext
argument_list|()
operator|.
name|resolveLanguage
argument_list|(
name|before
argument_list|)
decl_stmt|;
if|if
condition|(
name|partLanguage
operator|!=
literal|null
condition|)
block|{
name|Expression
name|exp
init|=
name|partLanguage
operator|.
name|createExpression
argument_list|(
name|after
argument_list|)
decl_stmt|;
name|list
operator|.
name|add
argument_list|(
name|exp
argument_list|)
expr_stmt|;
continue|continue;
block|}
block|}
catch|catch
parameter_list|(
name|NoSuchLanguageException
name|e
parameter_list|)
block|{
comment|// ignore
block|}
block|}
block|}
comment|// fallback and use simple language
name|Language
name|lan
init|=
name|routeContext
operator|.
name|getCamelContext
argument_list|()
operator|.
name|resolveLanguage
argument_list|(
literal|"simple"
argument_list|)
decl_stmt|;
name|Expression
name|exp
init|=
name|lan
operator|.
name|createExpression
argument_list|(
name|part
argument_list|)
decl_stmt|;
name|list
operator|.
name|add
argument_list|(
name|exp
argument_list|)
expr_stmt|;
block|}
name|Expression
name|exp
decl_stmt|;
if|if
condition|(
name|list
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
name|exp
operator|=
name|list
operator|.
name|get
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|exp
operator|=
name|ExpressionBuilder
operator|.
name|concatExpression
argument_list|(
name|list
argument_list|)
expr_stmt|;
block|}
return|return
name|exp
return|;
block|}
comment|// Utilities
comment|// -------------------------------------------------------------------------
comment|/**      * We need to split the string safely for each + sign, but avoid splitting within RAW(...).      */
DECL|method|safeSplitRaw (String s)
specifier|private
specifier|static
name|String
index|[]
name|safeSplitRaw
parameter_list|(
name|String
name|s
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|s
operator|.
name|contains
argument_list|(
literal|"+"
argument_list|)
condition|)
block|{
comment|// no plus sign so there is only one part, so no need to split
name|list
operator|.
name|add
argument_list|(
name|s
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// there is a plus sign so we need to split in a safe manner
name|List
argument_list|<
name|Pair
argument_list|<
name|Integer
argument_list|>
argument_list|>
name|rawPairs
init|=
name|URISupport
operator|.
name|scanRaw
argument_list|(
name|s
argument_list|)
decl_stmt|;
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|char
name|chars
index|[]
init|=
name|s
operator|.
name|toCharArray
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
name|chars
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|char
name|ch
init|=
name|chars
index|[
name|i
index|]
decl_stmt|;
if|if
condition|(
name|ch
operator|!=
literal|'+'
operator|||
name|URISupport
operator|.
name|isRaw
argument_list|(
name|i
argument_list|,
name|rawPairs
argument_list|)
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
else|else
block|{
name|list
operator|.
name|add
argument_list|(
name|sb
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|sb
operator|.
name|setLength
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
block|}
comment|// any leftover?
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
name|list
operator|.
name|add
argument_list|(
name|sb
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|sb
operator|.
name|setLength
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|list
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
name|list
operator|.
name|size
argument_list|()
index|]
argument_list|)
return|;
block|}
block|}
end_class

end_unit

