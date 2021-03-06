begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.wordpress.producer
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|wordpress
operator|.
name|producer
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
name|component
operator|.
name|wordpress
operator|.
name|WordpressComponentConfiguration
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
name|component
operator|.
name|wordpress
operator|.
name|WordpressEndpoint
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
name|component
operator|.
name|wordpress
operator|.
name|api
operator|.
name|WordpressServiceProvider
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
name|DefaultProducer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_class
DECL|class|AbstractWordpressProducer
specifier|public
specifier|abstract
class|class
name|AbstractWordpressProducer
parameter_list|<
name|T
parameter_list|>
extends|extends
name|DefaultProducer
block|{
DECL|field|LOG
specifier|protected
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|WordpressPostProducer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|configuration
specifier|private
name|WordpressComponentConfiguration
name|configuration
decl_stmt|;
DECL|method|AbstractWordpressProducer (WordpressEndpoint endpoint)
specifier|public
name|AbstractWordpressProducer
parameter_list|(
name|WordpressEndpoint
name|endpoint
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|this
operator|.
name|configuration
operator|=
name|endpoint
operator|.
name|getConfiguration
argument_list|()
expr_stmt|;
if|if
condition|(
operator|!
name|WordpressServiceProvider
operator|.
name|getInstance
argument_list|()
operator|.
name|hasAuthentication
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Wordpress Producer hasn't authentication. This may lead to errors during route execution. Wordpress writing operations need authentication."
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|getConfiguration ()
specifier|public
name|WordpressComponentConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
annotation|@
name|Override
DECL|method|getEndpoint ()
specifier|public
name|WordpressEndpoint
name|getEndpoint
parameter_list|()
block|{
return|return
operator|(
name|WordpressEndpoint
operator|)
name|super
operator|.
name|getEndpoint
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|process (Exchange exchange)
specifier|public
specifier|final
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|this
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getId
argument_list|()
operator|==
literal|null
condition|)
block|{
name|exchange
operator|.
name|getMessage
argument_list|()
operator|.
name|setBody
argument_list|(
name|this
operator|.
name|processInsert
argument_list|(
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|this
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getOperationDetail
argument_list|()
operator|==
literal|null
condition|)
block|{
name|exchange
operator|.
name|getMessage
argument_list|()
operator|.
name|setBody
argument_list|(
name|this
operator|.
name|processUpdate
argument_list|(
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|exchange
operator|.
name|getMessage
argument_list|()
operator|.
name|setBody
argument_list|(
name|this
operator|.
name|processDelete
argument_list|(
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|processInsert (Exchange exchange)
specifier|protected
specifier|abstract
name|T
name|processInsert
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
function_decl|;
DECL|method|processUpdate (Exchange exchange)
specifier|protected
specifier|abstract
name|T
name|processUpdate
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
function_decl|;
DECL|method|processDelete (Exchange exchange)
specifier|protected
specifier|abstract
name|T
name|processDelete
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
function_decl|;
block|}
end_class

end_unit

