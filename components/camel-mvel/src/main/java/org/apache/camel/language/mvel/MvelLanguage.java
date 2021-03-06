begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.language.mvel
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|language
operator|.
name|mvel
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
name|annotations
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

begin_comment
comment|/**  * An<a href="http://mvel.codehaus.org/">MVEL</a> {@link org.apache.camel.spi.Language} plugin  */
end_comment

begin_class
annotation|@
name|Language
argument_list|(
literal|"mvel"
argument_list|)
DECL|class|MvelLanguage
specifier|public
class|class
name|MvelLanguage
extends|extends
name|LanguageSupport
block|{
annotation|@
name|Override
DECL|method|createPredicate (String expression)
specifier|public
name|Predicate
name|createPredicate
parameter_list|(
name|String
name|expression
parameter_list|)
block|{
name|expression
operator|=
name|loadResource
argument_list|(
name|expression
argument_list|)
expr_stmt|;
return|return
operator|new
name|MvelExpression
argument_list|(
name|this
argument_list|,
name|expression
argument_list|,
name|Boolean
operator|.
name|class
argument_list|)
return|;
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
name|expression
operator|=
name|loadResource
argument_list|(
name|expression
argument_list|)
expr_stmt|;
return|return
operator|new
name|MvelExpression
argument_list|(
name|this
argument_list|,
name|expression
argument_list|,
name|Object
operator|.
name|class
argument_list|)
return|;
block|}
block|}
end_class

end_unit

