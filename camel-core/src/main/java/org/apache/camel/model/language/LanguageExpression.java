begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.model.language
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|model
operator|.
name|language
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessorType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAttribute
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlRootElement
import|;
end_import

begin_comment
comment|/**  * Represents a parameterised language expression which can support any language  * at runtime using the language attribute.  *  * @version   */
end_comment

begin_class
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"language"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|LanguageExpression
specifier|public
class|class
name|LanguageExpression
extends|extends
name|ExpressionDefinition
block|{
annotation|@
name|XmlAttribute
DECL|field|language
specifier|private
name|String
name|language
decl_stmt|;
DECL|method|LanguageExpression ()
specifier|public
name|LanguageExpression
parameter_list|()
block|{     }
DECL|method|LanguageExpression (String language, String expression)
specifier|public
name|LanguageExpression
parameter_list|(
name|String
name|language
parameter_list|,
name|String
name|expression
parameter_list|)
block|{
name|setLanguage
argument_list|(
name|language
argument_list|)
expr_stmt|;
name|setExpression
argument_list|(
name|expression
argument_list|)
expr_stmt|;
block|}
DECL|method|getLanguage ()
specifier|public
name|String
name|getLanguage
parameter_list|()
block|{
return|return
name|language
return|;
block|}
DECL|method|setLanguage (String language)
specifier|public
name|void
name|setLanguage
parameter_list|(
name|String
name|language
parameter_list|)
block|{
name|this
operator|.
name|language
operator|=
name|language
expr_stmt|;
block|}
block|}
end_class

end_unit

