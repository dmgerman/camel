begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.model
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|model
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
name|XmlElementRef
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
name|model
operator|.
name|language
operator|.
name|ExpressionDefinition
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
name|Metadata
import|;
end_import

begin_comment
comment|/**  * Allows to declare options on sagas  */
end_comment

begin_class
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"eip,routing"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|SagaOptionDefinition
specifier|public
class|class
name|SagaOptionDefinition
block|{
annotation|@
name|XmlAttribute
argument_list|(
name|required
operator|=
literal|true
argument_list|)
DECL|field|optionName
specifier|private
name|String
name|optionName
decl_stmt|;
annotation|@
name|XmlElementRef
DECL|field|expression
specifier|private
name|ExpressionDefinition
name|expression
decl_stmt|;
DECL|method|SagaOptionDefinition ()
specifier|public
name|SagaOptionDefinition
parameter_list|()
block|{     }
DECL|method|SagaOptionDefinition (String optionName, Expression expression)
specifier|public
name|SagaOptionDefinition
parameter_list|(
name|String
name|optionName
parameter_list|,
name|Expression
name|expression
parameter_list|)
block|{
name|setOptionName
argument_list|(
name|optionName
argument_list|)
expr_stmt|;
name|setExpression
argument_list|(
name|ExpressionNodeHelper
operator|.
name|toExpressionDefinition
argument_list|(
name|expression
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"option:"
operator|+
name|getOptionName
argument_list|()
operator|+
literal|"="
operator|+
name|getExpression
argument_list|()
return|;
block|}
comment|/**      * Name of the option. It identifies the name of the header where the value      * of the expression will be stored when the compensation or completion      * routes will be called.      */
DECL|method|setOptionName (String optionName)
specifier|public
name|void
name|setOptionName
parameter_list|(
name|String
name|optionName
parameter_list|)
block|{
name|this
operator|.
name|optionName
operator|=
name|optionName
expr_stmt|;
block|}
DECL|method|getOptionName ()
specifier|public
name|String
name|getOptionName
parameter_list|()
block|{
return|return
name|optionName
return|;
block|}
DECL|method|getExpression ()
specifier|public
name|ExpressionDefinition
name|getExpression
parameter_list|()
block|{
return|return
name|expression
return|;
block|}
comment|/**      * The expression to be used to determine the value of the option.      */
DECL|method|setExpression (ExpressionDefinition expression)
specifier|public
name|void
name|setExpression
parameter_list|(
name|ExpressionDefinition
name|expression
parameter_list|)
block|{
name|this
operator|.
name|expression
operator|=
name|expression
expr_stmt|;
block|}
block|}
end_class

end_unit

