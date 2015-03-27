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
name|Metadata
import|;
end_import

begin_comment
comment|/**  * For JXPath expressions and predicates  *  * @version   */
end_comment

begin_class
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"language"
argument_list|,
name|title
operator|=
literal|"JXPath"
argument_list|)
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"jxpath"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|JXPathExpression
specifier|public
class|class
name|JXPathExpression
extends|extends
name|ExpressionDefinition
block|{
annotation|@
name|XmlAttribute
DECL|field|lenient
specifier|private
name|Boolean
name|lenient
decl_stmt|;
DECL|method|JXPathExpression ()
specifier|public
name|JXPathExpression
parameter_list|()
block|{     }
DECL|method|JXPathExpression (String expression)
specifier|public
name|JXPathExpression
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
literal|"jxpath"
return|;
block|}
DECL|method|getLenient ()
specifier|public
name|Boolean
name|getLenient
parameter_list|()
block|{
return|return
name|lenient
return|;
block|}
comment|/**      * Allows to turn lenient on the JXPathContext.      * When turned on this allows the JXPath expression to evaluate against expressions and message bodies which may      * be invalid / missing data.      *<p/>      * This option is by default false      */
DECL|method|setLenient (Boolean lenient)
specifier|public
name|void
name|setLenient
parameter_list|(
name|Boolean
name|lenient
parameter_list|)
block|{
name|this
operator|.
name|lenient
operator|=
name|lenient
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|configureExpression (CamelContext camelContext, Expression expression)
specifier|protected
name|void
name|configureExpression
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|Expression
name|expression
parameter_list|)
block|{
if|if
condition|(
name|lenient
operator|!=
literal|null
condition|)
block|{
name|setProperty
argument_list|(
name|expression
argument_list|,
literal|"lenient"
argument_list|,
name|lenient
argument_list|)
expr_stmt|;
block|}
name|super
operator|.
name|configureExpression
argument_list|(
name|camelContext
argument_list|,
name|expression
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|configurePredicate (CamelContext camelContext, Predicate predicate)
specifier|protected
name|void
name|configurePredicate
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|Predicate
name|predicate
parameter_list|)
block|{
if|if
condition|(
name|lenient
operator|!=
literal|null
condition|)
block|{
name|setProperty
argument_list|(
name|predicate
argument_list|,
literal|"lenient"
argument_list|,
name|lenient
argument_list|)
expr_stmt|;
block|}
name|super
operator|.
name|configurePredicate
argument_list|(
name|camelContext
argument_list|,
name|predicate
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

