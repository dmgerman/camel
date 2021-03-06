begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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

begin_class
DECL|class|DnsConstants
specifier|public
class|class
name|DnsConstants
block|{
DECL|field|OPERATION_DIG
specifier|public
specifier|static
specifier|final
name|String
name|OPERATION_DIG
init|=
name|DnsType
operator|.
name|dig
operator|.
name|name
argument_list|()
decl_stmt|;
DECL|field|OPERATION_IP
specifier|public
specifier|static
specifier|final
name|String
name|OPERATION_IP
init|=
name|DnsType
operator|.
name|ip
operator|.
name|name
argument_list|()
decl_stmt|;
DECL|field|OPERATION_LOOKUP
specifier|public
specifier|static
specifier|final
name|String
name|OPERATION_LOOKUP
init|=
name|DnsType
operator|.
name|lookup
operator|.
name|name
argument_list|()
decl_stmt|;
DECL|field|OPERATION_WIKIPEDIA
specifier|public
specifier|static
specifier|final
name|String
name|OPERATION_WIKIPEDIA
init|=
name|DnsType
operator|.
name|wikipedia
operator|.
name|name
argument_list|()
decl_stmt|;
DECL|field|DNS_CLASS
specifier|public
specifier|static
specifier|final
name|String
name|DNS_CLASS
init|=
literal|"dns.class"
decl_stmt|;
DECL|field|DNS_NAME
specifier|public
specifier|static
specifier|final
name|String
name|DNS_NAME
init|=
literal|"dns.name"
decl_stmt|;
DECL|field|DNS_DOMAIN
specifier|public
specifier|static
specifier|final
name|String
name|DNS_DOMAIN
init|=
literal|"dns.domain"
decl_stmt|;
DECL|field|DNS_SERVER
specifier|public
specifier|static
specifier|final
name|String
name|DNS_SERVER
init|=
literal|"dns.server"
decl_stmt|;
DECL|field|DNS_TYPE
specifier|public
specifier|static
specifier|final
name|String
name|DNS_TYPE
init|=
literal|"dns.type"
decl_stmt|;
DECL|field|TERM
specifier|public
specifier|static
specifier|final
name|String
name|TERM
init|=
literal|"term"
decl_stmt|;
DECL|method|DnsConstants ()
specifier|protected
name|DnsConstants
parameter_list|()
block|{
comment|//Utility class
block|}
block|}
end_class

end_unit

