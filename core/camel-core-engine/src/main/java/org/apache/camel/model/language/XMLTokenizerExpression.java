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
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlSchemaType
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
comment|/**  * To use Camel message body or header with a XML tokenizer in Camel expressions  * or predicates.  *  * @see org.apache.camel.language.xtokenizer.XMLTokenizeLanguage  */
end_comment

begin_class
annotation|@
name|Metadata
argument_list|(
name|firstVersion
operator|=
literal|"2.14.0"
argument_list|,
name|label
operator|=
literal|"language,core,xml"
argument_list|,
name|title
operator|=
literal|"XML Tokenize"
argument_list|)
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"xtokenize"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|XMLTokenizerExpression
specifier|public
class|class
name|XMLTokenizerExpression
extends|extends
name|NamespaceAwareExpression
block|{
annotation|@
name|XmlAttribute
DECL|field|headerName
specifier|private
name|String
name|headerName
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|mode
specifier|private
name|String
name|mode
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|javaType
operator|=
literal|"java.lang.Integer"
argument_list|)
DECL|field|group
specifier|private
name|String
name|group
decl_stmt|;
DECL|method|XMLTokenizerExpression ()
specifier|public
name|XMLTokenizerExpression
parameter_list|()
block|{     }
DECL|method|XMLTokenizerExpression (String expression)
specifier|public
name|XMLTokenizerExpression
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
literal|"xtokenize"
return|;
block|}
DECL|method|getHeaderName ()
specifier|public
name|String
name|getHeaderName
parameter_list|()
block|{
return|return
name|headerName
return|;
block|}
comment|/**      * Name of header to tokenize instead of using the message body.      */
DECL|method|setHeaderName (String headerName)
specifier|public
name|void
name|setHeaderName
parameter_list|(
name|String
name|headerName
parameter_list|)
block|{
name|this
operator|.
name|headerName
operator|=
name|headerName
expr_stmt|;
block|}
DECL|method|getMode ()
specifier|public
name|String
name|getMode
parameter_list|()
block|{
return|return
name|mode
return|;
block|}
comment|/**      * The extraction mode. The available extraction modes are:      *<ul>      *<li>i - injecting the contextual namespace bindings into the extracted      * token (default)</li>      *<li>w - wrapping the extracted token in its ancestor context</li>      *<li>u - unwrapping the extracted token to its child content</li>      *<li>t - extracting the text content of the specified element</li>      *</ul>      */
DECL|method|setMode (String mode)
specifier|public
name|void
name|setMode
parameter_list|(
name|String
name|mode
parameter_list|)
block|{
name|this
operator|.
name|mode
operator|=
name|mode
expr_stmt|;
block|}
DECL|method|getGroup ()
specifier|public
name|String
name|getGroup
parameter_list|()
block|{
return|return
name|group
return|;
block|}
comment|/**      * To group N parts together      */
DECL|method|setGroup (String group)
specifier|public
name|void
name|setGroup
parameter_list|(
name|String
name|group
parameter_list|)
block|{
name|this
operator|.
name|group
operator|=
name|group
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
name|headerName
operator|!=
literal|null
condition|)
block|{
name|setProperty
argument_list|(
name|camelContext
argument_list|,
name|expression
argument_list|,
literal|"headerName"
argument_list|,
name|headerName
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|mode
operator|!=
literal|null
condition|)
block|{
name|setProperty
argument_list|(
name|camelContext
argument_list|,
name|expression
argument_list|,
literal|"mode"
argument_list|,
name|mode
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|group
operator|!=
literal|null
condition|)
block|{
name|setProperty
argument_list|(
name|camelContext
argument_list|,
name|expression
argument_list|,
literal|"group"
argument_list|,
name|group
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
name|headerName
operator|!=
literal|null
condition|)
block|{
name|setProperty
argument_list|(
name|camelContext
argument_list|,
name|predicate
argument_list|,
literal|"headerName"
argument_list|,
name|headerName
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|mode
operator|!=
literal|null
condition|)
block|{
name|setProperty
argument_list|(
name|camelContext
argument_list|,
name|predicate
argument_list|,
literal|"mode"
argument_list|,
name|mode
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|group
operator|!=
literal|null
condition|)
block|{
name|setProperty
argument_list|(
name|camelContext
argument_list|,
name|predicate
argument_list|,
literal|"group"
argument_list|,
name|group
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

