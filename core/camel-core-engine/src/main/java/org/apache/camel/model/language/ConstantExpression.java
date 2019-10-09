begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|XmlRootElement
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
comment|/**  * To use a constant value in Camel expressions or predicates.  *<p/>  *<b>Important:</b>  * this is a fixed constant value that is only set once during starting up the  * route, do not use this if you want dynamic values during routing.  */
end_comment

begin_class
annotation|@
name|Metadata
argument_list|(
name|firstVersion
operator|=
literal|"1.5.0"
argument_list|,
name|label
operator|=
literal|"language,core"
argument_list|,
name|title
operator|=
literal|"Constant"
argument_list|)
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"constant"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|ConstantExpression
specifier|public
class|class
name|ConstantExpression
extends|extends
name|ExpressionDefinition
block|{
DECL|method|ConstantExpression ()
specifier|public
name|ConstantExpression
parameter_list|()
block|{     }
DECL|method|ConstantExpression (String expression)
specifier|public
name|ConstantExpression
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
annotation|@
name|Override
DECL|method|getLanguage ()
specifier|public
name|String
name|getLanguage
parameter_list|()
block|{
return|return
literal|"constant"
return|;
block|}
block|}
end_class

end_unit

