begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.builder.script
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
operator|.
name|script
package|;
end_package

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
name|Predicate
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
name|LanguageSupport
import|;
end_import

begin_class
annotation|@
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spi
operator|.
name|annotations
operator|.
name|Language
argument_list|(
literal|"javaScript"
argument_list|)
DECL|class|JavaScriptLanguage
specifier|public
class|class
name|JavaScriptLanguage
extends|extends
name|LanguageSupport
block|{
annotation|@
name|Override
DECL|method|createPredicate (String predicate)
specifier|public
name|Predicate
name|createPredicate
parameter_list|(
name|String
name|predicate
parameter_list|)
block|{
name|Language
name|language
init|=
operator|new
name|ScriptLanguageResolver
argument_list|()
operator|.
name|resolveLanguage
argument_list|(
literal|"js"
argument_list|,
name|getCamelContext
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|language
operator|!=
literal|null
condition|)
block|{
return|return
name|language
operator|.
name|createPredicate
argument_list|(
name|predicate
argument_list|)
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|createExpression (String expression)
specifier|public
name|Expression
name|createExpression
parameter_list|(
name|String
name|expression
parameter_list|)
block|{
name|Language
name|language
init|=
operator|new
name|ScriptLanguageResolver
argument_list|()
operator|.
name|resolveLanguage
argument_list|(
literal|"js"
argument_list|,
name|getCamelContext
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|language
operator|!=
literal|null
condition|)
block|{
return|return
name|language
operator|.
name|createExpression
argument_list|(
name|expression
argument_list|)
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
block|}
end_class

end_unit

