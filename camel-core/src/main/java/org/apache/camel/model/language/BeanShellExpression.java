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
name|XmlRootElement
import|;
end_import

begin_comment
comment|/**  * For BeanShell expressions and predicates  *  * @deprecated use {@link org.apache.camel.model.language.LanguageExpression} will be removed in Camel 2.0  * @version $Revision$  */
end_comment

begin_class
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"beanshell"
argument_list|)
DECL|class|BeanShellExpression
specifier|public
class|class
name|BeanShellExpression
extends|extends
name|ExpressionType
block|{
DECL|method|BeanShellExpression ()
specifier|public
name|BeanShellExpression
parameter_list|()
block|{     }
DECL|method|BeanShellExpression (String expression)
specifier|public
name|BeanShellExpression
parameter_list|(
name|String
name|expression
parameter_list|)
block|{
name|super
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
literal|"beanshell"
return|;
block|}
block|}
end_class

end_unit

