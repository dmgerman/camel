begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.loanbroker
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|loanbroker
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

begin_comment
comment|//START SNIPPET: translator
end_comment

begin_class
DECL|class|ReplyProcessor
specifier|public
class|class
name|ReplyProcessor
implements|implements
name|Processor
block|{
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
name|String
name|bankName
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|Constants
operator|.
name|PROPERTY_BANK
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|String
name|ssn
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|Constants
operator|.
name|PROPERTY_SSN
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|Double
name|rate
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|Constants
operator|.
name|PROPERTY_RATE
argument_list|,
name|Double
operator|.
name|class
argument_list|)
decl_stmt|;
name|String
name|answer
init|=
literal|"The best rate is [ssn:"
operator|+
name|ssn
operator|+
literal|" bank:"
operator|+
name|bankName
operator|+
literal|" rate:"
operator|+
name|rate
operator|+
literal|"]"
decl_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|answer
argument_list|)
expr_stmt|;
block|}
block|}
end_class

begin_comment
comment|//END SNIPPET: translator
end_comment

end_unit

