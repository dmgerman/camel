begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.dns
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|dns
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
name|Endpoint
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
name|support
operator|.
name|DefaultProducer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|xbill
operator|.
name|DNS
operator|.
name|DClass
import|;
end_import

begin_import
import|import
name|org
operator|.
name|xbill
operator|.
name|DNS
operator|.
name|Message
import|;
end_import

begin_import
import|import
name|org
operator|.
name|xbill
operator|.
name|DNS
operator|.
name|Name
import|;
end_import

begin_import
import|import
name|org
operator|.
name|xbill
operator|.
name|DNS
operator|.
name|Record
import|;
end_import

begin_import
import|import
name|org
operator|.
name|xbill
operator|.
name|DNS
operator|.
name|Section
import|;
end_import

begin_import
import|import
name|org
operator|.
name|xbill
operator|.
name|DNS
operator|.
name|SimpleResolver
import|;
end_import

begin_import
import|import
name|org
operator|.
name|xbill
operator|.
name|DNS
operator|.
name|Type
import|;
end_import

begin_comment
comment|/**  * an endpoint to make queries against wikipedia using  * the short TXT query.  *<p/>  * See here for a reference:  * http://www.commandlinefu.com/commands/view/2829/query-wikipedia-via-console-over-dns  *<p/>  * This endpoint accepts the following header:  * term: a simple term to use to query wikipedia.  */
end_comment

begin_class
DECL|class|DnsWikipediaProducer
specifier|public
class|class
name|DnsWikipediaProducer
extends|extends
name|DefaultProducer
block|{
DECL|method|DnsWikipediaProducer (Endpoint endpoint)
specifier|public
name|DnsWikipediaProducer
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
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
name|SimpleResolver
name|resolver
init|=
operator|new
name|SimpleResolver
argument_list|()
decl_stmt|;
name|int
name|type
init|=
name|Type
operator|.
name|TXT
decl_stmt|;
name|Name
name|name
init|=
name|Name
operator|.
name|fromString
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|DnsConstants
operator|.
name|TERM
argument_list|)
argument_list|)
operator|+
literal|".wp.dg.cx"
argument_list|,
name|Name
operator|.
name|root
argument_list|)
decl_stmt|;
name|Record
name|rec
init|=
name|Record
operator|.
name|newRecord
argument_list|(
name|name
argument_list|,
name|type
argument_list|,
name|DClass
operator|.
name|IN
argument_list|)
decl_stmt|;
name|Message
name|query
init|=
name|Message
operator|.
name|newQuery
argument_list|(
name|rec
argument_list|)
decl_stmt|;
name|Message
name|response
init|=
name|resolver
operator|.
name|send
argument_list|(
name|query
argument_list|)
decl_stmt|;
name|Record
index|[]
name|records
init|=
name|response
operator|.
name|getSectionArray
argument_list|(
name|Section
operator|.
name|ANSWER
argument_list|)
decl_stmt|;
if|if
condition|(
name|records
operator|.
name|length
operator|>
literal|0
condition|)
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|records
index|[
literal|0
index|]
operator|.
name|rdataToString
argument_list|()
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
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

