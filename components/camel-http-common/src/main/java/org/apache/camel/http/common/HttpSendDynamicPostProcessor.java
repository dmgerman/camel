begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.http.common
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|http
operator|.
name|common
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
comment|/**  * Post {@link Processor} used by {@link HttpSendDynamicAware}.  */
end_comment

begin_class
DECL|class|HttpSendDynamicPostProcessor
specifier|public
class|class
name|HttpSendDynamicPostProcessor
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
comment|// cleanup and remove the headers we used
name|exchange
operator|.
name|getMessage
argument_list|()
operator|.
name|removeHeader
argument_list|(
name|Exchange
operator|.
name|HTTP_PATH
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getMessage
argument_list|()
operator|.
name|removeHeader
argument_list|(
name|Exchange
operator|.
name|HTTP_QUERY
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

