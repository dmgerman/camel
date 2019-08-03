begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.language.xpath
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|language
operator|.
name|xpath
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|namespace
operator|.
name|QName
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|xpath
operator|.
name|XPathFactory
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
name|annotations
operator|.
name|Language
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

begin_comment
comment|/**  * XPath language.  */
end_comment

begin_class
annotation|@
name|Language
argument_list|(
literal|"xpath"
argument_list|)
DECL|class|XPathLanguage
specifier|public
class|class
name|XPathLanguage
extends|extends
name|LanguageSupport
block|{
DECL|field|resultType
specifier|private
name|QName
name|resultType
decl_stmt|;
DECL|field|xpathFactory
specifier|private
name|XPathFactory
name|xpathFactory
decl_stmt|;
DECL|field|useSaxon
specifier|private
name|Boolean
name|useSaxon
decl_stmt|;
DECL|field|objectModelUri
specifier|private
name|String
name|objectModelUri
decl_stmt|;
DECL|field|threadSafety
specifier|private
name|Boolean
name|threadSafety
decl_stmt|;
DECL|field|logNamespaces
specifier|private
name|Boolean
name|logNamespaces
decl_stmt|;
DECL|field|headerName
specifier|private
name|String
name|headerName
decl_stmt|;
annotation|@
name|Override
DECL|method|createPredicate (String expression)
specifier|public
name|Predicate
name|createPredicate
parameter_list|(
name|String
name|expression
parameter_list|)
block|{
name|expression
operator|=
name|loadResource
argument_list|(
name|expression
argument_list|)
expr_stmt|;
name|XPathBuilder
name|builder
init|=
name|XPathBuilder
operator|.
name|xpath
argument_list|(
name|expression
argument_list|)
decl_stmt|;
name|configureBuilder
argument_list|(
name|builder
argument_list|)
expr_stmt|;
return|return
name|builder
return|;
block|}
annotation|@
name|Override
DECL|method|createExpression (String expression)
specifier|public
name|Expression
name|createExpression
parameter_list|(
name|String
name|expression
parameter_list|)
block|{
name|expression
operator|=
name|loadResource
argument_list|(
name|expression
argument_list|)
expr_stmt|;
name|XPathBuilder
name|builder
init|=
name|XPathBuilder
operator|.
name|xpath
argument_list|(
name|expression
argument_list|)
decl_stmt|;
name|configureBuilder
argument_list|(
name|builder
argument_list|)
expr_stmt|;
return|return
name|builder
return|;
block|}
DECL|method|getResultType ()
specifier|public
name|QName
name|getResultType
parameter_list|()
block|{
return|return
name|resultType
return|;
block|}
DECL|method|setResultType (QName resultType)
specifier|public
name|void
name|setResultType
parameter_list|(
name|QName
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
DECL|method|getXpathFactory ()
specifier|public
name|XPathFactory
name|getXpathFactory
parameter_list|()
block|{
return|return
name|xpathFactory
return|;
block|}
DECL|method|setXpathFactory (XPathFactory xpathFactory)
specifier|public
name|void
name|setXpathFactory
parameter_list|(
name|XPathFactory
name|xpathFactory
parameter_list|)
block|{
name|this
operator|.
name|xpathFactory
operator|=
name|xpathFactory
expr_stmt|;
block|}
DECL|method|setUseSaxon (Boolean useSaxon)
specifier|public
name|void
name|setUseSaxon
parameter_list|(
name|Boolean
name|useSaxon
parameter_list|)
block|{
name|this
operator|.
name|useSaxon
operator|=
name|useSaxon
expr_stmt|;
block|}
DECL|method|getUseSaxon ()
specifier|public
name|Boolean
name|getUseSaxon
parameter_list|()
block|{
return|return
name|useSaxon
return|;
block|}
DECL|method|getObjectModelUri ()
specifier|public
name|String
name|getObjectModelUri
parameter_list|()
block|{
return|return
name|objectModelUri
return|;
block|}
DECL|method|setObjectModelUri (String objectModelUri)
specifier|public
name|void
name|setObjectModelUri
parameter_list|(
name|String
name|objectModelUri
parameter_list|)
block|{
name|this
operator|.
name|objectModelUri
operator|=
name|objectModelUri
expr_stmt|;
block|}
DECL|method|getThreadSafety ()
specifier|public
name|Boolean
name|getThreadSafety
parameter_list|()
block|{
return|return
name|threadSafety
return|;
block|}
DECL|method|setThreadSafety (Boolean threadSafety)
specifier|public
name|void
name|setThreadSafety
parameter_list|(
name|Boolean
name|threadSafety
parameter_list|)
block|{
name|this
operator|.
name|threadSafety
operator|=
name|threadSafety
expr_stmt|;
block|}
DECL|method|getLogNamespaces ()
specifier|public
name|Boolean
name|getLogNamespaces
parameter_list|()
block|{
return|return
name|logNamespaces
return|;
block|}
DECL|method|setLogNamespaces (Boolean logNamespaces)
specifier|public
name|void
name|setLogNamespaces
parameter_list|(
name|Boolean
name|logNamespaces
parameter_list|)
block|{
name|this
operator|.
name|logNamespaces
operator|=
name|logNamespaces
expr_stmt|;
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
DECL|method|isUseSaxon ()
specifier|private
name|boolean
name|isUseSaxon
parameter_list|()
block|{
return|return
name|useSaxon
operator|!=
literal|null
operator|&&
name|useSaxon
return|;
block|}
DECL|method|configureBuilder (XPathBuilder builder)
specifier|protected
name|void
name|configureBuilder
parameter_list|(
name|XPathBuilder
name|builder
parameter_list|)
block|{
if|if
condition|(
name|threadSafety
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|setThreadSafety
argument_list|(
name|threadSafety
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|resultType
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|setResultQName
argument_list|(
name|resultType
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|logNamespaces
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|setLogNamespaces
argument_list|(
name|logNamespaces
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|headerName
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|setHeaderName
argument_list|(
name|headerName
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|isUseSaxon
argument_list|()
condition|)
block|{
name|builder
operator|.
name|enableSaxon
argument_list|()
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|xpathFactory
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|setXPathFactory
argument_list|(
name|xpathFactory
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|objectModelUri
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|setObjectModelUri
argument_list|(
name|objectModelUri
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Override
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
block|}
end_class

end_unit

