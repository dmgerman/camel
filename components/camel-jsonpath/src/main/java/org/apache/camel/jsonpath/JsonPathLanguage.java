begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.jsonpath
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|jsonpath
package|;
end_package

begin_import
import|import
name|com
operator|.
name|jayway
operator|.
name|jsonpath
operator|.
name|Option
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
name|support
operator|.
name|LanguageSupport
import|;
end_import

begin_class
DECL|class|JsonPathLanguage
specifier|public
class|class
name|JsonPathLanguage
extends|extends
name|LanguageSupport
block|{
DECL|field|resultType
specifier|private
name|Class
argument_list|<
name|?
argument_list|>
name|resultType
decl_stmt|;
DECL|field|options
specifier|private
name|Option
index|[]
name|options
decl_stmt|;
DECL|method|getResultType ()
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
name|getResultType
parameter_list|()
block|{
return|return
name|resultType
return|;
block|}
DECL|method|setResultType (Class<?> resultType)
specifier|public
name|void
name|setResultType
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|resultType
parameter_list|)
block|{
name|this
operator|.
name|resultType
operator|=
name|resultType
expr_stmt|;
block|}
DECL|method|getOptions ()
specifier|public
name|Option
index|[]
name|getOptions
parameter_list|()
block|{
return|return
name|options
return|;
block|}
DECL|method|setOption (Option option)
specifier|public
name|void
name|setOption
parameter_list|(
name|Option
name|option
parameter_list|)
block|{
name|this
operator|.
name|options
operator|=
operator|new
name|Option
index|[]
block|{
name|option
block|}
expr_stmt|;
block|}
DECL|method|setOptions (Option[] options)
specifier|public
name|void
name|setOptions
parameter_list|(
name|Option
index|[]
name|options
parameter_list|)
block|{
name|this
operator|.
name|options
operator|=
name|options
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createPredicate (final String predicate)
specifier|public
name|Predicate
name|createPredicate
parameter_list|(
specifier|final
name|String
name|predicate
parameter_list|)
block|{
name|JsonPathExpression
name|answer
init|=
operator|new
name|JsonPathExpression
argument_list|(
name|predicate
argument_list|)
decl_stmt|;
name|answer
operator|.
name|setResultType
argument_list|(
name|resultType
argument_list|)
expr_stmt|;
name|answer
operator|.
name|setOptions
argument_list|(
name|options
argument_list|)
expr_stmt|;
name|answer
operator|.
name|init
argument_list|()
expr_stmt|;
return|return
name|answer
return|;
block|}
annotation|@
name|Override
DECL|method|createExpression (final String expression)
specifier|public
name|Expression
name|createExpression
parameter_list|(
specifier|final
name|String
name|expression
parameter_list|)
block|{
name|JsonPathExpression
name|answer
init|=
operator|new
name|JsonPathExpression
argument_list|(
name|expression
argument_list|)
decl_stmt|;
name|answer
operator|.
name|setResultType
argument_list|(
name|resultType
argument_list|)
expr_stmt|;
name|answer
operator|.
name|setOptions
argument_list|(
name|options
argument_list|)
expr_stmt|;
name|answer
operator|.
name|init
argument_list|()
expr_stmt|;
return|return
name|answer
return|;
block|}
annotation|@
name|Override
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
comment|// cannot be singleton due options
return|return
literal|false
return|;
block|}
block|}
end_class

end_unit

