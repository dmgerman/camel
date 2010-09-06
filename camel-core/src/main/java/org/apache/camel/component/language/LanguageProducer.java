begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.language
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
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
name|impl
operator|.
name|DefaultProducer
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
name|util
operator|.
name|ObjectHelper
import|;
end_import

begin_comment
comment|/**  * Language producer.  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|LanguageProducer
specifier|public
class|class
name|LanguageProducer
extends|extends
name|DefaultProducer
block|{
DECL|method|LanguageProducer (LanguageEndpoint endpoint)
specifier|public
name|LanguageProducer
parameter_list|(
name|LanguageEndpoint
name|endpoint
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
block|}
DECL|method|process (Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
comment|// is there a custom expression in the header?
name|Expression
name|exp
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|Exchange
operator|.
name|LANGUAGE_SCRIPT
argument_list|,
name|Expression
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|exp
operator|==
literal|null
condition|)
block|{
name|String
name|script
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|Exchange
operator|.
name|LANGUAGE_SCRIPT
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|script
operator|!=
literal|null
condition|)
block|{
name|exp
operator|=
name|getEndpoint
argument_list|()
operator|.
name|getLanguage
argument_list|()
operator|.
name|createExpression
argument_list|(
name|script
argument_list|)
expr_stmt|;
block|}
block|}
comment|// if not fallback to use expression from endpoint
if|if
condition|(
name|exp
operator|==
literal|null
condition|)
block|{
name|exp
operator|=
name|getEndpoint
argument_list|()
operator|.
name|getExpression
argument_list|()
expr_stmt|;
block|}
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|exp
argument_list|,
literal|"expression"
argument_list|)
expr_stmt|;
name|Object
name|result
init|=
name|exp
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|,
name|Object
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|log
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Evaluated expression as: "
operator|+
name|result
operator|+
literal|" with: "
operator|+
name|exchange
argument_list|)
expr_stmt|;
block|}
comment|// set message body if transform is enabled
if|if
condition|(
name|getEndpoint
argument_list|()
operator|.
name|isTransform
argument_list|()
condition|)
block|{
if|if
condition|(
name|exchange
operator|.
name|hasOut
argument_list|()
condition|)
block|{
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|result
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|result
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Override
DECL|method|getEndpoint ()
specifier|public
name|LanguageEndpoint
name|getEndpoint
parameter_list|()
block|{
return|return
operator|(
name|LanguageEndpoint
operator|)
name|super
operator|.
name|getEndpoint
argument_list|()
return|;
block|}
block|}
end_class

end_unit

