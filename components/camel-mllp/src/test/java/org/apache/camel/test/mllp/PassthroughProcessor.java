begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.test.mllp
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|test
operator|.
name|mllp
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

begin_comment
comment|/**  * A Passthrough test processor  *  * Used to set breakpoints and diagnose what is going on in test routes  */
end_comment

begin_class
DECL|class|PassthroughProcessor
specifier|public
class|class
name|PassthroughProcessor
implements|implements
name|Processor
block|{
DECL|field|id
name|String
name|id
decl_stmt|;
DECL|field|log
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|this
operator|.
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
DECL|method|PassthroughProcessor (String id)
specifier|public
name|PassthroughProcessor
parameter_list|(
name|String
name|id
parameter_list|)
block|{
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
block|}
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
name|message
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
literal|null
operator|!=
name|message
condition|)
block|{
name|String
name|msh
init|=
name|message
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|message
operator|.
name|indexOf
argument_list|(
literal|'\r'
argument_list|)
argument_list|)
decl_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Processing MSH {}: \n{}\n"
argument_list|,
name|id
argument_list|,
name|msh
argument_list|)
expr_stmt|;
block|}
name|log
operator|.
name|debug
argument_list|(
literal|"Null inbound message body"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

