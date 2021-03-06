begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.example.as2
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|example
operator|.
name|as2
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
name|Processor
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
name|as2
operator|.
name|api
operator|.
name|util
operator|.
name|AS2Utils
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
name|as2
operator|.
name|internal
operator|.
name|AS2Constants
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|HttpRequest
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|protocol
operator|.
name|HttpCoreContext
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
DECL|class|ExamineAS2ServerEndpointExchange
specifier|public
class|class
name|ExamineAS2ServerEndpointExchange
implements|implements
name|Processor
block|{
DECL|field|log
specifier|private
specifier|final
specifier|transient
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
annotation|@
name|Override
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
name|HttpCoreContext
name|context
init|=
name|exchange
operator|.
name|getProperty
argument_list|(
name|AS2Constants
operator|.
name|AS2_INTERCHANGE
argument_list|,
name|HttpCoreContext
operator|.
name|class
argument_list|)
decl_stmt|;
name|String
name|ediMessage
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|context
operator|!=
literal|null
condition|)
block|{
name|HttpRequest
name|request
init|=
name|context
operator|.
name|getRequest
argument_list|()
decl_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"\n*******************************************************************************"
operator|+
literal|"\n*******************************************************************************"
operator|+
literal|"\n\n******************* AS2 Server Endpoint Received Request **********************"
operator|+
literal|"\n\n"
operator|+
name|AS2Utils
operator|.
name|printMessage
argument_list|(
name|request
argument_list|)
operator|+
literal|"\n"
operator|+
literal|"\n************************** Containing EDI message *****************************"
operator|+
literal|"\n\n"
operator|+
name|ediMessage
operator|+
literal|"\n"
operator|+
literal|"\n*******************************************************************************"
operator|+
literal|"\n*******************************************************************************"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|log
operator|.
name|info
argument_list|(
literal|"AS2 Interchange missing from context"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

