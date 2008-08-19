begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.language
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|language
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
name|RuntimeCamelException
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
comment|/**  * An exception thrown if some illegal syntax is rejected by a specific language  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|IllegalSyntaxException
specifier|public
class|class
name|IllegalSyntaxException
extends|extends
name|RuntimeCamelException
block|{
DECL|field|language
specifier|private
specifier|final
name|Language
name|language
decl_stmt|;
DECL|field|expression
specifier|private
specifier|final
name|String
name|expression
decl_stmt|;
DECL|method|IllegalSyntaxException (Language language, String expression)
specifier|public
name|IllegalSyntaxException
parameter_list|(
name|Language
name|language
parameter_list|,
name|String
name|expression
parameter_list|)
block|{
name|this
argument_list|(
name|language
argument_list|,
name|expression
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|IllegalSyntaxException (Language language, String expression, Throwable cause)
specifier|public
name|IllegalSyntaxException
parameter_list|(
name|Language
name|language
parameter_list|,
name|String
name|expression
parameter_list|,
name|Throwable
name|cause
parameter_list|)
block|{
name|super
argument_list|(
literal|"Illegal syntax for language: "
operator|+
name|language
operator|+
literal|". Expression: "
operator|+
name|expression
argument_list|,
name|cause
argument_list|)
expr_stmt|;
name|this
operator|.
name|language
operator|=
name|language
expr_stmt|;
name|this
operator|.
name|expression
operator|=
name|expression
expr_stmt|;
block|}
DECL|method|getExpression ()
specifier|public
name|String
name|getExpression
parameter_list|()
block|{
return|return
name|expression
return|;
block|}
DECL|method|getLanguage ()
specifier|public
name|Language
name|getLanguage
parameter_list|()
block|{
return|return
name|language
return|;
block|}
block|}
end_class

end_unit

