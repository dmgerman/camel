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
name|Component
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
name|Consumer
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
name|Producer
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
name|DefaultEndpoint
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
name|Lookup
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
comment|/**  * An endpoint to manage lookup operations, using the API from dnsjava.  */
end_comment

begin_class
DECL|class|DnsLookupEndpoint
specifier|public
class|class
name|DnsLookupEndpoint
extends|extends
name|DefaultEndpoint
block|{
DECL|method|DnsLookupEndpoint (Component component)
specifier|public
name|DnsLookupEndpoint
parameter_list|(
name|Component
name|component
parameter_list|)
block|{
name|super
argument_list|(
literal|"dns://lookup"
argument_list|,
name|component
argument_list|)
expr_stmt|;
block|}
DECL|method|createConsumer (Processor processor)
specifier|public
name|Consumer
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|DefaultProducer
argument_list|(
name|this
argument_list|)
block|{
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
name|dnsName
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|DnsConstants
operator|.
name|DNS_NAME
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|ObjectHelper
operator|.
name|notEmpty
argument_list|(
name|dnsName
argument_list|,
literal|"Header "
operator|+
name|DnsConstants
operator|.
name|DNS_NAME
argument_list|)
expr_stmt|;
name|Object
name|type
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|DnsConstants
operator|.
name|DNS_TYPE
argument_list|)
decl_stmt|;
name|Integer
name|dnsType
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|type
operator|!=
literal|null
condition|)
block|{
name|dnsType
operator|=
name|Type
operator|.
name|value
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
name|type
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|Object
name|dclass
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|DnsConstants
operator|.
name|DNS_CLASS
argument_list|)
decl_stmt|;
name|Integer
name|dnsClass
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|dclass
operator|!=
literal|null
condition|)
block|{
name|dnsClass
operator|=
name|DClass
operator|.
name|value
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
name|dclass
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|Lookup
name|lookup
decl_stmt|;
if|if
condition|(
name|dnsType
operator|!=
literal|null
operator|&&
name|dnsClass
operator|!=
literal|null
condition|)
block|{
name|lookup
operator|=
operator|new
name|Lookup
argument_list|(
name|dnsName
argument_list|,
name|dnsType
argument_list|,
name|dnsClass
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|dnsType
operator|!=
literal|null
condition|)
block|{
name|lookup
operator|=
operator|new
name|Lookup
argument_list|(
name|dnsName
argument_list|,
name|dnsType
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|lookup
operator|=
operator|new
name|Lookup
argument_list|(
name|dnsName
argument_list|)
expr_stmt|;
block|}
block|}
name|lookup
operator|.
name|run
argument_list|()
expr_stmt|;
if|if
condition|(
name|lookup
operator|.
name|getAnswers
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|lookup
operator|.
name|getAnswers
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
name|lookup
operator|.
name|getErrorString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|;
block|}
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

