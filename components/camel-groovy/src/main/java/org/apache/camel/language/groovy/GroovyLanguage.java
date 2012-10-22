begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.language.groovy
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|language
operator|.
name|groovy
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
name|IsSingleton
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

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|GroovyLanguage
specifier|public
class|class
name|GroovyLanguage
implements|implements
name|Language
implements|,
name|IsSingleton
block|{
DECL|method|groovy (String expression)
specifier|public
specifier|static
name|GroovyExpression
name|groovy
parameter_list|(
name|String
name|expression
parameter_list|)
block|{
return|return
operator|new
name|GroovyLanguage
argument_list|()
operator|.
name|createExpression
argument_list|(
name|expression
argument_list|)
return|;
block|}
DECL|method|createPredicate (String expression)
specifier|public
name|GroovyExpression
name|createPredicate
parameter_list|(
name|String
name|expression
parameter_list|)
block|{
return|return
name|createExpression
argument_list|(
name|expression
argument_list|)
return|;
block|}
DECL|method|createExpression (String expression)
specifier|public
name|GroovyExpression
name|createExpression
parameter_list|(
name|String
name|expression
parameter_list|)
block|{
return|return
operator|new
name|GroovyExpression
argument_list|(
name|expression
argument_list|)
return|;
block|}
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
block|}
end_class

end_unit

