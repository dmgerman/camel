begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|CamelContext
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
name|Exchange
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
name|impl
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
name|spi
operator|.
name|Language
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
name|XmlID
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
name|XmlType
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
name|XmlValue
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
name|adapters
operator|.
name|CollapsedStringAdapter
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
name|adapters
operator|.
name|XmlJavaTypeAdapter
import|;
end_import

begin_comment
comment|/**  * A useful base class for an expression  *  * @version $Revision: 1.1 $  */
end_comment

begin_class
annotation|@
name|XmlType
argument_list|(
name|name
operator|=
literal|"expressionType"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|ExpressionType
specifier|public
specifier|abstract
class|class
name|ExpressionType
block|{
annotation|@
name|XmlAttribute
annotation|@
name|XmlJavaTypeAdapter
argument_list|(
name|CollapsedStringAdapter
operator|.
name|class
argument_list|)
annotation|@
name|XmlID
DECL|field|id
specifier|private
name|String
name|id
decl_stmt|;
annotation|@
name|XmlValue
DECL|field|expression
specifier|private
name|String
name|expression
decl_stmt|;
DECL|method|getLanguage ()
specifier|public
specifier|abstract
name|String
name|getLanguage
parameter_list|()
function_decl|;
DECL|method|ExpressionType ()
specifier|protected
name|ExpressionType
parameter_list|()
block|{     }
DECL|method|ExpressionType (String expression)
specifier|protected
name|ExpressionType
parameter_list|(
name|String
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
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|getLanguage
argument_list|()
operator|+
literal|"Expression["
operator|+
name|getExpression
argument_list|()
operator|+
literal|"]"
return|;
block|}
DECL|method|createPredicate (RouteContext route)
specifier|public
name|Predicate
argument_list|<
name|Exchange
argument_list|>
name|createPredicate
parameter_list|(
name|RouteContext
name|route
parameter_list|)
block|{
name|CamelContext
name|camelContext
init|=
name|route
operator|.
name|getCamelContext
argument_list|()
decl_stmt|;
name|Language
name|language
init|=
name|camelContext
operator|.
name|resolveLanguage
argument_list|(
name|getLanguage
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|language
operator|.
name|createPredicate
argument_list|(
name|getExpression
argument_list|()
argument_list|)
return|;
block|}
DECL|method|createExpression (RouteContext routeContext)
specifier|public
name|Expression
name|createExpression
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|)
block|{
name|CamelContext
name|camelContext
init|=
name|routeContext
operator|.
name|getCamelContext
argument_list|()
decl_stmt|;
name|Language
name|language
init|=
name|camelContext
operator|.
name|resolveLanguage
argument_list|(
name|getLanguage
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|language
operator|.
name|createExpression
argument_list|(
name|getExpression
argument_list|()
argument_list|)
return|;
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
DECL|method|setExpression (String expression)
specifier|public
name|void
name|setExpression
parameter_list|(
name|String
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
comment|/**      * Gets the value of the id property.      *      * @return possible object is      *         {@link String }      */
DECL|method|getId ()
specifier|public
name|String
name|getId
parameter_list|()
block|{
return|return
name|id
return|;
block|}
comment|/**      * Sets the value of the id property.      *      * @param value allowed object is      *              {@link String }      */
DECL|method|setId (String value)
specifier|public
name|void
name|setId
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|this
operator|.
name|id
operator|=
name|value
expr_stmt|;
block|}
block|}
end_class

end_unit

